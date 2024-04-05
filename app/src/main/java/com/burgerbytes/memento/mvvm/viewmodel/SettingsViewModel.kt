package com.burgerbytes.memento.mvvm.viewmodel
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class SettingsViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()

    val mhResourceLink = android.content.Intent(android.content.Intent.ACTION_VIEW)
    val mhPhoneLink = android.content.Intent(android.content.Intent.ACTION_VIEW)


    init {
        mhResourceLink.data = android.net.Uri.parse("https://www.canada.ca/en/public-health/services/mental-health-services/mental-health-get-help.html")
        mhPhoneLink.data = android.net.Uri.parse("https://www.camh.ca/en/health-info/crisis-resources")

    }
}