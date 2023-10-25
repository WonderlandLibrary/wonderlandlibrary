/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.player.EntityPlayer;
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
import org.celestial.client.settings.impl.NumberSetting;

public class Tracers
extends Feature {
    public static NumberSetting lineWidth;
    public static BooleanSetting friend;
    public static BooleanSetting onlyFriend;
    public static BooleanSetting seeOnly;
    public static BooleanSetting tntMinecart;
    private final ListSetting tracersToEntityMode = new ListSetting("Tracers To Entity Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");
    private final ListSetting tracersToMineCartMode = new ListSetting("Tracers To Minecart Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");
    private final ColorSetting tracersColor;
    private final ColorSetting tracersColor2;

    public Tracers() {
        super("Tracers", "\u041f\u0440\u043e\u0432\u043e\u0434\u0438\u0442 \u043b\u0438\u043d\u0438\u044e \u043a \u0438\u0433\u0440\u043e\u043a\u0430\u043c", Type.Visuals);
        seeOnly = new BooleanSetting("See Only", false, () -> true);
        onlyFriend = new BooleanSetting("Friend Only", false, () -> true);
        friend = new BooleanSetting("Friend Highlight", true, () -> true);
        lineWidth = new NumberSetting("Line Width", 1.5f, 0.1f, 5.0f, 0.1f, () -> true);
        this.tracersColor = new ColorSetting("Color To Entity", Color.WHITE.getRGB(), () -> this.tracersToEntityMode.currentMode.equals("Custom"));
        this.tracersColor2 = new ColorSetting("Color To Minecart", Color.WHITE.getRGB(), () -> this.tracersToMineCartMode.currentMode.equals("Custom"));
        tntMinecart = new BooleanSetting("TNT Minecart", false, () -> true);
        this.addSettings(this.tracersToEntityMode, this.tracersColor, seeOnly, onlyFriend, friend, tntMinecart, this.tracersToMineCartMode, this.tracersColor2, lineWidth);
    }

    @EventTarget
    public void onEvent3D(EventRender3D event) {
        int oneColor = this.tracersColor.getColor();
        int color = 0;
        switch (this.tracersToEntityMode.currentMode) {
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
        int oneColor2 = this.tracersColor2.getColor();
        int color1 = 0;
        switch (this.tracersToMineCartMode.currentMode) {
            case "Client": {
                color1 = ClientHelper.getClientColor().getRGB();
                break;
            }
            case "Custom": {
                color1 = oneColor2;
                break;
            }
            case "Astolfo": {
                color1 = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                break;
            }
            case "Rainbow": {
                color1 = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
            }
        }
        for (Entity entity : Tracers.mc.world.loadedEntityList) {
            if (entity == Tracers.mc.player || entity == null || !(entity instanceof EntityPlayer) && (!tntMinecart.getCurrentValue() || !(entity instanceof EntityMinecartTNT)) || !Tracers.mc.player.canEntityBeSeen(entity) && seeOnly.getCurrentValue()) continue;
            RenderHelper.tracersEsp(entity, lineWidth.getCurrentValue(), event.getPartialTicks(), entity instanceof EntityPlayer ? color : (tntMinecart.getCurrentValue() ? color1 : color));
        }
    }
}

