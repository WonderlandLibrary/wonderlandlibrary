// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import com.elementars.eclient.command.Command;
import com.elementars.eclient.Xulu;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import com.elementars.eclient.event.events.EventReceivePacket;
import net.minecraft.network.play.client.CPacketPlayer;
import com.elementars.eclient.event.events.EventSendPacket;
import java.util.Random;
import com.elementars.eclient.event.events.MotionEventPost;
import com.elementars.eclient.event.EventTarget;
import com.elementars.eclient.event.events.MotionEvent;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAppleGold;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.util.Plane;
import java.awt.Color;
import com.elementars.eclient.util.MathUtil;
import com.elementars.eclient.util.VectorUtils;
import com.elementars.eclient.util.RainbowUtils;
import net.minecraft.util.math.RayTraceResult;
import java.util.Iterator;
import com.elementars.eclient.util.TargetPlayers;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import com.elementars.eclient.enemy.Enemies;
import com.elementars.eclient.friend.Friends;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.init.MobEffects;
import com.elementars.eclient.guirewrite.elements.PvPInfo;
import java.util.Comparator;
import net.minecraft.init.Items;
import java.util.WeakHashMap;
import java.util.Collection;
import java.util.Arrays;
import com.elementars.eclient.util.ColorTextUtils;
import java.util.ArrayList;
import com.elementars.eclient.module.Category;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import dev.xulu.settings.Value;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import com.elementars.eclient.module.Module;

