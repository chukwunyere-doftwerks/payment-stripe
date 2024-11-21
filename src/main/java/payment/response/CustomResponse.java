package payment.response;

import lombok.Data;

@Data
public class CustomResponse<T> {
    private ResponseStatus status;
    private String message;
    private T data;

    public CustomResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public CustomResponse(ResponseStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public enum ResponseStatus {
        SUCCESS,
        ERROR
    }
}