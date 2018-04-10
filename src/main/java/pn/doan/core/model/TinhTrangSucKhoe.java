package pn.doan.core.model;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class TinhTrangSucKhoe {
    private int id;
    private int chieucao;
    private int cannang;
    private float bmi;
    private long thoigian;
    private int idnguoidung;
    private String hoten;
    private String email;
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",id);
        object.addProperty("chieucao",chieucao);
        object.addProperty("cannang",cannang);
        object.addProperty("bmi",bmi);
        object.addProperty("thoigian",thoigian);
        object.addProperty("idnguoidung",idnguoidung);
        object.addProperty("email",email);
        object.addProperty("hoten",hoten);
        return object;
    }
}
