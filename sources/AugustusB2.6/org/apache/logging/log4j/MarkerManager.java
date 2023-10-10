// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import org.apache.logging.log4j.util.PerformanceSensitive;
import java.util.Arrays;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MarkerManager
{
    private static final ConcurrentMap<String, Marker> MARKERS;
    
    private MarkerManager() {
    }
    
    public static void clear() {
        MarkerManager.MARKERS.clear();
    }
    
    public static boolean exists(final String key) {
        return MarkerManager.MARKERS.containsKey(key);
    }
    
    public static Marker getMarker(final String name) {
        Marker result = MarkerManager.MARKERS.get(name);
        if (result == null) {
            MarkerManager.MARKERS.putIfAbsent(name, new Log4jMarker(name));
            result = MarkerManager.MARKERS.get(name);
        }
        return result;
    }
    
    @Deprecated
    public static Marker getMarker(final String name, final String parent) {
        final Marker parentMarker = MarkerManager.MARKERS.get(parent);
        if (parentMarker == null) {
            throw new IllegalArgumentException("Parent Marker " + parent + " has not been defined");
        }
        return getMarker(name, parentMarker);
    }
    
    @Deprecated
    public static Marker getMarker(final String name, final Marker parent) {
        return getMarker(name).addParents(parent);
    }
    
    private static void requireNonNull(final Object obj, final String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
    
    static {
        MARKERS = new ConcurrentHashMap<String, Marker>();
    }
    
    public static class Log4jMarker implements Marker, StringBuilderFormattable
    {
        private static final long serialVersionUID = 100L;
        private final String name;
        private volatile Marker[] parents;
        
        private Log4jMarker() {
            this.name = null;
            this.parents = null;
        }
        
        public Log4jMarker(final String name) {
            requireNonNull(name, "Marker name cannot be null.");
            this.name = name;
            this.parents = null;
        }
        
        @Override
        public synchronized Marker addParents(final Marker... parentMarkers) {
            requireNonNull(parentMarkers, "A parent marker must be specified");
            final Marker[] localParents = this.parents;
            int count = 0;
            int size = parentMarkers.length;
            if (localParents != null) {
                for (final Marker parent : parentMarkers) {
                    if (!contains(parent, localParents) && !parent.isInstanceOf(this)) {
                        ++count;
                    }
                }
                if (count == 0) {
                    return this;
                }
                size = localParents.length + count;
            }
            final Marker[] markers = new Marker[size];
            if (localParents != null) {
                System.arraycopy(localParents, 0, markers, 0, localParents.length);
            }
            int index = (localParents == null) ? 0 : localParents.length;
            for (final Marker parent2 : parentMarkers) {
                if (localParents == null || (!contains(parent2, localParents) && !parent2.isInstanceOf(this))) {
                    markers[index++] = parent2;
                }
            }
            this.parents = markers;
            return this;
        }
        
        @Override
        public synchronized boolean remove(final Marker parent) {
            requireNonNull(parent, "A parent marker must be specified");
            final Marker[] localParents = this.parents;
            if (localParents == null) {
                return false;
            }
            final int localParentsLength = localParents.length;
            if (localParentsLength != 1) {
                int index = 0;
                final Marker[] markers = new Marker[localParentsLength - 1];
                for (final Marker marker : localParents) {
                    if (!marker.equals(parent)) {
                        if (index == localParentsLength - 1) {
                            return false;
                        }
                        markers[index++] = marker;
                    }
                }
                this.parents = markers;
                return true;
            }
            if (localParents[0].equals(parent)) {
                this.parents = null;
                return true;
            }
            return false;
        }
        
        @Override
        public Marker setParents(final Marker... markers) {
            if (markers == null || markers.length == 0) {
                this.parents = null;
            }
            else {
                final Marker[] array = new Marker[markers.length];
                System.arraycopy(markers, 0, array, 0, markers.length);
                this.parents = array;
            }
            return this;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public Marker[] getParents() {
            final Marker[] parentsSnapshot = this.parents;
            if (parentsSnapshot == null) {
                return null;
            }
            return Arrays.copyOf(parentsSnapshot, parentsSnapshot.length);
        }
        
        @Override
        public boolean hasParents() {
            return this.parents != null;
        }
        
        @PerformanceSensitive({ "allocation", "unrolled" })
        @Override
        public boolean isInstanceOf(final Marker marker) {
            requireNonNull(marker, "A marker parameter is required");
            if (this == marker) {
                return true;
            }
            final Marker[] localParents = this.parents;
            if (localParents != null) {
                final int localParentsLength = localParents.length;
                if (localParentsLength == 1) {
                    return checkParent(localParents[0], marker);
                }
                if (localParentsLength == 2) {
                    return checkParent(localParents[0], marker) || checkParent(localParents[1], marker);
                }
                for (final Marker localParent : localParents) {
                    if (checkParent(localParent, marker)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @PerformanceSensitive({ "allocation", "unrolled" })
        @Override
        public boolean isInstanceOf(final String markerName) {
            requireNonNull(markerName, "A marker name is required");
            if (markerName.equals(this.getName())) {
                return true;
            }
            final Marker marker = (Marker)MarkerManager.MARKERS.get(markerName);
            if (marker == null) {
                return false;
            }
            final Marker[] localParents = this.parents;
            if (localParents != null) {
                final int localParentsLength = localParents.length;
                if (localParentsLength == 1) {
                    return checkParent(localParents[0], marker);
                }
                if (localParentsLength == 2) {
                    return checkParent(localParents[0], marker) || checkParent(localParents[1], marker);
                }
                for (final Marker localParent : localParents) {
                    if (checkParent(localParent, marker)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @PerformanceSensitive({ "allocation", "unrolled" })
        private static boolean checkParent(final Marker parent, final Marker marker) {
            if (parent == marker) {
                return true;
            }
            final Marker[] localParents = (parent instanceof Log4jMarker) ? ((Log4jMarker)parent).parents : parent.getParents();
            if (localParents != null) {
                final int localParentsLength = localParents.length;
                if (localParentsLength == 1) {
                    return checkParent(localParents[0], marker);
                }
                if (localParentsLength == 2) {
                    return checkParent(localParents[0], marker) || checkParent(localParents[1], marker);
                }
                for (final Marker localParent : localParents) {
                    if (checkParent(localParent, marker)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @PerformanceSensitive({ "allocation" })
        private static boolean contains(final Marker parent, final Marker... localParents) {
            for (int i = 0, localParentsLength = localParents.length; i < localParentsLength; ++i) {
                final Marker marker = localParents[i];
                if (marker == parent) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof Marker)) {
                return false;
            }
            final Marker marker = (Marker)o;
            return this.name.equals(marker.getName());
        }
        
        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            this.formatTo(sb);
            return sb.toString();
        }
        
        @Override
        public void formatTo(final StringBuilder sb) {
            sb.append(this.name);
            final Marker[] localParents = this.parents;
            if (localParents != null) {
                addParentInfo(sb, localParents);
            }
        }
        
        @PerformanceSensitive({ "allocation" })
        private static void addParentInfo(final StringBuilder sb, final Marker... parents) {
            sb.append("[ ");
            boolean first = true;
            for (int i = 0, parentsLength = parents.length; i < parentsLength; ++i) {
                final Marker marker = parents[i];
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                sb.append(marker.getName());
                final Marker[] p = (marker instanceof Log4jMarker) ? ((Log4jMarker)marker).parents : marker.getParents();
                if (p != null) {
                    addParentInfo(sb, p);
                }
            }
            sb.append(" ]");
        }
    }
}
