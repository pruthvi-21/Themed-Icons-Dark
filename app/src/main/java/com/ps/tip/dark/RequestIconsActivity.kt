package com.ps.tip.dark

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller
import com.ps.tip.dark.icons.EmailProviderAdapter
import com.ps.tip.dark.icons.IconsHelper
import com.ps.tip.dark.icons.RequestIconsListAdapter
import com.ps.tip.dark.utils.Constants.REQUEST_TIME_OUT_DURATION
import com.ps.tip.dark.utils.EmailUtils
import com.ps.tip.dark.utils.FirebaseDatabaseHelper
import com.ps.tip.dark.utils.IconsRequestBuilder

class RequestIconsActivity : AppCompatActivity() {

    private var adapter: RequestIconsListAdapter? = null
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }
    private val submitFab: FloatingActionButton by lazy { findViewById(R.id.fab_submit_request) }
    private val fastScroller: RecyclerFastScroller by lazy { findViewById(R.id.fast_scroller) }

    private var isCancelled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_icons)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val missingAppsList = IconsHelper.getMissingAppsList(this)
        adapter = RequestIconsListAdapter(this, missingAppsList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        fastScroller.attachRecyclerView(recyclerView)

        submitFab.setOnClickListener {
            sendIconRequestToFirebase()
        }
    }

    private fun sendIconRequestToFirebase() {
        if (adapter == null) return
        if (adapter?.appsList?.any { it.isChecked } == false) return
        isCancelled = false

        val progressDialog = getRequestProgressDialog {
            //cancel firebase request
            isCancelled = true
        }
        progressDialog.show()

        val selectedAppsList = adapter!!.appsList
            .filter { it.isChecked }
            .map { it.appInfo }

        val iconRequestJson = IconsRequestBuilder.build(this, selectedAppsList)
        FirebaseDatabaseHelper.checkConnection {
            if (isCancelled) return@checkConnection
            FirebaseDatabaseHelper.sendRequest(iconRequestJson.toString()) { status ->
                progressDialog.dismiss()

                if (status) {
                    Toast.makeText(
                        this,
                        R.string.toast_request_icons_positive_text,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    finish()
                } else {
                    //TODO: Fallback to email
                    //showEmailDialog()
                }
            }
        }

    }

    private fun getRequestProgressDialog(onDialogClose: () -> Unit): AlertDialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_progress, null)
        val negativeButton = dialogView.findViewById<Button>(R.id.cancel_button)
        val warningText = dialogView.findViewById<TextView>(R.id.body)
        negativeButton.isEnabled = false

        val progressDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val countDownTimer = object : CountDownTimer(REQUEST_TIME_OUT_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                negativeButton.text =
                    getString(R.string.dialog_btn_cancel_countdown, secondsRemaining + 1)
            }

            override fun onFinish() {
                negativeButton.setText(R.string.dialog_btn_cancel)
                negativeButton.isEnabled = true
                warningText.isVisible = true
            }
        }

        negativeButton.setOnClickListener {
            progressDialog.dismiss()
            onDialogClose()
        }

        countDownTimer.start()
        return progressDialog
    }

    private fun showEmailDialog() {
        val emailProviders = EmailUtils.getEmailProviders(this)
        val adapter = EmailProviderAdapter(this, emailProviders)

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_title_pick_email_client)
            .setAdapter(adapter) { _: DialogInterface, which: Int ->
                val chosenProvider = emailProviders[which]
                EmailUtils.openEmailApp(this, chosenProvider)
            }
            .setNegativeButton(R.string.dialog_btn_cancel, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ic_menu_request_icons, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return false
            }

            R.id.menu_select_all -> {
                adapter?.toggleAllIconsCheckedState()
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }
}