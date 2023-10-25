package ru.smertnix.celestial.draggable;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.draggable.component.DraggableComponent;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.TextureEngine;

public class DraggableScreen {

    public void draw(int mouseX, int mouseY) {
        for(DraggableComponent draggableComponent : Celestial.instance.draggableHUD.getComponents()) {
            if(draggableComponent.allowDraw()) {
                drawComponent(mouseX, mouseY, draggableComponent);
            }
        }
    }

    private void drawComponent(int mouseX, int mouseY, DraggableComponent draggableComponent) {
        draggableComponent.draw(mouseX, mouseY);

        boolean hover = MathHelper.isMouseHoveringOnRect(draggableComponent.getX(), draggableComponent.getY(), draggableComponent.getWidth(), draggableComponent.getHeight(), mouseX, mouseY);


    }

    public void click(int mouseX, int mouseY) {
        for(DraggableComponent draggableComponent : Celestial.instance.draggableHUD.getComponents()) {
            if(draggableComponent.allowDraw()) {
                draggableComponent.click(mouseX, mouseY);
            }
        }
    }

    public void release() {
        for(DraggableComponent draggableComponent : Celestial.instance.draggableHUD.getComponents()) {
            draggableComponent.release();
        }
    }

}