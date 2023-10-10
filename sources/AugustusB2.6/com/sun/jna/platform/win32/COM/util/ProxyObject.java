// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.COM.IUnknownCallback;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import com.sun.jna.platform.win32.COM.ConnectionPointContainer;
import com.sun.jna.platform.win32.COM.ConnectionPoint;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.internal.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.Dispatch;
import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.COM.IUnknown;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.COMUtils;
import java.lang.reflect.InvocationHandler;

public class ProxyObject implements InvocationHandler, IDispatch, IRawDispatchHandle, IConnectionPoint
{
    private long unknownId;
    private final Class<?> theInterface;
    private final ObjectFactory factory;
    private final com.sun.jna.platform.win32.COM.IDispatch rawDispatch;
    
    public ProxyObject(final Class<?> theInterface, final com.sun.jna.platform.win32.COM.IDispatch rawDispatch, final ObjectFactory factory) {
        this.unknownId = -1L;
        this.rawDispatch = rawDispatch;
        this.theInterface = theInterface;
        this.factory = factory;
        final int n = this.rawDispatch.AddRef();
        this.getUnknownId();
        factory.register(this);
    }
    
    private long getUnknownId() {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        if (-1L == this.unknownId) {
            try {
                final PointerByReference ppvObject = new PointerByReference();
                final Thread current = Thread.currentThread();
                final String tn = current.getName();
                final Guid.IID iid = com.sun.jna.platform.win32.COM.IUnknown.IID_IUNKNOWN;
                final WinNT.HRESULT hr = this.getRawDispatch().QueryInterface(new Guid.REFIID(iid), ppvObject);
                if (!WinNT.S_OK.equals(hr)) {
                    final String formatMessageFromHR = Kernel32Util.formatMessage(hr);
                    throw new COMException("getUnknownId: " + formatMessageFromHR, hr);
                }
                final Dispatch dispatch = new Dispatch(ppvObject.getValue());
                this.unknownId = Pointer.nativeValue(dispatch.getPointer());
                dispatch.Release();
            }
            catch (RuntimeException e) {
                if (e instanceof COMException) {
                    throw e;
                }
                throw new COMException("Error occured when trying get Unknown Id ", e);
            }
        }
        return this.unknownId;
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.dispose();
        super.finalize();
    }
    
    public synchronized void dispose() {
        if (((Dispatch)this.rawDispatch).getPointer() != Pointer.NULL) {
            this.rawDispatch.Release();
            ((Dispatch)this.rawDispatch).setPointer(Pointer.NULL);
            this.factory.unregister(this);
        }
    }
    
    @Override
    public com.sun.jna.platform.win32.COM.IDispatch getRawDispatch() {
        return this.rawDispatch;
    }
    
