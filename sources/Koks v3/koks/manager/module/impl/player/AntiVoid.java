package koks.manager.module.impl.player;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

/**
 * @author kroko
 * @created on 15.11.2020 : 22:06
 */

@ModuleInfo(name = "AntiVoid", description = "You doesn't fall in to the void", category = Module.Category.PLAYER)
public class AntiVoid extends Module {

    public Setting fallDistance = new Setting("Fall Distance", 3, 3, 6, false, this);
    public Setting mode = new Setting("Mode", new String[]{"AAC1.9.10", "Hypixel"}, "AAC1.9.10", this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {

            boolean isVoid = true;
            for(int i = (int) getY(); i > 0; i--) {
                if(getWorld().getBlockState(new BlockPos(getX(), i, getZ())).getBlock() != Blocks.air)
                    isVoid = false;
            }

            switch (mode.getCurrentMode()) {
                case "Hypixel":
                    if (getPlayer().fallDistance > fallDistance.getCurrentValue() && !getPlayer().onGround && isVoid) {
                        sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 4, getZ(), false));
                    }
                    break;
                case "AAC1.9.10":
                    if (getPlayer().fallDistance > (fallDistance.getCurrentValue() > 4.5 ? 4.5 : fallDistance.getCurrentValue()) && !getPlayer().onGround && isVoid) {
                        sendPacket(new C03PacketPlayer(true));
                        if (getHurtTime() != 0) {
                            getPlayer().fallDistance = 0;
                            getPlayer().motionY = 1.5;
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
