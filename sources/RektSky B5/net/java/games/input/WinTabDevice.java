/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.AbstractController;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Rumbler;
import net.java.games.input.WinTabComponent;
import net.java.games.input.WinTabContext;
import net.java.games.input.WinTabEnvironmentPlugin;
import net.java.games.input.WinTabPacket;

public class WinTabDevice
extends AbstractController {
    public static final int XAxis = 1;
    public static final int YAxis = 2;
    public static final int ZAxis = 3;
    public static final int NPressureAxis = 4;
    public static final int TPressureAxis = 5;
    public static final int OrientationAxis = 6;
    public static final int RotationAxis = 7;
    private WinTabContext context;
    private List<Event> eventList = new ArrayList<Event>();

    private WinTabDevice(WinTabContext context, int index, String name, Component[] components) {
        super(name, components, new Controller[0], new Rumbler[0]);
        this.context = context;
    }

    @Override
    protected boolean getNextDeviceEvent(Event event) throws IOException {
        if (this.eventList.size() > 0) {
            Event ourEvent = this.eventList.remove(0);
            event.set(ourEvent);
            return true;
        }
        return false;
    }

    @Override
    protected void pollDevice() throws IOException {
        this.context.processEvents();
        super.pollDevice();
    }

    @Override
    public Controller.Type getType() {
        return Controller.Type.TRACKPAD;
    }

    public void processPacket(WinTabPacket packet) {
        Component[] components = this.getComponents();
        for (int i2 = 0; i2 < components.length; ++i2) {
            Event event = ((WinTabComponent)components[i2]).processPacket(packet);
            if (event == null) continue;
            this.eventList.add(event);
        }
    }

    public static WinTabDevice createDevice(WinTabContext context, int deviceIndex) {
        String name = WinTabDevice.nGetName(deviceIndex);
        WinTabEnvironmentPlugin.log("Device " + deviceIndex + ", name: " + name);
        ArrayList<WinTabComponent> componentsList = new ArrayList<WinTabComponent>();
        int[] axisDetails = WinTabDevice.nGetAxisDetails(deviceIndex, 1);
        if (axisDetails.length == 0) {
            WinTabEnvironmentPlugin.log("ZAxis not supported");
        } else {
            WinTabEnvironmentPlugin.log("Xmin: " + axisDetails[0] + ", Xmax: " + axisDetails[1]);
            componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, 1, axisDetails));
        }
        axisDetails = WinTabDevice.nGetAxisDetails(deviceIndex, 2);
        if (axisDetails.length == 0) {
            WinTabEnvironmentPlugin.log("YAxis not supported");
        } else {
            WinTabEnvironmentPlugin.log("Ymin: " + axisDetails[0] + ", Ymax: " + axisDetails[1]);
            componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, 2, axisDetails));
        }
        axisDetails = WinTabDevice.nGetAxisDetails(deviceIndex, 3);
        if (axisDetails.length == 0) {
            WinTabEnvironmentPlugin.log("ZAxis not supported");
        } else {
            WinTabEnvironmentPlugin.log("Zmin: " + axisDetails[0] + ", Zmax: " + axisDetails[1]);
            componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, 3, axisDetails));
        }
        axisDetails = WinTabDevice.nGetAxisDetails(deviceIndex, 4);
        if (axisDetails.length == 0) {
            WinTabEnvironmentPlugin.log("NPressureAxis not supported");
        } else {
            WinTabEnvironmentPlugin.log("NPressMin: " + axisDetails[0] + ", NPressMax: " + axisDetails[1]);
            componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, 4, axisDetails));
        }
        axisDetails = WinTabDevice.nGetAxisDetails(deviceIndex, 5);
        if (axisDetails.length == 0) {
            WinTabEnvironmentPlugin.log("TPressureAxis not supported");
        } else {
            WinTabEnvironmentPlugin.log("TPressureAxismin: " + axisDetails[0] + ", TPressureAxismax: " + axisDetails[1]);
            componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, 5, axisDetails));
        }
        axisDetails = WinTabDevice.nGetAxisDetails(deviceIndex, 6);
        if (axisDetails.length == 0) {
            WinTabEnvironmentPlugin.log("OrientationAxis not supported");
        } else {
            WinTabEnvironmentPlugin.log("OrientationAxis mins/maxs: " + axisDetails[0] + "," + axisDetails[1] + ", " + axisDetails[2] + "," + axisDetails[3] + ", " + axisDetails[4] + "," + axisDetails[5]);
            componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, 6, axisDetails));
        }
        axisDetails = WinTabDevice.nGetAxisDetails(deviceIndex, 7);
        if (axisDetails.length == 0) {
            WinTabEnvironmentPlugin.log("RotationAxis not supported");
        } else {
            WinTabEnvironmentPlugin.log("RotationAxis is supported (by the device, not by this plugin)");
            componentsList.addAll(WinTabComponent.createComponents(context, deviceIndex, 7, axisDetails));
        }
        String[] cursorNames = WinTabDevice.nGetCursorNames(deviceIndex);
        componentsList.addAll(WinTabComponent.createCursors(context, deviceIndex, cursorNames));
        for (int i2 = 0; i2 < cursorNames.length; ++i2) {
            WinTabEnvironmentPlugin.log("Cursor " + i2 + "'s name: " + cursorNames[i2]);
        }
        int numberOfButtons = WinTabDevice.nGetMaxButtonCount(deviceIndex);
        WinTabEnvironmentPlugin.log("Device has " + numberOfButtons + " buttons");
        componentsList.addAll(WinTabComponent.createButtons(context, deviceIndex, numberOfButtons));
        Component[] components = componentsList.toArray(new Component[0]);
        return new WinTabDevice(context, deviceIndex, name, components);
    }

    private static final native String nGetName(int var0);

    private static final native int[] nGetAxisDetails(int var0, int var1);

    private static final native String[] nGetCursorNames(int var0);

    private static final native int nGetMaxButtonCount(int var0);
}

