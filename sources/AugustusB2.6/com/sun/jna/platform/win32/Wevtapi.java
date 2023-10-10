// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.Map;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Wevtapi extends StdCallLibrary
{
    public static final Wevtapi INSTANCE = Native.load("wevtapi", Wevtapi.class, W32APIOptions.UNICODE_OPTIONS);
    
    Winevt.EVT_HANDLE EvtOpenSession(final int p0, final Winevt.EVT_RPC_LOGIN p1, final int p2, final int p3);
    
    boolean EvtClose(final Winevt.EVT_HANDLE p0);
    
    boolean EvtCancel(final Winevt.EVT_HANDLE p0);
    
    int EvtGetExtendedStatus(final int p0, final char[] p1, final IntByReference p2);
    
    Winevt.EVT_HANDLE EvtQuery(final Winevt.EVT_HANDLE p0, final String p1, final String p2, final int p3);
    
    boolean EvtNext(final Winevt.EVT_HANDLE p0, final int p1, final Winevt.EVT_HANDLE[] p2, final int p3, final int p4, final IntByReference p5);
    
    boolean EvtSeek(final Winevt.EVT_HANDLE p0, final long p1, final Winevt.EVT_HANDLE p2, final int p3, final int p4);
    
    Winevt.EVT_HANDLE EvtSubscribe(final Winevt.EVT_HANDLE p0, final Winevt.EVT_HANDLE p1, final String p2, final String p3, final Winevt.EVT_HANDLE p4, final Pointer p5, final Callback p6, final int p7);
    
    Winevt.EVT_HANDLE EvtCreateRenderContext(final int p0, final String[] p1, final int p2);
    
    boolean EvtRender(final Winevt.EVT_HANDLE p0, final Winevt.EVT_HANDLE p1, final int p2, final int p3, final Pointer p4, final IntByReference p5, final IntByReference p6);
    
    boolean EvtFormatMessage(final Winevt.EVT_HANDLE p0, final Winevt.EVT_HANDLE p1, final int p2, final int p3, final Winevt.EVT_VARIANT[] p4, final int p5, final int p6, final char[] p7, final IntByReference p8);
    
    Winevt.EVT_HANDLE EvtOpenLog(final Winevt.EVT_HANDLE p0, final String p1, final int p2);
    
    boolean EvtGetLogInfo(final Winevt.EVT_HANDLE p0, final int p1, final int p2, final Pointer p3, final IntByReference p4);
    
    boolean EvtClearLog(final Winevt.EVT_HANDLE p0, final String p1, final String p2, final int p3);
    
    boolean EvtExportLog(final Winevt.EVT_HANDLE p0, final String p1, final String p2, final String p3, final int p4);
    
    boolean EvtArchiveExportedLog(final Winevt.EVT_HANDLE p0, final String p1, final int p2, final int p3);
    
    Winevt.EVT_HANDLE EvtOpenChannelEnum(final Winevt.EVT_HANDLE p0, final int p1);
    
    boolean EvtNextChannelPath(final Winevt.EVT_HANDLE p0, final int p1, final char[] p2, final IntByReference p3);
    
    Winevt.EVT_HANDLE EvtOpenChannelConfig(final Winevt.EVT_HANDLE p0, final String p1, final int p2);
    
    boolean EvtSaveChannelConfig(final Winevt.EVT_HANDLE p0, final int p1);
    
    boolean EvtSetChannelConfigProperty(final Winevt.EVT_HANDLE p0, final int p1, final int p2, final Winevt.EVT_VARIANT p3);
    
    boolean EvtGetChannelConfigProperty(final Winevt.EVT_HANDLE p0, final int p1, final int p2, final int p3, final Pointer p4, final IntByReference p5);
    
    Winevt.EVT_HANDLE EvtOpenPublisherEnum(final Winevt.EVT_HANDLE p0, final int p1);
    
    boolean EvtNextPublisherId(final Winevt.EVT_HANDLE p0, final int p1, final char[] p2, final IntByReference p3);
    
    Winevt.EVT_HANDLE EvtOpenPublisherMetadata(final Winevt.EVT_HANDLE p0, final String p1, final String p2, final int p3, final int p4);
    
    boolean EvtGetPublisherMetadataProperty(final Winevt.EVT_HANDLE p0, final int p1, final int p2, final int p3, final Pointer p4, final IntByReference p5);
    
    Winevt.EVT_HANDLE EvtOpenEventMetadataEnum(final Winevt.EVT_HANDLE p0, final int p1);
    
    Winevt.EVT_HANDLE EvtNextEventMetadata(final Winevt.EVT_HANDLE p0, final int p1);
    
    boolean EvtGetEventMetadataProperty(final Winevt.EVT_HANDLE p0, final int p1, final int p2, final int p3, final Pointer p4, final IntByReference p5);
    
    boolean EvtGetObjectArraySize(final Pointer p0, final IntByReference p1);
    
    boolean EvtGetObjectArrayProperty(final Pointer p0, final int p1, final int p2, final int p3, final int p4, final Pointer p5, final IntByReference p6);
    
    boolean EvtGetQueryInfo(final Winevt.EVT_HANDLE p0, final int p1, final int p2, final Pointer p3, final IntByReference p4);
    
    Winevt.EVT_HANDLE EvtCreateBookmark(final String p0);
    
    boolean EvtUpdateBookmark(final Winevt.EVT_HANDLE p0, final Winevt.EVT_HANDLE p1);
    
    boolean EvtGetEventInfo(final Winevt.EVT_HANDLE p0, final int p1, final int p2, final Pointer p3, final IntByReference p4);
}
