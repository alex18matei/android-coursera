package dailyselfie.mateialexandru.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class PhotoListFragment extends ListFragment {

    private SelectionListener mCallback;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private PhotoViewAdapter mAdapter;
    static final String TAG = "Daily-Selfie";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new PhotoViewAdapter(getActivity().getApplicationContext(), R.layout.photo_item_view);
        setListAdapter(mAdapter);

        setHasOptionsMenu(true);

        refreshListView();

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        getListView().setMultiChoiceModeListener(
                new ListViewMultiChoiceListener(getListView(), mAdapter)
        );
    }

    private void refreshListView() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MySelfie");

        dir.mkdirs();
        mAdapter.removeAllViews();

        File[] files = dir
                .listFiles();

        for (File file : files) {
            SelfieImage record = new SelfieImage();
            record.setmPhotoURI(file.getAbsolutePath());
            record.setPhotoNameFromURI();
            Bitmap bitmap = decodeFile(record.getmPhotoURI());
            if (bitmap != null)
                Log.i(TAG, "::::nu este null");
            record.setmPhotoBitmap(bitmap);

            mAdapter.add(record);
        }

        sortAdapterList();

    }

    private void sortAdapterList() {

        Collections.sort(mAdapter.getList(), new Comparator<SelfieImage>() {
            @Override
            public int compare(SelfieImage obj1, SelfieImage obj2) {
                if (obj2.getmPhotoName().compareTo(obj1.getmPhotoName()) > 0)
                    return 1;
                else if (obj2.getmPhotoName().compareTo(obj1.getmPhotoName()) < 0)
                    return -1;
                else
                    return 0;
            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();
        registerForContextMenu(getListView());

        //sortAdapterList();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the hosting Activity has implemented
        // the callback interface.
        try {
            mCallback = (SelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectionListener");
        }

    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        // Inform hosting Activity of user's selection
        if (null != mCallback) {
            mCallback.onItemSelected(position);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(getActivity(),
//                        "dailyselfie.mateialexandru.myapplication.fileprovider",
//                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date());
        Log.i(TAG, "::::date: " + timeStamp);
        String imageFileName = timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MySelfie");
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
        File image = new File(storageDir, imageFileName + ".jpg");
        image.createNewFile();

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(TAG, "::::" + mCurrentPhotoPath);
        Log.e(TAG, "create ImageFile: " + image.getAbsolutePath());
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO) {
            Log.i(TAG, ":::::::::am intrat aici");
            if (resultCode == Activity.RESULT_OK) {
                //if (data != null) {
                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (mCurrentPhotoPath != null) {

                    Bitmap imageBitmap = decodeFile(mCurrentPhotoPath);

                    SelfieImage record = new SelfieImage();
                    record.setmPhotoURI(mCurrentPhotoPath);
                    record.setmPhotoBitmap(imageBitmap);
                    record.setPhotoNameFromURI();

                    mAdapter.addAtPosition(record, 0);

                    galleryAddPic();

                    Log.i(TAG, mCurrentPhotoPath);
                }

            } else {
                deleteFile(mCurrentPhotoPath);
            }

        }

    }

    private void galleryAddPic() {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        Log.i(TAG, "URI ::::::::::" + contentUri);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);

    }


    public Bitmap decodeFile(String path) {
        try {

            // Get the dimensions of the View
            /*int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();*/

            int targetW = 100;
            int targetH = 100;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            //

            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
            return bitmap;

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu using activity_menu.xml
        menu.clear();
        inflater.inflate(R.menu.activity_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_menu_item:

                dispatchTakePictureIntent();

                // return value true indicates that the menu click has been handled
                return true;

            case R.id.alarm_item:
                showTimePickerDialog();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        private Calendar c;
        private int mHour;
        private int mMinute;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, mHour, mMinute, true);
        }

        //method from interface TimePickerDialog.OnTimeSetListener
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ((DailySelfieActivity) getActivity()).setHM(hourOfDay, minute);
            ((DailySelfieActivity) getActivity()).startAlarmForNotification();

            //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        }

    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setCancelable(true);
        newFragment.show(getActivity().getFragmentManager(), "timePicker");

    }




    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        Log.i("Context-Menu", item.toString());
        switch (item.getItemId()) {
            case R.id.delete:
                Log.i("Context-Menu", "Delete button pressed");
                deleteItem(info.position);
                return true;
            default:
                Log.i("Context-Menu", "Altceva");
                return super.onContextItemSelected(item);
        }
    }

    private void deleteItem(int position) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_delete_message)
                .setTitle(R.string.dialog_delete_title);

        // Add the buttons
        final int finalPosition = position;
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                SelfieImage photo = (SelfieImage) mAdapter.getItem(finalPosition);

                deleteFile(photo.getmPhotoURI());

                mAdapter.removeItem(finalPosition);
                mAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "You removed the selected item", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();

    }
    */

    private void deleteFile(String photo) {

        File file = new File(photo);
        file.delete();

    }
}
