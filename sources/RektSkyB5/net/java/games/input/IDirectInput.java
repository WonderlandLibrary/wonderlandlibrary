/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.DirectInputEnvironmentPlugin;
import net.java.games.input.DummyWindow;
import net.java.games.input.IDirectInputDevice;

final class IDirectInput {
    private final List<IDirectInputDevice> devices = new ArrayList<IDirectInputDevice>();
    private final long idirectinput_address;
    private final DummyWindow window;

    public IDirectInput(DummyWindow window) throws IOException {
        this.window = window;
        this.idirectinput_address = IDirectInput.createIDirectInput();
        try {
            this.enumDevices();
        }
        catch (IOException e2) {
            this.releaseDevices();
            this.release();
            throw e2;
        }
    }

    private static final native long createIDirectInput() throws IOException;

    public final List<IDirectInputDevice> getDevices() {
        return this.devices;
    }

    private final void enumDevices() throws IOException {
        this.nEnumDevices(this.idirectinput_address);
    }

    private final native void nEnumDevices(long var1) throws IOException;

    private final void addDevice(long address, byte[] instance_guid, byte[] product_guid, int dev_type, int dev_subtype, String instance_name, String product_name) throws IOException {
        try {
            IDirectInputDevice device = new IDirectInputDevice(this.window, address, instance_guid, product_guid, dev_type, dev_subtype, instance_name, product_name);
            this.devices.add(device);
        }
        catch (IOException e2) {
            DirectInputEnvironmentPlugin.log("Failed to initialize device " + product_name + " because of: " + e2);
        }
    }

    public final void releaseDevices() {
        for (int i2 = 0; i2 < this.devices.size(); ++i2) {
            IDirectInputDevice device = this.devices.get(i2);
            device.release();
        }
    }

    public final void release() {
        IDirectInput.nRelease(this.idirectinput_address);
    }

    private static final native void nRelease(long var0);
}

