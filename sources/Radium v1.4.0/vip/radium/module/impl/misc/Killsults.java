package vip.radium.module.impl.misc;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S02PacketChat;
import org.apache.commons.lang3.RandomUtils;
import vip.radium.RadiumClient;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.utils.Wrapper;

import java.util.Arrays;

@ModuleInfo(label = "Killsults", category = ModuleCategory.MISCELLANEOUS)
public class Killsults extends Module {

    private final String[] deathMessages = {"killed by", "void by", "slain by", "void while escaping", "was killed with magic while fighting",
            "couldn't fly while escaping", "fell to their death while escaping"};

    private final String[] insults = {"Rolled by $$$ Manuel Cortes $$$", "mfw running sigma and my cpu goes 100%",
            "imagine your client getting cracked by chinks", "SYSTEMS OVERLOADING",
            "mfw on novoline getting watchdogged for standing still",
            "selling astolfo invite in exchange for thighhigh pics OWO", "absolutely demolished by ZaneTutorialsHD",
            "sigma users explaining why they get 30fps on a 2080ti rig", "dortware with the fly bypass that got patched in 2 hours",
            "auth is a degenerate loser who codes dortware all day", "nice hvh bro", "rolled",
            "dort is an absolute imbecille", "astolfo client, turning kids into queers since 2019", "ill stomp your brains in",
            "mees is a pedophile groomer, do not trust remix client",
            "hello? i run dortware... why my computer turn off and say the n word", "hello? i run novoline on hypixel, why ban?",
            "watchdog is SO GOOD <3", "nice client LOL",
            "exhi go crazy with the modules 10000 pixels off of the gui", "imagine having your client autoban",
            "me and the boys on novoline silent flagging", "dortware devs coping hard when their clients cracked", "allahware on top baby",
            "dort weight 300lbs", "mfw astolfo down for 3 days bcuz cant skid new disabler", "destroyed by spec da savage $$", "mfw astolfo step watchdogs"};

    @EventLink
    public final Listener<PacketReceiveEvent> PacketReceiveEvent = event -> {
        if (Wrapper.getPlayer() == null || !(event.getPacket() instanceof S02PacketChat))
            return;

        S02PacketChat packetChat = (S02PacketChat) event.getPacket();
        String chatComponent = packetChat.getChatComponent().getUnformattedText();
        Entity target = RadiumClient.getInstance().getModuleManager().getModule(KillAura.class).getTarget();

        Arrays.stream(deathMessages).filter(deathMessage -> chatComponent.contains(deathMessage + " " + Wrapper.getMinecraft().session.getUsername())).forEach(deathMessage -> Wrapper.getPlayer().sendChatMessage((target != null ? target.getCommandSenderName() + " " : "") + insults[RandomUtils.nextInt(0, insults.length)]));
        if (chatComponent.contains("KILL!"))
            Wrapper.getPlayer().sendChatMessage((target != null ? target.getCommandSenderName() + " " : "") + insults[RandomUtils.nextInt(0, insults.length)]);
    };
}