// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import vip.Resolute.util.render.Colors;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.util.render.RenderUtils;
import vip.Resolute.modules.impl.exploit.HackerDetector;
import vip.Resolute.util.font.FontUtil;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.impl.EventRenderNametag;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.util.ArrayList;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.awt.Color;
import net.minecraft.util.Vec3;
import java.util.List;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class ESP extends Module
{
    public static ModeSetting espMode;
    public static ColorSetting wireColor;
    public static NumberSetting wireWidth;
    public BooleanSetting boxProp;
    public ModeSetting boxMode;
    public ColorSetting colorProp;
    public NumberSetting boxWidth;
    public BooleanSetting armorProp;
    public BooleanSetting healthProp;
    public BooleanSetting itemProp;
    public BooleanSetting nametagProp;
    private final DecimalFormat decimalFormat;
    private static final FloatBuffer modelView;
    private static final FloatBuffer projection;
    private static final IntBuffer viewport;
    private final List<Vec3> positions;
    public static boolean enabled;
    
    public ESP() {
        super("ESP", 0, "", Category.RENDER);
        this.boxProp = new BooleanSetting("Box", true, () -> ESP.espMode.is("Multi"));
        this.boxMode = new ModeSetting("2D Mode", "Corner", () -> this.boxProp.isEnabled() && this.boxProp.isAvailable(), new String[] { "Corner", "Box" });
        this.colorProp = new ColorSetting("Color", new Color(11010027));
        this.boxWidth = new NumberSetting("Box Width", 0.5, () -> this.boxProp.isEnabled() && this.boxProp.isAvailable(), 0.1, 1.0, 0.1);
        this.armorProp = new BooleanSetting("Armor", true, () -> ESP.espMode.is("Multi"));
        this.healthProp = new BooleanSetting("Health", true, () -> ESP.espMode.is("Multi"));
        this.itemProp = new BooleanSetting("Item", true, () -> ESP.espMode.is("Multi"));
        this.nametagProp = new BooleanSetting("Nametag", true, () -> ESP.espMode.is("Multi"));
        this.decimalFormat = new DecimalFormat("0.0#", new DecimalFormatSymbols(Locale.ENGLISH));
        this.positions = new ArrayList<Vec3>();
        this.addSettings(ESP.espMode, ESP.wireColor, ESP.wireWidth, this.boxProp, this.boxMode, this.colorProp, this.boxWidth, this.armorProp, this.healthProp, this.itemProp, this.nametagProp);
    }
    
    @Override
    public void onEnable() {
        ESP.enabled = true;
    }
    
    @Override
    public void onDisable() {
        ESP.enabled = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(ESP.espMode.getMode());
        if (e instanceof EventRenderNametag && ESP.espMode.is("Multi") && this.nametagProp.isEnabled()) {
            e.setCancelled(true);
        }
        if (e instanceof EventRender3D && ESP.espMode.is("Multi")) {
            if (this.boxMode.is("Box")) {
                for (final Entity entity : ESP.mc.theWorld.loadedEntityList) {
                    if (!(entity instanceof EntityItem) && !isValid(entity)) {
                        continue;
                    }
                    updateView();
                }
            }
            if (this.boxMode.is("Corner")) {
                for (final Entity entity : ESP.mc.theWorld.loadedEntityList) {
                    if (!(entity instanceof EntityItem) && !isValid(entity)) {
                        continue;
                    }
                    updateView();
                }
            }
        }
        if (e instanceof EventRender2D && ESP.espMode.is("Multi")) {
            final ScaledResolution sr = new ScaledResolution(ESP.mc);
            GlStateManager.pushMatrix();
            GL11.glDisable(2929);
            final double twoScale = sr.getScaleFactor() / Math.pow(sr.getScaleFactor(), 2.0);
            GlStateManager.scale(twoScale, twoScale, twoScale);
            for (final Entity entity2 : ESP.mc.theWorld.loadedEntityList) {
                if (!isValid(entity2)) {
                    continue;
                }
                this.updatePositions(entity2);
                int maxLeft = Integer.MAX_VALUE;
                int maxRight = Integer.MIN_VALUE;
                int maxBottom = Integer.MIN_VALUE;
                int maxTop = Integer.MAX_VALUE;
                final Iterator<Vec3> iterator2 = this.positions.iterator();
                boolean canEntityBeSeen = false;
                while (iterator2.hasNext()) {
                    final Vec3 screenPosition = WorldToScreen(iterator2.next());
                    if (screenPosition != null && screenPosition.zCoord >= 0.0) {
                        if (screenPosition.zCoord >= 1.0) {
                            continue;
                        }
                        maxLeft = (int)Math.min(screenPosition.xCoord, maxLeft);
                        maxRight = (int)Math.max(screenPosition.xCoord, maxRight);
                        maxBottom = (int)Math.max(screenPosition.yCoord, maxBottom);
                        maxTop = (int)Math.min(screenPosition.yCoord, maxTop);
                        canEntityBeSeen = true;
                    }
                }
                if (!canEntityBeSeen) {
                    continue;
                }
                Gui.drawRect(0.0, 0.0, 0.0, 0.0, 0);
                if (this.healthProp.isEnabled()) {
                    this.drawHealth((EntityLivingBase)entity2, (float)maxLeft, (float)maxTop, (float)maxRight, (float)maxBottom);
                }
                if (this.armorProp.isEnabled()) {
                    this.drawArmor((EntityLivingBase)entity2, (float)maxLeft, (float)maxTop, (float)maxRight, (float)maxBottom);
                }
                if (this.boxProp.isEnabled()) {
                    this.drawBox(entity2, maxLeft, maxTop, maxRight, maxBottom);
                }
                if (((EntityPlayer)entity2).getCurrentEquippedItem() != null && this.itemProp.isEnabled()) {
                    this.drawItem(entity2, maxLeft, maxTop, maxRight, maxBottom);
                }
                if (!this.nametagProp.isEnabled()) {
                    continue;
                }
                this.drawName(entity2, maxLeft, maxTop, maxRight, maxBottom);
            }
            GL11.glEnable(2929);
            GlStateManager.popMatrix();
        }
    }
    
    private void drawName(final Entity e, final int left, final int top, final int right, final int bottom) {
        final EntityPlayer ent = (EntityPlayer)e;
        final String renderName = getPing(ent) + "ms " + ent.getName();
        final MinecraftFontRenderer font = FontUtil.tahomaVerySmall;
        final float meme2 = (float)((right - left) / 2.0 - font.getStringWidth(renderName));
        final float halfWidth = font.getHeight() / 2.0f;
        final float xDif = (float)(right - left);
        final float middle = left + xDif / 2.0f;
        final float textHeight = (float)font.getHeight();
        final float renderY = top - textHeight - 2.0f;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        if ((HackerDetector.enabled && HackerDetector.isHacker(ent)) || isTeamMate(ent)) {
            RenderUtils.drawRect(middle - halfWidth * 2.0f - 2.0f, renderY - 10.0f, middle + halfWidth * 2.0f + 2.0f, renderY + textHeight - 0.5f, new Color(0, 0, 0).getRGB());
            RenderUtils.drawRect(middle - halfWidth * 2.0f - 1.0f, renderY - 9.0f, middle + halfWidth * 2.0f + 0.5f, renderY + textHeight - 1.5f, this.getColor(ent));
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        font.drawStringWithShadow(renderName, (left + meme2) / 2.0f, (top - font.getHeight() / 1.5f * 2.0f) / 2.0f - 4.0f, new Color(0, 0, 0, 210).getRGB());
        GlStateManager.popMatrix();
    }
    
    private void drawItem(final Entity e, final int left, final int top, final int right, final int bottom) {
        final EntityPlayer ent = (EntityPlayer)e;
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        final ItemStack stack = ent.getCurrentEquippedItem();
        final String customName = this.nametagProp.isEnabled() ? ent.getCurrentEquippedItem().getDisplayName() : ent.getCurrentEquippedItem().getItem().getItemStackDisplayName(stack);
        final float meme5 = (float)((right - left) / 2.0 - FontUtil.tahomaVerySmall.getStringWidth(customName));
        FontUtil.tahomaVerySmall.drawStringWithShadow(customName, (left + meme5) / 2.0f, (bottom + FontUtil.tahomaVerySmall.getHeight() / 2.0f * 2.0f) / 2.0f + 1.0f, -1);
        GlStateManager.popMatrix();
        if (stack != null) {
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            ESP.mc.getRenderItem().renderItemIntoGUI(stack, (int)(left + meme5) + 29, (int)(bottom + FontUtil.tahomaVerySmall.getHeight() / 2.0f * 2.0f) + 15);
            ESP.mc.getRenderItem().renderItemOverlays(ESP.mc.fontRendererObj, stack, (int)(left + meme5) + 29, (int)(bottom + FontUtil.tahomaVerySmall.getHeight() / 2.0f * 2.0f) + 15);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }
    
    public static int getPing(final EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return 0;
        }
        final NetworkPlayerInfo networkPlayerInfo = ESP.mc.getNetHandler().getPlayerInfo(entityPlayer.getUniqueID());
        return (networkPlayerInfo == null) ? 0 : networkPlayerInfo.getResponseTime();
    }
    
    private void drawBox(final Entity e, final int left, final int top, final int right, final int bottom) {
        final int line = 1;
        final int bg = new Color(0, 0, 0).getRGB();
        if (this.boxMode.is("Corner")) {
            final int p_drawRect_0_ = left + (right - left) / 3 + line;
            final int p_drawRect_2_ = right - (right - left) / 3 - line;
            final int p_drawRect_3_ = top + (bottom - top) / 3 + line;
            final int p_drawRect_3_2 = bottom - 1 - (bottom - top) / 3 - line;
            Gui.drawRect(left + 1 + line, top - line, left - line, p_drawRect_3_, bg);
            Gui.drawRect(p_drawRect_0_, top + line, left, top - 1 - line, bg);
            Gui.drawRect(right + line, top - line, right - 1 - line, p_drawRect_3_, bg);
            Gui.drawRect(right, top + line, p_drawRect_2_, top - 1 - line, bg);
            Gui.drawRect(left + 1 + line, bottom - 1 - line, left - line, p_drawRect_3_2, bg);
            Gui.drawRect(p_drawRect_0_, bottom + line, left - line, bottom - 1 - line, bg);
            Gui.drawRect(right + line, bottom - 1 + line, right - 1 - line, p_drawRect_3_2, bg);
            Gui.drawRect(right + line, bottom + line, p_drawRect_2_, bottom - 1 - line, bg);
            Gui.drawRect(left + 1, top, left, top + (bottom - top) / 3.0f, this.getColor(e).getRGB());
            Gui.drawRect(left + (right - left) / 3.0f, top, left, top - 1, this.getColor(e).getRGB());
            Gui.drawRect(right, top, right - 1, top + (bottom - top) / 3.0f, this.getColor(e).getRGB());
            Gui.drawRect(right, top, right - (right - left) / 3.0f, top - 1, this.getColor(e).getRGB());
            Gui.drawRect(left + 1, bottom - 1, left, bottom - 1 - (bottom - top) / 3.0f, this.getColor(e).getRGB());
            Gui.drawRect(left + (right - left) / 3.0f, bottom, left, bottom - 1, this.getColor(e).getRGB());
            Gui.drawRect(right, bottom - 1, right - 1, bottom - 1 - (bottom - top) / 3.0f, this.getColor(e).getRGB());
            Gui.drawRect(right, bottom, right - (right - left) / 3.0f, bottom - 1, this.getColor(e).getRGB());
        }
        else if (this.boxMode.is("Box")) {
            Gui.drawRect(right + line, top + line, left - line, top - 1 - line, bg);
            Gui.drawRect(right + line, bottom + line, left - line, bottom - 1 - line, bg);
            Gui.drawRect(left + 1 + line, top, left - line, bottom, bg);
            Gui.drawRect(right + line, top, right - 1 - line, bottom, bg);
            Gui.drawRect(right, top, left, top - 1, this.getColor(e).getRGB());
            Gui.drawRect(right, bottom, left, bottom - 1, this.getColor(e).getRGB());
            Gui.drawRect(left + 1, top, left, bottom, this.getColor(e).getRGB());
            Gui.drawRect(right, top, right - 1, bottom, this.getColor(e).getRGB());
        }
    }
    
    private void drawArmor(final EntityLivingBase entityLivingBase, final float left, final float top, final float right, final float bottom) {
        final float height = bottom + 1.0f - top;
        final float currentArmor = (float)entityLivingBase.getTotalArmorValue();
        final float armorPercent = currentArmor / 20.0f;
        final float MOVE = 2.0f;
        final float line = 1.0f;
        if (ESP.mc.thePlayer.getDistanceToEntity(entityLivingBase) > 16.0f) {
            return;
        }
        for (int i = 0; i < 4; ++i) {
            final double h = (bottom - top) / 4.0f;
            final ItemStack itemStack = entityLivingBase.getEquipmentInSlot(i + 1);
            final double difference = top - bottom + 0.5;
            if (itemStack != null) {
                RenderUtils.drawESPRect(right + 2.0f + 1.0f + MOVE, top - 2.0f, right + 1.0f - 1.0f + MOVE, bottom + 1.0f, new Color(25, 25, 25, 150).getRGB());
                RenderUtils.drawESPRect(right + 3.0f + MOVE, top + height * (1.0f - armorPercent) - 1.0f, right + 1.0f + MOVE, bottom, new Color(78, 206, 229).getRGB());
                RenderUtils.drawESPRect(right + 3.0f + MOVE + line, bottom + 1.0f, right + 3.0f + MOVE, top - 2.0f, new Color(0, 0, 0, 255).getRGB());
                RenderUtils.drawESPRect(right + 1.0f + MOVE, bottom + 1.0f, right + 1.0f + MOVE - line, top - 2.0f, new Color(0, 0, 0, 255).getRGB());
                RenderUtils.drawESPRect(right + 1.0f + MOVE, top - 1.0f, right + 3.0f + MOVE, top - 2.0f, new Color(0, 0, 0, 255).getRGB());
                RenderUtils.drawESPRect(right + 1.0f + MOVE, bottom + 1.0f, right + 3.0f + MOVE, bottom, new Color(0, 0, 0, 255).getRGB());
                RenderUtils.renderItemStack(itemStack, (int)(right + 6.0f + MOVE), (int)(bottom + 30.0f - (i + 1) * h));
                final float scale = 1.0f;
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, scale);
                ESP.mc.fontRendererObj.drawStringWithShadow(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage()), (right + 6.0f + MOVE + (16.0f - ESP.mc.fontRendererObj.getStringWidth(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage())) * scale) / 2.0f) / scale, ((int)(bottom + 30.0f - (i + 1) * h) + 16) / scale, -1);
                GlStateManager.popMatrix();
                if (-difference > 50.0) {
                    for (int j = 1; j < 4; ++j) {
                        final double dThing = difference / 4.0 * j;
                        RenderUtils.rectangle(right + 2.0f, bottom - 0.5 + dThing, right + 6.0, bottom - 0.5 + dThing - 1.0, Colors.getColor(0));
                    }
                }
            }
        }
    }
    
    private void drawHealth(final EntityLivingBase entityLivingBase, final float left, final float top, final float right, final float bottom) {
        final float height = bottom + 1.0f - top;
        final float currentHealth = entityLivingBase.getHealth();
        final float maxHealth = entityLivingBase.getMaxHealth();
        final float healthPercent = currentHealth / maxHealth;
        final float MOVE = 2.0f;
        final float line = 1.0f;
        final String healthStr = "§f" + this.decimalFormat.format(currentHealth) + "§c\u2764";
        final float bottom2 = top + height * (1.0f - healthPercent) - 1.0f;
        final float health = entityLivingBase.getHealth();
        final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
        final float progress = health / entityLivingBase.getMaxHealth();
        final Color customColor = (health >= 0.0f) ? Colors.blendColors(fractions, colors, progress).brighter() : Color.RED;
        ESP.mc.fontRendererObj.drawStringWithShadow(healthStr, left - 3.0f - MOVE - ESP.mc.fontRendererObj.getStringWidth(healthStr), bottom2, -1);
        RenderUtils.drawESPRect(left - 3.0f - MOVE, bottom, left - 1.0f - MOVE, top - 1.0f, new Color(25, 25, 25, 150).getRGB());
        RenderUtils.drawESPRect(left - 3.0f - MOVE, bottom, left - 1.0f - MOVE, bottom2, customColor.getRGB());
        RenderUtils.drawESPRect(left - 3.0f - MOVE, bottom + 1.0f, left - 3.0f - MOVE - line, top - 2.0f, new Color(0, 0, 0, 255).getRGB());
        RenderUtils.drawESPRect(left - 1.0f - MOVE + line, bottom + 1.0f, left - 1.0f - MOVE, top - 2.0f, new Color(0, 0, 0, 255).getRGB());
        RenderUtils.drawESPRect(left - 3.0f - MOVE, top - 1.0f, left - 1.0f - MOVE, top - 2.0f, new Color(0, 0, 0, 255).getRGB());
        RenderUtils.drawESPRect(left - 3.0f - MOVE, bottom + 1.0f, left - 1.0f - MOVE, bottom, new Color(0, 0, 0, 255).getRGB());
        final double difference = top - bottom + 0.5;
        if (-difference > 50.0) {
            for (int j = 1; j < 10; ++j) {
                final double dThing = difference / 10.0 * j;
                RenderUtils.rectangle(left - 5.5, bottom - 0.5 + dThing, left - 2.5, bottom - 0.5 + dThing - 1.0, Colors.getColor(0));
            }
        }
    }
    
    public Color getColor(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            return this.colorProp.getValue();
        }
        return new Color(255, 255, 255);
    }
    
    private static Vec3 WorldToScreen(final Vec3 position) {
        final FloatBuffer screenPositions = BufferUtils.createFloatBuffer(3);
        final boolean result = GLU.gluProject((float)position.xCoord, (float)position.yCoord, (float)position.zCoord, ESP.modelView, ESP.projection, ESP.viewport, screenPositions);
        if (result) {
            return new Vec3(screenPositions.get(0), Display.getHeight() - screenPositions.get(1), screenPositions.get(2));
        }
        return null;
    }
    
    public void updatePositions(final Entity entity) {
        this.positions.clear();
        final Vec3 position = getEntityRenderPosition(entity);
        final double x = position.xCoord - entity.posX;
        final double y = position.yCoord - entity.posY;
        final double z = position.zCoord - entity.posZ;
        final double height = (entity instanceof EntityItem) ? 0.5 : (entity.height + 0.1);
        final double width = (entity instanceof EntityItem) ? 0.25 : this.boxWidth.getValue();
        final AxisAlignedBB aabb = new AxisAlignedBB(entity.posX - width + x, entity.posY + y, entity.posZ - width + z, entity.posX + width + x, entity.posY + height + y, entity.posZ + width + z);
        this.positions.add(new Vec3(aabb.minX, aabb.minY, aabb.minZ));
        this.positions.add(new Vec3(aabb.minX, aabb.minY, aabb.maxZ));
        this.positions.add(new Vec3(aabb.minX, aabb.maxY, aabb.minZ));
        this.positions.add(new Vec3(aabb.minX, aabb.maxY, aabb.maxZ));
        this.positions.add(new Vec3(aabb.maxX, aabb.minY, aabb.minZ));
        this.positions.add(new Vec3(aabb.maxX, aabb.minY, aabb.maxZ));
        this.positions.add(new Vec3(aabb.maxX, aabb.maxY, aabb.minZ));
        this.positions.add(new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ));
    }
    
    private static Vec3 getEntityRenderPosition(final Entity entity) {
        return new Vec3(getEntityRenderX(entity), getEntityRenderY(entity), getEntityRenderZ(entity));
    }
    
    private static double getEntityRenderX(final Entity entity) {
        return entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosX;
    }
    
    private static double getEntityRenderY(final Entity entity) {
        return entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosY;
    }
    
    private static double getEntityRenderZ(final Entity entity) {
        return entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosZ;
    }
    
    private int getColor(final EntityLivingBase ent) {
        if (ent.getName().equals(ESP.mc.thePlayer.getName())) {
            return new Color(50, 255, 50).getRGB();
        }
        if (HackerDetector.enabled && HackerDetector.isHacker(ent)) {
            return new Color(255, 0, 0).getRGB();
        }
        if (isTeamMate((EntityPlayer)ent)) {
            return new Color(0, 200, 0).getRGB();
        }
        return new Color(200, 0, 0, 50).getRGB();
    }
    
    public static boolean isValid(final Entity entity) {
        return (entity != ESP.mc.thePlayer || ESP.mc.gameSettings.thirdPersonView != 0) && !entity.isInvisible() && entity instanceof EntityPlayer;
    }
    
    private static void updateView() {
        GL11.glGetFloat(2982, ESP.modelView);
        GL11.glGetFloat(2983, ESP.projection);
        GL11.glGetInteger(2978, ESP.viewport);
    }
    
    public static boolean isTeamMate(final EntityPlayer entity) {
        final String entName = entity.getDisplayName().getFormattedText();
        final String playerName = ESP.mc.thePlayer.getDisplayName().getFormattedText();
        return entName.length() >= 2 && playerName.length() >= 2 && entName.startsWith("§") && playerName.startsWith("§") && entName.charAt(1) == playerName.charAt(1);
    }
    
    static {
        ESP.espMode = new ModeSetting("Mode", "Multi", new String[] { "Multi", "Wireframe" });
        ESP.wireColor = new ColorSetting("Wire Color", new Color(255, 255, 255), () -> ESP.espMode.is("Wireframe"));
        ESP.wireWidth = new NumberSetting("Wire Width", 2.0, () -> ESP.espMode.is("Wireframe"), 0.5, 5.0, 0.1);
        modelView = BufferUtils.createFloatBuffer(16);
        projection = BufferUtils.createFloatBuffer(16);
        viewport = BufferUtils.createIntBuffer(16);
        ESP.enabled = false;
    }
}
