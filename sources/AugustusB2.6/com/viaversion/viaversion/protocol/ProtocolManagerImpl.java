// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocol;

import com.viaversion.viaversion.protocols.base.BaseProtocol;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.protocol.packet.PacketWrapperImpl;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.util.function.Function;
import java.util.concurrent.Executor;
import java.util.Collection;
import java.util.TreeSet;
import java.util.SortedSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntBidirectionalIterator;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.protocol.packet.VersionedPacketTransformerImpl;
import com.viaversion.viaversion.api.protocol.packet.VersionedPacketTransformer;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectSortedMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import java.util.Iterator;
import java.util.Objects;
import com.viaversion.viaversion.api.Via;
import com.google.common.base.Preconditions;
import java.util.Collections;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.Protocol1_18To1_17_1;
import com.viaversion.viaversion.protocols.protocol1_17_1to1_17.Protocol1_17_1To1_17;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;
import com.viaversion.viaversion.protocols.protocol1_16_4to1_16_3.Protocol1_16_4To1_16_3;
import com.viaversion.viaversion.protocols.protocol1_16_3to1_16_2.Protocol1_16_3To1_16_2;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import com.viaversion.viaversion.protocols.protocol1_16_1to1_16.Protocol1_16_1To1_16;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import com.viaversion.viaversion.protocols.protocol1_15_2to1_15_1.Protocol1_15_2To1_15_1;
import com.viaversion.viaversion.protocols.protocol1_15_1to1_15.Protocol1_15_1To1_15;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import com.viaversion.viaversion.protocols.protocol1_14_4to1_14_3.Protocol1_14_4To1_14_3;
import com.viaversion.viaversion.protocols.protocol1_14_3to1_14_2.Protocol1_14_3To1_14_2;
import com.viaversion.viaversion.protocols.protocol1_14_2to1_14_1.Protocol1_14_2To1_14_1;
import com.viaversion.viaversion.protocols.protocol1_14_1to1_14.Protocol1_14_1To1_14;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import com.viaversion.viaversion.protocols.protocol1_13_2to1_13_1.Protocol1_13_2To1_13_1;
import com.viaversion.viaversion.protocols.protocol1_13_1to1_13.Protocol1_13_1To1_13;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.protocols.protocol1_12_2to1_12_1.Protocol1_12_2To1_12_1;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.Protocol1_12_1To1_12;
import com.viaversion.viaversion.protocols.protocol1_12to1_11_1.Protocol1_12To1_11_1;
import com.viaversion.viaversion.protocols.protocol1_11_1to1_11.Protocol1_11_1To1_11;
import com.viaversion.viaversion.protocols.protocol1_11to1_10.Protocol1_11To1_10;
import com.viaversion.viaversion.protocols.protocol1_10to1_9_3.Protocol1_10To1_9_3_4;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.Protocol1_9_1_2To1_9_3_4;
import com.viaversion.viaversion.protocols.protocol1_9to1_9_1.Protocol1_9To1_9_1;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.Protocol1_9_3To1_9_1_2;
import java.util.Arrays;
import com.viaversion.viaversion.protocols.protocol1_9_1to1_9.Protocol1_9_1To1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.base.BaseProtocol1_16;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocols.base.BaseProtocol1_7;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.api.protocol.version.ServerProtocolVersion;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReadWriteLock;
import com.google.common.collect.Range;
import com.viaversion.viaversion.util.Pair;
import java.util.Set;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import java.util.List;
import com.viaversion.viaversion.api.protocol.ProtocolPathKey;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.ProtocolManager;

public class ProtocolManagerImpl implements ProtocolManager
{
    private static final Protocol BASE_PROTOCOL;
    private final Int2ObjectMap<Int2ObjectMap<Protocol>> registryMap;
    private final Map<Class<? extends Protocol>, Protocol> protocols;
    private final Map<ProtocolPathKey, List<ProtocolPathEntry>> pathCache;
    private final Set<Integer> supportedVersions;
    private final List<Pair<Range<Integer>, Protocol>> baseProtocols;
    private final List<Protocol> registerList;
    private final ReadWriteLock mappingLoaderLock;
    private Map<Class<? extends Protocol>, CompletableFuture<Void>> mappingLoaderFutures;
    private ThreadPoolExecutor mappingLoaderExecutor;
    private boolean mappingsLoaded;
    private ServerProtocolVersion serverProtocolVersion;
    private boolean onlyCheckLoweringPathEntries;
    private int maxProtocolPathSize;
    
