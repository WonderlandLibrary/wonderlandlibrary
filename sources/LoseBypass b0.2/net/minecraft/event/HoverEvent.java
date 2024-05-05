/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.IChatComponent;

public class HoverEvent {
    private final Action action;
    private final IChatComponent value;

    public HoverEvent(Action actionIn, IChatComponent valueIn) {
        this.action = actionIn;
        this.value = valueIn;
    }

    public Action getAction() {
        return this.action;
    }

    public IChatComponent getValue() {
        return this.value;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ == null) return false;
        if (this.getClass() != p_equals_1_.getClass()) return false;
        HoverEvent hoverevent = (HoverEvent)p_equals_1_;
        if (this.action != hoverevent.action) {
            return false;
        }
        if (this.value != null) {
            if (this.value.equals(hoverevent.value)) return true;
            return false;
        }
        if (hoverevent.value == null) return true;
        return false;
    }

    public String toString() {
        return "HoverEvent{action=" + (Object)((Object)this.action) + ", value='" + this.value + '\'' + '}';
    }

    public int hashCode() {
        int i = this.action.hashCode();
        return 31 * i + (this.value != null ? this.value.hashCode() : 0);
    }

    public static enum Action {
        SHOW_TEXT("show_text", true),
        SHOW_ACHIEVEMENT("show_achievement", true),
        SHOW_ITEM("show_item", true),
        SHOW_ENTITY("show_entity", true);

        private static final Map<String, Action> nameMapping;
        private final boolean allowedInChat;
        private final String canonicalName;

        private Action(String canonicalNameIn, boolean allowedInChatIn) {
            this.canonicalName = canonicalNameIn;
            this.allowedInChat = allowedInChatIn;
        }

        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }

        public String getCanonicalName() {
            return this.canonicalName;
        }

        public static Action getValueByCanonicalName(String canonicalNameIn) {
            return nameMapping.get(canonicalNameIn);
        }

        static {
            nameMapping = Maps.newHashMap();
            Action[] actionArray = Action.values();
            int n = actionArray.length;
            int n2 = 0;
            while (n2 < n) {
                Action hoverevent$action = actionArray[n2];
                nameMapping.put(hoverevent$action.getCanonicalName(), hoverevent$action);
                ++n2;
            }
        }
    }
}

