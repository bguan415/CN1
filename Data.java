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

class Summary {
    public Summary(
                int summaryType, String[] summaryArgs) {
        totalTime = 0;
        this.summaryArgs = summaryArgs;
        this.summaryType = summaryType;
        totalMinTime = Integer.MAX_VALUE;
    }

    public void GenerateSummary() {
        // TODO: Generate summary of task
    }
    
    private void PrintAllSummary() {
        // Prints the size of the task
        if(taskList.containsKey("")) {
            //PrintSizeSummary("");
        }
        if(taskList.containsKey("S")) {
            //PrintSizeSummary("S");
        }
        if(taskList.containsKey("M")) {
            //PrintSizeSummary("M");
        }
        if(taskList.containsKey("L")) {
            //PrintSizeSummary("L");
        }
        if(taskList.containsKey("XL")) {
            //PrintSizeSummary("XL");
        }
        //PrintStatisticsForAll();
    }

    private String GenerateTimeStringFromSeconds(int timeInSeconds) {
        // Converts time from seconds into a different time value
        int hours = timeInSeconds/3600;
        int minutes = (timeInSeconds % 3600)/60;
        int seconds = (timeInSeconds % 3600)%60;

        return String.format("%d:%02d:%02d",hours,minutes,seconds);
    }

    private int summaryType;
    private String[] summaryArgs;
    private HashMap<String,List<Task>> taskList;
    private int totalTime;
    private int totalNumTasks;
    private int totalMinTime;
    private int totalMaxTime;
}
