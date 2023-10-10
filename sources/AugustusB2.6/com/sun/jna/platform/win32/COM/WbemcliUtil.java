// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.WString;
import java.util.HashMap;
import com.sun.jna.ptr.IntByReference;
import java.util.concurrent.TimeoutException;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.Ole32;

public class WbemcliUtil
{
    public static final WbemcliUtil INSTANCE;
    public static final String DEFAULT_NAMESPACE = "ROOT\\CIMV2";
    
    public static boolean hasNamespace(final String namespace) {
        String ns = namespace;
        if (namespace.toUpperCase().startsWith("ROOT\\")) {
            ns = namespace.substring(5);
        }
        final WmiQuery<NamespaceProperty> namespaceQuery = new WmiQuery<NamespaceProperty>("ROOT", "__NAMESPACE", NamespaceProperty.class);
        final WmiResult<NamespaceProperty> namespaces = namespaceQuery.execute();
        for (int i = 0; i < namespaces.getResultCount(); ++i) {
            if (ns.equalsIgnoreCase((String)namespaces.getValue(NamespaceProperty.NAME, i))) {
                return true;
            }
        }
        return false;
    }
    
    public static Wbemcli.IWbemServices connectServer(final String namespace) {
        final Wbemcli.IWbemLocator loc = Wbemcli.IWbemLocator.create();
        if (loc == null) {
            throw new COMException("Failed to create WbemLocator object.");
        }
        final Wbemcli.IWbemServices services = loc.ConnectServer(namespace, null, null, null, 0, null, null);
        loc.Release();
        final WinNT.HRESULT hres = Ole32.INSTANCE.CoSetProxyBlanket(services, 10, 0, null, 3, 3, null, 0);
        if (COMUtils.FAILED(hres)) {
            services.Release();
            throw new COMException("Could not set proxy blanket.", hres);
        }
        return services;
    }
    
    static {
        INSTANCE = new WbemcliUtil();
    }
    
    private enum NamespaceProperty
    {
        NAME;
    }
    
    public static class WmiQuery<T extends Enum<T>>
    {
        private String nameSpace;
        private String wmiClassName;
        private Class<T> propertyEnum;
        
        public WmiQuery(final String nameSpace, final String wmiClassName, final Class<T> propertyEnum) {
            this.nameSpace = nameSpace;
            this.wmiClassName = wmiClassName;
            this.propertyEnum = propertyEnum;
        }
        
        public WmiQuery(final String wmiClassName, final Class<T> propertyEnum) {
            this("ROOT\\CIMV2", wmiClassName, propertyEnum);
        }
        
        public Class<T> getPropertyEnum() {
            return this.propertyEnum;
        }
        
        public String getNameSpace() {
            return this.nameSpace;
        }
        
        public void setNameSpace(final String nameSpace) {
            this.nameSpace = nameSpace;
        }
        
        public String getWmiClassName() {
            return this.wmiClassName;
        }
        
        public void setWmiClassName(final String wmiClassName) {
            this.wmiClassName = wmiClassName;
        }
        
        public WmiResult<T> execute() {
            try {
                return this.execute(-1);
            }
            catch (TimeoutException e) {
                throw new COMException("Got a WMI timeout when infinite wait was specified. This should never happen.");
            }
        }
        
        public WmiResult<T> execute(final int timeout) throws TimeoutException {
            if (this.getPropertyEnum().getEnumConstants().length < 1) {
                throw new IllegalArgumentException("The query's property enum has no values.");
            }
            final Wbemcli.IWbemServices svc = WbemcliUtil.connectServer(this.getNameSpace());
            try {
                final Wbemcli.IEnumWbemClassObject enumerator = selectProperties(svc, (WmiQuery<Enum>)this);
                try {
                    return enumerateProperties(enumerator, this.getPropertyEnum(), timeout);
                }
                finally {
                    enumerator.Release();
                }
            }
            finally {
                svc.Release();
            }
        }
        
        private static <T extends Enum<T>> Wbemcli.IEnumWbemClassObject selectProperties(final Wbemcli.IWbemServices svc, final WmiQuery<T> query) {
            final T[] props = query.getPropertyEnum().getEnumConstants();
            final StringBuilder sb = new StringBuilder("SELECT ");
            sb.append(props[0].name());
            for (int i = 1; i < props.length; ++i) {
                sb.append(',').append(props[i].name());
            }
            sb.append(" FROM ").append(query.getWmiClassName());
            return svc.ExecQuery("WQL", sb.toString().replaceAll("\\\\", "\\\\\\\\"), 48, null);
        }
        
