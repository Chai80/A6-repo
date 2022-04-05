public final class Logger {
    private static final StringBuffer buffer = new StringBuffer();

    private Logger() {
    }

    public static void print(String message) {
        print(message, true);
    }

    public static void println(String message) {
        println(message, true);
    }

    public static void print(String message, Boolean broadcast) {
        buffer.append(message);

        if (broadcast) {
            System.err.print(message);
        }
    }

    public static void println(String message, Boolean broadcast) {
        buffer.append(message).append(System.lineSeparator());

        if (broadcast) {
            System.err.println(message);
        }
    }

    public static String getContents() {
        return buffer.toString();
    }
}
