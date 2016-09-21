package course.labs.todomanager.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import course.labs.todomanager.todo.ToDoItem;
import course.labs.todomanager.todo.ToDoListAdapter;

public class TabFragment2 extends ListFragment implements Serializable {

    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private ICallback mCallback;
    ToDoListAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initAdapter();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    private void initAdapter() {
        mAdapter = new ToDoListAdapter(getActivity().getApplicationContext(),mCallback.getToDoList());
        //loadItems();
        mAdapter.sortByDate();
        setListAdapter(mAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter!=null && mAdapter.getCount() == 0)
            initAdapter();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the hosting Activity has implemented
        // the SelectionListener callback interface. We need this
        // because when an item in this ListFragment is selected,
        // the hosting Activity's onItemSelected() method will be called.

        try {

            mCallback = (ICallback) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectionListener");
        }

    }


    @Override
    public String toString() {
        return "tab2String";
    }
}
