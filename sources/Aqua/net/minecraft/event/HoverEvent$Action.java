package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;

public static enum HoverEvent.Action {
    SHOW_TEXT("show_text", true),
    SHOW_ACHIEVEMENT("show_achievement", true),
    SHOW_ITEM("show_item", true),
    SHOW_ENTITY("show_entity", true);

    private static final Map<String, HoverEvent.Action> nameMapping;
    private final boolean allowedInChat;
    private final String canonicalName;

    private HoverEvent.Action(String canonicalNameIn, boolean allowedInChatIn) {
        this.canonicalName = canonicalNameIn;
        this.allowedInChat = allowedInChatIn;
    }

    public boolean shouldAllowInChat() {
        return this.allowedInChat;
    }

    public String getCanonicalName() {
        return this.canonicalName;
    }

    public static HoverEvent.Action getValueByCanonicalName(String canonicalNameIn) {
        return (HoverEvent.Action)((Object)nameMapping.get((Object)canonicalNameIn));
    }

    static {
        nameMapping = Maps.newHashMap();
        for (HoverEvent.Action hoverevent$action : HoverEvent.Action.values()) {
            nameMapping.put((Object)hoverevent$action.getCanonicalName(), (Object)hoverevent$action);
        }
    }
}
