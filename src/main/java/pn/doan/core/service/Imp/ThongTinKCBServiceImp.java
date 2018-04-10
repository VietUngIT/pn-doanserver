package pn.doan.core.service.Imp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import pn.doan.apis.response.ThongTinKCBResponse;
import pn.doan.core.config.HikariPool;
import pn.doan.core.model.*;
import pn.doan.core.service.ThongTinKCBService;
import pn.doan.core.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class ThongTinKCBServiceImp implements ThongTinKCBService {
    @Override
    public ThongTinKCBResponse themThongTinKCB(int idUser, boolean b, long starttime,String chuandoan, String dieutri, String tenbenh, String thuoc) throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        JsonArray arrayBenh = null;
        JsonArray arrayThuoc = null;
        try {
            arrayBenh = Utils.toJsonArray(tenbenh);
        }catch (Exception e){
            e.printStackTrace();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Dữ liệu tên bệnh đưa lên không đúng định dạng.");
            return response;
        }
        try {
            arrayThuoc = Utils.toJsonArray(thuoc);
        }catch (Exception e){
            e.printStackTrace();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("Dữ liệu thuốc đưa lên không đúng định dạng.");
            return response;
        }

        try {
            connection = HikariPool.getConnection();
            String query = "INSERT INTO thongtinkcb(KhamTaiBV,ChanDoanBS,PhuongPhapDieuTri,ThoiGian,idNguoiDung) VALUES (?,?,?,?,?)";
            st = connection.prepareStatement(query);
            st.setBoolean(1, b);
            st.setString(2, chuandoan);
            st.setString(3, dieutri);
            st.setLong(4, starttime);
            st.setInt(5, idUser);
            st.executeUpdate();
            String query1 = "SELECT * FROM thongtinkcb ORDER BY idThongTinKCB DESC LIMIT 1";
            st = connection.prepareStatement(query1);
            rs = st.executeQuery();
            JsonObject root = new JsonObject();
            if(rs.next()) {
                ThongTinKCB thongTinKCB = new ThongTinKCB();
                thongTinKCB.setId(rs.getInt("idThongTinKCB"));
                thongTinKCB.setChuandoan(rs.getString("ChanDoanBS"));
                thongTinKCB.setPpDieuTri(rs.getString("PhuongPhapDieuTri"));
                thongTinKCB.setBV(rs.getBoolean("KhamTaiBV"));
                thongTinKCB.setThoigian(rs.getLong("ThoiGian"));
                thongTinKCB.setIdNguoiDung(rs.getInt("idNguoiDung"));
                root.add("ttkcb",thongTinKCB.toJson());
                int idttkcb = rs.getInt("idThongTinKCB");
                root.add("benh",addBenh(arrayBenh,connection,st,rs,idttkcb,starttime,idUser));
                root.add("thuoc",addThuoc(arrayThuoc,connection,st,rs,idttkcb,starttime,idUser));
                response.setData(root);
            }else {
                response.setError(ErrorCode.SYSTEM_ERROR);
                response.setMsg("Có lỗi trong quá trình xử lý.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    @Override
    public ThongTinKCBResponse editChuanDoan(int id, String chuandoan) throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = HikariPool.getConnection();
            String query = "UPDATE thongtinkcb SET ChanDoanBS = ? WHERE idThongTinKCB = ?";
            st = connection.prepareStatement(query);
            st.setString(1, chuandoan);
            st.setInt(2, id);
            st.executeUpdate();
            String query1 = "SELECT * FROM thongtinkcb  WHERE idThongTinKCB = ?";
            st = connection.prepareStatement(query1);
            st.setInt(1,id);
            rs = st.executeQuery();
            if(rs.next()) {
                ThongTinKCB thongTinKCB = new ThongTinKCB();
                thongTinKCB.setId(rs.getInt("idThongTinKCB"));
                thongTinKCB.setChuandoan(rs.getString("ChanDoanBS"));
                thongTinKCB.setPpDieuTri(rs.getString("PhuongPhapDieuTri"));
                thongTinKCB.setBV(rs.getBoolean("KhamTaiBV"));
                thongTinKCB.setThoigian(rs.getLong("ThoiGian"));
                thongTinKCB.setIdNguoiDung(rs.getInt("idNguoiDung"));
                response.setData(thongTinKCB.toJson());
            }else {
                response.setError(ErrorCode.SYSTEM_ERROR);
                response.setMsg("Có lỗi trong quá trình xử lý.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    @Override
    public ThongTinKCBResponse editBenh(int idbenh, int idkcb, long time, String bieuhien) throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = HikariPool.getConnection();
            String query = "UPDATE chitietbenh SET BieuHien=?,TGHetBenh=? WHERE idbenh = ? AND idThongTinKCB=?";
            st = connection.prepareStatement(query);
            st.setString(1, bieuhien);
            st.setLong(2, time);
            st.setInt(3, idbenh);
            st.setInt(4, idkcb);
            st.executeUpdate();
            String query1 = "SELECT * FROM chitietbenh WHERE idbenh = ? AND idThongTinKCB=?";
            st = connection.prepareStatement(query1);
            st.setInt(1,idbenh);
            st.setInt(2,idkcb);
            rs = st.executeQuery();
            if(rs.next()) {
                Benh benh1 = new Benh();
                benh1.setId(rs.getInt("idBenh"));
                benh1.setTenbenh(rs.getString("TenBenh"));
                response.setData(benh1.toJson());
            }else {
                response.setError(ErrorCode.SYSTEM_ERROR);
                response.setMsg("Có lỗi trong quá trình xử lý.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    @Override
    public ThongTinKCBResponse getListBenh() throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = HikariPool.getConnection();
            String query = "SELECT * FROM benh";
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            JsonArray array = new JsonArray();
            while(rs.next()) {
                Benh benh = new Benh();
                benh.setId(rs.getInt("idBenh"));
                benh.setTenbenh(rs.getString("TenBenh"));
                array.add(benh.toJson());
            }
            response.setArray(array);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    @Override
    public ThongTinKCBResponse getListThuoc() throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = HikariPool.getConnection();
            String query = "SELECT * FROM thuoc";
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            JsonArray array = new JsonArray();
            while(rs.next()) {
                Thuoc thuoc = new Thuoc();
                thuoc.setId(rs.getInt("idThuoc"));
                thuoc.setTenthuoc(rs.getString("TenThuoc"));
                array.add(thuoc.toJson());
            }
            response.setArray(array);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    @Override
    public ThongTinKCBResponse getChiTietBenhByYear(int idbenh,int idUser, int year) throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.YEAR,year);
        calendarStart.set(Calendar.MONTH,0);
        calendarStart.set(Calendar.DATE,1);
        calendarStart.set(Calendar.HOUR_OF_DAY,0);
        calendarStart.set(Calendar.MINUTE,0);
        calendarStart.set(Calendar.SECOND,0);
        calendarStart.set(Calendar.MILLISECOND,0);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.YEAR,year);
        calendarEnd.set(Calendar.MONTH,11);
        calendarEnd.set(Calendar.DATE,31);
        calendarEnd.set(Calendar.HOUR_OF_DAY,0);
        calendarEnd.set(Calendar.MINUTE,0);
        calendarEnd.set(Calendar.SECOND,0);
        calendarEnd.set(Calendar.MILLISECOND,0);
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = HikariPool.getConnection();
            String query = "SELECT * FROM chitietbenh WHERE idbenh=? AND idUser=? AND TGBatDau>=? AND TGBatDau<=?";
            st = connection.prepareStatement(query);
            st.setInt(1,idbenh);
            st.setInt(2,idUser);
            st.setLong(3,calendarStart.getTimeInMillis());
            st.setLong(4,calendarEnd.getTimeInMillis());
            rs = st.executeQuery();
            JsonArray array = new JsonArray();
            while(rs.next()) {
                ChiTietBenh chiTietBenh = new ChiTietBenh();
                chiTietBenh.setIdBenh(rs.getInt("idbenh"));
                chiTietBenh.setIdKCB(rs.getInt("idThongTinKCB"));
                chiTietBenh.setBieuhien(rs.getString("BieuHien"));
                chiTietBenh.setStartTime(rs.getLong("TGBatDau"));
                chiTietBenh.setEndTime(rs.getLong("TGHetBenh"));
                array.add(chiTietBenh.toJson());
            }
            response.setArray(array);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    @Override
    public ThongTinKCBResponse getChiTietThuocByYear(int idthuoc, int idUser, int year) throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.YEAR,year);
        calendarStart.set(Calendar.MONTH,0);
        calendarStart.set(Calendar.DATE,1);
        calendarStart.set(Calendar.HOUR_OF_DAY,0);
        calendarStart.set(Calendar.MINUTE,0);
        calendarStart.set(Calendar.SECOND,0);
        calendarStart.set(Calendar.MILLISECOND,0);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.YEAR,year);
        calendarEnd.set(Calendar.MONTH,11);
        calendarEnd.set(Calendar.DATE,31);
        calendarEnd.set(Calendar.HOUR_OF_DAY,0);
        calendarEnd.set(Calendar.MINUTE,0);
        calendarEnd.set(Calendar.SECOND,0);
        calendarEnd.set(Calendar.MILLISECOND,0);
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = HikariPool.getConnection();
            String query = "SELECT * FROM chitietthuoc WHERE idThuoc=? AND idUser=? AND TGBatDau>=? AND TGBatDau<=?";
            st = connection.prepareStatement(query);
            st.setInt(1,idthuoc);
            st.setInt(2,idUser);
            st.setLong(3,calendarStart.getTimeInMillis());
            st.setLong(4,calendarEnd.getTimeInMillis());
            rs = st.executeQuery();
            JsonArray array = new JsonArray();
            while(rs.next()) {
                ChiTietThuoc chiTietThuoc = new ChiTietThuoc();
                chiTietThuoc.setIdThuoc(rs.getInt("idThuoc"));
                chiTietThuoc.setIdttkcb(rs.getInt("idThongTinKCB"));
                chiTietThuoc.setLieuluong(rs.getString("LieuLuong"));
                chiTietThuoc.setCachdung(rs.getString("CachDung"));
                chiTietThuoc.setStartTime(rs.getLong("TGBatDau"));
                chiTietThuoc.setEndTime(rs.getLong("TGHetThuoc"));
                array.add(chiTietThuoc.toJson());
            }
            response.setArray(array);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    @Override
    public ThongTinKCBResponse thongKeBenhByYear(int idUser, int year) throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Calendar calendar = Calendar.getInstance();
        String query = null;
        if(year==0){
            calendar.set(Calendar.MONTH,0);
            calendar.set(Calendar.DATE,1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
        }else if (year>0){
            calendar.add(Calendar.YEAR,(-1)*year);
            calendar.set(Calendar.MONTH,0);
            calendar.set(Calendar.DATE,1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
        }

        try {
            connection = HikariPool.getConnection();
            query = "SELECT idbenh,Count(idbenh) As numBenh FROM chitietbenh WHERE idUser=? AND TGBatDau>=? GROUP BY idbenh";
            st = connection.prepareStatement(query);
            st.setInt(1,idUser);
            st.setLong(2,calendar.getTimeInMillis());
            rs = st.executeQuery();
            JsonArray jsonArray = new JsonArray();
            while (rs.next()) {
                JsonObject object = new JsonObject();
                object.addProperty("idbenh",rs.getInt("idbenh"));
                object.addProperty("numBenh",rs.getInt("numBenh"));
                query="SELECT * FROM benh WHERE idBenh=?";
                st = connection.prepareStatement(query);
                st.setInt(1,rs.getInt("idbenh"));
                rs = st.executeQuery();
                if(rs.next()){
                    object.addProperty("tenbenh",rs.getString("TenBenh"));
                }
                jsonArray.add(object);
            }
            response.setArray(jsonArray);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    @Override
    public ThongTinKCBResponse thongKeThuocByYear(int idUser, int year) throws Exception {
        ThongTinKCBResponse response = new ThongTinKCBResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Calendar calendar = Calendar.getInstance();
        String query = null;
        if(year==0){
            calendar.set(Calendar.MONTH,0);
            calendar.set(Calendar.DATE,1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
        }else if (year>0){
            calendar.add(Calendar.YEAR,(-1)*year);
            calendar.set(Calendar.MONTH,0);
            calendar.set(Calendar.DATE,1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
        }

        try {
            connection = HikariPool.getConnection();
            query = "SELECT idThuoc,Count(idThuoc) As numThuoc FROM chitietthuoc WHERE idUser=? AND TGBatDau>=? GROUP BY idThuoc";
            st = connection.prepareStatement(query);
            st.setInt(1,idUser);
            st.setLong(2,calendar.getTimeInMillis());
            rs = st.executeQuery();
            JsonArray jsonArray = new JsonArray();
            while (rs.next()) {
                JsonObject object = new JsonObject();
                object.addProperty("idthuoc",rs.getInt("idThuoc"));
                object.addProperty("numThuoc",rs.getInt("numThuoc"));
                query="SELECT * FROM thuoc WHERE idThuoc=?";
                st = connection.prepareStatement(query);
                st.setInt(1,rs.getInt("idThuoc"));
                rs = st.executeQuery();
                if(rs.next()){
                    object.addProperty("TenThuoc",rs.getString("TenThuoc"));
                }
                jsonArray.add(object);
            }
            response.setArray(jsonArray);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi trong quá trình xử lý.");
        } finally {
            connection.close();
            rs.close();
            st.close();
        }
        return response;
    }

    private JsonArray addBenh(JsonArray arrayBenh,Connection connection,PreparedStatement st,ResultSet rs, int idttkcb, long time,int idUser) throws SQLException {
        String querybenh = "INSERT INTO chitietbenh(idbenh,idThongTinKCB,BieuHien,TGHetBenh,TGBatDau,idUser) VALUES (?,?,?,?,?,?)";

        JsonArray jsonArray = new JsonArray();
        for (int i=0;i<arrayBenh.size();i++){
            JsonObject benh = arrayBenh.get(i).getAsJsonObject();
            st = connection.prepareStatement(querybenh);
            st.setInt(1,benh.get("idbenh").getAsInt());
            st.setInt(2,idttkcb);
            st.setString(3,benh.get("bieuhien").getAsString());
            st.setLong(4,benh.get("tghetbenh").getAsLong());
            st.setLong(5,time);
            st.setInt(6,idUser);
            st.executeUpdate();
            if(st.executeUpdate()>0){
                ChiTietBenh chiTietBenh = new ChiTietBenh();
                chiTietBenh.setIdBenh(benh.get("idbenh").getAsInt());
                chiTietBenh.setIdKCB(idttkcb);
                chiTietBenh.setBieuhien(benh.get("bieuhien").getAsString());
                chiTietBenh.setStartTime(time);
                chiTietBenh.setEndTime(benh.get("tghetbenh").getAsLong());
                jsonArray.add(chiTietBenh.toJson());
            }
        }
        connection.close();
        rs.close();
        st.close();
        return jsonArray;
    }
    private JsonArray addThuoc(JsonArray arrayThuoc,Connection connection,PreparedStatement st,ResultSet rs, int idttkcb, long time, int idUser) throws SQLException {
        String querythuoc = "INSERT INTO thuoc(idThuoc,idThongTinKCB,LieuLuong,CachDung,TGHetThuoc,TGBatDau,idUser) VALUES (?,?,?,?,?,?,?)";

        JsonArray jsonArray = new JsonArray();
        for (int i=0;i<arrayThuoc.size();i++){
            JsonObject benh = arrayThuoc.get(i).getAsJsonObject();
            st = connection.prepareStatement(querythuoc);
            st.setInt(1,benh.get("idthuoc").getAsInt());
            st.setInt(2,idttkcb);
            st.setString(3,benh.get("lieuluong").getAsString());
            st.setString(4,benh.get("cachdung").getAsString());
            st.setLong(5,benh.get("tghetbenh").getAsLong());
            st.setLong(6,time);
            st.setInt(7,idUser);
            st.executeUpdate();
            if(st.executeUpdate()>0){
                ChiTietThuoc chiTietThuoc = new ChiTietThuoc();
                chiTietThuoc.setIdThuoc(benh.get("idthuoc").getAsInt());
                chiTietThuoc.setIdttkcb(idttkcb);
                chiTietThuoc.setLieuluong(benh.get("lieuluong").getAsString());
                chiTietThuoc.setCachdung(benh.get("cachdung").getAsString());
                chiTietThuoc.setEndTime(benh.get("tghetbenh").getAsLong());
                chiTietThuoc.setStartTime(time);
                jsonArray.add(chiTietThuoc.toJson());
            }
        }
        connection.close();
        rs.close();
        st.close();
        return jsonArray;
    }
}
