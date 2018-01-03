package com.dong.statistics.data;

import android.text.TextUtils;

/**
 * @author <dr_dong>
 *         Time : 2017/11/20 10:02
 *         用于定义大数据上传信息字段
 */
public class StatisticsInfo {

    /**
     * event_type
     * 可取值为 launch、page_view、page_leave、element_click、element_view、share，
     * 分别表示启动事件、页面展示事件、页面离开事件、元素点击事件、元素曝光事件、社交分享事件。
     * 其中，分享事件需要根据不通的社交平台，进行相应的回调。
     */
    private String e_t;

    /**
     * element_path
     * 元素路径（XPath），element_click、element_view事件有该字段，
     * 示例：/html/body/div[4]/ul[1]/li[6]/a[1]/img[1]
     */
    private String e_p;

    /**
     * element_data
     * 通过某种方式为点击元素或曝光元素添加_tracker_data_属性，属性值得格式为{'key': 'value', ...}，
     */
    private String e_d;

    /**
     * duration
     * 页面的停留时长，取值为当前页面加载完成到当前页面离开的毫秒数，发送page_leave事件时有该字段。
     */
    private String dur;

    /**
     * distinct_id
     * android 传 TelephonyManager.getDeviceId()
     * 此属性作为客户端的唯一标识，存储在cookie中，
     * 参考：https://mixpanel.com/help/questions/articles/how-do-mixpanels-libraries-assign-distinctid-by-default
     * 注意：一般情况下该值是不变的，除非用户手动清除了浏览器的cookie
     */
    private String d_i;

    /**
     * user_agent
     * 完整的用户代理信息。例如：Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36
     */
    private String u_a;

    /**
     * language
     * 浏览器语言
     */
    private String lan;

    /**
     * color_depth
     * 屏幕色深
     * android 设置屏幕亮度
     */
    private String c_d;

    /**
     * screen_width
     * 屏幕宽度
     */
    private String s_w;

    /**
     * screen_height
     * 屏幕高度
     */
    private String s_h;

    /**
     * screenX
     * 鼠标相对屏幕X坐标，点击事件有该字段,
     * android 对应 getRawX()
     */
    private String s_x;

    /**
     * screenY
     * 鼠标相对屏幕Y坐标，点击事件有该字段,
     * android 对应 getRawY()
     */
    private String s_y;

    /**
     * pageUrl
     * 当前页地址
     * android 当前 Activity 名字
     */
    private String url;

    /**
     * referrer
     * 前一页地址
     * android 前一个 Activity 名字
     */
    private String ref;

    /**
     * client_time
     * 日志时间戳（客户端时间）
     */
    private String c_t;

    /**
     * version
     * 取值为sdk对应版本，例如：1.0.0
     */
    private String ver;

    /**
     * ip
     * 客户端的IPv4值，在log-server中自动获取
     */
    private String ip;

    /**
     * operation_system
     * 客户端操作系统
     */
    private String os;

    /**
     * os_version
     * 客户端操作系统版本
     */
    private String o_v;

    /**
     * engine
     * 客户端浏览器引擎
     * android 写死 chrome
     */
    private String eng;

    /**
     * manufacturer
     * 设备生产厂商。注意：并不是所有设备厂商都能正确解析，若解析不到，默认值为 unknown
     */
    private String man;

    /**
     * model
     * 设备型号。注意：并不是所有的设备型号都可以正确解析，若解析不到，默认值为unknown
     */
    private String mod;

    /**
     * latitude
     * 维度，根据IP解析。注意：使用IP解析得到的位置信息并不一定准确，这依赖于IP库，目前版本中使用的是GeoLite2
     */
    private String lat;

    /**
     * longitude
     * 经度，根据IP解析。注意：使用IP解析得到的位置信息并不一定准确，这依赖于IP库，目前版本中使用的是GeoLite2
     */
    private String lon;

    /**
     * net_type
     * 网络类型，取值为 ethernet、wifi、2g、3g、4g、none、unknown
     */
    private String n_t;

    /**
     * client_type
     * 客户端类型，取值：PC，iOS，Android，WAP，ZJX_iOS，ZJX_Android，通过_tracker_设置该值，
     * 例如：var tracker = window._tracker_ || []; _tracker_.push(['client_type', 'PC']);
     */
    private String c_p;

