/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import java.lang.reflect.Field;
import java.util.Map;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opencl.APPLEGLSharing;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CL11;
import org.lwjgl.opencl.EXTDeviceFission;
import org.lwjgl.opencl.KHRGLSharing;
import org.lwjgl.opencl.KHRICD;
import org.lwjgl.opencl.OpenCLException;

public final class Util {
    private static final Map<Integer, String> CL_ERROR_TOKENS = LWJGLUtil.getClassTokens(new LWJGLUtil.TokenFilter(){

        public boolean accept(Field field, int value) {
            return value < 0;
        }
    }, null, CL10.class, CL11.class, KHRGLSharing.class, KHRICD.class, APPLEGLSharing.class, EXTDeviceFission.class);

    private Util() {
    }

    public static void checkCLError(int errcode) {
        if (errcode != 0) {
            Util.throwCLError(errcode);
        }
    }

    private static void throwCLError(int errcode) {
        String errname = CL_ERROR_TOKENS.get(errcode);
        if (errname == null) {
            errname = "UNKNOWN";
        }
        throw new OpenCLException("Error Code: " + errname + " (" + LWJGLUtil.toHexString(errcode) + ")");
    }
}

