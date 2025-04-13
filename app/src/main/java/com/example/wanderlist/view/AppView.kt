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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

@Serializable object FindFriendsView

@Serializable object Welcome
@Serializable object Settings
@Serializable object UserSettings
@Serializable object Profile
@Serializable object ShowMore

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
                onNavigateToProfile = {navController.navigate(route = Profile)},
                onNavigateToShowMore = { establishmentId -> navController.navigate("showMore/$establishmentId") },
            )
        }
        composable(
            route = "showMore/{establishmentId}",
            arguments = listOf(navArgument("establishmentId") { type = NavType.StringType })
        ) { backStackEntry ->
                val establishmentId = backStackEntry.arguments?.getString("establishmentId") ?: ""
                ShowMoreView(
                    establishmentId = establishmentId,
                    onBackClick = { navController.popBackStack() }

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
                onNavigateToFindFriends = { navController.navigate(route = FindFriendsView) },
                onNavigateToLikedPlace = { establishmentId -> navController.navigate("likedPlace/$establishmentId") },
            )
        }
        composable(
            route = "likedPlace/{establishmentId}",
            arguments = listOf(navArgument("establishmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val establishmentId = backStackEntry.arguments?.getString("establishmentId") ?: ""
            LikedPlaceView(
                establishmentId = establishmentId,
                onBackClick = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate(route = MainView) },
                onNavigateToShowMore = { id -> navController.navigate("showMore/$id") }
            )
        }
        composable<FindFriendsView> {
            FindFriendsView(
                onBackClick = { navController.popBackStack() }
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
