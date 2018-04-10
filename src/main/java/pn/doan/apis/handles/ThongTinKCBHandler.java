package pn.doan.apis.handles;

import io.vertx.core.http.HttpServerRequest;
import pn.doan.apis.response.BaseResponse;
import pn.doan.core.model.ErrorCode;
import pn.doan.core.service.Imp.ThongTinKCBServiceImp;
import pn.doan.core.service.ThongTinKCBService;
import pn.doan.core.utils.Utils;

public class ThongTinKCBHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        ThongTinKCBService service = new ThongTinKCBServiceImp();
        String type = request.getFormAttribute("t");
        if(type.equals("them")){
            String striduser = request.getFormAttribute("iduser");
            String strbv = request.getFormAttribute("bv");
            String chuandoan = request.getFormAttribute("chuandoan");
            String dieutri = request.getFormAttribute("dieutri");
            String strstarttime = request.getFormAttribute("startime");
            String tenbenh = request.getFormAttribute("tenbenh");
            String thuoc = request.getFormAttribute("thuoc");
            return themThongTinKCBHandle(striduser,strbv,chuandoan,dieutri,strstarttime,tenbenh,thuoc,service);
        }else if(type.equals("del")){
            return null;
        }else if(type.equals("getlistbenh")){
            return getListBenhHandle(service);
        }else if(type.equals("getlistthuoc")){
            return getListThuocHandle(service);
        }else if(type.equals("editchuandoan")){
            String stridkcb = request.getFormAttribute("id");
            String chuandoan = request.getFormAttribute("chuandoan");
            return editCHuanDoanHandle(stridkcb,chuandoan,service);
        }else if(type.equals("editchitietbenh")){
            String stribenh = request.getFormAttribute("idbenh");
            String strikcb = request.getFormAttribute("idkcb");
            String thoigian = request.getFormAttribute("thoigian");
            String bieuhien = request.getFormAttribute("bieuhien");
            return editBenhHandle(stribenh,strikcb,thoigian,bieuhien,service);
        }else if(type.equals("getchitietbenhbyyear")){
            String stribenh = request.getFormAttribute("idbenh");
            String striuser = request.getFormAttribute("iduser");
            String stryear = request.getFormAttribute("year");
            return getChiTietBenhByYearHandle(stribenh,stryear,striuser,service);
        }else if(type.equals("thongkebenhbyyear")){
            String striuser = request.getFormAttribute("iduser");
            String stryear = request.getFormAttribute("subyear");
            return thongKeBenhByYearHandle(stryear,striuser,service);
        }else if(type.equals("getchitietthuocbyyear")){
            String strithuoc = request.getFormAttribute("idthuoc");
            String striuser = request.getFormAttribute("iduser");
            String stryear = request.getFormAttribute("year");
            return getChiTietThuocByYearHandle(strithuoc,stryear,striuser,service);
        }else if(type.equals("thongkethuocbyyear")){
            String striuser = request.getFormAttribute("iduser");
            String stryear = request.getFormAttribute("subyear");
            return thongKeThuocByYearHandle(stryear,striuser,service);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse thongKeThuocByYearHandle(String stryear, String striuser, ThongTinKCBService service) throws Exception {
        if(striuser!=null && stryear!=null){
            try{
                int year = Integer.parseInt(stryear);
                int idUser = Integer.parseInt(striuser);
                return service.thongKeThuocByYear(idUser,year);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else{
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse thongKeBenhByYearHandle(String stryear, String striuser, ThongTinKCBService service) throws Exception{
        if(striuser!=null && stryear!=null){
            try{
                int year = Integer.parseInt(stryear);
                int idUser = Integer.parseInt(striuser);
                return service.thongKeBenhByYear(idUser,year);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else{
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse getChiTietThuocByYearHandle(String strithuoc, String stryear, String striuser, ThongTinKCBService service) throws Exception {
        if(strithuoc!=null && stryear!=null && striuser!=null){
            try{
                int idthuoc = Integer.parseInt(strithuoc);
                int idUser = Integer.parseInt(striuser);
                int year = Integer.parseInt(stryear);
                return service.getChiTietThuocByYear(idthuoc,idUser,year);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse getChiTietBenhByYearHandle(String stribenh, String stryear,String striuser, ThongTinKCBService service) throws Exception{
        if(stribenh!=null && stryear!=null && striuser!=null){
            try{
                int idbenh = Integer.parseInt(stribenh);
                int idUser = Integer.parseInt(striuser);
                int year = Integer.parseInt(stryear);
                return service.getChiTietBenhByYear(idbenh,idUser,year);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse getListThuocHandle(ThongTinKCBService service) throws Exception {
        return service.getListThuoc();
    }

    private BaseResponse getListBenhHandle(ThongTinKCBService service) throws Exception {
        return service.getListBenh();
    }

    private BaseResponse editBenhHandle(String stribenh, String strikcb, String thoigian, String bieuhien, ThongTinKCBService service) throws Exception {
        if(stribenh!=null && strikcb!=null && thoigian!=null && bieuhien!=null){
            try{
                int idbenh = Integer.parseInt(stribenh);
                int idkcb = Integer.parseInt(strikcb);
                long time = Long.parseLong(thoigian);
                return service.editBenh(idbenh,idkcb,time,bieuhien);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse editCHuanDoanHandle(String stridkcb, String chuandoan, ThongTinKCBService service) throws Exception {
        if(stridkcb!=null && chuandoan!=null){
            try{
                int id = Integer.parseInt(stridkcb);
                return service.editChuanDoan(id,chuandoan);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse themThongTinKCBHandle(String striduser, String strbv, String chuandoan, String dieutri, String strstarttime,String tenbenh,String thuoc,ThongTinKCBService service) throws Exception {
        if(striduser!=null && strbv!=null && strstarttime!=null && tenbenh!=null && thuoc!=null){
            try {
                int idUser = Integer.parseInt(striduser);
                int isbv = Integer.parseInt(strbv);
                long starttime = Long.parseLong(strstarttime);
                return service.themThongTinKCB(idUser,isbv==1?true:false,starttime,chuandoan,dieutri,tenbenh,thuoc);
            }catch (NumberFormatException e){
                return Utils.responseError(ErrorCode.CANT_CAST_TYPE,"Định dạng dữ liệu gửi lên không đúng.");
            }
        }else{
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

}
