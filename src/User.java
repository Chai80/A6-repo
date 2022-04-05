public class User {
    private final String name;
    private final User.Type type;

    public User(String name, User.Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public User.Type getType() {
        return type;
    }

    public enum Type {
        ADMIN,
        ANALYST
    }
}
