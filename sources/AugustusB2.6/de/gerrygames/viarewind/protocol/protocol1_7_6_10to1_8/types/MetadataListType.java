// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types;

import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import java.util.List;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListTypeTemplate;

public class MetadataListType extends MetaListTypeTemplate
{
    private MetadataType metadataType;
    
    public MetadataListType() {
        this.metadataType = new MetadataType();
    }
    
    @Override
    public List<Metadata> read(final ByteBuf buffer) throws Exception {
        final ArrayList<Metadata> list = new ArrayList<Metadata>();
        Metadata m;
        do {
            m = Types1_7_6_10.METADATA.read(buffer);
            if (m != null) {
                list.add(m);
            }
        } while (m != null);
        return list;
    }
    
    @Override
    public void write(final ByteBuf buffer, final List<Metadata> metadata) throws Exception {
        for (final Metadata meta : metadata) {
            Types1_7_6_10.METADATA.write(buffer, meta);
        }
        if (metadata.isEmpty()) {
            Types1_7_6_10.METADATA.write(buffer, new Metadata(0, MetaType1_7_6_10.Byte, 0));
        }
        buffer.writeByte(127);
    }
}
