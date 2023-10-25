package club.async.module.impl.player;

import club.async.event.impl.*;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.system.CallbackI;
import rip.hippo.lwjeb.annotation.Handler;

import java.util.Objects;

@ModuleInfo(name = "Scaffold", description = "Place blocks below you", category = Category.PLAYER)
public class Scaffold extends Module {

    private Vec3 pos;
    private BlockPos blockPos;
    private ScaffoldUtil.BlockData blockData;
    private float[] rotations = new float[1];
    private int slot;
    private double y;
    private final TimeUtil timer = new TimeUtil();

    public ModeSetting placeTiming = new ModeSetting("PlaceTiming", this, new String[] {"Pre", "Post"});
    public NumberSetting placeDelay = new NumberSetting("Place Delay", this, 0, 1000, 0, 1);
    public ModeSetting rotationMode = new ModeSetting("Rotations", this, new String[]{"Normal", "Static"});
    public BooleanSetting safeWalk = new BooleanSetting("SafeWalk", this, false);
    public BooleanSetting sprint = new BooleanSetting("Sprint", this, true);
    public BooleanSetting silent = new BooleanSetting("Silent", this, true);
    public BooleanSetting keepY = new BooleanSetting("KeepY", this, true);
    public BooleanSetting showCount = new BooleanSetting("RenderBlockCount", this, true);

    @Handler
    public void onUpdate(Event2D event) {
        if(showCount.get()) {
            RenderUtil.drawStringOutlined(ScaffoldUtil.getBlockCount() + "", (int) (event.getScaledResolution().getScaledWidth() / 2f - (mc.fontRendererObj.getStringWidth(ScaffoldUtil.getBlockCount() + "") / 2f)), (int) (event.getScaledResolution().getScaledHeight() / 2f - 40), -1);
        }
    }

    @Handler
    public void onUpdate(EventPreUpdate event) {

        if(mc.thePlayer.posY < y || (!mc.thePlayer.onGround && !MovementUtil.isMoving()) || mc.thePlayer.posY - y > 6 || !keepY.get()) y = mc.thePlayer.posY - 0.9D;

        pos = new Vec3(mc.thePlayer.posX, y , mc.thePlayer.posZ);
        blockPos = new BlockPos(pos.xCoord, pos.yCoord, pos.zCoord);

        blockData = ScaffoldUtil.getBlockData(blockPos);

        if (blockData == null || ScaffoldUtil.getBlockSlot() == -1)
            return;

        if(!sprint.get()){
            mc.thePlayer.setSprinting(false);
        }
        if (rotationMode.is("Normal"))
            rotations = RotationUtil.getRotations(blockData.position, blockData.face);
        else {
            rotations[0] = mc.thePlayer.rotationYaw + 180;
            rotations[1] = RotationUtil.getRotations(blockData.position, blockData.face)[1];
        }
        mc.thePlayer.rotationYawHead = rotations[0];
        mc.thePlayer.renderYawOffset = rotations[0];

        event.setYaw(rotations[0]);
        event.setPitch(rotations[1]);

        if(placeTiming.is("pre")) {
            doPlacement();
        }

    }

    @Handler
    public void onSafeWalk(EventSafeWalk event) {
        event.setCancelled(safeWalk.get());
    }

    @Handler
    public void onPreUpdate(EventPostUpdate event) {
        if (blockData == null || ScaffoldUtil.getBlockSlot() == -1)
            return;

        if(placeTiming.is("post")) {
            doPlacement();
        }
    }

    public void doPlacement() {
        slot = mc.thePlayer.inventory.currentItem;
        if (WorldUtil.getBlock(blockPos).getMaterial() == Material.air && timer.hasTimePassed(placeDelay.getInt())) {
            mc.thePlayer.inventory.currentItem = ScaffoldUtil.getBlockSlot();
            mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockData.position, blockData.face, pos.addVector(0.5,0.5,0.5));
            timer.reset();
        }
        if (silent.get()) mc.thePlayer.inventory.currentItem = slot;
    }

    @Handler
    public void onRenderPitch(EventRenderPitch event) {
        if (blockData == null || ScaffoldUtil.getBlockSlot() == -1 || rotations == null)
            return;

        event.setPitch(rotations[1]);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        y = mc.thePlayer.posY - 0.9D;
        slot = mc.thePlayer.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.inventory.currentItem = slot;
    }

}
