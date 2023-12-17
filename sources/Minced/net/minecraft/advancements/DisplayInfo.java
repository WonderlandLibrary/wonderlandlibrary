// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.Item;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class DisplayInfo
{
    private final ITextComponent title;
    private final ITextComponent description;
    private final ItemStack icon;
    private final ResourceLocation background;
    private final FrameType frame;
    private final boolean showToast;
    private final boolean announceToChat;
    private final boolean hidden;
    private float x;
    private float y;
    
    public DisplayInfo(final ItemStack icon, final ITextComponent title, final ITextComponent description, @Nullable final ResourceLocation background, final FrameType frame, final boolean showToast, final boolean announceToChat, final boolean hidden) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.background = background;
        this.frame = frame;
        this.showToast = showToast;
        this.announceToChat = announceToChat;
        this.hidden = hidden;
    }
    
    public void setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public ITextComponent getTitle() {
        return this.title;
    }
    
    public ITextComponent getDescription() {
        return this.description;
    }
    
    public ItemStack getIcon() {
        return this.icon;
    }
    
    @Nullable
    public ResourceLocation getBackground() {
        return this.background;
    }
    
    public FrameType getFrame() {
        return this.frame;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public boolean shouldShowToast() {
        return this.showToast;
    }
    
    public boolean shouldAnnounceToChat() {
        return this.announceToChat;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public static DisplayInfo deserialize(final JsonObject object, final JsonDeserializationContext context) {
        final ITextComponent itextcomponent = JsonUtils.deserializeClass(object, "title", context, (Class<? extends ITextComponent>)ITextComponent.class);
        final ITextComponent itextcomponent2 = JsonUtils.deserializeClass(object, "description", context, (Class<? extends ITextComponent>)ITextComponent.class);
        if (itextcomponent != null && itextcomponent2 != null) {
            final ItemStack itemstack = deserializeIcon(JsonUtils.getJsonObject(object, "icon"));
            final ResourceLocation resourcelocation = object.has("background") ? new ResourceLocation(JsonUtils.getString(object, "background")) : null;
            final FrameType frametype = object.has("frame") ? FrameType.byName(JsonUtils.getString(object, "frame")) : FrameType.TASK;
            final boolean flag = JsonUtils.getBoolean(object, "show_toast", true);
            final boolean flag2 = JsonUtils.getBoolean(object, "announce_to_chat", true);
            final boolean flag3 = JsonUtils.getBoolean(object, "hidden", false);
            return new DisplayInfo(itemstack, itextcomponent, itextcomponent2, resourcelocation, frametype, flag, flag2, flag3);
        }
        throw new JsonSyntaxException("Both title and description must be set");
    }
    
    private static ItemStack deserializeIcon(final JsonObject object) {
        if (!object.has("item")) {
            throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add 'item' key)");
        }
        final Item item = JsonUtils.getItem(object, "item");
        final int i = JsonUtils.getInt(object, "data", 0);
        return new ItemStack(item, 1, i);
    }
    
    public void write(final PacketBuffer buf) {
        buf.writeTextComponent(this.title);
        buf.writeTextComponent(this.description);
        buf.writeItemStack(this.icon);
        buf.writeEnumValue(this.frame);
        int i = 0;
        if (this.background != null) {
            i |= 0x1;
        }
        if (this.showToast) {
            i |= 0x2;
        }
        if (this.hidden) {
            i |= 0x4;
        }
        buf.writeInt(i);
        if (this.background != null) {
            buf.writeResourceLocation(this.background);
        }
        buf.writeFloat(this.x);
        buf.writeFloat(this.y);
    }
    
    public static DisplayInfo read(final PacketBuffer buf) throws IOException {
        final ITextComponent itextcomponent = buf.readTextComponent();
        final ITextComponent itextcomponent2 = buf.readTextComponent();
        final ItemStack itemstack = buf.readItemStack();
        final FrameType frametype = buf.readEnumValue(FrameType.class);
        final int i = buf.readInt();
        final ResourceLocation resourcelocation = ((i & 0x1) != 0x0) ? buf.readResourceLocation() : null;
        final boolean flag = (i & 0x2) != 0x0;
        final boolean flag2 = (i & 0x4) != 0x0;
        final DisplayInfo displayinfo = new DisplayInfo(itemstack, itextcomponent, itextcomponent2, resourcelocation, frametype, flag, false, flag2);
        displayinfo.setPosition(buf.readFloat(), buf.readFloat());
        return displayinfo;
    }
}
