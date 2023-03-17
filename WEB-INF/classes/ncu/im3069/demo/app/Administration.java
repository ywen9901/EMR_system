package ncu.im3069.demo.app;

import org.json.*;

import java.sql.Timestamp;
// import java.util.Calendar;

public class Administration {

    /** id 櫃台行政編號 */
    private int id;

    /** account 櫃台行政帳號 */
    private String account;

    /** password 櫃台行政密碼 */
    private String password;

    /** name 櫃台行政姓名 */
    private String name;

    /** dob 櫃台行政生日 */
    private String dob;

    /** phone 櫃台行政電話 */
    private int phone;

    /** address 櫃台行政地址 */
    private String address;

    /** create_date，生成日期 */
    private Timestamp create_date;

    /** modify_date，修改日期 */
    private Timestamp modify_date;

    /** dh，AdministrationHelper之物件與Administration相關之資料庫方法（Sigleton） */
    private AdministrationHelper adh = AdministrationHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Administration物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立櫃台行政資料時，產生一名新的櫃台行政
     *
     * @param account  櫃台行政帳號
     * @param password 櫃台行政密碼
     * @param name     櫃台行政姓名
     * @param dob      櫃台行政生日
     * @param phone    櫃台行政電話
     * @param address  櫃台行政地址
     */
    public Administration(String account, String password, String name, String dob, int phone, String address) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.address = address;
        update();
    }

    /**
     * 實例化（Instantiates）一個新的（new）Administration物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新櫃台行政資料時，產生一名櫃台行政同時需要去資料庫檢索原有修改日期
     *
     * @param id          櫃台行政編號
     * @param account     櫃台行政帳號
     * @param password    櫃台行政密碼
     * @param name        櫃台行政姓名
     * @param dob         櫃台行政生日
     * @param phone       櫃台行政電話
     * @param address     櫃台行政地址
     * @param create_date 生成日期
     * @param modify_date 修改日期
     */
    public Administration(int id, String account, String password, String name, String dob, int phone, String address,
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

        /** 取回原有資料庫內該名櫃台行政之更新日期 */
        /** getmodify_date(); */
    }

    /**
     * 實例化（Instantiates）一個新的（new）Administration物件<br>
     * 採用多載（overload）方法進行，此建構子用於查詢櫃台行政資料時，將每一筆資料新增為一個櫃台行政物件
     *
     * @param id       櫃台行政編號
     * @param account  櫃台行政帳號
     * @param password 櫃台行政密碼
     * @param name     櫃台行政姓名
     * @param dob      櫃台行政生日
     * @param phone    櫃台行政電話
     * @param address  櫃台行政地址
     */
    public Administration(int id, String account, String password, String name, String dob, int phone, String address) {
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
     * 取得櫃台行政之編號
     *
     * @return the id 回傳櫃台行政編號
     */
    public int getID() {
        return this.id;
    }

    /**
     * 取 櫃台行政之地址
     *
     * @return the account 回 櫃台行政地址
     */
    public String getAccount() {
        return this.account;
    }

    /**
     * 取得櫃台行政之姓名
     *
     * @return the name 回傳櫃台行政姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 取得櫃台行政之密碼
     *
     * @return the password 回傳櫃台行政密碼
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 取得櫃台行政之生日
     *
     * @return the dob 回傳櫃台行政生日
     */
    public String getDob() {
        return this.dob;
    }

    /**
     * 取得櫃台行政之電話
     *
     * @return the phone 回傳櫃台行政電話
     */
    public int getPhone() {
        return this.phone;
    }

    /**
     * 取得櫃台行政之地址
     *
     * @return the address 回傳櫃台行政地址
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * 取得櫃台行政資訊創建時間
     *
     * @return Timestamp 回傳櫃台行政資訊創建時間
     */
    public Timestamp getCreateTime() {
        return this.create_date;
    }

    /**
     * 取得櫃台行政資訊修改時間
     *
     * @return Timestamp 回傳病患資訊修改時間
     */
    public Timestamp getModifyTime() {
        return this.modify_date;
    }

    /**
     * 更 櫃台行政資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();

        /** 檢查該名櫃台行政是否已經在資料庫 */
        if (this.id != 0) {
            /** 透過AdministrationHelper物件，更新目前之櫃台行政資料置資料庫中 */
            data = adh.update(this);
        }

        return data;
    }

    /**
     * 取得該 櫃台行政所有資料
     *
     * @return the data 取得該 櫃台行政之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該 櫃台行政所需之資料全部進行封裝 */
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
