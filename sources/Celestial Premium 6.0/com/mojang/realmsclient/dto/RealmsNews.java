/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.dto.ValueObject;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsNews
extends ValueObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public String newsLink;

    public static RealmsNews parse(String json) {
        RealmsNews news = new RealmsNews();
        try {
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(json).getAsJsonObject();
            news.newsLink = JsonUtils.getStringOr("newsLink", object, null);
        }
        catch (Exception e) {
            LOGGER.error("Could not parse RealmsNews: " + e.getMessage());
        }
        return news;
    }
}

