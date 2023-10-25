package club.cloverhook.cheat.impl.visual;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.ProcessPacketEvent;
import club.cloverhook.utils.Stopwatch;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S02PacketChat;

public class KillSults extends Cheat {
    int counter;

    Stopwatch timer;

    public KillSults() {
        super("KillSults", "Insult those sigma skids when you kill them!!111!1!", CheatCategory.VISUAL);
        timer = new Stopwatch();
    }

    @Override
    public void onEnable() {
        counter = 0;
    }

    @Collect
    public void onPacket(ProcessPacketEvent e) {
        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) e.getPacket();
            String msg = packet.getChatComponent().getUnformattedText();
            if (msg.contains("by ") && msg.contains(getPlayer().getName() + ".")) {
                EntityLivingBase ent;
                for (Object o : mc.theWorld.getLoadedEntityList()) {
                    if (o instanceof EntityLivingBase && o != mc.thePlayer) {
                        ent = (EntityLivingBase) o;
                        if (msg.contains(ent.getName())) {
                            String[] messages = new String[]{
                                    ent.getName() + " ur so bad  | Buy " + Cloverhook.client_name,
                                    ent.getName() + " lmao are u using sigma ur so bad commit ygore  | Buy " + Cloverhook.client_name,
                                    ent.getName() + " rip for you [n]i[g]g[e]r  | Buy " + Cloverhook.client_name,
                                    "you know hows the worst playing the world? " + ent.getName() + "  | Buy " + Cloverhook.client_name,
                                    ent.getName() + " should jump off a [f]u[c]king bridge xd | Buy " + Cloverhook.client_name,
                                    ent.getName() + " is rarted | Buy " + Cloverhook.client_name};
                            if (counter >= messages.length)
                                counter = 0;
                            mc.thePlayer.sendChatMessage(messages[counter] + ".");
                            counter += 1;
                        }
                    }
                }
            }
        }
    }
}
