// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.registry;

import org.slf4j.LoggerFactory;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import oshi.software.os.OSSession;
import java.util.List;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class HkeyUserData
{
    private static final String PATH_DELIMITER = "\\";
    private static final String DEFAULT_DEVICE = "Console";
    private static final String VOLATILE_ENV_SUBKEY = "Volatile Environment";
    private static final String CLIENTNAME = "CLIENTNAME";
    private static final String SESSIONNAME = "SESSIONNAME";
    private static final Logger LOG;
    
    private HkeyUserData() {
    }
    
    public static List<OSSession> queryUserSessions() {
        final List<OSSession> sessions = new ArrayList<OSSession>();
        for (final String sidKey : Advapi32Util.registryGetKeys(WinReg.HKEY_USERS)) {
            if (!sidKey.startsWith(".") && !sidKey.endsWith("_Classes")) {
                try {
                    final Advapi32Util.Account a = Advapi32Util.getAccountBySid(sidKey);
                    final String name = a.name;
                    String device = "Console";
                    String host = a.domain;
                    long loginTime = 0L;
                    final String keyPath = sidKey + "\\" + "Volatile Environment";
                    if (Advapi32Util.registryKeyExists(WinReg.HKEY_USERS, keyPath)) {
                        final WinReg.HKEY hKey = Advapi32Util.registryGetKey(WinReg.HKEY_USERS, keyPath, 131097).getValue();
                        final Advapi32Util.InfoKey info = Advapi32Util.registryQueryInfoKey(hKey, 0);
                        loginTime = info.lpftLastWriteTime.toTime();
                        for (final String subKey : Advapi32Util.registryGetKeys(hKey)) {
                            final String subKeyPath = keyPath + "\\" + subKey;
                            if (Advapi32Util.registryValueExists(WinReg.HKEY_USERS, subKeyPath, "SESSIONNAME")) {
                                final String session = Advapi32Util.registryGetStringValue(WinReg.HKEY_USERS, subKeyPath, "SESSIONNAME");
                                if (!session.isEmpty()) {
                                    device = session;
                                }
                            }
                            if (Advapi32Util.registryValueExists(WinReg.HKEY_USERS, subKeyPath, "CLIENTNAME")) {
                                final String client = Advapi32Util.registryGetStringValue(WinReg.HKEY_USERS, subKeyPath, "CLIENTNAME");
                                if (!client.isEmpty() && !"Console".equals(client)) {
                                    host = client;
                                }
                            }
                        }
                        Advapi32Util.registryCloseKey(hKey);
                    }
                    sessions.add(new OSSession(name, device, loginTime, host));
                }
                catch (Win32Exception ex) {
                    HkeyUserData.LOG.warn("Error querying SID {} from registry: {}", sidKey, ex.getMessage());
                }
            }
        }
        return sessions;
    }
    
    static {
        LOG = LoggerFactory.getLogger(HkeyUserData.class);
    }
}
