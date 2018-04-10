package pn.doan.apis.response;

import lombok.Data;
import pn.doan.core.model.ErrorCode;

@Data
public abstract class BaseResponse {
    private int error = ErrorCode.SUCCESS;
    private String msg = "Success.";
    public boolean isSuccess(){
        return error == ErrorCode.SUCCESS;
    }
    public abstract String toJonString();
}
