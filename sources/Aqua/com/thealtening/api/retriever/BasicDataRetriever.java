package com.thealtening.api.retriever;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;
import com.thealtening.api.retriever.AsynchronousDataRetriever;
import com.thealtening.api.retriever.DataRetriever;
import java.util.ArrayList;
import java.util.List;

public class BasicDataRetriever
implements DataRetriever {
    private String apiKey;

    public BasicDataRetriever(String apiKey) {
        this.apiKey = apiKey;
    }

    public void updateKey(String newApiKey) {
        this.apiKey = newApiKey;
    }

    public License getLicense() throws TheAlteningException {
        JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v2/license?key=" + this.apiKey).getAsJsonObject();
        return (License)gson.fromJson((JsonElement)jsonObject, License.class);
    }

    public Account getAccount() throws TheAlteningException {
        JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v2/generate?info=true&key=" + this.apiKey).getAsJsonObject();
        return (Account)gson.fromJson((JsonElement)jsonObject, Account.class);
    }

    public boolean isPrivate(String token) throws TheAlteningException {
        JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v2/private?token=" + token + "&key=" + this.apiKey).getAsJsonObject();
        return this.isSuccess(jsonObject);
    }

    public boolean isFavorite(String token) throws TheAlteningException {
        JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v2/favorite?token=" + token + "&key=" + this.apiKey).getAsJsonObject();
        return this.isSuccess(jsonObject);
    }

    public List<Account> getPrivatedAccounts() {
        ArrayList privatedAccountList = new ArrayList();
        JsonArray privatedAccountsObject = this.retrieveData("http://api.thealtening.com/v2/privates?key=" + this.apiKey).getAsJsonArray();
        for (JsonElement jsonElement : privatedAccountsObject) {
            if (!jsonElement.isJsonObject()) continue;
            privatedAccountList.add(gson.fromJson(jsonElement, Account.class));
        }
        return privatedAccountList;
    }

    public List<Account> getFavoriteAccounts() {
        ArrayList favoritedAccountList = new ArrayList();
        JsonArray favoritedAccountsObject = this.retrieveData("http://api.thealtening.com/v2/favorites?key=" + this.apiKey).getAsJsonArray();
        for (JsonElement jsonElement : favoritedAccountsObject) {
            if (!jsonElement.isJsonObject()) continue;
            favoritedAccountList.add(gson.fromJson(jsonElement, Account.class));
        }
        return favoritedAccountList;
    }

    public AsynchronousDataRetriever toAsync() {
        return new AsynchronousDataRetriever(this.apiKey);
    }
}
