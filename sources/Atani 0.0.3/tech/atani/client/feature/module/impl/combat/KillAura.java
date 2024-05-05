package tech.atani.client.feature.module.impl.combat;

import cn.muyang.nativeobfuscator.Native;
import com.google.common.base.Supplier;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.listener.event.minecraft.game.PostTickEvent;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.minecraft.input.ClickingEvent;
import tech.atani.client.listener.event.minecraft.player.rotation.RayTraceRangeEvent;
import tech.atani.client.listener.event.minecraft.player.rotation.RotationEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.player.PlayerHandler;
import tech.atani.client.utility.player.combat.FightUtil;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.utility.player.rotation.RotationUtil;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;

@Native
@ModuleData(name = "KillAura", description = "Attacks people", category = Category.COMBAT, key = Keyboard.KEY_R)
public class KillAura extends Module {

    public SliderValue<Float> findRange = new SliderValue<>("Search Range", "What'll be the range for searching for targets?", this, 4f, 3f, 10f, 1);
    public StringBoxValue targetMode = new StringBoxValue("Target Mode", "How will the aura search for targets?", this, new String[]{"Single", "Hybrid", "Switch", "Multi"});
    public StringBoxValue priority = new StringBoxValue("Priority", "How will the aura sort targets?", this, new String[]{"Health", "Distance"});
    public SliderValue<Long> switchDelay = new SliderValue<Long>("Switch Delay", "How long will it take to switch between targets?", this, 300L, 0L, 1000L, 0, new Supplier[]{() -> targetMode.is("Switch")});
    public CheckBoxValue players = new CheckBoxValue("Players", "Attack Players?", this, true);
    public CheckBoxValue animals = new CheckBoxValue("Animals", "Attack Animals?", this, true);
    public CheckBoxValue monsters = new CheckBoxValue("Monsters", "Attack Monsters?", this, true);
    public CheckBoxValue invisible = new CheckBoxValue("Invisibles", "Attack Invisibles?", this, true);
    public CheckBoxValue walls = new CheckBoxValue("Walls", "Check for walls?", this, true);
    public CheckBoxValue autoBlock = new CheckBoxValue("Auto Block", "Should the aura block on hit?", this, true);
    public StringBoxValue autoBlockMode = new StringBoxValue("Auto Block Mode", "Which mode should the autoblock use?", this, new String[] {"Vanilla", "NCP", "AAC", "GrimAC", "Intave"}, new Supplier[]{() -> autoBlock.getValue()});
    public SliderValue<Integer> fov = new SliderValue<>("FOV", "What'll the be fov for allowing targets?", this, 90, 0, 180, 0);
    public SliderValue<Float> attackRange = new SliderValue<>("Attack Range", "What'll be the range for Attacking?", this, 3f, 3f, 6f, 1);
    public CheckBoxValue fixServersSideMisplace = new CheckBoxValue("Fix Misplace", "Fix Server-Side Misplace?", this, true);
    public CheckBoxValue waitBeforeAttack = new CheckBoxValue("Delay Clicks", "Wait before attacking the target?", this, true);
    public StringBoxValue waitMode = new StringBoxValue("Wait for", "For what will the module wait before attacking?", this, new String[]{"CPS", "1.9"}, new Supplier[]{() -> waitBeforeAttack.getValue()});
    public SliderValue<Float> cps = new SliderValue<Float>("CPS", "How much will the killaura click every second?", this, 12f, 0f, 20f, 1, new Supplier[]{() -> waitBeforeAttack.getValue() && waitMode.is("CPS")});
    public CheckBoxValue randomizeCps = new CheckBoxValue("Randomize CPS", "Randomize CPS Value to bypass anticheats?", this, true, new Supplier[]{() -> waitBeforeAttack.getValue() && waitMode.is("CPS")});
    public CheckBoxValue lockView = new CheckBoxValue("Lock-view", "Rotate non-silently?", this, false);
    public CheckBoxValue snapYaw = new CheckBoxValue("Snap Yaw", "Skip smoothing out yaw rotations?", this, false);
    public CheckBoxValue snapPitch = new CheckBoxValue("Snap Pitch", "Skip smoothing out pitch rotations?", this, false);
    public StringBoxValue aimVector = new StringBoxValue("Aim Vector", "Where to aim?", this, new String[]{"Perfect", "Bruteforce", "Head", "Torso", "Feet", "Custom", "Random"});
    public SliderValue<Float> heightDivisor = new SliderValue<Float>("Height Divisor", "By what amount to divide the height?", this, 2f, 1f, 10f, 1, new Supplier[]{() -> aimVector.getValue().equalsIgnoreCase("Custom")});
    public CheckBoxValue skipUnnecessaryRotations = new CheckBoxValue("Necessary Rotations", "Rotate only if necessary?", this, false);
    public StringBoxValue unnecessaryRotations = new StringBoxValue("Necessary Mode", "What rotations to skip?", this, new String[]{"Both", "Yaw", "Pitch"}, new Supplier[]{() -> skipUnnecessaryRotations.getValue()});
    public CheckBoxValue skipIfNear = new CheckBoxValue("Skip If Near", "Rotate only if far enough?", this, true, new Supplier[]{() -> skipUnnecessaryRotations.getValue()});
    public SliderValue<Float> nearDistance = new SliderValue<Float>("Near Distance", "How near to skip rotations?", this, 0.5F, 0F, 0.5F, 2, new Supplier[]{() -> skipUnnecessaryRotations.getValue() && skipIfNear.getValue()});
    public SliderValue<Float> minYaw = new SliderValue<>("Minimum Yaw", "What will be the minimum yaw for rotating?", this, 40f, 0f, 180f, 0);
    public SliderValue<Float> maxYaw = new SliderValue<>("Maximum Yaw", "What will be the maximum yaw for rotating?", this, 40f, 0f, 180f, 0);
    public SliderValue<Float> minPitch = new SliderValue<>("Minimum Pitch", "What will be the minimum pitch for rotating?", this, 40f, 0f, 180f, 0);
    public SliderValue<Float> maxPitch = new SliderValue<>("Maximum Pitch", "What will be the maximum pitch for rotating?", this, 40f, 0f, 180f, 0);
    public CheckBoxValue mouseFix = new CheckBoxValue("Mouse Fix", "Simulate mouse movements in rotations?", this, true);
    public CheckBoxValue heuristics = new CheckBoxValue("Heuristics", "Bypass heuristics checks?", this, true);
    public SliderValue<Float> minRandomYaw = new SliderValue<>("Min Random Yaw", "What'll be the minimum randomization for yaw?", this, 0f, -1f, 0f, 2);
    public SliderValue<Float> maxRandomYaw = new SliderValue<>("Max Random Yaw", "What'll be the maximum randomization for yaw?", this, 0f, 0f, 1f, 2);
    public SliderValue<Float> minRandomPitch = new SliderValue<>("Min Random Pitch", "What'll be the minimum randomization for pitch?", this, 0f, -1f, 0f, 2);
    public SliderValue<Float> maxRandomPitch = new SliderValue<>("Max Random Pitch", "What'll be the maximum randomization for pitch?", this, 0f, 0f, 1f, 2);
    public CheckBoxValue prediction = new CheckBoxValue("Prediction", "Predict the players position?", this, false);

