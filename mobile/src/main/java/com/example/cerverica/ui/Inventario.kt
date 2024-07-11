package com.example.cerverica.ui

import android.widget.LinearLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.overscroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cerverica.R

@Preview
@Composable
fun Inventario(text: MutableState<String> = remember { mutableStateOf("INVENTARIOS") }) {
    Text(
        text = "${text.value}",
        textAlign = TextAlign.Center,
        fontWeight = FontWeight(weight = 800),
        style = TextStyle(fontSize = 24.sp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),

        )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(500.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(10) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
            ) {
                Column(
                    modifier = Modifier.padding(6.dp)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "Localized description",)
                }
                Column (
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text(text = "PRODUCTO", fontWeight = FontWeight(weight = 800))
                    Text(text = "TOTAL: 80", modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }


    }

}