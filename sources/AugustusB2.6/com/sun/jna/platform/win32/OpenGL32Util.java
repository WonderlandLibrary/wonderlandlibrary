// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Function;

public abstract class OpenGL32Util
{
    public static Function wglGetProcAddress(final String procName) {
        final Pointer funcPointer = OpenGL32.INSTANCE.wglGetProcAddress("wglEnumGpusNV");
        return (funcPointer == null) ? null : Function.getFunction(funcPointer);
    }
    
    public static int countGpusNV() {
        final WinDef.HWND hWnd = User32Util.createWindow("Message", null, 0, 0, 0, 0, 0, null, null, null, null);
        final WinDef.HDC hdc = User32.INSTANCE.GetDC(hWnd);
        final WinGDI.PIXELFORMATDESCRIPTOR.ByReference pfd = new WinGDI.PIXELFORMATDESCRIPTOR.ByReference();
        pfd.nVersion = 1;
        pfd.dwFlags = 37;
        pfd.iPixelType = 0;
        pfd.cColorBits = 24;
        pfd.cDepthBits = 16;
        pfd.iLayerType = 0;
        GDI32.INSTANCE.SetPixelFormat(hdc, GDI32.INSTANCE.ChoosePixelFormat(hdc, pfd), pfd);
        final WinDef.HGLRC hGLRC = OpenGL32.INSTANCE.wglCreateContext(hdc);
        OpenGL32.INSTANCE.wglMakeCurrent(hdc, hGLRC);
        final Pointer funcPointer = OpenGL32.INSTANCE.wglGetProcAddress("wglEnumGpusNV");
        final Function fncEnumGpusNV = (funcPointer == null) ? null : Function.getFunction(funcPointer);
        OpenGL32.INSTANCE.wglDeleteContext(hGLRC);
        User32.INSTANCE.ReleaseDC(hWnd, hdc);
        User32Util.destroyWindow(hWnd);
        if (fncEnumGpusNV == null) {
            return 0;
        }
        final WinDef.HGLRCByReference hGPU = new WinDef.HGLRCByReference();
        for (int i = 0; i < 16; ++i) {
            final Boolean ok = (Boolean)fncEnumGpusNV.invoke(Boolean.class, new Object[] { i, hGPU });
            if (!ok) {
                return i;
            }
        }
        return 0;
    }
}
