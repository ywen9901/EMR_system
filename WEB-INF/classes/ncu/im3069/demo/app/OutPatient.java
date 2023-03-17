package ncu.im3069.demo.app;

import org.json.*;

public class OutPatient {

    /** id，門診編號 */
    private int id;

    /** doctor_id，醫師編號 */
    private int doctor_id;

    /** clinic_hours，看診時段 */
    private String clinic_hours;

    /** doctor_name，醫師 */
    private String doctor_name;

    /** oh，OutpatientHelper之物件與Member相關之資料庫方法（Sigleton) */
    private OutpatientHelper oh = OutpatientHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增門診
     */

    public OutPatient(int id, int doctor_id, String clinic_hours, String doctor_name) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.clinic_hours = clinic_hours;
        this.doctor_name = doctor_name;
    }

    /**
     * 取得門診編號
     *
     * @return the id 回傳掛號門診編號
     */
    public int getID() {
        return this.id;
    }

    /**
     * 取得醫師編號
     *
     * @return the doctor_id 回傳醫師編號
     */
    public int getDoctorID() {
        return this.doctor_id;
    }

    /**
     * 取得看診時段
     *
     * @return the doctor_id 回傳看診時段
     */
    public String getClinicHours() {
        return this.clinic_hours;
    }

    /**
     * 取得醫師姓名
     *
     * @return the doctor_name 回傳醫師姓名
     */
    public String getDoctorName() {
        return this.doctor_name;
    }

    /**
     * 取得門診資訊
     *
     * @return JSONObject 回傳門診資訊
     */
    public JSONObject getData() {
        /** 透過JSONObject將該門診所需之資料全部進行封裝 */
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("doctor_id", getDoctorID());
        jso.put("clinic_hours", getClinicHours());
        jso.put("doctor_name", getDoctorName());
        return jso;
    }
}