    public ProtocolManagerImpl() {
        this.registryMap = new Int2ObjectOpenHashMap<Int2ObjectMap<Protocol>>(32);
        this.protocols = new HashMap<Class<? extends Protocol>, Protocol>();
        this.pathCache = new ConcurrentHashMap<ProtocolPathKey, List<ProtocolPathEntry>>();
        this.supportedVersions = new HashSet<Integer>();
        this.baseProtocols = (List<Pair<Range<Integer>, Protocol>>)Lists.newCopyOnWriteArrayList();
        this.registerList = new ArrayList<Protocol>();
        this.mappingLoaderLock = new ReentrantReadWriteLock();
        this.mappingLoaderFutures = new HashMap<Class<? extends Protocol>, CompletableFuture<Void>>();
        this.serverProtocolVersion = new ServerProtocolVersionSingleton(-1);
        this.onlyCheckLoweringPathEntries = true;
        this.maxProtocolPathSize = 50;
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Via-Mappingloader-%d").build();
        (this.mappingLoaderExecutor = new ThreadPoolExecutor(5, 16, 45L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory)).allowCoreThreadTimeOut(true);
    }
    
    public void registerProtocols() {
        this.registerBaseProtocol(ProtocolManagerImpl.BASE_PROTOCOL, Range.lessThan(Integer.MIN_VALUE));
        this.registerBaseProtocol(new BaseProtocol1_7(), Range.lessThan(ProtocolVersion.v1_16.getVersion()));
        this.registerBaseProtocol(new BaseProtocol1_16(), Range.atLeast(ProtocolVersion.v1_16.getVersion()));
        this.registerProtocol(new Protocol1_9To1_8(), ProtocolVersion.v1_9, ProtocolVersion.v1_8);
        this.registerProtocol(new Protocol1_9_1To1_9(), Arrays.asList(ProtocolVersion.v1_9_1.getVersion(), ProtocolVersion.v1_9_2.getVersion()), ProtocolVersion.v1_9.getVersion());
        this.registerProtocol(new Protocol1_9_3To1_9_1_2(), ProtocolVersion.v1_9_3, ProtocolVersion.v1_9_2);
        this.registerProtocol(new Protocol1_9To1_9_1(), ProtocolVersion.v1_9, ProtocolVersion.v1_9_1);
        this.registerProtocol(new Protocol1_9_1_2To1_9_3_4(), Arrays.asList(ProtocolVersion.v1_9_1.getVersion(), ProtocolVersion.v1_9_2.getVersion()), ProtocolVersion.v1_9_3.getVersion());
        this.registerProtocol(new Protocol1_10To1_9_3_4(), ProtocolVersion.v1_10, ProtocolVersion.v1_9_3);
        this.registerProtocol(new Protocol1_11To1_10(), ProtocolVersion.v1_11, ProtocolVersion.v1_10);
        this.registerProtocol(new Protocol1_11_1To1_11(), ProtocolVersion.v1_11_1, ProtocolVersion.v1_11);
        this.registerProtocol(new Protocol1_12To1_11_1(), ProtocolVersion.v1_12, ProtocolVersion.v1_11_1);
        this.registerProtocol(new Protocol1_12_1To1_12(), ProtocolVersion.v1_12_1, ProtocolVersion.v1_12);
        this.registerProtocol(new Protocol1_12_2To1_12_1(), ProtocolVersion.v1_12_2, ProtocolVersion.v1_12_1);
        this.registerProtocol(new Protocol1_13To1_12_2(), ProtocolVersion.v1_13, ProtocolVersion.v1_12_2);
        this.registerProtocol(new Protocol1_13_1To1_13(), ProtocolVersion.v1_13_1, ProtocolVersion.v1_13);
        this.registerProtocol(new Protocol1_13_2To1_13_1(), ProtocolVersion.v1_13_2, ProtocolVersion.v1_13_1);
        this.registerProtocol(new Protocol1_14To1_13_2(), ProtocolVersion.v1_14, ProtocolVersion.v1_13_2);
        this.registerProtocol(new Protocol1_14_1To1_14(), ProtocolVersion.v1_14_1, ProtocolVersion.v1_14);
        this.registerProtocol(new Protocol1_14_2To1_14_1(), ProtocolVersion.v1_14_2, ProtocolVersion.v1_14_1);
        this.registerProtocol(new Protocol1_14_3To1_14_2(), ProtocolVersion.v1_14_3, ProtocolVersion.v1_14_2);
        this.registerProtocol(new Protocol1_14_4To1_14_3(), ProtocolVersion.v1_14_4, ProtocolVersion.v1_14_3);
        this.registerProtocol(new Protocol1_15To1_14_4(), ProtocolVersion.v1_15, ProtocolVersion.v1_14_4);
        this.registerProtocol(new Protocol1_15_1To1_15(), ProtocolVersion.v1_15_1, ProtocolVersion.v1_15);
        this.registerProtocol(new Protocol1_15_2To1_15_1(), ProtocolVersion.v1_15_2, ProtocolVersion.v1_15_1);
        this.registerProtocol(new Protocol1_16To1_15_2(), ProtocolVersion.v1_16, ProtocolVersion.v1_15_2);
        this.registerProtocol(new Protocol1_16_1To1_16(), ProtocolVersion.v1_16_1, ProtocolVersion.v1_16);
        this.registerProtocol(new Protocol1_16_2To1_16_1(), ProtocolVersion.v1_16_2, ProtocolVersion.v1_16_1);
        this.registerProtocol(new Protocol1_16_3To1_16_2(), ProtocolVersion.v1_16_3, ProtocolVersion.v1_16_2);
        this.registerProtocol(new Protocol1_16_4To1_16_3(), ProtocolVersion.v1_16_4, ProtocolVersion.v1_16_3);
        this.registerProtocol(new Protocol1_17To1_16_4(), ProtocolVersion.v1_17, ProtocolVersion.v1_16_4);
        this.registerProtocol(new Protocol1_17_1To1_17(), ProtocolVersion.v1_17_1, ProtocolVersion.v1_17);
        this.registerProtocol(new Protocol1_18To1_17_1(), ProtocolVersion.v1_18, ProtocolVersion.v1_17_1);
    }
    
