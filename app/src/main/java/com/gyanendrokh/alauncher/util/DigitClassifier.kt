package com.gyanendrokh.alauncher.util

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.Scanner


class DigitClassifier(context: Context) {
    private val assetManager: AssetManager = context.assets
    private val classes: MutableList<String> = mutableListOf()

    private lateinit var interpreter: Interpreter

    var imageWidth: Int = 0; private set
    var imageHeight: Int = 0; private set

    init {
        initModel()
        loadClasses()
    }

    private fun initModel() {
        val model = loadModelFile()
        val interpreter = Interpreter(model, Interpreter.Options().apply {
            useNNAPI = true
        })

        val inputShape = interpreter.getInputTensor(0).shape()
        imageWidth = inputShape[1]
        imageHeight = inputShape[2]

        this.interpreter = interpreter
        Log.d(TAG, "Interpreter Loaded! $imageWidth $imageHeight")
    }

    private fun loadClasses() {
        val scanner = Scanner(assetManager.open(CLASSES_FILE))

        while (scanner.hasNextLine()) {
            classes.add(scanner.nextLine())
        }
    }

    private fun loadModelFile(): ByteBuffer {
        val fd = assetManager.openFd(MODEL_FILE)
        val inputStream = FileInputStream(fd.fileDescriptor)

        return inputStream.channel.map(
            FileChannel.MapMode.READ_ONLY,
            fd.startOffset,
            fd.declaredLength
        )
    }

    fun classify(bitmap: Bitmap): String? {
        val startTime: Long = System.nanoTime()

        val byteBuffer = convertBitmapToByteBuffer(bitmap)
        val result = Array(1) { FloatArray(classes.size) }
        interpreter.run(byteBuffer, result)

        val elapsedTime = (System.nanoTime() - startTime) / 1000000 // in ms

        Log.d(TAG, "Inference Time = ${elapsedTime}ms")

        return getOutputClass(result[0])
    }

    fun close() {
        this.interpreter.close()
        Log.d(TAG, "Interpreter Closed!")
    }

    private fun getOutputClass(result: FloatArray): String? {
        val argmax = result.indices.maxByOrNull { result[it] }

        return argmax?.let { classes[it] }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer
            .allocateDirect(FLOAT_TYPE_SIZE * imageHeight * imageWidth * PIXEL_SIZE)
            .apply {
                order(ByteOrder.nativeOrder())
            }

        val pixels = IntArray(imageWidth * imageHeight)

        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            // Convert RGB to grayscale and normalize pixel value to [0..1]
            val normalizedPixelValue = (0.299f * r + 0.587f * g + 0.114f * b) / 255.0f
            byteBuffer.putFloat(normalizedPixelValue)
        }

        return byteBuffer
    }

    companion object {
        private const val TAG = "DigitClassifier"

        private const val MODEL_FILE = "model.tflite"
        private const val CLASSES_FILE = "classes.txt"

        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1
    }
}
