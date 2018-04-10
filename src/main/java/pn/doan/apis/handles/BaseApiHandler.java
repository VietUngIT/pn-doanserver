package pn.doan.apis.handles;

import com.google.gson.JsonArray;
import io.vertx.core.http.HttpServerRequest;
import lombok.Data;
import pn.doan.apis.response.BaseResponse;
import pn.doan.core.model.NguoiDung;
import pn.doan.core.utils.Utils;

import java.sql.SQLException;

@Data
public abstract class BaseApiHandler {
    protected String path = "";
    protected boolean isPublic = false;
    protected String method;

    public abstract BaseResponse handle(HttpServerRequest request) throws Exception;

    public boolean checkSecurityCheck(String email, String pass){
        NguoiDung users = null;
        try {
            users = Utils.getNguoiDungByEmail(email);
            if(users != null) {
                if(users.getMatkhau().equals(pass)){
                    return true;
                }
                return false;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
