// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import oshi.annotation.concurrent.Immutable;

@Immutable
public class OSSession
{
    private static final DateTimeFormatter LOGIN_FORMAT;
    private final String userName;
    private final String terminalDevice;
    private final long loginTime;
    private final String host;
    
    public OSSession(final String userName, final String terminalDevice, final long loginTime, final String host) {
        this.userName = userName;
        this.terminalDevice = terminalDevice;
        this.loginTime = loginTime;
        this.host = host;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public String getTerminalDevice() {
        return this.terminalDevice;
    }
    
    public long getLoginTime() {
        return this.loginTime;
    }
    
    public String getHost() {
        return this.host;
    }
    
    @Override
    public String toString() {
        final String loginStr = (this.loginTime == 0L) ? "No login" : LocalDateTime.ofInstant(Instant.ofEpochMilli(this.loginTime), ZoneId.systemDefault()).format(OSSession.LOGIN_FORMAT);
        String hostStr = "";
        if (!this.host.isEmpty() && !this.host.equals("::") && !this.host.equals("0.0.0.0")) {
            hostStr = ", (" + this.host + ")";
        }
        return String.format("%s, %s, %s%s", this.userName, this.terminalDevice, loginStr, hostStr);
    }
    
    static {
        LOGIN_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }
}