    @Override
    public boolean equals(final Object arg) {
        if (null == arg) {
            return false;
        }
        if (arg instanceof ProxyObject) {
            final ProxyObject other = (ProxyObject)arg;
            return this.getUnknownId() == other.getUnknownId();
        }
        if (Proxy.isProxyClass(arg.getClass())) {
            final InvocationHandler handler = Proxy.getInvocationHandler(arg);
            if (handler instanceof ProxyObject) {
                try {
                    final ProxyObject other2 = (ProxyObject)handler;
                    return this.getUnknownId() == other2.getUnknownId();
                }
                catch (Exception e) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long id = this.getUnknownId();
        return (int)(id >>> 32 & -1L) + (int)(id & -1L);
    }
    
    @Override
    public String toString() {
        return this.theInterface.getName() + "{unk=" + this.hashCode() + "}";
    }
    
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final boolean declaredAsInterface = method.getAnnotation(ComMethod.class) != null || method.getAnnotation(ComProperty.class) != null;
        Label_0105: {
            if (!declaredAsInterface) {
                if (!method.getDeclaringClass().equals(Object.class) && !method.getDeclaringClass().equals(IRawDispatchHandle.class) && !method.getDeclaringClass().equals(IUnknown.class) && !method.getDeclaringClass().equals(IDispatch.class)) {
                    if (!method.getDeclaringClass().equals(IConnectionPoint.class)) {
                        break Label_0105;
                    }
                }
                try {
                    return method.invoke(this, args);
                }
                catch (InvocationTargetException ex) {
                    throw ex.getCause();
                }
            }
        }
        if (!declaredAsInterface && ReflectionUtils.isDefault(method)) {
            final Object methodHandle = ReflectionUtils.getMethodHandle(method);
            return ReflectionUtils.invokeDefaultMethod(proxy, methodHandle, args);
        }
        final Class<?> returnType = method.getReturnType();
        final boolean isVoid = Void.TYPE.equals(returnType);
        final ComProperty prop = method.getAnnotation(ComProperty.class);
        if (null != prop) {
            final int dispId = prop.dispId();
            final Object[] fullLengthArgs = this.unfoldWhenVarargs(method, args);
            if (isVoid) {
                if (dispId != -1) {
                    this.setProperty(new OaIdl.DISPID(dispId), fullLengthArgs);
                    return null;
                }
                final String propName = this.getMutatorName(method, prop);
                this.setProperty(propName, fullLengthArgs);
                return null;
            }
            else {
                if (dispId != -1) {
                    return this.getProperty(returnType, new OaIdl.DISPID(dispId), args);
                }
                final String propName = this.getAccessorName(method, prop);
                return this.getProperty(returnType, propName, args);
            }
        }
        else {
            final ComMethod meth = method.getAnnotation(ComMethod.class);
            if (null == meth) {
                return null;
            }
            final Object[] fullLengthArgs = this.unfoldWhenVarargs(method, args);
            final int dispId2 = meth.dispId();
            if (dispId2 != -1) {
                return this.invokeMethod(returnType, new OaIdl.DISPID(dispId2), fullLengthArgs);
            }
            final String methName = this.getMethodName(method, meth);
            return this.invokeMethod(returnType, methName, fullLengthArgs);
        }
    }
    
    private ConnectionPoint fetchRawConnectionPoint(final Guid.IID iid) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final IConnectionPointContainer cpc = this.queryInterface(IConnectionPointContainer.class);
        final Dispatch rawCpcDispatch = (Dispatch)cpc.getRawDispatch();
        final ConnectionPointContainer rawCpc = new ConnectionPointContainer(rawCpcDispatch.getPointer());
        final Guid.REFIID adviseRiid = new Guid.REFIID(iid.getPointer());
        final PointerByReference ppCp = new PointerByReference();
        final WinNT.HRESULT hr = rawCpc.FindConnectionPoint(adviseRiid, ppCp);
        COMUtils.checkRC(hr);
        final ConnectionPoint rawCp = new ConnectionPoint(ppCp.getValue());
        return rawCp;
    }
    
    @Override
    public IComEventCallbackCookie advise(final Class<?> comEventCallbackInterface, final IComEventCallbackListener comEventCallbackListener) throws COMException {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        try {
            final ComInterface comInterfaceAnnotation = comEventCallbackInterface.getAnnotation(ComInterface.class);
            if (null == comInterfaceAnnotation) {
                throw new COMException("advise: Interface must define a value for either iid via the ComInterface annotation");
            }
            final Guid.IID iid = this.getIID(comInterfaceAnnotation);
            final ConnectionPoint rawCp = this.fetchRawConnectionPoint(iid);
            final IDispatchCallback rawListener = this.factory.createDispatchCallback(comEventCallbackInterface, comEventCallbackListener);
            comEventCallbackListener.setDispatchCallbackListener(rawListener);
            final WinDef.DWORDByReference pdwCookie = new WinDef.DWORDByReference();
            final WinNT.HRESULT hr = rawCp.Advise(rawListener, pdwCookie);
            final int n = rawCp.Release();
            COMUtils.checkRC(hr);
            return new ComEventCallbackCookie(pdwCookie.getValue());
        }
        catch (RuntimeException e) {
            if (e instanceof COMException) {
                throw e;
            }
            throw new COMException("Error occured in advise when trying to connect the listener " + comEventCallbackListener, e);
        }
    }
    
