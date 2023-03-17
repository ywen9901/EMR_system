package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

import org.json.*;

public class Record {

    /** id，看診紀錄編號 */
    private int id;

    /** patient_id 病患編號 */
    private int patient_id;

    private String symptoms;

    private String days;

    private String degree;

    private int Medicine_id;

    private String note;

    private String visited_date;

    private String doctor;

    private String edited_by;

    /** rh，RecordHelper 之物件與 Record 相關之資料庫方法（Sigleton） */
    private RecordHelper rh = RecordHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Record 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立看診紀錄資料時，產生一個新的看診紀錄
     */
    public Record(int patient_id, String symptoms, String days, String degree, int Medicine_id, String note,
            String visited_date, String doctor, String edited_by) {
        this.patient_id = patient_id;
        this.symptoms = symptoms;
        this.days = days;
        this.degree = degree;
        this.Medicine_id = Medicine_id;
        this.note = note;
        this.visited_date = visited_date;
        this.doctor = doctor;
        this.edited_by = edited_by;

    }

    /**
     * 實例化（Instantiates）一個新的（new）Record 物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新看診紀錄資料時，修改資料庫已存在的看診紀錄
     *
     */
    public Record(int id, int patient_id, String symptoms, String days, String degree, int Medicine_id, String note,
            String visited_date, String doctor, String edited_by) {
        this.id = id;
        this.patient_id = patient_id;
        this.symptoms = symptoms;
        this.days = days;
        this.degree = degree;
        this.Medicine_id = Medicine_id;
        this.note = note;
        this.visited_date = visited_date;
        this.doctor = doctor;
        this.edited_by = edited_by;

    }

    /**
     * 取得看診紀錄編號
     *
     * @return int 回傳看診紀錄編號
     */
    public int getId() {
        return this.id;
    }

    public int getPatient_id() {
        return this.patient_id;
    }

    public String getSymptoms() {
        return this.symptoms;
    }

    public String getDays() {
        return this.days;
    }

    public String getDegree() {
        return this.degree;
    }

    public int getMedicine_id() {
        return this.Medicine_id;
    }

    public String getNote() {
        return this.note;
    }

    public String getVisited_date() {
        return this.visited_date;
    }

    public String getDoctor() {
        return this.doctor;
    }

    public String getEdited_by() {
        return this.edited_by;
    }

    /**
     * 更新看診紀錄資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();

        /** 檢查該看診紀錄是否已經在資料庫 */
        if (this.id != 0) {
            data = rh.update(this);
        }

        return data;
    }

    /**
     * 取得訂單基本資料
     *
     * @return JSONObject 取得訂單基本資料
     */
    public JSONObject getRecordData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("patient_id", getPatient_id());
        jso.put("symptoms", getSymptoms());
        jso.put("days", getDays());
        jso.put("degree", getDegree());
        jso.put("Medicine_id", getMedicine_id());
        jso.put("note", getNote());
        jso.put("visited_date", getVisited_date());
        jso.put("doctor", getDoctor());
        jso.put("edited_by", getEdited_by());

        return jso;
    }

    /**
     * 取得病歷資料所有資訊
     *
     * @return JSONObject 取得訂單所有資訊
     */
    public JSONObject getRecordAllInfo() {
        JSONObject jso = new JSONObject();
        jso.put("Record_info", getRecordData());
        return jso;
    }

    // /**
    // * 從 DB 中取得藥品
    // */
    // private void getMedicineFromDB(int medicine_id) {
    // String id = String.valueOf(medicine_id);

    // this.md = mdh.getByID(id);
    // }

}
