package info.sigmaclient.sigma.utils.music;

import com.alibaba.fastjson.JSONArray;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 页对象, 处理翻页
 */
public abstract class Page {
    protected final int limit = 5;
    protected final List<List<?>> data;
    protected final int pageCount;
    protected int pageIn;
    private Text infoText;

    /**
     * 设置每一项的数据格式
     * @param newPageData 当前页所有数据
     * @param data 当前页一项数据
     * @return 当前页所有数据
     */
    protected abstract Map<String, String> putPageItem(Map<String, String> newPageData, Object data);

    protected Map<String, String> setPageData(List<?> pageData) {
        Map<String, String> newPageData = new LinkedHashMap<>();
        for(Object data: pageData){
            newPageData = this.putPageItem(newPageData, data);
        }
        return newPageData;
    }

    protected void printToChatHud(Map<String, String> pageData) {

    }

    protected List<List<?>> splitData(List<?> data) {
        List<List<?>> splitData = new ArrayList<>();
        for(int index = 0; index <= this.pageCount - 1; index ++){
            try {
                splitData.add(data.subList(index * limit, (index + 1) * limit));
            } catch (IndexOutOfBoundsException e) {
                if(index * limit != data.size()){
                    splitData.add(data.subList(index * limit, data.size()));
                }
                break;
            }
        }
        return splitData;
    }

    public Page(JSONArray data){
        this.pageCount = (int) Math.ceil(data.size() * 1.0f / limit);
        this.data = this.splitData(data);
    }

    public Page(JSONArray data, int dataCount){
        this.pageCount = (int) Math.ceil(dataCount * 1.0f / limit);
        if(dataCount < data.size()){
            data = new JSONArray(data.subList(0, dataCount));
        }
        this.data = this.splitData(data);
    }


    public Map<String, String> getPages(){
        if(true){
            List<Object> objects = new ArrayList<>();
            for(List<?> list : data){
                objects.addAll(list);
            }
            return this.setPageData(objects);
        }
        return this.setPageData(this.data.get(this.pageIn));
    }

    public void look(){
    }

    /**
     * 查看上一页
     */
    public void prev(){
        if(this.pageIn <= 0){
            return;
        }

        this.pageIn --;
        this.look();
    }

    /**
     * 查看下一页
     */
    public void next(){
        if(this.pageIn >= this.pageCount - 1){
            return;
        }

        this.pageIn ++;
        this.look();
    }

    /**
     * 跳转至
     * @param page 页索引
     */
    public void to(int page){
        if(page < 0){
            page = 0;
        }

        if(page >= this.pageCount - 1){
            page = this.pageCount - 1;
        }

        if(this.pageIn == page){
            return;
        }

        this.pageIn = page;
        this.look();
    }

    public void setInfoText(Text info){
        this.infoText = info;
    }

}