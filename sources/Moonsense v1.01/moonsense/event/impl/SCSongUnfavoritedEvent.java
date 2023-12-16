// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import moonsense.event.SCEvent;

public class SCSongUnfavoritedEvent extends SCEvent
{
    public final String id;
    public final IPlaylistItem track;
    public final AlbumSimplified album;
    
    public SCSongUnfavoritedEvent(final String id, final IPlaylistItem t, final AlbumSimplified a) {
        this.id = id;
        this.track = t;
        this.album = a;
    }
}
