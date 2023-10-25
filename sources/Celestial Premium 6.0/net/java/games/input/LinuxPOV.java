/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import java.io.IOException;
import net.java.games.input.LinuxAxisDescriptor;
import net.java.games.input.LinuxComponent;
import net.java.games.input.LinuxControllers;
import net.java.games.input.LinuxEnvironmentPlugin;
import net.java.games.input.LinuxEventComponent;

final class LinuxPOV
extends LinuxComponent {
    private final LinuxEventComponent component_x;
    private final LinuxEventComponent component_y;
    private float last_x;
    private float last_y;

    public LinuxPOV(LinuxEventComponent component_x, LinuxEventComponent component_y) {
        super(component_x);
        this.component_x = component_x;
        this.component_y = component_y;
    }

    protected final float poll() throws IOException {
        this.last_x = LinuxControllers.poll(this.component_x);
        this.last_y = LinuxControllers.poll(this.component_y);
        return this.convertValue(0.0f, null);
    }

    public float convertValue(float value, LinuxAxisDescriptor descriptor) {
        if (descriptor == this.component_x.getDescriptor()) {
            this.last_x = value;
        }
        if (descriptor == this.component_y.getDescriptor()) {
            this.last_y = value;
        }
        if (this.last_x == -1.0f && this.last_y == -1.0f) {
            return 0.125f;
        }
        if (this.last_x == -1.0f && this.last_y == 0.0f) {
            return 1.0f;
        }
        if (this.last_x == -1.0f && this.last_y == 1.0f) {
            return 0.875f;
        }
        if (this.last_x == 0.0f && this.last_y == -1.0f) {
            return 0.25f;
        }
        if (this.last_x == 0.0f && this.last_y == 0.0f) {
            return 0.0f;
        }
        if (this.last_x == 0.0f && this.last_y == 1.0f) {
            return 0.75f;
        }
        if (this.last_x == 1.0f && this.last_y == -1.0f) {
            return 0.375f;
        }
        if (this.last_x == 1.0f && this.last_y == 0.0f) {
            return 0.5f;
        }
        if (this.last_x == 1.0f && this.last_y == 1.0f) {
            return 0.625f;
        }
        LinuxEnvironmentPlugin.logln("Unknown values x = " + this.last_x + " | y = " + this.last_y);
        return 0.0f;
    }
}

