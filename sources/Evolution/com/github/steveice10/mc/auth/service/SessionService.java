/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.auth.service;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.profile.ProfileException;
import com.github.steveice10.mc.auth.exception.profile.ProfileLookupException;
import com.github.steveice10.mc.auth.exception.profile.ProfileNotFoundException;
import com.github.steveice10.mc.auth.exception.property.ProfileTextureException;
import com.github.steveice10.mc.auth.exception.property.PropertyException;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.util.Base64;
import com.github.steveice10.mc.auth.util.HTTP;
import com.github.steveice10.mc.auth.util.UUIDSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;

public class SessionService {
    private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
    private static final String JOIN_URL = "https://sessionserver.mojang.com/session/minecraft/join";
    private static final String HAS_JOINED_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined";
    private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile";
    private static final PublicKey SIGNATURE_KEY;
    private static final Gson GSON;
    private Proxy proxy;

    static {
        InputStream in = null;
        try {
            try {
                in = SessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int length = -1;
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
                SIGNATURE_KEY = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(out.toByteArray()));
            }
            catch (Exception e) {
                throw new ExceptionInInitializerError("Missing/invalid yggdrasil public key.");
            }
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException iOException) {}
            }
        }
        GSON = new GsonBuilder().registerTypeAdapter((Type)((Object)UUID.class), new UUIDSerializer()).create();
    }

    public SessionService() {
        this(Proxy.NO_PROXY);
    }

    public SessionService(Proxy proxy) {
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        }
        this.proxy = proxy;
    }

    public String getServerId(String base, PublicKey publicKey, SecretKey secretKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(base.getBytes(StandardCharsets.ISO_8859_1));
            digest.update(secretKey.getEncoded());
            digest.update(publicKey.getEncoded());
            return new BigInteger(digest.digest()).toString(16);
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Server ID hash algorithm unavailable.", e);
        }
    }

    public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws RequestException {
        JoinServerRequest request = new JoinServerRequest(authenticationToken, profile.getId(), serverId);
        HTTP.makeRequest(this.proxy, JOIN_URL, request, null);
    }

    public GameProfile getProfileByServer(String name, String serverId) throws RequestException {
        HasJoinedResponse response = HTTP.makeRequest(this.proxy, "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + name + "&serverId=" + serverId, null, HasJoinedResponse.class);
        if (response != null && response.id != null) {
            GameProfile result = new GameProfile(response.id, name);
            if (response.properties != null) {
                result.getProperties().addAll(response.properties);
            }
            return result;
        }
        return null;
    }

    public GameProfile fillProfileProperties(GameProfile profile) throws ProfileException {
        if (profile.getId() == null) {
            return profile;
        }
        try {
            MinecraftProfileResponse response = HTTP.makeRequest(this.proxy, "https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDSerializer.fromUUID(profile.getId()) + "?unsigned=false", null, MinecraftProfileResponse.class);
            if (response == null) {
                throw new ProfileNotFoundException("Couldn't fetch profile properties for " + profile + " as the profile does not exist.");
            }
            if (response.properties != null) {
                profile.getProperties().addAll(response.properties);
            }
            return profile;
        }
        catch (RequestException e) {
            throw new ProfileLookupException("Couldn't look up profile properties for " + profile + ".", e);
        }
    }

    public GameProfile fillProfileTextures(GameProfile profile, boolean requireSecure) throws PropertyException {
        GameProfile.Property textures = profile.getProperty("textures");
        if (textures != null) {
            MinecraftTexturesPayload result;
            if (!textures.hasSignature()) {
                throw new ProfileTextureException("Signature is missing from textures payload.");
            }
            if (!textures.isSignatureValid(SIGNATURE_KEY)) {
                throw new ProfileTextureException("Textures payload has been tampered with. (signature invalid)");
            }
            try {
                String json = new String(Base64.decode(textures.getValue().getBytes("UTF-8")));
                result = GSON.fromJson(json, MinecraftTexturesPayload.class);
            }
            catch (Exception e) {
                throw new ProfileTextureException("Could not decode texture payload.", e);
            }
            if (result.profileId == null || !result.profileId.equals(profile.getId())) {
                throw new ProfileTextureException("Decrypted textures payload was for another user. (expected id " + profile.getId() + " but was for " + result.profileId + ")");
            }
            if (result.profileName == null || !result.profileName.equals(profile.getName())) {
                throw new ProfileTextureException("Decrypted textures payload was for another user. (expected name " + profile.getName() + " but was for " + result.profileName + ")");
            }
            if (requireSecure) {
                if (result.isPublic) {
                    throw new ProfileTextureException("Decrypted textures payload was public when secure data is required.");
                }
                Calendar limit = Calendar.getInstance();
                limit.add(5, -1);
                Date validFrom = new Date(result.timestamp);
                if (validFrom.before(limit.getTime())) {
                    throw new ProfileTextureException("Decrypted textures payload is too old. (" + validFrom + ", needs to be at least " + limit + ")");
                }
            }
            if (result.textures != null) {
                profile.getTextures().putAll(result.textures);
            }
        }
        return profile;
    }

    public String toString() {
        return "SessionService{}";
    }

    private static class HasJoinedResponse {
        public UUID id;
        public List<GameProfile.Property> properties;

        private HasJoinedResponse() {
        }
    }

    private static class JoinServerRequest {
        private String accessToken;
        private UUID selectedProfile;
        private String serverId;

        protected JoinServerRequest(String accessToken, UUID selectedProfile, String serverId) {
            this.accessToken = accessToken;
            this.selectedProfile = selectedProfile;
            this.serverId = serverId;
        }
    }

    private static class MinecraftProfileResponse {
        public UUID id;
        public String name;
        public List<GameProfile.Property> properties;

        private MinecraftProfileResponse() {
        }
    }

    private static class MinecraftTexturesPayload {
        public long timestamp;
        public UUID profileId;
        public String profileName;
        public boolean isPublic;
        public Map<GameProfile.TextureType, GameProfile.Texture> textures;

        private MinecraftTexturesPayload() {
        }
    }
}

