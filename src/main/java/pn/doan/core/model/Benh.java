package pn.doan.core.model;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class Benh {
    private int id;
    private String tenbenh;
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",id);
        object.addProperty("tenbenh",tenbenh);
        return object;
    }
}
