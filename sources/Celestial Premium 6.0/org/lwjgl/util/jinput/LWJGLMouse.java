/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.jinput;

import java.io.IOException;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Rumbler;
import org.lwjgl.input.Mouse;

final class LWJGLMouse
extends net.java.games.input.Mouse {
    private static final int EVENT_X = 1;
    private static final int EVENT_Y = 2;
    private static final int EVENT_WHEEL = 3;
    private static final int EVENT_BUTTON = 4;
    private static final int EVENT_DONE = 5;
    private int event_state = 5;

    LWJGLMouse() {
        super("LWJGLMouse", LWJGLMouse.createComponents(), new Controller[0], new Rumbler[0]);
    }

    private static Component[] createComponents() {
        return new Component[]{new Axis(Component.Identifier.Axis.X), new Axis(Component.Identifier.Axis.Y), new Axis(Component.Identifier.Axis.Z), new Button(Component.Identifier.Button.LEFT), new Button(Component.Identifier.Button.MIDDLE), new Button(Component.Identifier.Button.RIGHT)};
    }

    public synchronized void pollDevice() throws IOException {
        if (!Mouse.isCreated()) {
            return;
        }
        Mouse.poll();
        for (int i = 0; i < 3; ++i) {
            this.setButtonState(i);
        }
    }

    private Button map(int lwjgl_button) {
        switch (lwjgl_button) {
            case 0: {
                return (Button)this.getLeft();
            }
            case 1: {
                return (Button)this.getRight();
            }
            case 2: {
                return (Button)this.getMiddle();
            }
        }
        return null;
    }

    private void setButtonState(int lwjgl_button) {
        Button button = this.map(lwjgl_button);
        if (button != null) {
            button.setValue(Mouse.isButtonDown(lwjgl_button) ? 1.0f : 0.0f);
        }
    }

    protected synchronized boolean getNextDeviceEvent(Event event) throws IOException {
        if (!Mouse.isCreated()) {
            return false;
        }
        block7: while (true) {
            long nanos = Mouse.getEventNanoseconds();
            switch (this.event_state) {
                case 1: {
                    this.event_state = 2;
                    int dx = Mouse.getEventDX();
                    if (dx == 0) break;
                    event.set(this.getX(), dx, nanos);
                    return true;
                }
                case 2: {
                    this.event_state = 3;
                    int dy = -Mouse.getEventDY();
                    if (dy == 0) break;
                    event.set(this.getY(), dy, nanos);
                    return true;
                }
                case 3: {
                    this.event_state = 4;
                    int dwheel = Mouse.getEventDWheel();
                    if (dwheel == 0) break;
                    event.set(this.getWheel(), dwheel, nanos);
                    return true;
                }
                case 4: {
                    this.event_state = 5;
                    int lwjgl_button = Mouse.getEventButton();
                    if (lwjgl_button == -1) break;
                    Button button = this.map(lwjgl_button);
                    if (button == null) continue block7;
                    event.set(button, Mouse.getEventButtonState() ? 1.0f : 0.0f, nanos);
                    return true;
                }
                case 5: {
                    if (!Mouse.next()) {
                        return false;
                    }
                    this.event_state = 1;
                    break;
                }
            }
        }
    }

    static final class Button
    extends AbstractComponent {
        private float value;

        Button(Component.Identifier.Button button_id) {
            super(button_id.getName(), button_id);
        }

        void setValue(float value) {
            this.value = value;
        }

        protected float poll() throws IOException {
            return this.value;
        }

        public boolean isRelative() {
            return false;
        }

        public boolean isAnalog() {
            return false;
        }
    }

    static final class Axis
    extends AbstractComponent {
        Axis(Component.Identifier.Axis axis_id) {
            super(axis_id.getName(), axis_id);
        }

        public boolean isRelative() {
            return true;
        }

        protected float poll() throws IOException {
            return 0.0f;
        }

        public boolean isAnalog() {
            return true;
        }
    }
}