    // Targets
    public static EntityLivingBase curEntity;
    private int currentIndex;
    private TimeHelper switchTimer = new TimeHelper();

    // Range
    private double correctedRange = 0D;

    // Clicking
    private TimeHelper attackTimer = new TimeHelper();
    private double cpsDelay = 0;

    // Rotations
    private float yawRot, pitchRot;

    @Override
    public String getSuffix() {
    	return targetMode.getValue();
    }
    
    private final class AttackRangeSorter implements Comparator<EntityLivingBase> {
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            int first = FightUtil.getRange(o1) <= attackRange.getValue() ? 0 : 1;
            int second = FightUtil.getRange(o2) <= attackRange.getValue() ? 0 : 1;
            return Double.compare(first, second);
        }
    }

    private final class HealthSorter implements Comparator<EntityLivingBase> {
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            return Double.compare(FightUtil.getEffectiveHealth(o1), FightUtil.getEffectiveHealth(o2));
        }
    }

    private final class DistanceSorter implements Comparator<EntityLivingBase> {
        public int compare(EntityLivingBase o1, EntityLivingBase o2) {
            return Double.compare(Methods.mc.thePlayer.getDistanceToEntity(o1), Methods.mc.thePlayer.getDistanceToEntity(o2));
        }
    }

    @Listen
    public void onRayTrace(RayTraceRangeEvent rayTraceRangeEvent) {
        if(curEntity != null) {
            correctedRange = this.attackRange.getValue() + 0.00256f;
            if (this.fixServersSideMisplace.getValue()) {
                final float n = 0.010625f;
                if (Methods.mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH || Methods.mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST) {
                    correctedRange += n * 2.0f;
                }
            }
            rayTraceRangeEvent.setRange((float) correctedRange);
            rayTraceRangeEvent.setBlockReachDistance((float) Math.max(Methods.mc.playerController.getBlockReachDistance(), correctedRange));
        }
    }

    @Listen
    public void onPostTickEvent(PostTickEvent postTickEvent) {
        if (mc.thePlayer == null || mc.thePlayer.ticksExisted % 5 != 0)
            return;

        List<EntityLivingBase> targets = FightUtil.getMultipleTargets(findRange.getValue(), players.getValue(), animals.getValue(), walls.getValue(), monsters.getValue(), invisible.getValue());
        switch (this.priority.getValue()) {
            case "Distance":
                targets.sort(new DistanceSorter());
                break;
            case "Health":
                targets.sort(new HealthSorter());
                break;
        }
        targets.sort(new AttackRangeSorter());
        if (targets.isEmpty() || (curEntity != null && !targets.contains(curEntity))) {
            cpsDelay = 0L;
            curEntity = null;
            return;
        }
        switch (targetMode.getValue()) {
            case "Hybrid":
                curEntity = targets.get(0);
                break;
            case "Single":
                if (curEntity == null || !FightUtil.isValid(curEntity, findRange.getValue(), players.getValue(), animals.getValue(), monsters.getValue(), invisible.getValue()))
                    curEntity = targets.get(0);
                break;
            case "Multi":
            case "Switch":
                long switchDelay = targetMode.is("Multi") ? 0 : this.switchDelay.getValue();
                if (!this.switchTimer.hasReached(switchDelay)) {
                    if (curEntity == null || !FightUtil.isValid(curEntity, findRange.getValue(), players.getValue(), animals.getValue(), monsters.getValue(), invisible.getValue()))
                        curEntity = targets.get(0);
                    return;
                }
                if (curEntity != null && FightUtil.isValid(curEntity, findRange.getValue(), players.getValue(), animals.getValue(), monsters.getValue(), invisible.getValue()) && targets.size() == 1) {
                    return;
                } else if (curEntity == null) {
                    curEntity = targets.get(0);
                } else if (targets.size() > 1) {
                    int maxIndex = targets.size() - 1;
                    if (this.currentIndex >= maxIndex) {
                        this.currentIndex = 0;
                    } else {
                        this.currentIndex += 1;
                    }
                    if (targets.get(currentIndex) != null && targets.get(currentIndex) != curEntity) {
                        curEntity = targets.get(currentIndex);
                        this.switchTimer.reset();
                    }
                } else {
                    curEntity = null;
                }
                break;
        }
    }

    @Listen
    public final void onRotation(RotationEvent rotationEvent) {
        if (curEntity != null) {
            final float[] rotations = RotationUtil.getRotation(curEntity, aimVector.getValue(), heightDivisor.getValue(), mouseFix.getValue(), heuristics.getValue(), minRandomYaw.getValue(), maxRandomYaw.getValue(), minRandomPitch.getValue(), maxRandomPitch.getValue(), this.prediction.getValue(), this.minYaw.getValue(), this.maxYaw.getValue(), this.minPitch.getValue(), this.maxPitch.getValue(), snapYaw.getValue(), snapPitch.getValue());

            neccessaryRots: {
                if(skipUnnecessaryRotations.getValue()) {
                    MovingObjectPosition movingObjectPosition = mc.objectMouseOver;
                    if(movingObjectPosition == null) {
                        yawRot = rotations[0];
                        pitchRot = rotations[1];
                        break neccessaryRots;
                    }
                    boolean shouldSkip = (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY || movingObjectPosition.entityHit == curEntity) || (skipIfNear.getValue() && FightUtil.getRange(curEntity) <= nearDistance.getValue());
                    if(shouldSkip) {
                        if(unnecessaryRotations.getValue().equalsIgnoreCase("Yaw") || unnecessaryRotations.getValue().equalsIgnoreCase("Both"))
                            yawRot = PlayerHandler.yaw;
                        if(unnecessaryRotations.getValue().equalsIgnoreCase("Pitch") || unnecessaryRotations.getValue().equalsIgnoreCase("Both"))
                            pitchRot = PlayerHandler.pitch;
                    } else {
                        yawRot = rotations[0];
                        pitchRot = rotations[1];
                    }
                } else {
                    yawRot = rotations[0];
                    pitchRot = rotations[1];
                }
            }

            rotationEvent.setYaw(yawRot);
            rotationEvent.setPitch(pitchRot);

            if(this.lockView.getValue()) {
                Methods.mc.thePlayer.rotationYaw = yawRot;
                Methods.mc.thePlayer.rotationPitch = pitchRot;
            }
        }
    }

    @Listen
    public final void onClick(ClickingEvent clickingEvent) {
        if(curEntity != null) {
            // We need to calculate 1.9 wait BEFORE attempting to attack to make sure we hit the cooldown correctly
            switch (this.waitMode.getValue()) {
                case "1.9":
                    cpsDelay = getAttackSpeed(Methods.mc.thePlayer.getHeldItem(), true);
                    break;
            }

            if(!this.waitBeforeAttack.getValue() || this.attackTimer.hasReached(cpsDelay)) {
                // We need to calculate cps delay after checking if the timer has reached, since the delay would be first set to 0, therefore we hit earlier
                switch (this.waitMode.getValue()) {
                    case "CPS":
                        final double cps = this.cps.getValue() > 10 ? this.cps.getValue() + 5 : this.cps.getValue();
                        long calcCPS = (long) (1000 / cps);
                        if(this.randomizeCps.getValue()) {
                            try {
                                calcCPS += SecureRandom.getInstanceStrong().nextGaussian() * 50;
                            } catch (NoSuchAlgorithmException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        cpsDelay = calcCPS;
                        break;
                }
                MovingObjectPosition objectPosition = Methods.mc.objectMouseOver;
                if (objectPosition != null && objectPosition.entityHit != null && objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                    if (autoBlock.getValue()) {
                        ItemStack currentItem = Methods.mc.thePlayer.getHeldItem();
                        if (currentItem != null && currentItem.getItem() instanceof ItemSword) {
                            switch (autoBlockMode.getValue()) {
                                case "Vanilla":
                                    Methods.mc.playerController.sendUseItem(Methods.mc.thePlayer, Methods.mc.theWorld, currentItem);
                                    break;
                                case "NCP":
                                    Methods.mc.thePlayer.setItemInUse(currentItem, 32767);
                                    break;
                                case "AAC":
                                    if (Methods.mc.thePlayer.ticksExisted % 2 == 0) {
                                        Methods.mc.playerController.interactWithEntitySendPacket(Methods.mc.thePlayer, objectPosition.entityHit);
                                        Methods.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(currentItem));
                                    }
                                    break;
                                case "GrimAC":
                                case "Intave":
                                    Methods.mc.playerController.interactWithEntitySendPacket(Methods.mc.thePlayer, objectPosition.entityHit);
                                    Methods.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(currentItem));
                                    break;
                            }
                        }
                    }

                    Methods.mc.thePlayer.swingItem();
                    Methods.mc.playerController.attackEntity(Methods.mc.thePlayer, objectPosition.entityHit);
                }
                this.attackTimer.reset();
            }
        } else {
            cpsDelay = 0L;
        }
    }

    public static long getAttackSpeed(final ItemStack itemStack, final boolean responsive) {
        double baseSpeed = 250;
        if (!responsive) {
            return Long.MAX_VALUE;
        } else if (itemStack != null) {
            if (itemStack.getItem() instanceof ItemSword) {
                baseSpeed = 625;
            }
            if (itemStack.getItem() instanceof ItemSpade) {
                baseSpeed = 1000;
            }
            if (itemStack.getItem() instanceof ItemPickaxe) {
                baseSpeed = 833.333333333333333;
            }
            if (itemStack.getItem() instanceof ItemAxe) {
                if (itemStack.getItem() == Items.wooden_axe) {
                    baseSpeed = 1250;
                }
                if (itemStack.getItem() == Items.stone_axe) {
                    baseSpeed = 1250;
                }
                if (itemStack.getItem() == Items.iron_axe) {
                    baseSpeed = 1111.111111111111111;
                }
                if (itemStack.getItem() == Items.diamond_axe) {
                    baseSpeed = 1000;
                }
                if (itemStack.getItem() == Items.golden_axe) {
                    baseSpeed = 1000;
                }
            }
            if (itemStack.getItem() instanceof ItemHoe) {
                if (itemStack.getItem() == Items.wooden_hoe) {
                    baseSpeed = 1000;
                }
                if (itemStack.getItem() == Items.stone_hoe) {
                    baseSpeed = 500;
                }
                if (itemStack.getItem() == Items.iron_hoe) {
                    baseSpeed = 333.333333333333333;
                }
                if (itemStack.getItem() == Items.diamond_hoe) {
                    baseSpeed = 250;
                }
                if (itemStack.getItem() == Items.golden_hoe) {
                    baseSpeed = 1000;
                }
            }
        }
        if (Methods.mc.thePlayer.isPotionActive(Potion.digSpeed)) {
            baseSpeed *= 1.0 + 0.1 * (Methods.mc.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1);
        }
        return Math.round(baseSpeed);
    }

    @Override
    public void onEnable() { curEntity = null; }

    @Override
    public void onDisable() {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }

        if(autoBlock.getValue() && autoBlockMode.is("GrimAC")) {
            Methods.mc.gameSettings.keyBindUseItem.pressed = false;
        }
        curEntity = null;
    }

}
