// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.helpers.world.EntityHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import ru.fluger.client.event.events.impl.render.EventRenderBlock;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.misc.ChatHelper;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import java.util.Iterator;
import ru.fluger.client.helpers.world.BlockHelper;
import ru.fluger.client.settings.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import ru.fluger.client.feature.impl.Type;
import java.util.List;
import java.util.ArrayList;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class XRay extends Feature
{
    public static int done;
    public static int all;
    public static BooleanSetting bypass;
    public static BooleanSetting diamond;
    public static BooleanSetting gold;
    public static BooleanSetting iron;
    public static BooleanSetting emerald;
    public static BooleanSetting redstone;
    public static BooleanSetting lapis;
    public static BooleanSetting coal;
    private final NumberSetting checkSpeed;
    private final NumberSetting renderDist;
    private final NumberSetting rxz;
    private final NumberSetting ry;
    public static ArrayList<et> ores;
    private final ArrayList<et> toCheck;
    private final List<fq> blocks;
    
    public XRay() {
        super("XRay", "\u0418\u043a\u0441\u0440\u0435\u0439, \u043f\u043e\u0437\u0432\u043e\u043b\u044f\u044e\u0449\u0438\u0439 \u043e\u0431\u043e\u0439\u0442\u0438 \u0430\u043d\u0442\u0438-\u0438\u043a\u0441\u0440\u0435\u0439 \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430\u0445", Type.Misc);
        this.toCheck = new ArrayList<et>();
        this.blocks = new CopyOnWriteArrayList<fq>();
        XRay.bypass = new BooleanSetting("Bypass", false, () -> true);
        this.renderDist = new NumberSetting("Render Distance", 35.0f, 15.0f, 150.0f, 5.0f, () -> !XRay.bypass.getCurrentValue());
        XRay.diamond = new BooleanSetting("Diamond", aow.c(56), true, () -> true);
        XRay.gold = new BooleanSetting("Gold", aow.c(14), false, () -> true);
        XRay.iron = new BooleanSetting("Iron", aow.c(15), false, () -> true);
        XRay.emerald = new BooleanSetting("Emerald", aow.c(129), false, () -> true);
        XRay.redstone = new BooleanSetting("Redstone", aow.c(73), false, () -> true);
        XRay.lapis = new BooleanSetting("Lapis", aow.c(21), false, () -> true);
        XRay.coal = new BooleanSetting("Coal", aow.c(16), false, () -> true);
        this.checkSpeed = new NumberSetting("CheckSpeed", 4.0f, 1.0f, 10.0f, 1.0f, XRay.bypass::getCurrentValue);
        this.rxz = new NumberSetting("Radius XZ", 20.0f, 5.0f, 200.0f, 1.0f, XRay.bypass::getCurrentValue);
        this.ry = new NumberSetting("Radius Y", 6.0f, 2.0f, 50.0f, 1.0f, XRay.bypass::getCurrentValue);
        this.addSettings(this.renderDist, XRay.bypass, this.checkSpeed, this.rxz, this.ry, XRay.diamond, XRay.gold, XRay.iron, XRay.emerald, XRay.redstone, XRay.lapis, XRay.coal);
    }
    
    private boolean isEnabledOre(final int id) {
        int check = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        int check7 = 0;
        final int check8 = 0;
        if (XRay.diamond.getCurrentValue() && id != 0) {
            check = 56;
        }
        if (XRay.gold.getCurrentValue() && id != 0) {
            check2 = 14;
        }
        if (XRay.iron.getCurrentValue() && id != 0) {
            check3 = 15;
        }
        if (XRay.emerald.getCurrentValue() && id != 0) {
            check4 = 129;
        }
        if (XRay.redstone.getCurrentValue() && id != 0) {
            check5 = 73;
        }
        if (XRay.coal.getCurrentValue() && id != 0) {
            check6 = 16;
        }
        if (XRay.lapis.getCurrentValue() && id != 0) {
            check7 = 21;
        }
        return id != 0 && (id == check || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7);
    }
    
    private ArrayList<et> getBlocks(final int x, final int y, final int z) {
        final et min = new et(XRay.mc.h.p - x, XRay.mc.h.q - y, XRay.mc.h.r - z);
        final et max = new et(XRay.mc.h.p + x, XRay.mc.h.q + y, XRay.mc.h.r + z);
        return BlockHelper.getAllInBox(min, max);
    }
    
    @Override
    public void onEnable() {
        if (XRay.bypass.getCurrentValue()) {
            final int radXZ = (int)this.rxz.getCurrentValue();
            final int radY = (int)this.ry.getCurrentValue();
            final ArrayList<et> blockPositions = this.getBlocks(radXZ, radY, radXZ);
            for (final et pos : blockPositions) {
                final awt state = BlockHelper.getState(pos);
                if (!this.isCheckableOre(aow.a(state.u()))) {
                    continue;
                }
                this.toCheck.add(pos);
            }
            XRay.all = this.toCheck.size();
            XRay.done = 0;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        XRay.mc.g.a();
        super.onDisable();
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        final String string;
        final String allDone = string = ((XRay.done == XRay.all) ? ("Done: " + XRay.all) : ("" + XRay.done + " / " + XRay.all));
        if (XRay.bypass.getCurrentValue()) {
            this.setSuffix(allDone);
            for (int i = 0; i < this.checkSpeed.getCurrentValue(); ++i) {
                if (this.toCheck.size() < 1) {
                    return;
                }
                final et pos = this.toCheck.remove(0);
                ++XRay.done;
                XRay.mc.h.d.a(new lp(lp.a.a, pos, fa.d));
            }
        }
        if (!XRay.bypass.getCurrentValue() && XRay.mc.h.T % 100 == 0) {
            XRay.mc.g.a();
            ChatHelper.addChatMessage("Reloading chunks...");
        }
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (XRay.bypass.getCurrentValue()) {
            if (e.getPacket() instanceof ij) {
                final ij p = (ij)e.getPacket();
                if (this.isEnabledOre(aow.a(p.a().u())) && !XRay.mc.f.d(p.b())) {
                    XRay.ores.add(p.b());
                }
            }
            else if (e.getPacket() instanceof io) {
                final io p2 = (io)e.getPacket();
                for (final io.a dat : p2.a()) {
                    if (this.isEnabledOre(aow.a(dat.c().u()))) {
                        if (!XRay.mc.f.d(dat.a())) {
                            XRay.ores.add(dat.a());
                        }
                    }
                }
            }
        }
    }
    
    @EventTarget
    public void onRenderBlock(final EventRenderBlock event) {
        if (!XRay.bypass.getCurrentValue()) {
            final et pos = event.getPos();
            final awt blockState = event.getState();
            if (this.isEnabledOre(aow.a(blockState.u()))) {
                final fq vec3i = new fq(pos.p(), pos.q(), pos.r());
                this.blocks.add(vec3i);
            }
        }
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        if (XRay.bypass.getCurrentValue()) {
            for (final et pos : XRay.ores) {
                final awt state = BlockHelper.getState(pos);
                final aow stateBlock = state.u();
                if (aow.a(stateBlock) != 0 && aow.a(stateBlock) == 56 && XRay.diamond.getCurrentValue() && aow.a(stateBlock) == 56) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 255.0f, 255.0f);
                }
                if (aow.a(stateBlock) != 0 && aow.a(stateBlock) == 14 && XRay.gold.getCurrentValue() && aow.a(stateBlock) == 14) {
                    RenderHelper.blockEspFrame(pos, 255.0f, 215.0f, 0.0f);
                }
                if (aow.a(stateBlock) != 0 && aow.a(stateBlock) == 15 && XRay.iron.getCurrentValue() && aow.a(stateBlock) == 15) {
                    RenderHelper.blockEspFrame(pos, 213.0f, 213.0f, 213.0f);
                }
                if (aow.a(stateBlock) != 0 && aow.a(stateBlock) == 129 && XRay.emerald.getCurrentValue() && aow.a(stateBlock) == 129) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 255.0f, 0.0f);
                }
                if (aow.a(stateBlock) != 0 && aow.a(stateBlock) == 73 && XRay.redstone.getCurrentValue() && aow.a(stateBlock) == 73) {
                    RenderHelper.blockEspFrame(pos, 255.0f, 0.0f, 0.0f);
                }
                if (aow.a(stateBlock) != 0 && aow.a(stateBlock) == 16 && XRay.coal.getCurrentValue() && aow.a(stateBlock) == 16) {
                    RenderHelper.blockEspFrame(pos, 0.0f, 0.0f, 0.0f);
                }
                if (aow.a(stateBlock) != 0 && aow.a(stateBlock) == 21 && XRay.lapis.getCurrentValue() && aow.a(stateBlock) == 21) {
                    RenderHelper.blockEspFrame(pos, 38.0f, 97.0f, 156.0f);
                }
            }
        }
        else {
            for (final fq neededBlock : this.blocks) {
                final et pos2 = new et(neededBlock);
                final awt state2 = BlockHelper.getState(pos2);
                final aow stateBlock2 = state2.u();
                final aow block = XRay.mc.f.o(pos2).u();
                if (block instanceof aom) {
                    continue;
                }
                if (EntityHelper.getDistance(XRay.mc.h.p, XRay.mc.h.r, neededBlock.p(), neededBlock.r()) > this.renderDist.getCurrentValue()) {
                    this.blocks.remove(neededBlock);
                }
                else {
                    if (aow.a(stateBlock2) != 0 && aow.a(stateBlock2) == 56 && XRay.diamond.getCurrentValue() && aow.a(stateBlock2) == 56) {
                        RenderHelper.blockEspFrame(pos2, 0.0f, 255.0f, 255.0f);
                    }
                    if (aow.a(stateBlock2) != 0 && aow.a(stateBlock2) == 14 && XRay.gold.getCurrentValue() && aow.a(stateBlock2) == 14) {
                        RenderHelper.blockEspFrame(pos2, 255.0f, 215.0f, 0.0f);
                    }
                    if (aow.a(stateBlock2) != 0 && aow.a(stateBlock2) == 15 && XRay.iron.getCurrentValue() && aow.a(stateBlock2) == 15) {
                        RenderHelper.blockEspFrame(pos2, 213.0f, 213.0f, 213.0f);
                    }
                    if (aow.a(stateBlock2) != 0 && aow.a(stateBlock2) == 129 && XRay.emerald.getCurrentValue() && aow.a(stateBlock2) == 129) {
                        RenderHelper.blockEspFrame(pos2, 0.0f, 255.0f, 0.0f);
                    }
                    if (aow.a(stateBlock2) != 0 && aow.a(stateBlock2) == 73 && XRay.redstone.getCurrentValue() && aow.a(stateBlock2) == 73) {
                        RenderHelper.blockEspFrame(pos2, 255.0f, 0.0f, 0.0f);
                    }
                    if (aow.a(stateBlock2) != 0 && aow.a(stateBlock2) == 16 && XRay.coal.getCurrentValue() && aow.a(stateBlock2) == 16) {
                        RenderHelper.blockEspFrame(pos2, 0.0f, 0.0f, 0.0f);
                    }
                    if (aow.a(stateBlock2) == 0 || aow.a(stateBlock2) != 21 || !XRay.lapis.getCurrentValue() || aow.a(stateBlock2) != 21) {
                        continue;
                    }
                    RenderHelper.blockEspFrame(pos2, 38.0f, 97.0f, 156.0f);
                }
            }
        }
    }
    
    private boolean isCheckableOre(final int id) {
        int check = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        int check7 = 0;
        final int check8 = 0;
        if (XRay.diamond.getCurrentValue() && id != 0) {
            check = 56;
        }
        if (XRay.gold.getCurrentValue() && id != 0) {
            check2 = 14;
        }
        if (XRay.iron.getCurrentValue() && id != 0) {
            check3 = 15;
        }
        if (XRay.emerald.getCurrentValue() && id != 0) {
            check4 = 129;
        }
        if (XRay.redstone.getCurrentValue() && id != 0) {
            check5 = 73;
        }
        if (XRay.coal.getCurrentValue() && id != 0) {
            check6 = 16;
        }
        if (XRay.lapis.getCurrentValue() && id != 0) {
            check7 = 21;
        }
        return id != 0 && (id == check || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7);
    }
    
    static {
        XRay.ores = new ArrayList<et>();
    }
}
