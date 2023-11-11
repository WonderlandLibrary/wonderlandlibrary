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
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
    private final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false),
            players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false);
    private final ColorSetting color = new ColorSetting("Color", this, -1);
    private final NumberSetting lineWidth = new NumberSetting("Line Width", this, 0.6, 0.2, 5, 0.1);

    public Tracers() {
        super("Tracers", Category.RENDER);
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks()) - mc.getRenderManager().renderPosX;
            double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks()) - mc.getRenderManager().renderPosY;
            double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks()) - mc.getRenderManager().renderPosZ;

            if (!(entity instanceof EntityLivingBase && isValid((EntityLivingBase) entity))) {
                continue;
            }

            int color = rainbow.getValue() ? ColorUtils.getRainbow(1, 0.8F, 2) : this.color.getRGB();

            GlStateManager.loadIdentity();
            mc.entityRenderer.orientCamera(e.getPartialTicks());
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            RenderUtils.glColor(ColorUtils.alpha(color, 170));
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(lineWidth.getValueFloat());
            drawTracers(x, y + (double) entity.getEyeHeight(), z);
            GlStateManager.enableDepth();
            GlStateManager.color(1,1,1,1);
        }
    }

    public void drawTracers(double x, double y, double z) {
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(0.0, mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
    }

    public boolean isValid(EntityLivingBase entity) {
        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(), villagers.getValue(), invisibles.getValue(),
                                   ignoreTeam.getValue());
    }
}
