// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;
import java.awt.image.BufferedImage;

public class ClickEvent
{
    private final Action action;
    private final String value;
    private BufferedImage possibleImgurImage;
    private static final String __OBFID = "CL_00001260";
    
    public ClickEvent(final Action p_i45156_1_, final String p_i45156_2_) {
        this.action = p_i45156_1_;
        this.value = p_i45156_2_;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public void setPossibleImgurImage(final BufferedImage possibleImgurImage) {
        this.possibleImgurImage = possibleImgurImage;
    }
    
    public BufferedImage getPossibleImgurImage() {
        return this.possibleImgurImage;
    }
    
    public String getValue() {
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
        final ClickEvent var2 = (ClickEvent)p_equals_1_;
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
        return "ClickEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
    }
    
    @Override
    public int hashCode() {
        int var1 = this.action.hashCode();
        var1 = 31 * var1 + ((this.value != null) ? this.value.hashCode() : 0);
        return var1;
    }
    
    public enum Action
    {
        OPEN_URL("OPEN_URL", 0, "OPEN_URL", 0, "open_url", true), 
        OPEN_FILE("OPEN_FILE", 1, "OPEN_FILE", 1, "open_file", false), 
        RUN_COMMAND("RUN_COMMAND", 2, "RUN_COMMAND", 2, "run_command", true), 
        TWITCH_USER_INFO("TWITCH_USER_INFO", 3, "TWITCH_USER_INFO", 3, "twitch_user_info", false), 
        SUGGEST_COMMAND("SUGGEST_COMMAND", 4, "SUGGEST_COMMAND", 4, "suggest_command", true), 
        CHANGE_PAGE("CHANGE_PAGE", 5, "CHANGE_PAGE", 5, "change_page", true), 
        UPLOAD_TO_IMGUR("UPLOAD_TO_IMGUR", 6, "UPLOAD_TO_IMGUR", 6, "upload_to_imgur", true), 
        COPY_IMAGE("COPY_IMAGE", 7, "COPY_IMAGE", 7, "copy_image", true), 
        COPY_TEXT("COPY_TEXT", 8, "COPY_TEXT", 8, "copy_text", true);
        
        private static final Map nameMapping;
        private final boolean allowedInChat;
        private final String canonicalName;
        private static final Action[] $VALUES;
        private static final String __OBFID = "CL_00001261";
        
        static {
            nameMapping = Maps.newHashMap();
            $VALUES = new Action[] { Action.OPEN_URL, Action.OPEN_FILE, Action.RUN_COMMAND, Action.TWITCH_USER_INFO, Action.SUGGEST_COMMAND, Action.CHANGE_PAGE, Action.UPLOAD_TO_IMGUR, Action.COPY_IMAGE, Action.COPY_TEXT };
            for (final Action var4 : values()) {
                Action.nameMapping.put(var4.getCanonicalName(), var4);
            }
        }
        
        private Action(final String name, final int ordinal, final String p_i45155_1_, final int p_i45155_2_, final String p_i45155_3_, final boolean p_i45155_4_) {
            this.canonicalName = p_i45155_3_;
            this.allowedInChat = p_i45155_4_;
        }
        
        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }
        
        public String getCanonicalName() {
            return this.canonicalName;
        }
        
        public static Action getValueByCanonicalName(final String p_150672_0_) {
            return Action.nameMapping.get(p_150672_0_);
        }
    }
}
