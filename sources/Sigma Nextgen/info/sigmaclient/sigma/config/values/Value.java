package info.sigmaclient.sigma.config.values;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.math.vector.Vector4f;

public class Value<T> {
    @Expose
    @SerializedName("value")
    private T value;
    @Expose
    @SerializedName("id")
    public String id;
    public Vector4f box = new Vector4f();
    public String name;
    {
        id = "";
    }
    public boolean isHidden(){
        return false;
    }
    public Value(String name){
        this.name = name;
    }
    public Value(String name, T value){
        this.name = name;
        this.value = value;
    }
    public T getValue(){
        return value;
    }
    public void setValue(T value){
        this.value = value;
    }
}
