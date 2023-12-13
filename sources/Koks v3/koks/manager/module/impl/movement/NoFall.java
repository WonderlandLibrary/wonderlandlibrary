package koks.manager.module.impl.movement;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 18:13
 */

@ModuleInfo(name = "NoFall", description = "Prevents you from getting fall damage", category = Module.Category.MOVEMENT)
public class NoFall extends Module {
    
    public Setting mode = new Setting("Mode", new String[]{"Mineplex", "Intave", "AAC4"}, "Mineplex", this);

    public void mineplex() {
        if (getPlayer().fallDistance > 2) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            getPlayer().fallDistance = 0;
        }
    }

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    public void intave() {
        if (getPlayer().fallDistance > 2F) {
            getPlayer().onGround = true;
            getPlayer().capabilities.isFlying = true;
        }
    }

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    public void aac4() {
        if (!getPlayer().onGround && getPlayer().fallDistance > 1.2 && getPlayer().ticksExisted % 3 == 2) {
            getPlayer().motionY = 0;
            getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(true));
            getPlayer().fallDistance = 1;
        }
    }

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
        }
        switch (mode.getCurrentMode()) {
            case "Mineplex":
                if (event instanceof EventUpdate) {
                    mineplex();
                }
                break;
            case "AAC4":
                if (event instanceof EventUpdate) {
                    aac4();
                }
            case "Intave":
                if (event instanceof EventUpdate) {
                    intave();
                }
                break;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}