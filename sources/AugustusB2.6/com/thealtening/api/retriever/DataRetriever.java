// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.api.retriever;

import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Level;
import com.google.gson.JsonElement;
import java.util.List;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;
import com.google.gson.Gson;
import java.util.logging.Logger;

public interface DataRetriever
{
    public static final Logger LOGGER = Logger.getLogger("The Altening");
    public static final Gson gson = new GsonBuilder().create();
    public static final String BASE_URL = "http://api.thealtening.com/v2/";
    public static final String LICENCE_URL = "http://api.thealtening.com/v2/license?key=";
    public static final String GENERATE_URL = "http://api.thealtening.com/v2/generate?info=true&key=";
    public static final String PRIVATE_ACC_URL = "http://api.thealtening.com/v2/private?token=";
    public static final String FAVORITE_ACC_URL = "http://api.thealtening.com/v2/favorite?token=";
    public static final String PRIVATES_URL = "http://api.thealtening.com/v2/privates?key=";
    public static final String FAVORITES_URL = "http://api.thealtening.com/v2/favorites?key=";
    
    License getLicense();
    
    Account getAccount();
    
    boolean isPrivate(final String p0) throws TheAlteningException;
    
    boolean isFavorite(final String p0) throws TheAlteningException;
    
    List<Account> getPrivatedAccounts();
    
    List<Account> getFavoriteAccounts();
    
    void updateKey(final String p0);
    
    default JsonElement retrieveData(final String url) throws TheAlteningException {
        String response;
        JsonElement jsonElement;
        try {
            response = this.connect(url);
            jsonElement = DataRetriever.gson.fromJson(response, JsonElement.class);
        }
        catch (IOException e) {
            DataRetriever.LOGGER.log(Level.SEVERE, "Error while reading retrieved data from the website");
            throw new TheAlteningException("IO", e.getCause());
        }
        if (jsonElement == null) {
            DataRetriever.LOGGER.log(Level.SEVERE, "Error while parsing website's response");
            throw new TheAlteningException("JSON", "Parsing error: \n" + response);
        }
        if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("error") && jsonElement.getAsJsonObject().has("errorMessage")) {
            DataRetriever.LOGGER.log(Level.SEVERE, "The website returned, type: " + jsonElement.getAsJsonObject().get("error").getAsString() + ". Details:" + jsonElement.getAsJsonObject().get("errorMessage").getAsString());
            throw new TheAlteningException("Connection", "Bad response");
        }
        return jsonElement;
    }
    
    default boolean isSuccess(final JsonObject jsonObject) {
        return jsonObject.has("success") && jsonObject.get("success").getAsBoolean();
    }
    
    default String connect(final String link) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        final URLConnection connection = new URL(link).openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        final InputStream connectionStream = connection.getInputStream();
        final String encodingId = connection.getContentEncoding();
        try {
            final Charset encoding = (encodingId == null) ? StandardCharsets.UTF_8 : Charset.forName(encodingId);
        }
        catch (UnsupportedCharsetException ex) {
            final Charset encoding = StandardCharsets.UTF_8;
        }
        Charset encoding;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connectionStream, encoding))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
