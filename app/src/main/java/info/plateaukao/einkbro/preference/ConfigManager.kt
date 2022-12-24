package info.plateaukao.einkbro.preference

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Build
import android.print.PrintAttributes
import androidx.core.content.edit
import info.plateaukao.einkbro.R
import info.plateaukao.einkbro.database.Bookmark
import info.plateaukao.einkbro.epub.EpubFileInfo
import info.plateaukao.einkbro.unit.ViewUnit
import info.plateaukao.einkbro.util.Constants
import info.plateaukao.einkbro.util.TranslationLanguage
import info.plateaukao.einkbro.view.GestureType
import info.plateaukao.einkbro.view.Orientation
import info.plateaukao.einkbro.view.toolbaricons.ToolbarAction
import org.koin.core.component.KoinComponent
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

class ConfigManager(
    private val context: Context,
    private val sp: SharedPreferences
) : KoinComponent {

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sp.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sp.unregisterOnSharedPreferenceChangeListener(listener)
    }

    var enableWebBkgndLoad by BooleanPreference(sp, K_BKGND_LOAD, true)
    var enableJavascript by BooleanPreference(sp, K_JAVASCRIPT, true)
    var isToolbarOnTop by BooleanPreference(sp, K_TOOLBAR_TOP, false)
    var enableViBinding by BooleanPreference(sp, K_VI_BINDING, false)
    var isMultitouchEnabled by BooleanPreference(sp, K_MULTITOUCH, false)
    var whiteBackground by BooleanPreference(sp, K_WHITE_BACKGROUND, false)
    var useUpDownPageTurn by BooleanPreference(sp, K_UPDOWN_PAGE_TURN, false)
    var touchAreaHint by BooleanPreference(sp, K_TOUCH_HINT, true)
    var volumePageTurn by BooleanPreference(sp, K_VOLUME_PAGE_TURN, true)
    var boldFontStyle by BooleanPreference(sp, K_BOLD_FONT, false)
    var shouldSaveTabs by BooleanPreference(sp, K_SHOULD_SAVE_TABS, true)
    var adBlock by BooleanPreference(sp, K_ADBLOCK, true)
    var cookies by BooleanPreference(sp, K_COOKIES, true)
    var saveHistory by BooleanPreference(sp, K_SAVE_HISTORY, true)
    var shareLocation by BooleanPreference(sp, K_SHARE_LOCATION, false)
    var enableTouchTurn by BooleanPreference(sp, K_ENABLE_TOUCH, false)
    var keepAwake by BooleanPreference(sp, K_KEEP_AWAKE, false)
    var desktop by BooleanPreference(sp, K_DESKTOP, false)
    var continueMedia by BooleanPreference(sp, K_MEDIA_CONTINUE, false)
    var restartChanged by BooleanPreference(sp, K_RESTART_CHANGED, false)
    var autoFillForm by BooleanPreference(sp, K_AUTO_FILL, true)
    var shouldTrimInputUrl by BooleanPreference(sp, K_TRIM_INPUT_URL, false)
    var enableZoom by BooleanPreference(sp, K_ENABLE_ZOOM, false)
    var shouldPruneQueryParameters by BooleanPreference(sp, K_PRUNE_QUERY_PARAMETERS, false)
    var translationPanelSwitched by BooleanPreference(sp, K_TRANSLATE_PANEL_SWITCHED, false)
    var translationScrollSync by BooleanPreference(sp, K_TRANSLATE_SCROLL_SYNC, false)
    var twoPanelLinkHere by BooleanPreference(sp, K_TWO_PANE_LINK_HERE, false)
    var switchTouchAreaAction by BooleanPreference(sp, K_TOUCH_AREA_ACTION_SWITCH, false)
    var hideTouchAreaWhenInput by BooleanPreference(sp, K_TOUCH_AREA_HIDE_WHEN_INPUT, false)
    var customFontChanged by BooleanPreference(sp, K_CUSTOM_FONT_CHANGED, false)
    var debugWebView by BooleanPreference(sp, K_DEBUG_WEBVIEW, false)
    var shouldShowTabBar by BooleanPreference(sp, K_SHOW_TAB_BAR, false)
    var confirmTabClose by BooleanPreference(sp, K_CONFIRM_TAB_CLOSE, false)
    var shouldHideToolbar by BooleanPreference(sp, K_HIDE_TOOLBAR, false)
    var showToolbarFirst by BooleanPreference(sp, K_SHOW_TOOLBAR_FIRST, true)
    var enableNavButtonGesture by BooleanPreference(sp, K_NAV_BUTTON_GESTURE, false)
    var clearCache by BooleanPreference(sp, K_CLEAR_CACHE, false)
    var clearIndexedDB by BooleanPreference(sp, K_CLEAR_INDEXEDDB, false)
    var clearCookies by BooleanPreference(sp, K_CLEAR_COOKIES, false)
    var clearHistory by BooleanPreference(sp, K_CLEAR_HISTORY, false)
    var clearWhenQuit by BooleanPreference(sp, K_CLEAR_WHEN_QUIT, false)
    var enableRemoteAccess by BooleanPreference(sp, K_ENABLE_REMOTE_ACCESS, true)
    var enableImages by BooleanPreference(sp, K_ENABLE_IMAGES, true)

    var isIncognitoMode: Boolean
        get() = sp.getBoolean(K_IS_INCOGNITO_MODE, false)
        set(value) {
            cookies = !value
            saveHistory = !value
            sp.edit { putBoolean(K_IS_INCOGNITO_MODE, value) }
        }

    var pageReservedOffset: Int by IntPreference(sp, "sp_page_turn_left_value", 80)

    var fontSize: Int
        get() = sp.getString(K_FONT_SIZE, "100")?.toInt() ?: 100
        set(value) {
            sp.edit { putString(K_FONT_SIZE, value.toString()) }
        }

    var ttsSpeedValue: Int
        get() = sp.getString(K_TTS_SPEED_VALUE, "100")?.toInt() ?: 100
        set(value) {
            sp.edit { putString(K_TTS_SPEED_VALUE, value.toString()) }
        }

    var touchAreaCustomizeY by IntPreference(sp, K_TOUCH_AREA_OFFSET, 0)

    var customUserAgent by StringPreference(sp, K_CUSTOM_USER_AGENT)
    val customProcessTextUrl by StringPreference(sp, K_CUSTOM_PROCESS_TEXT_URL)
    var preferredTranslateLanguageString by StringPreference(sp, K_TRANSLATED_LANGS)
    var searchEngine by StringPreference(sp, K_SEARCH_ENGINE, if (Locale.getDefault().country == "CN") "2" else "5")
    var searchEngineUrl by StringPreference(sp, K_SEARCH_ENGINE_URL, "https://www.google.com/search?q=%s")
    var processTextUrl by StringPreference(sp, K_CUSTOM_PROCESS_TEXT_URL, "")

    var multitouchUp by GestureTypePreference(sp, K_MULTITOUCH_UP)
    var multitouchDown by GestureTypePreference(sp, K_MULTITOUCH_DOWN)
    var multitouchLeft by GestureTypePreference(sp, K_MULTITOUCH_LEFT)
    var multitouchRight by GestureTypePreference(sp, K_MULTITOUCH_RIGHT)
    var navGestureUp by GestureTypePreference(sp, K_GESTURE_NAV_UP)
    var navGestureDown by GestureTypePreference(sp, K_GESTURE_NAV_DOWN)
    var navGestureLeft by GestureTypePreference(sp, K_GESTURE_NAV_LEFT)
    var navGestureRight by GestureTypePreference(sp, K_GESTURE_NAV_RIGHT)

    var fabPosition: FabPosition
        get() = FabPosition.values()[sp.getString(K_NAV_POSITION, "0")?.toInt() ?: 0]
        set(value) {
            sp.edit { putString(K_NAV_POSITION, value.ordinal.toString()) }
        }

    var touchAreaType: TouchAreaType
        get() = TouchAreaType.values()[sp.getInt(K_TOUCH_AREA_TYPE, 0)]
        set(value) {
            sp.edit { putInt(K_TOUCH_AREA_TYPE, value.ordinal) }
        }

    var pdfPaperSize: PaperSize
        get() = PaperSize.values()[sp.getInt("pdf_paper_size", PaperSize.ISO_13.ordinal)]
        set(value) {
            sp.edit { putInt("pdf_paper_size", value.ordinal) }
        }

    var translationLanguage: TranslationLanguage
        get() = TranslationLanguage.values()[sp.getInt(
            K_TRANSLATE_LANGUAGE,
            getDefaultTranslationLanguage().ordinal
        )]
        set(value) {
            sp.edit { putInt(K_TRANSLATE_LANGUAGE, value.ordinal) }
        }

    var translationOrientation: Orientation
        get() = Orientation.values()[sp.getInt(
            K_TRANSLATE_ORIENTATION,
            Orientation.Horizontal.ordinal
        )]
        set(value) {
            sp.edit { putInt(K_TRANSLATE_ORIENTATION, value.ordinal) }
        }

    var favoriteUrl by StringPreference(sp, K_FAVORITE_URL, Constants.DEFAULT_HOME_URL)

    var toolbarActions: List<ToolbarAction>
        get() {
            val key =
                if (ViewUnit.isLandscape(context)) K_TOOLBAR_ICONS_FOR_LARGE else K_TOOLBAR_ICONS
            val iconListString =
                sp.getString(key, sp.getString(K_TOOLBAR_ICONS, getDefaultIconStrings())) ?: ""
            return iconStringToEnumList(iconListString)
        }
        set(value) {
            sp.edit {
                val key =
                    if (ViewUnit.isLandscape(context)) K_TOOLBAR_ICONS_FOR_LARGE else K_TOOLBAR_ICONS
                putString(key, value.map { it.ordinal }.joinToString(","))
            }
        }

    var customFontInfo: CustomFontInfo?
        get() = sp.getString(K_CUSTOM_FONT, "")?.toCustomFontInfo()
        set(value) {
            sp.edit { putString(K_CUSTOM_FONT, value?.toSerializedString() ?: "") }
            if (fontType == FontType.CUSTOM) {
                customFontChanged = true
            }
        }

    var recentBookmarks: List<RecentBookmark>
        get() {
            val string = sp.getString(K_RECENT_BOOKMARKS, "") ?: ""
            if (string.isBlank()) return emptyList()

            return try {
                string.split(RECENT_BOOKMARKS_SEPARATOR)
                    .mapNotNull { it.toRecentBookmark() }
                    .sortedByDescending { it.count }
            } catch (exception: Exception) {
                sp.edit { remove(K_RECENT_BOOKMARKS) }
                emptyList()
            }
        }
        set(value) {
            if (value.containsAll(recentBookmarks) && recentBookmarks.containsAll(value)) {
                return
            }

            sp.edit {
                if (value.isEmpty()) {
                    remove(K_RECENT_BOOKMARKS)
                } else {
                    // check if the new value the same as the old one
                    putString(
                        K_RECENT_BOOKMARKS,
                        value.joinToString(RECENT_BOOKMARKS_SEPARATOR) { it.toSerializedString() }
                    )
                }
            }
        }

    fun addRecentBookmark(bookmark: Bookmark) {
        var newList = recentBookmarks.toMutableList()
        val sameItem = newList.firstOrNull { it.url == bookmark.url }
        if (sameItem != null) {
            sameItem.count++
        } else {
            newList.add(RecentBookmark(bookmark.title, bookmark.url, 1))
        }

        recentBookmarks = if (newList.size > RECENT_BOOKMARK_LIST_SIZE) {
            newList.sortedByDescending { it.count }.subList(0, RECENT_BOOKMARK_LIST_SIZE - 1)
        } else {
            newList.sortedByDescending { it.count }
        }
    }

    fun clearRecentBookmarks() {
        recentBookmarks = emptyList()
    }

    var savedAlbumInfoList: List<AlbumInfo>
        get() {
            val string = sp.getString(K_SAVED_ALBUM_INFO, "") ?: ""
            if (string.isBlank()) return emptyList()

            return try {
                string.split(ALBUM_INFO_SEPARATOR).mapNotNull { it.toAlbumInfo() }
            } catch (exception: Exception) {
                sp.edit { remove(K_SAVED_ALBUM_INFO) }
                emptyList()
            }
        }
        set(value) {
            if (value.containsAll(savedAlbumInfoList) && savedAlbumInfoList.containsAll(value)) {
                return
            }

            sp.edit {
                if (value.isEmpty()) {
                    remove(K_SAVED_ALBUM_INFO)
                } else {
                    // check if the new value the same as the old one
                    putString(
                        K_SAVED_ALBUM_INFO,
                        value.joinToString(ALBUM_INFO_SEPARATOR) { it.toSerializedString() }
                    )
                }
            }
        }

    var purgeHistoryTimestamp: Long
        get() = sp.getLong(K_HISTORY_PURGE_TS, 0L)
        set(value) = sp.edit { putLong(K_HISTORY_PURGE_TS, value) }

    var currentAlbumIndex: Int
        get() = sp.getInt(K_SAVED_ALBUM_INDEX, 0)
        set(value) {
            if (currentAlbumIndex == value) return

            sp.edit { putInt(K_SAVED_ALBUM_INDEX, value) }
        }

    var fontType: FontType
        get() = FontType.values()[sp.getInt(K_FONT_TYPE, 0)]
        set(value) = sp.edit { putInt(K_FONT_TYPE, value.ordinal) }

    var translationMode: TranslationMode
        get() = TranslationMode.values()[sp.getInt(K_TRANSLATION_MODE, 6)]
        set(value) = sp.edit { putInt(K_TRANSLATION_MODE, value.ordinal) }

    var adSites: MutableSet<String>
        get() = sp.getStringSet(K_ADBLOCK_SITES, mutableSetOf()) ?: mutableSetOf()
        set(value) = sp.edit { putStringSet(K_ADBLOCK_SITES, value) }

    var savedEpubFileInfos: List<EpubFileInfo>
        get() = sp.getString(K_SAVED_EPUBS, "")?.toEpubFileInfoList() ?: mutableListOf()
        set(value) = sp.edit { putString(K_SAVED_EPUBS, toEpubFileInfosString(value)) }

    var darkMode: DarkMode
        get() = DarkMode.values()[sp.getString(K_DARK_MODE, "0")?.toInt() ?: 0]
        set(value) = sp.edit { putString(K_DARK_MODE, value.ordinal.toString()) }

    var newTabBehavior: NewTabBehavior
        get() = NewTabBehavior.values()[sp.getString(K_NEW_TAB_BEHAVIOR, "0")?.toInt() ?: 0]
        set(value) = sp.edit { putString(K_NEW_TAB_BEHAVIOR, value.ordinal.toString()) }

    var fabCustomPosition: Point
        get() {
            val str = sp.getString(K_FAB_POSITION, "") ?: ""
            return if (str.isBlank()) Point(0, 0)
            else Point(str.split(",").first().toInt(), str.split(",").last().toInt())
        }
        set(value) {
            sp.edit { putString(K_FAB_POSITION, "${value.x},${value.y}") }
        }

    var fabCustomPositionLandscape: Point
        get() {
            val str = sp.getString(K_FAB_POSITION_LAND, "") ?: ""
            return if (str.isBlank()) Point(0, 0)
            else Point(str.split(",").first().toInt(), str.split(",").last().toInt())
        }
        set(value) {
            sp.edit { putString(K_FAB_POSITION_LAND, "${value.x},${value.y}") }
        }

    private fun iconStringToEnumList(iconListString: String): List<ToolbarAction> {
        if (iconListString.isBlank()) return listOf()

        return iconListString.split(",").map { ToolbarAction.fromOrdinal(it.toInt()) }
    }

    private fun getDefaultTranslationLanguage(): TranslationLanguage {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
        return TranslationLanguage.findByLanguage(locale.language)
    }

    private fun getDefaultIconStrings(): String =
        ToolbarAction.defaultActions.joinToString(",") { action ->
            action.ordinal.toString()
        }

    companion object {
        const val K_TOUCH_AREA_TYPE = "sp_touch_area_type"
        const val K_TOOLBAR_ICONS = "sp_toolbar_icons"
        const val K_TOOLBAR_ICONS_FOR_LARGE = "sp_toolbar_icons_for_large"
        const val K_BOLD_FONT = "sp_bold_font"
        const val K_NAV_POSITION = "nav_position"
        const val K_FONT_SIZE = "sp_fontSize"
        const val K_TTS_SPEED_VALUE = "sp_tts_speed"
        const val K_FAVORITE_URL = "favoriteURL"
        const val K_VOLUME_PAGE_TURN = "volume_page_turn"
        const val K_SHOULD_SAVE_TABS = "sp_shouldSaveTabs"
        const val K_SAVED_ALBUM_INFO = "sp_saved_album_info"
        const val K_SAVED_ALBUM_INDEX = "sp_saved_album_index"
        const val K_DB_VERSION = "sp_db_version"
        const val K_IS_INCOGNITO_MODE = "sp_incognito"
        const val K_ADBLOCK = "SP_AD_BLOCK_9"
        const val K_SAVE_HISTORY = "saveHistory"
        const val K_COOKIES = "SP_COOKIES_9"
        const val K_TRANSLATION_MODE = "sp_translation_mode"
        const val K_ENABLE_TOUCH = "sp_enable_touch"
        const val K_TOUCH_HINT = "sp_touch_area_hint"
        const val K_KEEP_AWAKE = "sp_screen_awake"
        const val K_DESKTOP = "sp_desktop"
        const val K_TRANSLATE_LANGUAGE = "sp_translate_language"
        const val K_TRANSLATE_ORIENTATION = "sp_translate_orientation"
        const val K_TRANSLATE_PANEL_SWITCHED = "sp_translate_panel_switched"
        const val K_TRANSLATE_SCROLL_SYNC = "sp_translate_scroll_sync"
        const val K_ADBLOCK_SITES = "sp_adblock_sites"
        const val K_CUSTOM_USER_AGENT = "userAgent"
        const val K_WHITE_BACKGROUND = "sp_whitebackground"
        const val K_UPDOWN_PAGE_TURN = "sp_useUpDownForPageTurn"
        const val K_CUSTOM_PROCESS_TEXT_URL = "sp_process_text_custom"
        const val K_TWO_PANE_LINK_HERE = "sp_two_pane_link_here"
        const val K_DARK_MODE = "sp_dark_mode"
        const val K_TOUCH_AREA_OFFSET = "sp_touch_area_offset"
        const val K_TOUCH_AREA_ACTION_SWITCH = "sp_touch_area_action_switch"
        const val K_TOUCH_AREA_HIDE_WHEN_INPUT = "sp_touch_area_hide_when_input"
        const val K_SAVED_EPUBS = "sp_saved_epubs"
        const val K_MULTITOUCH = "sp_multitouch"
        const val K_CUSTOM_FONT = "sp_custom_font"
        const val K_CUSTOM_FONT_CHANGED = "sp_custom_font_changed"
        const val K_FONT_TYPE = "sp_font_type"
        const val K_TOOLBAR_TOP = "sp_toolbar_top"
        const val K_VI_BINDING = "sp_enable_vi_binding"
        const val K_MEDIA_CONTINUE = "sp_media_continue"
        const val K_RECENT_BOOKMARKS = "sp_recent_bookmarks"
        const val K_RESTART_CHANGED = "restart_changed"
        const val K_JAVASCRIPT = "SP_JAVASCRIPT_9"
        const val K_BKGND_LOAD = "sp_background_loading"
        const val K_SHARE_LOCATION = "SP_LOCATION_9"
        const val K_AUTO_FILL = "sp_auto_fill"
        const val K_TRIM_INPUT_URL = "sp_trim_input_url"
        const val K_ENABLE_ZOOM = "sp_enable_zoom"
        const val K_DEBUG_WEBVIEW = "sp_debug_webview"
        const val K_HISTORY_PURGE_TS = "sp_history_purge_ts"
        const val K_PRUNE_QUERY_PARAMETERS = "sp_prune_query_parameter"
        const val K_SHOW_TAB_BAR = "sp_show_tab_bar"
        const val K_NEW_TAB_BEHAVIOR = "sp_plus_behavior"
        const val K_TRANSLATED_LANGS = "sp_translated_langs"
        const val K_FAB_POSITION = "sp_fab_position"
        const val K_FAB_POSITION_LAND = "sp_fab_position_land"
        const val K_CONFIRM_TAB_CLOSE = "sp_close_tab_confirm"
        const val K_HIDE_TOOLBAR = "hideToolbar"
        const val K_SHOW_TOOLBAR_FIRST = "sp_toolbarShow"
        const val K_NAV_BUTTON_GESTURE = "sp_gestures_use"
        const val K_SEARCH_ENGINE = "SP_SEARCH_ENGINE_9"
        const val K_SEARCH_ENGINE_URL = "sp_search_engine_custom"
        const val K_ENABLE_REMOTE_ACCESS = "sp_remote"
        const val K_ENABLE_IMAGES = "SP_IMAGES_9"

        const val K_CLEAR_CACHE = "SP_CLEAR_CACHE_9"
        const val K_CLEAR_HISTORY = "SP_CLEAR_HISTORY_9"
        const val K_CLEAR_COOKIES = "SP_CLEAR_COOKIE_9"
        const val K_CLEAR_INDEXEDDB = "sp_clear_Indexeddb"
        const val K_CLEAR_WHEN_QUIT = "SP_CLEAR_QUIT_9"

        const val K_MULTITOUCH_UP = "sp_multitouch_up"
        const val K_MULTITOUCH_DOWN = "sp_multitouch_down"
        const val K_MULTITOUCH_LEFT = "sp_multitouch_left"
        const val K_MULTITOUCH_RIGHT = "sp_multitouch_right"

        const val K_GESTURE_NAV_UP = "setting_gesture_nav_up"
        const val K_GESTURE_NAV_DOWN = "setting_gesture_nav_down"
        const val K_GESTURE_NAV_LEFT = "setting_gesture_nav_left"
        const val K_GESTURE_NAV_RIGHT = "setting_gesture_nav_right"

        private const val ALBUM_INFO_SEPARATOR = "::::"
        private const val RECENT_BOOKMARKS_SEPARATOR = "::::"
        private const val EPUB_FILE_INFO_SEPARATOR = "::::"

        private const val RECENT_BOOKMARK_LIST_SIZE = 10
    }

    private fun String.toEpubFileInfoList(): MutableList<EpubFileInfo> =
        if (this.isEmpty() || this == EPUB_FILE_INFO_SEPARATOR) mutableListOf()
        else this.split(EPUB_FILE_INFO_SEPARATOR).map { fileString ->
            EpubFileInfo.fromString(fileString)
        }.toMutableList()

    private fun toEpubFileInfosString(list: List<EpubFileInfo>): String =
        list.joinToString(separator = EPUB_FILE_INFO_SEPARATOR) { it.toPrefString() }

    fun addSavedEpubFile(epubFileInfo: EpubFileInfo) {
        savedEpubFileInfos = savedEpubFileInfos.toMutableList().apply { add(epubFileInfo) }
    }

    fun removeSavedEpubFile(epubFileInfo: EpubFileInfo) {
        savedEpubFileInfos = savedEpubFileInfos.toMutableList().apply { remove(epubFileInfo) }
    }

}

class BooleanPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean = false
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        sharedPreferences.edit { putBoolean(key, value) }
}

class IntPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Int = 0
) : ReadWriteProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
        sharedPreferences.edit { putInt(key, value) }
}

class StringPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: String = ""
) : ReadWriteProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String =
        sharedPreferences.getString(key, defaultValue) ?: defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) =
        sharedPreferences.edit { putString(key, value) }
}

class GestureTypePreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
) : ReadWriteProperty<Any, GestureType> {

    override fun getValue(thisRef: Any, property: KProperty<*>): GestureType =
        GestureType.from(sharedPreferences.getString(key, "01") ?: "01")

    override fun setValue(thisRef: Any, property: KProperty<*>, value: GestureType) =
        sharedPreferences.edit { putString(key, value.value) }
}


enum class PaperSize(val sizeString: String, val mediaSize: PrintAttributes.MediaSize) {
    ISO_13("A4 (13\")", PrintAttributes.MediaSize.ISO_A4),
    SIZE_10("A5 (10\")", PrintAttributes.MediaSize.ISO_A5),
    ISO_67("Hisense A7 (6.7\")", PrintAttributes.MediaSize.PRC_5),
    SIZE_8("C6 (8\")", PrintAttributes.MediaSize.ISO_C6),
}

