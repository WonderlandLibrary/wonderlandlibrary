// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProductType
{
    BASIC_DESKTOP("basic-desktop"), 
    DAYPASS("daypass"), 
    FREE("free"), 
    OPEN("open"), 
    PREMIUM("premium");
    
    private static final Map<String, ProductType> map;
    public final String type;
    
    private ProductType(final String type) {
        this.type = type;
    }
    
    public static ProductType keyOf(final String type) {
        return ProductType.map.get(type);
    }
    
    public String getType() {
        return this.type;
    }
    
    static {
        map = new HashMap<String, ProductType>();
        for (final ProductType productType : values()) {
            ProductType.map.put(productType.type, productType);
        }
    }
}
