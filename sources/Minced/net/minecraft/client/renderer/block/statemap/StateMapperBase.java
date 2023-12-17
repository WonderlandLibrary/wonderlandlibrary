// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import java.util.Map;

public abstract class StateMapperBase implements IStateMapper
{
    protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations;
    
    public StateMapperBase() {
        this.mapStateModelLocations = (Map<IBlockState, ModelResourceLocation>)Maps.newLinkedHashMap();
    }
    
    public String getPropertyString(final Map<IProperty<?>, Comparable<?>> values) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (final Map.Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }
            final IProperty<?> iproperty = entry.getKey();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(this.getPropertyName(iproperty, entry.getValue()));
        }
        if (stringbuilder.length() == 0) {
            stringbuilder.append("normal");
        }
        return stringbuilder.toString();
    }
    
    private <T extends Comparable<T>> String getPropertyName(final IProperty<T> property, final Comparable<?> value) {
        return property.getName((T)value);
    }
    
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(final Block blockIn) {
        for (final IBlockState iblockstate : blockIn.getBlockState().getValidStates()) {
            this.mapStateModelLocations.put(iblockstate, this.getModelResourceLocation(iblockstate));
        }
        return this.mapStateModelLocations;
    }
    
    protected abstract ModelResourceLocation getModelResourceLocation(final IBlockState p0);
}