public class AutoCrystal extends Module
{
    private final List<BlockPos> placedCrystals;
    private BlockPos render;
    private BlockPos renderOld;
    private Entity renderEnt;
    private boolean switchCooldown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    private int waitCounter;
    private int placeCounter;
    EnumFacing f;
    public static boolean isRand;
    private final Value<Boolean> explode;
    private final Value<String> explodeMode;
    private final Value<Boolean> sync;
    private final Value<Boolean> spam;
    private final Value<Integer> hitAttempts;
    private final Value<Integer> hitRetryDelay;
    private final Value<Integer> waitTick;
    private final Value<Integer> placeTick;
    private final Value<Float> range;
    private final Value<Float> walls;
    private final Value<Float> ER;
    private final Value<Boolean> pre;
    private final Value<Boolean> antiWeakness;
    private final Value<Boolean> nodesync;
    private final Value<Boolean> place;
    private final Value<Boolean> oneHole;
    private final Value<Boolean> noSuicide;
    private final Value<Boolean> autoSwitch;
    private final Value<Float> placeRange;
    private final Value<Integer> minDmg;
    private final Value<Integer> facePlace;
    private final Value<Boolean> armor;
    private final Value<Integer> armorDmg;
    private final Value<Boolean> raytrace;
    private final Value<Boolean> rotate;
    private final Value<String> rotateMode;
    private final Value<Boolean> randRotations;
    private final Value<Boolean> fast;
    private final Value<String> fastType;
    private final Value<Boolean> toggleOff;
    private final Value<Integer> toggleHealth;
    private final Value<Boolean> chat;
    private final Value<Boolean> watermark;
    private final Value<String> echatcolor;
    private final Value<String> dchatcolor;
    private final Value<Boolean> renderDamage;
    private final Value<Boolean> damageWhite;
    private final Value<Boolean> renderBoolean;
    private final Value<String> rendermode;
    private final Value<Boolean> rainbow;
    private final Value<Integer> espR;
    private final Value<Integer> espG;
    private final Value<Integer> espB;
    private final Value<Integer> espA;
    private final Value<Integer> espF;
    private final Value<Integer> maxSelfDmg;
    private final Value<Boolean> noGappleSwitch;
    private final Value<Boolean> smoothEsp;
    private final Value<Integer> smoothSpeed;
    public boolean isActive;
    ConcurrentHashMap<BlockPos, Integer> fadeList;
    private Map<EntityEnderCrystal, Integer> attemptMap;
    private Map<EntityEnderCrystal, Integer> retryMap;
    private final List<Entity> ignoreList;
    private static float newYaw;
    private static float newPitch;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    
    public AutoCrystal() {
        super("AutoCrystal", "Xulu AutoCrystal", 0, Category.COMBAT, true);
        this.placedCrystals = new ArrayList<BlockPos>();
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.explode = this.register(new Value<Boolean>("Hit", this, true));
        this.explodeMode = this.register(new Value<String>("Hit Mode", this, "All", new String[] { "All", "OnlyOwn" }));
        this.sync = this.register(new Value<Boolean>("Sync Break", this, true));
        this.spam = this.register(new Value<Boolean>("Spam", this, true));
        this.hitAttempts = this.register(new Value<Integer>("Hit Attempts", this, 5, 0, 20));
        this.hitRetryDelay = this.register(new Value<Integer>("Retry Delay", this, 2, 0, 20));
        this.waitTick = this.register(new Value<Integer>("Tick Delay", this, 1, 0, 20));
        this.placeTick = this.register(new Value<Integer>("Place Delay", this, 1, 0, 20));
        this.range = this.register(new Value<Float>("Hit Range", this, 5.0f, 0.0f, 10.0f));
        this.walls = this.register(new Value<Float>("Walls Range", this, 3.5f, 0.0f, 10.0f));
        this.ER = this.register(new Value<Float>("Enemy Range", this, 5.0f, 0.0f, 10.0f));
        this.pre = this.register(new Value<Boolean>("Prioritize Enemies", this, false));
        this.antiWeakness = this.register(new Value<Boolean>("Anti Weakness", this, true));
        this.nodesync = this.register(new Value<Boolean>("No Desync", this, true));
        this.place = this.register(new Value<Boolean>("Place", this, true));
        this.oneHole = this.register(new Value<Boolean>("1.13 Mode", this, false));
        this.noSuicide = this.register(new Value<Boolean>("No Suicide", this, true));
        this.autoSwitch = this.register(new Value<Boolean>("Auto Switch", this, true));
        this.placeRange = this.register(new Value<Float>("Place Range", this, 5.0f, 0.0f, 10.0f));
        this.minDmg = this.register(new Value<Integer>("Min Damage", this, 5, 0, 40));
        this.facePlace = this.register(new Value<Integer>("Faceplace HP", this, 6, 0, 40));
        this.armor = this.register(new Value<Boolean>("Armor Place", this, true));
        this.armorDmg = this.register(new Value<Integer>("Armor %", this, 15, 0, 100));
        this.raytrace = this.register(new Value<Boolean>("Raytrace", this, false));
        this.rotate = this.register(new Value<Boolean>("Rotate", this, true));
        this.rotateMode = this.register(new Value<String>("Rot. Mode", this, "New", new String[] { "Old", "New" })).visibleWhen(string -> this.rotate.getValue());
        this.randRotations = this.register(new Value<Boolean>("Random Rotations", this, true));
        this.fast = this.register(new Value<Boolean>("Fast Mode", this, false));
        this.fastType = this.register(new Value<String>("Fast Type", this, "Instant", new String[] { "Instant", "Ignore" }));
        this.toggleOff = this.register(new Value<Boolean>("Toggle Off", this, false));
        this.toggleHealth = this.register(new Value<Integer>("Toggle Off Health", this, 10, 0, 20));
        this.chat = this.register(new Value<Boolean>("Toggle Msgs", this, true));
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.echatcolor = this.register(new Value<String>("Enable Color", this, "White", ColorTextUtils.colors));
        this.dchatcolor = this.register(new Value<String>("Disable Color", this, "White", ColorTextUtils.colors));
        this.renderDamage = this.register(new Value<Boolean>("Render Damage", this, false));
        this.damageWhite = this.register(new Value<Boolean>("Damage Color White", this, false));
        this.renderBoolean = this.register(new Value<Boolean>("Render", this, true));
        this.rendermode = this.register(new Value<String>("RenderMode", this, "Solid", new ArrayList<String>(Arrays.asList("Solid", "Outline", "Full"))));
        this.rainbow = this.register(new Value<Boolean>("Esp Rainbow", this, false));
        this.espR = this.register(new Value<Integer>("Esp Red", this, 200, 0, 255));
        this.espG = this.register(new Value<Integer>("Esp Green", this, 50, 0, 255));
        this.espB = this.register(new Value<Integer>("Esp Blue", this, 200, 0, 255));
        this.espA = this.register(new Value<Integer>("Esp Alpha", this, 50, 0, 255));
        this.espF = this.register(new Value<Integer>("Full Alpha", this, 80, 0, 255));
        this.maxSelfDmg = this.register(new Value<Integer>("Max Self Dmg", this, 10, 0, 36));
        this.noGappleSwitch = this.register(new Value<Boolean>("No Gap Switch", this, false));
        this.smoothEsp = this.register(new Value<Boolean>("Smooth ESP", this, false));
        this.smoothSpeed = this.register(new Value<Integer>("Smooth Speed", this, 5, 1, 20));
        this.isActive = false;
        this.fadeList = new ConcurrentHashMap<BlockPos, Integer>();
        this.attemptMap = new WeakHashMap<EntityEnderCrystal, Integer>();
        this.retryMap = new WeakHashMap<EntityEnderCrystal, Integer>();
        this.ignoreList = new ArrayList<Entity>();
    }
    
