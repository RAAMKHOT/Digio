package com.ramagouda.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.ramagouda.network.RestClient
import com.ramagouda.network.SOService
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

object CommonUtils {
    val BASE_URL =
        "https://ext.digio.in:444"//https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json

    fun getSOService(): SOService {
        return RestClient.getClient(BASE_URL)!!.create(SOService::class.java)
    }

    fun showToast(context: Context, message: String) {
        try {
            Toast.makeText(
                context,
                message, Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
        }

    }

    fun encodeFileToBase64Binary(fileName: File): String? {
        var encodedBase64: String? = null
        try {
            val fileInputStreamReader = FileInputStream(fileName)
            val bytes = ByteArray(fileName.length().toInt())
            fileInputStreamReader.read(bytes)
            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return encodedBase64
    }

    fun getPdfList(ctx: Context): List<String> {
        val pdf: ArrayList<String> = ArrayList()
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
                            pdf.add(path)
                            Log.d("TAG", "getPdf: $path")
                        } while (cursor.moveToNext())
                    }
                }
            }
        return pdf
    }

    private val PERMISSIONS_LIST = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )

    fun verifyPermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity, PERMISSIONS_LIST, 1
            )
        }

        if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(
                        String.format(
                            "package:%s",
                            activity.applicationContext.packageName
                        )
                    )
                    activity.startActivityForResult(
                        intent,
                        3
                    )
                } catch (e: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    activity.startActivityForResult(
                        intent,
                        3
                    )
                }
            }
        }

    }
}