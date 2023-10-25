/*
 * Decompiled with CFR 0.150.
 */
package baritone.cache;

import baritone.api.cache.IWaypoint;
import baritone.api.cache.IWaypointCollection;
import baritone.api.cache.Waypoint;
import baritone.api.utils.BetterBlockPos;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WaypointCollection
implements IWaypointCollection {
    private static final long WAYPOINT_MAGIC_VALUE = 121977993584L;
    private final Path directory;
    private final Map<IWaypoint.Tag, Set<IWaypoint>> waypoints;

    WaypointCollection(Path directory) {
        this.directory = directory;
        if (!Files.exists(directory, new LinkOption[0])) {
            try {
                Files.createDirectories(directory, new FileAttribute[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        System.out.println("Would save waypoints to " + directory);
        this.waypoints = new HashMap<IWaypoint.Tag, Set<IWaypoint>>();
        this.load();
    }

    private void load() {
        for (IWaypoint.Tag tag : IWaypoint.Tag.values()) {
            this.load(tag);
        }
    }

    private synchronized void load(IWaypoint.Tag tag) {
        this.waypoints.put(tag, new HashSet());
        Path fileName = this.directory.resolve(tag.name().toLowerCase() + ".mp4");
        if (!Files.exists(fileName, new LinkOption[0])) {
            return;
        }
        try (FileInputStream fileIn = new FileInputStream(fileName.toFile());
             BufferedInputStream bufIn = new BufferedInputStream(fileIn);
             DataInputStream in = new DataInputStream(bufIn);){
            long magic = in.readLong();
            if (magic != 121977993584L) {
                throw new IOException("Bad magic value " + magic);
            }
            long length = in.readLong();
            while (length-- > 0L) {
                String name = in.readUTF();
                long creationTimestamp = in.readLong();
                int x = in.readInt();
                int y = in.readInt();
                int z = in.readInt();
                this.waypoints.get((Object)tag).add(new Waypoint(name, tag, new BetterBlockPos(x, y, z), creationTimestamp));
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private synchronized void save(IWaypoint.Tag tag) {
        Path fileName = this.directory.resolve(tag.name().toLowerCase() + ".mp4");
        try (FileOutputStream fileOut = new FileOutputStream(fileName.toFile());
             BufferedOutputStream bufOut = new BufferedOutputStream(fileOut);
             DataOutputStream out = new DataOutputStream(bufOut);){
            out.writeLong(121977993584L);
            out.writeLong(this.waypoints.get((Object)tag).size());
            for (IWaypoint waypoint : this.waypoints.get((Object)tag)) {
                out.writeUTF(waypoint.getName());
                out.writeLong(waypoint.getCreationTimestamp());
                out.writeInt(waypoint.getLocation().getX());
                out.writeInt(waypoint.getLocation().getY());
                out.writeInt(waypoint.getLocation().getZ());
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addWaypoint(IWaypoint waypoint) {
        if (this.waypoints.get((Object)waypoint.getTag()).add(waypoint)) {
            this.save(waypoint.getTag());
        }
    }

    @Override
    public void removeWaypoint(IWaypoint waypoint) {
        if (this.waypoints.get((Object)waypoint.getTag()).remove(waypoint)) {
            this.save(waypoint.getTag());
        }
    }

    @Override
    public IWaypoint getMostRecentByTag(IWaypoint.Tag tag) {
        return this.waypoints.get((Object)tag).stream().min(Comparator.comparingLong(w -> -w.getCreationTimestamp())).orElse(null);
    }

    @Override
    public Set<IWaypoint> getByTag(IWaypoint.Tag tag) {
        return Collections.unmodifiableSet(this.waypoints.get((Object)tag));
    }

    @Override
    public Set<IWaypoint> getAllWaypoints() {
        return this.waypoints.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }
}

