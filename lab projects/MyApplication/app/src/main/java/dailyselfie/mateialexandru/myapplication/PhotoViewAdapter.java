package dailyselfie.mateialexandru.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

public class PhotoViewAdapter extends ArrayAdapter<SelfieImage> {
    private ArrayList<SelfieImage> list;
    private static LayoutInflater inflater;
    private Context mContext;

    private SparseBooleanArray mSelectedItems;

    public PhotoViewAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        inflater = LayoutInflater.from(mContext);

        list = new ArrayList<>();
        mSelectedItems = new SparseBooleanArray();
    }

    public int getCount() {
        return list.size();
    }

    public SelfieImage getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {

        ImageView image;
        TextView fileName;
        LinearLayout layout;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        ViewHolder holder;

        SelfieImage curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater
                    .inflate(R.layout.photo_item_view, parent, false);
            holder.image = (ImageView) newView.findViewById(R.id.selfie);
            holder.fileName = (TextView) newView.findViewById(R.id.selfie_date);
            holder.layout = (LinearLayout) newView.findViewById(R.id.item_list_view);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        holder.image.setImageBitmap(curr.getmPhotoBitmap());

        holder.fileName.setText(curr.getmPhotoName());

        return newView;

    }

    /*
    Methods for multi choice listener
    ###################################
     */
    public void removeItem(int finalPosition) {
        list.remove(finalPosition);
        notifyDataSetChanged();
    }

    public void removeItem(SelfieImage object) {
        list.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position){
        selectView(position, !mSelectedItems.get(position));
    }

    public void removeSelection() {
        mSelectedItems = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItems.put(position, value);
        } else {
            mSelectedItems.delete(position);
        }
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIDs(){
        return mSelectedItems;
    }

    public int getSelectedCount(){
        return mSelectedItems.size();
    }

    /*
    ###################################
     */

    public void add(SelfieImage listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public void addAtPosition(SelfieImage record, int i) {

        list.add(i, record);
        this.notifyDataSetChanged();

    }

    public ArrayList<SelfieImage> getList() {
        return list;
    }

    public void removeAllViews() {
        list.clear();
        this.notifyDataSetChanged();
    }

}
