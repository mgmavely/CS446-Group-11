import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    var currentLanguage: MutableState<Language> = mutableStateOf(Language.ENGLISH)
}

enum class Language {
    ENGLISH,
    SPANISH
}
