package pn.doan.core.service.Imp;

import pn.doan.apis.response.NguoiDungResponse;
import pn.doan.core.config.HikariPool;
import pn.doan.core.model.ErrorCode;
import pn.doan.core.model.NguoiDung;
import pn.doan.core.service.NguoiDungService;
import pn.doan.core.utils.Utils;

import java.sql.*;
import java.util.Calendar;

import static java.sql.Types.INTEGER;

public class NguoiDungServiceImp implements NguoiDungService {

    @Override
    public NguoiDungResponse register(String email, String hoten, String ngaysinh, Boolean gioitinh, String sdt, String matkhau) throws SQLException {
        NguoiDungResponse response = new NguoiDungResponse();
        Connection connection = HikariPool.getConnection();
        try {
            String query = "CALL createUser(?,?,?,?,?,?,?)";
            CallableStatement st = connection.prepareCall(query);
            st.setString(1,hoten);
            st.setString(2, ngaysinh);
            st.setBoolean(3, gioitinh);
            st.setString(4, sdt);
            st.setString(5, email);
            st.setString(6, Utils.sha256(matkhau));
            st.registerOutParameter(7, INTEGER);
            st.executeUpdate();
            int checkUsername = st.getInt(7);
            if (checkUsername == 2) {
                response.setError(ErrorCode.DUPLICATE_USER);
                response.setMsg("Số điện thoại đã được sử dụng.");
            }else if(checkUsername==1){
                response.setError(ErrorCode.DUPLICATE_USER);
                response.setMsg("Email đã được sử dụng.");
            } else {
                NguoiDung nguoiDung = Utils.getNguoiDungByEmail(email);
                response.setData(nguoiDung.toJson());
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Lỗi hệ thống.");
        } finally {
            connection.close();
        }
        return response;
    }

    @Override
    public NguoiDungResponse login(String email, String matkhau) {
        NguoiDungResponse response = new NguoiDungResponse();
        NguoiDung nguoiDung = null;
        try {
            nguoiDung = Utils.getNguoiDungByEmail(email);
            if(nguoiDung!=null){
                if(nguoiDung.getMatkhau().equals(Utils.sha256(matkhau.trim()))){
                    response.setData(nguoiDung.toJson());
                }else {
                    response.setError(ErrorCode.NOT_AUTHORISED);
                    response.setMsg("Mật khẩu không đúng.");
                }
            }else{
                response.setError(ErrorCode.USER_NOT_EXIST);
                response.setMsg("Người dùng không tồn tại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Có lỗi hệ thống.");
        }
        return response;
    }

    @Override
    public NguoiDungResponse changeMatKhau(String email, String matkhau, String matkhaumoi) throws SQLException {
        NguoiDungResponse response = new NguoiDungResponse();
        Connection connection = null;
        try {
            connection = HikariPool.getConnection();
            String query = "CALL updatePass(?,?,?,?)";
            CallableStatement st = connection.prepareCall(query);
            st.setString(1,email);
            st.setString(2, Utils.sha256(matkhau));
            st.setString(3, Utils.sha256(matkhaumoi));
            st.registerOutParameter(4, INTEGER);
            st.executeUpdate();
            int checkUsername = st.getInt(4);
            if (checkUsername == 2) {
                response.setError(ErrorCode.USER_NOT_EXIST);
                response.setMsg("Tài khoản người dùng không tồn tại.");
            }else if(checkUsername==1){
                response.setError(ErrorCode.NOT_AUTHORISED);
                response.setMsg("Mật khẩu không đúng.");
            } else {
                NguoiDung nguoiDung = Utils.getNguoiDungByEmail(email);
                response.setData(nguoiDung.toJson());
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Lỗi hệ thống.");
        } finally {
            connection.close();
        }
        return response;
    }

    @Override
    public NguoiDungResponse changeInfo(String email, String hoten, String ngaysinh, Boolean gioitinh, String matkhau, String diachi) throws Exception {
        NguoiDungResponse response = new NguoiDungResponse();
        Connection connection = null;
        try {
            connection = HikariPool.getConnection();
            String query = "CALL updateInfo(?,?,?,?,?,?,?)";
            CallableStatement st = connection.prepareCall(query);
            st.setString(1,email);
            st.setString(2, Utils.sha256(matkhau));
            st.setString(3, hoten);
            st.setString(4, ngaysinh);
            st.setBoolean(5, gioitinh);
            st.setString(6, diachi);
            st.registerOutParameter(7, INTEGER);
            st.executeUpdate();
            int checkUsername = st.getInt(7);
            if (checkUsername == 2) {
                response.setError(ErrorCode.USER_NOT_EXIST);
                response.setMsg("Tài khoản người dùng không tồn tại.");
            }else if(checkUsername==1){
                response.setError(ErrorCode.NOT_AUTHORISED);
                response.setMsg("Mật khẩu không đúng.");
            } else {
                NguoiDung nguoiDung = Utils.getNguoiDungByEmail(email);
                response.setData(nguoiDung.toJson());
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Lỗi hệ thống.");
        } finally {
            connection.close();
        }
        return response;
    }

    @Override
    public NguoiDungResponse changeEmail(String email, String matkhau, String emailmoi) throws Exception {
        NguoiDungResponse response = new NguoiDungResponse();
        Connection connection = null;
        try {
            connection = HikariPool.getConnection();
            String query = "CALL updateEmail(?,?,?,?)";
            CallableStatement st = connection.prepareCall(query);
            st.setString(1,email);
            st.setString(2, Utils.sha256(matkhau));
            st.setString(3, emailmoi);
            st.registerOutParameter(4, INTEGER);
            st.executeUpdate();
            int checkUsername = st.getInt(4);
            if(checkUsername == 3){
                response.setError(ErrorCode.DUPLICATE_USER);
                response.setMsg("Email mới đã được sử dụng.");
            }else if (checkUsername == 2) {
                response.setError(ErrorCode.USER_NOT_EXIST);
                response.setMsg("Tài khoản người dùng không tồn tại.");
            }else if(checkUsername==1){
                response.setError(ErrorCode.NOT_AUTHORISED);
                response.setMsg("Mật khẩu không đúng.");
            } else {
                NguoiDung nguoiDung = Utils.getNguoiDungByEmail(emailmoi);
                response.setData(nguoiDung.toJson());
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Lỗi hệ thống.");
        } finally {
            connection.close();
        }
        return response;
    }

    @Override
    public NguoiDungResponse changeSDT(String email, String matkhau, String sdt) throws Exception {
        NguoiDungResponse response = new NguoiDungResponse();
        Connection connection = null;
        try {
            connection = HikariPool.getConnection();
            String query = "CALL updateSDT(?,?,?,?)";
            CallableStatement st = connection.prepareCall(query);
            st.setString(1,email);
            st.setString(2, Utils.sha256(matkhau));
            st.setString(3, sdt);
            st.registerOutParameter(4, INTEGER);
            st.executeUpdate();
            int checkUsername = st.getInt(4);
            if(checkUsername == 3){
                response.setError(ErrorCode.DUPLICATE_USER);
                response.setMsg("Email mới đã được sử dụng.");
            }else if (checkUsername == 2) {
                response.setError(ErrorCode.USER_NOT_EXIST);
                response.setMsg("Tài khoản người dùng không tồn tại.");
            }else if(checkUsername==1){
                response.setError(ErrorCode.NOT_AUTHORISED);
                response.setMsg("Mật khẩu không đúng.");
            } else {
                NguoiDung nguoiDung = Utils.getNguoiDungByEmail(email);
                response.setData(nguoiDung.toJson());
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Lỗi hệ thống.");
        } finally {
            connection.close();
        }
        return response;
    }

    private boolean checkEmailExist(String email) throws Exception{
        NguoiDung nguoiDung = Utils.getNguoiDungByEmail(email);
        if(nguoiDung!=null) return true;
        return false;
    }
    private boolean checkSDTExist(String sdt) throws Exception{
        NguoiDung nguoiDung = Utils.getNguoiDungByMobile(sdt);
        if(nguoiDung!=null) return true;
        return false;
    }
}
