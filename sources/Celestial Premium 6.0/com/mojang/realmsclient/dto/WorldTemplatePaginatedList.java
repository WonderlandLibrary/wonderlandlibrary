/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.dto.ValueObject;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldTemplatePaginatedList
extends ValueObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public List<WorldTemplate> templates;
    public int page;
    public int size;
    public int total;

    public WorldTemplatePaginatedList() {
    }

    public WorldTemplatePaginatedList(WorldTemplatePaginatedList src) {
        this.set(src);
    }

    public void set(WorldTemplatePaginatedList src) {
        this.templates = new ArrayList<WorldTemplate>(src.templates == null ? new ArrayList() : src.templates);
        this.page = src.page;
        this.size = src.size;
        this.total = src.total;
    }

    public boolean isLastPage() {
        boolean b = this.page * this.size >= this.total && this.page > 0 && this.total > 0 && this.size > 0;
        return b;
    }

    public static WorldTemplatePaginatedList parse(String json) {
        WorldTemplatePaginatedList list = new WorldTemplatePaginatedList();
        list.templates = new ArrayList<WorldTemplate>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(json).getAsJsonObject();
            if (object.get("templates").isJsonArray()) {
                Iterator<JsonElement> it = object.get("templates").getAsJsonArray().iterator();
                while (it.hasNext()) {
                    list.templates.add(WorldTemplate.parse(it.next().getAsJsonObject()));
                }
            }
            list.page = JsonUtils.getIntOr("page", object, 0);
            list.size = JsonUtils.getIntOr("size", object, 0);
            list.total = JsonUtils.getIntOr("total", object, 0);
        }
        catch (Exception e) {
            LOGGER.error("Could not parse WorldTemplatePaginatedList: " + e.getMessage());
        }
        return list;
    }
}

