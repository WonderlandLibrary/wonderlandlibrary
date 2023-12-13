/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;

public class ClickEvent {
    private final Action action;
    private final String value;
    private static final String __OBFID = "CL_00001260";

    public ClickEvent(Action p_i45156_1_, String p_i45156_2_) {
        this.action = p_i45156_1_;
        this.value = p_i45156_2_;
    }

    public Action getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            ClickEvent var2 = (ClickEvent)p_equals_1_;
            if (this.action != var2.action) {
                return false;
            }
            return !(this.value != null ? !this.value.equals(var2.value) : var2.value != null);
        }
        return false;
    }

    public String toString() {
        return "ClickEvent{action=" + (Object)((Object)this.action) + ", value='" + this.value + '\'' + '}';
    }

    public int hashCode() {
        int var1 = this.action.hashCode();
        var1 = 31 * var1 + (this.value != null ? this.value.hashCode() : 0);
        return var1;
    }

    public static enum Action {
        OPEN_URL("OPEN_URL", 0, "open_url", true),
        OPEN_FILE("OPEN_FILE", 1, "open_file", false),
        RUN_COMMAND("RUN_COMMAND", 2, "run_command", true),
        TWITCH_USER_INFO("TWITCH_USER_INFO", 3, "twitch_user_info", false),
        SUGGEST_COMMAND("SUGGEST_COMMAND", 4, "suggest_command", true),
        CHANGE_PAGE("CHANGE_PAGE", 5, "change_page", true);

        private static final Map nameMapping;
        private final boolean allowedInChat;
        private final String canonicalName;
        private static final Action[] $VALUES;
        private static final String __OBFID = "CL_00001261";

        static {
            nameMapping = Maps.newHashMap();
            $VALUES = new Action[]{OPEN_URL, OPEN_FILE, RUN_COMMAND, TWITCH_USER_INFO, SUGGEST_COMMAND, CHANGE_PAGE};
            for (Action var3 : Action.values()) {
                nameMapping.put(var3.getCanonicalName(), var3);
            }
        }

        private Action(String p_i45155_1_, int p_i45155_2_, String p_i45155_3_, boolean p_i45155_4_) {
            this.canonicalName = p_i45155_3_;
            this.allowedInChat = p_i45155_4_;
        }

        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }

        public String getCanonicalName() {
            return this.canonicalName;
        }

        public static Action getValueByCanonicalName(String p_150672_0_) {
            return (Action)((Object)nameMapping.get(p_150672_0_));
        }
    }
}

