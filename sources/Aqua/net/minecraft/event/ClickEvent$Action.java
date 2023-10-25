package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;

public static enum ClickEvent.Action {
    OPEN_URL("open_url", true),
    OPEN_FILE("open_file", false),
    RUN_COMMAND("run_command", true),
    TWITCH_USER_INFO("twitch_user_info", false),
    SUGGEST_COMMAND("suggest_command", true),
    CHANGE_PAGE("change_page", true);

    private static final Map<String, ClickEvent.Action> nameMapping;
    private final boolean allowedInChat;
    private final String canonicalName;

    private ClickEvent.Action(String canonicalNameIn, boolean allowedInChatIn) {
        this.canonicalName = canonicalNameIn;
        this.allowedInChat = allowedInChatIn;
    }

    public boolean shouldAllowInChat() {
        return this.allowedInChat;
    }

    public String getCanonicalName() {
        return this.canonicalName;
    }

    public static ClickEvent.Action getValueByCanonicalName(String canonicalNameIn) {
        return (ClickEvent.Action)((Object)nameMapping.get((Object)canonicalNameIn));
    }

    static {
        nameMapping = Maps.newHashMap();
        for (ClickEvent.Action clickevent$action : ClickEvent.Action.values()) {
            nameMapping.put((Object)clickevent$action.getCanonicalName(), (Object)clickevent$action);
        }
    }
}
