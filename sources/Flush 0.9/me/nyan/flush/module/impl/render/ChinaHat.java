package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ColorSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public class ChinaHat extends Module {
    private final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, true),
            myself = new BooleanSetting("Myself", this, true),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false);
    private final NumberSetting radius = new NumberSetting("Radius", this, 100, 50, 200),
            height = new NumberSetting("Height", this, 50, 0, 200);
    private final ColorSetting color = new ColorSetting("Color", this, 0xFFFF0000);

    public ChinaHat() {
        super("ChinaHat", Category.RENDER);
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        int offset = 0;

        ArrayList<Entity> entities = mc.theWorld.loadedEntityList.stream()
                .sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(entities);

        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase && isValid((EntityLivingBase) entity)) {
                if (entity == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) {
                    continue;
                }
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().renderPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().renderPosZ;
                int color = ColorUtils.alpha(rainbow.getValue() ?
                        ColorUtils.getRainbow(offset / 8, 0.8F, 2)
                        : this.color.getRGB(), 120);

                AxisAlignedBB bb = entity.getEntityBoundingBox();
                Vector3d vector3d = new Vector3d(
                        bb.minX - entity.posX + x + (entity.boundingBox.maxX - entity.boundingBox.minX) / 2F,
                        y + (bb.maxY - bb.minY) + 0.1F,
                        bb.minZ - entity.posZ + z + (entity.boundingBox.maxZ - entity.boundingBox.minZ) / 2F
                );

                float radius = this.radius.getValueInt() / 100F;

                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableTexture2D();
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL_SMOOTH);

                if (entity == mc.thePlayer) {
                    GlStateManager.rotate(-entity.rotationYaw + 180, 0, 1, 0);
                }

                RenderUtils.glColor(ColorUtils.brighter(color));
                glBegin(GL_TRIANGLE_FAN);
                glVertex3d(vector3d.x, vector3d.y + height.getValueInt() / 100F, vector3d.z);

                RenderUtils.glColor(ColorUtils.brightness(color, 0.24F));

                for (float i = -180; i <= 180; i++) {
                    float radians = i / 180F * MathHelper.PI;
                    glVertex3d(vector3d.x + MathHelper.cos(radians) * (radius - 0.5F), vector3d.y, vector3d.z + MathHelper.sin(radians) * (radius - 0.5F));
                }
                glEnd();
                RenderUtils.glColor(-1);

                GlStateManager.shadeModel(GL_FLAT);
                GlStateManager.enableCull();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();

                GlStateManager.color(1, 1, 1, 1);

                offset++;
            }
        }
    }

    public boolean isValid(EntityLivingBase entity) {
        return (myself.getValue() && entity == mc.thePlayer) ||
                PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(), villagers.getValue(), invisibles.getValue(),
                ignoreTeam.getValue());
    }
}