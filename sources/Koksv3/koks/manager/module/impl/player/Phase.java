package koks.manager.module.impl.player;

import god.buddy.aot.BCompiler;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:54
 */

@ModuleInfo(name = "Phase", description = "You can walk through walls", category = Module.Category.PLAYER)
public class Phase extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Hive", "Intave", "AAC1.9.10"}, "Hive", this);

    public void aac1910() {
        getPlayer().setPosition(getX(), getY() - 2, getZ());

        boolean ground = false;
        for(int i = 0; i < 6; i++) {
            ground = !ground;
            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() - 2, getZ(), ground));
        }
    }

    public void hive() {
        if (getPlayer().isCollidedHorizontally) {
            double motionX = -(Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.7);
            double motionZ = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.7;
            mc.thePlayer.setPosition(mc.thePlayer.posX + motionX, mc.thePlayer.posY, mc.thePlayer.posZ + motionZ);
            getPlayer().setSprinting(true);
            mc.thePlayer.motionY = 0;
            mc.thePlayer.onGround = true;
        }
    }

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    public void intave() {
        if (getPlayer().isCollidedHorizontally) {
            if (isMoving()) {
                if (timeHelper.hasReached(150)) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX - Math.sin(Math.toRadians(getDirection())) * 0.1, mc.thePlayer.posY, getPlayer().posZ + Math.cos(Math.toRadians(getDirection())) * 0.1);
                    timeHelper.reset();
                }
            }
        }
    }

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Hive":
                    hive();
                    break;
                case "Intave":
                    intave();
                    break;
                case "AAC1.9.10":
                    aac1910();
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
