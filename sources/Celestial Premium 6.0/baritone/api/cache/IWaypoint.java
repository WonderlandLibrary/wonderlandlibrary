/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import baritone.api.utils.BetterBlockPos;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public interface IWaypoint {
    public String getName();

    public Tag getTag();

    public long getCreationTimestamp();

    public BetterBlockPos getLocation();

    public static enum Tag {
        HOME("home", "base"),
        DEATH("death"),
        BED("bed", "spawn"),
        USER("user");

        private static final List<Tag> TAG_LIST;
        public final String[] names;

        private Tag(String ... names) {
            this.names = names;
        }

        public String getName() {
            return this.names[0];
        }

        public static Tag getByName(String name) {
            for (Tag action : Tag.values()) {
                for (String alias : action.names) {
                    if (!alias.equalsIgnoreCase(name)) continue;
                    return action;
                }
            }
            return null;
        }

        public static String[] getAllNames() {
            HashSet<String> names = new HashSet<String>();
            for (Tag tag : Tag.values()) {
                names.addAll(Arrays.asList(tag.names));
            }
            return names.toArray(new String[0]);
        }

        static {
            TAG_LIST = Collections.unmodifiableList(Arrays.asList(Tag.values()));
        }
    }
}

