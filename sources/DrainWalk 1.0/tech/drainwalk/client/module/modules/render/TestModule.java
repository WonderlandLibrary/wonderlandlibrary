package tech.drainwalk.client.module.modules.render;


import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import tech.drainwalk.DrainWalk;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Category;
import tech.drainwalk.client.option.options.BooleanOption;

import tech.drainwalk.events.EventRender2D;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.utility.color.ColorUtility;

import java.awt.*;

import static net.minecraft.client.entity.AbstractClientPlayer.getLocationSkin;


public class TestModule extends Module {
    BooleanOption booleanOption = new BooleanOption("Enable", true)
            .addSettingDescription("CheckBox setting");

    public TestModule() {
        super("Test", Category.RENDER);
        register(booleanOption);

    }

    @EventTarget
    public void onRender2D(EventRender2D eventRender2D) {



    }

}