package ncu.im3069.demo.app;

import org.json.*;

import java.sql.Timestamp;

public class Doctor {

    /** id 醫師編號 */
    private int id;

    /** account 醫師帳號 */
    private String account;

    /** password 醫師密碼 */
    private String password;

    /** name 醫師姓名 */
    private String name;

    /** dob 醫師生日 */
    private String dob;

    /** phone 醫師電話 */
    private int phone;

    /** address 醫師地址 */
    private String address;

    /** create_date，生成日期 */
    private Timestamp create_date;

    /** modify_date，修改日期 */
    private Timestamp modify_date;

    /** dh，DoctorHelper之物件與Doctor相關之資料庫方法（Sigleton） */
    private DoctorHelper dh = DoctorHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Doctor物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立醫師資料時，產生一名新的醫師
     *
     * @param account  醫師帳號
     * @param password 醫師密碼
     * @param name     醫師姓名
     * @param dob      醫師生日
     * @param phone    醫師電話
     * @param address  醫師地址
     */
    public Doctor(String account, String password, String name, String dob, int phone, String address) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.address = address;
        update();
    }

    /**
     * 實例化（Instantiates）一個新的（new）Doctor物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新醫師資料時，產生一名醫師同時需要去資料庫檢索原有修改日期
     *
     * @param id          醫師編號
     * @param account     醫師帳號
     * @param password    醫師密碼
     * @param name        醫師姓名
     * @param dob         醫師生日
     * @param phone       醫師電話
     * @param address     醫師地址
     * @param create_date 生成日期
     * @param modify_date 修改日期
     */
    public Doctor(int id, String account, String password, String name, String dob, int phone, String address,
            Timestamp create_date, Timestamp modify_date) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.address = address;
        this.create_date = create_date;
        this.modify_date = modify_date;

        /** 取回原有資料庫內該名醫師之更新日期 */
        /** getmodify_date(); */
    }

    /**
     * 實例化（Instantiates）一個新的（new）Doctor物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢醫師資料時，將每一筆資料新增為一個醫師物件
     *
     * @param id       醫師編號
     * @param account  醫師帳號
     * @param password 醫師密碼
     * @param name     醫師姓名
     * @param dob      醫師生日
     * @param phone    醫師電話
     * @param address  醫師地址
     */
    public Doctor(int id, String account, String password, String name, String dob, int phone, String address) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.address = address;
        update();
    }

    /**
     * 取得醫師之編號
     *
     * @return the id 回傳醫師編號
     */
    public int getID() {
        return this.id;
    }

    /**
     * 取得醫師之地址
     *
     * @return the account 回 醫師地址
     */
    public String getAccount() {
        return this.account;
    }

    /**
     * 取得醫師之姓名
     *
     * @return the name 回傳醫師姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 取得醫師之密碼
     *
     * @return the password 回傳醫師密碼
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 取得醫師之生日
     *
     * @return the dob 回傳醫師生日
     */
    public String getDob() {
        return this.dob;
    }

    /**
     * 取得醫師之電話
     *
     * @return the phone 回傳醫師電話
     */
    public int getPhone() {
        return this.phone;
    }

    /**
     * 取得醫師之地址
     *
     * @return the address 回傳醫師地址
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * 取得醫師資訊創建時間
     *
     * @return Timestamp 回傳醫師資訊創建時間
     */
    public Timestamp getCreateTime() {
        return this.create_date;
    }

    /**
     * 取得醫師資訊修改時間
     *
     * @return Timestamp 回傳病患資訊修改時間
     */
    public Timestamp getModifyTime() {
        return this.modify_date;
    }

    /**
     * 更 醫師資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();

        /** 檢查該名醫師是否已經在資料庫 */
        if (this.id != 0) {
            /** 透過DoctorHelper物件，更新目前之醫師資料置資料庫中 */
            data = dh.update(this);
        }

        return data;
    }

    /**
     * 取得該 醫師所有資料
     *
     * @return the data 取得該 醫師之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該 醫師所需之資料全部進行封裝 */
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("account", getAccount());
        jso.put("password", getPassword());
        jso.put("name", getName());
        jso.put("dob", getDob());
        jso.put("phone", getPhone());
        jso.put("address", getAddress());
        jso.put("create_date", getCreateTime());
        jso.put("modify_date", getModifyTime());

        return jso;
    }

}
