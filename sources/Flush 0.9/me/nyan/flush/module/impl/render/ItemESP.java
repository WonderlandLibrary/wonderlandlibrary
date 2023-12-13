package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ColorSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ItemESP extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Box & BoxOutline", "Box & BoxOutline", "Box", "BoxOutline");
    private final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, true);
    private final ColorSetting color = new ColorSetting("Color", this, 0xFFFF0000);
    private final NumberSetting outlineWidth = new NumberSetting("Outline Width", this, 1, 0.2, 3, 0.1);

    public ItemESP() {
        super("ItemESP", Category.RENDER);
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        int offset = 0;

        ArrayList<Entity> entities = mc.theWorld.loadedEntityList.stream()
                .sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(entities);

        for (Entity entity : entities) {
            if (isValid(entity)) {
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().renderPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().renderPosZ;
                int color = rainbow.getValue() ? ColorUtils.getRainbow(offset / 8, 0.8F, 2) : this.color.getRGB();

                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);

                switch (mode.getValue().toUpperCase()) {
                    case "BOX & BOXOUTLINE":
                        drawESPBox(entity, x, y, z, ColorUtils.alpha(color, 100));
                    case "BOXOUTLINE":
                        drawESPBoxOutline(entity, x, y, z, ColorUtils.alpha(ColorUtils.brighter(color), 255));
                        break;

                    case "BOX":
                        drawESPBox(entity, x, y, z, ColorUtils.alpha(color, 100));
                        break;
                }

                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.color(1, 1, 1, 1);

                offset++;
            }
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    private void drawESPBoxOutline(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        GL11.glLineWidth(outlineWidth.getValueFloat());
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderUtils.drawBoundingBoxOutline(axisAlignedBB, color);
    }

    private void drawESPBox(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        RenderUtils.fillBoundingBox(axisAlignedBB, color);
    }

    public boolean isValid(Entity entity) {
        return entity instanceof EntityItem;
    }
}