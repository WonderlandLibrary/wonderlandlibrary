package me.aquavit.liquidsense.module.modules.world;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.MotionEvent;
import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.utils.block.BlockUtils;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.client.VecRotation;
import me.aquavit.liquidsense.utils.extensions.BlockExtensionUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.player.Blink;
import me.aquavit.liquidsense.module.modules.render.FreeCam;
import me.aquavit.liquidsense.value.BlockValue;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleInfo(name = "ChestAura", description = "Automatically opens chests around you.", category = ModuleCategory.WORLD)
public class ChestAura extends Module {

    private FloatValue rangeValue = new FloatValue("Range", 5F, 1F, 6F);
    private IntegerValue delayValue = new IntegerValue("Delay", 100, 50, 200);
    private BoolValue throughWallsValue = new BoolValue("ThroughWalls", true);
    private BoolValue visualSwing = new BoolValue("VisualSwing", true);
    private BlockValue chestValue = new BlockValue("Chest", Block.getIdFromBlock(Blocks.chest));
    private BoolValue rotationsValue = new BoolValue("Rotations", true);

    public static List<BlockPos> clickedBlocks = new ArrayList<BlockPos>();
    private BlockPos currentBlock;
    private MSTimer timer = new MSTimer();

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (mc.thePlayer.isEating() || LiquidSense.moduleManager.getModule(FreeCam.class).getState() ||
                LiquidSense.moduleManager.getModule(Blink.class).getState() || LiquidSense.moduleManager.getModule(Scaffold.class).getState() ||
                (((Aura) LiquidSense.moduleManager.getModule(Aura.class)).getBlockingStatus()))
            return;

        switch (event.getEventState()) {
            case PRE: {
                if (mc.currentScreen instanceof GuiContainer) timer.reset();

                int radius = (int) (rangeValue.get() + 0.5);

                Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(),
                        mc.thePlayer.posZ);

                BlockUtils.searchBlocks(radius).entrySet().stream().filter(it ->
                        Block.getIdFromBlock(it.getValue()) == chestValue.get() && !clickedBlocks.contains(it.getKey()) && BlockUtils.getCenterDistance(it.getKey()) < rangeValue.get()).filter(it ->{
                    if (throughWallsValue.get()) {
                        return true;
                    } else {
                        BlockPos blockPos = it.getKey();
                        MovingObjectPosition movingObjectPosition = mc.theWorld.rayTraceBlocks(eyesPos,
                                BlockExtensionUtils.getVec(blockPos), false, true, false);

                        return movingObjectPosition != null && movingObjectPosition.getBlockPos() == blockPos;
                    }
                }).min(Comparator.comparingDouble(value -> BlockUtils.getCenterDistance(value.getKey()))).ifPresent(entry -> currentBlock = entry.getKey());
                if (!rotationsValue.get()) break;
                if (currentBlock == null) return;
                VecRotation vecRotation = RotationUtils.faceBlock((currentBlock));
                if (vecRotation == null) return;
                RotationUtils.setTargetRotation(vecRotation.getRotation());
                break;
            }
            case POST: {
                if (currentBlock != null && timer.hasTimePassed((long)delayValue.get())) {
                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), currentBlock, EnumFacing.DOWN
                            , BlockExtensionUtils.getVec(currentBlock))){
                        if (visualSwing.get()) mc.thePlayer.swingItem();
                        else mc.getNetHandler().addToSendQueue(new C0APacketAnimation());

                        clickedBlocks.add(currentBlock);
                        currentBlock = null;
                        timer.reset();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onDisable() {
        clickedBlocks.clear();
    }

}
