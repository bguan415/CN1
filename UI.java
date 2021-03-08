package org.ecs160.a2;
import static com.codename1.ui.CN.*;
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

    private Form current;
    private Resources theme;
    private BorderLayout bl;
    private Form screen;
    private Container currentLayout;

    public UI() {

        logic = new Logic();

        setTitle("Task List");
        setLayout(new BorderLayout());

        //Container taskEntry = new Container(new BoxLayout(BoxLayout.X_AXIS));

        Container taskList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        taskList.getAllStyles().setFgColor(0x6b7e8c);
        taskList.getAllStyles().setBgTransparency(255);
        taskList.getAllStyles().setBgColor(0x37454f);
        taskList.getStyle().setPadding(20, 20, 20, 20);
        taskList.getAllStyles().setBorder(Border.createLineBorder(6, 0x37454f));


        Container blankSpace = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        blankSpace.getAllStyles().setFgColor(0x99a1a1);
        blankSpace.getAllStyles().setBgTransparency(255);
        blankSpace.getAllStyles().setBgColor(0x37454f);

        //taskList.add(addTask());

        // Initialization for NewTask/Statistics buttons
        Container lower = new Container(new GridLayout(1, 2));
        Button newTask = new Button("New Task");
        newTask.getAllStyles().setFgColor(0x2e3030);
        newTask.getAllStyles().setBorder(Border.createLineBorder(6, 0x435059));
        newTask.getAllStyles().setBgTransparency(255);
        newTask.getAllStyles().setBgColor(0x6b7e8c);
        newTask.getStyle().setAlignment(CENTER);

        Button stats = new Button("Statistics");
        stats.getAllStyles().setFgColor(0x2e3030);
        stats.getAllStyles().setBorder(Border.createLineBorder(6, 0x435059));
        stats.getAllStyles().setBgTransparency(255);
        stats.getAllStyles().setBgColor(0x6b7e8c);
        stats.getStyle().setAlignment(CENTER);



        // Listener for New Task button
        newTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                taskList.add(addTask());
                taskList.animateLayout(5);
            }
        });


        stats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                (statsForm()).show();
            }
        });

        lower.add(newTask).
                add(stats);

        setScrollable(false);
        add(BorderLayout.SOUTH, lower);
        add(BorderLayout.NORTH, taskList);
        add(BorderLayout.CENTER, blankSpace);

    }

    public Task addTask() {
        // Initialization for task viewer
        TableLayout tl = new TableLayout(1, 4);
        Task upper = newStyledTask(tl);

        // Initialization for size button
        upper.add(tl.createConstraint().widthPercentage(20), upper.createSizeButton());


        Accordion accr = new Accordion();
        TextArea ta = new TextArea(7, 40);
        accr.addContent("Task", ta);
        ta.setHint("Description");
        upper.add(tl.createConstraint().widthPercentage(60), accr);
        ta.getAllStyles().setBgTransparency(0);

        // Init for Time
        upper.add(tl.createConstraint().widthPercentage(-2), upper.createTimeButton());

        return upper;
    }

    private Task newStyledTask(Layout tl) {
        Task upper = new Task(tl);

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

    public class Task extends Container {
        String taskName;
        int currentSize;

        public Task(Layout t1) {
            super(t1);
            currentSize = 0;
            taskName = "Task";
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

        public void changeSize(Button sizeButton) {
            currentSize += 1;
            if (currentSize == 4)
                currentSize = 0;
            String[] sizes = {"S", "M", "L", "XL"};
            sizeButton.setText(sizes[currentSize]);
            logic.ResizeTask(taskName,sizes[currentSize]);
        }

        public TimeDisplay createTimeButton() {
            TimeDisplay time = new TimeDisplay();
            time.getStyle().setAlignment(CENTER);
            time.getAllStyles().setFgColor(0x752c29);
            time.getAllStyles().setBgTransparency(0);
            time.getAllStyles().setBgColor(0x752c29);

            time.addActionListener((e) -> toggleRunning(time));
            return time;
        }

        private void toggleRunning(TimeDisplay time) {
            if (time.timerRunning) {
                time.stop(time);
                logic.StartTask(taskName);
            }
            else {
                time.start(time);
                logic.StopTask(taskName);
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

        public void start(Button time) {
            System.out.print(time + "\n");
            timerRunning = true;
            registerAnimated(time);
        }

        public void stop(Button time) {
            System.out.print(time + "\n");
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

    public Form statsForm() {
        Form statsPage = new Form();
        statsPage.setLayout(new BorderLayout());

        Button b1 = new Button("1");
        b1.getAllStyles().setFgColor(0x2e3030);
        b1.getAllStyles().setBorder(Border.createLineBorder(6, 0x3b4852));

        statsPage.add(BorderLayout.NORTH, b1);

        Toolbar.setGlobalToolbar(true);
        Style s = UIManager.getInstance().getComponentStyle("Title");

        Form hi = new Form("Toolbar", new BoxLayout(BoxLayout.Y_AXIS));
        TextField searchField = new TextField("", "Search"); // <1>
        searchField.getHintLabel().setUIID("Title");
        searchField.setUIID("Title");
        searchField.getAllStyles().setAlignment(Component.LEFT);
        hi.getToolbar().setTitleComponent(searchField);
        FontImage searchIcon = FontImage.createMaterial(FontImage.MATERIAL_SEARCH, s);
        searchField.addDataChangeListener((i1, i2) -> { // <2>
            String t = searchField.getText();
            if (t.length() < 1) {
                for (Component cmp : hi.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
            } else {
                t = t.toLowerCase();
                for (Component cmp : hi.getContentPane()) {
                    String val = null;
                    if (cmp instanceof Label) {
                        val = ((Label) cmp).getText();
                    } else {
                        if (cmp instanceof TextArea) {
                            val = ((TextArea) cmp).getText();
                        } else {
                            val = (String) cmp.getPropertyValue("text");
                        }
                    }
                    boolean show = val != null && val.toLowerCase().indexOf(t) > -1;
                    cmp.setHidden(!show); // <3>
                    cmp.setVisible(show);
                }
            }
            hi.getContentPane().animateLayout(250);
        });
        hi.getToolbar().addCommandToRightBar("", searchIcon, (e) -> {
            searchField.startEditingAsync(); // <4>
        });


        try {
            ResultSet rs = logic.GetAllTasks();
            while (rs.next()) {
                String name = rs.getString("name");
                hi.add(name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*
        hi.add("Task A").
                add("Task B").
                add("Task C").
                add("Task D").
                add("Task E").
                add("Task F").
                add("Task G");*/


        Container lower = new Container(new GridLayout(1, 1));
        Button newTask = new Button("Task List");
        newTask.getAllStyles().setFgColor(0x2e3030);
        newTask.getAllStyles().setBorder(Border.createLineBorder(6, 0x435059));
        newTask.getAllStyles().setBgTransparency(255);
        newTask.getAllStyles().setBgColor(0x6b7e8c);
        newTask.getStyle().setAlignment(CENTER);
        lower.add(newTask);

        statsPage.add(BorderLayout.SOUTH, lower);
        statsPage.add(BorderLayout.NORTH, hi);

        newTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Form UIForm = new UI();
                UIForm.show();
            }
        });

        Container blankSpace = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        blankSpace.getAllStyles().setFgColor(0x99a1a1);
        blankSpace.getAllStyles().setBgTransparency(255);
        blankSpace.getAllStyles().setBgColor(0x37454f);

        statsPage.add(BorderLayout.CENTER, blankSpace);

        return statsPage;

    }

    Logic logic;

    public void show() {
        super.show();
    }
}

