package pn.doan.apis.handles;

import io.vertx.core.http.HttpServerRequest;
import pn.doan.apis.response.BaseResponse;
import pn.doan.core.model.ErrorCode;
import pn.doan.core.service.Imp.NguoiDungServiceImp;
import pn.doan.core.service.NguoiDungService;
import pn.doan.core.utils.Utils;

public class NguoiDungHandler extends BaseApiHandler {
    @Override
    public BaseResponse handle(HttpServerRequest request) throws Exception {
        NguoiDungService service = new NguoiDungServiceImp();
        String type = request.getParam("t");
        if(type.equals("register")){
            String email = request.getParam("email");
            String hoten = request.getParam("hoten");
            String ngaysinh = request.getParam("ngaysinh");
            String gioitinh = request.getParam("gioitinh");
            String sdt = request.getParam("sdt");
            String matkhau = request.getParam("matkhau");
            return registerHandle(email,hoten,ngaysinh,gioitinh,sdt,matkhau,service);
        }else if(type.equals("login")){
            String email = request.getParam("email");
            String matkhau = request.getParam("matkhau");
            return loginHandle(email,matkhau,service);
        }else if(type.equals("changepass")){
            String email = request.getParam("email");
            String matkhau = request.getParam("matkhau");
            String matkhaumoi = request.getParam("matkhaumoi");
            return changeMatkhauHandle(email,matkhau,matkhaumoi,service);
        }else if(type.equals("changeemail")){
            String email = request.getParam("email");
            String matkhau = request.getParam("matkhau");
            String emailmoi = request.getParam("emailmoi");
            return changeEmailHandle(email,matkhau,emailmoi,service);
        }else if(type.equals("changesdt")){
            String email = request.getParam("email");
            String matkhau = request.getParam("matkhau");
            String sdt = request.getParam("sdtmoi");
            return changeSDTHandle(email,matkhau,sdt,service);
        }else if(type.equals("changeinfo")){
            String email = request.getParam("email");
            String matkhau = request.getParam("matkhau");
            String hoten = request.getParam("hoten");
            String ngaysinh = request.getParam("ngaysinh");
            String gioitinh = request.getParam("gioitinh");
            String diachi = request.getParam("diachi");
            return changeInfoHandle(email,hoten,ngaysinh,gioitinh,matkhau,diachi,service);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse changeSDTHandle(String email, String matkhau, String sdt, NguoiDungService service) throws Exception {
        if(email!=null && matkhau!=null && sdt!=null){
            return service.changeSDT(email,matkhau,sdt);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse changeEmailHandle(String email, String matkhau, String emailmoi, NguoiDungService service) throws Exception {
        if(email!=null && matkhau!=null && emailmoi!=null){
            return service.changeEmail(email,matkhau,emailmoi);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse changeInfoHandle(String email, String hoten, String ngaysinh, String gioitinh, String matkhau, String diachi, NguoiDungService service) throws  Exception{
        if(email!=null && hoten!=null && ngaysinh!=null && gioitinh!=null && matkhau!=null && diachi!=null){
            int gt = Integer.parseInt(gioitinh);
            return service.changeInfo(email,hoten,ngaysinh,gt==1?true:false,matkhau,diachi);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse changeMatkhauHandle(String email, String matkhau, String matkhaumoi, NguoiDungService service) throws Exception {
        if(email!=null && matkhau!=null && matkhaumoi!=null){
            return service.changeMatKhau(email,matkhau,matkhaumoi);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse loginHandle(String email, String matkhau,NguoiDungService service) throws Exception {
        if(email!=null && matkhau!=null){
            return service.login(email,matkhau);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }

    private BaseResponse registerHandle(String email, String hoten, String ngaysinh, String gioitinh, String sdt, String matkhau, NguoiDungService service) throws Exception{
        if(email!=null && hoten!=null && ngaysinh!=null && gioitinh!=null && sdt!=null && matkhau!=null){
            int gt = Integer.parseInt(gioitinh);
            return service.register(email,hoten,ngaysinh,gt==1?true:false,sdt,matkhau);
        }else {
            return Utils.responseError(ErrorCode.INVALID_PARAMS,"Invalid params");
        }
    }
}
