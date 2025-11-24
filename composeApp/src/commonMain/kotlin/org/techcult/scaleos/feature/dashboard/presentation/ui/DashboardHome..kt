@file:OptIn(ExperimentalMaterial3Api::class)

package org.techcult.scaleos.feature.dashboard.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DashboardHomeScreen(modifier: Modifier = Modifier
) {
   DashboardHomeContent()


}

@Composable
fun DashboardHomeContent(modifier: Modifier = Modifier) {
   Scaffold(topBar = {
      TopAppBar(title = {
         Text(text = "Dashboard")
      })

   }){
      Column {



      }

   }
}