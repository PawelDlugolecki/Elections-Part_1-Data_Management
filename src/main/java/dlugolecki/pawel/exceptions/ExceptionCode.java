package dlugolecki.pawel.exceptions;

public enum ExceptionCode {
    SERVICE ("SERVICE EXCEPTION"),
    SECURITY ("SECURITY EXCEPTION"),
    VALIDATION ("VALIDATION EXCEPTION"),
    FILE ("FILE EXCEPTION");

    private String description;

    ExceptionCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
