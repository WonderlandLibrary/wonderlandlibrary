package me.aquavit.liquidsense.module.modules.misc;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.movement.Step;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.movement.Fly;
import me.aquavit.liquidsense.module.modules.movement.LongJump;
import me.aquavit.liquidsense.module.modules.movement.Speed;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.ChatComponentText;

@ModuleInfo(name = "LagBack", description = "LagBack", category = ModuleCategory.MISC)
public class LagBack extends Module {

    public final BoolValue speedlag = new BoolValue("Speed", true);
    public final BoolValue longjumplag = new BoolValue("LongJump", true);
    public final BoolValue flylag = new BoolValue("Fly", true);
    public final BoolValue steplag = new BoolValue("Step", true);
    private TimeUtils deactivationDelay = new TimeUtils();

    @EventTarget
    public void onPacket(PacketEvent event) {
        Speed speed = (Speed) LiquidSense.moduleManager.getModule(Speed.class);
        LongJump longjump = (LongJump) LiquidSense.moduleManager.getModule(LongJump.class);
        Fly fly = (Fly) LiquidSense.moduleManager.getModule(Fly.class);
        Step step = (Step) LiquidSense.moduleManager.getModule(Step.class);

        if (event.getPacket() instanceof S08PacketPlayerPosLook && this.deactivationDelay.hasReached(2000L)) {
            if (speedlag.get()) {
                if (speed.getState()) {
                    if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()){
                        LiquidSense.moduleManager.getModule(Speed.class).setState(false);
                        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" + LiquidSense.CLIENT_NAME+ "§8] §3"+ "SpeedLagBack"));
                    }
                }
            }
            if (longjumplag.get()) {
                if (longjump.getState() && mc.thePlayer.motionX == 0 && mc.thePlayer.motionZ == 0) {
                    if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()){
                        LiquidSense.moduleManager.getModule(LongJump.class).setState(false);
                        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" + LiquidSense.CLIENT_NAME+ "§8] §3"+ "LongJumpLagBack"));
                    }
                }
            }
            if(flylag.get()){
                if(fly.getState()) {
                    if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                        LiquidSense.moduleManager.getModule(Fly.class).setState(false);
                        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" + LiquidSense.CLIENT_NAME+ "§8] §3"+ "FlyLagBack"));
                    }
                }
            }
            if (steplag.get()) {
                if (step.getState() && mc.thePlayer.motionX == 0 && mc.thePlayer.motionZ == 0) {
                    if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()){
                        LiquidSense.moduleManager.getModule(Step.class).setState(false);
                        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" + LiquidSense.CLIENT_NAME+ "§8] §3"+ "StepLagBack"));
                    }
                }
            }
        }

    }


}
