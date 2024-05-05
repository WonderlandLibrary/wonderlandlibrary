package slavikcodd3r.rainbow.module.modules.render;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.Render3DEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.RenderUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;

@Mod (displayName = "BlockOverlay")
public class BlockOverlay extends Module
{
    @EventTarget
    private void onPreUpdate(final UpdateEvent event) {
        event.getState();
    }
    
    @EventTarget
    private void onRender3D(final Render3DEvent event) {
        if (ClientUtils.mc().objectMouseOver.getBlockPos() != null) {
            ClientUtils.mc();
            final Block block = Minecraft.getMinecraft().theWorld.getBlockState(ClientUtils.mc().objectMouseOver.getBlockPos()).getBlock();
            if (block.getMaterial() != Material.air) {
                final double n = ClientUtils.mc().objectMouseOver.getBlockPos().getX();
                ClientUtils.mc().getRenderManager();
                final double posX = n - RenderManager.renderPosX;
                final double n2 = ClientUtils.mc().objectMouseOver.getBlockPos().getY();
                ClientUtils.mc().getRenderManager();
                final double posY = n2 - RenderManager.renderPosY;
                final double n3 = ClientUtils.mc().objectMouseOver.getBlockPos().getZ();
                ClientUtils.mc().getRenderManager();
                final double posZ = n3 - RenderManager.renderPosZ;
                final int color = new Color(0, 66, 255).getRGB();
                AxisAlignedBB box = new AxisAlignedBB(posX, posY, posZ, posX + 1.0, posY + 1.0, posZ + 1.0);
                RenderUtils.filledBox(box, color & 0x19FFFFFF, true);
            }
        }
    }
}
