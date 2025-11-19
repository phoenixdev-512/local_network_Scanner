package com.example.local_network_scanner.util

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Image storage service for avatar management
 * Handles saving, loading, and deleting profile avatar images
 */
@Singleton
class ImageStorageService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val imageDir = File(context.filesDir, "profile_images").apply {
        if (!exists()) mkdirs()
    }
    
    /**
     * Save an image from a URI to internal storage
     * @param uri The URI of the image to save
     * @param filename The desired filename for the saved image
     * @return The absolute path of the saved image
     */
    suspend fun saveImage(uri: Uri, filename: String): String = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IOException("Cannot open input stream for URI: $uri")
            
            val outputFile = File(imageDir, filename)
            inputStream.use { input ->
                outputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            
            Log.d("ImageStorageService", "Image saved successfully: ${outputFile.absolutePath}")
            outputFile.absolutePath
        } catch (e: Exception) {
            Log.e("ImageStorageService", "Error saving image from URI: $uri", e)
            throw e
        }
    }
    
    /**
     * Delete an image from internal storage
     * @param path The absolute path of the image to delete
     * @return true if deletion was successful, false otherwise
     */
    suspend fun deleteImage(path: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = File(path)
            val deleted = file.delete()
            if (deleted) {
                Log.d("ImageStorageService", "Image deleted successfully: $path")
            } else {
                Log.w("ImageStorageService", "Failed to delete image: $path")
            }
            deleted
        } catch (e: Exception) {
            Log.e("ImageStorageService", "Error deleting image: $path", e)
            false
        }
    }
    
    /**
     * Check if an image file exists
     * @param path The absolute path of the image
     * @return true if the file exists, false otherwise
     */
    fun imageExists(path: String): Boolean {
        return File(path).exists()
    }
    
    /**
     * Get the URI for a saved image
     * @param path The absolute path of the image
     * @return Uri for the image file
     */
    fun getImageUri(path: String): Uri {
        return Uri.fromFile(File(path))
    }
}
