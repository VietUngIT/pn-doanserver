package pn.doan.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class ThongTinKCBResponse extends BaseResponse {
    private JsonObject data;
    private JsonArray array;
    @Override
    public String toJonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("e",getError());
        jsonObject.addProperty("msg",getMsg());
        jsonObject.add("data",data);
        jsonObject.add("array",array);
        return jsonObject.toString();
    }
}
