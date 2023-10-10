package astronaut.modules.visual;


import astronaut.Duckware;
import astronaut.events.EventRender2D;
import astronaut.events.EventTick;
import astronaut.modules.Category;
import astronaut.modules.Module;
import astronaut.utils.TimeUtils;
import eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HUD extends Module {
    public HUD() {
        super("HUD", Type.Visual, Keyboard.KEY_RSHIFT, Category.VISUAL, Color.green, "Displays some client features");
    }

    private int index, indexModules;
    private boolean opened;
    private int last = 0;
    private boolean reversed = false;
    private boolean reversed2 = false;
    private TimeUtils moveDelayTimer = new TimeUtils();
    private int maxModules;

    @Override
    public void onDisable() {

    }

    @Override
    public void setup() {

    }

    @Override
    public void onEnable() {

    }

    @EventTarget
    public void onTick(EventTick event) {
        List<Module> modules = new ArrayList<>();
        for (Module module : Duckware.moduleManager.modules) {
            if (module.type == Type.values()[index])
                modules.add(module);
        }
        if (!opened)
            indexModules = 0;
        maxModules = modules.size();
        if (this.reversed) {
            this.opened = false;
        }
        if (!opened) {
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && index < Type.values().length - 1
                    && moveDelayTimer.hasReached(120)) {
                index++;
                moveDelayTimer.reset();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_UP) && index > 0 && moveDelayTimer.hasReached(120)) {
                index--;
                moveDelayTimer.reset();
            }
        } else {
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && indexModules < maxModules - 1
                    && moveDelayTimer.hasReached(120)) {
                indexModules++;
                moveDelayTimer.reset();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_UP) && indexModules > 0 && moveDelayTimer.hasReached(120)) {
                indexModules--;
                moveDelayTimer.reset();
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if (opened && moveDelayTimer.hasReached(200)) {

                modules.get(indexModules).setToggled(!modules.get(indexModules).isToggled());

                moveDelayTimer.reset();
            } else {
                moveDelayTimer.reset();
                this.reversed = false;
                opened = true;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            this.reversed = true;
        }
    }

    @EventTarget
    public void onUpdate(EventRender2D e) {
        ScaledResolution sr = new ScaledResolution(mc);
        float x = 5, y = 20;
        float height = Type.values().length * 15, width = 60;


        List<Module> modules = new ArrayList<>();
        for (Module module : Duckware.moduleManager.modules) {
            if (module.type == Type.values()[index])
                modules.add(module);
        }

//
        Gui.drawRect(0, (int) (y - 3), 23 + Duckware.arial22.getStringWidth("Thea") +  Duckware.arial22.getStringWidth(Minecraft.getDebugFPS() + ""), (int) (y + 63), Integer.MIN_VALUE);
        Gui.drawRect(22 + Duckware.arial22.getStringWidth("Thea") +  Duckware.arial22.getStringWidth(Minecraft.getDebugFPS() + ""), (int) (y + (index * 13 - 3)), (int) (x - 10), (int) (y + 13 + (index * 13) - 3),  new Color(232, 68, 255, 255) .getRGB());
        //Gui.drawRect((int) (x - 5), (int) (y + 79), (int) (x + 51), (int) (y + 80), new Color(20, 255, 100, 200).getRGB());
        for (int yT = 0; yT < Type.values().length; yT++) {
            Duckware.arial19.drawCenteredString(Type.values()[yT].name(), (int) (x + width / 2 - 10), (int) (y + (yT * 13) - 2), Color.white.getRGB());
        }
        if (opened) {

            float moduleHeight = modules.size() * 13;
            Gui.drawRect(24 + Duckware.arial22.getStringWidth("Thea") +  Duckware.arial22.getStringWidth(Minecraft.getDebugFPS() + ""), (int) (y + (index * 13)) - 3, (int) (x + width + 62.5F + 2.5F), (int) (y + (index * 13) + moduleHeight - 1.5), new Color(20, 20, 20, 120).getRGB());
            for (int i = 0; i < modules.size(); i++) {

                Duckware.arial19.drawCenteredString(modules.get(i).getName(), 19 + Duckware.arial22.getStringWidth("Thea") +  Duckware.arial22.getStringWidth(Minecraft.getDebugFPS() + "") + 33, (int) (y + (index * 13) + (i * 13) - 1), (indexModules == i || modules.get(i).isToggled()) ? new Color(232, 68, 255, 255).getRGB() : Color.white.getRGB());

            }
        }


        Gui.drawRect(0, 0, 23 + Duckware.arial22.getStringWidth("Thea") +  Duckware.arial22.getStringWidth(Minecraft.getDebugFPS() + ""), 17, Integer.MIN_VALUE);
        GlStateManager.pushMatrix();

        Duckware.arial22.drawStringWithShadow("Thea ", 2, 2,new Color(232, 68, 255, 255) .getRGB());
        Duckware.arial22.drawStringWithShadow(" | " + Minecraft.getDebugFPS(), 25, 2, new Color(200, 140, 244, 255).getRGB());
        GlStateManager.popMatrix();
    }
}
