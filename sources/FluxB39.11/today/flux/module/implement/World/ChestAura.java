package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import today.flux.event.PostUpdateEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.event.RespawnEvent;
import today.flux.event.WorldRenderEvent;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.value.FloatValue;
import today.flux.utility.BlockUtils;
import today.flux.utility.ChatUtils;
import today.flux.utility.TimeHelper;

import java.util.ArrayList;
import java.util.List;

public class ChestAura extends Module {
    private BlockPos globalPos, openingPos;
    public static List<BlockPos> list = new ArrayList<>();
    public static FloatValue maxChest = new FloatValue("ChestAura", "Max Chests", 3.0f, 1.0f, 30.0f, 1f);
    public static FloatValue range = new FloatValue("ChestAura", "Range", 2.0f, 1.0f, 6.0f, 0.1f);

    public TimeHelper waitBoxOpenTimer = new TimeHelper();
    public static boolean isWaitingOpen = false;
    public ChestAura() {
        super("ChestAura", Category.World, false);
    }

    @EventTarget
    public void onReload(RespawnEvent e) {
        list.clear();
    }

    @EventTarget
    public void onPre(PreUpdateEvent e) {
        globalPos = null;
        if (e.isModified() || mc.thePlayer.ticksExisted % 20 == 0 || KillAura.target != null || mc.currentScreen instanceof GuiContainer || ModuleManager.scaffoldMod.isEnabled()) {
            return;
        }

        if (list.size() >= maxChest.getValue())
            return;

        float radius;
        float y = radius = range.getValue();
        while (y >= -radius) {
            float x = -radius;
            while (x <= radius) {
                float z = -radius;
                while (z <= radius) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX - 0.5 + (double) x, mc.thePlayer.posY - 0.5 + (double) y, mc.thePlayer.posZ - 0.5 + (double) z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();
                    BlockPos targetPos = new BlockPos(mc.thePlayer.posX + (double) x, mc.thePlayer.posY + (double) y, mc.thePlayer.posZ + (double) z);
                    if (mc.thePlayer.getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < (double) mc.playerController.getBlockReachDistance() && block instanceof BlockChest && !list.contains(pos)) {
                        float[] rotations = BlockUtils.getBlockRotations(pos.getX(), pos.getY(), pos.getZ());
                        e.setYaw(rotations[0]);
                        e.setPitch(rotations[1]);
                        this.globalPos = pos;
                        return;
                    }
                    ++z;
                }
                ++x;
            }
            --y;
        }
    }

    @EventTarget
    public void onPost(PostUpdateEvent e) {
        if (isWaitingOpen) {
            if (waitBoxOpenTimer.isDelayComplete(600)) {
                ChatUtils.debug("Failed to open chest!");
                isWaitingOpen = false;
            } else if (openingPos != null && mc.currentScreen instanceof GuiContainer) {
                ChatUtils.debug("Open chest successfully! " + openingPos);
                list.add(openingPos);
                openingPos = null;
                isWaitingOpen = false;
            }
        }

        if (this.globalPos != null && !(mc.currentScreen instanceof GuiContainer) && list.size() < maxChest.getValue()) {
            if (!isWaitingOpen && !list.contains(globalPos)) {
                sendClick(globalPos);
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0APacketAnimation());
            }
        }
    }

    @EventTarget
    public void onRender(WorldRenderEvent e) {
        for (BlockPos pos : list) {
            double x = pos.getX() - mc.getRenderManager().renderPosX;
            double y = pos.getY() - mc.getRenderManager().renderPosY;
            double z = pos.getZ() - mc.getRenderManager().renderPosZ;
            RenderUtil.drawEntityESP(x, y, z, x + 1, y + 1, z + 1, 0, 1, 0, 0.3F);
        }
    }

    public void sendClick(BlockPos pos) {
        C08PacketPlayerBlockPlacement packet = new C08PacketPlayerBlockPlacement(pos, (double)pos.getY() + 0.5 < mc.thePlayer.posY + 1.7 ? 1 : 0, mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f);
        mc.thePlayer.sendQueue.addToSendQueue(packet);
        waitBoxOpenTimer.reset();
        isWaitingOpen = true;
        openingPos = globalPos;
    }
}
