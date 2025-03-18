package com.example.wanderlist.view


import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.wanderlist.components.LoginText
import com.example.wanderlist.model.AuthDataStore
import com.example.wanderlist.viewmodel.AuthState
import com.example.wanderlist.viewmodel.AuthViewModel
import com.example.wanderlist.viewmodel.SignUpViewModel
import kotlinx.serialization.Serializable

//Route to the actual app
@Serializable object MainView

//Nested NavGraph just for the login shit,
@Serializable object RegisterLoginView
@Serializable object Landing
@Serializable object Login
@Serializable object Register
@Serializable object Username
@Serializable object Welcome

@Composable
fun AppView(
    authViewModel: AuthViewModel = viewModel()
){
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState){
        when(authState){
            is AuthState.UnAuthenticated -> navController.navigate(RegisterLoginView)
            else -> Unit
        }
    }

    NavHost(navController, startDestination=MainView){
            composable<MainView>{
                Text("MAINVIEW")
                //testing button
                Button(
                    onClick = {
                        authViewModel.logout()
                         },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 31.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                ) {
                    Text("logout nka")
                }
            }
            navigation<RegisterLoginView>(startDestination=Landing){
                composable<Landing> {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Landing(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateToLogin = {
                                navController.navigate(route=Login)
                            },
                            onNavigateToRegister = {
                                navController.navigate(route=Register)
                            }
                        )
                    }
                }
                composable<Login> {
                    LoginView(
                        onNavigateToLanding = { navController.navigate(route=Landing)},
                        onNavigateToRegister = { navController.navigate(route=Register)},
                        onNavigateToHome = {navController.navigate(route=MainView)},
                        authViewModel = authViewModel
                    )


                }
                composable<Register> {
                    SignUpView(
                        onNavigateToHome = {navController.navigate(route=MainView)},
                        authViewModel=authViewModel
                    )
                }
                composable<Username> {

                }
                composable<Welcome>{

                }
            }
        }

}
