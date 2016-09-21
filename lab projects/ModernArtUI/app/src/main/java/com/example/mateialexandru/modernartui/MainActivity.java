package com.example.mateialexandru.modernartui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class MainActivity extends Activity {


    private MyCustomView myCustomView;
    private static final int MENU_MORE_INFORMATION = Menu.FIRST;
    static private final String URL = "http://www.moma.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.frags_relative);
        myCustomView = ((MyCustomView) findViewById(R.id.my_view));
        SeekBar mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

    }


    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            updateNow(i);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    private void updateNow(int progrss) {

        myCustomView.setmSeekProgess(progrss * 3);
        myCustomView.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_MORE_INFORMATION, Menu.NONE, getResources().getString(R.string.more_information));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_MORE_INFORMATION:
                showMoreInformationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMoreInformationDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.more_information_message)
                .setTitle(R.string.more_information);

        // Add the buttons
        builder.setPositiveButton(R.string.visit_moma, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                visitMOMA();
            }
        });
        builder.setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void visitMOMA() {
        Intent baseIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(URL)
        );
        startActivity(baseIntent);
    }

}
