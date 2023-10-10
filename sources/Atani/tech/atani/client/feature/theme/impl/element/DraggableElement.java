package tech.atani.client.feature.theme.impl.element;

import com.google.common.base.Supplier;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.theme.ThemeObject;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.utility.math.atomic.AtomicFloat;

public abstract class DraggableElement extends ThemeObject {

    private SliderValue<Float> x, y, width, height;
    private Class linkedModule;
    private float startX, startY;
    private CheckBoxValue locked;
    private float draggingX, draggingY;
    private boolean dragging = false;

    public DraggableElement(float x, float y, float width, float height, Supplier<Boolean> showWidthAndHeight, Class linkedModule) {
        startX = x;
        startY = y;
        this.locked = new CheckBoxValue("Locked", "Lock to it's pre determined position?", this, true);
        this.x = new SliderValue<>("X", "What should the X position be?", this, x, 0f, 999999999f, 1, new Supplier[]{() -> !locked.getValue()});
        this.y = new SliderValue<>("Y", "What should the Y position be?", this, y, 0f, 999999999f, 1, new Supplier[]{() -> !locked.getValue()});
        this.width = new SliderValue<>("Width", "What should the width be?", this, width, 0f, 999999999f, 0, new Supplier[]{() -> !locked.getValue(), showWidthAndHeight == null ? () -> false : showWidthAndHeight});
        this.height = new SliderValue<>("Height", "What should the height be?", this, height, 0f, 999999999f, 0, new Supplier[]{() -> !locked.getValue(), showWidthAndHeight == null ? () -> false : showWidthAndHeight});
        this.linkedModule = linkedModule;
    }

    public void onDrag(int mouseX, int mouseY) {
        if(this.locked.getValue()) {
            this.x.setValue(startX);
            this.y.setValue(startY);
        } else if(Mouse.isButtonDown(0)) {
            if(!dragging) {
                draggingX = mouseX - x.getValue();
                draggingY = mouseY - y.getValue();
            }
            x.setValue(mouseX - draggingX);
            y.setValue(mouseY - draggingY);
            dragging = true;
        } else {
            dragging = false;
        }
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, Object[] params) {
        this.x.setMaximum((float)scaledResolution.getScaledWidth());
        this.y.setMaximum((float)scaledResolution.getScaledHeight());

        if(this.locked.getValue()) {
            this.x.setValue(startX);
            this.y.setValue(startY);
        }
        onDraw(scaledResolution, partialTicks, leftY, rightY);
    }

    @Override
    public void onEnable() {
        Module linkedModule = ModuleStorage.getInstance().getByClass(this.linkedModule);
        for(Value value : ValueStorage.getInstance().getValues(this)) {
            ValueStorage.getInstance().addLinkedValues(linkedModule, value);
        }
    }

    @Override
    public void onDisable() {
        Module linkedModule = ModuleStorage.getInstance().getByClass(this.linkedModule);
        for(Value value : ValueStorage.getInstance().getValues(this)) {
            ValueStorage.getInstance().removeLinkedValues(linkedModule, value);
        }
    }

    public abstract void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY);


    public SliderValue<Float> getPosX() {
        return x;
    }

    public SliderValue<Float> getPosY() {
        return y;
    }

    public SliderValue<Float> getWidth() {
        return width;
    }

    public SliderValue<Float> getHeight() {
        return height;
    }

    public CheckBoxValue getLocked() {
        return locked;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }
}
