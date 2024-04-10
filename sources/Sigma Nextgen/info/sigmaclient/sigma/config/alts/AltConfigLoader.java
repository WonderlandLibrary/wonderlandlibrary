package info.sigmaclient.sigma.config.alts;

import com.google.gson.annotations.SerializedName;

public class AltConfigLoader {
    @SerializedName("token")
    public String token;
    @SerializedName("name")
    public String name;
    @SerializedName("uuid")
    public String uuid;
    @SerializedName("time")
    public long time;
    @SerializedName("offline")
    public boolean offline;
}
