package course.labs.locationlab;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class PlaceViewActivity extends ListActivity implements LocationListener {
    private static final long FIVE_MINS = 5 * 60 * 1000;
    private static final String TAG = "Lab-Location";

    // False if you don't have network access
    public static boolean sHasNetwork = false;

    private Location mLastLocationReading;
    private PlaceViewAdapter mAdapter;
    private LocationManager mLocationManager;
    private boolean mMockLocationOn = false;

    // default minimum time between new readings
    private long mMinTime = 5000;

    // default minimum distance between old and new readings.
    private float mMinDistance = 1000.0f;

    // A fake location provider used for testing
    private MockLocationProvider mMockLocationProvider;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_DISTANCE = 10.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the app's user interface. This class is a ListActivity,
        // so it has its own ListView. ListView's adapter should be a PlaceViewAdapter

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ListView placesListView = getListView();

        // TODO - add a footerView to the ListView
        // You can use footer_view.xml to define the footer
        TextView footerView = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.footer_view, null);
        placesListView.addFooterView(footerView);
        mAdapter = new PlaceViewAdapter(getApplicationContext());
        setListAdapter(mAdapter);

        // TODO - footerView must respond to user clicks, handling 3 cases:

        mLastLocationReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MINS);

        // There is no current location - response is up to you. The best
        // solution is to always disable the footerView until you have a
        // location.
        if (mLastLocationReading == null) {
            footerView.setEnabled(false);
        }

        // There is a current location, but the user has already acquired a
        // PlaceBadge for this location - issue a Toast message with the text -
        // "You already have this location badge."
        // Use the PlaceRecord class' intersects() method to determine whether
        // a PlaceBadge already exists for a given location
        else {


            // There is a current location for which the user does not already have
            // a PlaceBadge. In this case download the information needed to make a new
            // PlaceBadge.


            footerView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (mAdapter.intersects(mLastLocationReading)) {

                        Toast.makeText(getApplicationContext(),
                                "You already have this location badge.",
                                Toast.LENGTH_SHORT)
                                .show();

                    } else {

                        PlaceDownloaderTask placeDownloaderTask = new PlaceDownloaderTask(PlaceViewActivity.this, true);
                        placeDownloaderTask.execute(mLastLocationReading);

                    }
                }

            });

        }

    }

    private Location bestLastKnownLocation(float minAccuracy, long maxAge) {

        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestAge = Long.MIN_VALUE;

        List<String> matchingProviders = mLocationManager.getAllProviders();

        for (String provider : matchingProviders) {

            Location location = mLocationManager.getLastKnownLocation(provider);

            if (location != null) {

                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if (accuracy < bestAccuracy) {

                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestAge = time;

                }
            }
        }

        // Return best reading or null
        if (bestAccuracy > minAccuracy
                || (System.currentTimeMillis() - bestAge) > maxAge) {
            return null;
        } else {
            return bestResult;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        startMockLocationManager();

        // TODO - Check NETWORK_PROVIDER for an existing location reading.
        // Only keep this last reading if it is fresh - less than 5 minutes old


        mLastLocationReading = mLocationManager
                .getLastKnownLocation(NETWORK_PROVIDER);
        if (mLastLocationReading != null &&
                mLastLocationReading.getTime() > System.currentTimeMillis()
                        - FIVE_MINS) {
            mLastLocationReading = null;
        }


        // TODO - register to receive location updates from NETWORK_PROVIDER
        if (null != mLocationManager
                .getProvider(NETWORK_PROVIDER)) {
            mLocationManager.requestLocationUpdates(
                    NETWORK_PROVIDER, mMinTime,
                    mMinDistance, this);
        }

    }

    @Override
    protected void onPause() {

        // TODO - unregister for location updates
        mLocationManager.removeUpdates(this);

        shutdownMockLocationManager();
        super.onPause();
    }

    // Callback method used by PlaceDownloaderTask
    public void addNewPlace(PlaceRecord place) {

        // TODO - Attempt to add place to the adapter, considering the following cases

        // A PlaceBadge for this location already exists - issue a Toast message
        // with the text - "You already have this location badge." Use the PlaceRecord
        // class' intersects() method to determine whether a PlaceBadge already exists
        // for a given location. Do not add the PlaceBadge to the adapter

        // The place is null - issue a Toast message with the text
        // "PlaceBadge could not be acquired"
        // Do not add the PlaceBadge to the adapter

        // The place has no country name - issue a Toast message
        // with the text - "There is no country at this location".
        // Do not add the PlaceBadge to the adapter

        // Otherwise - add the PlaceBadge to the adapter

        if(place == null){
            Toast.makeText(PlaceViewActivity.this, "PlaceBadge could not be acquired", Toast.LENGTH_SHORT).show();
        }
        else if (mAdapter.intersects(place.getLocation())){
            Toast.makeText(PlaceViewActivity.this, "You already have this location badge.", Toast.LENGTH_SHORT).show();
        }
        else if(place.getCountryName().isEmpty()){
            Toast.makeText(PlaceViewActivity.this, "There is no country at this location", Toast.LENGTH_SHORT).show();
        }
        else {
            mAdapter.add(place);
        }
    }

    // LocationListener methods
    @Override
    public void onLocationChanged(Location currentLocation) {

        // TODO - Update location considering the following cases.
        // 1) If there is no last location, set the last location to the current
        // location.
        // 2) If the current location is older than the last location, ignore
        // the current location
        // 3) If the current location is newer than the last locations, keep the
        // current location.

        if( mLastLocationReading == null){
            mLastLocationReading = currentLocation;
        }
        else if ( mLastLocationReading.getTime() < currentLocation.getTime()){
            mLastLocationReading = currentLocation;
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        // not implemented
    }

    @Override
    public void onProviderEnabled(String provider) {
        // not implemented
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // not implemented
    }

    // Returns age of location in milliseconds
    private long ageInMilliseconds(Location location) {
        return System.currentTimeMillis() - location.getTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_badges:
                mAdapter.removeAllViews();
                return true;
            case R.id.place_one:
                mMockLocationProvider.pushLocation(37.422, -122.084);
                return true;
            case R.id.place_no_country:
                mMockLocationProvider.pushLocation(0, 0);
                return true;
            case R.id.place_two:
                mMockLocationProvider.pushLocation(38.996667, -76.9275);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shutdownMockLocationManager() {
        if (mMockLocationOn) {
            mMockLocationProvider.shutdown();
        }
    }

    private void startMockLocationManager() {
        if (!mMockLocationOn) {
            mMockLocationProvider = new MockLocationProvider(
                    NETWORK_PROVIDER, this);
        }
    }
}
