// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.unix;

import com.sun.jna.Union;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.Library;

public interface Kstat2 extends Library
{
    public static final Kstat2 INSTANCE = Native.load("kstat2", Kstat2.class);
    public static final int KSTAT2_S_OK = 0;
    public static final int KSTAT2_S_NO_PERM = 1;
    public static final int KSTAT2_S_NO_MEM = 2;
    public static final int KSTAT2_S_NO_SPACE = 3;
    public static final int KSTAT2_S_INVAL_ARG = 4;
    public static final int KSTAT2_S_INVAL_STATE = 5;
    public static final int KSTAT2_S_INVAL_TYPE = 6;
    public static final int KSTAT2_S_NOT_FOUND = 7;
    public static final int KSTAT2_S_CONC_MOD = 8;
    public static final int KSTAT2_S_DEL_MAP = 9;
    public static final int KSTAT2_S_SYS_FAIL = 10;
    public static final int KSTAT2_M_STRING = 0;
    public static final int KSTAT2_M_GLOB = 1;
    public static final int KSTAT2_M_RE = 2;
    public static final byte KSTAT2_NVVT_MAP = 0;
    public static final byte KSTAT2_NVVT_INT = 1;
    public static final byte KSTAT2_NVVT_INTS = 2;
    public static final byte KSTAT2_NVVT_STR = 3;
    public static final byte KSTAT2_NVVT_STRS = 4;
    public static final byte KSTAT2_NVK_SYS = 1;
    public static final byte KSTAT2_NVK_USR = 2;
    public static final byte KSTAT2_NVK_MAP = 4;
    public static final byte KSTAT2_NVK_ALL = 7;
    public static final short KSTAT2_NVF_NONE = 0;
    public static final short KSTAT2_NVF_INVAL = 1;
    
    int kstat2_open(final PointerByReference p0, final Kstat2MatcherList p1);
    
    int kstat2_update(final Kstat2Handle p0);
    
    int kstat2_close(final PointerByReference p0);
    
    int kstat2_alloc_matcher_list(final PointerByReference p0);
    
    int kstat2_free_matcher_list(final PointerByReference p0);
    
    int kstat2_add_matcher(final int p0, final String p1, final Kstat2MatcherList p2);
    
    int kstat2_lookup_map(final Kstat2Handle p0, final String p1, final PointerByReference p2);
    
    int kstat2_map_get(final Kstat2Map p0, final String p1, final PointerByReference p2);
    
    String kstat2_status_string(final int p0);
    
    public static class Kstat2Handle extends PointerType
    {
        private PointerByReference ref;
        
        public Kstat2Handle() {
            this((Kstat2MatcherList)null);
        }
        
        public Kstat2Handle(final Kstat2MatcherList matchers) {
            this.ref = new PointerByReference();
            final int ks = Kstat2.INSTANCE.kstat2_open(this.ref, matchers);
            if (ks != 0) {
                throw new Kstat2StatusException(ks);
            }
            this.setPointer(this.ref.getValue());
        }
        
        public int update() {
            return Kstat2.INSTANCE.kstat2_update(this);
        }
        
        public Kstat2Map lookupMap(final String uri) {
            final PointerByReference pMap = new PointerByReference();
            final int ks = Kstat2.INSTANCE.kstat2_lookup_map(this, uri, pMap);
            if (ks != 0) {
                throw new Kstat2StatusException(ks);
            }
            return new Kstat2Map(pMap.getValue());
        }
        
        public int close() {
            return Kstat2.INSTANCE.kstat2_close(this.ref);
        }
    }
    
    public static class Kstat2MatcherList extends PointerType
    {
        private PointerByReference ref;
        
        public Kstat2MatcherList() {
            this.ref = new PointerByReference();
            final int ks = Kstat2.INSTANCE.kstat2_alloc_matcher_list(this.ref);
            if (ks != 0) {
                throw new Kstat2StatusException(ks);
            }
            this.setPointer(this.ref.getValue());
        }
        
        public int addMatcher(final int type, final String match) {
            return Kstat2.INSTANCE.kstat2_add_matcher(type, match, this);
        }
        
        public int free() {
            return Kstat2.INSTANCE.kstat2_free_matcher_list(this.ref);
        }
    }
    
    public static class Kstat2Map extends PointerType
    {
        public Kstat2Map() {
        }
        
        public Kstat2Map(final Pointer p) {
            super(p);
        }
        
        public Kstat2NV mapGet(final String name) {
            final PointerByReference pbr = new PointerByReference();
            final int ks = Kstat2.INSTANCE.kstat2_map_get(this, name, pbr);
            if (ks != 0) {
                throw new Kstat2StatusException(ks);
            }
            return new Kstat2NV(pbr.getValue());
        }
        
        public Object getValue(final String name) {
            try {
                final Kstat2NV nv = this.mapGet(name);
                if (nv.flags == 1) {
                    return null;
                }
                switch (nv.type) {
                    case 0: {
                        return nv.data.map;
                    }
                    case 1: {
                        return nv.data.integerVal;
                    }
                    case 2: {
                        return nv.data.integers.addr.getLongArray(0L, nv.data.integers.len);
                    }
                    case 3: {
                        return nv.data.strings.addr.getString(0L);
                    }
                    case 4: {
                        return nv.data.strings.addr.getStringArray(0L, nv.data.strings.len);
                    }
                    default: {
                        return null;
                    }
                }
            }
            catch (Kstat2StatusException e) {
                return null;
            }
        }
    }
    
    @FieldOrder({ "name", "type", "kind", "flags", "data" })
    public static class Kstat2NV extends Structure
    {
        public String name;
        public byte type;
        public byte kind;
        public short flags;
        public UNION data;
        
        public Kstat2NV() {
        }
        
        public Kstat2NV(final Pointer p) {
            super(p);
            this.read();
        }
        
        @Override
        public void read() {
            super.read();
            switch (this.type) {
                case 0: {
                    this.data.setType(Kstat2Map.class);
                    break;
                }
                case 1: {
                    this.data.setType(Long.TYPE);
                    break;
                }
                case 2: {
                    this.data.setType(UNION.IntegersArr.class);
                    break;
                }
                case 3:
                case 4: {
                    this.data.setType(UNION.StringsArr.class);
                    break;
                }
            }
            this.data.read();
        }
        
        public static class UNION extends Union
        {
            public Kstat2Map map;
            public long integerVal;
            public IntegersArr integers;
            public StringsArr strings;
            
            @FieldOrder({ "addr", "len" })
            public static class IntegersArr extends Structure
            {
                public Pointer addr;
                public int len;
            }
            
            @FieldOrder({ "addr", "len" })
            public static class StringsArr extends Structure
            {
                public Pointer addr;
                public int len;
            }
        }
    }
}
