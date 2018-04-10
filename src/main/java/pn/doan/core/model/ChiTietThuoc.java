package pn.doan.core.model;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class ChiTietThuoc {
    private int idThuoc;
    private int idttkcb;
    private String lieuluong;
    private String cachdung;
    private long startTime;
    private long endTime;
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("idThuoc",idThuoc);
        object.addProperty("idttkcb",idttkcb);
        object.addProperty("lieuluong",lieuluong);
        object.addProperty("cachdung",cachdung);
        object.addProperty("startTime",startTime);
        object.addProperty("endTime",endTime);
        return object;
    }
}
