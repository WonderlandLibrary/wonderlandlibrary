package me.dev.legacy.features.modules.combat;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.text.TextComponentString;

public class AutoLog extends Module {
    public AutoLog() {
        super("AutoLog", "Automatically logs on combat.", Module.Category.COMBAT, true, false, false);
    }

    public final Setting<Boolean> settingPacketKick = this.register(new Setting<Boolean>("Packet Kick", false));
    public final Setting<Integer> settingHealth = this.register(new Setting<Integer>("Health",6, 1, 20));

    public void onTick() {
        if (mc.player == null || mc.world == null || mc.player.capabilities.isCreativeMode) {
            return;
        }

        float health = mc.player.getHealth();

        if (health <= settingHealth.getValue().intValue() && health != 0f && !mc.player.isDead) {
            this.doLog();
            this.toggle();
        }
    }

    public void doLog() {
        if (settingPacketKick.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 50, mc.player.posZ, false));
        }

        mc.player.connection.getNetworkManager().closeChannel(new TextComponentString("Auto Log!"));
    }
}
