package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.sigma5.utils.랾㔢酭콵䖼竬;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.util.math.vector.Vector2f;

import java.awt.*;

import static info.sigmaclient.sigma.modules.Category.Render;
import static info.sigmaclient.sigma.modules.render.NameTags.霥瀳놣㠠釒;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class DVDSimulator extends Module {
    private static String[] 묙䖼䖼홵츚;
    public 랾㔢酭콵䖼竬 室鄡竁ᔎ펊;
    public float 掬Ꮺ좯쟗髾;
    public float 曞杭酋阢㦖;
    public float 瀧硙쥡挐顸;
    public float 鱀侃ಽ璧娍;
    public Vector2f 鷏㐖哺퉧펊;
    public int 婯㥇待曞躚;

    public DVDSimulator() {
        super(DVDSimulator.묙䖼䖼홵츚[0], Render, DVDSimulator.묙䖼䖼홵츚[1]);
        this.室鄡竁ᔎ펊 = new 랾㔢酭콵䖼竬(1.0f, 1.0f);
        this.曞杭酋阢㦖 = 0.0f;
        this.瀧硙쥡挐顸 = 1.0f;
        this.鱀侃ಽ璧娍 = 1.0f;
        this.鷏㐖哺퉧펊 = new Vector2f(201 / 2f, 90 / 2f);
        this.婯㥇待曞躚 = 0;
        this.뵯聛ᙽ츚蚳();
    }

    @Override
    public void onEnable() {
        ScaledResolution sr = new ScaledResolution(mc);
        this.掬Ꮺ좯쟗髾 = (float) ((sr.getScaledWidth() - this.鷏㐖哺퉧펊.x) * Math.random());
        this.曞杭酋阢㦖 = (float) ((sr.getScaledHeight() - this.鷏㐖哺퉧펊.y) * Math.random());
        super.onEnable();
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        ScaledResolution sr = new ScaledResolution(mc);
        final int 㥇觯騜Ꮤ嘖堍 = sr.getScaledHeight();
        final int 䕦ꪕⰛ䁞ᢻ眓 = sr.getScaledWidth();
        final int n = 2;
        if (this.曞杭酋阢㦖 > n) {
            if (this.曞杭酋阢㦖 + this.鷏㐖哺퉧펊.y > 㥇觯騜Ꮤ嘖堍) {
                this.鱀侃ಽ璧娍 = -1.0f;
                this.뵯聛ᙽ츚蚳();
            }
        } else {
            this.鱀侃ಽ璧娍 = 1.0f;
            this.뵯聛ᙽ츚蚳();
        }
        if (this.掬Ꮺ좯쟗髾 > n) {
            if (this.掬Ꮺ좯쟗髾 + this.鷏㐖哺퉧펊.x > 䕦ꪕⰛ䁞ᢻ眓) {
                this.瀧硙쥡挐顸 = -1.0f;
                this.뵯聛ᙽ츚蚳();
            }
        } else {
            this.瀧硙쥡挐顸 = 1.0f;
            this.뵯聛ᙽ츚蚳();
        }
        this.掬Ꮺ좯쟗髾 += this.瀧硙쥡挐顸 * n;
        this.曞杭酋阢㦖 += this.鱀侃ಽ璧娍 * n;
        RenderUtils.drawTextureLocationZoom(this.掬Ꮺ좯쟗髾, this.曞杭酋阢㦖, (float) this.鷏㐖哺퉧펊.x, (float) this.鷏㐖哺퉧펊.y, "dvd", new Color(霥瀳놣㠠釒(this.婯㥇待曞躚, 0.8f)));
        super.onRenderEvent(event);
    }

    private void 뵯聛ᙽ츚蚳() {
        this.婯㥇待曞躚 = Color.getHSBColor((float) Math.random(), 0.6f, 1.0f).getRGB();
    }

    static {
        DVDSimulator.묙䖼䖼홵츚 = new String[]{"DVD Simulator", "wtf"};
    }
}
