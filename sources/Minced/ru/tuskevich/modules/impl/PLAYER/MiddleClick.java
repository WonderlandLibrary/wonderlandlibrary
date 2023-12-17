// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import ru.tuskevich.event.EventTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.network.NetHandlerPlayClient;
import ru.tuskevich.util.chat.ChatUtility;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import ru.tuskevich.util.math.RayCastUtility;
import org.lwjgl.util.vector.Vector2f;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayer;
import ru.tuskevich.Minced;
import ru.tuskevich.modules.impl.COMBAT.KillAura;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import ru.tuskevich.util.world.InventoryUtility;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.MouseEvent;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "MiddleClick", desc = "\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class MiddleClick extends Module
{
    public BooleanSetting friend;
    public BooleanSetting pearl;
    
    public MiddleClick() {
        this.friend = new BooleanSetting("Friend", false);
        this.pearl = new BooleanSetting("Pearl", true);
        this.add(this.friend, this.pearl);
    }
    
    @EventTarget
    public void onMouse(final MouseEvent event) {
        if (this.pearl.get() && event.button == 2) {
            final Minecraft mc = MiddleClick.mc;
            if (Minecraft.player.getCooldownTracker().getCooldown(Items.ENDER_PEARL, 1.0f) == 0.0f && InventoryUtility.getPearls() >= 0) {
                final Minecraft mc2 = MiddleClick.mc;
                Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtility.getPearls()));
                final KillAura instance = KillAura.instance;
                if (KillAura.target != null && Minced.getInstance().manager.getModule(KillAura.class).state) {
                    final Minecraft mc3 = MiddleClick.mc;
                    final NetHandlerPlayClient connection = Minecraft.player.connection;
                    final Minecraft mc4 = MiddleClick.mc;
                    final float rotationYaw = Minecraft.player.rotationYaw;
                    final Minecraft mc5 = MiddleClick.mc;
                    connection.sendPacket(new CPacketPlayer.Rotation(rotationYaw, Minecraft.player.rotationPitch, true));
                }
                final Minecraft mc6 = MiddleClick.mc;
                Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                final Minecraft mc7 = MiddleClick.mc;
                final NetHandlerPlayClient connection2 = Minecraft.player.connection;
                final Minecraft mc8 = MiddleClick.mc;
                connection2.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
            }
        }
        if (this.friend.get() && event.button == 2) {
            final Minecraft mc9 = MiddleClick.mc;
            final float rotationYaw2 = Minecraft.player.rotationYaw;
            final Minecraft mc10 = MiddleClick.mc;
            final EntityLivingBase pointedEntity;
            final EntityLivingBase entity = pointedEntity = RayCastUtility.getPointedEntity(new Vector2f(rotationYaw2, Minecraft.player.rotationPitch), 10.0, 2.0f, false);
            final Minecraft mc11 = MiddleClick.mc;
            if (pointedEntity != Minecraft.player && entity instanceof EntityPlayer) {
                if (Minced.getInstance().friendManager.isFriend(entity.getName())) {
                    Minced.getInstance().friendManager.removeFriend(entity.getName());
                    ChatUtility.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + entity.getName() + " as Friend!");
                }
                else {
                    Minced.getInstance().friendManager.addFriend(entity.getName());
                    ChatUtility.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + entity.getName() + " as Friend!");
                }
            }
        }
    }
}
