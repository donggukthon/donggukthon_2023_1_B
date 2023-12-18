package flirting.demo.common;

import lombok.*;

@Getter
public class ApiStatus {
    private String statusCode;
    private StatusCode statusCodeMessage;
    private String statusMessage;

    public ApiStatus(StatusCode statusCodeMessage, String statusMessage){
        this.statusCode = statusCodeMessage.getStatusCode();
        this.statusCodeMessage = statusCodeMessage;
        this.statusMessage = statusMessage;
    }
}
