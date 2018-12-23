package id.co.ardata.megatrik.customer;

import android.app.Application;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import id.co.ardata.megatrik.customer.utils.NotificationOpenedHandler;
import id.co.ardata.megatrik.customer.utils.SessionManager;

public class MyApplication extends Application {

    SessionManager sessionManager;
    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        sessionManager = new SessionManager(this);
        updateOsPlayerId();
    }

    private void updateOsPlayerId() {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        boolean isEnabled = status.getPermissionStatus().getEnabled();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
        if (isEnabled && isSubscribed){
            String userID = status.getSubscriptionStatus().getUserId();
            sessionManager.setUserOsPlayerId(userID);
        }
    }
}
