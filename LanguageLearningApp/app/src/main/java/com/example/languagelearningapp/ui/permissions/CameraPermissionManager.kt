package com.example.languagelearningapp.ui.permissions

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.languagelearningapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
@Preview
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionManager (){
    var rationaleDialogOpen by remember{ mutableStateOf(true) }
    var requestDialogOpen by remember{ mutableStateOf(true) }
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    if(cameraPermissionState.hasPermission){
        return
    }
    if(cameraPermissionState.shouldShowRationale){
        if(rationaleDialogOpen) {
            Dialog(onDismissRequest = {
                cameraPermissionState.launchPermissionRequest()
                rationaleDialogOpen = false
            }) {
                Text(stringResource(R.string.camera_rationale), color = Color.White)
            }
        }
    }else{
        if(requestDialogOpen) {
            Dialog(onDismissRequest = {
                cameraPermissionState.launchPermissionRequest()
                requestDialogOpen = false
            }) {
                Text(stringResource(R.string.camera_permission_request),  color = Color.White)
            }
        }
    }
}