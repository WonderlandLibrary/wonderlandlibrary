package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.client.renderer.RenderGlobal;
import java.awt.Color;
import net.minecraft.util.AxisAlignedBB;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.Render3DEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.RenderUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.GlStateManager;
@Module.Mod(displayName = "BlockESP")
public class BlockESP extends Module
{
	@Op(name = "Chest")
    public boolean chest;
	@Op(name = "EnderChest")
    public boolean enderchest;
	@Op(name = "Dispenser")
    public boolean dispenser;
	@Op(name = "Dropper")
    public boolean dropper;
	@Op(name = "Hopper")
    public boolean hopper;
	@Op(name = "Furnace")
    public boolean furnace;
    
    public BlockESP() {
        this.chest = true;
        this.enderchest = true;
        this.dispenser = true;
        this.dropper = true;
        this.hopper = true;
        this.furnace = true;
    }
    
    @EventTarget
    private void onRender(final Render3DEvent event) {
        GlStateManager.pushMatrix();
        for (final Object o : ClientUtils.world().loadedTileEntityList) {
        	final TileEntity ent = (TileEntity)o;
            if (!(ent instanceof TileEntityChest) && !(ent instanceof TileEntityEnderChest) && !(ent instanceof TileEntityDispenser) && !(ent instanceof TileEntityDropper) && !(ent instanceof TileEntityHopper) && !(ent instanceof TileEntityFurnace)) {
                continue;
            }
            if (ent instanceof TileEntityChest && !this.chest) {
                continue;
            }
            if (ent instanceof TileEntityEnderChest && !this.enderchest) {
                continue;
            }
            if (ent instanceof TileEntityDispenser && !this.dispenser) {
                continue;
            }
            if (ent instanceof TileEntityDropper && !this.dropper) {
                continue;
            }
            if (ent instanceof TileEntityHopper && !this.hopper) {
                continue;
            }
            if (ent instanceof TileEntityFurnace && !this.furnace) {
                continue;
            }
            this.drawEsp(ent, event.getPartialTicks());
        }
        GlStateManager.popMatrix();
    }
    
    private void drawEsp(final TileEntity ent, final float pTicks) {
        final double x1 = ent.getPos().getX() - RenderManager.renderPosX;
        final double y1 = ent.getPos().getY() - RenderManager.renderPosY;
        final double z1 = ent.getPos().getZ() - RenderManager.renderPosZ;
        final int color = new Color(0, 66, 255).getRGB();
        AxisAlignedBB box = new AxisAlignedBB(x1, y1, z1, x1 + 1.0, y1 + 1.0, z1 + 1.0);
        if (ent instanceof TileEntityChest) {
            final TileEntityChest chest = TileEntityChest.class.cast(ent);
            if (chest.adjacentChestZPos != null) {
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 1.9375);
            }
            else if (chest.adjacentChestXPos != null) {
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 1.9375, y1 + 0.875, z1 + 0.9375);
            }
            else {
                if (chest.adjacentChestZPos != null || chest.adjacentChestXPos != null || chest.adjacentChestZNeg != null || chest.adjacentChestXNeg != null) {
                    return;
                }
                box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 0.9375);
            }
        }
        else if (ent instanceof TileEntityEnderChest) {
            box = new AxisAlignedBB(x1 + 0.0625, y1, z1 + 0.0625, x1 + 0.9375, y1 + 0.875, z1 + 0.9375);
        }
        RenderUtils.filledBox(box, color & 0x19FFFFFF, true);
    }
}
