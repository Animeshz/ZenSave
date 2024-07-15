package io.github.animeshz.zensave

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.animeshz.zensave.ui.theme.ZenSaveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    ZenSaveTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("ZenSave") },
                    navigationIcon = {
                        Icon(
                            rememberVectorPainter(image = Icons.AutoMirrored.Filled.List),
                            "Menu",
                            modifier = Modifier.padding(8.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                        .copy(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                )
            },
            bottomBar = {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledTonalButton(
                            modifier = Modifier.fillMaxHeight(),
                            shape = RoundedCornerShape(10),
                            onClick = { /*TODO*/ },
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(rememberVectorPainter(image = Icons.Filled.Home), "")
                                Text("Home")
                            }
                        }
                    }
                }
            },
            floatingActionButton = { MainFAB() }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyCard("Income")
                MyCard("Expenses")
                MyCard("Savings")
            }
        }
    }
}

@Composable
fun MainFAB() {
    val context = LocalContext.current
    var expanded by remember {
        mutableStateOf(false)
    }

    val transition = updateTransition(targetState = expanded, label = "fab-transition")
    val rotation by transition.animateFloat(label = "fab-rotation") {
        if (it) 135f else 0f
    }

    val items = listOf(
        MiniFabItem(Icons.AutoMirrored.Filled.ArrowBack, "Import") {
            context.startActivity(
                Intent(
                    context,
                    TxnParserActivity::class.java
                )
            )
        },
        MiniFabItem(Icons.Filled.Info, "Help"),
    )
    Column(horizontalAlignment = Alignment.End) {

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically(),
            modifier = Modifier.padding(bottom = 12.dp),
        ) {

            Column {
                repeat(items.size) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(items[it].title)
                        Spacer(modifier = Modifier.width(12.dp))
                        FloatingActionButton(onClick = items[it].onClick) {
                            Icon(
                                painter = rememberVectorPainter(image = items[it].icon),
                                contentDescription = items[it].title
                            )
                        }
                    }
                }
            }

        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.rotate(rotation)
        ) {
            Icon(
                painter = rememberVectorPainter(Icons.Filled.Add),
                contentDescription = "Add"
            )
        }
    }
}

data class MiniFabItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit = {},
)

@Composable
fun MyCard(title: String) {
    ElevatedCard(
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}