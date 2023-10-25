package org.dreamcore.client.feature.impl.visual;

import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;

import java.awt.*;

public class BlockESP extends Feature {

    public static BooleanSetting enderChest;
    public static BooleanSetting chest;
    public static BooleanSetting clientColor;
    public static ColorSetting spawnerColor;
    public static BooleanSetting espOutline;
    public static ColorSetting chestColor;
    public static ColorSetting enderChestColor;
    public static ColorSetting shulkerColor;
    public static ColorSetting bedColor;
    private final BooleanSetting bed;
    private final BooleanSetting shulker;
    private final BooleanSetting spawner;

    public BlockESP() {
        super("BlockESP", "Подсвечивает опредленные блоки", Type.Visuals);
        chest = new BooleanSetting("Chest", true, () -> true);
        enderChest = new BooleanSetting("Ender Chest", false, () -> true);
        spawner = new BooleanSetting("Spawner", false, () -> true);
        shulker = new BooleanSetting("Shulker", false, () -> true);
        bed = new BooleanSetting("Bed", false, () -> true);
        chestColor = new ColorSetting("Chest Color", new Color(0xFFFFFF).getRGB(), chest::getBoolValue);
        enderChestColor = new ColorSetting("EnderChest Color", new Color(0xFFFFFF).getRGB(), enderChest::getBoolValue);
        shulkerColor = new ColorSetting("Shulker Color", new Color(0xFFFFFF).getRGB(), shulker::getBoolValue);
        spawnerColor = new ColorSetting("Spawner Color", new Color(0xFFFFFF).getRGB(), spawner::getBoolValue);
        bedColor = new ColorSetting("Bed Color", new Color(0xFFFFFF).getRGB(), bed::getBoolValue);
        clientColor = new BooleanSetting("Client Colors", false, () -> true);
        espOutline = new BooleanSetting("ESP Outline", false, () -> true);
        addSettings(espOutline, chest, enderChest, spawner, shulker, bed, chestColor, enderChestColor, spawnerColor, shulkerColor, bedColor, clientColor);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        Color colorChest = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(chestColor.getColorValue());
        Color enderColorChest = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(enderChestColor.getColorValue());
        Color shulkColor = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(shulkerColor.getColorValue());
        Color bedColoR = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(bedColor.getColorValue());
        Color spawnerColoR = clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(spawnerColor.getColorValue());
        if (mc.player != null || mc.world != null) {
            for (TileEntity entity : mc.world.loadedTileEntityList) {
                BlockPos pos = entity.getPos();
                if (entity instanceof TileEntityChest && chest.getBoolValue()) {
                    RenderHelper.blockEsp(pos, new Color(colorChest.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityEnderChest && enderChest.getBoolValue()) {
                    RenderHelper.blockEsp(pos, new Color(enderColorChest.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityBed && bed.getBoolValue()) {
                    RenderHelper.blockEsp(pos, new Color(bedColoR.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityShulkerBox && shulker.getBoolValue()) {
                    RenderHelper.blockEsp(pos, new Color(shulkColor.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityMobSpawner && spawner.getBoolValue()) {
                    RenderHelper.blockEsp(pos, new Color(spawnerColoR.getRGB()), espOutline.getBoolValue());
                }
            }
        }
    }
}
