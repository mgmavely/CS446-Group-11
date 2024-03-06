package com.example.memento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.memento.controller.TestController
import com.example.memento.theme.MementoTheme
import org.example.MementoApp
import org.example.model.TestModel
import org.example.model.TestViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val userModel = TestModel()
//        val userViewModel = TestViewModel(userModel)
//        val userController = TestController(userModel)

        setContent {
            MementoTheme {
                MementoApp()
                //UserView(userViewModel, userController)
            }
        }
    }
}
