package ncu.im3069.demo.controller;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.OutpatientHelper;
import ncu.im3069.tools.JsonReader;

@WebServlet("/api/outpatient.do")

public class OutpatientController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OutpatientHelper oh = OutpatientHelper.getHelper();

    public OutpatientController() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String doctor_id = jsr.getParameter("doctor_id");
        String clinic_hours = jsr.getParameter("clinic_hours");

        /** 判斷該字串是否存在，若不存在代表要特定醫師看診時段門診之資料，否則代表要取回特定醫師資料庫內門診之資料 */
        if (doctor_id.isEmpty()) {
            JSONObject query = oh.getByClinicHours(clinic_hours);
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "醫師看診時段資料取得成功");
            resp.put("response", query);

            jsr.response(resp, response);

        } else {
            JSONObject query = oh.getByDoctorID(doctor_id);
            JSONObject resp = new JSONObject();
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            resp.put("status", "200");
            resp.put("message", "醫師門診資料取得成功");
            resp.put("response", query);
            jsr.response(resp, response);

        }

    }

}
