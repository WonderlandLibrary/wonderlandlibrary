// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import exhibition.event.RegisterEvent;
import exhibition.util.misc.ChatUtil;
import net.minecraft.network.play.server.S02PacketChat;
import exhibition.event.impl.EventPacket;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.module.Module;

public class KopyKatt extends Module
{
    public static EntityPlayer target;
    
    public KopyKatt(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        final EventPacket ep = (EventPacket)event;
        if (ep.isIncoming() && ep.getPacket() instanceof S02PacketChat && KopyKatt.target != null) {
            final S02PacketChat pc = (S02PacketChat)ep.getPacket();
            final String message = pc.func_148915_c().getUnformattedText().toString();
            if (message.contains(KopyKatt.target.getDisplayName().getUnformattedText().toString()) && message.contains(">")) {
                final String[] yourMessage = message.split(">");
                String msg = yourMessage[1];
                msg = msg.replace(KopyKatt.mc.thePlayer.getName(), KopyKatt.target.getName());
                msg = msg.replace("Im ", "You're ");
                msg = msg.replace("I'm ", "You're ");
                msg = msg.replace("im ", "You're ");
                msg = msg.replace("i'm ", "You're ");
                msg = msg.replace("I ", "You ");
                msg = msg.replace("i ", "You ");
                msg = msg.replace("hate", "love");
                final String curseCheck = msg.toLowerCase();
                if (curseCheck.contains("nigger") || curseCheck.contains("fucker") || curseCheck.contains("dyke") || curseCheck.contains("faggot") || curseCheck.contains("asshole") || curseCheck.contains("fgt") || curseCheck.contains("kys") || curseCheck.contains("kill yourself") || curseCheck.contains("jew") || curseCheck.contains("cunt") || curseCheck.contains("chink") || curseCheck.contains("fuck") || curseCheck.contains("gay") || curseCheck.contains("fag") || curseCheck.contains("dick")) {
                    msg = "Hey! " + KopyKatt.target.getName() + ", that's not nice.";
                }
                ChatUtil.sendChat(msg);
            }
        }
    }
}
