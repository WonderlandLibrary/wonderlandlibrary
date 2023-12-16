// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects;

import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.specification.ExternalUrl;

public interface IPlaylistItem extends IModelObject
{
    Integer getDurationMs();
    
    ExternalUrl getExternalUrls();
    
    String getHref();
    
    String getId();
    
    String getName();
    
    ModelObjectType getType();
    
    String getUri();
}
