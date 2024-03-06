package com.example.dvtweatherapp.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface


class ErrorHandlerUtil() {

    fun connectionError(throwable: Throwable, context: Context) {
        var alert: Dialog? = null
        val showing = alert?.isShowing ?: false
        if (showing)
            return
        val message = throwable.toString()

        val title: String
        val content: String
        when {
            message.contains("java.net.UnknownHostException", true) -> {
                title = "Internet Not Available"
                content =
                    "Could not connect to the Internet. Please verify that you are connected and try again"
            }

            message.contains("java.net.SocketTimeoutException", true) -> {
                title = "Connection Timeout"
                content =
                    "Server took too long to respond. This may be caused by a bad network connection"
            }

            message.contains("javax.net.ssl.SSLPeerUnverifiedException", true) -> {
                title = "SSL Cert. Unverified"
                content = "Hostname not verified"
            }

            else -> {
                title = "Unknown Error"
                content = "An Unknown error has occurred. Please try again later"
            }
        }

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(content)
            .setCancelable(true)
            .setNegativeButton("Go Offline") { dialog: DialogInterface?, _: Int ->
                //pull from database
                dialog?.dismiss()
            }
            .setPositiveButton("Retry") { dialog: DialogInterface?, _: Int ->
                //getHistory()
                dialog?.dismiss()
            }

        alert = builder.create()
        alert?.show()
    }
}