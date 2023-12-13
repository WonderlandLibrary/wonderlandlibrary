// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.util.Iterator;
import java.awt.Color;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.util.ListHelper;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.friend.Friends;
import java.util.List;
import com.elementars.eclient.Xulu;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class TheGoons extends Element
{
    private final Value<Boolean> rainbow;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    
    public TheGoons() {
        super("TheGoons");
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.red = this.register(new Value<Integer>("Friend Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Friend Green", this, 255, 0, 255));
        this.blue = this.register(new Value<Integer>("Friend Blue", this, 255, 0, 255));
    }
    
    @Override
    public void onRender() {
        int color = ColorUtil.getClickGUIColor().getRGB();
        if (this.rainbow.getValue()) {
            color = Xulu.rgb;
        }
        final List<String> friends = (List<String>)TheGoons.mc.world.playerEntities.stream().filter(player -> Friends.isFriend(player.getName())).map(EntityPlayer::getName).collect(Collectors.toList());
        friends.add("The Goons");
        this.width = TheGoons.fontRenderer.getStringWidth(ListHelper.longest(friends)) + 2;
        this.height = (TheGoons.fontRenderer.FONT_HEIGHT + 1) * friends.size() + 1;
        float yCount = (float)this.y;
        if (Xulu.CustomFont) {
            Xulu.cFontRenderer.drawStringWithShadow("The Goons", (float)this.x + 1.0f, yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
        }
        else {
            Wrapper.getMinecraft().fontRenderer.drawStringWithShadow("The Goons", (float)this.x + 1.0f, yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
        }
        yCount += 10.0f;
        for (final EntityPlayer entityPlayer : TheGoons.mc.world.playerEntities) {
            if (entityPlayer.getName().equals(TheGoons.mc.player.getName())) {
                continue;
            }
            if (!Friends.isFriend(entityPlayer.getName())) {
                continue;
            }
            if (Xulu.CustomFont) {
                Xulu.cFontRenderer.drawStringWithShadow(entityPlayer.getName(), (float)this.x + 1.0f, yCount + 1.0f, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), Global.hudAlpha.getValue()).getRGB());
            }
            else {
                Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(entityPlayer.getName(), (float)this.x + 1.0f, yCount + 1.0f, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), Global.hudAlpha.getValue()).getRGB());
            }
            yCount += 10.0f;
        }
    }
}
