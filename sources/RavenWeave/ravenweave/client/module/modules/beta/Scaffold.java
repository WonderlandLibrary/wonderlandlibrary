package ravenweave.client.module.modules.beta;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import ravenweave.client.event.impl.LookEvent;
import ravenweave.client.event.impl.UpdateEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.RayTraceUtils;
import ravenweave.client.utils.Utils;

public class Scaffold extends Module {

    private final TickSetting disableSprint, noSwing, slotSwap;
    private final SliderSetting pitch;

    private float yaw, prevYaw;
    private BlockPos lastPos;


    public Scaffold() {
        super("Scaffold", ModuleCategory.beta); // Category: World
        this.registerSetting(new DescriptionSetting("Helps you make bridges/scaffold walk.")); // bad description, but manthe wrote it
        this.registerSetting(pitch = new SliderSetting("Pitch", 81, 70, 90, 1));
        this.registerSettings(noSwing = new TickSetting("No Swing", false));
        this.registerSetting(disableSprint = new TickSetting("Disable sprint", true));
        //this.registerSetting(doRots = new TickSetting("Do rotations", true));
        this.registerSetting(slotSwap = new TickSetting("Swap to blocks", true));
    }

    public void onDisable() {
        if (disableSprint.isToggled()) {
            mc.thePlayer.setSprinting(true);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            KeyBinding.onTick(mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent e) {
        if (!e.isPre()) return;
        if(!Utils.Player.isPlayerInGame()) {
            return;
        }
        //if (!doRots.isToggled()) return;
        yaw = mc.thePlayer.rotationYaw - 180;

        e.setYaw(yaw);
        e.setPitch(mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown() ? 90 : (float) pitch.getInput());
        mc.thePlayer.renderYawOffset = yaw;
        mc.thePlayer.rotationYawHead = yaw;

        prevYaw = e.getYaw();
    }

    @SubscribeEvent
    public void lookEvent(LookEvent e) {
        //if (!doRots.isToggled()) return;
        e.setPrevYaw(prevYaw);
        e.setYaw(yaw);

        e.setPitch(mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown() ? 90 : (float) pitch.getInput());
        e.setPrevPitch(mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown() ? 90 : (float) pitch.getInput());
    }


    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (!Utils.Player.isPlayerInGame()) return;
        if ((mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) && slotSwap.isToggled()) swapToBlock();

        if (mc.currentScreen != null || mc.thePlayer.getHeldItem() == null) return;

        MovingObjectPosition mop = mc.objectMouseOver;
        //MovingObjectPosition mop = doRots.isToggled() ? mc.objectMouseOver : RayTraceUtils.customRayTrace(3.0, mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown() ? 90 : (float) pitch.getInput(), yaw);


        if (shouldClickBlock(mop)) {
            clickBlock(mop.getBlockPos(), mop.sideHit, mop.hitVec);
        }
        if (disableSprint.isToggled()) {
            mc.thePlayer.setSprinting(false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
            KeyBinding.onTick(mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }


    public boolean shouldClickBlock(MovingObjectPosition mop) {
        if (mop == null) return false;
        ItemStack heldItem = mc.thePlayer.getHeldItem();
        if (!(heldItem.getItem() instanceof ItemBlock)) return false;
        if (mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return false;
        BlockPos pos = mop.getBlockPos();
        if (mop.sideHit == EnumFacing.UP) {
            if (!Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) return false;
            // Remove excessive clicks
            if (!((ItemBlock) mc.thePlayer.getHeldItem().getItem()).canPlaceBlockOnSide(mc.theWorld, pos, mop.sideHit, mc.thePlayer, mc.thePlayer.getHeldItem())) return false;
        }

        if (mop.sideHit == EnumFacing.DOWN) return false;
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if (block == null || block == Blocks.air || block instanceof BlockLiquid) return false;

        return true;
    }

    public void swapToBlock() {
        for (int slot = 0; slot <= 8; slot++) {
            ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
            if (itemInSlot != null && itemInSlot.getItem() instanceof ItemBlock
                    && (((ItemBlock) itemInSlot.getItem()).getBlock().isFullBlock()
                    || ((ItemBlock) itemInSlot.getItem()).getBlock().isFullCube())) {
                if (mc.thePlayer.inventory.currentItem != slot) {
                    mc.thePlayer.inventory.currentItem = slot;
                } else {
                    return;
                }
                return;
            }
        }
    }

    public void clickBlock(BlockPos pos, EnumFacing enumFacing, Vec3 vec3) {
        new Thread(() -> {
            try {
                boolean success = mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, enumFacing, vec3);
                if (!noSwing.isToggled()) {
                    mc.thePlayer.swingItem();
                }
                if (success) {
                    lastPos = pos;
                }
            } catch (Exception ignored) {}
        }).start();
    }
}
