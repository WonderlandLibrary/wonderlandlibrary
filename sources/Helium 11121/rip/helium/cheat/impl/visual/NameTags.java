package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.FriendManager;
import rip.helium.event.minecraft.EntityRenderEvent;
import rip.helium.event.minecraft.RenderPlayerNameEvent;
import rip.helium.utils.property.impl.DoubleProperty;

import java.awt.*;
import java.text.DecimalFormat;

public class NameTags extends Cheat {
    private final static DecimalFormat decimalFormat = new DecimalFormat("#.#");
    DoubleProperty darkness = new DoubleProperty("Darkness", "Darkness of tags", null, 0.3, 0.1, 1.0,
            0.1, null);

    public NameTags() {
        super("NameTags", "Shows a larger player nametag.", CheatCategory.VISUAL);
        registerProperties(darkness);
    }


    public void onEnable() {

    }

    public void onDisable() {

    }

    @Collect
    public void disableRender(RenderPlayerNameEvent event) {
        event.setCancelled(true);
    }

    @Collect
    public void renderTag(EntityRenderEvent e) {


        if (!Minecraft.isGuiEnabled())
            return;
        for (Object obj : mc.theWorld.playerEntities) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) obj;
                if (!isRenderingPossible(player))
                    continue;
                float partialTicks = e.getPartialTicks();
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - RenderManager.renderPosX;
                double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - RenderManager.renderPosY;
                double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - RenderManager.renderPosZ;
                drawNametags(player, x, y, z);
            }
        }
    }

    public void drawNametags(EntityLivingBase entity, double x, double y, double z) {

        String entityName = entity.getDisplayName().getFormattedText();
        if (entity == mc.thePlayer)
            return;
        if (entity.isInvisible() && mc.getCurrentServerData().serverIP.contains("hypixel"))
            return;
        if (entity.isDead && !mc.getCurrentServerData().serverIP.contains("mineplex"))
            return;
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying)
            entityName = "" + entityName;
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)
            entityName = "\0248[C] " + entityName;
        if (entity.getDistanceToEntity(mc.thePlayer) >= 64.0F)
            entityName = "" + entityName;

        if (getNametagColor(entity) != 0xFFFFFFFF) entityName = StringUtils.stripControlCodes(entityName);
        double health = entity.getHealth() / 2;
        double maxHealth = entity.getMaxHealth() / 2;
        double percentage = 100 * (health / maxHealth);
        String healthColor;


        if (percentage > 75) healthColor = "a";
        else if (percentage > 50) healthColor = "e";
        else if (percentage > 25) healthColor = "4";
        else healthColor = "4";

        String healthDisplay = decimalFormat.format(Math.floor((health + (double) 0.5F / 2) / 0.5F) * 0.5F);
        String healthDisplay1 = decimalFormat.format(Math.floor(((double) 0.5F / 2) / 0.5F) * 0.5F);


        String maxHealthDisplay = decimalFormat.format(Math.floor((entity.getMaxHealth() + (double) 0.5F / 2) / 0.5F) * 0.5F);
        if (mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.contains("mineplex"))
            entityName = String.format("%s §4%s \u2764", entity.getName(), healthDisplay);
        if (FriendManager.isFriend(entity.getName()))
            entityName = String.format("[F] %s ", entityName);

        if (Helium.instance.focusManager.isFocused(entity.getName())) {
            entityName = "§c[Focused] " + entity.getName();
        }

        float distance = mc.thePlayer.getDistanceToEntity(entity);
        float var13 = (distance / 5 <= 2 ? 2.0F : distance / 5) * 0.7F;
        float var14 = 0.016666668F * var13;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(x + 0.0F, y + entity.height + 0.4F, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        if (mc.gameSettings.thirdPersonView == 2) {
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(RenderManager.playerViewX, -1.0F, 0.0F, 0.0F);
        } else {
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        }
        GlStateManager.scale(-var14, -var14, var14);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        int var17 = 0;
        if (entity.isSneaking()) {
            var17 += 4;
        }
        var17 -= distance / 5;
        if (var17 < -8) {
            var17 = -8;
        }
        GlStateManager.disableTexture2D();
//        worldRenderer.startDrawingQuads();
        float var18 = mc.fontRendererObj.getStringWidth(entityName) / 2;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(-var18 + 2, -3 + var17, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
        worldRenderer.pos(-var18 + 2, 9 + var17, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
        worldRenderer.pos(var18 + 1, 8 + var17, 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
        worldRenderer.pos(var18 + 1, -3 + var17, 0.0D).color(0.0F, 0.0F, 0.0F, darkness.getValue().floatValue()).endVertex();
//        worldRenderer.putColorMultiplier(0.0F, 0.0F, 0.0F, (int) 0.3);
//        worldRenderer.pos(-var18 - 1, -5 + var17, 0.0D).endVertex();
//        worldRenderer.pos(-var18 - 1, 10 + var17, 0.0D).endVertex();
//        worldRenderer.pos(var18 + 1, 10 + var17, 0.0D).endVertex();
//        worldRenderer.pos(var18 + 1, -5 + var17, 0.0D).endVertex();
//        Gui.drawRect(-var18, -5 + var17, var18 + 2, var17 - 3, new Color(25, 25, 25, 120).getRGB());
        tessellator.draw();
        GlStateManager.enableTexture2D();
        mc.fontRendererObj.drawStringWithShadow(entityName, -var18, var17 - 1, getNametagColor(entity));
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }


    public double interpolate(double start, double end, double percent) {
        return start + (end - start) * percent;
    }

    public int getNametagColor(EntityLivingBase entity) {
        int i = 0;
        int color = 0xFFFFFFFF;
        if (entity instanceof EntityPlayer && FriendManager.isFriend(entity.getName())) {
            color = Color.CYAN.getRGB();
        } else if (entity.isInvisibleToPlayer(mc.thePlayer)) {
            //color = 27753;
        } else if (entity.isSneaking()) {
            //color = 7761001;

        } else if (entity.isEntityAlive() && !mc.getCurrentServerData().serverIP.contains("mineplex")) {

        }


        return color;
    }

    private boolean isRenderingPossible(Entity entity) {
        return entity != null && entity != mc.thePlayer;
    }

}
    
    
    

 

