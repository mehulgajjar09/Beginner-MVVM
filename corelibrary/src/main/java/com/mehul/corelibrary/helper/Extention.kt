package com.mehul.corelibrary.helper

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.mehul.corelibrary.BuildConfig
import com.mehul.corelibrary.R
import com.mehul.corelibrary.core.CoreBottomSheetDialogFragment
import com.mehul.corelibrary.core.CoreDialogFragment
import com.mehul.corelibrary.core.CoreFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

enum class NavigationOption { CONTINUE, BACK }

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitAllowingStateLoss()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.addAndHide(fragment: Fragment, frameId: Int) {
    if(fragment.isAdded) {
        when (fragment) {
            is CoreFragment<*> -> {
                fragment.initFragment()
            }
            is CoreDialogFragment<*> -> {
                fragment.initFragment()
            }
            is CoreBottomSheetDialogFragment<*> -> {
                fragment.initFragment()
            }
        }
        return
    }
    supportFragmentManager.inTransaction {
        add(frameId, fragment).hide(fragment)
    }
}

fun AppCompatActivity.loadFragment(old: Fragment?, fragment: Fragment): Fragment? {
    if (old?.javaClass?.simpleName == fragment.javaClass.simpleName
    ) {
        return fragment
    }
    supportFragmentManager.inTransaction {
        if (old != null) {
            hide(old).setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            ).show(fragment)
        } else {
            setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            ).show(fragment)
        }
    }
    return fragment
}

fun AppCompatActivity.getCurrentFragment(frameId: Int): Fragment? {
    return supportFragmentManager.findFragmentById(frameId)
}

fun Fragment.addFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.inTransaction { add(frameId, fragment) }
}

fun Fragment.replaceFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.inTransaction { replace(frameId, fragment) }
}

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    option: Bundle? = null,
    init: Intent.() -> Unit = {},
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, option)
}

inline fun <reified T : Any> Context.launchActivity(
    option: Bundle? = null,
    init: Intent.() -> Unit = {},
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, option)
}

inline fun <reified T : Any> Fragment.launchActivity(
    option: Bundle? = null,
    init: Intent.() -> Unit = {},
) {
    this.activity?.let {
        val intent = newIntent<T>(it)
        intent.init()
        startActivity(intent, option)
    }
}

