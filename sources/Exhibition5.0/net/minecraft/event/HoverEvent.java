// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.IChatComponent;

public class HoverEvent
{
    private final Action action;
    private final IChatComponent value;
    private static final String __OBFID = "CL_00001264";
    
    public HoverEvent(final Action p_i45158_1_, final IChatComponent p_i45158_2_) {
        this.action = p_i45158_1_;
        this.value = p_i45158_2_;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public IChatComponent getValue() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ == null || this.getClass() != p_equals_1_.getClass()) {
            return false;
        }
        final HoverEvent var2 = (HoverEvent)p_equals_1_;
        if (this.action != var2.action) {
            return false;
        }
        if (this.value != null) {
            if (!this.value.equals(var2.value)) {
                return false;
            }
        }
        else if (var2.value != null) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "HoverEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
    }
    
    @Override
    public int hashCode() {
        int var1 = this.action.hashCode();
        var1 = 31 * var1 + ((this.value != null) ? this.value.hashCode() : 0);
        return var1;
    }
    
    public enum Action
    {
        SHOW_TEXT("SHOW_TEXT", 0, "show_text", true), 
        SHOW_ACHIEVEMENT("SHOW_ACHIEVEMENT", 1, "show_achievement", true), 
        SHOW_ITEM("SHOW_ITEM", 2, "show_item", true), 
        SHOW_ENTITY("SHOW_ENTITY", 3, "show_entity", true);
        
        private static final Map nameMapping;
        private final boolean allowedInChat;
        private final String canonicalName;
        private static final Action[] $VALUES;
        private static final String __OBFID = "CL_00001265";
        
        private Action(final String p_i45157_1_, final int p_i45157_2_, final String p_i45157_3_, final boolean p_i45157_4_) {
            this.canonicalName = p_i45157_3_;
            this.allowedInChat = p_i45157_4_;
        }
        
        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }
        
        public String getCanonicalName() {
            return this.canonicalName;
        }
        
        public static Action getValueByCanonicalName(final String p_150684_0_) {
            return Action.nameMapping.get(p_150684_0_);
        }
        
        static {
            nameMapping = Maps.newHashMap();
            $VALUES = new Action[] { Action.SHOW_TEXT, Action.SHOW_ACHIEVEMENT, Action.SHOW_ITEM, Action.SHOW_ENTITY };
            for (final Action var4 : values()) {
                Action.nameMapping.put(var4.getCanonicalName(), var4);
            }
        }
    }
}
