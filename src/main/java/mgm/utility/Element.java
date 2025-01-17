package mgm.utility;

public enum Element {
    UPPERCASE("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    LOWERCASE("abcdefghijklmnopqrstuvwxyz"),
    DIGITS("0123456789"),
    SPECIAL_CHARACTERS("!@#$%^&*()-_+=<>?/");

    public final String value;

    private Element(String value) {
        this.value = value;
    }
}
