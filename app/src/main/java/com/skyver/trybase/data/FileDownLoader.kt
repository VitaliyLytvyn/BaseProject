package com.skyver.trybase.data

import androidx.lifecycle.LiveData


internal interface FileDownLoader {
    companion object {
        const val FILE_PATH = "newFile/repos"
    }

     suspend fun downloadFrom(path: String, to:String): LiveData<String>

}
