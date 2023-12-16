package wtf.expensive.ui.proxy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wtf.expensive.managment.Managment;
import wtf.expensive.proxy.ProxyConnection;
import wtf.expensive.proxy.ProxyType;
import wtf.expensive.util.font.Fonts;

import java.net.InetSocketAddress;
import java.util.Locale;

public class ProxyUI extends Screen {

    ProxyConnection pc = Managment.PROXY_CONN;
    private TextFieldWidget proxy;


    public ProxyUI() {
        super(new StringTextComponent(""));
    }

    @Override
    protected void init() {
        super.init();

        float[] center = {width / 2f, height / 2f};

        this.addButton(new Button((int) center[0] - 100 - 5, (int) center[1] + 30, 100, 20, new TranslationTextComponent("Применить"), (ppp) ->
                parse(proxy.getText())));

        this.addButton(new Button((int) center[0] + 5, (int) center[1] + 30, 100, 20, new TranslationTextComponent("Назад"), (ppp) ->
                Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(null))
        ));

        this.proxy = new TextFieldWidget(this.font, (int) center[0] - 100, (int) center[1] - 10, 200, 20, new TranslationTextComponent("Ваш прокси"));
        this.proxy.setMaxStringLength(32);
        this.children.add(this.proxy);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        String cProxy = pc.getProxyAddr() != null ? pc.getProxyType().name().toLowerCase(Locale.ROOT) + "://" + pc.getProxyAddr().getHostString() + ":" + pc.getProxyAddr().getPort() : "БЕЗ ПРОКСИ";

        float[] center = {width / 2f, height / 2f};

        Fonts.gilroyBold[14].drawCenteredString(matrixStack, "Активный прокси: " + cProxy, center[0], center[1] - 30, -1);

        Fonts.gilroyBold[14].drawCenteredString(matrixStack, "Пример: socks4://123.123.123.123:1234", center[0], center[1] + 60, -1);

        proxy.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void parse(String input) {
        input = input.toLowerCase(Locale.ROOT);

        try {
            ProxyType type = input.startsWith("http://") ? ProxyType.HTTP : input.startsWith("socks4://") ? ProxyType.SOCKS4 : input.startsWith("socks5://") ? ProxyType.SOCKS5 : ProxyType.DIRECT;
            String addr = input.split("//")[1];

            pc.setup(type, new InetSocketAddress(addr.split(":")[0], Integer.parseInt(addr.split(":")[1])));
        } catch (Exception e) {
            pc.reset();
        }
    }

    @Override
    public void tick() {
        super.tick();

        proxy.tick();
    }
}
