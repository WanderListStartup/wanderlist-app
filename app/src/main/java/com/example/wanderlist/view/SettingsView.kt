// SettingsView.kt
package com.example.wanderlist.view

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.provider.Settings
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wanderlist.components.BackCircle
import com.example.wanderlist.components.ClickableSettingItem
import com.example.wanderlist.components.EditProfileTextField
import com.example.wanderlist.components.LoginTitle
import com.example.wanderlist.components.SectionTitle
import com.example.wanderlist.components.ToggleSettingItem
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.viewmodel.AuthViewModel
import com.example.wanderlist.viewmodel.SettingsViewModel

@Composable
fun SettingsView(
    onNavigateToProfile: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Log.d("Permission", "Notification permission granted")
        } else {
            Log.d("Permission", "Notification permission denied")
        }
        viewModel.dismissNotificationDialog()
    }

    LaunchedEffect(Unit) {
        viewModel.checkNotificationPermissionStatus()
    }


    if (viewModel.showingNotificationDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissNotificationDialog() },
            title = { Text("Enable Notifications") },
            text = { Text("We use notifications for friend requests.") },
            confirmButton = {
                TextButton(onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else{
                        //noop should be true already? idfk
                    }
                }) {
                    Text("Allow")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.dismissNotificationDialog()
                }) {
                    Text("Not Now")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                }
                .padding(top = 50.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clickable {
                        onNavigateToProfile()
                    }
            ) {
                BackCircle()
            }
            Spacer(modifier = Modifier.weight(0.37f))
            LoginTitle("Settings")
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(50.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            SectionTitle("Account")
            HorizontalDivider()

            EditProfileTextField(
                label = "Phone Number",
                value = viewModel.phone,
                onValueChange = { viewModel.onPhoneChange(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            EditProfileTextField(
                label = "Email",
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            ToggleSettingItem(
                title = "Private account",
                checked = viewModel.isPrivateAccount,
                onCheckedChange = { viewModel.onPrivateAccountChange(it) }
            )

            Spacer(modifier = Modifier.height(15.dp))

            SectionTitle("Content")
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            ToggleSettingItem(
                title = "Notifications",
                checked = viewModel.isNotificationsEnabled,
                onCheckedChange = { newVal ->
                    if (!newVal) {
                        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                        } else {
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                        }
                        context.startActivity(intent)
                    }
                    viewModel.onNotificationsChange(newVal)
                }
            )

//             Save button at the bottom.
            Button(
                enabled = viewModel.isDomainValidState && viewModel.isValidPhoneNumber(),
                onClick = {
                    authViewModel.updateUserSettings(
                        phone = viewModel.phone,
                        email = viewModel.email,
                        isPrivateAccount = viewModel.isPrivateAccount,
                        isNotificationsEnabled = viewModel.isNotificationsEnabled
                    ) { result ->
                        when (result) {
                            is AuthDataStore.Result.Success -> {
                                Toast.makeText(context, "Settings updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            is AuthDataStore.Result.Error -> {
                                Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = wanderlistBlue),
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .align(Alignment.End)
                    .height(48.dp)
            ) {
                Text("Save", fontSize = 16.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
        ) {

            ClickableSettingItem(title = "Log out") {authViewModel.logout()}

            HorizontalDivider()
            ClickableSettingItem(title = "Delete Account", isDestructive = true) { authViewModel.deleteAccount {} }
        }
    }
}
