package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ACommand;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.ChatUtil;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;

public class BanCommand extends ACommand {

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "Automatically bans you on a server (if it has an anti-cheat)";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void handleCommand(String[] args) {
        for (int i = 0; i < 40; i++) {
            MovementUtil.spoof(0, false);
            MovementUtil.spoof(0, true);
            MovementUtil.spoof(-5, true);
            MovementUtil.spoof(0, false);
            MovementUtil.spoof(0, true);
            final PlayerCapabilities capabilities = new PlayerCapabilities();
            capabilities.isFlying = capabilities.isCreativeMode = capabilities.allowFlying = capabilities.allowEdit = true;
            capabilities.setFlySpeed(MathUtil.getRandom_float(0.1f, 9000.0f));
            capabilities.setPlayerWalkSpeed(MathUtil.getRandom_float(0.1f, 9000.0f));
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C13PacketPlayerAbilities(capabilities));
            mc.getNetHandler().addToSendQueueNoEvent(new C13PacketPlayerAbilities(capabilities));
            mc.getNetHandler().addToSendQueueNoEvent(new C0CPacketInput(25,25,true,true));
            mc.getNetHandler().addToSendQueueNoEvent(new C09PacketHeldItemChange(2));
            mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY + 1,mc.thePlayer.posZ,false));
        }
        ChatUtil.sendChat("You will be banned within the next minute.");
    }
}