    @Override
    public void registerProtocol(final Protocol protocol, final ProtocolVersion clientVersion, final ProtocolVersion serverVersion) {
        this.registerProtocol(protocol, Collections.singletonList(clientVersion.getVersion()), serverVersion.getVersion());
    }
    
    @Override
    public void registerProtocol(final Protocol protocol, final List<Integer> supportedClientVersion, final int serverVersion) {
        protocol.initialize();
        if (!this.pathCache.isEmpty()) {
            this.pathCache.clear();
        }
        this.protocols.put(protocol.getClass(), protocol);
        for (final int clientVersion : supportedClientVersion) {
            Preconditions.checkArgument(clientVersion != serverVersion);
            final Int2ObjectMap<Protocol> protocolMap = this.registryMap.computeIfAbsent(clientVersion, s -> new Int2ObjectOpenHashMap(2));
            protocolMap.put(serverVersion, protocol);
        }
        if (Via.getPlatform().isPluginEnabled()) {
            protocol.register(Via.getManager().getProviders());
            this.refreshVersions();
        }
        else {
            this.registerList.add(protocol);
        }
        if (protocol.hasMappingDataToLoad()) {
            if (this.mappingLoaderExecutor != null) {
                final Class<? extends Protocol> class1 = protocol.getClass();
                Objects.requireNonNull(protocol);
                this.addMappingLoaderFuture(class1, protocol::loadMappingData);
            }
            else {
                protocol.loadMappingData();
            }
        }
    }
    
    @Override
    public void registerBaseProtocol(final Protocol baseProtocol, final Range<Integer> supportedProtocols) {
        Preconditions.checkArgument(baseProtocol.isBaseProtocol(), (Object)"Protocol is not a base protocol");
        baseProtocol.initialize();
        this.baseProtocols.add(new Pair<Range<Integer>, Protocol>(supportedProtocols, baseProtocol));
        if (Via.getPlatform().isPluginEnabled()) {
            baseProtocol.register(Via.getManager().getProviders());
            this.refreshVersions();
        }
        else {
            this.registerList.add(baseProtocol);
        }
    }
    
    public void refreshVersions() {
        this.supportedVersions.clear();
        this.supportedVersions.add(this.serverProtocolVersion.lowestSupportedVersion());
        for (final ProtocolVersion version : ProtocolVersion.getProtocols()) {
            final List<ProtocolPathEntry> protocolPath = this.getProtocolPath(version.getVersion(), this.serverProtocolVersion.lowestSupportedVersion());
            if (protocolPath == null) {
                continue;
            }
            this.supportedVersions.add(version.getVersion());
            for (final ProtocolPathEntry pathEntry : protocolPath) {
                this.supportedVersions.add(pathEntry.outputProtocolVersion());
            }
        }
    }
    
