/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;

public class BlockESP
extends Feature {
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
        chest = new BooleanSetting("Chest", true, () -> true);
        enderChest = new BooleanSetting("Ender Chest", false, () -> true);
        this.spawner = new BooleanSetting("Spawner", false, () -> true);
        this.shulker = new BooleanSetting("Shulker", false, () -> true);
        this.bed = new BooleanSetting("Bed", false, () -> true);
        this.minecart = new BooleanSetting("Minecart", false, () -> true);
        minecartColor = new ColorSetting("Minecart Color", new Color(0xFFFFFF).getRGB(), this.minecart::getCurrentValue);
        chestColor = new ColorSetting("Chest Color", new Color(0xFFFFFF).getRGB(), chest::getCurrentValue);
        enderChestColor = new ColorSetting("EnderChest Color", new Color(0xFFFFFF).getRGB(), enderChest::getCurrentValue);
        shulkerColor = new ColorSetting("Shulker Color", new Color(0xFFFFFF).getRGB(), this.shulker::getCurrentValue);
        spawnerColor = new ColorSetting("Spawner Color", new Color(0xFFFFFF).getRGB(), this.spawner::getCurrentValue);
        bedColor = new ColorSetting("Bed Color", new Color(0xFFFFFF).getRGB(), this.bed::getCurrentValue);
        clientColor = new BooleanSetting("Client Colors", false, () -> true);
        espOutline = new BooleanSetting("ESP Outline", false, () -> true);
        this.addSettings(espOutline, chest, enderChest, this.spawner, this.shulker, this.bed, this.minecart, chestColor, enderChestColor, spawnerColor, shulkerColor, bedColor, minecartColor, clientColor);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        Color minecraftColoR;
        Color colorChest = clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(chestColor.getColor());
        Color enderColorChest = clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(enderChestColor.getColor());
        Color shulkColor = clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(shulkerColor.getColor());
        Color bedColoR = clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(bedColor.getColor());
        Color spawnerColoR = clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(spawnerColor.getColor());
        Color color = minecraftColoR = clientColor.getCurrentValue() ? ClientHelper.getClientColor() : new Color(minecartColor.getColor());
        if (BlockESP.mc.player != null || BlockESP.mc.world != null) {
            BlockPos pos;
            for (Object entity : BlockESP.mc.world.loadedTileEntityList) {
                pos = ((TileEntity)entity).getPos();
                if (entity instanceof TileEntityChest && chest.getCurrentValue()) {
                    RenderHelper.blockEsp(pos, new Color(colorChest.getRGB()), espOutline.getCurrentValue());
                    continue;
                }
                if (entity instanceof TileEntityEnderChest && enderChest.getCurrentValue()) {
                    RenderHelper.blockEsp(pos, new Color(enderColorChest.getRGB()), espOutline.getCurrentValue());
                    continue;
                }
                if (entity instanceof TileEntityBed && this.bed.getCurrentValue()) {
                    RenderHelper.blockEsp(pos, new Color(bedColoR.getRGB()), espOutline.getCurrentValue());
                    continue;
                }
                if (entity instanceof TileEntityShulkerBox && this.shulker.getCurrentValue()) {
                    RenderHelper.blockEsp(pos, new Color(shulkColor.getRGB()), espOutline.getCurrentValue());
                    continue;
                }
                if (!(entity instanceof TileEntityMobSpawner) || !this.spawner.getCurrentValue()) continue;
                RenderHelper.blockEsp(pos, new Color(spawnerColoR.getRGB()), espOutline.getCurrentValue());
            }
            for (Object entity : BlockESP.mc.world.loadedEntityList) {
                pos = ((Entity)entity).getPosition();
                if (!(entity instanceof EntityMinecartTNT) || !this.minecart.getCurrentValue()) continue;
                RenderHelper.blockEsp(pos, new Color(minecraftColoR.getRGB()), espOutline.getCurrentValue());
            }
        }
    }
}