    @Override
    public void unadvise(final Class<?> comEventCallbackInterface, final IComEventCallbackCookie cookie) throws COMException {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        try {
            final ComInterface comInterfaceAnnotation = comEventCallbackInterface.getAnnotation(ComInterface.class);
            if (null == comInterfaceAnnotation) {
                throw new COMException("unadvise: Interface must define a value for iid via the ComInterface annotation");
            }
            final Guid.IID iid = this.getIID(comInterfaceAnnotation);
            final ConnectionPoint rawCp = this.fetchRawConnectionPoint(iid);
            final WinNT.HRESULT hr = rawCp.Unadvise(((ComEventCallbackCookie)cookie).getValue());
            rawCp.Release();
            COMUtils.checkRC(hr);
        }
        catch (RuntimeException e) {
            if (e instanceof COMException) {
                throw e;
            }
            throw new COMException("Error occured in unadvise when trying to disconnect the listener from " + this, e);
        }
    }
    
    @Override
    public <T> void setProperty(final String name, final T value) {
        final OaIdl.DISPID dispID = this.resolveDispId(this.getRawDispatch(), name);
        this.setProperty(dispID, value);
    }
    
    @Override
    public <T> void setProperty(final OaIdl.DISPID dispId, final T value) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final Variant.VARIANT v = Convert.toVariant(value);
        final WinNT.HRESULT hr = this.oleMethod(4, null, this.getRawDispatch(), dispId, v);
        Convert.free(v, value);
        COMUtils.checkRC(hr);
    }
    
    private void setProperty(final String name, final Object... args) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final OaIdl.DISPID dispID = this.resolveDispId(this.getRawDispatch(), name);
        this.setProperty(dispID, args);
    }
    
    private void setProperty(final OaIdl.DISPID dispID, final Object... args) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        Variant.VARIANT[] vargs;
        if (null == args) {
            vargs = new Variant.VARIANT[0];
        }
        else {
            vargs = new Variant.VARIANT[args.length];
        }
        for (int i = 0; i < vargs.length; ++i) {
            vargs[i] = Convert.toVariant(args[i]);
        }
        final WinNT.HRESULT hr = this.oleMethod(4, null, this.getRawDispatch(), dispID, vargs);
        for (int j = 0; j < vargs.length; ++j) {
            Convert.free(vargs[j], args[j]);
        }
        COMUtils.checkRC(hr);
    }
    
    @Override
    public <T> T getProperty(final Class<T> returnType, final String name, final Object... args) {
        final OaIdl.DISPID dispID = this.resolveDispId(this.getRawDispatch(), name);
        return this.getProperty(returnType, dispID, args);
    }
    
    @Override
    public <T> T getProperty(final Class<T> returnType, final OaIdl.DISPID dispID, final Object... args) {
        Variant.VARIANT[] vargs;
        if (null == args) {
            vargs = new Variant.VARIANT[0];
        }
        else {
            vargs = new Variant.VARIANT[args.length];
        }
        for (int i = 0; i < vargs.length; ++i) {
            vargs[i] = Convert.toVariant(args[i]);
        }
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        final WinNT.HRESULT hr = this.oleMethod(2, result, this.getRawDispatch(), dispID, vargs);
        for (int j = 0; j < vargs.length; ++j) {
            Convert.free(vargs[j], args[j]);
        }
        COMUtils.checkRC(hr);
        return (T)Convert.toJavaObject(result, returnType, this.factory, false, true);
    }
    
    @Override
    public <T> T invokeMethod(final Class<T> returnType, final String name, final Object... args) {
        final OaIdl.DISPID dispID = this.resolveDispId(this.getRawDispatch(), name);
        return this.invokeMethod(returnType, dispID, args);
    }
    
    @Override
    public <T> T invokeMethod(final Class<T> returnType, final OaIdl.DISPID dispID, final Object... args) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        Variant.VARIANT[] vargs;
        if (null == args) {
            vargs = new Variant.VARIANT[0];
        }
        else {
            vargs = new Variant.VARIANT[args.length];
        }
        for (int i = 0; i < vargs.length; ++i) {
            vargs[i] = Convert.toVariant(args[i]);
        }
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        final WinNT.HRESULT hr = this.oleMethod(1, result, this.getRawDispatch(), dispID, vargs);
        for (int j = 0; j < vargs.length; ++j) {
            Convert.free(vargs[j], args[j]);
        }
        COMUtils.checkRC(hr);
        return (T)Convert.toJavaObject(result, returnType, this.factory, false, true);
    }
    
    private Object[] unfoldWhenVarargs(final Method method, final Object[] argParams) {
        if (null == argParams) {
            return null;
        }
        if (argParams.length == 0 || !method.isVarArgs() || !(argParams[argParams.length - 1] instanceof Object[])) {
            return argParams;
        }
        final Object[] varargs = (Object[])argParams[argParams.length - 1];
        final Object[] args = new Object[argParams.length - 1 + varargs.length];
        System.arraycopy(argParams, 0, args, 0, argParams.length - 1);
        System.arraycopy(varargs, 0, args, argParams.length - 1, varargs.length);
        return args;
    }
    
    @Override
    public <T> T queryInterface(final Class<T> comInterface) throws COMException {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        try {
            final ComInterface comInterfaceAnnotation = comInterface.getAnnotation(ComInterface.class);
            if (null == comInterfaceAnnotation) {
                throw new COMException("queryInterface: Interface must define a value for iid via the ComInterface annotation");
            }
            final Guid.IID iid = this.getIID(comInterfaceAnnotation);
            final PointerByReference ppvObject = new PointerByReference();
            final WinNT.HRESULT hr = this.getRawDispatch().QueryInterface(new Guid.REFIID(iid), ppvObject);
            if (WinNT.S_OK.equals(hr)) {
                final Dispatch dispatch = new Dispatch(ppvObject.getValue());
                final T t = this.factory.createProxy(comInterface, dispatch);
                final int n = dispatch.Release();
                return t;
            }
            final String formatMessageFromHR = Kernel32Util.formatMessage(hr);
            throw new COMException("queryInterface: " + formatMessageFromHR, hr);
        }
        catch (RuntimeException e) {
            if (e instanceof COMException) {
                throw e;
            }
            throw new COMException("Error occured when trying to query for interface " + comInterface.getName(), e);
        }
    }
    
    private Guid.IID getIID(final ComInterface annotation) {
        final String iidStr = annotation.iid();
        if (null != iidStr && !iidStr.isEmpty()) {
            return new Guid.IID(iidStr);
        }
        throw new COMException("ComInterface must define a value for iid");
    }
    
    private String getAccessorName(final Method method, final ComProperty prop) {
        if (!prop.name().isEmpty()) {
            return prop.name();
        }
        final String methName = method.getName();
        if (methName.startsWith("get")) {
            return methName.replaceFirst("get", "");
        }
        throw new RuntimeException("Property Accessor name must start with 'get', or set the anotation 'name' value");
    }
    
    private String getMutatorName(final Method method, final ComProperty prop) {
        if (!prop.name().isEmpty()) {
            return prop.name();
        }
        final String methName = method.getName();
        if (methName.startsWith("set")) {
            return methName.replaceFirst("set", "");
        }
        throw new RuntimeException("Property Mutator name must start with 'set', or set the anotation 'name' value");
    }
    
    private String getMethodName(final Method method, final ComMethod meth) {
        if (meth.name().isEmpty()) {
            final String methName = method.getName();
            return methName;
        }
        return meth.name();
    }
    
    protected OaIdl.DISPID resolveDispId(final String name) {
        return this.resolveDispId(this.getRawDispatch(), name);
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final String name, final Variant.VARIANT pArg) throws COMException {
        return this.oleMethod(nType, pvResult, name, new Variant.VARIANT[] { pArg });
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final OaIdl.DISPID dispId, final Variant.VARIANT pArg) throws COMException {
        return this.oleMethod(nType, pvResult, dispId, new Variant.VARIANT[] { pArg });
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final String name) throws COMException {
        return this.oleMethod(nType, pvResult, name, (Variant.VARIANT[])null);
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final OaIdl.DISPID dispId) throws COMException {
        return this.oleMethod(nType, pvResult, dispId, (Variant.VARIANT[])null);
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final String name, final Variant.VARIANT[] pArgs) throws COMException {
        return this.oleMethod(nType, pvResult, this.resolveDispId(name), pArgs);
    }
    
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final OaIdl.DISPID dispId, final Variant.VARIANT[] pArgs) throws COMException {
        return this.oleMethod(nType, pvResult, this.getRawDispatch(), dispId, pArgs);
    }
    
    @Deprecated
    protected OaIdl.DISPID resolveDispId(final com.sun.jna.platform.win32.COM.IDispatch pDisp, final String name) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        if (pDisp == null) {
            throw new COMException("pDisp (IDispatch) parameter is null!");
        }
        final WString[] ptName = { new WString(name) };
        final OaIdl.DISPIDByReference pdispID = new OaIdl.DISPIDByReference();
        final WinNT.HRESULT hr = pDisp.GetIDsOfNames(new Guid.REFIID(Guid.IID_NULL), ptName, 1, this.factory.getLCID(), pdispID);
        COMUtils.checkRC(hr);
        return pdispID.getValue();
    }
    
    @Deprecated
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final String name, final Variant.VARIANT pArg) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, name, new Variant.VARIANT[] { pArg });
    }
    
    @Deprecated
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final OaIdl.DISPID dispId, final Variant.VARIANT pArg) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, dispId, new Variant.VARIANT[] { pArg });
    }
    
    @Deprecated
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final String name) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, name, (Variant.VARIANT[])null);
    }
    
    @Deprecated
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final OaIdl.DISPID dispId) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, dispId, (Variant.VARIANT[])null);
    }
    
    @Deprecated
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final String name, final Variant.VARIANT[] pArgs) throws COMException {
        return this.oleMethod(nType, pvResult, pDisp, this.resolveDispId(pDisp, name), pArgs);
    }
    
    @Deprecated
    protected WinNT.HRESULT oleMethod(final int nType, final Variant.VARIANT.ByReference pvResult, final com.sun.jna.platform.win32.COM.IDispatch pDisp, final OaIdl.DISPID dispId, final Variant.VARIANT[] pArgs) throws COMException {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        if (pDisp == null) {
            throw new COMException("pDisp (IDispatch) parameter is null!");
        }
        int _argsLen = 0;
        Variant.VARIANT[] _args = null;
        final OleAuto.DISPPARAMS.ByReference dp = new OleAuto.DISPPARAMS.ByReference();
        final OaIdl.EXCEPINFO.ByReference pExcepInfo = new OaIdl.EXCEPINFO.ByReference();
        final IntByReference puArgErr = new IntByReference();
        if (pArgs != null && pArgs.length > 0) {
            _argsLen = pArgs.length;
            _args = new Variant.VARIANT[_argsLen];
            int revCount = _argsLen;
            for (int i = 0; i < _argsLen; ++i) {
                _args[i] = pArgs[--revCount];
            }
        }
        if (nType == 4) {
            dp.setRgdispidNamedArgs(new OaIdl.DISPID[] { OaIdl.DISPID_PROPERTYPUT });
        }
        int finalNType;
        if (nType == 1 || nType == 2) {
            finalNType = 3;
        }
        else {
            finalNType = nType;
        }
        if (_argsLen > 0) {
            dp.setArgs(_args);
            dp.write();
        }
        final WinNT.HRESULT hr = pDisp.Invoke(dispId, new Guid.REFIID(Guid.IID_NULL), this.factory.getLCID(), new WinDef.WORD((long)finalNType), dp, pvResult, pExcepInfo, puArgErr);
        COMUtils.checkRC(hr, pExcepInfo, puArgErr);
        return hr;
    }
}
