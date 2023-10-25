/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.IChatComponent;

public class HoverEvent {
    private final IChatComponent value;
    private final Action action;

    public Action getAction() {
        return this.action;
    }

    public HoverEvent(Action action, IChatComponent iChatComponent) {
        this.action = action;
        this.value = iChatComponent;
    }

    public int hashCode() {
        int n = this.action.hashCode();
        n = 31 * n + (this.value != null ? this.value.hashCode() : 0);
        return n;
    }

    public IChatComponent getValue() {
        return this.value;
    }

    public String toString() {
        return "HoverEvent{action=" + (Object)((Object)this.action) + ", value='" + this.value + '\'' + '}';
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            HoverEvent hoverEvent = (HoverEvent)object;
            if (this.action != hoverEvent.action) {
                return false;
            }
            return !(this.value != null ? !this.value.equals(hoverEvent.value) : hoverEvent.value != null);
        }
        return false;
    }

    public static enum Action {
        SHOW_TEXT("show_text", true),
        SHOW_ACHIEVEMENT("show_achievement", true),
        SHOW_ITEM("show_item", true),
        SHOW_ENTITY("show_entity", true);

        private static final Map<String, Action> nameMapping;
        private final boolean allowedInChat;
        private final String canonicalName;

        public static Action getValueByCanonicalName(String string) {
            return nameMapping.get(string);
        }

        public String getCanonicalName() {
            return this.canonicalName;
        }

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

        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }

        private Action(String string2, boolean bl) {
            this.canonicalName = string2;
            this.allowedInChat = bl;
        }
    }
}

