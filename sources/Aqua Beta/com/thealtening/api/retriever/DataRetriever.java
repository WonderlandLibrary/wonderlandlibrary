package com.thealtening.api.retriever;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface DataRetriever {
    public static final Logger LOGGER = Logger.getLogger((String)"The Altening");
    public static final Gson gson = new GsonBuilder().create();
    public static final String BASE_URL = "http://api.thealtening.com/v2/";
    public static final String LICENCE_URL = "http://api.thealtening.com/v2/license?key=";
    public static final String GENERATE_URL = "http://api.thealtening.com/v2/generate?info=true&key=";
    public static final String PRIVATE_ACC_URL = "http://api.thealtening.com/v2/private?token=";
    public static final String FAVORITE_ACC_URL = "http://api.thealtening.com/v2/favorite?token=";
    public static final String PRIVATES_URL = "http://api.thealtening.com/v2/privates?key=";
    public static final String FAVORITES_URL = "http://api.thealtening.com/v2/favorites?key=";

    public License getLicense();

    public Account getAccount();

    public boolean isPrivate(String var1) throws TheAlteningException;

    public boolean isFavorite(String var1) throws TheAlteningException;

    public List<Account> getPrivatedAccounts();

    public List<Account> getFavoriteAccounts();

    public void updateKey(String var1);

    default public JsonElement retrieveData(String url) throws TheAlteningException {
        JsonElement jsonElement;
        String response;
        try {
            response = this.connect(url);
            jsonElement = (JsonElement)gson.fromJson(response, JsonElement.class);
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while reading retrieved data from the website");
            throw new TheAlteningException("IO", e.getCause());
        }
        if (jsonElement == null) {
            LOGGER.log(Level.SEVERE, "Error while parsing website's response");
            throw new TheAlteningException("JSON", "Parsing error: \n" + response);
        }
        if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("error") && jsonElement.getAsJsonObject().has("errorMessage")) {
            LOGGER.log(Level.SEVERE, "The website returned, type: " + jsonElement.getAsJsonObject().get("error").getAsString() + ". Details:" + jsonElement.getAsJsonObject().get("errorMessage").getAsString());
            throw new TheAlteningException("Connection", "Bad response");
        }
        return jsonElement;
    }

    default public boolean isSuccess(JsonObject jsonObject) {
        return jsonObject.has("success") && jsonObject.get("success").getAsBoolean();
    }

    default public String connect(String link) throws IOException {
        Charset encoding;
        StringBuilder stringBuilder = new StringBuilder();
        URLConnection connection = new URL(link).openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        InputStream connectionStream = connection.getInputStream();
        String encodingId = connection.getContentEncoding();
        try {
            encoding = encodingId == null ? StandardCharsets.UTF_8 : Charset.forName((String)encodingId);
        }
        catch (UnsupportedCharsetException ex) {
            encoding = StandardCharsets.UTF_8;
        }
        try (BufferedReader reader = new BufferedReader((Reader)new InputStreamReader(connectionStream, encoding));){
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
