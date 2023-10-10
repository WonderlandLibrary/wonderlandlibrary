// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.ViaVersion.api.protocol;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.LinkedHashSet;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.version.VersionRange;
import java.util.Set;
import java.util.List;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

@Deprecated
public class ProtocolVersion
{
    private static final Int2ObjectMap<ProtocolVersion> versions;
    private static final List<ProtocolVersion> versionList;
    public static final ProtocolVersion v1_4_6;
    public static final ProtocolVersion v1_5_1;
    public static final ProtocolVersion v1_5_2;
    public static final ProtocolVersion v_1_6_1;
    public static final ProtocolVersion v_1_6_2;
    public static final ProtocolVersion v_1_6_3;
    public static final ProtocolVersion v_1_6_4;
    public static final ProtocolVersion v1_7_1;
    public static final ProtocolVersion v1_7_6;
    public static final ProtocolVersion v1_8;
    public static final ProtocolVersion v1_9;
    public static final ProtocolVersion v1_9_1;
    public static final ProtocolVersion v1_9_2;
    public static final ProtocolVersion v1_9_3;
    public static final ProtocolVersion v1_10;
    public static final ProtocolVersion v1_11;
    public static final ProtocolVersion v1_11_1;
    public static final ProtocolVersion v1_12;
    public static final ProtocolVersion v1_12_1;
    public static final ProtocolVersion v1_12_2;
    public static final ProtocolVersion v1_13;
    public static final ProtocolVersion v1_13_1;
    public static final ProtocolVersion v1_13_2;
    public static final ProtocolVersion v1_14;
    public static final ProtocolVersion v1_14_1;
    public static final ProtocolVersion v1_14_2;
    public static final ProtocolVersion v1_14_3;
    public static final ProtocolVersion v1_14_4;
    public static final ProtocolVersion v1_15;
    public static final ProtocolVersion v1_15_1;
    public static final ProtocolVersion v1_15_2;
    public static final ProtocolVersion v1_16;
    public static final ProtocolVersion v1_16_1;
    public static final ProtocolVersion v1_16_2;
    public static final ProtocolVersion v1_16_3;
    public static final ProtocolVersion v1_16_4;
    public static final ProtocolVersion v1_17;
    public static final ProtocolVersion v1_17_1;
    public static final ProtocolVersion v1_18;
    public static final ProtocolVersion unknown;
    private final int version;
    private final int snapshotVersion;
    private final String name;
    private final boolean versionWildcard;
    private final Set<String> includedVersions;
    
    public static ProtocolVersion register(final int version, final String name) {
        return register(version, -1, name);
    }
    
    public static ProtocolVersion register(final int version, final int snapshotVersion, final String name) {
        return register(version, snapshotVersion, name, null);
    }
    
    public static ProtocolVersion register(final int version, final String name, final VersionRange versionRange) {
        return register(version, -1, name, versionRange);
    }
    
    public static ProtocolVersion register(final int version, final int snapshotVersion, final String name, final VersionRange versionRange) {
        final ProtocolVersion protocol = new ProtocolVersion(version, snapshotVersion, name, versionRange);
        ProtocolVersion.versionList.add(protocol);
        ProtocolVersion.versions.put(protocol.getVersion(), protocol);
        if (protocol.isSnapshot()) {
            ProtocolVersion.versions.put(protocol.getFullSnapshotVersion(), protocol);
        }
        return protocol;
    }
    
    public static boolean isRegistered(final int id) {
        return ProtocolVersion.versions.containsKey(id);
    }
    
    public static ProtocolVersion getProtocol(final int id) {
        final ProtocolVersion protocolVersion = ProtocolVersion.versions.get(id);
        if (protocolVersion != null) {
            return protocolVersion;
        }
        return new ProtocolVersion(id, "Unknown (" + id + ")");
    }
    
    public static int getIndex(final ProtocolVersion version) {
        return ProtocolVersion.versionList.indexOf(version);
    }
    
    public static List<ProtocolVersion> getProtocols() {
        return Collections.unmodifiableList((List<? extends ProtocolVersion>)new ArrayList<ProtocolVersion>(ProtocolVersion.versions.values()));
    }
    
    public static ProtocolVersion getClosest(final String protocol) {
        for (final ProtocolVersion version : ProtocolVersion.versions.values()) {
            final String name = version.getName();
            if (name.equals(protocol)) {
                return version;
            }
            if (version.isVersionWildcard()) {
                final String majorVersion = name.substring(0, name.length() - 2);
                if (majorVersion.equals(protocol) || protocol.startsWith(name.substring(0, name.length() - 1))) {
                    return version;
                }
                continue;
            }
            else {
                if (version.isRange() && version.getIncludedVersions().contains(protocol)) {
                    return version;
                }
                continue;
            }
        }
        return null;
    }
    
