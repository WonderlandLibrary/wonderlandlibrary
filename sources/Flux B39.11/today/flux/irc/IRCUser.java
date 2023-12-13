package today.flux.irc;

import today.flux.utility.ChatUtils;
import today.flux.utility.TimeHelper;

import java.util.concurrent.ConcurrentHashMap;

public class IRCUser {
    public static IRCUser local;
    public static ConcurrentHashMap<String, IRCUser> users = new ConcurrentHashMap<>();

    public static void update(String username, String ign, int perm) {
        if (perm == 0)
            perm = 6;
        if (users.containsKey(username)) {
            IRCUser user = users.get(username);
            if (!user.ign.equals(ign)) {
                ChatUtils.debug("Updating " + username + "'s IGN: " + user.ign + " =>" + ign);
                user.ign = ign;
            }
            if (user.perm != perm) {
                ChatUtils.debug("Updating " + username + "'s Perm: " + user.perm + " =>" + perm);
                user.perm = perm;
                user.updatePrefix();
            }
            user.timer.reset();
        } else {
            IRCUser user = new IRCUser(username, ign, perm);
            if (IRCClient.loggedPacket != null && user.username.equals(IRCClient.loggedPacket.getRealUsername()))
                local = user;
            ChatUtils.debug("Adding IRC User: " + username + " => " + ign);
            users.put(username, user);
        }

        for (String s : users.keySet()) {
            IRCUser ircUser = users.get(s);
            if (ircUser.timer.isDelayComplete(30000)) {
                ChatUtils.debug("Removing IRC User: " + ircUser.username);
                users.remove(s);
            }
        }
    }

    public static IRCUser getIRCUserByIGN(String ign) {
        for (String s : users.keySet()) {
            IRCUser ircUser = users.get(s);
            if (ircUser.ign.equals(ign)) {
                return ircUser;
            }
        }
        return null;
    }

    public String username, ign, rank;
    public int perm;
    public TimeHelper timer = new TimeHelper();

    public IRCUser(String username, String ign, int perm) {
        this.username = username;
        this.ign = ign;
        this.perm = perm;
        updatePrefix();
        this.timer.reset();
    }

    public void updatePrefix() {
        if (perm == 1) {
            rank = "dAdmin";
        } else if (perm == 2) {
            rank = "cMod";
        } else if (perm == 3) {
            rank = "bBeta";
        } else if (perm == 4) {
            rank = "bReseller";
        } else if (perm == 5) {
            rank = "bMedia";
        } else {
            rank = "aUser";
        }
    }

    @Override
    public String toString() {
        return "IRCUser{" +
                "username='" + username + '\'' +
                ", ign='" + ign + '\'' +
                ", rank='" + rank + '\'' +
                ", perm=" + perm +
                ", timer=" + timer +
                '}';
    }
}
