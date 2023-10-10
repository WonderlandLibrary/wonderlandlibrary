// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import java.util.ArrayList;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public abstract class Netapi32Util
{
    public static String getDCName() {
        return getDCName(null, null);
    }
    
    public static String getDCName(final String serverName, final String domainName) {
        final PointerByReference bufptr = new PointerByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetGetDCName(domainName, serverName, bufptr);
            if (0 != rc) {
                throw new Win32Exception(rc);
            }
            return bufptr.getValue().getWideString(0L);
        }
        finally {
            if (0 != Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue())) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
        }
    }
    
    public static int getJoinStatus() {
        return getJoinStatus(null);
    }
    
    public static int getJoinStatus(final String computerName) {
        final PointerByReference lpNameBuffer = new PointerByReference();
        final IntByReference bufferType = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetGetJoinInformation(computerName, lpNameBuffer, bufferType);
            if (0 != rc) {
                throw new Win32Exception(rc);
            }
            return bufferType.getValue();
        }
        finally {
            if (lpNameBuffer.getPointer() != null) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(lpNameBuffer.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static String getDomainName(final String computerName) {
        final PointerByReference lpNameBuffer = new PointerByReference();
        final IntByReference bufferType = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetGetJoinInformation(computerName, lpNameBuffer, bufferType);
            if (0 != rc) {
                throw new Win32Exception(rc);
            }
            return lpNameBuffer.getValue().getWideString(0L);
        }
        finally {
            if (lpNameBuffer.getPointer() != null) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(lpNameBuffer.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static LocalGroup[] getLocalGroups() {
        return getLocalGroups(null);
    }
    
    public static LocalGroup[] getLocalGroups(final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesRead = new IntByReference();
        final IntByReference totalEntries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetLocalGroupEnum(serverName, 1, bufptr, -1, entriesRead, totalEntries, null);
            if (0 != rc || bufptr.getValue() == Pointer.NULL) {
                throw new Win32Exception(rc);
            }
            final ArrayList<LocalGroup> result = new ArrayList<LocalGroup>();
            if (entriesRead.getValue() > 0) {
                final LMAccess.LOCALGROUP_INFO_1 group = new LMAccess.LOCALGROUP_INFO_1(bufptr.getValue());
                final LMAccess.LOCALGROUP_INFO_1[] array;
                final LMAccess.LOCALGROUP_INFO_1[] groups = array = (LMAccess.LOCALGROUP_INFO_1[])group.toArray(entriesRead.getValue());
                for (final LMAccess.LOCALGROUP_INFO_1 lgpi : array) {
                    final LocalGroup lgp = new LocalGroup();
                    lgp.name = lgpi.lgrui1_name;
                    lgp.comment = lgpi.lgrui1_comment;
                    result.add(lgp);
                }
            }
            return result.toArray(new LocalGroup[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static Group[] getGlobalGroups() {
        return getGlobalGroups(null);
    }
    
    public static Group[] getGlobalGroups(final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesRead = new IntByReference();
        final IntByReference totalEntries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetGroupEnum(serverName, 1, bufptr, -1, entriesRead, totalEntries, null);
            if (0 != rc || bufptr.getValue() == Pointer.NULL) {
                throw new Win32Exception(rc);
            }
            final ArrayList<LocalGroup> result = new ArrayList<LocalGroup>();
            if (entriesRead.getValue() > 0) {
                final LMAccess.GROUP_INFO_1 group = new LMAccess.GROUP_INFO_1(bufptr.getValue());
                final LMAccess.GROUP_INFO_1[] array;
                final LMAccess.GROUP_INFO_1[] groups = array = (LMAccess.GROUP_INFO_1[])group.toArray(entriesRead.getValue());
                for (final LMAccess.GROUP_INFO_1 lgpi : array) {
                    final LocalGroup lgp = new LocalGroup();
                    lgp.name = lgpi.grpi1_name;
                    lgp.comment = lgpi.grpi1_comment;
                    result.add(lgp);
                }
            }
            return result.toArray(new LocalGroup[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static User[] getUsers() {
        return getUsers(null);
    }
    
    public static User[] getUsers(final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesRead = new IntByReference();
        final IntByReference totalEntries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetUserEnum(serverName, 1, 0, bufptr, -1, entriesRead, totalEntries, null);
            if (0 != rc || bufptr.getValue() == Pointer.NULL) {
                throw new Win32Exception(rc);
            }
            final ArrayList<User> result = new ArrayList<User>();
            if (entriesRead.getValue() > 0) {
                final LMAccess.USER_INFO_1 user = new LMAccess.USER_INFO_1(bufptr.getValue());
                final LMAccess.USER_INFO_1[] array;
                final LMAccess.USER_INFO_1[] users = array = (LMAccess.USER_INFO_1[])user.toArray(entriesRead.getValue());
                for (final LMAccess.USER_INFO_1 lu : array) {
                    final User auser = new User();
                    if (lu.usri1_name != null) {
                        auser.name = lu.usri1_name;
                    }
                    result.add(auser);
                }
            }
            return result.toArray(new User[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static Group[] getCurrentUserLocalGroups() {
        return getUserLocalGroups(Secur32Util.getUserNameEx(2));
    }
    
    public static Group[] getUserLocalGroups(final String userName) {
        return getUserLocalGroups(userName, null);
    }
    
    public static Group[] getUserLocalGroups(final String userName, final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesread = new IntByReference();
        final IntByReference totalentries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetUserGetLocalGroups(serverName, userName, 0, 0, bufptr, -1, entriesread, totalentries);
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
            final ArrayList<Group> result = new ArrayList<Group>();
            if (entriesread.getValue() > 0) {
                final LMAccess.LOCALGROUP_USERS_INFO_0 lgroup = new LMAccess.LOCALGROUP_USERS_INFO_0(bufptr.getValue());
                final LMAccess.LOCALGROUP_USERS_INFO_0[] array;
                final LMAccess.LOCALGROUP_USERS_INFO_0[] lgroups = array = (LMAccess.LOCALGROUP_USERS_INFO_0[])lgroup.toArray(entriesread.getValue());
                for (final LMAccess.LOCALGROUP_USERS_INFO_0 lgpi : array) {
                    final LocalGroup lgp = new LocalGroup();
                    if (lgpi.lgrui0_name != null) {
                        lgp.name = lgpi.lgrui0_name;
                    }
                    result.add(lgp);
                }
            }
            return result.toArray(new Group[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static Group[] getUserGroups(final String userName) {
        return getUserGroups(userName, null);
    }
    
    public static Group[] getUserGroups(final String userName, final String serverName) {
        final PointerByReference bufptr = new PointerByReference();
        final IntByReference entriesread = new IntByReference();
        final IntByReference totalentries = new IntByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetUserGetGroups(serverName, userName, 0, bufptr, -1, entriesread, totalentries);
            if (rc != 0) {
                throw new Win32Exception(rc);
            }
            final ArrayList<Group> result = new ArrayList<Group>();
            if (entriesread.getValue() > 0) {
                final LMAccess.GROUP_USERS_INFO_0 lgroup = new LMAccess.GROUP_USERS_INFO_0(bufptr.getValue());
                final LMAccess.GROUP_USERS_INFO_0[] array;
                final LMAccess.GROUP_USERS_INFO_0[] lgroups = array = (LMAccess.GROUP_USERS_INFO_0[])lgroup.toArray(entriesread.getValue());
                for (final LMAccess.GROUP_USERS_INFO_0 lgpi : array) {
                    final Group lgp = new Group();
                    if (lgpi.grui0_name != null) {
                        lgp.name = lgpi.grui0_name;
                    }
                    result.add(lgp);
                }
            }
            return result.toArray(new Group[0]);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                final int rc2 = Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
                if (0 != rc2) {
                    throw new Win32Exception(rc2);
                }
            }
        }
    }
    
    public static DomainController getDC() {
        final DsGetDC.PDOMAIN_CONTROLLER_INFO pdci = new DsGetDC.PDOMAIN_CONTROLLER_INFO();
        int rc = Netapi32.INSTANCE.DsGetDcName(null, null, null, null, 0, pdci);
        if (0 != rc) {
            throw new Win32Exception(rc);
        }
        final DomainController dc = new DomainController();
        dc.address = pdci.dci.DomainControllerAddress;
        dc.addressType = pdci.dci.DomainControllerAddressType;
        dc.clientSiteName = pdci.dci.ClientSiteName;
        dc.dnsForestName = pdci.dci.DnsForestName;
        dc.domainGuid = pdci.dci.DomainGuid;
        dc.domainName = pdci.dci.DomainName;
        dc.flags = pdci.dci.Flags;
        dc.name = pdci.dci.DomainControllerName;
        rc = Netapi32.INSTANCE.NetApiBufferFree(pdci.dci.getPointer());
        if (0 != rc) {
            throw new Win32Exception(rc);
        }
        return dc;
    }
    
    public static DomainTrust[] getDomainTrusts() {
        return getDomainTrusts(null);
    }
    
    public static DomainTrust[] getDomainTrusts(final String serverName) {
        final IntByReference domainTrustCount = new IntByReference();
        final PointerByReference domainsPointerRef = new PointerByReference();
        int rc = Netapi32.INSTANCE.DsEnumerateDomainTrusts(serverName, 63, domainsPointerRef, domainTrustCount);
        if (0 != rc) {
            throw new Win32Exception(rc);
        }
        try {
            final ArrayList<DomainTrust> trusts = new ArrayList<DomainTrust>(domainTrustCount.getValue());
            if (domainTrustCount.getValue() > 0) {
                final DsGetDC.DS_DOMAIN_TRUSTS domainTrustRefs = new DsGetDC.DS_DOMAIN_TRUSTS(domainsPointerRef.getValue());
                final DsGetDC.DS_DOMAIN_TRUSTS[] array;
                final DsGetDC.DS_DOMAIN_TRUSTS[] domainTrusts = array = (DsGetDC.DS_DOMAIN_TRUSTS[])domainTrustRefs.toArray(new DsGetDC.DS_DOMAIN_TRUSTS[domainTrustCount.getValue()]);
                for (final DsGetDC.DS_DOMAIN_TRUSTS domainTrust : array) {
                    final DomainTrust t = new DomainTrust();
                    if (domainTrust.DnsDomainName != null) {
                        t.DnsDomainName = domainTrust.DnsDomainName;
                    }
                    if (domainTrust.NetbiosDomainName != null) {
                        t.NetbiosDomainName = domainTrust.NetbiosDomainName;
                    }
                    t.DomainSid = domainTrust.DomainSid;
                    if (domainTrust.DomainSid != null) {
                        t.DomainSidString = Advapi32Util.convertSidToStringSid(domainTrust.DomainSid);
                    }
                    t.DomainGuid = domainTrust.DomainGuid;
                    if (domainTrust.DomainGuid != null) {
                        t.DomainGuidString = Ole32Util.getStringFromGUID(domainTrust.DomainGuid);
                    }
                    t.flags = domainTrust.Flags;
                    trusts.add(t);
                }
            }
            return trusts.toArray(new DomainTrust[0]);
        }
        finally {
            rc = Netapi32.INSTANCE.NetApiBufferFree(domainsPointerRef.getValue());
            if (0 != rc) {
                throw new Win32Exception(rc);
            }
        }
    }
    
    public static UserInfo getUserInfo(final String accountName) {
        return getUserInfo(accountName, getDCName());
    }
    
    public static UserInfo getUserInfo(final String accountName, final String domainName) {
        final PointerByReference bufptr = new PointerByReference();
        try {
            final int rc = Netapi32.INSTANCE.NetUserGetInfo(domainName, accountName, 23, bufptr);
            if (rc == 0) {
                final LMAccess.USER_INFO_23 info_23 = new LMAccess.USER_INFO_23(bufptr.getValue());
                final UserInfo userInfo = new UserInfo();
                userInfo.comment = info_23.usri23_comment;
                userInfo.flags = info_23.usri23_flags;
                userInfo.fullName = info_23.usri23_full_name;
                userInfo.name = info_23.usri23_name;
                if (info_23.usri23_user_sid != null) {
                    userInfo.sidString = Advapi32Util.convertSidToStringSid(info_23.usri23_user_sid);
                }
                userInfo.sid = info_23.usri23_user_sid;
                return userInfo;
            }
            throw new Win32Exception(rc);
        }
        finally {
            if (bufptr.getValue() != Pointer.NULL) {
                Netapi32.INSTANCE.NetApiBufferFree(bufptr.getValue());
            }
        }
    }
    
    public static class Group
    {
        public String name;
    }
    
    public static class User
    {
        public String name;
        public String comment;
    }
    
    public static class UserInfo extends User
    {
        public String fullName;
        public String sidString;
        public WinNT.PSID sid;
        public int flags;
    }
    
    public static class LocalGroup extends Group
    {
        public String comment;
    }
    
    public static class DomainController
    {
        public String name;
        public String address;
        public int addressType;
        public Guid.GUID domainGuid;
        public String domainName;
        public String dnsForestName;
        public int flags;
        public String clientSiteName;
    }
    
    public static class DomainTrust
    {
        public String NetbiosDomainName;
        public String DnsDomainName;
        public WinNT.PSID DomainSid;
        public String DomainSidString;
        public Guid.GUID DomainGuid;
        public String DomainGuidString;
        private int flags;
        
        public boolean isInForest() {
            return (this.flags & 0x1) != 0x0;
        }
        
        public boolean isOutbound() {
            return (this.flags & 0x2) != 0x0;
        }
        
        public boolean isRoot() {
            return (this.flags & 0x4) != 0x0;
        }
        
        public boolean isPrimary() {
            return (this.flags & 0x8) != 0x0;
        }
        
        public boolean isNativeMode() {
            return (this.flags & 0x10) != 0x0;
        }
        
        public boolean isInbound() {
            return (this.flags & 0x20) != 0x0;
        }
    }
}
