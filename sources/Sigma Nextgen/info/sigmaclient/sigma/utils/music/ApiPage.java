package info.sigmaclient.sigma.utils.music;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public abstract class ApiPage extends Page{
    private final String path;
    private final HttpClient api;
    private final Map<String, Object> postData;
    private int canUsePageCount = 0;
    private int getPageIn = 0;

    /**
     * 动态页对象, 翻页超出数据时自动请求 Api
     *
     * @param data      起始数据
     * @param dataCount 一共多少数据, 包括没在起始数据中的
     * @param path      Api 路径
     * @param api       HttpClient 对象
     * @param postData  Api 请求参数
     */
    public ApiPage(JSONArray data, int dataCount, String path, HttpClient api, Map<String, Object> postData) {
        super(data, dataCount);
        this.path = path;
        this.api = api;
        this.postData = postData;
        this.setCanUsePage(data);
    }

    private void setCanUsePage(JSONArray data) {
        this.canUsePageCount = (int) Math.ceil(data.size() * 1.0f / this.limit);
    }

    private void addCanUsePage(JSONArray data) {
        this.data.addAll(this.splitData(data));
        this.canUsePageCount = this.data.size();
    }

    private JSONArray getNewPageData() {
        this.getPageIn++;
        this.postData.put("offset", (int) postData.get("limit") * this.getPageIn);

        return this.getNewPageDataJSONArray(this.api.POST_API(this.path, postData));
    }

    /**
     * 设置每次请求 Api 后所需的数据 JSONArray
     *
     * @param result Api 数据
     * @return 页所需的数据
     */
    protected abstract JSONArray getNewPageDataJSONArray(JSONObject result);
}