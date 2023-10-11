package io.github.raze.modules.collection.visual;

import io.github.raze.Raze;
import io.github.raze.events.collection.visual.EventRender2D;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.collection.combat.Aura;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.fonts.CFontUtil;
import io.github.raze.utilities.collection.visual.ColorUtil;
import io.github.raze.utilities.collection.visual.RenderUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class TargetHUD extends AbstractModule {

    private final ArraySetting mode, colorMode;
    private final NumberSetting xPos, yPos, rColor, gColor, bColor;

    public TargetHUD() {
        super("TargetHud", "Displays a small GUI with the targets information.", ModuleCategory.VISUAL);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(
                mode = new ArraySetting(this, "Mode", "Astolfo", "Astolfo", "Old"),
                colorMode = new ArraySetting(this, "Color", "Custom",  "Custom", "Rainbow", "Astolfo"),

                xPos = new NumberSetting(this, "X Position", 0, 1000, 120),
                yPos = new NumberSetting(this, "Y Position", 0, 1000, 120),


                rColor = new NumberSetting(this, "R", 0, 255, 102)
                        .setHidden(() -> !colorMode.compare("Custom")),

                gColor = new NumberSetting(this, "G", 0, 255, 0)
                        .setHidden(() -> !colorMode.compare("Custom")),

                bColor = new NumberSetting(this, "B", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom"))
        );
    }

    @Listen
    public void onRender(EventRender2D eventRender2D) {

        float r, g, b;
        if(colorMode.compare("Custom")) {
            r = (float) (rColor.get().floatValue() / 255.0);
            g = (float) (gColor.get().floatValue() / 255.0);
            b = (float) (bColor.get().floatValue() / 255.0);
        } else if(colorMode.compare("Rainbow")) {
            Color rainbowColor = ColorUtil.getRainbow(5.0f, 1.0f, 1.0f, 2000);
            r = rainbowColor.getRed() / 255.0f;
            g = rainbowColor.getGreen() / 255.0f;
            b = rainbowColor.getBlue() / 255.0f;
        } else {
            int yOffset = 1000;
            int yTotal = 2000;
            int astolfoRGB = ColorUtil.AstolfoRGB(yOffset, yTotal);
            r = ((astolfoRGB >> 16) & 0xFF) / 255.0f;
            g = ((astolfoRGB >> 8) & 0xFF) / 255.0f;
            b = (astolfoRGB & 0xFF) / 255.0f;
        }

        EntityLivingBase kaTarget = Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(Aura.class).target;

        if(kaTarget != null && Raze.INSTANCE.managerRegistry.moduleRegistry.getByClass(Aura.class).isEnabled()) {
            if(kaTarget instanceof EntityPlayer) {
                switch(mode.get()) {
                    case "Astolfo":
                        RenderUtil.rectangle(xPos.get().doubleValue(), yPos.get().doubleValue(), 156, 57, true, new Color(0,0,0, 170));

                        CFontUtil.Jello_Medium_20.getRenderer().drawString(kaTarget.getName(), xPos.get().doubleValue() + 33, yPos.get().doubleValue() + 6, Color.WHITE);

                        CFontUtil.Jello_Medium_24.getRenderer().drawString(Math.round(kaTarget.getHealth() / 2.0f * 10) / 10 + " hp", xPos.get().doubleValue() + 33, yPos.get().doubleValue() + 20, new Color(r,g,b));

                        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
                        GuiInventory.drawEntityOnScreen(xPos.get().intValue() + 17, yPos.get().intValue() + 54, 25, kaTarget.rotationYaw, -kaTarget.rotationPitch, kaTarget);
                        RenderUtil.rectangle(xPos.get().doubleValue() + 30, yPos.get().doubleValue() + 46, 120, 8, new Color (r,g,b).darker().darker().darker());
                        RenderUtil.rectangle(xPos.get().doubleValue() + 30, yPos.get().doubleValue() + 46, kaTarget.getHealth() / kaTarget.getMaxHealth() * 120, 8, new Color (r,g,b));
                        RenderUtil.rectangle(xPos.get().doubleValue() + 30 + kaTarget.getHealth() / kaTarget.getMaxHealth() * 120 - 3, yPos.get().doubleValue() + 46, 3, 8, new Color (-1979711488, true));
                        break;

                    case "Old":
                        RenderUtil.rectangle(xPos.get().doubleValue(), yPos.get().doubleValue(), 120, 40, new Color(-1879048192, true));
                        RenderUtil.rectangle(xPos.get().doubleValue() + 5, yPos.get().doubleValue() + 2, 1, 35, new Color(r,g,b));
                        RenderUtil.rectangle(xPos.get().doubleValue() + 115, yPos.get().doubleValue() + 2, 1, 35, new Color(r,g,b));

                        CFontUtil.Jello_Medium_18.getRenderer().drawString(kaTarget.getName(), xPos.get().intValue() + ((double) 120 / 2) - 50, yPos.get().doubleValue() + 5, Color.WHITE);
                        CFontUtil.Jello_Medium_18.getRenderer().drawString("Health: " + Math.round(kaTarget.getHealth() / 2.0f * 10) / 10, xPos.get().intValue() + ((double) 120 / 2) - 50, yPos.get().doubleValue() + 15, Color.WHITE);
                        CFontUtil.Jello_Medium_18.getRenderer().drawString("Distance: " + Math.round(mc.thePlayer.getDistanceToEntity(kaTarget)) + "m", xPos.get().intValue() + ((double) 120 / 2) - 50, yPos.get().doubleValue() + 25, Color.WHITE);

                        ResourceLocation skinLoc1 = ((AbstractClientPlayer) kaTarget).getLocationSkin();
                        mc.getTextureManager().bindTexture(skinLoc1);
                        Gui.drawModalRectWithCustomSizedTexture(xPos.get().intValue() + 80, (int) (yPos.get().doubleValue() + 4), 32, 32, 32, 32, 256, 256);
                        break;
                }
            }
        }
    }
}