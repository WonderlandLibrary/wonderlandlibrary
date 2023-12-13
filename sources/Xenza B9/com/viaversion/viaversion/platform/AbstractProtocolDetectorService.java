// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.platform;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntOpenHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntMap;
import com.viaversion.viaversion.api.platform.ProtocolDetectorService;

public abstract class AbstractProtocolDetectorService implements ProtocolDetectorService
{
    protected final Object2IntMap<String> detectedProtocolIds;
    protected final ReadWriteLock lock;
    
    protected AbstractProtocolDetectorService() {
        this.detectedProtocolIds = new Object2IntOpenHashMap<String>();
        this.lock = new ReentrantReadWriteLock();
        this.detectedProtocolIds.defaultReturnValue(-1);
    }
    
    @Override
    public int serverProtocolVersion(final String serverName) {
        this.lock.readLock().lock();
        int detectedProtocol;
        try {
            detectedProtocol = this.detectedProtocolIds.getInt(serverName);
        }
        finally {
            this.lock.readLock().unlock();
        }
        if (detectedProtocol != -1) {
            return detectedProtocol;
        }
        final Map<String, Integer> servers = this.configuredServers();
        final Integer protocol = servers.get(serverName);
        if (protocol != null) {
            return protocol;
        }
        final Integer defaultProtocol = servers.get("default");
        if (defaultProtocol != null) {
            return defaultProtocol;
        }
        return this.lowestSupportedProtocolVersion();
    }
    
    @Override
    public void setProtocolVersion(final String serverName, final int protocolVersion) {
        this.lock.writeLock().lock();
        try {
            this.detectedProtocolIds.put(serverName, protocolVersion);
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    @Override
    public int uncacheProtocolVersion(final String serverName) {
        this.lock.writeLock().lock();
        try {
            return this.detectedProtocolIds.removeInt(serverName);
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    @Override
    public Object2IntMap<String> detectedProtocolVersions() {
        this.lock.readLock().lock();
        try {
            return new Object2IntOpenHashMap<String>(this.detectedProtocolIds);
        }
        finally {
            this.lock.readLock().unlock();
        }
    }
    
    protected abstract Map<String, Integer> configuredServers();
    
    protected abstract int lowestSupportedProtocolVersion();
}
