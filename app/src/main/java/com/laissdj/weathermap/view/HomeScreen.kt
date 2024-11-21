package com.laissdj.weathermap.view

import android.Manifest
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.laissdj.weathermap.R


class HomeScreen : Screen {
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun Content() {
        var requestPermission by remember { mutableStateOf(false) }
        val permissionState = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)
        var value by remember { mutableStateOf("") }
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        if (requestPermission || permissionState.status.isGranted) {
            RequestLocationPermission(onPermissionGranted = {
                returnLocation(
                    context,
                    onGetLastLocationSuccess = { lat, lon ->
                        println("$lat,$lon")
                        navigator.push(
                            WeatherScreen(
                                WeatherQuery.Location(lat, lon)
                            )
                        )
                    },
                    onGetLastLocationFailed = { e -> e.printStackTrace() }
                )
            }, permissionState)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.blue))
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(modifier = Modifier) {

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    value = value,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(text = "Digite a cidade")
                    },
                    onValueChange = { value = it },
                    trailingIcon = {
//                        if (value.isNotEmpty()) {
//                    Button(
//                        modifier = Modifier
//                            .padding(top = 16.dp)
//                            .align(Alignment.CenterHorizontally),
//                        onClick = {
//                            navigator.push(
//                                WeatherScreen(
//                                    WeatherQuery.Text(
//                                        value
//                                    )
//                                )
//                            )
//                        },
//                        colors = ButtonDefaults.buttonColors(
//                            contentColor = Color.DarkGray,
//                            containerColor = Color.White.copy(alpha = 0.6f)
//                        )
//                    ) {
//                        Text(text = "Buscar")
//
//                    }
                        IconButton(onClick = {
                            if (value.isNotEmpty()) {
                                navigator.push(
                                    WeatherScreen(
                                        WeatherQuery.Text(
                                            value
                                        )
                                    )
                                )
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Pesquisar",
                            )
                        }
                    })

                Spacer(modifier = Modifier.height(16.dp))
                if (!permissionState.status.isGranted) {
                    Button(
                        onClick = { requestPermission = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.DarkGray,
                            containerColor = Color.White.copy(alpha = 0.6f)
                        )
                    ) {
                        Text(text = "Localização atual")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalPermissionsApi
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    //onPermissionDenied: () -> Unit,
    //onPermissionsRevoked: () -> Unit,
    permissionState: PermissionState
) {
    LaunchedEffect(key1 = permissionState) {
        println("Passou aqui ${permissionState.status}")
        when {
            permissionState.status.isGranted -> onPermissionGranted()
            else -> permissionState.launchPermissionRequest()
        }

    }

}

fun returnLocation(
    context: Context,
    onGetLastLocationSuccess: (Double, Double) -> Unit,
    onGetLastLocationFailed: (Exception) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { onGetLastLocationSuccess(it.latitude, it.longitude) }
                ?: onGetLastLocationFailed(
                    java.lang.Exception("Localização não encontrada")
                )
        }
            .addOnFailureListener { exception ->
                onGetLastLocationFailed(exception)
            }

    } catch (e: SecurityException) {
        onGetLastLocationFailed(e)
    }

}
