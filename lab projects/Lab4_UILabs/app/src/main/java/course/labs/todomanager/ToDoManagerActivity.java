package course.labs.todomanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import course.labs.todomanager.todo.ToDoItem;
import course.labs.todomanager.todo.ToDoItem.Priority;
import course.labs.todomanager.todo.ToDoItem.Status;
import course.labs.todomanager.todo.ToDoListAdapter;


public class ToDoManagerActivity extends ListActivity {

    private static final int ADD_TODO_ITEM_REQUEST = 0;
    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private static final String TAG = "Lab-UserInterface";
    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    private static final int GET_TODO_ITEM_REQUEST_CODE = 1;

    ToDoListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new TodoListAdapter for this ListActivity's ListView
        mAdapter = new ToDoListAdapter(this);

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        // TODO - Inflate footerView for footer_view.xml file
        TextView footerView = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.footer_view, null);


        // TODO - Add footerView to ListView
        getListView().addFooterView(footerView);


        // TODO - Attach Listener to FooterView
        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO - Implement OnClick().
                Intent startAddToDoActivityIntent = new Intent(
                        ToDoManagerActivity.this,
                        AddToDoActivity.class
                        );
                startActivityForResult(startAddToDoActivityIntent,GET_TODO_ITEM_REQUEST_CODE);

            }
        });


        // TODO - Attach the adapter to this ListActivity's ListView
        getListView().setAdapter(mAdapter);

        registerForContextMenu(getListView());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "Entered onActivityResult()");

        // TODO - Check result code and request code
        // if user submitted a new ToDoItem
        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter

        if(requestCode == GET_TODO_ITEM_REQUEST_CODE){
            if( resultCode == RESULT_OK){
                ToDoItem newToDoItem = new ToDoItem(data);
                mAdapter.add(newToDoItem);
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(ToDoManagerActivity.this, "You canceled the todo", Toast.LENGTH_SHORT).show();
            }
            System.out.println();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        // Load saved ToDoItems, if necessary

        if (mAdapter.getCount() == 0)
            loadItems();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save ToDoItems
        saveItems();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);


        Log.i("context-menu", "aici : Context menu created");

        //menu.setHeaderTitle("Options");
        //menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_delete_message)
                .setTitle(R.string.dialog_delete_title);

        // Add the buttons
        final int finalPosition = position;
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                mAdapter.removeItem(finalPosition);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(ToDoManagerActivity.this, "You removed the selected item", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                mAdapter.clear();
                return true;
            case MENU_DUMP:
                dump();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dump() {

        for (int i = 0; i < mAdapter.getCount(); i++) {
            String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
            Log.i(TAG, "Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
        }

    }

    // Load stored ToDoItems
    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            String priority = null;
            String status = null;
            Date date = null;

            while (null != (title = reader.readLine())) {
                priority = reader.readLine();
                status = reader.readLine();
                date = ToDoItem.FORMAT.parse(reader.readLine());
                mAdapter.add(new ToDoItem(title, Priority.valueOf(priority),
                        Status.valueOf(status), date));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Save ToDoItems to file
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
}