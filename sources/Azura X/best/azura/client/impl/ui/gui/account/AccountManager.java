package best.azura.client.impl.ui.gui.account;

import best.azura.client.api.account.AccountData;
import best.azura.client.util.other.FileUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class AccountManager {

    private final ArrayList<AccountData> data = new ArrayList<>();
    private AccountData last;

    public AccountManager() {
        loadAccounts();
    }

    public void loadAccounts() {

        String content = FileUtil.getContentFromFileAsString(new File("Azura-X", "alts.json"));

        if (!content.isEmpty()) {
            data.addAll(Arrays.asList(new GsonBuilder().create().fromJson(content, AccountData[].class)));
        }
    }

    public void saveAccounts() {
        JsonArray jsonArray = new JsonArray();

        data.forEach(accountData -> jsonArray.add(new JsonParser().parse(new GsonBuilder().setPrettyPrinting().create().toJson(accountData))));

        try {
            FileUtil.writeContentToFile(new File("Azura-X", "alts.json"), new GsonBuilder().setPrettyPrinting().create().toJson(jsonArray), false);
        } catch (Exception ignore) {}
    }

    public void addAccount(AccountData accountData) {
        data.add(accountData);
        saveAccounts();
    }

    public ArrayList<AccountData> getData() {
        return data;
    }

    public AccountData getLast() {
        return last;
    }

    public void setLast(AccountData last) {
        this.last = last;
        if (this.last.getBanned() == null) this.last.setBanned(new ArrayList<>());
    }
}