    @Override
    public void onUpdate() {
        if (this.rotateMode.getValue().equalsIgnoreCase("Old")) {
            this.doAutoCrystal();
        }
    }
    
    private void doAutoCrystal() {
        AutoCrystal.isRand = this.randRotations.getValue();
        this.isActive = false;
        if (AutoCrystal.mc.player == null || AutoCrystal.mc.player.isDead) {
            return;
        }
        if (this.shouldPause()) {
            resetRotation();
            return;
        }
        if (AutoCrystal.mc.player.getHealth() <= this.toggleHealth.getValue() && this.toggleOff.getValue()) {
            this.toggle();
        }
        if (this.fast.getValue() && this.waitTick.getValue() > 0) {
            if (this.waitCounter < this.waitTick.getValue()) {
                ++this.waitCounter;
            }
            else {
                this.waitCounter = 0;
            }
        }
        this.fadeList.forEach((pos, alpha) -> {
            if (alpha <= 0) {
                this.fadeList.remove(pos);
            }
            else {
                this.fadeList.put(pos, alpha - this.smoothSpeed.getValue());
            }
            return;
        });
        int crystalSlot = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystal.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (AutoCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        this.retryMap.forEach((entityEnderCrystal, integer) -> {
            if (entityEnderCrystal.isDead) {
                this.retryMap.remove(entityEnderCrystal);
            }
            else if (this.retryMap.get(entityEnderCrystal) != 0) {
                this.retryMap.put(entityEnderCrystal, integer - 1);
            }
            return;
        });
        EntityEnderCrystal crystal = null;
        final List<EntityEnderCrystal> crystals = new ArrayList<EntityEnderCrystal>();
        for (final Entity e2 : AutoCrystal.mc.world.loadedEntityList) {
            if (e2 instanceof EntityEnderCrystal) {
                final EntityEnderCrystal ec = (EntityEnderCrystal)e2;
                if (AutoCrystal.mc.player.getDistance((Entity)ec) > this.range.getValue()) {
                    continue;
                }
                if (!this.checkCrystal((Entity)ec)) {
                    continue;
                }
                if (this.attemptMap.containsKey(ec) && this.attemptMap.get(ec).equals(this.hitAttempts.getValue())) {
                    if (this.retryMap.containsKey(ec) && this.retryMap.get(ec) == 0) {
                        this.attemptMap.put(ec, 0);
                        this.retryMap.remove(ec);
                    }
                    else {
                        if (!this.retryMap.containsKey(ec)) {
                            this.retryMap.put(ec, this.hitRetryDelay.getValue());
                            continue;
                        }
                        continue;
                    }
                }
                crystals.add(ec);
            }
        }
        if (!crystals.isEmpty()) {
            crystals.sort(Comparator.comparing(c -> AutoCrystal.mc.player.getDistance(c)));
            crystal = crystals.get(0);
        }
        if (this.explode.getValue() && crystal != null && (!this.fast.getValue() || this.waitCounter == 0)) {
            if (!AutoCrystal.mc.player.canEntityBeSeen((Entity)crystal) && AutoCrystal.mc.player.getDistance((Entity)crystal) > this.walls.getValue()) {
                PvPInfo.attack = false;
                return;
            }
            if (!this.fast.getValue() && this.waitTick.getValue() > 0) {
                if (this.waitCounter < this.waitTick.getValue()) {
                    ++this.waitCounter;
                    PvPInfo.attack = false;
                    return;
                }
                this.waitCounter = 0;
            }
            if (this.antiWeakness.getValue() && AutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                if (!this.isAttacking) {
                    this.oldSlot = AutoCrystal.mc.player.inventory.currentItem;
                    PvPInfo.attack = true;
                    this.isAttacking = true;
                }
                this.newSlot = -1;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack stack = AutoCrystal.mc.player.inventory.getStackInSlot(i);
                    if (stack != ItemStack.EMPTY) {
                        if (stack.getItem() instanceof ItemSword) {
                            this.newSlot = i;
                            break;
                        }
                        if (stack.getItem() instanceof ItemTool) {
                            this.newSlot = i;
                            break;
                        }
                    }
                }
                if (this.newSlot != -1) {
                    AutoCrystal.mc.player.inventory.currentItem = this.newSlot;
                    this.switchCooldown = true;
                }
            }
            PvPInfo.attack = true;
            this.isActive = true;
            if (this.rotate.getValue()) {
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AutoCrystal.mc.player);
            }
            if (this.spam.getValue()) {
                for (int i = 0; i < 50; ++i) {
                    if (this.sync.getValue()) {
                        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity((Entity)crystal));
                    }
                    else {
                        AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)crystal);
                    }
                }
            }
            else if (this.sync.getValue()) {
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity((Entity)crystal));
            }
            else {
                AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)crystal);
            }
            AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            this.isActive = false;
            if (this.fast.getValue()) {
                final String s = this.fastType.getValue();
                switch (s) {
                    case "Instant": {
                        crystal.setDead();
                        break;
                    }
                    case "Ignore": {
                        this.ignoreList.add((Entity)crystal);
                        break;
                    }
                }
            }
            if (this.attemptMap.containsKey(crystal)) {
                this.attemptMap.put(crystal, this.attemptMap.get(crystal) + 1);
            }
            else {
                this.attemptMap.put(crystal, 1);
            }
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                AutoCrystal.mc.player.inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
            this.isActive = false;
            if (this.placeTick.getValue() > 0) {
                if (this.placeCounter < this.placeTick.getValue()) {
                    ++this.placeCounter;
                    PvPInfo.place = false;
                    return;
                }
                this.placeCounter = 0;
            }
            boolean offhand = false;
            if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                offhand = true;
            }
            else if (crystalSlot == -1) {
                return;
            }
            final List<BlockPos> blocks = this.findCrystalBlocks();
            final List<Entity> entities = new ArrayList<Entity>();
            final List<Entity> enemies = new ArrayList<Entity>();
            for (final EntityPlayer player : AutoCrystal.mc.world.playerEntities) {
                if (Friends.isFriend(player.getName())) {
                    continue;
                }
                entities.add((Entity)player);
            }
            entities.sort(Comparator.comparing(e -> AutoCrystal.mc.player.getDistance(e)));
            entities.removeIf(entity -> AutoCrystal.mc.player.getDistance(entity) > this.ER.getValue());
            if (this.pre.getValue()) {
                for (final EntityPlayer player : AutoCrystal.mc.world.playerEntities) {
                    if (!Friends.isFriend(player.getName())) {
                        if (!Enemies.isEnemy(player.getName())) {
                            continue;
                        }
                        enemies.add((Entity)player);
                    }
                }
                enemies.sort(Comparator.comparing(e -> AutoCrystal.mc.player.getDistance(e)));
                enemies.removeIf(entity -> AutoCrystal.mc.player.getDistance(entity) > this.ER.getValue());
                if (!enemies.isEmpty()) {
                    entities.clear();
                    entities.addAll(enemies);
                }
            }
            BlockPos q = null;
            double damage = 0.5;
            final double dist = 6.9696969E7;
            for (final Entity entity2 : entities) {
                if (entity2 == AutoCrystal.mc.player) {
                    continue;
                }
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f || entity2.isDead) {
                    continue;
                }
                if (AutoCrystal.mc.player == null) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    final double b = entity2.getDistanceSq(blockPos);
                    if (!entity2.isDead) {
                        if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                            continue;
                        }
                        if (b >= 169.0) {
                            continue;
                        }
                        final double d = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, entity2);
                        if (d < this.minDmg.getValue() && ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() > ((AutoTotem.isFullArmor((EntityPlayer)entity2) && !this.isArmorLow((EntityPlayer)entity2)) ? this.facePlace.getValue() : 36)) {
                            continue;
                        }
                        if (d <= damage) {
                            continue;
                        }
                        final double self = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, (Entity)AutoCrystal.mc.player);
                        if (self > d && d >= ((EntityLivingBase)entity2).getHealth()) {
                            continue;
                        }
                        if (self - 0.5 > AutoCrystal.mc.player.getHealth()) {
                            continue;
                        }
                        if (self > this.maxSelfDmg.getValue()) {
                            continue;
                        }
                        damage = d;
                        q = blockPos;
                        this.renderEnt = entity2;
                    }
                }
            }
            if (damage == 0.5) {
                this.render = null;
                this.renderEnt = null;
                resetRotation();
                return;
            }
            this.render = q;
            if (this.place.getValue()) {
                if (AutoCrystal.mc.player == null) {
                    PvPInfo.place = false;
                    return;
                }
                this.isActive = true;
                if (this.rotate.getValue()) {
                    this.lookAtPacket(q.getX() + 0.5, q.getY() - 0.5, q.getZ() + 0.5, (EntityPlayer)AutoCrystal.mc.player);
                }
                final RayTraceResult result = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(q.getX() + 0.5, q.getY() - 0.5, q.getZ() + 0.5));
                if (this.raytrace.getValue()) {
                    if (result == null || result.sideHit == null) {
                        q = null;
                        this.f = null;
                        this.render = null;
                        resetRotation();
                        this.isActive = false;
                        PvPInfo.place = false;
                        return;
                    }
                    this.f = result.sideHit;
                }
                if (!offhand && AutoCrystal.mc.player.inventory.currentItem != crystalSlot) {
                    if (this.autoSwitch.getValue()) {
                        if (this.noGappleSwitch.getValue() && this.isEatingGap()) {
                            this.isActive = false;
                            resetRotation();
                            PvPInfo.place = false;
                            return;
                        }
                        this.isActive = true;
                        AutoCrystal.mc.player.inventory.currentItem = crystalSlot;
                        resetRotation();
                        this.switchCooldown = true;
                    }
                    PvPInfo.place = false;
                    return;
                }
                if (this.switchCooldown) {
                    this.switchCooldown = false;
                    PvPInfo.place = false;
                    return;
                }
                if (q != null && AutoCrystal.mc.player != null) {
                    PvPInfo.place = true;
                    this.isActive = true;
                    if (this.raytrace.getValue() && this.f != null) {
                        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, this.f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                    else {
                        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                    TargetPlayers.addTargetedPlayer(this.renderEnt.getName());
                }
                this.isActive = false;
            }
        }
    }
    
    @Override
    public void onRender() {
        if (this.render == null || AutoCrystal.mc.player == null) {
            return;
        }
        if (!this.renderBoolean.getValue()) {
            return;
        }
        if (this.renderDamage.getValue()) {
            int color1 = this.espR.getValue();
            int color2 = this.espG.getValue();
            int color3 = this.espB.getValue();
            if (this.rainbow.getValue()) {
                color1 = RainbowUtils.r;
                color2 = RainbowUtils.g;
                color3 = RainbowUtils.b;
            }
            final Plane pos = VectorUtils.toScreen(this.render.getX() + 0.5, this.render.getY() + 0.5, this.render.getZ() + 0.5);
            final float damage = calculateDamage(this.render.getX() + 0.5, this.render.getY() + 1, this.render.getZ() + 0.5, this.renderEnt);
            final String text = String.valueOf(MathUtil.round(damage, 1));
            AutoCrystal.fontRenderer.drawStringWithShadow(text, (float)pos.getX() - AutoCrystal.fontRenderer.getStringWidth(text) / 2, (float)pos.getY() - AutoCrystal.fontRenderer.FONT_HEIGHT / 2, ((boolean)this.damageWhite.getValue()) ? -1 : new Color(color1, color2, color3).getRGB());
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render == null || AutoCrystal.mc.player == null) {
            return;
        }
        if (!this.renderBoolean.getValue()) {
            return;
        }
        if (this.renderOld != null && this.renderOld != this.render) {
            this.fadeList.put(this.renderOld, this.espA.getValue());
        }
        int color1 = this.espR.getValue();
        int color2 = this.espG.getValue();
        int color3 = this.espB.getValue();
        if (this.rainbow.getValue()) {
            color1 = RainbowUtils.r;
            color2 = RainbowUtils.g;
            color3 = RainbowUtils.b;
        }
        if (this.rendermode.getValue().equalsIgnoreCase("Solid")) {
            XuluTessellator.prepare(7);
            XuluTessellator.drawBox(this.render, color1, color2, color3, this.espA.getValue(), 63);
            XuluTessellator.release();
        }
        else if (this.rendermode.getValue().equalsIgnoreCase("Outline")) {
            final IBlockState iBlockState2 = AutoCrystal.mc.world.getBlockState(this.render);
            final Vec3d interp2 = MathUtil.interpolateEntity((Entity)AutoCrystal.mc.player, AutoCrystal.mc.getRenderPartialTicks());
            XuluTessellator.drawBoundingBox(iBlockState2.getSelectedBoundingBox((World)AutoCrystal.mc.world, this.render).grow(0.0020000000949949026).offset(-interp2.x, -interp2.y, -interp2.z), 1.5f, color1, color2, color3, this.espA.getValue());
        }
        else if (this.rendermode.getValue().equalsIgnoreCase("Full")) {
            final IBlockState iBlockState3 = AutoCrystal.mc.world.getBlockState(this.render);
            final Vec3d interp3 = MathUtil.interpolateEntity((Entity)AutoCrystal.mc.player, AutoCrystal.mc.getRenderPartialTicks());
            XuluTessellator.drawFullBox(iBlockState3.getSelectedBoundingBox((World)AutoCrystal.mc.world, this.render).grow(0.0020000000949949026).offset(-interp3.x, -interp3.y, -interp3.z), this.render, 1.5f, color1, color2, color3, this.espA.getValue(), this.espF.getValue());
        }
        if (this.smoothEsp.getValue()) {
            for (final BlockPos pos : this.fadeList.keySet()) {
                if (this.fadeList.get(pos) < 0) {
                    this.fadeList.remove(pos);
                }
                else if (this.rendermode.getValue().equalsIgnoreCase("Solid")) {
                    XuluTessellator.prepare(7);
                    XuluTessellator.drawBox(pos, color1, color2, color3, this.fadeList.get(pos), 63);
                    XuluTessellator.release();
                }
                else if (this.rendermode.getValue().equalsIgnoreCase("Outline")) {
                    final IBlockState iBlockState4 = AutoCrystal.mc.world.getBlockState(pos);
                    final Vec3d interp4 = MathUtil.interpolateEntity((Entity)AutoCrystal.mc.player, AutoCrystal.mc.getRenderPartialTicks());
                    XuluTessellator.drawBoundingBox(iBlockState4.getSelectedBoundingBox((World)AutoCrystal.mc.world, pos).grow(0.0020000000949949026).offset(-interp4.x, -interp4.y, -interp4.z), 1.5f, color1, color2, color3, this.fadeList.get(pos));
                }
                else {
                    if (!this.rendermode.getValue().equalsIgnoreCase("Full")) {
                        continue;
                    }
                    final IBlockState iBlockState5 = AutoCrystal.mc.world.getBlockState(pos);
                    final Vec3d interp5 = MathUtil.interpolateEntity((Entity)AutoCrystal.mc.player, AutoCrystal.mc.getRenderPartialTicks());
                    XuluTessellator.drawFullBox(iBlockState5.getSelectedBoundingBox((World)AutoCrystal.mc.world, pos).grow(0.0020000000949949026).offset(-interp5.x, -interp5.y, -interp5.z), this.render, 1.5f, color1, color2, color3, this.fadeList.get(pos), this.espF.getValue());
                }
            }
        }
        this.renderOld = this.render;
    }
    
    @Override
    public String getHudInfo() {
        if (this.renderEnt == null) {
            return null;
        }
        if (AutoCrystal.mc.player.getDistance(this.renderEnt) <= this.range.getValue()) {
            return ChatFormatting.GREEN + this.renderEnt.getName();
        }
        return ChatFormatting.RED + this.renderEnt.getName();
    }
    
    private boolean checkCrystal(final Entity e) {
        if (e == null) {
            return false;
        }
        if (this.noSuicide.getValue()) {
            final double self = calculateDamage(e.posX, e.posY, e.posZ, (Entity)AutoCrystal.mc.player);
            if (self - 0.5 > AutoCrystal.mc.player.getHealth()) {
                return false;
            }
        }
        final String s = this.explodeMode.getValue();
        switch (s) {
            case "OnlyOwn": {
                if (this.render != null && this.render.getDistance((int)e.posX, (int)e.posY, (int)e.posZ) <= 3.0) {
                    return true;
                }
                if (!this.placedCrystals.isEmpty()) {
                    synchronized (this.placedCrystals) {
                        try {
                            for (final BlockPos pos : this.placedCrystals) {
                                if (pos.getDistance((int)e.posX, (int)e.posY, (int)e.posZ) <= 3.0) {
                                    return true;
                                }
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;
                }
                break;
            }
            case "All": {
                return true;
            }
        }
        return false;
    }
    
    private boolean isArmorLow(final EntityPlayer player) {
        if (!this.armor.getValue()) {
            return false;
        }
        for (final ItemStack stack : player.getArmorInventoryList()) {
            final float green = (stack.getMaxDamage() - (float)stack.getItemDamage()) / stack.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            if (dmg <= this.armorDmg.getValue()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isEatingGap() {
        return AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && AutoCrystal.mc.player.isHandActive();
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && (this.oneHole.getValue() || AutoCrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR) && this.isTrulyEmpty(AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost))) && this.isTrulyEmpty(AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)));
    }
    
    private boolean isTrulyEmpty(final List<Entity> list) {
        if (list.isEmpty()) {
            return true;
        }
        boolean isEmpty = true;
        for (final Entity e : list) {
            if (!this.ignoreList.contains(e)) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoCrystal.mc.player.posX), Math.floor(AutoCrystal.mc.player.posY), Math.floor(AutoCrystal.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.placeRange.getValue(), this.placeRange.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    public static void updateRotations() {
        AutoCrystal.newYaw = AutoCrystal.mc.player.rotationYaw;
        AutoCrystal.newPitch = AutoCrystal.mc.player.rotationPitch;
    }
    
    public static void restoreRotations() {
        AutoCrystal.mc.player.rotationYaw = AutoCrystal.newYaw;
        AutoCrystal.mc.player.rotationYawHead = AutoCrystal.newYaw;
        AutoCrystal.mc.player.rotationPitch = AutoCrystal.newPitch;
        resetRotation();
    }
    
    public static void setPlayerRotations(final float yaw, final float pitch) {
        AutoCrystal.mc.player.rotationYaw = yaw;
        AutoCrystal.mc.player.rotationYawHead = yaw;
        AutoCrystal.mc.player.rotationPitch = pitch;
    }
    
    @EventTarget
    public void onMotionPre(final MotionEvent event) {
        if (this.rotate.getValue() && this.rotateMode.getValue().equalsIgnoreCase("New")) {
            updateRotations();
            this.doAutoCrystal();
        }
    }
    
    @EventTarget
    public void onMotionPost(final MotionEventPost event) {
        if (this.rotate.getValue() && this.rotateMode.getValue().equalsIgnoreCase("New")) {
            restoreRotations();
        }
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        final Random rand = new Random(2L);
        AutoCrystal.yaw = yaw1 + (AutoCrystal.isRand ? (rand.nextFloat() / 100.0f) : 0.0f);
        AutoCrystal.pitch = pitch1 + (AutoCrystal.isRand ? (rand.nextFloat() / 100.0f) : 0.0f);
        AutoCrystal.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AutoCrystal.isSpoofingAngles) {
            AutoCrystal.yaw = AutoCrystal.mc.player.rotationYaw;
            AutoCrystal.pitch = AutoCrystal.mc.player.rotationPitch;
            AutoCrystal.isSpoofingAngles = false;
        }
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }
    
    @EventTarget
    public void onSend(final EventSendPacket event) {
        final Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && this.rotate.getValue() && AutoCrystal.isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)AutoCrystal.yaw;
            ((CPacketPlayer)packet).pitch = (float)AutoCrystal.pitch;
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock cpacket = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            if (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                synchronized (this.placedCrystals) {
                    this.placedCrystals.add(cpacket.getPos());
                }
            }
        }
    }
    
    @EventTarget
    public void onRecieve(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketSoundEffect && this.nodesync.getValue()) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0) {
                            e.setDead();
                        }
                        final SPacketSoundEffect sPacketSoundEffect;
                        this.placedCrystals.removeIf(p_Pos -> p_Pos.getDistance((int)sPacketSoundEffect.getX(), (int)sPacketSoundEffect.getY(), (int)sPacketSoundEffect.getZ()) <= 6.0);
                    }
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        Xulu.EVENT_MANAGER.register(this);
        this.isActive = false;
        if (this.chat.getValue() && AutoCrystal.mc.player != null) {
            if (this.watermark.getValue()) {
                Command.sendChatMessage(ColorTextUtils.getColor(this.echatcolor.getValue()) + "AutoCrystal ON");
            }
            else {
                Command.sendRawChatMessage(ColorTextUtils.getColor(this.echatcolor.getValue()) + "AutoCrystal ON");
            }
        }
    }
    
    @Override
    public void onDisable() {
        Xulu.EVENT_MANAGER.unregister(this);
        PvPInfo.place = false;
        PvPInfo.attack = false;
        this.render = null;
        this.renderEnt = null;
        resetRotation();
        this.isActive = false;
        this.ignoreList.clear();
        this.attemptMap.clear();
        this.retryMap.clear();
        if (this.chat.getValue()) {
            if (this.watermark.getValue()) {
                Command.sendChatMessage(ColorTextUtils.getColor(this.dchatcolor.getValue()) + "AutoCrystal OFF");
            }
            else {
                Command.sendRawChatMessage(ColorTextUtils.getColor(this.dchatcolor.getValue()) + "AutoCrystal OFF");
            }
        }
    }
    
    private boolean shouldPause() {
        return (Xulu.MODULE_MANAGER.getModule(Surround.class).isToggled() && Surround.isExposed() && Xulu.MODULE_MANAGER.getModuleT(Surround.class).findObiInHotbar() != -1) || Xulu.MODULE_MANAGER.getModule(AutoTrap.class).isToggled() || Xulu.MODULE_MANAGER.getModule(HoleFill.class).isToggled() || (Xulu.MODULE_MANAGER.getModule(HoleBlocker.class).isToggled() && HoleBlocker.isExposed() && Xulu.MODULE_MANAGER.getModuleT(Surround.class).findObiInHotbar() != -1);
    }
}
