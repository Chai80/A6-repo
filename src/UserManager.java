public class UserManager {
    private final User user;

    private final Database db;

    public UserManager(User user, Database db) {
        this.user = user;

        this.db = db;
    }

    public boolean process(String command) {
        boolean processed = true;

        String[] tokens = command.split(",");

        String firstWord = tokens[0];

        if (user.getType() != User.Type.ADMIN) {
            return false;
        }

        switch (firstWord) {
            case "create_user":
                System.out.println("> " + command);
                create(tokens);
                break;

            case "update_user_type":
                System.out.println("> " + command);
                updateType(tokens);
                break;

            case "update_user_password":
                System.out.println("> " + command);
                updatePassword(tokens);
                break;

            case "delete_user":
                System.out.println("> " + command);
                delete(tokens);
                break;

            default:
                processed = false;
        }

        return processed;
    }

    private void create(String[] tokens) {
        db.createUser(tokens[1], tokens[2], User.Type.valueOf(tokens[3].toUpperCase()));
    }

    private void updateType(String[] tokens) {
        db.updateUser(tokens[1], null, User.Type.valueOf(tokens[2].toUpperCase()));
    }

    private void updatePassword(String[] tokens) {
        db.updateUser(tokens[1], tokens[2], null);
    }

    private void delete(String[] tokens) {
        db.deleteUser(tokens[1]);
    }
}
