package com.skyver.trybase.presentation.platform

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.skyver.trybase.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton


@SuppressLint("ApplySharedPref")
@Singleton
class PreferenceHelper @Inject constructor(context: Application) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(PREFS_LOGGED_IN, false)
        set(value) {
            prefs.edit().putBoolean(PREFS_LOGGED_IN, value).commit()
        }

    var currentEqupAssetName: String?
        get() = prefs.getString(PREFS_CURRENT_EQUIPMENT_ASSET_NAME, null)
        set(value) {
            prefs.edit().putString(PREFS_CURRENT_EQUIPMENT_ASSET_NAME, value).commit()
        }

//    var currentProfile: StatusProfile?
//        get() {
//            val profile = prefs.getString(PREFS_STATUS_PROFILE, null)
//            return if (profile != null)
//                Gson().fromJson(profile, StatusProfile::class.java)
//            else null
//        }
//        set(value) {
//            val jsonProfile =
//                    if (value == null) null
//                    else {
//                        Gson().toJson(value)
//                    }
//            prefs.edit().putString(PREFS_STATUS_PROFILE, jsonProfile).commit()
//        }


    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun clearAll() = prefs.edit().clear().apply()

    companion object {
        const val PREFS_FILENAME = "${BuildConfig.APPLICATION_ID}.prefs"
        const val PREFS_REVOKED_LOCATION_PERMISSION = "$PREFS_FILENAME.revoked_location_permission"
        const val PREFS_LOCATION_WAS_DISABLED = "$PREFS_FILENAME.location_was_disabled"
        const val PREFS_LOGGED_IN = "$PREFS_FILENAME.loged_in"
        const val PREFS_CURRENT_EQUIPMENT_ASSET_NAME = "$PREFS_FILENAME.current_eq_asset_name"  //EqAssetName

    }
}