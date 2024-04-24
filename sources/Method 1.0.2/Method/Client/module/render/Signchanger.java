package Method.Client.module.render;


import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;

public class Signchanger extends Module {

    public static Setting ChangeFont;
    public static Setting SignColor;
    public static Setting TextColor;
    public static Setting TextScale;
    public static Setting Frame;
    public static Setting Nametag;


    @Override
    public void setup() {
        TextScale = setmgr.add(new Setting("TextScale", this, 1, .1, 5, false));
        SignColor = setmgr.add(new Setting("SignColor", this, 0, 1, 1, 1));
        TextColor = setmgr.add(new Setting("TextColor", this, .4, 1, 1, 1));
        ChangeFont = setmgr.add(new Setting("ChangeFont", this, true));
        Nametag = setmgr.add(new Setting("Nametag", this, false));
        Frame = setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));

    }

    public Signchanger() {
        super("Signchanger", Keyboard.KEY_NONE, Category.RENDER, "Change stuff about signs ");
    }


    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        for (Object object : mc.world.loadedTileEntityList) {
            if (object instanceof TileEntitySign) {
                TileEntitySign sign = (TileEntitySign) object;
                String[] line = new String[5];
                for (int i = 0; i < sign.signText.length; i++) {
                    line[i] = sign.signText[i].getUnformattedText();
                }
                if (Nametag.getValBoolean()) {
                    RenderUtils.SimpleNametag(new Vec3d(sign.getPos().x, sign.getPos().y + 1, sign.getPos().z), line[0]);
                    RenderUtils.SimpleNametag(new Vec3d(sign.getPos().x, sign.getPos().y + .75, sign.getPos().z), line[1]);
                    RenderUtils.SimpleNametag(new Vec3d(sign.getPos().x, sign.getPos().y + .5, sign.getPos().z), line[2]);
                    RenderUtils.SimpleNametag(new Vec3d(sign.getPos().x, sign.getPos().y + .25, sign.getPos().z), line[3]);
                }
            }
        }


    }
}
