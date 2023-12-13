package summer.cheat.cheats.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.render.EventRender3D;
import summer.base.manager.config.Cheats;

public class ChestESP extends Cheats {
    public static String chestMode;
    public Minecraft mc = Minecraft.getMinecraft();

    public static Setting colorRed;
    public static Setting colorGreen;
    public static Setting colorBlue;
    public static Setting chestType;


    public ChestESP() {
        super("ChestESP", "Allows you to see chests through walls", Selection.RENDER);

        ArrayList chestMode = new ArrayList();
        chestMode.add("Outline");
        chestMode.add("Fill");
        Summer.INSTANCE.settingsManager.Property(chestType = new Setting("Chest Type", this, "Outline", chestMode));
        Summer.INSTANCE.settingsManager.Property(colorRed = new Setting("Red", this, 249, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(colorGreen = new Setting("Green", this, 255, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(colorBlue = new Setting("Blue", this, 0, 0, 255, true));

    }

    @EventTarget
    public void render(EventRender3D e) {
        for (final Object o : mc.theWorld.loadedTileEntityList) {
            if (o instanceof TileEntityChest) {
                final TileEntityLockable storage = (TileEntityLockable) o;
                this.drawESPOnStorage(storage, storage.getPos().getX(), storage.getPos().getY(),
                        storage.getPos().getZ());
            }
        }
    }

    public void drawESPOnStorage(final TileEntityLockable storage, final double x, final double y, final double z) {
        this.setDisplayName("ChestESP\2477 " + chestType.getValString());
        if (chestType.getValString().equalsIgnoreCase("Fill")) {
            assert !storage.isLocked();
            final TileEntityChest chest = (TileEntityChest) storage;
            Vec3 vec = new Vec3(0.0, 0.0, 0.0);
            Vec3 vec2 = new Vec3(0.0, 0.0, 0.0);
            if (chest.adjacentChestZNeg != null) {
                vec = new Vec3(x + 0.0625, y, z - 0.9375);
                vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
            } else if (chest.adjacentChestXNeg != null) {
                vec = new Vec3(x + 0.9375, y, z + 0.0625);
                vec2 = new Vec3(x - 0.9375, y + 0.875, z + 0.9375);
            } else {
                if (chest.adjacentChestXPos != null || chest.adjacentChestZPos != null) {
                    return;
                }
                vec = new Vec3(x + 0.0625, y, z + 0.0625);
                vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
            }
            GL11.glPushMatrix();
            pre3D();
            GlStateManager.disableDepth();
            mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
            GL11.glColor4f(colorRed.getValFloat() / 255F,
                    colorGreen.getValFloat() / 255F,
                    colorBlue.getValFloat() / 255F, 0.3F);
            drawFilledBoundingBox(
                    new AxisAlignedBB(vec.xCoord - RenderManager.renderPosX, vec.yCoord - RenderManager.renderPosY,
                            vec.zCoord - RenderManager.renderPosZ, vec2.xCoord - RenderManager.renderPosX,
                            vec2.yCoord - RenderManager.renderPosY, vec2.zCoord - RenderManager.renderPosZ));
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.enableDepth();
            post3D();
            GL11.glPopMatrix();
        }
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(3154, 4354);
    }

    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawFilledBoundingBox(final AxisAlignedBB box) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        tessellator.draw();
    }

    public void updateSettings() {
        this.chestMode = chestType.getValString();
    }
}
