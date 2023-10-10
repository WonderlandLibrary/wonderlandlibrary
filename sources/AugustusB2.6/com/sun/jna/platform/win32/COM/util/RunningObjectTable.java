// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import java.util.Iterator;
import com.sun.jna.platform.win32.COM.COMException;
import java.util.ArrayList;
import java.util.List;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.COM.IEnumMoniker;
import com.sun.jna.platform.win32.COM.EnumMoniker;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.platform.win32.COM.COMUtils;

public class RunningObjectTable implements IRunningObjectTable
{
    ObjectFactory factory;
    com.sun.jna.platform.win32.COM.RunningObjectTable raw;
    
    protected RunningObjectTable(final com.sun.jna.platform.win32.COM.RunningObjectTable raw, final ObjectFactory factory) {
        this.raw = raw;
        this.factory = factory;
    }
    
    @Override
    public Iterable<IDispatch> enumRunning() {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final PointerByReference ppenumMoniker = new PointerByReference();
        final WinNT.HRESULT hr = this.raw.EnumRunning(ppenumMoniker);
        COMUtils.checkRC(hr);
        final EnumMoniker raw = new EnumMoniker(ppenumMoniker.getValue());
        return new com.sun.jna.platform.win32.COM.util.EnumMoniker(raw, this.raw, this.factory);
    }
    
    @Override
    public <T> List<T> getActiveObjectsByInterface(final Class<T> comInterface) {
        assert COMUtils.comIsInitialized() : "COM not initialized";
        final List<T> result = new ArrayList<T>();
        for (final IDispatch obj : this.enumRunning()) {
            try {
                final T dobj = obj.queryInterface(comInterface);
                result.add(dobj);
            }
            catch (COMException ex) {}
        }
        return result;
    }
}
