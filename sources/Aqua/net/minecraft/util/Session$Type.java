package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;

public static enum Session.Type {
    LEGACY("legacy"),
    MOJANG("mojang");

    private static final Map<String, Session.Type> SESSION_TYPES;
    private final String sessionType;

    private Session.Type(String sessionTypeIn) {
        this.sessionType = sessionTypeIn;
    }

    public static Session.Type setSessionType(String sessionTypeIn) {
        return (Session.Type)((Object)SESSION_TYPES.get((Object)sessionTypeIn.toLowerCase()));
    }

    static {
        SESSION_TYPES = Maps.newHashMap();
        for (Session.Type session$type : Session.Type.values()) {
            SESSION_TYPES.put((Object)session$type.sessionType, (Object)session$type);
        }
    }
}
