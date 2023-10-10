package tech.atani.client.files.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import tech.atani.client.feature.account.Account;
import tech.atani.client.feature.account.storage.AccountStorage;
import tech.atani.client.files.LocalFile;
import tech.atani.client.files.data.FileData;

import java.util.Map;

@FileData(fileName = "accounts")
public class AccountsFile extends LocalFile {

    @Override
    public void save(Gson gson) {
        JsonObject object = new JsonObject();

        JsonObject accountsObject = new JsonObject();

        for (Account account : AccountStorage.getInstance().getList())
            accountsObject.add(account.getName(), account.save());

        object.add("Accounts", accountsObject);

        writeFile(gson.toJson(object), file);
    }

    @Override
    public void load(Gson gson) {
        if (!file.exists()) {
            return;
        }

        JsonObject object = gson.fromJson(readFile(file), JsonObject.class);
        if (object.has("Accounts")){
            JsonObject accountsObject = object.getAsJsonObject("Accounts");
            for(Map.Entry<String, JsonElement> entry : accountsObject.entrySet()) {
                JsonObject jsonObject = entry.getValue().getAsJsonObject();
                AccountStorage.getInstance().add(new Account(entry.getKey(), jsonObject.get("Password").getAsString(), jsonObject.get("Cracked").getAsBoolean()));
            }
        }
    }

}