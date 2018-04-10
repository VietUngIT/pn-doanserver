package pn.doan.core.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zaxxer.hikari.pool.HikariPool;
import pn.doan.apis.response.BaseResponse;
import pn.doan.apis.response.SimpleResponse;
import pn.doan.core.model.NguoiDung;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class Utils {
    public static final Random rd = new Random();
    private static Gson gson = new Gson();
    public static JsonObject toJsonObject(String json) {
        try {
            return gson.fromJson(json, JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }
    public static String toJsonStringGson(Object obj) {
        return gson.toJson(obj);
    }
    public static JsonArray toJsonArray(String json) {
        try {
            return gson.fromJson(json, JsonArray.class);
        } catch (Exception e) {
            return null;
        }
    }
    public static String sha256(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(src.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
    public static NguoiDung getNguoiDungByEmail(String email) throws SQLException {
        Connection connection = pn.doan.core.config.HikariPool.getConnection();
        NguoiDung nguoiDung = null;
        try {
            String query = "SELECT * FROM nguoidung WHERE Email=?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                nguoiDung = new NguoiDung();
                nguoiDung.setId(rs.getInt("idNguoiDung"));
                nguoiDung.setHoten(rs.getString("HoTen"));
                nguoiDung.setNgaysinh(rs.getString("NgaySinh"));
                nguoiDung.setGioitinh(rs.getBoolean("GioiTinh"));
                nguoiDung.setSdt(rs.getString("SDT"));
                nguoiDung.setEmail(rs.getString("Email"));
                nguoiDung.setDiachi(rs.getString("DiaChi"));
                nguoiDung.setMatkhau(rs.getString("MatKhau"));
                return nguoiDung;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return nguoiDung;
    }
    public static NguoiDung getNguoiDungByMobile(String mobile) throws SQLException {
        Connection connection = pn.doan.core.config.HikariPool.getConnection();
        NguoiDung nguoiDung = null;
        try {
            String query = "SELECT * FROM nguoidung WHERE SDT=?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, mobile);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                nguoiDung = new NguoiDung();
                nguoiDung.setId(rs.getInt("idNguoiDung"));
                nguoiDung.setHoten(rs.getString("HoTen"));
                nguoiDung.setNgaysinh(rs.getString("NgaySinh"));
                nguoiDung.setGioitinh(rs.getBoolean("GioiTinh"));
                nguoiDung.setSdt(rs.getString("SDT"));
                nguoiDung.setEmail(rs.getString("Email"));
                nguoiDung.setDiachi(rs.getString("DiaChi"));
                nguoiDung.setMatkhau(rs.getString("MatKhau"));
                return nguoiDung;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return nguoiDung;
    }
    public static BaseResponse responseError(int errorCode, String msg){
        SimpleResponse response = new SimpleResponse();
        response.setError(errorCode);
        response.setMsg(msg);
        return response;
    }
}
