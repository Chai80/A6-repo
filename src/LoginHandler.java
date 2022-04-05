import java.util.Scanner;

public class LoginHandler {
    private final Scanner input;

    private final Database db;

    private final Boolean emptyDatabase;

    public LoginHandler(Scanner input, Database db) {
        this.input = input;

        this.db = db;

        emptyDatabase = (db.getUserCount() == 0);
    }

    public User process() {
        String name, password;

        User user;

        if (emptyDatabase) {
            System.out.println("As this is the first run of the application, you will need to");
            System.out.println("create the initial administrator account.");
        } else {
            System.out.println("To use this system, you must be an authorized user. Please login");
            System.out.println("with your user name and password.");
        }

        System.out.println();
        System.out.print("User name: ");

        name = input.nextLine();

        System.out.print("Password : ");

        password = input.nextLine();

        System.out.println();

        if (emptyDatabase) {
            user = db.createUser(name, password, User.Type.ADMIN);
        } else {
            user = db.checkUser(name, password);
        }

        return user;
    }
}
