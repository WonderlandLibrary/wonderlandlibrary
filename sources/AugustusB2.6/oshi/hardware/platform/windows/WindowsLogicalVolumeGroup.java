// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import com.sun.jna.platform.win32.VersionHelpers;
import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.regex.Matcher;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.COM.COMException;
import oshi.util.ParseUtil;
import java.util.HashSet;
import java.util.ArrayList;
import oshi.util.tuples.Pair;
import oshi.util.platform.windows.WmiUtil;
import java.util.HashMap;
import oshi.driver.windows.wmi.MSFTStorage;
import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import java.util.Collections;
import oshi.hardware.LogicalVolumeGroup;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import oshi.hardware.common.AbstractLogicalVolumeGroup;

final class WindowsLogicalVolumeGroup extends AbstractLogicalVolumeGroup
{
    private static final Logger LOG;
    private static final Pattern SP_OBJECT_ID;
    private static final Pattern PD_OBJECT_ID;
    private static final Pattern VD_OBJECT_ID;
    private static final boolean IS_WINDOWS8_OR_GREATER;
    
    WindowsLogicalVolumeGroup(final String name, final Map<String, Set<String>> lvMap, final Set<String> pvSet) {
        super(name, lvMap, pvSet);
    }
    
    static List<LogicalVolumeGroup> getLogicalVolumeGroups() {
        if (!WindowsLogicalVolumeGroup.IS_WINDOWS8_OR_GREATER) {
            return Collections.emptyList();
        }
        final WmiQueryHandler h = Objects.requireNonNull(WmiQueryHandler.createInstance());
        boolean comInit = false;
        try {
            comInit = h.initCOM();
            final WbemcliUtil.WmiResult<MSFTStorage.StoragePoolProperty> sp = MSFTStorage.queryStoragePools(h);
            int count = sp.getResultCount();
            if (count == 0) {
                return Collections.emptyList();
            }
            final Map<String, String> vdMap = new HashMap<String, String>();
            final WbemcliUtil.WmiResult<MSFTStorage.VirtualDiskProperty> vds = MSFTStorage.queryVirtualDisks(h);
            count = vds.getResultCount();
            for (int i = 0; i < count; ++i) {
                String vdObjectId = WmiUtil.getString(vds, MSFTStorage.VirtualDiskProperty.OBJECTID, i);
                final Matcher m = WindowsLogicalVolumeGroup.VD_OBJECT_ID.matcher(vdObjectId);
                if (m.matches()) {
                    vdObjectId = m.group(2) + " " + m.group(1);
                }
                vdMap.put(vdObjectId, WmiUtil.getString(vds, MSFTStorage.VirtualDiskProperty.FRIENDLYNAME, i));
            }
            final Map<String, Pair<String, String>> pdMap = new HashMap<String, Pair<String, String>>();
            final WbemcliUtil.WmiResult<MSFTStorage.PhysicalDiskProperty> pds = MSFTStorage.queryPhysicalDisks(h);
            count = pds.getResultCount();
            for (int j = 0; j < count; ++j) {
                String pdObjectId = WmiUtil.getString(pds, MSFTStorage.PhysicalDiskProperty.OBJECTID, j);
                final Matcher k = WindowsLogicalVolumeGroup.PD_OBJECT_ID.matcher(pdObjectId);
                if (k.matches()) {
                    pdObjectId = k.group(1);
                }
                pdMap.put(pdObjectId, new Pair<String, String>(WmiUtil.getString(pds, MSFTStorage.PhysicalDiskProperty.FRIENDLYNAME, j), WmiUtil.getString(pds, MSFTStorage.PhysicalDiskProperty.PHYSICALLOCATION, j)));
            }
            final Map<String, String> sppdMap = new HashMap<String, String>();
            final WbemcliUtil.WmiResult<MSFTStorage.StoragePoolToPhysicalDiskProperty> sppd = MSFTStorage.queryStoragePoolPhysicalDisks(h);
            count = sppd.getResultCount();
            for (int l = 0; l < count; ++l) {
                String spObjectId = WmiUtil.getRefString(sppd, MSFTStorage.StoragePoolToPhysicalDiskProperty.STORAGEPOOL, l);
                Matcher m2 = WindowsLogicalVolumeGroup.SP_OBJECT_ID.matcher(spObjectId);
                if (m2.matches()) {
                    spObjectId = m2.group(1);
                }
                String pdObjectId2 = WmiUtil.getRefString(sppd, MSFTStorage.StoragePoolToPhysicalDiskProperty.PHYSICALDISK, l);
                m2 = WindowsLogicalVolumeGroup.PD_OBJECT_ID.matcher(pdObjectId2);
                if (m2.matches()) {
                    pdObjectId2 = m2.group(1);
                }
                sppdMap.put(spObjectId + " " + pdObjectId2, pdObjectId2);
            }
            final List<LogicalVolumeGroup> lvgList = new ArrayList<LogicalVolumeGroup>();
            count = sp.getResultCount();
            for (int i2 = 0; i2 < count; ++i2) {
                final String name = WmiUtil.getString(sp, MSFTStorage.StoragePoolProperty.FRIENDLYNAME, i2);
                String spObjectId2 = WmiUtil.getString(sp, MSFTStorage.StoragePoolProperty.OBJECTID, i2);
                final Matcher m3 = WindowsLogicalVolumeGroup.SP_OBJECT_ID.matcher(spObjectId2);
                if (m3.matches()) {
                    spObjectId2 = m3.group(1);
                }
                final Set<String> physicalVolumeSet = new HashSet<String>();
                for (final Map.Entry<String, String> entry : sppdMap.entrySet()) {
                    if (entry.getKey().contains(spObjectId2)) {
                        final String pdObjectId3 = entry.getValue();
                        final Pair<String, String> nameLoc = pdMap.get(pdObjectId3);
                        if (nameLoc == null) {
                            continue;
                        }
                        physicalVolumeSet.add(nameLoc.getA() + " @ " + nameLoc.getB());
                    }
                }
                final Map<String, Set<String>> logicalVolumeMap = new HashMap<String, Set<String>>();
                for (final Map.Entry<String, String> entry2 : vdMap.entrySet()) {
                    if (entry2.getKey().contains(spObjectId2)) {
                        final String vdObjectId2 = ParseUtil.whitespaces.split(entry2.getKey())[0];
                        logicalVolumeMap.put(entry2.getValue() + " " + vdObjectId2, physicalVolumeSet);
                    }
                }
                lvgList.add(new WindowsLogicalVolumeGroup(name, logicalVolumeMap, physicalVolumeSet));
            }
            return lvgList;
        }
        catch (COMException e) {
            WindowsLogicalVolumeGroup.LOG.warn("COM exception: {}", e.getMessage());
            return Collections.emptyList();
        }
        finally {
            if (comInit) {
                h.unInitCOM();
            }
        }
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsLogicalVolumeGroup.class);
        SP_OBJECT_ID = Pattern.compile(".*ObjectId=.*SP:(\\{.*\\}).*");
        PD_OBJECT_ID = Pattern.compile(".*ObjectId=.*PD:(\\{.*\\}).*");
        VD_OBJECT_ID = Pattern.compile(".*ObjectId=.*VD:(\\{.*\\})(\\{.*\\}).*");
        IS_WINDOWS8_OR_GREATER = VersionHelpers.IsWindows8OrGreater();
    }
}