enum class FabPosition {
    Right, Left, Center, NotShow, Custom
}

enum class TranslationMode(val label: String) {
    ONYX("ONYX"),
    GOOGLE("Google Text"),
    PAPAGO("Papago Text"),
    PAPAGO_URL("Papago Full Page"),
    GOOGLE_URL("Google Full Page"),
    PAPAGO_DUAL("Papago Dual Pane"),
    GOOGLE_IN_PLACE("Google in-Place"),
}

enum class FontType(val resId: Int) {
    SYSTEM_DEFAULT(R.string.system_default),
    SERIF(R.string.serif),
    GOOGLE_SERIF(R.string.googleserif),
    CUSTOM(R.string.custom_font)
}

enum class DarkMode {
    SYSTEM, FORCE_ON, DISABLED
}

enum class NewTabBehavior {
    START_INPUT, SHOW_HOME, SHOW_RECENT_BOOKMARKS
}

data class AlbumInfo(
    val title: String,
    val url: String
) {
    fun toSerializedString(): String = "$title::$url"
}

data class CustomFontInfo(
    val name: String,
    val url: String
) {
    fun toSerializedString(): String = "$name::$url"
}

data class RecentBookmark(val name: String, val url: String, var count: Int) {
    fun toSerializedString(): String = "$name::$url::$count"
}

private fun String.toRecentBookmark(): RecentBookmark? {
    val segments = this.split("::", limit = 3)
    if (segments.size != 3) return null
    return RecentBookmark(segments[0], segments[1], segments[2].toInt())
}

private fun String.toAlbumInfo(): AlbumInfo? {
    val segments = this.split("::", limit = 2)
    if (segments.size != 2) return null
    return AlbumInfo(segments[0], segments[1])
}

private fun String.toCustomFontInfo(): CustomFontInfo? {
    val segments = this.split("::", limit = 2)
    if (segments.size != 2) return null
    return CustomFontInfo(segments[0], segments[1])
}

fun KMutableProperty0<Boolean>.toggle() = set(!get())
