package com.peacecodetech.medeli.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

    fun routUserToMap(source: String, destination: String) {
        //check if map not installed
        try {
            //when map is installed, initialize uri
            val uri = Uri.parse("https://www.google.co.in/maps/dir/$source/$destination")

            val launchMap = Intent(Intent.ACTION_VIEW, uri)
            launchMap.setPackage("com.google.android.apps.maps")
            launchMap.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(launchMap)

        } catch (e: ActivityNotFoundException) {
            val uri =
                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
            val goToPlayStoreIntent = Intent(Intent.ACTION_VIEW, uri)
            goToPlayStoreIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(goToPlayStoreIntent)
        }
    }



    /**
     * Add extended data to the intent. The name must include a package prefix,
     * for example the app com.android.contacts would use names like "com.android.contacts
     * @param title of the extra data, with package prefix
     * @param value – The String data value.
     * @return Returns the same Intent object, for chaining multiple calls into a single statement.
     * */

    fun sharedSheet(title:String, value: String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, value)
            putExtra(Intent.EXTRA_TITLE,title)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    /**
     *A function to navigate user to designated page when press on the device back press. call this on onAttach()
     * Call this [onBackPressed] method and pass the navigation Id
     * @param directionId is the id for the fragment to navigate to
     * @sample onAttach see the commented line for usage
     * */

    fun Fragment.onBackPressed(directionId: Int) {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(directionId)
                //TODO: DO YOUR USE CASE HERE OR JUST NAVIGATE
             //   (activity as HomeActivity).binding.coordinator.visibility = View.VISIBLE
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(
            this, callback
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
       // bottomOnNavOnBackPress(R.id.action_checkoutFragment_to_cartFragment)
    }


    override fun onStop() {
        super.onStop()
        hideProgressBar()
    }
}