package koks.module.combat;

import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.PlayerHandler;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.RandomUtil;
import koks.api.utils.RotationUtil;
import koks.api.utils.TimeHelper;
import koks.event.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

import java.util.ArrayList;
import java.util.Random;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "KillAura", description = "You hit entities automatically", category = Module.Category.COMBAT)
public class KillAura extends Module implements Module.Unsafe {

    public static EntityLivingBase curEntity;

    int switchCounter;
    float curYaw, curPitch;
    double lastCPS = 70, curHazeRange;
    long lastAttack;
    boolean ignoreCPS, changeCPS, hasSilentRotations;

    private final ArrayList<Entity> entities = new ArrayList<>();

    private final TimeHelper timeHelper = new TimeHelper();
    private final TimeHelper searchTime = new TimeHelper();

    @Value(name = "HitChance", minimum = 0, maximum = 100)
    int hitChance = 100;

    @Value(name = "OnlyWhenNotRayCast", displayName = "Only when not raycast")
    boolean onlyWhenNotRayCast = false;

    @Value(name = "CPS", minimum = 1, maximum = 20)
    double cps = 12;

    @Value(name = "Cps-Mode", displayName = "Mode", modes = {"Randomize", "Smooth", "Static", "Calculate after Hit", "Legit"})
    String cpsMode = "Randomize";

    @Value(name = "SmoothCPS-AfterHit", displayName = "After Hit")
    boolean smoothAfterHit = false;

    @Value(name = "NoTimerAttack")
    boolean noTimerAttack = false;

    @Value(name = "SmoothCPS-CPS-Speed", displayName = "CPS Speed", minimum = 0.1, maximum = 2)
    double smoothCpsSpeed = 0.5;

    @Value(name = "SmoothCPS-RandomStrength", displayName = "Random Strength", minimum = 0.1, maximum = 0.9)
    double smoothCpsRandomStrength = 0.85;

    @Value(name = "SmoothCPS-Randomizing", displayName = "Randomizing")
    boolean smoothCpsRandomizing = true;

    @Value(name = "Range", minimum = 1, maximum = 6)
    double range = 3;

    @Value(name = "PreAim Range", minimum = 1, maximum = 6)
    double preAimRange = 1.5;

    @Value(name = "TargetMode", modes = {"Single", "Hybrid", "Switch", "Multi"})
    String targetMode = "Single";

    @Value(name = "PreferType", modes = {"Distance", "Health"})
    String preferType = "Distance";

    @Value(name = "Switch Delay", minimum = 0, maximum = 1000)
    int switchDelay = 0;

    @Value(name = "Criticals")
    boolean criticals = false;

    @Value(name = "Criticals-Mode", displayName = "Mode", modes = {"Motion", "onGround", "Packet"})
    String criticalsMode = "onGround";

    @Value(name = "AutoBlock")
    boolean autoBlock = false;

    @Value(name = "AutoBlock-Mode", displayName = "Mode", modes = {"Intave", "Full", "Verus"})
    String autoBlockMode = "Intave";

    @Value(name = "Player")
    boolean player = true;

    @Value(name = "Animals")
    boolean animals = false;

    @Value(name = "Villager")
    boolean villager = false;

    @Value(name = "Mobs")
    boolean mobs = false;

    @Value(name = "Ignore Death")
    boolean ignoreDeath = true;

    @Value(name = "Ignore Invisible")
    boolean ignoreInvisible = true;

    @Value(name = "NoSwing")
    boolean noSwing = false;

    @Value(name = "NoSwing-Mode", displayName = "Mode", modes = {"Vanilla", "Packet"})
    String noSwingMode = "Vanilla";

    @Value(name = "Heuristics")
    boolean heuristics = false;

    @Value(name = "Prediction")
    boolean prediction = false;

    @Value(name = "RayCast")
    boolean rayCast = false;

    @Value(name = "StaticPitch")
    boolean staticPitch = false;

    @Value(name = "Pitch", minimum = -90, maximum = 90)
    int pitch = -90;

    @Value(name = "MouseFix")
    boolean mouseFix = true;

    @Value(name = "Mouse Speed", minimum = 0, maximum = 6)
    double mouseSpeed = 5;

    @Value(name = "MoveFix")
    boolean moveFix = false;

    @Value(name = "SilentMoveFix")
    boolean silentMoveFix = false;