        private static <T extends Enum<T>> WmiResult<T> enumerateProperties(final Wbemcli.IEnumWbemClassObject enumerator, final Class<T> propertyEnum, final int timeout) throws TimeoutException {
            final WmiResult<T> values = new WmiResult<T>(propertyEnum);
            final Pointer[] pclsObj = { null };
            final IntByReference uReturn = new IntByReference(0);
            final Map<T, WString> wstrMap = new HashMap<T, WString>();
            WinNT.HRESULT hres = null;
            for (final T property : propertyEnum.getEnumConstants()) {
                wstrMap.put(property, new WString(property.name()));
            }
            while (enumerator.getPointer() != Pointer.NULL) {
                hres = enumerator.Next(timeout, pclsObj.length, pclsObj, uReturn);
                if (hres.intValue() == 1) {
                    break;
                }
                if (hres.intValue() == 262149) {
                    break;
                }
                if (hres.intValue() == 262148) {
                    throw new TimeoutException("No results after " + timeout + " ms.");
                }
                if (COMUtils.FAILED(hres)) {
                    throw new COMException("Failed to enumerate results.", hres);
                }
                final Variant.VARIANT.ByReference pVal = new Variant.VARIANT.ByReference();
                final IntByReference pType = new IntByReference();
                final Wbemcli.IWbemClassObject clsObj = new Wbemcli.IWbemClassObject(pclsObj[0]);
                for (final T property2 : propertyEnum.getEnumConstants()) {
                    clsObj.Get(wstrMap.get(property2), 0, pVal, pType, null);
                    final int vtType = ((pVal.getValue() == null) ? Integer.valueOf(1) : pVal.getVarType()).intValue();
                    final int cimType = pType.getValue();
                    switch (vtType) {
                        case 8: {
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, pVal.stringValue());
                            break;
                        }
                        case 3: {
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, pVal.intValue());
                            break;
                        }
                        case 17: {
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, pVal.byteValue());
                            break;
                        }
                        case 2: {
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, pVal.shortValue());
                            break;
                        }
                        case 11: {
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, pVal.booleanValue());
                            break;
                        }
                        case 4: {
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, pVal.floatValue());
                            break;
                        }
                        case 5: {
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, pVal.doubleValue());
                            break;
                        }
                        case 0:
                        case 1: {
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, null);
                            break;
                        }
                        default: {
                            if ((vtType & 0x2000) == 0x2000 || (vtType & 0xD) == 0xD || (vtType & 0x9) == 0x9 || (vtType & 0x1000) == 0x1000) {
                                ((WmiResult<Enum>)values).add(vtType, cimType, property2, null);
                                break;
                            }
                            ((WmiResult<Enum>)values).add(vtType, cimType, property2, pVal.getValue());
                            break;
                        }
                    }
                    OleAuto.INSTANCE.VariantClear(pVal);
                }
                clsObj.Release();
                ((WmiResult<Enum>)values).incrementResultCount();
            }
            return values;
        }
    }
    
    public class WmiResult<T extends Enum<T>>
    {
        private Map<T, List<Object>> propertyMap;
        private Map<T, Integer> vtTypeMap;
        private Map<T, Integer> cimTypeMap;
        private int resultCount;
        
        public WmiResult(final Class<T> propertyEnum) {
            this.resultCount = 0;
            this.propertyMap = new EnumMap<T, List<Object>>(propertyEnum);
            this.vtTypeMap = new EnumMap<T, Integer>(propertyEnum);
            this.cimTypeMap = new EnumMap<T, Integer>(propertyEnum);
            for (final T prop : propertyEnum.getEnumConstants()) {
                this.propertyMap.put(prop, new ArrayList<Object>());
                this.vtTypeMap.put(prop, 1);
                this.cimTypeMap.put(prop, 0);
            }
        }
        
        public Object getValue(final T property, final int index) {
            return this.propertyMap.get(property).get(index);
        }
        
        public int getVtType(final T property) {
            return this.vtTypeMap.get(property);
        }
        
        public int getCIMType(final T property) {
            return this.cimTypeMap.get(property);
        }
        
        private void add(final int vtType, final int cimType, final T property, final Object o) {
            this.propertyMap.get(property).add(o);
            if (vtType != 1 && this.vtTypeMap.get(property).equals(1)) {
                this.vtTypeMap.put(property, vtType);
            }
            if (this.cimTypeMap.get(property).equals(0)) {
                this.cimTypeMap.put(property, cimType);
            }
        }
        
        public int getResultCount() {
            return this.resultCount;
        }
        
        private void incrementResultCount() {
            ++this.resultCount;
        }
    }
}
