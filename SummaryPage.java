package org.ecs160.a2;


import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;

import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;


import java.util.ArrayList;
import java.util.HashMap;

public class SummaryPage extends Form{

    Data sqler = new Data();

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
        filterer.addActionListener((e) -> {
            changeSize(filterer);
            replace(getContentPane().getComponentAt(1),BuildStatsCnt(sizesForSummary[currentSizeNum]), CommonTransitions.createFade(5));
        });

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
        container.getAllStyles().setFgColor(0x2e3030);
        container.getAllStyles().setBorder(Border.createLineBorder(6, 0x435059));
        container.getAllStyles().setBgTransparency(255);
        container.getAllStyles().setBgColor(0x6b7e8c);
        return container;
    }

    private Label whiteCenterLabel(String input) {
        Label output = new Label(input);
        output.getStyle().setAlignment(CENTER);
        output.getStyle().setFgColor(0xffffff);
        output.getStyle().setFont(Font.createSystemFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
        return output;
    }

    private Container BuildStatsCnt(String size) {
        Container stats = new Container(new GridLayout(3, 2));

        HashMap<String, Integer> statMap = logic.SizeSummaryStatistics(sizesForSummary[currentSizeNum]);
        HashMap<String, String> minMax = logic.GetMinandMaxRuntimeTasks(sizesForSummary[currentSizeNum]);

        System.out.println(sizesForSummary[currentSizeNum]);
        Container totalTimeCnt = titleAndTime("Total Time:", logic.GenerateTimeStringFromSeconds(statMap.get("totalTime")));
        totalTimeCnt.add("_____________________________");
        totalTimeCnt.add(whiteCenterLabel("Number of Tasks:"));
        totalTimeCnt.add(whiteCenterLabel(statMap.get("numTasks")+""));

        Container meanTimeCnt = titleAndTime("Mean Time:", logic.GenerateTimeStringFromSeconds(statMap.get("meanTime")));

        Container minTimeCnt = titleAndTime("Min Time:", logic.GenerateTimeStringFromSeconds(statMap.get("minTime")));
        minTimeCnt.add(whiteCenterLabel(minMax.get("shortestTask")));

        Container maxTimeCnt = titleAndTime("Max Time:", logic.GenerateTimeStringFromSeconds(statMap.get("maxTime")));
        maxTimeCnt.add(whiteCenterLabel(minMax.get("longestTask")));


        Container newestTasks = titleAndTime("Newest Tasks","1. newestTaskName1");
        newestTasks.add(whiteCenterLabel("2. newestTaskName2"));
        newestTasks.add(whiteCenterLabel("3. newestTaskName3"));
        Container longestTasks = titleAndTime("Longest Tasks","1. longestTaskName");
        longestTasks.add(whiteCenterLabel("2. longestTaskName2"));
        longestTasks.add(whiteCenterLabel("3. longestTaskName3"));

        stats.add(totalTimeCnt).add(meanTimeCnt).add(minTimeCnt).add(maxTimeCnt).add(newestTasks).add(longestTasks);
        return stats;
    }//set name and call remove OR set to invisible/visible as needed

    int currentSizeNum = 0;

    public void changeSize(Button sizeButton) {
        ArrayList<String> sizes = getAvailableSizes();

        currentSizeNum += 1;

        if (currentSizeNum >= sizes.size()){
            // If end of array, return to head
            currentSizeNum = 0;
        }
        while (sizes.get(currentSizeNum).equals("")){
            // While going through sizes, skip unavailable sizes
            currentSizeNum += 1;
        }
        sizeButton.setText(sizes.get(currentSizeNum));
    }

    public ArrayList getAvailableSizes(){
        ArrayList<String> availableSizes = new ArrayList<String>();
        availableSizes.add("All Tasks");
        String[] allSizes = {"S", "M", "L", "XL"};
        for (String size : allSizes){
            if (sqler.GetCountBySize(size) > 0 ){
                availableSizes.add(size + " Tasks");
            } else {
                availableSizes.add("");
            }
        }
        return availableSizes;
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
    String[] sizesForSummary = {"", "S", "M", "L", "XL"};
}
