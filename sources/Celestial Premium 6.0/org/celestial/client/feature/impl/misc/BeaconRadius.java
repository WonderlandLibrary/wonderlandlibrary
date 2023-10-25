/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.awt.Color;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;

public class BeaconRadius
extends Feature {
    private final ListSetting colorMode = new ListSetting("Circle Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom", "Cosmo");
    private final ColorSetting customColor = new ColorSetting("Custom Color", Color.WHITE.getRGB(), () -> this.colorMode.currentMode.equals("Custom"));
    private final BooleanSetting outline = new BooleanSetting("Outline", true, () -> true);

    public BeaconRadius() {
        super("BeaconRadius", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0437\u043e\u043d\u0443 \u0440\u0430\u0434\u0438\u0443\u0441\u0430 \u043c\u0430\u044f\u043a\u0430", Type.Misc);
        this.addSettings(this.colorMode, this.customColor, this.outline);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        int oneColor = this.customColor.getColor();
        int color = 0;
        switch (this.colorMode.currentMode) {
            case "Client": {
                color = ClientHelper.getClientColor().getRGB();
                break;
            }
            case "Custom": {
                color = oneColor;
                break;
            }
            case "Astolfo": {
                color = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                break;
            }
            case "Rainbow": {
                color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
            }
        }
        for (TileEntity entity : BeaconRadius.mc.world.loadedTileEntityList) {
            if (!(entity instanceof TileEntityBeacon)) continue;
            float beaconLevel = ((TileEntityBeacon)entity).getLevels();
            float beaconRad = beaconLevel == 1.0f ? 21.0f : (beaconLevel == 2.0f ? 31.0f : (beaconLevel == 3.0f ? 41.0f : (beaconLevel == 4.0f ? 51.0f : 0.0f)));
            int points = 360;
            if (this.outline.getCurrentValue()) {
                RenderHelper.drawCircle3D(entity, (double)beaconRad - 0.006, event.getPartialTicks(), points, 6.0f, Color.BLACK.getRGB());
                RenderHelper.drawCircle3D(entity, (double)beaconRad + 0.006, event.getPartialTicks(), points, 6.0f, Color.BLACK.getRGB());
            }
            RenderHelper.drawCircle3D(entity, (double)beaconRad, event.getPartialTicks(), points, 2.0f, color);
        }
    }
}

