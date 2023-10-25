package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;

import java.awt.*;

public class BlockESP extends Feature {

    private final BooleanSetting chest;
    private final ColorSetting spawnerColor;
    private final ColorSetting chestColor;
    private final ColorSetting shulkerColor;
    private final BooleanSetting shulker;
    private final BooleanSetting spawner;
    public boolean esp;
    public BlockESP() {
        super("Block ESP", "Показывает обозначенные блоки через стены", FeatureCategory.Render);
        chest = new BooleanSetting("Chest", true, () -> true);
        spawner = new BooleanSetting("Spawner", false, () -> true);
        shulker = new BooleanSetting("Shulker", false, () -> true);
        chestColor = new ColorSetting("Chest Color", new Color(0xEE2CFF).getRGB(), chest::getBoolValue);
        shulkerColor = new ColorSetting("Shulker Color", new Color(0xEE2CFF).getRGB(), shulker::getBoolValue);
        spawnerColor = new ColorSetting("Spawner Color", new Color(0xEE2CFF).getRGB(), spawner::getBoolValue);
        addSettings( spawner, spawnerColor,  shulker, shulkerColor,chest, chestColor);
    }
    @Override
    public void onEnable() {
    	if (Celestial.instance.featureManager.getFeature(ShaderESP.class).isEnabled()) {
    		esp = true;
    		Celestial.instance.featureManager.getFeature(ShaderESP.class).toggle();
    	}
        super.onEnable();
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        Color colorChest = new Color(chestColor.getColorValue());
        Color shulkColor = new Color(shulkerColor.getColorValue());
        Color spawnerColoR = new Color(spawnerColor.getColorValue());
        if (mc.player != null || mc.world != null) {
            for (TileEntity entity : mc.world.loadedTileEntityList) {
                BlockPos pos = entity.getPos();
                if (entity instanceof TileEntityChest && chest.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(colorChest.getRGB()), true);
                } else if (entity instanceof TileEntityShulkerBox && shulker.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(shulkColor.getRGB()), true);
                } else if (entity instanceof TileEntityMobSpawner && spawner.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(spawnerColoR.getRGB()), true);
                }
            }
        }
        if (esp) {
        	Celestial.instance.featureManager.getFeature(ShaderESP.class).toggle();
        	esp = false;
        }
    }
}
