package com.gustavo.cursomeetups.model

import android.os.Build

data class Dispositivo(
    val marca: String = Build.BOARD,
    val modelo: String = Build.MODEL,
    val token: String,
)
