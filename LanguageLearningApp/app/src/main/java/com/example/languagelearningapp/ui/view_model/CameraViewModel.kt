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
import com.google.common.util.concurrent.ListenableFuture


class CameraViewModel  :ViewModel() {
    var capturedImage: MutableLiveData<ImageProxy> = MutableLiveData()
    private val imageCapture = ImageCapture.Builder().build()
    private lateinit var camera : Camera
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    fun startCameraOnSurface(previewView: PreviewView, lifecycleOwner: LifecycleOwner, context: Context){
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider, previewView, lifecycleOwner)
        }, ContextCompat.getMainExecutor(context))
    }

    fun capturePicture(context: Context){
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object: OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                analyze(image)
                super.onCaptureSuccess(image)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Image capture failed. The problem was: ${exception.message}")
                super.onError(exception)
            }
        }
        )
    }

    private fun analyze(imageProxy: ImageProxy) {
        capturedImage.value = imageProxy
    }

    fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun bindPreview(cameraProvider : ProcessCameraProvider, previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            try {
                cameraProvider.unbindAll()

                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, imageCapture, preview
                )
            } catch (exc: Exception) {
                Log.e("Camera", "Use case binding failed", exc)
            }
    }

}