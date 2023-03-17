package ncu.im3069.demo.app;

import org.json.*;

public class Appointment {

    /** id，病患編號 */
    private int id;

    /** name，病患姓名 */
    private String name;

    /** pid，病患身份證字號 */
    private String pid;

    /** pid，病患看診醫師 */
    private String doctor_name;

    /** dob，病患生日 */
    private String dob;

    /** visited_date，病患看診日期 */
    private String visited_date;

    /** appointment_number，病患看診序號 */
    private int appointment_number;

    /** clinic_hours，病患看診時段 */
    private String clinic_hours;

    /** done，病患看診完成紀錄 */
    private boolean done;

    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private AppointmentHelper ah = AppointmentHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增掛號
     */

    public Appointment(int id, String pid, String name, String dob, String visited_date, int appointment_number,
            String clinic_hours) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.dob = dob;
        this.visited_date = visited_date;
        this.appointment_number = appointment_number;
        this.clinic_hours = clinic_hours;
    }

    // /**
    // * 實例化（Instantiates）一個新的（new）Order 物件<br>
    // * 採用多載（overload）方法進行，此建構子用於顯示掛號
    // */

    public Appointment(int id, String pid, String name, String dob, String visited_date, int appointment_number,
            String clinic_hours, String doctor_name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.dob = dob;
        this.visited_date = visited_date;
        this.appointment_number = appointment_number;
        this.clinic_hours = clinic_hours;
        this.doctor_name = doctor_name;
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增掛號
     */

    public Appointment(String pid, String name, String dob, String visited_date, String clinic_hours) {
        this.pid = pid;
        this.name = name;
        this.dob = dob;
        this.visited_date = visited_date;
        this.clinic_hours = clinic_hours;
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於刪除掛號時
     */
    public Appointment(int id, boolean done) {
        this.id = id;
        this.done = done;
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於刪除掛號時
     */
    public Appointment(int id, String visited_date) {
        this.id = id;
        this.visited_date = visited_date;
    }

    // /**
    // * 實例化（Instantiates）一個新的（new）Order 物件<br>
    // * 採用多載（overload）方法進行，此建構子用於刪除掛號時
    // */
    // public Appointment(String clinic_hours, String doctor_name) {
    // this.clinic_hours = clinic_hours;
    // this.doctor_name = doctor_name;
    // }

    /**
     * 取得掛號編號
     *
     * @return the id 回傳掛號掛號編號
     */
    public int getID() {
        return this.id;
    }

    /**
     * 取得病患姓名
     *
     * @return the name 回傳病患姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 取得病患身份證字號
     *
     * @return the pid 回傳病患身份證字號
     */
    public String getPID() {
        return this.pid;
    }

    /**
     * 設定病患看診醫師
     */
    public void setDoctorName(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    /**
     * 取得病患看診醫師
     *
     * @return the doctor_name 回傳病患身份證字號
     */
    public String getDoctorName() {
        return this.doctor_name;
    }

    /**
     * 取得病患看診日期
     *
     * @return the visited_date 回傳病患看診日期
     */
    public String getVisitDate() {
        return this.visited_date;
    }

    /**
     * 取得病患看診序號
     *
     * @return the appointment_number 回傳病患看診序號
     */
    public int getAppointmentNumber() {
        return this.appointment_number;
    }

    /**
     * 設定病患看診序號
     */
    public void setAppointmentNumber(int appointment_number) {
        this.appointment_number = appointment_number;
    }

    /**
     * 取得病患看診時段
     *
     * @return the clinic_hours 回傳病患看診時段
     */
    public String getClinicHours() {
        return this.clinic_hours;
    }

    /**
     * 取得病患生日
     *
     * @return the dob 回傳病患生日
     */
    public String getDob() {
        return this.dob;
    }

    /**
     * 取得病患看診完成紀錄
     *
     * @return the done 回傳病患看診完成紀錄
     */
    public boolean getDone() {
        return this.done;
    }

    /**
     * 設定病患看診完成紀錄
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * 取得病患掛號資訊
     *
     * @return JSONObject 回傳掛號資訊
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝 */
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("pid", getPID());
        jso.put("visited_date", getVisitDate());
        jso.put("appointment_number", getAppointmentNumber());
        jso.put("clinic_hours", getClinicHours());
        jso.put("dob", getDob());
        jso.put("done", getDone());
        jso.put("doctor_name", getDoctorName());

        return jso;
    }

}
