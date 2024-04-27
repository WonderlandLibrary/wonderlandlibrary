package me.aquavit.liquidsense.module.modules.blatant;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.AttackEvent;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.utils.entity.MovementUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.movement.Fly;
import me.aquavit.liquidsense.module.modules.movement.Speed;
import me.aquavit.liquidsense.value.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Criticals", description = "automatically deals critical hits.", category = ModuleCategory.BLATANT)
public class Criticals extends Module {
    public final ListValue mode = new ListValue("Mode", new String[]{"Packet", "Motion", "NoGround"}, "Packet");

    private final Value<String> packetType = new ListValue("PacketType", new String[]{"C04", "C06"}, "C04").displayable(()-> mode.get().equals("Packet"));
    private final Value<String> bypassMode = new ListValue("BypassMode", new String[]{"Hypixel", "AAC", "Infinity"}, "Hypixel").displayable(()-> mode.get().equals("Packet"));
    private final Value<String> motionMode = new ListValue("MotionMode", new String[]{"Jump", "LowHop", "Hop", "TPHop"}, "TPHop").displayable(()-> mode.get().equals("Motion"));
    private final Value<Float> f1 = new FloatValue("Float-1", 0.0625f, 0f, 0.125f).displayable(()-> bypassMode.get().equals("Infinity"));
    private final Value<Float> f2 = new FloatValue("Float-2", 0.001f, 0f, 0.125f).displayable(()-> bypassMode.get().equals("Infinity"));
    private final Value<Float> f3 = new FloatValue("Float-3", 0.0313f, 0f, 0.125f).displayable(()-> bypassMode.get().equals("Infinity"));
    private final Value<Float> f4 = new FloatValue("Float-4", 0.001f, 0f, 0.125f).displayable(()-> bypassMode.get().equals("Infinity"));

    private final Value<Integer> delay = new IntegerValue("Delay", 250, 0, 500).displayable(()-> !mode.get().equals("NoGround"));
    private final Value<Integer> hurtTime = new IntegerValue("Hurttime", 10, 0, 10).displayable(()-> !mode.get().equals("NoGround"));

    private final Value<Boolean> onlyPlayer = new BoolValue("OnlyPlayer", true).displayable(()-> !mode.get().equals("Motion"));
    private final Value<Boolean> onSpeed = new BoolValue("OnSpeed", false).displayable(()-> !mode.get().equals("Motion") && !mode.get().equals("NoGround"));
    private final Value<Boolean> onFly = new BoolValue("OnFly", false).displayable(()-> !mode.get().equals("Motion") && !mode.get().equals("NoGround"));

    private final Value<Boolean> hurtTag = new BoolValue("HurtTag", false).displayable(()-> !mode.get().equals("NoGround"));

    private final MSTimer msTimer = new MSTimer();

    @Override
    public void onEnable() {
        if (mode.get().equals("NoGround"))
            mc.thePlayer.jump();
    }

    @EventTarget
    public void onAttack(AttackEvent e) {
        if (mc.thePlayer == null)
            return;

        if (e.getTargetEntity() instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase) e.getTargetEntity();
            final Speed speed = (Speed) LiquidSense.moduleManager.getModule(Speed.class);
            final Fly fly = (Fly) LiquidSense.moduleManager.getModule(Fly.class);

            if (!mc.thePlayer.onGround || mc.thePlayer.isOnLadder() || mc.thePlayer.ridingEntity != null
                    || mc.thePlayer.isInWeb || mc.thePlayer.isInWater() || mc.thePlayer.isInLava()
                    || entity.hurtTime > hurtTime.get() || !msTimer.hasTimePassed((long) delay.get())
                    || (!onSpeed.get() && speed.getState() && speed.stopTicks == 0 && MovementUtils.isMoving())
                    || (!onFly.get() && fly.getState() && (mode.get().equals("Packet") || mode.get().equals("Infinity")))
                    || (onlyPlayer.get() && !(entity instanceof EntityPlayer))
            ) return;

            switch (mode.get().toLowerCase()) {
                case "packet":
                    double[] offsets;
                    switch (bypassMode.get().toLowerCase()) {
                        case "hypixel":
                            offsets = new double[]{0.1010000017285347, 0.06100000075995922, 0.048000000026077032};
                            break;
                        case "aac":
                            offsets = new double[]{0.11921599284565, 0.00163166800276, 0.15919999545217, 0.11999999731779};
                            break;
                        default:
                            offsets = new double[]{f1.get(), f2.get(), f3.get(), f4.get()};
                    }

                    for (final double offset : offsets) {
                        sendPacket(offset);
                    }
                    break;
                case "motion":
                    switch (motionMode.get().toLowerCase()) {
                        case "jump":
                            mc.thePlayer.motionY = MovementUtils.getJumpBoostModifier(0.41999998688698, true);
                            break;
                        case "lowhop":
                            mc.thePlayer.motionY = 0.3425;
                            break;
                        case "hop":
                            mc.thePlayer.motionY = 0.1;
                            mc.thePlayer.fallDistance = 0.1f;
                            mc.thePlayer.onGround = false;
                            break;
                        case "tphop":
                            sendPacket(0.02);
                            sendPacket(0.01);
                            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ);
                            break;
                    }
                    break;
            }

            mc.thePlayer.onCriticalHit(entity);
            msTimer.reset();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent e) {
        final Packet<?> packet = e.getPacket();
        final Speed speed = (Speed) LiquidSense.moduleManager.getModule(Speed.class);
        final Fly fly = (Fly) LiquidSense.moduleManager.getModule(Fly.class);
        final Aura aura = (Aura) LiquidSense.moduleManager.getModule(Aura.class);

        if (packet instanceof C03PacketPlayer && mode.get().equals("NoGround")) {
            if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && !speed.getState() && !fly.getState()) {
                if ((aura.getState() && aura.getTarget() != null) || mc.thePlayer.fallDistance == 0f) {
                    ((C03PacketPlayer) packet).onGround = false;
                }
            }
        }
    }

    private void sendPacket(double yOffset) {
        final double posY = mc.thePlayer.posY + yOffset;

        if (packetType.get().equals("C04")) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
        } else {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, false));
        }
    }

    @Override
    public String getTag() {
        return mode.get().equals("NoGround") ? "NoGround"
                : (mode.get().equals("Packet") && bypassMode.get().equals("Infinity") ?
                "Infinity" : mode.get()) + (hurtTag.get() ? " " + hurtTime.get() : "");
    }
}
