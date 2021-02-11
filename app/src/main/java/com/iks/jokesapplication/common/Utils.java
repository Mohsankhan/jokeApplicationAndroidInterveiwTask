package com.iks.jokesapplication.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.iks.jokesapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Utils {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String twoHyphens = "--";
    private static final String lineEnd = "\r\n";
    private static final String boundary = "apiclient-" + System.currentTimeMillis();

    public static boolean isValidEmail(String emailAddress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    public static boolean isValidNickName(String nick) {
        if (nick == null || nick.length() < JokesConstants.NICK_NAME_MIN_LENGTH || nick.length() > JokesConstants.NICK_NAME_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    public static boolean isValidVerificationCode(String code) {
        if (code == null || code.length() != JokesConstants.PASSWORD_MIN_LENGTH) {
            return false;
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < JokesConstants.PASSWORD_MIN_LENGTH || password.length() > JokesConstants.PASSWORD_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    public static String getDeviceLanguageCode() {
        return Locale.getDefault().getLanguage();
    }

    public static String getCurrentUTCDateTimeAsString() {
        Date dateTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateTime = sdf.format(dateTime);
        return currentDateTime;
    }

    public static SimpleDateFormat getUTCDateTimeFormat() {
        SimpleDateFormat simpleDateFormat = getDateTimeFormat();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    public static SimpleDateFormat getDateTimeFormat() {
        return new SimpleDateFormat(DATE_TIME_FORMAT);
    }

    public static String getAppVersion(Context context) {
        String appVersion = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    public static String getDateFormatMonthAndDateSeparated(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM-dd");
        String month_name = "";
        try {
            Date date1 = getUTCDateTimeFormat().parse(date);
            month_name = fmt.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month_name;

    }

    public static Date getDateFromString(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date1 = null;
        try {
            date1 = getUTCDateTimeFormat().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static Date getDateWithUTC(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_TIME_FORMAT);
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = null;
        try {
            date1 = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static String getDateFormatWeekOfDayAndMonth(String date) {

        SimpleDateFormat fmt = new SimpleDateFormat("EEEE, MMMM, dd @ HH:mm");
        String month_name = "";
        try {
            if (date != null && date.length() > 0) {
                Date date1 = getUTCDateTimeFormat().parse(date);
                month_name = fmt.format(date1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month_name;
    }

    public static String getDateInStringUTCFormat(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_TIME_FORMAT);
        return fmt.format(date);
    }

    public static int distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        double distanceInKiloMeters = distanceInKiloMeters(lat1, lon1, lat2, lon2);
        return (int) (distanceInKiloMeters * 1000);
    }

    public static double distanceInKiloMeters(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static Integer getDollarFromValue(double value) {
        int dollars = (int) value;
        return dollars;
    }

    public static String getFormattedNumber(double number) {
        int value = (int) number;
        return NumberFormat.getInstance(Locale.FRANCE).format(value);
    }

    public static String getCentFromValue(double value) {
        int dollars = (int) value;
        int cents = (int) ((value - dollars) * 100);
        return String.format("%02d", cents);

    }

    public static String getDateFormatMonthAndDate(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM d");
        String month_name = "";
        try {
            Date date1 = getUTCDateTimeFormat().parse(date);
            month_name = fmt.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month_name;
    }

    public static String getDateFormatMonthAndDate(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String month_name = "";
        try {
            Date date1 = getUTCDateTimeFormat().parse(String.valueOf(date));
            month_name = fmt.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month_name;
    }

    public static String getDateFormatMonthAndDateForOffer(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("d MMMM");
        String month_name = "";
        try {
            Date date1 = getUTCDateTimeFormat().parse(date);
            month_name = fmt.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month_name;
    }

    public static String getDateFormatForRewardList(String date) {
        SimpleDateFormat fmt = new SimpleDateFormat("d MMMM yyyy");
        String month_name = "";
        try {
            Date date1 = getUTCDateTimeFormat().parse(date);
            month_name = fmt.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month_name;
    }

    public static boolean isStringOnlyAlphabet(String str) {
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^[a-zA-Z]*$")));
    }

    public static String getCurrentMonthLastDate() {
        Calendar cal = Calendar.getInstance();
        int lastDate = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DATE, lastDate);
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM dd");
        String month_name = month_date.format(cal.getTime());
        try {
            Date date = getUTCDateTimeFormat().parse(month_name);
            month_name = month_date.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month_name;
    }

    public static void strikeThroughText(TextView price) {
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void setViewClickable(View view, Boolean isEnable) {
        view.setClickable(isEnable);
        view.setEnabled(isEnable);
    }

    public static String getCurrentTime(long currentTime) {
        Date date = new Date(currentTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String time = sdf.format(cal.getTime());
        return time;
    }

    public static String getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM yyyy");
        String month_name = month_date.format(cal.getTime());
        Log.i("TAG", "getCurrentMonth: " + month_name);
        return month_name;
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd MMMM yyyy");
        String date_name = date.format(cal.getTime());
        return date_name;
    }

    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar specifiedDate = Calendar.getInstance();
        specifiedDate.setTime(date);

        return today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH)
                && today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH)
                && today.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR);
    }

    public static Bitmap getRoundedCornerImage(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String passwordMd5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }



    public static void hideSoftKeyBoard(View view, Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static void showInternetError(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.internet_error_dialog_message));
        builder.setCancelable(false);
    /*    builder.setPositiveButton(context.getString(R.string.dialog_button_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                context.startActivity(intent);
            }
        });*/
        AlertDialog internetAlertDialog = builder.create();
        internetAlertDialog.show();
    }
}