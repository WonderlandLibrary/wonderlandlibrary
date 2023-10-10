/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;

public abstract class StateMapperBase
implements IStateMapper {
    protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = Maps.newLinkedHashMap();

    public String getPropertyString(Map<IProperty, Comparable> p_178131_1_) {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator<Map.Entry<IProperty, Comparable>> iterator = p_178131_1_.entrySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (stringbuilder.length() != 0) return stringbuilder.toString();
                stringbuilder.append("normal");
                return stringbuilder.toString();
            }
            Map.Entry<IProperty, Comparable> entry = iterator.next();
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }
            IProperty iproperty = entry.getKey();
            Comparable comparable = entry.getValue();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(iproperty.getName(comparable));
        }
    }

    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
        Iterator iterator = blockIn.getBlockState().getValidStates().iterator();
        while (iterator.hasNext()) {
            IBlockState iblockstate = (IBlockState)iterator.next();
            this.mapStateModelLocations.put(iblockstate, this.getModelResourceLocation(iblockstate));
        }
        return this.mapStateModelLocations;
    }

    protected abstract ModelResourceLocation getModelResourceLocation(IBlockState var1);
}

