package net.ccbluex.liquidbounce.api.minecraft.client.multiplayer;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000f\n\n\u0000\n\u0000\n\b\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\b\n\n\n\b\b\bf\u000020J02020H&J\b0H&J02020H&J:02 0!2\"0#2\b$0%2&02'02(0)H&J*0+2,0!H&J -02 0.2\"0/2$0%H&J\b00+H&J010+220230240250260!H&R0X¦¢\f\b\"\bR\b0\tX¦¢\b\nR\f0\tX¦¢\f\b\r\"\bR0X¦¢\bR0X¦¢\bR0X¦¢\b¨7"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IPlayerControllerMP;", "", "blockHitDelay", "", "getBlockHitDelay", "()I", "setBlockHitDelay", "(I)V", "blockReachDistance", "", "getBlockReachDistance", "()F", "curBlockDamageMP", "getCurBlockDamageMP", "setCurBlockDamageMP", "(F)V", "currentGameType", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings$WGameType;", "getCurrentGameType", "()Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings$WGameType;", "isInCreativeMode", "", "()Z", "isNotCreative", "clickBlock", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "enumFacing", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IEnumFacing;", "extendedReach", "onPlayerDestroyBlock", "onPlayerRightClick", "playerSP", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "theWorld", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "itemStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "position", "sideOpposite", "hitVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "onStoppedUsingItem", "", "thePlayer", "sendUseItem", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/IEntityPlayer;", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "updateController", "windowClick", "windowId", "slot", "mouseButton", "mode", "player", "Pride"})
public interface IPlayerControllerMP {
    public boolean isNotCreative();

    public float getBlockReachDistance();

    @NotNull
    public IWorldSettings.WGameType getCurrentGameType();

    public boolean isInCreativeMode();

    public float getCurBlockDamageMP();

    public void setCurBlockDamageMP(float var1);

    public int getBlockHitDelay();

    public void setBlockHitDelay(int var1);

    public void windowClick(int var1, int var2, int var3, int var4, @NotNull IEntityPlayerSP var5);

    public void updateController();

    public boolean sendUseItem(@NotNull IEntityPlayer var1, @NotNull IWorld var2, @NotNull IItemStack var3);

    public boolean onPlayerRightClick(@NotNull IEntityPlayerSP var1, @NotNull IWorldClient var2, @Nullable IItemStack var3, @NotNull WBlockPos var4, @NotNull IEnumFacing var5, @NotNull WVec3 var6);

    public void onStoppedUsingItem(@NotNull IEntityPlayerSP var1);

    public boolean clickBlock(@NotNull WBlockPos var1, @NotNull IEnumFacing var2);

    public boolean onPlayerDestroyBlock(@NotNull WBlockPos var1, @NotNull IEnumFacing var2);

    public boolean extendedReach();
}
