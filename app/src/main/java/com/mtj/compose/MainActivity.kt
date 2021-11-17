package com.mtj.compose

import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scaleMatrix
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mtj.compose.demo.data.DataCenter
import com.mtj.compose.demo.data.Name
import com.mtj.compose.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                MainScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun MainScreen() {
        var inputting by remember { mutableStateOf(false) }
        var inputContent by remember { mutableStateOf("") }
        val animatedFabScale by animateFloatAsState(if (inputting) 0f else 1f)
        val animatedFabOutScale  by animateFloatAsState(if (inputting) 1f else 0f)

        Scaffold(topBar = {
            TopAppBar() {
                Icon(Icons.Filled.ArrowBack, "返回")
            }
        }, floatingActionButton = {
            if(!inputting){
                FloatingActionButton(onClick = {
                    inputContent = ""
                    inputting = true
                }, Modifier.scale(animatedFabScale)) {
                    Icon(Icons.Filled.Add, "添加")
                }
            }
        }) {
            Box(
                Modifier
                    .fillMaxSize()
                    .scale(animatedFabOutScale)) {
                Names(DataCenter.nameList)
                if (inputting) {
                    Row(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                    ) {
                        TextField(value = inputContent, onValueChange = { inputContent = it }, Modifier.weight(1f))
                        IconButton(onClick = {
                            inputting = false
                            DataCenter.nameList.add(Name().apply { name = inputContent })
                        }) {
                            Icon(Icons.Filled.Send, "确认")
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Names(ns: List<Name>) {
        Surface(
            Modifier.padding(16.dp),
            elevation = 0.dp,
            shape = RoundedCornerShape(5.dp)
        ) {
            //LazyRow(content = ) //相当于横着的RV
            LazyColumn(Modifier.fillMaxWidth()) {//相当于RV
                items(ns) { name ->
                    Row(
                        Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(checked = name.compled, onCheckedChange = { name.compled = it })
                        Text(name.name)
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun NamesPreview() {
        Names(DataCenter.nameList)
    }
}
