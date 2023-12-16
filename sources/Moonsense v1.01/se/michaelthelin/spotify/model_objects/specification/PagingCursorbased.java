// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import java.lang.reflect.ParameterizedType;
import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class PagingCursorbased<T> extends AbstractModelObject
{
    private final String href;
    private final T[] items;
    private final Integer limit;
    private final String next;
    private final Cursor[] cursors;
    private final Integer total;
    
    private PagingCursorbased(final Builder<T> builder) {
        super(builder);
        this.href = ((Builder<Object>)builder).href;
        this.items = (T[])((Builder<Object>)builder).items;
        this.limit = ((Builder<Object>)builder).limit;
        this.next = ((Builder<Object>)builder).next;
        this.cursors = ((Builder<Object>)builder).cursors;
        this.total = ((Builder<Object>)builder).total;
    }
    
    public String getHref() {
        return this.href;
    }
    
    public T[] getItems() {
        return this.items;
    }
    
    public Integer getLimit() {
        return this.limit;
    }
    
    public String getNext() {
        return this.next;
    }
    
    public Cursor[] getCursors() {
        return this.cursors;
    }
    
    public Integer getTotal() {
        return this.total;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/String;, this.href, Arrays.toString(this.items), this.limit, this.next, Arrays.toString(this.cursors), this.total);
    }
    
    @Override
    public Builder<T> builder() {
        return new Builder<T>();
    }
    
    public static final class Builder<T> extends AbstractModelObject.Builder
    {
        private String href;
        private T[] items;
        private Integer limit;
        private String next;
        private Cursor[] cursors;
        private Integer total;
        
        public Builder<T> setHref(final String href) {
            this.href = href;
            return this;
        }
        
        public Builder<T> setItems(final T[] items) {
            this.items = items;
            return this;
        }
        
        public Builder<T> setLimit(final Integer limit) {
            this.limit = limit;
            return this;
        }
        
        public Builder<T> setNext(final String next) {
            this.next = next;
            return this;
        }
        
        public Builder<T> setCursors(final Cursor... cursors) {
            this.cursors = cursors;
            return this;
        }
        
        public Builder<T> setTotal(final Integer total) {
            this.total = total;
            return this;
        }
        
        @Override
        public PagingCursorbased<T> build() {
            return new PagingCursorbased<T>(this, null);
        }
    }
    
    public static final class JsonUtil<X> extends AbstractModelObject.JsonUtil<PagingCursorbased<X>>
    {
        @Override
        public PagingCursorbased<X> createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new PagingCursorbased.Builder<X>().setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setItems((X[])(this.hasAndNotNull(jsonObject, "items") ? this.createModelObjectArray(jsonObject.getAsJsonArray("items"), (Class<X>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]) : null)).setLimit(this.hasAndNotNull(jsonObject, "limit") ? Integer.valueOf(jsonObject.get("limit").getAsInt()) : null).setNext(this.hasAndNotNull(jsonObject, "next") ? jsonObject.get("next").getAsString() : null).setCursors((Cursor[])(this.hasAndNotNull(jsonObject, "cursors") ? ((Cursor[])new Cursor.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("cursors"))) : null)).setTotal(this.hasAndNotNull(jsonObject, "total") ? Integer.valueOf(jsonObject.get("total").getAsInt()) : null).build();
        }
    }
}
