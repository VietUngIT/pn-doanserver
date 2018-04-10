package pn.doan.core.model;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class Thuoc {
    private int id;
    private String tenthuoc;
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",id);
        object.addProperty("tenthuoc",tenthuoc);
        return object;
    }
}
