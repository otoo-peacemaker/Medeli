package com.peacecodetech.medeli.util


import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.peacecodetech.medeli.MainActivity
import com.peacecodetech.medeli.ui.main.home.HomeActivity
import java.io.IOException
import java.util.regex.Pattern

val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

fun isValidString(str: String): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
}

fun validatePassword(str: String): Boolean {
    return str.length > 10 && (str.isNotEmpty())
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun Context.startHomeActivity() =
    Intent(this, HomeActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startAuthActivity() =
    Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.message
        return null
    }
    return jsonString
}

/**checking the current state of the graph*/
fun Fragment.checkNavDestination(checkCurrentDestination: Boolean) {
    var currentDestination = checkCurrentDestination
    currentDestination = false
    if (currentDestination && findNavController().currentDestination == null) {
        findNavController().navigate(findNavController().graph.startDestinationId)
    } else {
        currentDestination = false
    }
}