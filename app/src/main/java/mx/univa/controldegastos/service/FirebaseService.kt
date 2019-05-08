package mx.univa.controldegastos.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mx.univa.controldegastos.PrincipalMain
import mx.univa.controldegastos.R
import mx.univa.controldegastos.resourses.globales
import java.lang.Exception


class FirebaseService : FirebaseMessagingService() {

    val TAG ="Firebase: "

    var notificacion : NotificationCompat.Builder = NotificationCompat.Builder(this)

    private var notificationManager: NotificationManager? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            /*if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }*/

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)

            var notificationIntent : Intent = Intent(this, PrincipalMain::class.java)

            /*if(PrincipalMain.isAppRunning){
                //Some action
            }else{
                //Show notification as usual
            }*/

            //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            var pendingIntent : PendingIntent = PendingIntent.getActivity(this,0 /* Request code */, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

            notificationManager =
                getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelID = "mx.univa.controldegastos"
            var likeIntent : Intent = Intent(this, PrincipalMain::class.java)
            var likePendingIntent = PendingIntent.getService(this, 1, likeIntent,PendingIntent.FLAG_ONE_SHOT)

            notificacion.setSmallIcon(R.drawable.ic_attach_money_black_24dp)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setContentTitle( remoteMessage.notification!!.title)
                .setContentText( remoteMessage.notification!!.body!!)
                .setChannelId(channelID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setAutoCancel(true)
                .addAction(R.drawable.ic_menu_camera,
                    getString(R.string.title_activity_principal_main),likePendingIntent)
                .setContentIntent(pendingIntent)
            notificationManager?.notify(600, notificacion.build())

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token)

        validaEstatusTokenSesion(pToken = token)
    }


    fun validaEstatusTokenSesion(pToken : String){
        var preferences : SharedPreferences = getSharedPreferences(globales.STRING_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putString(globales.PREFERENCE_TOKEN_FIREBASE, pToken).apply()



        if(preferences.getBoolean(globales.PREFERENCE_ESTADO_SESION, false)){

            try{
                var principal : PrincipalMain = PrincipalMain()
                principal.actualizaTokenFirebase(pToken)
            }catch(ex: Exception){
                Log.i(TAG, ex.message)
            }
        }
    }
}
