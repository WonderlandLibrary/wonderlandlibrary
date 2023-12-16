// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.model_objects.specification;

import com.google.gson.JsonObject;
import se.michaelthelin.spotify.model_objects.IModelObject;
import java.util.Arrays;
import se.michaelthelin.spotify.enums.ModelObjectType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.michaelthelin.spotify.model_objects.AbstractModelObject;

@JsonDeserialize(builder = Builder.class)
public class Playlist extends AbstractModelObject
{
    private final Boolean collaborative;
    private final String description;
    private final ExternalUrl externalUrls;
    private final Followers followers;
    private final String href;
    private final String id;
    private final Image[] images;
    private final String name;
    private final User owner;
    private final Boolean publicAccess;
    private final String snapshotId;
    private final Paging<PlaylistTrack> tracks;
    private final ModelObjectType type;
    private final String uri;
    
    private Playlist(final Builder builder) {
        super(builder);
        this.collaborative = builder.collaborative;
        this.description = builder.description;
        this.externalUrls = builder.externalUrls;
        this.followers = builder.followers;
        this.href = builder.href;
        this.id = builder.id;
        this.images = builder.images;
        this.name = builder.name;
        this.owner = builder.owner;
        this.publicAccess = builder.publicAccess;
        this.snapshotId = builder.snapshotId;
        this.tracks = builder.tracks;
        this.type = builder.type;
        this.uri = builder.uri;
    }
    
    public Boolean getIsCollaborative() {
        return this.collaborative;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public ExternalUrl getExternalUrls() {
        return this.externalUrls;
    }
    
    public Followers getFollowers() {
        return this.followers;
    }
    
    public String getHref() {
        return this.href;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Image[] getImages() {
        return this.images;
    }
    
    public String getName() {
        return this.name;
    }
    
    public User getOwner() {
        return this.owner;
    }
    
    public Boolean getIsPublicAccess() {
        return this.publicAccess;
    }
    
    public String getSnapshotId() {
        return this.snapshotId;
    }
    
    public Paging<PlaylistTrack> getTracks() {
        return this.tracks;
    }
    
    public ModelObjectType getType() {
        return this.type;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/Paging;Ljava/lang/Boolean;Lse/michaelthelin/spotify/model_objects/specification/ExternalUrl;Lse/michaelthelin/spotify/model_objects/specification/Followers;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lse/michaelthelin/spotify/model_objects/specification/User;Ljava/lang/Boolean;Ljava/lang/String;Lse/michaelthelin/spotify/enums/ModelObjectType;Ljava/lang/String;)Ljava/lang/String;, this.name, this.description, this.tracks, this.collaborative, this.externalUrls, this.followers, this.href, this.id, Arrays.toString(this.images), this.owner, this.publicAccess, this.snapshotId, this.type, this.uri);
    }
    
    @Override
    public Builder builder() {
        return new Builder();
    }
    
    public static final class Builder extends AbstractModelObject.Builder
    {
        private Boolean collaborative;
        private String description;
        private ExternalUrl externalUrls;
        private Followers followers;
        private String href;
        private String id;
        private Image[] images;
        private String name;
        private User owner;
        private Boolean publicAccess;
        private String snapshotId;
        private Paging<PlaylistTrack> tracks;
        private ModelObjectType type;
        private String uri;
        
        public Builder setCollaborative(final Boolean collaborative) {
            this.collaborative = collaborative;
            return this;
        }
        
        public Builder setDescription(final String description) {
            this.description = description;
            return this;
        }
        
        public Builder setExternalUrls(final ExternalUrl externalUrls) {
            this.externalUrls = externalUrls;
            return this;
        }
        
        public Builder setFollowers(final Followers followers) {
            this.followers = followers;
            return this;
        }
        
        public Builder setHref(final String href) {
            this.href = href;
            return this;
        }
        
        public Builder setId(final String id) {
            this.id = id;
            return this;
        }
        
        public Builder setImages(final Image... images) {
            this.images = images;
            return this;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setOwner(final User owner) {
            this.owner = owner;
            return this;
        }
        
        public Builder setPublicAccess(final Boolean publicAccess) {
            this.publicAccess = publicAccess;
            return this;
        }
        
        public Builder setSnapshotId(final String snapshotId) {
            this.snapshotId = snapshotId;
            return this;
        }
        
        public Builder setTracks(final Paging<PlaylistTrack> tracks) {
            this.tracks = tracks;
            return this;
        }
        
        public Builder setType(final ModelObjectType type) {
            this.type = type;
            return this;
        }
        
        public Builder setUri(final String uri) {
            this.uri = uri;
            return this;
        }
        
        @Override
        public Playlist build() {
            return new Playlist(this, null);
        }
    }
    
    public static final class JsonUtil extends AbstractModelObject.JsonUtil<Playlist>
    {
        @Override
        public Playlist createModelObject(final JsonObject jsonObject) {
            if (jsonObject == null || jsonObject.isJsonNull()) {
                return null;
            }
            return new Playlist.Builder().setCollaborative(this.hasAndNotNull(jsonObject, "collaborative") ? Boolean.valueOf(jsonObject.get("collaborative").getAsBoolean()) : null).setDescription(this.hasAndNotNull(jsonObject, "description") ? jsonObject.get("description").getAsString() : null).setExternalUrls(this.hasAndNotNull(jsonObject, "external_urls") ? new ExternalUrl.JsonUtil().createModelObject(jsonObject.getAsJsonObject("external_urls")) : null).setFollowers(this.hasAndNotNull(jsonObject, "followers") ? new Followers.JsonUtil().createModelObject(jsonObject.getAsJsonObject("followers")) : null).setHref(this.hasAndNotNull(jsonObject, "href") ? jsonObject.get("href").getAsString() : null).setId(this.hasAndNotNull(jsonObject, "id") ? jsonObject.get("id").getAsString() : null).setImages((Image[])(this.hasAndNotNull(jsonObject, "images") ? ((Image[])new Image.JsonUtil().createModelObjectArray(jsonObject.getAsJsonArray("images"))) : null)).setName(this.hasAndNotNull(jsonObject, "name") ? jsonObject.get("name").getAsString() : null).setOwner(this.hasAndNotNull(jsonObject, "owner") ? new User.JsonUtil().createModelObject(jsonObject.getAsJsonObject("owner")) : null).setPublicAccess(this.hasAndNotNull(jsonObject, "public") ? Boolean.valueOf(jsonObject.get("public").getAsBoolean()) : null).setSnapshotId(this.hasAndNotNull(jsonObject, "snapshot_id") ? jsonObject.get("snapshot_id").getAsString() : null).setTracks(this.hasAndNotNull(jsonObject, "tracks") ? new PlaylistTrack.JsonUtil().createModelObjectPaging(jsonObject.getAsJsonObject("tracks")) : null).setType(this.hasAndNotNull(jsonObject, "type") ? ModelObjectType.keyOf(jsonObject.get("type").getAsString().toLowerCase()) : null).setUri(this.hasAndNotNull(jsonObject, "uri") ? jsonObject.get("uri").getAsString() : null).build();
        }
    }
}
