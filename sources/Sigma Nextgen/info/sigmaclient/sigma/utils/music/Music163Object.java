package info.sigmaclient.sigma.utils.music;


import com.alibaba.fastjson.JSONObject;
import top.fl0wowp4rty.phantomshield.annotations.Native;
@Native

public class Music163Object {
    protected final HttpClient api;

    /**
     * 初始化对象
     * @param api HttpClient api
     * @param data 对象数据
     */
    public Music163Object(HttpClient api, JSONObject data){
        this.api = api;
    }
}
