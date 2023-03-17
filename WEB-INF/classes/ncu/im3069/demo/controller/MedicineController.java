package ncu.im3069.demo.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Medicine;
import ncu.im3069.demo.app.MedicineHelper;
import ncu.im3069.tools.JsonReader;

@WebServlet("/api/medicine.do")
public class MedicineController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MedicineHelper mdh = MedicineHelper.getHelper();

    public MedicineController() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("id");

        /** 判斷該字串是否存在，若不存在代表要特定藥品之資料，否則代表要取回全部資料庫內藥品之資料 */
        if (id.isEmpty()) {
            JSONObject query = mdh.getAll();
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有藥品資料取得成功");
            resp.put("response", query);

            jsr.response(resp, response);

        } else {
            JSONObject query = mdh.getByID(id);
            JSONObject resp = new JSONObject();
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            resp.put("status", "200");
            resp.put("message", "所有商品資料取得成功");
            resp.put("response", query);
            jsr.response(resp, response);

        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        String name = jso.getString("name");
        String quantity = jso.getString("quantity");
        String category = jso.getString("category");

        Medicine med = new Medicine(name, quantity, category);

        if (name.isEmpty() || quantity.isEmpty() || category.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        } else {
            /** 透過PatientHelper物件的create()方法新建一個藥品至資料庫 */
            JSONObject data = mdh.create(med);

            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 已建立藥品資料");
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
        String name = jso.getString("name");
        String quantity = jso.getString("quantity");
        String category = jso.getString("category");

        /** 透過傳入之參數，新建一個以這些參數之藥品物件 */
        Medicine med = new Medicine(id, name, quantity, category);

        /** 透過Patient物件的update()方法至資料庫更新該名藥品資料，回傳之資料為JSONObject物件 */
        JSONObject data = med.update();

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新藥品資料成功");
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
        JSONObject query = mdh.deleteByID(id);

        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "藥品移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

}
