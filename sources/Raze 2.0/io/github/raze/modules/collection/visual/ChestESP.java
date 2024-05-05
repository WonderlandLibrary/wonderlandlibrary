package io.github.raze.modules.collection.visual;

import io.github.raze.Raze;
import io.github.raze.events.collection.visual.EventRender3D;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.visual.ColorUtil;
import io.github.raze.utilities.collection.visual.esp.ChestESPUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

import java.awt.*;

public class ChestESP extends AbstractModule {

    private final ArraySetting mode, colorMode;
    private final NumberSetting rColor, gColor, bColor, alphaColor;

    public ChestESP() {
        super("ChestEsp", "Renders a box on tile entities.", ModuleCategory.VISUAL);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Box",  "Box", "2D", "Other Box"),
                colorMode = new ArraySetting(this, "Color", "Custom",  "Custom", "Rainbow", "Astolfo"),

                rColor = new NumberSetting(this, "R", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom")),

                gColor = new NumberSetting(this, "G", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom")),

                bColor = new NumberSetting(this, "B", 0, 255, 255)
                        .setHidden(() -> !colorMode.compare("Custom")),

                alphaColor = new NumberSetting(this, "Transparency", 0, 255, 120)
                        .setHidden(() -> !mode.compare("Box"))

        );
    }

    @Listen
    public void onRender(EventRender3D eventRender3D) {
        for(final TileEntity entityObject : mc.theWorld.loadedTileEntityList) {

            double x = entityObject.getPos().getX() - mc.getRenderManager().renderPosX;
            double y = entityObject.getPos().getY() - mc.getRenderManager().renderPosY;
            double z = entityObject.getPos().getZ() - mc.getRenderManager().renderPosZ;

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

            if(entityObject instanceof TileEntityChest) {
                switch (mode.get()) {
                    case "Box":
                        ChestESPUtil.renderBoxESP(x, y, z, r, g, b, (float) (alphaColor.get().floatValue() / 255.0));
                        break;

                    case "2D":
                        ChestESPUtil.render2DESP(x, y, z, r, g, b);
                        break;

                    case "Other Box":
                        ChestESPUtil.renderOtherBoxESP(x, y, z, r, g, b);
                        break;
                }
            }

            if(entityObject instanceof TileEntityEnderChest) {
                switch (mode.get()) {
                    case "Box":
                        ChestESPUtil.renderBoxESP(x, y, z, r, g, b, (float) (alphaColor.get().floatValue() / 255.0));
                        break;

                    case "2D":
                        ChestESPUtil.render2DESP(x, y, z, r, g, b);
                        break;

                    case "Other Box":
                        ChestESPUtil.renderOtherBoxESP(x, y, z, r, g, b);
                        break;
                }
            }

        }
    }
}
