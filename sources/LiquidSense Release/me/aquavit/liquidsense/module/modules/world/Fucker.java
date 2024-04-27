package me.aquavit.liquidsense.module.modules.world;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.event.events.WorldEvent;
import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.module.modules.player.AutoTool;
import me.aquavit.liquidsense.utils.block.BlockUtils;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.extensions.BlockExtensionUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.ColorType;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;
import me.aquavit.liquidsense.utils.client.VecRotation;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;

@ModuleInfo(name = "Fucker", description = "Destroys selected blocks around you. (aka.  IDNuker)", category = ModuleCategory.WORLD)
public class Fucker extends Module {

    private BlockValue blockValue = new BlockValue("Block", 26);
    private Value<String> throughWallsValue = new ListValue("ThroughWalls", new String[] { "None", "Raycast", "Around" }, "None").displayable(() -> !surroundingsValue.get());
    private FloatValue rangeValue = new FloatValue("Range", 5F, 1F, 7F);
    private ListValue actionValue = new ListValue("Action", new String[] { "Destroy", "Use", "New"}, "Destroy");
    private Value<Boolean> instantValue = new BoolValue("Instant", false).displayable(() -> !actionValue.get().equalsIgnoreCase("Use"));
    private IntegerValue switchValue = new IntegerValue("SwitchDelay", 250, 0, 1000);
    private BoolValue swingValue = new BoolValue("Swing", true);
    private BoolValue rotationsValue = new BoolValue("Rotations", true);
    public static BoolValue surroundingsValue = new BoolValue("Surroundings", true);
    private BoolValue noHitValue = new BoolValue("NoHit", false);
    private BoolValue nofuckerValue = new BoolValue("NoFucker", true);

    private static BlockPos pos;
    public static float currentDamage = 0F;
    private BlockPos oldPos;
    private int blockHitDelay = 0;
    private MSTimer switchTimer = new MSTimer();

    private BlockPos nofuckpos;
    private boolean sendnotif = false;
    private int nofuckdis;

    @EventTarget
    public void onWorld(WorldEvent event) {
        sendnotif = false;
        nofuckpos = null;
    }

