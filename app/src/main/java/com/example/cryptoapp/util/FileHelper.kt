package com.example.cryptoapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.FileNotFoundException

class FileHelper(private val context: Context) {

    fun saveImageToInternalStorage(bitmap: Bitmap, fileName: String): String {
        val fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
        return context.getFileStreamPath(fileName).absolutePath
    }

    fun loadImageFromInternalStorage(fileName: String): Bitmap? {
        return try {
            val inputStream = context.openFileInput(fileName)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            null
        }
    }
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}