    @Value(name = "AttackDelay")
    boolean attackDelay = true;

    @Value(name = "RightAttackPacketOrdering", displayName = "Right attack packet ordering")
    boolean rightAttackPacketOrdering = true;

    @Value(name = "ThroughWalls")
    boolean throughWalls = false;

    @Value(name = "ThroughWalls Range", displayName = "Range", minimum = 1, maximum = 6)
    double throughWallsRange = 3;

    @Value(name = "HitWhileRespawnScreen", displayName = "Hit while respawn screen")
    boolean hitWhileRespawnScreen = false;

    @Value(name = "NoInvAttack")
    boolean noInvAttack = true;

    @Value(name = "CloseInventory")
    boolean closeInventory = false;

    @Value(name = "EatAttack")
    boolean eatAttack = false;

    @Value(name = "UseAttack")
    boolean useAttack = false;

    @Value(name = "PreAimAttack")
    boolean preAimAttack = false;

    @Value(name = "KeepCPS")
    boolean keepCPS = true;

    @Value(name = "SprintReset")
    boolean sprintReset = true;

    @Value(name = "Haze Range")
    boolean hazeRange = false;

    @Value(name = "Haze Range Adjustment", displayName = "Adjustment", minimum = 0, maximum = 1)
    double hazeRangeAdjustment = 0.5;

    @Value(name = "Max Haze Range", displayName = "Maximum Range", minimum = 0, maximum = 3)
    double maxHazeRange = 1;

    @Value(name = "Rotations")
    boolean rotations = true;

    @Value(name = "Clamp Yaw")
    boolean clampYaw = false;

    @Value(name = "A3Fix")
    boolean a3Fix = true;

    @Value(name = "ResetRotation")
    boolean resetRotation = false;

    @Value(name = "Reset-Mode", displayName = "Mode", modes = {"Silent", "Visible"})
    String resetMode = "Silent";

    @Value(name = "BestVector")
    boolean bestVector = true;

    @Value(name = "Inaccuracy", minimum = 0, maximum = 1)
    double inaccuracy = 0;

    @Value(name = "LockView")
    boolean lockView = false;

    @Value(name = "Necessary Rotations")
    boolean necessaryRotations = false;

    @Value(name = "Necessary Mode", displayName = "Mode", modes = {"Pitch", "Yaw", "Both"})
    String necessaryMode = "Both";

    @Value(name = "Near Rotate")
    boolean nearRotate = false;

    @Value(name = "Near Distance", displayName = "Distance", minimum = 0, maximum = 0.5)
    double nearDistance = 0.5;

    @Value(name = "Smooth Rotations")
    boolean smooth = false;

    @Value(name = "FOV", minimum = 0, maximum = 180)
    int fov = 180;

    @Value(name = "Rotation Speed", minimum = 0, maximum = 180)
    int rotationSpeed = 180;

