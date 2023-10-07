package cc.novoline.modules.combat;


import cc.novoline.Novoline;
import cc.novoline.events.EventManager;
import cc.novoline.events.EventTarget;
import cc.novoline.events.events.*;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.PlayerManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.modules.exploits.Blink;
import cc.novoline.modules.exploits.Teleport;
import cc.novoline.modules.move.Scaffold;
import cc.novoline.modules.player.Freecam;
import cc.novoline.utils.Timer;
import cc.novoline.utils.*;
import cc.novoline.utils.fonts.impl.Fonts;
import cc.novoline.utils.notifications.NotificationType;
import cc.novoline.utils.pathfinding.AStarCustomPathfinder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static cc.novoline.gui.screen.setting.SettingType.COLOR_PICKER;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createColor;

public final class KillAura extends AbstractModule {

    /* fields */
    private Entity target;
    private int switchIndex;
    private List<Entity> targetList = new CopyOnWriteArrayList<>();
    private final Timer timerAttack = new Timer(), timerSwitch = new Timer(), lossTimer = new Timer();
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private float yaw, pitch, iyaw, ipitch, prevIYaw, prevIPitch, alpha;
    private final List<Packet<?>> toDispatch = new ArrayList<>();
    private boolean blinking, blocking;

    /* properties @off */
    @Property("mode")
    private final StringProperty mode = PropertyFactory.createString("Switch").acceptableValues("Switch", "Single", "MW Infinite");
    @Property("switch-delay")
    private final FloatProperty switchDelay = PropertyFactory.createFloat(400.0F).minimum(50.0F).maximum(1000.0F);
    @Property("rejoin")
    private final IntProperty rejoin = PropertyFactory.createInt(3500).minimum(3100).maximum(5000);
    @Property("aps")
    private final DoubleProperty aps = PropertyFactory.createDouble(5.0D).minimum(1.0D).maximum(18.0D);
    @Property("range")
    private final DoubleProperty range = PropertyFactory.createDouble(3.5D).minimum(1.0D).maximum(7.0D);
    @Property("wall-range")
    private final DoubleProperty wallRange = PropertyFactory.createDouble(3.5D).minimum(1.0D).maximum(7.0D);
    @Property("block-range")
    private final DoubleProperty blockRange = PropertyFactory.createDouble(3.5D).minimum(1.0D).maximum(10.0D);
    @Property("fov-check")
    private final IntProperty fovCheck = PropertyFactory.createInt(180).minimum(1).maximum(180);
    @Property("rotations-smoothness")
    private final IntProperty smoothness = PropertyFactory.createInt(60).minimum(1).maximum(100);
    @Property("targets")
    private final ListProperty<String> targets = PropertyFactory.createList("Players").acceptableValues("Players", "Animals", "Mobs", "Invisibles");
    @Property("aura-sort")
    private final StringProperty sort = PropertyFactory.createString("Distance").acceptableValues("Distance", "Health", "Armor", "FOV", "HurtTime");
    @Property("filters")
    private final ListProperty<String> filters = PropertyFactory.createList("Teams").acceptableValues("Teams", "Users", "Armor");
    @Property("particles")
    private final ListProperty<String> particles = PropertyFactory.createList("Enchant").acceptableValues("Enchant", "Critical");
    @Property("auto-block")
    private final BooleanProperty autoBlock = PropertyFactory.booleanFalse();
    @Property("target-hud")
    private final BooleanProperty targetHud = PropertyFactory.booleanFalse();
    @Property("autodisable")
    private final ListProperty<String> autoDisable = PropertyFactory.createList("Death").acceptableValues("World Change", "Game End", "Death");
    @Property("blink")
    private final BooleanProperty blink = PropertyFactory.booleanFalse();
    @Property("raytrace")
    private final BooleanProperty raytrace = PropertyFactory.booleanFalse();
    @Property("lag-check")
    private final BooleanProperty lagCheck = PropertyFactory.booleanFalse();
    @Property("visualize-range")
    private final BooleanProperty visRange = PropertyFactory.booleanFalse();
    @Property("color")
    private final ColorProperty color = createColor(0xFFFFFFFF);
    @Property("th-x")
    private final IntProperty thx = PropertyFactory.createInt((int) screenSize.getWidth() / 4).minimum(1).maximum((int) screenSize.getWidth() / 2);
    @Property("th-y")
    private final IntProperty thy = PropertyFactory.createInt((int) screenSize.getHeight() / 4).minimum(1).maximum((int) screenSize.getHeight() / 2);
    //  @Property("hvh")
    //  private final KeyBindProperty hvh = PropertyFactory.keyboardKeyBinding(Keyboard.KEY_N);


