package dailyselfie.mateialexandru.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PhotoFragment extends Fragment {

    private ViewPager mViewPager;
    private int mPosition;
    ArrayList<String> mSelfiesPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.photo_fragment, container, false);
        //getActivity().getActionBar().hide();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewPager = (ViewPager) getView()
                .findViewById(R.id.pager);
        Bundle bundle = getArguments();

        mSelfiesPath = (ArrayList<String>) bundle.getStringArrayList(MainActivity.TAG_PHOTO_FRAGMENT);

        mPosition = bundle.getInt("position");

        if (mViewPager.getAdapter() == null) {

            final CollectionPagerAdapter adapter = new CollectionPagerAdapter(getFragmentManager(), mSelfiesPath);

            mViewPager.setAdapter(adapter);

            Log.i(PhotoFragment.class.toString(), " a fost creat " + mSelfiesPath.get(mPosition));

            mViewPager.setCurrentItem(mPosition);
        }

    }

}
