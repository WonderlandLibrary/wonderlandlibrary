package Velo.api.Module.Config;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cfg {

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("setting")
    public Object setting;

    public Cfg(String name, Object setting) {
        this.name = name;
        this.setting = setting;
    }

}