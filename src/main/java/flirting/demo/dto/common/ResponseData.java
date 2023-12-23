package flirting.demo.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseData {
    private ApiStatus apiStatus;
    private Object data;
}
