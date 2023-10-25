/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.AWTKeyMap;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Keyboard;
import net.java.games.input.Rumbler;

final class AWTKeyboard
extends Keyboard
implements AWTEventListener {
    private final List awt_events = new ArrayList();
    private Event[] processed_events;
    private int processed_events_index;

    protected AWTKeyboard() {
        super("AWTKeyboard", AWTKeyboard.createComponents(), new Controller[0], new Rumbler[0]);
        Toolkit.getDefaultToolkit().addAWTEventListener(this, 8L);
        this.resizeEventQueue(32);
    }

    private static final Component[] createComponents() {
        ArrayList<Key> components = new ArrayList<Key>();
        Field[] vkey_fields = KeyEvent.class.getFields();
        for (int i = 0; i < vkey_fields.length; ++i) {
            Field vkey_field = vkey_fields[i];
            try {
                int vkey_code;
                Component.Identifier.Key key_id;
                if (!Modifier.isStatic(vkey_field.getModifiers()) || vkey_field.getType() != Integer.TYPE || !vkey_field.getName().startsWith("VK_") || (key_id = AWTKeyMap.mapKeyCode(vkey_code = vkey_field.getInt(null))) == Component.Identifier.Key.UNKNOWN) continue;
                components.add(new Key(key_id));
                continue;
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        components.add(new Key(Component.Identifier.Key.RCONTROL));
        components.add(new Key(Component.Identifier.Key.LCONTROL));
        components.add(new Key(Component.Identifier.Key.RSHIFT));
        components.add(new Key(Component.Identifier.Key.LSHIFT));
        components.add(new Key(Component.Identifier.Key.RALT));
        components.add(new Key(Component.Identifier.Key.LALT));
        components.add(new Key(Component.Identifier.Key.NUMPADENTER));
        components.add(new Key(Component.Identifier.Key.RETURN));
        components.add(new Key(Component.Identifier.Key.NUMPADCOMMA));
        components.add(new Key(Component.Identifier.Key.COMMA));
        return components.toArray(new Component[0]);
    }

    private final void resizeEventQueue(int size) {
        this.processed_events = new Event[size];
        for (int i = 0; i < this.processed_events.length; ++i) {
            this.processed_events[i] = new Event();
        }
        this.processed_events_index = 0;
    }

    protected final void setDeviceEventQueueSize(int size) throws IOException {
        this.resizeEventQueue(size);
    }

    public final synchronized void eventDispatched(AWTEvent event) {
        if (event instanceof KeyEvent) {
            this.awt_events.add(event);
        }
    }

    public final synchronized void pollDevice() throws IOException {
        for (int i = 0; i < this.awt_events.size(); ++i) {
            KeyEvent event = (KeyEvent)this.awt_events.get(i);
            this.processEvent(event);
        }
        this.awt_events.clear();
    }

    private final void processEvent(KeyEvent event) {
        KeyEvent nextPress;
        Component.Identifier.Key key_id = AWTKeyMap.map(event);
        if (key_id == null) {
            return;
        }
        Key key = (Key)this.getComponent(key_id);
        if (key == null) {
            return;
        }
        long nanos = event.getWhen() * 1000000L;
        if (event.getID() == 401) {
            this.addEvent(key, 1.0f, nanos);
        } else if (event.getID() == 402 && ((nextPress = (KeyEvent)Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent(401)) == null || nextPress.getWhen() != event.getWhen())) {
            this.addEvent(key, 0.0f, nanos);
        }
    }

    private final void addEvent(Key key, float value, long nanos) {
        key.setValue(value);
        if (this.processed_events_index < this.processed_events.length) {
            this.processed_events[this.processed_events_index++].set(key, value, nanos);
        }
    }

    protected final synchronized boolean getNextDeviceEvent(Event event) throws IOException {
        if (this.processed_events_index == 0) {
            return false;
        }
        --this.processed_events_index;
        event.set(this.processed_events[0]);
        Event tmp = this.processed_events[0];
        this.processed_events[0] = this.processed_events[this.processed_events_index];
        this.processed_events[this.processed_events_index] = tmp;
        return true;
    }

    private static final class Key
    extends AbstractComponent {
        private float value;

        public Key(Component.Identifier.Key key_id) {
            super(key_id.getName(), key_id);
        }

        public final void setValue(float value) {
            this.value = value;
        }

        protected final float poll() {
            return this.value;
        }

        public final boolean isAnalog() {
            return false;
        }

        public final boolean isRelative() {
            return false;
        }
    }
}