    @Value(name = "HurtTime", minimum = 0, maximum = 10)
    int hurtTime = 10;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        if (name.split("-")[0].equalsIgnoreCase("SmoothCPS")) {
            return cpsMode.equalsIgnoreCase("Smooth");
        }
        return switch (name) {
            case "NoInvAttack" -> !attackDelay && !rightAttackPacketOrdering;
            case "Inaccuracy" -> bestVector;
            case "Near Rotate", "Necessary Mode" -> necessaryRotations;
            case "Near Distance" -> nearRotate;
            case "NoSwing-Mode" -> noSwing;
            case "StaticPitch" -> !rayCast;
            case "Pitch" -> staticPitch;
            case "Reset-Mode" -> resetRotation;
            case "PreferType" -> targetMode.equalsIgnoreCase("Hybrid");
            case "Criticals-Mode" -> criticals;
            case "AutoBlock-Mode" -> autoBlock;
            case "Mouse Speed" -> mouseFix;
            case "ResetRotation", "BestVector", "Heuristics", "Prediction", "RayCast", "RotationFix", "MouseFix", "MoveFix", "ThroughWalls", "PreAimAttack", "LockView", "Necessary Rotations", "Smooth Rotations", "A3Fix" -> rotations;
            case "Rotation Speed" -> rotations && !smooth;
            case "SilentMoveFix" -> moveFix;
            case "ThroughWalls Range" -> throughWalls;
            case "Haze Range Adjustment", "Max Haze Range" -> hazeRange;
            case "KeepCPS" -> preAimAttack;
            default -> super.isVisible(value, name);
        };
    }

    @Override
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        final RotationUtil rotationUtil = RotationUtil.getInstance();

        if (event instanceof final MouseOverEvent mouseOverEvent) {
            mouseOverEvent.setRange(range);
            mouseOverEvent.setRangeCheck(range <= 3);
        }

        if (event instanceof RendererEvent) {
            if (lockView && curEntity != null) {
                getPlayer().rotationYaw = getYaw();
                getPlayer().rotationPitch = getPitch();
            }
        }

        if (event instanceof final RotationEvent rotationEvent) {
            if (curEntity != null && isValid(curEntity) && (eatAttack || (!getPlayer().isEating() || getPlayer().getFakeItem() == null || !(getPlayer().getFakeItem().getItem() instanceof ItemFood)))) {

                if (mc.currentScreen != null && noInvAttack) {
                    if (closeInventory)
                        getPlayer().closeScreen();
                    else
                        return;
                }

                float[] rots = null;
                if (curEntity.hurtTime <= hurtTime)
                    rots = rotationUtil.facePlayer(curEntity, a3Fix, heuristics, smooth, prediction, mouseFix, mouseSpeed, bestVector, inaccuracy, clampYaw, rotationSpeed);

                if (rots != null) {
                    boolean necessary = !necessaryRotations || (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) || (getPlayer().getDistanceToEntity(curEntity) >= nearDistance && nearRotate);
                    boolean yaw = necessaryMode.equalsIgnoreCase("Yaw");
                    boolean pitch = necessaryMode.equalsIgnoreCase("Pitch");
                    boolean both = necessaryMode.equalsIgnoreCase("Both");
                    if (both) {
                        yaw = true;
                        pitch = true;
                    }
                    if (!necessaryRotations || necessary || !yaw)
                        curYaw = rots[0];
                    if (!necessaryRotations || necessary || !pitch)
                        curPitch = rots[1];
                }

                if (rotations) {
                    rotationEvent.setYaw(curYaw);
                    rotationEvent.setPitch(staticPitch ? pitch : curPitch);
                    hasSilentRotations = true;
                }
            } else {
                if (hasSilentRotations && resetRotation) {
                    resetRotations(getYaw(), getPitch(), resetMode.equalsIgnoreCase("Silent"));
                    hasSilentRotations = false;
                }
                curPitch = getPlayer().rotationPitch;
                curYaw = getPlayer().rotationYaw;
            }
        }

        if (event instanceof final UpdateMotionEvent updateMotionEvent) {
            if (updateMotionEvent.getType() == UpdateMotionEvent.Type.POST) {
                if (autoBlock) {
                    if (curEntity != null) {
                        switch (autoBlockMode) {
                            case "Full":
                                if ((eatAttack || (!getPlayer().isEating() || getPlayer().inventory.getStackInSlot(getPlayer().inventory.fakeItem) == null || !(getPlayer().inventory.getStackInSlot(getPlayer().inventory.fakeItem).getItem() instanceof ItemFood))) && getPlayer().getCurrentEquippedItem() != null && getPlayer().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                    getPlayerController().sendUseItem(getPlayer(), getWorld(), getPlayer().getCurrentEquippedItem());
                                }
                                break;
                            case "Verus":
                                if (getPlayer().isSwingInProgress && getPlayer().hurtTime != 0) {
                                    if (getPlayer().getHeldItem() != null)
                                        if (getPlayer().getHeldItem().getItem() instanceof ItemSword) {
                                            getGameSettings().keyBindUseItem.pressed = true;
                                        }
                                } else {
                                    if (getPlayer().getHeldItem() != null)
                                        if (getPlayer().getHeldItem().getItem() instanceof ItemSword) {
                                            getGameSettings().keyBindUseItem.pressed = false;
                                        }
                                }
                                break;
                        }
                    }
                }
            }
        }

        if (event instanceof final MoveEvent moveEvent) {
            if (moveFix && curEntity != null && isValid(curEntity)) {
                moveEvent.setYaw(getYaw());
            }
        }

        if (event instanceof final JumpEvent jumpEvent) {
            if (moveFix && curEntity != null && isValid(curEntity)) {
                jumpEvent.setYaw(getYaw());
            }
        }

        if (event instanceof final UpdatePlayerMovementState updatePlayerMovementState) {
            if (moveFix && curEntity != null && silentMoveFix && isValid(curEntity)) {
                updatePlayerMovementState.setSilentMoveFix(true);
                updatePlayerMovementState.setYaw(getYaw());
            }
        }

        if (event instanceof UpdateEvent) {
            if (curEntity != null) {
                if (curEntity.hurtTime == 10 && curHazeRange < maxHazeRange && hazeRange) {
                    curHazeRange += hazeRangeAdjustment;
                    lastAttack = System.currentTimeMillis() / 1000;
                }
            }

            boolean hasSword = getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemSword;
            boolean isBlocking = getGameSettings().keyBindUseItem.pressed && hasSword;

            if (autoBlock) {
                switch (autoBlockMode) {
                    case "Intave":
                        if (timeHelper.hasReached((long) (lastCPS - 20))) {
                            if (isBlocking)
                                getGameSettings().keyBindUseItem.pressed = false;
                        } else {
                            if (hasSword) {
                                getGameSettings().keyBindUseItem.pressed = true;
                            }
                        }
                        break;
                }
            }

            if (!hazeRange)
                curHazeRange = 0;

            if (System.currentTimeMillis() / 1000 - lastAttack >= 2) {
                curHazeRange = 0;
            }

            entities.clear();
            for (Entity entity : getWorld().loadedEntityList) {
                if (isValid(entity) && !entities.contains(entity))
                    entities.add(entity);
            }

            if (!targetMode.equalsIgnoreCase("Multi")) {
                for (Entity entity : getWorld().loadedEntityList) {
                    if (isValid(entity) && searchTime.hasReached(switchDelay)) {
                        switch (targetMode) {
                            case "Single":
                                if (curEntity == null)
                                    curEntity = (EntityLivingBase) entity;
                                break;
                            case "Hybrid":
                                switch (preferType) {
                                    case "Distance" -> curEntity = (EntityLivingBase) getNearestEntity();
                                    case "Health" -> curEntity = (EntityLivingBase) getLowTarget();
                                }
                                break;
                            case "Switch":
                                if (entities.size() - 1 >= switchCounter)
                                    if (isValid(entities.get(switchCounter)))
                                        curEntity = (EntityLivingBase) entities.get(switchCounter);
                                    else
                                        entities.remove(entities.get(switchCounter));
                                else
                                    switchCounter = 0;
                                break;
                        }
                    }
                }

                if (curEntity != null) {
                    if (!isValid(curEntity)) {
                        if (hasSilentRotations && resetRotation) {
                            resetRotations(getYaw(), getPitch(), resetMode.equalsIgnoreCase("Silent"));
                            hasSilentRotations = false;
                        }
                        curEntity = null;
                    }
                }
            }
        }
        if (event instanceof AttackEvent) {
            if (this.sprintReset && curEntity != null && curEntity.hurtTime == 10) {
                PlayerHandler.shouldSprintReset = true;
            }
        }

        if (rightAttackPacketOrdering ? event instanceof AttackEvent : event instanceof UpdateEvent) {
            if (getTimer().timerSpeed != 1 && noTimerAttack) return;
            if (mc.currentScreen instanceof GuiGameOver && !hitWhileRespawnScreen) return;
            if (!targetMode.equalsIgnoreCase("Multi")) {
                if (curEntity != null) {
                    changeCPS = false;

                    if (!cpsMode.equalsIgnoreCase("Calculate after hit") && !smoothAfterHit) {
                        calculateCPS();
                    }
                    if (timeHelper.hasReached((long) (lastCPS)) || (ignoreCPS && keepCPS))
                        if (RandomUtil.getInstance().getRandomInteger(0, 100) <= hitChance && (!onlyWhenNotRayCast || mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY))
                            attack(curEntity);
                        else {
                            getPlayer().swingItem();
                            changeCPS = true;
                            mc.leftClickCounter = 10;
                            timeHelper.reset();
                        }
                }
            } else {
                for (Entity entity : getWorld().loadedEntityList) {
                    if (isValid(entity))
                        attack(entity);
                }
            }
        }
    }

    public Entity getLowTarget() {
        EntityLivingBase entity = null;
        for (Entity ent : getWorld().loadedEntityList) {
            if (isValid(ent)) {
                if (entity != null && ((EntityLivingBase) ent).getHealth() < entity.getHealth())
                    entity = (EntityLivingBase) ent;
                else if (entity == null)
                    entity = (EntityLivingBase) ent;
            }
        }
        return entity;
    }

    public Entity getNearestEntity() {
        Entity entity = null;
        for (Entity ent : getWorld().loadedEntityList) {
            if (isValid(ent)) {
                if (entity != null && getPlayer().getDistanceToEntity(ent) < getPlayer().getDistanceToEntity(entity))
                    entity = ent;
                else if (entity == null)
                    entity = ent;
            }
        }
        return entity;
    }

    public void attack(Entity entity) {
        final RotationUtil rotationUtil = RotationUtil.getInstance();
        if (mc.currentScreen != null && noInvAttack) {
            if (closeInventory)
                getPlayer().closeScreen();
            else
                return;
        }
        double rangeVal = throughWalls && !getPlayer().canEntityBeSeen(entity) ? throughWallsRange : range;
        if (rayCast) {
            entity = rotationUtil.rayCastedEntity(rangeVal, getYaw(), getPitch());
        }
        final boolean withoutRayCast = entity != null && getPlayer().getDistanceToEntity(entity) <= rangeVal + curHazeRange;
        if (mc.leftClickCounter <= 0 || !attackDelay) {
            if (entity != null && (rayCast || withoutRayCast) && (eatAttack || (!getPlayer().isEating() || getPlayer().inventory.getStackInSlot(getPlayer().inventory.fakeItem) == null || !(getPlayer().inventory.getStackInSlot(getPlayer().inventory.fakeItem).getItem() instanceof ItemFood)))) {
                if (useAttack || (!getGameSettings().keyBindUseItem.pressed && getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemSword && autoBlock || !getGameSettings().keyBindUseItem.pressed)) {
                    switchCounter++;
                    if (noSwing) {
                        switch (noSwingMode) {
                            case "Packet":
                                sendPacket(new C0APacketAnimation());
                                break;
                        }
                    } else
                        getPlayer().swingItem();

                    if (criticals) {
                        switch (criticalsMode) {
                            case "Motion":
                                if (getPlayer().onGround) {
                                    getPlayer().motionY = 0.2;
                                    getPlayer().fallDistance = 1.15F;
                                    getPlayer().motionY -= 0.2;
                                }
                                break;
                            case "Packet":
                                if (getPlayer().onGround) {
                                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.42, getZ(), false));
                                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.2, getZ(), false));
                                }
                                break;
                            case "onGround":
                                if (getPlayer().onGround)
                                    sendPacket(new C03PacketPlayer(false));
                                break;
                        }
                    }

                    /*if(entity instanceof EntityPlayer) {
                        debugPlayer((EntityPlayer) entity);
                    }*/

                    switch (autoBlockMode) {
                        case "Full":
                            if ((eatAttack || (!getPlayer().isEating() || getPlayer().inventory.getStackInSlot(getPlayer().inventory.fakeItem) == null || !(getPlayer().inventory.getStackInSlot(getPlayer().inventory.fakeItem).getItem() instanceof ItemFood))) && getPlayer().getCurrentEquippedItem() != null && getPlayer().getCurrentEquippedItem().getItem() instanceof ItemSword)
                                sendPacketUnlogged(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            break;
                    }

                    getPlayerController().attackEntity(getPlayer(), entity);

                    if (criticals) {
                        switch (criticalsMode) {
                            case "Packet":
                                if (getPlayer().onGround) {
                                    sendPacketUnlogged(new C03PacketPlayer(true));
                                }
                                break;
                        }
                    }

                    timeHelper.reset();
                    ignoreCPS = false;
                    changeCPS = true;
                    if (cpsMode.equalsIgnoreCase("Calculate after Hit")) {
                        calculateCPS();
                    }
                }
            } else {
                mc.leftClickCounter = 10;
                if (preAimAttack) {
                    if (cpsMode.equalsIgnoreCase("Calculate after hit") || smoothAfterHit) {
                        calculateCPS();
                    }
                    getPlayer().swingItem();
                    timeHelper.reset();
                    if (keepCPS)
                        ignoreCPS = true;
                    changeCPS = true;
                }
            }
        }
        searchTime.reset();
    }

    boolean wasCPSDrop = false;
    final TimeHelper cpsTimeHelper = new TimeHelper();

    public void calculateCPS() {
        double cps = this.cps > 10 ? this.cps + 5 : this.cps;
        if(cpsMode.equalsIgnoreCase("Legit")) {
            final Random random = new Random();
            final double maxCPS = cps + 1;
            lastCPS = cps + (random.nextInt() * (maxCPS - cps));

            if (cpsTimeHelper.hasReached((long) (1000.0 / RandomUtil.getInstance().randomInRange(cps, maxCPS)), true)) {
                wasCPSDrop = !wasCPSDrop;
            }

            final double cur = System.currentTimeMillis() * random.nextInt(220);

            double timeCovert = Math.max(lastCPS, cur) / 3;
            if (wasCPSDrop) {
                lastCPS = (int) timeCovert;
            } else {
                lastCPS = (int) (cps + (random.nextInt() * (maxCPS - cps)) / timeCovert);
            }
        } else {
            switch (cpsMode) {
                case "Smooth":
                    final RandomUtil randomUtil = RandomUtil.getInstance();
                    cps = randomUtil.smooth(this.cps + 1, this.cps, smoothCpsSpeed / 10, smoothCpsRandomizing, smoothCpsRandomStrength);
                    break;
            }
            lastCPS = 1000D / cps;
            if (this.cps == 20) {
                this.lastCPS = 0;
            }
            if (cpsMode.equalsIgnoreCase("Randomize") || cpsMode.equalsIgnoreCase("Calculate after Hit"))
                lastCPS += RandomUtil.getInstance().getRandomGaussian(50);
        }
    }

    public boolean isValid(Entity entity) {
        if (entity == null) return false;

        double rangeVal = throughWalls && !getPlayer().canEntityBeSeen(entity) ? throughWallsRange : range;

        if (entity.getDistanceToEntity(getPlayer()) >= rangeVal + curHazeRange + preAimRange) return false;

        if (entity instanceof EntityPlayerSP) return false;

        if (!(entity instanceof EntityLivingBase)) return false;

        if (entity instanceof EntityPlayer && !player) return false;

        if (entity instanceof EntityVillager && !villager) return false;

        if (entity instanceof EntityArmorStand) return false;

        if ((entity instanceof EntityAmbientCreature || entity instanceof EntityAnimal) && !animals) return false;

        if (entity instanceof EntityMob && !mobs) return false;

        if (!getPlayer().canEntityBeSeen(entity) && !throughWalls) return false;

        if (entity.isDead && ignoreDeath) return false;

        if (entity.isInvisible() && ignoreInvisible) return false;
        if (((EntityLivingBase) entity).deathTime != 0) return false;

        final Friends friends = ModuleRegistry.getModule(Friends.class);
        if (friends.isToggled() && Koks.getKoks().friendManager.isFriend(entity.getName())) return false;

        final AntiBot antiBot = ModuleRegistry.getModule(AntiBot.class);
        if (antiBot.isToggled() && antiBot.isBot((EntityLivingBase) entity)) return false;

        double calcYaw = RotationUtil.getInstance().calculateRotationDiff(MathHelper.wrapAngleTo180_float(getYaw()) + 180, MathHelper.wrapAngleTo180_float(RotationUtil.getInstance().getYaw(getPlayer(), entity)))[0];

        if (calcYaw > fov)
            return false;

        final Teams teams = ModuleRegistry.getModule(Teams.class);
        if (entity instanceof EntityPlayer && teams.isToggled() && isTeam(getPlayer(), (EntityPlayer) entity))
            return false;

        return true;
    }

    @Override
    public void onEnable() {
        changeCPS = true;
        curYaw = getPlayer().rotationYaw;
        curPitch = getPlayer().rotationPitch;
        if (cpsMode.equalsIgnoreCase("Calculate after Hit")) {
            calculateCPS();
        }
    }

    @Override
    public void onDisable() {
        switch (autoBlockMode) {
            case "Full":
                if (getPlayer().getCurrentEquippedItem() != null && getPlayer().getCurrentEquippedItem().getItem() instanceof ItemSword)
                    sendPacketUnlogged(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                break;
        }
        if (hasSilentRotations && resetRotation)
            resetRotations(getYaw(), getPitch(), resetMode.equalsIgnoreCase("Silent"));
        hasSilentRotations = false;
        if (autoBlock)
            getGameSettings().keyBindUseItem.pressed = false;
        curEntity = null;
    }

    public static EntityLivingBase getCurEntity() {
        return curEntity;
    }
}