// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.HashMap;
import java.util.Comparator;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import exhibition.management.friend.FriendManager;
import exhibition.util.TeamUtils;
import java.util.ArrayList;
import net.minecraft.network.play.client.C03PacketPlayer;
import exhibition.event.impl.EventPacket;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import exhibition.module.impl.movement.Bhop;
import exhibition.Client;
import exhibition.util.RotationUtils;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.item.ItemSword;
import exhibition.management.notifications.Notifications;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import exhibition.module.data.Options;
import exhibition.module.data.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import exhibition.module.data.ModuleData;
import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import exhibition.util.Timer;
import exhibition.module.Module;

public class Killaura extends Module
{
    private static final String AUTOBLOCK = "AUTOBLOCK";
    private static final String RANGE = "RANGE";
    private static final String PLAYERS = "PLAYERS";
    private static final String ANIMALS = "OTHERS";
    private static final String ARM = "ARMOR";
    private static final String TEAMS = "TEAMS";
    private final String INVISIBLES = "INVISIBLES";
    private String TICK;
    private String DELAY;
    private String MAXRAND;
    private String DEATH;
    private String TARGETMODE;
    private String AURAMODE;
    private String FOVCHECK;
    private Timer delay;
    private Timer deathTimer;
    private Timer switchTimer;
    private List<EntityLivingBase> loaded;
    private int index;
    private static boolean canJump;
    private boolean setupTick;
    public static EntityLivingBase target;
    public static EntityLivingBase vip;
    static boolean allowCrits;
    private boolean disabled;
    
    public static boolean isSetupTick() {
        return Killaura.canJump;
    }
    
