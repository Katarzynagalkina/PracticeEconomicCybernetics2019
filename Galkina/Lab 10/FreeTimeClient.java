
import javafx.util.Pair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.TreeSet;


public class FreeTimeClient {

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) {
        try {

            String hostname = args[0];
            String username = args[1];

            Mud.RemoteFreeTimeServer server = (Mud.RemoteFreeTimeServer) Naming.lookup("rmi://" + hostname
                    + ":" + FreeTimeServer.PORT + "/" + username);
            System.out.println("You are connected to RemoteFreeTimeServer!");
            runUser(username, server);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("Usage: java FreeTimeClient <host> <user>");
            System.exit(1);
        }

    }


    private static void runUser(String username, Mud.RemoteFreeTimeServer server) throws IOException, InterruptedException {

        String currentPersonName = welcome(username, server);

        String cmd;

        for (; ; ) {

           try {              
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                }

                cmd = getLine(">> ");

                switch (cmd) {
                    case "s":
                        showFreeTime(server, currentPersonName);
                        break;

                    case "u":
                        updateFreeTime(server, currentPersonName);
                        break;

                    case "m":
                        makeAppointment(server, currentPersonName);
                        break;

                    case "help":
                    case "h":
                        System.out.println(help);
                        break;

                    case "quit":
                    case "q":
                        System.out.println("Bye!");
                        System.out.flush();
                        System.exit(0);

                    default:
                        System.out.println("Unknown command.  Try 'help'.\n");

                }

            } catch (Exception e) {
                System.out.println("Syntax or other error:");
                System.out.println(e);
                System.out.println("Try using the 'help' command.");
            }

            checkOffers(server, currentPersonName);
            checkMail(server, currentPersonName);
        }

    }

    private static void checkMail(Mud.RemoteFreeTimeServer server, String personName) throws RemoteException {
        Mud.RemoteFreeTimePerson person = server.getPerson(personName);
        if (!person.getLetters().isEmpty()) {
            System.out.println(">> Answers to your suggestions for a meeting:");
            for (String letter : person.getLetters()) {
                System.out.println(letter);
            }
            System.out.flush();
            person.cleanLetters();
        }
    }

    private static void checkOffers(Mud.RemoteFreeTimeServer server, String personName) throws RemoteException, InterruptedException {
        Mud.RemoteFreeTimePerson person = server.getPerson(personName);

        ArrayList<MeetingOffer> offers = person.getOffers();

        synchronized (offers) {

            for (MeetingOffer offer : offers) {
                Mud.RemoteFreeTimePerson secondPerson = server.getPerson(offer.getFromWho());
                String answer = getLine("Do you want to meet \"" + offer.getFromWho() + "\" on Day " + offer.getTime().getKey() +
                        " at " + offer.getTime().getValue() + ":00?\n" +
                        "y : yes\n" +
                        "n : no\n" +
                        "2 >> ");

                String toSecondPerson = null;

                switch (answer) {
                    case "y":
                        secondPerson.remove(offer.getTime().getKey(), offer.getTime().getValue());
                        person.remove(offer.getTime().getKey(), offer.getTime().getValue());
                        toSecondPerson = ">> You are meeting with \"" + personName + "\" on Day " + offer.getTime().getKey() +
                                " at " + offer.getTime().getValue() + ":00.\n";
                        break;
                    case "n":
                        toSecondPerson = ">> You aren't meeting with \"" + personName + "\" on Day " + offer.getTime().getKey() +
                                " at " + offer.getTime().getValue() + ":00.\n";
                        break;
                    default:
                        System.out.println("WRONG");
                }

                secondPerson.addLetter(toSecondPerson);
                person.removeOffer(offer);


            }
        }
    }

    private static void makeAppointment(Mud.RemoteFreeTimeServer server, String personName) throws IOException, InterruptedException {

        String secondPersonName = getLine(">> Who do you want to make appointment with?\n" +
                ">> ");

        Mud.RemoteFreeTimePerson person = server.getPerson(personName);
        Mud.RemoteFreeTimePerson secondPerson = server.getPerson(secondPersonName);

        if (secondPerson != null) {

            TimeOrganizer mutualFreeTime = TimeOrganizer.getMutualWeekTime(person.getOrganizer(), secondPerson.getOrganizer());

            if (!mutualFreeTime.isBusy()) {
                boolean done = false;

                for (Pair<Integer, Integer> time : mutualFreeTime.getFreeTimeList()) {

                    String answer = getLine("Do you want to meet \"" + secondPersonName + "\" on Day " + time.getKey() +
                            " at " + time.getValue() + ":00?\n" +
                            "y : yes\n" +
                            "n : no\n" +
                            ">> ");
                    switch (answer) {
                        case "y":
                            System.out.println(">> The answer from \"" + secondPersonName + "\" will come later.\n");

                            secondPerson.addOffer(new MeetingOffer(personName, time));

                            done = true;
                            break;
                        case "n":
                            break;
                        default:
                            System.out.println("WRONG");
                    }

                    if (done) {
                        break;
                    }
                }

                if (!done) {
                    System.out.println(">> You and \"" + secondPersonName + "\" don't have no more opportunity to meet\n");
                }
            } else {
                System.out.println(">> You and \"" + secondPersonName + "\" don't have opportunity to meet\n");
            }
        } else {
            System.out.println(">> No user with this name was found.\n");
        }

        System.out.flush();
    }

    private static void showFreeTime(Mud.RemoteFreeTimeServer server, String personName) throws RemoteException {
        Mud.RemoteFreeTimePerson person = server.getPerson(personName);
        System.out.println(person.printFreeTime());
        System.out.flush();
    }

    private static String welcome(String username, Mud.RemoteFreeTimeServer server) throws IOException, InterruptedException {
        Mud.RemoteFreeTimePerson currentPerson = null;
        do {
            String cmd = getLine("[" + username + "]:" + "Do you want to register or login?\n" +
                    "\tr : register\n" +
                    "\tl : login\n" +
                    ">> ");

            String name = getLine(">> Input your name: ");

            switch (cmd) {
                case "r": {
                    PrintWriter out = new PrintWriter(System.out);
                    FreeTimePerson current = new FreeTimePerson(name, out, in);

                    if (server.addPerson(current)) {
                        System.out.println(">> You are successful registered as \"" + name + "\"");
                        currentPerson = server.getPerson(name);
                    } else {
                        System.out.println(">> Name \"" + name + "\" is already used.");
                    }
                    break;
                }
                case "l": {
                    currentPerson = server.getPerson(name);
                    if (currentPerson != null) {
                        System.out.println(">> You are logged in as \"" + currentPerson.getName() + "\".");
                    } else {
                        System.out.println(">> No user with this name was found.");
                    }
                    break;
                }
                default: {
                    System.out.println(">> Incorrect key + \"" + cmd + "\"");
                    currentPerson = null;
                    break;
                }
            }
        } while (currentPerson == null);
        return currentPerson.getName();
    }

    private static void updateFreeTime(Mud.RemoteFreeTimeServer server, String name) throws IOException {
        System.out.println("\nUpdating free time. Syntax:\n" +
                "for adding: -a <day> <hour> [, <hour>, <hour> ... ]\n" +
                "for removing: -r <day> <hour> [, <hour>, <hour> ... ]\n" +
                "for exiting : q | quit | exit\n" +
                "Input:");

        System.out.flush();

        TimeOrganizer add = new TimeOrganizer(true);
        TimeOrganizer remove = new TimeOrganizer(true);

        String line;
        boolean done = false;
        while (!done && (line = in.readLine()) != null) {
            String[] words = line.split(" ");

            TreeSet<Integer> hours = new TreeSet<>();
            for (int i = 2; i < words.length; i++) {
                hours.add(Integer.parseInt(words[i]));
            }
            try {
                switch (words[0]) {

                    case "-a":
                        add.add(Integer.parseInt(words[1]), hours);
                        break;

                    case "-r":
                        remove.add(Integer.parseInt(words[1]), hours);
                        break;

                    case "exit":
                    case "q":
                    case "quit":
                        done = true;
                        break;
                    default:
                        System.out.println("Incorrect key \"" + words[0] + "\"");
                }

            } catch (Mud.IncorrectHour ih) {
                System.out.println("<error> Incorrect hour.");
            } catch (Mud.IncorrectDay id) {
                System.out.println("<error> Incorrect day.");
            }

        }

        server.updateFreeTime(name, add, remove);

        System.out.println(">> Your free time updated\n");
        System.out.flush();

    }

    private static String getLine(String prompt) throws InterruptedException {
        String line = null;
        do {                      
            try {
                System.out.print(prompt);            
                System.out.flush();                   

                line = in.readLine();                 
                if (line != null) line = line.trim(); 
            } catch (Exception ignored) {
            }
        } while ((line == null) || (line.length() == 0));

        return line;
    }

    private static final String help = "Commands:\n" +
            "s : show free time\n" +
            "u : update free time\n" +
            "m : make appointment\n" +
            "help | h: display this message\n" +
            "q : quit\n";
}
