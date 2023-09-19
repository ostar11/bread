package freshbread.bread.exception;

public class NoStockQuantityException extends RuntimeException {
    public NoStockQuantityException() {
        super();
    }

    public NoStockQuantityException(String message) {
        super(message);
    }

    public NoStockQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoStockQuantityException(Throwable cause) {
        super(cause);
    }
}
