// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.rewriter.meta;

import com.viaversion.viaversion.rewriter.EntityRewriter;
import java.util.Objects;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;

public class MetaFilter
{
    private final MetaHandler handler;
    private final EntityType type;
    private final int index;
    private final boolean filterFamily;
    
    public MetaFilter(final EntityType type, final boolean filterFamily, final int index, final MetaHandler handler) {
        Preconditions.checkNotNull(handler, (Object)"MetaHandler cannot be null");
        this.type = type;
        this.filterFamily = filterFamily;
        this.index = index;
        this.handler = handler;
    }
    
    public int index() {
        return this.index;
    }
    
    public EntityType type() {
        return this.type;
    }
    
    public MetaHandler handler() {
        return this.handler;
    }
    
    public boolean filterFamily() {
        return this.filterFamily;
    }
    
    public boolean isFiltered(final EntityType type, final Metadata metadata) {
        return (this.index == -1 || metadata.id() == this.index) && (this.type == null || this.matchesType(type));
    }
    
    private boolean matchesType(final EntityType type) {
        return type != null && (this.filterFamily ? type.isOrHasParent(this.type) : (this.type == type));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MetaFilter that = (MetaFilter)o;
        return this.index == that.index && this.filterFamily == that.filterFamily && this.handler.equals(that.handler) && Objects.equals(this.type, that.type);
    }
    
    @Override
    public int hashCode() {
        int result = this.handler.hashCode();
        result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
        result = 31 * result + this.index;
        result = 31 * result + (this.filterFamily ? 1 : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "MetaFilter{type=" + this.type + ", filterFamily=" + this.filterFamily + ", index=" + this.index + ", handler=" + this.handler + '}';
    }
    
    public static final class Builder
    {
        private final EntityRewriter rewriter;
        private EntityType type;
        private int index;
        private boolean filterFamily;
        private MetaHandler handler;
        
        public Builder(final EntityRewriter rewriter) {
            this.index = -1;
            this.rewriter = rewriter;
        }
        
        public Builder type(final EntityType type) {
            Preconditions.checkArgument(this.type == null);
            this.type = type;
            return this;
        }
        
        public Builder index(final int index) {
            Preconditions.checkArgument(this.index == -1);
            this.index = index;
            return this;
        }
        
        public Builder filterFamily(final EntityType type) {
            Preconditions.checkArgument(this.type == null);
            this.type = type;
            this.filterFamily = true;
            return this;
        }
        
        public Builder handlerNoRegister(final MetaHandler handler) {
            Preconditions.checkArgument(this.handler == null);
            this.handler = handler;
            return this;
        }
        
        public void handler(final MetaHandler handler) {
            Preconditions.checkArgument(this.handler == null);
            this.handler = handler;
            this.register();
        }
        
        public void cancel(final int index) {
            this.index = index;
            this.handler((event, meta) -> event.cancel());
        }
        
        public void toIndex(final int newIndex) {
            Preconditions.checkArgument(this.index != -1);
            this.handler((event, meta) -> event.setIndex(newIndex));
        }
        
        public void addIndex(final int index) {
            Preconditions.checkArgument(this.index == -1);
            this.handler((event, meta) -> {
                if (event.index() >= index) {
                    event.setIndex(event.index() + 1);
                }
            });
        }
        
        public void removeIndex(final int index) {
            Preconditions.checkArgument(this.index == -1);
            final int metaIndex;
            this.handler((event, meta) -> {
                metaIndex = event.index();
                if (metaIndex == index) {
                    event.cancel();
                }
                else if (metaIndex > index) {
                    event.setIndex(metaIndex - 1);
                }
            });
        }
        
        public void register() {
            this.rewriter.registerFilter(this.build());
        }
        
        public MetaFilter build() {
            return new MetaFilter(this.type, this.filterFamily, this.index, this.handler);
        }
    }
}
