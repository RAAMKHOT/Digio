package com.ramagouda.screens.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.example.Signders
import com.example.example.UploadPdf
import com.ramagouda.R
import com.ramagouda.helper.CommonUtils
import kotlinx.android.synthetic.main.activity_pdf_list.*
import java.io.File


class PdfListActivity : AppCompatActivity(), PdfListView {
    private lateinit var pdfListAdapter: PdfListAdapter
    private var landingPresenter: PdfListPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_list)
        init()
        CommonUtils.verifyPermissions(this)
    }

    override fun init() {
        landingPresenter = PdfListPresenter(this)
        pdfListAdapter = PdfListAdapter(ArrayList<String>(), this, this)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext)
        pdfFilesRecyclerView.layoutManager = mLayoutManager
        pdfFilesRecyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        pdfFilesRecyclerView.adapter = pdfListAdapter

        setTitle("PDF Files List")
        landingPresenter!!.getPdfFiles(this)
    }

    private fun initListView(factList: ArrayList<String>) {
        pdfListAdapter.setList(factList)
    }

    override fun onPdfUploadSuccess(pdfFiles: ArrayList<String>) {
        //setTitle("Size of List " + pdfFiles.size)
        if (pdfFiles == null || pdfFiles.size == 0) {
            textViewNoFiles.visibility = View.VISIBLE
            pdfFilesRecyclerView.visibility = View.GONE
        } else {
            textViewNoFiles.visibility = View.GONE
            pdfFilesRecyclerView.visibility = View.VISIBLE
            initListView(pdfFiles)
        }
    }

    override fun onPdfUploadError(error: String) {
        CommonUtils.showToast(this, error)
    }

    override fun sendFileToServer(file: File) {
        val uploadPdf = UploadPdf()

        val signders = Signders()
        val signdersArray = ArrayList<Signders>()
        signders.identifier = "7338531835"
        signders.name = "Ramagouda"
        signders.reason = "Loan Agreement"
        signdersArray.add(signders)

        uploadPdf.signders = signdersArray

        uploadPdf.expireInDays = 10
        uploadPdf.displayOnPage = "all"
        uploadPdf.fileName = file.name

        val base64String = CommonUtils.encodeFileToBase64Binary(file)
        uploadPdf.fileData = base64String

        //Log.e("BASE64STRING : ", base64String)

        landingPresenter!!.postPdfFile(uploadPdf)
    }
}
