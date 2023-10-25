/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.input;

public interface Controller {
    public String getName();

    public int getIndex();

    public int getButtonCount();

    public String getButtonName(int var1);

    public boolean isButtonPressed(int var1);

    public void poll();

    public float getPovX();

    public float getPovY();

    public float getDeadZone(int var1);

    public void setDeadZone(int var1, float var2);

    public int getAxisCount();

    public String getAxisName(int var1);

    public float getAxisValue(int var1);

    public float getXAxisValue();

    public float getXAxisDeadZone();

    public void setXAxisDeadZone(float var1);

    public float getYAxisValue();

    public float getYAxisDeadZone();

    public void setYAxisDeadZone(float var1);

    public float getZAxisValue();

    public float getZAxisDeadZone();

    public void setZAxisDeadZone(float var1);

    public float getRXAxisValue();

    public float getRXAxisDeadZone();

    public void setRXAxisDeadZone(float var1);

    public float getRYAxisValue();

    public float getRYAxisDeadZone();

    public void setRYAxisDeadZone(float var1);

    public float getRZAxisValue();

    public float getRZAxisDeadZone();

    public void setRZAxisDeadZone(float var1);

    public int getRumblerCount();

    public String getRumblerName(int var1);

    public void setRumblerStrength(int var1, float var2);
}

