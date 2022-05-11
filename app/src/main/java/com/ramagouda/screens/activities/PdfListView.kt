package com.ramagouda.screens.activities

import java.io.File

interface PdfListView {
    fun init()
    fun onPdfUploadSuccess(pdfFiles: ArrayList<String>)
    fun onPdfUploadError(error: String)
    fun sendFileToServer(file: File)
}