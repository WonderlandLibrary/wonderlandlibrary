/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Keyboard;
import net.java.games.input.RawDevice;
import net.java.games.input.RawIdentifierMap;
import net.java.games.input.RawKeyboardEvent;
import net.java.games.input.Rumbler;

final class RawKeyboard
extends Keyboard {
    private final RawKeyboardEvent raw_event = new RawKeyboardEvent();
    private final RawDevice device;

    protected RawKeyboard(String name, RawDevice device, Controller[] children, Rumbler[] rumblers) throws IOException {
        super(name, RawKeyboard.createKeyboardComponents(device), children, rumblers);
        this.device = device;
    }

    private static final Component[] createKeyboardComponents(RawDevice device) {
        ArrayList<Key> components = new ArrayList<Key>();
        Field[] vkey_fields = RawIdentifierMap.class.getFields();
        for (int i2 = 0; i2 < vkey_fields.length; ++i2) {
            Field vkey_field = vkey_fields[i2];
            try {
                int vkey_code;
                Component.Identifier.Key key_id;
                if (!Modifier.isStatic(vkey_field.getModifiers()) || vkey_field.getType() != Integer.TYPE || (key_id = RawIdentifierMap.mapVKey(vkey_code = vkey_field.getInt(null))) == Component.Identifier.Key.UNKNOWN) continue;
                components.add(new Key(device, vkey_code, key_id));
                continue;
            }
            catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        }
        return components.toArray(new Component[0]);
    }

    @Override
    protected final synchronized boolean getNextDeviceEvent(Event event) throws IOException {
        Component key;
        while (true) {
            if (!this.device.getNextKeyboardEvent(this.raw_event)) {
                return false;
            }
            int vkey = this.raw_event.getVKey();
            Component.Identifier.Key key_id = RawIdentifierMap.mapVKey(vkey);
            key = this.getComponent(key_id);
            if (key == null) continue;
            int message = this.raw_event.getMessage();
            if (message == 256 || message == 260) {
                event.set(key, 1.0f, this.raw_event.getNanos());
                return true;
            }
            if (message == 257 || message == 261) break;
        }
        event.set(key, 0.0f, this.raw_event.getNanos());
        return true;
    }

    @Override
    public final void pollDevice() throws IOException {
        this.device.pollKeyboard();
    }

    @Override
    protected final void setDeviceEventQueueSize(int size) throws IOException {
        this.device.setBufferSize(size);
    }

    static final class Key
    extends AbstractComponent {
        private final RawDevice device;
        private final int vkey_code;

        public Key(RawDevice device, int vkey_code, Component.Identifier.Key key_id) {
            super(key_id.getName(), key_id);
            this.device = device;
            this.vkey_code = vkey_code;
        }

        @Override
        protected final float poll() throws IOException {
            return this.device.isKeyDown(this.vkey_code) ? 1.0f : 0.0f;
        }

        @Override
        public final boolean isAnalog() {
            return false;
        }

        @Override
        public final boolean isRelative() {
            return false;
        }
    }
}

