package com.ozkanfurkan.mvvmuserapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ozkanfurkan.mvvmuserapp.ui.screen.UserListScreen
import com.ozkanfurkan.mvvmuserapp.ui.theme.MVVMUserAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVMUserAppTheme {
                UserListScreen()
            }
        }
    }
}
