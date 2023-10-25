/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.dto.ValueObject;
import com.mojang.realmsclient.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldTemplate
extends ValueObject {
    private static final Logger LOGGER = LogManager.getLogger();
    public String id;
    public String name;
    public String version;
    public String author;
    public String link;
    public String image;
    public String trailer;
    public String recommendedPlayers;
    public WorldTemplateType type;

    public static WorldTemplate parse(JsonObject node) {
        WorldTemplate template = new WorldTemplate();
        try {
            template.id = JsonUtils.getStringOr("id", node, "");
            template.name = JsonUtils.getStringOr("name", node, "");
            template.version = JsonUtils.getStringOr("version", node, "");
            template.author = JsonUtils.getStringOr("author", node, "");
            template.link = JsonUtils.getStringOr("link", node, "");
            template.image = JsonUtils.getStringOr("image", node, null);
            template.trailer = JsonUtils.getStringOr("trailer", node, "");
            template.recommendedPlayers = JsonUtils.getStringOr("recommendedPlayers", node, "");
            template.type = WorldTemplateType.valueOf(JsonUtils.getStringOr("type", node, WorldTemplateType.WORLD_TEMPLATE.name()));
        }
        catch (Exception e) {
            LOGGER.error("Could not parse WorldTemplate: " + e.getMessage());
        }
        return template;
    }

    public static enum WorldTemplateType {
        WORLD_TEMPLATE,
        MINIGAME,
        ADVENTUREMAP,
        EXPERIENCE,
        INSPIRATION;

    }
}

