package pn.doan.core.model;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class ChiTietBenh {
    private int idBenh;
    private int idKCB;
    private String bieuhien;
    private long startTime;
    private long endTime;
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("idBenh",idBenh);
        object.addProperty("idKCB",idKCB);
        object.addProperty("bieuhien",bieuhien);
        object.addProperty("startTime",startTime);
        object.addProperty("endTime",endTime);
        return object;
    }
}
