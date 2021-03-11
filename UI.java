package org.ecs160.a2;
import static com.codename1.ui.CN.*;

import com.codename1.components.FloatingActionButton;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.animations.Animation;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.*;
import com.codename1.ui.plaf.*;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.UITimer;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;

import com.codename1.components.Accordion;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.components.InfiniteProgress;
import com.codename1.ui.Component;
import com.codename1.components.MultiButton;
import com.codename1.contacts.Contact;
import com.codename1.ui.Graphics;
import com.codename1.ui.FontImage;






import com.codename1.io.NetworkEvent;

// Main UI for the Timer App
public class UI extends Form {

// Timer Display Screen
// Timers are sectioned boxes in a list. Each box has a variety of labels and buttons.
// On the left of the timer box is the size section. This is a combination button that iterates the size, and a label that displays the size.
// On the right is a the timer section. This displays the current amount of time passed; tapping it starts/stops the timer.
//    (Only show two most relevant times here? Minutes/seconds, hours/minutes, days/hours?) (Change color for start/stop?)
// The middle section contains the name label. Tapping it opens the timer box (Accordion funtion? See CN1) showing the description.
// From the "opened" view, the user can tap the name or description to edit them. The full time passed could also be shown on the right?
// An X button also appears by the name when a timer is opened, which allows the user to delete.

// At the bottom of the timer scroll screen, there are two buttons. New Timer adds a timer, Statistics opens the Statistics Screen

// Statistics Screen
// Four buttons to show stats by size? A search bar to find a specific timer? A log? Settings?

    public Container taskList;
    private int taskNumber;

