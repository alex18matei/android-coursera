package dailyselfie.mateialexandru.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;


public class SinglePhotoFragment extends Fragment {

    private ImageView mImageView;
    private String photoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getActivity().getActionBar().hide();


        return inflater.inflate(R.layout.single_photo_fragment, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        //setHasOptionsMenu(false);
    }

    @Override
    public void onResume() {

        setHasOptionsMenu(true);

        mImageView = (ImageView) getView()
                .findViewById(R.id.single_photo);

        Bundle bundle = getArguments();

        photoUri = bundle
                .getString(MainActivity.TAG_SINGLE_PHOTO_FRAGMENT);



        Log.i(SinglePhotoFragment.class.toString(), "a fost creat::::::::");

        super.onResume();
        if (photoUri != null) {
            mImageView.post(new Runnable() {
                @Override
                public void run() {
                    int targetH = mImageView.getHeight();
                    int targetW = mImageView.getWidth();
                    mImageView.setImageBitmap(
                            decodeFile(photoUri,
                                    targetW,
                                    targetH));

                }
            });
        }
    }


    public Bitmap decodeFile(String path, int targetW, int targetH) {
        try {

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
        inflater.inflate(R.menu.single_photo_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:

                share();
                // return value true indicates that the menu click has been handled
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        String uri = photoUri;
        Uri photoUri = Uri.fromFile(new File(uri));

        share.putExtra(Intent.EXTRA_STREAM, photoUri);

        startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
    }


}

