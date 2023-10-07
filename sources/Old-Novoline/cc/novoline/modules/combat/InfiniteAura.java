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
import cc.novoline.modules.move.Speed;
import cc.novoline.modules.player.Freecam;
import cc.novoline.utils.*;
import cc.novoline.utils.Timer;
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
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static cc.novoline.modules.configurations.property.object.PropertyFactory.createColor;

public class InfiniteAura extends AbstractModule {
    public InfiniteAura(@NotNull ModuleManager novoline) {
        super(novoline, EnumModuleType.COMBAT, "InfiniteAura");
        Manager.put(new Setting("APS", "APS", SettingType.SLIDER, this, aps, 1.0D));
        Manager.put(new Setting("RANGE", "Range", SettingType.SLIDER, this, range, 0.1));
        Manager.put(new Setting("WALL_RANGE", "Wall Range", SettingType.SLIDER, this, wallRange, 0.1));
        Manager.put(new Setting("TARGETS", "Targets", SettingType.SELECTBOX, this, targets));
        Manager.put(new Setting("KA_FILTER", "Filters", SettingType.SELECTBOX, this, filters));
        Manager.put(new Setting("KA_LAG_CHECK", "Lag Check", SettingType.CHECKBOX, this, lagCheck));
        Manager.put(new Setting("TAR_HUD", "Target HUD", SettingType.CHECKBOX, this, targetHud));
    }


    /* fields */
    private Entity target;
    private int switchIndex;
    private List<Entity> targetList = new CopyOnWriteArrayList<>();
    private final Timer timerAttack = new Timer(), timerSwitch = new Timer(), lossTimer = new Timer();
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final List<Packet<?>> toDispatch = new ArrayList<>();
    private boolean blinking, blocking, sentC08;

    /* properties @off */
    @Property("aps")
    private final DoubleProperty aps = PropertyFactory.createDouble(5.0D).minimum(1.0D).maximum(18.0D);
    @Property("range")
    private final DoubleProperty range = PropertyFactory.createDouble(3.5D).minimum(1.0D).maximum(50.0D);
    @Property("wall-range")
    private final DoubleProperty wallRange = PropertyFactory.createDouble(3.5D).minimum(1.0D).maximum(50.0D);
    @Property("targets")
    private final ListProperty<String> targets = PropertyFactory.createList("Players").acceptableValues("Players", "Animals", "Mobs", "Invisibles");
    @Property("filters")
    private final ListProperty<String> filters = PropertyFactory.createList("Teams").acceptableValues("Teams", "Users", "Armor");
    @Property("target-hud")
    private final BooleanProperty targetHud = PropertyFactory.booleanFalse();
    @Property("lag-check")
    private final BooleanProperty lagCheck = PropertyFactory.booleanFalse();
    @Property("color")
    private final ColorProperty color = createColor(0xFFFFFFFF);
    @Property("th-x")
    private final IntProperty thx = PropertyFactory.createInt((int) screenSize.getWidth() / 4).minimum(1).maximum((int) screenSize.getWidth() / 2);
    @Property("th-y")
    private final IntProperty thy = PropertyFactory.createInt((int) screenSize.getHeight() / 4).minimum(1).maximum((int) screenSize.getHeight() / 2);


    //methods
    @Override
    public void onDisable() {
        target = null;
        targetList.clear();
    }

    @Override
    public void onEnable() {
        lossTimer.reset();
    }

    private boolean hasArmor(@NonNull EntityPlayer e) {
        return e.inventory.armorInventory[0] != null || e.inventory.armorInventory[1] != null || e.inventory.armorInventory[2] != null || e.inventory.armorInventory[3] != null;
    }

    public boolean serverLag() {
        return lagCheck.get() && lossTimer.getLastDelay() >= 100;
    }

    public boolean shouldAttack() {
        return target != null && target.isEntityAlive() && mc.player.getDistanceToEntity(target) <=
                (mc.player.canEntityBeSeen(target) ? range() : wallRange()) && !isEnabled(Scaffold.class);
    }

