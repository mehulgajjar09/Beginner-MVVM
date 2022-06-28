package com.mehul.corelibrary.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.ParseException
import android.net.Uri
import android.text.Html
import android.text.InputFilter
import android.text.Spanned
import android.text.format.DateUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehul.corelibrary.core.CoreApplication
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


object Utils {

    @Suppress("DEPRECATION")
    @SuppressLint("PackageManagerGetSignatures")
    fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("hash", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("", "printHashKey()", e)
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isValidPassword(password: String): Boolean {
        return password.trim().length >= 6
    }


    @Suppress("DEPRECATION")
    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        return info != null && info.isConnected
    }

    fun dismissAllDialogs(manager: FragmentManager?) {
        val fragments: List<Fragment> = manager?.fragments ?: return
        for (fragment in fragments) {
            if (fragment is DialogFragment) {
                val dialogFragment: DialogFragment = fragment
                dialogFragment.dismissAllowingStateLoss()
            }
            val childFragmentManager: FragmentManager? = fragment.childFragmentManager
            childFragmentManager?.let { dismissAllDialogs(childFragmentManager) }
        }
    }

    fun covertToTimeAgo(time: String?): CharSequence {
        if (time == null) {
            return ""
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        return try {
            val time = sdf.parse(time).time
            val now = System.currentTimeMillis()
            val ago: CharSequence =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            ago
        } catch (e: ParseException) {
            e.printStackTrace()
            "Invalid Date"
        }
    }

    fun appInstalledOrNot(uri: String): Boolean {
        val pm: PackageManager = CoreApplication.getInstance().packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }

    val emojiFilter = InputFilter { source, start, end, dest, dstart, dend ->
        for (index in start until end) {
            val type = Character.getType(source[index])

            when (type) {
                '*'.toInt(),
                Character.OTHER_SYMBOL.toInt(),
                Character.SURROGATE.toInt(),
                -> {
                    return@InputFilter ""
                }
                Character.LOWERCASE_LETTER.toInt() -> {
                    val index2 = index + 1
                    if (index2 < end && Character.getType(source[index + 1]) == Character.NON_SPACING_MARK.toInt())
                        return@InputFilter ""
                }
                Character.DECIMAL_DIGIT_NUMBER.toInt() -> {
                    val index2 = index + 1
                    val index3 = index + 2
                    if (index2 < end && index3 < end &&
                        Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt() &&
                        Character.getType(source[index3]) == Character.ENCLOSING_MARK.toInt()
                    )
                        return@InputFilter ""
                }
                Character.OTHER_PUNCTUATION.toInt() -> {
                    val index2 = index + 1

                    if (index2 < end && Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt()) {
                        return@InputFilter ""
                    }
                }
                Character.MATH_SYMBOL.toInt() -> {
                    val index2 = index + 1
                    if (index2 < end && Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt())
                        return@InputFilter ""
                }
            }
        }
        return@InputFilter null
    }

    fun convertStringToHTML(txt: String, color: String): String {
        val myCustomStyleString =
            "<html><head>" +
                    "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/poppins_regular.ttf\")}" +
                    "body,* { color: $color ; font-family: MyFont; font-size: 16px;text-align: justify;}" +
                    "img{max-width:100%;height:auto; border-radius: 8px;}</style></head>"
        return "$myCustomStyleString<body><div style=\"direction:ltr\">$txt</div></body>"

    }

}
/*
private val progressObserver = Observer<Event<Boolean>> {
    it?.getContentIfNotHandled()?.let { showProgress ->
        if (showProgress) {
            showProgressBar()
        } else {
            hideProgressBar()
        }
    }
}
private val apiErrorObserver = Observer<Event<String>> { message ->
    message?.getContentIfNotHandled()?.let {
        rootView?.snack(it, color = R.color.red)
    }
}
private val successMessageObserver = Observer<Event<String>> { message ->
    message?.getContentIfNotHandled()?.let {
        rootView?.snack(it, color = R.color.challenge_color_green_dark)
    }
}
private val validationErrorMessageobserver = Observer<Event<Int>> { message ->
    message?.getContentIfNotHandled()?.let {
        rootView?.snack(it, color = R.color.red)
    }
}

fun addExtraObserver() {
    viewModel().showProgress.observe(this, progressObserver)
    viewModel().apiErrorMessage.observe(this, apiErrorObserver)
    viewModel().successMessageFromAPI.observe(this, successMessageObserver)
    viewModel().validationErrorMessage.observe(this, validationErrorMessageobserver)
}

fun removeExtraObserver() {
    viewModel().showProgress.removeObserver(progressObserver)
    viewModel().apiErrorMessage.removeObserver(apiErrorObserver)
    viewModel().successMessageFromAPI.removeObserver(successMessageObserver)
    viewModel().validationErrorMessage.removeObserver(validationErrorMessageobserver)
}
*/
