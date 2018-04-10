package pn.doan.core.service;

import pn.doan.apis.response.BaseResponse;
import pn.doan.apis.response.TinhTrangSucKhoeResponse;

public interface TinhTrangSucKhoeService {
    TinhTrangSucKhoeResponse themTinhTrangSucKhoe(int idUser, int chieucao, int cannang, float bmi, long time) throws Exception;

    TinhTrangSucKhoeResponse delTTSK(int id) throws Exception;

    TinhTrangSucKhoeResponse getTTSKByYear(int year, int idUser) throws Exception;
}
