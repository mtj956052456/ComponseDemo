package com.mtj.compose.demo.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Name() {
    var name: String by mutableStateOf("")
    var compled: Boolean  by mutableStateOf(false)
}