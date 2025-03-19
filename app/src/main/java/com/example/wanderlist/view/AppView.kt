package com.example.wanderlist.view


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.wanderlist.HomePageView
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
fun AppView(){
    val navController = rememberNavController()
    val viewModel: SignUpViewModel = viewModel()
    NavHost(navController, startDestination=MainView){
            composable<MainView>{
                HomePageView()
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
                        viewModel = viewModel,
                        onNavigateToLanding = { navController.navigate(route=Landing)},
                        onNavigateToRegister = { navController.navigate(route=Register)}
                    )


                }
                composable<Register> {
                    SignUpView()
                }
                composable<Username> {

                }
                composable<Welcome>{

                }
            }
        }

}
