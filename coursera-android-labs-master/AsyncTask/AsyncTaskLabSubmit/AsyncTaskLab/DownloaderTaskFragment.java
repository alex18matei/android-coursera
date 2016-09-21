package course.labs.asynctasklab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class DownloaderTaskFragment extends Fragment {

    private DownloaderTask downloaderTask;
    private DownloadFinishedListener mCallback;
    private Context mContext;

    @SuppressWarnings("unused")
    private static final String TAG = "Lab-Threads";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Preserve across reconfigurations
        setRetainInstance(true);

        // Create new DownloaderTask that "downloads" data
        downloaderTask = new DownloaderTask();


        // Retrieve arguments from DownloaderTaskFragment
        // Prepare them for use with DownloaderTask.
        Bundle bundle = this.getArguments();
        ArrayList<Integer> resIDs = bundle.getIntegerArrayList(MainActivity.TAG_FRIEND_RES_IDS);

        Log.i("CHECK::::::::::::::::", "" + resIDs.size());

        //Start the DownloaderTask
        //noinspection unchecked
        downloaderTask.execute(resIDs);


    }

    // Assign current hosting Activity to mCallback
    // Store application context for use by downloadTweets()
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity.getApplicationContext();

        // Make sure that the hosting activity has implemented
        // the correct callback interface.
        try {
            mCallback = (DownloadFinishedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DownloadFinishedListener");
        }
    }

    // Null out mCallback
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    // Implement an AsyncTask subclass called DownLoaderTask.
    // This class must use the downloadTweets method (currently commented
    // out). Ultimately, it must also pass newly available data back to
    // the hosting Activity using the DownloadFinishedListener interface.

    public class DownloaderTask extends AsyncTask<ArrayList<Integer>, Integer, String[]> {


        // Simulates downloading Twitter data from the network


        private String[] downloadTweets(Integer resourceIDS[]) {
            final int simulatedDelay = 1000;
            String[] feeds = new String[resourceIDS.length];
            try {
                for (int idx = 0; idx < resourceIDS.length; idx++) {
                    InputStream inputStream;
                    BufferedReader in;
                    try {
                        // Pretend downloading takes a long time
                        Thread.sleep(simulatedDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    inputStream = mContext.getResources().openRawResource(
                            resourceIDS[idx]);
                    in = new BufferedReader(new InputStreamReader(inputStream));

                    String readLine;
                    StringBuffer buf = new StringBuffer();

                    while ((readLine = in.readLine()) != null) {
                        buf.append(readLine);
                    }

                    feeds[idx] = buf.toString();

                    if (null != in) {
                        in.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return feeds;
        }


        @Override
        protected String[] doInBackground(ArrayList<Integer>... integers) {

            ArrayList<Integer> data = integers[0];
            Integer[] dataSet = new Integer[data.size()];
            for ( int i = 0; i < data.size(); ++i){
                dataSet[i] = data.get(i);
            }
            return downloadTweets(dataSet);

        }

        @Override
        protected void onPostExecute(String[] strings) {
            mCallback.notifyDataRefreshed(strings);
        }

    }

}