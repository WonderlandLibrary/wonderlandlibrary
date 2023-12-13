package koks.manager.module.impl.render;

import koks.Koks;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventOutline;
import koks.manager.event.impl.EventRender3D;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

/**
 * @author kroko
 * @created on 07.10.2020 : 15:46
 */

@ModuleInfo(name = "ChestESP", description = "Marks all chests in the world.", category = Module.Category.RENDER)
public class ChestESP extends Module {

    public Setting espMode = new Setting("ESP Mode", new String[]{"2D Style", "Box", "Shader", "Cylinder"}, "Box", this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventOutline) {
            if (espMode.getCurrentMode().equalsIgnoreCase("Shader"))
                ((EventOutline) event).setOutline(true);
        }

        if (event instanceof EventUpdate) {
            setInfo(espMode.getCurrentMode());
        }
        if (event instanceof EventRender3D) {
            float partialTicks = ((EventRender3D) event).getPartialTicks();
            for (TileEntity e : mc.theWorld.loadedTileEntityList) {
                if (e instanceof TileEntityChest || e instanceof TileEntityEnderChest) {
                    mc.theWorld.getBlockState(e.getPos()).getBlock();
                    double x = (e.getPos().getX() - mc.getRenderManager().renderPosX);
                    double y = (e.getPos().getY() - mc.getRenderManager().renderPosY);
                    double z = (e.getPos().getZ() - mc.getRenderManager().renderPosZ);
                    if (espMode.getCurrentMode().equals("2D Style")) {
                        espUtil.drawCorners(x + 0.5, y + 0.5, z + 0.5, 16, 16, 8, 3);
                    }
                    if (espMode.getCurrentMode().equals("Box")) {
                        AxisAlignedBB axisAlignedBB = e.getBlockType().getSelectedBoundingBox(mc.theWorld, e.getPos()).offset(-mc.getRenderManager().renderPosX, -mc.getRenderManager().renderPosY, -mc.getRenderManager().renderPosZ);
                        espUtil.renderBox(axisAlignedBB);
                    }
                    if (espMode.getCurrentMode().equalsIgnoreCase("Cylinder")) {

                        GL11.glPushMatrix();
                        GL11.glTranslated(x + 0.5, y + 0.9, z + 0.5);
                        GL11.glRotated(90, 1, 0, 0);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);

                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glLineWidth(1);
//                        renderUtil.setColor(Color.red);
                        renderUtil.setColor(Koks.getKoks().clientColor);
                        Cylinder cylinder = new Cylinder();
//
                        cylinder.setDrawStyle(GLU.GLU_LINE);
                        cylinder.setOrientation(GLU.GLU_INSIDE);
                        cylinder.draw(0.62f, 0.62f, 0.9f, 8, 1);

                        renderUtil.setColor(renderUtil.getAlphaColor(Koks.getKoks().clientColor, 150));
                        cylinder.setDrawStyle(GLU.GLU_FILL);
                        cylinder.setOrientation(GLU.GLU_INSIDE);
                        cylinder.draw(0.62f, 0.65f, 0.9f, 8, 1);

                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glPopMatrix();
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
