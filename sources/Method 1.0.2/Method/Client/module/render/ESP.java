package Method.Client.module.render;


import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

import static Method.Client.Main.GuiEsp;
import static Method.Client.Main.setmgr;

public class ESP extends Module {


    Setting Box = setmgr.add(new Setting("Box", this, true));
    Setting Nametag = setmgr.add(new Setting("Nametag", this, false));
    Setting MobColor = setmgr.add(new Setting("MobColor", this, .88, 1, 1, 1));
    Setting Mode = setmgr.add(new Setting("Hole Mode", this, "Outline", BlockEspOptions()));
    Setting LineWidth = setmgr.add(new Setting("LineWidth", this, 1, 0, 3, false));
    Setting Glow = setmgr.add(new Setting("Glow", this, true));
    Setting GlowWidth = setmgr.add(new Setting("GlowWidth", this, 0, 0, 1, false));
    Setting MobSelect = setmgr.add(new Setting("Gui", this, GuiEsp));


    public ESP() {
        super("ESP", Keyboard.KEY_NONE, Category.RENDER, "ESP");
    }

    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        for (Entity object : mc.world.loadedEntityList) {
            for (SubGui.SelectedThing selectedThing : GuiEsp.getListGui()) {
                if (Objects.requireNonNull(EntityList.getClassFromName(selectedThing.name)).getName().equalsIgnoreCase(object.getClass().getName())) {
                    render(object);
                }
            }
        }

    }


    void render(Entity ent) {
        if (ent == mc.player)
            return;
        if (Nametag.getValBoolean())
            ent.setAlwaysRenderNameTag(true);

        if (Glow.getValBoolean()) {
            mc.renderGlobal.entityOutlineShader.listShaders.forEach(shader -> {
                ShaderUniform outlineRadius = shader.getShaderManager().getShaderUniform("Radius");
                if (outlineRadius != null) outlineRadius.set((float) GlowWidth.getValDouble());
            });
        }


        if (ent instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) ent;
            entity.setGlowing(Glow.getValBoolean());

            if (Nametag.getValBoolean())
                entity.setCustomNameTag(ent.getName());
            if (Box.getValBoolean()) {
                RenderUtils.RenderBlock(Mode.getValString(), RenderUtils.Boundingbb(entity, 0, 0, 0, 0, 0, 0), MobColor.getcolor(), LineWidth.getValDouble());
            }
        }
    }

    @Override
    public void onDisable() {
        for (Entity object : mc.world.loadedEntityList) {
            if (object.isGlowing())
                object.setGlowing(false);
        }
        super.onDisable();
    }

}
