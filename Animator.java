package org.ecs160.a2;

public class Animator {
        Vector<TimeDisplay> activeTasks = new Vector<>();
        int currentActive = 0;
        Logic logic = new Logic();

        public void addAnimated(TimeDisplay timer) {
            registerAnimated(timer);
            activeTasks.add(timer);
            currentActive += 1;
        }

        public void removeAnimated(TimeDisplay timer) {
            deregisterAnimated(timer);
            activeTasks.remove(timer);
            currentActive -= 1;
        }

        public void updateTimers() {
            if (currentActive > 0) {
                for (TimeDisplay timer : activeTasks) {
                    deregisterAnimated(timer);
                    String taskName = timer.taskName;
                    int runTime = logic.GetRuntime(taskName) + logic.CalculateTimeDifference(taskName);
                    System.out.print(runTime);
                    timer.updateTimeStamp(runTime);
                    registerAnimated(timer);
                }
            }
        }
    }
