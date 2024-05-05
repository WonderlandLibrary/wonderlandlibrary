package rip.helium.cheat.impl.combat.aura;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import com.google.common.collect.Lists;

import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.FocusManager;
import rip.helium.cheat.FriendManager;
import rip.helium.cheat.impl.combat.AntiBot;
import rip.helium.cheat.impl.visual.Hud;
import rip.helium.event.minecraft.*;
import rip.helium.utils.BlockUtils;
import rip.helium.utils.Mafs;
import rip.helium.utils.R2DUtils;
import rip.helium.utils.RenderUtils;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.UPlayer;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Aura extends Cheat {

    private StringsProperty abmode = new StringsProperty("Autoblock Mode", "Changes the AB mode", null, false, true, new String[]{"Hypixel", "Real", "Fake", "Ghostly"}, new Boolean[]{true, false, false, false});
    private static StringsProperty mode = new StringsProperty("Mode", "Changes the mode", null, false, true, new String[]{"Priority", "Multi"}, new Boolean[]{true, false});
    private StringsProperty targetPriority = new StringsProperty("Priority", "How the priority target will be selected.", null, false, true, new String[]{"Lowest Health", "Least Armor", "Closest"}, new Boolean[]{true, false, false});
    private StringsProperty targetEntities = new StringsProperty("Targets", "The entites that will be targetted.", null, true, false, new String[]{"Players", "Monsters", "Animals", "Villagers", "Golems"}, new Boolean[]{true, true, false, false, false});
    private BooleanProperty prioritisePlayers = new BooleanProperty("Prioritize Players", "Always hit players before monsters.", () -> targetEntities.getValue().get("Players") && targetEntities.getSelectedStrings().size() > 2, false);
    private BooleanProperty hvh = new BooleanProperty("HvH", "Optimises Aura for HvH.", null, false);
    private BooleanProperty click = new BooleanProperty("Left Click", "makes it sqo u have to hit left hit", null, false);
    private BooleanProperty silent = new BooleanProperty("Silent", "Silent / Lock Aimbot", null, true);
    private BooleanProperty debug = new BooleanProperty("Debug", "Toggle the debug mode", null, true);
    private BooleanProperty miss = new BooleanProperty("Miss Hits", "Miss hits, might bypass anticheats!", null, true);
    private BooleanProperty hidedis = new BooleanProperty("Hide Display Name", "in debug mode", null, true);
    private BooleanProperty multi = new BooleanProperty("Multi", "Hits multiple people, detected.", null, true);
    private BooleanProperty middleClickIgnore = new BooleanProperty("Middle Click to Ignore", "Ignores any entity that you middle click on.", null, false);
    //private BooleanProperty propWalls = new BooleanProperty("Walls", "Ignore entities through walls", null, false);
    private BooleanProperty keepSprint = new BooleanProperty("KeepSprint", "Prevents your sprint from being reset. can cause bans", null, true);
    public BooleanProperty middleClickReset = new BooleanProperty("Middle Click Reset", "Resets your ignored targets when the world changes.", () -> middleClickIgnore.getValue(), false);
    private BooleanProperty teams = new BooleanProperty("Teams", "Hypixel teams", null, false);
    //private BooleanProperty autoBlock = new BooleanProperty("Auto Block", "Blocks when you arent attacking to decrease the amount of damage you take.", null, false);
    private DoubleProperty minimumAttackSpeed = new DoubleProperty("APS Flucutation", "The maximum amount your aps can fluctuate.", null, 2.0, 0, 5.0, 0.1, null);
    private static DoubleProperty prop_maxDistance = new DoubleProperty("Max Distance", "The maximum distance en entity can be to be targetted.", null, 4.0, 0.1, 6.0, 0.1, null);
    private DoubleProperty maximumAttackSpeed = new DoubleProperty("APS", "The minimum amount of times you will attack in a second.", null, 10, 1, 20.0, 0.1, null);
    private BooleanProperty fakeab = new BooleanProperty("Autoblock", "yes.", null, false);
    private BooleanProperty showCircle = new BooleanProperty("Show Circle", "Show a circle.", null, false);

    private boolean isBlocking, apsDecrease, yawDecrease, pitchDecrease;

    private Stopwatch apsStopwatch, botClearStopwatch;
    private float animated = 20f;

    public static int blockDelay, attackSpeed;
    public int waitDelay, groundTicks, crits;
    private float aps;
    public long lastHit;
    private ScaledResolution sr;

    private static int auraDelay;
    private float yaw, pitch, yawIncrease, pitchIncrease, serverSideYaw, serverSidePitch;

    public BooleanProperty spinbot = new BooleanProperty("Spinbot", "Spins around", null, false);

    public static int targetIndex;

    private ArrayList<EntityLivingBase> mcf;
    private static ArrayList<EntityLivingBase> targetList;
    public static EntityLivingBase currentEntity;
    public ArrayList<EntityLivingBase> ignoredEntities;
    private ArrayList<EntityLivingBase> whitelistedEntity;

    private double y, x, z, fall;

    public Aura() {
        super("KillAura", "Automatically attacks entities.", CheatCategory.COMBAT);
        registerProperties(mode, hvh, click, targetPriority, spinbot, miss, keepSprint, showCircle, targetEntities, teams, prioritisePlayers, middleClickIgnore, middleClickReset, minimumAttackSpeed, maximumAttackSpeed, prop_maxDistance, fakeab, abmode);

        isBlocking = false;
        targetIndex = 0;
        mcf = new ArrayList<>();
        this.sr = RenderUtils.getResolution();
        targetList = new ArrayList<>();
        apsStopwatch = new Stopwatch();
        ignoredEntities = new ArrayList<>();
        whitelistedEntity = new ArrayList<>();
        botClearStopwatch = new Stopwatch();

    }

    public void onEnable() {
        propertyupdate();
        //ChatUtil.chat("");
    }

    public void onDisable() {
        propertyupdate();
    }

    public void propertyupdate() {

        //Start fresh with a new target list

        targetIndex = -1;
        targetList.clear();
        ignoredEntities.clear();

        /* Reset block start*/

        isBlocking = false;

        /*Reset block state with packet AutoBlock*/
        attemptStopAutoblockNoSet_Watchdog();

        /*Make last reported aps to 6 aps*/
        aps = 1000 / 6;
        yawIncrease = 0;
        pitchIncrease = 0;

        /*Make sure player isn't null when setting yaw - ClickGui can be called from main menu*/
        if (mc.thePlayer != null) {
            serverSideYaw = getPlayer().rotationYaw;
            serverSidePitch = getPlayer().rotationPitch;
        }

        lastHit = System.currentTimeMillis() + 50;
        setDelay(0);
    }

    /*Pre and post motion updates*/
    @Collect
    public void onBlockStep(BlockStepEvent event) {
        if (mc.thePlayer == null)
            return;
        if (getPlayer().getEntityBoundingBox().minY - getPlayer().posY < .626 && getPlayer().getEntityBoundingBox().minY - getPlayer().posY > .4) {
            waitDelay = 4;
        }
    }

    @Collect
    public void RenderEntityEvent(EntityRenderEvent e) {
        if (showCircle.getValue()) {
            drawCircle(mc.thePlayer, e.getPartialTicks(), prop_maxDistance.getValue());
        }
    }

    @Collect
    public void processPacketEvent(ProcessPacketEvent e) {
        if (keepSprint.getValue()) {
            if (e.getPacket() instanceof C0BPacketEntityAction) {
                if (((C0BPacketEntityAction) e.getPacket()).getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING || ((C0BPacketEntityAction) e.getPacket()).getAction() == C0BPacketEntityAction.Action.START_SPRINTING) {
                    e.setCancelled(true);
                }
            }
        }
    }


    public void attack(final EntityLivingBase entity) {
        this.attack(entity, true);
    }

    public void swingItem() {
        mc.thePlayer.swingItem();
    }

    public void attack(final EntityLivingBase entity, final boolean crit) {
        this.swingItem();
        final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = mc.thePlayer.fallDistance > 0.0f && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null;
        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            mc.thePlayer.onCriticalHit(entity);

        }
        if (sharpLevel > 0.0f) {
            mc.thePlayer.onEnchantmentCritical(entity);
        }
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        this.setMode(this.mode.getSelectedStrings().get(0));

        if (getCurrentTarget() != null) {
            if (abmode.getValue().get("Ghostly")) {
                mc.gameSettings.keyBindUseItem.pressed = false;
            }
        }

        if (mode.getValue().get("Priority")) {
            if (mc.currentScreen != null)
                return;
            //mc.theWorld.spawnParticle(EnumParticleTypes.CRIT_MAGIC, getCurrentTarget().posX, getCurrentTarget().posY, getCurrentTarget().posX, 2, 3, 4, 1);
            boolean preblocking = !mc.thePlayer.isMoving() && mc.thePlayer.ticksExisted % 2 == 0;
            if (auraDelay <= 0) {
                auraDelay = 0;

                if (botClearStopwatch.hasPassed(30000)) {
                    ignoredEntities.clear();
                    botClearStopwatch.reset();
                }


                getWorld().getLoadedEntityList().forEach(entity -> {
                    if (entity != getPlayer() && entity instanceof EntityLivingBase) {
                        if (Helium.instance.cheatManager.isCheatEnabled("AntiBot") && !mc.getCurrentServerData().serverIP.contains("mineplex")) {
                            if (entity != getPlayer()) {
                                if (entity != getPlayer() && entity instanceof EntityPlayer) {
                                    EntityPlayer entityPlayer = (EntityPlayer) entity;
                                    if (AntiBot.bots.contains(entityPlayer)) ignoredEntities.add(entityPlayer);
                                    if (!isInTablist(entityPlayer)) {
                                        if (!ignoredEntities.contains(entityPlayer)) {
                                            ignoredEntities.add(entityPlayer);
//                                            entityPlayer.setInvisible(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

                updateTargetList();

                if (targetList.isEmpty() || targetList.size() - 1 < targetIndex) {
                    targetIndex = -1;
                    attemptStopAutoblock_Watchdog();
                    if (groundTicks != 0) {
                        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX, mc.thePlayer.posY, mc.thePlayer.posZ, event.getYaw(), event.getPitch(), event.onGround));
                        groundTicks = 0;
                    }
                    return;
                }

                if (targetIndex == -1) {
                    targetIndex = 0;
                    attemptStopAutoblock_Watchdog();
                    if (groundTicks != 0) {
                        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX, mc.thePlayer.posY, mc.thePlayer.posZ, event.getYaw(), event.getPitch(), event.onGround));
                        groundTicks = 0;
                    }
                    return;
                }

                if (!isValidTarget(targetList.get(targetIndex))) {
                    targetIndex = -1;
                    attemptStopAutoblock_Watchdog();
                    if (groundTicks != 0) {
                        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX, mc.thePlayer.posY, mc.thePlayer.posZ, event.getYaw(), event.getPitch(), event.onGround));
                        groundTicks = 0;
                    }
                    return;
                }

                if (targetIndex == -1) {
                    targetIndex = 0;
                    return;
                }

                if (!isValidTarget(targetList.get(targetIndex))) {
                    targetIndex = -1;
                    attemptStopAutoblock_Watchdog();
                    return;
                }

                pitchIncrease += pitchDecrease ? -Mafs.getRandomInRange(.1, .12) : Mafs.getRandomInRange(.1, .12);
                if (pitchIncrease >= (Aim(targetList.get(targetIndex), event, false)[1] - 30)) {
                    pitchDecrease = true;
                }
                if (event.getPitch() <= (Aim(targetList.get(targetIndex), event, false)[1] + 5)) {
                    pitchDecrease = false;
                }
                if (event.isPre()) {
                    if (mc.thePlayer.fallDistance != 0) {
                        waitDelay = 2;
                    }
                    boolean cancritical = !Helium.instance.cheatManager.isCheatEnabled("LongJump") && !Helium.instance.cheatManager.isCheatEnabled("Flight") && !Helium.instance.cheatManager.isCheatEnabled("Speed") && targetIndex != -1 && getPlayer().fallDistance == 0.0 && !mc.gameSettings.keyBindJump.isKeyDown() && getPlayer().isCollidedVertically && getPlayer().onGround && !getPlayer().isInWater();
                    if (Helium.instance.cheatManager.isCheatEnabled("Criticals")) {
                        if (BlockUtils.getBlockAtPos(new BlockPos(getPlayer().posX, getPlayer().posY - 1, getPlayer().posZ)).isFullBlock() && !BlockUtils.getBlockAtPos(new BlockPos(getPlayer().posX, getPlayer().posY + 1, getPlayer().posZ)).isFullBlock()) {
                            if (waitDelay <= 0) {
                                waitDelay = 0;

                                if (cancritical) {
                                    event.setOnGround(false);
                                    groundTicks += 1;
                                    if (groundTicks == 1) {
                                        event.setOnGround(false);
                                        event.setPosY(event.getPosY() + 0.0627878);
                                    } else if (groundTicks == 2) {
                                        event.setOnGround(false);
                                        event.setPosY(event.getPosY() + 0.062663);
                                    } else if (groundTicks == 3) {

                                        event.setOnGround(false);
                                        event.setPosY(event.getPosY() + 0.0001);
                                    } else if (groundTicks >= 4) {
                                        event.setOnGround(false);
                                        event.setPosY(event.getPosY() + 0.0001);
                                        groundTicks = 0;
                                    }
                                } else {
                                    waitDelay = 2;
                                }
                            } else {
                                waitDelay -= 1;
                            }
                        } else if (groundTicks != 0) {
                            waitDelay = 4;
                            groundTicks = 0;
                        }
                    }
                    if (getPlayer().getDistanceToEntity(targetList.get(targetIndex)) > (.1 + Math.abs(getPlayer().posY - targetList.get(targetIndex).posY) * .1)) {
                        if (!click.getValue()) {
                            float randomr = (float) Mafs.getRandomInRange(0.01, 0.999999);
                            if (!spinbot.getValue()) {
                                event.setYaw(serverSidePitch = randomr + Aim(targetList.get(targetIndex), event, false)[0]);
                                event.setPitch(serverSidePitch = randomr + Aim(targetList.get(targetIndex), event, false)[1]);

                            } else {
                                float randommmm = Mafs.getRandomInRange(1, 360);
                                event.setYaw(randommmm);

                            }
                        } else {
                            event.setYaw(serverSideYaw);
                            event.setPitch(serverSidePitch);
                        }
                        if (click.getValue()) {
                            if (mc.gameSettings.keyBindAttack.pressed) {
                                float randomr = (float) Mafs.getRandomInRange(0.01, 0.999999);
                                event.setYaw(serverSidePitch = randomr + Aim(targetList.get(targetIndex), event, false)[0]);
                                event.setPitch(serverSidePitch = randomr + Aim(targetList.get(targetIndex), event, false)[1]);
                            } else {
                                event.setYaw(serverSideYaw);
                                event.setPitch(serverSidePitch);
                            }
                        }
                    }


                    if (!holdingSword() && isBlocking) {
                        isBlocking = false;
                    }

                    if (aps >= (1000 / maximumAttackSpeed.getValue() - minimumAttackSpeed.getValue() + 2)) {
                        apsDecrease = true;
                    }
                    if (apsDecrease) {
                        if (aps <= ((1000 / (maximumAttackSpeed.getValue())))) {
                            apsDecrease = false;
                        }
                    }
                    if (mode.getValue().get("Priority")) {
                        if (abmode.getValue().get("Hypixel")) {
                            attemptStopAutoblock_Watchdog();
                        }

                        //todo: gamer
                        blockDelay++;
                        if (apsStopwatch.hasPassed((hvh.getValue() ? 50 : aps))) {
                            // no click, just hit normally
                            if (!click.getValue()) {
                                attackexecute(event);
                            } else {
                                // is holding attack kb?
                                if (mc.gameSettings.keyBindAttack.pressed) {
                                    attackexecute(event);
                                }
                            }
                        }
                    }
                } else
                    attemptStartAutoblock_Watchdog();


                yaw = event.getYaw();
                pitch = event.getPitch();
            } else {
                auraDelay -= 1;
            }
        } else {
            if (!event.isPre()) {
                if (Helium.instance.cheatManager.isCheatEnabled("Criticals")) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.001, mc.thePlayer.posZ, true));
                    event.setOnGround(false);
                }
                for (final Object entity : mc.theWorld.loadedEntityList) {
                    if (entity instanceof EntityLivingBase) {
                        if (entity != mc.thePlayer) {
                            if (mc.thePlayer.getDistanceToEntity((Entity) entity) < prop_maxDistance.getValue()) {
                                if (isValidTarget((EntityLivingBase) entity)) {
                                    attack((EntityLivingBase) entity);
                                    currentEntity = (EntityLivingBase) entity;
                                    if (fakeab.getValue() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void attackexecute(PlayerUpdateEvent event) {
        if (targetList.isEmpty())
            return;

        if (targetIndex == -1)
            return;

        if (targetIndex > targetList.size() - 1)
            return;

        if (abmode.getValue().get("Ghostly")) {
            mc.gameSettings.keyBindUseItem.pressed = true;
        }

        attack(event);
        if (abmode.getValue().get("Real")) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
        }
        apsStopwatch.reset();
    }

    public boolean isInputBetween(double input, double min, double max) {
        return input >= min && input <= max;//Check to see if yaw is between a specified number(input*)
    }

    public Entity raycast(EntityLivingBase target) {
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;//Credits to verble for this
            if (entity.isInvisible() && object != null) {
                if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)
                        && !(entity instanceof EntityArrow)) {
                    if (entity.getEntityBoundingBox().intersectsWith(getPlayer().getEntityBoundingBox())) {
                        return entity;
                    }
                }
            }
        }
        return target;
    }

    private boolean isInTablist(EntityLivingBase player) {
        if (mc.isSingleplayer()) {
            return true;
        }
        for (Object o : mc.getNetHandler().getPlayerInfoMap()) {
            NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
            if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean raytraceCheck(EntityLivingBase entity) {
        EntitySnowball entitySnowball = new EntitySnowball(mc.theWorld);
        entitySnowball.posX = entity.posX;
        entitySnowball.posY = entity.posY + entity.getEyeHeight() / 2;
        entitySnowball.posZ = entity.posZ;
        return getPlayer().canEntityBeSeen(entitySnowball);
    }

    public float wrapAngleToSpecified_float(float value, float maxangle) {
        value = value % 360.0F;
        if (value >= maxangle) {
            value -= 360.0F;
        }
        if (value < -maxangle) {
            value += 360.0F;
        }
        return value;
    }

    private boolean isPosSolid(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

    public float[] Aim(Entity ent, PlayerUpdateEvent event, boolean BBB) {
        double x = ent.posX - Minecraft.getMinecraft().thePlayer.posX, y = ent.posY + (BBB ? .1 : ent.getEyeHeight() / 2) - event.getPosY() - (BBB ? 0 : 1.2), z = ent.posZ - Minecraft.getMinecraft().thePlayer.posZ;


        return new float[]{MathHelper.wrapAngleTo180_float((float) (Math.atan2(z, x) * 180 / Math.PI) - 90), (float) -(Math.atan2(y, MathHelper.sqrt_double(x * x + z * z)) * 180 / Math.PI)};
    }

    public void attack(PlayerUpdateEvent event) {
        EntityLivingBase target = targetList.get(targetIndex);
        attack(target);

        aps += apsDecrease ? -Mafs.getRandomInRange(15, 75) : Mafs.getRandomInRange(15, 75);
        attackSpeed += 1;
        if (attackSpeed >= 4) {
            attackSpeed = 0;
        }
        blockDelay = 5;
    }

    //int sel = 1;
    int rand;

    public void attackexecute(EntityLivingBase target) {
        if (getPlayer().getDistanceToEntity(targetList.get(targetIndex)) <= (hvh.getValue() ? 4.5 : prop_maxDistance.getValue())) {
            getPlayer().swingItem();

            rand = Mafs.getRandomInRange(1, 20);
            //ChatUtil.chat(rand + " is random; " + "has to be 1 to miss!");
            if (!miss.getValue()) {
                //UPlayer.sendPackets(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                attack((EntityLivingBase) target);
            } else {
                if (!(rand == 1)) {
                    //UPlayer.sendPackets(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                    attack((EntityLivingBase) target);
                } else {
                    ChatUtil.chat("§cMissed a hit!");
                }
            }
            target.attacks += 1;
        } else {
            if (target.attacks <= 0) {
                target.attacks = 0;

            } else {
                target.attacks -= 1;
            }
            attackSpeed = 0;
        }
    }

    public boolean isValidTarget(EntityLivingBase entity) {
        if (teams.getValue() && entity.getDisplayName().getUnformattedText().contains("\247a") && !FriendManager.friends.contains(entity)) {
            return false;
        }
        if (ignoredEntities.contains(entity) && !whitelistedEntity.contains(entity)) {
            return false;
        }
        if (FriendManager.friends.contains(entity.getName())) {
            return false;
        }
        try {
            if (mc.getCurrentServerData().serverIP.contains("mineplex")) {
                if (!Double.isNaN(entity.getHealth())) {
                    return false;
                }
            }
        } catch (Exception e ) {
        }

        if (entity != getPlayer() && (entity instanceof EntityPlayer && targetEntities.getValue().get("Players"))
                || (entity instanceof EntityMob && targetEntities.getValue().get("Monsters"))
                || (entity instanceof EntitySlime && targetEntities.getValue().get("Monsters"))
                || (entity instanceof EntityAnimal && targetEntities.getValue().get("Animals"))
                || (entity instanceof EntityPig && targetEntities.getValue().get("Animals"))
                || (entity instanceof EntityVillager && targetEntities.getValue().get("Villagers"))
                || (entity instanceof EntityGolem && targetEntities.getValue().get("Golems"))) {
            if ((!prop_maxDistance.checkDependency() || (getPlayer().getDistanceToEntity(entity) <= (prop_maxDistance.getValue()))
                    //&& (!entity.isDead)
                    //&& ((entity).getHealth() > 0)
                    && (!middleClickIgnore.getValue() || !mcf.contains(entity)))) {
                return true;
            }

        }
        return false;
    }


    private static boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

    private void drawCircle(Entity entity, float partialTicks, double rad) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(1.0f);
        glBegin(GL_LINE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        final float r = ((float) 1 / 255) * Color.WHITE.getRed();
        final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
        final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

        final double pix2 = Math.PI * 2.0D;

        for (int i = 0; i <= 90; ++i) {
            glColor3f(r, g, b);
            glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y, z + rad * Math.sin(i * pix2 / 45.0));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }


    public void updateTargetList() {
        targetList.clear();

        getWorld().getLoadedEntityList().forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                if (isValidTarget((EntityLivingBase) entity)) {
                    targetList.add((EntityLivingBase) entity);
                } else {
                    targetList.remove((EntityLivingBase) entity);
                }

            }
        });

        if (targetList.size() > 1) {
            if (targetPriority.getValue().get("Lowest Health"))
                targetList.sort(Comparator.comparingDouble(EntityLivingBase::getHealth).reversed());
            else if (targetPriority.getValue().get("Least Armor"))
                targetList.sort(Comparator.comparingInt(EntityLivingBase::getTotalArmorValue));
            else if (targetPriority.getValue().get("Closest"))
                targetList.sort(Comparator.comparingDouble(UPlayer::getDistanceToEntity));
            if (prioritisePlayers.checkDependency() && prioritisePlayers.getValue()) {
                targetList.sort((e1, e2) -> Boolean.compare(e2 instanceof EntityPlayer, e1 instanceof EntityPlayer));
            }
        }
    }

    /*Things for autoblock*/
    public boolean holdingSword() {
        if (getPlayer().getCurrentEquippedItem() != null && getPlayer().inventory.getCurrentItem().getItem() instanceof ItemSword) {
            return true;
        }
        if (getPlayer().getCurrentEquippedItem() != null && getPlayer().inventory.getCurrentItem().getItem() instanceof ItemAxe) {
            return true;
        }
        return false;
    }

    public void attemptStartAutoblock_Watchdog() {
        if (fakeab.getValue()) {
            if (!targetList.isEmpty() && targetIndex <= targetList.size() - 1 && UPlayer.getDistanceToEntity(targetList.get(targetIndex)) <= prop_maxDistance.getValue() + (hvh.getValue() ? 2 : 0) && holdingSword() && !isBlocking) {
                if (abmode.getValue().get("Hypixel")) {
                    getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-.8, -.8, -.8), -1, getPlayer().getHeldItem(), 0, 0, 0));
                } else if (abmode.getValue().get("Fake")) {

                }
                isBlocking = true;
            }
        }
    }


    @Collect
    public void onPlayerJump(PlayerJumpEvent event) {

        if (Helium.instance.cheatManager.isCheatEnabled("Criticals") && groundTicks != 0 && getPlayer().isMoving()) {
            event.setCancelled(true);
            getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX, mc.thePlayer.posY, mc.thePlayer.posZ, yaw, pitch, true));
            mc.thePlayer.motionY = .42f;
            groundTicks = 0;
        } else {
            event.setCancelled(false);
        }
    }

    public void attemptStopAutoblock_Watchdog() {
        if (holdingSword() || fakeab.getValue()) {
            if (isBlocking) {
                if (abmode.getValue().get("Hypixel")) {
                    getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-.8, -.8, -.8), EnumFacing.DOWN));
                } else if (abmode.getValue().get("Fake")) {

                }
                isBlocking = false;
            }
        }
    }

    public void attemptStopAutoblockNoSet_Watchdog() {
        if (isBlocking || fakeab.getValue()) {
            if (abmode.getValue().get("Hypixel")) {
                //getPlayer().addChatMessage(new ChatComponentText("UnBlocc"));
                getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-.8, -.8, -.8), EnumFacing.DOWN));
            } else if (abmode.getValue().get("Fake")) {

            }
            isBlocking = false;

        }
    }

    public boolean shouldDoAutoblockAnim() {
        return isBlocking;
    }

    public static EntityLivingBase getCurrentTarget() {
        if (!mode.getValue().get("Multi")) {
            if (!targetList.isEmpty() && targetIndex != -1) {
                return targetList.get(targetIndex);
            } else {
                return null;
            }
        } else {
            if (currentEntity != null) { //This code is bad I know it. I'm just too lazy to fix it.
                if (!currentEntity.isDead) {
                    if (mc.thePlayer.getDistanceToEntity(currentEntity) < prop_maxDistance.getValue()) {
                        return currentEntity;
                    } else {
                        currentEntity = null;
                        return null;
                    }
                } else {
                    currentEntity = null;
                    return null;
                }
            } else {
                return null;
            }
        }

    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

    boolean didcrit;

    @Collect
    public void onMouseClick(MouseClickEvent mouseClickEvent) {
        if (mouseClickEvent.getMouseButton() == 2) {
            if (getMc().objectMouseOver != null && getMc().objectMouseOver.entityHit != null && getMc().objectMouseOver.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) getMc().objectMouseOver.entityHit;
                if (!mcf.contains(entity)) {
                    mcf.add(entity);
                } else {
                    mcf.remove(entity);
                }
            }
        }
    }


    @Collect
    public void onRender(RenderOverlayEvent e) {
        ScaledResolution rolf = new ScaledResolution(this.mc);

        float xNigga = (rolf.getScaledWidth() / 2) + 150;
        float yNigga = (rolf.getScaledHeight() / 2) + 120;
        if (Hud.targethud.getValue()) {
            if (Minecraft.getMinecraft().thePlayer != null && this.getCurrentTarget() instanceof EntityPlayer) {

                String playerName = "Name: " + StringUtils.stripControlCodes(getCurrentTarget().getName());
                int distance = (int) ((mc.thePlayer.getDistanceToEntity(getCurrentTarget())));
//				R2DUtils.drawERect(xNigga - 1, yNigga - 1, 142F, 44F, new Color(0, 0, 0, 150).getRGB());
                R2DUtils.drawERect(xNigga, yNigga, 140F, 40F, new Color(0, 0, 0, 90).getRGB());
                R2DUtils.drawERect(xNigga, yNigga + 40, 140, 2, new Color(0, 0, 0).getRGB());
                //if (getCurrentTarget().getName().length() > 15) playerName = "Name: LongNameNigga";
                mc.fontRendererObj.drawStringWithShadow(playerName, xNigga + 25.5F, yNigga + 4F, new Color(200, 200, 200, 255).getRGB());
                mc.fontRendererObj.drawStringWithShadow("Distance: " +Integer.toString(distance), xNigga + 25.5F, yNigga + 15F, new Color(200, 200, 200, 255).getRGB());
                mc.fontRendererObj.drawStringWithShadow("Armor: " + Math.round(getCurrentTarget().getTotalArmorValue()), xNigga + 25.5F, yNigga + 25F, new Color(200, 200, 200, 255).getRGB());
                //drawEntityOnScreen((int) xNigga + 12, (int) yNigga + 31,13, getCurrentTarget().rotationYaw, -getCurrentTarget().rotationPitch, getCurrentTarget());
                float xSpeed = 133f / (mc.debugFPS * 1.05f);
                float desiredWidth = (140F / getCurrentTarget().getMaxHealth()) * Math.min(getCurrentTarget().getHealth(), getCurrentTarget().getMaxHealth());
                if (desiredWidth < animated || desiredWidth > animated) {
                    if (Math.abs(desiredWidth - animated) <= xSpeed) {
                        animated = desiredWidth;
                    }else{
                        animated += (animated < desiredWidth ? xSpeed * 3 : -xSpeed);
                    }
                }
                R2DUtils.drawERect(xNigga, yNigga + 40F, animated, 2F, getHealthColor(getCurrentTarget()));


            }
        }
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f,1.0f,1.0f,255f);
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(100.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-170.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = mouseX;
        ent.rotationYaw = mouseX;
        ent.rotationPitch = -mouseY;
        ent.rotationYawHead = mouseX;
        ent.prevRotationYawHead = mouseX;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
//		 rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
//		 rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
//		 GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);



    }




    private int getHealthColor(final EntityLivingBase player) {
        final float f = player.getHealth();
        final float f2 = player.getMaxHealth();
        final float f3 = Math.max(0.0f, Math.min(f, f2) / f2);
        return Color.HSBtoRGB(f3 / 3.0f, 1.0f, 0.75f) | 0xFF000000;
    }



    @Collect
    public void onProcessPacket(ProcessPacketEvent processPacketEvent) {
        if (processPacketEvent.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) processPacketEvent.getPacket();
            if (packet.getYaw() == 0.0 && packet.getPitch() == 0.0 && packet.getX() == 0.0 && packet.getZ() == 0.0 && packet.getY() == 0.0) {
                packet.setYaw(getPlayer().rotationYaw);
                packet.setPitch(getPlayer().rotationPitch);
            }
        }
    }

    public void setDelay(int delay) {//set the delay to actually use aura
        auraDelay = delay;
    }

}