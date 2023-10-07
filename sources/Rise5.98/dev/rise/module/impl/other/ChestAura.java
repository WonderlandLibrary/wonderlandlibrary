package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

@ModuleInfo(name = "ChestAura", description = "Opens nearby chests for you", category = Category.PLAYER)
public final class ChestAura extends Module {

    private final NumberSetting range = new NumberSetting("Range", this, 5, 1, 6, 0.1);
    private final ArrayList<BlockPos> clickedChests = new ArrayList<>();
    private BlockPos chestToOpen;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.currentScreen instanceof GuiContainer) return;
        if (chestToOpen != null) {
            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), chestToOpen, EnumFacing.DOWN, new Vec3(chestToOpen))) {
                mc.thePlayer.swingItem();
                chestToOpen = null;
                return;
            }
        }

        for (final TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
            if (tileEntity instanceof TileEntityChest && mc.thePlayer.getDistance(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) < range.getValue()) {
                if (clickedChests.contains(tileEntity.getPos())) continue;
                final TileEntityChest chest = (TileEntityChest) tileEntity;
                clickedChests.add(chest.getPos());
                chestToOpen = chest.getPos();
            }
        }
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        clickedChests.clear();
    }

    @Override
    protected void onEnable() {
        clickedChests.clear();
        chestToOpen = null;
    }
}
