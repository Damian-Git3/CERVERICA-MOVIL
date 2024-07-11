package com.example.cerverica.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview
@Composable
fun Formulario(
    modifier: Modifier = Modifier,
    onAccept: () -> Unit = {},
    onCancel: () -> Unit = {},
    text: MutableState<String> = remember { mutableStateOf("Formulario") }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White)
    ) {
        Text(
            text = "${text.value}",
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

        )
        TextField(
            value = "",
            onValueChange = { },
            label = { Text("Campo 1") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        TextField(
            value = "",
            onValueChange = { },
            label = { Text("Campo 2") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        TextField(
            value = "",
            onValueChange = { },
            label = { Text("Campo 3") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
            ,
            shape = RoundedCornerShape(8.dp)
        )
        TextField(
            value = "",
            onValueChange = { },
            label = { Text("Campo 4") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        TextField(
            value = "",
            onValueChange = { },
            label = { Text("Campo 5") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ElevatedButton(
                onClick = onCancel,
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
            ) {

                Icon(Icons.Default.Cancel, contentDescription = "Cancelar")

            }
            ElevatedButton(
                onClick = onAccept,
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
                ) {

                Icon(Icons.Default.Done, contentDescription = "Aceptar")
            }
        }
    }
}
