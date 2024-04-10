package info.sigmaclient.sigma.config.antisniper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import info.sigmaclient.sigma.config.values.Value;

import java.util.ArrayList;

public class AntiSniperValues {
    @Expose
    @SerializedName("id")
    public String name;
    @Expose
    @SerializedName("key")
    public int key = -1;
    @Expose
    @SerializedName("values")
    public ArrayList<Value<?>> values;
    @Expose
    @SerializedName("enable")
    public boolean enable = false;

    @Expose
    @SerializedName("version")
    public String version = "11J";
}
