package command;

import java.util.List;

public final class Parser {
    private Parser() {}         // Makes the constructor unavailable

    public static Integer parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    public static String parseString(List<String> stringList) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String string : stringList) {
            stringBuilder.append(string).append(" ");
        }

        return stringBuilder.toString();
    }
}
