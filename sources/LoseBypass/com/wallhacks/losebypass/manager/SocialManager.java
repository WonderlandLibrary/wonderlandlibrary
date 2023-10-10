/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.manager;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.utils.FileUtil;
import com.wallhacks.losebypass.utils.SessionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

public class SocialManager {
    private final CopyOnWriteArrayList<SocialEntry<?>> friends = new CopyOnWriteArrayList();

    public SocialManager() {
        this.LoadFriends();
    }

    public void clearFriends() {
        this.friends.clear();
    }

    public List<SocialEntry<?>> getFriends() {
        return this.friends;
    }

    public void addFriend(SocialEntry<?> entry) {
        if (this.friends.contains(entry)) return;
        this.friends.add(entry);
    }

    public void removeFriend(SocialEntry<?> name) {
        this.getFriends().remove(name);
    }

    public void removeFriend(String name) {
        Iterator<SocialEntry<?>> iterator = this.getFriends().iterator();
        while (iterator.hasNext()) {
            SocialEntry<?> s = iterator.next();
            if (!name.equalsIgnoreCase(s.getName())) continue;
            this.removeFriend(s);
        }
    }

    public SocialEntry getSocial(String name) {
        UUID id = SessionUtils.fromString(name);
        if (id == null) return new INGSocial(name);
        return new UUIDSocial(id);
    }

    public void addFriend(String name) {
        UUID id = SessionUtils.fromString(name);
        if (id != null) {
            this.addFriend(new UUIDSocial(id));
            return;
        }
        this.addFriend(new INGSocial(name));
    }

    public boolean isFriend(SocialEntry entry) {
        return this.getFriends().contains(entry);
    }

    public boolean isFriend(String name) {
        SocialEntry<?> s;
        Iterator<SocialEntry<?>> iterator = this.getFriends().iterator();
        do {
            if (!iterator.hasNext()) return false;
        } while (!name.equalsIgnoreCase((s = iterator.next()).getName()));
        return true;
    }

    public boolean isFriend(EntityPlayer player) {
        SocialEntry<?> s;
        Iterator<SocialEntry<?>> iterator = this.getFriends().iterator();
        do {
            if (!iterator.hasNext()) return false;
        } while (!(s = iterator.next()).isEntityPlayer(player));
        return true;
    }

    String getFriendsFile() {
        String base = LoseBypass.ParentPath.getAbsolutePath() + "" + System.getProperty("file.separator") + "socials" + System.getProperty("file.separator") + "";
        return base + "friends.socials";
    }

    public void LoadFriends() {
        try {
            String s = FileUtil.read(this.getFriendsFile());
            if (s != null) {
                String[] List2 = s.split("\n");
                this.clearFriends();
                for (String var : List2) {
                    String[] parts = var.split("/");
                    if (parts.length != 2) continue;
                    if (parts[0].equals("ING")) {
                        this.addFriend(parts[1]);
                        continue;
                    }
                    this.addFriend(new UUIDSocial(UUID.fromString(parts[1])));
                }
            }
        }
        catch (Exception e) {
            LoseBypass.logger.info("Failed to load friends");
            e.printStackTrace();
        }
        LoseBypass.logger.info("Loaded " + this.friends.size() + " friend(s)");
    }

    public void SaveFriends() {
        try {
            ArrayList lines = new ArrayList();
            String content = "";
            Iterator<SocialEntry<?>> iterator = LoseBypass.socialManager.getFriends().iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    FileUtil.write(this.getFriendsFile(), content);
                    return;
                }
                SocialEntry<?> e = iterator.next();
                if (e instanceof INGSocial) {
                    content = content + "ING/" + e.getName() + "\n";
                    continue;
                }
                content = content + "UUID/" + e.getUUID() + "\n";
            }
        }
        catch (Exception e) {
            LoseBypass.logger.info("Failed to save friends");
            e.printStackTrace();
        }
    }

    public static SocialEntry getSocialFromNetworkPlayerInfo(NetworkPlayerInfo info) {
        if (info.getGameProfile() == null) return null;
        if (info.getGameProfile().getId() == null) return new INGSocial(info.getGameProfile().getName());
        if (SessionUtils.getname(info.getGameProfile().getId()) == null) return new INGSocial(info.getGameProfile().getName());
        return new UUIDSocial(info.getGameProfile().getId());
    }

    public static class UUIDSocial
    extends SocialEntry<UUID> {
        public UUIDSocial(UUID identifier) {
            super(identifier);
        }

        @Override
        public boolean isEntityPlayer(EntityPlayer player) {
            if (player == null) return false;
            if (player.getGameProfile() != null) return player.getGameProfile().getId().equals(this.getIdentifier());
            return false;
        }

        @Override
        public String getName() {
            return SessionUtils.getname((UUID)this.getIdentifier());
        }

        @Override
        public UUID getUUID() {
            return (UUID)this.getIdentifier();
        }
    }

    public static class INGSocial
    extends SocialEntry<String> {
        public INGSocial(String identifier) {
            super(identifier.toLowerCase());
        }

        @Override
        public boolean isEntityPlayer(EntityPlayer player) {
            if (player == null) return false;
            if (player.getGameProfile() != null) return player.getGameProfile().getName().equalsIgnoreCase((String)this.getIdentifier());
            return false;
        }

        @Override
        public String getName() {
            return (String)this.getIdentifier();
        }

        @Override
        public UUID getUUID() {
            return SessionUtils.getid((String)this.getIdentifier());
        }
    }

    public static abstract class SocialEntry<T> {
        T identifier;

        public T getIdentifier() {
            return this.identifier;
        }

        public SocialEntry(T identifier) {
            this.identifier = identifier;
        }

        public abstract boolean isEntityPlayer(EntityPlayer var1);

        public abstract String getName();

        public abstract UUID getUUID();

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof SocialEntry) {
                SocialEntry other = (SocialEntry)o;
                if (other.getName() != null) return other.getName().equalsIgnoreCase(this.getName());
                return false;
            }
            if (o instanceof String) {
                return ((String)o).equalsIgnoreCase(this.getName());
            }
            if (!(o instanceof UUID)) return false;
            return ((UUID)o).equals(this.getUUID());
        }

        public int hashCode() {
            return this.getName().hashCode();
        }
    }
}

