// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.registry;

import com.sun.jna.platform.win32.VersionHelpers;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.sun.jna.Pointer;
import oshi.util.ParseUtil;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.util.Arrays;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import oshi.software.os.OSSession;
import java.util.List;
import com.sun.jna.platform.win32.Wtsapi32;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class SessionWtsData
{
    private static final int WTS_ACTIVE = 0;
    private static final int WTS_CLIENTADDRESS = 14;
    private static final int WTS_SESSIONINFO = 24;
    private static final int WTS_CLIENTPROTOCOLTYPE = 16;
    private static final boolean IS_VISTA_OR_GREATER;
    private static final Wtsapi32 WTS;
    
    private SessionWtsData() {
    }
    
    public static List<OSSession> queryUserSessions() {
        final List<OSSession> sessions = new ArrayList<OSSession>();
        if (SessionWtsData.IS_VISTA_OR_GREATER) {
            final PointerByReference ppSessionInfo = new PointerByReference();
            final IntByReference pCount = new IntByReference();
            if (SessionWtsData.WTS.WTSEnumerateSessions(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, 0, 1, ppSessionInfo, pCount)) {
                final Pointer pSessionInfo = ppSessionInfo.getValue();
                if (pCount.getValue() > 0) {
                    final Wtsapi32.WTS_SESSION_INFO sessionInfoRef = new Wtsapi32.WTS_SESSION_INFO(pSessionInfo);
                    final Wtsapi32.WTS_SESSION_INFO[] array;
                    final Wtsapi32.WTS_SESSION_INFO[] sessionInfo = array = (Wtsapi32.WTS_SESSION_INFO[])sessionInfoRef.toArray(pCount.getValue());
                    for (final Wtsapi32.WTS_SESSION_INFO session : array) {
                        if (session.State == 0) {
                            final PointerByReference ppBuffer = new PointerByReference();
                            final IntByReference pBytes = new IntByReference();
                            SessionWtsData.WTS.WTSQuerySessionInformation(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, session.SessionId, 16, ppBuffer, pBytes);
                            Pointer pBuffer = ppBuffer.getValue();
                            final short protocolType = pBuffer.getShort(0L);
                            SessionWtsData.WTS.WTSFreeMemory(pBuffer);
                            if (protocolType > 0) {
                                final String device = session.pWinStationName;
                                SessionWtsData.WTS.WTSQuerySessionInformation(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, session.SessionId, 24, ppBuffer, pBytes);
                                pBuffer = ppBuffer.getValue();
                                final Wtsapi32.WTSINFO wtsInfo = new Wtsapi32.WTSINFO(pBuffer);
                                final long logonTime = new WinBase.FILETIME(new WinNT.LARGE_INTEGER(wtsInfo.LogonTime.getValue())).toTime();
                                final String userName = wtsInfo.getUserName();
                                SessionWtsData.WTS.WTSFreeMemory(pBuffer);
                                SessionWtsData.WTS.WTSQuerySessionInformation(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, session.SessionId, 14, ppBuffer, pBytes);
                                pBuffer = ppBuffer.getValue();
                                final Wtsapi32.WTS_CLIENT_ADDRESS addr = new Wtsapi32.WTS_CLIENT_ADDRESS(pBuffer);
                                SessionWtsData.WTS.WTSFreeMemory(pBuffer);
                                String host = "::";
                                if (addr.AddressFamily == 2) {
                                    try {
                                        host = InetAddress.getByAddress(Arrays.copyOfRange(addr.Address, 2, 6)).getHostAddress();
                                    }
                                    catch (UnknownHostException e) {
                                        host = "Illegal length IP Array";
                                    }
                                }
                                else if (addr.AddressFamily == 23) {
                                    final int[] ipArray = convertBytesToInts(addr.Address);
                                    host = ParseUtil.parseUtAddrV6toIP(ipArray);
                                }
                                sessions.add(new OSSession(userName, device, logonTime, host));
                            }
                        }
                    }
                }
                SessionWtsData.WTS.WTSFreeMemory(pSessionInfo);
            }
        }
        return sessions;
    }
    
    private static int[] convertBytesToInts(final byte[] address) {
        final IntBuffer intBuf = ByteBuffer.wrap(Arrays.copyOfRange(address, 2, 18)).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        final int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return array;
    }
    
    static {
        IS_VISTA_OR_GREATER = VersionHelpers.IsWindowsVistaOrGreater();
        WTS = Wtsapi32.INSTANCE;
    }
}
