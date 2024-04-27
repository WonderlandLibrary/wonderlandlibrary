package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.StepConfirmEvent;
import net.ccbluex.liquidbounce.event.StepEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "Stair", description = ":/", category = ModuleCategory.MOVEMENT)
public class Stair extends Module {
    private final ListValue ModeValue = new ListValue("Mode", new String[] {"Hypixel","New"}, "Hypixel");
    private final IntegerValue delay = new IntegerValue("Delay", 200, 0, 1000);
    public final BoolValue steplag = new BoolValue("Timer", true);
    private final MSTimer delayTimer = new MSTimer();
    boolean resetTimer;

    @Override
    public String getTag() {
        return ModeValue.get();
    }

    public ListValue getModeValue() {
        return ModeValue;
    }

    @Override
    public void onEnable() {
        if(ModeValue.get().equalsIgnoreCase("Hypixel")){
            resetTimer = false;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if(ModeValue.get().equalsIgnoreCase("Hypixel")){
            if (mc.thePlayer != null) {
                mc.thePlayer.stepHeight = 0.5F;
            }
            mc.timer.timerSpeed = 1.0F;
        }
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(ModeValue.get().equalsIgnoreCase("Hypixel")){
            if(mc.timer.timerSpeed < 1 && mc.thePlayer.onGround) {
                mc.timer.timerSpeed = 1;
            }
        }
    }

    @EventTarget
    public void onStep(StepEvent event) {
        if(ModeValue.get().equalsIgnoreCase("Hypixel")){
            if (!MovementUtils.isInLiquid()) {
                if(resetTimer){
                    resetTimer = !resetTimer;
                    mc.timer.timerSpeed = 1;
                }
                if (!mc.thePlayer.onGround || !delayTimer.isDelayComplete(delay.get().longValue())) {
                    mc.thePlayer.stepHeight = 0.5F;
                    event.setStepHeight(0.5F);
                    return;
                }
                mc.thePlayer.stepHeight = 1.0F;
                event.setStepHeight(1.0F);
            }
        }
        if(ModeValue.get().equalsIgnoreCase("New")){
            if(!mc.thePlayer.isInWater()) {
                if(mc.thePlayer.isCollidedVertically && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    event.setStepHeight(2f);
                    return;
                }
            }

        }
    }
    @EventTarget(ignoreCondition = true)
    public void onStepConfirm(StepConfirmEvent event) {
        if(ModeValue.get().equalsIgnoreCase("Hypixel")){
            if (!MovementUtils.isInLiquid()) {
                if (resetTimer) {
                    resetTimer = !resetTimer;
                    mc.timer.timerSpeed = 1;
                }
                if (event.getStepHeight() > 0.5) {
                    double height = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
                    if (height >= 0.625) {
                        if (steplag.get()) {
                            mc.timer.timerSpeed = 0.6f - (height >= 1 ? Math.abs(1 - (float) height) * (0.6f * 0.55f) : 0);
                            if (mc.timer.timerSpeed <= 0.05f) {
                                mc.timer.timerSpeed = 0.05f;
                            }
                        }
                        ncpStep(height);
                        delayTimer.reset();
                    }
                }
            }
        }
        if(ModeValue.get().equalsIgnoreCase("New")){
            if(!mc.thePlayer.isInWater()) {
                double rheight = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
                boolean canStep = rheight >= 0.625D;
                if(canStep) {
                    this.ncpStep(rheight);

                    mc.timer.timerSpeed = 0.4F;
                    new Thread(new Runnable()
                    {
                        public void run() {
                            try {
                                Thread.sleep(100L);
                            }
                            catch (InterruptedException localInterruptedException) {}
                            mc.timer.timerSpeed = 1.0F;}}).start();
                }
            }
        }
    }

    void ncpStep(double height) {
        List<Double> offset = Arrays.asList(0.42, 0.333, 0.248, 0.083, -0.078);
        double posX = mc.thePlayer.posX;
        double posZ = mc.thePlayer.posZ;
        double y = mc.thePlayer.posY;
        if (height < 1.1) {
            double first = 0.42;
            double second = 0.75;
            if (height != 1) {
                first *= height;
                second *= height;
                if (first > 0.425) {
                    first = 0.425;
                }
                if (second > 0.78) {
                    second = 0.78;
                }
                if (second < 0.49) {
                    second = 0.49;
                }
            }
            if (first == 0.42)
                first = 0.41999998688698;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
            if (y + second < y + height)
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
            return;
        } else if (height < 1.6) {
            for (int i = 0; i < offset.size(); i++) {
                double off = offset.get(i);
                y += off;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
            }
        } else if (height < 2.1) {
            double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
            for (double off : heights) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        } else {
            double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            for (double off : heights) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        }

    }
}
