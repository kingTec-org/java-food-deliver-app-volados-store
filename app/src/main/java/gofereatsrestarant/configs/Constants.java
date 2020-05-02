package gofereatsrestarant.configs;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage configs
 * @category Constants
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;

/*****************************************************************
 Constants
 ****************************************************************/
public class Constants {

    // Test server IP address Full
    public static final String CLIENT_ID = "14fa50fa2ea44c408fb1134ebc1c2721";
    public static final String REDIRECT_URL = "http://www.gofereatsrestarant.com";

    public static final String APP_NAME = "GoferEats Restaurant";
    public static final String FILE_NAME = "GoferEats Restaurant";
    public static final String CAMERA_ROLL = "Camera Roll";
    public static final String ALL_IMAGES = "All Images";
    public static final String ALL_VIDEOS = "All Videos";
    public static final String[] SUPPORTED_VIDEO_FORMAT = {"MP4", "3GP", "AVI", "FLV", "MPEG-2"};

    public static final String VP_LOGIN_SLIDER = "Login_Slider";

    public static final String STATUS_MSG = "status_message";
    public static final String STATUS_CODE = "status_code";
    public static final String OTP = "otp";
    public static final String TOKEN = "token";
    public static final String ALREADY_USER = "already_user";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_ACCESS_TOKEN = "refresh_token";
    public static final String RESTAURANT_NAME = "store_name";
    public static final String TOT_SUPER_LIKE = "tot_super_likes_per_day";
    public static final String REMAINING_SUPER_LIKE = "remaining_likes_count";
    public static final String REMAINING_SUPER_LIKE_HOURS = "remaining_likes_hours";
    public static final String TOT_BOOST = "tot_boost_count_per_month";
    public static final String REMAINING_BOOST = "remaining_boost_count";
    public static final String REMAINING_BOOST_DAY = "remaining_boost_days";
    public static final String REMAINING_BOOST_HOURS = "remaining_boost_hours";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final int REQUEST_CODE_GALLERY = 5;

    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            //Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final String[] PERMISSIONS_PHOTO = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    public static final String[] PERMISSIONS_PHONE_STATE = {
            //Manifest.permission.READ_PHONE_STATE,
    };
    public static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final TimeZone utcTZ = TimeZone.getTimeZone("UTC");
    public static final SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    public static final SimpleDateFormat chatUtcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    public static final SimpleDateFormat galleryDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
    public static final String MUTE_NOTIFICATION = "Mute\nNotification";
    public static final String REPORT = "Report";
    public static final String UNMATCH = "Unmatch";
    public static final String[] QUICK_MENU_TITLE = {UNMATCH};
    public static final String listenerSignUpException = "must implement SignUpActivityListener";
    public static final String listenerHomeException = "must implement ActivityListener";
    public static final String imageUrl = "http://smash.trioangle.com/gofereatsrestarant/public/image/users/%s/%s";
    public static final String userName = "http://www.";
    public static long mLastClickTime = 0;
    public static boolean isDisableFragmentAnimations = false;
}
