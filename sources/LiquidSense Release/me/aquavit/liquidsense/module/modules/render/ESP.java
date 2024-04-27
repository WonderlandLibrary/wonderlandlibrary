package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.render.shader.FramebufferShader;
import me.aquavit.liquidsense.utils.render.shader.shaders.GlowShader;
import me.aquavit.liquidsense.utils.render.shader.shaders.OutlineShader;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static me.aquavit.liquidsense.utils.render.AnimationUtils.getHealthColor;

@ModuleInfo(name = "ESP", description = "Allows you to see targets through walls.", category = ModuleCategory.RENDER)
public class ESP extends Module {

    public final ListValue modeValue = new ListValue("Mode", new String[] {"Box","New2D","NewBox", "OtherBox", "WireFrame", "2D", "Outline", "ShaderOutline", "ShaderGlow","Shadow"}, "Box");
    public final FloatValue outlineWidth = new FloatValue("Outline-Width", 3F, 0.5F, 5F);
    public final FloatValue wireframeWidth = new FloatValue("WireFrame-Width", 2F, 0.5F, 5F);

    private final FloatValue shaderOutlineRadius = new FloatValue("ShaderOutline-Radius", 1.35F, 1F, 2F);
    private final FloatValue shaderGlowRadius = new FloatValue("ShaderGlow-Radius", 2.3F, 2F, 3F);

    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final IntegerValue shadowRedValue = new IntegerValue("ShadowR", 255, 0, 255);
    private final IntegerValue shadowGreenValue = new IntegerValue("ShadowG", 255, 0, 255);
    private final IntegerValue shadowBlueValue = new IntegerValue("ShadowB", 255, 0, 255);
    private final FloatValue width2d = new FloatValue("NewWidth2D", 0.5f, 0.0f, 0.5f);
    private final BoolValue outline = new BoolValue("NewOutLine", true);
    private final BoolValue health = new BoolValue("NewHealth", true);
    private final BoolValue armr = new BoolValue("NewArmor", true);
    private final BoolValue rect = new BoolValue("NewRect", true);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final BoolValue colorTeam = new BoolValue("Team", false);
    private FloatValue range = new FloatValue("ShadowRange", 1.0F, 0.0F, 5.0F);

