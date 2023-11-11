package me.nyan.flush.module.impl.render;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ColorSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ModuleClickGui extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", this, "Flush", "Flush", "Sigma", "Remix", "Astolfo", "Discord"),
            theme = new ModeSetting("Theme", this, "Dark", "Light", "Dark");
    private final ColorSetting color = new ColorSetting("Color", this, 0xFFBB86FF);
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow",this,false);

    public ModuleClickGui() {
        super("ClickGUI", Category.RENDER);
        addKey(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.theWorld == null) {
            toggle();
            return;
        }

        GuiScreen screen = null;
        switch (mode.getValue().toUpperCase()) {
            case "FLUSH":
                screen = flush.flushClickGui;
                break;
            case "ASTOLFO":
                screen = flush.astolfoClickGui;
                break;
            case "REMIX":
                screen = flush.remixClickGui;
                break;
            case "DISCORD":
                screen = flush.discordClickGui;
                break;
            case "SIGMA":
                screen = flush.sigmaClickGui;
                break;
        }
        if (screen != null) {
            mc.displayGuiScreen(screen);
            return;
        }
        toggle();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.displayGuiScreen(null);

        for (KeyBinding bind : MovementUtils.getMoveKeys()) {
            bind.pressed = Keyboard.isKeyDown(bind.getKeyCode());
        }
    }

     @SubscribeEvent
     public void onUpdate(EventUpdate e) {
         for (KeyBinding bind : MovementUtils.getMoveKeys()) {
             bind.pressed = Keyboard.isKeyDown(bind.getKeyCode());
         }
     }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    public int getClickGUIColor() {
        return getClickGUIColor(1);
    }

    public int getClickGUIColor(int offset) {
        return getClickGUIColor(255, offset);
    }

    public int getClickGUIColor(int alpha, int offset) {
        if (rainbow.getValue()) {
            return ColorUtils.alpha(ColorUtils.getRainbow(offset, 0.8F, 2), alpha);
        }

        return ColorUtils.alpha(color.getRGB(), alpha);
    }

    public boolean isLightTheme() {
        return theme.is("light");
    }
}
