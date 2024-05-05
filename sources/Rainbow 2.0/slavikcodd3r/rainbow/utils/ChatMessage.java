// 
// Decompiled by Procyon v0.5.30
// 

package slavikcodd3r.rainbow.utils;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import slavikcodd3r.rainbow.Rainbow;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatMessage
{
    private final ChatComponentText message;
    
    private ChatMessage(final ChatComponentText message) {
        this.message = message;
    }
    
    public void displayClientSided() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(this.message);
    }
    
    private ChatComponentText getChatComponent() {
        return this.message;
    }
    
    public static class ChatMessageBuilder
    {
        private static final EnumChatFormatting defaultMessageColor;
        private ChatComponentText theMessage;
        private boolean useDefaultMessageColor;
        private ChatStyle workingStyle;
        private ChatComponentText workerMessage;
        
        static {
            defaultMessageColor = EnumChatFormatting.WHITE;
        }
        
        public ChatMessageBuilder(final boolean prependDefaultPrefix, final boolean useDefaultMessageColor) {
            this.theMessage = new ChatComponentText("");
            this.useDefaultMessageColor = false;
            this.workingStyle = new ChatStyle();
            this.workerMessage = new ChatComponentText("");
            if (prependDefaultPrefix) {
                this.theMessage.appendSibling(new ChatMessageBuilder(false, false).appendText(String.valueOf("§c§lR§6§la§e§li§a§ln§b§lb§1§lo§d§lw §7§l2.0") + "§l: ").setColor(EnumChatFormatting.WHITE).build().getChatComponent());
            }
            this.useDefaultMessageColor = useDefaultMessageColor;
        }
        
        public ChatMessageBuilder() {
            this.theMessage = new ChatComponentText("");
            this.useDefaultMessageColor = false;
            this.workingStyle = new ChatStyle();
            this.workerMessage = new ChatComponentText("");
        }
        
        public ChatMessageBuilder appendText(final String text) {
            this.appendSibling();
            this.workerMessage = new ChatComponentText(text);
            this.workingStyle = new ChatStyle();
            if (this.useDefaultMessageColor) {
                this.setColor(ChatMessageBuilder.defaultMessageColor);
            }
            return this;
        }
        
        public ChatMessageBuilder setColor(final EnumChatFormatting color) {
            this.workingStyle.setColor(color);
            return this;
        }
        
        public ChatMessageBuilder bold() {
            this.workingStyle.setBold(true);
            return this;
        }
        
        public ChatMessageBuilder italic() {
            this.workingStyle.setItalic(true);
            return this;
        }
        
        public ChatMessageBuilder strikethrough() {
            this.workingStyle.setStrikethrough(true);
            return this;
        }
        
        public ChatMessageBuilder underline() {
            this.workingStyle.setUnderlined(true);
            return this;
        }
        
        public ChatMessage build() {
            this.appendSibling();
            return new ChatMessage(this.theMessage);
        }
        
        private void appendSibling() {
            this.theMessage.appendSibling(this.workerMessage.setChatStyle(this.workingStyle));
        }
    }
}
