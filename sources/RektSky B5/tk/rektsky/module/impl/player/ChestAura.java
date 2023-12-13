/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import java.util.ArrayList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector2f;
import tk.rektsky.Client;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.module.settings.IntSetting;
import tk.rektsky.utils.block.BlockUtils;
import tk.rektsky.utils.combat.RotationUtil;
import tk.rektsky.utils.display.ColorUtil;

public class ChestAura
extends Module {
    public ArrayList<BlockPos> position = new ArrayList();
    public IntSetting rangeSetting = new IntSetting("Range", 1, 10, 4);
    private int ticks = 0;
    private int ticksSinceStartGame;
    private boolean notified;

    public ChestAura() {
        super("ChestAura", "Auto open chests in a specific range", 0, Category.PLAYER);
        this.registerSetting(this.rangeSetting);
    }

    @Override
    public void onEnable() {
        this.position.clear();
        Client.notifyWithClassName(new Notification.PopupMessage("ChestAura", "You can re-open all chests you've opened by re-enable this module!", ColorUtil.NotificationColors.GREEN.getColor(), ColorUtil.NotificationColors.GREEN.getTitleColor(), 20));
        this.ticks = 0;
        this.ticksSinceStartGame = 0;
        this.notified = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        RotationUtil.setRotating("chest_aura", false);
    }

    @Override
    public void onEvent(Event event) {
        Packet<?> p2;
        ++this.ticks;
        if (event instanceof WorldTickEvent) {
            if (this.ticks < this.ticksSinceStartGame + 40) {
                return;
            }
            if (this.ticks % 2 == 0) {
                for (BlockPos block : BlockUtils.searchForBlock("tile.chest", 3.0f)) {
                    if (this.position.contains(block)) continue;
                    this.position.add(block);
                    RotationUtil.setRotating("chest_aura", true);
                    Vector2f vector2f = RotationUtil.tryFaceBlock(block, EnumFacing.DOWN);
                    RotationUtil.setYaw(vector2f.x);
                    RotationUtil.setPitch(vector2f.y);
                    this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), block, EnumFacing.DOWN, new Vec3(block.getX(), block.getY(), block.getZ()));
                    break;
                }
            } else {
                RotationUtil.setRotating("chest_aura", false);
            }
        }
        if (event instanceof PacketReceiveEvent && (p2 = ((PacketReceiveEvent)event).getPacket()) instanceof S02PacketChat && ((S02PacketChat)p2).getChatComponent().getUnformattedText().contains("Utilize os itens encontrados em ba\u00fas em sua ilha para se equipar, eliminar outros jogadores e chegar ao centro do mapa, onde encontrar\u00e1 itens ainda melhores. \u00c9 um jogo r\u00e1pido de muito combate. Vencer\u00e1 o \u00faltimo jogador")) {
            this.ticksSinceStartGame = this.ticks;
            Client.notify(new Notification.PopupMessage("ChestAura", "Detect game started! ChestAura will work after 2 seconds!", ColorUtil.NotificationColors.YELLOW.getColor(), ColorUtil.NotificationColors.YELLOW.getTitleColor(), 40));
        }
    }
}

