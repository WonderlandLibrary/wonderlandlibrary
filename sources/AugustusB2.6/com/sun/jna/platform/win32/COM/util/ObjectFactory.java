// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.Kernel32;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import com.sun.jna.platform.win32.COM.IDispatch;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.COM.RunningObjectTable;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.COMUtils;
import java.util.LinkedList;
import com.sun.jna.platform.win32.WinDef;
import java.lang.ref.WeakReference;
import java.util.List;

public class ObjectFactory
{
    private final List<WeakReference<ProxyObject>> registeredObjects;
    private static final WinDef.LCID LOCALE_USER_DEFAULT;
    private WinDef.LCID LCID;
    
    public ObjectFactory() {
        this.registeredObjects = new LinkedList<WeakReference<ProxyObject>>();
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.disposeAll();
        }
        finally {
            super.finalize();
        }
    }
    
    public IRunningObjectTable getRunningObjectTable() {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final PointerByReference rotPtr = new PointerByReference();
        final WinNT.HRESULT hr = Ole32.INSTANCE.GetRunningObjectTable(new WinDef.DWORD(0L), rotPtr);
        COMUtils.checkRC(hr);
        final RunningObjectTable raw = new RunningObjectTable(rotPtr.getValue());
        final IRunningObjectTable rot = new com.sun.jna.platform.win32.COM.util.RunningObjectTable(raw, this);
        return rot;
    }
    
    public <T> T createProxy(final Class<T> comInterface, final IDispatch dispatch) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final ProxyObject jop = new ProxyObject(comInterface, dispatch, this);
        final Object proxy = Proxy.newProxyInstance(comInterface.getClassLoader(), new Class[] { comInterface }, jop);
        final T result = comInterface.cast(proxy);
        return result;
    }
    
    public <T> T createObject(final Class<T> comInterface) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final ComObject comObectAnnotation = comInterface.getAnnotation(ComObject.class);
        if (null == comObectAnnotation) {
            throw new COMException("createObject: Interface must define a value for either clsId or progId via the ComInterface annotation");
        }
        final Guid.GUID guid = this.discoverClsId(comObectAnnotation);
        final PointerByReference ptrDisp = new PointerByReference();
        final WinNT.HRESULT hr = Ole32.INSTANCE.CoCreateInstance(guid, null, 21, IDispatch.IID_IDISPATCH, ptrDisp);
        COMUtils.checkRC(hr);
        final Dispatch d = new Dispatch(ptrDisp.getValue());
        final T t = this.createProxy(comInterface, d);
        final int n = d.Release();
        return t;
    }
    
    public <T> T fetchObject(final Class<T> comInterface) throws COMException {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final ComObject comObectAnnotation = comInterface.getAnnotation(ComObject.class);
        if (null == comObectAnnotation) {
            throw new COMException("createObject: Interface must define a value for either clsId or progId via the ComInterface annotation");
        }
        final Guid.GUID guid = this.discoverClsId(comObectAnnotation);
        final PointerByReference ptrDisp = new PointerByReference();
        final WinNT.HRESULT hr = OleAuto.INSTANCE.GetActiveObject(guid, null, ptrDisp);
        COMUtils.checkRC(hr);
        final Dispatch d = new Dispatch(ptrDisp.getValue());
        final T t = this.createProxy(comInterface, d);
        d.Release();
        return t;
    }
    
    Guid.GUID discoverClsId(final ComObject annotation) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final String clsIdStr = annotation.clsId();
        final String progIdStr = annotation.progId();
        if (null != clsIdStr && !clsIdStr.isEmpty()) {
            return new Guid.CLSID(clsIdStr);
        }
        if (null != progIdStr && !progIdStr.isEmpty()) {
            final Guid.CLSID.ByReference rclsid = new Guid.CLSID.ByReference();
            final WinNT.HRESULT hr = Ole32.INSTANCE.CLSIDFromProgID(progIdStr, rclsid);
            COMUtils.checkRC(hr);
            return rclsid;
        }
        throw new COMException("ComObject must define a value for either clsId or progId");
    }
    
    IDispatchCallback createDispatchCallback(final Class<?> comEventCallbackInterface, final IComEventCallbackListener comEventCallbackListener) {
        return new CallbackProxy(this, comEventCallbackInterface, comEventCallbackListener);
    }
    
    public void register(final ProxyObject proxyObject) {
        synchronized (this.registeredObjects) {
            this.registeredObjects.add(new WeakReference<ProxyObject>(proxyObject));
        }
    }
    
    public void unregister(final ProxyObject proxyObject) {
        synchronized (this.registeredObjects) {
            final Iterator<WeakReference<ProxyObject>> iterator = this.registeredObjects.iterator();
            while (iterator.hasNext()) {
                final WeakReference<ProxyObject> weakRef = iterator.next();
                final ProxyObject po = weakRef.get();
                if (po == null || po == proxyObject) {
                    iterator.remove();
                }
            }
        }
    }
    
    public void disposeAll() {
        synchronized (this.registeredObjects) {
            final List<WeakReference<ProxyObject>> s = new ArrayList<WeakReference<ProxyObject>>(this.registeredObjects);
            for (final WeakReference<ProxyObject> weakRef : s) {
                final ProxyObject po = weakRef.get();
                if (po != null) {
                    po.dispose();
                }
            }
            this.registeredObjects.clear();
        }
    }
    
    public WinDef.LCID getLCID() {
        if (this.LCID != null) {
            return this.LCID;
        }
        return ObjectFactory.LOCALE_USER_DEFAULT;
    }
    
    public void setLCID(final WinDef.LCID value) {
        this.LCID = value;
    }
    
    static {
        LOCALE_USER_DEFAULT = Kernel32.INSTANCE.GetUserDefaultLCID();
    }
}