    public boolean shouldRotate() {
        return shouldAttack() && !serverLag() && !isEnabled(Scaffold.class) && !mc.playerController.isBreakingBlock();
    }

    private boolean isAutismShopKeeperCheck(Entity target) {
        IChatComponent component = target.getDisplayName();
        String formatted = component.getFormattedText();
        String unFormatted = component.getUnformattedText();
        boolean first = !formatted.substring(0, formatted.length() - 2).contains("\u00A7");
        boolean second = formatted.substring(formatted.length() - 2).contains("\u00A7");
        return ServerUtils.isHypixel() && ServerUtils.serverIs(Servers.BW) && first && second;
    }

    @EventTarget
    public void targetSwitching(MotionUpdateEvent e) {
        if (e.getState().equals(MotionUpdateEvent.State.PRE)) {

            if (targetsInRange() > 0) {
                targetList = getTargetsFromRange();
            } else {
                targetList = getEntityList();
            }

            if (target != null && !target.isEntityAlive()) {
                target = null;
            }


            if(target != null){
                if(!canTeleport(target)){
                    target = null;
                }
            }

            if (targetList != null) {
                if (!isContainsTarget()) {
                    if (target != null) {
                        if (mc.player.getDistanceToEntity(target) > range() && targetsInRange() > 0) {
                            target = getTargetFromRange();
                        } else if (!target.isEntityAlive() && ((EntityLivingBase) target).getHealth() <= 0) {
                            target = null;
                        }

                    } else {
                        target = targetList.get(0);
                    }

                } else {
                    for (Entity entity : targetList) {
                        if (novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.TARGET)) {
                            target = entity;
                        }
                    }
                }
            }
        }
    }


    private void attackEntity(Entity target) {
        if (canTeleport(target)) {
            if (timerAttack.delay(1000L / aps.get().longValue())) {
                EventManager.call(new AttackEvent());

                List<Packet> packets = teleport(target);

                packets.forEach(this::sendPacketNoEvent);

                mc.player.swingItem();
                sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

                Collections.reverse(packets);

                packets.forEach(this::sendPacketNoEvent);

                timerAttack.reset();
            }
        }
    }

    @EventTarget
    public void onMotions(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (shouldAttack()) {
                attackEntity(target);
            }
        }
    }

    @EventTarget
    public void onSend(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
        } else {
            lossTimer.reset();
        }
    }


    @EventTarget
    public void Rotations(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (shouldRotate()) {
                float[] rotations = RotationUtil.getAngles((EntityLivingBase) target);

                event.setYaw(rotations[0]);
                event.setPitch(rotations[1]);
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

    private boolean isContainsTarget() {
        for (Entity entity : getEntityList().stream()
                .filter(entity -> mc.player.getDistanceToEntity(entity) < range())
                .collect(Collectors.toCollection(ObjectArrayList::new))) {
            if (novoline.getPlayerManager().hasType(entity.getName(), PlayerManager.EnumPlayerType.TARGET)) {
                return true;
            }
        }

        return false;
    }

    private List<Entity> getEntityList() {
        List<Entity> list = mc.world.getLoadedEntityList().stream().filter(this::isValidEntity).collect(Collectors.toList());

        list = list.stream().sorted(Comparator.comparing(o -> o.getDistanceToEntity(mc.player)))
                .collect(Collectors.toCollection(ObjectArrayList::new));

        list = list.stream().filter(this::isInReach).collect(Collectors.toList());

        return list.stream().filter(e -> canTeleport(e)).collect(Collectors.toList());
    }

    private List<Entity> getTargetsFromRange() {

        List<Entity> collect = getEntityList().stream().filter(e -> mc.player.getDistanceToEntity(e) <= range()).collect(Collectors.toList());
        collect = collect.stream().filter(e -> canTeleport(e)).collect(Collectors.toList());
        return collect;
    }

    private int targetsInRange() {
        int i = 0;

        if (getEntityList().isEmpty()) {
            return 0;

        } else {
            for (Entity entity : getEntityList()) {
                if (mc.player.getDistanceToEntity(entity) <= range()) {
                    i++;
                }
            }

            return i;
        }
    }

    private Entity getTargetFromRange() {
        for (Entity entity : getEntityList()) {
            if (mc.player.getDistanceToEntity(entity) <= range()) {
                return entity;
            }
        }

        return null;
    }


    private boolean isInReach(Entity target) {
        return mc.player.getDistanceToEntity(target) <= (mc.player.canEntityBeSeen(target) ?
                range() : wallRange());
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (targetHud.get()) {
            if (shouldAttack()) {
                if (target instanceof EntityPlayer && !(mc.currentScreen instanceof GuiChat)) {
//                    RenderUtils.renderTHUD(this, (EntityPlayer) target);
                }
            }
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


    //region Lombok
    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
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

    public double range() {
        return range.get();
    }


    public double wallRange() {
        return wallRange.get();
    }


    public ListProperty<String> getTargets() {
        return targets;
    }

    public ListProperty<String> getFilters() {
        return filters;
    }

    public BooleanProperty getTargetHud() {
        return targetHud;
    }

    public IntProperty getThx() {
        return thx;
    }

    public IntProperty getThy() {
        return thy;
    }

    public List<Entity> getTargetList() {
        return targetList;
    }

    public boolean lagCheck() {
        return lagCheck.get();
    }

    public Vec3 getPositionForVector(double x, double y, double z) {
        return new Vec3(x, y, z);
    }


    private boolean canTeleport(Entity entity) {
        if(!mc.player.onGround){
            return false;
        }
        double maximumStepDistance = 0.1;
        double x = entity.posX, z = entity.posZ;
        float angleYaw = RotationUtil.getYawToPoint(x, z);
        double distance = Math.abs(mc.player.getDistance(x, mc.player.posY, z)) - 1;

        double height = mc.player.posY;


        for (double step = maximumStepDistance; step < distance; step += maximumStepDistance) {

            double xAxis = -Math.sin(Math.toRadians(angleYaw)) * step;
            double zAxis = Math.cos(Math.toRadians(angleYaw)) * step;

            { // Checking if inside a block
                Vec3 position = getPositionForVector(mc.player.posX + xAxis, height + 0.01, mc.player.posZ + zAxis);
                Vec3 startVec = RotationUtil.getVectorForRotation(0, angleYaw);
                Vec3 endVec = position.addVector(startVec.xCoord * 0.1, startVec.yCoord * 0.1, startVec.zCoord * 0.1);
                MovingObjectPosition blockAhead = mc.world.rayTraceBlocks(position, endVec,
                        false,
                        false,
                        false);
                if (blockAhead != null) {
                    if (blockAhead.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        return false;
                    }
                }
            }

            { // Checking if there's a block below
                BlockPos pos = new BlockPos(mc.player.posX + xAxis, height - 1, mc.player.posZ + zAxis);
                if(mc.world.getBlockState(pos).getBlock() == Blocks.air || !mc.world.getBlockState(pos).getBlock().isFullBlock()){
                    return false;
                }
            }


        }
        return true;
    }

    private List<Packet> teleport(Entity entity) {
        List<Packet> packets = new CopyOnWriteArrayList<>();
        double maximumStepDistance = 0.23061663902;
        double x = entity.posX, z = entity.posZ;
        float angleYaw = RotationUtil.getYawToPoint(x, z);
        double distance = Math.abs(mc.player.getDistance(x, mc.player.posY, z)) - 1;

        double height = mc.player.posY;

        for (double step = maximumStepDistance; step < distance; step += maximumStepDistance) {

            double xAxis = -Math.sin(Math.toRadians(angleYaw)) * step;
            double zAxis = Math.cos(Math.toRadians(angleYaw)) * step;

            packets.add(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.player.posX + xAxis,
                    height,
                    mc.player.posZ + zAxis,
                    true
            ));
        }

        return packets;
    }


    //endregion

}
