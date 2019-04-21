import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


public class FreeTimeServer extends UnicastRemoteObject implements Mud.RemoteFreeTimeServer {
    static int PORT = 2020;
    private ArrayList<Mud.RemoteFreeTimePerson> persons;

    private String userName;

    private FreeTimeServer(String userName) throws RemoteException {
        this.userName = userName;
        persons = new ArrayList<>();
    }

    public FreeTimeServer() throws RemoteException {
       
    }


    public static void main(String[] args) {
        try {
            FreeTimeServer server = new FreeTimeServer(args[0]);
            LocateRegistry.createRegistry(2020);
            Naming.rebind("rmi://" + Mud.mudPrefix + ":" + PORT + "/" + server.userName, server);
            System.out.println("Server connecting!");


        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("Usage: java FreeTimeServer <username> \n");
            System.exit(1);
        }
    }


    @Override
    public Mud.RemoteFreeTimePerson getPerson(String name) throws RemoteException {
        Optional<Mud.RemoteFreeTimePerson> optional = persons.stream().filter(p -> {
            try {
                return Objects.equals(p.getName(), name);
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
            return false;
        }).findFirst();

        System.out.println("Persons are:");
        for (Mud.RemoteFreeTimePerson person : persons) {
            System.out.println(person.toString());
        }
        System.out.println("\n");

        return optional.orElse(null);

    }

    @Override
    public void updateFreeTime(String name, TimeOrganizer add, TimeOrganizer remove) throws RemoteException {
        Mud.RemoteFreeTimePerson person = getPerson(name);
        person.add(add);
        person.remove(remove);
    }

    @Override
    public boolean addPerson(Mud.RemoteFreeTimePerson current) throws RemoteException {
        Optional<Mud.RemoteFreeTimePerson> optional = persons.stream().filter(p -> {
            try {
                return Objects.equals(p.getName(), current.getName());
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
            return false;
        }).findFirst();
        if (!optional.isPresent()) {
            persons.add(current);
            return true;
        }
        return false;
    }

}
