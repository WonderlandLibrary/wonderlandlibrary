package me.aquavit.liquidsense.module.modules.blatant;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventState;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.*;
import me.aquavit.liquidsense.module.modules.client.Target;
import me.aquavit.liquidsense.module.modules.misc.Teams;
import me.aquavit.liquidsense.module.modules.player.Blink;
import me.aquavit.liquidsense.module.modules.render.FreeCam;
import me.aquavit.liquidsense.utils.client.Rotation;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.client.VecRotation;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.entity.RaycastUtils;
import me.aquavit.liquidsense.utils.extensions.PlayerExtensionUtils;
import me.aquavit.liquidsense.utils.misc.RandomUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.misc.AntiBot;
import me.aquavit.liquidsense.value.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import org.lwjgl.input.Keyboard;

import java.util.*;

@ModuleInfo(name = "Aura", description = "Automatically attacks targets around you.", category = ModuleCategory.BLATANT, keyBind = Keyboard.KEY_R)
public class Aura extends Module {

    // 点击速度
    private IntegerValue maxCPS = new IntegerValue("MaxCPS", 10, 1, 20) {
        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = minCPS.get();
            if (i > newValue)
                set(i);

            attackDelay = TimeUtils.randomClickDelay(minCPS.get(), this.get());
        }
    };

    private IntegerValue minCPS = new IntegerValue("MinCPS", 8, 1, 20) {
        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = maxCPS.get();
            if (i < newValue)
                set(i);

            attackDelay = TimeUtils.randomClickDelay(this.get(), maxCPS.get());
        }
    };
    // 伤害时间
    private IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    // NCP更多设置
    private Value<Float> hitBoxValue = new FloatValue("HitBox", 0.4f, 0.0f, 0.4f).displayable(() -> getModeValue().equals("NCP"));
    // 距离
    private FloatValue rangeValue = new FloatValue("Range", 4.6f, 1f, 6f);
    private FloatValue blockRangeValue = new FloatValue("BlockRange", 8.0f, 1f, 12f);
    private FloatValue throughWallsRangeValue = new FloatValue("ThroughWallsRange", 4.0f, 0f, 6f);
    // 锁敌模式
    private ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction", "LivingTime", "Armor", "HurtTime", "HurtResistantTime"}, "Distance");
    private ListValue targetModeValue = new ListValue("TargetMode", new String[]{"Single", "Switch", "Multi"}, "Switch");
    // Switch更多设置
    private Value<Integer> switchDelayValue = new IntegerValue("SwitchDelay", 250, 0, 2000).displayable(() -> targetModeValue.get().equals("Switch"));
    // Multi更多设置
    private Value<Integer> limitedMultiTargetsValue = new IntegerValue("LimitedMultiTargets", 0, 0, 50).displayable(() -> targetModeValue.get().equals("Multi"));
    // 摇头速度
    private IntegerValue maxTurnSpeed = new IntegerValue("MaxTurnSpeed", 180, 0, 180) {
        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = minTurnSpeed.get();
            if (i > newValue)
                set(i);
        }
    };
    private IntegerValue minTurnSpeed = new IntegerValue("MinTurnSpeed", 180, 0, 180) {
        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int i = maxTurnSpeed.get();
            if (i < newValue) {
                set(i);
            }
        }
    };
    // 摇头
    private ListValue rotationMode = new ListValue("RotationMode", new String[]{"Vanilla", "RayCast", "BackTrack", "None"}, "Vanilla");
    private BoolValue silentRotationValue = new BoolValue("SilentRotation", true);
    private Value<Boolean> faceHitableValue = new BoolValue("FaceHitable", false).displayable(() -> !rotationMode.get().equals("None") || !getRaycastValue());
    // AAC摇头设置
    private Value<String> rotationStrafeValue = new ListValue("Strafe", new String[]{"Off", "Strict", "Silent"}, "Off").displayable(() -> getModeValue().equals("AAC"));
    private Value<Boolean> randomCenterValue = new BoolValue("RandomCenter", true).displayable(() -> getModeValue().equals("AAC"));
    private Value<Boolean> outborderValue = new BoolValue("Outborder", false).displayable(() -> getModeValue().equals("AAC"));
    private Value<Float> fovValue = new FloatValue("FOV", 180f, 0f, 180f).displayable(() -> getModeValue().equals("AAC"));
    // 格挡
    private ListValue AutoBlockValue = new ListValue("AutoBlockMode", new String[]{"Off", "Normal", "Pos", "Click", "AfterTick", "AAC"}, "Normal");
    // NCP格挡设置
    private Value<Boolean> interactAutoBlockValue = new BoolValue("InteractAutoBlock", true).displayable(() -> getModeValue().equals("NCP") && !AutoBlockValue.get().equals("Off"));
    // AAC格挡设置
    private Value<Boolean> delayedBlockValue = new BoolValue("DelayedBlock", true).displayable(() -> getModeValue().equals("AAC") && !AutoBlockValue.get().equals("Off"));
    // 格挡速度
    private Value<Integer> blockRate = new IntegerValue("BlockRate", 100, 1, 100).displayable(() -> !AutoBlockValue.get().equals("Off"));
    // 其他设置
    private BoolValue swingValue = new BoolValue("Swing", true);
    private BoolValue keepSprintValue = new BoolValue("KeepSprint", true);
    // AAC预测设置
    private Value<Boolean> predictValue = new BoolValue("Predict", true).displayable(() -> getModeValue().equals("AAC"));
    private Value<Float> maxPredictSize = new FloatValue("MaxPredictSize", 1f, 0f, 5f) {
        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            float v = minPredictSize.get();
            if (v > newValue) {
                set(v);
            }
        }
    }.displayable(() -> getModeValue().equals("AAC") && predictValue.get());
    private Value<Float> minPredictSize = new FloatValue("MinPredictSize", 1f, 0f, 5f) {
        @Override
        protected void onChanged(Float oldValue, Float newValue) {
            float v = maxPredictSize.get();
            if (v < newValue) {
                set(v);
            }
        }
    }.displayable(() -> getModeValue().equals("AAC") && predictValue.get());
    // AAC更多设置
    private Value<Boolean> raycastValue = new BoolValue("RayCast", true).displayable(() -> getModeValue().equals("AAC"));
    private Value<Boolean> raycastIgnoredValue = new BoolValue("RayCastIgnored", false).displayable(() -> getModeValue().equals("AAC") && raycastValue.get());
    private Value<Boolean> livingRaycastValue = new BoolValue("LivingRayCast", true).displayable(() -> getModeValue().equals("AAC") && raycastValue.get());
    private Value<Boolean> fakeSwingValue = new BoolValue("FakeSwing", true).displayable(() -> getModeValue().equals("AAC"));

    // 其他设置
    private FloatValue failRateValue = new FloatValue("FailRate", 0f, 0f, 100f);
    private BoolValue witherValue = new BoolValue("TargetWither", true);
    private BoolValue noInventoryAttackValue = new BoolValue("NoInvAttack", false);
    private BoolValue autoDisableValue = new BoolValue("AutoDisable", true);

    // 主绕过设置
    private ListValue modeValue = new ListValue("BypassMode", new String[]{"NCP", "AAC"}, "NCP");

    private boolean getRaycastValue(){
        return raycastValue.get();
    }

    private String getModeValue(){
        return modeValue.get();
    }

    public EntityLivingBase getTarget() {
        return target;
    }

    public boolean getBlockingStatus(){
        return blockingStatus;
    }

    public Value<Float> getHitBoxValue() {
        return hitBoxValue;
    }

    // 目标
    private EntityLivingBase target = null;
    private EntityLivingBase currentTarget = null;
    private EntityLivingBase useItemEntity = null;
    private boolean hitable = false;
    private List<Integer> prevTargetEntities = new ArrayList<>();
    private boolean flag = false;

    // 攻击延迟
    private MSTimer attackTimer = new MSTimer();
    private MSTimer switchDelay = new MSTimer();
    private long attackDelay = 0L;
    private int clicks = 0;

    // Fake block status
    private boolean blockingStatus = false;

    // 凋零
    private EntityLivingBase preBoss = null;
    private boolean boss = false;

    // AAC
    private boolean aacMode = modeValue.get().equalsIgnoreCase("AAC");
    private float turnSpeed = (float) (Math.random() * (maxTurnSpeed.get() - minTurnSpeed.get()) + minTurnSpeed.get());

    /**
     * Enable killAura module
     */
    @Override
    public void onEnable() {
        if (mc.thePlayer == null)
            return;
        if (mc.theWorld == null)
            return;

        updateTarget();
    }

    /**
     * Disable killAura module
     */
    @Override
    public void onDisable() {
        target = null;
        currentTarget = null;
        hitable = false;
        prevTargetEntities.clear();
        attackTimer.reset();
        clicks = 0;

        stopBlocking();
    }

    /**
     * Motion event
     */
    @EventTarget
    public void onMotion(MotionEvent event) {
        if (event.getEventState() == EventState.POST) {

            if (autoDisableValue.get() && (mc.thePlayer == null || mc.thePlayer.isDead || mc.thePlayer.ticksExisted <= 1))
                state = false;

            if (target == null || currentTarget == null)
                return;

            // Update hitable
            updateHitable();

            // AutoBlock
            if (useItemEntity != null) {
                // AutoBlock 防砍
                if (AutoBlockValue.get().equals("AfterTick") && canBlock())
                    startBlocking(currentTarget, hitable);

                // Start blocking after attack 攻击之后开始防砍
                if (AutoBlockValue.get().equals("Packet") && !blockingStatus && canBlock()) {
                    startBlocking(useItemEntity, hitable);
                }
            }

            if (target != null && currentTarget != null && attackTimer.hasTimePassed(TimeUtils.randomClickDelay(maxCPS.get(), minCPS.get())) && currentTarget.hurtTime <= 10) {
                clicks++;
                attackTimer.reset();
            }
        } else {
            if (!aacMode || rotationStrafeValue.get().equals("Off"))
                update();

            useItemEntity = addtarget();
        }
    }

    /**
     * Strafe event
     */
    @EventTarget
    public void onStrafe(StrafeEvent event) {
        if (!aacMode || rotationStrafeValue.get().equals("Off"))
            return;

        update();

        if (!silentRotationValue.get())
            return;

        String rotationStrafe = rotationStrafeValue.get().toLowerCase();
        if (rotationStrafe.equals("strict")) {
            if (currentTarget == null)
                return;

            Rotation rotationData = RotationUtils.targetRotation;
            if (rotationData == null)
                return;

            float yaw = rotationData.getYaw();
            float strafe = event.getStrafe();
            float forward = event.getForward();
            float friction = event.getFriction();

            float f = strafe * strafe + forward * forward;

            if (f >= 1.0E-4F) {
                f = MathHelper.sqrt_float(f);

                if (f < 1.0F)
                    f = 1.0F;

                f = friction / f;
                strafe *= f;
                forward *= f;

                float yawSin = MathHelper.sin((yaw * (float) Math.PI / 180F));
                float yawCos = MathHelper.cos((yaw * (float) Math.PI / 180F));

                mc.thePlayer.motionX += strafe * yawCos - forward * yawSin;
                mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin;
            }
            event.cancelEvent();
        } else if (rotationStrafe.equals("silent")) {
            if (currentTarget == null)
                return;

            update();

            RotationUtils.targetRotation.applyStrafeToPlayer(event);
            event.cancelEvent();
        }
    }

    /**
     * Update event
     */
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (AutoBlockValue.get().equalsIgnoreCase("AAC") && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
            return;
        if (getCancelRun()) {
            target = null;
            currentTarget = null;
            hitable = false;
            stopBlocking();
            return;
        }

        if (noInventoryAttackValue.get() && mc.currentScreen instanceof GuiContainer) {
            target = null;
            currentTarget = null;
            hitable = false;
            return;
        }

        if (target != null && currentTarget != null) {
            while (clicks > 0) {
                runAttack();
                clicks--;
            }
        }
    }

    /**
     * Attack enemy 发起进攻~
     */
    private void runAttack() {
        if (target == null || currentTarget == null)
            return;

        EntityPlayerSP thePlayer = mc.thePlayer;
        WorldClient theWorld = mc.theWorld;

        // Settings 设置
        float failRate = failRateValue.get();
        boolean swing = swingValue.get();
        boolean multi = targetModeValue.get().equalsIgnoreCase("Multi");
        boolean switchmode = targetModeValue.get().equalsIgnoreCase("Switch");
        boolean openInventory = aacMode && mc.currentScreen instanceof GuiContainer;
        boolean failHit = failRate > 0 && new Random().nextInt(100) <= failRate;

        // Close inventory when open 打开杀戮时关闭背包
        if (openInventory)
            mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow());

        // Check is not hitable or check failrate 检测是否可以攻击和空刀率
        if (!hitable || failHit) {
            if (swing && (fakeSwingValue.get() || failHit))
                mc.thePlayer.swingItem();
        } else {
            // Attack 攻击
            if (!multi) {
                attackEntity(currentTarget);
            } else {
                int targets = 0;

                for (Entity entity : theWorld.loadedEntityList) {
                    double distance = PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, entity);

                    if (entity instanceof EntityLivingBase && isEnemy((EntityLivingBase) entity) && distance <= getRange((EntityLivingBase) entity)) {
                        attackEntity((EntityLivingBase) entity);

                        targets += 1;

                        if (limitedMultiTargetsValue.get() != 0 && limitedMultiTargetsValue.get() <= targets)
                            break;
                    }
                }
            }

            if (switchmode) {
                if (switchDelay.hasTimePassed(switchDelayValue.get())) {
                    if (switchDelayValue.get() != 0) {
                        prevTargetEntities.add(aacMode ? target.getEntityId() : currentTarget.getEntityId());
                        switchDelay.reset();
                    }
                }
            /*if (target == currentTarget)
                target = null;*/
            } else {
                prevTargetEntities.add(aacMode ? target.getEntityId() : currentTarget.getEntityId());
            }
        }

        // Open inventory
        if (openInventory)
            mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
    }

    /**
     * Attack [entity] 攻击实体
     */
    private void attackEntity(EntityLivingBase entity) {
        // Stop blocking 停止防砍

        if (mc.thePlayer.isBlocking() || blockingStatus)
            stopBlocking();

        double distance = PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, entity);

        if (hitable && distance < rangeValue.get()) {
            // Call attack event 调用攻击事件
            LiquidSense.eventManager.callEvent(new AttackEvent(entity));

            // Attack target
            if (swingValue.get())
                mc.thePlayer.swingItem();
            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));

            if (keepSprintValue.get()) {
                // Critical Effect
                if (mc.thePlayer.fallDistance > 0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() &&
                        !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && !mc.thePlayer.isRiding())
                    mc.thePlayer.onCriticalHit(entity);

                // Enchant Effect
                if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), entity.getCreatureAttribute()) > 0F)
                    mc.thePlayer.onEnchantmentCritical(entity);
            } else {
                if (mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR)
                    mc.thePlayer.attackTargetEntityWithCurrentItem(entity);
            }

            // Extra critical effects
            Criticals criticals = (Criticals) LiquidSense.moduleManager.getModule(Criticals.class);

            for (int i = 0; i <= 2; i++) {
                // Critical Effect
                if (mc.thePlayer.fallDistance > 0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && !mc.thePlayer.isInWeb)
                    mc.thePlayer.onCriticalHit(target);

                // Extra critical effects 更多的暴击粒子
                if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), target.getCreatureAttribute()) > 0.0f)
                    mc.thePlayer.onEnchantmentCritical(target);
            }
        }

        // Start blocking after attack
        if (mc.thePlayer.isBlocking() || (!AutoBlockValue.get().equalsIgnoreCase("Off") && canBlock())) {
            if (!(blockRate.get() > 0 && new Random().nextInt(100) <= blockRate.get()))
                return;

            if (aacMode && delayedBlockValue.get())
                return;

            startBlocking(entity, !aacMode && interactAutoBlockValue.get());
        }
    }

    /**
     * Add target 添加目标
     */
    private EntityLivingBase addtarget() {
        // Find possible targets 寻找可能的目标
        List<EntityLivingBase> targets = new ArrayList<>();

        targets.clear();

        for (Object entity : mc.theWorld.getLoadedEntityList()) {
            if (!(entity instanceof EntityLivingBase) || !isEnemy((EntityLivingBase) entity))
                continue;

            if (mc.thePlayer.getDistanceToEntity((Entity) entity) <= blockRangeValue.get()) {
                targets.add((EntityLivingBase) entity);
                useItemEntity = (EntityLivingBase) entity;
            }
        }

        if (targets.isEmpty()) {
            return null;
        } else {
            return targets.get(0);
        }
    }

    public void update() {
        if (getCancelRun() || (noInventoryAttackValue.get() && mc.currentScreen instanceof GuiContainer))
            return;

        // Update target
        updateTarget();

        if (target == null) {
            stopBlocking();
            return;
        }

        // Target
        currentTarget = target;

        if (!targetModeValue.get().equalsIgnoreCase("Switch") && isEnemy(currentTarget))
            target = currentTarget;
    }

    /**
     * Check if enemy is hitable with current rotations 检测敌人是否是当前摇头可攻击的
     */
    private void updateHitable() {
        if (maxTurnSpeed.get() <= 0F) {
            hitable = true;
            return;
        }

        double reach = aacMode ? Math.min(maxRange(), PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, target)) + 1.4 :
                Math.min(maxRange(), PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, target) + hitBoxValue.get()) + 1;

        if (aacMode && raycastValue.get()) {
            Entity raycastedEntity = RaycastUtils.raycastEntity(reach, entity ->
                    (!livingRaycastValue.get() || (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand))) &&
                            (isEnemy(entity) || raycastIgnoredValue.get() || (aacMode && mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox()).size() > 0))
            );

            if (raycastValue.get() && raycastedEntity instanceof EntityLivingBase
                    && (LiquidSense.moduleManager.get(NoFriends.class).getState() || !EntityUtils.isFriend((EntityLivingBase) raycastedEntity))) {
                currentTarget = (EntityLivingBase) raycastedEntity;
            }

            hitable = maxTurnSpeed.get() > 0F ? currentTarget == raycastedEntity : true;
        } else {
            switch (rotationMode.get()) {
                case "Normal":
                    hitable = !faceHitableValue.get() || RotationUtils.isFaced(currentTarget, reach);
                    break;

                case "RayCast":
                    Entity raycastedEntity = RaycastUtils.raycastEntity(reach, entity ->
                            (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand)) &&
                                    (isEnemy(entity) || aacMode && mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox()).size() > 0)
                    );

                    if (raycastedEntity instanceof EntityLivingBase
                            && (LiquidSense.moduleManager.get(NoFriends.class).getState() || !EntityUtils.isFriend((EntityLivingBase) raycastedEntity))) {
                        currentTarget = (EntityLivingBase) raycastedEntity;
                    }

                    hitable = !faceHitableValue.get() || RotationUtils.isFaced(currentTarget, reach);
                    break;

                default:
                    hitable = true;
                    break;
            }
        }
    }

    /**
     * Update current target 更新当前的目标
     */
    private void updateTarget() {
        // Reset fixed target to null 将固定目标重置为空
        target = null;

        // Settings 设置
        int hurtTime = hurtTimeValue.get();
        float fov = fovValue.get();

        // Find possible targets 寻找可能的目标
        List<EntityLivingBase> targets = new ArrayList<>();

        World theWorld = mc.theWorld;
        EntityPlayerSP thePlayer = mc.thePlayer;

        for (Entity entity : theWorld.loadedEntityList) {
            if (entity instanceof EntityWither) {
                preBoss = (EntityWither) entity;
            }

            if (!(entity instanceof EntityLivingBase) || !isEnemy((EntityLivingBase) entity) ||
                    (boss ? false : (targetModeValue.get().equalsIgnoreCase("Switch") && prevTargetEntities.contains(entity.getEntityId())))) {
                continue;
            }

            double distance = PlayerExtensionUtils.getDistanceToEntityBox(thePlayer, entity);
            double entityFov = RotationUtils.getRotationDifference(entity);

            if (distance <= maxRange() && (fov == 180F || entityFov <= fov) && ((EntityLivingBase) entity).hurtTime <= hurtTime) {
                targets.add((EntityLivingBase) entity);
            }

            if (entity instanceof EntityWither) {
                if (aacMode ? distance <= maxRange() - 1 && (fov == 180F || entityFov <= fov) && ((EntityLivingBase) entity).hurtTime <= hurtTime :
                        distance <= maxRange() - 1 && ((EntityLivingBase) entity).hurtTime <= hurtTime) {
                    targets.add((EntityLivingBase) entity);
                }
            } else {
                if (aacMode ? distance <= maxRange() && (fov == 180F || entityFov <= fov) && ((EntityLivingBase) entity).hurtTime <= hurtTime :
                        distance <= maxRange() && ((EntityLivingBase) entity).hurtTime <= hurtTime) {
                    targets.add((EntityLivingBase) entity);
                } else {
                    preBoss = null;
                }
            }

            boss = witherValue.get() && targets.contains(preBoss);
        }

        // Sort targets by priority 使用优先级整理目标
        switch (priorityValue.get().toLowerCase()) {
            case "distance":
                targets.sort(Comparator.comparingDouble(entity -> PlayerExtensionUtils.getDistanceToEntityBox(thePlayer, entity)));
                break;
            case "health":
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            case "direction":
                targets.sort(Comparator.comparingDouble(RotationUtils::getRotationDifference));
                break;
            case "armor":
                targets.sort(Comparator.comparingInt(EntityLivingBase::getTotalArmorValue));
                break;
            case "hurttime":
                targets.sort(Comparator.comparingInt(entity -> entity.hurtTime));
                break;
            case "hurtresistanttime":
                targets.sort(Comparator.comparingInt(entity -> entity.hurtResistantTime));
                break;
            case "livingtime":
                targets.sort(Comparator.comparingInt(entity -> -entity.ticksExisted));
                break;
        }

        // Find best target 寻找最好的目标
        for (EntityLivingBase entity : targets) {
            double distance = PlayerExtensionUtils.getDistanceToEntityBox(thePlayer, entity);

            // Set target to current entity
            // target = entity
            if (boss && witherValue.get()) {
                if (distance <= maxRange() - 1 && preBoss != null) {
                    updateRotations((EntityWither) preBoss);
                    target = preBoss;
                    return;
                }
            } else {
                updateRotations(entity);
                target = entity;
                return;
            }
        }

        if (!prevTargetEntities.isEmpty()) {
            prevTargetEntities.clear();
            updateTarget();
        }
    }

    /**
     * Update killAura rotations to enemy 设置杀戮摇头到可攻击实体
     */
    private boolean updateRotations(Entity entity) {
        AxisAlignedBB boundingBox = entity.getEntityBoundingBox();

        if (maxTurnSpeed.get() <= 0F)
            return true;

        if (aacMode && predictValue.get()) {
            boundingBox = boundingBox.offset(
                    (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get()),
                    (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(minPredictSize.get(), maxPredictSize.get())
            );
        }

        VecRotation vecRotation = aacMode ?
                RotationUtils.searchCenterAAC(boundingBox, outborderValue.get() && !attackTimer.hasTimePassed(attackDelay / 2), randomCenterValue.get(), predictValue.get(),
                        PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, entity) < throughWallsRangeValue.get(), maxRange()) :
                RotationUtils.searchCenterNCP(boundingBox, PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, entity) < throughWallsRangeValue.get(), maxRange());

        if (vecRotation == null)
            return false;

        Rotation rotation = vecRotation.getRotation();
        Rotation limitedRotation;
        switch (rotationMode.get()) {
            case "Vanilla":
                limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation, turnSpeed);

                if (silentRotationValue.get()) {
                    RotationUtils.setTargetRotation(limitedRotation, aacMode ? 15 : 0);
                } else {
                    limitedRotation.toPlayer(mc.thePlayer);
                }
                break;

            case "RayCast":
                boolean raycastedEntity = RotationUtils.isFaced(entity, maxRange());
                limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation, raycastedEntity ? 0f : turnSpeed);

                if (silentRotationValue.get()) {
                    RotationUtils.setTargetRotation(limitedRotation, aacMode ? 15 : 0);
                } else {
                    limitedRotation.toPlayer(mc.thePlayer);
                }
                break;

            case "BackTrack":
                limitedRotation = RotationUtils.limitAngleChange(
                        RotationUtils.serverRotation,
                        RotationUtils.OtherRotation(
                                boundingBox,
                                RotationUtils.getCenter(entity.getEntityBoundingBox()),
                                PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, entity) < throughWallsRangeValue.get(),
                                maxRange()
                        ),
                        turnSpeed
                );

                if (silentRotationValue.get()) {
                    RotationUtils.setTargetRotation(limitedRotation, aacMode ? 15 : 0);
                } else {
                    limitedRotation.toPlayer(mc.thePlayer);
                }
                break;
        }

        return true;
    }

    /**
     * Check if [entity] is selected as enemy with current target options and other modules 检测实体是否应该攻击
     */
    private boolean isEnemy(Entity entity) {
        if (entity instanceof EntityLivingBase && (Target.dead.get() || isAlive((EntityLivingBase) entity)) && entity != mc.thePlayer) {
            if (!Target.invisible.get() && entity.isInvisible())
                return false;

            if (Target.player.get() && entity instanceof EntityPlayer) {
                if (((EntityPlayer)entity).isSpectator() || AntiBot.isBot((EntityLivingBase) entity))
                    return false;

                if (EntityUtils.isFriend(entity) && !LiquidSense.moduleManager.get(NoFriends.class).getState())
                    return false;

                Teams teams = (Teams) LiquidSense.moduleManager.get(Teams.class);

                return !teams.getState() || !Teams.isInYourTeam((EntityLivingBase) entity);
            }

            if (entity instanceof EntityWither) {
                return witherValue.get();
            }

            return Target.mob.get() && EntityUtils.isMob(entity) || Target.animal.get() && EntityUtils.isAnimal(entity);
        }

        return false;
    }

    /**
     * Check if run should be cancelled 检测是否应该取消攻击
     */
    private boolean getCancelRun() {
        return mc.thePlayer == null || mc.thePlayer.isSpectator() || !isAlive(mc.thePlayer)
                || LiquidSense.moduleManager.get(Blink.class).getState()
                || LiquidSense.moduleManager.get(FreeCam.class).getState();
    }

    /**
     * Start blocking 开始格挡
     */
    private void startBlocking(Entity interactEntity, boolean interact) {
        if (interact) {
            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(interactEntity, interactEntity.getPositionVector()));
            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(interactEntity, C02PacketUseEntity.Action.INTERACT));
        }

        String autoBlockValue = AutoBlockValue.get().toLowerCase();
        switch (autoBlockValue) {
            case "normal":
            case "aftertick":
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                break;
            case "pos":
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                break;
            case "click":
                mc.gameSettings.keyBindUseItem.pressed = true;
                break;
            case "aac":
                if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                    mc.gameSettings.keyBindUseItem.pressed = true;
                break;
        }

        blockingStatus = true;
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();

        if (packet instanceof C07PacketPlayerDigging && AutoBlockValue.get().equalsIgnoreCase("AAC") && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            flag = !flag;
            if (flag) {
                event.cancelEvent();
                mc.getNetHandler().addToSendQueue(packet);
                if (getCancelRun()) {
                    target = null;
                    currentTarget = null;
                    hitable = false;
                    stopBlocking();
                    return;
                }

                if (noInventoryAttackValue.get() && mc.currentScreen instanceof GuiContainer) {
                    target = null;
                    currentTarget = null;
                    hitable = false;
                    return;
                }

                if (target != null && currentTarget != null) {
                    while (clicks > 0) {
                        runAttack();
                        clicks--;
                    }
                }
            }
        }
    }

    /**
     * Stop blocking 停止格挡
     */
    private void stopBlocking() {
        if (!blockingStatus) return;

        switch (AutoBlockValue.get().toLowerCase()) {
            case "normal":
            case "aftertick":
            case "pos":
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                break;

            case "click":
            case "aac":
                mc.gameSettings.keyBindUseItem.pressed = false;
                break;
        }

        blockingStatus = false;
    }

    /**
     * Check if [entity] is alive 检测实体是否是活着
     */
    private boolean isAlive(EntityLivingBase entity) {
        return entity.isEntityAlive() && entity.getHealth() > 0 || aacMode && entity.hurtTime > 5;
    }

    /**
     * Check if player is able to block 检测玩家是否可以防砍
     */
    private boolean canBlock() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    /**
     * Range 返回最大距离
     */
    private float maxRange() {
        return Math.max(rangeValue.get(), throughWallsRangeValue.get());
    }

    private float getRange(Entity entity) {
        return PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, entity) >= throughWallsRangeValue.get() ? rangeValue.get() : throughWallsRangeValue.get();
    }

}
