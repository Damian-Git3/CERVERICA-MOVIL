package com.example.cerverica.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun NavBar() {
    BottomAppBar(
        actions = {
            Row(
               horizontalArrangement = Arrangement.Center

            ) {
                IconButton(onClick = { /* do something */ }) {
                    Icon(Icons.Filled.AllInbox, contentDescription = "Localized description")
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Localized description",
                    )
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "Localized description",
                    )
                }
            }

        }
    )
}