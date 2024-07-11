package com.example.cerverica.ui

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.AllInbox
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import com.example.cerverica.R

@Preview
@Composable
fun NavBar() {
    BottomAppBar(
        actions = {
            Row(
               horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* do something */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.package_2),
                        contentDescription = "Localized description"
                    )
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Rounded.Home,
                        contentDescription = "Localized description",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Rounded.ShoppingCart,
                        contentDescription = "Localized description",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    )
}