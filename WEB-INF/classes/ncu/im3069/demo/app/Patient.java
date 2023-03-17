package ncu.im3069.demo.app;

import org.json.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

public class Patient {

    /** id 病患編號 */
    private int id;

    /** pid 病患身分證字號 */
    private String pid;

    /** name 病患姓名 */
    private String name;

    /** gender 病患性別 */
    private String gender;

    /** gender 病患出生日期 */
    private String dob;

    /** bloodType 病患血型 */
    private String bloodType;

    /** phone 病患手機 */
    private int phone;

    /** address 病患地址 */
    private String address;

    /** specialDisease 病患特特殊病史 */
    private String specialDisease;

    /** drugAllergy 病患過敏藥物 */
    private String drugAllergy;

    /** create_date 病患基本資料創建時間 */
    private Timestamp create_date;

    /** modify_date 病患基本資料更新時間 */
    private Timestamp modify_date;

    /** edited_by 最後更新人員姓名 */
    private String edited_by;

    /** ph PatientHelper之物件與Patient相關之資料庫方法（Sigleton） */
    private PatientHelper ph = PatientHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Patient物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立病患資料時，產生一名新的病患
     *
     * @param pid            病患身分證字號
     * @param name           病患姓名
     * @param gender         病患性別
     * @param dob            病患出生日期
     * @param bloodType      病患血型
     * @param phone          病患手機
     * @param address        病患地址
     * @param specialDisease 病患特特殊病史
     * @param drugAllergy    病患過敏藥物
     * @param edited_by      最後修改人員
     *
     */
    public Patient(String pid, String name, String gender, String dob, String bloodType, int phone, String address,
            String specialDisease, String drugAllergy, String edited_by) {
        this.pid = pid;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.bloodType = bloodType;
        this.phone = phone;
        this.address = address;
        this.specialDisease = specialDisease;
        this.drugAllergy = drugAllergy;
        this.create_date = Timestamp.valueOf(LocalDateTime.now());
        this.modify_date = Timestamp.valueOf(LocalDateTime.now());
        this.edited_by = edited_by;

        update();
    }

    /**
     * 實例化（Instantiates）一個新的（new）Patient物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新病患資料時，修改資料庫已存在的病患資訊
     *
     * @param id             病患編號
     * @param pid            病患身分證字號
     * @param name           病患姓名
     * @param gender         病患性別
     * @param dob            病患出生日期
     * @param bloodType      病患血型
     * @param phone          病患手機
     * @param address        病患地址
     * @param specialDisease 病患特特殊病史
     * @param drugAllergy    病患過敏藥物
     * @param create_date    資料創建時間
     * @param modify_date    病患修改時間
     * @param edited_by      最後修改人員
     *
     */
    public Patient(int id, String pid, String name, String gender, String dob, String bloodType, int phone,
            String address, String specialDisease, String drugAllergy, String edited_by) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.bloodType = bloodType;
        this.phone = phone;
        this.address = address;
        this.specialDisease = specialDisease;
        this.drugAllergy = drugAllergy;
        this.modify_date = Timestamp.valueOf(LocalDateTime.now());
        this.edited_by = edited_by;

    }

    /**
     * 實例化（Instantiates）一個新的（new）Patient物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢病患資料時，產生一名新的病患
     *
     * @param id             病患編號
     * @param pid            病患身分證字號
     * @param name           病患姓名
     * @param gender         病患性別
     * @param dob            病患出生日期
     * @param bloodType      病患血型
     * @param phone          病患手機
     * @param address        病患地址
     * @param specialDisease 病患特特殊病史
     * @param drugAllergy    病患過敏藥物
     *
     */
    public Patient(int id, String pid, String name, String gender, String dob, String bloodType, int phone,
            String address, String specialDisease, String drugAllergy, Timestamp create_date, Timestamp modify_date) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.bloodType = bloodType;
        this.phone = phone;
        this.address = address;
        this.specialDisease = specialDisease;
        this.drugAllergy = drugAllergy;
        this.create_date = create_date;
        this.modify_date = modify_date;
    }

    /**
     * 取得病患之編號
     *
     * @return the id 回傳病患編號
     */
    public int getID() {
        return this.id;
    }

    /**
     * 取得病患之身分證字號
     *
     * @return the pid 回傳病患編號
     */
    public String getPID() {
        return this.pid;
    }

    /**
     * 取得病患之編號
     *
     * @return the name 回傳病患姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 取得病￼患之性別
     *
     * @return the gender 回傳病￼患性別
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * 取得病患之出生日期
     *
     * @return the dob 回傳病患出生日期
     */
    public String getDob() {
        return this.dob;
    }

    /**
     * 取得病患之血型
     *
     * @return the bloodType 回傳病患血型
     */
    public String getBloodType() {
        return this.bloodType;
    }

    /**
     * 取得病患之聯絡電話
     *
     * @return the phone 回傳病患聯絡電話
     */
    public int getPhone() {
        return this.phone;
    }

    /**
     * 取得病患之地址
     *
     * @return the address 回傳病患地址
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * 取得病患之特殊疾病
     *
     * @return the specialDisease 回傳病患特殊疾病
     */
    public String getSpecialDisease() {
        return this.specialDisease;
    }

    /**
     * 取得病患之過敏藥物
     *
     * @return the drugAllergy 回傳病患過敏藥物
     */
    public String getDrugAllergy() {
        return this.drugAllergy;
    }

    /**
     * 取得病患資訊創建時間
     *
     * @return createTime 回傳病患資訊創建時間
     */
    public Timestamp getCreateTime() {
        return this.create_date;
    }

    /**
     * 取得病患資訊修改時間
     *
     * @return modifyTime 回傳病患資訊修改時間
     */
    public Timestamp getModifyTime() {
        return this.modify_date;
    }

    /**
     * 取得病患之資料修改人員
     *
     * @return the editedby 病患之資料修改人員
     */
    public String geteditedby() {
        return this.edited_by;
    }

    /**
     * 更新病患資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();

        /** 檢查該名病患是否已經在資料庫 */
        if (this.id != 0) {
            /** 透過MemberHelper物件，更新目前之病患資料置資料庫中 */
            data = ph.update(this);
        }

        return data;
    }

    /**
     * 取得該名病患所有資料
     *
     * @return the data 取得該名病患之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名病患所需之資料全部進行封裝 */
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("pid", getPID());
        jso.put("name", getName());
        jso.put("gender", getGender());
        jso.put("dob", getDob());
        jso.put("bloodType", getBloodType());
        jso.put("phone", getPhone());
        jso.put("address", getAddress());
        jso.put("specialDisease", getSpecialDisease());
        jso.put("drugAllergy", getDrugAllergy());
        jso.put("create_date", getCreateTime());
        jso.put("modify_date", getModifyTime());
        jso.put("edited_by", geteditedby());

        return jso;
    }

}