    /* constructors @on */
    public KillAura(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Killaura", "Kill Aura", EnumModuleType.COMBAT, "Automatically attacks entities around you");
        Manager.put(new Setting("AURA_MODE", "Mode", SettingType.COMBOBOX, this, mode));
        Manager.put(new Setting("AURA_SORT", "Sort by", SettingType.COMBOBOX, this, sort, () -> mode().equalsIgnoreCase("Single") || mode.equalsIgnoreCase("MW Infinite")));
        Manager.put(new Setting("AURA_REJOIN_DELAY", "Rejoin Delay", SettingType.SLIDER, this, rejoin, 100, () -> mode.get().equals("MW Infinite")));
        Manager.put(new Setting("SWITCH_DELAY", "Switch Delay", SettingType.SLIDER, this, switchDelay, 50, () -> mode().equalsIgnoreCase("Switch")));
        Manager.put(new Setting("APS", "APS", SettingType.SLIDER, this, aps, 1.0D));
        Manager.put(new Setting("RANGE", "Range", SettingType.SLIDER, this, range, 0.1));
        Manager.put(new Setting("BLOCK_RANGE", "Block Range", SettingType.SLIDER, this, blockRange, 0.1));
        Manager.put(new Setting("WALL_RANGE", "Wall Range", SettingType.SLIDER, this, wallRange, 0.1));
        Manager.put(new Setting("AURA_FOV", "FOV Check", SettingType.SLIDER, this, fovCheck, 1));
        Manager.put(new Setting("AURA_FOV", "Angle Smoothing", SettingType.SLIDER, this, smoothness, 5));
        Manager.put(new Setting("TARGETS", "Targets", SettingType.SELECTBOX, this, targets));
        Manager.put(new Setting("KA_FILTER", "Filters", SettingType.SELECTBOX, this, filters));
        Manager.put(new Setting("KA_PARTICLES", "Particles", SettingType.SELECTBOX, this, particles));
        Manager.put(new Setting("KA_LAG_CHECK", "Lag Check", SettingType.CHECKBOX, this, lagCheck));
        Manager.put(new Setting("KA_RAY_TRACE", "Ray Trace", SettingType.CHECKBOX, this, raytrace));
        Manager.put(new Setting("KA_VIS_RANGE", "Visualize Range", SettingType.CHECKBOX, this, visRange));
        Manager.put(new Setting("KA_RANGE_COLOR", "Range Color", COLOR_PICKER, this, color, null, visRange::get));
        Manager.put(new Setting("KA_BLINK", "Blink", SettingType.CHECKBOX, this, blink));
        Manager.put(new Setting("AUTOBLOCK", "AutoBlock", SettingType.CHECKBOX, this, autoBlock));
        Manager.put(new Setting("TAR_HUD", "Target HUD", SettingType.CHECKBOX, this, targetHud));
        Manager.put(new Setting("KADISABLE", "Disable On", SettingType.SELECTBOX, this, autoDisable));
        //  Manager.put(new Setting("KA_HVH", "HVH preset", SettingType.BINDABLE, this, hvh));
    }

