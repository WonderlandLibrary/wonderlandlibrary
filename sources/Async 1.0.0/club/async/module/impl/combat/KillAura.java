package club.async.module.impl.combat;

import club.async.Async;
import club.async.event.impl.*;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.AuraUtil;
import club.async.util.RotationUtil;
import club.async.util.WorldUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import rip.hippo.lwjeb.annotation.Handler;

import java.util.Objects;

@ModuleInfo(name = "KillAura", description = "Attack Entity's around you", category = Category.COMBAT)
public class KillAura extends Module {

    /*
    My old KillAura from my old Client, please don't roast me for the Code :)
    */

    private AuraUtil auraUtil = new AuraUtil();
    public EntityLivingBase target;
    public float[] rotations = new float[1], prevRotations = new float[1];
    public boolean isBlocking;

    public ModeSetting mode = new ModeSetting("Mode", this, new String[]{"Single", "Switch"}, "Single");
    public ModeSetting attackOn = new ModeSetting("Attack on", this, new String[]{"Pre", "Post"}, "Pre");
    public ModeSetting rotateOn = new ModeSetting("Rotate on", this, new String[]{"Pre", "Tick"}, "Pre");
    public ModeSetting attackMode = new ModeSetting("Attack mode", this, new String[]{"Normal", "Mouse Click"}, "Normal");
    public ModeSetting priority = new ModeSetting("Priority", this, new String[]{"Health", "Distance"}, "Health");
    public NumberSetting delay = new NumberSetting("Switch delay", this, 10, 1000, 400, 10, () -> mode.getCurrMode().equalsIgnoreCase("Switch"));
    public NumberSetting range = new NumberSetting("Range", this, 3, 7, 4, 0.1);
    public NumberSetting maxAPS = new NumberSetting("Max APS", this, 2, 20, 12, 1);
    public NumberSetting minAPS = new NumberSetting("Min APS", this, 1, 19, 11, 1);
    public NumberSetting particleDelay = new NumberSetting("Particle delay", this, 10, 200, 100, 10);
    public ModeSetting autoBlock = new ModeSetting("Auto Block", this, new String[] {"None", "Fake", "NCP", "Verus", "AAC"});
    public BooleanSetting rotate = new BooleanSetting("Rotate", this, true);
    public BooleanSetting randomRotations = new BooleanSetting("Randomize Rotations", this, false, rotate::get);
    public BooleanSetting sensiFix = new BooleanSetting("GCD fix", this, true, rotate::get);
    public BooleanSetting moveFix = new BooleanSetting("Move fix", this, false, rotate::get);
    public BooleanSetting jumpFix = new BooleanSetting("Jump fix", this, false, rotate::get);
    public BooleanSetting rayCast = new BooleanSetting("RayCast", this, false, () -> attackMode.getCurrMode().equalsIgnoreCase("Normal") && rotate.get());
    public BooleanSetting bestVec = new BooleanSetting("Best VEC", this, false, rotate::get);
    public BooleanSetting predictRotations = new BooleanSetting("Predict rotations", this, false, rotate::get);
    public BooleanSetting keepRotations = new BooleanSetting("Keep rotations", this, false, rotate::get);
    public BooleanSetting deathCheck = new BooleanSetting("Death check", this, true);
    public BooleanSetting wallCheck = new BooleanSetting("Wall check", this, false);
    public BooleanSetting scaffoldCheck = new BooleanSetting("Scaffold check", this, false);
    public BooleanSetting guiCheck = new BooleanSetting("Gui check", this, false);
    public BooleanSetting keepSprint = new BooleanSetting("Keep Sprinting", this, false);

