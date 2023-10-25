/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;

public class ClickEvent {
    private final String value;
    private final Action action;

    public int hashCode() {
        int n = this.action.hashCode();
        n = 31 * n + (this.value != null ? this.value.hashCode() : 0);
        return n;
    }

    public Action getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "ClickEvent{action=" + (Object)((Object)this.action) + ", value='" + this.value + '\'' + '}';
    }

    public ClickEvent(Action action, String string) {
        this.action = action;
        this.value = string;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            ClickEvent clickEvent = (ClickEvent)object;
            if (this.action != clickEvent.action) {
                return false;
            }
            return !(this.value != null ? !this.value.equals(clickEvent.value) : clickEvent.value != null);
        }
        return false;
    }

    public static enum Action {
        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        TWITCH_USER_INFO("twitch_user_info", false),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true);

        private static final Map<String, Action> nameMapping;
        private final String canonicalName;
        private final boolean allowedInChat;

        static {
            nameMapping = Maps.newHashMap();
            Action[] actionArray = Action.values();
            int n = actionArray.length;
            int n2 = 0;
            while (n2 < n) {
                Action action = actionArray[n2];
                nameMapping.put(action.getCanonicalName(), action);
                ++n2;
            }
        }

        public static Action getValueByCanonicalName(String string) {
            return nameMapping.get(string);
        }

        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }

        public String getCanonicalName() {
            return this.canonicalName;
        }

        private Action(String string2, boolean bl) {
            this.canonicalName = string2;
            this.allowedInChat = bl;
        }
    }
}

