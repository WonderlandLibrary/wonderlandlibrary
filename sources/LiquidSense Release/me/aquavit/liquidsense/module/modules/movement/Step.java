package me.aquavit.liquidsense.module.modules.movement;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.StepConfirmEvent;
import me.aquavit.liquidsense.event.events.StepEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.module.modules.exploit.Phase;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ModuleInfo(name = "Step", description = ":/", category = ModuleCategory.MOVEMENT)
public class Step extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[] {"Vanilla", "NCP", "MotionNCP"}, "NCP");
    private final FloatValue heightValue = new FloatValue("Height", 1.5F, 0.6F, 10F);
    private final IntegerValue delay = new IntegerValue("Delay", 0, 0, 500);

    private final MSTimer delayTimer = new MSTimer();
    public boolean isStep = false;
    private double posX = 0.0, posY = 0.0, posZ = 0.0;

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.stepHeight = 0.625F;
        }

        super.onDisable();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (!isStep && posY == new BigDecimal(posY).setScale(3, RoundingMode.HALF_DOWN).doubleValue())
            mc.timer.timerSpeed = 1.0f;
    }

    @EventTarget
    public void onStep(StepEvent event) {
        final Phase phase = (Phase) LiquidSense.moduleManager.getModule(Phase.class);
        final Fly fly = (Fly) LiquidSense.moduleManager.getModule(Fly.class);
        if (phase.getState() || (fly.getState() && (fly.modeValue.get().contains("Hypixel") || fly.modeValue.get().contains("Zoom")))) {
            event.setStepHeight(0F);
            return;
        }

        if (!mc.thePlayer.onGround || !delayTimer.hasTimePassed((long) delay.get())) {
            mc.thePlayer.stepHeight = 0.625F;
            event.setStepHeight(0.625F);
            return;
        }

        mc.thePlayer.stepHeight = heightValue.get();
        event.setStepHeight(heightValue.get());

        if (event.getStepHeight() > 0.625F) {
            isStep = true;
            posX = mc.thePlayer.posX;
            posY = mc.thePlayer.posY;
            posZ = mc.thePlayer.posZ;
        }
    }

    @EventTarget(ignoreCondition = true)
    public void onStepConfirm(StepConfirmEvent event) {
        final double height = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;

        if (!isStep) {
            return;
        }

        if (height > 0.625) {
            if (modeValue.get().equals("NCP")) {
                MovementUtils.fakeJump();
                // Half legit step (1 packet missing) [COULD TRIGGER TOO MANY PACKETS]
                mc.timer.timerSpeed = Math.max(0.5F - (((float) height >= 1) ? Math.abs(0.6F - (float) height) * 0.25F : 0.0F), 0.1F);
                ncpOffsets(height);
                delayTimer.reset();
            }
        }

        isStep = false;
        posX = 0.0;
        posY = 0.0;
        posZ = 0.0;
    }

    private void ncpOffsets(double height) {
        if (height <= 1.0) {
            double first = 0.41999998688698;
            double second = 0.7531999805212;

            if (height != 1.0) {
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

            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + first, posZ, false));

            if (posY + second < posY + height)
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + second, posZ, false));

        } else if (height <= 1.5) {
            double[] offsets = {0.42, 0.753, 1.001, 1.084, 1.006};
            for (double offset : offsets) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + offset, posZ, false));
            }
        } else if (height <= 2.0) {
            double[] offsets = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
            for (double offset : offsets) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + offset, posZ, false));
            }
        } else {
            double[] offsets = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            for (double offset : offsets) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + offset, posZ, false));
            }
        }
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
