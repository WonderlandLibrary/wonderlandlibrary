// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.login.system;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

public class Account
{
    private String email;
    private String password;
    private String name;
    private boolean banned;
    private ResourceLocation head;
    
    public Account(final String email, final String password, final String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    
    public Account() {
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isBanned() {
        return this.banned;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setBanned(final boolean banned) {
        this.banned = banned;
    }
    
    public JsonObject toJson() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", this.email);
        jsonObject.addProperty("password", this.password);
        jsonObject.addProperty("name", this.name);
        jsonObject.addProperty("banned", Boolean.valueOf(this.banned));
        return jsonObject;
    }
    
    public void fromJson(final JsonObject json) {
        this.email = json.get("email").getAsString();
        this.password = json.get("password").getAsString();
        this.name = json.get("name").getAsString();
        this.banned = json.get("banned").getAsBoolean();
    }
    
    public ResourceLocation getHead() {
        this.loadHead();
        return this.head;
    }
    
    private void loadHead() {
        if (this.head == null) {
            this.head = new ResourceLocation("heads/" + this.name);
            final ThreadDownloadImageData textureHead = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s", this.name), null, null);
            Minecraft.getMinecraft().getTextureManager().loadTexture(this.head, textureHead);
        }
    }
}
