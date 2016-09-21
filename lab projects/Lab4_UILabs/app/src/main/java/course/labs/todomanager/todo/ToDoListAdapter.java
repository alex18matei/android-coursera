package course.labs.todomanager.todo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import course.labs.todomanager.R;
import course.labs.todomanager.tabs.ICallback;
import course.labs.todomanager.tabs.MainActivity;

public class ToDoListAdapter extends BaseAdapter {

    private List<ToDoItem> mItems = new ArrayList<>();
    private final Context mContext;
    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private ICallback mCallback;

    public ToDoListAdapter(Context context,List<ToDoItem> items) {

        mContext = context;
        mItems = items;

    }

    public ToDoListAdapter(Context context) {

        mContext = context;

    }

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    public void add(ToDoItem item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    // Clears the list adapter of all items.

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();

    }

    public List<ToDoItem> getItems() {

        return mItems;

    }

    public void setItems(List<ToDoItem> list){
        mItems = list;
        notifyDataSetChanged();
    }

    public void sortByPriority(){
        ArrayList<ToDoItem> temp = new ArrayList<>();
        temp.addAll(mItems);
        Collections.sort(temp, new Comparator<ToDoItem>() {
            public int compare(ToDoItem o1, ToDoItem o2) {
                return o2.getPriority().compareTo(o1.getPriority());
            }
        });
        mItems = temp;
        notifyDataSetChanged();
    }

    public void sortByDate(){
        ArrayList<ToDoItem> temp = new ArrayList<>();
        temp.addAll(mItems);
        Collections.sort(temp, new Comparator<ToDoItem>() {
            public int compare(ToDoItem o1, ToDoItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        mItems = temp;
        notifyDataSetChanged();
    }

    public void displayNotDone(){
        List<ToDoItem> newItems = new ArrayList<>();
        for ( ToDoItem item : mItems ){
            if( item.getStatus() == ToDoItem.Status.NOTDONE)
                newItems.add(item);
        }
        setItems(newItems);
        //return newItems;
    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {

        return mItems.size();

    }

    // Retrieve the number of ToDoItems

    @Override
    public Object getItem(int pos) {
        if( pos >=0 && pos < mItems.size() )
            return mItems.get(pos);
        else
            return null;
    }

    public void removeItem(int pos){
        mItems.remove(pos);
    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    static class ViewHolder {
        TextView titleView;
        CheckBox statusView;
        Spinner priorityView;
        TextView dateView;
        RelativeLayout relativeLayout;
        ArrayAdapter<CharSequence> adapter;
    }

    // Create a View for the ToDoItem at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder sViewHolder;

        if (convertView == null) {
            sViewHolder = new ViewHolder();

            // TODO - Inflate the View for this ToDoItem
            // from todo_item.xml

            //RelativeLayout itemLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.todo_item, null);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.todo_item, parent, false);
            //((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.todo_item, parent,false);

            sViewHolder.titleView = (TextView) convertView.findViewById(R.id.titleView);

            // TODO - Set up Status CheckBox
            sViewHolder.statusView = (CheckBox) convertView.findViewById(R.id.statusCheckBox);

            // TODO - Display Priority in a TextView
            sViewHolder.priorityView = (Spinner) convertView.findViewById(R.id.priorityView);
            sViewHolder.adapter = ArrayAdapter.createFromResource(mContext,
                    R.array.priority_array, android.R.layout.simple_spinner_item);
            sViewHolder.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            sViewHolder.priorityView.setAdapter(sViewHolder.adapter);


            // TODO - Display Time and Date.
            // Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and
            // time String
            sViewHolder.dateView = (TextView) convertView.findViewById(R.id.dateView);

            convertView.setTag(sViewHolder);
        } else
            sViewHolder = (ViewHolder) convertView.getTag();

        // TODO - Get the current ToDoItem
        final ToDoItem toDoItem = mItems.get(position);

        // Fill in specific ToDoItem data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file


        // TODO - Display Title in TextView
        sViewHolder.titleView.setText(toDoItem.getTitle());


        if (toDoItem.getStatus() == ToDoItem.Status.DONE)
            sViewHolder.statusView.setChecked(true);
        else
            sViewHolder.statusView.setChecked(false);

        // TODO - Must also set up an OnCheckedChangeListener,
        // which is called when the user toggles the status checkbox

        final ViewHolder finalSViewHolder = sViewHolder;
        sViewHolder.statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                toDoItem.setStatus((isChecked) ? ToDoItem.Status.DONE : ToDoItem.Status.NOTDONE);

                if (!isChecked)
                    finalSViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color1));
                else
                    finalSViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color2));

                /*if( mContext instanceof MainActivity)
                    ((MainActivity)mContext).saveItems();*/

                notifyDataSetChanged();
            }
        });


        // TODO - Display Priority in a TextView
        //sViewHolder.priorityView.setText(toDoItem.getPriority().toString());
        int spinnerPosition = sViewHolder.adapter.getPosition(toDoItem.getPriority().toString() );
        sViewHolder.priorityView.setSelection(spinnerPosition);


        final ViewHolder fSViewHolder = sViewHolder;
        sViewHolder.priorityView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();
                int spinnerPosition = fSViewHolder.adapter.getPosition(selectedItem);
                fSViewHolder.priorityView.setSelection(spinnerPosition);
                Log.i("aici::::::::",selectedItem);

                toDoItem.setPriority(selectedItem);

                notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /*
                String selectedItem = toDoItem.getPriority().toString();
                Log.i("User-Interface", "Aici : " + selectedItem);
                int spinnerPosition = fSViewHolder.adapter.getPosition(selectedItem);
                fSViewHolder.priorityView.setSelection(spinnerPosition);
                */
            }
        });

        // TODO - Display Time and Date.
        // Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and
        // time String
        sViewHolder.dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));

        sViewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout1);
        if (toDoItem.getStatus() == ToDoItem.Status.NOTDONE)
            sViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color1));
        else
            sViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color2));

        //((ToDoManagerActivity)mContext).registerForContextMenu(convertView);

        // Return the View you just created
        return convertView;

    }


}
