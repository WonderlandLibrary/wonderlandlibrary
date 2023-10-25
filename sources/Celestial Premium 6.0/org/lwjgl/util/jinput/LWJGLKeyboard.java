/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.jinput;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Rumbler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.jinput.KeyMap;

final class LWJGLKeyboard
extends net.java.games.input.Keyboard {
    LWJGLKeyboard() {
        super("LWJGLKeyboard", LWJGLKeyboard.createComponents(), new Controller[0], new Rumbler[0]);
    }

    private static Component[] createComponents() {
        Field[] vkey_fields;
        ArrayList<Key> components = new ArrayList<Key>();
        for (Field vkey_field : vkey_fields = Keyboard.class.getFields()) {
            try {
                int vkey_code;
                Component.Identifier.Key key_id;
                if (!Modifier.isStatic(vkey_field.getModifiers()) || vkey_field.getType() != Integer.TYPE || !vkey_field.getName().startsWith("KEY_") || (key_id = KeyMap.map(vkey_code = vkey_field.getInt(null))) == Component.Identifier.Key.UNKNOWN) continue;
                components.add(new Key(key_id, vkey_code));
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return components.toArray(new Component[components.size()]);
    }

    public synchronized void pollDevice() throws IOException {
        if (!Keyboard.isCreated()) {
            return;
        }
        Keyboard.poll();
        for (Component component : this.getComponents()) {
            Key key = (Key)component;
            key.update();
        }
    }

    protected synchronized boolean getNextDeviceEvent(Event event) throws IOException {
        if (!Keyboard.isCreated()) {
            return false;
        }
        if (!Keyboard.next()) {
            return false;
        }
        int lwjgl_key = Keyboard.getEventKey();
        if (lwjgl_key == 0) {
            return false;
        }
        Component.Identifier.Key key_id = KeyMap.map(lwjgl_key);
        if (key_id == null) {
            return false;
        }
        Component key = this.getComponent(key_id);
        if (key == null) {
            return false;
        }
        float value = Keyboard.getEventKeyState() ? 1.0f : 0.0f;
        event.set(key, value, Keyboard.getEventNanoseconds());
        return true;
    }

    private static final class Key
    extends AbstractComponent {
        private final int lwjgl_key;
        private float value;

        Key(Component.Identifier.Key key_id, int lwjgl_key) {
            super(key_id.getName(), key_id);
            this.lwjgl_key = lwjgl_key;
        }

        public void update() {
            this.value = Keyboard.isKeyDown(this.lwjgl_key) ? 1.0f : 0.0f;
        }

        protected float poll() {
            return this.value;
        }

        public boolean isRelative() {
            return false;
        }

        public boolean isAnalog() {
            return false;
        }
    }
}

