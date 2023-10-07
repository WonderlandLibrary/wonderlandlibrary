package viaversion.viaversion.api.type.types.version;

import viaversion.viaversion.api.minecraft.metadata.Metadata;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.api.type.types.minecraft.ModernMetaListType;

public class MetadataList1_13_2Type extends ModernMetaListType {
    @Override
    protected Type<Metadata> getType() {
        return Types1_13_2.METADATA;
    }
}
