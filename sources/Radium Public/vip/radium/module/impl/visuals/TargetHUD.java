package vip.radium.module.impl.visuals;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import vip.radium.event.impl.entity.EntityHealthUpdateEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.overlay.Render2DEvent;
import vip.radium.event.impl.world.WorldLoadEvent;
import vip.radium.gui.font.FontRenderer;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.PlayerUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.OGLUtils;
import vip.radium.utils.render.RenderingUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(label = "Target HUD", category = ModuleCategory.VISUALS)
public final class TargetHUD extends Module {

    private final EnumProperty<ColorMode> colorModeProperty = new EnumProperty<>("Color Mode", ColorMode.COLOR);

    private final Property<Boolean> pulsingProperty = new Property<>("Pulsing", true,
            () -> colorModeProperty.getValue() != ColorMode.BLEND);
    private final Property<Integer> colorProperty = new Property<>("Color", 0xFFFFFFFF,
            () -> colorModeProperty.getValue() != ColorMode.HEALTH);
    private final Property<Integer> secondColorProperty = new Property<>("Second Color", 0xFFFFFFFF,
            () -> colorModeProperty.getValue() == ColorMode.BLEND);
    private final DoubleProperty opacityProperty = new DoubleProperty("Opacity", 100, 0, 100, 1, Representation.PERCENTAGE);

    private final Map<EntityLivingBase, Double> entityDamageMap = new HashMap<>();
    private final Map<EntityLivingBase, Integer> entityArmorCache = new HashMap<>();

    @EventLink
    public final Listener<EntityHealthUpdateEvent> onDamageEntityEvent =
            event -> entityDamageMap.put(event.getEntity(), event.getDamage());

    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoadEvent = event -> {
        this.entityDamageMap.clear();
    };

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePosEvent = event -> {
        if (event.isPre()) {
            this.entityArmorCache.clear();
        }
    };

