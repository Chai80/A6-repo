import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean interactive = ((args.length == 1) && args[0].equals("-i"));

        boolean running = true;

        Scanner input = new Scanner(System.in);

        Database db = new Database("db/users.db");

        User user = new LoginHandler(input, db).process();

        if (user == null) {
            System.out.println("Invalid account. Terminating");

            System.exit(-1);
        } else {
            System.out.println("Account authenticated.");
            System.out.println();
        }

        UserManager userManager = new UserManager(user, db);

        Simulation simulation = new Simulation(interactive);

        if (!interactive) {
            boolean foundStop = false;
            boolean logged = false;

            StringBuilder sb = new StringBuilder();

            while (input.hasNext()) {
                String temp = input.nextLine();

                if (foundStop && !logged) {
                    Logger.println("Contents found after 'stop' command.");

                    logged = true;
                }

                sb.append(temp).append(System.lineSeparator());

                if (temp.startsWith("stop")) {
                    foundStop = true;
                }
            }

            input = new Scanner(sb.toString());
        }

        while (running && input.hasNext()) {
            String temp = input.nextLine();

            if (!userManager.process(temp)) {
                running = simulation.execute(temp);
            }
        }
    }
}
