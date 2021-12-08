package exceptionWrappers;
import net.dv8tion.jda.api.entities.User;

public final class Getter {
    private Getter() {}

    public static User getUser(String id) {
        try {
            return User.fromId(id);
        } catch(Exception e) {
            return null;
        }
    }

}
