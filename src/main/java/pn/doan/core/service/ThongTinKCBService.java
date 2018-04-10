package pn.doan.core.service;

import pn.doan.apis.response.BaseResponse;
import pn.doan.apis.response.ThongTinKCBResponse;

public interface ThongTinKCBService {
    ThongTinKCBResponse themThongTinKCB(int idUser, boolean b, long starttime,String chuandoan, String dieutri, String tenbenh, String thuoc) throws Exception;

    ThongTinKCBResponse editChuanDoan(int id, String chuandoan) throws Exception;

    ThongTinKCBResponse editBenh(int idbenh, int idkcb, long time, String bieuhien) throws Exception;

    ThongTinKCBResponse getListBenh() throws Exception;

    ThongTinKCBResponse getListThuoc() throws Exception;

    ThongTinKCBResponse getChiTietBenhByYear(int idbenh,int idUser, int year) throws Exception;

    ThongTinKCBResponse getChiTietThuocByYear(int idthuoc, int idUser, int year) throws Exception;

    ThongTinKCBResponse thongKeBenhByYear(int idUser, int year) throws Exception;

    ThongTinKCBResponse thongKeThuocByYear(int idUser, int year) throws Exception;
}
