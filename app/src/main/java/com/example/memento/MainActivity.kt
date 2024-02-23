package com.example.memento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.memento.controller.UserController
import com.example.memento.theme.MementoTheme
import org.example.MementoApp
import org.example.model.UserModel
import org.example.model.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userModel = UserModel()
        val userViewModel = UserViewModel(userModel)
        val userController = UserController(userModel)

        setContent {
            MementoTheme {
                MementoApp()
                //UserView(userViewModel, userController)
            }
        }
    }
}
