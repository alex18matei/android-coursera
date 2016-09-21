package course.labs.todomanager.tabs;

import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import course.labs.todomanager.todo.ToDoItem;

public interface ICallback {
    List<ToDoItem> getToDoList();
}
