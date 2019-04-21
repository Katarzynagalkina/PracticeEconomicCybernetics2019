import javafx.util.Pair;

import java.io.Serializable;
import java.util.Objects;

public class MeetingOffer implements Serializable{
    private String fromWho;
    private Pair<Integer, Integer> time;

    MeetingOffer(String fromWho, Pair<Integer, Integer> times) {
        this.fromWho = fromWho;
        this.time = times;
    }

    String getFromWho() {
        return fromWho;
    }

    Pair<Integer, Integer> getTime() {
        return time;
    }

    public static boolean isEqual(MeetingOffer o1, MeetingOffer o2) {
        return Objects.equals(o1.getFromWho(), o2.getFromWho()) && Objects.equals(o1.getTime().getValue(), o2.getTime().getValue()) &&
                Objects.equals(o1.getTime().getKey(), o2.getTime().getKey());
    }
}