    public UI() {

        logic = new Logic();

        setTitle("[APP NAME HERE]");
        setLayout(new BorderLayout());

        taskList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        taskList.setScrollableY(true);
        taskList.getAllStyles().setFgColor(0x6b7e8c);
        taskList.getAllStyles().setBgTransparency(255);
        taskList.getAllStyles().setBgColor(0x37454f);
        taskList.getStyle().setPadding(20, 20, 20, 20);
        taskList.getAllStyles().setBorder(Border.createLineBorder(6, 0x37454f));

        try {
            ResultSet rs = logic.GetAllTasks();
            while (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("size");
                String description = rs.getString("description");
                int runTime = rs.getInt("runTime");

                taskList.add(DisplayTask(false, name, size, description, runTime));
                taskList.animateLayout(5);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Container blankSpace = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        blankSpace.getAllStyles().setFgColor(0x99a1a1);
        blankSpace.getAllStyles().setBgTransparency(255);
        blankSpace.getAllStyles().setBgColor(0x37454f);

        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        fab.getAllStyles().setFgColor(0x2e3030);
        fab.getAllStyles().setBgColor(0x6b7e8c);
        fab.getAllStyles().setBgTransparency(255);
        fab.bindFabToContainer(getContentPane());
        fab.addActionListener(e -> {
            Dialog popup = taskPopup();
            popup.show();
        });

        FloatingActionButton fab2 = FloatingActionButton.createFAB(FontImage.MATERIAL_ASSESSMENT);
        fab2.getAllStyles().setFgColor(0x2e3030);
        fab2.getAllStyles().setBgColor(0x6b7e8c);
        fab2.getAllStyles().setBgTransparency(255);
        fab2.bindFabToContainer(getContentPane());
        fab2.addActionListener(e -> {
            new SummaryPage(logic).show();
        });

        getToolbar().addSearchCommand(e -> search((String)e.getSource()));


        setScrollable(false);
        add(BorderLayout.NORTH, taskList);
        add(BorderLayout.CENTER, blankSpace);

    }

    void search(String text) {
        if(text == null || text.length() == 0) {
            for(Component c : taskList) {
                c.setHidden(false);
                c.setVisible(true);
            }
        } else {
            for(Component c : taskList) {
                //Note n = (Note)c.getClientProperty("note");
                text = text.toLowerCase();

                boolean show = c.getName().toLowerCase().indexOf(text) > -1;
                c.setHidden(!show);
                c.setVisible(show);
            }
        }
        getContentPane().animateLayout(200);
    }//set all to invisible and

    private Dialog taskPopup() {
        BorderLayout bl = new BorderLayout();
        Dialog popup = new Dialog(bl);

        Label instruction = new Label("Enter New Task Name:");
        TextField nameBar = new TextField();
        nameBar.getStyle().setAlignment(CENTER);
        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");
        nameBar.setHint("Task " + (taskNumber + 1));

        confirmButton.addActionListener(ev -> {
            taskList.add(addTask(nameBar.getText()));
            taskList.animateLayout(5);
            taskNumber++;
            popup.dispose();
        });

        popup.add(BorderLayout.NORTH, instruction);
        popup.add(BorderLayout.CENTER, nameBar);
        popup.add(BorderLayout.SOUTH, confirmButton);
        //popup.add(BorderLayout.SOUTH, cancelButton);

        return popup;
    }

    public Task addTask(String name) {
        // Initialization for task viewer
        TableLayout tl = new TableLayout(1, 4);
        String taskName;
        if (name.equals("")) { taskName = "Task " + (taskNumber + 1); }
        else { taskName = name; }

        Task upper = newStyledTask(tl, taskName);

        // Initialization for size button
        upper.add(tl.createConstraint().widthPercentage(20), upper.createSizeButton());

        Accordion accr = new Accordion();
        TextArea ta = new TextArea(7, 40);
        accr.addContent(taskName, ta);
        ta.setHint("Description");
        upper.add(tl.createConstraint().widthPercentage(60), accr);
        ta.getAllStyles().setBgTransparency(0);

        // Init for Time
        upper.add(tl.createConstraint().widthPercentage(-2), upper.createTimeButton());

        logic.CreateNewTask(taskName, "S");

        return upper;
    }

    public Task DisplayTask(boolean isNew, String name, String size, String description, int runTime) {
        // Initialization for task viewer
        TableLayout tl = new TableLayout(1, 4);
        String taskName;
        if (name.equals("")) { taskName = "Task " + (taskNumber + 1); }
        else { taskName = name; }

        Task upper = newStyledTask1(tl, taskName, size, description, runTime);
        // Initialization for size button
        upper.add(tl.createConstraint().widthPercentage(20), upper.createSizeButton(size));

        Accordion accr = new Accordion();
        TextArea ta = new TextArea(7, 40);
        accr.addContent(taskName, ta);
        ta.setHint(description == null ? "Description" : description);
        upper.add(tl.createConstraint().widthPercentage(60), accr);
        ta.getAllStyles().setBgTransparency(0);

        // Init for Time
        upper.add(tl.createConstraint().widthPercentage(-2), upper.createTimeButton(runTime));

        if(isNew) logic.CreateNewTask(taskName, size);

        return upper;
    }

    private Task newStyledTask(Layout tl, String name) {
        Task upper = new Task(tl, name);

        Stroke borderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);


        upper.getStyle().setBgTransparency(255);
        upper.getStyle().setBgColor(0xc0c0c0);
        upper.getStyle().setPadding(10, 10, 10, 10);
        upper.getStyle().setBorder(Border.createEtchedLowered());
        upper.getAllStyles().setFgColor(0x2e3030);
        upper.getUnselectedStyle().setBorder(RoundRectBorder.create().
                strokeColor(0).
                strokeOpacity(20).
                stroke(borderStroke));
        upper.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        upper.getAllStyles().setMargin(Component.BOTTOM, 1);

        return upper;
    }

    private Task newStyledTask1(Layout tl, String name, String size, String description, int runTime) {
        Task upper = new Task(tl, name, size, description, runTime);

        Stroke borderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);


        upper.getStyle().setBgTransparency(255);
        upper.getStyle().setBgColor(0xc0c0c0);
        upper.getStyle().setPadding(10, 10, 10, 10);
        upper.getStyle().setBorder(Border.createEtchedLowered());
        upper.getAllStyles().setFgColor(0x2e3030);
        upper.getUnselectedStyle().setBorder(RoundRectBorder.create().
                strokeColor(0).
                strokeOpacity(20).
                stroke(borderStroke));
        upper.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        upper.getAllStyles().setMargin(Component.BOTTOM, 1);

        upper.setName(name + description);

        return upper;
    }

    public class Task extends Container {
        String taskName;
        int currentSize;
        String description;
        int runTime;

        public Task(Layout t1, String name) {
            super(t1);
            currentSize = 0;
            taskName = name;
        }

        public Task(Layout t1, String name, String size, String description, int runTime) {
            super(t1);
            currentSize = SizeStringtoInt(size);
            taskName = name;
            this.description = description;
            this.runTime = runTime;
        }


        public Button createSizeButton() {
            Button size = new Button("S");
            size.getStyle().setAlignment(CENTER);
            size.getAllStyles().setFgColor(0x2e3030);
            size.getAllStyles().setBgTransparency(0);
            size.getAllStyles().setBgColor(0x6b7e8c);

            size.addActionListener((e) -> changeSize(size));

            return size;
        }

