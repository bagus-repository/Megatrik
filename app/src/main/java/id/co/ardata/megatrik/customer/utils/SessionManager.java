package id.co.ardata.megatrik.customer.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MegatrikCustomerSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_FIRST_TIME = "isFirstTime";

    private static final String KEY_ACCESS_TOKEN = "user_access_token";
    private static final String KEY_APPLICATION_ACCESS_TOKEN = "";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_OS_PLAYER_ID = "user_os_player_id";
    private static final String KEY_USER_IMG_PROFILE = "user_img";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsLogin(boolean isLogin){
        editor.putBoolean(KEY_IS_LOGGED_IN, isLogin);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setAccessToken(String accessToken){
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public String getAccessToken(){
        return pref.getString(KEY_ACCESS_TOKEN, null);
    }

    public void setUserId(String userId){
        editor.putString(KEY_USER_ID, userId);
        editor.commit();
    }

    public String getUserId(){
        return pref.getString(KEY_USER_ID, null);
    }

    public void setUserEmail(String userEmail){
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.commit();
    }

    public String getUserEmail(){
        return pref.getString(KEY_USER_EMAIL, null);
    }

    public void setUserName(String userName){
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String getUserName(){
        return pref.getString(KEY_USER_NAME, null);
    }

    public void setUserOsPlayerId(String userOsPlayerId){
        editor.putString(KEY_USER_OS_PLAYER_ID, userOsPlayerId);
        editor.commit();
    }

    public String getUserOsPlayerId(){
        return pref.getString(KEY_USER_OS_PLAYER_ID, null);
    }

    public void setUserImgProfile(String userImg){
        editor.putString(KEY_USER_IMG_PROFILE, userImg);
        editor.commit();
    }

    public String getUserImgProfile(){
        return pref.getString(KEY_USER_IMG_PROFILE, null);
    }

    public void setApplicationAccessToken(String token){
        editor.putString(KEY_APPLICATION_ACCESS_TOKEN, token);
        editor.commit();
    }

    public String getApplicationAccessToken(){
        return pref.getString(KEY_APPLICATION_ACCESS_TOKEN, null);
    }

    public void setIsFirstTime(boolean value){
        editor.putBoolean(KEY_IS_FIRST_TIME, value);
        editor.commit();
    }

    public boolean getIsFirstTime(){
        return pref.getBoolean(KEY_IS_FIRST_TIME, true);
    }

    public void setUserPhone(String phone){
        editor.putString(KEY_USER_PHONE, phone);
        editor.commit();
    }

    public String getUserPhone(){
        return pref.getString(KEY_USER_PHONE, null);
    }

    public void resetProfile(){
        setIsLogin(false);
        setAccessToken(null);
        setUserEmail(null);
        setUserName(null);
        setUserId(null);
        setUserImgProfile(null);
        setUserPhone(null);
        setApplicationAccessToken(null);
    }
}
