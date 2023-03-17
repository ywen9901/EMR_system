package ncu.im3069.demo.controller;

import java.sql.Timestamp;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;
import ncu.im3069.demo.app.Record;
import ncu.im3069.demo.app.RecordHelper;
import ncu.im3069.tools.JsonReader;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/record.do")
public class RecordController extends HttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** oh，RecordHelper 之物件與 Record 相關之資料庫方法（Sigleton） */
    private RecordHelper rh = RecordHelper.getHelper();

    public RecordController() {
        super();
    }

    /**
     * 處理 Http Method 請求 GET 方法（新增資料）
     *
     * @param request  Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        String id = jsr.getParameter("id");
        String patient_id = jsr.getParameter("patient_id");

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (patient_id.isEmpty() && id.isEmpty()) {
            /** 透過 RecordHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
            JSONObject query = rh.getAll();
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有看診紀錄資料取得成功");
            resp.put("response", query);

            jsr.response(resp, response);

        } else if (!patient_id.isEmpty()) {
            JSONObject query = rh.getByPId(patient_id);
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "特定看診紀錄資料取得成功");
            resp.put("response", query);
            jsr.response(resp, response);

        } else if (!id.isEmpty()) {
            JSONObject query = rh.getById(id);
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "特定看診紀錄資料取得成功");
            resp.put("response", query);
            jsr.response(resp, response);
        }

    }

    /**
     * 處理 Http Method 請求 POST 方法（新增資料）
     *
     * @param request  Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int patient_id = jso.getInt("patient_id");
        String symptoms = jso.getString("symptoms");
        String days = jso.getString("days");
        String degree = jso.getString("degree");
        int Medicine_id = jso.getInt("Medicine_id");
        String note = jso.getString("note");
        String visited_date = jso.getString("visited_date");
        String doctor = jso.getString("doctor");
        String edited_by = jso.getString("edited_by");

        /** 建立一個新的訂單物件 */
        Record r = new Record(patient_id, symptoms, days, degree, Medicine_id, note, visited_date, doctor, edited_by);

        if (symptoms.isEmpty() || days.isEmpty() || degree.isEmpty() || note.isEmpty() || visited_date.isEmpty()
                || doctor.isEmpty() || edited_by.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        } else {
            /** 透過PatientHelper物件的create()方法新建一個藥品至資料庫 */
            JSONObject data = rh.create(r);
            /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "看診紀錄新增成功！");
            resp.put("response", data);

            /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
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
        int patient_id = jso.getInt("patient_id");
        String symptoms = jso.getString("symptoms");
        String days = jso.getString("days");
        String degree = jso.getString("degree");
        int Medicine_id = jso.getInt("Medicine_id");
        String note = jso.getString("note");
        String visited_date = jso.getString("visited_date");
        String doctor = jso.getString("doctor");
        String edited_by = jso.getString("edited_by");

        /** 透過傳入之參數，新建一個以這些參數之藥品物件 */
        Record r = new Record(id, patient_id, symptoms, days, degree, Medicine_id, note, visited_date, doctor,
                edited_by);

        /** 透過Patient物件的update()方法至資料庫更新該名藥品資料，回傳之資料為JSONObject物件 */
        JSONObject data = r.update();

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新看診紀錄資料成功");
        resp.put("response", data);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
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

        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該藥品，回傳之資料為JSONObject物件 */
        JSONObject query = rh.deleteByID(id);

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "看診紀錄移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

}
