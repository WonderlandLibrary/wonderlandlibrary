// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.WString;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.Pointer;
import java.util.List;
import com.sun.jna.platform.win32.Variant;
import java.util.ArrayList;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import com.sun.jna.platform.win32.COM.util.annotation.ComEventCallback;
import java.util.HashMap;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import java.lang.reflect.Method;
import com.sun.jna.platform.win32.OaIdl;
import java.util.Map;
import com.sun.jna.platform.win32.COM.DispatchListener;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.COM.IDispatchCallback;

public class CallbackProxy implements IDispatchCallback
{
    private static boolean DEFAULT_BOOLEAN;
    private static byte DEFAULT_BYTE;
    private static short DEFAULT_SHORT;
    private static int DEFAULT_INT;
    private static long DEFAULT_LONG;
    private static float DEFAULT_FLOAT;
    private static double DEFAULT_DOUBLE;
    ObjectFactory factory;
    Class<?> comEventCallbackInterface;
    IComEventCallbackListener comEventCallbackListener;
    Guid.REFIID listenedToRiid;
    public DispatchListener dispatchListener;
    Map<OaIdl.DISPID, Method> dsipIdMap;
    
    public CallbackProxy(final ObjectFactory factory, final Class<?> comEventCallbackInterface, final IComEventCallbackListener comEventCallbackListener) {
        this.factory = factory;
        this.comEventCallbackInterface = comEventCallbackInterface;
        this.comEventCallbackListener = comEventCallbackListener;
        this.listenedToRiid = this.createRIID(comEventCallbackInterface);
        this.dsipIdMap = this.createDispIdMap(comEventCallbackInterface);
        this.dispatchListener = new DispatchListener(this);
    }
    
    Guid.REFIID createRIID(final Class<?> comEventCallbackInterface) {
        final ComInterface comInterfaceAnnotation = comEventCallbackInterface.getAnnotation(ComInterface.class);
        if (null == comInterfaceAnnotation) {
            throw new COMException("advise: Interface must define a value for either iid via the ComInterface annotation");
        }
        final String iidStr = comInterfaceAnnotation.iid();
        if (null == iidStr || iidStr.isEmpty()) {
            throw new COMException("ComInterface must define a value for iid");
        }
        return new Guid.REFIID(new Guid.IID(iidStr).getPointer());
    }
    
    Map<OaIdl.DISPID, Method> createDispIdMap(final Class<?> comEventCallbackInterface) {
        final Map<OaIdl.DISPID, Method> map = new HashMap<OaIdl.DISPID, Method>();
        for (final Method meth : comEventCallbackInterface.getMethods()) {
            final ComEventCallback callbackAnnotation = meth.getAnnotation(ComEventCallback.class);
            final ComMethod methodAnnotation = meth.getAnnotation(ComMethod.class);
            if (methodAnnotation != null) {
                int dispId = methodAnnotation.dispId();
                if (-1 == dispId) {
                    dispId = this.fetchDispIdFromName(callbackAnnotation);
                }
                if (dispId == -1) {
                    this.comEventCallbackListener.errorReceivingCallbackEvent("DISPID for " + meth.getName() + " not found", null);
                }
                map.put(new OaIdl.DISPID(dispId), meth);
            }
            else if (null != callbackAnnotation) {
                int dispId = callbackAnnotation.dispid();
                if (-1 == dispId) {
                    dispId = this.fetchDispIdFromName(callbackAnnotation);
                }
                if (dispId == -1) {
                    this.comEventCallbackListener.errorReceivingCallbackEvent("DISPID for " + meth.getName() + " not found", null);
                }
                map.put(new OaIdl.DISPID(dispId), meth);
            }
        }
        return map;
    }
    
    int fetchDispIdFromName(final ComEventCallback annotation) {
        return -1;
    }
    
