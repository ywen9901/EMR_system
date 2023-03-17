package ncu.im3069.demo.controller;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Appointment;
import ncu.im3069.demo.app.AppointmentHelper;
import ncu.im3069.tools.JsonReader;

@WebServlet("/api/appointment.do")

public class AppointmentController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AppointmentHelper ah = AppointmentHelper.getHelper();

    public AppointmentController() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("id");
        String pid = jsr.getParameter("pid");
        String searching_date = jsr.getParameter("searching_date");

        /** 判斷該字串是否存在，若不存在代表要特定掛號之資料，否則代表要取回全部資料庫內掛號之資料 */
        if (!searching_date.isEmpty()) {
            JSONObject query = ah.getAll(searching_date);
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有掛號資料取得成功");
            resp.put("response", query);

            jsr.response(resp, response);

        } else if (!pid.isEmpty()) {
            JSONObject query = ah.getByPID(pid);
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有掛號資料取得成功");
            resp.put("response", query);

            jsr.response(resp, response);
        } else if (!id.isEmpty()) {
            JSONObject query = ah.getByID(id);
            JSONObject resp = new JSONObject();
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            resp.put("status", "200");
            resp.put("message", "所有掛號資料取得成功");
            resp.put("response", query);
            jsr.response(resp, response);

        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String name = jso.getString("name");
        String pid = jso.getString("pid");
        String dob = jso.getString("dob");
        String visited_date = jso.getString("visited_date");
        String clinic_hours = jso.getString("clinic_hours");

        Appointment a = new Appointment(pid, name, dob, visited_date, clinic_hours);

        if (name.isEmpty() || pid.isEmpty() || dob.isEmpty() || clinic_hours.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        } else {
            /** 透過AppointmentHelper物件的create()方法新建一個掛號至資料庫 */
            JSONObject data = ah.create(a);

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 已建立掛號資料");
            resp.put("response", data);

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
        String visited_date = jso.getString("visited_date");

        /** 透過傳入之參數，新建一個以這些參數之掛號物件 */
        Appointment a = new Appointment(id, visited_date);

        /** 透過AppointmentHelper物件的passAppointment()方法至資料庫更新掛號資料，回傳之資料為JSONObject物件 */
        JSONObject data = ah.passAppointment(a);

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新掛號資料成功");
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
        Boolean done = jso.getBoolean("done");

        /** 透過傳入之參數，新建一個以這些參數之掛號物件 */
        Appointment a = new Appointment(id, done);

        /**
         * 透過AppointmentHelper物件的updateAppointment()方法至資料庫更新掛號完成紀錄，回傳之資料為JSONObject物件
         */
        JSONObject query = ah.updateAppointment(a);

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "掛號刪除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

}