    @Override
    public List<ProtocolPathEntry> getProtocolPath(final int clientVersion, final int serverVersion) {
        if (clientVersion == serverVersion) {
            return null;
        }
        final ProtocolPathKey protocolKey = new ProtocolPathKeyImpl(clientVersion, serverVersion);
        final List<ProtocolPathEntry> protocolList = this.pathCache.get(protocolKey);
        if (protocolList != null) {
            return protocolList;
        }
        final Int2ObjectSortedMap<Protocol> outputPath = this.getProtocolPath(new Int2ObjectLinkedOpenHashMap<Protocol>(), clientVersion, serverVersion);
        if (outputPath == null) {
            return null;
        }
        final List<ProtocolPathEntry> path = new ArrayList<ProtocolPathEntry>(outputPath.size());
        for (final Int2ObjectMap.Entry<Protocol> entry : outputPath.int2ObjectEntrySet()) {
            path.add(new ProtocolPathEntryImpl(entry.getIntKey(), entry.getValue()));
        }
        this.pathCache.put(protocolKey, path);
        return path;
    }
    
    @Override
    public <C extends ClientboundPacketType, S extends ServerboundPacketType> VersionedPacketTransformer<C, S> createPacketTransformer(final ProtocolVersion inputVersion, final Class<C> clientboundPacketsClass, final Class<S> serverboundPacketsClass) {
        Preconditions.checkArgument(clientboundPacketsClass != ClientboundPacketType.class && serverboundPacketsClass != ServerboundPacketType.class);
        return new VersionedPacketTransformerImpl<C, S>(inputVersion, clientboundPacketsClass, serverboundPacketsClass);
    }
    
    private Int2ObjectSortedMap<Protocol> getProtocolPath(final Int2ObjectSortedMap<Protocol> current, final int clientVersion, final int serverVersion) {
        if (current.size() > this.maxProtocolPathSize) {
            return null;
        }
        final Int2ObjectMap<Protocol> toServerProtocolMap = this.registryMap.get(clientVersion);
        if (toServerProtocolMap == null) {
            return null;
        }
        final Protocol protocol = toServerProtocolMap.get(serverVersion);
        if (protocol != null) {
            current.put(serverVersion, protocol);
            return current;
        }
        Int2ObjectSortedMap<Protocol> shortest = null;
        for (final Int2ObjectMap.Entry<Protocol> entry : toServerProtocolMap.int2ObjectEntrySet()) {
            final int translatedToVersion = entry.getIntKey();
            if (current.containsKey(translatedToVersion)) {
                continue;
            }
            if (this.onlyCheckLoweringPathEntries && Math.abs(serverVersion - translatedToVersion) > Math.abs(serverVersion - clientVersion)) {
                continue;
            }
            Int2ObjectSortedMap<Protocol> newCurrent = new Int2ObjectLinkedOpenHashMap<Protocol>(current);
            newCurrent.put(translatedToVersion, entry.getValue());
            newCurrent = this.getProtocolPath(newCurrent, translatedToVersion, serverVersion);
            if (newCurrent == null || (shortest != null && newCurrent.size() >= shortest.size())) {
                continue;
            }
            shortest = newCurrent;
        }
        return shortest;
    }
    
    @Override
    public <T extends Protocol> T getProtocol(final Class<T> protocolClass) {
        return (T)this.protocols.get(protocolClass);
    }
    
    @Override
    public Protocol getProtocol(final int clientVersion, final int serverVersion) {
        final Int2ObjectMap<Protocol> map = this.registryMap.get(clientVersion);
        return (map != null) ? map.get(serverVersion) : null;
    }
    
    @Override
    public Protocol getBaseProtocol(final int serverVersion) {
        for (final Pair<Range<Integer>, Protocol> rangeProtocol : Lists.reverse(this.baseProtocols)) {
            if (rangeProtocol.key().contains(serverVersion)) {
                return rangeProtocol.value();
            }
        }
        throw new IllegalStateException("No Base Protocol for " + serverVersion);
    }
    
    @Override
    public ServerProtocolVersion getServerProtocolVersion() {
        return this.serverProtocolVersion;
    }
    
    public void setServerProtocol(final ServerProtocolVersion serverProtocolVersion) {
        this.serverProtocolVersion = serverProtocolVersion;
        ProtocolRegistry.SERVER_PROTOCOL = serverProtocolVersion.lowestSupportedVersion();
    }
    
