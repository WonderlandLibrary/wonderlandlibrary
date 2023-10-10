/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CacheValue;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUResourceBundleReader;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceTypeMismatchException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

class ICUResourceBundleImpl
extends ICUResourceBundle {
    protected int resource;

    protected ICUResourceBundleImpl(ICUResourceBundleImpl container, String key, int resource) {
        super(container, key);
        this.resource = resource;
    }

    ICUResourceBundleImpl(ICUResourceBundle.WholeBundle wholeBundle) {
        super(wholeBundle);
        this.resource = wholeBundle.reader.getRootResource();
    }

    public int getResource() {
        return this.resource;
    }

    protected final ICUResourceBundle createBundleObject(String _key, int _resource, HashMap<String, String> aliasesVisited, UResourceBundle requested) {
        switch (ICUResourceBundleReader.RES_GET_TYPE(_resource)) {
            case 0: 
            case 6: {
                return new ResourceString(this, _key, _resource);
            }
            case 1: {
                return new ResourceBinary(this, _key, _resource);
            }
            case 3: {
                return ICUResourceBundleImpl.getAliasedResource(this, null, 0, _key, _resource, aliasesVisited, requested);
            }
            case 7: {
                return new ResourceInt(this, _key, _resource);
            }
            case 14: {
                return new ResourceIntVector(this, _key, _resource);
            }
            case 8: 
            case 9: {
                return new ResourceArray(this, _key, _resource);
            }
            case 2: 
            case 4: 
            case 5: {
                return new ResourceTable(this, _key, _resource);
            }
        }
        throw new IllegalStateException("The resource type is unknown");
    }

    static class ResourceTable
    extends ResourceContainer {
        @Override
        public int getType() {
            return 2;
        }

        protected String getKey(int index) {
            return ((ICUResourceBundleReader.Table)this.value).getKey(this.wholeBundle.reader, index);
        }

        @Override
        protected Set<String> handleKeySet() {
            ICUResourceBundleReader reader = this.wholeBundle.reader;
            TreeSet<String> keySet = new TreeSet<String>();
            ICUResourceBundleReader.Table table = (ICUResourceBundleReader.Table)this.value;
            for (int i2 = 0; i2 < table.getSize(); ++i2) {
                keySet.add(table.getKey(reader, i2));
            }
            return keySet;
        }

        @Override
        protected UResourceBundle handleGet(String resKey, HashMap<String, String> aliasesVisited, UResourceBundle requested) {
            int i2 = ((ICUResourceBundleReader.Table)this.value).findTableItem(this.wholeBundle.reader, resKey);
            if (i2 < 0) {
                return null;
            }
            return this.createBundleObject(resKey, this.getContainerResource(i2), aliasesVisited, requested);
        }

        @Override
        protected UResourceBundle handleGet(int index, HashMap<String, String> aliasesVisited, UResourceBundle requested) {
            String itemKey = ((ICUResourceBundleReader.Table)this.value).getKey(this.wholeBundle.reader, index);
            if (itemKey == null) {
                throw new IndexOutOfBoundsException();
            }
            return this.createBundleObject(itemKey, this.getContainerResource(index), aliasesVisited, requested);
        }

        @Override
        protected Object handleGetObject(String key) {
            ICUResourceBundleReader reader = this.wholeBundle.reader;
            int index = ((ICUResourceBundleReader.Table)this.value).findTableItem(reader, key);
            if (index >= 0) {
                int res = this.value.getContainerResource(reader, index);
                String s2 = reader.getString(res);
                if (s2 != null) {
                    return s2;
                }
                ICUResourceBundleReader.Array array = reader.getArray(res);
                if (array != null) {
                    int length = array.getSize();
                    String[] strings = new String[length];
                    int j2 = 0;
                    while (true) {
                        if (j2 == length) {
                            return strings;
                        }
                        s2 = reader.getString(array.getContainerResource(reader, j2));
                        if (s2 == null) break;
                        strings[j2] = s2;
                        ++j2;
                    }
                }
            }
            return super.handleGetObject(key);
        }

        String findString(String key) {
            ICUResourceBundleReader reader = this.wholeBundle.reader;
            int index = ((ICUResourceBundleReader.Table)this.value).findTableItem(reader, key);
            if (index < 0) {
                return null;
            }
            return reader.getString(this.value.getContainerResource(reader, index));
        }

        ResourceTable(ICUResourceBundleImpl container, String key, int resource) {
            super(container, key, resource);
            this.value = this.wholeBundle.reader.getTable(resource);
        }

        ResourceTable(ICUResourceBundle.WholeBundle wholeBundle, int rootRes) {
            super(wholeBundle);
            this.value = wholeBundle.reader.getTable(rootRes);
        }
    }

    static class ResourceArray
    extends ResourceContainer {
        @Override
        public int getType() {
            return 8;
        }

        @Override
        protected String[] handleGetStringArray() {
            ICUResourceBundleReader reader = this.wholeBundle.reader;
            int length = this.value.getSize();
            String[] strings = new String[length];
            for (int i2 = 0; i2 < length; ++i2) {
                String s2 = reader.getString(this.value.getContainerResource(reader, i2));
                if (s2 == null) {
                    throw new UResourceTypeMismatchException("");
                }
                strings[i2] = s2;
            }
            return strings;
        }

        @Override
        public String[] getStringArray() {
            return this.handleGetStringArray();
        }

        @Override
        protected UResourceBundle handleGet(String indexStr, HashMap<String, String> aliasesVisited, UResourceBundle requested) {
            int i2 = Integer.parseInt(indexStr);
            return this.createBundleObject(i2, indexStr, aliasesVisited, requested);
        }

        @Override
        protected UResourceBundle handleGet(int index, HashMap<String, String> aliasesVisited, UResourceBundle requested) {
            return this.createBundleObject(index, Integer.toString(index), aliasesVisited, requested);
        }

        ResourceArray(ICUResourceBundleImpl container, String key, int resource) {
            super(container, key, resource);
            this.value = this.wholeBundle.reader.getArray(resource);
        }
    }

    static abstract class ResourceContainer
    extends ICUResourceBundleImpl {
        protected ICUResourceBundleReader.Container value;

        @Override
        public int getSize() {
            return this.value.getSize();
        }

        @Override
        public String getString(int index) {
            int res = this.value.getContainerResource(this.wholeBundle.reader, index);
            if (res == -1) {
                throw new IndexOutOfBoundsException();
            }
            String s2 = this.wholeBundle.reader.getString(res);
            if (s2 != null) {
                return s2;
            }
            return super.getString(index);
        }

        protected int getContainerResource(int index) {
            return this.value.getContainerResource(this.wholeBundle.reader, index);
        }

        protected UResourceBundle createBundleObject(int index, String resKey, HashMap<String, String> aliasesVisited, UResourceBundle requested) {
            int item = this.getContainerResource(index);
            if (item == -1) {
                throw new IndexOutOfBoundsException();
            }
            return this.createBundleObject(resKey, item, aliasesVisited, requested);
        }

        ResourceContainer(ICUResourceBundleImpl container, String key, int resource) {
            super(container, key, resource);
        }

        ResourceContainer(ICUResourceBundle.WholeBundle wholeBundle) {
            super(wholeBundle);
        }
    }

    private static final class ResourceIntVector
    extends ICUResourceBundleImpl {
        @Override
        public int getType() {
            return 14;
        }

        @Override
        public int[] getIntVector() {
            return this.wholeBundle.reader.getIntVector(this.resource);
        }

        ResourceIntVector(ICUResourceBundleImpl container, String key, int resource) {
            super(container, key, resource);
        }
    }

    private static final class ResourceString
    extends ICUResourceBundleImpl {
        private String value;

        @Override
        public int getType() {
            return 0;
        }

        @Override
        public String getString() {
            if (this.value != null) {
                return this.value;
            }
            return this.wholeBundle.reader.getString(this.resource);
        }

        ResourceString(ICUResourceBundleImpl container, String key, int resource) {
            super(container, key, resource);
            String s2 = this.wholeBundle.reader.getString(resource);
            if (s2.length() < 12 || CacheValue.futureInstancesWillBeStrong()) {
                this.value = s2;
            }
        }
    }

    private static final class ResourceInt
    extends ICUResourceBundleImpl {
        @Override
        public int getType() {
            return 7;
        }

        @Override
        public int getInt() {
            return ICUResourceBundleReader.RES_GET_INT(this.resource);
        }

        @Override
        public int getUInt() {
            return ICUResourceBundleReader.RES_GET_UINT(this.resource);
        }

        ResourceInt(ICUResourceBundleImpl container, String key, int resource) {
            super(container, key, resource);
        }
    }

    private static final class ResourceBinary
    extends ICUResourceBundleImpl {
        @Override
        public int getType() {
            return 1;
        }

        @Override
        public ByteBuffer getBinary() {
            return this.wholeBundle.reader.getBinary(this.resource);
        }

        @Override
        public byte[] getBinary(byte[] ba) {
            return this.wholeBundle.reader.getBinary(this.resource, ba);
        }

        ResourceBinary(ICUResourceBundleImpl container, String key, int resource) {
            super(container, key, resource);
        }
    }
}

