package command;

public final class Parser {
    private Parser() {} // Makes the constructor unavailable

    public static Integer parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(NumberFormatException e) {
            return null;
        }
    }
}
