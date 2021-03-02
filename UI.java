package org.ecs160.a2;
import static com.codename1.ui.CN.*;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.*;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.UITimer;
import java.io.IOException;
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

        taskList.add(addTask());

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

        hi.add("Task A").
                add("Task B").
                add("Task C").
                add("Task D").
                add("Task E").
                add("Task F").
                add("Task G");


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

    public Container addTask() {
        // Initialization for task viewer
        TableLayout tl = new TableLayout(1, 4);
        Container upper = new Container(tl);

        Stroke borderStroke = new Stroke(2, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);


        upper.getStyle().setBgTransparency(255);
        upper.getStyle().setBgColor(0xc0c0c0);
        upper.getStyle().setPadding(10, 10, 10, 10);
        upper.getStyle().setBorder(Border.createEtchedLowered());
        upper.getAllStyles().setFgColor(0x2e3030);
        upper.getUnselectedStyle().setBorder(RoundBorder.create().
                rectangle(true).
                color(0xcfdae3).
                strokeColor(0).
                strokeOpacity(20).
                stroke(borderStroke));
        upper.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        upper.getAllStyles().setMargin(Component.BOTTOM, 1);


        // Initialization for size button
        Button size = new Button("Size");
        size.getStyle().setAlignment(CENTER);
        upper.add(tl.createConstraint().widthPercentage(20), size);
        size.addActionListener((e) -> changeSize(size));
        size.getAllStyles().setFgColor(0x2e3030);
        size.getAllStyles().setBgTransparency(0);
        size.getAllStyles().setBgColor(0x6b7e8c);

        // Initialization for Name button/display
        //Button name = new Button("Task");
        //name.getStyle().setAlignment(CENTER);
        //upper.add(tl.createConstraint().widthPercentage(60), name);

        Accordion accr = new Accordion();
        TextArea ta = new TextArea(7, 40);
        accr.addContent("Task", ta);
        ta.setHint("Description");
        upper.add(tl.createConstraint().widthPercentage(60), accr);
        ta.getAllStyles().setBgTransparency(0);


        // Init for Time
        Button time = new Button("00:00");
        time.getStyle().setAlignment(CENTER);
        time.getAllStyles().setFgColor(0x752c29);
        time.getAllStyles().setBgTransparency(0);
        time.getAllStyles().setBgColor(0x752c29);
        upper.add(tl.createConstraint().widthPercentage(-2), time);

        return upper;
    }

    int currentSizeNum = 0;

    public void changeSize(Button sizeButton) {
        currentSizeNum += 1;
        if (currentSizeNum == 5)
            currentSizeNum = 0;
        String[] sizes = {"Size", "S", "M", "L", "XL"};
        sizeButton.setText(sizes[currentSizeNum]);
    }

    public void show() {
        super.show();
    }
}

