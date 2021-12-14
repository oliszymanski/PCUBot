package exceptionWrappers;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public final class Getter {
    private Getter() {}

    public static User getUser(String id) {
        try {
            return User.fromId(id);
        } catch(Exception e) {
            return null;
        }
    }

    public static String get(List<String> arrayList, int index) {
        try {
            return arrayList.get(index);
        } catch (Exception e) {
            return null;
        }
    }
}