    public String getE_t() {
        return e_t;
    }

    public void setE_t(String e_t) {
        this.e_t = e_t;
    }

    public String getE_p() {
        return e_p;
    }

    public void setE_p(String e_p) {
        this.e_p = e_p;
    }

    public String getE_d() {
        return e_d;
    }

    public void setE_d(String e_d) {
        this.e_d = e_d;
    }

    public String getDur() {
        return dur;
    }

    public void setDur(long dur) {
        this.dur = dur + "";
    }

    public String getD_i() {
        return d_i;
    }

    public void setD_i(String d_i) {
        this.d_i = d_i;
    }

    public String getU_a() {
        return u_a;
    }

    public void setU_a(String u_a) {
        this.u_a = u_a;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getC_d() {
        return c_d;
    }

    public void setC_d(int c_d) {
        this.c_d = c_d + "";
    }

    public String getS_w() {
        return s_w;
    }

    public void setS_w(int s_w) {
        this.s_w = s_w + "";
    }

    public String getS_h() {
        return s_h;
    }

    public void setS_h(int s_h) {
        this.s_h = s_h + "";
    }

    public String getS_x() {
        return s_x;
    }

    public void setS_x(int s_x) {
        this.s_x = s_x + "";
    }

    public String getS_y() {
        return s_y;
    }

    public void setS_y(int s_y) {
        this.s_y = s_y + "";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getC_t() {
        return c_t;
    }

    public void setC_t(long c_t) {
        this.c_t = c_t + "";
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getO_v() {
        return o_v;
    }

    public void setO_v(String o_v) {
        this.o_v = o_v;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getN_t() {
        return n_t;
    }

    public void setN_t(String n_t) {
        this.n_t = n_t;
    }

    public String getC_p() {
        return c_p;
    }

    public void setC_p(String c_p) {
        this.c_p = c_p;
    }

    @Override
    public String toString() {
        return "e_t=" + (TextUtils.isEmpty(e_t) ? "" : e_t) +
                "&e_p=" + (TextUtils.isEmpty(e_p) ? "" : e_p) +
                "&e_d=" + (TextUtils.isEmpty(e_d) ? "" : e_d) +
                "&dur=" + (TextUtils.isEmpty(dur) ? "" : dur) +
                "&d_i=" + (TextUtils.isEmpty(d_i) ? "" : d_i) +
                "&u_a=" + (TextUtils.isEmpty(u_a) ? "" : u_a) +
                "&lan=" + (TextUtils.isEmpty(lan) ? "" : lan) +
                "&c_d=" + (TextUtils.isEmpty(c_d) ? "" : c_d) +
                "&s_w=" + (TextUtils.isEmpty(s_w) ? "" : s_w) +
                "&s_h=" + (TextUtils.isEmpty(s_h) ? "" : s_h) +
                "&s_x=" + (TextUtils.isEmpty(s_x) ? "" : s_x) +
                "&s_y=" + (TextUtils.isEmpty(s_y) ? "" : s_y) +
                "&url=" + (TextUtils.isEmpty(url) ? "" : url) +
                "&ref=" + (TextUtils.isEmpty(ref) ? "" : ref) +
                "&c_t=" + (TextUtils.isEmpty(c_t) ? "" : c_t) +
                "&ver=" + (TextUtils.isEmpty(ver) ? "" : ver) +
                "&ip=" + (TextUtils.isEmpty(ip) ? "" : ip) +
                "&os=" + (TextUtils.isEmpty(os) ? "" : os) +
                "&o_v=" + (TextUtils.isEmpty(o_v) ? "" : o_v) +
                "&eng=" + (TextUtils.isEmpty(eng) ? "" : eng) +
                "&man=" + (TextUtils.isEmpty(man) ? "" : man) +
                "&mod=" + (TextUtils.isEmpty(mod) ? "" : mod) +
                "&lat=" + (TextUtils.isEmpty(lan) ? "" : lat) +
                "&lon=" + (TextUtils.isEmpty(lon) ? "" : lon) +
                "&n_t=" + (TextUtils.isEmpty(n_t) ? "" : n_t) +
                "&c_p=" + (TextUtils.isEmpty(c_p) ? "" : c_p);
    }

}
