/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.management.notifications.Notifications;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Options;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.module.impl.combat.AutoPot;
import me.arithmo.module.impl.combat.Criticals;
import me.arithmo.module.impl.other.AntiBot;
import me.arithmo.util.RotationUtils;
import me.arithmo.util.TeamUtils;
import me.arithmo.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Killaura
extends Module {
    private static final String AUTOBLOCK = "AUTOBLOCK";
    private static final String RANGE = "RANGE";
    private static final String PLAYERS = "PLAYERS";
    private static final String ANIMALS = "OTHERS";
    private static final String ARM = "ARMOR";
    private static final String TEAMS = "TEAMS";
    private final String INVISIBLES = "INVISIBLES";
    private String TICK = "Existed";
    private String DELAY = "DELAY";
    private String MAXRAND = "RANDELAY";
    private String DEATH = "DEATH";
    private String TARGETMODE = "TARGETMODE";
    private String AURAMODE = "AURAMODE";
    private Timer delay = new Timer();
    private Timer deathTimer = new Timer();
    private Timer switchTimer = new Timer();
    private List<EntityLivingBase> loaded = new CopyOnWriteArrayList<EntityLivingBase>();
    private int index;
    public static EntityLivingBase target;
    public static EntityLivingBase vip;
    static boolean allowCrits;
    private boolean disabled;

    public Killaura(ModuleData data) {
        super(data);
        this.settings.put(this.MAXRAND, new Setting<Integer>(this.MAXRAND, 25, "MS delay randomization.", 25.0, 0.0, 200.0));
        this.settings.put(this.TICK, new Setting<Integer>(this.TICK, 50, "Existed ticks before attacking.", 5.0, 1.0, 120.0));
        this.settings.put("AUTOBLOCK", new Setting<Boolean>("AUTOBLOCK", true, "Automatically blocks for you."));
        this.settings.put("RANGE", new Setting<Double>("RANGE", 4.5, "Range for killaura.", 0.1, 1.0, 8.0));
        this.settings.put(this.DELAY, new Setting<Integer>(this.DELAY, 100, "Aura speed (MS Delay)", 25.0, 50.0, 500.0));
        this.settings.put("PLAYERS", new Setting<Boolean>("PLAYERS", true, "Attack players."));
        this.settings.put("OTHERS", new Setting<Boolean>("OTHERS", false, "Attack Animals."));
        this.settings.put("ARMOR", new Setting<Boolean>("ARMOR", true, "Check if player has armor equipped."));
        this.settings.put("TEAMS", new Setting<Boolean>("TEAMS", false, "Check if player is not on your team."));
        this.settings.put("INVISIBLES", new Setting<Boolean>("INVISIBLES", false, "Attack invisibles."));
        this.settings.put(this.DEATH, new Setting<Boolean>(this.DEATH, true, "Disables killaura when you die."));
        this.settings.put(this.TARGETMODE, new Setting<Options>(this.TARGETMODE, new Options("Target Mode", "Angle", new String[]{"Angle", "Range", "FOV", "Armor", "Dynamic"}), "Target mode priority."));
        this.settings.put(this.AURAMODE, new Setting<Options>(this.AURAMODE, new Options("Aura Mode", "Switch", new String[]{"Tick2", "Tick", "Switch", "Single"}), "Attack method for the aura."));
    }

    private static int randomNumber(int max, int min) {
        return - min + (int)(Math.random() * (double)(max - (- min) + 1));
    }

    @Override
    public void onDisable() {
        if (Killaura.mc.thePlayer != null) {
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        this.loaded.clear();
        this.disabled = false;
        allowCrits = true;
    }

    @Override
    public void onEnable() {
        target = null;
        this.loaded.clear();
        this.disabled = false;
        allowCrits = true;
    }

    @RegisterEvent(events={EventMotion.class, EventPacket.class})
    public void onEvent(Event event) {
        this.setSuffix(((Options)((Setting)this.settings.get(this.AURAMODE)).getValue()).getSelected());
        allowCrits = !this.getSuffix().contains("Tick");
        int max = ((Number)((Setting)this.settings.get(this.MAXRAND)).getValue()).intValue();
        int delayValue = ((Number)((Setting)this.settings.get(this.DELAY)).getValue()).intValue() + Killaura.randomNumber(max, max);
        boolean block = (Boolean)((Setting)this.settings.get("AUTOBLOCK")).getValue();
        if (event instanceof EventMotion) {
            EventMotion em = (EventMotion)event;
            if (em.isPre() && ((Boolean)((Setting)this.settings.get(this.DEATH)).getValue()).booleanValue()) {
                if (!Killaura.mc.thePlayer.isEntityAlive() && !this.disabled) {
                    this.toggle();
                    this.deathTimer.reset();
                    Notifications.getManager().post("Aura Death", "Aura disabled due to death.");
                }
                if (this.disabled && this.deathTimer.delay(10000.0f)) {
                    this.disabled = false;
                }
                if (this.disabled) {
                    return;
                }
            }
            if (em.isPre() && this.getSuffix().contains("Tick")) {
                this.updateTicks();
            }
            if (AutoPot.potting && AutoPot.haltTicks > 4) {
                return;
            }
            switch (((Options)((Setting)this.settings.get(this.AURAMODE)).getValue()).getSelected()) {
                case "Single": {
                    target = this.getOptimalTarget();
                    if (em.isPre()) {
                        if (target == null) break;
                        if (Killaura.mc.thePlayer.getCurrentEquippedItem() != null && block && Killaura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                            Killaura.mc.thePlayer.setItemInUse(Killaura.mc.thePlayer.getHeldItem(), Killaura.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                        }
                        float[] rots = RotationUtils.getRotations(target);
                        em.setYaw(rots[0]);
                        em.setPitch(rots[1]);
                        break;
                    }
                    if (target == null || !this.delay.delay(delayValue)) break;
                    if (Killaura.mc.thePlayer.isBlocking()) {
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    Killaura.mc.thePlayer.swingItem();
                    mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                    if (Killaura.mc.thePlayer.getCurrentEquippedItem() != null && Killaura.mc.thePlayer.getCurrentEquippedItem().isItemEnchanted()) {
                        Killaura.mc.thePlayer.onEnchantmentCritical(target);
                    }
                    this.delay.reset();
                    break;
                }
                case "Tick2": {
                    if (em.isPre()) {
                        target = this.getOptimalTarget();
                        if (target == null) break;
                        if (Killaura.mc.thePlayer.getCurrentEquippedItem() != null && block && Killaura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                            Killaura.mc.thePlayer.setItemInUse(Killaura.mc.thePlayer.getHeldItem(), Killaura.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                        }
                        float[] r = RotationUtils.getRotations(target);
                        em.setYaw(r[0]);
                        em.setPitch(r[1]);
                        break;
                    }
                    if (target == null || !this.delay.delay(150.0f) || Killaura.target.waitTicks >= 8) break;
                    if (Killaura.mc.thePlayer.isBlocking()) {
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    this.attack(target, false);
                    this.attack(target, true);
                    Killaura.target.waitTicks = 10;
                    this.delay.reset();
                    break;
                }
                case "Tick": {
                    if (em.isPre()) {
                        target = this.getOptimalTarget();
                        if (target == null) break;
                        if (Killaura.mc.thePlayer.getCurrentEquippedItem() != null && block && Killaura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                            Killaura.mc.thePlayer.setItemInUse(Killaura.mc.thePlayer.getHeldItem(), Killaura.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                        }
                        float[] r = RotationUtils.getRotations(target);
                        em.setYaw(r[0]);
                        em.setPitch(r[1]);
                        break;
                    }
                    if (target == null || Killaura.target.waitTicks >= 0 || !this.delay.delay(450.0f)) break;
                    if (Killaura.mc.thePlayer.isBlocking()) {
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    this.swap(9, Killaura.mc.thePlayer.inventory.currentItem);
                    this.attack(target, false);
                    this.attack(target, false);
                    this.attack(target, true);
                    this.swap(9, Killaura.mc.thePlayer.inventory.currentItem);
                    this.attack(target, false);
                    this.attack(target, true);
                    Killaura.target.waitTicks = 10;
                    this.delay.reset();
                    break;
                }
                case "Switch": {
                    if (em.isPre()) {
                        EntityLivingBase ent1;
                        int surroundingEnts = 0;
                        if (target != null && !this.validEntity(target)) {
                            target = null;
                        }
                        for (Object o : Killaura.mc.theWorld.getLoadedEntityList()) {
                            if (!(o instanceof EntityLivingBase) || !this.validEntity(ent1 = (EntityLivingBase)o)) continue;
                            ++surroundingEnts;
                        }
                        if (surroundingEnts > 0) {
                            if (Killaura.mc.thePlayer.getCurrentEquippedItem() != null && block && Killaura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                Killaura.mc.thePlayer.setItemInUse(Killaura.mc.thePlayer.getHeldItem(), Killaura.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                            }
                        } else {
                            this.loaded.clear();
                        }
                        if (this.loaded.isEmpty() || this.switchTimer.delay(200.0f) && !this.loaded.isEmpty()) {
                            for (Object o : Killaura.mc.theWorld.getLoadedEntityList()) {
                                if (!(o instanceof EntityLivingBase) || !this.validEntity(ent1 = (EntityLivingBase)o)) continue;
                                this.loaded.add(ent1);
                            }
                        }
                        for (EntityLivingBase ent2 : this.loaded) {
                            if (this.validEntity(ent2)) continue;
                            this.loaded.remove(ent2);
                        }
                        this.sortList(this.loaded);
                        if (this.switchTimer.delay(150.0f)) {
                            this.incrementIndex();
                            this.switchTimer.reset();
                        }
                        if (this.index >= this.loaded.size()) {
                            this.index = 0;
                        }
                        if (this.loaded.size() <= 0 || (Killaura.target = this.loaded.get(this.index)) == null) break;
                        float[] rot = RotationUtils.getRotations(target);
                        em.setYaw(rot[0]);
                        em.setPitch(rot[1]);
                        break;
                    }
                    if (target == null || !this.delay.delay(delayValue)) break;
                    if (Killaura.mc.thePlayer.isBlocking()) {
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    Killaura.mc.thePlayer.swingItem();
                    Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                    if (Killaura.mc.thePlayer.getCurrentEquippedItem() != null && Killaura.mc.thePlayer.getCurrentEquippedItem().isItemEnchanted()) {
                        Killaura.mc.thePlayer.onEnchantmentCritical(target);
                    }
                    this.delay.reset();
                    this.loaded.remove(target);
                }
            }
        }
    }

    private void incrementIndex() {
        ++this.index;
        if (this.index >= this.loaded.size()) {
            this.index = 0;
        }
    }

    private void updateTicks() {
        for (Object o : Killaura.mc.theWorld.getLoadedEntityList()) {
            if (!(o instanceof EntityLivingBase)) continue;
            EntityLivingBase ent = (EntityLivingBase)o;
            --ent.waitTicks;
        }
    }

    protected void swap(int slot, int hotbarNum) {
        Killaura.mc.playerController.windowClick(Killaura.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Killaura.mc.thePlayer);
    }

    public void attack(Entity ent, boolean crits) {
        Killaura.mc.thePlayer.swingItem();
        if (crits) {
            Criticals.doCrits();
        } else {
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
        }
        mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
        try {
            if (Killaura.mc.thePlayer.getCurrentEquippedItem().isItemEnchanted()) {
                Killaura.mc.thePlayer.onEnchantmentCritical(ent);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private EntityLivingBase getOptimalTarget() {
        ArrayList<EntityLivingBase> load = new ArrayList<EntityLivingBase>();
        for (Object o : Killaura.mc.theWorld.getLoadedEntityList()) {
            EntityLivingBase ent;
            if (!(o instanceof EntityLivingBase) || !this.validEntity(ent = (EntityLivingBase)o)) continue;
            if (ent == vip) {
                return ent;
            }
            load.add(ent);
        }
        if (load.isEmpty()) {
            return null;
        }
        return this.getTarget(load);
    }

    private boolean validEntity(EntityLivingBase entity) {
        float range = ((Number)((Setting)this.settings.get("RANGE")).getValue()).floatValue();
        boolean players = (Boolean)((Setting)this.settings.get("PLAYERS")).getValue();
        boolean animals = (Boolean)((Setting)this.settings.get("OTHERS")).getValue();
        if (Killaura.mc.thePlayer.isEntityAlive() && entity.isEntityAlive() && (double)Killaura.mc.thePlayer.getDistanceToEntity(entity) <= (Killaura.mc.thePlayer.canEntityBeSeen(entity) ? (double)range : 3.5) && entity.ticksExisted > ((Number)((Setting)this.settings.get(this.TICK)).getValue()).intValue()) {
            if (AntiBot.getInvalid().contains(entity)) {
                return false;
            }
            if (entity.isPlayerSleeping()) {
                return false;
            }
            if (entity instanceof EntityPlayer && players) {
                EntityPlayer ent = (EntityPlayer)entity;
                boolean armor = (Boolean)((Setting)this.settings.get("ARMOR")).getValue();
                return !(TeamUtils.isTeam(Killaura.mc.thePlayer, ent) && (Boolean)((Setting)this.settings.get("TEAMS")).getValue() != false || ent.isInvisible() && (Boolean)((Setting)this.settings.get("INVISIBLES")).getValue() == false || armor && !this.hasArmor(ent) || FriendManager.isFriend(ent.getName()));
            }
            if (entity instanceof EntityMob && animals) {
                return true;
            }
            if (entity instanceof EntityAnimal && animals) {
                return true;
            }
        }
        return false;
    }

    private boolean hasArmor(EntityPlayer player) {
        ItemStack boots = player.inventory.armorInventory[0];
        ItemStack pants = player.inventory.armorInventory[1];
        ItemStack chest = player.inventory.armorInventory[2];
        ItemStack head = player.inventory.armorInventory[3];
        return boots != null || pants != null || chest != null || head != null;
    }

    private void sortList(List<EntityLivingBase> weed) {
        String current;
        switch (current = ((Options)((Setting)this.settings.get(this.TARGETMODE)).getValue()).getSelected()) {
            case "Range": {
                weed.sort((o1, o2) -> (int)(o1.getDistanceToEntity(Killaura.mc.thePlayer) - o2.getDistanceToEntity(Killaura.mc.thePlayer)));
                break;
            }
            case "FOV": {
                weed.sort(Comparator.comparingDouble(o -> RotationUtils.getDistanceBetweenAngles(Killaura.mc.thePlayer.rotationPitch, RotationUtils.getRotations(o)[0])));
                break;
            }
            case "Angle": {
                weed.sort((o1, o2) -> {
                    float[] rot1 = RotationUtils.getRotations(o1);
                    float[] rot2 = RotationUtils.getRotations(o2);
                    return (int)(Killaura.mc.thePlayer.rotationYaw - rot1[0] - (Killaura.mc.thePlayer.rotationYaw - rot2[0]));
                }
                );
                break;
            }
            case "Health": {
                weed.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
                break;
            }
            case "Armor": {
                weed.sort(Comparator.comparingInt(o -> o instanceof EntityPlayer ? ((EntityPlayer)o).inventory.getTotalArmorValue() : (int)o.getHealth()));
                break;
            }
            case "Dynamic": {
                weed.sort(Comparator.comparingInt(o -> (int)(o.getHealth() + o.getDistanceToEntity(Killaura.mc.thePlayer) + (Killaura.mc.thePlayer.rotationYaw - RotationUtils.getRotations(o)[0]) + (float)(o instanceof EntityPlayer ? ((EntityPlayer)o).inventory.getTotalArmorValue() : 0))));
            }
        }
    }

    private EntityLivingBase getTarget(List<EntityLivingBase> list) {
        this.sortList(list);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}

