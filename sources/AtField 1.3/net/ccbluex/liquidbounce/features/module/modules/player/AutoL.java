/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.Random;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.autoL;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AutoL", description="AutoL. ", category=ModuleCategory.PLAYER)
public final class AutoL
extends Module {
    private int index;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Chinese", "English", "zhuboMessage", "yurluMessage", "YuJiangJun", "Ikun", "L", "None", "Text"}, "None");
    private final MSTimer msTimer;
    private final IntegerValue delay;
    private int kill;
    private IEntity target;
    private Random R;
    private final TextValue lobbyValue = new TextValue("Text", "AtField 2023/Genuine edition");
    private final BoolValue prefix = new BoolValue("@", true);
    private String[] englishabuse;
    private String[] abuse;

    public AutoL() {
        this.delay = new IntegerValue("Delay", 100, 0, 2000);
        this.R = new Random();
        this.abuse = new String[]{"AtField 2023/Genuine edition"};
        this.englishabuse = new String[]{"You are loser"};
        this.msTimer = new MSTimer();
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        this.target = attackEvent.getTargetEntity();
    }

    public final TextValue getLobbyValue() {
        return this.lobbyValue;
    }

    public final void setIndex(int n) {
        this.index = n;
    }

    public final void setR(Random random) {
        this.R = random;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        if (this.target != null) {
            IEntity iEntity = this.target;
            if (iEntity == null) {
                Intrinsics.throwNpe();
            }
            if (iEntity.isDead() && this.msTimer.hasTimePassed(((Number)this.delay.get()).intValue())) {
                int n = this.index;
                this.index = n + 1;
                switch ((String)this.modeValue.get()) {
                    case "Chinese": {
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + ":" + this.abuse[this.R.nextInt(this.abuse.length)]);
                        ++this.kill;
                        this.target = null;
                        break;
                    }
                    case "YuJiangJun": {
                        if (this.index > autoL.YuJiangJun.length) {
                            this.index = 0;
                        }
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + " " + autoL.YuJiangJun[this.index]);
                        ++this.kill;
                        this.target = null;
                        break;
                    }
                    case "zhuboMessage": {
                        if (this.index > autoL.zhuboMessage.length) {
                            this.index = 0;
                        }
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + " " + autoL.zhuboMessage[this.index]);
                        ++this.kill;
                        this.target = null;
                        break;
                    }
                    case "yurluMessage": {
                        if (this.index > autoL.yurluMessage.length) {
                            this.index = 0;
                        }
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + " " + autoL.yurluMessage[this.index]);
                        ++this.kill;
                        this.target = null;
                        break;
                    }
                    case "Ikun": {
                        if (this.index > autoL.Ikun.length) {
                            this.index = 0;
                        }
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + " " + autoL.Ikun[this.index]);
                        ++this.kill;
                        this.target = null;
                        break;
                    }
                    case "English": {
                        ++this.kill;
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + "  " + this.englishabuse[this.R.nextInt(this.englishabuse.length)]);
                        this.target = null;
                        break;
                    }
                    case "L": {
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        StringBuilder stringBuilder = new StringBuilder().append((Boolean)this.prefix.get() != false ? "@" : "").append(" L ");
                        IEntity iEntity2 = this.target;
                        if (iEntity2 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(stringBuilder.append(iEntity2.getName()).toString());
                        ++this.kill;
                        this.target = null;
                        break;
                    }
                    case "None": {
                        ++this.kill;
                        this.target = null;
                        break;
                    }
                    case "Text": {
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        StringBuilder stringBuilder = new StringBuilder().append((Boolean)this.prefix.get() != false ? "@" : "").append((String)this.lobbyValue.get()).append(" [");
                        IEntity iEntity3 = this.target;
                        if (iEntity3 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(stringBuilder.append(iEntity3.getName()).append("]").toString());
                        ++this.kill;
                        this.target = null;
                        break;
                    }
                }
                this.msTimer.reset();
            }
        }
    }

    public final int getKill() {
        return this.kill;
    }

    public final void AutoL() {
        this.setState(true);
    }

    public final Random getR() {
        return this.R;
    }

    public final String[] getAbuse() {
        return this.abuse;
    }

    public final void setKill(int n) {
        this.kill = n;
    }

    @Override
    public String getTag() {
        return "Kills%" + this.kill;
    }

    public final void setAbuse(String[] stringArray) {
        this.abuse = stringArray;
    }

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final String[] getEnglishabuse() {
        return this.englishabuse;
    }

    public final int getIndex() {
        return this.index;
    }

    public final void setEnglishabuse(String[] stringArray) {
        this.englishabuse = stringArray;
    }

    public final MSTimer getMsTimer() {
        return this.msTimer;
    }
}

