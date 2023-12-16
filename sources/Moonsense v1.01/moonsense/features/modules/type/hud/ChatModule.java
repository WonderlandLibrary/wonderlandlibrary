// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.enums.ModuleCategory;
import java.awt.Color;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class ChatModule extends SCModule
{
    public static ChatModule INSTANCE;
    public final Setting color;
    public final Setting shadow;
    public final Setting unlimitedChat;
    public final Setting stackSpamMessages;
    public final Setting chatHeightFix;
    public final Setting highlightName;
    public final Setting highlightedNameColor;
    public final Setting hideIncomingMessages;
    public final Setting smoothChat;
    public final Setting smoothChatSpeed;
    
    public ChatModule() {
        super("Chat", "Modifies chat behavior and looks.");
        new Setting(this, "Style Options");
        this.color = new Setting(this, "Color").setDefault(new Color(0, 0, 0, 50).getRGB(), 0);
        this.shadow = new Setting(this, "Shadow").setDefault(true);
        new Setting(this, "Chat Options");
        this.unlimitedChat = new Setting(this, "Unlimited Chat").setDefault(true);
        this.stackSpamMessages = new Setting(this, "Stack Spam Messages").setDefault(true);
        this.chatHeightFix = new Setting(this, "Chat Height Fix").setDefault(false);
        this.highlightName = new Setting(this, "Highlight Own Name in Chat").setDefault(false);
        this.highlightedNameColor = new Setting(this, "Highlighted Name Color").setDefault(14).setRange("Black", "Dark Blue", "Dark Green", "Dark Aqua", "Dark Red", "Dark Purple", "Gold", "Gray", "Dark Gray", "Blue", "Green", "Aqua", "Red", "Light Purple", "Yellow", "White");
        this.hideIncomingMessages = new Setting(this, "Hide Incoming Messages").setDefault(false);
        new Setting(this, "Smooth Chat Options");
        this.smoothChat = new Setting(this, "Smooth Chat").setDefault(true);
        this.smoothChatSpeed = new Setting(this, "Smooth Chat Speed").setDefault(1.0f).setRange(0.0f, 5.0f, 0.01f);
        ChatModule.INSTANCE = this;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    public String getNameHighlight() {
        final int index = this.highlightedNameColor.getInt();
        if (index == 0) {
            return "\u00ef¿½0";
        }
        if (index == 1) {
            return "\u00ef¿½1";
        }
        if (index == 2) {
            return "\u00ef¿½2";
        }
        if (index == 3) {
            return "\u00ef¿½3";
        }
        if (index == 4) {
            return "\u00ef¿½4";
        }
        if (index == 5) {
            return "\u00ef¿½5";
        }
        if (index == 6) {
            return "\u00ef¿½6";
        }
        if (index == 7) {
            return "\u00ef¿½7";
        }
        if (index == 8) {
            return "\u00ef¿½8";
        }
        if (index == 9) {
            return "\u00ef¿½9";
        }
        if (index == 10) {
            return "\u00ef¿½a";
        }
        if (index == 11) {
            return "\u00ef¿½b";
        }
        if (index == 12) {
            return "\u00ef¿½c";
        }
        if (index == 13) {
            return "\u00ef¿½d";
        }
        if (index == 14) {
            return "\u00ef¿½e";
        }
        if (index == 15) {
            return "\u00ef¿½f";
        }
        return "\u00ef¿½r";
    }
}
