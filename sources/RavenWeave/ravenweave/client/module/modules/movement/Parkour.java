package ravenweave.client.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.utils.Utils;

public class Parkour extends Module {

    private boolean jumping = false;

    public Parkour() {
        super("Parkour", ModuleCategory.movement);
        this.registerSetting(new DescriptionSetting("Jumps at the edge of a block."));
    }

    @Override
    public void onDisable() {
        this.jump(this.jumping = false);
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (Utils.Player.isPlayerInGame()) {
            if (mc.thePlayer.onGround && !mc.thePlayer.isSneaking()) {
                if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX / 3.0D, -1.0D, mc.thePlayer.motionZ / 3.0D)).isEmpty()) {
                    this.jump(this.jumping = true);
                } else if (this.jumping) {
                    this.jump(this.jumping = false);
                }
            } else if (this.jumping) {
                this.jump(this.jumping = false);
            }

        }
    }

    private void jump(boolean jump) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), jump);
    }
}
