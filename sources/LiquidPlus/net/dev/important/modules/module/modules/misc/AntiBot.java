/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S0BPacketAnimation
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S38PacketPlayerListItem
 *  net.minecraft.network.play.server.S38PacketPlayerListItem$Action
 *  net.minecraft.network.play.server.S38PacketPlayerListItem$AddPlayerData
 *  net.minecraft.world.World
 */
package net.dev.important.modules.module.modules.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dev.important.Client;
import net.dev.important.event.AttackEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.TimerUtil;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.world.World;
import oh.yalan.NativeClass;

@NativeClass
@Info(name="AntiBot", description="Prevents KillAura from attacking AntiCheat bots.", category=Category.MISC, cnName="\u53cd\u5047\u4eba")
public final class AntiBot
extends Module {
    public static final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "AntiCheat", "Hypixel", "Mineplex", "Syuu", "TestMatrix", "Matrix"}, "Normal");
    private final BoolValue tabValue = new BoolValue("Tab", true);
    private final ListValue tabModeValue = new ListValue("TabMode", new String[]{"Equals", "Contains"}, "Contains");
    private final BoolValue entityIDValue = new BoolValue("EntityID", true);
    private final BoolValue colorValue = new BoolValue("Color", false);
    private final BoolValue livingTimeValue = new BoolValue("LivingTime", false);
    private final IntegerValue livingTimeTicksValue = new IntegerValue("LivingTimeTicks", 40, 1, 200);
    private final BoolValue groundValue = new BoolValue("Ground", true);
    private final BoolValue airValue = new BoolValue("Air", false);
    private final BoolValue invalidGroundValue = new BoolValue("InvalidGround", true);
    private final BoolValue swingValue = new BoolValue("Swing", false);
    private final BoolValue healthValue = new BoolValue("Health", false);
    private final BoolValue derpValue = new BoolValue("Derp", true);
    private final BoolValue wasInvisibleValue = new BoolValue("WasInvisible", false);
    private final BoolValue armorValue = new BoolValue("Armor", false);
    private final BoolValue pingValue = new BoolValue("Ping", false);
    private final BoolValue needHitValue = new BoolValue("NeedHit", false);
    private final BoolValue duplicateInWorldValue = new BoolValue("DuplicateInWorld", false);
    private final BoolValue duplicateInTabValue = new BoolValue("DuplicateInTab", false);
    private final List<Integer> ground = new ArrayList<Integer>();
    private final List<Integer> air = new ArrayList<Integer>();
    private final Map<Integer, Integer> invalidGround = new HashMap<Integer, Integer>();
    private final List<Integer> swing = new ArrayList<Integer>();
    private final List<Integer> invisible = new ArrayList<Integer>();
    private final List<Integer> hitted = new ArrayList<Integer>();
    private final List<EntityPlayer> removedBots = new ArrayList<EntityPlayer>();
    private final TimerUtil sendTimer = new TimerUtil();
    private int groundTicks = 0;
    private int matrixTicks = 0;

    @Override
    public void onDisable() {
        this.matrixTicks = 0;
        this.removedBots.clear();
        this.groundTicks = 0;
        this.clearAll();
        super.onDisable();
    }

    @Override
    public final String getTag() {
        return (String)modeValue.get();
    }

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (this.sendTimer.hasReached(3000.0)) {
            if (this.removedBots.size() != 0) {
                ClientUtils.displayChatMessage("\u00a77[\u00a79BotKiller\u00a77] \u00a79Removed " + this.removedBots.size() + " Bots");
            }
            this.removedBots.clear();
            this.sendTimer.reset();
        }
        switch ((String)modeValue.get()) {
            case "Mineplex": {
                for (EntityPlayer e : AntiBot.mc.field_71441_e.field_73010_i) {
                    if (e == null || e.field_70173_aa >= 2 || !(e.func_110143_aJ() < 20.0f) || !(e.func_110143_aJ() > 0.0f) || e == AntiBot.mc.field_71439_g) continue;
                    this.removedBots.add(e);
                    AntiBot.mc.field_71441_e.func_72900_e((Entity)e);
                }
                break;
            }
            case "Hypixel": {
                for (EntityPlayer i : AntiBot.mc.field_71441_e.field_73010_i) {
                    if (i == null || i == AntiBot.mc.field_71439_g || !i.func_70005_c_().contains("\u00a7") && (!i.func_145818_k_() || !i.func_95999_t().contains(i.func_70005_c_()))) continue;
                    this.removedBots.add(i);
                    AntiBot.mc.field_71441_e.func_72900_e((Entity)i);
                }
                break;
            }
            case "Syuu": {
                for (EntityPlayer i : AntiBot.mc.field_71441_e.field_73010_i) {
                    ItemStack[] stacks = i.field_71071_by.field_70460_b;
                    if (i.func_110143_aJ() != 3838.0f || !(stacks[1].func_77973_b() instanceof ItemFood)) continue;
                    ClientUtils.displayChatMessage("\u00a77[\u00a79BotKiller\u00a77] \u00a79Removed a bot: \u00a74" + i.func_70005_c_() + " \u00a79Coords \u00a7bX:\u00a7f" + Math.round(i.field_70165_t) + "\u00a7bY:\u00a7f" + Math.round(i.field_70163_u) + "\u00a7bZ:\u00a7f" + Math.round(i.field_70161_v));
                    AntiBot.mc.field_71441_e.func_72900_e((Entity)i);
                }
                break;
            }
            case "TestMatrix": {
                for (EntityPlayer i : AntiBot.mc.field_71441_e.field_73010_i) {
                    if (i == null || i.field_70143_R != 0.0f || i.field_70122_E || i == AntiBot.mc.field_71439_g) continue;
                    ++this.groundTicks;
                    if (this.groundTicks <= 20) continue;
                    ClientUtils.displayChatMessage("\u00a77[\u00a79BotKiller\u00a77] \u00a79Removed a bot: \u00a74" + i.func_70005_c_() + " \u00a79Coords \u00a7bX:\u00a7f" + Math.round(i.field_70165_t) + "\u00a7bY:\u00a7f" + Math.round(i.field_70163_u) + "\u00a7bZ:\u00a7f" + Math.round(i.field_70161_v));
                    AntiBot.mc.field_71441_e.func_72900_e((Entity)i);
                    this.groundTicks = 0;
                }
                break;
            }
            case "Matrix": {
                ++this.matrixTicks;
                for (EntityPlayer i : AntiBot.mc.field_71441_e.field_73010_i) {
                    double zDiff;
                    double xDiff;
                    double oldPosX = i.field_70165_t;
                    double oldPosZ = i.field_70161_v;
                    if (this.matrixTicks <= 15 || !(Math.sqrt((xDiff = oldPosX - i.field_70165_t) * xDiff + (zDiff = oldPosZ - i.field_70161_v) * zDiff) > 4.7) || !(i.field_70163_u > AntiBot.mc.field_71439_g.field_70163_u - 1.5) || !(i.field_70163_u < AntiBot.mc.field_71439_g.field_70163_u + 1.5) || !(AntiBot.mc.field_71439_g.func_70032_d((Entity)i) < 6.0f) || i == AntiBot.mc.field_71439_g) continue;
                    ClientUtils.displayChatMessage("\u00a77[\u00a79BotKiller\u00a77] \u00a79Removed a bot: \u00a74" + i.func_70005_c_() + " \u00a79Coords \u00a7bX:\u00a7f" + Math.round(i.field_70165_t) + "\u00a7bY:\u00a7f" + Math.round(i.field_70163_u) + "\u00a7bZ:\u00a7f" + Math.round(i.field_70161_v));
                    AntiBot.mc.field_71441_e.func_72900_e((Entity)i);
                }
                break;
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (AntiBot.mc.field_71439_g == null || AntiBot.mc.field_71441_e == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (((String)modeValue.get()).equals("AntiCheat") && packet instanceof S38PacketPlayerListItem && ((S38PacketPlayerListItem)packet).func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
            for (Entity i : AntiBot.mc.field_71441_e.field_72996_f) {
                EntityLivingBase entityLivingBase = (EntityLivingBase)i;
                for (S38PacketPlayerListItem.AddPlayerData j : ((S38PacketPlayerListItem)packet).func_179767_a()) {
                    if (entityLivingBase instanceof EntityPlayerSP || !j.func_179962_a().getName().equalsIgnoreCase(entityLivingBase.func_70005_c_()) || j.func_179962_a().getId() == entityLivingBase.func_110124_au() && j.func_179962_a().getId() == entityLivingBase.getPersistentID()) continue;
                    ClientUtils.displayChatMessage("\u00a77[\u00a79BotKiller\u00a77] \u00a79Removed a bot: \u00a74" + j.func_179962_a().getName());
                    event.cancelEvent();
                }
            }
        }
        if (((String)modeValue.get()).equals("Normal")) {
            S0BPacketAnimation packetAnimation;
            S14PacketEntity packetEntity;
            Entity entity;
            if (packet instanceof S14PacketEntity && (entity = (packetEntity = (S14PacketEntity)event.getPacket()).func_149065_a((World)AntiBot.mc.field_71441_e)) instanceof EntityPlayer) {
                if (packetEntity.func_179742_g() && !this.ground.contains(entity.func_145782_y())) {
                    this.ground.add(entity.func_145782_y());
                }
                if (!packetEntity.func_179742_g() && !this.air.contains(entity.func_145782_y())) {
                    this.air.add(entity.func_145782_y());
                }
                if (packetEntity.func_179742_g()) {
                    if (entity.field_70167_r != entity.field_70163_u) {
                        this.invalidGround.put(entity.func_145782_y(), this.invalidGround.getOrDefault(entity.func_145782_y(), 0) + 1);
                    }
                } else {
                    int currentVL = this.invalidGround.getOrDefault(entity.func_145782_y(), 0) / 2;
                    if (currentVL <= 0) {
                        this.invalidGround.remove(entity.func_145782_y());
                    } else {
                        this.invalidGround.put(entity.func_145782_y(), currentVL);
                    }
                }
                if (entity.func_82150_aj() && !this.invisible.contains(entity.func_145782_y())) {
                    this.invisible.add(entity.func_145782_y());
                }
            }
            if (packet instanceof S0BPacketAnimation && (entity = AntiBot.mc.field_71441_e.func_73045_a((packetAnimation = (S0BPacketAnimation)event.getPacket()).func_148978_c())) instanceof EntityLivingBase && packetAnimation.func_148977_d() == 0 && !this.swing.contains(entity.func_145782_y())) {
                this.swing.add(entity.func_145782_y());
            }
        }
    }

    @EventTarget
    public void onAttack(AttackEvent e) {
        Entity entity;
        if (((String)modeValue.get()).equals("Normal") && (entity = e.getTargetEntity()) instanceof EntityLivingBase && !this.hitted.contains(entity.func_145782_y())) {
            this.hitted.add(entity.func_145782_y());
        }
    }

    @EventTarget
    public void onWorld(WorldEvent event) {
        this.clearAll();
    }

    private void clearAll() {
        this.hitted.clear();
        this.swing.clear();
        this.ground.clear();
        this.invalidGround.clear();
        this.invisible.clear();
    }

    public static boolean isBot(EntityLivingBase entity) {
        if (((String)modeValue.get()).equals("Normal")) {
            EntityPlayer player;
            if (!(entity instanceof EntityPlayer)) {
                return false;
            }
            AntiBot antiBot = (AntiBot)Client.moduleManager.getModule(AntiBot.class);
            if (antiBot == null || !antiBot.getState()) {
                return false;
            }
            if (((Boolean)antiBot.colorValue.get()).booleanValue() && !entity.func_145748_c_().func_150254_d().replace("\u00a7r", "").contains("\u00a7")) {
                return true;
            }
            if (((Boolean)antiBot.livingTimeValue.get()).booleanValue() && entity.field_70173_aa < (Integer)antiBot.livingTimeTicksValue.get()) {
                return true;
            }
            if (((Boolean)antiBot.groundValue.get()).booleanValue() && !antiBot.ground.contains(entity.func_145782_y())) {
                return true;
            }
            if (((Boolean)antiBot.airValue.get()).booleanValue() && !antiBot.air.contains(entity.func_145782_y())) {
                return true;
            }
            if (((Boolean)antiBot.swingValue.get()).booleanValue() && !antiBot.swing.contains(entity.func_145782_y())) {
                return true;
            }
            if (((Boolean)antiBot.healthValue.get()).booleanValue() && entity.func_110143_aJ() > 20.0f) {
                return true;
            }
            if (((Boolean)antiBot.entityIDValue.get()).booleanValue() && (entity.func_145782_y() >= 1000000000 || entity.func_145782_y() <= -1)) {
                return true;
            }
            if (((Boolean)antiBot.derpValue.get()).booleanValue() && (entity.field_70125_A > 90.0f || entity.field_70125_A < -90.0f)) {
                return true;
            }
            if (((Boolean)antiBot.wasInvisibleValue.get()).booleanValue() && antiBot.invisible.contains(entity.func_145782_y())) {
                return true;
            }
            if (((Boolean)antiBot.armorValue.get()).booleanValue()) {
                player = (EntityPlayer)entity;
                if (player.field_71071_by.field_70460_b[0] == null && player.field_71071_by.field_70460_b[1] == null && player.field_71071_by.field_70460_b[2] == null && player.field_71071_by.field_70460_b[3] == null) {
                    return true;
                }
            }
            if (((Boolean)antiBot.pingValue.get()).booleanValue()) {
                player = (EntityPlayer)entity;
                if (mc.func_147114_u().func_175102_a(player.func_110124_au()).func_178853_c() == 0) {
                    return true;
                }
            }
            if (((Boolean)antiBot.needHitValue.get()).booleanValue() && !antiBot.hitted.contains(entity.func_145782_y())) {
                return true;
            }
            if (((Boolean)antiBot.invalidGroundValue.get()).booleanValue() && antiBot.invalidGround.getOrDefault(entity.func_145782_y(), 0) >= 10) {
                return true;
            }
            if (((Boolean)antiBot.tabValue.get()).booleanValue()) {
                boolean equals = ((String)antiBot.tabModeValue.get()).equalsIgnoreCase("Equals");
                String targetName = ColorUtils.stripColor(entity.func_145748_c_().func_150254_d());
                if (targetName != null) {
                    for (NetworkPlayerInfo networkPlayerInfo : mc.func_147114_u().func_175106_d()) {
                        String networkName = ColorUtils.stripColor(EntityUtils.getName(networkPlayerInfo));
                        if (networkName == null || !(equals ? targetName.equals(networkName) : targetName.contains(networkName))) continue;
                        return false;
                    }
                    return true;
                }
            }
            if (((Boolean)antiBot.duplicateInWorldValue.get()).booleanValue() && AntiBot.mc.field_71441_e.field_72996_f.stream().filter(currEntity -> currEntity instanceof EntityPlayer && ((EntityPlayer)currEntity).getDisplayNameString().equals(((EntityPlayer)currEntity).getDisplayNameString())).count() > 1L) {
                return true;
            }
            if (((Boolean)antiBot.duplicateInTabValue.get()).booleanValue() && mc.func_147114_u().func_175106_d().stream().filter(networkPlayer -> entity.func_70005_c_().equals(ColorUtils.stripColor(EntityUtils.getName(networkPlayer)))).count() > 1L) {
                return true;
            }
            return entity.func_70005_c_().isEmpty() || entity.func_70005_c_().equals(AntiBot.mc.field_71439_g.func_70005_c_());
        }
        return false;
    }
}

