package pn.doan.core.service;

import pn.doan.apis.response.BaseResponse;
import pn.doan.apis.response.NguoiDungResponse;

import java.sql.SQLException;

public interface NguoiDungService {
    NguoiDungResponse register(String email, String hoten, String ngaysinh, Boolean gioitinh, String sdt, String matkhau) throws SQLException;

    NguoiDungResponse login(String email, String matkhau);

    NguoiDungResponse changeMatKhau(String email, String matkhau, String matkhaumoi) throws SQLException;

    NguoiDungResponse changeInfo(String email, String hoten, String ngaysinh, Boolean gioitinh, String matkhau, String diachi) throws Exception;

    NguoiDungResponse changeEmail(String email, String matkhau, String emailmoi) throws Exception;

    NguoiDungResponse changeSDT(String email, String matkhau, String sdt) throws Exception;
}
