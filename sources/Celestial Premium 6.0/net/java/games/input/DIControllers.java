/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import java.io.IOException;
import net.java.games.input.Component;
import net.java.games.input.DIComponent;
import net.java.games.input.DIDeviceObject;
import net.java.games.input.DIDeviceObjectData;
import net.java.games.input.Event;
import net.java.games.input.IDirectInputDevice;

final class DIControllers {
    private static final DIDeviceObjectData di_event = new DIDeviceObjectData();

    DIControllers() {
    }

    public static final synchronized boolean getNextDeviceEvent(Event event, IDirectInputDevice device) throws IOException {
        if (!device.getNextEvent(di_event)) {
            return false;
        }
        DIDeviceObject object = device.mapEvent(di_event);
        DIComponent component = device.mapObject(object);
        if (component == null) {
            return false;
        }
        int event_value = object.isRelative() ? object.getRelativeEventValue(di_event.getData()) : di_event.getData();
        event.set(component, component.getDeviceObject().convertValue(event_value), di_event.getNanos());
        return true;
    }

    public static final float poll(Component component, DIDeviceObject object) throws IOException {
        int poll_data = object.getDevice().getPollData(object);
        float result = object.isRelative() ? (float)object.getRelativePollValue(poll_data) : (float)poll_data;
        return object.convertValue(result);
    }
}

