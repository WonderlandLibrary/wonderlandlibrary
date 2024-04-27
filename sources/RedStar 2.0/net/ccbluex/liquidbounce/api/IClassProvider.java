package net.ccbluex.liquidbounce.api;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.MinecraftVersion;
import net.ccbluex.liquidbounce.api.SupportsMinecraftVersions;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnchantmentType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.enums.MaterialType;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityOtherPlayerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IThreadDownloadImageData;
import net.ccbluex.liquidbounce.api.minecraft.client.render.WIImageBuffer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IDynamicTexture;
import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IGlStateManager;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.vertex.IVertexBuffer;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IGameSettings;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.nbt.IJsonToNBT;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagDouble;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagList;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagString;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketAnimation;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketClientStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCloseWindow;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketKeepAlive;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerBlockPlacement;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerPosLook;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketResourcePackStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.api.minecraft.stats.IStatBase;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.ISession;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.network.IPacketBuffer;
import net.ccbluex.liquidbounce.api.util.IWrappedFontRenderer;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000¸\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\b\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\n\n\b\b\n\n\n\u0000\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\n\u0000\n\n\n\u0000\n\n\n\u0000\n\n\bd\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\bf\u000020J8\n02\f0\r20\r20\r20\r20\r20\rH&J\b0H&J020H&J\b0H&J020H&J0202 0!H&J\"0#2$0%2&0'H&J (02)0*2+0,2-0.H&J/0021022304H&J50620H&J\b708H&J90:2;0<H&J=0>2\b?0!H'J:=0>2@0A2B02\bC0!2D0E2F0E2G0EH&J H0230I2J0A2K0LH&J M0:2N0E2O0E2;0<H&J8P0Q2R0\r2S0\r2T0\r2N0E2O0E2;0<H&J(U0:2R0\r2V0\r2T0\r2;0<H&JW02X0%H&JY\b0Z2?0[H'J\\\u001a0]2102230^H&J\\\u001a0]2_022`0aH&Jb0c2X0%H&Jd0e2f0g2h0%H&Ji0j2k0lH&Jm0n2o0p2q0rH&J8s0t2u02R02S02v02w02X0%H&J(s0t2u02R02S02X0%H&J x0y2z0y2{0|2}0~H&J0y20yH&J0y20yH&J0y20y2\b0H&J<02u02\b02R02S02v02w0H&J0y20yH&J<02u02\b02R02S02v02w0H&J020%2\b0H&J\n0H&J0!2\b0H&J0!2\b0H&J%0!2\b02020H&J\n0H&J02h0\rH&J\n0H&J020%H&J 0'2\b¡0¢H&J$£0¤2u02¥02¦0H&J§0¨2©0%H&Jª0«2\b¬0­H&J®0¯2{0|H&J.°0±2²0%2³0%2´0%2µ0%H&J5¶0·2\n¸0¹2º0%2\n»0¨2\b¼0½H&J¾02\b¿0ÀH&JÁ0Â2\b¿0ÃH&JÄ0L2\b¿0ÅH&J\nÆ0ÇH&JÈ02\b¿0ÉH&JÊ0Ë2\b¿0ÌH&JÍ0Î2\b¿0ÏH&JÐ0Ñ2\b¿0ÒH&JÓ0­2\b¿0ÔH&JÕ0<2\tÖ0H&J×0<2\tÖ0H&JØ0<2\tÖ0H&JÙ0<2\tÖ0H&JÚ0<2\tÖ0H&JÛ0<2\tÖ0H&JÜ0<2\tÖ0H&JÝ0<2\tÖ0H&JÞ0<2\tÖ0H&Jß0<2\tÖ0H&Jà0<2\tÖ0H&Já0<2\tÖ0H&Jâ0<2\tÖ0H&Jã0<2\tÖ0H&Jä0<2\tÖ0H&Jå0<2\tÖ0H&Jæ0<2\tÖ0H&Jç0<2\tÖ0H&Jè0<2\tÖ0H&Jé0<2\tÖ0H&Jê0<2\tÖ0H&Jë0<2\tÖ0H&Jì0<2\tÖ0H&Jí0<2\tÖ0H&Jî0<2\tÖ0H&Jï0<2\tÖ0H&Jð0<2\tÖ0H&Jñ0<2\tÖ0H&Jò0<2\tÖ0H&Jó0<2\tÖ0H&Jô0<2\tÖ0H&Jõ0<2\tÖ0H&Jö0<2\tÖ0H&J÷0<2\tÖ0H&Jø0<2\tÖ0H&Jù0<2\tÖ0H&Jú0<2\tÖ0H&Jû0<2\tÖ0H&Jü0<2\tÖ0H&Jý0<2\tÖ0H&Jþ0<2\tÖ0H&Jÿ0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J0<2\tÖ0H&J 0<2\tÖ0H&J¡0<2\tÖ0H&J¢0<2\tÖ0H&J£0<2\tÖ0H&J¤0<2\tÖ0H&J¥0<2\tÖ0H&J¦0<2\tÖ0H&J§0<2\tÖ0H&J¨0<2\tÖ0H&J©0<2\tÖ0H&Jª0<2\tÖ0H&J«0<2\tÖ0H&J¬0<2\tÖ0H&J­0<2\tÖ0H&J®0<2\tÖ0H&J¯0<2\tÖ0H&J°0<2\tÖ0H&J±0<2\tÖ0H&J²0<2\tÖ0H&J³0<2\tÖ0H&J´0<2\tÖ0H&Jµ0<2\tÖ0H&J¶0<2\tÖ0H&J·0<2\tÖ0H&J¸0¹2²0%2\bº0»H&J¼02\b½0¾H&J¿0y2\bÀ0ÁH&JGÂ0¹2\bÃ0Ä2{0|2v02w02Å02Æ02Ç0H&R0X¦¢\bR0X¦¢\b\b\t¨È"}, d2={"Lnet/ccbluex/liquidbounce/api/IClassProvider;", "", "jsonToNBTInstance", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/IJsonToNBT;", "getJsonToNBTInstance", "()Lnet/ccbluex/liquidbounce/api/minecraft/nbt/IJsonToNBT;", "tessellatorInstance", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/ITessellator;", "getTessellatorInstance", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/ITessellator;", "createAxisAlignedBB", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "minX", "", "minY", "minZ", "maxX", "maxY", "maxZ", "createCPacketAnimation", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketAnimation;", "createCPacketClientStatus", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus;", "state", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus$WEnumState;", "createCPacketCloseWindow", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCloseWindow;", "windowId", "", "createCPacketCreativeInventoryAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "slot", "itemStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "createCPacketCustomPayload", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCustomPayload;", "channel", "", "payload", "Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;", "createCPacketEncryptionResponse", "secretKey", "Ljavax/crypto/SecretKey;", "publicKey", "Ljava/security/PublicKey;", "VerifyToken", "", "createCPacketEntityAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction;", "player", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "wAction", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction$WAction;", "createCPacketHeldItemChange", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketHeldItemChange;", "createCPacketKeepAlive", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketKeepAlive;", "createCPacketPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "onGround", "", "createCPacketPlayerBlockPlacement", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerBlockPlacement;", "stack", "positionIn", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "placedBlockDirectionIn", "stackIn", "facingXIn", "", "facingYIn", "facingZIn", "createCPacketPlayerDigging", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "pos", "facing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "createCPacketPlayerLook", "yaw", "pitch", "createCPacketPlayerPosLook", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerPosLook;", "x", "y", "z", "createCPacketPlayerPosition", "negativeInfinity", "createCPacketTabComplete", "text", "createCPacketTryUseItem", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/enums/WEnumHand;", "createCPacketUseEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "entity", "positionVector", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "createChatComponentText", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "createClickEvent", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "action", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent$WAction;", "value", "createDynamicTexture", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/IDynamicTexture;", "image", "Ljava/awt/image/BufferedImage;", "createEntityOtherPlayerMP", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityOtherPlayerMP;", "world", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "GameProfile", "Lcom/mojang/authlib/GameProfile;", "createGuiButton", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiButton;", "id", "width", "height", "createGuiConnecting", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "parent", "mc", "Lnet/ccbluex/liquidbounce/api/minecraft/client/IMinecraft;", "serverData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "createGuiModList", "parentScreen", "createGuiMultiplayer", "createGuiOptions", "gameSettings", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "createGuiPasswordField", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiTextField;", "iFontRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "createGuiSelectWorld", "createGuiTextField", "createICPacketResourcePackStatus", "hash", "status", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus$WAction;", "createItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "createItemStack", "blockEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "item", "amount", "meta", "createNBTTagCompound", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "createNBTTagDouble", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagDouble;", "createNBTTagList", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagList;", "createNBTTagString", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagString;", "string", "createPacketBuffer", "buffer", "Lio/netty/buffer/ByteBuf;", "createPotionEffect", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotionEffect;", "time", "strength", "createResourceLocation", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "resourceName", "createSafeVertexBuffer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/vertex/IVertexBuffer;", "vertexFormat", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/vertex/IVertexFormat;", "createScaledResolution", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IScaledResolution;", "createSession", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ISession;", "name", "uuid", "accessToken", "accountType", "createThreadDownloadImageData", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/IThreadDownloadImageData;", "cacheFileIn", "Ljava/io/File;", "imageUrlIn", "textureResourceLocation", "imageBufferIn", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/WIImageBuffer;", "getBlockEnum", "type", "Lnet/ccbluex/liquidbounce/api/enums/BlockType;", "getEnchantmentEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/enchantments/IEnchantment;", "Lnet/ccbluex/liquidbounce/api/enums/EnchantmentType;", "getEnumFacing", "Lnet/ccbluex/liquidbounce/api/enums/EnumFacingType;", "getGlStateManager", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IGlStateManager;", "getItemEnum", "Lnet/ccbluex/liquidbounce/api/enums/ItemType;", "getMaterialEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/block/material/IMaterial;", "Lnet/ccbluex/liquidbounce/api/enums/MaterialType;", "getPotionEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/PotionType;", "getStatEnum", "Lnet/ccbluex/liquidbounce/api/minecraft/stats/IStatBase;", "Lnet/ccbluex/liquidbounce/api/enums/StatType;", "getVertexFormatEnum", "Lnet/ccbluex/liquidbounce/api/enums/WDefaultVertexFormats;", "isBlockAir", "obj", "isBlockBedrock", "isBlockBush", "isBlockCactus", "isBlockCarpet", "isBlockFence", "isBlockLadder", "isBlockLiquid", "isBlockPane", "isBlockSlab", "isBlockSlime", "isBlockSnow", "isBlockStairs", "isBlockVine", "isCPacketAnimation", "isCPacketChatMessage", "isCPacketClientStatus", "isCPacketCloseWindow", "isCPacketCustomPayload", "isCPacketEntityAction", "isCPacketHandshake", "isCPacketHeldItemChange", "isCPacketKeepAlive", "isCPacketPlayer", "isCPacketPlayerBlockPlacement", "isCPacketPlayerDigging", "isCPacketPlayerLook", "isCPacketPlayerPosLook", "isCPacketPlayerPosition", "isCPacketTryUseItem", "isCPacketUseEntity", "isClickGui", "isEntityAnimal", "isEntityArmorStand", "isEntityArrow", "isEntityBat", "isEntityBoat", "isEntityDragon", "isEntityFallingBlock", "isEntityFireball", "isEntityGhast", "isEntityGolem", "isEntityItem", "isEntityLivingBase", "isEntityMinecart", "isEntityMinecartChest", "isEntityMob", "isEntityPlayer", "isEntityShulker", "isEntitySlime", "isEntitySquid", "isEntityTNTPrimed", "isEntityVillager", "isGuiChat", "isGuiChest", "isGuiContainer", "isGuiGameOver", "isGuiHudDesigner", "isGuiIngameMenu", "isGuiInventory", "isItemAir", "isItemAppleGold", "isItemArmor", "isItemAxe", "isItemBed", "isItemBlock", "isItemBoat", "isItemBow", "isItemBucket", "isItemBucketMilk", "isItemEgg", "isItemEnchantedBook", "isItemEnderPearl", "isItemFishingRod", "isItemFood", "isItemMinecart", "isItemPickaxe", "isItemPotion", "isItemSnowball", "isItemSword", "isItemTool", "isSPacketAnimation", "isSPacketChat", "isSPacketCloseWindow", "isSPacketEntity", "isSPacketEntityVelocity", "isSPacketExplosion", "isSPacketPlayerPosLook", "isSPacketResourcePackSend", "isSPacketTabComplete", "isSPacketTimeUpdate", "isSPacketWindowItems", "isTileEntityChest", "isTileEntityDispenser", "isTileEntityEnderChest", "isTileEntityFurnace", "isTileEntityHopper", "isTileEntityShulkerBox", "wrapCreativeTab", "", "wrappedCreativeTabs", "Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;", "wrapFontRenderer", "fontRenderer", "Lnet/ccbluex/liquidbounce/api/util/IWrappedFontRenderer;", "wrapGuiScreen", "clickGui", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;", "wrapGuiSlot", "wrappedGuiSlot", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiSlot;", "top", "bottom", "slotHeight", "Pride"})
public interface IClassProvider {
    @NotNull
    public ITessellator getTessellatorInstance();

    @NotNull
    public IJsonToNBT getJsonToNBTInstance();

    @NotNull
    public IResourceLocation createResourceLocation(@NotNull String var1);

    @NotNull
    public IThreadDownloadImageData createThreadDownloadImageData(@Nullable File var1, @NotNull String var2, @Nullable IResourceLocation var3, @NotNull WIImageBuffer var4);

    @NotNull
    public IPacketBuffer createPacketBuffer(@NotNull ByteBuf var1);

    @NotNull
    public IIChatComponent createChatComponentText(@NotNull String var1);

    @NotNull
    public IClickEvent createClickEvent(@NotNull IClickEvent.WAction var1, @NotNull String var2);

    @NotNull
    public IGuiTextField createGuiTextField(int var1, @NotNull IFontRenderer var2, int var3, int var4, int var5, int var6);

    @NotNull
    public IGuiTextField createGuiPasswordField(int var1, @NotNull IFontRenderer var2, int var3, int var4, int var5, int var6);

    @NotNull
    public IGuiButton createGuiButton(int var1, int var2, int var3, int var4, int var5, @NotNull String var6);

    @NotNull
    public IGuiButton createGuiButton(int var1, int var2, int var3, @NotNull String var4);

    @NotNull
    public ISession createSession(@NotNull String var1, @NotNull String var2, @NotNull String var3, @NotNull String var4);

    @NotNull
    public IDynamicTexture createDynamicTexture(@NotNull BufferedImage var1);

    @NotNull
    public IItem createItem();

    @NotNull
    public IItemStack createItemStack(@NotNull IItem var1, int var2, int var3);

    @NotNull
    public IItemStack createItemStack(@NotNull IItem var1);

    @NotNull
    public IItemStack createItemStack(@NotNull IBlock var1);

    @NotNull
    public IAxisAlignedBB createAxisAlignedBB(double var1, double var3, double var5, double var7, double var9, double var11);

    @NotNull
    public IScaledResolution createScaledResolution(@NotNull IMinecraft var1);

    @NotNull
    public INBTTagCompound createNBTTagCompound();

    @NotNull
    public INBTTagList createNBTTagList();

    @NotNull
    public INBTTagString createNBTTagString(@NotNull String var1);

    @NotNull
    public INBTTagDouble createNBTTagDouble(double var1);

    @NotNull
    public IEntityOtherPlayerMP createEntityOtherPlayerMP(@NotNull IWorldClient var1, @NotNull GameProfile var2);

    @NotNull
    public IPotionEffect createPotionEffect(int var1, int var2, int var3);

    @NotNull
    public IGuiScreen createGuiOptions(@NotNull IGuiScreen var1, @NotNull IGameSettings var2);

    @NotNull
    public IGuiScreen createGuiSelectWorld(@NotNull IGuiScreen var1);

    @NotNull
    public IGuiScreen createGuiMultiplayer(@NotNull IGuiScreen var1);

    @NotNull
    public IGuiScreen createGuiModList(@NotNull IGuiScreen var1);

    @NotNull
    public IGuiScreen createGuiConnecting(@NotNull IGuiScreen var1, @NotNull IMinecraft var2, @NotNull IServerData var3);

    @NotNull
    public ICPacketPlayerBlockPlacement createCPacketPlayerBlockPlacement(@NotNull WBlockPos var1, int var2, @Nullable IItemStack var3, float var4, float var5, float var6);

    @NotNull
    public ICPacketHeldItemChange createCPacketHeldItemChange(int var1);

    @SupportsMinecraftVersions(value={MinecraftVersion.MC_1_12})
    @NotNull
    public ICPacketPlayerBlockPlacement createCPacketPlayerBlockPlacement(@Nullable IItemStack var1);

    @NotNull
    public ICPacketPlayerPosLook createCPacketPlayerPosLook(double var1, double var3, double var5, float var7, float var8, boolean var9);

    @NotNull
    public ICPacketClientStatus createCPacketClientStatus(@NotNull ICPacketClientStatus.WEnumState var1);

    @NotNull
    public IPacket createCPacketPlayerDigging(@NotNull ICPacketPlayerDigging.WAction var1, @NotNull WBlockPos var2, @NotNull IEnumFacing var3);

    @NotNull
    public ICPacketPlayer createCPacketPlayerPosition(double var1, double var3, double var5, boolean var7);

    @NotNull
    public IPacket createICPacketResourcePackStatus(@NotNull String var1, @NotNull ICPacketResourcePackStatus.WAction var2);

    @NotNull
    public ICPacketPlayer createCPacketPlayerLook(float var1, float var2, boolean var3);

    @NotNull
    public ICPacketUseEntity createCPacketUseEntity(@NotNull IEntity var1, @NotNull ICPacketUseEntity.WAction var2);

    @NotNull
    public ICPacketUseEntity createCPacketUseEntity(@NotNull IEntity var1, @NotNull WVec3 var2);

    @NotNull
    public IPacket createCPacketCreativeInventoryAction(int var1, @NotNull IItemStack var2);

    @NotNull
    public ICPacketEntityAction createCPacketEntityAction(@NotNull IEntity var1, @NotNull ICPacketEntityAction.WAction var2);

    @NotNull
    public ICPacketCustomPayload createCPacketCustomPayload(@NotNull String var1, @NotNull IPacketBuffer var2);

    @NotNull
    public ICPacketCloseWindow createCPacketCloseWindow(int var1);

    @NotNull
    public ICPacketCloseWindow createCPacketCloseWindow();

    @NotNull
    public ICPacketPlayer createCPacketPlayer(boolean var1);

    @NotNull
    public IPacket createCPacketTabComplete(@NotNull String var1);

    @NotNull
    public ICPacketAnimation createCPacketAnimation();

    @NotNull
    public ICPacketKeepAlive createCPacketKeepAlive();

    public boolean isEntityAnimal(@Nullable Object var1);

    public boolean isEntitySquid(@Nullable Object var1);

    public boolean isEntityBat(@Nullable Object var1);

    public boolean isEntityGolem(@Nullable Object var1);

    public boolean isEntityMob(@Nullable Object var1);

    public boolean isEntityVillager(@Nullable Object var1);

    public boolean isEntitySlime(@Nullable Object var1);

    public boolean isEntityGhast(@Nullable Object var1);

    public boolean isEntityDragon(@Nullable Object var1);

    public boolean isEntityFireball(@Nullable Object var1);

    public boolean isEntityLivingBase(@Nullable Object var1);

    public boolean isEntityPlayer(@Nullable Object var1);

    public boolean isEntityArmorStand(@Nullable Object var1);

    public boolean isEntityTNTPrimed(@Nullable Object var1);

    public boolean isEntityBoat(@Nullable Object var1);

    public boolean isEntityMinecart(@Nullable Object var1);

    public boolean isEntityItem(@Nullable Object var1);

    public boolean isEntityArrow(@Nullable Object var1);

    public boolean isEntityFallingBlock(@Nullable Object var1);

    public boolean isEntityMinecartChest(@Nullable Object var1);

    public boolean isEntityShulker(@Nullable Object var1);

    public boolean isTileEntityChest(@Nullable Object var1);

    public boolean isTileEntityEnderChest(@Nullable Object var1);

    public boolean isTileEntityFurnace(@Nullable Object var1);

    public boolean isTileEntityDispenser(@Nullable Object var1);

    public boolean isTileEntityHopper(@Nullable Object var1);

    public boolean isSPacketChat(@Nullable Object var1);

    public boolean isSPacketEntity(@Nullable Object var1);

    public boolean isSPacketResourcePackSend(@Nullable Object var1);

    public boolean isSPacketPlayerPosLook(@Nullable Object var1);

    public boolean isSPacketAnimation(@Nullable Object var1);

    public boolean isSPacketEntityVelocity(@Nullable Object var1);

    public boolean isSPacketExplosion(@Nullable Object var1);

    public boolean isSPacketCloseWindow(@Nullable Object var1);

    public boolean isSPacketTabComplete(@Nullable Object var1);

    public boolean isCPacketPlayer(@Nullable Object var1);

    public boolean isCPacketPlayerBlockPlacement(@Nullable Object var1);

    public boolean isCPacketUseEntity(@Nullable Object var1);

    public boolean isCPacketCloseWindow(@Nullable Object var1);

    public boolean isCPacketChatMessage(@Nullable Object var1);

    public boolean isCPacketKeepAlive(@Nullable Object var1);

    public boolean isCPacketPlayerPosition(@Nullable Object var1);

    public boolean isCPacketPlayerPosLook(@Nullable Object var1);

    public boolean isCPacketClientStatus(@Nullable Object var1);

    public boolean isCPacketAnimation(@Nullable Object var1);

    public boolean isCPacketEntityAction(@Nullable Object var1);

    public boolean isSPacketWindowItems(@Nullable Object var1);

    public boolean isCPacketHeldItemChange(@Nullable Object var1);

    public boolean isCPacketPlayerLook(@Nullable Object var1);

    public boolean isCPacketCustomPayload(@Nullable Object var1);

    public boolean isCPacketHandshake(@Nullable Object var1);

    public boolean isCPacketPlayerDigging(@Nullable Object var1);

    public boolean isCPacketTryUseItem(@Nullable Object var1);

    public boolean isItemSword(@Nullable Object var1);

    public boolean isItemTool(@Nullable Object var1);

    public boolean isItemArmor(@Nullable Object var1);

    public boolean isItemPotion(@Nullable Object var1);

    public boolean isItemBlock(@Nullable Object var1);

    public boolean isItemBow(@Nullable Object var1);

    public boolean isItemBucket(@Nullable Object var1);

    public boolean isItemFood(@Nullable Object var1);

    public boolean isItemBucketMilk(@Nullable Object var1);

    public boolean isItemPickaxe(@Nullable Object var1);

    public boolean isItemAxe(@Nullable Object var1);

    public boolean isItemBed(@Nullable Object var1);

    public boolean isItemEnderPearl(@Nullable Object var1);

    public boolean isItemEnchantedBook(@Nullable Object var1);

    public boolean isItemBoat(@Nullable Object var1);

    public boolean isItemMinecart(@Nullable Object var1);

    public boolean isItemAppleGold(@Nullable Object var1);

    public boolean isItemSnowball(@Nullable Object var1);

    public boolean isItemEgg(@Nullable Object var1);

    public boolean isItemFishingRod(@Nullable Object var1);

    public boolean isItemAir(@Nullable Object var1);

    public boolean isBlockAir(@Nullable Object var1);

    public boolean isBlockFence(@Nullable Object var1);

    public boolean isBlockSnow(@Nullable Object var1);

    public boolean isBlockLadder(@Nullable Object var1);

    public boolean isBlockVine(@Nullable Object var1);

    public boolean isBlockSlime(@Nullable Object var1);

    public boolean isBlockSlab(@Nullable Object var1);

    public boolean isBlockStairs(@Nullable Object var1);

    public boolean isBlockCarpet(@Nullable Object var1);

    public boolean isBlockPane(@Nullable Object var1);

    public boolean isBlockLiquid(@Nullable Object var1);

    public boolean isBlockCactus(@Nullable Object var1);

    public boolean isBlockBedrock(@Nullable Object var1);

    public boolean isBlockBush(@Nullable Object var1);

    public boolean isGuiInventory(@Nullable Object var1);

    public boolean isGuiContainer(@Nullable Object var1);

    public boolean isGuiGameOver(@Nullable Object var1);

    public boolean isGuiChat(@Nullable Object var1);

    public boolean isGuiIngameMenu(@Nullable Object var1);

    public boolean isGuiChest(@Nullable Object var1);

    public boolean isGuiHudDesigner(@Nullable Object var1);

    public boolean isClickGui(@Nullable Object var1);

    @NotNull
    public IPotion getPotionEnum(@NotNull PotionType var1);

    @NotNull
    public IEnumFacing getEnumFacing(@NotNull EnumFacingType var1);

    @NotNull
    public IBlock getBlockEnum(@NotNull BlockType var1);

    @NotNull
    public IMaterial getMaterialEnum(@NotNull MaterialType var1);

    @NotNull
    public IStatBase getStatEnum(@NotNull StatType var1);

    @NotNull
    public IItem getItemEnum(@NotNull ItemType var1);

    @NotNull
    public IEnchantment getEnchantmentEnum(@NotNull EnchantmentType var1);

    @NotNull
    public IVertexFormat getVertexFormatEnum(@NotNull WDefaultVertexFormats var1);

    @NotNull
    public IFontRenderer wrapFontRenderer(@NotNull IWrappedFontRenderer var1);

    @NotNull
    public IGuiScreen wrapGuiScreen(@NotNull WrappedGuiScreen var1);

    @NotNull
    public IVertexBuffer createSafeVertexBuffer(@NotNull IVertexFormat var1);

    public void wrapCreativeTab(@NotNull String var1, @NotNull WrappedCreativeTabs var2);

    public void wrapGuiSlot(@NotNull WrappedGuiSlot var1, @NotNull IMinecraft var2, int var3, int var4, int var5, int var6, int var7);

    @NotNull
    public IGlStateManager getGlStateManager();

    @NotNull
    public IPacket createCPacketEncryptionResponse(@NotNull SecretKey var1, @NotNull PublicKey var2, @NotNull byte[] var3);

    @SupportsMinecraftVersions(value={MinecraftVersion.MC_1_12})
    @NotNull
    public PacketImpl<?> createCPacketTryUseItem(@NotNull WEnumHand var1);

    public boolean isTileEntityShulkerBox(@Nullable Object var1);

    public boolean isSPacketTimeUpdate(@Nullable Object var1);
}
