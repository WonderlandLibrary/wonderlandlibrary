package info.sigmaclient.module.impl.combat;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.TeamUtils;
import info.sigmaclient.util.Timer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Killaura extends Module {
    private static final String AUTOBLOCK = "AUTOBLOCK";
    private static final String RANGE = "RANGE";
    private static final String PLAYERS = "PLAYERS";
    private static final String ANIMALS = "OTHERS";
    private static final String ARM = "ARMOR";
    private static final String TEAMS = "TEAMS";
    private final String INVISIBLES = "INVISIBLES";
    private String TICK = "EXISTED";
    private String MAX = "MAXAPS";
    private String MIN = "MINAPS";
    private String DEATH = "DEATH";
    private String TARGETMODE = "PRIORITY";
    private String AURAMODE = "MODE";
    private String FOVCHECK = "FOV";
    private String WEAPONS = "WEAPONS";
    private Timer delay = new Timer();
    private Timer deathTimer = new Timer();
    private Timer switchTimer = new Timer();
    private List<EntityLivingBase> loaded = new CopyOnWriteArrayList<>();
    private int index;
    private static boolean canJump;

    public static boolean isSetupTick() {
        return canJump;
    }

    public Killaura(ModuleData data) {
        super(data);
        settings.put(FOVCHECK, new Setting<>(FOVCHECK, 360, "Targets must be in FOV.", 15, 45, 360));
        settings.put(TICK, new Setting<>(TICK, 50, "Existed ticks before attacking.", 5, 1, 120));
        settings.put(AUTOBLOCK, new Setting<>(AUTOBLOCK, true, "Automatically blocks for you."));
        settings.put(RANGE, new Setting<>(RANGE, 4.5, "Range for killaura.", 0.1, 1, 8));
        settings.put(MIN, new Setting<>(MIN, 5, "Minimum APS.", 1, 1, 20));
        settings.put(MAX, new Setting<>(MAX, 5, "Maximum APS.", 1, 1, 20));
        settings.put(PLAYERS, new Setting<>(PLAYERS, true, "Attack players."));
        settings.put(ANIMALS, new Setting<>(ANIMALS, false, "Attack Animals."));
        settings.put(ARM, new Setting<>(ARM, true, "Check if player has armor equipped."));
        settings.put(TEAMS, new Setting<>(TEAMS, false, "Check if player is not on your team."));
        settings.put(INVISIBLES, new Setting<>(INVISIBLES, false, "Attack invisibles."));
        settings.put(DEATH, new Setting<>(DEATH, true, "Disables killaura when you die."));
        settings.put(WEAPONS, new Setting<>(WEAPONS, false, "Only attack when weapon is equipped."));
        settings.put(TARGETMODE, new Setting<>(TARGETMODE, new Options("Priority", "Angle", new String[]{"Angle", "Range", "FOV", "Armor"}), "Target mode priority."));
        settings.put(AURAMODE, new Setting<>(AURAMODE, new Options("Mode", "Switch", new String[]{"Tick2", "Tick", "Switch", "Single", "Vanilla"}), "Attack method for the aura."));
    }

    public static int randomNumber(int max, int min) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public static EntityLivingBase target;
    public static EntityLivingBase vip;
    static boolean allowCrits;

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        loaded.clear();
        disabled = false;
        allowCrits = true;
    }

    @Override
    public void onEnable() {
        target = null;
        loaded.clear();
        disabled = false;
        allowCrits = true;
    }

    private boolean disabled;

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class})
    public void onEvent(Event event) {
        setSuffix(((Options) settings.get(AURAMODE).getValue()).getSelected());
        allowCrits = !(getSuffix().contains("Tick") || getSuffix().equals("Single"));
        int min = ((Number) settings.get(MIN).getValue()).intValue();
        int max = ((Number) settings.get(MAX).getValue()).intValue();
        int delayValue = (20 / randomNumber(min,max)) * 50;
        boolean block = (Boolean) settings.get(AUTOBLOCK).getValue();
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (canJump && em.isPre()) {
                canJump = false;
            }
            if (em.isPre() && (Boolean) settings.get(DEATH).getValue()) {
                if (!mc.thePlayer.isEntityAlive() && !disabled) {
                    toggle();
                    deathTimer.reset();
                    Notifications.getManager().post("Aura Death", "Aura disabled due to death.");
                }
                if (disabled && deathTimer.delay(10000)) {
                    disabled = false;
                }
                if (disabled) {
                    return;
                }

            }
            if (em.isPre() && getSuffix().contains("Tick")) {
                updateTicks();
            }
            if (AutoPot.potting && AutoPot.haltTicks > 4) {
                return;
            }
            switch (((Options) settings.get(AURAMODE).getValue()).getSelected()) {
                case "Vanilla":
                    if (em.isPre()) {
                        int entityCount = 0;
                        for (Object o : mc.theWorld.loadedEntityList)
                            if (o instanceof EntityLivingBase) {
                                EntityLivingBase ent = (EntityLivingBase) o;
                                if (validEntity(ent) && entityCount < 5 && ent != mc.thePlayer) {
                                    entityCount++;
                                    if (mc.thePlayer.inventory.getCurrentItem() != null && block && (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword)) {
                                        mc.thePlayer.setItemInUse(mc.thePlayer.inventory.getCurrentItem(), 71999);
                                    }
                                    if (delay.delay(delayValue)) {
                                        mc.thePlayer.swingItem();
                                        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
                                        if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().isItemEnchanted()) {
                                            mc.thePlayer.onEnchantmentCritical(ent);
                                        }
                                        delay.reset();
                                    }
                                }
                            }
                    }
                    break;
                case "Single": {
                    if (em.isPre()) {
                        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && mc.getIntegratedServer() == null) {
                            Notifications.getManager().post("Auto Config", "Single gets you watchdog banned.", Notifications.Type.WARNING);
                            ((Options) settings.get(AURAMODE).getValue()).setSelected("Switch");
                        }
                        target = getOptimalTarget();
                        if (target != null) {
                            if (block && (mc.thePlayer.inventory.getCurrentItem() != null) && ((mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword))) {
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                            }
                            float[] rotations = RotationUtils.getRotations(target);
                            em.setYaw(rotations[0]);
                            em.setPitch(rotations[1]);
                            boolean crits = Client.getModuleManager().isEnabled(Criticals.class) && !((Options) Client.getModuleManager().get(Criticals.class).getSetting("MODE").getValue()).getSelected().equals("Jump");
                            if (mc.thePlayer.ticksExisted % 2 == 0) {
                                if (crits && (mc.thePlayer.isCollidedVertically || mc.thePlayer.onGround) && (!Client.getModuleManager().isEnabled(Bhop.class))) {
                                    canJump = true;
                                    em.setY(em.getY() + 0.062511D);
                                    em.setGround(false);
                                }
                            } else {
                                if (crits && (em.getY() == mc.thePlayer.posY && em.isOnground()) && (!Client.getModuleManager().isEnabled(Bhop.class))) {
                                    em.setGround(false);
                                    em.setAlwaysSend(true);
                                }
                            }
                        }
                    } else if (em.isPost() && mc.thePlayer.ticksExisted % 2 != 0 && (target != null)) {
                        if (mc.thePlayer.isBlocking() && block && (mc.thePlayer.inventory.getCurrentItem() != null) && ((mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword))) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                        }
                        mc.thePlayer.swingItem();
                        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                        float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.inventory.getCurrentItem(), target.getCreatureAttribute());
                        if (sharpLevel > 0.0F) {
                            mc.thePlayer.onEnchantmentCritical(target);
                        }
                        if (mc.thePlayer.isBlocking() && block || mc.gameSettings.keyBindUseItem.getIsKeyPressed() && (mc.thePlayer.inventory.getCurrentItem() != null) && ((mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword))) {
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                        }
                    }
                    break;
                }
                case "Tick2": {
                    if (em.isPre()) {
                        target = getOptimalTarget();
                        if (target != null) {
                            if (mc.thePlayer.inventory.getCurrentItem() != null && block && (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword)) {
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                            }
                            float[] r = RotationUtils.getRotations(target);
                            em.setPitch(r[1]);
                            em.setYaw(r[0]);
                        }
                    } else {
                        if (target != null && delay.delay(100) && target.waitTicks < 8) {
                            if (mc.thePlayer.isBlocking() && block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            attack(target, false);
                            attack(target, true);
                            target.waitTicks = 10;
                            if (mc.thePlayer.isBlocking() && block || mc.gameSettings.keyBindUseItem.getIsKeyPressed()) {
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                            }
                            delay.reset();
                            if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.isBlocking() && block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                            }
                        }
                    }
                    break;
                }
                case "Tick": {
                    if (em.isPre()) {
                        target = getOptimalTarget();
                        if (target != null) {
                            if (mc.thePlayer.getCurrentEquippedItem() != null && block && (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)) {
                                mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999);
                            }
                            float[] r = RotationUtils.getRotations(target);
                            em.setYaw(r[0]);
                            em.setPitch(r[1]);
                        }
                    } else {
                        if (target != null && target.waitTicks < 1 && delay.delay(450)) {
                            if (mc.thePlayer.isBlocking() && block) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            swap(9, mc.thePlayer.inventory.currentItem);
                            attack(target, false);
                            attack(target, true);
                            swap(9, mc.thePlayer.inventory.currentItem);
                            attack(target, false);
                            attack(target, true);
                            if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.isBlocking() && block || mc.gameSettings.keyBindUseItem.getIsKeyPressed()) {
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                            }
                            target.waitTicks = 10;
                            delay.reset();
                        }
                    }
                    break;
                }
                case "Switch": {
                    if (em.isPre()) {
                        if (switchTimer.delay(300)) {
                            loaded = getTargets();
                        }
                        if (index >= loaded.size()) {
                            index = 0;
                        }
                        if (loaded.size() > 0) {
                            if (switchTimer.delay(300)) {
                                incrementIndex();
                                switchTimer.reset();
                            }
                            EntityLivingBase target = loaded.get(index);
                            if (target != null) {
                                if(!validEntity(target)) {
                                    loaded = getTargets();
                                    incrementIndex();
                                    return;
                                }
                                if ((block) && (mc.thePlayer.inventory.getCurrentItem() != null) && ((mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword))) {
                                    mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 71999);
                                }
                                float[] rotations = RotationUtils.getRotations(target);
                                em.setYaw(rotations[0]);
                                em.setPitch(rotations[1]);
                            }
                        }
                    } else if (em.isPost() && delay.delay(delayValue) && (loaded.size() > 0) && (loaded.get(index) != null)) {
                        EntityLivingBase target = loaded.get(index);
                        if(!validEntity(target)) {
                            loaded = getTargets();
                            incrementIndex();
                            return;
                        }
                        if (mc.thePlayer.isBlocking() && block) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        mc.thePlayer.swingItem();
                        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                        float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.inventory.getCurrentItem(), target.getCreatureAttribute());
                        if (sharpLevel > 0.0F) {
                            mc.thePlayer.onEnchantmentCritical(target);
                        }
                        if (mc.thePlayer.isBlocking() && block || mc.gameSettings.keyBindUseItem.getIsKeyPressed() && mc.thePlayer.getCurrentEquippedItem() != null) {
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                        }
                        delay.reset();
                    }
                    break;
                }
            }
        }
    }

     /*   if (event instanceof EventPacket) {
        EventPacket ep = (EventPacket) event;
        float range = ((Number) settings.get(RANGE).getValue()).floatValue();
        if (ep.isOutgoing() && ep.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity packet = (C02PacketUseEntity) ep.getPacket();
            if (send && packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                send = false;
                return;
            }
        }
        if (ep.isOutgoing() && target != null && ep.getPacket() instanceof C03PacketPlayer && range > 6 && mc.thePlayer.getDistanceToEntity(target) > 5.5) {
            C03PacketPlayer packet = (C03PacketPlayer) ep.getPacket();
            packet.x = target.posX;
            packet.y = target.posY + 2;
            packet.z = target.posZ / 2;
        }
        if (ep.isOutgoing() && ep.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook && psilent && target != null) {
            C03PacketPlayer.C05PacketPlayerLook packet = (C03PacketPlayer.C05PacketPlayerLook) ep.getPacket();
            C03PacketPlayer.C05PacketPlayerLook packet = (C03PacketPlayer.C05PacketPlayerLook) ep.getPacket();
            float[] rotations = RotationUtils.getRotations(target);
            mc.thePlayer.sendQueue.addToSendQueue();(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], true));
            send = true;
            mc.thePlayer.sendQueue.addToSendQueue();(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        }
    }*/

    private void incrementIndex() {
        index += 1;
        if (index >= loaded.size()) {
            index = 0;
        }
    }

    private void updateTicks() {
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) o;
                ent.waitTicks--;
            }
        }
    }


    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    public void attack(Entity ent, boolean crits) {
        mc.thePlayer.swingItem();
        if (crits) {
            Criticals.doCrits();
        } else {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
        float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.inventory.getCurrentItem(), target.getCreatureAttribute());
        if (sharpLevel > 0.0F) {
            mc.thePlayer.onEnchantmentCritical(target);
        }
    }

    private EntityLivingBase getOptimalTarget() {

        List<EntityLivingBase> load = new ArrayList<>();
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) o;
                if (validEntity(ent)) {
                    if (ent == vip) {
                        return ent;
                    }
                    load.add(ent);
                }
            }
        }
        if (load.isEmpty()) {
            return null;
        }
        return getTarget(load);
    }

    private boolean validEntity(EntityLivingBase entity) {
        float range = ((Number) settings.get(RANGE).getValue()).floatValue();
        boolean players = (Boolean) settings.get(PLAYERS).getValue();
        boolean animals = (Boolean) settings.get(ANIMALS).getValue();
        if ((mc.thePlayer.isEntityAlive()) && (entity.isEntityAlive())) {
            if (mc.thePlayer.getDistanceToEntity(entity) <= (mc.thePlayer.canEntityBeSeen(entity) ? range : 3.5) && entity.ticksExisted > ((Number) settings.get(TICK).getValue()).intValue()) {
                if (!isInFOV(entity)) {
                    return false;
                }
                if (AntiBot.getInvalid().contains(entity)) {
                    return false;
                }
                if (entity.isPlayerSleeping()) {
                    return false;
                }
                if (entity instanceof EntityPlayer && players) {
                    EntityPlayer ent = (EntityPlayer) entity;
                    boolean armor = (Boolean) settings.get(ARM).getValue();
                    return !(TeamUtils.isTeam(mc.thePlayer, ent) && (Boolean) settings.get(TEAMS).getValue()) && !(ent.isInvisible() && !(Boolean) settings.get(INVISIBLES).getValue()) && !(armor && !hasArmor(ent)) && !FriendManager.isFriend(ent.getName());
                }
                if (entity instanceof EntityMob && animals) {
                    return true;
                }
                if (entity instanceof EntityAnimal && animals) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasArmor(EntityPlayer player) {
        ItemStack boots = player.inventory.armorInventory[0];
        ItemStack pants = player.inventory.armorInventory[1];
        ItemStack chest = player.inventory.armorInventory[2];
        ItemStack head = player.inventory.armorInventory[3];
        return (boots != null) || (pants != null) || (chest != null) || (head != null);
    }

    private void sortList(List<EntityLivingBase> weed) {
        //TODO: Weigh the fatties, make sure that it's the best fatty <3
        String current = ((Options) settings.get(TARGETMODE).getValue()).getSelected();
        switch (current) {
            case "Range":
                weed.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
                break;
            case "FOV":
                weed.sort(Comparator.comparingDouble(o -> (RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, RotationUtils.getRotations(o)[0]))));
                break;
            case "Angle":
                weed.sort((o1, o2) -> {
                    float[] rot1 = RotationUtils.getRotations(o1);
                    float[] rot2 = RotationUtils.getRotations(o2);
                    return (int) ((mc.thePlayer.rotationYaw - rot1[0])
                            - (mc.thePlayer.rotationYaw - rot2[0]));
                });
                break;
            case "Health":
                weed.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
                break;
            case "Armor":
                weed.sort(Comparator.comparingInt(o -> (o instanceof EntityPlayer ? ((EntityPlayer) o).inventory.getTotalArmorValue() : (int) o.getHealth())));
                break;
        }
    }

    private EntityLivingBase getTarget(List<EntityLivingBase> list) {
        sortList(list);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private List<EntityLivingBase> getTargets() {
        List<EntityLivingBase> targets = new ArrayList<>();
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) o;
                if (validEntity(entity)) {
                    targets.add(entity);
                }
            }
        }
        sortList(targets);
        return targets;
    }

    private boolean isInFOV(EntityLivingBase entity) {
        int fov = ((Number) settings.get(FOVCHECK).getValue()).intValue();
        return RotationUtils.getYawChange(entity.posX, entity.posZ) <= fov && RotationUtils.getPitchChange(entity, entity.posY) <= fov;
    }
}
