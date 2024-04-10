package info.sigmaclient.sigma.commands.impl;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.commands.Command;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class GPSCommand extends Command {
    public static double x, z;
    static Vector3d vec;
    public static void render() {
        if (vec == null) return;
        float angel = RotationUtils.toRotation(vec).getYaw() + 90F;
        float aa = angel - MathHelper.wrapAngleTo180_float(mc.player.rotationYaw) - 90F;
        GlStateManager.pushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        float yO = -100;
        GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f + yO, 0);
        GlStateManager.rotate(aa, 0, 0, 1);
        GlStateManager.translate(-sr.getScaledWidth() / 2f, -(sr.getScaledHeight() / 2f + yO), 0);
        String str = "GPS" + " (" + ((int) mc.player.getDistance(x, mc.player.getPosY(), z)) + "m)";
        FontUtil.sfuiFont16.drawSmoothString(str,
                sr.getScaledWidth() / 2f - FontUtil.sfuiFont16.getStringWidth(str) / 2f,
                sr.getScaledHeight() / 2f - 45 + yO, -1);
        RenderUtils.drawTextureLocationZoom(sr.getScaledWidth() / 2f - 32 * 0.6f * 0.5f, sr.getScaledHeight() / 2f - 30 - 32 * 0.6f * 0.5f + yO, 32 * 0.6f, 32 * 0.6f, "arrow", ColorUtils.reAlpha(-1, 0.9f));
        GlStateManager.popMatrix();
    }
    @Override
    public void onChat(String[] args, String joinArgs) {
        if(args.length == 1 && args[0].equalsIgnoreCase("stop")){
            vec = null;
            return;
        }
        if(args.length != 2){
            sendUsages();
            return;
        }

        try{
            double x = Double.parseDouble(args[0]);
            double z = Double.parseDouble(args[1]);
            ChatUtils.sendMessageWithPrefix("GPS to " + x + ", " + z + "!");
            GPSCommand.x = x;
            GPSCommand.z = z;
            vec = new Vector3d(x, 0, z);
        }catch (Exception e){
            ChatUtils.sendMessageWithPrefix("Bad postion!");
        }
    }

    @Override
    public String usages() {
        return "[x, z] / stop";
    }

    @Override
    public String describe() {
        return "gps to pos.";
    }

    @Override
    public String[] getName() {
        return new String[]{"gps"};
    }
}
