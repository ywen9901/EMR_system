package ncu.im3069.demo.controller;

import java.io.*;
// import java.sql.Timestamp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.Cookie;

import org.json.*;
import ncu.im3069.demo.app.Administration;
import ncu.im3069.demo.app.AdministrationHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/administration.do")
public class AdministrationController extends HttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private AdministrationHelper adh = AdministrationHelper.getHelper();

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
        else if (adh.checkLogin(job, account, password)) {

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            int administration_id = 0;
            JSONObject resp = new JSONObject();
            administration_id = adh.getIDByAccount(account);
            resp.put("status", "200");
            resp.put("message", "成功登入！id=" + administration_id);
            resp.put("id", administration_id);

            Cookie ck = new Cookie("administration_id", String.valueOf(administration_id));
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
        // String name = jsr.getParameter("name");
        /** 判斷該字串是否存在，若存在代表要取回個別櫃台行政之資料，否則代表要取回全部資料庫內櫃台行政之資料 */
        if (id.isEmpty()) {
            /** 透過MemberHelper物件之getAll()方法取回所有櫃台行政之資料，回傳之資料為JSONObject物件 */
            JSONObject query = adh.getAll();

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有櫃台行政資料取得成功");
            resp.put("response", query);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        } else {
            /** 透過AdministrationHelper物件的getByID()方法自資料庫取回該名櫃台行政之資料，回傳之資料為JSONObject物件 */
            JSONObject query = adh.getByID(id);

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "櫃台行政資料取得成功");
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

        /** 透過AdministrationHelper物件的deleteByID()方法至資料庫刪除該名櫃台行政，回傳之資料為JSONObject物件 */
        JSONObject query = adh.deleteByID(id);

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "櫃台行政移除成功！");
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

        /** 透過傳入之參數，新建一個以這些參數之櫃台行政Member物件 */
        Administration ad = new Administration(id, account, password, name, dob, phone, address);

        /** 透過Administration物件的update()方法至資料庫更新該名櫃台行政資料，回傳之資料為JSONObject物件 */
        JSONObject data = ad.update();

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新櫃台行政資料");
        resp.put("response", data);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
}
