// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class BlockESP extends Feature
{
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
    public static ColorSetting minecartColor;
    private final BooleanSetting minecart;
    private final BooleanSetting shulker;
    private final BooleanSetting spawner;
    
    public BlockESP() {
        super("BlockESP", "\u041f\u043e\u0434\u0441\u0432\u0435\u0447\u0438\u0432\u0430\u0435\u0442 \u043e\u043f\u0440\u0435\u0434\u043b\u0435\u043d\u043d\u044b\u0435 \u0431\u043b\u043e\u043a\u0438", Type.Visuals);
        BlockESP.chest = new BooleanSetting("Chest", true, () -> true);
        BlockESP.enderChest = new BooleanSetting("Ender Chest", false, () -> true);
        this.spawner = new BooleanSetting("Spawner", false, () -> true);
        this.shulker = new BooleanSetting("Shulker", false, () -> true);
        this.bed = new BooleanSetting("Bed", false, () -> true);
        this.minecart = new BooleanSetting("Minecart", false, () -> true);
        BlockESP.minecartColor = new ColorSetting("Minecart Color", new Color(16777215).getRGB(), this.minecart::getCurrentValue);
        BlockESP.chestColor = new ColorSetting("Chest Color", new Color(16777215).getRGB(), BlockESP.chest::getCurrentValue);
        BlockESP.enderChestColor = new ColorSetting("EnderChest Color", new Color(16777215).getRGB(), BlockESP.enderChest::getCurrentValue);
        BlockESP.shulkerColor = new ColorSetting("Shulker Color", new Color(16777215).getRGB(), this.shulker::getCurrentValue);
        BlockESP.spawnerColor = new ColorSetting("Spawner Color", new Color(16777215).getRGB(), this.spawner::getCurrentValue);
        BlockESP.bedColor = new ColorSetting("Bed Color", new Color(16777215).getRGB(), this.bed::getCurrentValue);
        BlockESP.clientColor = new BooleanSetting("Client Colors", false, () -> true);
        BlockESP.espOutline = new BooleanSetting("ESP Outline", false, () -> true);
        this.addSettings(BlockESP.espOutline, BlockESP.chest, BlockESP.enderChest, this.spawner, this.shulker, this.bed, this.minecart, BlockESP.chestColor, BlockESP.enderChestColor, BlockESP.spawnerColor, BlockESP.shulkerColor, BlockESP.bedColor, BlockESP.minecartColor, BlockESP.clientColor);
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        final Color colorChest = BlockESP.clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(BlockESP.chestColor.getColor());
        final Color enderColorChest = BlockESP.clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(BlockESP.enderChestColor.getColor());
        final Color shulkColor = BlockESP.clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(BlockESP.shulkerColor.getColor());
        final Color bedColoR = BlockESP.clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(BlockESP.bedColor.getColor());
        final Color spawnerColoR = BlockESP.clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(BlockESP.spawnerColor.getColor());
        final Color color;
        final Color minecraftColoR = color = (BlockESP.clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(BlockESP.minecartColor.getColor()));
        if (BlockESP.mc.h != null || BlockESP.mc.f != null) {
            for (final Object entity : BlockESP.mc.f.g) {
                final et pos = ((avj)entity).w();
                if (entity instanceof avl && BlockESP.chest.getCurrentValue()) {
                    RenderHelper.blockEsp(pos, new Color(colorChest.getRGB()), BlockESP.espOutline.getCurrentValue());
                }
                else if (entity instanceof avs && BlockESP.enderChest.getCurrentValue()) {
                    RenderHelper.blockEsp(pos, new Color(enderColorChest.getRGB()), BlockESP.espOutline.getCurrentValue());
                }
                else if (entity instanceof avi && this.bed.getCurrentValue()) {
                    RenderHelper.blockEsp(pos, new Color(bedColoR.getRGB()), BlockESP.espOutline.getCurrentValue());
                }
                else if (entity instanceof awb && this.shulker.getCurrentValue()) {
                    RenderHelper.blockEsp(pos, new Color(shulkColor.getRGB()), BlockESP.espOutline.getCurrentValue());
                }
                else {
                    if (!(entity instanceof avy)) {
                        continue;
                    }
                    if (!this.spawner.getCurrentValue()) {
                        continue;
                    }
                    RenderHelper.blockEsp(pos, new Color(spawnerColoR.getRGB()), BlockESP.espOutline.getCurrentValue());
                }
            }
            for (final Object entity : BlockESP.mc.f.e) {
                final et pos = ((vg)entity).c();
                if (entity instanceof afm) {
                    if (!this.minecart.getCurrentValue()) {
                        continue;
                    }
                    RenderHelper.blockEsp(pos, new Color(minecraftColoR.getRGB()), BlockESP.espOutline.getCurrentValue());
                }
            }
        }
    }
}
