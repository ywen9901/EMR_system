package ncu.im3069.demo.app;

import java.sql.*;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class OutpatientHelper {

    private OutpatientHelper() {

    }

    /** 靜態變數，儲存OutpatientHelper物件 */
    private static OutpatientHelper oh;

    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;

    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;

    public static OutpatientHelper getHelper() {
        /** Singleton檢查是否已經有OutpatientHelper物件，若無則new一個，若有則直接回傳 */
        if (oh == null)
            oh = new OutpatientHelper();

        return oh;
    }

    /**
     * 透過醫師編號（doctor_id）取得門診資料
     *
     * @param doctor_id 醫師編號
     * @return the JSON object 回傳SQL執行結果與該醫師編號之門診資料
     */
    public JSONObject getByDoctorID(String doctor_id) {
        /** 新建一個 Appointment 物件之 a 變數，用於紀錄每一位查詢回之掛號資料 */
        OutPatient o = null;
        /** 用於儲存所有檢索回之病患，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa_project`.`outpatient` WHERE `doctor_id` = ?";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, doctor_id);

            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while (rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;

                /** 將 ResultSet 之資料取出 */
                int id = rs.getInt("id");
                int int_doctor_id = Integer.parseInt(doctor_id);
                String clinic_hours = rs.getString("clinic_hours");
                String doctor_name = rs.getString("doctor_name");

                /** 將每一筆病患資料產生一名新Member物件 */
                o = new OutPatient(id, int_doctor_id, clinic_hours, doctor_name);

                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(o.getData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間、影響行數與所有病患資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

    /**
     * 透過看診時段（clinic_hours）取得門診資料
     *
     * @param clinic_hours 醫師編號
     * @return the JSON object 回傳SQL執行結果與該醫師編號之門診資料
     */
    public JSONObject getByClinicHours(String clinic_hours) {
        /** 新建一個 Appointment 物件之 a 變數，用於紀錄每一位查詢回之掛號資料 */
        OutPatient o = null;
        /** 用於儲存所有檢索回之病患，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `sa_project`.`outpatient` WHERE `clinic_hours` = ?";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, clinic_hours);

            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while (rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;

                /** 將 ResultSet 之資料取出 */
                int id = rs.getInt("id");
                int doctor_id = rs.getInt("doctor_id");
                String doctor_name = rs.getString("doctor_name");

                /** 將每一筆病患資料產生一名新Member物件 */
                o = new OutPatient(id, doctor_id, clinic_hours, doctor_name);

                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(o.getData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間、影響行數與所有病患資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

}
