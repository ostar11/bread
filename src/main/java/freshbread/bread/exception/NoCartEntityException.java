package freshbread.bread.exception;

public class NoCartEntityException extends RuntimeException {
    public NoCartEntityException() {
        super();
    }

    public NoCartEntityException(String message) {
        super(message);
    }

    public NoCartEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCartEntityException(Throwable cause) {
        super(cause);
    }
}
