import com.google.gson.JsonArray;
import pn.doan.core.config.HikariPool;
import pn.doan.core.model.NguoiDung;
import pn.doan.core.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

public class TestHikari {
    public static void main(String[] args) {
        try {
            HikariPool.init();
            System.out.println("start");
//            String abc = "[{\"tenbenh\": \"benh1\",\"bieuhien\":\"bieuhien1\",\"tghetbenh\":1523814984000}," +
//                    "{\"tenbenh\": \"benh2\",\"bieuhien\":\"bieuhien2\",\"tghetbenh\":1523814984000}," +
//                    "{\"tenbenh\": \"benh3\",\"bieuhien\":\"bieuhien3\",\"tghetbenh\":1523814984000}]";
//            JsonArray array = Utils.toJsonArray(abc);
//            for (int i=0;i<array.size();i++){
//                System.out.println("name: "+array.get(i).getAsJsonObject().get("name").getAsString());
//                System.out.println("name: "+array.get(i).getAsJsonObject().get("age").getAsInt());
//                System.out.println("------");
//            }
            Calendar calendar = Calendar.getInstance();
            System.out.println(calendar.get(Calendar.MONTH));
            System.out.println("end.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
