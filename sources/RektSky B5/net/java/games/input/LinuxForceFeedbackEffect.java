/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import net.java.games.input.Component;
import net.java.games.input.LinuxDeviceTask;
import net.java.games.input.LinuxEnvironmentPlugin;
import net.java.games.input.LinuxEventDevice;
import net.java.games.input.Rumbler;

abstract class LinuxForceFeedbackEffect
implements Rumbler {
    private final LinuxEventDevice device;
    private final int ff_id;
    private final WriteTask write_task = new WriteTask();
    private final UploadTask upload_task = new UploadTask();

    public LinuxForceFeedbackEffect(LinuxEventDevice device) throws IOException {
        this.device = device;
        this.ff_id = this.upload_task.doUpload(-1, 0.0f);
    }

    protected abstract int upload(int var1, float var2) throws IOException;

    protected final LinuxEventDevice getDevice() {
        return this.device;
    }

    @Override
    public final synchronized void rumble(float intensity) {
        try {
            if (intensity > 0.0f) {
                this.upload_task.doUpload(this.ff_id, intensity);
                this.write_task.write(1);
            } else {
                this.write_task.write(0);
            }
        }
        catch (IOException e2) {
            LinuxEnvironmentPlugin.log("Failed to rumble: " + e2);
        }
    }

    @Override
    public final String getAxisName() {
        return null;
    }

    @Override
    public final Component.Identifier getAxisIdentifier() {
        return null;
    }

    private final class WriteTask
    extends LinuxDeviceTask {
        private int value;

        private WriteTask() {
        }

        public final void write(int value) throws IOException {
            this.value = value;
            LinuxEnvironmentPlugin.execute(this);
        }

        @Override
        protected final Object execute() throws IOException {
            LinuxForceFeedbackEffect.this.device.writeEvent(21, LinuxForceFeedbackEffect.this.ff_id, this.value);
            return null;
        }
    }

    private final class UploadTask
    extends LinuxDeviceTask {
        private int id;
        private float intensity;

        private UploadTask() {
        }

        public final int doUpload(int id, float intensity) throws IOException {
            this.id = id;
            this.intensity = intensity;
            LinuxEnvironmentPlugin.execute(this);
            return this.id;
        }

        @Override
        protected final Object execute() throws IOException {
            this.id = LinuxForceFeedbackEffect.this.upload(this.id, this.intensity);
            return null;
        }
    }
}

