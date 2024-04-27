package me.aquavit.liquidsense.ui.client.clickgui.neverlose.hud;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Impl;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Main;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import net.minecraft.client.gui.ScaledResolution;

public class HUD extends MinecraftInstance {
    public static boolean isdrag = false;

    public static void canDrag(boolean state){
        for (Module module : LiquidSense.moduleManager.getModules()) {
            if (module.getCategory() == ModuleCategory.HUD && module.getState()){
                module.setDrag(state);
            }
        }
    }

    public static boolean isInClickGui(double mousex, double mousey, float x, float y, float width, float height){
        float minX = Math.min(x, width);
        float minY = Math.min(y, height);

        float maxX = Math.max(x, width);
        float maxY = Math.max(y, height);

        return minX <= mousex && minY <= mousey && maxX >= mousex && maxY >= mousey;
    }

    public static void handleMouseClick(final int mouseX, final int mouseY) {
        for (Module module : LiquidSense.moduleManager.getModules()) {
            boolean isHUDModule = module.getCategory() == ModuleCategory.HUD;
            boolean isModuleState = module.getState();
            boolean isInHUDBorder = module.isInBorder(mouseX, mouseY);
            boolean isInMainClickGui = isInClickGui(mouseX, mouseY, Impl.coordinateX, Impl.coordinateY, Impl.coordinateX + 460f, Impl.coordinateY + 345f);
            boolean isInSubClickGui = isInClickGui(mouseX, mouseY, Impl.coordinateX + 470, Impl.coordinateY + 70, Impl.coordinateX + 630, Impl.coordinateY + 220);

            if (isHUDModule && isModuleState && isInHUDBorder &&
                    !isdrag && !Main.hovermove &&
                    (!isInMainClickGui || (isdrag && isInMainClickGui)) &&
                    (!isInSubClickGui || (isdrag && isInSubClickGui))) {
                module.setDrag(true);
                isdrag = true;
            }
        }
    }

    public static void handleMouseReleased() {
        for (Module module : LiquidSense.moduleManager.getModules()) {
            if (module.getCategory() == ModuleCategory.HUD && module.getState()) {
                module.setDrag(false);
                isdrag = false;
            }
        }
    }

    public static void handleMouseMove(int mouseX, int mouseY) {
        if (!(mc.currentScreen instanceof Main)) return;

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        for (Module module : LiquidSense.moduleManager.getModules()) {

            if (module.getCategory() == ModuleCategory.HUD && module.getState()) {

                float prevMouseX = (float) module.getPrevMouseX();
                float prevMouseY = (float) module.getPrevMouseY();

                module.setPrevMouseX(mouseX);
                module.setPrevMouseY(mouseY);

                if (module.getDrag()) {
                    isdrag = true;

                    float moveX = mouseX - prevMouseX;
                    float moveY = mouseY - prevMouseY;

                    if (moveX == 0F && moveY == 0F) continue;

                    float minX = (float) (Math.min(module.getBorderX(), module.getBorderWidth()) + 1);
                    float minY = (float) (Math.min(module.getBorderY(), module.getBorderHeight()) + 1);

                    float maxX = (float) (Math.max(module.getBorderX(), module.getBorderWidth()) - 1);
                    float maxY = (float) (Math.max(module.getBorderY(), module.getBorderHeight()) - 1);

                    float width = scaledResolution.getScaledWidth() ;
                    float height = scaledResolution.getScaledHeight();

                    if ((minX + moveX >= 0.0 || moveX > 0) && (maxX + moveX <= width || moveX < 0))
                        module.setRenderx(module.isLeft ? module.getRenderx() + moveX : module.getRenderx() - moveX);
                    if ((minY + moveY >= 0.0 || moveY > 0) && (maxY + moveY <= height || moveY < 0))
                        module.setRendery(module.isUp ? module.getRendery() + moveY : module.getRendery() - moveY);

                }

            }
        }
    }
}
