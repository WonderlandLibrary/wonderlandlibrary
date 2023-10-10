package rosegoldclient.features;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.*;
import rosegoldclient.utils.pathfinding.Pathfinder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Pathfinding {

    private static int stuckTicks = 0;
    private static BlockPos oldPos;
    private static BlockPos curPos;
    public static boolean walk = false;
    public static HashSet<BlockPos> temp = new HashSet<>();

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if(!walk) return;
        if (Pathfinder.hasPath()) {
            if (++stuckTicks >= Main.configFile.pathfindingUnstuckTime * 20) {
                curPos = Main.mc.player.getPosition();
                if (oldPos != null && VecUtils.getHorizontalDistance(new Vec3d(curPos), new Vec3d(oldPos)) <= 0.1) {
                    initWalk();
                    Pathfinder.path.clear();
                    new Thread(() -> Pathfinder.setup(new BlockPos(VecUtils.floorVec(Main.mc.player.getPositionVector())), Pathfinder.goal, 0.0)).start();
                    return;
                }
                oldPos = curPos;
                stuckTicks = 0;
            }
            Vec3d nextPos = goodPoints(Pathfinder.path);
            Pathfinder.path.removeIf(vec3d -> new BlockPos(vec3d).getY() == Main.mc.player.getPosition().getY() && Pathfinder.path.indexOf(vec3d) < Pathfinder.path.indexOf(nextPos));
            Vec3d first = Pathfinder.getCurrent().addVector(0.5, 0.0, 0.5);
            Rotation needed = CheetoRotation.getRotation(first);
            needed.setPitch(Main.mc.player.rotationPitch);
            if (VecUtils.getHorizontalDistance(Main.mc.player.getPositionVector(), first) < 0.7) {
                if(Main.mc.player.getPositionVector().distanceTo(first) > 2) {
                    if (CheetoRotation.done && needed.getYaw() < 135.0f) {
                        CheetoRotation.setup(needed, (long) 150);
                    }
                    Vec3d lastTick = new Vec3d(Main.mc.player.lastTickPosX, Main.mc.player.lastTickPosY, Main.mc.player.lastTickPosZ);
                    Vec3d diffy = Main.mc.player.getPositionVector().subtract(lastTick);
                    diffy = diffy.addVector(diffy.x * 4.0, 0.0, diffy.z * 4.0);
                    Vec3d nextTick = Main.mc.player.getPositionVector().add(diffy);
                    stopMovement();
                    Main.mc.player.setSprinting(false);
                    ArrayList<KeyBinding> neededPresses = VecUtils.getNeededKeyPresses(Main.mc.player.getPositionVector(), first);
                    if (Math.abs(nextTick.distanceTo(first) - Main.mc.player.getPositionVector().distanceTo(first)) <= 0.05 || nextTick.distanceTo(first) <= Main.mc.player.getPositionVector().distanceTo(first)) {
                        neededPresses.forEach(v -> KeyBinding.setKeyBindState(v.getKeyCode(), true));
                    }
                    if (Math.abs(Main.mc.player.posY - first.y) > 0.5) {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), Main.mc.player.posY < first.y);
                    }
                    else {
                        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), false);
                    }
                } else {
                    CheetoRotation.reset();
                    if (!Pathfinder.goNext()) {
                        stopMovement();
                    }
                }
            } else {
                if (CheetoRotation.done) {
                    CheetoRotation.setup(needed, (long) 150);
                }
                stopMovement();
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                Main.mc.player.setSprinting(true);
                if (Math.abs(Main.mc.player.posY - first.y) > 0.5) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), Main.mc.player.posY < first.y);
                }
                else {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Pathfinder.path != null && !Pathfinder.path.isEmpty()) {
            Vec3d last = Pathfinder.path.get(Pathfinder.path.size() - 1).addVector(0.0, -1.0, 0.0);
            RenderUtils.drawBlockESP(new BlockPos(last), ColorUtils.getChroma(3000.0f, (int)(last.x + last.y + last.z)), event.getPartialTicks());
            if(walk) {
                RenderUtils.drawLines(Pathfinder.path, 1, event.getPartialTicks());
            }
        }
        if (!CheetoRotation.done) {
            CheetoRotation.update();
        }
        for(BlockPos blockPos : temp) {
            RenderUtils.drawBlockESP(blockPos, Color.WHITE, event.getPartialTicks());
        }
    }

    private static Vec3d goodPoints(ArrayList<Vec3d> path) {
        ArrayList<Vec3d> reversed = new ArrayList<>(path);
        Collections.reverse(reversed);
        for(Vec3d vec3d : reversed.stream().filter(vec3d -> new BlockPos(vec3d).getY() == Main.mc.player.getPosition().getY()).collect(Collectors.toList())) {
            if(isGood(vec3d)) {
                return vec3d;
            }
        }
        return null;
    }


    private static boolean isGood(Vec3d point) {
        if(point == null) return false;
        Vec3d topPoint = point.add(new Vec3d(0, 2, 0));

        Vec3d topPos = Main.mc.player.getPositionVector().addVector(0, 1, 0);
        Vec3d botPos = Main.mc.player.getPositionVector();
        Vec3d underPos = Main.mc.player.getPositionVector().addVector(0, -1, 0);

        temp.clear();

        Vec3d directionTop = CheetoRotation.getLook(topPoint);
        directionTop = VecUtils.scaleVec(directionTop, 0.5f);
        for (int i = 0; i < Math.round(topPoint.distanceTo(Main.mc.player.getPositionEyes(1))) * 2; i++) {
            if(Main.mc.world.getBlockState(new BlockPos(topPos)).getCollisionBoundingBox(
                    Main.mc.world,
                    new BlockPos(topPos)
            ) != Block.NULL_AABB) return false;
            topPos = topPos.add(directionTop);

            if(Main.mc.world.getBlockState(new BlockPos(botPos)).getCollisionBoundingBox(
                    Main.mc.world,
                    new BlockPos(botPos)
            ) != Block.NULL_AABB) return false;
            botPos = botPos.add(directionTop);

            if(Main.mc.world.getBlockState(new BlockPos(underPos)).getCollisionBoundingBox(
                    Main.mc.world,
                    new BlockPos(underPos)
            ) == Block.NULL_AABB) return false;
            underPos = underPos.add(directionTop);
        }
        return true;
    }

    public static void initTeleport() {
        walk = false;
        stuckTicks = 0;
        oldPos = null;
        curPos = null;
    }

    public static void initWalk() {
        walk = true;
        stuckTicks = 0;
        oldPos = null;
        curPos = null;
    }

    private void stopMovement() {
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), false);
    }

}
