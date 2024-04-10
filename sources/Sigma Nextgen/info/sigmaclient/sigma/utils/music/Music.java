package info.sigmaclient.sigma.utils.music;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;

import java.util.HashMap;
import java.util.Map;
import top.fl0wowp4rty.phantomshield.annotations.Native;
@Native

public class Music extends Music163Object{
    public long id = 0;
    public String ytid = "";
    public boolean copyRight = false;
    public TextureImage textureImage = new TextureImage();
    public String name = "null";
    public String aliasName = "null";
    public JSONObject artists = new JSONObject();
    public JSONObject album = new JSONObject();
    public long duration = 0;
    public String picUrl = "null";
    public PartialTicksAnim hover = new PartialTicksAnim(0);

    public static String getArtistsName(String sb) {
        StringBuilder artistsName = new StringBuilder();
        for (Map.Entry<String, Object> artistData : JSONObject.parseObject(sb).getInnerMap().entrySet()) {
            if(artistData.getKey().equals("name"))
                artistsName.append((String) (artistData.getValue())).append("/");
        }
        return artistsName.substring(0, artistsName.length() - 1);
    }

    public Music() {
        super(null,null);
    }
    public void download(){
        textureImage = Resources.downloadTexture(this.picUrl);
    }
    /**
     * 专辑歌曲没有 picUrl, 通过 cover 传入封面 picUrl
     */
    public Music(HttpClient api, JSONObject data, String cover) {
        super(api, data);
        this.id = data.getLong("id");
        this.name = data.getString("name");
        System.out.println(data.toJSONString());
        JSONArray alias;
        if(data.get("ar") != null){
            alias = data.getJSONArray("ar");
        } else {
            alias = data.getJSONArray("artist");
        }
        copyRight = data.getInteger("copyright") != 0;

        if(alias.size() > 0){
            this.aliasName = alias.getJSONObject(0).getString("name");
        }else{
            this.aliasName = "";
        }

//        if(data.get("ar") != null){
            this.artists = null;
//        }else{
//            this.artists = data.getJSONObject("artists");
//        }

        if(data.get("al") != null){
            this.album = data.getJSONObject("al");
        }else{
            this.album = data.getJSONObject("album");
        }

        if(data.get("dt") != null){
            this.duration = data.getLong("dt") / 1000;
        }else{
            this.duration = data.getLong("duration") / 1000;
        }

        if(this.album.get("picUrl") != null){
            this.picUrl = this.album.getString("picUrl");
        }else{
            this.picUrl = cover;
        }
        System.out.println(picUrl);
        textureImage = Resources.downloadTexture(this.picUrl);
    }
    public String getPlayUrl(int br){
        if(br == 0){
            br = 999000;
        }

        HttpClient playApi = new HttpClient("http://music.163.com", this.api.getHeader());
        Map<String, Object> data = new HashMap<>();
        data.put("caches", "[" + this.id +"]");
        data.put("br", br);
        data.put("ids","%5B" + this.id + "%5D");
        data.put("id",this.id);

        JSONObject result = playApi.POST_API("/api/song/enhance/player/url", data);
        JSONObject music = result.getJSONArray("data").getJSONObject(0);
        if(music.getInteger("code") != 200){
            throw new HttpClient.ActionException(String.valueOf(music.getInteger("code")));
        }
        return music.getString("url");
    }

}