    //methods
    @Override
    public void onDisable() {
        yaw = iyaw = mc.player.rotationYaw;
        pitch = ipitch = mc.player.rotationPitch;
        target = null;
        targetList.clear();
        dispatchPackets();
    }

    @Override
    public void onEnable() {
        setSuffix(mode.get());
        alpha = 0;
        iyaw = mc.player.rotationYaw;
        ipitch = mc.player.rotationPitch;
        lossTimer.reset();

        if (blink.get()) {
            toDispatch.clear();
            blinking = true;
        } else {
            blinking = false;
        }
    }

    private boolean hasArmor(@NonNull EntityPlayer e) {
        return e.inventory.armorInventory[0] != null || e.inventory.armorInventory[1] != null || e.inventory.armorInventory[2] != null || e.inventory.armorInventory[3] != null;
    }

    private boolean heldSword() {
        return mc.player.getHeldItem() != null && mc.player.getHeldItem().getItem() instanceof ItemSword;
    }

    private boolean isInFOV() {
        if (target != null) {
            float[] rotationPosition = RotationUtil.getAngles((EntityLivingBase) target);
            int pitchByPos = (int) rotationPosition[1], yawByPos = (int) rotationPosition[0], yaw = (int) mc.player.rotationYaw,
                    pitch = (int) mc.player.rotationPitch, differenceYaw = Math.abs(yaw - yawByPos), differencePitch = Math.abs(pitch - pitchByPos);

            return differenceYaw <= fovCheck.get() && differencePitch <= fovCheck.get();
        }

        return false;
    }

    public boolean serverLag() {
        return lagCheck.get() && lossTimer.getLastDelay() >= 100;
    }

    public boolean shouldAttack() {
        return target != null && target.isEntityAlive() && mc.player.getDistanceToEntity(target) <=
                (mc.player.canEntityBeSeen(target) ? range() : wallRange()) && isInFOV() && !isEnabled(Scaffold.class);
    }

    public boolean shouldRotate() {
        return shouldAttack() && !serverLag() && !isEnabled(Scaffold.class) && !mc.playerController.isBreakingBlock();
    }

    public boolean shouldBlock() {
        return autoBlock.get() && target != null && heldSword() && target.isEntityAlive() && isEnabled() && !mc.playerController.isBreakingBlock() &&
                mc.player.getDistanceToEntity(target) <= (mc.player.canEntityBeSeen(target) ? blockRange() : wallRange()) && !isEnabled(Scaffold.class);
    }

    private boolean isAutismShopKeeperCheck(Entity target) {
        IChatComponent component = target.getDisplayName();
        String formatted = component.getFormattedText();
        String unFormatted = component.getUnformattedText();
        boolean first = !formatted.substring(0, formatted.length() - 2).contains("\u00A7");
        boolean second = formatted.substring(formatted.length() - 2).contains("\u00A7");
        return ServerUtils.isHypixel() && ServerUtils.serverIs(Servers.BW) && first && second;
    }

    private double getYawDifference(Entity e) {
        final float[] rotationPosition = RotationUtil.getAngles((EntityLivingBase) e);
        final float yaw = (int) rotationPosition[0], differenceYaw;
        differenceYaw = mc.player.rotationYaw > yaw ? mc.player.rotationYaw - yaw : yaw - mc.player.rotationYaw;
        return differenceYaw;
    }

    private boolean goBack;
    private Timer swapLobbyStopwatch = new Timer();

    @EventTarget
    public void onLoadWorld(LoadWorldEvent event) {
        if (goBack) {
            sendPacketNoEvent(new C01PacketChatMessage("/back"));
            goBack = false;
        }
    }

