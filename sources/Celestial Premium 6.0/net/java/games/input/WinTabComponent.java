/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.WinTabButtonComponent;
import net.java.games.input.WinTabContext;
import net.java.games.input.WinTabCursorComponent;
import net.java.games.input.WinTabPacket;

public class WinTabComponent
extends AbstractComponent {
    public static final int XAxis = 1;
    public static final int YAxis = 2;
    public static final int ZAxis = 3;
    public static final int NPressureAxis = 4;
    public static final int TPressureAxis = 5;
    public static final int OrientationAxis = 6;
    public static final int RotationAxis = 7;
    private int min;
    private int max;
    protected float lastKnownValue;
    private boolean analog;
    static /* synthetic */ Class class$net$java$games$input$Component$Identifier$Button;

    protected WinTabComponent(WinTabContext context, int parentDevice, String name, Component.Identifier id, int min, int max) {
        super(name, id);
        this.min = min;
        this.max = max;
        this.analog = true;
    }

    protected WinTabComponent(WinTabContext context, int parentDevice, String name, Component.Identifier id) {
        super(name, id);
        this.min = 0;
        this.max = 1;
        this.analog = false;
    }

    protected float poll() throws IOException {
        return this.lastKnownValue;
    }

    public boolean isAnalog() {
        return this.analog;
    }

    public boolean isRelative() {
        return false;
    }

    public static List createComponents(WinTabContext context, int parentDevice, int axisId, int[] axisRanges) {
        ArrayList<WinTabComponent> components = new ArrayList<WinTabComponent>();
        switch (axisId) {
            case 1: {
                Component.Identifier.Axis id = Component.Identifier.Axis.X;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 2: {
                Component.Identifier.Axis id = Component.Identifier.Axis.Y;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 3: {
                Component.Identifier.Axis id = Component.Identifier.Axis.Z;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 4: {
                Component.Identifier.Axis id = Component.Identifier.Axis.X_FORCE;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 5: {
                Component.Identifier.Axis id = Component.Identifier.Axis.Y_FORCE;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                break;
            }
            case 6: {
                Component.Identifier.Axis id = Component.Identifier.Axis.RX;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                id = Component.Identifier.Axis.RY;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[2], axisRanges[3]));
                id = Component.Identifier.Axis.RZ;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[4], axisRanges[5]));
                break;
            }
            case 7: {
                Component.Identifier.Axis id = Component.Identifier.Axis.RX;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[0], axisRanges[1]));
                id = Component.Identifier.Axis.RY;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[2], axisRanges[3]));
                id = Component.Identifier.Axis.RZ;
                components.add(new WinTabComponent(context, parentDevice, id.getName(), id, axisRanges[4], axisRanges[5]));
            }
        }
        return components;
    }

    public static Collection createButtons(WinTabContext context, int deviceIndex, int numberOfButtons) {
        ArrayList<WinTabButtonComponent> buttons = new ArrayList<WinTabButtonComponent>();
        for (int i = 0; i < numberOfButtons; ++i) {
            try {
                Class buttonIdClass = class$net$java$games$input$Component$Identifier$Button == null ? WinTabComponent.class$("net.java.games.input.Component$Identifier$Button") : class$net$java$games$input$Component$Identifier$Button;
                Field idField = buttonIdClass.getField("_" + i);
                Component.Identifier id = (Component.Identifier)idField.get(null);
                buttons.add(new WinTabButtonComponent(context, deviceIndex, id.getName(), id, i));
                continue;
            }
            catch (SecurityException e) {
                e.printStackTrace();
                continue;
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
                continue;
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
                continue;
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return buttons;
    }

    public Event processPacket(WinTabPacket packet) {
        float newValue = this.lastKnownValue;
        if (this.getIdentifier() == Component.Identifier.Axis.X) {
            newValue = this.normalise(packet.PK_X);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.Y) {
            newValue = this.normalise(packet.PK_Y);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.Z) {
            newValue = this.normalise(packet.PK_Z);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.X_FORCE) {
            newValue = this.normalise(packet.PK_NORMAL_PRESSURE);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.Y_FORCE) {
            newValue = this.normalise(packet.PK_TANGENT_PRESSURE);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.RX) {
            newValue = this.normalise(packet.PK_ORIENTATION_ALT);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.RY) {
            newValue = this.normalise(packet.PK_ORIENTATION_AZ);
        }
        if (this.getIdentifier() == Component.Identifier.Axis.RZ) {
            newValue = this.normalise(packet.PK_ORIENTATION_TWIST);
        }
        if (newValue != this.getPollData()) {
            this.lastKnownValue = newValue;
            Event newEvent = new Event();
            newEvent.set(this, newValue, packet.PK_TIME * 1000L);
            return newEvent;
        }
        return null;
    }

    private float normalise(float value) {
        if (this.max == this.min) {
            return value;
        }
        float bottom = this.max - this.min;
        return (value - (float)this.min) / bottom;
    }

    public static Collection createCursors(WinTabContext context, int deviceIndex, String[] cursorNames) {
        ArrayList<WinTabCursorComponent> cursors = new ArrayList<WinTabCursorComponent>();
        for (int i = 0; i < cursorNames.length; ++i) {
            Component.Identifier.Button id = cursorNames[i].matches("Puck") ? Component.Identifier.Button.TOOL_FINGER : (cursorNames[i].matches("Eraser.*") ? Component.Identifier.Button.TOOL_RUBBER : Component.Identifier.Button.TOOL_PEN);
            cursors.add(new WinTabCursorComponent(context, deviceIndex, id.getName(), id, i));
        }
        return cursors;
    }
}

