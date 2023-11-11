package me.nyan.flush.altmanager;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;

public class AccountInfo {
    private final String username;
    private final Type type;
    private String password;
    private Date creationDate;
    private String displayName;
    private BufferedImage faceImage;
    private UUID uuid = null;

    private DynamicTexture faceTexture;
    private ResourceLocation textureId;

    public AccountInfo(String username, String password, Type type, Date creationDate, String displayName, BufferedImage faceImage) {
        this.username = username;
        this.password = StringUtils.isEmpty(password) ? null : password;
        this.type = type;
        this.creationDate = creationDate;
        this.displayName = displayName;
        this.faceImage = faceImage;
    }

    public AccountInfo(String username, String password, Type type, Date creationDate, String displayName) {
        this(username, password, type, creationDate, displayName, null);
    }

    public AccountInfo(String username, String password, Type type) {
        this(username, password, type, new Date(), null, null);
    }

    public void downloadFace() throws IOException {
        faceImage = ImageIO.read(new URL("https://crafatar.com/avatars/" + uuid.toString() + "?overlay&size=16"));
        faceTexture = null;
        textureId = new ResourceLocation(uuid.toString());
    }

    public boolean isCracked() {
        return password == null;
    }

    public boolean isValidCracked() {
        for (char ch : username.toCharArray())
            if (!(Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_')) {
                return false;
            }
        return username.length() >= 3 && username.length() <= 16;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public DynamicTexture getFaceTexture() {
        return faceTexture;
    }

    public ResourceLocation getTextureId() {
        return textureId;
    }

    public BufferedImage getFaceImage() {
        return faceImage;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setFaceTexture(DynamicTexture faceTexture) {
        this.faceTexture = faceTexture;
    }

    @Override
    public String toString() {
        return displayName == null ? username : displayName;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        MOJANG,
        MICROSOFT
    }
}