// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public abstract class WevtapiUtil
{
    public static String EvtGetExtendedStatus() {
        final IntByReference buffUsed = new IntByReference();
        int errorCode = Wevtapi.INSTANCE.EvtGetExtendedStatus(0, null, buffUsed);
        if (errorCode != 0 && errorCode != 122) {
            throw new Win32Exception(errorCode);
        }
        if (buffUsed.getValue() == 0) {
            return "";
        }
        final char[] mem = new char[buffUsed.getValue()];
        errorCode = Wevtapi.INSTANCE.EvtGetExtendedStatus(mem.length, mem, buffUsed);
        if (errorCode != 0) {
            throw new Win32Exception(errorCode);
        }
        return Native.toString(mem);
    }
    
    public static Memory EvtRender(final Winevt.EVT_HANDLE context, final Winevt.EVT_HANDLE fragment, final int flags, final IntByReference propertyCount) {
        final IntByReference buffUsed = new IntByReference();
        boolean result = Wevtapi.INSTANCE.EvtRender(context, fragment, flags, 0, null, buffUsed, propertyCount);
        final int errorCode = Kernel32.INSTANCE.GetLastError();
        if (!result && errorCode != 122) {
            throw new Win32Exception(errorCode);
        }
        final Memory mem = new Memory(buffUsed.getValue());
        result = Wevtapi.INSTANCE.EvtRender(context, fragment, flags, (int)mem.size(), mem, buffUsed, propertyCount);
        if (!result) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return mem;
    }
    
    public static String EvtFormatMessage(final Winevt.EVT_HANDLE publisherMetadata, final Winevt.EVT_HANDLE event, final int messageId, final int valueCount, final Winevt.EVT_VARIANT[] values, final int flags) {
        final IntByReference bufferUsed = new IntByReference();
        boolean result = Wevtapi.INSTANCE.EvtFormatMessage(publisherMetadata, event, messageId, valueCount, values, flags, 0, null, bufferUsed);
        final int errorCode = Kernel32.INSTANCE.GetLastError();
        if (!result && errorCode != 122) {
            throw new Win32Exception(errorCode);
        }
        final char[] buffer = new char[bufferUsed.getValue()];
        result = Wevtapi.INSTANCE.EvtFormatMessage(publisherMetadata, event, messageId, valueCount, values, flags, buffer.length, buffer, bufferUsed);
        if (!result) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(buffer);
    }
    
    public static Winevt.EVT_VARIANT EvtGetChannelConfigProperty(final Winevt.EVT_HANDLE channelHandle, final int propertyId) {
        final IntByReference propertyValueBufferUsed = new IntByReference();
        boolean result = Wevtapi.INSTANCE.EvtGetChannelConfigProperty(channelHandle, propertyId, 0, 0, null, propertyValueBufferUsed);
        final int errorCode = Kernel32.INSTANCE.GetLastError();
        if (!result && errorCode != 122) {
            throw new Win32Exception(errorCode);
        }
        final Memory propertyValueBuffer = new Memory(propertyValueBufferUsed.getValue());
        result = Wevtapi.INSTANCE.EvtGetChannelConfigProperty(channelHandle, propertyId, 0, (int)propertyValueBuffer.size(), propertyValueBuffer, propertyValueBufferUsed);
        if (!result) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        final Winevt.EVT_VARIANT resultEvt = new Winevt.EVT_VARIANT(propertyValueBuffer);
        resultEvt.read();
        return resultEvt;
    }
    
    public static String EvtNextPublisherId(final Winevt.EVT_HANDLE publisherEnum) {
        final IntByReference publisherIdBufferUsed = new IntByReference();
        boolean result = Wevtapi.INSTANCE.EvtNextPublisherId(publisherEnum, 0, null, publisherIdBufferUsed);
        final int errorCode = Kernel32.INSTANCE.GetLastError();
        if (!result && errorCode != 122) {
            throw new Win32Exception(errorCode);
        }
        final char[] publisherIdBuffer = new char[publisherIdBufferUsed.getValue()];
        result = Wevtapi.INSTANCE.EvtNextPublisherId(publisherEnum, publisherIdBuffer.length, publisherIdBuffer, publisherIdBufferUsed);
        if (!result) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return Native.toString(publisherIdBuffer);
    }
    
    public static Memory EvtGetPublisherMetadataProperty(final Winevt.EVT_HANDLE PublisherMetadata, final int PropertyId, final int Flags) {
        final IntByReference publisherMetadataPropertyBufferUsed = new IntByReference();
        boolean result = Wevtapi.INSTANCE.EvtGetPublisherMetadataProperty(PublisherMetadata, PropertyId, Flags, 0, null, publisherMetadataPropertyBufferUsed);
        final int errorCode = Kernel32.INSTANCE.GetLastError();
        if (!result && errorCode != 122) {
            throw new Win32Exception(errorCode);
        }
        final Memory publisherMetadataPropertyBuffer = new Memory(publisherMetadataPropertyBufferUsed.getValue());
        result = Wevtapi.INSTANCE.EvtGetPublisherMetadataProperty(PublisherMetadata, PropertyId, Flags, (int)publisherMetadataPropertyBuffer.size(), publisherMetadataPropertyBuffer, publisherMetadataPropertyBufferUsed);
        if (!result) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return publisherMetadataPropertyBuffer;
    }
}
