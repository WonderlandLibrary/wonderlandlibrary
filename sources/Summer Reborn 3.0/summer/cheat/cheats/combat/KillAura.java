package summer.cheat.cheats.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Timer;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.CombatUtils;
import summer.base.utilities.RotationUtils;
import summer.base.utilities.TimerUtils;
import summer.cheat.cheats.player.Scaffold;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.cheat.guiutil.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KillAura extends Cheats {
    public static Minecraft mc = Minecraft.getMinecraft();
    private TimerUtils switchTimer = new TimerUtils();
    private TimerUtils attackTimer = new TimerUtils();
    public static EntityLivingBase target;
    private List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
    private boolean executingblock;
    private boolean availableBlock;
    private int currentIndex;
    public static boolean isAttackTick;
    public static TimerUtils timer = new TimerUtils();
    private static EntityLivingBase finalTarget = null;
    public static Setting autoblockType;
    public static Setting rotationsMode;
    public static Setting modeType;
    public static Setting rangeValue;
    public static Setting blockRange;
    public static Setting hitChance;
    public static Setting switchDelay;
    public static Setting cps;
    public static Setting crack;
    public static Setting crackSize;
    public static Setting autoblock;
    public static Setting weapon;
    public static Setting players;
    public static Setting invisibles;
    public static Setting mobs;
    public static Setting friends;
    public static Setting animals;
    public static Setting teams;
    public static Setting others;

    public KillAura() {
        super("KillAura", "Automatically hits entities around you", Selection.COMBAT, false);
    }

    @Override
    public void onSetup() {
        ArrayList targeting = new ArrayList();
        targeting.add("Switch");
        targeting.add("Single");
        Summer.INSTANCE.settingsManager.Property(modeType = new Setting("Mode", this, "Single", targeting));
        ArrayList Rotations = new ArrayList();
        Rotations.add("Normal");
        Rotations.add("None");
        Summer.INSTANCE.settingsManager.Property(rotationsMode = new Setting("Rotations", this, "Normal", Rotations));
        Summer.INSTANCE.settingsManager.Property(rangeValue = new Setting("Range", this, 4.2, 1, 6, false));
        Summer.INSTANCE.settingsManager.Property(blockRange = new Setting("Block Range", this, 4.2, 1, 10, false));
        Summer.INSTANCE.settingsManager.Property(hitChance = new Setting("Hit Chance", this, 100, 1, 100, true));
        Summer.INSTANCE.settingsManager.Property(switchDelay = new Setting("Switch Delay", this, 200, 1, 1000, true));
        Summer.INSTANCE.settingsManager.Property(cps = new Setting("APS", this, 10, 2, 20, false));
        Summer.INSTANCE.settingsManager.Property(crack = new Setting("Crack", this, false));
        Summer.INSTANCE.settingsManager.Property(crackSize = new Setting("Crack Size", this, 2, 0, 6, false, crack::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(autoblock = new Setting("Autoblock", this, true));
        ArrayList autoblockMode = new ArrayList();
        autoblockMode.add("Watchdog");
        Summer.INSTANCE.settingsManager.Property(autoblockType = new Setting("Block Mode", this, "Watchdog", autoblockMode, autoblock::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(weapon = new Setting("Weapon", this, true));
        Summer.INSTANCE.settingsManager.Property(players = new Setting("Players", this, true));
        Summer.INSTANCE.settingsManager.Property(invisibles = new Setting("Invisibles", this, false));
        Summer.INSTANCE.settingsManager.Property(mobs = new Setting("Mobs", this, false));
        Summer.INSTANCE.settingsManager.Property(friends = new Setting("Friends", this, false));
        Summer.INSTANCE.settingsManager.Property(animals = new Setting("Animals", this, false));
        Summer.INSTANCE.settingsManager.Property(teams = new Setting("Teams", this, false));
        Summer.INSTANCE.settingsManager.Property(others = new Setting("Others", this, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate eu) {
        if(CheatManager.getInstance(Scaffold.class).isToggled())
            return;
        final KillAura ka = CheatManager.getInstance(KillAura.class);
//          if(mc.thePlayer.isSwingInProgress) { ka.target.setFire(10);
//          ka.target.setHealth(ka.target.getHealth() -0.3f); mc.timer.timerSpeed = 1f;
//          }
//          if (Client.instance.mm.getModuleByName("Flight").isToggled() &&
//          mc.thePlayer.ticksExisted % 6 == 0 && mc.thePlayer.isSwingInProgress) {
//         Flight.sendPackets();
//          }
        this.setDisplayName("KillAura\u00A77 " + modeType.getValString());
//        if (!(weapon && mc.thePlayer.getCurrentEquippedItem() != null
//                && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword
//                || mc.thePlayer.getCurrentEquippedItem() != null
//                && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe)) {
//            return;
//        }
        if (weapon.getValBoolean()) {
            if (!(Minecraft.thePlayer.getCurrentEquippedItem() != null
                    && Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword
                    || Minecraft.thePlayer.getCurrentEquippedItem() != null
                    && Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe)) {
                return;
            }
        }
        this.updateSettings();
        this.setTargets();
        if (target != null && eu.isPre()) {
            this.rotate(eu);
            double distance = Minecraft.thePlayer.getDistanceToEntity(target);
            if (distance <= blockRange.getValDouble() && distance <= rangeValue.getValDouble() && autoblock.getValBoolean()
                    && Minecraft.thePlayer.getCurrentEquippedItem() != null
                    && Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                this.availableBlock = true;
            } else {
                this.availableBlock = false;
            }
            if (distance <= blockRange.getValDouble() && autoblock.getValBoolean() && Minecraft.thePlayer.getCurrentEquippedItem() != null
                    && Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword
                    && !autoblockType.getValString().equalsIgnoreCase("Vanilla")) {
                Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.getCurrentEquippedItem(),
                        Minecraft.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
            }

            if (this.availableBlock && !this.executingblock) {
                this.block();
            }
            if (distance <= rangeValue.getValDouble() && this.attackTimer.hasReached(1000 / cps.getValDouble())) {
                if (this.executingblock) {
                    this.unBlock();
                }
                Minecraft.thePlayer.swingItem();
                if (crack.getValBoolean()) {
                    for (int i = 0; i < crackSize.getValDouble(); i++) {
                        mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT);
                        mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT_MAGIC);
                    }
                }
                if (ThreadLocalRandom.current().nextInt(0, 100) <= hitChance.getValDouble()) {
                    isAttackTick = true;
                    attack();

                }
                if (this.availableBlock) {
                    this.block();
                }
                this.attackTimer.reset();
            } else {
                isAttackTick = false;
            }
        } else if (eu.isPre()) {
            if (this.executingblock) {
                this.unBlock();

            }
        }
    }

    private void rotate(EventUpdate eu) {
        float[] rots = RotationUtils.faceTarget(target, 1000F, 1000F, false);
        if (rotationsMode.getValString().equalsIgnoreCase("Normal")) {
            eu.setYaw(rots[0]);
            eu.setPitch(rots[1]);
        }
    }

    @Override
    public void onEnable() {
        this.targets.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Timer.timerSpeed = 1f;
        if (this.executingblock) {
            this.block();
            this.unBlock();
        }
        super.onDisable();
    }

    private void setTargets() {
        this.targets = getTargets();
        if (targets.isEmpty() || (target != null && !targets.contains(target))) {
            target = null;
            return;
        }
        if (modeType.getValString().equalsIgnoreCase("single") || !this.switchTimer.hasReached(switchDelay.getValInt())) {
            if (target != null && this.isValid(target)) {
                return;
            } else {
                target = targets.get(0);
            }
        } else if (modeType.getValString().equalsIgnoreCase("switch")) {
            if (target != null && this.isValid(target) && this.targets.size() == 1) {
                return;
            } else if (target == null) {
                target = targets.get(0);
            } else if (this.targets.size() > 1) {
                int maxIndex = this.targets.size() - 1;
                if (this.currentIndex >= maxIndex) {
                    this.currentIndex = 0;
                } else {
                    this.currentIndex += 1;
                }
                if (targets.get(currentIndex) != null && targets.get(currentIndex) != target) {
                    target = targets.get(currentIndex);
                    this.switchTimer.reset();
                }
            } else {
                target = null;
            }
        }
    }

    private int getTargetInt() {
        for (int k = 0; k < this.targets.size(); k++) {
            if (this.targets.get(k) == target) {
                return k;
            }
        }
        return -1;
    }

    private List<EntityLivingBase> getTargets() {
        ArrayList<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
        for (Entity e : Minecraft.theWorld.loadedEntityList) {
            if (this.isValid(e)) {
                targets.add((EntityLivingBase) e);
            }
        }
        return targets;
    }

    private boolean isValid(Entity target) {
        if (!(target instanceof EntityLivingBase) || target == Minecraft.thePlayer || !target.isEntityAlive()
                || target instanceof EntityArmorStand)
            return false;
        if (target instanceof EntityPlayer && !players.getValBoolean()) {
            return false;
        } else if (target instanceof EntityPlayer && players.getValBoolean() && target.isInvisible() && !invisibles.getValBoolean()) {
            return false;
        } else if (target instanceof EntityMob && !mobs.getValBoolean()) {
            return false;
        } else if (target instanceof EntityAnimal && !animals.getValBoolean()) {
            return false;
        } else if (!(target instanceof EntityAnimal) && !(target instanceof EntityMob)
                && !(target instanceof EntityPlayer) && !others.getValBoolean()) {
            return false;
        } else if (Minecraft.thePlayer.getDistanceToEntity(target) > Math.max(rangeValue.getValDouble(), blockRange.getValDouble())) {
            return false;
        }
        if (target instanceof EntityPlayer && teams.getValBoolean() && getTabName((EntityPlayer) target).length() > 2
                && getTabName(Minecraft.thePlayer).startsWith(getTabName((EntityPlayer) target).substring(0, 2))) {
            return false;
        } else if (Summer.INSTANCE.friendManager.isFriend(target.getName()) && friends.getValBoolean()) {
            return false;
        } else if (AntiBot.isBot((EntityPlayer) target)) {
            return false;
        }
        return true;
    }

    public static String getTabName(EntityPlayer player) {
        String realName = "";
        for (Object o5 : mc.ingameGUI.getTabList().getPlayerList()) {
            NetworkPlayerInfo o = (NetworkPlayerInfo) o5;
            String mcName = mc.ingameGUI.getTabList().getPlayerName(o);
            if ((mcName.contains(player.getName())) && (player.getName() != mcName)) {
                realName = mcName;
            }
        }
        return realName;
    }

    @EventTarget
    public void onPacket(EventPacket ep) {
        if (ep.isSending()) {
            Packet packet = ep.getPacket();
            if (packet instanceof C07PacketPlayerDigging) {
                this.executingblock = false;
            } else if (packet instanceof C08PacketPlayerBlockPlacement) {
                this.executingblock = true;
            }
        }
    }

    private static boolean qualifies(final Entity e) {
        return e != KillAura.mc.thePlayer
                && (CombatUtils.canAttackEntity(e, teams.getValBoolean())
                && e.isEntityAlive()
                && ((e instanceof EntityPlayer && players.getValBoolean()) || (e instanceof EntityMob && mobs.getValBoolean())
                || (e instanceof EntityAnimal && animals.getValBoolean()) || (e.isInvisible() && !invisibles.getValBoolean())));
    }

    public static boolean attack() {
        if (!qualifies(KillAura.target)) {
            mc.timer.timerSpeed = 1f;
            KillAura.target = null;
            if (crack.getValBoolean()) {
                for (int i = 0; i < crackSize.getValDouble(); ++i) {
                    mc.thePlayer.onEnchantmentCritical(finalTarget);
                    ++i;
                }
            }

        }
        if (target == null) {
            Timer.timerSpeed = 1f;
            return false;
        }
        Timer.timerSpeed = 1f;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
        Timer.timerSpeed = 1f;
        return true;
    }


    private void unBlock() {
        if (autoblockType.getValString().equalsIgnoreCase("Watchdog")) {
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    new BlockPos(-1, -1, -1),
                    EnumFacing.DOWN));
        }
    }

    private void block() {
        if (autoblockType.getValString().equalsIgnoreCase("Watchdog")) {
            double value = -1;
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(value, value, value), 255, mc.thePlayer.inventory.getCurrentItem(), 0, 0, 0));
        }
    }
}
