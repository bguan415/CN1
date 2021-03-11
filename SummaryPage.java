package org.ecs160.a2;

import com.codename1.components.FloatingActionButton;
import com.codename1.io.Log;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SummaryPage extends Form{

    public SummaryPage(Logic logic) {
        this.logic = logic;
        setLayout(new BorderLayout());
        setTitle("Statistics");
        Button b1 = new Button("");
        b1.getAllStyles().setBorder(Border.createLineBorder(6, 0x3b4852));


        add(BorderLayout.NORTH, b1);

        Toolbar.setGlobalToolbar(true);
        Style s = UIManager.getInstance().getComponentStyle("Title");

        Form toolbar = new Form("Toolbar", new BoxLayout(BoxLayout.Y_AXIS));

        Button filterer = new Button("All Tasks");
        filterer.setIcon(FontImage.createMaterial(FontImage.MATERIAL_FILTER_ALT, s));
        toolbar.getToolbar().setTitleComponent(filterer);
        filterer.addActionListener((e) -> changeSize(filterer));

        getToolbar().setBackCommand("",e -> {
                Form UIForm = new UI();
                UIForm.show();
        });

        add(BorderLayout.NORTH, toolbar);

        add(BorderLayout.CENTER, BuildStatsCnt(sizesForSummary[currentSizeNum]));
    }

    private Container titleAndTime(String label, String timeStr) {
        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.add(whiteCenterLabel(label));
        container.add(whiteCenterLabel(timeStr));
        return container;
    }

    private Label whiteCenterLabel(String input) {
        Label output = new Label(input);
        output.getStyle().setAlignment(CENTER);
        output.getStyle().setFgColor(0xffffff);
        return output;
    }

    private Container BuildStatsCnt(String size) {
        Container stats = new Container(new GridLayout(2, 2));

        HashMap<String, Integer> statMap = logic.SizeSummaryStatistics(sizesForSummary[currentSizeNum]);
        HashMap<String, String> minMax = logic.GetMinandMaxRuntimeTasks(sizesForSummary[currentSizeNum]);

        Container totalTimeCnt = titleAndTime("Total Time:", logic.GenerateTimeStringFromSeconds(statMap.get("totalTime")));
        totalTimeCnt.getAllStyles().setFgColor(0x2e3030);
        totalTimeCnt.getAllStyles().setBorder(Border.createLineBorder(6, 0x435059));
        totalTimeCnt.getAllStyles().setBgTransparency(255);
        totalTimeCnt.getAllStyles().setBgColor(0x6b7e8c);
        totalTimeCnt.getStyle().setAlignment(CENTER);

        Container meanTimeCnt = titleAndTime("Mean Time:", logic.GenerateTimeStringFromSeconds(statMap.get("meanTime")));
        meanTimeCnt.getAllStyles().setFgColor(0x2e3030);
        meanTimeCnt.getAllStyles().setBorder(Border.createLineBorder(6, 0x435059));
        meanTimeCnt.getAllStyles().setBgTransparency(255);
        meanTimeCnt.getAllStyles().setBgColor(0x6b7e8c);
        meanTimeCnt.getStyle().setAlignment(CENTER);

        Container minTimeCnt = titleAndTime("Min Time:", logic.GenerateTimeStringFromSeconds(statMap.get("minTime")));
        minTimeCnt.add(whiteCenterLabel(minMax.get("shortestTask")));
        minTimeCnt.getAllStyles().setFgColor(0x2e3030);
        minTimeCnt.getAllStyles().setBorder(Border.createLineBorder(6, 0x435059));
        minTimeCnt.getAllStyles().setBgTransparency(255);
        minTimeCnt.getAllStyles().setBgColor(0x6b7e8c);
        minTimeCnt.getStyle().setAlignment(CENTER);

        Container maxTimeCnt = titleAndTime("Max Time:", logic.GenerateTimeStringFromSeconds(statMap.get("maxTime")));
        maxTimeCnt.add(whiteCenterLabel(minMax.get("longestTask")));
        maxTimeCnt.getAllStyles().setFgColor(0x2e3030);
        maxTimeCnt.getAllStyles().setBorder(Border.createLineBorder(6, 0x435059));
        maxTimeCnt.getAllStyles().setBgTransparency(255);
        maxTimeCnt.getAllStyles().setBgColor(0x6b7e8c);
        maxTimeCnt.getStyle().setAlignment(CENTER);

        stats.add(totalTimeCnt).add(meanTimeCnt).add(minTimeCnt).add(maxTimeCnt);
        return stats;
    }

    int currentSizeNum = 0;

    public void changeSize(Button sizeButton) {
        currentSizeNum += 1;
        if (currentSizeNum == 5)
            currentSizeNum = 0;
        String[] sizes = {"All Tasks", "S Tasks", "M Tasks", "L Tasks", "XL Tasks"};
        sizeButton.setText(sizes[currentSizeNum]);
    }

    public String padRightPadSpace(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder(inputString);
        while (sb.length() < length - inputString.length()) {
            sb.append(' ');
        }

        return sb.toString();
    }

    Logic logic;
    String[] sizesForSummary = {"", "S Tasks", "M Tasks", "L Tasks", "XL Tasks"};
}