    @Override
    public void onToggle(final boolean state) {
        sendnotif = false;
        nofuckpos = null;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        nofuckdis = (int) (rangeValue.get() + 1);
        int targetId = blockValue.get();

        if (nofuckerValue.get()){
            if (nofuckpos == null || Block.getIdFromBlock(BlockUtils.getBlock(nofuckpos)) != targetId) {
                nofuckpos = nofuckfind(targetId);
                sendnotif = true;
            }

            if (nofuckpos != null) {
                if (sendnotif) {
                    LiquidSense.hud.addNotification(new Notification("NoFucker",
                            "Block in " + nofuckpos.getX() + " " + nofuckpos.getY() + " " + nofuckpos.getZ() + ".",
                            ColorType.INFO, 1500, 500));
                    sendnotif = false;
                }

                if (mc.thePlayer.posZ > nofuckpos.getZ() - nofuckdis &&
                        mc.thePlayer.posZ < nofuckpos.getZ() + nofuckdis &&
                        mc.thePlayer.posX > nofuckpos.getX() - nofuckdis &&
                        mc.thePlayer.posX < nofuckpos.getX() + nofuckdis)
                    return;
            }
        }

        if (noHitValue.get()) {
            Aura aura = (Aura) LiquidSense.moduleManager.getModule(Aura.class);

            if (aura.getState() && aura.getTarget() != null) return;
        }

        if (pos == null || Block.getIdFromBlock(BlockUtils.getBlock(pos)) != targetId ||
                BlockUtils.getCenterDistance(pos) > rangeValue.get())
            pos = find(targetId);

        if (pos == null) {
            currentDamage = 0F;
            return;
        }

        BlockPos currentPos = pos;
        VecRotation rotations = RotationUtils.faceBlock(currentPos);
        if (rotations == null) return;


        boolean surroundings = false;

        if (surroundingsValue.get()) {
            Vec3 eyes = mc.thePlayer.getPositionEyes(1F);
            if (eyes == null) return;
            BlockPos blockPos = mc.theWorld.rayTraceBlocks(eyes, rotations.getVec(), false,
                    false, true).getBlockPos();

            if (blockPos != null && !(BlockExtensionUtils.getBlock(blockPos) instanceof BlockAir)) {
                if (currentPos.getX() != blockPos.getX() || currentPos.getY() != blockPos.getY() || currentPos.getZ() != blockPos.getZ())
                    surroundings = true;

                pos = blockPos;
                currentPos = pos;
                rotations = RotationUtils.faceBlock(currentPos);
                if (rotations == null) return;
            }
        }

        if (oldPos != null && oldPos != currentPos) {
            currentDamage = 0F;
            switchTimer.reset();
        }

        oldPos = currentPos;

        if (!switchTimer.hasTimePassed((long)switchValue.get()))
            return;

        if (blockHitDelay > 0) {
            blockHitDelay--;
            return;
        }

        if (rotationsValue.get())
            RotationUtils.setTargetRotation(rotations.getRotation());

        if (actionValue.get().equalsIgnoreCase("Destroy") || surroundings || actionValue.get().equalsIgnoreCase("New")) {
            AutoTool autoTool = (AutoTool) LiquidSense.moduleManager.getModule(AutoTool.class);
            if (autoTool.getState())
                autoTool.switchSlot(currentPos);

            if (instantValue.get()) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                        currentPos, EnumFacing.DOWN));

                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                        currentPos, EnumFacing.DOWN));

                if(actionValue.get().equalsIgnoreCase("New")){
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                            currentPos, EnumFacing.DOWN));
                }

                if (swingValue.get())
                    mc.thePlayer.swingItem();
                currentDamage = 0F;
                return;
            }

            Block block = BlockExtensionUtils.getBlock(currentPos);
            if (block == null) return;

            if (currentDamage == 0F) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                        currentPos, EnumFacing.DOWN));

                if (mc.thePlayer.capabilities.isCreativeMode ||
                        block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, pos) >= 1.0F) {
                    if (swingValue.get())
                        mc.thePlayer.swingItem();
                    mc.playerController.onPlayerDestroyBlock(pos, EnumFacing.DOWN);

                    currentDamage = 0F;
                    pos = null;
                    return;
                }
            }

            if (swingValue.get())
                mc.thePlayer.swingItem();

            currentDamage += block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, currentPos);
            mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), currentPos, (int) ((currentDamage * 10F) - 1));

            if (currentDamage >= 1F) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                        currentPos, EnumFacing.DOWN));
                mc.playerController.onPlayerDestroyBlock(currentPos, EnumFacing.DOWN);
                blockHitDelay = 4;
                currentDamage = 0F;
                pos = null;
            }
        } else if (actionValue.get().equalsIgnoreCase("Use")) {
            if (mc.playerController.onPlayerRightClick(
                    mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.DOWN,
                    new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()))) {
                if (swingValue.get())
                    mc.thePlayer.swingItem();

                blockHitDelay = 4;
                currentDamage = 0F;
                pos = null;
            }
        }

    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (nofuckerValue.get() && nofuckpos != null) RenderUtils.draw2D(new BlockPos(nofuckpos), Color.WHITE.getRGB(), Color.BLACK.getRGB());

        if (pos != null) RenderUtils.drawBlockBox(pos, Color.RED, true);
    }

    private BlockPos find(int targetID) {
        return BlockUtils.searchBlocks((int) (rangeValue.get() + 1)).entrySet().stream().filter(it ->
                Block.getIdFromBlock(it.getValue()) == targetID && BlockUtils.getCenterDistance(it.getKey()) <= rangeValue.get() &&
                        (isHitable(it.getKey()) || surroundingsValue.get())).min(Comparator.comparingDouble(it ->
                BlockUtils.getCenterDistance(it.getKey()))).map(Map.Entry::getKey).orElse(null);
    }

    private BlockPos nofuckfind(int targetID) {
        return BlockUtils.searchBlocks(nofuckdis + 1).entrySet().stream().filter(it ->
                Block.getIdFromBlock(it.getValue()) == targetID && BlockUtils.getCenterDistance(it.getKey()) <= nofuckdis).min(
                        Comparator.comparingDouble(it -> BlockUtils.getCenterDistance(it.getKey()))).map(Map.Entry::getKey).orElse(null);
    }

    private boolean isHitable(BlockPos blockPos) {
        switch (throughWallsValue.get().toLowerCase()) {
            case "raycast": {
                final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY +
                        mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
                final MovingObjectPosition movingObjectPosition = mc.theWorld.rayTraceBlocks(eyesPos,
                        new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), false,
                        true, false);
                return movingObjectPosition != null && movingObjectPosition.getBlockPos().equals(blockPos);
            }
            case "around":
                return !BlockUtils.isFullBlock(blockPos.down()) || !BlockUtils.isFullBlock(blockPos.up()) || !BlockUtils.isFullBlock(blockPos.north())
                        || !BlockUtils.isFullBlock(blockPos.east()) || !BlockUtils.isFullBlock(blockPos.south()) || !BlockUtils.isFullBlock(blockPos.west());

            default:
                return true;
        }
    }

    @Override
    public String getTag() {
        return BlockUtils.getBlockName(blockValue.get());
    }
}
