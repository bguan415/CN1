import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {


}

class Task {
    public Task(String taskName) {
        this.taskName = taskName;
        size = "";
        descriptions = new ArrayList<>();
        timeSpentOnTask = new HashMap<String,Integer>();
        timeSpentOnTask.put("hours", 0);
        timeSpentOnTask.put("minutes", 0);
        timeSpentOnTask.put("seconds", 0);
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    
    public void incrementRunTime(String startTime, String stopTime) {
    private String taskName;
    String size;
    private Map<String, Integer> timeSpentOnTask;
    private List<String> descriptions;
}
