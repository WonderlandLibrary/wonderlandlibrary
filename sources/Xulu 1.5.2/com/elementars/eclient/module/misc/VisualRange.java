// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import com.elementars.eclient.command.Command;
import com.elementars.eclient.friend.Friends;
import net.minecraft.entity.Entity;
import java.util.function.Consumer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import java.util.function.Predicate;
import com.elementars.eclient.util.EntityUtil;
import net.minecraft.client.Minecraft;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.util.ColorTextUtils;
import java.util.Collection;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import java.util.ArrayList;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class VisualRange extends Module
{
    private final Value<Boolean> mode;
    private final Value<Boolean> sf;
    private final Value<String> message;
    private final Value<Integer> delayN;
    private final Value<Boolean> ignoreFriends;
    private final Value<Boolean> vr;
    private final Value<Boolean> watermark;
    private final Value<Boolean> color;
    private final Value<String> selectcolor;
    public ArrayList<String> names;
    public ArrayList<String> names2;
    public ArrayList<String> removal;
    private int delay;
    
    public VisualRange() {
        super("VisualRange", "Alerts people appearing in your visual range", 0, Category.MISC, true);
        this.mode = this.register(new Value<Boolean>("Send Message", this, false));
        this.sf = this.register(new Value<Boolean>("No Friend Send", this, false));
        this.message = this.register(new Value<String>("Message", this, "hello uwu", new ArrayList<String>(Arrays.asList("Change this in the settings"))));
        this.delayN = this.register(new Value<Integer>("Delay", this, 15, 1, 30));
        this.ignoreFriends = this.register(new Value<Boolean>("Ignore Friends", this, false));
        this.vr = this.register(new Value<Boolean>("VisualRange", this, true));
        this.watermark = this.register(new Value<Boolean>("Watermark", this, true));
        this.color = this.register(new Value<Boolean>("Color", this, false));
        this.selectcolor = this.register(new Value<String>("Color Pick", this, "LightGreen", ColorTextUtils.colors));
        this.names = new ArrayList<String>();
        this.names2 = new ArrayList<String>();
        this.removal = new ArrayList<String>();
    }
    
    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        this.names2.clear();
        Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> !EntityUtil.isFakeLocalPlayer(entity)).filter(entity -> entity instanceof EntityPlayer).filter(entity -> !(entity instanceof EntityPlayerSP)).forEach(this::testName);
        this.testLeave();
    }
    
    private void testName(final Entity entityIn) {
        this.names2.add(entityIn.getName());
        if (!this.names.contains(entityIn.getName())) {
            this.sendMessage(entityIn);
            this.names.add(entityIn.getName());
        }
    }
    
    private void testLeave() {
        this.names.forEach(name -> {
            if (!this.names2.contains(name)) {
                this.removal.add(name);
            }
            return;
        });
        this.removal.forEach(name -> this.names.remove(name));
        this.removal.clear();
    }
    
    private void sendMessage(final Entity entityIn) {
        if (this.mode.getValue() && this.delay == 0) {
            if (this.sf.getValue() && Friends.isFriend(entityIn.getName())) {
                return;
            }
            VisualRange.mc.player.sendChatMessage("/msg " + entityIn.getName() + " " + this.message.getValue());
            this.delay = this.delayN.getValue() * 20;
        }
        if (this.vr.getValue()) {
            if (this.ignoreFriends.getValue() && Friends.isFriend(entityIn.getName())) {
                return;
            }
            if (this.watermark.getValue()) {
                if (this.color.getValue()) {
                    Command.sendChatMessage(ColorTextUtils.getColor(this.selectcolor.getValue()) + entityIn.getName() + " entered visual range");
                }
                else {
                    Command.sendChatMessage(entityIn.getName() + " entered visual range");
                }
            }
            else if (this.color.getValue()) {
                Command.sendRawChatMessage(ColorTextUtils.getColor(this.selectcolor.getValue()) + entityIn.getName() + " entered visual range");
            }
            else {
                Command.sendRawChatMessage(entityIn.getName() + " entered visual range");
            }
        }
    }
}
