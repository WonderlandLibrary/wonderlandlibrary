/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 */
package net.ccbluex.liquidbounce.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.ccbluex.liquidbounce.value.Value;

public class ColorValue
extends Value<Integer> {
    public boolean Expanded;

    public ColorValue(String name, int color) {
        super(name, color);
        this.setValue(color);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive((Number)this.getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            this.setValue(element.getAsInt());
        }
    }

    public boolean isExpanded() {
        return this.Expanded;
    }

    public void setExpanded(boolean expanded) {
        this.Expanded = expanded;
    }

    public float[] getHSB() {
        float hue;
        float saturation;
        float[] hsbValues = new float[3];
        int cMax = Math.max((Integer)this.getValue() >>> 16 & 0xFF, (Integer)this.getValue() >>> 8 & 0xFF);
        if (((Integer)this.getValue() & 0xFF) > cMax) {
            cMax = (Integer)this.getValue() & 0xFF;
        }
        int cMin = Math.min((Integer)this.getValue() >>> 16 & 0xFF, (Integer)this.getValue() >>> 8 & 0xFF);
        if (((Integer)this.getValue() & 0xFF) < cMin) {
            cMin = (Integer)this.getValue() & 0xFF;
        }
        float brightness = (float)cMax / 255.0f;
        float f = saturation = cMax != 0 ? (float)(cMax - cMin) / (float)cMax : 0.0f;
        if (saturation == 0.0f) {
            hue = 0.0f;
        } else {
            float redC = (float)(cMax - ((Integer)this.getValue() >>> 16 & 0xFF)) / (float)(cMax - cMin);
            float greenC = (float)(cMax - ((Integer)this.getValue() >>> 8 & 0xFF)) / (float)(cMax - cMin);
            float blueC = (float)(cMax - ((Integer)this.getValue() & 0xFF)) / (float)(cMax - cMin);
            hue = (((Integer)this.getValue() >>> 16 & 0xFF) == cMax ? blueC - greenC : (((Integer)this.getValue() >>> 8 & 0xFF) == cMax ? 2.0f + redC - blueC : 4.0f + greenC - redC)) / 6.0f;
            if (hue < 0.0f) {
                hue += 1.0f;
            }
        }
        hsbValues[0] = hue;
        hsbValues[1] = saturation;
        hsbValues[2] = brightness;
        return hsbValues;
    }
}

