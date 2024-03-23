package xyz.northclient.features.values;

import lombok.Getter;
import net.minecraft.client.gui.GuiStreamIndicator;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.ModernSlider;
import xyz.northclient.features.Value;
import xyz.northclient.features.VisibleSupplier;

public class DoubleValue extends Value<Number> {
    public ModernSlider slider;
    private Number number;
    private String name;
    @Getter
    private AbstractModule owner;

    @Getter
    private double min, max, inc;

    private VisibleSupplier visibleSupplier;
    public DoubleValue(String name, AbstractModule owner) {
        this.name = name;
        this.owner = owner;
        this.slider = new ModernSlider();
        this.slider.s = this;

        this.max = 1;
        this.min = 0;
        this.inc = 1;
        this.number = 0.2d;
    }

    public DoubleValue(String name, AbstractModule owner, VisibleSupplier visibleSupplier) {
        this.name = name;
        this.owner = owner;
        this.slider = new ModernSlider();
        this.slider.s = this;

        this.max = 1;
        this.min = 0;
        this.inc = 1;
        this.number = 0.2d;
        this.visibleSupplier = visibleSupplier;
    }
    @Override
    public Number get() {
        return this.number;
    }

    public DoubleValue setMin(double min) {
        this.min = min;
        return this;
    }

    public DoubleValue enableOnlyInt() {
        this.inc = 1;
        return this;
    }

    public DoubleValue setMax(double max) {
        this.max = max;
        return this;
    }

    public DoubleValue setIncrement(double inc) {
        this.inc = inc;
        return this;
    }

    public DoubleValue setDefault(double def) {
        set(def);
        return this;
    }

    @Override
    public void set(Number value) {
        this.number =value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public VisibleSupplier isVisible() {
        return this.visibleSupplier == null ? () -> true : this.visibleSupplier;
    }
}
