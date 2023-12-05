package koks.manager.module.impl.player;

import god.buddy.aot.BCompiler;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Random;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:52
 */

@ModuleInfo(name = "SetBack", description = "Your respawn at the same position", category = Module.Category.PLAYER)
public class SetBack extends Module {

    public double deathX, deathY, deathZ;

    public Setting mode = new Setting("Mode", new String[]{"Intave", "AAC3.0.1"},"Intave", this);

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    public void aac301() {
        Random rnd = new Random();
        if (mc.thePlayer.hurtResistantTime != 0 && mc.thePlayer.getHealth() == 0) {
            for (int i = 0; i < 30; i++) {
                mc.thePlayer.setPosition(mc.thePlayer.posX + i, mc.thePlayer.posY + 40, mc.thePlayer.posZ + i);
            }
            mc.thePlayer.respawnPlayer();
        }
        if (mc.thePlayer.getHealth() <= 6F) {
            deathX = mc.thePlayer.posX;
            deathY = mc.thePlayer.posY;
            deathZ = mc.thePlayer.posZ;
            mc.thePlayer.setPosition(mc.thePlayer.posX + rnd.nextDouble() * 1.2, mc.thePlayer.posY - 0.15, mc.thePlayer.posZ + rnd.nextDouble() * 1.2);
        }

    }

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    public void intave() {
        if (!mc.thePlayer.isInWater()) {
            if (mc.thePlayer.getHealth() < 3F) {
                for (int i = 0; i < 30; i++) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY, mc.thePlayer.posZ, true));
                }
            }
            if (mc.currentScreen instanceof GuiGameOver) {
                for (int i = 0; i < 30; i++) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY, mc.thePlayer.posZ, true));

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
                case "Intave":
                    intave();
                    break;
                case "AAC3.0.1":
                    aac301();
                    break;
            }
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        deathX = 0;
        deathY = 0;
        deathZ = 0;
    }
}