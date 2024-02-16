package info.plateaukao.einkbro.activity

import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import info.plateaukao.einkbro.R
import info.plateaukao.einkbro.preference.ConfigManager
import info.plateaukao.einkbro.util.Constants.Companion.ACTION_DICT
import info.plateaukao.einkbro.view.NinjaToast
import info.plateaukao.einkbro.view.dialog.compose.TranslateDialogFragment
import info.plateaukao.einkbro.viewmodel.TRANSLATE_API
import info.plateaukao.einkbro.viewmodel.TranslationViewModel
import org.koin.android.ext.android.inject

class DictActivity : AppCompatActivity() {
    private val config: ConfigManager by inject()
    private val translationViewModel: TranslationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict)

        hideStatusBar()

        when (intent.action) {
            in listOf("colordict.intent.action.PICK_RESULT", "colordict.intent.action.SEARCH") -> {
                if (!config.externalSearchWithGpt) {
                    forwardDictIntentAndFinish()
                } else {
                    val text = intent.getStringExtra("EXTRA_QUERY") ?: return
                    searchWithGpt(text)
                }
            }

            Intent.ACTION_PROCESS_TEXT -> {
                if (!config.externalSearchWithGpt) {
                    forwardProcessTextIntentAndFinish()
                } else {
                    val text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT) ?: return
                    searchWithGpt(text)
                }
            }
        }
    }

    private fun searchWithGpt(text: String) {
        translationViewModel.updateInputMessage(text)
        if (translationViewModel.hasOpenAiApiKey()) {
            TranslateDialogFragment(
                translationViewModel,
                TRANSLATE_API.GPT,
                Point(50, 50),
            ).show(supportFragmentManager, "contextMenu")
            monitorFragmentStack()
        } else {
            NinjaToast.show(this, R.string.gpt_api_key_not_set)
        }
    }

    private fun forwardDictIntentAndFinish() {
        val newIntent = Intent(this, BrowserActivity::class.java).apply {
            action = ACTION_DICT
            putExtra("EXTRA_QUERY", intent.getStringExtra("EXTRA_QUERY"))
        }
        startActivity(newIntent)
        finish()
    }

    private fun forwardProcessTextIntentAndFinish() {
        val newIntent = Intent(this, BrowserActivity::class.java).apply {
            action = Intent.ACTION_PROCESS_TEXT
            putExtra(Intent.EXTRA_PROCESS_TEXT, intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT))
        }
        startActivity(newIntent)
        finish()
    }

    private fun monitorFragmentStack() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }

    private fun hideStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.systemBars())
            window.setDecorFitsSystemWindows(false)
        }
    }
}