inline fun <reified T : Any> Fragment.launchActivity(
    requestCode: Int = -1,
    option: Bundle? = null,
    init: Intent.() -> Unit = {},
) {
    this.activity?.let {
        val intent = newIntent<T>(it)
        intent.init()
        startActivityForResult(intent, requestCode, option)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent {
    return Intent(context, T::class.java)
}

inline fun View.snack(
    @StringRes messageRes: Int,
    @ColorRes color: Int = R.color.black,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {},
) {
    snack(resources.getString(messageRes), color, length, f)
}

inline fun View.snack(
    message: String,
    @ColorRes color: Int = R.color.aaa,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {},
) {
    val snack = Snackbar.make(this, message, length)
    val tv: TextView = snack.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    tv.setTextColor(Color.WHITE)
    tv.maxLines = 5
    snack.view.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
    snack.f()
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit = {}) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit = {}) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun Activity.snack(
    @StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG,
    @ColorRes color: Int = R.color.black,

    f: Snackbar.() -> Unit = {},
) {
    snack(resources.getString(messageRes), color, length, f)
}

fun Fragment.snack(
    @StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG,
    @ColorRes color: Int = R.color.black,

    f: Snackbar.() -> Unit = {},
) {
    view?.snack(resources.getString(messageRes), color, length, f)
}

fun Fragment.snack(
    message: String,
    @ColorRes color: Int = R.color.black,
    length: Int = Snackbar.LENGTH_LONG,

    f: Snackbar.() -> Unit = {},
) {
    view?.snack(message, color, length, f)
}

fun DialogFragment.snack(
    message: String,
    @ColorRes color: Int = R.color.black,
    length: Int = Snackbar.LENGTH_LONG,

    f: Snackbar.() -> Unit = {},
) {
    dialog?.window?.decorView?.findViewById<View>(android.R.id.content)
        ?.snack(message, color, length, f)
}

fun BottomSheetDialogFragment.snack(
    message: String,
    @ColorRes color: Int = R.color.black,
    length: Int = Snackbar.LENGTH_LONG,

    f: Snackbar.() -> Unit = {},
) {
    dialog?.window?.decorView?.findViewById<View>(android.R.id.content)
        ?.snack(message, color, length, f)
}

fun Activity.snack(
    message: String,
    @ColorRes color: Int = R.color.black,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {},
) {
    val view = findViewById<ViewGroup>(android.R.id.content)
    view?.apply {
        snack(message, color, length, f)
    }
}

inline fun <reified T : Any, V : ViewDataBinding> Activity.showBottomSheet(
    @LayoutRes id: Int, obj: T,
    listener: View.OnClickListener,
): BottomSheetDialog? {
    val bottomSheetDialog = BottomSheetDialog(this)
    bottomSheetDialog.let {
        val binding = DataBindingUtil.inflate<V>(this.layoutInflater, id, null, false)
        bottomSheetDialog.setContentView(binding.root)
//        binding.setVariable(BR.model, obj)
//        binding.setVariable(BR.clickListener, listener)
        bottomSheetDialog.show()
    }
    return bottomSheetDialog
}

inline fun <reified T : Any, V : ViewDataBinding> Fragment.showBottomSheet(
    @LayoutRes id: Int, obj: T,
    listener: View.OnClickListener,
): BottomSheetDialog? {
    return activity?.showBottomSheet<T, V>(id, obj, listener)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun TabLayout.stopTouch() {
    val touchableList = touchables
    touchableList?.forEach { it.isEnabled = false }
}

fun Date.convert_dd_MM_yyyy(toRegx: String = "dd/MM/yyyy"): String {
    try {
        val dateFormate = SimpleDateFormat(toRegx, Locale.getDefault())
        return dateFormate.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "format error"
}

fun Date.convert_MMM_dd_yyyy(toRegx: String = "MMM dd, yyyy"): String {
    try {
        val dateFormate = SimpleDateFormat(toRegx, Locale.getDefault())
        return dateFormate.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "format error"
}

fun Date.convert_MMM_yyyy_(toRegx: String = "MMM, yyyy"): String {
    try {
        val dateFormate = SimpleDateFormat(toRegx, Locale.getDefault())
        return dateFormate.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "format error"
}

fun Date.convert_yyyy_MM_dd_HH_mm_ss(toRegx: String = "yyyy-MM-dd HH:mm:ss"): String {
    try {
        val dateFormate = SimpleDateFormat(toRegx, Locale.getDefault())
        return dateFormate.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "format error"
}

fun Date.convert_EEE(toRegx: String = "EEE"): String {
    try {
        val dateFormate = SimpleDateFormat(toRegx, Locale.getDefault())
        return dateFormate.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "format error"
}

fun Date.convert_yyyy_MM_dd(toRegx: String = "yyyy-MM-dd"): String {
    try {
        val dateFormate = SimpleDateFormat(toRegx, Locale.getDefault())
        return dateFormate.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "format error"
}

fun String.convert_in_date_yyyy_mm_dd(fromRegex: String = "yyyy-MM-dd"): Date? {
    try {
        val dateFormate = SimpleDateFormat(fromRegex, Locale.getDefault())
        return dateFormate.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun String.getDate(fromRegex: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): Date? {
    try {
        val dateFormate = SimpleDateFormat(fromRegex, Locale.getDefault())
        return dateFormate.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun Context.showDateDialog(
    date: Calendar = Calendar.getInstance(),
    listener: ((DatePicker, year: Int, month: Int, day: Int) -> Unit)? = null,
) {
    val year = date.get(Calendar.YEAR)
    val month = date.get(Calendar.MONTH)
    val day = date.get(Calendar.DAY_OF_MONTH)
    val dateDialog = DatePickerDialog(this, listener, year, month, day)

    dateDialog.show()
}

fun Context.showTimeDialog(
    hour: Int = Calendar.getInstance().get(Calendar.HOUR),
    minute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    listener: ((TimePicker, hour: Int, minute: Int) -> Unit)? = null,
) {
    val mTimePicker = TimePickerDialog(
        this,
        listener,
        hour,
        minute,
        false
    )
    mTimePicker.show()
}

fun ImageView.load(file: File, click: Boolean = true) {
    Glide.with(this).load(file).into(this)
    /*if (click) {
        this.setOnClickListener {
            val fullImageIntent = Intent(this.context, FullScreenImageActivity::class.java)
            fullImageIntent.putExtra("file", file)
            this.context.startActivity(fullImageIntent)
        }
    }*/
}

fun ImageView.load(@DrawableRes src: Int, click: Boolean = true) {
    Glide.with(this).load(src).into(this)
    /*if (click) {
        this.setOnClickListener {
            val fullImageIntent = Intent(this.context, FullScreenImageActivity::class.java)
            fullImageIntent.putExtra("drw", src)
            this.context.startActivity(fullImageIntent)
        }
    }*/
}

fun ImageView.load(src: String, click: Boolean = true) {
    Glide.with(this).load(src).placeholder(R.drawable.ic_no_image).into(this)
    /*if (click) {
        this.setOnClickListener {
            val fullImageIntent = Intent(this.context, FullScreenImageActivity::class.java)
            fullImageIntent.putExtra("url", src)
            this.context.startActivity(fullImageIntent)
        }
    }*/
}

fun ImageView.loadProfileURL(src: String, click: Boolean = true) {
    Glide.with(this).load(src).placeholder(R.drawable.ic_no_profile_image).into(this)
    /*if (click) {
        this.setOnClickListener {
            val fullImageIntent = Intent(this.context, FullScreenImageActivity::class.java)
            fullImageIntent.putExtra("url", src)
            this.context.startActivity(fullImageIntent)
        }
    }*/
}

fun Context.logError(message: String) {
    if (BuildConfig.DEBUG)
        Log.e(this::class.java.simpleName, message)
}

fun Context.logNormal(message: String) {
    if (BuildConfig.DEBUG)
        Log.d(this::class.java.simpleName, message)
}

fun Fragment.logNormal(message: String) {
    if (BuildConfig.DEBUG)
        Log.d(this::class.java.simpleName, message)
}

fun String.isValidEmail() = Pattern.compile(
    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
              + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
              + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
              + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
              + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
).matcher(this).matches()

fun String.isValidStringForEditText() = Pattern.compile(
    "[a-zA-Z0-9_ ]"
).matcher(this).matches()

/*
fun String.isStrongPassword() = Pattern.compile(
    "(?=^.{8,20}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+}{\":;'?/>.<,])(?!.*\\s).*$"
).matcher(this).matches()
*/

fun String.removeSpaceFromString() : String{
    return trim().replace(Regex("(\\s)+"), " ")
}

fun String.isStrongPassword() = true

fun TextView.setHtml(string: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
    } else {
        this.text = Html.fromHtml(string)
    }
}

fun Activity.setStatusBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

fun RecyclerView.removeSpacePadding() {
    // apply spacing
    this.setPadding(
        0,
        0,
        0,
        0
    )
    this.clipToPadding = false
    this.clipChildren = false
    for (i in 0 until this.itemDecorationCount) {
        this.removeItemDecorationAt(i)
    }
}

// extension function to get / download bitmap from url
fun URL.toBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeStream(openStream())
    } catch (e: IOException) {
        null
    }
}


// extension function to save an image to internal storage
fun Bitmap.saveToInternalStorage(context: Context): Uri? {
    // get the context wrapper instance
    val wrapper = ContextWrapper(context)

    // initializing a new file
    // bellow line return a directory in internal storage
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)

    // create a file to save the image
    file = File(file, "${UUID.randomUUID()}.jpg")

    return try {
        // get the file output stream
        val stream: OutputStream = FileOutputStream(file)

        // compress bitmap
        compress(Bitmap.CompressFormat.JPEG, 100, stream)

        // flush the stream
        stream.flush()

        // close stream
        stream.close()

        // return the saved image uri
        Uri.parse(file.absolutePath)
    } catch (e: IOException) { // catch the exception
        e.printStackTrace()
        null
    }
}

inline fun View.snackWithBottomMargin(
    message: String,
    @ColorRes color: Int = R.color.black,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {},
) {
    val snack = Snackbar.make(this, message, length)
    //set bottom margin
    val snackBarView = snack.view
    val param =
        ((snackBarView /*as FrameLayout*/)/*.getChildAt(0*/).layoutParams as CoordinatorLayout.LayoutParams
    param.setMargins(
        param.leftMargin,
        param.topMargin,
        param.rightMargin,
        param.bottomMargin + 100
    )
//    snackBarView.layoutParams = param
    //set textview background
    val tv: TextView = snack.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    tv.setTextColor(Color.WHITE)
    tv.maxLines = 5
    snackBarView.setBackgroundColor(ContextCompat.getColor(context, color))
    snack.f()
    snack.show()
}
