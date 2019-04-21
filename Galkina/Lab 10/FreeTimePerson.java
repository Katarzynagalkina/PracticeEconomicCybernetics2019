import java.io.BufferedReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class FreeTimePerson extends UnicastRemoteObject implements Mud.RemoteFreeTimePerson {
    private String name;
    private TimeOrganizer organizer;

    private PrintWriter out;
    private final BufferedReader in;

    private ArrayList<MeetingOffer> offers;
    private ArrayList<String> letters;


    FreeTimePerson(String name, PrintWriter out, BufferedReader in) throws RemoteException {
        this.name = name;
        this.out = out;
        this.in = in;
        offers = new ArrayList<>();
        letters = new ArrayList<>();
        organizer = new TimeOrganizer(true);
    }

    @Override
    public ArrayList<MeetingOffer> getOffers() throws RemoteException {
        return offers;
    }

    @Override
    public ArrayList<String> getLetters() throws RemoteException {
        return letters;
    }


    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public String toString() {
        return "{" + name + '\'' +
                ", organizer=" + organizer +
                '}';
    }

    @Override
    public String printFreeTime() throws RemoteException {
        if (organizer.isBusy()) {
            return "You don't have free time at the week :(\n";
        }
        return "Your free time:\n" + organizer;
    }


    @Override
    public TimeOrganizer getOrganizer() throws RemoteException {
        return organizer;
    }


    @Override
    public void add(TimeOrganizer add) throws RemoteException {
        organizer.add(add);
    }

    @Override
    public void remove(TimeOrganizer remove) throws RemoteException {
        organizer.remove(remove);
    }

    private void talk(String text) {
        out.print(text);
        out.flush();
    }

    @Override
    public void addLetter(String letter) throws RemoteException {
        letters.add(letter);
    }

    @Override
    public void removeOffer(MeetingOffer offerToRemove) throws RemoteException {
        for (MeetingOffer offer : offers) {
            if (MeetingOffer.isEqual(offer, offerToRemove)) {
                offers.remove(offer);
                break;
            }
        }
    }

    @Override
    public void addOffer(MeetingOffer offer) throws RemoteException {
        offers.add(offer);
    }

    @Override
    public void cleanLetters() throws RemoteException {
        letters.clear();
    }

    @Override
    public String print() throws RemoteException {
        return toString();
    }

    @Override
    public void remove(Integer day, Integer hour) throws RemoteException {
        organizer.remove(day, hour);
    }
}
