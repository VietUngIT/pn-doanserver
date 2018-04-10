package pn.doan.core.service.Imp;

import com.google.gson.JsonArray;
import pn.doan.apis.response.TinhTrangSucKhoeResponse;
import pn.doan.core.config.HikariPool;
import pn.doan.core.model.ErrorCode;
import pn.doan.core.model.TinhTrangSucKhoe;
import pn.doan.core.service.TinhTrangSucKhoeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class TinhTrangSucKhoeServiceImp implements TinhTrangSucKhoeService {
    @Override
    public TinhTrangSucKhoeResponse themTinhTrangSucKhoe(int idUser, int chieucao, int cannang, float bmi, long time) throws Exception {
        TinhTrangSucKhoeResponse response = new TinhTrangSucKhoeResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = HikariPool.getConnection();
            String query = "INSERT INTO tinhtrangsuckhoe(ChieuCao,CanNang,ChiSoBMI,ThoiGian,idNguoiDung) VALUES (?,?,?,?,?)";
            st = connection.prepareStatement(query);
            st.setInt(1, chieucao);
            st.setInt(2, cannang);
            st.setFloat(3, bmi);
            st.setLong(4, time);
            st.setInt(5, idUser);
            st.executeUpdate();
            String query1 = "SELECT * FROM tinhtrangsuckhoe ORDER BY idTinhTrangSucKhoe DESC LIMIT 1";
            st = connection.prepareStatement(query1);
            rs = st.executeQuery();
            if(rs.next()) {
                TinhTrangSucKhoe tinhTrangSucKhoe = new TinhTrangSucKhoe();
                tinhTrangSucKhoe.setId(rs.getInt("idTinhTrangSucKhoe"));
                tinhTrangSucKhoe.setChieucao(rs.getInt("ChieuCao"));
                tinhTrangSucKhoe.setCannang(rs.getInt("CanNang"));
                tinhTrangSucKhoe.setBmi(rs.getFloat("ChiSoBMI"));
                tinhTrangSucKhoe.setThoigian(rs.getLong("ThoiGian"));
                tinhTrangSucKhoe.setIdnguoidung(rs.getInt("idNguoiDung"));
                response.setData(tinhTrangSucKhoe.toJson());
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
    public TinhTrangSucKhoeResponse delTTSK(int id) throws Exception {
        TinhTrangSucKhoeResponse response = new TinhTrangSucKhoeResponse();
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = HikariPool.getConnection();
            String query = "DELETE FROM tinhtrangsuckhoe WHERE idTinhTrangSucKhoe = ?";
            st = connection.prepareStatement(query);
            st.setInt(1,id);
            rs = st.executeQuery();
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
    public TinhTrangSucKhoeResponse getTTSKByYear(int year, int idUser) throws Exception {
        TinhTrangSucKhoeResponse response = new TinhTrangSucKhoeResponse();
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
            if(year==-1){
                query = "SELECT * FROM tinhtrangsuckhoe WHERE idNguoiDung=?";
                st = connection.prepareStatement(query);
                st.setInt(1,idUser);
            }else{
                query = "SELECT * FROM tinhtrangsuckhoe WHERE idNguoiDung=? AND ThoiGian>=?";
                st = connection.prepareStatement(query);
                st.setInt(1,idUser);
                st.setLong(2,calendar.getTimeInMillis());
            }
            rs = st.executeQuery();
            JsonArray jsonArray = new JsonArray();
            while (rs.next()) {
                TinhTrangSucKhoe tinhTrangSucKhoe = new TinhTrangSucKhoe();
                tinhTrangSucKhoe.setId(rs.getInt("idTinhTrangSucKhoe"));
                tinhTrangSucKhoe.setChieucao(rs.getInt("ChieuCao"));
                tinhTrangSucKhoe.setCannang(rs.getInt("CanNang"));
                tinhTrangSucKhoe.setBmi(rs.getFloat("ChiSoBMI"));
                tinhTrangSucKhoe.setThoigian(rs.getLong("ThoiGian"));
                tinhTrangSucKhoe.setIdnguoidung(rs.getInt("idNguoiDung"));
                jsonArray.add(tinhTrangSucKhoe.toJson());
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
}