        public Button createSizeButton(String sizeStr) {
            Button size = new Button(sizeStr);
            size.getStyle().setAlignment(CENTER);
            size.getAllStyles().setFgColor(0x2e3030);
            size.getAllStyles().setBgTransparency(0);
            size.getAllStyles().setBgColor(0x6b7e8c);

            size.addActionListener((e) -> changeSize(size));

            return size;
        }

        public void changeSize(Button sizeButton) {
            currentSize += 1;
            if (currentSize == 4)
                currentSize = 0;
            String[] sizes = {"S", "M", "L", "XL"};
            sizeButton.setText(sizes[currentSize]);
            logic.ResizeTask(taskName,sizes[currentSize]);
        }

        public UI.TimeDisplay createTimeButton() {
            UI.TimeDisplay time = new UI.TimeDisplay();
            time.getStyle().setAlignment(CENTER);
            time.getAllStyles().setFgColor(0x752c29);
            time.getAllStyles().setBgTransparency(0);
            time.getAllStyles().setBgColor(0x752c29);

            time.addActionListener((e) -> toggleRunning(time));
            return time;
        }

        public UI.TimeDisplay createTimeButton(int runTime) {
            UI.TimeDisplay time = new UI.TimeDisplay(logic.GenerateTimeStringFromSeconds(runTime));
            time.getStyle().setAlignment(CENTER);
            time.getAllStyles().setFgColor(0x752c29);
            time.getAllStyles().setBgTransparency(0);
            time.getAllStyles().setBgColor(0x752c29);

            time.addActionListener((e) -> toggleRunning(time));
            return time;
        }

        private void toggleRunning(UI.TimeDisplay time) {
            if (time.timerRunning) {
                time.stop(time);
                logic.StopTask(taskName);
            }
            else {
                time.start(time);
                logic.StartTask(taskName);
            }
        }

        private int SizeStringtoInt(String size) {
            if(size.equals("S")) {
                return 0;
            }
            if(size.equals("M")) {
                return 1;
            }
            if(size.equals("L")) {
                return 2;
            }
            if(size.equals("XL")) {
                return 3;
            }
            else {
                return 0;
            }
        }
    }



    public class TimeDisplay extends Button {
        int seconds;
        int minutes;
        int hours;
        int days;
        public String timeStamp;
        int lastSecond;
        boolean timerRunning;

        public TimeDisplay() {
            seconds = 0;
            minutes = 0;
            hours = 0;
            days = 0;
            timeStamp = "00:00";
            Calendar curTime = Calendar.getInstance();
            lastSecond = curTime.get(Calendar.SECOND);
            timerRunning =  false;
            setText(timeStamp);
        }

        public TimeDisplay(String runTime) {
            seconds = 0;
            minutes = 0;
            hours = 0;
            days = 0;
            timeStamp = runTime;
            Calendar curTime = Calendar.getInstance();
            lastSecond = curTime.get(Calendar.SECOND);
            timerRunning =  false;
            setText(timeStamp);
        }

        public void start(Button time) {
            timerRunning = true;
            registerAnimated(time);
        }

        public void stop(Button time) {
            timerRunning = false;
            deregisterAnimated(time);
        }

        public boolean animate() {
            if (timePassed()) {
                secondPassed();
                setText(timeStamp);
                return true;
            }

            return false;
        }

        private boolean timePassed() {
            Calendar curTime = Calendar.getInstance();
            int  curSec = curTime.get(Calendar.SECOND);
            if (curSec != lastSecond) {
                lastSecond = curSec;
                return true;
            }
            else {return false;}
        }

        private void secondPassed() {
            seconds++;
            if (seconds == 60) {
                seconds = 0;
                minutes++;
            }
            if (minutes == 60) {
                minutes = 0;
                hours++;
            }
            if (hours == 24) {
                hours = 0;
                days++;
            }
            setTimeString();
        }

        private void setTimeString() {
            if (days > 0)
            {
                timeStamp = days + ":" + pad(hours);
            }
            else if (hours > 0) {
                timeStamp = pad(hours) + ":" + pad(minutes);
            }
            else {
                timeStamp = pad(minutes) + ":" + pad(seconds);
            }
        }

        private String pad(int number) {
            if (number < 10) {
                return "0" + number;
            } else {
                return String.valueOf(number);
            }
        }
    }

    int currentSizeNum = 0;

    public void changeSize(Button sizeButton) {
        currentSizeNum += 1;
        if (currentSizeNum == 5)
            currentSizeNum = 0;
        String[] sizes = {"Size", "S", "M", "L", "XL"};
        sizeButton.setText(sizes[currentSizeNum]);
    }

    Logic logic;

    public void show() {
        super.show();
    }
}