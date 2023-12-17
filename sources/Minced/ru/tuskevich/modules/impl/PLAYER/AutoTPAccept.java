// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import ru.tuskevich.managers.Friend;
import ru.tuskevich.Minced;
import net.minecraft.network.play.server.SPacketChat;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AutoTPAccept", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class AutoTPAccept extends Module
{
    TimerUtility timerHelper;
    private final BooleanSetting onlyfriends;
    
    public AutoTPAccept() {
        this.timerHelper = new TimerUtility();
        this.onlyfriends = new BooleanSetting("Only Friends", false);
        this.add(this.onlyfriends);
    }
    
    @EventTarget
    public void onPacket(final EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof SPacketChat) {
            final SPacketChat message = (SPacketChat)eventPacket.getPacket();
            final String m = message.getChatComponent().getUnformattedText();
            final StringBuilder builder = new StringBuilder();
            final char[] buffer = m.toCharArray();
            for (int i = 0; i < buffer.length; ++i) {
                if (buffer[i] == '\ufffd') {
                    ++i;
                }
                else {
                    builder.append(buffer[i]);
                }
            }
            if (builder.toString().contains("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd")) {
                if (this.onlyfriends.get()) {
                    for (final Friend friends : Minced.getInstance().friendManager.getFriends()) {
                        if (builder.toString().contains(friends.getName())) {
                            if (!this.timerHelper.hasTimeElapsed(500L)) {
                                continue;
                            }
                            final Minecraft mc = AutoTPAccept.mc;
                            Minecraft.player.sendChatMessage("/tpaccept");
                            this.timerHelper.reset();
                        }
                    }
                }
                else if (this.timerHelper.hasTimeElapsed(300L)) {
                    final Minecraft mc2 = AutoTPAccept.mc;
                    Minecraft.player.sendChatMessage("/tpaccept");
                    this.timerHelper.reset();
                }
            }
        }
    }
}
