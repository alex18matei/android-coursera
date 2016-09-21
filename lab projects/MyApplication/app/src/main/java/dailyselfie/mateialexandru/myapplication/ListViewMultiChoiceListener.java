package dailyselfie.mateialexandru.myapplication;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class ListViewMultiChoiceListener implements AbsListView.MultiChoiceModeListener {

    private ListView mListView;
    private PhotoViewAdapter mAdapter;

    public ListViewMultiChoiceListener(ListView mListView, ArrayAdapter mAdapter) {
        this.mListView = mListView;
        this.mAdapter = (PhotoViewAdapter) mAdapter;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
        final int checkedCount = mListView.getCheckedItemCount();
        actionMode.setTitle(checkedCount + " Selected");
        mAdapter.toggleSelection(position);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete:
                SparseBooleanArray selected = mAdapter.getSelectedIDs();

                for (int i = selected.size() - 1; i >= 0; --i) {
                    if (selected.valueAt(i)) {
                        Log.i(MainActivity.TAG, "" + selected.keyAt(i));

                        SelfieImage selectedItem = mAdapter.getItem(selected.keyAt(i)  );
                        mAdapter.removeItem(selectedItem);
                        deleteFile(selectedItem.getmPhotoURI());
                    }
                }

                actionMode.finish();
                return true;

            case R.id.select_all:

                final int checkedCount  = mListView.getCount();

                mAdapter.removeSelection();
                for (int i = 0; i <  checkedCount; i++) {
                    mListView.setItemChecked(i,   true);
                }

                actionMode.setTitle(checkedCount  + " Selected");

            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mAdapter.removeSelection();
    }

    private void deleteFile(String photo) {

        File file = new File(photo);
        file.delete();

    }
}
