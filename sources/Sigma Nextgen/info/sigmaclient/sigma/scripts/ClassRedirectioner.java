package info.sigmaclient.sigma.scripts;

import info.sigmaclient.sigma.utils.player.MovementUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import java.util.HashMap;

import static info.sigmaclient.sigma.modules.Module.mc;

public class ClassRedirectioner {
    public HashMap<String, Object> varHashMap = new HashMap<>();
    public HashMap<String, ReMethod> methodHashMap = new HashMap<>();
    public void init() {
        varHashMap.put("player", mc.player);
        varHashMap.put("world", mc.world);
        varHashMap.put("renderManager", RenderUtils.getRenderPos());

        methodHashMap.put("player.isUsingItem", new ReMethod((p) -> mc.player.isHandActive()));
        methodHashMap.put("player.isMoving", new ReMethod((p) -> MovementUtils.isMoving()));
//        methodHashMap.put("player.getHandItem", new ReMethod((p) -> mc.player.getHeldItem(EnumHand.MAIN_HAND)));

        methodHashMap.put(
                "gl.drawRect",
                new ReMethod((p) -> {
                    RenderUtils.drawRect(
                            p.getPramD("x"),
                            p.getPramD("y"),
                            p.getPramD("x2"),
                            p.getPramD("y2"),
                            p.getPramI("color")
                    );
                    return null;
                })
                .addPrame("x | double")
                .addPrame("y | double")
                .addPrame("x2 | double")
                .addPrame("y2 | double")
                .addPrame("color | int")
        );
        methodHashMap.put(
                "player.strafe",
                new ReMethod((p) -> {
                    MovementUtils.strafing(
                            p.getPramD("speed")
                    );
                    return null;
                })
                .addPrame("speed | double")
        );
        methodHashMap.put(
                "mc.drawString",
                new ReMethod((p) -> {
                    mc.fontRenderer.drawString(
                            null,
                            p.getPram("str"),
                            p.getPramI("x"),
                            p.getPramI("y"),
                            p.getPramI("int")
                    );
                    return null;
                })
                        .addPrame("str | string")
                        .addPrame("x | int")
                        .addPrame("y | int")
                        .addPrame("color | int")
        );
    }
}