    @EventLink
    public final Listener<Render2DEvent> onRender2DEvent = event -> {
        final EntityLivingBase target;
        if ((target = KillAura.getInstance().getTarget()) != null) {
            final boolean isPlayer = target instanceof EntityOtherPlayerMP;

            final LockedResolution lr = event.getResolution();
            final FontRenderer fontRenderer = Wrapper.getMinecraftFontRenderer();

            final int sWidth = lr.getWidth();
            final int sHeight = lr.getHeight();

            final int middleX = sWidth / 2;
            final int middleY = sHeight / 2;

            final String name;
            if (isPlayer)
                name = ((EntityPlayer) target).getGameProfile().getName();
            else
                name = target.getDisplayName().getUnformattedText();

            final int yOffset = 20;
            final int headSize = 32;
            final int margin = 2;
            final int width = Math.max(100, headSize + (int) Math.ceil(fontRenderer.getWidth(name) / 2) + margin * 2);
            final int half = width / 2;
            final int left = middleX - half;
            final int right = middleX + half;
            final int top = middleY + yOffset;
            final int bottom = top + headSize;
            final float alpha = opacityProperty.getValue().floatValue() / 100.0F;

            // Head
            if (isPlayer) {
                OGLUtils.enableBlending();
                final AbstractClientPlayer clientPlayer = (AbstractClientPlayer) target;
                glEnable(GL_TEXTURE_2D);
                Minecraft.getMinecraft().getTextureManager().bindTexture(clientPlayer.getLocationSkin());
                glColor4f(1.0F, 1.0F, 1.0F, alpha);
                final float eightPixelOff = 1.0F / 8;
                glBegin(GL_QUADS);
                {
                    glTexCoord2f(eightPixelOff, eightPixelOff);
                    glVertex2i(left, top);
                    glTexCoord2f(eightPixelOff, eightPixelOff * 2);
                    glVertex2i(left, bottom);
                    glTexCoord2f(eightPixelOff * 2, eightPixelOff * 2);
                    glVertex2i(left + headSize, bottom);
                    glTexCoord2f(eightPixelOff * 2, eightPixelOff);
                    glVertex2i(left + headSize, top);
                }
                glEnd();
                glDisable(GL_BLEND);
            }

            final float health = target.getHealth();
            final float maxHealth = target.getMaxHealth();
            final float healthPercentage = health / maxHealth;

            int fadeColor;
            switch (colorModeProperty.getValue()) {
                case COLOR:
                    fadeColor = colorProperty.getValue();
                    break;
                case BLEND:
                    fadeColor = RenderingUtils.fadeBetween(
                            colorProperty.getValue(), secondColorProperty.getValue());
                    break;
                default:
                    fadeColor = RenderingUtils.getColorFromPercentage(healthPercentage);
            }

            if (pulsingProperty.isAvailable() && pulsingProperty.getValue())
                fadeColor = RenderingUtils.fadeBetween(
                        fadeColor,
                        RenderingUtils.darker(fadeColor));

            final int alphaInt = alphaToInt(alpha, 0);
            final int textAlpha = alphaToInt(alpha, 0x46);

            fadeColor = RenderingUtils.alphaComponent(fadeColor, textAlpha);

            // Background
            final int backgroundColor = RenderingUtils.alphaComponent(0x000000, alphaInt);
            Gui.drawRect(left + headSize, top, right, bottom, backgroundColor);

            final float infoLeft = left + headSize + margin;
            final float infoTop = top + margin;

            // Scale down
            final float scale = 0.5F;
            glScalef(scale, scale, 0);
            // Name
            float infoypos = infoTop / scale;
            fontRenderer.drawStringWithShadow(name, infoLeft / scale, infoypos, RenderingUtils.alphaComponent(0xFFFFFF, textAlpha));
            infoypos += fontRenderer.getHeight(name);
            // Health
            final String healthText = String.format("\247FHP: \247R%.1f", health);
            fontRenderer.drawStringWithShadow(healthText, infoLeft / scale, infoypos, fadeColor);
            infoypos += fontRenderer.getHeight(healthText);
            // Armor
            if (isPlayer) {
                final EntityPlayer player = (EntityPlayer) target;
                final int targetArmor = this.getOrCacheArmor(player);
                final int localArmor = this.getOrCacheArmor(Wrapper.getPlayer());

                final char prefix;
                if (targetArmor > localArmor) {
                    prefix = '4';
                } else if (targetArmor < localArmor) {
                    prefix = 'A';
                } else {
                    prefix = 'F';
                }

                final String armorText = String.format("\247FArmor: \247R%s%% \247F/ \247%s%s%%", targetArmor, prefix, Math.abs(targetArmor - localArmor));
                fontRenderer.drawStringWithShadow(armorText, infoLeft / scale, infoypos, RenderingUtils.alphaComponent(0x50FFFF, textAlpha));
            }
            // Revert scaling
            final float scaleUp = 1 / scale;
            glScalef(scaleUp, scaleUp, 0);

            // Health bar
            // Animate health bar
            target.healthProgressX = (float) RenderingUtils.linearAnimation(target.healthProgressX, healthPercentage, 0.02);

            final float healthBarRight = right - margin;
            final float xDif = healthBarRight - infoLeft;
            final float healthBarThickness = 4;
            final float healthBarEnd = infoLeft + xDif * target.healthProgressX;
            final float healthBarBottom = bottom - margin;
            final float healthBarTop = healthBarBottom - healthBarThickness;

            Gui.drawRect(infoLeft,
                    healthBarTop,
                    healthBarRight,
                    healthBarBottom,
                    backgroundColor);

            if (entityDamageMap.containsKey(target)) {
                final double lastDamage = entityDamageMap.get(target);

                if (lastDamage > 0.0) {
                    final double damageAsHealthBarWidth = xDif * (lastDamage / maxHealth);

                    Gui.drawRect(healthBarEnd,
                            healthBarTop,
                            Math.min(healthBarEnd + damageAsHealthBarWidth, healthBarRight),
                            healthBarBottom,
                            RenderingUtils.darker(fadeColor));
                }
            }

            Gui.drawRect(infoLeft,
                    healthBarTop,
                    healthBarEnd,
                    healthBarBottom,
                    fadeColor);
        }
    };

    private int getOrCacheArmor(final EntityPlayer player) {
        Integer cachedTargetArmor = this.entityArmorCache.get(player);

        if (cachedTargetArmor == null) {
            final int targetArmor = (int) Math.ceil(PlayerUtils.getTotalArmorProtection(player) / 20.0 * 100);
            this.entityArmorCache.put(player, targetArmor);
            return targetArmor;
        }

        return cachedTargetArmor;
    }

    private static int alphaToInt(float alpha, int offset) {
        return Math.min(0xFF, (int) Math.ceil(alpha * 255.0F) + offset);
    }

    private enum ColorMode {
        HEALTH, COLOR, BLEND
    }
}
