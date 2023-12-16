// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Serializable;

public interface IModelObject extends Serializable
{
    Builder builder();
    
    public interface IJsonUtil<T>
    {
        boolean hasAndNotNull(final JsonObject p0, final String p1);
        
        T createModelObject(final JsonObject p0);
        
        T createModelObject(final String p0);
        
        T[] createModelObjectArray(final JsonArray p0);
        
        T[] createModelObjectArray(final String p0);
        
        T[] createModelObjectArray(final String p0, final String p1);
        
         <X> X[] createModelObjectArray(final JsonArray p0, final Class<X> p1);
        
        Paging<T> createModelObjectPaging(final JsonObject p0);
        
        Paging<T> createModelObjectPaging(final String p0);
        
        Paging<T> createModelObjectPaging(final String p0, final String p1);
        
        PagingCursorbased<T> createModelObjectPagingCursorbased(final JsonObject p0);
        
        PagingCursorbased<T> createModelObjectPagingCursorbased(final String p0);
        
        PagingCursorbased<T> createModelObjectPagingCursorbased(final String p0, final String p1);
    }
    
    @JsonPOJOBuilder(withPrefix = "set")
    public interface Builder
    {
        IModelObject build();
    }
}
