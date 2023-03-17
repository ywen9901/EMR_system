package ncu.im3069.demo.app;

import java.sql.*;
import java.util.Date;
import java.time.LocalDateTime;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class PatientHelper {

    /**
     * 實例化（Instantiates）一個新的（new）PatientHelper物件<br>
     * 採用Singleton不需要透過new
     */
    private PatientHelper() {

    }

    /** 靜態變數，儲存PatientHelper物件 */
    private static PatientHelper ph;

    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;

    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;

    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個PatientHelper物件
     *
     * @return the helper 回傳PatientHelper物件
     */
    public static PatientHelper getHelper() {
        /** Singleton檢查是否已經有PatientHelper物件，若無則new一個，若有則直接回傳 */
        if (ph == null)
            ph = new PatientHelper();

        return ph;
    }

    /**
     * 建立該名病患至資料庫
     *
     * @param p 一名病患之Patient物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(Patient p) {
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
            String sql = "INSERT INTO `sa_project`.`patient`(`pid`, `name`, `gender`,`dob`, `bloodType`,`phone`,`address`,`specialDisease`,`drugAllergy`,`create_date`,`modify_date`, `edited_by`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            /** 取得所需之參數 */
            String pid = p.getPID();
            String name = p.getName();
            String gender = p.getGender();
            String dob = p.getDob();
            String bloodType = p.getBloodType();
            int phone = p.getPhone();
            String address = p.getAddress();
            String specialDisease = p.getSpecialDisease();
            String drugAllergy = p.getDrugAllergy();
            String editedby = p.geteditedby();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, pid);
            pres.setString(2, name);
            pres.setString(3, gender);
            pres.setString(4, dob);
            pres.setString(5, bloodType);
            pres.setInt(6, phone);
            pres.setString(7, address);
            pres.setString(8, specialDisease);
            pres.setString(9, drugAllergy);
            pres.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pres.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pres.setString(12, editedby);

            /** 執行新增之SQL指令並記錄影響之行數 */
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
        response.put("time", duration);
        response.put("row", row);

        return response;
    }

    /**
     * 更新一名病患之病患資料
     *
     * @param p 一名病患之Patient物件
     * @return the JSONObject 回傳SQL指令執行結果與執行之資料
     */
    public JSONObject update(Patient p) {
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
            String sql = "Update `sa_project`.`patient` SET `pid` = ?,`name` = ? ,`gender` = ? ,`dob` = ? ,`bloodType` = ? ,`phone` = ? ,`address` = ? ,`specialDisease` = ? ,`drugAllergy` = ? , `modify_date` = ?,`edited_by` = ?  WHERE `pid` = ?";

            /** 取得所需之參數 */
            String pid = p.getPID();
            String name = p.getName();
            String gender = p.getGender();
            String dob = p.getDob();
            String bloodType = p.getBloodType();
            int phone = p.getPhone();
            String address = p.getAddress();
            String specialDisease = p.getSpecialDisease();
            String drugAllergy = p.getDrugAllergy();
            String editedby = p.geteditedby();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, pid);
            pres.setString(2, name);
            pres.setString(3, gender);
            pres.setString(4, dob);
            pres.setString(5, bloodType);
            pres.setInt(6, phone);
            pres.setString(7, address);
            pres.setString(8, specialDisease);
            pres.setString(9, drugAllergy);
            pres.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pres.setString(11, editedby);
            pres.setString(12, pid);

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

    /**
     * 取回所有病患資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public JSONObject getAll() {
        /** 新建一個 Patient 物件之 p 變數，用於紀錄每一位查詢回之病患資料 */
        Patient p = null;
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
            String sql = "SELECT * FROM `sa_project`.`patient`";

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
                int patient_id = rs.getInt("id");
                String pid = rs.getString("pid");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String dob = rs.getString("dob");
                String bloodType = rs.getString("bloodType");
                int phone = rs.getInt("phone");
                String address = rs.getString("address");
                String specialDisease = rs.getString("specialDisease");
                String drugAllergy = rs.getString("drugAllergy");
                Timestamp create_date = rs.getTimestamp("create_date");
                Timestamp modify_date = rs.getTimestamp("modify_date");

                /** 將每一筆病患資料產生一名新Patient物件 */
                p = new Patient(patient_id, pid, name, gender, dob, bloodType, phone, address, specialDisease,
                        drugAllergy, create_date, modify_date);
                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(p.getData());
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
     * 透過病患編號（ID）取得病患資料
     *
     * @param id 病患編號
     * @return the JSON object 回傳SQL執行結果與該病患編號之病患資料
     */
    public JSONObject getByID(String id) {
        /** 新建一個 Patient 物件之 p 變數，用於紀錄每一位查詢回之病患資料 */
        Patient p = null;
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
            String sql = "SELECT * FROM `sa_project`.`patient` WHERE `id` = ? LIMIT 1";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該病患編號之資料，因此其實可以不用使用 while 迴圈 */
            while (rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;

                /** 將 ResultSet 之資料取出 */
                int patient_id = rs.getInt("id");
                String pid = rs.getString("pid");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String dob = rs.getString("dob");
                String bloodType = rs.getString("bloodType");
                int phone = rs.getInt("phone");
                String address = rs.getString("address");
                String specialDisease = rs.getString("specialDisease");
                String drugAllergy = rs.getString("drugAllergy");
                Timestamp create_date = rs.getTimestamp("create_date");
                Timestamp modify_date = rs.getTimestamp("modify_date");
                /** 將每一筆病患資料產生一名新Member物件 */
                p = new Patient(patient_id, pid, name, gender, dob, bloodType, phone, address, specialDisease,
                        drugAllergy, create_date, modify_date);

                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(p.getData());
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
     * 透過病患身分證（PID）取得病患資料
     *
     * @param pid 病患編號
     * @return the JSON object 回傳SQL執行結果與該病患編號之病患資料
     */
    public JSONObject getByPID(String pid) {
        /** 新建一個 Patient 物件之 p 變數，用於紀錄每一位查詢回之病患資料 */
        Patient p = null;
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
            String sql = "SELECT * FROM `sa_project`.`patient` WHERE `pid` = ? LIMIT 1";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, pid);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            /** 正確來說資料庫只會有一筆該病患編號之資料，因此其實可以不用使用 while 迴圈 */
            while (rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;

                /** 將 ResultSet 之資料取出 */
                int patient_id = rs.getInt("id");
                String patient_pid = rs.getString("pid");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String dob = rs.getString("dob");
                String bloodType = rs.getString("bloodType");
                int phone = rs.getInt("phone");
                String address = rs.getString("address");
                String specialDisease = rs.getString("specialDisease");
                String drugAllergy = rs.getString("drugAllergy");
                Timestamp create_date = rs.getTimestamp("create_date");
                Timestamp modify_date = rs.getTimestamp("modify_date");

                /** 將每一筆病患資料產生一名新Member物件 */
                p = new Patient(patient_id, patient_pid, name, gender, dob, bloodType, phone, address, specialDisease,
                        drugAllergy, create_date, modify_date);

                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(p.getData());
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


    public boolean checkDuplicate(Patient p) {
        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = -1;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT count(*) FROM `sa_project`.`patient` WHERE `pid` = ?";

            /** 取得所需之參數 */
            String pid = p.getPID();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, pid);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            rs.next();
            row = rs.getInt("count(*)");
            System.out.print(row);

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

        /**
         * 判斷是否已經有一筆該病患之資料 若無一筆則回傳False，否則回傳True
         */
        return (row == 0) ? false : true;
    }

}
