// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.util.ResourceLocation;

public class ModelResourceLocation extends ResourceLocation
{
    private final String variant;
    
    protected ModelResourceLocation(final int unused, final String... resourceName) {
        super(0, new String[] { resourceName[0], resourceName[1] });
        this.variant = (StringUtils.isEmpty((CharSequence)resourceName[2]) ? "normal" : resourceName[2].toLowerCase(Locale.ROOT));
    }
    
    public ModelResourceLocation(final String pathIn) {
        this(0, parsePathString(pathIn));
    }
    
    public ModelResourceLocation(final ResourceLocation location, final String variantIn) {
        this(location.toString(), variantIn);
    }
    
    public ModelResourceLocation(final String location, final String variantIn) {
        this(0, parsePathString(location + '#' + ((variantIn == null) ? "normal" : variantIn)));
    }
    
    protected static String[] parsePathString(final String pathIn) {
        final String[] astring = { null, pathIn, null };
        final int i = pathIn.indexOf(35);
        String s = pathIn;
        if (i >= 0) {
            astring[2] = pathIn.substring(i + 1, pathIn.length());
            if (i > 1) {
                s = pathIn.substring(0, i);
            }
        }
        System.arraycopy(ResourceLocation.splitObjectName(s), 0, astring, 0, 2);
        return astring;
    }
    
    public String getVariant() {
        return this.variant;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof ModelResourceLocation && super.equals(p_equals_1_)) {
            final ModelResourceLocation modelresourcelocation = (ModelResourceLocation)p_equals_1_;
            return this.variant.equals(modelresourcelocation.variant);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + this.variant.hashCode();
    }
    
    @Override
    public String toString() {
        return super.toString() + '#' + this.variant;
    }
}
