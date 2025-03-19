package com.example.wanderlist.view


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.components.BackCircle
import com.example.wanderlist.components.ClickableSettingItem
import com.example.wanderlist.components.LoginTitle
import com.example.wanderlist.components.SectionTitle
import com.example.wanderlist.components.SettingItem
import com.example.wanderlist.components.ToggleSettingItem
import com.example.wanderlist.viewmodel.SettingsViewModel
import com.example.wanderlist.viewmodel.SignUpViewModel


@Composable
fun SettingsView(
    viewModel: SettingsViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {

        Row(
            modifier = Modifier.padding(top = 50.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            BackCircle()
            // IDK IF THIS IS CENTERED TO THE PIXEL I JUST EYEBALLED IT
            Spacer(modifier = Modifier.weight(.37f))

            LoginTitle("Settings")

            Spacer(modifier = Modifier.weight(1f))

        }

        Spacer(modifier = Modifier.height(50.dp))


        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp)

        ) {

            SectionTitle("Account")

            HorizontalDivider()

            SettingItem(title = "Phone Number", value = "+1 (518) 123-4567")

            HorizontalDivider()

            SettingItem(title = "Email", value = "lpaul_45@gmail.com")


            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            // Private Account Toggle Button
            ToggleSettingItem(
                title = "Private account",
                checked = viewModel.isPrivateAccount
            ) { viewModel.onPrivateAccountChange(it) }

            Spacer(modifier = Modifier.height(15.dp))

            SectionTitle("Content")
            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            // Notif Toggle Button
            ToggleSettingItem(
                title = "Notifications",
                checked = viewModel.isNotificationsEnabled
            ) { viewModel.onNotificationsChange(it) }


        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = 100.dp)



        ) {
            ClickableSettingItem(title = "Log out") {}

            HorizontalDivider()

            ClickableSettingItem(title = "Delete Account", isDestructive = true) {}

            HorizontalDivider()

        }


    }
}