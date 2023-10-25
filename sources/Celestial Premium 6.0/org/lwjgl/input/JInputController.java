/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.input;

import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;
import org.lwjgl.input.Controller;
import org.lwjgl.input.ControllerEvent;
import org.lwjgl.input.Controllers;

class JInputController
implements Controller {
    private net.java.games.input.Controller target;
    private int index;
    private ArrayList<Component> buttons = new ArrayList();
    private ArrayList<Component> axes = new ArrayList();
    private ArrayList<Component> pov = new ArrayList();
    private Rumbler[] rumblers;
    private boolean[] buttonState;
    private float[] povValues;
    private float[] axesValue;
    private float[] axesMax;
    private float[] deadZones;
    private int xaxis = -1;
    private int yaxis = -1;
    private int zaxis = -1;
    private int rxaxis = -1;
    private int ryaxis = -1;
    private int rzaxis = -1;

    JInputController(int index, net.java.games.input.Controller target) {
        Component[] sourceAxes;
        this.target = target;
        this.index = index;
        for (Component sourceAxis : sourceAxes = target.getComponents()) {
            if (sourceAxis.getIdentifier() instanceof Component.Identifier.Button) {
                this.buttons.add(sourceAxis);
                continue;
            }
            if (sourceAxis.getIdentifier().equals(Component.Identifier.Axis.POV)) {
                this.pov.add(sourceAxis);
                continue;
            }
            this.axes.add(sourceAxis);
        }
        this.buttonState = new boolean[this.buttons.size()];
        this.povValues = new float[this.pov.size()];
        this.axesValue = new float[this.axes.size()];
        int buttonsCount = 0;
        int axesCount = 0;
        for (Component sourceAxis : sourceAxes) {
            if (sourceAxis.getIdentifier() instanceof Component.Identifier.Button) {
                this.buttonState[buttonsCount] = sourceAxis.getPollData() != 0.0f;
                ++buttonsCount;
                continue;
            }
            if (sourceAxis.getIdentifier().equals(Component.Identifier.Axis.POV)) continue;
            this.axesValue[axesCount] = sourceAxis.getPollData();
            if (sourceAxis.getIdentifier().equals(Component.Identifier.Axis.X)) {
                this.xaxis = axesCount;
            }
            if (sourceAxis.getIdentifier().equals(Component.Identifier.Axis.Y)) {
                this.yaxis = axesCount;
            }
            if (sourceAxis.getIdentifier().equals(Component.Identifier.Axis.Z)) {
                this.zaxis = axesCount;
            }
            if (sourceAxis.getIdentifier().equals(Component.Identifier.Axis.RX)) {
                this.rxaxis = axesCount;
            }
            if (sourceAxis.getIdentifier().equals(Component.Identifier.Axis.RY)) {
                this.ryaxis = axesCount;
            }
            if (sourceAxis.getIdentifier().equals(Component.Identifier.Axis.RZ)) {
                this.rzaxis = axesCount;
            }
            ++axesCount;
        }
        this.axesMax = new float[this.axes.size()];
        this.deadZones = new float[this.axes.size()];
        for (int i = 0; i < this.axesMax.length; ++i) {
            this.axesMax[i] = 1.0f;
            this.deadZones[i] = 0.05f;
        }
        this.rumblers = target.getRumblers();
    }

    public String getName() {
        String name = this.target.getName();
        return name;
    }

    public int getIndex() {
        return this.index;
    }

    public int getButtonCount() {
        return this.buttons.size();
    }

    public String getButtonName(int index) {
        return this.buttons.get(index).getName();
    }

    public boolean isButtonPressed(int index) {
        return this.buttonState[index];
    }

    public void poll() {
        this.target.poll();
        Event event = new Event();
        EventQueue queue = this.target.getEventQueue();
        while (queue.getNextEvent(event)) {
            if (this.buttons.contains(event.getComponent())) {
                Component button = event.getComponent();
                int buttonIndex = this.buttons.indexOf(button);
                this.buttonState[buttonIndex] = event.getValue() != 0.0f;
                Controllers.addEvent(new ControllerEvent(this, event.getNanos(), 1, buttonIndex, this.buttonState[buttonIndex], false, false, 0.0f, 0.0f));
            }
            if (this.pov.contains(event.getComponent())) {
                Component povComponent = event.getComponent();
                int povIndex = this.pov.indexOf(povComponent);
                float prevX = this.getPovX();
                float prevY = this.getPovY();
                this.povValues[povIndex] = event.getValue();
                if (prevX != this.getPovX()) {
                    Controllers.addEvent(new ControllerEvent(this, event.getNanos(), 3, 0, false, false));
                }
                if (prevY != this.getPovY()) {
                    Controllers.addEvent(new ControllerEvent(this, event.getNanos(), 4, 0, false, false));
                }
            }
            if (!this.axes.contains(event.getComponent())) continue;
            Component axis = event.getComponent();
            int axisIndex = this.axes.indexOf(axis);
            float value = axis.getPollData();
            float xaxisValue = 0.0f;
            float yaxisValue = 0.0f;
            if (Math.abs(value) < this.deadZones[axisIndex]) {
                value = 0.0f;
            }
            if (Math.abs(value) < axis.getDeadZone()) {
                value = 0.0f;
            }
            if (Math.abs(value) > this.axesMax[axisIndex]) {
                this.axesMax[axisIndex] = Math.abs(value);
            }
            value /= this.axesMax[axisIndex];
            if (axisIndex == this.xaxis) {
                xaxisValue = value;
            }
            if (axisIndex == this.yaxis) {
                yaxisValue = value;
            }
            Controllers.addEvent(new ControllerEvent(this, event.getNanos(), 2, axisIndex, false, axisIndex == this.xaxis, axisIndex == this.yaxis, xaxisValue, yaxisValue));
            this.axesValue[axisIndex] = value;
        }
    }

    public int getAxisCount() {
        return this.axes.size();
    }

    public String getAxisName(int index) {
        return this.axes.get(index).getName();
    }

    public float getAxisValue(int index) {
        return this.axesValue[index];
    }

    public float getXAxisValue() {
        if (this.xaxis == -1) {
            return 0.0f;
        }
        return this.getAxisValue(this.xaxis);
    }

    public float getYAxisValue() {
        if (this.yaxis == -1) {
            return 0.0f;
        }
        return this.getAxisValue(this.yaxis);
    }

    public float getXAxisDeadZone() {
        if (this.xaxis == -1) {
            return 0.0f;
        }
        return this.getDeadZone(this.xaxis);
    }

    public float getYAxisDeadZone() {
        if (this.yaxis == -1) {
            return 0.0f;
        }
        return this.getDeadZone(this.yaxis);
    }

    public void setXAxisDeadZone(float zone) {
        this.setDeadZone(this.xaxis, zone);
    }

    public void setYAxisDeadZone(float zone) {
        this.setDeadZone(this.yaxis, zone);
    }

    public float getDeadZone(int index) {
        return this.deadZones[index];
    }

    public void setDeadZone(int index, float zone) {
        this.deadZones[index] = zone;
    }

    public float getZAxisValue() {
        if (this.zaxis == -1) {
            return 0.0f;
        }
        return this.getAxisValue(this.zaxis);
    }

    public float getZAxisDeadZone() {
        if (this.zaxis == -1) {
            return 0.0f;
        }
        return this.getDeadZone(this.zaxis);
    }

    public void setZAxisDeadZone(float zone) {
        this.setDeadZone(this.zaxis, zone);
    }

    public float getRXAxisValue() {
        if (this.rxaxis == -1) {
            return 0.0f;
        }
        return this.getAxisValue(this.rxaxis);
    }

    public float getRXAxisDeadZone() {
        if (this.rxaxis == -1) {
            return 0.0f;
        }
        return this.getDeadZone(this.rxaxis);
    }

    public void setRXAxisDeadZone(float zone) {
        this.setDeadZone(this.rxaxis, zone);
    }

    public float getRYAxisValue() {
        if (this.ryaxis == -1) {
            return 0.0f;
        }
        return this.getAxisValue(this.ryaxis);
    }

    public float getRYAxisDeadZone() {
        if (this.ryaxis == -1) {
            return 0.0f;
        }
        return this.getDeadZone(this.ryaxis);
    }

    public void setRYAxisDeadZone(float zone) {
        this.setDeadZone(this.ryaxis, zone);
    }

    public float getRZAxisValue() {
        if (this.rzaxis == -1) {
            return 0.0f;
        }
        return this.getAxisValue(this.rzaxis);
    }

    public float getRZAxisDeadZone() {
        if (this.rzaxis == -1) {
            return 0.0f;
        }
        return this.getDeadZone(this.rzaxis);
    }

    public void setRZAxisDeadZone(float zone) {
        this.setDeadZone(this.rzaxis, zone);
    }

    public float getPovX() {
        if (this.pov.size() == 0) {
            return 0.0f;
        }
        float value = this.povValues[0];
        if (value == 0.875f || value == 0.125f || value == 1.0f) {
            return -1.0f;
        }
        if (value == 0.625f || value == 0.375f || value == 0.5f) {
            return 1.0f;
        }
        return 0.0f;
    }

    public float getPovY() {
        if (this.pov.size() == 0) {
            return 0.0f;
        }
        float value = this.povValues[0];
        if (value == 0.875f || value == 0.625f || value == 0.75f) {
            return 1.0f;
        }
        if (value == 0.125f || value == 0.375f || value == 0.25f) {
            return -1.0f;
        }
        return 0.0f;
    }

    public int getRumblerCount() {
        return this.rumblers.length;
    }

    public String getRumblerName(int index) {
        return this.rumblers[index].getAxisName();
    }

    public void setRumblerStrength(int index, float strength) {
        this.rumblers[index].rumble(strength);
    }
}