    @Override
    public boolean isWorkingPipe() {
        for (final Int2ObjectMap<Protocol> map : this.registryMap.values()) {
            for (final int protocolVersion : this.serverProtocolVersion.supportedVersions()) {
                if (map.containsKey(protocolVersion)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public SortedSet<Integer> getSupportedVersions() {
        return Collections.unmodifiableSortedSet(new TreeSet<Integer>(this.supportedVersions));
    }
    
    @Override
    public void setOnlyCheckLoweringPathEntries(final boolean onlyCheckLoweringPathEntries) {
        this.onlyCheckLoweringPathEntries = onlyCheckLoweringPathEntries;
    }
    
    @Override
    public boolean onlyCheckLoweringPathEntries() {
        return this.onlyCheckLoweringPathEntries;
    }
    
    @Override
    public int getMaxProtocolPathSize() {
        return this.maxProtocolPathSize;
    }
    
    @Override
    public void setMaxProtocolPathSize(final int maxProtocolPathSize) {
        this.maxProtocolPathSize = maxProtocolPathSize;
    }
    
    @Override
    public Protocol getBaseProtocol() {
        return ProtocolManagerImpl.BASE_PROTOCOL;
    }
    
    @Override
    public void completeMappingDataLoading(final Class<? extends Protocol> protocolClass) throws Exception {
        if (this.mappingsLoaded) {
            return;
        }
        final CompletableFuture<Void> future = this.getMappingLoaderFuture(protocolClass);
        if (future != null) {
            future.get();
        }
    }
    
    @Override
    public boolean checkForMappingCompletion() {
        this.mappingLoaderLock.readLock().lock();
        try {
            if (this.mappingsLoaded) {
                return false;
            }
            for (final CompletableFuture<Void> future : this.mappingLoaderFutures.values()) {
                if (!future.isDone()) {
                    return false;
                }
            }
            this.shutdownLoaderExecutor();
            return true;
        }
        finally {
            this.mappingLoaderLock.readLock().unlock();
        }
    }
    
    @Override
    public void addMappingLoaderFuture(final Class<? extends Protocol> protocolClass, final Runnable runnable) {
        final CompletableFuture<Void> future = CompletableFuture.runAsync(runnable, this.mappingLoaderExecutor).exceptionally((Function<Throwable, ? extends Void>)this.mappingLoaderThrowable(protocolClass));
        this.mappingLoaderLock.writeLock().lock();
        try {
            this.mappingLoaderFutures.put(protocolClass, future);
        }
        finally {
            this.mappingLoaderLock.writeLock().unlock();
        }
    }
    
    @Override
    public void addMappingLoaderFuture(final Class<? extends Protocol> protocolClass, final Class<? extends Protocol> dependsOn, final Runnable runnable) {
        final CompletableFuture<Void> future = this.getMappingLoaderFuture(dependsOn).whenCompleteAsync((v, throwable) -> runnable.run(), (Executor)this.mappingLoaderExecutor).exceptionally((Function<Throwable, ? extends Void>)this.mappingLoaderThrowable(protocolClass));
        this.mappingLoaderLock.writeLock().lock();
        try {
            this.mappingLoaderFutures.put(protocolClass, future);
        }
        finally {
            this.mappingLoaderLock.writeLock().unlock();
        }
    }
    
    @Override
    public CompletableFuture<Void> getMappingLoaderFuture(final Class<? extends Protocol> protocolClass) {
        this.mappingLoaderLock.readLock().lock();
        try {
            return this.mappingsLoaded ? null : this.mappingLoaderFutures.get(protocolClass);
        }
        finally {
            this.mappingLoaderLock.readLock().unlock();
        }
    }
    
    @Override
    public PacketWrapper createPacketWrapper(final PacketType packetType, final ByteBuf buf, final UserConnection connection) {
        return new PacketWrapperImpl(packetType, buf, connection);
    }
    
    @Deprecated
    @Override
    public PacketWrapper createPacketWrapper(final int packetId, final ByteBuf buf, final UserConnection connection) {
        return new PacketWrapperImpl(packetId, buf, connection);
    }
    
    public void onServerLoaded() {
        for (final Protocol protocol : this.registerList) {
            protocol.register(Via.getManager().getProviders());
        }
        this.registerList.clear();
    }
    
    private void shutdownLoaderExecutor() {
        Preconditions.checkArgument(!this.mappingsLoaded);
        Via.getPlatform().getLogger().info("Finished mapping loading, shutting down loader executor!");
        this.mappingsLoaded = true;
        this.mappingLoaderExecutor.shutdown();
        this.mappingLoaderExecutor = null;
        this.mappingLoaderFutures.clear();
        this.mappingLoaderFutures = null;
        if (MappingDataLoader.isCacheJsonMappings()) {
            MappingDataLoader.getMappingsCache().clear();
        }
    }
    
    private Function<Throwable, Void> mappingLoaderThrowable(final Class<? extends Protocol> protocolClass) {
        return (Function<Throwable, Void>)(throwable -> {
            Via.getPlatform().getLogger().severe("Error during mapping loading of " + protocolClass.getSimpleName());
            throwable.printStackTrace();
            return null;
        });
    }
    
    static {
        BASE_PROTOCOL = new BaseProtocol();
    }
}
