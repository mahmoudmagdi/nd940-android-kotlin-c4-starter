package com.udacity.project4.locationreminders.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminders.geofence.GeofenceTransitionsJobIntentService.Companion.enqueueWork
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment.Companion.ACTION_GEOFENCE_EVENT
import com.udacity.project4.utils.errorMessage

/**
 * Triggered by the Geofence.  Since we can have many Geofences at once, we pull the request
 * ID from the first Geofence, and locate it within the cached data in our Room DB
 *
 * Or users can add the reminders and then close the app, So our app has to run in the background
 * and handle the geofencing in the background.
 * To do that you can use https://developer.android.com/reference/android/support/v4/app/JobIntentService to do that.
 *
 */
// TODO: implement the onReceive method to receive the geofencing events at the background

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = GeofenceBroadcastReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {

        Log.e("onReceive", "onReceive")

        //implement the onReceive method to receive the geofencing events at the background
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geoFenceEvent = GeofencingEvent.fromIntent(intent)

            if (geoFenceEvent.hasError()) {
                val errorMessage = errorMessage(context, geoFenceEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }

            enqueueWork(context, intent)
        }
    }
}