package rosegoldclient.features;

import gg.essential.api.utils.Multithreading;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.KeybindEvent;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.*;
import rosegoldclient.utils.pathfinding.Pathfinder;

import java.util.ArrayList;
import java.util.HashMap;

public class AutoWalk {

    public static HashMap<String, ArrayList<Point>> profiles = new HashMap<>();
    public static ArrayList<Point> points = new ArrayList<>();
    public static Point current;
    public static boolean waiting = false;
    public static boolean started = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if(!Main.configFile.autoWalk) return;
        if(!Main.doAutoWalk) return;
        if(Main.mc.player == null) return;
        if(points.size() == 0) return;
        if(current == null) current = points.get(0);
        if(!reachedPoint(current.getLocation())) {
            walkToPoint(current);
        } else if(!waiting) {
            stopMovement();
            waiting = true;
            Multithreading.runAsync(() -> {
                try {
                    Thread.sleep(current.getDelay() * Main.configFile.autoWalkWaitModifier);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waiting = false;
                nextPoint();
            });
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(!Main.configFile.autoWalk) return;
        for(Point point : points) {
            RenderUtils.drawPointESP(point.getLocation(), ColorUtils.getChroma(3000.0f, (int) (point.getLocation().x + point.getLocation().y + point.getLocation().z)), event.getPartialTicks());
        }
    }

    private static void walkToPoint(Point point) {
        if(point.isPathfind()) {
            if(started) return;
            Multithreading.runAsync(() -> {
                started = true;
                Pathfinding.initWalk();
                Pathfinder.setup(new BlockPos(VecUtils.floorVec(Main.mc.player.getPositionVector())), new BlockPos(point.getLocation().subtract(0, 1, 0)), 0.0);
            });
        } else {
            Vec3d location = point.getLocation();
            Rotation needed = CheetoRotation.getRotation(location);
            needed.setPitch(Main.mc.player.rotationPitch);
            if (CheetoRotation.done && needed.getYaw() < 135.0f) {
                CheetoRotation.setup(needed, (long) 150);
            }
            Vec3d lastTick = new Vec3d(Main.mc.player.lastTickPosX, Main.mc.player.lastTickPosY, Main.mc.player.lastTickPosZ);
            Vec3d diffy = Main.mc.player.getPositionVector().subtract(lastTick);
            diffy = diffy.addVector(diffy.x * 4.0, 0.0, diffy.z * 4.0);
            Vec3d nextTick = Main.mc.player.getPositionVector().add(diffy);
            stopMovement();
            ArrayList<KeyBinding> neededPresses = VecUtils.getNeededKeyPresses(Main.mc.player.getPositionVector(), location);
            if (Math.abs(nextTick.distanceTo(location) - Main.mc.player.getPositionVector().distanceTo(location)) <= 0.05 || nextTick.distanceTo(location) <= Main.mc.player.getPositionVector().distanceTo(location)) {
                neededPresses.forEach(v -> KeyBinding.setKeyBindState(v.getKeyCode(), true));
            }
            if (location.y - Main.mc.player.posY > 0.5 && distanceHorizontal(Main.mc.player.getPositionVector(), location) < 2) {
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), Main.mc.player.posY < location.y);
            } else {
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), false);
            }
            if (Main.mc.player.posY - location.y > 0.9 && distanceHorizontal(Main.mc.player.getPositionVector(), location) < 2) {
                descend();
            } else {
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
            }
        }
    }

    private static void descend() {
        Multithreading.runAsync(() -> {
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        });
    }

    private static boolean reachedPoint(Vec3d point) {
        return Main.mc.player.getPositionVector().distanceTo(VecUtils.floorVec(point).addVector(0.5, 0.0, 0.5)) < 1;
    }

    private static double distanceHorizontal(Vec3d a, Vec3d b) {
        double d0 = a.x - b.x;
        double d2 = a.z - b.z;
        return MathHelper.sqrt(d0 * d0 + d2 * d2);
    }

    private static void stopMovement() {
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), false);
    }

    private static void nextPoint() {
        System.out.println(points.indexOf(current) + " + 1 % " + points.size() + " = " + ((points.indexOf(current) + 1) % points.size()));
        current = points.get((points.indexOf(current) + 1) % points.size());
        started = false;
    }
}
