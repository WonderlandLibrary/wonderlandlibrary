// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.List;
import com.sun.jna.Platform;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class UserGroupInfo
{
    private static final Supplier<Map<String, String>> USERS_ID_MAP;
    private static final Supplier<Map<String, String>> GROUPS_ID_MAP;
    
    private UserGroupInfo() {
    }
    
    public static String getUser(final String userId) {
        return UserGroupInfo.USERS_ID_MAP.get().getOrDefault(userId, "unknown");
    }
    
    public static String getGroupName(final String groupId) {
        return UserGroupInfo.GROUPS_ID_MAP.get().getOrDefault(groupId, "unknown");
    }
    
    private static Map<String, String> getUserMap() {
        final HashMap<String, String> userMap = new HashMap<String, String>();
        List<String> passwd;
        if (Platform.isAIX()) {
            passwd = FileUtil.readFile("/etc/passwd");
        }
        else {
            passwd = ExecutingCommand.runNative("getent passwd");
        }
        for (final String entry : passwd) {
            final String[] split = entry.split(":");
            if (split.length > 2) {
                final String userName = split[0];
                final String uid = split[2];
                userMap.putIfAbsent(uid, userName);
            }
        }
        return userMap;
    }
    
    private static Map<String, String> getGroupMap() {
        final Map<String, String> groupMap = new HashMap<String, String>();
        List<String> group;
        if (Platform.isAIX()) {
            group = FileUtil.readFile("/etc/group");
        }
        else {
            group = ExecutingCommand.runNative("getent group");
        }
        for (final String entry : group) {
            final String[] split = entry.split(":");
            if (split.length > 2) {
                final String groupName = split[0];
                final String gid = split[2];
                groupMap.putIfAbsent(gid, groupName);
            }
        }
        return groupMap;
    }
    
    static {
        USERS_ID_MAP = Memoizer.memoize(UserGroupInfo::getUserMap, TimeUnit.MINUTES.toNanos(1L));
        GROUPS_ID_MAP = Memoizer.memoize(UserGroupInfo::getGroupMap, TimeUnit.MINUTES.toNanos(1L));
    }
}