    public static boolean renderNameTags = true;
    private List<Vec3> positions = new ArrayList<Vec3>();

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        final String mode = modeValue.get();
        for(final Entity entity : mc.theWorld.loadedEntityList) {
            if(entity != null && entity != mc.thePlayer && EntityUtils.isSelected(entity, false, false)) {
                final EntityLivingBase entityLiving = (EntityLivingBase) entity;
                final RenderManager renderManager = mc.getRenderManager();
                final Timer timer = mc.timer;
                final double posX = entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX;
                final double posY = entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY;
                final double posZ = entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ;

                switch(mode.toLowerCase()) {
                    case "box":
                    case "otherbox":
                        RenderUtils.drawEntityBox(entity, getColor(entityLiving), !mode.equalsIgnoreCase("otherbox"));
                        break;
                    case "2d":
                        RenderUtils.draw2D(entityLiving, posX, posY, posZ, getColor(entityLiving).getRGB(), Color.BLACK.getRGB());
                        break;
                    case "shadow":
                        RenderUtils.shadow(entityLiving, (double) posX, (double) posY, posZ, range.get(), 64,shadowgetColor(entityLiving).getRGB());
                        RenderUtils.cylinder(entityLiving, (double) posX, (double) posY, posZ, range.get(), 64,getColor(entityLiving).getRGB());
                        break;
                    case "new2d":
                    case "newbox":
                        RenderUtils.updateView();
                        break;
                }
            }
        }
    }

    public void updatePositions(Entity entity) {
        positions.clear();
        Vec3 position = RenderUtils.getEntityRenderPosition(entity);
        double x = position.xCoord - entity.posX;
        double y = position.yCoord - entity.posY;
        double z = position.zCoord - entity.posZ;
        double height = entity instanceof EntityItem ? 0.5D : entity.height + 0.1D, width = entity instanceof EntityItem ? 0.25D : width2d.get();
        AxisAlignedBB aabb = new AxisAlignedBB(entity.posX - width + x, entity.posY + y, entity.posZ - width + z, entity.posX + width + x, entity.posY + height + y, entity.posZ + width + z);
        positions.add(new Vec3(aabb.minX, aabb.minY, aabb.minZ));
        positions.add(new Vec3(aabb.minX, aabb.minY, aabb.maxZ));
        positions.add(new Vec3(aabb.minX, aabb.maxY, aabb.minZ));
        positions.add(new Vec3(aabb.minX, aabb.maxY, aabb.maxZ));
        positions.add(new Vec3(aabb.maxX, aabb.minY, aabb.minZ));
        positions.add(new Vec3(aabb.maxX, aabb.minY, aabb.maxZ));
        positions.add(new Vec3(aabb.maxX, aabb.maxY, aabb.minZ));
        positions.add(new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ));
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        final String mode = modeValue.get().toLowerCase();

        if(mode.equalsIgnoreCase("shaderoutline") || mode.equalsIgnoreCase("shaderglow")){
            final FramebufferShader shader = mode.equalsIgnoreCase("shaderoutline")
                    ? OutlineShader.OUTLINE_SHADER : mode.equalsIgnoreCase("shaderglow")
                    ? GlowShader.GLOW_SHADER : null;

            if(shader == null) return;

            shader.startDraw(event.getPartialTicks());

            renderNameTags = false;

            try {
                for (final Entity entity : mc.theWorld.loadedEntityList) {
                    if (!EntityUtils.isSelected(entity, false, false))
                        continue;

                    mc.getRenderManager().renderEntityStatic(entity, mc.timer.renderPartialTicks, true);
                }
            }catch (final Exception ex) {
                ClientUtils.getLogger().error("An error occurred while rendering all entities for shader esp", ex);
            }

            renderNameTags = true;

            final float radius = mode.equalsIgnoreCase("shaderoutline")
                    ? shaderOutlineRadius.get() : mode.equalsIgnoreCase("shaderglow")
                    ? shaderGlowRadius.get() : 1F;

            shader.stopDraw(getColor(null), radius, 1F);
        } else if (mode.equalsIgnoreCase("newbox")) {
            GlStateManager.pushMatrix();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            ScaledResolution sr = new ScaledResolution(mc);
            double twoDscale = sr.getScaleFactor() / Math.pow(sr.getScaleFactor(), 2);
            GlStateManager.scale(twoDscale, twoDscale, twoDscale);

            for (Entity entity : mc.theWorld.loadedEntityList){
                if (entity != null && entity != mc.thePlayer && EntityUtils.isSelected(entity, false, false)) {
                    this.updatePositions(entity);
                    int maxLeft = Integer.MAX_VALUE;
                    int maxRight = Integer.MIN_VALUE;
                    int maxBottom = Integer.MIN_VALUE;
                    int maxTop = Integer.MAX_VALUE;

                    boolean canEntityBeSeen = false;
                    for (Vec3 position : positions) {
                        Vec3 screenPosition = RenderUtils.WorldToScreen(position);
                        if (screenPosition != null) {
                            if (screenPosition.zCoord >= 0.0 && screenPosition.zCoord < 1.0) {
                                maxLeft = (int) Math.min(screenPosition.xCoord, maxLeft);
                                maxRight = (int) Math.max(screenPosition.xCoord, maxRight);
                                maxBottom = (int) Math.max(screenPosition.yCoord, maxBottom);
                                maxTop = (int) Math.min(screenPosition.yCoord, maxTop);
                                canEntityBeSeen = true;
                            }
                        }
                    }
                    if (!canEntityBeSeen) {
                        continue;
                    }

                    RenderUtils.drawnewrect(0, 0, 0, 0, 0);
                    if (health.get()) {
                        this.drawHealth((EntityLivingBase) entity, maxLeft, maxTop, maxRight, maxBottom);
                    }
                    if (armr.get()){
                        this.drawArmor((EntityLivingBase) entity, maxLeft, maxTop, maxRight, maxBottom);
                    }
                    if (rect.get()){
                        this.drawnew(entity, maxLeft, maxTop, maxRight, maxBottom);
                    }
                }
            }
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GlStateManager.popMatrix();
        } else if (mode.equalsIgnoreCase("new2d")) {
            GlStateManager.pushMatrix();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            ScaledResolution sr = new ScaledResolution(this.mc);
            double twoDscale = sr.getScaleFactor() / Math.pow(sr.getScaleFactor(), 2);
            GlStateManager.scale(twoDscale, twoDscale, twoDscale);
            for (Entity entity:mc.theWorld.loadedEntityList){
                if (entity != null && entity != mc.thePlayer && EntityUtils.isSelected(entity, false, false)) {
                    this.updatePositions(entity);
                    int maxLeft = Integer.MAX_VALUE;
                    int maxRight = Integer.MIN_VALUE;
                    int maxBottom = Integer.MIN_VALUE;
                    int maxTop = Integer.MAX_VALUE;

                    boolean canEntityBeSeen = false;
                    for (Vec3 position : positions) {
                        Vec3 screenPosition = RenderUtils.WorldToScreen(position);
                        if (screenPosition != null) {
                            if (screenPosition.zCoord >= 0.0 && screenPosition.zCoord < 1.0) {
                                maxLeft = (int) Math.min(screenPosition.xCoord, maxLeft);
                                maxRight = (int) Math.max(screenPosition.xCoord, maxRight);
                                maxBottom = (int) Math.max(screenPosition.yCoord, maxBottom);
                                maxTop = (int) Math.min(screenPosition.yCoord, maxTop);
                                canEntityBeSeen = true;
                            }
                        }
                    }
                    if (!canEntityBeSeen) {
                        continue;
                    }

                    RenderUtils.drawnewrect(0, 0, 0, 0, 0);
                    if (health.get()) {
                        this.drawHealth((EntityLivingBase) entity, maxLeft, maxTop, maxRight, maxBottom);
                    }
                    if (armr.get()) {
                        this.drawArmor((EntityLivingBase) entity, maxLeft, maxTop, maxRight, maxBottom);
                    }
                    if (rect.get()) {
                        this.drawnew(entity, maxLeft, maxTop, maxRight, maxBottom);
                    }
                }
            }
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }

    public Color shadowgetColor(final Entity entity) {
        if(entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

            if(entityLivingBase.hurtTime > 0)
                return Color.RED;

            if(EntityUtils.isFriend(entityLivingBase))
                return Color.BLUE;

            if(colorTeam.get()) {
                final char[] chars = entityLivingBase.getDisplayName().getFormattedText().toCharArray();
                int color = Integer.MAX_VALUE;
                final String colors = "0123456789abcdef";

                for(int i = 0; i < chars.length; i++) {
                    if(chars[i] != '§' || i + 1 >= chars.length)
                        continue;

                    final int index = colors.indexOf(chars[i + 1]);

                    if(index == -1)
                        continue;

                    color = ColorUtils.hexColors[index];
                    break;
                }

                return new Color(color);
            }
        }

        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(shadowRedValue.get(), shadowGreenValue.get(), shadowBlueValue.get());
    }

    public Color getColor(final Entity entity) {
        if(entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

            if(entityLivingBase.hurtTime > 0)
                return Color.RED;

            if(EntityUtils.isFriend(entityLivingBase))
                return Color.BLUE;

            if(colorTeam.get()) {
                final char[] chars = entityLivingBase.getDisplayName().getFormattedText().toCharArray();
                int color = Integer.MAX_VALUE;
                final String colors = "0123456789abcdef";

                for(int i = 0; i < chars.length; i++) {
                    if(chars[i] != '§' || i + 1 >= chars.length)
                        continue;

                    final int index = colors.indexOf(chars[i + 1]);

                    if(index == -1)
                        continue;

                    color = ColorUtils.hexColors[index];
                    break;
                }

                return new Color(color);
            }
        }

        return colorRainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());
    }

    private void  drawnew(Entity e, int left, int top, int right, int bottom) {
        RenderUtils.drawnewrect(0, 0, 0, 0, 0);

        int line = 1;
        int bg = new Color(0,0,0).getRGB();

        if (modeValue.get().equalsIgnoreCase("newbox")) {
            if (outline.get()) {
                //Up
                RenderUtils.drawnewrect(right + line, top + line, left - line, top - 1.0f - line, bg);

                //Down
                RenderUtils.drawnewrect(right + line, bottom + line, left - line, bottom - 1.0f - line, bg);

                //Left
                RenderUtils.drawnewrect(left + 1.0f + line, top, left - line, bottom, bg);

                //Right
                RenderUtils.drawnewrect(right + line, top, right - 1.0f - line, bottom, bg);
            }

            //Up
            RenderUtils.drawnewrect(right, top, left, top - 1.0f, getColor(e).getRGB());

            //Down
            RenderUtils.drawnewrect(right, bottom, left, bottom - 1.0f, getColor(e).getRGB());

            //Left
            RenderUtils.drawnewrect(left + 1.0f, top, left, bottom, getColor(e).getRGB());

            //Right
            RenderUtils.drawnewrect(right, top, right - 1.0f, bottom, getColor(e).getRGB());

        }
        if (modeValue.get().equalsIgnoreCase("new2d")) {
            if (this.outline.get()) {
                //Left - Up
                RenderUtils.drawnewrect(left + 1.0f + line, top - line, left - line, top + (bottom - top) / 3F + line, bg);
                RenderUtils.drawnewrect(left + (right - left) / 3F + line, top + line, left, top - 1.0f - line, bg);

                //Right - Up
                RenderUtils.drawnewrect(right + line, top - line, right - 1.0f - line, top + (bottom - top) / 3F + line, bg);
                RenderUtils.drawnewrect(right, top + line, right - (right - left) / 3F - line, top - 1.0f - line, bg);

                //Left - Down
                RenderUtils.drawnewrect(left + 1.0f + line, bottom - 1.0f - line, left - line, bottom - 1.0f - (bottom - top) / 3F - line, bg);
                RenderUtils.drawnewrect(left + (right - left) / 3F + line, bottom + line, left - line, bottom - 1.0f - line, bg);

                //Right - Down
                RenderUtils.drawnewrect(right + line, bottom - 1.0f + line, right - 1.0f - line, bottom - 1.0f - (bottom - top) / 3F - line, bg);
                RenderUtils.drawnewrect(right + line, bottom + line, right - (right - left) / 3F - line, bottom - 1.0f - line, bg);
            }

            //Left - Up
            RenderUtils.drawnewrect(left + 1.0f, top, left, top + (bottom - top) / 3F, getColor(e).getRGB());
            RenderUtils.drawnewrect(left + (right - left) / 3F, top, left, top - 1.0f, getColor(e).getRGB());

            //Right - Up
            RenderUtils.drawnewrect(right, top, right - 1.0f, top + (bottom - top) / 3F, getColor(e).getRGB());
            RenderUtils.drawnewrect(right, top, right - (right - left) / 3F, top - 1.0f, getColor(e).getRGB());

            //Left - Down
            RenderUtils.drawnewrect(left + 1.0f, bottom - 1.0f, left, bottom - 1.0f - (bottom - top) / 3F, getColor(e).getRGB());
            RenderUtils.drawnewrect(left + (right - left) / 3F, bottom, left, bottom - 1.0f, getColor(e).getRGB());

            //Right - Down
            RenderUtils.drawnewrect(right, bottom - 1.0f, right - 1.0f, bottom - 1.0f - (bottom - top) / 3F, getColor(e).getRGB());
            RenderUtils.drawnewrect(right, bottom, right - (right - left) / 3F, bottom - 1.0f, getColor(e).getRGB());
        }
    }

    private void drawArmor(EntityLivingBase entityLivingBase, float left, float top, float right, float bottom) {
        float height = (bottom + 1) - top;
        float currentArmor = entityLivingBase.getTotalArmorValue();
        float armorPercent = currentArmor / 20;

        float MOVE = 2F;
        int line = 1;

        if (mc.thePlayer.getDistanceToEntity(entityLivingBase) > 16)
            return;
        RenderUtils.drawnewrect(right + 2.0f + 1f + MOVE, top - 2f, right + 1.0f - 1f + MOVE, bottom + 1f, new Color(25,25,25,150).getRGB());

        RenderUtils.drawnewrect(right + 3.0f + MOVE, top + height * (1.0F - armorPercent) - 1F, right + 1.0f + MOVE, bottom, new Color(125,155,205).getRGB());

        //Right
        RenderUtils.drawnewrect(right + 3.0f + MOVE + line, bottom + 1f, right + 3.0f + MOVE, top - 2f, new Color(0,0,0,255).getRGB());
        //Left
        RenderUtils.drawnewrect(right + 1.0f + MOVE, bottom + 1f, right + 1.0f + MOVE - line, top - 2f, new Color(0,0,0,255).getRGB());
        //Up
        RenderUtils.drawnewrect(right + 1.0f + MOVE, top - 1f, right + 3.0f + MOVE, top - 2f, new Color(0,0,0,255).getRGB());
        //Down
        RenderUtils.drawnewrect(right + 1.0f + MOVE, bottom + 1.0f, right + 3.0f + MOVE, bottom, new Color(0,0,0,255).getRGB());

        for (int i = 0; i < 4; i++) {
            double h = (bottom - top) / 4;

            ItemStack itemStack = entityLivingBase.getEquipmentInSlot(i + 1);
            if (itemStack != null) {
                RenderUtils.renderItemStack(itemStack, (int)(right + 6.0f + MOVE), (int)(bottom - 2f - (i + 1) * h));
                GlStateManager.pushMatrix();
                float scale = 1F;
                GlStateManager.scale(scale, scale, scale);
                mc.fontRendererObj.drawStringWithShadow(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage()), (right + 6.0f + MOVE + (16 - mc.fontRendererObj.getStringWidth(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage())) * scale) / 2) / scale, ((int)(bottom - 2f - (i + 1) * h) + 16) / scale, -1);
                GlStateManager.popMatrix();
            }
            //Gui.drawRect(right + 3.0f + MOVE, top - 1f + i * h, right + 1.0f + MOVE, top - 2f + i * h, new Color(0,0,0,255).getRGB());
        }
    }

    private void drawHealth(EntityLivingBase entityLivingBase, float left, float top, float right, float bottom) {
        float height = (bottom + 1) - top;
        float currentHealth = entityLivingBase.getHealth();
        float maxHealth = entityLivingBase.getMaxHealth();
        float healthPercent = currentHealth / maxHealth;
        float MOVE = 2F;
        int line = 1;
        String healthStr = "§f" + currentHealth + "§c❤";
        this.mc.fontRendererObj.drawStringWithShadow(healthStr, left - 3.0f - MOVE - mc.fontRendererObj.getStringWidth(healthStr), top + height * (1.0f - healthPercent) - 1f, -1);


        RenderUtils.drawnewrect(left - 3.0f - MOVE, bottom, left - 1.0f - MOVE, top - 1f, new Color(25,25,25,150).getRGB());

        RenderUtils.drawnewrect(left - 3.0f - MOVE, bottom, left - 1.0f - MOVE, top + height * (1.0f - healthPercent) - 1F, getHealthColor(entityLivingBase.getHealth(), entityLivingBase.getMaxHealth()).getRGB());
        //Right
        RenderUtils.drawnewrect(left - 3.0f - MOVE, bottom + 1f, left - 3.0f - MOVE - line, top - 2f, new Color(0,0,0,255).getRGB());
        //Left
        RenderUtils.drawnewrect(left - 1.0f - MOVE + line, bottom + 1f, left - 1.0f - MOVE, top - 2f, new Color(0,0,0,255).getRGB());
        //Up
        RenderUtils.drawnewrect(left - 3.0f - MOVE, top - 1f, left - 1.0f - MOVE, top - 2f, new Color(0,0,0,255).getRGB());
        //Down
        RenderUtils.drawnewrect(left - 3.0f - MOVE, bottom + 1f, left - 1.0f - MOVE, bottom, new Color(0,0,0,255).getRGB());

        /*if (mc.thePlayer.getDistanceToEntity(entityLivingBase) <= 24)
            for (int i = 1; i < 8; i++) {
            	double h = (bottom - top) / 8;
            	Gui.drawRect(left - 3.0f - MOVE, top - 1f + i * h, left - 1.0f - MOVE, top - 2f + i * h, new Color(0,0,0,255).getRGB());
            }*/
    }
}
