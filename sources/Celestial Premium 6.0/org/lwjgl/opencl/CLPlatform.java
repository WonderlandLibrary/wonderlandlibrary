/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLObject;
import org.lwjgl.opencl.CLObjectRegistry;
import org.lwjgl.opencl.FastLongMap;
import org.lwjgl.opencl.InfoUtil;
import org.lwjgl.opencl.api.Filter;

public final class CLPlatform
extends CLObject {
    private static final CLPlatformUtil util = (CLPlatformUtil)CLPlatform.getInfoUtilInstance(CLPlatform.class, "CL_PLATFORM_UTIL");
    private static final FastLongMap<CLPlatform> clPlatforms = new FastLongMap();
    private final CLObjectRegistry<CLDevice> clDevices;
    private Object caps;

    CLPlatform(long pointer) {
        super(pointer);
        if (this.isValid()) {
            clPlatforms.put(pointer, this);
            this.clDevices = new CLObjectRegistry();
        } else {
            this.clDevices = null;
        }
    }

    public static CLPlatform getCLPlatform(long id) {
        return clPlatforms.get(id);
    }

    public CLDevice getCLDevice(long id) {
        return this.clDevices.getObject(id);
    }

    static <T extends CLObject> InfoUtil<T> getInfoUtilInstance(Class<T> clazz, String fieldName) {
        InfoUtil instance = null;
        try {
            Class<?> infoUtil = Class.forName("org.lwjgl.opencl.InfoUtilFactory");
            instance = (InfoUtil)infoUtil.getDeclaredField(fieldName).get(null);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return instance;
    }

    public static List<CLPlatform> getPlatforms() {
        return CLPlatform.getPlatforms(null);
    }

    public static List<CLPlatform> getPlatforms(Filter<CLPlatform> filter) {
        return util.getPlatforms(filter);
    }

    public String getInfoString(int param_name) {
        return util.getInfoString(this, param_name);
    }

    public List<CLDevice> getDevices(int device_type) {
        return this.getDevices(device_type, null);
    }

    public List<CLDevice> getDevices(int device_type, Filter<CLDevice> filter) {
        return util.getDevices(this, device_type, filter);
    }

    void setCapabilities(Object caps) {
        this.caps = caps;
    }

    Object getCapabilities() {
        return this.caps;
    }

    static void registerCLPlatforms(PointerBuffer platforms, IntBuffer num_platforms) {
        if (platforms == null) {
            return;
        }
        int pos = platforms.position();
        int count = Math.min(num_platforms.get(0), platforms.remaining());
        for (int i = 0; i < count; ++i) {
            long id = platforms.get(pos + i);
            if (clPlatforms.containsKey(id)) continue;
            new CLPlatform(id);
        }
    }

    CLObjectRegistry<CLDevice> getCLDeviceRegistry() {
        return this.clDevices;
    }

    void registerCLDevices(PointerBuffer devices, IntBuffer num_devices) {
        int pos = devices.position();
        int count = Math.min(num_devices.get(num_devices.position()), devices.remaining());
        for (int i = 0; i < count; ++i) {
            long id = devices.get(pos + i);
            if (this.clDevices.hasObject(id)) continue;
            new CLDevice(id, this);
        }
    }

    void registerCLDevices(ByteBuffer devices, PointerBuffer num_devices) {
        int pos = devices.position();
        int count = Math.min((int)num_devices.get(num_devices.position()), devices.remaining()) / PointerBuffer.getPointerSize();
        for (int i = 0; i < count; ++i) {
            long id;
            int offset = pos + i * PointerBuffer.getPointerSize();
            long l = id = PointerBuffer.is64Bit() ? devices.getLong(offset) : (long)devices.getInt(offset);
            if (this.clDevices.hasObject(id)) continue;
            new CLDevice(id, this);
        }
    }

    static interface CLPlatformUtil
    extends InfoUtil<CLPlatform> {
        public List<CLPlatform> getPlatforms(Filter<CLPlatform> var1);

        public List<CLDevice> getDevices(CLPlatform var1, int var2, Filter<CLDevice> var3);
    }
}

