// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import java.util.HashMap;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.util.Map;

public abstract class Rasapi32Util
{
    private static final int RASP_PppIp = 32801;
    private static Object phoneBookMutex;
    public static final Map CONNECTION_STATE_TEXT;
    
    public static String getRasErrorString(final int code) {
        final char[] msg = new char[1024];
        final int err = Rasapi32.INSTANCE.RasGetErrorString(code, msg, msg.length);
        if (err != 0) {
            return "Unknown error " + code;
        }
        int len;
        for (len = 0; len < msg.length && msg[len] != '\0'; ++len) {}
        return new String(msg, 0, len);
    }
    
    public static String getRasConnectionStatusText(final int connStatus) {
        if (!Rasapi32Util.CONNECTION_STATE_TEXT.containsKey(connStatus)) {
            return Integer.toString(connStatus);
        }
        return Rasapi32Util.CONNECTION_STATE_TEXT.get(connStatus);
    }
    
    public static WinNT.HANDLE getRasConnection(final String connName) throws Ras32Exception {
        IntByReference lpcb = new IntByReference(0);
        final IntByReference lpcConnections = new IntByReference();
        int err = Rasapi32.INSTANCE.RasEnumConnections(null, lpcb, lpcConnections);
        if (err != 0 && err != 603) {
            throw new Ras32Exception(err);
        }
        if (lpcb.getValue() == 0) {
            return null;
        }
        final WinRas.RASCONN[] connections = new WinRas.RASCONN[lpcConnections.getValue()];
        for (int i = 0; i < lpcConnections.getValue(); ++i) {
            connections[i] = new WinRas.RASCONN();
        }
        lpcb = new IntByReference(connections[0].dwSize * lpcConnections.getValue());
        err = Rasapi32.INSTANCE.RasEnumConnections(connections, lpcb, lpcConnections);
        if (err != 0) {
            throw new Ras32Exception(err);
        }
        for (int i = 0; i < lpcConnections.getValue(); ++i) {
            if (new String(connections[i].szEntryName).equals(connName)) {
                return connections[i].hrasconn;
            }
        }
        return null;
    }
    
    public static void hangupRasConnection(final String connName) throws Ras32Exception {
        final WinNT.HANDLE hrasConn = getRasConnection(connName);
        if (hrasConn == null) {
            return;
        }
        final int err = Rasapi32.INSTANCE.RasHangUp(hrasConn);
        if (err != 0) {
            throw new Ras32Exception(err);
        }
    }
    
    public static void hangupRasConnection(final WinNT.HANDLE hrasConn) throws Ras32Exception {
        if (hrasConn == null) {
            return;
        }
        final int err = Rasapi32.INSTANCE.RasHangUp(hrasConn);
        if (err != 0) {
            throw new Ras32Exception(err);
        }
    }
    
    public static WinRas.RASPPPIP getIPProjection(final WinNT.HANDLE hrasConn) throws Ras32Exception {
        final WinRas.RASPPPIP pppIpProjection = new WinRas.RASPPPIP();
        final IntByReference lpcb = new IntByReference(pppIpProjection.size());
        pppIpProjection.write();
        final int err = Rasapi32.INSTANCE.RasGetProjectionInfo(hrasConn, 32801, pppIpProjection.getPointer(), lpcb);
        if (err != 0) {
            throw new Ras32Exception(err);
        }
        pppIpProjection.read();
        return pppIpProjection;
    }
    
    public static WinRas.RASENTRY.ByReference getPhoneBookEntry(final String entryName) throws Ras32Exception {
        synchronized (Rasapi32Util.phoneBookMutex) {
            final WinRas.RASENTRY.ByReference rasEntry = new WinRas.RASENTRY.ByReference();
            final IntByReference lpdwEntryInfoSize = new IntByReference(rasEntry.size());
            final int err = Rasapi32.INSTANCE.RasGetEntryProperties(null, entryName, rasEntry, lpdwEntryInfoSize, null, null);
            if (err != 0) {
                throw new Ras32Exception(err);
            }
            return rasEntry;
        }
    }
    
    public static void setPhoneBookEntry(final String entryName, final WinRas.RASENTRY.ByReference rasEntry) throws Ras32Exception {
        synchronized (Rasapi32Util.phoneBookMutex) {
            final int err = Rasapi32.INSTANCE.RasSetEntryProperties(null, entryName, rasEntry, rasEntry.size(), null, 0);
            if (err != 0) {
                throw new Ras32Exception(err);
            }
        }
    }
    
    public static WinRas.RASDIALPARAMS getPhoneBookDialingParams(final String entryName) throws Ras32Exception {
        synchronized (Rasapi32Util.phoneBookMutex) {
            final WinRas.RASDIALPARAMS.ByReference rasDialParams = new WinRas.RASDIALPARAMS.ByReference();
            System.arraycopy(rasDialParams.szEntryName, 0, entryName.toCharArray(), 0, entryName.length());
            final WinDef.BOOLByReference lpfPassword = new WinDef.BOOLByReference();
            final int err = Rasapi32.INSTANCE.RasGetEntryDialParams(null, rasDialParams, lpfPassword);
            if (err != 0) {
                throw new Ras32Exception(err);
            }
            return rasDialParams;
        }
    }
    
