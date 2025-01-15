package mgm.model.dto;

public class Result {
    public boolean outcome;
    public String error;
    public boolean temporaryPassword;

    public Result(boolean outcome, String error, boolean temporaryPassword) {
        this.outcome = outcome;
        this.error = error;
        this.temporaryPassword = temporaryPassword;
    }
}
