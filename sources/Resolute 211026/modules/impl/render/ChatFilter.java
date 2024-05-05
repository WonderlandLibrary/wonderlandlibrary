// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.RandomUtils;
import vip.Resolute.events.impl.EventChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class ChatFilter extends Module
{
    public ModeSetting mode;
    private static final String[] INVIS_CHARS;
    private String lastMessage;
    private int amount;
    private int line;
    
    public ChatFilter() {
        super("ChatFilter", 0, "Filters Minecraft chat", Category.RENDER);
        this.mode = new ModeSetting("Mode", "Filter", new String[] { "Filter", "Bypass" });
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEvent(final Event e) {
        this.lastMessage = "";
        if (e instanceof EventPacket) {
            if (((EventPacket)e).getPacket() instanceof S2EPacketCloseWindow) {
                if (this.isTypingInChat()) {
                    e.setCancelled(true);
                }
            }
            else if (((EventPacket)e).getPacket() instanceof S02PacketChat) {
                if (this.mode.is("Filter")) {
                    final S02PacketChat s02PacketChat = ((EventPacket)e).getPacket();
                    if (s02PacketChat.getType() == 0) {
                        final IChatComponent message = s02PacketChat.getChatComponent();
                        final String rawMessage = message.getFormattedText();
                        final GuiNewChat chatGui = ChatFilter.mc.ingameGUI.getChatGUI();
                        if (this.lastMessage.equals(rawMessage)) {
                            chatGui.deleteChatLine(this.line);
                            ++this.amount;
                            s02PacketChat.getChatComponent().appendText(EnumChatFormatting.GRAY + " [x" + this.amount + "]");
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
                        e.setCancelled(true);
                    }
                }
                if (this.mode.is("Bypass") && e instanceof EventChat) {
                    final EventChat eventChat = (EventChat)e;
                    if (!eventChat.getMessage().startsWith("/")) {
                        final StringBuilder stringBuilder = new StringBuilder();
                        for (final char character : eventChat.getMessage().toCharArray()) {
                            stringBuilder.append(character).append(ChatFilter.INVIS_CHARS[RandomUtils.nextInt(0, ChatFilter.INVIS_CHARS.length)]);
                        }
                        eventChat.setMessage(stringBuilder.toString());
                    }
                }
            }
        }
    }
    
    private boolean isTypingInChat() {
        return ChatFilter.mc.currentScreen instanceof GuiChat;
    }
    
    static {
        INVIS_CHARS = new String[] { "\u00e2\u203a\ufffd", "\u00e2\u203a\u02dc", "\u00e2\u203a\u0153", "\u00e2\u203a ", "\u00e2\u203a\u0178", "\u00e2\u203a\ufffd", "\u00e2\u203a\ufffd", "\u00e2\u203a¡", "\u00e2\u203a\u2039", "\u00e2\u203a\u0152", "\u00e2\u203a\u2014", "\u00e2\u203a©", "\u00e2\u203a\u2030" };
    }
}
