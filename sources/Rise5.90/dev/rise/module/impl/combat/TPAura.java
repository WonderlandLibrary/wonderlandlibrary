package dev.rise.module.impl.combat;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.render.Freecam;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.pathfinding.MainPathFinder;
import dev.rise.util.pathfinding.Vec3;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ModuleInfo(name = "TPAura", description = "Attacks entities from far ranges by teleporting to them and back", category = Category.COMBAT)
public class TPAura extends Module {

    private final NoteSetting modeSettings = new NoteSetting("Mode Settings", this);
    private final ModeSetting mode = new ModeSetting("Mode", this, "Single", "Single", "Multi");
    private final NoteSetting generalSettings = new NoteSetting("General Settings", this);
    private final NumberSetting cps = new NumberSetting("CPS", this, 5, 1, 20, 1);
    private final NumberSetting range = new NumberSetting("Range", this, 20, 4, 120, 1);
    private final NumberSetting timerSpeed = new NumberSetting("Timer", this, 1, 0.1, 3, 0.1);
    private final NumberSetting maxTargets = new NumberSetting("Max Targets", this, 25, 2, 50, 1);
    private final NoteSetting targets = new NoteSetting("Targets", this);
    private final BooleanSetting players = new BooleanSetting("Players", this, true);
    private final BooleanSetting nonPlayers = new BooleanSetting("Non Players", this, true);
    private final BooleanSetting ignoreTeammates = new BooleanSetting("Ignore Teammates", this, true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", this, false);
    private final BooleanSetting attackDead = new BooleanSetting("Attack Dead", this, false);
    private final NoteSetting renderSettings = new NoteSetting("Render Settings", this);
    private final BooleanSetting render = new BooleanSetting("Render", this, true);

    private ArrayList<Vec3> path;
    private Entity target;
    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onUpdateAlwaysInGui() {
        maxTargets.hidden = !mode.is("Multi");
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        /* Getting target */
        if (!timer.hasReached((long) (((20 - cps.getValue()) * 50) - Math.random() * 100))) return;

        timer.reset();

        final List<EntityLivingBase> targets = PlayerUtil.getEntities(range.getValue(), players.isEnabled(), nonPlayers.isEnabled(), attackDead.isEnabled(), invisibles.isEnabled(), ignoreTeammates.isEnabled());

        final int maxTargets = (int) Math.round(this.maxTargets.getValue());

        if (mode.is("Multi") && targets.size() > maxTargets)
            targets.subList(maxTargets, targets.size()).clear();

        if (targets.isEmpty()) {
            target = null;
            return;
        }

        target = targets.get(0);

        /* Setting useful variables */
        final EntityPlayer player = mc.thePlayer;

        final boolean freecamEnabled = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Freecam")).isEnabled();
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;

        if (freecamEnabled) {
            x = Freecam.startX;
            y = Freecam.startY;
            z = Freecam.startZ;
        }

        final double targetX = target.posX;
        final double targetY = target.posY;
        final double targetZ = target.posZ;

        mc.timer.timerSpeed = (float) timerSpeed.getValue();

        /* Creating a new thread */
        final double finalZ = z;
        final double finalY = y;
        final double finalX = x;
        new Thread(() -> {
            switch (mode.getMode()) {
                case "Single": {
                    /* Getting path */
                    path = MainPathFinder.computePath(new Vec3(finalX, finalY, finalZ), new Vec3(targetX, targetY, targetZ));

                    for (final Vec3 vec : path)
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), false));

                    mc.thePlayer.swingItem();

                    /*
                     * Calls attack event so other modules can use information from the entity
                     * When the C02 packet is sent the attack event does not
                     * get called, so we have to manually call it ourselves in here.
                     */
                    final AttackEvent attackEvent = new AttackEvent(target);
                    attackEvent.call();

                    if (!attackEvent.isCancelled())
                        PacketUtil.sendPacketWithoutEvent(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

                    Collections.reverse(path);

                    for (final Vec3 vec : path)
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), false));
                    break;
                }

                case "Multi": {
                    for (final Entity entity : targets) {
                        /* Getting path */
                        path = MainPathFinder.computePath(new Vec3(finalX, finalY, finalZ), new Vec3(entity.posX, entity.posY, entity.posZ));

                        for (final Vec3 vec : path)
                            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                        mc.thePlayer.swingItem();

                        /*
                         * Calls attack event so other modules can use information from the entity
                         * When the C02 packet is sent the attack event does not
                         * get called, so we have to manually call it ourselves in here.
                         */
                        final AttackEvent attackEvent = new AttackEvent(entity);
                        attackEvent.call();

                        if (!attackEvent.isCancelled())
                            PacketUtil.sendPacketWithoutEvent(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));

                        Collections.reverse(path);

                        for (final Vec3 vec : path)
                            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
                    }
                    break;
                }
            }
        }).start();
    }

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1;
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        /* Drawing path to target */
        if (!render.isEnabled() || path == null || target == null) return;

        Vec3 lastPoint = null;

        final Color c = new Color(Rise.CLIENT_THEME_COLOR).brighter();

        for (final Vec3 point : path) {
            if (lastPoint != null) {
                RenderUtil.draw3DLine(lastPoint.getX(), lastPoint.getY() + 0.01, lastPoint.getZ(), point.getX(), point.getY() + 0.01, point.getZ(), c.getRed(), c.getGreen(), c.getBlue(), 255, 1);
            }
            lastPoint = point;
        }
    }
}