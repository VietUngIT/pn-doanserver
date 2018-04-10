package pn.doan.apis;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.doan.core.config.APIConfig;
import pn.doan.core.config.HikariPool;
import pn.doan.core.config.VertXHttpConfig;

public class APILauncher {
    private static Logger logger = LoggerFactory.getLogger(APILauncher.class.getName());
    public static void main(String[] args) {
        runVertx();
    }

    public static void runVertx() {

        try {
            PropertyConfigurator.configure("config/log4j.properties");
            APIConfig.init();
            logger.info("APIConfig is initialed");
            HikariPool.init();
            logger.info("HikariPool is initialed");
            Vertx vertx = Vertx.vertx();
            int procs = Runtime.getRuntime().availableProcessors();
            vertx.deployVerticle(VertXHttpConfig.class.getName(),
                    new DeploymentOptions().setInstances(procs*2), event -> {
                        if (event.succeeded()) {
                            logger.debug("Vert.x application is started!");
                        } else {

                            logger.error("Unable to start your application", event.cause());
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
