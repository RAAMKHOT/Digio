package com.ramagouda.screens.activities

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.example.UploadPdf
import com.ramagouda.helper.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.net.ssl.SSLHandshakeException

class PdfListPresenter(private val view: PdfListView) {
    private val repository = CommonUtils.getSOService()
    fun postPdfFile(uploadPdf: UploadPdf) {
        CompositeDisposable().add(
            repository.uploadPdf(uploadPdf).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(
                    { result ->
                        if (result != null) {
                            when {
                                result.code() == 200 -> {
                                    view.onPdfUploadError("Success")
                                }
                                result.code() == 400 -> {
                                    view.onPdfUploadError("Invalid request. Some field missing/invalid")
                                }
                                result.code() == 401 -> {
                                    view.onPdfUploadError("Authentication Error. Check client_id and/or client_secret")
                                }
                                result.code() == 403 -> {
                                    view.onPdfUploadError("Authorization Error. Operation/Action is not allowed")
                                }
                            }
                        }
                    },
                    { error ->
                        if (error is SSLHandshakeException) {
                            view.onPdfUploadError("We are getting SSL Handshake Exception, We need to add SSL cert in Server side \n Please use the lower version device.")
                        } else {
                            view.onPdfUploadError("Something went wrong")
                        }
                    })
        )
    }

    fun getPdfFiles(context: Context) {
        CompositeDisposable().add(
            getPdfList(context)
                .subscribeOn(Schedulers.computation())
                .subscribe(
                    { result ->
                        Log.e("FILE-LIST:-->", "" + result.size)
                        view.onPdfUploadSuccess(result)
                    },

                    { error ->
                        Log.e("FILE-LIST:-->", "error")
                        //view.onPdfUploadError("error")
                    })
        )
    }

    fun getPdfList(ctx: Context): Observable<ArrayList<String>> {
        return Observable.create { subscriber ->
            val fileList: ArrayList<String> = ArrayList()
            val collection: Uri
            val projection = arrayOf(
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE
            )
            val sortOrder = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            val selection = MediaStore.Files.FileColumns.MIME_TYPE + " = ?"
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")
            val selectionArgs = arrayOf(mimeType)
            collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Files.getContentUri("external")
            }
            ctx.getApplicationContext().getContentResolver()
                .query(collection, projection, selection, selectionArgs, sortOrder).use { cursor ->
                    assert(cursor != null)
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            val columnData: Int =
                                cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                            val columnName: Int =
                                cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                            do {
                                val path: String = cursor.getString(columnData)
                                fileList.add(path)

                                Log.d("TAG", "getPdf: $path")
                            } while (cursor.moveToNext() && fileList.size < 8)
                        }
                    }
                }
            subscriber.onNext(fileList)
            subscriber.onComplete()
        }
    }

}
