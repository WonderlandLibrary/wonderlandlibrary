package slavikcodd3r.rainbow.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NameArrayHook
{
    @SerializedName("name")
    @Expose
    private String name;
    public boolean e;
    
    public NameArrayHook() {
        this.e = false;
    }
    
    public final synchronized String getName() {
        return this.name;
    }
    
    public void setE(final boolean e) {
        this.e = e;
    }
    
    public final synchronized void setName(final String name) {
        this.name = name;
    }
}
