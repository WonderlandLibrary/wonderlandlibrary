package me.earth.earthhack.impl.services.minecraft;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.util.TextColor;
import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.event.events.render.Render2DEvent;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import me.earth.earthhack.impl.services.render.TextRenderer;

public class Logo extends EventListener<Render2DEvent>
{
    protected Logo()
    {
        super(Render2DEvent.class);
    }

    @Override
    public void invoke(Render2DEvent event)
    {
        String text = "3arthh4ck - " + Earthhack.VERSION + getPingString();
        TextRenderer.getInstance().drawStringWithShadow(text, 2, 2, 0xffffffff);
    }

    private String getPingString()
    {
        if (PingBypass.getInstance().isEnabled())
        {
            int sPing = PingBypass.getInstance().getServerPing();
            long ping = PingBypass.getInstance().getPing();

            return " - " + sPing + "ms" + " (" + TextColor.RED + ping + TextColor.WHITE + "ms)";
        }

        return "";
    }

}
