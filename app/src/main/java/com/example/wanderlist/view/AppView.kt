package com.example.wanderlist.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.wanderlist.HomePageView
import com.example.wanderlist.viewmodel.AuthState
import com.example.wanderlist.viewmodel.AuthViewModel
import kotlinx.serialization.Serializable

// Route to the actual app
@Serializable object MainView

// Nested NavGraph just for the login shit,
@Serializable object RegisterLoginView

@Serializable object Landing

@Serializable object Login

@Serializable object Register

@Serializable object Welcome
@Serializable object Settings
@Serializable object UserSettings
@Serializable object Profile

@Composable
fun AppView(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.UnAuthenticated -> navController.navigate(RegisterLoginView)
            else -> Unit
        }
    }

    NavHost(navController, startDestination = MainView) {
        composable<MainView>{
            HomePageView(
                authViewModel = authViewModel,
                onNavigateToProfile = {navController.navigate(route = Profile)}
            )
        }
        composable<Settings> {
            SettingsView(
                onNavigateToProfile = {navController.navigate(route=Profile)},
                authViewModel=authViewModel
            )
        }
        composable<UserSettings> {
            EditProfileView(
                onNavigateToProfile = {navController.navigate(route=Profile)},
                authViewModel=authViewModel
            )
        }
        composable<Profile> {
            ProfileView(
                onNavigateToHome = { navController.navigate(route = MainView) },
                onNavigateToSettings = {navController.navigate(route=Settings)},
                onNavigateToUserSettings = {navController.navigate(route=UserSettings)},
                authViewModel=authViewModel
            )
        }
        navigation<RegisterLoginView>(startDestination = Landing) {
            composable<Landing> {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Landing(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToLogin = {
                            navController.navigate(route = Login)
                        },
                        onNavigateToRegister = {
                            navController.navigate(route = Register)
                        },
                    )
                }
            }
            composable<Login> {
                LoginView(
                    onNavigateToLanding = { navController.navigate(route = Landing) },
                    onNavigateToRegister = { navController.navigate(route = Register) },
                    onNavigateToHome = { navController.navigate(route = MainView) },
                    authViewModel = authViewModel,
                )
            }
            composable<Register> {
                SignUpView(
                    onNavigateToHome = { navController.navigate(route = MainView) },
                    onNavigateToLogin = { navController.navigate(route = Login) },
                    onBack = { navController.navigate(route = Landing) },
                    authViewModel = authViewModel,
                )
            }
            composable<Welcome> {
                //implement later
            }
        }
    }
}
