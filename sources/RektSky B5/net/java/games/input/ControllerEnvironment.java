/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;
import net.java.games.input.DefaultControllerEnvironment;

public abstract class ControllerEnvironment {
    private static ControllerEnvironment defaultEnvironment = new DefaultControllerEnvironment();
    protected final ArrayList<ControllerListener> controllerListeners = new ArrayList();

    static void log(String msg) {
        Logger.getLogger(ControllerEnvironment.class.getName()).info(msg);
    }

    protected ControllerEnvironment() {
        if (System.getProperty("jinput.loglevel") != null) {
            String loggerName = ControllerEnvironment.class.getPackage().getName();
            Level level = Level.parse(System.getProperty("jinput.loglevel"));
            Logger.getLogger(loggerName).setLevel(level);
        }
    }

    public abstract Controller[] getControllers();

    public void addControllerListener(ControllerListener l2) {
        assert (l2 != null);
        this.controllerListeners.add(l2);
    }

    public abstract boolean isSupported();

    public void removeControllerListener(ControllerListener l2) {
        assert (l2 != null);
        this.controllerListeners.remove(l2);
    }

    protected void fireControllerAdded(Controller c2) {
        ControllerEvent ev = new ControllerEvent(c2);
        Iterator<ControllerListener> it = this.controllerListeners.iterator();
        while (it.hasNext()) {
            it.next().controllerAdded(ev);
        }
    }

    protected void fireControllerRemoved(Controller c2) {
        ControllerEvent ev = new ControllerEvent(c2);
        Iterator<ControllerListener> it = this.controllerListeners.iterator();
        while (it.hasNext()) {
            it.next().controllerRemoved(ev);
        }
    }

    public static ControllerEnvironment getDefaultEnvironment() {
        return defaultEnvironment;
    }
}

