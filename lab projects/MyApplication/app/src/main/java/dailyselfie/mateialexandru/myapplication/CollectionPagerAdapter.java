package dailyselfie.mateialexandru.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> photoList;

    public CollectionPagerAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        photoList = list;
    }

    @Override
    public Fragment getItem(int position) {

        Log.i("Aici intru vreodata ? ", "da");

        Fragment fragment = new SinglePhotoFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.TAG_SINGLE_PHOTO_FRAGMENT, photoList.get(position) );
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public int getCount() {
        return photoList.size();
    }
}
