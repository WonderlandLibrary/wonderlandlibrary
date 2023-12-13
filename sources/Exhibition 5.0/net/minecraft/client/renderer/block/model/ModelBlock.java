// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.model;

import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.HashMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Collections;
import java.io.StringReader;
import java.io.Reader;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import java.util.List;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class ModelBlock
{
    private static final Logger LOGGER;
    static final Gson SERIALIZER;
    private final List elements;
    private final boolean ambientOcclusion;
    private final boolean field_178322_i;
    private ItemCameraTransforms itemCameraTransforms;
    public String field_178317_b;
    protected final Map textures;
    protected ModelBlock parent;
    protected ResourceLocation parentLocation;
    private static final String __OBFID = "CL_00002503";
    
    public static ModelBlock deserialize(final Reader p_178307_0_) {
        return (ModelBlock)ModelBlock.SERIALIZER.fromJson(p_178307_0_, (Class)ModelBlock.class);
    }
    
    public static ModelBlock deserialize(final String p_178294_0_) {
        return deserialize(new StringReader(p_178294_0_));
    }
    
    protected ModelBlock(final List p_i46225_1_, final Map p_i46225_2_, final boolean p_i46225_3_, final boolean p_i46225_4_, final ItemCameraTransforms p_i46225_5_) {
        this(null, p_i46225_1_, p_i46225_2_, p_i46225_3_, p_i46225_4_, p_i46225_5_);
    }
    
    protected ModelBlock(final ResourceLocation p_i46226_1_, final Map p_i46226_2_, final boolean p_i46226_3_, final boolean p_i46226_4_, final ItemCameraTransforms p_i46226_5_) {
        this(p_i46226_1_, Collections.emptyList(), p_i46226_2_, p_i46226_3_, p_i46226_4_, p_i46226_5_);
    }
    
    private ModelBlock(final ResourceLocation p_i46227_1_, final List p_i46227_2_, final Map p_i46227_3_, final boolean p_i46227_4_, final boolean p_i46227_5_, final ItemCameraTransforms p_i46227_6_) {
        this.field_178317_b = "";
        this.elements = p_i46227_2_;
        this.field_178322_i = p_i46227_4_;
        this.ambientOcclusion = p_i46227_5_;
        this.textures = p_i46227_3_;
        this.parentLocation = p_i46227_1_;
        this.itemCameraTransforms = p_i46227_6_;
    }
    
    public List getElements() {
        return this.hasParent() ? this.parent.getElements() : this.elements;
    }
    
    private boolean hasParent() {
        return this.parent != null;
    }
    
    public boolean func_178309_b() {
        return this.hasParent() ? this.parent.func_178309_b() : this.field_178322_i;
    }
    
    public boolean isAmbientOcclusionEnabled() {
        return this.ambientOcclusion;
    }
    
    public boolean isResolved() {
        return this.parentLocation == null || (this.parent != null && this.parent.isResolved());
    }
    
    public void getParentFromMap(final Map p_178299_1_) {
        if (this.parentLocation != null) {
            this.parent = p_178299_1_.get(this.parentLocation);
        }
    }
    
    public boolean isTexturePresent(final String p_178300_1_) {
        return !"missingno".equals(this.resolveTextureName(p_178300_1_));
    }
    
    public String resolveTextureName(String p_178308_1_) {
        if (!this.isTextureName(p_178308_1_)) {
            p_178308_1_ = '#' + p_178308_1_;
        }
        return this.resolveTextureName(p_178308_1_, new Bookkeep(null));
    }
    
    private String resolveTextureName(final String p_178302_1_, final Bookkeep p_178302_2_) {
        if (!this.isTextureName(p_178302_1_)) {
            return p_178302_1_;
        }
        if (this == p_178302_2_.field_178323_b) {
            ModelBlock.LOGGER.warn("Unable to resolve texture due to upward reference: " + p_178302_1_ + " in " + this.field_178317_b);
            return "missingno";
        }
        String var3 = this.textures.get(p_178302_1_.substring(1));
        if (var3 == null && this.hasParent()) {
            var3 = this.parent.resolveTextureName(p_178302_1_, p_178302_2_);
        }
        p_178302_2_.field_178323_b = this;
        if (var3 != null && this.isTextureName(var3)) {
            var3 = p_178302_2_.model.resolveTextureName(var3, p_178302_2_);
        }
        return (var3 != null && !this.isTextureName(var3)) ? var3 : "missingno";
    }
    
    private boolean isTextureName(final String p_178304_1_) {
        return p_178304_1_.charAt(0) == '#';
    }
    
    public ResourceLocation getParentLocation() {
        return this.parentLocation;
    }
    
    public ModelBlock getRootModel() {
        return this.hasParent() ? this.parent.getRootModel() : this;
    }
    
    public ItemTransformVec3f getThirdPersonTransform() {
        return (this.parent != null && this.itemCameraTransforms.field_178355_b == ItemTransformVec3f.field_178366_a) ? this.parent.getThirdPersonTransform() : this.itemCameraTransforms.field_178355_b;
    }
    
    public ItemTransformVec3f getFirstPersonTransform() {
        return (this.parent != null && this.itemCameraTransforms.field_178356_c == ItemTransformVec3f.field_178366_a) ? this.parent.getFirstPersonTransform() : this.itemCameraTransforms.field_178356_c;
    }
    
    public ItemTransformVec3f getHeadTransform() {
        return (this.parent != null && this.itemCameraTransforms.field_178353_d == ItemTransformVec3f.field_178366_a) ? this.parent.getHeadTransform() : this.itemCameraTransforms.field_178353_d;
    }
    
    public ItemTransformVec3f getInGuiTransform() {
        return (this.parent != null && this.itemCameraTransforms.field_178354_e == ItemTransformVec3f.field_178366_a) ? this.parent.getInGuiTransform() : this.itemCameraTransforms.field_178354_e;
    }
    
    public static void func_178312_b(final Map p_178312_0_) {
        for (final ModelBlock var2 : p_178312_0_.values()) {
            try {
                for (ModelBlock var3 = var2.parent, var4 = var3.parent; var3 != var4; var3 = var3.parent, var4 = var4.parent.parent) {}
                throw new LoopException();
            }
            catch (NullPointerException ex) {
                continue;
            }
            break;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SERIALIZER = new GsonBuilder().registerTypeAdapter((Type)ModelBlock.class, (Object)new Deserializer()).registerTypeAdapter((Type)BlockPart.class, (Object)new BlockPart.Deserializer()).registerTypeAdapter((Type)BlockPartFace.class, (Object)new BlockPartFace.Deserializer()).registerTypeAdapter((Type)BlockFaceUV.class, (Object)new BlockFaceUV.Deserializer()).registerTypeAdapter((Type)ItemTransformVec3f.class, (Object)new ItemTransformVec3f.Deserializer()).registerTypeAdapter((Type)ItemCameraTransforms.class, (Object)new ItemCameraTransforms.Deserializer()).create();
    }
    
    final class Bookkeep
    {
        public final ModelBlock model;
        public ModelBlock field_178323_b;
        private static final String __OBFID = "CL_00002501";
        
        private Bookkeep() {
            this.model = ModelBlock.this;
        }
        
        Bookkeep(final ModelBlock this$0, final Object p_i46224_2_) {
            this(this$0);
        }
    }
    
    public static class Deserializer implements JsonDeserializer
    {
        private static final String __OBFID = "CL_00002500";
        
        public ModelBlock func_178327_a(final JsonElement p_178327_1_, final Type p_178327_2_, final JsonDeserializationContext p_178327_3_) {
            final JsonObject var4 = p_178327_1_.getAsJsonObject();
            final List var5 = this.getModelElements(p_178327_3_, var4);
            final String var6 = this.getParent(var4);
            final boolean var7 = StringUtils.isEmpty((CharSequence)var6);
            final boolean var8 = var5.isEmpty();
            if (var8 && var7) {
                throw new JsonParseException("BlockModel requires either elements or parent, found neither");
            }
            if (!var7 && !var8) {
                throw new JsonParseException("BlockModel requires either elements or parent, found both");
            }
            final Map var9 = this.getTextures(var4);
            final boolean var10 = this.getAmbientOcclusionEnabled(var4);
            ItemCameraTransforms var11 = ItemCameraTransforms.field_178357_a;
            if (var4.has("display")) {
                final JsonObject var12 = JsonUtils.getJsonObject(var4, "display");
                var11 = (ItemCameraTransforms)p_178327_3_.deserialize((JsonElement)var12, (Type)ItemCameraTransforms.class);
            }
            return var8 ? new ModelBlock(new ResourceLocation(var6), var9, var10, true, var11) : new ModelBlock(var5, var9, var10, true, var11);
        }
        
        private Map getTextures(final JsonObject p_178329_1_) {
            final HashMap var2 = Maps.newHashMap();
            if (p_178329_1_.has("textures")) {
                final JsonObject var3 = p_178329_1_.getAsJsonObject("textures");
                for (final Map.Entry var5 : var3.entrySet()) {
                    var2.put(var5.getKey(), var5.getValue().getAsString());
                }
            }
            return var2;
        }
        
        private String getParent(final JsonObject p_178326_1_) {
            return JsonUtils.getJsonObjectStringFieldValueOrDefault(p_178326_1_, "parent", "");
        }
        
        protected boolean getAmbientOcclusionEnabled(final JsonObject p_178328_1_) {
            return JsonUtils.getJsonObjectBooleanFieldValueOrDefault(p_178328_1_, "ambientocclusion", true);
        }
        
        protected List getModelElements(final JsonDeserializationContext p_178325_1_, final JsonObject p_178325_2_) {
            final ArrayList var3 = Lists.newArrayList();
            if (p_178325_2_.has("elements")) {
                for (final JsonElement var5 : JsonUtils.getJsonObjectJsonArrayField(p_178325_2_, "elements")) {
                    var3.add(p_178325_1_.deserialize(var5, (Type)BlockPart.class));
                }
            }
            return var3;
        }
        
        public Object deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) {
            return this.func_178327_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }
    
    public static class LoopException extends RuntimeException
    {
        private static final String __OBFID = "CL_00002499";
    }
}
