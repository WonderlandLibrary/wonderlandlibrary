package com.enjoytheban.utils;

import com.enjoytheban.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {
	
private final ChatComponentText message;
    
    private ChatUtils(final ChatComponentText message) {
        this.message = message;
    }
    
    public static String addFormat(String message, String regex) {
        return message.replaceAll("(?i)" + regex + "([0-9a-fklmnor])", "\u00a7$1");
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
            	this.theMessage.appendSibling(new ChatMessageBuilder(false, false).appendText(String.valueOf(EnumChatFormatting.AQUA + Client.instance.name + " ")).setColor(EnumChatFormatting.RED).build().getChatComponent());
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
        
        public ChatUtils build() {
            this.appendSibling();
            return new ChatUtils(this.theMessage);
        }
        
        private void appendSibling() {
            this.theMessage.appendSibling(this.workerMessage.setChatStyle(this.workingStyle));
        }
    }
}

