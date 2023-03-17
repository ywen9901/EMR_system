package ncu.im3069.demo.controller;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.demo.app.Patient;
import ncu.im3069.demo.app.PatientHelper;
import ncu.im3069.tools.JsonReader;

@WebServlet("/api/patient.do")
public class PatientController extends HttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** ph，PatientHelper之物件與Patient相關之資料庫方法（Sigleton） */
    private PatientHelper ph = PatientHelper.getHelper();

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
        String pid = jso.getString("pid");
        String name = jso.getString("name");
        String gender = jso.getString("gender");
        String dob = jso.getString("dob");
        String bloodType = jso.getString("bloodType");
        int phone = jso.getInt("phone");
        String address = jso.getString("address");
        String specialDisease = jso.getString("specialDisease");
        String drugAllergy = jso.getString("drugAllergy");
        String edited_by = jso.getString("edited_by");

        /** 建立一個新的病患物件 */
        Patient p = new Patient(pid, name, gender, dob, bloodType, phone, address, specialDisease, drugAllergy,
                edited_by);

        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if (pid.isEmpty() || name.isEmpty() || gender.isEmpty() || dob.isEmpty() || bloodType.isEmpty()
                || Integer.toString(phone).isEmpty() || address.isEmpty() || specialDisease.isEmpty()
                || drugAllergy.isEmpty() || edited_by.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        } else if (!ph.checkDuplicate(p)) {
            /** 透過PatientHelper物件的create()方法新建一個病患至資料庫 */
            JSONObject data = ph.create(p);

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 已建立病患資料");
            resp.put("response", data);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        } else {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'新增病例失敗，身分證重複！\', \'response\': \'\'}";
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
        String pid = jsr.getParameter("pid");

        /** 判斷該字串是否存在，若存在代表要取回個別病患之資料，否則代表要取回全部資料庫內病患之資料 */
        if (pid.isEmpty()) {
            /** 透過PatientHelper物件之getAll()方法取回所有病患之資料，回傳之資料為JSONObject物件 */
            JSONObject query = ph.getAll();

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有病患資料取得成功");
            resp.put("response", query);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        } else {
            /** 透過PatientHelper物件的getByID()方法自資料庫取回該名病患之資料，回傳之資料為JSONObject物件 */
            // JSONObject query = ph.getByID(id);
            JSONObject query = ph.getByPID(pid);
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "特定病患資料取得成功");
            resp.put("response", query);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
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
        String pid = jso.getString("pid");
        String name = jso.getString("name");
        String gender = jso.getString("gender");
        String dob = jso.getString("dob");
        String bloodType = jso.getString("bloodType");
        int phone = jso.getInt("phone");
        String address = jso.getString("address");
        String specialDisease = jso.getString("specialDisease");
        String drugAllergy = jso.getString("drugAllergy");
        String edited_by = jso.getString("edited_by");

        /** 透過傳入之參數，新建一個以這些參數之病患Patient物件 */
        Patient p = new Patient(id, pid, name, gender, dob, bloodType, phone, address, specialDisease, drugAllergy,
                edited_by);

        /** 透過Patient物件的update()方法至資料庫更新該名病患資料，回傳之資料為JSONObject物件 */
        JSONObject data = p.update();

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新病患資料成功");
        resp.put("response", data);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
}