    void invokeOnThread(final OaIdl.DISPID dispIdMember, final Guid.REFIID riid, final WinDef.LCID lcid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams) {
        final Variant.VARIANT[] arguments = pDispParams.getArgs();
        final Method eventMethod = this.dsipIdMap.get(dispIdMember);
        if (eventMethod == null) {
            this.comEventCallbackListener.errorReceivingCallbackEvent("No method found with dispId = " + dispIdMember, null);
            return;
        }
        final OaIdl.DISPID[] positionMap = pDispParams.getRgdispidNamedArgs();
        final Class<?>[] paramTypes = eventMethod.getParameterTypes();
        final Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < params.length && arguments.length - positionMap.length - i > 0; ++i) {
            final Class targetClass = paramTypes[i];
            final Variant.VARIANT varg = arguments[arguments.length - i - 1];
            params[i] = Convert.toJavaObject(varg, targetClass, this.factory, true, false);
        }
        for (int i = 0; i < positionMap.length; ++i) {
            final int targetPosition = positionMap[i].intValue();
            if (targetPosition < params.length) {
                final Class targetClass2 = paramTypes[targetPosition];
                final Variant.VARIANT varg2 = arguments[i];
                params[targetPosition] = Convert.toJavaObject(varg2, targetClass2, this.factory, true, false);
            }
        }
        for (int i = 0; i < params.length; ++i) {
            if (params[i] == null && paramTypes[i].isPrimitive()) {
                if (paramTypes[i].equals(Boolean.TYPE)) {
                    params[i] = CallbackProxy.DEFAULT_BOOLEAN;
                }
                else if (paramTypes[i].equals(Byte.TYPE)) {
                    params[i] = CallbackProxy.DEFAULT_BYTE;
                }
                else if (paramTypes[i].equals(Short.TYPE)) {
                    params[i] = CallbackProxy.DEFAULT_SHORT;
                }
                else if (paramTypes[i].equals(Integer.TYPE)) {
                    params[i] = CallbackProxy.DEFAULT_INT;
                }
                else if (paramTypes[i].equals(Long.TYPE)) {
                    params[i] = CallbackProxy.DEFAULT_LONG;
                }
                else if (paramTypes[i].equals(Float.TYPE)) {
                    params[i] = CallbackProxy.DEFAULT_FLOAT;
                }
                else {
                    if (!paramTypes[i].equals(Double.TYPE)) {
                        throw new IllegalArgumentException("Class type " + paramTypes[i].getName() + " not mapped to primitive default value.");
                    }
                    params[i] = CallbackProxy.DEFAULT_DOUBLE;
                }
            }
        }
        try {
            eventMethod.invoke(this.comEventCallbackListener, params);
        }
        catch (Exception e) {
            final List<String> decodedClassNames = new ArrayList<String>(params.length);
            for (final Object o : params) {
                if (o == null) {
                    decodedClassNames.add("NULL");
                }
                else {
                    decodedClassNames.add(o.getClass().getName());
                }
            }
            this.comEventCallbackListener.errorReceivingCallbackEvent("Exception invoking method " + eventMethod + " supplied: " + decodedClassNames.toString(), e);
        }
    }
    
    @Override
    public Pointer getPointer() {
        return this.dispatchListener.getPointer();
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfoCount(final WinDef.UINTByReference pctinfo) {
        return new WinNT.HRESULT(-2147467263);
    }
    
    @Override
    public WinNT.HRESULT GetTypeInfo(final WinDef.UINT iTInfo, final WinDef.LCID lcid, final PointerByReference ppTInfo) {
        return new WinNT.HRESULT(-2147467263);
    }
    
    @Override
    public WinNT.HRESULT GetIDsOfNames(final Guid.REFIID riid, final WString[] rgszNames, final int cNames, final WinDef.LCID lcid, final OaIdl.DISPIDByReference rgDispId) {
        return new WinNT.HRESULT(-2147467263);
    }
    
    @Override
    public WinNT.HRESULT Invoke(final OaIdl.DISPID dispIdMember, final Guid.REFIID riid, final WinDef.LCID lcid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams, final Variant.VARIANT.ByReference pVarResult, final OaIdl.EXCEPINFO.ByReference pExcepInfo, final IntByReference puArgErr) {
        assert COMUtils.comIsInitialized() : "Assumption about COM threading broken.";
        this.invokeOnThread(dispIdMember, riid, lcid, wFlags, pDispParams);
        return WinError.S_OK;
    }
    
    @Override
    public WinNT.HRESULT QueryInterface(final Guid.REFIID refid, final PointerByReference ppvObject) {
        if (null == ppvObject) {
            return new WinNT.HRESULT(-2147467261);
        }
        if (refid.equals(this.listenedToRiid)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        if (refid.getValue().equals(Unknown.IID_IUNKNOWN)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        if (refid.getValue().equals(Dispatch.IID_IDISPATCH)) {
            ppvObject.setValue(this.getPointer());
            return WinError.S_OK;
        }
        return new WinNT.HRESULT(-2147467262);
    }
    
    @Override
    public int AddRef() {
        return 0;
    }
    
    @Override
    public int Release() {
        return 0;
    }
}
