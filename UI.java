
import static com.codename1.ui.CN.*;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.*;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.UITimer;
import java.io.IOException;
import com.codename1.components.Accordion;

import com.codename1.io.NetworkEvent;

// Main UI for the Timer App
public class UI{

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

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);
        
        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if(err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });        
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }

        bl = new BorderLayout();
        screen = new Form("Timers", bl);

        screen.add(BorderLayout.NORTH, addTask());


        // Initialization for NewTask/Statistics buttons
        Container lower = new Container(new GridLayout(1,2));
        Button newTask = new Button("New Task");
        Button stats = new Button("Statistics");
        newTask.getStyle().setAlignment(CENTER);
        stats.getStyle().setAlignment(CENTER);
        lower.add(newTask).
                add(stats);

        screen.setScrollable(false);
        screen.add(BorderLayout.SOUTH, lower);

        screen.show();
        currentLayout = screen;
    }

    public Container addTask() {
        // Initialization for task viewer
        TableLayout tl = new TableLayout(1,4);
        Container upper = new Container(tl);
        upper.getStyle().setBgTransparency(255);
        upper.getStyle().setBgColor(0xc0c0c0);
        upper.getStyle().setPadding(10,10,10,10);
        upper.getStyle().setBorder(Border.createEtchedLowered());

        // Initialization for size button
        Button size = new Button("Size");
        size.getStyle().setAlignment(CENTER);
        upper.add(tl.createConstraint().widthPercentage(20), size);
        size.addActionListener((e) -> changeSize(size));

        // Initialization for Name button/display
        //Button name = new Button("Task");
        //name.getStyle().setAlignment(CENTER);
        //upper.add(tl.createConstraint().widthPercentage(60), name);

        Accordion accr = new Accordion();
        TextArea ta = new TextArea(7,40);
        accr.addContent("Task", ta);
        ta.setHint("Description");
        upper.add(tl.createConstraint().widthPercentage(60), accr);

        // Init for Time
        Button time = new Button("00:00");
        time.getStyle().setAlignment(CENTER);
        upper.add(tl.createConstraint().widthPercentage(-2), time);

        return upper;
    }

    int currentSizeNum = 0;

    public void changeSize(Button sizeButton)
    {
        currentSizeNum += 1;
        if (currentSizeNum == 5)
            currentSizeNum = 0;
        String [] sizes = {"Size", "S", "M", "L", "XL"};
        sizeButton.setText(sizes[currentSizeNum]);
    }

    public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = getCurrentForm();
        }
    }
    
    public void destroy() {
    }

}