    public ProtocolVersion(final int version, final String name) {
        this(version, -1, name, null);
    }
    
    public ProtocolVersion(final int version, final int snapshotVersion, final String name, final VersionRange versionRange) {
        this.version = version;
        this.snapshotVersion = snapshotVersion;
        this.name = name;
        this.versionWildcard = name.endsWith(".x");
        Preconditions.checkArgument(!this.versionWildcard || versionRange == null, (Object)"A version cannot be a wildcard and a range at the same time!");
        if (versionRange != null) {
            this.includedVersions = new LinkedHashSet<String>();
            for (int i = versionRange.rangeFrom(); i <= versionRange.rangeTo(); ++i) {
                if (i == 0) {
                    this.includedVersions.add(versionRange.baseVersion());
                }
                this.includedVersions.add(versionRange.baseVersion() + "." + i);
            }
        }
        else {
            this.includedVersions = Collections.singleton(name);
        }
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public int getSnapshotVersion() {
        Preconditions.checkArgument(this.isSnapshot());
        return this.snapshotVersion;
    }
    
    public int getFullSnapshotVersion() {
        Preconditions.checkArgument(this.isSnapshot());
        return 0x40000000 | this.snapshotVersion;
    }
    
    public int getOriginalVersion() {
        return (this.snapshotVersion == -1) ? this.version : (0x40000000 | this.snapshotVersion);
    }
    
    public boolean isKnown() {
        return this.version != -1;
    }
    
    public boolean isRange() {
        return this.includedVersions.size() != 1;
    }
    
    public Set<String> getIncludedVersions() {
        return Collections.unmodifiableSet((Set<? extends String>)this.includedVersions);
    }
    
    public boolean isVersionWildcard() {
        return this.versionWildcard;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isSnapshot() {
        return this.snapshotVersion != -1;
    }
    
    public int getId() {
        return this.version;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ProtocolVersion that = (ProtocolVersion)o;
        return this.version == that.version;
    }
    
    @Override
    public int hashCode() {
        return this.version;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%d)", this.name, this.version);
    }
    
    static {
        versions = new Int2ObjectOpenHashMap<ProtocolVersion>();
        versionList = new ArrayList<ProtocolVersion>();
        v1_4_6 = register(51, "1.4.6/7", new VersionRange("1.4", 6, 7));
        v1_5_1 = register(60, "1.5.1");
        v1_5_2 = register(61, "1.5.2");
        v_1_6_1 = register(73, "1.6.1");
        v_1_6_2 = register(74, "1.6.2");
        v_1_6_3 = register(77, "1.6.3");
        v_1_6_4 = register(78, "1.6.4");
        v1_7_1 = register(4, "1.7-1.7.5", new VersionRange("1.7", 0, 5));
        v1_7_6 = register(5, "1.7.6-1.7.10", new VersionRange("1.7", 6, 10));
        v1_8 = register(47, "1.8.x");
        v1_9 = register(107, "1.9");
        v1_9_1 = register(108, "1.9.1");
        v1_9_2 = register(109, "1.9.2");
        v1_9_3 = register(110, "1.9.3/4", new VersionRange("1.9", 3, 4));
        v1_10 = register(210, "1.10.x");
        v1_11 = register(315, "1.11");
        v1_11_1 = register(316, "1.11.1/2", new VersionRange("1.11", 1, 2));
        v1_12 = register(335, "1.12");
        v1_12_1 = register(338, "1.12.1");
        v1_12_2 = register(340, "1.12.2");
        v1_13 = register(393, "1.13");
        v1_13_1 = register(401, "1.13.1");
        v1_13_2 = register(404, "1.13.2");
        v1_14 = register(477, "1.14");
        v1_14_1 = register(480, "1.14.1");
        v1_14_2 = register(485, "1.14.2");
        v1_14_3 = register(490, "1.14.3");
        v1_14_4 = register(498, "1.14.4");
        v1_15 = register(573, "1.15");
        v1_15_1 = register(575, "1.15.1");
        v1_15_2 = register(578, "1.15.2");
        v1_16 = register(735, "1.16");
        v1_16_1 = register(736, "1.16.1");
        v1_16_2 = register(751, "1.16.2");
        v1_16_3 = register(753, "1.16.3");
        v1_16_4 = register(754, "1.16.4/5", new VersionRange("1.16", 4, 5));
        v1_17 = register(755, "1.17");
        v1_17_1 = register(756, "1.17.1");
        v1_18 = register(757, "1.18");
        unknown = register(-1, "UNKNOWN");
    }
}
