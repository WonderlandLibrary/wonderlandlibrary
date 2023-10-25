/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.input;

import java.util.ArrayList;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.ControllerEvent;
import org.lwjgl.input.JInputController;

public class Controllers {
    private static ArrayList<JInputController> controllers = new ArrayList();
    private static int controllerCount;
    private static ArrayList<ControllerEvent> events;
    private static ControllerEvent event;
    private static boolean created;

    public static void create() throws LWJGLException {
        if (created) {
            return;
        }
        try {
            ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();
            net.java.games.input.Controller[] found = env.getControllers();
            ArrayList<net.java.games.input.Controller> lollers = new ArrayList<net.java.games.input.Controller>();
            for (net.java.games.input.Controller c : found) {
                if (c.getType().equals(Controller.Type.KEYBOARD) || c.getType().equals(Controller.Type.MOUSE)) continue;
                lollers.add(c);
            }
            for (net.java.games.input.Controller c : lollers) {
                Controllers.createController(c);
            }
            created = true;
        }
        catch (Throwable e) {
            throw new LWJGLException("Failed to initialise controllers", e);
        }
    }

    private static void createController(net.java.games.input.Controller c) {
        net.java.games.input.Controller[] subControllers = c.getControllers();
        if (subControllers.length == 0) {
            JInputController controller = new JInputController(controllerCount, c);
            controllers.add(controller);
            ++controllerCount;
        } else {
            for (net.java.games.input.Controller sub : subControllers) {
                Controllers.createController(sub);
            }
        }
    }

    public static Controller getController(int index) {
        return controllers.get(index);
    }

    public static int getControllerCount() {
        return controllers.size();
    }

    public static void poll() {
        for (int i = 0; i < controllers.size(); ++i) {
            Controllers.getController(i).poll();
        }
    }

    public static void clearEvents() {
        events.clear();
    }

    public static boolean next() {
        if (events.size() == 0) {
            event = null;
            return false;
        }
        event = events.remove(0);
        return event != null;
    }

    public static boolean isCreated() {
        return created;
    }

    public static void destroy() {
    }

    public static Controller getEventSource() {
        return event.getSource();
    }

    public static int getEventControlIndex() {
        return event.getControlIndex();
    }

    public static boolean isEventButton() {
        return event.isButton();
    }

    public static boolean isEventAxis() {
        return event.isAxis();
    }

    public static boolean isEventXAxis() {
        return event.isXAxis();
    }

    public static boolean isEventYAxis() {
        return event.isYAxis();
    }

    public static boolean isEventPovX() {
        return event.isPovX();
    }

    public static boolean isEventPovY() {
        return event.isPovY();
    }

    public static long getEventNanoseconds() {
        return event.getTimeStamp();
    }

    public static boolean getEventButtonState() {
        return event.getButtonState();
    }

    public static float getEventXAxisValue() {
        return event.getXAxisValue();
    }

    public static float getEventYAxisValue() {
        return event.getYAxisValue();
    }

    static void addEvent(ControllerEvent event) {
        if (event != null) {
            events.add(event);
        }
    }

    static {
        events = new ArrayList();
    }
}

