package ncu.im3069.demo.app;

import java.sql.*;
import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class AppointmentHelper {

    private AppointmentHelper() {

    }

    /** 靜態變數，儲存AppointmentHelper物件 */
    private static AppointmentHelper ah;

    /** 儲存JDBC資料庫連線 */
    private Connection conn = null;

    /** 儲存JDBC預準備之SQL指令 */
    private PreparedStatement pres = null;

    public static AppointmentHelper getHelper() {
        /** Singleton檢查是否已經有AppointmentHelper物件，若無則new一個，若有則直接回傳 */
        if (ah == null)
            ah = new AppointmentHelper();

        return ah;
    }

    /**
     * 建立掛號至資料庫
     *
     * @param a 一名掛號之Appointment物件
     * @return the JSON object 回傳SQL指令執行之結果
     */
    public JSONObject create(Appointment a) {
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

            String sql1 = "select * from `sa_project`.`appointment` WHERE `visited_date` = ? order by appointment_number desc limit 1";
            String sql2 = "INSERT IGNORE INTO `sa_project`.`appointment`(`name`, `pid`,`dob`,`visited_date`,`appointment_number`,`clinic_hours`,`done`)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?) ";

            /** 取得所需之參數 */
            String name = a.getName();
            String pid = a.getPID();
            String dob = a.getDob();
            String visited_date = a.getVisitDate();
            String clinic_hours = a.getClinicHours();
            a.setDone(true);
            Boolean done = a.getDone();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql1);
            pres.setString(1, visited_date);
            ResultSet rs = pres.executeQuery();

            pres = conn.prepareStatement(sql2);
            pres.setString(1, name);
            pres.setString(2, pid);
            pres.setString(3, dob);
            pres.setString(4, visited_date);

            if (rs.next()) {
                pres.setInt(5, rs.getInt("appointment_number") + 1);
            } else {
                pres.setInt(5, 1);
            }

            pres.setString(6, clinic_hours);
            pres.setBoolean(7, done);

            /** 執行查詢之SQL指令並記錄其回傳之資料 */
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
     * 取回所有掛號資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public JSONObject getAll(String searching_date) {
        /** 新建一個 Appointment 物件之 a 變數，用於紀錄每一位查詢回之掛號資料 */
        Appointment a = null;
        /** 用於儲存所有檢索回之掛號，以JSONArray方式儲存 */
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
            String sql = "SELECT * FROM `sa_project`.`appointment` WHERE `done` = true AND `visited_date` = ? ORDER BY appointment_number";
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, searching_date);

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
                String name = rs.getString("name");
                String pid = rs.getString("pid");
                String dob = rs.getString("dob");
                String visited_date = rs.getString("visited_date");
                int appointment_number = rs.getInt("appointment_number");
                String clinic_hours = rs.getString("clinic_hours");
                String doctor_name = getDoctorName(rs.getString("clinic_hours"));

                /** 將每一筆掛號資料產生一名新Appointment物件 */
                a = new Appointment(id, pid, name, dob, visited_date, appointment_number, clinic_hours, doctor_name);

                // a = new Appointment(id, pid, name, dob, visited_date, appointment_number,
                // clinic_hours, doctor_name);

                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                jsa.put(a.getData());
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
     * 取回所有掛號資料
     *
     * @return the JSONObject 回傳SQL執行結果與自資料庫取回之所有資料
     */
    public String getDoctorName(String clinic_hours) {
        /** 新建一個 Appointment 物件之 a 變數，用於紀錄每一位查詢回之看診醫師資料 */
        // Appointment a = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄SQL總行數 */
        // int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        String doctor_name = null;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT outpatient.doctor_name FROM `sa_project`.`outpatient` right join `sa_project`.`appointment` on outpatient.clinic_hours = ? LIMIT 1";

            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
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
                // row += 1;

                /** 將 ResultSet 之資料取出 */
                doctor_name = rs.getString("doctor_name");
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

        return doctor_name;
    }

    /**
     * 透過病患身份證字號（PID）取得掛號資料
     *
     * @param pid 病患編號
     * @return the JSON object 回傳SQL執行結果與該病患編號之病患資料
     */
    public JSONObject getByID(String id) {
        /** 新建一個 Appointment 物件之 a 變數，用於紀錄每一位查詢回之掛號資料 */
        Appointment a = null;
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
            String sql = "SELECT * FROM `sa_project`.`appointment` WHERE done = true AND `id` = ? LIMIT 1";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);

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
                String name = rs.getString("name");
                String pid = rs.getString("pid");
                String dob = rs.getString("dob");
                String visited_date = rs.getString("visited_date");
                String clinic_hours = rs.getString("clinic_hours");

                /** 將每一筆病患資料產生一名新Member物件 */
                a = new Appointment(pid, name, dob, visited_date, clinic_hours);

                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(a.getData());
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
     * 透過病患身份證字號（PID）取得掛號資料
     *
     * @param pid 病患編號
     * @return the JSON object 回傳SQL執行結果與該病患編號之病患資料
     */
    public JSONObject getByPID(String pid) {
        /** 新建一個 Appointment 物件之 a 變數，用於紀錄每一位查詢回之掛號資料 */
        Appointment a = null;
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
            String sql = "SELECT * FROM `sa_project`.`appointment` WHERE done = true AND `pid` = ? LIMIT 1";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, pid);

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
                String name = rs.getString("name");
                String dob = rs.getString("dob");
                String visited_date = rs.getString("visited_date");
                int appointment_number = rs.getInt("appointment_number");
                String clinic_hours = rs.getString("clinic_hours");
                String doctor_name = getDoctorName(rs.getString("clinic_hours"));

                /** 將每一筆病患資料產生一名新Member物件 */
                a = new Appointment(id, pid, name, dob, visited_date, appointment_number, clinic_hours, doctor_name);

                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(a.getData());
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
     * 透過病患看診時段取得掛號資料
     *
     * @param clinic_hours 病患看診時段
     * @return the JSON object 回傳SQL執行結果與該病患看診時段之掛號資料
     */
    public JSONObject getByClinicHours(String clinic_hours) {
        /** 新建一個 Appointment 物件之 a 變數，用於紀錄每一位查詢回之掛號資料 */
        Appointment a = null;
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
            String sql = "SELECT `clinic_hours` FROM `sa_project`.`appointment` WHERE `clinic_hours` = ? ";

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
                String name = rs.getString("name");
                String pid = rs.getString("pid");
                String dob = rs.getString("dob");
                String visited_date = rs.getString("visited_date");

                /** 將每一筆病患資料產生一名新Member物件 */
                a = new Appointment(pid, name, dob, visited_date, clinic_hours);

                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(a.getData());
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

    public JSONObject getByVisitDate(String visited_date) {
        /** 新建一個 Appointment 物件之 a 變數，用於紀錄每一位查詢回之掛號資料 */
        Appointment a = null;
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
            String sql = "SELECT `visited_date` FROM `sa_project`.`appointment` WHERE `visited_date` = ? ";

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, visited_date);
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
                String name = rs.getString("name");
                String pid = rs.getString("pid");
                String dob = rs.getString("dob");
                String clinic_hours = rs.getString("clinic_hours");

                /** 將每一筆病患資料產生一名新Member物件 */
                a = new Appointment(pid, name, dob, visited_date, clinic_hours);

                /** 取出該名病患之資料並封裝至 JSONsonArray 內 */
                jsa.put(a.getData());
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
     * 過號，更新病患之掛號序號
     *
     * @return the JSON object 回傳SQL執行結果
     */
    public JSONObject passAppointment(Appointment a) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        ResultSet rs = null;

        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /**
             * SQL指令 sql1 是取出最後一筆掛號序號 sql2 是更新病患掛號序號
             */

            String sql1 = "select * from `sa_project`.`appointment` WHERE `done` = true AND `visited_date` = ? order by appointment_number desc limit 1";
            String sql2 = "update `sa_project`.`appointment` set `appointment_number` = ? where `id` = ?";

            /** 取得所需之參數 */
            int id = a.getID();
            String visited_date = a.getVisitDate();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql1);
            pres.setString(1, visited_date);

            rs = pres.executeQuery();
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

            rs.next();
            pres = conn.prepareStatement(sql2);

            pres.setInt(1, rs.getInt("appointment_number") + 1);
            pres.setInt(2, id);

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
     * 更新病患之掛號看診完成紀錄
     *
     * @return the JSON object 回傳SQL執行結果
     */
    public JSONObject updateAppointment(Appointment a) {
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
            String sql = "Update `sa_project`.`appointment` SET `done` = ?  WHERE `id` = ?";

            /** 取得所需之參數 */
            a.setDone(false);
            Boolean done = a.getDone();
            int id = a.getID();

            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setBoolean(1, done);
            pres.setInt(2, id);

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

}