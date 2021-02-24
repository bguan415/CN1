import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    
    public Data() {
        taskList = new ArrayList<>();
        for(List<Task> sizeList : taskList) {
            sizeList = new ArrayList<>();
        }
    }

    private List<List<Task>> taskList;
}

class Task {
    public Task(String taskName, String size, boolean startUponCreation) {
        this.taskName = taskName;
        this.size = size;
        isRunning = startUponCreation;
        if(startUponCreation) lastStartTime = LocalDateTime.now();
        descriptions = new ArrayList<>();
        timeSpentOnTask = new HashMap<String,Integer>();
        timeSpentOnTask.put("hours", 0);
        timeSpentOnTask.put("minutes", 0);
        timeSpentOnTask.put("seconds", 0);
    }

    public void setName(String name) {
        this.taskName = name;
    }

    public String getName() {
        return taskName;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setStartTime() {
        this.lastStartTime = LocalDateTime.now();
    }

    public LocalDateTime getLastStartTime() {
        return lastStartTime;
    }
    
    public int getTimeSpentInSeconds() {
        return timeSpentOnTask.get("hours")*3600 +
            timeSpentOnTask.get("minutes")*60 +
            timeSpentOnTask.get("seconds");
    }

    private String taskName;
    private String size;
    private boolean isRunning;
    private LocalDateTime lastStartTime;
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
