package me.AquaVit.liquidSense.modules.misc;

import me.AquaVit.liquidSense.modules.movement.Flight;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.Step;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
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
        Speed speed = (Speed) LiquidBounce.moduleManager.getModule(Speed.class);
        LongJump longjump = (LongJump) LiquidBounce.moduleManager.getModule(LongJump.class);
        Fly fly = (Fly) LiquidBounce.moduleManager.getModule(Fly.class);
        Flight flight = (Flight) LiquidBounce.moduleManager.getModule(Flight.class);
        Step step = (Step) LiquidBounce.moduleManager.getModule(Step.class);

        if (event.getPacket() instanceof S08PacketPlayerPosLook && this.deactivationDelay.delay(2000F)) {
            if (speedlag.get()) {
                if (speed.getState()) {
                    if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()){
                        LiquidBounce.moduleManager.getModule(Speed.class).setState(false);
                        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" +LiquidBounce.CLIENT_NAME+ "§8] §3"+ "SpeedLagBack"));
                    }
                }
            }
            if (longjumplag.get()) {
                if (longjump.getState() && mc.thePlayer.motionX == 0 && mc.thePlayer.motionZ == 0) {
                    if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()){
                        LiquidBounce.moduleManager.getModule(LongJump.class).setState(false);
                        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" +LiquidBounce.CLIENT_NAME+ "§8] §3"+ "LongJumpLagBack"));
                    }
                }
            }
            if(flylag.get()){
                if(fly.getState() || flight.getState()) {
                    if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                        LiquidBounce.moduleManager.getModule(Fly.class).setState(false);
                        LiquidBounce.moduleManager.getModule(Flight.class).setState(false);
                        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" +LiquidBounce.CLIENT_NAME+ "§8] §3"+ "FlyLagBack"));
                    }
                }
            }
            if (steplag.get()) {
                if (step.getState() && mc.thePlayer.motionX == 0 && mc.thePlayer.motionZ == 0) {
                    if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()){
                        LiquidBounce.moduleManager.getModule(Step.class).setState(false);
                        mc.thePlayer.addChatMessage(new ChatComponentText("§8[§9§l" +LiquidBounce.CLIENT_NAME+ "§8] §3"+ "StepLagBack"));
                    }
                }
            }
        }

    }


}
