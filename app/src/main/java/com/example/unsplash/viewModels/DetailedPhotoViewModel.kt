package com.example.unsplash.viewModels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.bumptech.glide.Glide
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.repository.AuthRepository
import com.example.unsplash.data.modelsDetail.DetailedPhotoInfo
import com.example.unsplash.workers.DownloadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class DetailedPhotoViewModel@Inject constructor(private val repository: AuthRepository): ViewModel() {
    private var _detailedInfo: MutableStateFlow<DetailedPhotoInfo?> = MutableStateFlow(null)
    val detailedInfo = _detailedInfo.asStateFlow()
    var intent :Intent? = null
    var status = MutableStateFlow<Boolean?>(null)

    suspend fun getDetailedInfo(token: String, id: String){
        _detailedInfo.value = repository.getDetailedPhoto(token, id)
    }

    fun download(context: Context){
        val handler = CoroutineExceptionHandler { _, _ ->
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val downloadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setInputData(
                    Data.Builder()
                        .putString(DownloadWorker.URL, detailedInfo.value?.urls?.raw)
                        .putString(DownloadWorker.ID,detailedInfo.value?.id)
                        .build())
                .setConstraints(constraints)
                .build()
            WorkManager
                .getInstance(context)
                .enqueue(downloadWorkRequest)
        }
        download1(handler, context)

    }
    private fun download1(handler : CoroutineExceptionHandler, context: Context){
        CoroutineScope(Dispatchers.IO).launch(handler) {
            detailedInfo.value?.id?.let { id->
                saveImage(
                    Glide.with(context)
                        .asBitmap()
                        .load(detailedInfo.value?.urls?.raw) // sample image
                        .placeholder(android.R.drawable.progress_indeterminate_horizontal) // need placeholder to avoid issue like glide annotations
                        .error(android.R.drawable.stat_notify_error) // need error to avoid issue like glide annotations
                        .submit()
                        .get(), id, context
                )
            }
        }
    }
    private fun saveImage(image: Bitmap, name:String, context: Context): String? {
        var savedImagePath: String? = null
        val imageFileName = "$name.jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/YOUR_FOLDER_NAME"
        )
        Log.d("MyLog", "storageDir: $storageDir")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.getAbsolutePath()
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            galleryAddPic(savedImagePath, context )
        }
        return savedImagePath
    }
    private fun galleryAddPic(imagePath: String?, context: Context) {
        imagePath?.let { path ->
            val file = File(path)
            MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)

            intent = Intent(Intent.ACTION_VIEW)
            intent?.let {intent->
                intent.setDataAndType(path.toUri(), "image/*")
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            status.value = true

        }
    }
    fun openGallery(context: Context){
        intent?.let {intent->
            ContextCompat.startActivity(context, intent, null)
        }
    }

    suspend fun changeLike(isLiked:Boolean, id:String, context:Context){
        if(isLiked){
            repository.unLike(id, token = SharedPrefToken(context).getText()!!)
        }
        else{
            repository.like(id, token = SharedPrefToken(context).getText()!!)
        }

    }


}