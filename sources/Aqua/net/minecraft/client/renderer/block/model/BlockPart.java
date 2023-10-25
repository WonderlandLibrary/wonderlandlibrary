package net.minecraft.client.renderer.block.model;

import java.util.Map;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

public class BlockPart {
    public final Vector3f positionFrom;
    public final Vector3f positionTo;
    public final Map<EnumFacing, BlockPartFace> mapFaces;
    public final BlockPartRotation partRotation;
    public final boolean shade;

    public BlockPart(Vector3f positionFromIn, Vector3f positionToIn, Map<EnumFacing, BlockPartFace> mapFacesIn, BlockPartRotation partRotationIn, boolean shadeIn) {
        this.positionFrom = positionFromIn;
        this.positionTo = positionToIn;
        this.mapFaces = mapFacesIn;
        this.partRotation = partRotationIn;
        this.shade = shadeIn;
        this.setDefaultUvs();
    }

    private void setDefaultUvs() {
        for (Map.Entry entry : this.mapFaces.entrySet()) {
            float[] afloat = this.getFaceUvs((EnumFacing)entry.getKey());
            ((BlockPartFace)entry.getValue()).blockFaceUV.setUvs(afloat);
        }
    }

    private float[] getFaceUvs(EnumFacing p_178236_1_) {
        float[] afloat;
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[p_178236_1_.ordinal()]) {
            case 1: 
            case 2: {
                afloat = new float[]{this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z};
                break;
            }
            case 3: 
            case 4: {
                afloat = new float[]{this.positionFrom.x, 16.0f - this.positionTo.y, this.positionTo.x, 16.0f - this.positionFrom.y};
                break;
            }
            case 5: 
            case 6: {
                afloat = new float[]{this.positionFrom.z, 16.0f - this.positionTo.y, this.positionTo.z, 16.0f - this.positionFrom.y};
                break;
            }
            default: {
                throw new NullPointerException();
            }
        }
        return afloat;
    }
}
