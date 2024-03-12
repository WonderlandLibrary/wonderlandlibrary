// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import java.util.HashMap;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.ColorUtil;
import net.minecraft.entity.Entity;
import dev.tenacity.event.impl.game.WorldEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import java.awt.Color;
import java.util.Map;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.module.Module;

public class CustomModel extends Module
{
    public static final ResourceLocation amongusModel;
    public static final ResourceLocation rabbitModel;
    public static boolean enabled;
    public static final ModeSetting model;
    private static final ModeSetting mogusColorMode;
    private static final ColorSetting amongusColor;
    private static final Map<Object, Color> entityColorMap;
    
    public CustomModel() {
        super("CustomModel", "Custom Model", Category.RENDER, "Renders an custom model on every player");
        CustomModel.mogusColorMode.addParent(CustomModel.model, modeSetting -> modeSetting.is("Among Us"));
        CustomModel.amongusColor.addParent(CustomModel.mogusColorMode, modeSetting -> modeSetting.is("Custom"));
        this.addSettings(CustomModel.model, CustomModel.mogusColorMode, CustomModel.amongusColor);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        CustomModel.enabled = false;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        CustomModel.entityColorMap.clear();
        CustomModel.enabled = true;
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        CustomModel.entityColorMap.clear();
    }
    
    public static Color getColor(final Entity entity) {
        Color color = Color.WHITE;
        final String mode = CustomModel.mogusColorMode.getMode();
        switch (mode) {
            case "Sync": {
                final Pair<Color, Color> clientColors = HUDMod.getClientColors();
                color = ColorUtil.interpolateColorsBackAndForth(15, 1, clientColors.getFirst(), clientColors.getSecond(), false);
                break;
            }
            case "Random": {
                if (CustomModel.entityColorMap.containsKey(entity)) {
                    color = CustomModel.entityColorMap.get(entity);
                    break;
                }
                color = ColorUtil.getRandomColor();
                CustomModel.entityColorMap.put(entity, color);
                break;
            }
            case "Custom": {
                color = CustomModel.amongusColor.getColor();
                break;
            }
        }
        return color;
    }
    
    public static double getYOffset() {
        final String mode = CustomModel.model.getMode();
        switch (mode) {
            case "Among Us": {
                return 0.25;
            }
            default: {
                return 0.0;
            }
        }
    }
    
    static {
        amongusModel = new ResourceLocation("Tenacity/Models/amogus.png");
        rabbitModel = new ResourceLocation("Tenacity/Models/rabbit.png");
        CustomModel.enabled = false;
        model = new ModeSetting("Model", "Among Us", new String[] { "Among Us", "Rabbit" });
        mogusColorMode = new ModeSetting("Among Us Mode", "Random", new String[] { "Random", "Sync", "Custom" });
        amongusColor = new ColorSetting("Among Us Color", Color.RED);
        entityColorMap = new HashMap<Object, Color>();
    }
}
