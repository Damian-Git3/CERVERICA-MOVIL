package com.example.cerverica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.example.cerverica.ui.Formulario

import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.compose.setContent


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyScreen()
        }

    }

    @Preview
    @Composable
    fun MyScreen() {
        Formulario (
            onAccept = { /* acción al presionar el botón de aceptar */ },
            onCancel = { /* acción al presionar el botón de cancelar */ }
        )
    }
}