    @EventTarget
    public void targetSwitching(MotionUpdateEvent e) {
        if (e.getState().equals(MotionUpdateEvent.State.PRE)) {

            if (mode.get().equals("MW Infinite")) {
                if (swapLobbyStopwatch.delay(rejoin.get())) {
                    sendPacketNoEvent(new C01PacketChatMessage("/hub"));
                    goBack = true;
                    swapLobbyStopwatch.reset();
                }
            }

            double clamp = MathHelper.clamp_double(mc.getDebugFPS() / 30, 1, 9999);

            if (alpha < 1) {
                alpha = (float) (alpha + (1 - alpha) * (0.99F / clamp));
            }

            alpha = MathHelper.clamp_float(alpha, 0, 1);

            if (mc.player.inventory.getStackInSlot(0) != null) {
                if (mc.player.inventory.getStackInSlot(0).getItem() == Items.compass) {
                    if (mc.player.inventory.getStackInSlot(0).getDisplayName().contains("Teleporter")) {
                        target = null;
                    }
                }
            }

            if (targetsInRange() > 0) {
                targetList = getTargetsFromRange();
            } else {
                targetList = getEntityList(sort.get());
            }

            if (target != null && !target.isEntityAlive()) {
                target = null;
            }

            if (targetList != null) {
                if (timerSwitch.delay(switchDelay.get())) {
                    switchAndReset();
                }

                if (!isContainsTarget(mode().equalsIgnoreCase("Switch"))) {
                    if (target != null) {
                        if (mc.player.getDistanceToEntity(target) > range() && targetsInRange() > 0) {
                            target = getTargetFromRange();
                        } else if (mc.player.getDistanceToEntity(target) > blockRange()) {
                            target = null;
                            switchAndReset();
                        } else if (!target.isEntityAlive() && ((EntityLivingBase) target).getHealth() <= 0) {
                            target = null;
                            switchAndReset();
                        } else if (mode().equalsIgnoreCase("Switch")) {
                            target = targetList.get(switchIndex);
                        }

                    } else {
                        target = targetList.get(mode().equalsIgnoreCase("Switch") ? switchIndex : 0);
                    }

                } else {
                    for (Entity entity : targetList) {
                        if (novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.TARGET)) {
                            target = entity;
                        }
                    }
                }
            }

            prevIPitch = ipitch;
            prevIYaw = iyaw;
        }
    }

    public float getPrevIPitch() {
        return prevIPitch;
    }

    public float getPrevIYaw() {
        return prevIYaw;
    }

    private void switchAndReset() {
        switchIndex = (switchIndex + 1) % targetList.size();
        timerSwitch.reset();
    }

    private boolean rayTraycing(Entity target) {
        return !raytrace.get() || novoline.getPlayerManager().hasType(target.getName(), PlayerManager.EnumPlayerType.TARGET) || getPosition() != null || mc.playerController.isBreakingBlock();
    }

    private void attackEntity(Entity target) {
        if (timerAttack.delay(1000L / aps.get().longValue())) {
            EventManager.call(new AttackEvent());
            ArrayList<cc.novoline.utils.pathfinding.Vec3> vec3s = computePath(new cc.novoline.utils.pathfinding.Vec3(mc.player.posX, mc.player.posY, mc.player.posZ), new cc.novoline.utils.pathfinding.Vec3(target.posX, target.posY, target.posZ));

            if (mode.equals("MW Infinite")) {
                for (cc.novoline.utils.pathfinding.Vec3 vec3 : vec3s) {
                    sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
                    sendPacketNoEvent(new C03PacketPlayer(true));
                }
            }

            if (shouldBlock()) {
                sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                //   print(mc.player.ticksExisted, "unBlock");
                blocking = false;
            }

            mc.player.swingItem();
            sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

            if (mode.equals("MW Infinite")) {
                Collections.reverse(vec3s);

                for (cc.novoline.utils.pathfinding.Vec3 vec3 : vec3s) {
                    sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
                    sendPacketNoEvent(new C03PacketPlayer(true));
                }
            }

            if (particles.contains("Enchant")) {
                mc.player.onEnchantmentCritical(target);
            }

            if (particles.contains("Critical")) {
                mc.player.onCriticalHit(target);
            }

            timerAttack.reset();
        }
    }

    @EventTarget
    public void onMotions(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (shouldBlock() || blocking /*&& mc.player.ticksExisted % 2 != 0*/) {
                sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                //print(mc.player.ticksExisted, "unBlock");
                blocking = false;
            }

        } else {
            if (shouldAttack() && rayTraycing(target) && !serverLag()) {
                attackEntity(target);
            }

            if (shouldBlock()) {
                interactAutoBlock();
                mc.player.getHeldItem().useItemRightClick(mc.world, mc.player);

                //  if (mc.player.ticksExisted % 2 == 0) {
                sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.player.getHeldItem()));
                //print(mc.player.ticksExisted, "Block");
                blocking = true;
                //   }
            }
        }
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {
        setSuffix(mode());
    }

    @EventTarget
    public void onSlowDown(SlowdownEvent event) {
        if (shouldBlock()) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onSend(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (blinking) {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();

                    if (packet.isMoving()) {
                        toDispatch.add(event.getPacket());
                        event.setCancelled(true);
                    }
                }

                if (event.getPacket() instanceof C02PacketUseEntity) {
                    C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();

                    if (packet.getAction().equals(C02PacketUseEntity.Action.ATTACK) && packet.getEntityFromWorld(mc.world).equals(target)) {
                        dispatchPackets();
                        blinking = false;
                    }
                }

                if (toDispatch.size() > 60) {
                    dispatchPackets();
                    novoline.getNotificationManager().pop(getDisplayName(), "Packet buffer overloaded!", 2000, NotificationType.ERROR);
                    blinking = false;
                }
            }

            if (shouldBlock() || blocking) {
                if (event.getPacket() instanceof C07PacketPlayerDigging) {
                    C07PacketPlayerDigging packet = (C07PacketPlayerDigging) event.getPacket();

                    if (packet.getStatus().equals(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
                        event.setCancelled(true);
                    }
                }

                if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                    C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.getPacket();

                    if (packet.getPlacedBlockDirection() == 255) {
                        event.setCancelled(true);
                    }
                }
            }

        } else {
            lossTimer.reset();
        }
    }


    @EventTarget
    public void Rotations(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (shouldRotate()) {
                float frac = MathHelper.clamp_float(1f - smoothness.get() / 100f, 0.1f, 1f);
                float[] rotations = RotationUtil.getAngles((EntityLivingBase) target);

                iyaw = iyaw + (rotations[0] - iyaw) * frac;
                ipitch = ipitch + (rotations[1] - ipitch) * frac;

                event.setYaw(yaw = iyaw);
                event.setPitch(pitch = ipitch);

            } else {
                iyaw = mc.player.rotationYaw;
                ipitch = mc.player.rotationPitch;
            }
        }
    }

    public float getIYaw() {
        return iyaw;
    }

    public float getIPitch() {
        return ipitch;
    }

    private boolean interactable(Block block) {
        return block == Blocks.chest || block == Blocks.trapped_chest || block == Blocks.crafting_table
                || block == Blocks.furnace || block == Blocks.ender_chest || block == Blocks.enchanting_table;
    }

    private void interactAutoBlock() {
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if (mc.objectMouseOver.entityHit != null) {
                sendPacket(new C02PacketUseEntity(mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.INTERACT));

            } else if (interactable(mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock())) {
                mc.playerController.onPlayerRightClick(mc.player, mc.world, mc.player.getHeldItem(),
                        mc.objectMouseOver.getBlockPos(), Block.getFacingDirection(mc.objectMouseOver.getBlockPos()), mc.objectMouseOver.hitVec);
            }
        }
    }

    public boolean isValidEntity(@NonNull Entity target) {
        final boolean mobsTarget = targets.contains("Mobs"), animalsTarget = targets.contains("Animals"), //
                playersTarget = targets.contains("Players"), invisibleTarget = targets.contains("Invisibles");

        if (target.isEntityAlive() && mc.player.getHealth() > 0.0F && !novoline.getPlayerManager()
                .hasType(target.getName(), PlayerManager.EnumPlayerType.FRIEND)) {
            if (target instanceof EntityMob || target instanceof EntitySlime || target instanceof EntityGolem) {
                return mobsTarget && !(target.isInvisible() && !invisibleTarget);
            }

            if (target instanceof EntityAnimal || target instanceof EntityVillager) {
                return animalsTarget && !(target.isInvisible() && !invisibleTarget);
            }

            if (target instanceof EntityPlayer) {
                return playersTarget
                        && target != mc.player
                        && (!filters.contains("Armor")
                        || hasArmor((EntityPlayer) target))
                        && (!filters.contains("Teams")
                        || !PlayerUtils
                        .inTeam(mc.player, target))
                        && !(target.isInvisible() && !invisibleTarget) &&
                        !isAutismShopKeeperCheck(target)
                        && (!filters.contains("Users"))
                        && target != getModule(Freecam.class).getFreecamEntity()
                        && target != getModule(Blink.class).getBlinkEntity()
                        && target != getModule(Teleport.class).getTeleportEntity();
            }
        }

        return false;
    }

    private boolean isContainsTarget(boolean isSwitch) {
        for (Entity entity : getEntityList(sort.get()).stream()
                .filter(entity -> mc.player.getDistanceToEntity(entity) < range())
                .collect(Collectors.toCollection(ObjectArrayList::new))) {
            if (novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.TARGET)) {
                return true;
            }
        }

        return false;
    }

    private List<Entity> getEntityList(String sort) {
        List<Entity> list = mc.world.getLoadedEntityList().stream().filter(this::isValidEntity).collect(Collectors.toList());

        if (mode().equalsIgnoreCase("Single")) {
            switch (sort) {
                case "Distance":
                    list = list.stream().sorted(Comparator.comparing(o -> o.getDistanceToEntity(mc.player)))
                            .collect(Collectors.toCollection(ObjectArrayList::new));
                    break;
                case "Armor":
                    list = list.stream().sorted(Comparator.comparing(
                            o -> o instanceof EntityPlayer ? getProtection(((EntityPlayer) o).inventory.armorInventory) :
                                    99999)).collect(Collectors.toCollection(ObjectArrayList::new));
                    break;
                case "Health":
                    list = list.stream().sorted(Comparator.comparing(o -> ((EntityLivingBase) o).getHealth()))
                            .collect(Collectors.toCollection(ObjectArrayList::new));
                    break;
                case "FOV":
                    list = list.stream().sorted(Comparator.comparing(this::getYawDifference))
                            .collect(Collectors.toCollection(ObjectArrayList::new));
                    break;
                case "HurtTime":
                    list = list.stream().sorted(Comparator.comparing(o -> o.hurtResistantTime))
                            .collect(Collectors.toCollection(ObjectArrayList::new));
                    break;
            }
        }
        if (mode.get().equals("MW Infinite")) {
            return list.stream().filter(this::isInReach).filter(e -> e.getDisplayName().getFormattedText().startsWith("ยง")).collect(Collectors.toList());
        } else {
            return list.stream().filter(this::isInReach).collect(Collectors.toList());
        }
    }

    private List<Entity> getTargetsFromRange() {
        return getEntityList(sort.get()).stream().filter(e -> mc.player.getDistanceToEntity(e) <= range()).collect(Collectors.toList());
    }

    private int targetsInRange() {
        int i = 0;

        if (getEntityList(sort.get()).isEmpty()) {
            return 0;

        } else {
            for (Entity entity : getEntityList(sort.get())) {
                if (mc.player.getDistanceToEntity(entity) <= range()) {
                    i++;
                }
            }

            return i;
        }
    }

    private Entity getTargetFromRange() {
        for (Entity entity : getEntityList(sort.get())) {
            if (mc.player.getDistanceToEntity(entity) <= range()) {
                return entity;
            }
        }

        return null;
    }

    private float interpolateAngles(float fraction) {
        return 0;
    }

    private float getProtection(ItemStack[] stack1) {
        float protection = 0;

        for (ItemStack stack : stack1) {
            if (stack != null) {
                if (stack.getItem() instanceof ItemArmor) {
                    final ItemArmor armor = (ItemArmor) stack.getItem();

                    protection += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper
                            .getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
                    protection += EnchantmentHelper
                            .getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100d;
                    protection += EnchantmentHelper
                            .getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100d;
                    protection += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100d;
                    protection += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50d;
                    protection += EnchantmentHelper
                            .getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100d;
                }
            }
        }

        return protection;
    }

    private boolean isInReach(Entity target) {
        return mc.player.getDistanceToEntity(target) <= (mc.player.canEntityBeSeen(target) ?
                Math.max(range(), blockRange()) : wallRange());
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (targetHud.get()) {
            if (shouldAttack()) {
                if (target instanceof EntityPlayer && !(mc.currentScreen instanceof GuiChat)) {
                    RenderUtils.renderTHUD(this, (EntityPlayer) target);
                }
            }
        }

        if (blinking) {
            ScaledResolution sr = event.getResolution();
            int size = toDispatch.size();
            String color = "";

            if (size > 40) {
                color = "\u00A78";
            } else if (size > 20) {
                color = "\u00A77";
            }

            String sizeStr = color + toDispatch.size();
            Fonts.SF.SF_18.SF_18.drawString("Buffer size: " + sizeStr, sr.getScaledWidthStatic(mc) / 2 - 26, sr.getScaledHeightStatic(mc) / 2 + 10, 0xFFFFFF, true);
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (visRange.get()) {
            RenderUtils.pre3D();
            GL11.glLineWidth(6);
            GL11.glBegin(GL11.GL_LINE_STRIP);

            GL11.glColor4f(0, 0, 0, alpha);
            for (double d = 0; d < Math.PI * 2; d += Math.PI * 2 / 51) {
                double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * event.getPartialTicks() + Math.sin(d) * range.get() - mc.getRenderManager().renderPosX, // @off
                        y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY,
                        z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * event.getPartialTicks() + Math.cos(d) * range.get() - mc.getRenderManager().renderPosZ; // @on
                GL11.glVertex3d(x, y, z);
            }

            GL11.glEnd();
            GL11.glLineWidth(3);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glColor4f(color.getAwtColor().getRed() / 255F, color.getAwtColor().getGreen() / 255F, color.getAwtColor().getBlue() / 255F, 1);

            for (double d = 0; d < Math.PI * 2; d += Math.PI * 2 / 51) {
                double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * event.getPartialTicks() + Math.sin(d) * range.get() - mc.getRenderManager().renderPosX, // @off
                        y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY,
                        z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * event.getPartialTicks() + Math.cos(d) * range.get() - mc.getRenderManager().renderPosZ; // @on
                GL11.glVertex3d(x, y, z);
            }

            GL11.glEnd();
            RenderUtils.post3D();
        }
    }

    @EventTarget
    public void onAutoDisable(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (ServerUtils.serverIs(Servers.DUELS) && event.getPacket() instanceof S45PacketTitle) {
                final S45PacketTitle packet = (S45PacketTitle) event.getPacket();

                if (packet.getType().equals(S45PacketTitle.Type.TITLE)) {
                    final String text = packet.getMessage().getUnformattedText();

                    if (text.equals("VICTORY!")) {
                        novoline.getNotificationManager().pop("Check chat for stats", 1000, NotificationType.INFO);
                        DebugUtil.log(true, "[" + EnumChatFormatting.RED + "Stats" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.RESET + "Your hp: " + EnumChatFormatting.GREEN + mc.player.getHealth());
                    }

                    if (text.equals("GAME OVER!")) {
                        novoline.getNotificationManager().pop("Check chat for stats", 1000, NotificationType.INFO);
                        DebugUtil.log(true, "[" + EnumChatFormatting.RED + "Stats" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.RESET + "Opponent hp: " + EnumChatFormatting.GREEN + ((EntityLivingBase) target).getHealth());
                    }
                }
            }
        }
    }

    private void dispatchPackets() {
        blinking = false;
        toDispatch.forEach(this::sendPacketNoEvent);
        toDispatch.clear();
    }

    public MovingObjectPosition getPosition() {
        float borderSize = target.getCollisionBorderSize();
        Vec3 positionEyes = RotationUtil.getPositionEyes(1);
        Vec3 startVec = RotationUtil.getVectorForRotation(getIPitch(), getIYaw());
        Vec3 endVec = positionEyes.addVector(startVec.xCoord * range(), startVec.yCoord * range(), startVec.zCoord * range());
        return target.getEntityBoundingBox().expand(borderSize, borderSize, borderSize).calculateIntercept(positionEyes, endVec);
    }

    private ArrayList<cc.novoline.utils.pathfinding.Vec3> computePath(cc.novoline.utils.pathfinding.Vec3 topFrom, cc.novoline.utils.pathfinding.Vec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0, 1, 0);
        }
        AStarCustomPathfinder pathfinder = new AStarCustomPathfinder(topFrom, to);
        pathfinder.compute();

        int i = 0;
        cc.novoline.utils.pathfinding.Vec3 lastLoc = null;
        cc.novoline.utils.pathfinding.Vec3 lastDashLoc = null;
        ArrayList<cc.novoline.utils.pathfinding.Vec3> path = new ArrayList<>();
        ArrayList<cc.novoline.utils.pathfinding.Vec3> pathFinderPath = pathfinder.getPath();
        for (cc.novoline.utils.pathfinding.Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > 4 * 4) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    cordsLoop:
                    for (int x = (int) smallX; x <= bigX; x++) {
                        for (int y = (int) smallY; y <= bigY; y++) {
                            for (int z = (int) smallZ; z <= bigZ; z++) {
                                if (!AStarCustomPathfinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break cordsLoop;
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            i++;
        }
        return path;
    }

    private boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getInstance().world.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }


    //endregion


    //region Lombok
    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public StringProperty getMode() {
        return mode;
    }

    public String mode() {
        return mode.get();
    }

    public FloatProperty getSwitchDelay() {
        return switchDelay;
    }

    public DoubleProperty getAps() {
        return aps;
    }

    public DoubleProperty getRange() {
        return range;
    }

    public DoubleProperty getWallRange() {
        return wallRange;
    }

    public DoubleProperty getBlockRange() {
        return blockRange;
    }

    public double range() {
        return mode.get().equals("MW Infinite") ? 100 : range.get();
    }

    public double blockRange() {
        return mode.get().equals("MW Infinite") ? 100 : blockRange.get();
    }

    public double wallRange() {
        return mode.get().equals("MW Infinite") ? 100 : wallRange.get();
    }

    public IntProperty getFovCheck() {
        return fovCheck;
    }

    public ListProperty<String> getTargets() {
        return targets;
    }

    public ListProperty<String> getFilters() {
        return filters;
    }

    public ListProperty<String> getParticles() {
        return particles;
    }

    public BooleanProperty getAutoBlock() {
        return autoBlock;
    }

    public BooleanProperty getTargetHud() {
        return targetHud;
    }

    public ListProperty getAutoDisable() {
        return autoDisable;
    }

    public IntProperty getThx() {
        return thx;
    }

    public IntProperty getThy() {
        return thy;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public List<Entity> getTargetList() {
        return targetList;
    }

    public StringProperty getSort() {
        return sort;
    }

    public boolean lagCheck() {
        return lagCheck.get();
    }

    //endregion
}