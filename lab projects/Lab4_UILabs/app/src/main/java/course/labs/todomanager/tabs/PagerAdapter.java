package course.labs.todomanager.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class PagerAdapter extends FragmentPagerAdapter {
    String[] tabTitles = {"Priority", "Date", "Not Done"};
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                Log.i("fragment::::::::::", "tab fragment 1 created");
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                Log.i("fragment::::::::::", "tab fragment 2 created");
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                Log.i("fragment::::::::::", "tab fragment 3 created");
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {

        return mNumOfTabs;
    }
}