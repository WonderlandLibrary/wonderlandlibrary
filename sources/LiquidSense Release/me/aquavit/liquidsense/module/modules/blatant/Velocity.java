package me.aquavit.liquidsense.module.modules.blatant;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.JumpEvent;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.movement.Speed;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.ListValue;
import me.aquavit.liquidsense.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "Velocity", description = "Allows you to modify the amount of knockback you take.", category = ModuleCategory.BLATANT)
public class Velocity extends Module {

    private final Value<Float> horizontalValue = new FloatValue("Horizontal", 0.0f, 0.0f, 1.0f).displayable(() ->
            modeValue.get().equalsIgnoreCase("Simple") || modeValue.get().equalsIgnoreCase("OldAAC"));
    private final Value<Float> verticalValue = new FloatValue("Vertical", 0.0f, 0.0f, 1.0f).displayable(() ->
            modeValue.get().equalsIgnoreCase("Simple") || modeValue.get().equalsIgnoreCase("OldAAC"));
    private static final ListValue modeValue = new ListValue("Mode", new String[]{
            "Simple", "OldAAC", "AACPush", "AAC4", "AAC5", "AAC5Reduce", "Jump", "Reverse"}, "Simple");
    private final Value<Float> reverseStrengthValue = new FloatValue("ReverseStrength", 0.05f, 0.02f, 0.1f).displayable(() ->
            modeValue.get().equalsIgnoreCase("Reverse"));
    private final Value<Float> aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2.0f, 1.0f, 3.0f).displayable(() ->
            modeValue.get().equalsIgnoreCase("AACPush"));
    private final Value<Boolean> aacPushYReducerValue = new BoolValue("AACPushYReducer", true).displayable(() ->
            modeValue.get().equalsIgnoreCase("AACPush"));
    private MSTimer velocityTimer = new MSTimer();
    private boolean velocityInput;
    private boolean hurt;
    private boolean jump;

    @Override
    public String getTag() {
        return modeValue.get();
    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isInWeb)
            return;

        switch (modeValue.get().toLowerCase()) {
            case "jump":{
                if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.42;

                    float yaw = mc.thePlayer.rotationYaw * 0.017453292F;
                    mc.thePlayer.motionX -= MathHelper.sin(yaw) * 0.2;
                    mc.thePlayer.motionZ += MathHelper.cos(yaw) * 0.2;
                }
                break;
            }
            case "reverse":{
                if (!velocityInput) {
                    mc.thePlayer.speedInAir = 0.02F;
                    return;
                }

                if (mc.thePlayer.hurtTime > 0)
                    hurt = true;

                if (!mc.thePlayer.onGround) {
                    if (hurt)
                        mc.thePlayer.speedInAir = reverseStrengthValue.get();
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false;
                    hurt = false;
                }
                break;
            }
            case "aac5":
            case "aac4":{
                if (!mc.thePlayer.onGround) {
                    if (velocityInput) {
                        mc.gameSettings.keyBindForward.isKeyDown();
                        mc.thePlayer.speedInAir = 0.02f;
                        mc.thePlayer.motionX *= 0.6;
                        mc.thePlayer.motionZ *= 0.6;
                    }
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false;
                    mc.thePlayer.speedInAir = 0.02f;
                }
                break;
            }
            case "aac5reduce":{
                if (mc.thePlayer.hurtTime> 1 && velocityInput) {
                    mc.thePlayer.motionX *= 0.81;
                    mc.thePlayer.motionZ *= 0.81;
                }
                if (velocityInput && (mc.thePlayer.hurtTime <5 || mc.thePlayer.onGround) && velocityTimer.hasTimePassed(120L)) {
                    velocityInput = false;
                }
                break;
            }
            case "aacpush":{
                if (jump) {
                    if (mc.thePlayer.onGround)
                        jump = false;
                } else {
                    if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.motionX != 0.0 && mc.thePlayer.motionZ != 0.0)
                        mc.thePlayer.onGround = true;

                    if (mc.thePlayer.hurtResistantTime > 0 && aacPushYReducerValue.get()
                            && !LiquidSense.moduleManager.getModule(Speed.class).getState())
                    mc.thePlayer.motionY -= 0.014999993;
                }

                if (mc.thePlayer.hurtResistantTime >= 19) {
                    float reduce = aacPushXZReducerValue.get();

                    mc.thePlayer.motionX /= reduce;
                    mc.thePlayer.motionZ /= reduce;
                }
                break;
            }
            case "oldaac": {
                if (velocityInput && velocityTimer.hasTimePassed(80L)) {
                    mc.thePlayer.motionX *= horizontalValue.get();
                    mc.thePlayer.motionZ *= verticalValue.get();
                    velocityInput = false;
                }
                break;
            }

        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();

        if (packet instanceof S12PacketEntityVelocity && mc.thePlayer != null && mc.theWorld != null) {
            final S12PacketEntityVelocity packetEntityVelocity = (S12PacketEntityVelocity) packet;
            if (mc.theWorld.getEntityByID(packetEntityVelocity.getEntityID()) == mc.thePlayer) {

                velocityTimer.reset();

                switch (modeValue.get().toLowerCase()){
                    case "simple":{
                        float horizontal = horizontalValue.get();
                        float vertical = verticalValue.get();

                        if (horizontal == 0F && vertical == 0F)
                            event.cancelEvent();

                        ((S12PacketEntityVelocity) packet).motionX = (int) (((S12PacketEntityVelocity) packet).getMotionX() * horizontal);
                        ((S12PacketEntityVelocity) packet).motionY = (int) (((S12PacketEntityVelocity) packet).getMotionY() * vertical);
                        ((S12PacketEntityVelocity) packet).motionZ = (int) (((S12PacketEntityVelocity) packet).getMotionZ() * horizontal);
                        break;
                    }
                    case "oldaac":
                    case "reverse":
                    case "aac4":
                    case "aac5":
                    case "aac5reduce":
                        velocityInput = true;
                        break;
                }


            }
        }

        if (packet instanceof S27PacketExplosion) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        if (mc.thePlayer == null || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isInWeb) {
            return;
        }

        switch (modeValue.get().toLowerCase()) {
            case "aacpush":{
                jump = true;

                if (!mc.thePlayer.isCollidedVertically) {
                    event.cancelEvent();
                }
                break;
            }
        }
    }

}
