package pn.doan.core.config;

import io.netty.util.AsciiString;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.doan.apis.handles.BaseApiHandler;
import pn.doan.apis.response.BaseResponse;
import pn.doan.apis.response.SimpleResponse;
import pn.doan.core.model.ErrorCode;

public class VertXHttpConfig extends AbstractVerticle implements Handler<HttpServerRequest> {
    static Logger logger = LoggerFactory.getLogger(VertXHttpConfig.class.getName());
    private HttpServer server;
    private static final CharSequence RESPONSE_TYPE_JSON = new AsciiString("application/json");

    @Override
    public void start() {
        int port = APIConfig.PORT;
        server = vertx.createHttpServer();
        server.requestHandler(VertXHttpConfig.this).listen(port);
        logger.debug("start on port {}", port);
    }

    @Override
    public void handle(HttpServerRequest request) {
        BaseResponse response = null;
        BaseApiHandler handler = APIConfig.getHandler(request.path());
        try {
            if (handler != null) {
                switch (handler.getMethod()) {
                    case "GET":
                        handleGet(handler, request);
                        break;
                    case "POST":
                        handlePOST(handler, request);
                        break;
                }
            } else {
                response = new SimpleResponse();
                response.setError(ErrorCode.HANDLER_NOT_FOUND);
                response.setMsg("Handler not found.");
                makeHttpResponse(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error Request", e.toString());
            response = new SimpleResponse();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg(e.getMessage());
            makeHttpResponse(request, response);
        }
    }

    private void handleGet(BaseApiHandler handler, HttpServerRequest request) throws Exception {
        BaseResponse response = null;
        if (handler.isPublic()) {
            response = handler.handle(request);
        } else {
            String email = request.getParam("email");
            String pass = request.getParam("pass");
            response = handlePrivateRequest(handler, request, email,pass);
        }
        if (response == null) {
            logger.error("Response NULL: {}", request.path());
            response = new SimpleResponse();
            response.setError(ErrorCode.SYSTEM_ERROR);
            response.setMsg("Lỗi hệ thống.");
        }
        makeHttpResponse(request, response);
    }

    private void handlePOST(BaseApiHandler handler, HttpServerRequest request) throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        request.setExpectMultipart(true);
        request.endHandler(req -> {
            BaseResponse response = null;
            try {
                if (handler.isPublic()) {
                    response = handler.handle(request);
                } else {
                    String email = request.getFormAttribute("email");
                    String pass = request.getFormAttribute("pass");
                    response = handlePrivateRequest(handler, request, email, pass);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = new SimpleResponse();
                response.setError(ErrorCode.SYSTEM_ERROR);
                response.setMsg("Lỗi hệ thống.");
            }
            makeHttpResponse(request, response);
        });
    }
    private BaseResponse handlePrivateRequest(BaseApiHandler handler, HttpServerRequest request, String phone, String pass) throws Exception {
        BaseResponse response = null;
        if (phone != null) {
            boolean securityCheck = handler.checkSecurityCheck(phone,pass);
            if (securityCheck) {
                response = handler.handle(request);
            } else {
                response = new SimpleResponse();
                response.setError(ErrorCode.NOT_AUTHORISED);
                response.setMsg("Lỗi xác thực.");
            }
        } else {
            response = new SimpleResponse();
            response.setError(ErrorCode.INVALID_PARAMS);
            response.setMsg("---> phone invalid params.");
        }
        return response;
    }

    public void allowAccessControlOrigin(HttpServerRequest request) {
        request.response().putHeader("Access-Control-Allow-Origin", "*");
        request.response().putHeader("Access-Control-Allow-Credentials", "true");
        request.response().putHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
        request.response().putHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
    }

    private void makeHttpResponse(HttpServerRequest request, BaseResponse response) {
        allowAccessControlOrigin(request);
        String content = response.toJonString();
        CharSequence contentLength = new AsciiString(String.valueOf(content.length()));
        Buffer contentBuffer = Buffer.buffer(content);
        request.response().putHeader("CONTENT_TYPE", RESPONSE_TYPE_JSON)
                .putHeader("CONTENT_LENGTH", contentLength).end(contentBuffer);
    }

    @Override
    public void stop() {
        if (server != null) server.close();
    }
}
