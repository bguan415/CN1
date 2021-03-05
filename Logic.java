package org.ecs160.a2;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logic {

    public Logic() {
        sqler = new Data();
        sqler.createTable();
    }

    public void CreateNewTask(String taskName, String size) {
        sqler.insert(taskName, size);
    }

    public ResultSet GetAllTasks() {
        return sqler.GetAllTasks();
    }

    public void SizeSummaryStatistics(String size) {
        int numTasks = sqler.GetSizeClassSize(size);
        if(numTasks == 0) return;

        int totalTime = 0;
        int meanTime = 0;
        int maxTime = 0;
        int minTime = 0;

        ResultSet rs; 
        try {
            rs = sqler.GetSearchResultsBySize(size);
            maxTime = rs.getInt("runTime");
            minTime = maxTime;
            
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
    
            meanTime = totalTime/numTasks;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    private String GenerateTimeStringFromSeconds(int timeInSeconds) {
        int hours = timeInSeconds/3600;
        int minutes = (timeInSeconds % 3600)/60;
        int seconds = (timeInSeconds % 3600)%60;

        return String.format("%d:%02d:%02d",hours,minutes,seconds);
    }

    private Data sqler;
}