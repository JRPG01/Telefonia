package com.example.respuestaautomaticas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.SignalStrength
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService

class BroadcastReciver: BroadcastReceiver() {
    private var mListener: ServiceStateListener? = null
    private var mTelephonyManager: TelephonyManager? = null
    private var mContext: Context? = null

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        mContext = context

        if (action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            mTelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            Toast.makeText(mContext, "¡Receptor registrado!", Toast.LENGTH_LONG).show()
            mListener = ServiceStateListener()
            mTelephonyManager?.listen(mListener, PhoneStateListener.LISTEN_SERVICE_STATE)
            mTelephonyManager?.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        }
        Log.d("Llamada detectada","Se detecto")
    }

    private inner class ServiceStateListener : PhoneStateListener() {
        override fun onServiceStateChanged(serviceState: ServiceState) {
            super.onServiceStateChanged(serviceState)
            val connected = serviceState.state == ServiceState.STATE_IN_SERVICE
            if (connected) {
                Toast.makeText(mContext, "¡Conexión establecida!", Toast.LENGTH_LONG).show()
                // Aquí puedes manejar la lógica para reintentar los SMS
            } else {
                Toast.makeText(mContext, "¡Conexión perdida!", Toast.LENGTH_LONG).show()
            }
        }

        override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
            super.onSignalStrengthsChanged(signalStrength)
            Toast.makeText(
                mContext,
                "Señal cambiada - CDMA: ${signalStrength.cdmaDbm} GSM: ${signalStrength.gsmSignalStrength}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