    @Handler
    public void preUpdate(EventPreUpdate e) {
        setExtraTag(mode.getCurrMode());

        auraUtil.setTarget();

        setExtraTag(priority.getCurrMode());

        if (target == null)
            return;

        // Getting Rotations to target, when mode is on Pre
        if (rotateOn.getCurrMode().equalsIgnoreCase("Pre") && ((!keepRotations.get() || (WorldUtil.raycast(range.getDouble(), rotations) == null || WorldUtil.raycast(range.getDouble(), rotations) != target)))) {
            prevRotations = rotations;
            rotations = sensiFix.get() ? RotationUtil.fixSensi(Objects.requireNonNull(RotationUtil.getRotations(target, bestVec.get(), predictRotations.get()))) : RotationUtil.getRotations(target, bestVec.get(), predictRotations.get());
        }

        // Setting Rotarions
        if (rotate.get()) {
            e.setYaw(rotations[0]);
            e.setPitch(rotations[1]);
            mc.thePlayer.rotationYawHead = rotations[0];
            mc.thePlayer.renderYawOffset = rotations[0];
        }

        // Ray cast check
        EntityLivingBase rayCastedEntity = WorldUtil.raycast(range.getDouble(), rotations);
        if (rayCast.get() && (rayCastedEntity == null || rayCastedEntity != target))
            return;

        // Attacking, Blocking and Unblocking
        if (autoBlock.is("NCP") && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
            auraUtil.unblock();
        if (attackOn.getCurrMode().equalsIgnoreCase("Pre"))
            auraUtil.attack(target);
    }

    @Handler
    public void postUpdate(EventPostUpdate e) {

        if(target != null && autoBlock.is("verus") && isBlocking) {
            mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 8100);
        }
        if(target != null && (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && autoBlock.is("verus") && !isBlocking) {
            auraUtil.block();
            isBlocking = true;
        }
        if(target == null && (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && autoBlock.is("verus") && isBlocking) {
            auraUtil.unblock();
            isBlocking = false;
        }

        if(autoBlock.is("aac")) {
            if(isBlocking) {
                mc.gameSettings.keyBindUseItem.pressed = false;
                isBlocking = false;
            } else {
                if(target != null){
                    mc.gameSettings.keyBindUseItem.pressed = true;
                    isBlocking = true;
                }
            }
        }

        if (target == null)
            return;

        // Ray cast check
        EntityLivingBase rayCastedEntity = WorldUtil.raycast(range.getDouble(), rotations);
        if (rayCast.get() && (rayCastedEntity == null || rayCastedEntity != target))
            return;

        // Attacking and Blocking the target when Attack mode is Post
        if (attackOn.getCurrMode().equalsIgnoreCase("Post"))
            auraUtil.attack(target);
        if (autoBlock.is("NCP") && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
            auraUtil.block();
    }

    @Handler
    public void packet(EventPacket e) {
        if(e.getPacket() instanceof C09PacketHeldItemChange) isBlocking = false;
    }

    @Handler
    public void update(EventUpdate e) {
        if (mc.thePlayer.ticksExisted == 1 || !mc.thePlayer.isEntityAlive())
            toggle();

        if (target == null) {
            rotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
            return;
        }

        // Getting Rotations to target, when mode is on Pre
        if (rotateOn.getCurrMode().equalsIgnoreCase("Tick") && ((!keepRotations.get() || (WorldUtil.raycast(range.getDouble(), rotations) == null || WorldUtil.raycast(range.getDouble(), rotations) != target)))) {
            prevRotations = rotations;
            rotations = sensiFix.get() ? RotationUtil.fixSensi(Objects.requireNonNull(RotationUtil.getRotations(target, bestVec.get(), predictRotations.get()))) : RotationUtil.getRotations(target, bestVec.get(), predictRotations.get());
        }

        // Rendering the particles
        auraUtil.renderParticles();
    }

    @Handler
    public final void moveFlying(EventMoveFlying eventMoveFlying) {
        if (target != null && rotations != null && moveFix.get())
            eventMoveFlying.setYaw(rotations[0]);
    }

    @Handler
    public void look(EventLook e) {
        // Setting the hovered block or Entity (Important to prevent flags and make ClickMouse Attack Methode working)
        if (target == null || rotations == null)
            return;
        e.setRotations(rotations);
        e.setPrevRotations(rotations);
    }

    @Handler
    public void renderPitch(EventRenderPitch e) {
        if (target != null)
            e.setPitch(rotations[1]);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        auraUtil.targetIndex = 0;
        rotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static KillAura getInstance() {
        return Async.INSTANCE.getModuleManager().moduleBy(KillAura.class);
    }

}
