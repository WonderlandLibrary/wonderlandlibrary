/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package com.thealtening.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thealtening.api.data.AccountData;
import com.thealtening.api.data.LicenseData;
import com.thealtening.api.utils.HttpUtils;
import com.thealtening.api.utils.exceptions.TheAlteningException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class TheAltening
extends HttpUtils {
    private final String apiKey;
    private final String endpoint = "http://api.thealtening.com/v1/";
    private final Logger logger = Logger.getLogger("TheAltening");
    private final Gson gson = new Gson();

    public TheAltening(String apiKey) {
        this.apiKey = apiKey;
    }

    public LicenseData getLicenseData() throws TheAlteningException {
        try {
            String connectionData = this.connect("http://api.thealtening.com/v1/license?token=" + this.apiKey);
            JsonObject jsonObject = (JsonObject)this.gson.fromJson(connectionData, JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return (LicenseData)this.gson.fromJson((JsonElement)jsonObject, LicenseData.class);
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }

    public AccountData getAccountData() throws TheAlteningException {
        try {
            String connectionData = this.connect("http://api.thealtening.com/v1/generate?info=true&token=" + this.apiKey);
            JsonObject jsonObject = (JsonObject)this.gson.fromJson(connectionData, JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return (AccountData)this.gson.fromJson((JsonElement)jsonObject, AccountData.class);
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }

    public boolean isPrivate(String token) throws TheAlteningException {
        try {
            String connectionData = this.connect("http://api.thealtening.com/v1/private?acctoken=" + token + "&token=" + this.apiKey);
            JsonObject jsonObject = (JsonObject)this.gson.fromJson(connectionData, JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("success")) {
                return jsonObject.get("success").getAsBoolean();
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return false;
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }

    public boolean isFavorite(String token) throws TheAlteningException {
        try {
            String connectionData = this.connect("http://api.thealtening.com/v1/favorite?acctoken=" + token + "&token=" + this.apiKey);
            JsonObject jsonObject = (JsonObject)this.gson.fromJson(connectionData, JsonObject.class);
            if (jsonObject == null) {
                throw new TheAlteningException("JSON", "Unable to parse JSON data, here's what is in there: \n" + connectionData);
            }
            if (jsonObject.has("success")) {
                return jsonObject.get("success").getAsBoolean();
            }
            if (jsonObject.has("error") && jsonObject.has("errorMessage")) {
                throw new TheAlteningException(jsonObject.get("error").getAsString(), jsonObject.get("errorMessage").getAsString());
            }
            return false;
        }
        catch (IOException e) {
            throw new TheAlteningException("IOException", "Unable to establish a connection, here's why: \n" + e.getCause());
        }
    }

    public static class Asynchronous {
        private final TheAltening instance;

        public Asynchronous(TheAltening instance) {
            this.instance = instance;
        }

        public CompletableFuture<LicenseData> getLicenseData() {
            CompletableFuture<LicenseData> returnValue = new CompletableFuture<LicenseData>();
            try {
                returnValue.complete(this.instance.getLicenseData());
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }

        public CompletableFuture<AccountData> getAccountData() {
            CompletableFuture<AccountData> returnValue = new CompletableFuture<AccountData>();
            try {
                returnValue.complete(this.instance.getAccountData());
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }

        public CompletableFuture<Boolean> isPrivate(String token) {
            CompletableFuture<Boolean> returnValue = new CompletableFuture<Boolean>();
            try {
                returnValue.complete(this.instance.isPrivate(token));
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }

        public CompletableFuture<Boolean> isFavorite(String token) {
            CompletableFuture<Boolean> returnValue = new CompletableFuture<Boolean>();
            try {
                returnValue.complete(this.instance.isFavorite(token));
            }
            catch (TheAlteningException exception) {
                returnValue.completeExceptionally(exception);
            }
            return returnValue;
        }
    }
}

