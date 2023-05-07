package com.example.languagelearningapp.ui.view_model

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CameraViewModel  :ViewModel() {
    var capturedImage: MutableLiveData<ImageProxy> = MutableLiveData()
    var captureSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    private lateinit var imageCapture: ImageCapture
    private lateinit var camera: Camera

    fun startCameraOnSurface(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        context: Context
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindUseCases(cameraProvider, previewView, lifecycleOwner)
            Log.d("CameraModel", "Bind finished, camera: ${camera}")
        }, ContextCompat.getMainExecutor(context))
    }

    fun capturePicture(context: Context){
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object: OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.d("CameraModel", "Image capture success")
                    analyze(image)
                    super.onCaptureSuccess(image)
                }

                override fun onError(exception: ImageCaptureException) {
                    captureSuccess.value = false
                    Log.e(
                        "CameraModel",
                        "Image capture failed. The problem was: ${exception.message}"
                    )
                    super.onError(exception)
                }
            }
        )
    }

    private fun analyze(imageProxy: ImageProxy) {
        capturedImage.value = imageProxy
        captureSuccess.value = true
    }

    fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun bindUseCases(
        cameraProvider: ProcessCameraProvider,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        imageCapture = ImageCapture.Builder().build()
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
        val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, imageCapture, preview
            )
            Log.d("CameraModel", "Successful bind")
        } catch (exc: Exception) {
            Log.e("CameraModel", "Use case binding failed", exc)
        }
    }
}