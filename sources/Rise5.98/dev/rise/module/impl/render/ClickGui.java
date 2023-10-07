/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.movement.InvMove;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.ui.clickgui.impl.ClickGUI;
import dev.rise.ui.mainmenu.MainMenu;
import dev.rise.util.InstanceAccess;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.util.Objects;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "ClickGui", description = "Opens a Gui where you can toggle modules and change their settings", category = Category.RENDER, defaultKey = Keyboard.KEY_RSHIFT)
public final class ClickGui extends Module implements InstanceAccess {

    private final ModeSetting theme = new ModeSetting("Theme", this, "Deep Blue Rise", "Deep Blue Rise",
            "Rural Amethyst", "Rustic Desert", "Orchid Aqua", "Alyssum Pink", "Sweet Grape Vine", "Disco");
    private final BooleanSetting dark = new BooleanSetting("Dark Mode", this, true);
    private final BooleanSetting transparency = new BooleanSetting("Transparency", this, false);
    private final BooleanSetting shadow = new BooleanSetting("Shadow", this, true);
//    private final BooleanSetting blur = new BooleanSetting("Blur Background", this, false);

    private final KeyBinding[] affectedBindings = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindJump,
            mc.gameSettings.keyBindSneak
    };

    public static Boolean brickClickGUI = null;

    @Override
    public void onUpdateAlwaysInGui() {
//        transparency.hidden = !mode.is("Rise");
////        blur.hidden = !mode.is("Rise");
//
//        scale.hidden = !mode.is("Dropdown");
    }

    @Override
    public void onEnable() {
//
//        switch (mode.getMode()) {
//            case "Rise": {
//                mc.displayGuiScreen(Rise.INSTANCE.getClickGUI());
//                break;
//            }
//
//            case "Dropdown": {
//                mc.displayGuiScreen(Rise.INSTANCE.getStrikeGUI());
//                break;
//            }
//        }

        mc.displayGuiScreen(Rise.INSTANCE.getClickGUI());
        Rise.INSTANCE.getExecutorService().execute(Rise.INSTANCE::saveClient);
    }

    @Override
    protected void onDisable() {
        Rise.INSTANCE.getExecutorService().execute(Rise.INSTANCE::saveClient);
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        //Invmove for clickgui
        if (!(mc.currentScreen instanceof GuiChat)
                && !getModule(InvMove.class).isEnabled()) {
            for (final KeyBinding a : affectedBindings) {
                a.setKeyPressed(GameSettings.isKeyDown(a));
            }
        }
    }

    @Override
    public void onUpdate(final UpdateEvent event) {
        if (mc.currentScreen == Rise.INSTANCE.getClickGUI()) ClickGUI.updateScroll();
    }

    // main menu scroll checking lol
    @Override
    public void onUpdateAlways() {
//        if (mc.currentScreen instanceof MainMenu) {
//            MainMenu mainMenu = (MainMenu) mc.currentScreen;
//
//            if (mainMenu.drawingClickGui) {
//                ClickGUI.updateScroll();
//            }
//        }

//        ClickGUI.updateScroll();
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (mc.currentScreen == Rise.INSTANCE.getStrikeGUI()) Rise.INSTANCE.strikeGUI.updateScroll();
    }
}
