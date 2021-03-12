package org.ecs160.a2;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Logic {

    public Logic() {
        sqler = new Data();
        sqler.createTable();
        /*System.out.println(TaskExists("newTasks"));
        HashMap<String, String> res = GetMinandMaxRuntimeTasks("S");
        res.forEach((k,v) -> System.out.println("key: "+k+" value:"+v));
        System.out.println();
        HashMap<String, Integer> results = SizeSummaryStatistics("");
        results.forEach((k,v) -> System.out.println("key: "+k+" value:"+v));*/
    }

    public void CreateNewTask(String taskName, String size) {
        sqler.insert(taskName, size);
    }

    public ResultSet GetTask(String taskName) {
        return sqler.GetTask(taskName);
    }

    public boolean TaskExists(String taskName) {
        return sqler.TaskExists(taskName);
    }
    
    public ResultSet GetAllTasks() {
        return sqler.GetAllTasks();
    }

    public HashMap<String, String> GetMinandMaxRuntimeTasks(String size) {
        HashMap<String, String> maxAndMinTasks = new HashMap<>();

        maxAndMinTasks.put("shortestTask", sqler.GetMinRuntimeTask(size));
        maxAndMinTasks.put("longestTask",sqler.GetMaxRuntimeTask(size));

        return maxAndMinTasks;
    }

    public ResultSet GetSearchResultsBySize(String size) {
        return sqler.GetSearchResultsBySize(size);
    }

    public HashMap<String, Integer> SizeSummaryStatistics(String size) {
        if(size.isEmpty()) return FullSummaryStatistics();

        HashMap<String, Integer> stats = new HashMap<>();
        int numTasks = CountSizeClass(size);
        //if(numTasks < 2) return stats;
        if(numTasks == 0) return stats;

        int totalTime = 0;
        int meanTime = 0;
        int maxTime = 0;
        int minTime = 0;

        try {
            ResultSet rs = sqler.GetSearchResultsBySize(size);
            maxTime = 0;
            minTime = Integer.MAX_VALUE;
            
            while (rs.next()) {
                int taskTimeSeconds = rs.getInt("runTime");
                totalTime += taskTimeSeconds;
                if(taskTimeSeconds < minTime) {
                    minTime = taskTimeSeconds;
                }
                if(taskTimeSeconds > maxTime) {
                    maxTime = taskTimeSeconds;
                }
            }
            rs.close();
    
            meanTime = totalTime/numTasks;
            System.out.println(totalTime + "\t" + meanTime + "\t" + minTime + "\t" + maxTime);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        stats.put("totalTime",totalTime);
        stats.put("meanTime",meanTime);
        stats.put("maxTime",maxTime);
        stats.put("minTime",minTime);
        stats.put("numTasks",numTasks);

        return stats;
    }

    public HashMap<String, Integer> FullSummaryStatistics() {
        HashMap<String, Integer> stats = new HashMap<>();
        int numTasks = CountAllTasks();
        if(numTasks < 2) return stats;

        int totalTime = 0;
        int meanTime = 0;
        int maxTime = 0;
        int minTime = Integer.MAX_VALUE;


        List<String> sizes = Arrays.asList( "S", "M", "L", "XL");

        for(String size : sizes) {
            HashMap<String, Integer> sizeStats = SizeSummaryStatistics(size);
            if(sizeStats.isEmpty()) {
                continue;
            }

            totalTime += sizeStats.get("totalTime");
            if(sizeStats.get("minTime") < minTime) {
                minTime = sizeStats.get("minTime");
            }
            if(sizeStats.get("maxTime") > maxTime) {
                maxTime = sizeStats.get("maxTime");
            }
        }

        meanTime = totalTime/numTasks;

        stats.put("totalTime",totalTime);
        stats.put("meanTime",meanTime);
        stats.put("maxTime",maxTime);
        stats.put("minTime",minTime);
        stats.put("numTasks",numTasks);

        return stats;
    }

    public void StartTask(String taskName) {
        LocalDateTime startTimeLDT = LocalDateTime.now();
        DateTimeFormatter dtf = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd,HH:mm:ss"); 

        sqler.StartTask(taskName, dtf.format(startTimeLDT));
    }

    public void StopTask(String taskName) {
        DateTimeFormatter dtf = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd,HH:mm:ss"); 
        LocalDateTime startTimeLDT = sqler.GetStartTimeObject(taskName, dtf);
        LocalDateTime stopTimeLDT = LocalDateTime.now();
        
        Duration duration = Duration.between(stopTimeLDT, startTimeLDT);
        int timeDifference = (int)Math.abs(duration.toSeconds());

        sqler.IncrementRuntime(taskName, timeDifference);

        sqler.DeleteStartTime(taskName);
    }

    public String[] threeTopTaskNames() throws SQLException {
        ResultSet rs = sqler.GetAllTasks();
        String[] taskNames = new String[3];
        for (int i = 0; rs.next() && i < 3; i++) {
            taskNames[i] = rs.getString("name");
        }
        return taskNames;
    }

    public String[] longestThreeTasks() throws SQLException {
        ResultSet rs = sqler.GetAllTasks();
        HashMap<String, String> taskNames = new HashMap<>();
        for (int i = 0; rs.next() && i < 3; i++) {
            taskNames.put(rs.getString("name"), rs.getString("runTime"));
        }

        String[] three_task_names = new String[3];
        for (int i = 0; i < 3; i++) {
            three_task_names[i] = getMaxName(taskNames);
            taskNames.remove(three_task_names[i]);
        }
        return three_task_names;
    }

    private String getMaxName(HashMap<String, String> taskNameList) {
        int curr_time = -1;
        String curr_name = "";
        for (String taskName : taskNameList.keySet()) {
            int tmp_time = Integer.parseInt(taskNameList.get(taskName));
            if (curr_time < tmp_time) {
                curr_time = tmp_time;
                curr_name = taskName;
            }
        }
        return curr_name;
    }

    public int CountAllTasks() {
        return sqler.CountAllTasks();
    }

    public int CountSizeClass(String size) {
        return sqler.GetSizeClassSize(size);
    }

    public void RenameTask(String oldTaskName, String newTaskName) {
        sqler.RenameTask(oldTaskName, newTaskName);
    }

    public void ResizeTask(String taskName, String newSize) {
        sqler.ResizeTask(taskName, newSize);
    }

    public void SetTaskDescription(String taskName, String description) {
        sqler.SetTaskDescription(taskName, description);
    }

    public String GenerateTimeStringFromSeconds(int timeInSeconds) {
        int hours = timeInSeconds/3600;
        int minutes = (timeInSeconds % 3600)/60;
        int seconds = (timeInSeconds % 3600)%60;

        return String.format("%d:%02d:%02d",hours,minutes,seconds);
    }

    private Data sqler;
}