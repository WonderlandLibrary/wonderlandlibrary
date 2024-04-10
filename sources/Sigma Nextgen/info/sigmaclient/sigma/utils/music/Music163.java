package info.sigmaclient.sigma.utils.music;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import info.sigmaclient.sigma.modules.item.ChestStealer;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import top.fl0wowp4rty.phantomshield.annotations.Native;
import java.util.Map;
@Native
public class Music163 {
    private Map<String, String> Header = new HashMap<String, String>();
    public final HttpClient api;

    public Music163(@Nullable String cookies){
        if(cookies == null){
            cookies = "";
        }
        this.Header.put("Accept", "*/*");
        this.Header.put("Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4");
        this.Header.put("Connection", "keep-alive");
        this.Header.put("Content-Type", "application/x-www-form-urlencoded");
        this.Header.put("Referer", "http://music.163.com");
        this.Header.put("Host", "music.163.com");
        this.Header.put("Cookie", "appver=2.7.1.198277; os=pc; " + cookies);
        this.Header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        this.api = new HttpClient("http://music.163.com", this.Header);
    }

    public HttpClient getHttpClient(){
        return this.api;
    }

    /**
     * 获取音乐
     * @param id 音乐 id
     * @return 音乐对象
     */
    public Music getMusicByID(long id){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("c", "[{\"id\": " + id + "}]");

        JSONArray json = this.api.POST_API("/api/v3/song/detail", data).getJSONArray("songs");
        if(json.isEmpty()){
            throw new HttpClient.ActionException(Text.translatable("cloudmusic.exception.music.id"));
        }
        return new Music(getHttpClient(), json.getJSONObject(0), null);
    }


    /**
     * 获取搜索数据
     * @param key 搜索内容
     * @param type 搜索类型 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户
    1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频
     * @return POST_API data
     */
    private Map<String, Object> searchData(String key, int type){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("s", key);
        data.put("type", type);
        data.put("limit", 30);
        data.put("offset", 0);
        data.put("total", true);
        return data;
    }

    /**
     * 搜索歌曲
     * @param key 搜索内容
     * @return 页对象
     */
    public ApiPage searchMusic(String key){
        Map<String, Object> data = this.searchData(key, 1);
        JSONObject json = this.api.POST_API("/api/cloudsearch/pc", data);
        return new ApiPage(
                json.getJSONObject("result").getJSONArray("songs"),
                json.getJSONObject("result").getInteger("songCount"),
                "/api/cloudsearch/pc", this.api, data) {
            @Override
            protected JSONArray getNewPageDataJSONArray(JSONObject result) {
                return result.getJSONObject("result").getJSONArray("songs");
            }


            @Override
            protected Map<String, String> putPageItem(Map<String, String> newPageData, Object data) {
                JSONObject music = (JSONObject) data;
                newPageData.put(music.getString("name"),
                        String.valueOf(music.getLong("id")));
                return newPageData;
            }

        };
    }

}
