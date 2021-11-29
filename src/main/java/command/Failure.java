package command;

public class Failure {
    private final String reason;

    public Failure(String reason) {
        this.reason = reason;
    }

    public static Failure createFailure(String reason) {
        return new Failure(reason);
    }

    public String getReason() { return this.reason; }
}
