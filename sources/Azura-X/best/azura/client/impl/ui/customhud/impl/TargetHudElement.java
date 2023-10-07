package best.azura.client.impl.ui.customhud.impl;

import best.azura.client.api.ui.customhud.Element;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventFastTick;
import best.azura.client.impl.module.impl.combat.KillAura;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.module.impl.render.HUD;
import best.azura.client.impl.ui.customhud.GuiEditHUD;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.render.DrawUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class TargetHudElement extends Element {

    private float entityHealth, entityArmor, fade;
    private EntityLivingBase target;
    private double maxHealth;
    private String[] strings;
    private AbstractClientPlayer abstractClientPlayer;
    private int index = 0;
    private final ModeValue design = new ModeValue("Design", "Design for the target hud", "Azura X", "Azura X", "Error", "Old", "LEGO", "Roblox");

    public TargetHudElement() {
        super("Target HUD", 3, 3, 300, 120);
    }

    @Override
    public void onAdd() {
        Client.INSTANCE.getEventBus().register(this);
    }

    @Override
    public void onRemove() {
        Client.INSTANCE.getEventBus().unregister(this);
    }

    @Override
    public List<Value<?>> getValues() {
        return createValuesArray(design);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public final Listener<EventFastTick> eventFastTickListener = e -> {
        if (target != null) fade = Math.min(1, fade + 0.1f);
        else fade = Math.max(0, fade - 0.1f);
        if (target == null) return;
        maxHealth = target.getMaxHealth();
        entityHealth += (target.getHealth() - entityHealth) * 0.2f;
        entityArmor += (target.getTotalArmorValue() - entityArmor) * 0.2f;
        entityHealth = MathHelper.clamp_float(entityHealth, 0, target.getMaxHealth());
        if (Double.isNaN(entityHealth) || Double.isInfinite(entityHealth)) entityHealth = 0;
        if (Double.isNaN(entityArmor) || Double.isInfinite(entityArmor)) entityArmor = 0;
    };


    @Override
    public void render() {
        fitInScreen(mc.displayWidth, mc.displayHeight);
        switch (design.getObject()) {
            case "Azura X": {
                setWidth(300);
                setHeight(120);
                final KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
                final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
                if (killAura == null || hud == null) return;
                if (killAura.targets.size() <= index || killAura.targets.isEmpty() || !(killAura.targets.get(index) instanceof EntityLivingBase))
                    target = mc.thePlayer;
                else target = (EntityLivingBase) killAura.targets.get(index);
                if (index > 0 && killAura.targets.size() <= 1) {
                    target = null;
                    return;
                }
                if (target == mc.thePlayer && !(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditHUD))
                    target = null;
                if (target instanceof AbstractClientPlayer) abstractClientPlayer = (AbstractClientPlayer) target;
                else if (fade < 0.1 || target != null) abstractClientPlayer = null;
                if (fade == 0.0) return;
                final FontRenderer fr = hud.useClientFont.getObject() ? Fonts.INSTANCE.hudFont : mc.fontRendererObj;
                if (Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurTargetHud.getObject()) {
                    final float lastRadius = BlurModule.blurShader.getRadius();
                    BlurModule.blurTasks.add(() -> {
                        RenderUtil.INSTANCE.scaleFix(1.0);
                        BlurModule.blurShader.setRadius(BlurModule.radius.getObject() * fade);
                        RenderUtil.INSTANCE.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 10, new Color(24, 24, 24, (int) (200 * fade)));
                        RenderUtil.INSTANCE.drawHollowRoundedRect(getX(), getY(), getWidth(), getHeight(), 10, new Color(24, 24, 24, (int) (200 * fade)));
                        BlurModule.blurShader.setRadius(lastRadius);
                        RenderUtil.INSTANCE.invertScaleFix(1.0);
                    });
                } else {
                    RenderUtil.INSTANCE.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 10, new Color(24, 24, 24, (int) (200 * fade)));
                    RenderUtil.INSTANCE.drawHollowRoundedRect(getX(), getY(), getWidth(), getHeight(), 10, new Color(24, 24, 24, (int) (200 * fade)));
                }
                RenderUtil.INSTANCE.drawRect(getX() + 3, getY() + getHeight() - 6, getX() + getWidth() - 4, getY() + getHeight() - 4,
                        ColorUtil.withAlpha(ColorUtil.getColorFromHealth(maxHealth, entityHealth), (int) (fade * 255)).darker().darker().darker());
                RenderUtil.INSTANCE.drawRect(getX() + 3, getY() + getHeight() - 6, getX() + 3 + (getWidth() - 7) *
                        (entityHealth / maxHealth), getY() + getHeight() - 4, ColorUtil.withAlpha(ColorUtil.getColorFromHealth(maxHealth, entityHealth), (int) (fade * 255)));

                RenderUtil.INSTANCE.drawRect(getX() + 3, getY() + getHeight() - 10, getX() + getWidth() - 4, getY() + getHeight() - 8,
                        ColorUtil.withAlpha(new Color(20, 150, 255), (int) (fade * 255)).darker().darker().darker());
                RenderUtil.INSTANCE.drawRect(getX() + 3, getY() + getHeight() - 10, getX() + 3 + (getWidth() - 7) * (entityArmor / 20),
                        getY() + getHeight() - 8, ColorUtil.withAlpha(new Color(20, 150, 255), (int) (fade * 255)));

                if (abstractClientPlayer != null) {
                    boolean flag1 = abstractClientPlayer.isWearing(EnumPlayerModelParts.CAPE) && (abstractClientPlayer.getGameProfile().getName().equals("Dinnerbone") ||
                            abstractClientPlayer.getGameProfile().getName().equals("Grumm"));
                    this.mc.getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
                    int l2 = 8 + (flag1 ? 8 : 0);
                    int i3 = 8 * (flag1 ? -1 : 1);
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    GlStateManager.resetColor();
                    GlStateManager.color(1, 1, 1, fade);
                    GlStateManager.translate(getX() + 12, getY() + 20, 0);
                    Gui.drawScaledCustomSizeModalRect(0, 0, 8.0F, (float) l2, 8, i3, 76, 76, 64.0F, 64.0F);
                    GlStateManager.translate(-(getX() + 12), -(getY() + 20), 0);
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }

                if (target != null) {
                    strings = new String[]{target.getName(), String.format("%.2f", mc.thePlayer.getDistanceToEntity(target)).replace(",", ".") + "m"};

                }

                int offset = 0;
                for (final String string : strings) {
                    if (!hud.useClientFont.getObject()) GlStateManager.scale(2, 2, 1);
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    fr.drawStringWithShadow(string, (getX() + 96) / (hud.useClientFont.getObject() ? 1 : 2),
                            (getY() + 16) / (hud.useClientFont.getObject() ? 1 : 2) + offset * fr.FONT_HEIGHT, ColorUtil.withAlpha(Color.WHITE, (int) (fade * 255)).getRGB());
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                    if (!hud.useClientFont.getObject()) GlStateManager.scale(0.5, 0.5, 1);
                    offset++;
                }

            }
            break;
            case "Error": {
                setWidth(300);
                setHeight(120);
                final KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
                final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
                if (killAura == null || hud == null) return;
                if (killAura.targets.size() <= index || killAura.targets.isEmpty() || !(killAura.targets.get(index) instanceof EntityLivingBase))
                    target = mc.thePlayer;
                else target = (EntityLivingBase) killAura.targets.get(index);
                if (index > 0 && killAura.targets.size() <= 1) {
                    target = null;
                    return;
                }
                if (target == mc.thePlayer && !(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditHUD))
                    target = null;
                if (target instanceof AbstractClientPlayer) abstractClientPlayer = (AbstractClientPlayer) target;
                else if (fade < 0.1 || target != null) abstractClientPlayer = null;
                if (target == null) break;
                final float health = target.getHealth() + (entityHealth - target.getHealth()) * 0.25F;
                final float armor = target.getTotalArmorValue() + (entityArmor - target.getTotalArmorValue()) * 0.25F;
                GL11.glLineWidth(1.0f);
                DrawUtil.glDrawRect(GL_POLYGON, getX(), getY(), getX() + getWidth(), getY() + getHeight(), new Color(5, 7, 3));
                DrawUtil.glDrawRect(GL_LINE_LOOP, getX(), getY(), getX() + getWidth(), getY() + getHeight(), new Color(55, 57, 53));
                DrawUtil.glDrawRect(GL_LINE_LOOP, getX() + 3, getY() + 3, getX() + getWidth() - 3, getY() + getHeight() - 3, new Color(55, 57, 53));
                DrawUtil.glDrawRect(GL_LINE_LOOP, getX() + 2, getY() + 2, getX() + getWidth() - 2, getY() + getHeight() - 2, new Color(35, 37, 33));
                DrawUtil.glDrawRect(GL_LINE_LOOP, getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, new Color(35, 37, 33));
                final double hStartX = getX() + 80, hStartY = getY() + 35, hEndX = getX() + 80 + (getWidth() - 100) * health / maxHealth, hEndY = getY() + 40,
                        aEndX = getX() + 80 + (getWidth() - 100) * armor / 20;
                DrawUtil.glDrawRect(GL_POLYGON, hStartX, hStartY, getX() + 80 + getWidth() - 100, hEndY, new Color(15, 17, 13));
                DrawUtil.glDrawRect(GL_POLYGON, hStartX, hStartY + 20, getX() + 80 + getWidth() - 100, hEndY + 20, new Color(15, 17, 13));
                DrawUtil.glDrawRect(GL_POLYGON, hStartX, hStartY, hEndX, hEndY, ColorUtil.getColorFromHealth(maxHealth, entityHealth));
                DrawUtil.glDrawRect(GL_POLYGON, hStartX, hStartY + 20, aEndX, hEndY + 20, new Color(20, 150, 255));
                DrawUtil.glDrawRect(GL_LINE_LOOP, hStartX, hStartY, getX() + 80 + getWidth() - 100, hEndY, new Color(35, 37, 33));
                DrawUtil.glDrawRect(GL_LINE_LOOP, hStartX, hStartY + 20, getX() + 80 + getWidth() - 100, hEndY + 20, new Color(35, 37, 33));
                Fonts.INSTANCE.hudFont.drawStringWithShadow(target.getName(), getX() + 80, getY() + 6, -1);
                GlStateManager.resetColor();
                GuiInventory.drawEntityOnScreen((int)getX() + 40, (int)getY() + 100, (int)((target.width / getWidth()) * 20000),
                        MathHelper.wrapAngleTo180_float(target.rotationYaw), -target.rotationPitch, target);
                int offset = 80;
                double scale = 2.0;
                for (int i = 6; i >= -1; i--) {
                    final ItemStack stack = i > 3 ? null : i == -1 ? target.getHeldItem() : target.getCurrentArmor(i);
                    if (stack != null) {
                        GlStateManager.scale(scale, scale, 1);
                        GlStateManager.enableRescaleNormal();
                        GlStateManager.enableBlend();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        RenderHelper.enableGUIStandardItemLighting();
                        final int lastDamage = stack.getItemDamage();
                        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) ((getX() + offset) / scale), (int) ((getY() + 80) / scale));
                        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) ((getX() + offset) / scale), (int) ((getY() + 80) / scale), null);
                        stack.setItemDamage(lastDamage);
                        offset += 32;
                        GlStateManager.scale(1.0 / scale, 1.0 / scale, 1);
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.disableRescaleNormal();
                        GlStateManager.disableBlend();
                    }
                    final double startX = getX() + 80 + (getWidth() - 100) * i / 6.0;
                    DrawUtil.glDrawRect(GL_LINES, startX, hStartY, startX, hEndY, new Color(35, 37, 33));
                    DrawUtil.glDrawRect(GL_LINES, startX, hStartY + 20, startX, hEndY + 20, new Color(35, 37, 33));
                }
            }
            break;
            case "Old": {
                setWidth(210);
                setHeight(80);
                final KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
                final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
                if (killAura == null || hud == null) break;
                if (killAura.targets.size() <= index || killAura.targets.isEmpty() || !(killAura.targets.get(index) instanceof EntityLivingBase))
                    target = mc.thePlayer;
                else target = (EntityLivingBase) killAura.targets.get(index);
                if (index > 0 && killAura.targets.size() <= 1) {
                    target = null;
                    break;
                }
                if (target == mc.thePlayer && !(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditHUD))
                    target = null;
                if (target instanceof AbstractClientPlayer) abstractClientPlayer = (AbstractClientPlayer) target;
                else if (fade < 0.1 || target != null) abstractClientPlayer = null;
                if (target == null) break;
                final float health = target.getHealth() + (entityHealth - target.getHealth()) * 0.35F;
                DrawUtil.glDrawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), new Color(0, 0, 0, 150));
                DrawUtil.glDrawRect(getX(), getY() + getHeight(), getX() + getWidth() * (health / maxHealth), getY() + getHeight() - 4, ColorUtil.getColorFromHealth(maxHealth, health));
                GlStateManager.scale(2, 2, 1);
                mc.fontRendererObj.drawStringWithShadow(target.getName(), (getX() + 6) / 2, (getY() + getHeight() - 24) / 2, -1);
                GlStateManager.scale(0.5, 0.5, 1);
                int offset = 0;
                double scale = 2;
                for (int i = 4; i >= 0; i--) {
                    final ItemStack stack = i > 3 ? target.getHeldItem() : target.getCurrentArmor(i);
                    if (stack != null) {
                        GlStateManager.scale(scale, scale, 1);
                        GlStateManager.enableRescaleNormal();
                        GlStateManager.enableBlend();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        RenderHelper.enableGUIStandardItemLighting();
                        final int lastDamage = stack.getItemDamage();
                        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) (((getX() + 3) + offset) / scale), (int) ((getY() + 3) / scale));
                        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) (((getX() + 3) + offset) / scale), (int) ((getY() + 3) / scale), null);
                        stack.setItemDamage(lastDamage);
                        offset += 32;
                        GlStateManager.scale(1.0 / scale, 1.0 / scale, 1);
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.disableRescaleNormal();
                        GlStateManager.disableBlend();
                    }
                }
            }
            break;
            case "LEGO": {
                setWidth(300);
                setHeight(120);
                final KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
                final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
                if (killAura == null || hud == null) return;
                if (killAura.targets.size() <= index || killAura.targets.isEmpty() || !(killAura.targets.get(index) instanceof EntityLivingBase))
                    target = mc.thePlayer;
                else target = (EntityLivingBase) killAura.targets.get(index);
                if (index > 0 && killAura.targets.size() <= 1) {
                    target = null;
                    return;
                }
                if (target == mc.thePlayer && !(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditHUD))
                    target = null;
                if (target instanceof AbstractClientPlayer) abstractClientPlayer = (AbstractClientPlayer) target;
                else if (fade < 0.1 || target != null) abstractClientPlayer = null;
                if (target == null) break;
                RenderUtil.INSTANCE.drawHollowRoundedRect(getX(), getY(), getWidth(), getHeight(), 15, new Color(250, 250, 250, 150));
                RenderUtil.INSTANCE.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 15, new Color(250, 250, 250));
                GlStateManager.scale(2.0, 2.0, 1);
                mc.fontRendererObj.drawStringWithShadow(target.getName(), (getX() + 60) / 2.0, getY() / 2.0 + 6, 0x000000);
                GlStateManager.scale(0.5, 0.5, 1);
                GlStateManager.resetColor();
                RenderUtil.INSTANCE.color(target.hurtTime == 0 ? -1 : new Color(Math.max(100, (int) (255 * (1.0 - (target.hurtTime / (double)target.maxHurtTime)))), 0, 0).getRGB());
                mc.getTextureManager().bindTexture(new ResourceLocation("custom/lego_head.png"));
                RenderUtil.INSTANCE.drawTexture(getX() + 4, getY() + 32, getX() + 64, getY() + 92);
                RenderUtil.INSTANCE.drawRoundedRect(getX() + 80, getY() + getHeight() - 36, (getWidth() - 85) * (entityHealth / maxHealth), 12, 5, new Color(217, 66, 24));
                if (entityHealth > 5)
                    RenderUtil.INSTANCE.drawRoundedRect(getX() + 110, getY() + getHeight() - 42, 12, 12, 5, new Color(217, 66, 24));
                if (entityHealth > 11)
                    RenderUtil.INSTANCE.drawRoundedRect(getX() + 180, getY() + getHeight() - 42, 12, 12, 5, new Color(217, 66, 24));
                if (entityHealth > 17)
                    RenderUtil.INSTANCE.drawRoundedRect(getX() + 250, getY() + getHeight() - 42, 12, 12, 5, new Color(217, 66, 24));
            }
            break;
            case "Roblox": {
                setWidth(300);
                setHeight(120);
                final KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
                final HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule(HUD.class);
                if (killAura == null || hud == null) return;
                if (killAura.targets.size() <= index || killAura.targets.isEmpty() || !(killAura.targets.get(index) instanceof EntityLivingBase))
                    target = mc.thePlayer;
                else target = (EntityLivingBase) killAura.targets.get(index);
                if (index > 0 && killAura.targets.size() <= 1) {
                    target = null;
                    return;
                }
                if (target == mc.thePlayer && !(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditHUD))
                    target = null;
                if (target instanceof AbstractClientPlayer) abstractClientPlayer = (AbstractClientPlayer) target;
                else if (fade < 0.1 || target != null) abstractClientPlayer = null;
                if (target == null) break;
                RenderUtil.INSTANCE.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 5, new Color(57, 59, 61));
                RenderUtil.INSTANCE.drawHollowRoundedRect(getX(), getY(), getWidth(), getHeight(), 5, new Color(48, 50, 52));
                Fonts.INSTANCE.robloxFontBoldBig.drawString(target.getName(), getX() + 75, getY() + 3, -1);
                final String hpString = String.valueOf((int)(Math.round(entityHealth * 10.) / 10.)),
                        distanceString = String.valueOf((int)(Math.round(mc.thePlayer.getDistanceToEntity(target) * 10.) / 10.));
                final double hpStringWidth = Fonts.INSTANCE.robloxFontBold.getStringWidth(hpString),
                        totalHPStringWidth = hpStringWidth + Fonts.INSTANCE.robloxFont.getStringWidth(" Health"),
                        distanceWidth = Fonts.INSTANCE.robloxFontBold.getStringWidth(distanceString);
                Fonts.INSTANCE.robloxFontBold.drawString(hpString, getX() + 75, getY() + 60, -1);
                Fonts.INSTANCE.robloxFont.drawString(" Health", getX() + 75 + hpStringWidth, getY() + 70, new Color(117, 120, 123).getRGB());
                Fonts.INSTANCE.robloxFontBold.drawString(distanceString, getX() + 85 + totalHPStringWidth, getY() + 60, -1);
                Fonts.INSTANCE.robloxFont.drawString(" Distance", getX() + 85 + totalHPStringWidth + distanceWidth, getY() + 70, new Color(117, 120, 123).getRGB());
                if (target instanceof EntityPlayer) {
                    RenderUtil.INSTANCE.drawRect(getX() + 10, getY() + 30, getX() + 60, getY() + 80, new Color(135, 136, 137).getRGB());
                    final GameProfile gameprofile = abstractClientPlayer.getGameProfile();
                    GlStateManager.resetColor();
                    GlStateManager.color(1, 1, 1, 1);
                    GlStateManager.scale(6.0, 6.0, 1);
                    EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
                    boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
                    this.mc.getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
                    int l2 = 8 + (flag1 ? 8 : 0);
                    int i3 = 8 * (flag1 ? -1 : 1);
                    GlStateManager.translate((getX() + 11) / 6.0, (getY() + 31) / 6.0, 0);
                    Gui.drawScaledCustomSizeModalRect(0, 0, 8.0F, (float)l2, 8, i3, 8, 8, 64.0F, 64.0F);
                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT))
                    {
                        int j3 = 8 + (flag1 ? 8 : 0);
                        int k3 = 8 * (flag1 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(0, 0, 40.0F, (float)j3, 8, k3, 8, 8, 64.0F, 64.0F);
                    }
                    GlStateManager.translate(-(getX() + 11) / 6.0, -(getY() + 31) / 6.0, 0);
                    GlStateManager.scale(1.0 / 6.0, 1.0 / 6.0, 1);
                }
            }
            break;
        }
    }

    @Override
    public Element copy() {
        return new TargetHudElement();
    }

    public void setIndex(int index) {
        this.index = index;
    }
}