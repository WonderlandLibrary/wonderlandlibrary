
package info.sigmaclient.sigma.modules.world;


import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.nbs.NoteFile;
import info.sigmaclient.sigma.utils.nbs.NotePlayerHelper;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class NoteblockPlayer extends Module {
    NoteFile noteFile;
    Thread LOL;
    NotePlayerHelper helper;
    public float yaw = -999,pitch = -999;
    BooleanValue tune = new BooleanValue("Tune",false);
    public NumberValue delayed = new NumberValue("Tune-Delay",100,20,500, NumberValue.NUMBER_TYPE.INT);
    public NoteblockPlayer() {
        super("NoteblockPlayer", Category.World, "Auto play noteblock.");
     registerValue(tune);
     registerValue(delayed);
    }
    public void onDisable(){
        if(LOL != null && LOL.isAlive()){
            LOL.stop();
        }
    }
    public void onEnable(){
//        File newFile = new File(FileManager.getConfigDir("nbs"), Sigma.nbsName);
//        if(!newFile.exists()){
            this.enabled = false;
        NotificationManager.notify("NoteBlockPlayer", "Umm, lemme fix it", 6000);
            return;
//        }
//        noteFile = new NoteFile(newFile);
//        helper = new NotePlayerHelper();
//        helper.initFile();

//        LOL = new Thread(
//                ()->{
//                    NotificationsManager.addNotification(new Notification(
//                            "Start play!","NoteblockPlayer", Notification.Type.Info
//                    ));
//                    //init 读取�?�?
//                    if(tune.getValue())
//                        for (ANoteBlock 方块 : helper.zBJd){
//                        final IBlockAccess blockState = Helper.mc.theWorld.getBlockState(方块.blockPos);
//                        方块.yindiao = blockState.get(NoteBlock.NOTE);
//
//                        float[] rots = getHypixelRotations(方块.blockPos);
//                        yaw = rots[0];
//                        pitch = rots[1];
//                            //调音部分
//                            while(方块.yindiao != 方块.id){
//                                mc.thePlayer.swingItem(Hand.MAIN_HAND);
//                                mc.getNetHandler().sendPacketWithoutEvent(new CPlayerTryUseItemOnBlockPacket(
//                                        Hand.MAIN_HAND,
//                                        new BlockRayTraceResult(
//                                                BlockUtils.getVec3(方块.blockPos, Direction.UP),
//                                                Direction.UP,
//                                                方块.blockPos,
//                                                false
//                                        )
//                                ));
//
//                                final BlockState oo = Helper.mc.theWorld.getBlockState(方块.blockPos);
//                                方块.yindiao = oo.get(NoteBlock.NOTE);
//                                try {
//                                    Thread.sleep(delayed.getValue().intValue());
//                                } catch (InterruptedException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                            //调音部分
//
//                            mc.thePlayer.swingItem(Hand.MAIN_HAND);
//                            mc.playerController.clickBlock(
//                                    方块.blockPos,
//                                    Direction.UP);
//                    }
//                    float tick = 0;
//                    //init 读取结束
//                    while (tick < noteFile.nbsFile.maxtick) {
//                        for (NBSReader.NBSFile.Note note : noteFile.nbsFile.FgWb) {
//                            if(note.inst != 0 && note.inst != 1) continue;
//                            int 音调 = note.key;
//                            if (Math.abs(tick - note.tick) < 0.3) {
//                                for (ANoteBlock 方块 : helper.zBJd) {
//                                    if (方块.id == 音调) {
//                                        float[] rots = getHypixelRotations(方块.blockPos);
//                                        yaw = rots[0];
//                                        pitch = rots[1];
//                                        mc.thePlayer.swingItem(Hand.MAIN_HAND);
//                                        mc.playerController.clickBlock(
//                                                方块.blockPos,
//                                                Direction.UP);
//                                    }
//                                }
//                            }
//                        }
//
//                        try {
//                            Thread.sleep(
//                                    70
//                            );
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        tick += 0.5F;
//                    }
//
//                    NotificationsManager.addNotification(new Notification(
//                            "Play done!","NoteblockPlayer", Notification.Type.Info
//                    ));
//                }
//        )
//        ;
//        LOL.start();
    }
//    @EventHandler
//    public void onPre(EventPreUpdate e){
//        if(pitch != -999 && yaw != -999){
//            e.setYaw(yaw);
//            e.setPitch(pitch);
//        }
//    }
}
