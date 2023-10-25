/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.CLObjectRegistry;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.InfoUtil;

public final class CLDevice
extends CLObjectChild<CLDevice> {
    private static final InfoUtil<CLDevice> util = CLPlatform.getInfoUtilInstance(CLDevice.class, "CL_DEVICE_UTIL");
    private final CLPlatform platform;
    private final CLObjectRegistry<CLDevice> subCLDevices;
    private Object caps;

    CLDevice(long pointer, CLPlatform platform) {
        this(pointer, null, platform);
    }

    CLDevice(long pointer, CLDevice parent) {
        this(pointer, parent, parent.getPlatform());
    }

    CLDevice(long pointer, CLDevice parent, CLPlatform platform) {
        super(pointer, parent);
        if (this.isValid()) {
            this.platform = platform;
            platform.getCLDeviceRegistry().registerObject(this);
            this.subCLDevices = new CLObjectRegistry();
            if (parent != null) {
                parent.subCLDevices.registerObject(this);
            }
        } else {
            this.platform = null;
            this.subCLDevices = null;
        }
    }

    public CLPlatform getPlatform() {
        return this.platform;
    }

    public CLDevice getSubCLDevice(long id) {
        return this.subCLDevices.getObject(id);
    }

    public String getInfoString(int param_name) {
        return util.getInfoString(this, param_name);
    }

    public int getInfoInt(int param_name) {
        return util.getInfoInt(this, param_name);
    }

    public boolean getInfoBoolean(int param_name) {
        return util.getInfoInt(this, param_name) != 0;
    }

    public long getInfoSize(int param_name) {
        return util.getInfoSize(this, param_name);
    }

    public long[] getInfoSizeArray(int param_name) {
        return util.getInfoSizeArray(this, param_name);
    }

    public long getInfoLong(int param_name) {
        return util.getInfoLong(this, param_name);
    }

    void setCapabilities(Object caps) {
        this.caps = caps;
    }

    Object getCapabilities() {
        return this.caps;
    }

    @Override
    int retain() {
        if (this.getParent() == null) {
            return this.getReferenceCount();
        }
        return super.retain();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    int release() {
        if (this.getParent() == null) {
            return this.getReferenceCount();
        }
        try {
            int n = super.release();
            return n;
        }
        finally {
            if (!this.isValid()) {
                ((CLDevice)this.getParent()).subCLDevices.unregisterObject(this);
            }
        }
    }

    CLObjectRegistry<CLDevice> getSubCLDeviceRegistry() {
        return this.subCLDevices;
    }

    void registerSubCLDevices(PointerBuffer devices) {
        for (int i = devices.position(); i < devices.limit(); ++i) {
            long pointer = devices.get(i);
            if (pointer == 0L) continue;
            new CLDevice(pointer, this);
        }
    }
}

