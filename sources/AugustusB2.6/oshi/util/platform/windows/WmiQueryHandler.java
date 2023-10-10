// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.windows;

import oshi.util.GlobalConfig;
import org.slf4j.LoggerFactory;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Ole32;
import java.util.concurrent.TimeoutException;
import com.sun.jna.platform.win32.COM.COMException;
import java.util.Objects;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class WmiQueryHandler
{
    private static final Logger LOG;
    private static int globalTimeout;
    protected int wmiTimeout;
    protected final Set<String> failedWmiClassNames;
    private int comThreading;
    private boolean securityInitialized;
    private static final Class<?>[] EMPTY_CLASS_ARRAY;
    private static final Object[] EMPTY_OBJECT_ARRAY;
    private static Class<? extends WmiQueryHandler> customClass;
    
    public WmiQueryHandler() {
        this.wmiTimeout = WmiQueryHandler.globalTimeout;
        this.failedWmiClassNames = new HashSet<String>();
        this.comThreading = 0;
        this.securityInitialized = false;
    }
    
    public static synchronized WmiQueryHandler createInstance() {
        if (WmiQueryHandler.customClass == null) {
            return new WmiQueryHandler();
        }
        try {
            return (WmiQueryHandler)WmiQueryHandler.customClass.getConstructor(WmiQueryHandler.EMPTY_CLASS_ARRAY).newInstance(WmiQueryHandler.EMPTY_OBJECT_ARRAY);
        }
        catch (NoSuchMethodException | SecurityException ex3) {
            final Exception ex;
            final Exception e = ex;
            WmiQueryHandler.LOG.error("Failed to find or access a no-arg constructor for {}", WmiQueryHandler.customClass);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex4) {
            final Exception ex2;
            final Exception e = ex2;
            WmiQueryHandler.LOG.error("Failed to create a new instance of {}", WmiQueryHandler.customClass);
        }
        return null;
    }
    
    public static synchronized void setInstanceClass(final Class<? extends WmiQueryHandler> instanceClass) {
        WmiQueryHandler.customClass = instanceClass;
    }
    
    public <T extends Enum<T>> WbemcliUtil.WmiResult<T> queryWMI(final WbemcliUtil.WmiQuery<T> query) {
        return this.queryWMI(query, true);
    }
    
    public <T extends Enum<T>> WbemcliUtil.WmiResult<T> queryWMI(final WbemcliUtil.WmiQuery<T> query, final boolean initCom) {
        final WbemcliUtil instance = WbemcliUtil.INSTANCE;
        Objects.requireNonNull(instance);
        WbemcliUtil.WmiResult<T> result = new WbemcliUtil.WmiResult<T>((Class<Enum>)query.getPropertyEnum());
        if (this.failedWmiClassNames.contains(query.getWmiClassName())) {
            return result;
        }
        boolean comInit = false;
        try {
            if (initCom) {
                comInit = this.initCOM();
            }
            result = query.execute(this.wmiTimeout);
        }
        catch (COMException e) {
            if (!"ROOT\\OpenHardwareMonitor".equals(query.getNameSpace())) {
                final int hresult = (e.getHresult() == null) ? -1 : e.getHresult().intValue();
                switch (hresult) {
                    case -2147217394: {
                        WmiQueryHandler.LOG.warn("COM exception: Invalid Namespace {}", query.getNameSpace());
                        break;
                    }
                    case -2147217392: {
                        WmiQueryHandler.LOG.warn("COM exception: Invalid Class {}", query.getWmiClassName());
                        break;
                    }
                    case -2147217385: {
                        WmiQueryHandler.LOG.warn("COM exception: Invalid Query: {}", WmiUtil.queryToString(query));
                        break;
                    }
                    default: {
                        this.handleComException(query, e);
                        break;
                    }
                }
                this.failedWmiClassNames.add(query.getWmiClassName());
            }
        }
        catch (TimeoutException e2) {
            WmiQueryHandler.LOG.warn("WMI query timed out after {} ms: {}", (Object)this.wmiTimeout, WmiUtil.queryToString(query));
        }
        finally {
            if (comInit) {
                this.unInitCOM();
            }
        }
        return result;
    }
    
    protected void handleComException(final WbemcliUtil.WmiQuery<?> query, final COMException ex) {
        WmiQueryHandler.LOG.warn("COM exception querying {}, which might not be on your system. Will not attempt to query it again. Error was {}: {}", query.getWmiClassName(), (ex.getHresult() == null) ? null : Integer.valueOf(ex.getHresult().intValue()), ex.getMessage());
    }
    
    public boolean initCOM() {
        boolean comInit = false;
        comInit = this.initCOM(this.getComThreading());
        if (!comInit) {
            comInit = this.initCOM(this.switchComThreading());
        }
        if (comInit && !this.isSecurityInitialized()) {
            final WinNT.HRESULT hres = Ole32.INSTANCE.CoInitializeSecurity(null, -1, null, null, 0, 3, null, 0, null);
            if (COMUtils.FAILED(hres) && hres.intValue() != -2147417831) {
                Ole32.INSTANCE.CoUninitialize();
                throw new COMException("Failed to initialize security.", hres);
            }
            this.securityInitialized = true;
        }
        return comInit;
    }
    
    protected boolean initCOM(final int coInitThreading) {
        final WinNT.HRESULT hres = Ole32.INSTANCE.CoInitializeEx(null, coInitThreading);
        switch (hres.intValue()) {
            case 0:
            case 1: {
                return true;
            }
            case -2147417850: {
                return false;
            }
            default: {
                throw new COMException("Failed to initialize COM library.", hres);
            }
        }
    }
    
    public void unInitCOM() {
        Ole32.INSTANCE.CoUninitialize();
    }
    
    public int getComThreading() {
        return this.comThreading;
    }
    
    public int switchComThreading() {
        if (this.comThreading == 2) {
            this.comThreading = 0;
        }
        else {
            this.comThreading = 2;
        }
        return this.comThreading;
    }
    
    public boolean isSecurityInitialized() {
        return this.securityInitialized;
    }
    
    public int getWmiTimeout() {
        return this.wmiTimeout;
    }
    
    public void setWmiTimeout(final int wmiTimeout) {
        this.wmiTimeout = wmiTimeout;
    }
    
    static {
        LOG = LoggerFactory.getLogger(WmiQueryHandler.class);
        WmiQueryHandler.globalTimeout = GlobalConfig.get("oshi.util.wmi.timeout", -1);
        if (WmiQueryHandler.globalTimeout == 0 || WmiQueryHandler.globalTimeout < -1) {
            throw new GlobalConfig.PropertyException("oshi.util.wmi.timeout");
        }
        EMPTY_CLASS_ARRAY = new Class[0];
        EMPTY_OBJECT_ARRAY = new Object[0];
        WmiQueryHandler.customClass = null;
    }
}
