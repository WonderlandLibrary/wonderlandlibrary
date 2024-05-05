package intent.AquaDev.aqua.modules.visual;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPostRender2D;
import events.listeners.EventRender2D;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;

public class KeyStrokes
extends Module {
    public KeyStrokes() {
        super("KeyStrokes", "KeyStrokes", 0, Category.Visual);
        Aqua.setmgr.register(new Setting("Left|Right", (Module)this, false));
        Aqua.setmgr.register(new Setting("ClientColor", (Module)this, true));
        Aqua.setmgr.register(new Setting("Color", (Module)this));
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventPostRender2D) {
            this.render();
        }
        if (e instanceof EventRender2D) {
            this.renderShaders();
        }
    }

    public void renderShaders() {
        int posX = 4;
        int posY = 120;
        int width = 20;
        int height = 20;
        int cornerRadius = 3;
        boolean lmbPressed = KeyStrokes.mc.gameSettings.keyBindAttack.pressed;
        boolean rmbPressed = KeyStrokes.mc.gameSettings.keyBindUseItem.pressed;
        boolean spacePressed = KeyStrokes.mc.gameSettings.keyBindJump.pressed;
        boolean aPressed = KeyStrokes.mc.gameSettings.keyBindLeft.pressed;
        boolean sPressed = KeyStrokes.mc.gameSettings.keyBindBack.pressed;
        boolean dPressed = KeyStrokes.mc.gameSettings.keyBindRight.pressed;
        boolean wPressed = KeyStrokes.mc.gameSettings.keyBindForward.pressed;
        Color pressed = Aqua.setmgr.getSetting("KeyStrokesClientColor").isState() ? new Color(Aqua.setmgr.getSetting("HUDColor").getColor()) : new Color(this.getColor2());
        int color = Color.black.getRGB();
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
        if (aPressed) {
            Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
        } else {
            Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadius, (int)color), (boolean)false);
        }
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)(posX + 25), (double)posY, (double)width, (double)height, (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
        if (sPressed) {
            Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)(posX + 25), (double)posY, (double)width, (double)height, (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
        } else {
            Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)(posX + 25), (double)posY, (double)width, (double)height, (double)cornerRadius, (int)color), (boolean)false);
        }
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)(posX + 50), (double)posY, (double)width, (double)height, (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
        if (dPressed) {
            Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)(posX + 50), (double)posY, (double)width, (double)height, (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
        } else {
            Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)(posX + 50), (double)posY, (double)width, (double)height, (double)cornerRadius, (int)color), (boolean)false);
        }
        Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)(posX + 25), (double)(posY - 25), (double)width, (double)height, (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
        if (wPressed) {
            Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)(posX + 25), (double)(posY - 25), (double)width, (double)height, (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
        } else {
            Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)(posX + 25), (double)(posY - 25), (double)width, (double)height, (double)cornerRadius, (int)color), (boolean)false);
        }
        if (Aqua.setmgr.getSetting("KeyStrokesLeft|Right").isState()) {
            Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
            if (lmbPressed) {
                Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
            } else {
                Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (int)color), (boolean)false);
            }
            Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)(posX + 36), (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
            if (rmbPressed) {
                Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)(posX + 36), (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
            } else {
                Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)(posX + 36), (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (int)color), (boolean)false);
            }
            Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 45), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
            if (spacePressed) {
                Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 45), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
            } else {
                Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 45), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (int)color), (boolean)false);
            }
        } else {
            Blur.drawBlurred(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 25), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
            if (spacePressed) {
                Arraylist.drawGlowArray(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 25), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (int)pressed.getRGB()), (boolean)false);
            } else {
                Shadow.drawGlow(() -> RenderUtil.drawRoundedRect((double)posX, (double)(posY + 25), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (int)color), (boolean)false);
            }
        }
    }

    public void render() {
        Color pressed;
        int posX = 4;
        int posY = 120;
        int width = 20;
        int height = 20;
        int cornerRadius = 3;
        boolean lmbPressed = KeyStrokes.mc.gameSettings.keyBindAttack.pressed;
        boolean rmbPressed = KeyStrokes.mc.gameSettings.keyBindUseItem.pressed;
        boolean spacePressed = KeyStrokes.mc.gameSettings.keyBindJump.pressed;
        boolean aPressed = KeyStrokes.mc.gameSettings.keyBindLeft.pressed;
        boolean sPressed = KeyStrokes.mc.gameSettings.keyBindBack.pressed;
        boolean dPressed = KeyStrokes.mc.gameSettings.keyBindRight.pressed;
        boolean wPressed = KeyStrokes.mc.gameSettings.keyBindForward.pressed;
        Color color = pressed = Aqua.setmgr.getSetting("KeyStrokesClientColor").isState() ? new Color(Aqua.setmgr.getSetting("HUDColor").getColor()) : new Color(this.getColor2());
        if (aPressed) {
            RenderUtil.drawRoundedRect2Alpha((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadius, (Color)pressed);
        } else {
            RenderUtil.drawRoundedRect2Alpha((double)posX, (double)posY, (double)width, (double)height, (double)cornerRadius, (Color)new Color(0, 0, 0, 70));
        }
        if (sPressed) {
            RenderUtil.drawRoundedRect2Alpha((double)(posX + 25), (double)posY, (double)width, (double)height, (double)cornerRadius, (Color)pressed);
        } else {
            RenderUtil.drawRoundedRect2Alpha((double)(posX + 25), (double)posY, (double)width, (double)height, (double)cornerRadius, (Color)new Color(0, 0, 0, 70));
        }
        if (dPressed) {
            RenderUtil.drawRoundedRect2Alpha((double)(posX + 50), (double)posY, (double)width, (double)height, (double)cornerRadius, (Color)pressed);
        } else {
            RenderUtil.drawRoundedRect2Alpha((double)(posX + 50), (double)posY, (double)width, (double)height, (double)cornerRadius, (Color)new Color(0, 0, 0, 70));
        }
        if (wPressed) {
            RenderUtil.drawRoundedRect2Alpha((double)(posX + 25), (double)(posY - 25), (double)width, (double)height, (double)cornerRadius, (Color)pressed);
        } else {
            RenderUtil.drawRoundedRect2Alpha((double)(posX + 25), (double)(posY - 25), (double)width, (double)height, (double)cornerRadius, (Color)new Color(0, 0, 0, 70));
        }
        if (Aqua.setmgr.getSetting("KeyStrokesLeft|Right").isState()) {
            if (lmbPressed) {
                RenderUtil.drawRoundedRect2Alpha((double)posX, (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (Color)pressed);
            } else {
                RenderUtil.drawRoundedRect2Alpha((double)posX, (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (Color)new Color(0, 0, 0, 70));
            }
            if (rmbPressed) {
                RenderUtil.drawRoundedRect2Alpha((double)(posX + 36), (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (Color)pressed);
            } else {
                RenderUtil.drawRoundedRect2Alpha((double)(posX + 36), (double)(posY + 25), (double)(width + 15), (double)(height - 5), (double)cornerRadius, (Color)new Color(0, 0, 0, 70));
            }
            if (spacePressed) {
                RenderUtil.drawRoundedRect2Alpha((double)posX, (double)(posY + 45), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (Color)pressed);
            } else {
                RenderUtil.drawRoundedRect2Alpha((double)posX, (double)(posY + 45), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (Color)new Color(0, 0, 0, 70));
            }
        } else if (spacePressed) {
            RenderUtil.drawRoundedRect2Alpha((double)posX, (double)(posY + 25), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (Color)pressed);
        } else {
            RenderUtil.drawRoundedRect2Alpha((double)posX, (double)(posY + 25), (double)(width + 50), (double)(height - 5), (double)cornerRadius, (Color)new Color(0, 0, 0, 70));
        }
        Aqua.INSTANCE.comfortaa3.drawString("A", (float)(posX + 6), (float)(posY + 5), -1);
        Aqua.INSTANCE.comfortaa3.drawString("S", (float)(posX + 32), (float)(posY + 5), -1);
        Aqua.INSTANCE.comfortaa3.drawString("D", (float)(posX + 56), (float)(posY + 5), -1);
        Aqua.INSTANCE.comfortaa3.drawString("W", (float)(posX + 30), (float)(posY - 20), -1);
        if (Aqua.setmgr.getSetting("KeyStrokesLeft|Right").isState()) {
            Aqua.INSTANCE.comfortaa3.drawString("LMB", (float)(posX + 6), (float)(posY + 27), -1);
            Aqua.INSTANCE.comfortaa3.drawString("RMB", (float)(posX + 43), (float)(posY + 27), -1);
            Aqua.INSTANCE.comfortaa3.drawString("Space", (float)(posX + 20), (float)(posY + 47), -1);
        } else {
            Aqua.INSTANCE.comfortaa3.drawString("Space", (float)(posX + 20), (float)(posY + 27), -1);
        }
    }

    public int getColor2() {
        try {
            return Aqua.setmgr.getSetting("KeyStrokesColor").getColor();
        }
        catch (Exception e) {
            return Color.white.getRGB();
        }
    }
}
