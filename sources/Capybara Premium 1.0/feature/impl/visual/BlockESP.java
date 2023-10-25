package fun.expensive.client.feature.impl.visual;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.render.EventRender3D;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.utils.render.ClientHelper;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class BlockESP extends Feature {

    private final BooleanSetting enderChest;
    private final BooleanSetting chest;
    private final BooleanSetting clientColor;
    private final ColorSetting spawnerColor;
    private final BooleanSetting espOutline;
    private final ColorSetting chestColor;
    private final ColorSetting enderChestColor;
    private final ColorSetting shulkerColor;
    private final ColorSetting bedColor;
    private final BooleanSetting bed;
    private final BooleanSetting shulker;
    private final BooleanSetting spawner;
    private final BooleanSetting f;

    public BlockESP() {
        super("BlockESP", "Подсвечивает опредленные блоки", FeatureCategory.Visuals);
        chest = new BooleanSetting("Chest", true, () -> true);
        enderChest = new BooleanSetting("Ender Chest", false, () -> true);
        f = new BooleanSetting("Minecard", false, () -> true);

        spawner = new BooleanSetting("Spawner", false, () -> true);
        shulker = new BooleanSetting("Shulker", false, () -> true);
        bed = new BooleanSetting("Bed", false, () -> true);
        chestColor = new ColorSetting("Chest Color", new Color(0xEE2CFF).getRGB(), chest::getBoolValue);
        enderChestColor = new ColorSetting("EnderChest Color", new Color(0xEE2CFF).getRGB(), enderChest::getBoolValue);
        shulkerColor = new ColorSetting("Shulker Color", new Color(0xEE2CFF).getRGB(), shulker::getBoolValue);
        spawnerColor = new ColorSetting("Spawner Color", new Color(0xEE2CFF).getRGB(), spawner::getBoolValue);
        bedColor = new ColorSetting("Bed Color", new Color(0xEE2CFF).getRGB(), bed::getBoolValue);
        clientColor = new BooleanSetting("Client Colors", false, () -> true);
        espOutline = new BooleanSetting("ESP Outline", false, () -> true);
        addSettings(espOutline, chest, enderChest, spawner, shulker, bed, f, chestColor, enderChestColor, spawnerColor, shulkerColor, bedColor, clientColor);
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
                    RenderUtils.blockEsp(pos, new Color(colorChest.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityEnderChest && enderChest.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(enderColorChest.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityBed && bed.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(bedColoR.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityShulkerBox && shulker.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(shulkColor.getRGB()), espOutline.getBoolValue());
                } else if (entity instanceof TileEntityMobSpawner && spawner.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(spawnerColoR.getRGB()), espOutline.getBoolValue());
                }
            }
        }
    }
}
