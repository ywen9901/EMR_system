package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class RecordHelper {

    private static RecordHelper rh;
    private Connection conn = null;
    private PreparedStatement pres = null;

    private RecordHelper() {
    }

    public static RecordHelper getHelper() {
        if (rh == null)
            rh = new RecordHelper();

        return rh;
    }

    public JSONObject create(Record r) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        int row = 0;
        long start_time = System.nanoTime();

        // long id = -1;
        // JSONArray recordArray = new JSONArray();

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `sa_project`.`record`(`patient_id`,`symptoms`, `days`, `degree`, `Medicine_id`, `note`, `visited_date`, `doctor`,`edited_by`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            /** 取得所需之參數 */
            int patient_id = r.getPatient_id();
            String symptoms = r.getSymptoms();
            String days = r.getDays();
            String degree = r.getDegree();
            int Medicine_id = r.getMedicine_id();
            String note = r.getNote();
            String visited_date = r.getVisited_date();
            String doctor = r.getDoctor();
            String edited_by = r.getEdited_by();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, patient_id);
            pres.setString(2, symptoms);
            pres.setString(3, days);
            pres.setString(4, degree);
            pres.setInt(5, Medicine_id);
            pres.setString(6, note);
            pres.setString(7, visited_date);
            pres.setString(8, doctor);
            pres.setString(9, edited_by);

            /** 執行新增之SQL指令並記錄影響之行數 */
            pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("time", duration);
        response.put("row", row);

        return response;
    }

    public JSONObject update(Record r) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `sa_project`.`record` SET `symptoms` = ? ,`days` = ? ,`degree` = ? ,`Medicine_id` = ? ,`note` = ? ,`visited_date` = ? , `doctor` = ?,`edited_by` = ?  WHERE `id` = ?";

            /** 取得所需之參數 */
            int id = r.getId();
            String symptoms = r.getSymptoms();
            String days = r.getDays();
            String degree = r.getDegree();
            int Medicine_id = r.getMedicine_id();
            String note = r.getNote();
            String visited_date = r.getVisited_date();
            String doctor = r.getDoctor();
            String edited_by = r.getEdited_by();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, symptoms);
            pres.setString(2, days);
            pres.setString(3, degree);
            pres.setInt(4, Medicine_id);
            pres.setString(5, note);
            pres.setString(6, visited_date);
            pres.setString(7, doctor);
            pres.setString(8, edited_by);
            pres.setInt(9, id);

            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

    public JSONObject getAll() {
        Record r = null;
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
            String sql = "SELECT * FROM `sa_project`.`record`";

            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
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
                int patient_id = rs.getInt("patient_id");
                String symptoms = rs.getString("symptoms");
                String days = rs.getString("days");
                String degree = rs.getString("degree");
                int Medicine_id = rs.getInt("Medicine_id");
                String note = rs.getString("note");
                String visited_date = rs.getString("visited_date");
                String doctor = rs.getString("doctor");
                String edited_by = rs.getString("edited_by");

                /** 將每一筆商品資料產生一名新Product物件 */
                r = new Record(id, patient_id, symptoms, days, degree, Medicine_id, note, visited_date, doctor,
                        edited_by);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(r.getRecordData());
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

        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

    public JSONObject getById(String record_id) {
        // JSONObject data = new JSONObject();
        Record r = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        JSONArray jsa = new JSONArray();

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
            String sql = "SELECT * FROM `sa_project`.`record` WHERE `id` = ? LIMIT 1";

            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, record_id);
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
                int rid = rs.getInt("id");
                int patient_id = rs.getInt("patient_id");
                String symptoms = rs.getString("symptoms");
                String days = rs.getString("days");
                String degree = rs.getString("degree");
                int Medicine_id = rs.getInt("Medicine_id");
                String note = rs.getString("note");
                String visited_date = rs.getString("visited_date");
                String doctor = rs.getString("doctor");
                String edited_by = rs.getString("edited_by");

                /** 將每一筆商品資料產生一名新Product物件 */
                r = new Record(rid, patient_id, symptoms, days, degree, Medicine_id, note, visited_date, doctor,
                        edited_by);

                // /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(r.getRecordData());
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

        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */

        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

    public JSONObject getByPId(String patient_id) {
        // JSONObject data = new JSONObject();
        Record r = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        JSONArray jsa = new JSONArray();

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
            String sql = "SELECT * FROM `sa_project`.`record` WHERE `patient_id` = ?";

            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, patient_id);
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
                int rid = rs.getInt("id");
                int patientid = rs.getInt("patient_id");
                String symptoms = rs.getString("symptoms");
                String days = rs.getString("days");
                String degree = rs.getString("degree");
                int Medicine_id = rs.getInt("Medicine_id");
                String note = rs.getString("note");
                String visited_date = rs.getString("visited_date");
                String doctor = rs.getString("doctor");
                String edited_by = rs.getString("edited_by");

                /** 將每一筆商品資料產生一名新Product物件 */
                r = new Record(rid, patientid, symptoms, days, degree, Medicine_id, note, visited_date, doctor,
                        edited_by);

                // /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(r.getRecordData());
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

        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */

        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }

    /**
     * 透過編號（ID）刪除看診紀錄
     *
     * @param id 看診紀錄編號
     * @return the JSONObject 回傳SQL執行結果
     */
    public JSONObject deleteByID(int id) {
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
            String sql = "DELETE FROM `sa_project`.`record` WHERE `id` = ? LIMIT 1";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行刪除之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

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

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);

        return response;
    }
}
