package summer.base.manager;

import com.google.gson.annotations.SerializedName;

public class Friend {

    @SerializedName(value = "name")
    private String name;

    @SerializedName(value = "alias")
    private String alias;

    public Friend(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}