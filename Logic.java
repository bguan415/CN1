import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Logic {



    public List<String> GetSearchResultsByName(String searchInput) {
        List<String> searchResults = new ArrayList<>();

        for(List<Task> sizeList : taskList) {
            for(Task task : sizeList) {
                if(task.getName().toLowerCase().contains(searchInput.toLowerCase())) {
                    searchResults.add(task.getName());
                }
            }
        }

        return searchResults;
    }

    public List<Task> GetSearchResultsBySize(String searchInput) {
        return taskList.get(StringToIndex(searchInput));
    }

    public void CreateNewTask(String taskName, String size, boolean startNow) {
        taskList.get(StringToIndex(size)).add(new Task(taskName, size, startNow));
    }

    public void StopRunningTask(String taskName) {
        for(List<Task> sizeList : taskList) {
            for(Task task : sizeList) {
                if(task.getName().equals(taskName)) {
                    //
                }
            }
        }
    }

    public void ChangeTaskSize(String taskName, String newSize) {
        for(List<Task> sizeList : taskList) {
            for(Task task : sizeList) {
                if(task.getName().equals(taskName)) {
                    task.setSize(newSize);
                    taskList.get(StringToIndex(newSize)).add(task);
                    sizeList.remove(task);
                    break;
                }
            }
        }
    }

    private int StringToIndex(String size) {
        switch(size) {
            case "S": return 0;
            case "M": return 1;
            case "L": return 2;
            case "XL": return 3;
            default: return 0;
        }
    }

    //To be moved to and handled by Data Class
    Size sizeType;
    private List<List<Task>> taskList;
}
