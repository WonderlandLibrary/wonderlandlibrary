package today.flux.gui.plugingui;

import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.addon.api.FluxAddon;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.utility.AnimationUtils;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class PluginScrollableList {
    public CopyOnWriteArrayList<FluxAddon> elements;

    public float x;
    public float y;
    public float width;
    public float height;

    public Consumer<FluxAddon> onSelected;
    public FluxAddon selectedAPI = null;

    public GuiPluginMgr parent;

    //Scroller
    public float scrollY = 0;
    public float scrollAni = 0;
    public float minY = -100;

    public PluginScrollableList(GuiPluginMgr parent, CopyOnWriteArrayList<FluxAddon> elements, Consumer<FluxAddon> onSelected) {
        this.parent = parent;
        this.elements = elements;
        this.onSelected = onSelected;
    }

    public void draw(float x, float y, float width, float height, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        GuiRenderUtils.drawRoundedRect(x, y, width, height, 2f, 0xff2f3136, .5f, 0xff2f3136);

        if(RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            scrollY += Mouse.getDWheel() / 4f;

            if (scrollY <= minY)
                scrollY = minY;
            if (scrollY >= 0f)
                scrollY = 0f;

            minY = height - 4;
        } else {
            Mouse.getDWheel(); //用于刷新滚轮数据
        }

        this.scrollAni = AnimationUtils.smoothAnimation(this.scrollAni, scrollY, 50, .3f);


        //GuiRenderUtils.drawRect(x + 2, y + 2, width - 4, height - 4, 0xff00ff00);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.doGlScissor((int) x + 2, (int) y + 4, width - 4, height - 8);
        float startY = y + 4 + this.scrollAni;
        float totalY = 0;
        if (parent.needToUpdateList) {
            this.elements = (CopyOnWriteArrayList<FluxAddon>) Flux.INSTANCE.api.getAddonManager().getFluxAddonList();
            this.scrollAni = 0;
            this.selectedAPI = null;
            parent.needToUpdateList = false;
        } else {
            for (FluxAddon api : this.elements) {
                boolean highlight = this.selectedAPI == api;
                boolean drawHover = RenderUtil.isHoveringBound(mouseX, mouseY, x + 4, startY, width - 8, 20);
                GuiRenderUtils.drawRoundedRect(x + 4, startY, width - 8, 20, 2f, highlight ? 0xff058669 : 0xff32353b, .5f, highlight ? 0xff058669 : 0xff32353b);

                FontManager.sans18.drawLimitedString(api.getAPIName() + " " + api.getVersion() + " by " + api.getAuthor(), x + 22, startY, 0xffffffff, 400);
                FontManager.sans14.drawLimitedString(String.format("Contains %d Module%s, %d Command%s.", api.getModules().size(), api.getModules().size() > 1 ? "s" : "", api.getCommands().size(), api.getCommands().size() > 1 ? "s" : ""), x + 22, startY + 10, 0xffffffff, 400);
                FontManager.icon18.drawString(Flux.INSTANCE.api.getAddonManager().getEnabledFluxAddonList().contains(api) ? "j" : "E", x + 8, startY + 5, 0xffffffff);

                if(drawHover) {
                    GuiRenderUtils.drawRoundedRect(x + 4, startY, width - 8, 20, 2f, 0x33000000, .5f, 0x33000000);
                }

                startY += 25;
                totalY += 25;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if(RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            minY -= totalY;
        }

        if(totalY > this.height - 8) {
            float viewable = this.height;
            float progress = MathHelper.clamp_float(-this.scrollAni / -this.minY, 0, 1);
            float ratio = (viewable / totalY) * viewable;
            float barHeight = Math.max(ratio, 20f);
            float position = progress * (viewable - barHeight);

            GuiRenderUtils.drawRoundedRect(this.x + this.width + 4, this.y, 4, this.height, 2, 0xff2e3338, .5f, 0xff2e3338);
            GuiRenderUtils.drawRoundedRect(this.x + this.width + 4, this.y + position, 4, barHeight, 2, 0xff202225, .5f, 0xff202225);
        }
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        float startY = y + 4 + this.scrollAni;
        if(RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            for (FluxAddon api : this.elements) {
                boolean isHovered = RenderUtil.isHoveringBound(mouseX, mouseY, x + 4, startY, width - 8, 20);
                if(isHovered) {
                    this.selectedAPI = api;
                    this.onSelected.accept(this.selectedAPI);
                    break;
                }
                startY += 25;
            }
        } else {
            this.selectedAPI = null;
            this.onSelected.accept(null);
        }
    }
}
