package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.event.render.RenderChestEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.sigma5.utils.ShadowESP;
import info.sigmaclient.sigma.utils.render.outline.OutlineUtils;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.EnderChestTileEntity;

import java.awt.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ChestESP extends Module {
    public static BooleanValue chest = new BooleanValue("Chest", true);
    public static BooleanValue enderChest = new BooleanValue("EnderChest", false);
    public static BooleanValue bed = new BooleanValue("Bed", false);
    public static ColorValue color = new ColorValue("Color", new Color(0xFF662E).getRGB());
    public ChestESP() {
        super("ChestESP", Category.Render, "ESP for all.");
     registerValue(chest);
     registerValue(enderChest);
     registerValue(bed);
     registerValue(color);
    }
    ShadowESP shadowESP = new ShadowESP();
    @Override
    public void onRenderChestEvent(RenderChestEvent event) {
    }
    @Override
    public void onRenderEvent(RenderEvent event) {
        super.onRenderEvent(event);
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        shadowESP.renderChest(()->{
        });
        super.onRender3DEvent(event);
    }
}
