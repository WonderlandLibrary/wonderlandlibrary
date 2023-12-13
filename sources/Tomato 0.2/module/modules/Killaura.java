/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventClick;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.events.EventPostMotionUpdates;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.files.FileManager;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.module.modules.AutoPot;
import cow.milkgod.cheese.module.modules.CivBreak;
import cow.milkgod.cheese.module.modules.Criticals;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.CombatUtils;
import cow.milkgod.cheese.utils.EntityUtil;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Killaura
extends Module {
    public static Property<Boolean> multiaura;
    public static Property<Boolean> aimbot;
    public static Property<Boolean> autoblock;
    public static Property<Boolean> animals;
    public static Property<Boolean> enemy;
    public static Property<Boolean> friends;
    public static Property<Boolean> lockview;
    public static Property<Boolean> monsters;
    public static Property<Boolean> neutral;
    public static Property<Boolean> players;
    public static Property<Boolean> duration;
    public static Property<Integer> ticksExisted;
    public static Property<Double> reach;
    public static Property<Integer> multivalue;
    public static Property<Integer> APS;
    private Property<TargetingMode> tMode;
    public static ArrayList<Entity> targets;
    public static ArrayList<Entity> attackList;
    public static Property<Boolean> bypass;
    public static Property<Double> switchDelay;
    private static ArrayList<Entity> multiTargets;
    public static int currentTarget;
    public static boolean attacking;
    public static boolean testBool;
    private int attackDelay;
    private int testInt;
    public static float yaw;
    public static float pitch;
    public static boolean duraX2;
    private int counter;
    private static /* synthetic */ int[] $SWITCH_TABLE$cow$milkgod$cheese$module$modules$Killaura$TargetingMode;

    public Killaura() {
        super("Kill Aura", 37, Category.COMBAT, 13369344, true, "Automatically attack entities that are within range", new String[]{"aura", "ka", "kaura", "killa", "killaura"});
        multiTargets = new ArrayList();
        multiaura = new Property<Boolean>(this, "multiaura", false);
        aimbot = new Property<Boolean>(this, "Aimbot", true);
        autoblock = new Property<Boolean>(this, "Autoblock", true);
        animals = new Property<Boolean>(this, "Animals", false);
        enemy = new Property<Boolean>(this, "Enemy", true);
        friends = new Property<Boolean>(this, "Friends", false);
        lockview = new Property<Boolean>(this, "Lockview", false);
        monsters = new Property<Boolean>(this, "Monsters", false);
        neutral = new Property<Boolean>(this, "Neutral", true);
        players = new Property<Boolean>(this, "Players", true);
        duration = new Property<Boolean>(this, "Duration", false);
        ticksExisted = new Property<Integer>(this, "Ticks", 30);
        reach = new Property<Double>(this, "Reach", 3.8);
        APS = new Property<Integer>(this, "APS", 10);
        switchDelay = new Property<Double>(this, "Switch Delay", 2.1);
        multivalue = new Property<Integer>(this, "Multi Value", 5);
        targets = new ArrayList();
        attackList = new ArrayList();
        bypass = new Property<Boolean>(this, "Bypass", false);
        this.tMode = new Property<TargetingMode>(this, "TargetingMode", TargetingMode.Angle);
        this.attackDelay = 0;
    }

    private boolean isValidTarget(Entity en2) {
        if (!en2.isDead && en2.isEntityAlive() && (double)Wrapper.getPlayer().getDistanceToEntity(en2) <= (Double)Killaura.reach.value && targets.contains(en2) && en2 != Wrapper.getPlayer() && en2.ticksExisted >= (Integer)Killaura.ticksExisted.value && (FriendManager.isFriend(en2.getName()) && ((Boolean)Killaura.friends.value).booleanValue() || ((Boolean)Killaura.neutral.value).booleanValue() && !FriendManager.isFriend(en2.getName()))) {
            return true;
        }
        return false;
    }

    @EventTarget(value=1)
    public void onRePreEvent(EventPreMotionUpdates evet) {
        this.onEvent(evet);
    }

    @EventTarget
    public void onRePost(EventPostMotionUpdates evet) {
        this.onPost(evet);
    }

    @EventTarget
    public void onREPacket(EventPacketSent sent) {
        this.onPacket(sent);
    }

    @EventTarget
    public void onReClick(EventClick click) {
        this.onClick(click);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (multiaura.getValue().booleanValue()) {
            this.setDisplayName("KillAura\u00a77 - Multi");
        } else {
            this.setDisplayName("KillAura\u00a77 - Switch");
        }
    }

    public void onEvent(EventPreMotionUpdates event) {
        Entity e2;
        for (Object o22 : Wrapper.mc.theWorld.loadedEntityList) {
            Entity e22 = (Entity)o22;
            if (((Boolean)Killaura.animals.value).booleanValue() && e22 instanceof EntityAnimal && !targets.contains(e22)) {
                targets.add(e22);
            }
            if (((Boolean)Killaura.players.value).booleanValue() && e22 instanceof EntityPlayer && !targets.contains(e22)) {
                targets.add(e22);
            }
            if (((Boolean)Killaura.monsters.value).booleanValue() && e22 instanceof EntityMob && !targets.contains(e22)) {
                targets.add(e22);
            }
            if (!((Boolean)Killaura.animals.value).booleanValue() && targets.contains(e22) && e22 instanceof EntityAnimal) {
                targets.remove(e22);
            }
            if (!((Boolean)Killaura.players.value).booleanValue() && targets.contains(e22) && e22 instanceof EntityPlayer) {
                targets.remove(e22);
            }
            if (((Boolean)Killaura.monsters.value).booleanValue() || !targets.contains(e22) || !(e22 instanceof EntityMob)) continue;
            targets.remove(e22);
        }
        if (currentTarget >= attackList.size()) {
            currentTarget = 0;
        }
        for (Object o : Wrapper.mc.theWorld.loadedEntityList) {
            e2 = (Entity)o;
            if (!this.isValidTarget(e2) || attackList.contains(e2)) continue;
            attackList.add(e2);
        }
        for (Object o2 : attackList) {
            e2 = (Entity)o2;
            if (this.isValidTarget(e2) || !attackList.contains(e2)) continue;
            attackList.remove(e2);
        }
        for (Entity e22 : multiTargets) {
            if (this.isValidTarget(e22) || !multiTargets.contains(e22)) continue;
            multiTargets.remove(e22);
        }
        if (!this.canAttack()) {
            attacking = false;
            return;
        }
        this.sortTargets();
        attacking = true;
        if (((Boolean)Killaura.aimbot.value).booleanValue() && !((Boolean)Killaura.multiaura.value).booleanValue()) {
            event.setYaw(CombatUtils.faceTarget(getCurrentTarget(), 1000.0F, 1000.0F)[0]);
            event.setPitch(CombatUtils.faceTarget(getCurrentTarget(), 1000.0F, 1000.0F)[1]);

            yaw = event.getYaw();
            pitch = event.getPitch();
            EntityPlayerSP player = Wrapper.getPlayer();
            player.rotationPitch += 9.0E-5f;
            if (((Boolean)Killaura.lockview.value).booleanValue()) {
                Wrapper.mc.thePlayer.rotationYaw = yaw;
                Wrapper.mc.thePlayer.rotationPitch = pitch;
            }
        }
        ++this.attackDelay;
    }

    private void doMultiAttack(int targets) {
        int i2 = 0;
        while (i2 < attackList.size()) {
            this.sortTargets();
            Wrapper.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(CombatUtils.faceTarget(attackList.get(i2), Float.MAX_VALUE, Float.MAX_VALUE)[0], CombatUtils.faceTarget(attackList.get(i2), Float.MAX_VALUE, Float.MAX_VALUE)[1], Wrapper.mc.thePlayer.onGround));
            Cheese.getInstance();
            this.doAttack(attackList.get(i2), Cheese.moduleManager.getModbyName("Criticals").getState());
            if (i2 > targets) break;
            ++i2;
        }
    }

    private void doAttack(Entity ent, boolean crit) {
        if (Wrapper.getPlayer().isBlocking()) {
            Wrapper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
        }
        Wrapper.mc.thePlayer.swingItem();
        if (crit) {
            Criticals.doCrits();
        } else {
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
        }
        float sharpLevel = EnchantmentHelper.func_152377_a(Wrapper.mc.thePlayer.getHeldItem(), ((EntityLivingBase)ent).getCreatureAttribute());
        boolean vanillaCrit = Wrapper.mc.thePlayer.fallDistance > 0.0f && !Wrapper.mc.thePlayer.onGround && !Wrapper.mc.thePlayer.isOnLadder() && !Wrapper.mc.thePlayer.isInWater() && !Wrapper.mc.thePlayer.isPotionActive(Potion.blindness) && Wrapper.mc.thePlayer.ridingEntity == null;
        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            Wrapper.mc.thePlayer.onCriticalHit(ent);
        }
        if (sharpLevel > 0.0f) {
            Wrapper.mc.thePlayer.onEnchantmentCritical(ent);
        }
    }

    protected void swap(int slot, int hotbarNum) {
        Wrapper.mc.playerController.windowClick(Wrapper.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Wrapper.mc.thePlayer);
    }

    public void onPost(EventPostMotionUpdates post) {
        if (!this.canAttack() || !attacking) {
            return;
        }
        if (this.canBlock()) {
            Wrapper.getPlayer().setItemInUse(Wrapper.getPlayer().getHeldItem(), 71999);
        }
        if (this.attackDelay >= 20 / (Integer)Killaura.APS.value) {
            ++this.counter;
            if (((Boolean)Killaura.duration.value).booleanValue() && !((Boolean)Killaura.multiaura.value).booleanValue() && this.counter != 10) {
                this.swap(9, Wrapper.mc.thePlayer.inventory.currentItem);
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                Cheese.getInstance();
                this.doAttack(Killaura.getCurrentTarget(), Cheese.moduleManager.getModState("Criticals"));
                this.swap(9, Wrapper.mc.thePlayer.inventory.currentItem);
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
            if (!((Boolean)Killaura.multiaura.value).booleanValue()) {
                Cheese.getInstance();
                this.doAttack(Killaura.getCurrentTarget(), Cheese.moduleManager.getModbyName("Criticals").getState());
            }
            if (((Boolean)Killaura.multiaura.value).booleanValue()) {
                this.doMultiAttack((Integer)Killaura.multivalue.value);
            }
            this.changeTargets();
            this.attackDelay = 0;
        }
        if (this.canBlock() || Wrapper.mc.thePlayer.isBlocking()) {
            Wrapper.sendPacket(new C08PacketPlayerBlockPlacement(Wrapper.mc.thePlayer.inventory.getCurrentItem()));
        }
    }

    private void changeTargets() {
        if (attackList.size() == 1) {
            return;
        }
        if ((Double)Killaura.switchDelay.value == 0.0) {
            ++currentTarget;
        }
        if ((double)this.counter >= (Double)Killaura.switchDelay.value && (Double)Killaura.switchDelay.value > 0.0) {
            ++currentTarget;
            this.counter = 0;
        }
        if (!this.isValidTarget(Killaura.getCurrentTarget())) {
            this.counter = 0;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        attackList.clear();
        currentTarget = 0;
        if (!Wrapper.isMoving()) {
            Wrapper.sendPacket(new C03PacketPlayer.C05PacketPlayerLook());
        }
    }

    public void onPacket(EventPacketSent send) {
        if (!((Boolean)Killaura.duration.value).booleanValue() || !attacking) {
            return;
        }
        if (AutoPot.CurrentlyPotting() || !attacking) {
            return;
        }
        if (send.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)send.getPacket();
            if (player.yaw == yaw && player.pitch == pitch) {
                return;
            }
            player.yaw = yaw;
            player.pitch = pitch;
            if (send.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
                C03PacketPlayer.C05PacketPlayerLook look = (C03PacketPlayer.C05PacketPlayerLook)send.getPacket();
                look.yaw = yaw;
                look.pitch = pitch;
            } else if (send.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                C03PacketPlayer.C06PacketPlayerPosLook posLook = (C03PacketPlayer.C06PacketPlayerPosLook)send.getPacket();
                posLook.yaw = yaw;
                posLook.pitch = pitch;
            }
        }
    }

    public void sortTargets() {
        switch (this.tMode.value) {
        case Angle: {
                attackList.sort((ent1, ent2) -> {
                    double rB;
                    double rA = EntityUtil.getRotationToEntity(ent1)[0];
                    return rA < (rB = EntityUtil.getRotationToEntity(ent2)[0]) ? 1 : (rA == rB ? 0 : -1);
                }
                );
                break;
            }
            case Distance: {
                attackList.sort((ent1, ent2) -> {
                    double d2;
                    double d1 = Wrapper.getPlayer().getDistanceToEntity(ent1);
                    return d1 < (d2 = (double)Wrapper.getPlayer().getDistanceToEntity(ent2)) ? -1 : (d1 == d2 ? 0 : 1);
                }
                );
                break;
            }
            case Health: {
                attackList.sort((ent1, ent2) -> {
                    double h2;
                    double h1 = ((EntityLivingBase)ent1).getHealth();
                    return h1 < (h2 = (double)((EntityLivingBase)ent2).getHealth()) ? -1 : (h1 == h2 ? 0 : 1);
                }
                );
            }
        }
    }

    public static EntityLivingBase getCurrentTarget() {
        return (EntityLivingBase)attackList.get(currentTarget);
    }

    public static boolean isKillauraActive() {
        Cheese.getInstance();
        if (Cheese.moduleManager.getModState("Killaura") && attacking && !attackList.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean canBlock() {
        if (Wrapper.mc.thePlayer.getHeldItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && Wrapper.mc.thePlayer.isSwingInProgress && autoblock.getValue().booleanValue()) {
            return true;
        }
        return false;
    }

    private boolean canAttack() {
        if (!attackList.isEmpty()) {
            Cheese.getInstance();
            if (!Cheese.moduleManager.getModState("Freecam") && !CivBreak.isMining && attackList.get(currentTarget) != null) {
                return true;
            }
        }
        return false;
    }

    public void onClick(EventClick click) {
        if (!attacking) {
            return;
        }
        click.setCancelled(true);
    }

    @Override
    protected void addCommand() {
        Cheese.getInstance();
        final CommandManager commandManager = Cheese.commandManager;
        CommandManager.addCommand(new Command("Killaura", "Invalid option! Options: ", new String[] { "ka", "aura" }, "<AutoBlock | §7Animals | Monsters | §7Players | Friends | Neutral | §7Enemy | Aimbot | Lockview | APS | §7Delay | §7Targeting | Reach | Multi | Values>", "Configure Kill Aura") {
            @EventTarget
            public void onTick(final EventTick ev) {
                try {
                    final String message = EventChatSend.getMessage().split(" ")[1];
                    if (message.equalsIgnoreCase("animals") || message.equalsIgnoreCase("a")) {
                        Killaura.animals.setValue(!Killaura.animals.getValue());
                        if (!Killaura.animals.getValue()) {
                            Logger.logChat("§eAnimals§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eAnimals§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("multi") || message.equalsIgnoreCase("ma") || message.equalsIgnoreCase("multiaura")) {
                        Killaura.multiaura.setValue(!Killaura.multiaura.getValue());
                        if (!Killaura.multiaura.getValue()) {
                            Logger.logChat("§eMultiAura Mode§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eMultiAura Mode§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("monsters") || message.equalsIgnoreCase("mobs") || message.equalsIgnoreCase("m")) {
                        Killaura.monsters.setValue(!Killaura.monsters.getValue());
                        if (!Killaura.monsters.getValue()) {
                            Logger.logChat("§eMonsters§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eMonsters§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("friends") || message.equalsIgnoreCase("f") || message.equalsIgnoreCase("ally")) {
                        Killaura.friends.setValue(!Killaura.friends.getValue());
                        if (!Killaura.friends.getValue()) {
                            Logger.logChat("§eFriends§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eFriends§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("enemy") || message.equalsIgnoreCase("e")) {
                        Killaura.enemy.setValue(!Killaura.enemy.getValue());
                        if (!Killaura.enemy.getValue()) {
                            Logger.logChat("§eEnemies§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eEnemies§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("neutral") || message.equalsIgnoreCase("n")) {
                        Killaura.neutral.setValue(!Killaura.neutral.getValue());
                        if (!Killaura.neutral.getValue()) {
                            Logger.logChat("§eNeutral§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eNeutral§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("duration") || message.equalsIgnoreCase("armorbreak") || message.equalsIgnoreCase("dura") || message.equalsIgnoreCase("armorbreaker")) {
                        Killaura.duration.setValue(!Killaura.duration.getValue());
                        if (!Killaura.duration.getValue()) {
                            Logger.logChat("§eArmor Breaker§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eArmor Breaker§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("players") || message.equalsIgnoreCase("p")) {
                        Killaura.players.setValue(!Killaura.players.getValue());
                        if (!Killaura.players.getValue()) {
                            Logger.logChat("§ePlayers§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§ePlayers§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("autoblock") || message.equalsIgnoreCase("ab")) {
                        Killaura.autoblock.setValue(!Killaura.autoblock.getValue());
                        if (!Killaura.autoblock.getValue()) {
                            Logger.logChat("§eAutoBlock§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eAutoBlock§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("aimbot") || message.equalsIgnoreCase("aim")) {
                        Killaura.aimbot.setValue(!Killaura.aimbot.getValue());
                        if (!Killaura.aimbot.getValue()) {
                            Logger.logChat("§eAimbot§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eAimbot§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (message.equalsIgnoreCase("lockview") || message.equalsIgnoreCase("lw")) {
                        Killaura.lockview.setValue(!Killaura.lockview.getValue());
                        if (!Killaura.lockview.getValue()) {
                            Logger.logChat("§eLockview§7 has been §cdisabled§7 for §eKill Aura§7.");
                        }
                        else {
                            Logger.logChat("§eLockview§7 has been §aenabled§7 for §eKill Aura§7.");
                        }
                    }
                    else if (!message.equalsIgnoreCase("APS") && !message.equalsIgnoreCase("attackdelay") && !message.equalsIgnoreCase("as")) {
                        if (!message.equalsIgnoreCase("ticks") && !message.equalsIgnoreCase("t") && !message.equalsIgnoreCase("delay")) {
                            if (!message.equalsIgnoreCase("reach") && !message.equalsIgnoreCase("r") && !message.equalsIgnoreCase("range")) {
                                if (!message.equalsIgnoreCase("targeting") && !message.equalsIgnoreCase("ta")) {
                                    if (message.equalsIgnoreCase("values")) {
                                        Logger.logChat("Aimbot: §e" + String.valueOf(Killaura.aimbot.getValue()));
                                        Logger.logChat("AutoBlock: §e" + String.valueOf(Killaura.autoblock.getValue()));
                                        Logger.logChat("Animals: §e" + String.valueOf(Killaura.animals.getValue()));
                                        Logger.logChat("Enemies: §e" + String.valueOf(Killaura.enemy.getValue()));
                                        Logger.logChat("Friends: §e" + String.valueOf(Killaura.friends.getValue()));
                                        Logger.logChat("Lockview: §e" + String.valueOf(Killaura.lockview.getValue()));
                                        Logger.logChat("Monsters: §e" + String.valueOf(Killaura.monsters.getValue()));
                                        Logger.logChat("Neutrals: §e" + String.valueOf(Killaura.neutral.getValue()));
                                        Logger.logChat("Players: §e" + String.valueOf(Killaura.players.getValue()));
                                        Logger.logChat("APS: §e" + String.valueOf(Killaura.APS.getValue()));
                                        Logger.logChat("Delay: §e" + String.valueOf(Killaura.ticksExisted.getValue()));
                                        Logger.logChat("Reach: §e" + String.valueOf(Killaura.reach.getValue()));
                                        Logger.logChat("ArmorBreaker: §e" + String.valueOf(Killaura.duration.getValue()));
                                        Logger.logChat("MultiAura: §e" + String.valueOf(Killaura.multiaura.getValue()));
                                    }
                                    else {
                                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                                    }
                                }
                                else {
                                    try {
                                        final String message2 = EventChatSend.getMessage().split(" ")[2];
                                        if (message2.equalsIgnoreCase("actual")) {
                                            Logger.logChat("Actual targeting mode: §e" + String.valueOf(Killaura.this.tMode.value));
                                        }
                                        else if (message2.equalsIgnoreCase("reset")) {
                                            Killaura.this.tMode.value = Killaura.this.tMode.defaultvalue;
                                            Logger.logChat("Kill Aura targeting mode reset. Set mode to: §e" + Killaura.this.tMode.value);
                                        }
                                        else {
                                            Label_1831: {
                                                Label_1825: {
                                                    Label_1809: {
                                                        Label_1793: {
                                                            Label_1777: {
                                                                final String s;
                                                                final String s2;
                                                                switch (s2 = (s = message2)) {
                                                                    case "Health": {
                                                                        break Label_1809;
                                                                    }
                                                                    case "health": {
                                                                        break Label_1809;
                                                                    }
                                                                    case "a": {
                                                                        break Label_1793;
                                                                    }
                                                                    case "d": {
                                                                        break Label_1777;
                                                                    }
                                                                    case "hp": {
                                                                        break Label_1809;
                                                                    }
                                                                    case "Angle": {
                                                                        break Label_1793;
                                                                    }
                                                                    case "angle": {
                                                                        break Label_1793;
                                                                    }
                                                                    case "distance": {
                                                                        break Label_1777;
                                                                    }
                                                                    case "Distance": {
                                                                        break Label_1777;
                                                                    }
                                                                    default:
                                                                        break;
                                                                }
                                                                break Label_1825;
                                                            }
                                                            Killaura.this.tMode.value = TargetingMode.Distance;
                                                            break Label_1831;
                                                        }
                                                        Killaura.this.tMode.value = TargetingMode.Angle;
                                                        break Label_1831;
                                                    }
                                                    Killaura.this.tMode.value = TargetingMode.Health;
                                                    break Label_1831;
                                                }
                                                Logger.logChat("Invalid Mode! targeting <actual | reset || Angle || Distance || Health>");
                                            }
                                            Logger.logChat("Kill Aura Targeting mode set to: §e" + Killaura.this.tMode.getValue());
                                        }
                                    }
                                    catch (Exception e2) {
                                        Logger.logChat("Invalid Mode! targeting <actual | reset || Angle || Distance || Health>");
                                    }
                                }
                            }
                            else {
                                try {
                                    final String message2 = EventChatSend.getMessage().split(" ")[2];
                                    if (message2.equalsIgnoreCase("actual")) {
                                        Logger.logChat("Actual reach: §e" + String.valueOf(Killaura.reach.getValue()));
                                    }
                                    else if (message2.equalsIgnoreCase("reset")) {
                                        Killaura.reach.setValue(3.8);
                                        Logger.logChat("Kill Aura reach reset. Set reach to: §e" + Killaura.reach.getValue());
                                    }
                                    else {
                                        Killaura.reach.setValue(Double.parseDouble(message2));
                                        if (Killaura.reach.getValue() > 7.0) {
                                            Killaura.reach.setValue(7.0);
                                            Logger.logChat("Cannot make the reach higher than 7!");
                                        }
                                        else if (Killaura.reach.getValue() < 1.0) {
                                            Killaura.reach.setValue(1.0);
                                        }
                                        Logger.logChat("Kill Aura reach set to: §e" + Killaura.reach.getValue());
                                    }
                                }
                                catch (Exception e2) {
                                    Logger.logChat("Invalid Value! reach <actual | reset || reach>");
                                }
                            }
                        }
                        else {
                            try {
                                final String message2 = EventChatSend.getMessage().split(" ")[2];
                                if (message2.equalsIgnoreCase("actual")) {
                                    Logger.logChat("Actual Delay: §e" + String.valueOf(Killaura.ticksExisted.getValue()));
                                }
                                else {
                                    Killaura.ticksExisted.setValue(Integer.parseInt(message2));
                                    Logger.logChat("Kill Aura Delay set to: §e" + Killaura.ticksExisted.getValue());
                                }
                            }
                            catch (Exception e2) {
                                Logger.logChat("Invalid value! Delay <actual || Delay>");
                            }
                        }
                    }
                    else {
                        try {
                            final String message2 = EventChatSend.getMessage().split(" ")[2];
                            if (message2.equalsIgnoreCase("actual")) {
                                Logger.logChat("Actual APS: §e" + String.valueOf(Killaura.APS.getValue()));
                            }
                            else {
                                Killaura.APS.setValue(Integer.parseInt(message2));
                                if (Killaura.APS.getValue() > 20) {
                                    Killaura.APS.setValue(20);
                                    Logger.logChat("Cannot make the APS higher than 20!");
                                }
                                else if (Killaura.APS.getValue() < 1) {
                                    Killaura.APS.setValue(1);
                                }
                                Logger.logChat("Kill Aura APS set to: §e" + Killaura.APS.getValue());
                            }
                        }
                        catch (Exception e2) {
                            Logger.logChat("Invalid value! ticks <actual || APS>");
                        }
                    }
                }
                catch (Exception e) {
                    Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    e.printStackTrace();
                }
                Cheese.getInstance();
                Cheese.fileManager.saveFiles();
                if (Killaura.multiaura.getValue()) {
                    Killaura.this.setDisplayName("KillAura§7 - Multi");
                }
                else {
                    Killaura.this.setDisplayName("KillAura§7 - Switch");
                }
                this.Toggle();
            }
        });
    }
    
    private enum TargetingMode
    {
        Distance("Distance", 0, "Distance", 0), 
        Health("Health", 1, "Health", 1), 
        Angle("Angle", 2, "Angle", 2);
        
        private TargetingMode(final String s2, final int n2, final String s, final int n) {
        }
    }
}
