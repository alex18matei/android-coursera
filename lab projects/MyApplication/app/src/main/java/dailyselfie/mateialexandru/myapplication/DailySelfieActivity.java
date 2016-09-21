package dailyselfie.mateialexandru.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailySelfieActivity extends FragmentActivity implements SelectionListener {


    static final String TAG = "Daily-Selfie";
    static final String TAG_PHOTO_LIST_FRAGMENT = "photo_list_fragment";
    static final String TAG_SINGLE_PHOTO_FRAGMENT = "single_photo_fragment";
    static final String TAG_PHOTO_FRAGMENT = "photo_fragment";

    private FragmentManager mFragmentManager;
    private PhotoListFragment mPhotoListFragment;
    private PhotoFragment mPhotoFragment;

    private int mHour;
    private int mMinute;
    private int mTempHour;
    private int mTempMinute;

    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;

    private SharedPreferences mSharedPreferences;

    private static final long ALARM_DELAY = 24 * 60 * 60 * 1000L;

    public void setHM(int hour, int minute) {
        this.mHour = hour;
        this.mMinute = minute;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        if (null != savedInstanceState) {
            restoreState(savedInstanceState);
        } else {
            installPhotosFragment();
        }

        mSharedPreferences = getSharedPreferences("selfies", MODE_PRIVATE);

        mTempHour = mSharedPreferences.getInt("hour", 9);
        mTempMinute = mSharedPreferences.getInt("minute", 10);

        Log.i(TAG, " alarm::: " + mTempHour + ":" + mTempMinute);

        startAlarmForNotification();

    }

    // Restore saved instance state
    private void restoreState(Bundle savedInstanceState) {

        // Fragments tags were saved in onSavedInstanceState
        mPhotoListFragment = (PhotoListFragment) mFragmentManager
                .findFragmentByTag(savedInstanceState
                        .getString(TAG_PHOTO_LIST_FRAGMENT));

        mPhotoFragment = (PhotoFragment) mFragmentManager
                .findFragmentByTag(savedInstanceState
                        .getString(TAG_PHOTO_FRAGMENT));

    }

    private void installPhotosFragment() {

        // Make new Fragment
        mPhotoListFragment = new PhotoListFragment();
        mPhotoFragment = new PhotoFragment();

        // Give Fragment to the FragmentManager
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, mPhotoListFragment,
                TAG_PHOTO_LIST_FRAGMENT);
        transaction.commit();

    }

    @Override
    public void onItemSelected(int position) {
        installSinglePhotoFragment(position);
    }

    // Add FeedFragment to Activity
    private void installSinglePhotoFragment(int position) {

        // Make new Fragment
        mPhotoFragment = new PhotoFragment();

        // Set Fragment arguments
        Bundle args = new Bundle();

        ArrayList<String> selfiesPath = getAllPhotoPaths();
        args.putStringArrayList(TAG_PHOTO_FRAGMENT, selfiesPath);
        args.putInt("position", position);

        mPhotoFragment.setArguments(args);

        // Give Fragment to the FragmentManager
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, mPhotoFragment,
                TAG_PHOTO_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public ArrayList<String> getAllPhotoPaths(){
        ArrayList<SelfieImage> list = ((PhotoViewAdapter) mPhotoListFragment.getListAdapter()).getList();
        ArrayList<String> selfiesPath = new ArrayList<>();
        for (SelfieImage selfie : list) {
            selfiesPath.add(selfie.getmPhotoURI());
        }
        return selfiesPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (null != mPhotoListFragment) {
            savedInstanceState.putString(TAG_PHOTO_LIST_FRAGMENT,
                    mPhotoListFragment.getTag());
        }
        if (null != mPhotoFragment) {
            savedInstanceState.putString(TAG_PHOTO_FRAGMENT,
                    mPhotoFragment.getTag());
        }

        super.onSaveInstanceState(savedInstanceState);

    }

    public void startAlarmForNotification() {

        if( mHour == 0 && mMinute == 0){
            mHour = mTempHour;
            mMinute = mTempMinute;

            setAlarm();
            Calendar calendar = setCalendar(mHour, mMinute);

            startAlarm(calendar);
            saveAlarm();

        }else if (mHour != mTempHour || mMinute != mTempMinute) {

            setAlarm();
            Calendar calendar = setCalendar(mHour, mMinute);

            startAlarm(calendar);
            saveAlarm();

            displayToastAlarmMessage();

        }
        else{
            displayToastAlarmMessage();
        }
    }

    public void setAlarm(){
        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(
                DailySelfieActivity.this,
                AlarmNotificationReceiver.class
        );
        mAlarmManager.cancel(mNotificationReceiverPendingIntent);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                DailySelfieActivity.this, 0, mNotificationReceiverIntent, 0);
    }

    public Calendar setCalendar(int hour, int minute){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Log.i(TAG, "Calendar :::::" + new Date(calendar.getTimeInMillis()));
        Log.i(TAG, "Time ::::" + new Date(System.currentTimeMillis()));

        return calendar;
    }

    private void startAlarm(Calendar calendar){
        // Set up repeating Alarm
        if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    ALARM_DELAY,
                    mNotificationReceiverPendingIntent);
        } else {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + ALARM_DELAY,
                    ALARM_DELAY,
                    mNotificationReceiverPendingIntent);
        }
    }

    private void saveAlarm(){

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("hour", mHour);
        editor.putInt("minute", mMinute);
        editor.commit();

    }

    private void displayToastAlarmMessage(){
        if (mHour != 9 || mMinute != 10) {
            if (mMinute < 10) {
                Toast.makeText(DailySelfieActivity.this, "Alarm at " + mHour + ":0" + mMinute, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DailySelfieActivity.this, "Alarm at " + mHour + ":" + mMinute, Toast.LENGTH_SHORT).show();
            }
        }
    }



}
