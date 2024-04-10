package info.sigmaclient.sigma.premium;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.moneta.MonetaCodec;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.ConfigManager;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.movement.Fly;
import info.sigmaclient.sigma.modules.movement.flys.impl.VulcanFly;
import info.sigmaclient.sigma.modules.movement.speeds.impl.GrimSpeed;
import info.sigmaclient.sigma.utils.Variable;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.TextureObf;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.math.AxisAlignedBB;
import org.apache.commons.codec.digest.DigestUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

import static info.sigmaclient.sigma.modules.Module.mc;
import static info.sigmaclient.sigma.music.youtubedl.YoutubeVideoHelper.visitSite;
import static info.sigmaclient.sigma.utils.alt.HCMLJsonParser.sendGetRequest;
import top.fl0wowp4rty.phantomshield.annotations.Native;
import top.fl0wowp4rty.phantomshield.annotations.license.MemoryCheck;
import top.fl0wowp4rty.phantomshield.annotations.license.Virtualization;
import top.fl0wowp4rty.phantomshield.annotations.obfuscation.CodeVirtualization;

@Native
public class PremiumManager {
    public static boolean isPremium = false;
    static byte[] key = "x8ahd77fhf".getBytes();
    static byte[] key2 = "sdfry657h".getBytes();
    public static String userName = "Log in";
    public static String password = "";
    public static String token = "";
    public static String serverBack = "";
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public String deObfuscate2(String c){
        String[] str = c.split(",");
        StringBuilder decode = new StringBuilder();
        int index = 0;
        for(String s : str){
            if(s.equals("")) continue;
            byte i = Byte.parseByte(s);
            decode.append(new String(new byte[]{(byte) (i ^ key2[index % key2.length])}));
//            System.out.println(decode);
            index ++;
        }
        return decode.toString();
    }
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public static boolean isPremium(){
        return isPremium && !serverBack.isEmpty();
    }
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public static String obfuscate2(String c){
        byte[] bs = c.getBytes();
        StringBuilder encode = new StringBuilder();
        for(int i = 0;i<bs.length;i++){
            byte key_b = key2[i % key2.length];
            bs[i] ^= key_b;
            encode.append(bs[i]).append(",");
        }
        return encode.toString();
    }
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public String deObfuscate(String c){
        String[] str = c.split(",");
        StringBuilder decode = new StringBuilder();
        int index = 0;
        for(String s : str){
            if(s.equals("")) continue;
            byte i = Byte.parseByte(s);
            decode.append(new String(new byte[]{(byte) (i ^ key[index % key.length])}));
//            System.out.println(decode);
            index ++;
        }
        return decode.toString();
    }
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public static String obfuscate(String c){
        byte[] bs = c.getBytes();
        StringBuilder encode = new StringBuilder();
        for(int i = 0;i<bs.length;i++){
            byte key_b = key[i % key.length];
            bs[i] ^= key_b;
            encode.append(bs[i]).append(",");
        }
        return encode.toString();
    }
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public static void save(String userName, String token){
        String realName = obfuscate(userName);
        String realToken = obfuscate2(token);
        String text = String.format("{\"user\":\"%s\", \"password\":\"%s\", \"isLog\":\"%s\"}", realName, realToken, isPremium ? "1" : "0");
        ConfigManager.saveConfig(text, SigmaNG.getSigmaNG().configManager.premiumFile);
    }

    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public void init(){
        if(isPremium) {
            AntiCrack.isPremium = false;
        }
        reset();
        File pre = SigmaNG.getSigmaNG().configManager.premiumFile;
        if(pre.exists()){
            String value = ConfigManager.readConfigData(pre.toPath());
            try {
                JSONObject j = JSONObject.parseObject(value);
                String name = j.getString("user");
                String token = j.getString("password");
                String log = j.getString("isLog");
                String realName = deObfuscate(name);
                String realToken = deObfuscate2(token);
//                System.out.println("SB: " + realName + " " + realToken + " " + log);
//                if(realName.startsWith("premium")){
                    PremiumManager.userName = realName;
                    PremiumManager.password = realToken;
                    isPremium = false;
                    if(log.equals("1")) {
                        login(realName, realToken);
                    }
//                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public interface PremiumRun {
        void run(Object event, Object pram);
    }
    public static byte[] encodeSHA256(String data) {
        // 执行消息摘要
        return DigestUtils.sha256(data);
    }
    public static String toOKPaass(String s) {
        return Arrays.toString(encodeSHA256(s)).replaceAll(",", "").replaceAll("-", "").replaceAll(" ", "").replace("[", "").replace("]", "");
    }
    static long ms = 0;
    static String serverIP = "https://sigma.nyaproxy.xyz/";
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public static void login(String username, String password){
        if(System.currentTimeMillis() - ms < 5000){
            return;//https://sigma.nyaproxy.xyz/
        }
        ms = System.currentTimeMillis();
        String hwid = getHWID();
        String url = String.format(serverIP + "login?username=%s&password=%s&hwid=%s&dhwid=%s", username, password, hwid, (hwid.hashCode() ^ 11401));
        String value = null;
        try {
            value = sendGetRequest(url.replace(" ", ""), "NEXTGEN_SIGMA");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(value != null && !value.isEmpty()){
//            System.out.println("Back: " + value);
//            PremiumManager.serverBack = value;
            String[] p = value.split(" ");
//            System.out.println(toOKPaass(password) + " " + toOKPaass(password).equals(p[1].replace("\n", "")) + " " + p[0] + " " + username + " " +
//                    p[0].trim().equals(username));
            if(p[0].trim().equals(username) && toOKPaass(password).equals(p[1].replace("\n", ""))){
                PremiumManager.userName = username;
                PremiumManager.password = password;
                PremiumManager.isPremium = true;
                AntiCrack.isPremium = true;
                initPremium();
                PremiumManager.serverBack = value;
                save(username, password);
            }
        }
    }
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public static void register(String username, String password, String key, Runnable callBack, Runnable badCallback){
//        serverIP = "http://154.23.240.188:11451/";
        if(System.currentTimeMillis() - ms < 5000){
            return;
        }
        ms = System.currentTimeMillis();
        String hwid = getHWID();
        String url = String.format(serverIP + "register?username=%s&password=%s&hwid=%s&key=%s", username, password, hwid, key);
        String value = null;
        try {
            value = sendGetRequest(url.replace(" ", ""), "NEXTGEN_SIGMA");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!value.isEmpty()){
//            System.out.println("Back: " + value);
            PremiumManager.serverBack = value;
            if(value.contains("true")){
                login(username, password);
                callBack.run();
            }else{
                badCallback.run();
            }
        }
    }
    /**
     *
     * @return HWID in MD5;
     *
     */

    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public static String getHWID() {
        try{
            String toEncrypt =  System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
    public static void reset() {
        AntiCrack.isPremium = false;
        userName = "Log in";
        token = "";
        serverBack = "";
        isPremium = false;
        GrimSpeed.premium = null;
        VulcanFly.premium1 = null;
        VulcanFly.premium2 = null;
    }
    static AntiCrack antiCrack = null;
    @CodeVirtualization("EAGLE_RED")
    @MemoryCheck
    public static void initPremium() {
        GrimSpeed.premium = () -> {
            AxisAlignedBB p_230318_2_ = mc.player.getBoundingBox();
            AxisAlignedBB axisalignedbb = p_230318_2_.grow(1.0E-7D);
            Variable.is_pushing = !mc.world.getEntitiesInAABBexcluding(mc.player, axisalignedbb, Entity::canBeCollidedWith).isEmpty();
            if (Variable.is_pushing) {
                mc.player.getMotion().x *= 1.2f;
                mc.player.getMotion().z *= 1.2f;
                mc.player.getMotion().x = Math.min(mc.player.getMotion().x, 0.6f);
                mc.player.getMotion().z = Math.min(mc.player.getMotion().z, 0.6f);
            }
        };
        VulcanFly.premium1 = (e, p) -> {
            Fly parent = (Fly) p;
            UpdateEvent event = (UpdateEvent) e;
//        mc.player.getPositionVec().y = parent.cacheY;
            if (mc.player.onGround) {
//            mc.player.getMotion().y = 0.41999998688697815;
            } else {
//            if (mc.player.getBoundingBox().minY < parent.cacheY) {
//            if(sendC03) {
//                event.cancelable = true;
//            }
                event.y = Math.floor(parent.cacheY * 2) / 2f;
                event.onGround = true;
                MovementUtils.strafing(0.2975);
                mc.player.setPosition(event.x, event.y, event.z);
                mc.player.getMotion().y = 0;
//            }
            }
        };
        VulcanFly.premium2 = (e, p) -> {
            Fly parent = (Fly) p;
            PacketEvent event = (PacketEvent) e;
            if(event.packet instanceof SPlayerPositionLookPacket && mc.currentScreen == null){
                mc.timer.setTimerSpeed(3);
//                mc.timer.setTimerSpeed(1);
                event.cancelable = true;
            }
        };
        VulcanFly.premium3 = (e, p) -> {
            if(!mc.player.onGround){
                mc.player.noClip = true;
            }
        };
//        AntiCrack.isPremium = true;
        if(antiCrack == null) {
            antiCrack = new AntiCrack();
            antiCrack.start();
        }
    }
}
