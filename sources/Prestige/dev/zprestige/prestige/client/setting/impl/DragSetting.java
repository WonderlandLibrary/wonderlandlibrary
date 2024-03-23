package dev.zprestige.prestige.client.setting.impl;

import dev.zprestige.prestige.client.setting.Setting;
import dev.zprestige.prestige.client.util.impl.RandomUtil;

import java.util.function.Predicate;

public class DragSetting extends Setting<Float> {

    public float first;
    public float second;
    public float min;
    public float max;
    public float value;

    public DragSetting(String name, float first, float second, float min, float max) {
        super(name, first);
        this.first = first;
        this.second = second;
        this.min = min;
        this.max = max;
        this.value = this.first;
    }

    public float getFirst() {
        return first;
    }

    public void setFirst(float first) {
        this.first = first;
    }

    public float getSecond() {
        return second;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getValue() {
        return value;
    }

    public void setValue() {
        value = getRandomValue();
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getRandomValue() {
        return RandomUtil.INSTANCE.randomInRange(first, second);
    }

    @Override
    public DragSetting invokeVisibility(Predicate<Float> visible) {
        super.invokeVisibility(visible);
        return this;
    }

    @Override
    public DragSetting description(String description) {
        super.description(description);
        return this;
    }
}