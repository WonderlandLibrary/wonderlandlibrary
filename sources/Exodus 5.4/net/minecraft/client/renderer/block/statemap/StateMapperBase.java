/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;

public abstract class StateMapperBase
implements IStateMapper {
    protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = Maps.newLinkedHashMap();

    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block block) {
        for (IBlockState iBlockState : block.getBlockState().getValidStates()) {
            this.mapStateModelLocations.put(iBlockState, this.getModelResourceLocation(iBlockState));
        }
        return this.mapStateModelLocations;
    }

    protected abstract ModelResourceLocation getModelResourceLocation(IBlockState var1);

    public String getPropertyString(Map<IProperty, Comparable> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<IProperty, Comparable> entry : map.entrySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(",");
            }
            IProperty iProperty = entry.getKey();
            Comparable comparable = entry.getValue();
            stringBuilder.append(iProperty.getName());
            stringBuilder.append("=");
            stringBuilder.append(iProperty.getName(comparable));
        }
        if (stringBuilder.length() == 0) {
            stringBuilder.append("normal");
        }
        return stringBuilder.toString();
    }
}

