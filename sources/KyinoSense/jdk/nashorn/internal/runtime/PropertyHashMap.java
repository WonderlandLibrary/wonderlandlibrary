/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jdk.nashorn.internal.runtime.Property;

public final class PropertyHashMap
implements Map<String, Property> {
    private static final int INITIAL_BINS = 32;
    private static final int LIST_THRESHOLD = 8;
    public static final PropertyHashMap EMPTY_HASHMAP = new PropertyHashMap();
    private final int size;
    private final int threshold;
    private final Element list;
    private final Element[] bins;
    private Property[] properties;

    private PropertyHashMap() {
        this.size = 0;
        this.threshold = 0;
        this.bins = null;
        this.list = null;
    }

    private PropertyHashMap(PropertyHashMap map) {
        this.size = map.size;
        this.threshold = map.threshold;
        this.bins = map.bins;
        this.list = map.list;
    }

    private PropertyHashMap(int size, Element[] bins, Element list) {
        this.size = size;
        this.threshold = bins != null ? PropertyHashMap.threeQuarters(bins.length) : 0;
        this.bins = bins;
        this.list = list;
    }

    public PropertyHashMap immutableReplace(Property property, Property newProperty) {
        assert (property.getKey().equals(newProperty.getKey())) : "replacing properties with different keys: '" + property.getKey() + "' != '" + newProperty.getKey() + "'";
        assert (this.findElement(property.getKey()) != null) : "replacing property that doesn't exist in map: '" + property.getKey() + "'";
        return this.cloneMap().replaceNoClone(property.getKey(), newProperty);
    }

    public PropertyHashMap immutableAdd(Property property) {
        int newSize = this.size + 1;
        PropertyHashMap newMap = this.cloneMap(newSize);
        newMap = newMap.addNoClone(property);
        return newMap;
    }

    public PropertyHashMap immutableAdd(Property ... newProperties) {
        int newSize = this.size + newProperties.length;
        PropertyHashMap newMap = this.cloneMap(newSize);
        for (Property property : newProperties) {
            newMap = newMap.addNoClone(property);
        }
        return newMap;
    }

    public PropertyHashMap immutableAdd(Collection<Property> newProperties) {
        if (newProperties != null) {
            int newSize = this.size + newProperties.size();
            PropertyHashMap newMap = this.cloneMap(newSize);
            for (Property property : newProperties) {
                newMap = newMap.addNoClone(property);
            }
            return newMap;
        }
        return this;
    }

    public PropertyHashMap immutableRemove(Property property) {
        return this.immutableRemove(property.getKey());
    }

    public PropertyHashMap immutableRemove(String key) {
        if (this.bins != null) {
            int binIndex = PropertyHashMap.binIndex(this.bins, key);
            Element bin = this.bins[binIndex];
            if (PropertyHashMap.findElement(bin, key) != null) {
                int newSize = this.size - 1;
                Element[] newBins = null;
                if (newSize >= 8) {
                    newBins = (Element[])this.bins.clone();
                    newBins[binIndex] = PropertyHashMap.removeFromList(bin, key);
                }
                Element newList = PropertyHashMap.removeFromList(this.list, key);
                return new PropertyHashMap(newSize, newBins, newList);
            }
        } else if (PropertyHashMap.findElement(this.list, key) != null) {
            int newSize = this.size - 1;
            return newSize != 0 ? new PropertyHashMap(newSize, null, PropertyHashMap.removeFromList(this.list, key)) : EMPTY_HASHMAP;
        }
        return this;
    }

    public Property find(String key) {
        Element element = this.findElement(key);
        return element != null ? element.getProperty() : null;
    }

    Property[] getProperties() {
        if (this.properties == null) {
            Property[] array = new Property[this.size];
            int i = this.size;
            for (Element element = this.list; element != null; element = element.getLink()) {
                array[--i] = element.getProperty();
            }
            this.properties = array;
        }
        return this.properties;
    }

    private static int binIndex(Element[] bins, String key) {
        return key.hashCode() & bins.length - 1;
    }

    private static int binsNeeded(int n) {
        return 1 << 32 - Integer.numberOfLeadingZeros(n + (n >>> 1) | 0x1F);
    }

    private static int threeQuarters(int n) {
        return (n >>> 1) + (n >>> 2);
    }

    private static Element[] rehash(Element list, int binSize) {
        Element[] newBins = new Element[binSize];
        for (Element element = list; element != null; element = element.getLink()) {
            Property property = element.getProperty();
            String key = property.getKey();
            int binIndex = PropertyHashMap.binIndex(newBins, key);
            newBins[binIndex] = new Element(newBins[binIndex], property);
        }
        return newBins;
    }

    private Element findElement(String key) {
        if (this.bins != null) {
            int binIndex = PropertyHashMap.binIndex(this.bins, key);
            return PropertyHashMap.findElement(this.bins[binIndex], key);
        }
        return PropertyHashMap.findElement(this.list, key);
    }

    private static Element findElement(Element elementList, String key) {
        int hashCode = key.hashCode();
        for (Element element = elementList; element != null; element = element.getLink()) {
            if (!element.match(key, hashCode)) continue;
            return element;
        }
        return null;
    }

    private PropertyHashMap cloneMap() {
        return new PropertyHashMap(this.size, this.bins == null ? null : (Element[])this.bins.clone(), this.list);
    }

    private PropertyHashMap cloneMap(int newSize) {
        Element[] newBins = this.bins == null && newSize <= 8 ? null : (newSize > this.threshold ? PropertyHashMap.rehash(this.list, PropertyHashMap.binsNeeded(newSize)) : (Element[])this.bins.clone());
        return new PropertyHashMap(newSize, newBins, this.list);
    }

    private PropertyHashMap addNoClone(Property property) {
        int newSize = this.size;
        String key = property.getKey();
        Element newList = this.list;
        if (this.bins != null) {
            int binIndex = PropertyHashMap.binIndex(this.bins, key);
            Element bin = this.bins[binIndex];
            if (PropertyHashMap.findElement(bin, key) != null) {
                --newSize;
                bin = PropertyHashMap.removeFromList(bin, key);
                newList = PropertyHashMap.removeFromList(this.list, key);
            }
            this.bins[binIndex] = new Element(bin, property);
        } else if (PropertyHashMap.findElement(this.list, key) != null) {
            --newSize;
            newList = PropertyHashMap.removeFromList(this.list, key);
        }
        newList = new Element(newList, property);
        return new PropertyHashMap(newSize, this.bins, newList);
    }

    private PropertyHashMap replaceNoClone(String key, Property property) {
        if (this.bins != null) {
            int binIndex = PropertyHashMap.binIndex(this.bins, key);
            Element bin = this.bins[binIndex];
            this.bins[binIndex] = bin = PropertyHashMap.replaceInList(bin, key, property);
        }
        Element newList = this.list;
        newList = PropertyHashMap.replaceInList(newList, key, property);
        return new PropertyHashMap(this.size, this.bins, newList);
    }

    private static Element removeFromList(Element list, String key) {
        Element head;
        if (list == null) {
            return null;
        }
        int hashCode = key.hashCode();
        if (list.match(key, hashCode)) {
            return list.getLink();
        }
        Element previous = head = new Element(null, list.getProperty());
        for (Element element = list.getLink(); element != null; element = element.getLink()) {
            if (element.match(key, hashCode)) {
                previous.setLink(element.getLink());
                return head;
            }
            Element next = new Element(null, element.getProperty());
            previous.setLink(next);
            previous = next;
        }
        return list;
    }

    private static Element replaceInList(Element list, String key, Property property) {
        Element head;
        assert (list != null);
        int hashCode = key.hashCode();
        if (list.match(key, hashCode)) {
            return new Element(list.getLink(), property);
        }
        Element previous = head = new Element(null, list.getProperty());
        for (Element element = list.getLink(); element != null; element = element.getLink()) {
            if (element.match(key, hashCode)) {
                previous.setLink(new Element(element.getLink(), property));
                return head;
            }
            Element next = new Element(null, element.getProperty());
            previous.setLink(next);
            previous = next;
        }
        return list;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return this.findElement((String)key) != null;
        }
        assert (key instanceof String);
        return false;
    }

    public boolean containsKey(String key) {
        return this.findElement(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof Property) {
            Property property = (Property)value;
            Element element = this.findElement(property.getKey());
            return element != null && element.getProperty().equals(value);
        }
        return false;
    }

    @Override
    public Property get(Object key) {
        if (key instanceof String) {
            Element element = this.findElement((String)key);
            return element != null ? element.getProperty() : null;
        }
        assert (key instanceof String);
        return null;
    }

    public Property get(String key) {
        Element element = this.findElement(key);
        return element != null ? element.getProperty() : null;
    }

    @Override
    public Property put(String key, Property value) {
        throw new UnsupportedOperationException("Immutable map.");
    }

    @Override
    public Property remove(Object key) {
        throw new UnsupportedOperationException("Immutable map.");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Property> m) {
        throw new UnsupportedOperationException("Immutable map.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Immutable map.");
    }

    @Override
    public Set<String> keySet() {
        HashSet<String> set = new HashSet<String>();
        for (Element element = this.list; element != null; element = element.getLink()) {
            set.add(element.getKey());
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public Collection<Property> values() {
        return Collections.unmodifiableList(Arrays.asList(this.getProperties()));
    }

    @Override
    public Set<Map.Entry<String, Property>> entrySet() {
        HashSet<Element> set = new HashSet<Element>();
        for (Element element = this.list; element != null; element = element.getLink()) {
            set.add(element);
        }
        return Collections.unmodifiableSet(set);
    }

    static final class Element
    implements Map.Entry<String, Property> {
        private Element link;
        private final Property property;
        private final String key;
        private final int hashCode;

        Element(Element link, Property property) {
            this.link = link;
            this.property = property;
            this.key = property.getKey();
            this.hashCode = this.key.hashCode();
        }

        boolean match(String otherKey, int otherHashCode) {
            return this.hashCode == otherHashCode && this.key.equals(otherKey);
        }

        @Override
        public boolean equals(Object other) {
            assert (this.property != null && other != null);
            return other instanceof Element && this.property.equals(((Element)other).property);
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Property getValue() {
            return this.property;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public Property setValue(Property value) {
            throw new UnsupportedOperationException("Immutable map.");
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append('[');
            Element elem = this;
            do {
                sb.append(elem.getValue());
                elem = elem.link;
                if (elem == null) continue;
                sb.append(" -> ");
            } while (elem != null);
            sb.append(']');
            return sb.toString();
        }

        Element getLink() {
            return this.link;
        }

        void setLink(Element link) {
            this.link = link;
        }

        Property getProperty() {
            return this.property;
        }
    }
}

