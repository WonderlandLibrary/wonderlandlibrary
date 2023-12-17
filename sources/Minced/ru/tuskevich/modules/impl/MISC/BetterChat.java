// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.Packet;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.ChatType;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "BetterChat", type = Type.MISC)
public class BetterChat extends Module
{
    public static BooleanSetting removeRect;
    public static BooleanSetting antiSpam;
    public static BooleanSetting chatAnimation;
    private String lastMessage;
    private int amount;
    private int line;
    
    public BetterChat() {
        this.lastMessage = "";
        this.add(BetterChat.antiSpam, BetterChat.chatAnimation, BetterChat.removeRect);
    }
    
    @EventTarget
    public void onReceivePacket(final EventPacket event) {
        if (!BetterChat.antiSpam.get()) {
            return;
        }
        final Packet<?> packet = (Packet<?>)event.getPacket();
        if (packet instanceof SPacketCloseWindow) {
            if (BetterChat.mc.currentScreen instanceof GuiChat) {
                event.cancel();
            }
        }
        else {
            final SPacketChat sPacketChat;
            if (packet instanceof SPacketChat && (sPacketChat = (SPacketChat)packet).getType() == ChatType.CHAT) {
                final ITextComponent message = sPacketChat.getChatComponent();
                final String rawMessage = message.getFormattedText();
                final GuiNewChat chatGui = BetterChat.mc.ingameGUI.getChatGUI();
                if (this.lastMessage.equals(rawMessage)) {
                    chatGui.deleteChatLine(this.line);
                    ++this.amount;
                    sPacketChat.getChatComponent().appendText(TextFormatting.GRAY + " [x" + this.amount + "]");
                }
                else {
                    this.amount = 1;
                }
                ++this.line;
                this.lastMessage = rawMessage;
                chatGui.printChatMessageWithOptionalDeletion(message, this.line);
                if (this.line > 256) {
                    this.line = 0;
                }
                event.cancel();
            }
        }
    }
    
    static {
        BetterChat.removeRect = new BooleanSetting("Remove Rect", false);
        BetterChat.antiSpam = new BooleanSetting("Anti Spam", false);
        BetterChat.chatAnimation = new BooleanSetting("Chat Animation", false);
    }
}
