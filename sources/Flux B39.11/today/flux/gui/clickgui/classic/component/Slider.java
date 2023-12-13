package today.flux.gui.clickgui.classic.component;

import today.flux.gui.clickgui.classic.window.Window;
import today.flux.module.value.FloatValue;

public class Slider extends BasicSlider {
    public FloatValue storage;
    public String setting;

    public Slider(FloatValue value, Window window, int offX, int offY, String title) {
        super(window, value, offX, offY, title, value.getMin(), value.getMax(), value.getIncrement());

        this.unit = value.getUnit();
        this.storage = value;
        this.setting = "WTF";
        this.type = "Slider";
    }

    public void update(int mouseX, int mouseY) {
        super.update(mouseX, mouseY);

        if (this.isDragging) {
            this.storage.setValue(this.value);
        } else {
            this.value = this.storage.getValue();
        }
    }
}
