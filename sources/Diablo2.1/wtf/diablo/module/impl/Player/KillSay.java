package wtf.diablo.module.impl.Player;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import wtf.diablo.Diablo;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.module.impl.Combat.KillAura;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.math.MathUtil;
import wtf.diablo.utils.packet.PacketUtil;

import java.util.Iterator;

@Getter
@Setter
public class KillSay extends Module {
    public KillSay() {
        super("KillSay", "Insult faggots", Category.PLAYER, ServerType.All);
    }

    @Subscribe
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof S38PacketPlayerListItem) {
            S38PacketPlayerListItem packet = (S38PacketPlayerListItem) e.getPacket();
            Iterator playerData = packet.players.iterator();
            String[] messages = {
                    "There is a reason why you are losing"
                    , "Investing in Diablo is ur best choice"
                    , "Even my dog is better at the game then you"
                    , "I can't imagine being that bad"
                    , "Did u forget to use your mouse?"
                    , "get good get diablo @ intent"
                    , "vince is quite literary the most hot man on earth"
            };

            if (playerData.hasNext()) {
                S38PacketPlayerListItem.AddPlayerData entityListed = (S38PacketPlayerListItem.AddPlayerData) playerData.next();
                EntityPlayer target = mc.theWorld.getPlayerEntityByUUID(entityListed.getProfile().getId());

                if (packet.action == S38PacketPlayerListItem.Action.REMOVE_PLAYER && KillAura.totalTargets.contains(target)) {
                    ChatUtil.log("sent");
                    String finalMessage = messages[MathUtil.getRandInt(0,messages.length)];
                    Diablo.hypixelStatus.killEvent(1);
                    PacketUtil.sendPacket(new C01PacketChatMessage(finalMessage + " " + target.getName()));
                }

            }
        }
    }
}
