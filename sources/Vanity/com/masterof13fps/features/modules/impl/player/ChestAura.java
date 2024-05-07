package com.masterof13fps.features.modules.impl.player;

import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.manager.settingsmanager.Setting;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MovingObjectPosition;

import java.util.ArrayList;

@ModuleInfo(name = "ChestAura", category = Category.PLAYER, description = "Steals items from chests around you")
public class ChestAura extends Module {

    Setting range = new Setting("Range", this, 5, 1, 10, true);

    float yaw, pitch;
    ArrayList<TileEntityChest> openedChests;

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        try {
            openedChests.clear();
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            for (final TileEntity tileEntity : getWorld().loadedTileEntityList) {
                if (tileEntity instanceof TileEntityChest) {
                    final TileEntityChest chest = (TileEntityChest) tileEntity;
                    if (getPlayer().getDistanceSqToCenter(chest.getPos()) >= range.getCurrentValue() || openedChests.contains(chest) || mc.currentScreen != null) {
                        continue;
                    }
                    if (event instanceof EventUpdate) {
                        final float[] rotations = faceBlock(chest.getPos(), false, this.yaw, this.pitch, 360.0f);
                        this.yaw = rotations[0];
                        this.pitch = rotations[1];
                        getPlayer().swingItem();
                        final MovingObjectPosition rayCast = getMouseOver(getPlayer(), this.yaw, this.pitch, range.getCurrentValue());
                        openedChests.add(chest);
                        ChestAura.mc.playerController.onPlayerRightClick(ChestAura.mc.thePlayer, ChestAura.mc.theWorld, getPlayer().getCurrentEquippedItem(), chest.getPos(), rayCast.sideHit, rayCast.hitVec);
                    }
                }
            }
        }
    }
}
