package best.azura.client.api.account;

import best.azura.client.impl.Client;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AccountData {

    @SerializedName("data")
    private String data;
    @SerializedName("type")
    private AccountType type;
    @SerializedName("username")
    private String username;
    @SerializedName("banned")
    private ArrayList<BannedData> banned = new ArrayList<>();

    public AccountData(AccountType type, String data) {
        this.type = type;
        this.data = data;
    }

    public void login() {
        new Thread(() -> {
            setUsername(getType().login(getData()));
            Client.INSTANCE.getAccountManager().setLast(this);
        }).start();
    }

    public void loginSync() {
        setUsername(getType().login(getData()));
        Client.INSTANCE.getAccountManager().setLast(this);
    }

    public String getData() {
    }

    public void setData(String data) {
        this.data = data;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<BannedData> getBanned() {
        return banned;
    }

    public void setBanned(ArrayList<BannedData> banned) {
        this.banned = banned;
    }
}
