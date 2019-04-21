import javafx.util.Pair;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.TreeSet;

public class TimeOrganizer implements Serializable {
    static final int COUNT_OF_DAYS = 7;

    private ArrayList<Day> week;

    TimeOrganizer() throws RemoteException {
       
    }

    TimeOrganizer(boolean b) throws Mud.IncorrectDay, Mud.IncorrectHour {
        this.week = new ArrayList<>();
        for (int i = 0; i < COUNT_OF_DAYS; i++) {
            week.add(new Day(i + 1));
        }
    }

    boolean isBusy() {
        for (Day day : week) {
            if (!day.isBusy()) {
                return false;
            }
        }
        return true;
    }

    public static class Day implements Serializable {
        private int number;
        private TreeSet<Integer> freeTime;

        Day() throws RemoteException {
         
        }

        Day(int number) {
            this.number = number;
            this.freeTime = new TreeSet<>();
        }

        static TreeSet<Integer> getMutualDayTime(Day d1, Day d2) {
            TreeSet<Integer> res = new TreeSet<>(d1.freeTime);
            res.retainAll(d2.freeTime);
            return res;
        }

        void add(int hour) throws Mud.IncorrectHour {
            if (hour >= 0 && hour <= 23) {
                freeTime.add(hour);
            } else {
                throw new Mud.IncorrectHour();
            }
        }

        void remove(int hour) {
            if (freeTime.contains(hour)) {
                freeTime.remove(hour);
            }
        }

        void print() {
            if (freeTime.size() != 0) {
                int count = 1;
                for (int hour : freeTime) {
                    System.out.println("\t" + count++ + ") " + hour + ":00 - " + (hour + 1) + ":00");
                }
            }
        }

        @Override
        public String toString() {
            String res = "";
            if (freeTime.size() != 0) {
                int count = 1;
                for (int hour : freeTime) {
                    res += ("\t" + count++ + ") " + hour + ":00 - " + (hour + 1) + ":00" + "\n");
                }
            }
            return res;
        }

        boolean isBusy() {
            return freeTime.isEmpty();
        }

    }

    private Day getDay(int number) {
        return number >= 1 && number <= 7 ? week.get(number - 1) : null;
    }

    void add(int day, int hour) throws Mud.IncorrectHour, Mud.IncorrectDay {
        if (day >= 1 && day <= COUNT_OF_DAYS) {
            week.get(day - 1).add(hour);
        } else {
            throw new Mud.IncorrectDay();
        }
    }

    void add(int day, TreeSet<Integer> hours) throws Mud.IncorrectHour, Mud.IncorrectDay {
        for (Integer hour : hours) {
            this.add(day, hour);
        }
    }

    void add(TimeOrganizer add) throws Mud.IncorrectDay, Mud.IncorrectHour {
        for (Day day : add.week) {
            this.add(day.number, day.freeTime);
        }
    }


    void remove(int day, int hour) throws Mud.IncorrectDay {
        if (day >=1 && day <= COUNT_OF_DAYS) {
            week.get(day - 1).remove(hour);
        } else {
            throw new Mud.IncorrectDay();
        }
    }


    private void remove(int day, TreeSet<Integer> hours) throws Mud.IncorrectDay {
        for (Integer hour : hours) {
            remove(day, hour);
        }
    }


    void remove(TimeOrganizer remove) throws Mud.IncorrectDay {
        for (Day day : remove.week) {
            this.remove(day.number, day.freeTime);
        }
    }

    static TimeOrganizer getMutualWeekTime(TimeOrganizer t1, TimeOrganizer t2) throws Mud.IncorrectDay, Mud.IncorrectHour {
        TimeOrganizer res = new TimeOrganizer(false);
        for (int i = 0; i < COUNT_OF_DAYS; i++) {
            res.add(i + 1, Day.getMutualDayTime(t1.getDay(i + 1), t2.getDay(i + 1)));
        }
        return res;
    }

    public void print() {
        for (Day day : week) {
            if (!day.isBusy()) {
                System.out.println("Day " + day.number + ":");
                day.print();
            }
        }
    }

    @Override
    public String toString() {
        String res = "";
        for (Day day : week) {
            if (!day.isBusy()) {
                res += ("Day " + day.number + ":\n" + day.toString());
            }
        }
        return res;
    }

    ArrayList<Pair<Integer, Integer>> getFreeTimeList() {
        ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
        for (Day day : week) {
            for (Integer hour : day.freeTime) {
                list.add(new Pair<>(day.number, hour));
            }
        }
        return list;
    }
}
