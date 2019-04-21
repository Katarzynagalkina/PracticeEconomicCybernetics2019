
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

class Mud {
    static String mudPrefix = "127.0.0.1";


    public interface RemoteFreeTimeServer extends Remote {

        RemoteFreeTimePerson getPerson(String name) throws RemoteException;

        void updateFreeTime(String name, TimeOrganizer add, TimeOrganizer remove) throws RemoteException;

        boolean addPerson(RemoteFreeTimePerson current) throws RemoteException;
    }

    public interface RemoteFreeTimePerson extends Remote {

        String getName() throws RemoteException;

        TimeOrganizer getOrganizer() throws RemoteException;

        String printFreeTime() throws RemoteException;


        ArrayList<MeetingOffer> getOffers() throws RemoteException;

        ArrayList<String> getLetters() throws RemoteException;

        void add(TimeOrganizer add) throws RemoteException;

        void remove(TimeOrganizer remove) throws RemoteException;

        void addLetter(String toSecondPerson) throws RemoteException;

        void removeOffer(MeetingOffer offer) throws RemoteException;

        void addOffer(MeetingOffer meetingOffer) throws RemoteException;

        void cleanLetters() throws RemoteException;

        String print() throws RemoteException;

        void remove(Integer day, Integer hour) throws RemoteException;
    }

    static class NoSuchPerson extends RemoteException { //TODO use
    }

    static class AlreadyExists extends RemoteException { //TODO use
    }

    static class IncorrectHour extends RemoteException {
    }

    static class IncorrectDay extends RemoteException {
    }
}
