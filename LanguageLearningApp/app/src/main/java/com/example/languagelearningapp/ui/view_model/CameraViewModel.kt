package com.example.languagelearningapp.ui.view_model

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
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
    private lateinit var previewViewMeteringFactory: MeteringPointFactory

    fun startCameraOnSurface(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        context: Context
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindUseCases(cameraProvider, previewView, lifecycleOwner)
            bindPreviewViewEventListeners(previewView, context)
            Log.d(TAG, "Bind finished, camera: $camera")
        }, ContextCompat.getMainExecutor(context))
    }

    fun resetCaptureState() {
        capturedImage.value = null
        captureSuccess.value = false
    }

    fun capturePicture(context: Context) {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.d(TAG, "Image capture success")
                    analyze(image)
                    super.onCaptureSuccess(image)
                }

                override fun onError(exception: ImageCaptureException) {
                    captureSuccess.value = false
                    Log.e(
                        TAG,
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
            .apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }
        val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, imageCapture, preview
            )
            Log.d(TAG, "Successful bind")
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun bindPreviewViewEventListeners(previewView: PreviewView, context: Context) {
        previewViewMeteringFactory = previewView.meteringPointFactory
        //previewView.setOnTouchListener{v, e -> onFocus(v, e)}
        val onTouchListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                return onZoom(detector)
            }
        }
        val scaleGestureDetector = ScaleGestureDetector(context, onTouchListener)
        previewView.setOnTouchListener { view, event ->
            view.performClick()
            if (scaleGestureDetector.onTouchEvent(event))
                return@setOnTouchListener true
            else
                return@setOnTouchListener onFocus(view, event)
        }
    }

    private fun onFocus(view: View, motionEvent: MotionEvent): Boolean {
        return when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                val point = previewViewMeteringFactory.createPoint(motionEvent.x, motionEvent.y)
                val action = FocusMeteringAction.Builder(point).build()

                // Trigger the focus and metering. The method returns a ListenableFuture since the operation
                // is asynchronous. It can be used to get notified when the focus is successful or if it fails.
                camera.cameraControl.startFocusAndMetering(action)
                Log.d(TAG, "Touch event processing.")
                view.performClick()
                true
            }
            else -> false
        }
    }

    private fun onZoom(detector: ScaleGestureDetector): Boolean {
        Log.d(TAG, "Zoom event processing")
        val currentZoomRatio = camera.cameraInfo.zoomState.value?.zoomRatio ?: 0F
        val scaleFactor = detector.scaleFactor
        Log.d(TAG, "Scalefactor: $scaleFactor")

        // Update the camera's zoom ratio. This is an asynchronous operation that returns
        // a ListenableFuture, allowing to listen when the operation completes.
        camera.cameraControl.setZoomRatio(currentZoomRatio * scaleFactor)
        return true
    }

    private companion object {
        val TAG = "CameraModel"
    }
}