    public Killaura(final ModuleData data) {
        super(data);
        this.TICK = "EXISTED";
        this.DELAY = "SPEED";
        this.MAXRAND = "RANDAPS";
        this.DEATH = "DEATH";
        this.TARGETMODE = "PRIORITY";
        this.AURAMODE = "MODE";
        this.FOVCHECK = "FOV";
        this.delay = new Timer();
        this.deathTimer = new Timer();
        this.switchTimer = new Timer();
        this.loaded = new CopyOnWriteArrayList<EntityLivingBase>();
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.FOVCHECK, new Setting<Integer>(this.FOVCHECK, 360, "Targets must be in FOV.", 5.0, 5.0, 180.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.MAXRAND, new Setting<Integer>(this.MAXRAND, 1, "Minimum APS.", 1.0, 0.0, 5.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.TICK, new Setting<Integer>(this.TICK, 50, "Existed ticks before attacking.", 5.0, 1.0, 120.0));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("AUTOBLOCK", new Setting<Boolean>("AUTOBLOCK", true, "Automatically blocks for you."));
        ((HashMap<String, Setting<Double>>)this.settings).put("RANGE", new Setting<Double>("RANGE", 4.5, "Range for killaura.", 0.1, 1.0, 8.0));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.DELAY, new Setting<Integer>(this.DELAY, 10, "Attacks per second.", 1.0, 1.0, 20.0));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("PLAYERS", new Setting<Boolean>("PLAYERS", true, "Attack players."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("OTHERS", new Setting<Boolean>("OTHERS", false, "Attack Animals."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("ARMOR", new Setting<Boolean>("ARMOR", true, "Check if player has armor equipped."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("TEAMS", new Setting<Boolean>("TEAMS", false, "Check if player is not on your team."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("INVISIBLES", new Setting<Boolean>("INVISIBLES", false, "Attack invisibles."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.DEATH, new Setting<Boolean>(this.DEATH, true, "Disables killaura when you die."));
        ((HashMap<String, Setting<Options>>)this.settings).put(this.TARGETMODE, new Setting<Options>(this.TARGETMODE, new Options("Priority", "Angle", new String[] { "Angle", "Range", "FOV", "Armor" }), "Target mode priority."));
        ((HashMap<String, Setting<Options>>)this.settings).put(this.AURAMODE, new Setting<Options>(this.AURAMODE, new Options("Mode", "Switch", new String[] { "Tick2", "Tick", "Switch", "Duel", "Vanilla" }), "Attack method for the aura."));
    }
    
    private static int randomNumber(final int max, final int min) {
        return -min + (int)(Math.random() * (max - -min + 1));
    }
    
    @Override
    public void onDisable() {
        if (Killaura.mc.thePlayer != null) {
            Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        this.loaded.clear();
        this.disabled = false;
        Killaura.allowCrits = true;
        this.setupTick = false;
    }
    
    @Override
    public void onEnable() {
        Killaura.target = null;
        this.loaded.clear();
        this.disabled = false;
        Killaura.allowCrits = true;
        this.setupTick = false;
    }
    
    @RegisterEvent(events = { EventMotion.class, EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        this.setSuffix(((HashMap<K, Setting<Options>>)this.settings).get(this.AURAMODE).getValue().getSelected());
        Killaura.allowCrits = (!this.getSuffix().contains("Tick") && !this.getSuffix().contains("Duel"));
        if (!this.getSuffix().contains("Duel")) {
            this.setupTick = false;
        }
        final int max = ((HashMap<K, Setting<Number>>)this.settings).get(this.MAXRAND).getValue().intValue();
        final int delayValue = (20 / ((HashMap<K, Setting<Number>>)this.settings).get(this.DELAY).getValue().intValue() + randomNumber(max, max)) * 50;
        final boolean block = ((HashMap<K, Setting<Boolean>>)this.settings).get("AUTOBLOCK").getValue();
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre() && ((HashMap<K, Setting<Boolean>>)this.settings).get(this.DEATH).getValue()) {
                if (Killaura.canJump) {
                    Killaura.canJump = false;
                }
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
            final String selected = ((HashMap<K, Setting<Options>>)this.settings).get(this.AURAMODE).getValue().getSelected();
            switch (selected) {
                case "Vanilla": {
                    if (em.isPre()) {
                        int entityCount = 0;
                        for (final Object o : Killaura.mc.theWorld.loadedEntityList) {
                            if (o instanceof EntityLivingBase) {
                                final EntityLivingBase ent = (EntityLivingBase)o;
                                if (!this.validEntity(ent) || entityCount >= 5 || ent == Killaura.mc.thePlayer) {
                                    continue;
                                }
                                ++entityCount;
                                if (Killaura.mc.thePlayer.inventory.getCurrentItem() != null && block && Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                                    Killaura.mc.thePlayer.setItemInUse(Killaura.mc.thePlayer.inventory.getCurrentItem(), 71999);
                                }
                                if (!this.delay.delay(delayValue)) {
                                    continue;
                                }
                                Killaura.mc.thePlayer.swingItem();
                                Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
                                if (Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.inventory.getCurrentItem().isItemEnchanted()) {
                                    Killaura.mc.thePlayer.onEnchantmentCritical(ent);
                                }
                                this.delay.reset();
                            }
                        }
                        break;
                    }
                    break;
                }
                case "Duel": {
                    if (em.isPre()) {
                        if (Killaura.mc.getCurrentServerData() != null && Killaura.mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && Killaura.mc.getIntegratedServer() == null) {
                            Notifications.getManager().post("Auto Config", "Single gets you watchdog banned.", Notifications.Type.WARNING);
                            ((HashMap<K, Setting<Options>>)this.settings).get(this.AURAMODE).getValue().setSelected("Switch");
                        }
                        Killaura.target = this.getOptimalTarget();
                        if (Killaura.target != null) {
                            if (block && Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                                Killaura.mc.playerController.sendUseItem(Killaura.mc.thePlayer, Killaura.mc.theWorld, Killaura.mc.thePlayer.inventory.getCurrentItem());
                            }
                            final float[] rotations = RotationUtils.getRotations(Killaura.target);
                            em.setYaw(rotations[0]);
                            em.setPitch(rotations[1]);
                            final boolean crits = Client.getModuleManager().isEnabled(Criticals.class) && !Client.getModuleManager().get(Criticals.class).getSetting("MODE").getValue().getSelected().equals("Jump");
                            if (Killaura.mc.thePlayer.ticksExisted % 2 == 0) {
                                if (crits && (Killaura.mc.thePlayer.isCollidedVertically || Killaura.mc.thePlayer.onGround) && !Client.getModuleManager().isEnabled(Bhop.class)) {
                                    Killaura.canJump = true;
                                    em.setY(em.getY() + 0.062511);
                                    em.setGround(false);
                                }
                            }
                            else if (crits && em.getY() == Killaura.mc.thePlayer.posY && em.isOnground() && !Client.getModuleManager().isEnabled(Bhop.class)) {
                                em.setGround(false);
                                em.setAlwaysSend(true);
                            }
                            break;
                        }
                        break;
                    }
                    else {
                        if (em.isPost() && Killaura.mc.thePlayer.ticksExisted % 2 != 0 && Killaura.target != null) {
                            if (Killaura.mc.thePlayer.isBlocking() && block && Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                                Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Killaura.mc.thePlayer.inventory.getCurrentItem()));
                            }
                            Killaura.mc.thePlayer.swingItem();
                            Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(Killaura.target, C02PacketUseEntity.Action.ATTACK));
                            final float sharpLevel = EnchantmentHelper.func_152377_a(Killaura.mc.thePlayer.inventory.getCurrentItem(), Killaura.target.getCreatureAttribute());
                            if (sharpLevel > 0.0f) {
                                Killaura.mc.thePlayer.onEnchantmentCritical(Killaura.target);
                            }
                            if ((Killaura.mc.thePlayer.isBlocking() && block) || (Killaura.mc.gameSettings.keyBindUseItem.getIsKeyPressed() && Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword)) {
                                Killaura.mc.playerController.sendUseItem(Killaura.mc.thePlayer, Killaura.mc.theWorld, Killaura.mc.thePlayer.getCurrentEquippedItem());
                            }
                            break;
                        }
                        break;
                    }
                    break;
                }
                case "Tick2": {
                    if (em.isPre()) {
                        Killaura.target = this.getOptimalTarget();
                        if (Killaura.target != null) {
                            if (Killaura.mc.thePlayer.inventory.getCurrentItem() != null && block && Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                                Killaura.mc.playerController.sendUseItem(Killaura.mc.thePlayer, Killaura.mc.theWorld, Killaura.mc.thePlayer.inventory.getCurrentItem());
                            }
                            final float[] r = RotationUtils.getRotations(Killaura.target);
                            em.setPitch(r[1]);
                            em.setYaw(r[0]);
                            break;
                        }
                        break;
                    }
                    else {
                        if (Killaura.target == null || !this.delay.delay(100.0f) || Killaura.target.waitTicks >= 8) {
                            break;
                        }
                        if (Killaura.mc.thePlayer.isBlocking() && block) {
                            Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        this.attack(Killaura.target, false);
                        this.attack(Killaura.target, true);
                        Killaura.target.waitTicks = 10;
                        if ((Killaura.mc.thePlayer.isBlocking() && block) || Killaura.mc.gameSettings.keyBindUseItem.getIsKeyPressed()) {
                            Killaura.mc.playerController.sendUseItem(Killaura.mc.thePlayer, Killaura.mc.theWorld, Killaura.mc.thePlayer.getCurrentEquippedItem());
                        }
                        this.delay.reset();
                        if (Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.isBlocking() && block) {
                            Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Killaura.mc.thePlayer.inventory.getCurrentItem()));
                            break;
                        }
                        break;
                    }
                    break;
                }
                case "Tick": {
                    if (em.isPre()) {
                        Killaura.target = this.getOptimalTarget();
                        if (Killaura.target != null) {
                            if (Killaura.mc.thePlayer.getCurrentEquippedItem() != null && block && Killaura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                Killaura.mc.thePlayer.setItemInUse(Killaura.mc.thePlayer.getCurrentEquippedItem(), 71999);
                            }
                            final float[] r = RotationUtils.getRotations(Killaura.target);
                            em.setYaw(r[0]);
                            em.setPitch(r[1]);
                            break;
                        }
                        break;
                    }
                    else {
                        if (Killaura.target != null && Killaura.target.waitTicks < 1 && this.delay.delay(450.0f)) {
                            if (Killaura.mc.thePlayer.isBlocking() && block) {
                                Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            this.swap(9, Killaura.mc.thePlayer.inventory.currentItem);
                            this.attack(Killaura.target, false);
                            this.attack(Killaura.target, true);
                            this.swap(9, Killaura.mc.thePlayer.inventory.currentItem);
                            this.attack(Killaura.target, false);
                            this.attack(Killaura.target, true);
                            if ((Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.isBlocking() && block) || Killaura.mc.gameSettings.keyBindUseItem.getIsKeyPressed()) {
                                Killaura.mc.playerController.sendUseItem(Killaura.mc.thePlayer, Killaura.mc.theWorld, Killaura.mc.thePlayer.getCurrentEquippedItem());
                            }
                            Killaura.target.waitTicks = 10;
                            this.delay.reset();
                            break;
                        }
                        break;
                    }
                    break;
                }
                case "Switch": {
                    if (em.isPre()) {
                        if (this.switchTimer.delay(200.0f)) {
                            this.loaded = this.getTargets();
                        }
                        if (this.index >= this.loaded.size()) {
                            this.index = 0;
                        }
                        if (this.loaded.size() > 0) {
                            if (this.switchTimer.delay(200.0f)) {
                                this.incrementIndex();
                                this.switchTimer.reset();
                            }
                            final EntityLivingBase target = this.loaded.get(this.index);
                            if (target != null) {
                                if (block && Killaura.mc.thePlayer.inventory.getCurrentItem() != null && Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
                                    Killaura.mc.thePlayer.setItemInUse(Killaura.mc.thePlayer.getCurrentEquippedItem(), 71999);
                                }
                                final float[] rotations2 = RotationUtils.getRotations(target);
                                em.setYaw(rotations2[0]);
                                em.setPitch(rotations2[1]);
                            }
                            break;
                        }
                        break;
                    }
                    else {
                        if (em.isPost() && this.delay.delay(delayValue) && this.loaded.size() > 0 && this.loaded.get(this.index) != null) {
                            final EntityLivingBase target = this.loaded.get(this.index);
                            if (Killaura.mc.thePlayer.isBlocking() && block) {
                                Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            Killaura.mc.thePlayer.swingItem();
                            Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                            final float sharpLevel2 = EnchantmentHelper.func_152377_a(Killaura.mc.thePlayer.inventory.getCurrentItem(), target.getCreatureAttribute());
                            if (sharpLevel2 > 0.0f) {
                                Killaura.mc.thePlayer.onEnchantmentCritical(target);
                            }
                            if ((Killaura.mc.thePlayer.isBlocking() && block) || (Killaura.mc.gameSettings.keyBindUseItem.getIsKeyPressed() && Killaura.mc.thePlayer.getCurrentEquippedItem() != null)) {
                                Killaura.mc.playerController.sendUseItem(Killaura.mc.thePlayer, Killaura.mc.theWorld, Killaura.mc.thePlayer.getCurrentEquippedItem());
                            }
                            this.delay.reset();
                            break;
                        }
                        break;
                    }
                    break;
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
        for (final Object o : Killaura.mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                final EntityLivingBase entityLivingBase;
                final EntityLivingBase ent = entityLivingBase = (EntityLivingBase)o;
                --entityLivingBase.waitTicks;
            }
        }
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        Killaura.mc.playerController.windowClick(Killaura.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Killaura.mc.thePlayer);
    }
    
    public void attack(final Entity ent, final boolean crits) {
        Killaura.mc.thePlayer.swingItem();
        if (crits) {
            Criticals.doCrits();
        }
        else {
            Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
        final float sharpLevel = EnchantmentHelper.func_152377_a(Killaura.mc.thePlayer.inventory.getCurrentItem(), Killaura.target.getCreatureAttribute());
        if (sharpLevel > 0.0f) {
            Killaura.mc.thePlayer.onEnchantmentCritical(Killaura.target);
        }
    }
    
    private EntityLivingBase getOptimalTarget() {
        final List<EntityLivingBase> load = new ArrayList<EntityLivingBase>();
        for (final Object o : Killaura.mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                final EntityLivingBase ent = (EntityLivingBase)o;
                if (!this.validEntity(ent)) {
                    continue;
                }
                if (ent == Killaura.vip) {
                    return ent;
                }
                load.add(ent);
            }
        }
        if (load.isEmpty()) {
            return null;
        }
        return this.getTarget(load);
    }
    
    private boolean validEntity(final EntityLivingBase entity) {
        final float range = ((HashMap<K, Setting<Number>>)this.settings).get("RANGE").getValue().floatValue();
        final boolean players = ((HashMap<K, Setting<Boolean>>)this.settings).get("PLAYERS").getValue();
        final boolean animals = ((HashMap<K, Setting<Boolean>>)this.settings).get("OTHERS").getValue();
        if (Killaura.mc.thePlayer.isEntityAlive() && entity.isEntityAlive() && Killaura.mc.thePlayer.getDistanceToEntity(entity) <= (Killaura.mc.thePlayer.canEntityBeSeen(entity) ? range : 3.5) && entity.ticksExisted > ((HashMap<K, Setting<Number>>)this.settings).get(this.TICK).getValue().intValue()) {
            if (!this.isInFOV(entity)) {
                return false;
            }
            if (AntiBot.getInvalid().contains(entity)) {
                return false;
            }
            if (entity.isPlayerSleeping()) {
                return false;
            }
            if (entity instanceof EntityPlayer && players) {
                final EntityPlayer ent = (EntityPlayer)entity;
                final boolean armor = ((HashMap<K, Setting<Boolean>>)this.settings).get("ARMOR").getValue();
                return (!TeamUtils.isTeam(Killaura.mc.thePlayer, ent) || !((HashMap<K, Setting<Boolean>>)this.settings).get("TEAMS").getValue()) && (!ent.isInvisible() || ((HashMap<K, Setting<Boolean>>)this.settings).get("INVISIBLES").getValue()) && (!armor || this.hasArmor(ent)) && !FriendManager.isFriend(ent.getName());
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
    
    private boolean hasArmor(final EntityPlayer player) {
        final ItemStack boots = player.inventory.armorInventory[0];
        final ItemStack pants = player.inventory.armorInventory[1];
        final ItemStack chest = player.inventory.armorInventory[2];
        final ItemStack head = player.inventory.armorInventory[3];
        return boots != null || pants != null || chest != null || head != null;
    }
    
    private void sortList(final List<EntityLivingBase> weed) {
        final String selected;
        final String current = selected = ((HashMap<K, Setting<Options>>)this.settings).get(this.TARGETMODE).getValue().getSelected();
        switch (selected) {
            case "Range": {
                weed.sort((o1, o2) -> (int)(o1.getDistanceToEntity(Killaura.mc.thePlayer) - o2.getDistanceToEntity(Killaura.mc.thePlayer)));
                break;
            }
            case "FOV": {
                weed.sort(Comparator.comparingDouble(o -> RotationUtils.getDistanceBetweenAngles(Killaura.mc.thePlayer.rotationPitch, RotationUtils.getRotations(o)[0])));
                break;
            }
            case "Angle": {
                final float[] rot1;
                final float[] rot2;
                weed.sort((o1, o2) -> {
                    rot1 = RotationUtils.getRotations(o1);
                    rot2 = RotationUtils.getRotations(o2);
                    return (int)(Killaura.mc.thePlayer.rotationYaw - rot1[0] - (Killaura.mc.thePlayer.rotationYaw - rot2[0]));
                });
                break;
            }
            case "Health": {
                weed.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
                break;
            }
            case "Armor": {
                weed.sort(Comparator.comparingInt(o -> (o instanceof EntityPlayer) ? o.inventory.getTotalArmorValue() : ((int)o.getHealth())));
                break;
            }
        }
    }
    
    private EntityLivingBase getTarget(final List<EntityLivingBase> list) {
        this.sortList(list);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    private List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (final Object o : Killaura.mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)o;
                if (!this.validEntity(entity)) {
                    continue;
                }
                targets.add(entity);
            }
        }
        return targets;
    }
    
    private boolean isInFOV(final EntityLivingBase entity) {
        final int fov = ((HashMap<K, Setting<Number>>)this.settings).get(this.FOVCHECK).getValue().intValue();
        return Math.abs(RotationUtils.getYawChange(entity.posX, entity.posZ)) <= fov && Math.abs(RotationUtils.getPitchChange(entity, entity.posY)) <= fov;
    }
}
