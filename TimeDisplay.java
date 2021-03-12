package org.ecs160.a2;

import com.codename1.ui.Button;

import java.util.Calendar;

public class TimeDisplay extends Button {
        int seconds;
        int minutes;
        int hours;
        int days;
        public String timeStamp;
        int lastSecond;
        boolean timerRunning;

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
        }

        public void stop(Button time) {
            timerRunning = false;
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