    public static WinNT.HANDLE dialEntry(final String entryName) throws Ras32Exception {
        final WinRas.RASCREDENTIALS.ByReference credentials = new WinRas.RASCREDENTIALS.ByReference();
        synchronized (Rasapi32Util.phoneBookMutex) {
            credentials.dwMask = 7;
            final int err = Rasapi32.INSTANCE.RasGetCredentials(null, entryName, credentials);
            if (err != 0) {
                throw new Ras32Exception(err);
            }
        }
        final WinRas.RASDIALPARAMS.ByReference rasDialParams = new WinRas.RASDIALPARAMS.ByReference();
        System.arraycopy(entryName.toCharArray(), 0, rasDialParams.szEntryName, 0, entryName.length());
        System.arraycopy(credentials.szUserName, 0, rasDialParams.szUserName, 0, credentials.szUserName.length);
        System.arraycopy(credentials.szPassword, 0, rasDialParams.szPassword, 0, credentials.szPassword.length);
        System.arraycopy(credentials.szDomain, 0, rasDialParams.szDomain, 0, credentials.szDomain.length);
        final WinNT.HANDLEByReference hrasConn = new WinNT.HANDLEByReference();
        final int err2 = Rasapi32.INSTANCE.RasDial(null, null, rasDialParams, 0, null, hrasConn);
        if (err2 != 0) {
            if (hrasConn.getValue() != null) {
                Rasapi32.INSTANCE.RasHangUp(hrasConn.getValue());
            }
            throw new Ras32Exception(err2);
        }
        return hrasConn.getValue();
    }
    
    public static WinNT.HANDLE dialEntry(final String entryName, final WinRas.RasDialFunc2 func2) throws Ras32Exception {
        final WinRas.RASCREDENTIALS.ByReference credentials = new WinRas.RASCREDENTIALS.ByReference();
        synchronized (Rasapi32Util.phoneBookMutex) {
            credentials.dwMask = 7;
            final int err = Rasapi32.INSTANCE.RasGetCredentials(null, entryName, credentials);
            if (err != 0) {
                throw new Ras32Exception(err);
            }
        }
        final WinRas.RASDIALPARAMS.ByReference rasDialParams = new WinRas.RASDIALPARAMS.ByReference();
        System.arraycopy(entryName.toCharArray(), 0, rasDialParams.szEntryName, 0, entryName.length());
        System.arraycopy(credentials.szUserName, 0, rasDialParams.szUserName, 0, credentials.szUserName.length);
        System.arraycopy(credentials.szPassword, 0, rasDialParams.szPassword, 0, credentials.szPassword.length);
        System.arraycopy(credentials.szDomain, 0, rasDialParams.szDomain, 0, credentials.szDomain.length);
        final WinNT.HANDLEByReference hrasConn = new WinNT.HANDLEByReference();
        final int err2 = Rasapi32.INSTANCE.RasDial(null, null, rasDialParams, 2, func2, hrasConn);
        if (err2 != 0) {
            if (hrasConn.getValue() != null) {
                Rasapi32.INSTANCE.RasHangUp(hrasConn.getValue());
            }
            throw new Ras32Exception(err2);
        }
        return hrasConn.getValue();
    }
    
    static {
        Rasapi32Util.phoneBookMutex = new Object();
        (CONNECTION_STATE_TEXT = new HashMap()).put(0, "Opening the port...");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(1, "Port has been opened successfully");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(2, "Connecting to the device...");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(3, "The device has connected successfully.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(4, "All devices in the device chain have successfully connected.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(5, "Verifying the user name and password...");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(6, "An authentication event has occurred.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(7, "Requested another validation attempt with a new user.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(8, "Server has requested a callback number.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(9, "The client has requested to change the password");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(10, "Registering your computer on the network...");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(11, "The link-speed calculation phase is starting...");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(12, "An authentication request is being acknowledged.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(13, "Reauthentication (after callback) is starting.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(14, "The client has successfully completed authentication.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(15, "The line is about to disconnect for callback.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(16, "Delaying to give the modem time to reset for callback.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(17, "Waiting for an incoming call from server.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(18, "Projection result information is available.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(19, "User authentication is being initiated or retried.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(20, "Client has been called back and is about to resume authentication.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(21, "Logging on to the network...");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(22, "Subentry has been connected");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(23, "Subentry has been disconnected");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(4096, "Terminal state supported by RASPHONE.EXE.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(4097, "Retry authentication state supported by RASPHONE.EXE.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(4098, "Callback state supported by RASPHONE.EXE.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(4099, "Change password state supported by RASPHONE.EXE.");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(4100, "Displaying authentication UI");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(8192, "Connected to remote server successfully");
        Rasapi32Util.CONNECTION_STATE_TEXT.put(8193, "Disconnected");
    }
    
    public static class Ras32Exception extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        private final int code;
        
        public int getCode() {
            return this.code;
        }
        
        public Ras32Exception(final int code) {
            super(Rasapi32Util.getRasErrorString(code));
            this.code = code;
        }
    }
}
