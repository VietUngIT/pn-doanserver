package pn.doan.core.model;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class ThongTinKCB {
    private int id;
    private boolean isBV;
    private String chuandoan;
    private String ppDieuTri;
    private long thoigian;
    private int idNguoiDung;
    private String hoten;
    private String email;
    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",id);
        object.addProperty("isBV",isBV);
        object.addProperty("chuandoan",chuandoan);
        object.addProperty("ppDieuTri",ppDieuTri);
        object.addProperty("thoigian",thoigian);
        object.addProperty("idNguoiDung",idNguoiDung);
        object.addProperty("email",email);
        object.addProperty("hoten",hoten);
        return object;
    }
}
