package com.example.unsplash.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.example.unsplash.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DownloadWorker(private val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification("image will download when internet appear", appContext)
        val url = inputData.getString(URL)
        val id = inputData.getString(ID)

        return try {

            makeStatusNotification("downloading", appContext)
            if (url != null) {
                if (id != null) {
                    download1(ctx,url,id)
                }
            }
            Result.success()
        } catch (throwable: Throwable) {

            Result.failure()
        }
    }
    private fun download1(ctx: Context, url:String, id:String){
        CoroutineScope(Dispatchers.IO).launch {
            saveImage(
                Glide.with(ctx)
                    .asBitmap()
                    .load(url) // sample image
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal) // need placeholder to avoid issue like glide annotations
                    .error(android.R.drawable.stat_notify_error) // need error to avoid issue like glide annotations
                    .submit()
                    .get(), id
            )

        }
    }
    private fun saveImage(image: Bitmap, name:String): String? {
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
            galleryAddPic(savedImagePath)

            //Toast.makeText(context, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
        }
        return savedImagePath
    }
    private fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val file = File(path)
            MediaScannerConnection.scanFile(ctx, arrayOf(file.toString()), null, null)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.let {intent->
                intent.setDataAndType(path.toUri(), "image/*")
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                makeIntentNotification("image saved",pendingIntent, ctx)
            }
        }


    }

    fun makeStatusNotification(message: String, context: Context) {

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
            val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
    fun makeIntentNotification(message: String,intent:PendingIntent, context: Context) {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
            val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(message)
            .setContentIntent(intent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
    companion object{
        const val URL:String = "imageurl"
        const val ID:String = "imageid"
    }
}

@JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
@JvmField val NOTIFICATION_TITLE: CharSequence = "DownloadWorkRequest"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1
