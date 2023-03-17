package ncu.im3069.demo.controller;

import java.io.*;
/**import java.sql.Timestamp;
*/

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.Cookie;

import org.json.*;
import ncu.im3069.demo.app.Doctor;
import ncu.im3069.demo.app.DoctorHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/doctor.do")
public class DoctorController extends HttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** dh，DoctorHelper之物件與Doctor相關之資料庫方法（Sigleton） */
    private DoctorHelper dh = DoctorHelper.getHelper();

    /**
     * 處理Http Method請求POST方法（新增資料）
     *
     * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到JSONObject之Request參數 */
        String job = jso.getString("job");
        String account = jso.getString("account");
        String password = jso.getString("password");

        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if (account.isEmpty() || password.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        /** 透過DoctorHelper物件的checkDuplicate()檢查該醫師帳號是否有重複 */
        else if (dh.checkLogin(job, account, password)) {

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            int doctor_id = 0;
            JSONObject resp = new JSONObject();
            doctor_id = dh.getIDByAccount(account);
            resp.put("status", "200");
            resp.put("message", "成功登入！id=" + doctor_id);
            resp.put("id", doctor_id);

            Cookie ck = new Cookie("doctor_id", String.valueOf(doctor_id));
            response.addCookie(ck);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        } else {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'403\', \"message\": \'帳號或密碼不符！\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
    }

    /**
     * 處理Http Method請求GET方法（取得資料）
     *
     * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("id");

        /**
         * String name =jsr.getParameter("name"); / /**
         * 判斷該字串是否存在，若存在代表要取回個別醫師之資料，否則代表要取回全部資料庫內醫師之資料
         */
        if (id.isEmpty()) {
            /** 透過DoctorHelper物件之getAll()方法取回所有醫師之資料，回傳之資料為JSONObject物件 */
            JSONObject query = dh.getAll();

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有醫師資料取得成功");
            resp.put("response", query);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        } else {
            /** 透過DoctorHelper物件的getByID()方法自資料庫取回該名醫師之資料，回傳之資料為JSONObject物件 */
            JSONObject query = dh.getByID(id);

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "醫師資料取得成功");
            resp.put("response", query);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
    }

    /**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");

        /** 透過DoctorHelper物件的deleteByID()方法至資料庫刪除該名醫師，回傳之資料為JSONObject物件 */
        JSONObject query = dh.deleteByID(id);

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "醫師移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

    /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request  Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        String account = jso.getString("account");
        String password = jso.getString("password");
        String name = jso.getString("name");
        String dob = jso.getString("dob");
        int phone = jso.getInt("phone");
        String address = jso.getString("address");
        /**
         * Timestamp create_date = jso.getTimestamp("create_date"); Timestamp
         * modify_date = jso.getTimestamp("modify_date");
         */

        /** 透過傳入之參數，新建一個以這些參數之醫師Doctor物件 */
        Doctor d = new Doctor(id, account, password, name, dob, phone, address);

        /** 透過Doctor物件的update()方法至資料庫更新該名醫師資料，回傳之資料為JSONObject物件 */
        JSONObject data = d.update();

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新醫師資料...");
        resp.put("response", data);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
    /**
     * public void doPost(HttpServletRequest request, HttpServletResponse response)
     * throws ServletException, IOException { /**
     * 透過JsonReader類別將Request之JSON格式資料解析並取回
     */
    // JsonReader jsr = new JsonReader(request);
    /**
     * JSONObject jso = jsr.getObject();
     *
     * /** 取出經解析到JSONObject之Request參數
     */
    // String account = jso.getString("account");
    // String password = jso.getString("password");
    // String name = jso.getString("name");
    // String dob = jso.getString("dob");
    // int phone = jso.getInt("phone");
    // String address = jso.getString("address");

    /** 建立一個新的醫師物件 */
    // Doctor d = new Doctor(account, password, name, dob, phone, address);

    /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
    // if (account.isEmpty() || password.isEmpty() || name.isEmpty() ||
    // dob.isEmpty()
    // || Integer.toString(phone).isEmpty() || address.isEmpty()) {
    /** 以字串組出JSON格式之資料 */
    // String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\':
    // \'\'}";
    /** 透過JsonReader物件回傳到前端（以字串方式） */
    // jsr.response(resp, response);
    // }
    /** 透過DoctorHelper物件的checkDuplicate()檢查該醫師帳號是否有重複 */
    // else if (!dh.checkDuplicate(d)) {
    /** 透過DoctorHelper物件的create()方法新建一個醫師至資料庫 */
    // JSONObject data = dh.create(d);

    /** 新建一個JSONObject用於將回傳之資料進行封裝 */
    // JSONObject resp = new JSONObject();
    // resp.put("status", "200");
    // resp.put("message", "成功! 註冊醫師資料！");
    // resp.put("response", data);

    /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
    // jsr.response(resp, response);
    // } else {
    /** 以字串組出JSON格式之資料 */
    // String resp = "{\"status\": \'400\', \"message\": \'新增資料失敗，此帳號重複！\',
    // \'response\': \'\'}";
    /** 透過JsonReader物件回傳到前端（以字串方式） */
    // jsr.response(resp, response);
    // }

}
