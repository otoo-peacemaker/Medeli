package com.peacecodetech.medeli.util

import android.app.UiModeManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.peacecodetech.medeli.MainActivity
import com.peacecodetech.medeli.ui.main.home.HomeActivity

open class BaseFragment : Fragment() {

    private var progressBar: ProgressBar? = null

    fun setProgressBar(bar: ProgressBar) {
        progressBar = bar
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    fun hideKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showDialog(title: String, message: String, action: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                action()
            }
            .show()
    }

    fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

     fun routUserToMap(source:String, destination:String){
        //check if map not installed
        try {
            //when map is installed, initialize uri
            val uri = Uri.parse("https://www.google.co.in/maps/dir/$source/$destination")

            val launchMap = Intent(Intent.ACTION_VIEW, uri)
            launchMap.setPackage("com.google.android.apps.maps")
            launchMap.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(launchMap)

        }catch (e: ActivityNotFoundException){
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
            val goToPlayStoreIntent = Intent(Intent.ACTION_VIEW, uri)
            goToPlayStoreIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(goToPlayStoreIntent)
        }
    }

    fun setNightMode(target: Context, state: Boolean) {
        val uiManager = target.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (state) {
            //uiManager.enableCarMode(0);
            uiManager.nightMode = UiModeManager.MODE_NIGHT_YES
        } else {
            // uiManager.disableCarMode(0);
            uiManager.nightMode = UiModeManager.MODE_NIGHT_NO
        }
    }


    override fun onStop() {
        super.onStop()
        hideProgressBar()
    }
}