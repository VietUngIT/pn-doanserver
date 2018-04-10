package pn.doan.core.model;

import com.google.gson.JsonObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class NguoiDung implements Serializable {
    private int id;
    private String hoten;
    private String ngaysinh;
    private Boolean gioitinh;
    private String sdt;
    private String diachi;
    private String email;
    private String matkhau;

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id",id);
        object.addProperty("hoten",hoten);
        object.addProperty("ngaysinh",ngaysinh);
        object.addProperty("gioitinh",gioitinh);
        object.addProperty("sdt",sdt);
        object.addProperty("diachi",diachi);
        object.addProperty("email",email);
        object.addProperty("matkhau",matkhau);
        return object;
    }
}
