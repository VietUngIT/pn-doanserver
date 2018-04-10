package pn.doan.apis.handles;

import io.vertx.core.http.HttpServerRequest;
import pn.doan.apis.response.BaseResponse;
import pn.doan.core.model.ErrorCode;
import pn.doan.core.service.Imp.TinhTrangSucKhoeServiceImp;
import pn.doan.core.service.TinhTrangSucKhoeService;
import pn.doan.core.utils.Utils;

public class TinhTrangSucKhoeHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        TinhTrangSucKhoeService service = new TinhTrangSucKhoeServiceImp();
        String type = request.getParam("t");
        if(type.equals("them")){
            String striduser = request.getParam("iduser");
            String strcc = request.getParam("chieucao");
            String strcn = request.getParam("cannang");
            String strtime = request.getParam("thoigian");
            String strbmi = request.getParam("bmi");
            return themTinhTrangSucKhoeHandle(striduser,strcc,strcn,strtime,strbmi,service);
        }else if(type.equals("getbyyear")){
            String stryear = request.getParam("subyear");
            String striduser = request.getParam("iduser");
            return getTTSKByYearHandle(stryear,striduser,service);
        }else if(type.equals("del")){
            String strid = request.getParam("id");
            return delTTSKHandle(strid,service);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse getTTSKByYearHandle(String stryear, String stridUser,TinhTrangSucKhoeService service) throws Exception{
        if(stridUser!=null){
            try {
                int year = Integer.parseInt(stryear);
                int idUser = Integer.parseInt(stridUser);
                return service.getTTSKByYear(year,idUser);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else{
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse delTTSKHandle(String strid, TinhTrangSucKhoeService service) throws Exception {
        if(strid!=null){
            try {
                int id = Integer.parseInt(strid);
                return service.delTTSK(id);
            }catch (NumberFormatException e){
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else{
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse themTinhTrangSucKhoeHandle(String striduser, String strcc, String strcn, String strtime, String strbmi, TinhTrangSucKhoeService service) throws Exception {
        if(striduser!=null && strcc!=null && strcn!=null && strtime!=null && strbmi!=null){
            try {
                int idUser = Integer.parseInt(striduser);
                int chieucao = Integer.parseInt(strcc);
                int cannang = Integer.parseInt(strcn);
                float bmi = Float.parseFloat(strbmi);
                long time = Long.parseLong(strtime);
                return service.themTinhTrangSucKhoe(idUser,chieucao,cannang,bmi,time);
            }catch (NumberFormatException e){
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else{
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }
}
