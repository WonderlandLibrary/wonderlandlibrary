package info.sigmaclient.sigma.utils.nbs;

import java.io.DataInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class NBSReader {

    public static class WkII
    {
        public int cCcA;
        public int fljR;
        public int eEsY;

        public WkII(final int cCcA, final int fljR, final int eEsY) {
        super();
        this.cCcA = cCcA;
        this.fljR = fljR;
        this.eEsY = eEsY;
    }
    }
    public static class ABpb
    {
        public int RZpY;
        public String MSAn;

        public ABpb(final int rZpY, final String msAn) {
        super();
        this.RZpY = rZpY;
        this.MSAn = msAn;
    }
    }

    public static class NBSFile
    {
        public float maxtick = 0;
        public ArrayList<ABpb> nOsS;
        public ArrayList<Integer> YDyR;
        public ArrayList<Note> FgWb;

        public NBSFile(final ArrayList<ABpb> nOsS, final ArrayList<String> list, final ArrayList<Note> fgWb) {
            super();
            this.nOsS = nOsS;
            this.YDyR = new ArrayList<Integer>();
            this.FgWb = fgWb;
        }

        public static class Note
    {
        public float tick;//aCac
        public int key;
        public int inst;

        public Note(final float tick, final int key, final int inst) {
        super();
        this.tick = tick;
        this.key = key;
        this.inst = inst;
    }
    }

        public static class ABpb
    {
        public int RZpY;
        public String MSAn;

        public ABpb(final int rZpY, final String msAn) {
        super();
        this.RZpY = rZpY;
        this.MSAn = msAn;
    }
    }
    }

    public NBSFile readFine(File io) {
        final NBSFile ajLR = new NBSFile(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        try {
            final File file = io;
            if (!file.exists()) {
                return null;
            }
            DataInputStream bufferedReader = new DataInputStream(Files.newInputStream(file.toPath()));
            short WDF = bufferedReader.readShort();
            byte Version = bufferedReader.readByte();
            System.out.println("NBS Version: " + Version);
            if(Version != 10){
                byte VanillaInterCount = bufferedReader.readByte();
            }
            short ALLTIME = bufferedReader.readShort();
            short SongHeight = bufferedReader.readShort();
            String name = bufferedReader.readUTF();
            String author = bufferedReader.readUTF();
            String desc = bufferedReader.readUTF();
            short jie = bufferedReader.readShort();
            byte AutoSave = bufferedReader.readByte();
            byte SaveTime = bufferedReader.readByte();
            byte PH = bufferedReader.readByte();
            int needtime = bufferedReader.readInt();
            int LclickLOL = bufferedReader.readInt();
            int RclickLOL = bufferedReader.readInt();
            int addTimes = bufferedReader.readInt();
            int delTimes = bufferedReader.readInt();
            String afterName = bufferedReader.readUTF();
            if(Version != 10) {
                byte Loop = bufferedReader.readByte();
                byte MaxLoop = bufferedReader.readByte();
                short LoopStartTick = bufferedReader.readShort();
            }

            System.out.println("" + name + " " + author + " " + desc + " 节奏:" + jie + " NAME:" +  afterName);
            int lastTick = 0;
            float maxTick = 0;
            short lastTick2 = 0;
            int tick = -1;
            short jumps = 0;
            bufferedReader.readShort();
            bufferedReader.readShort();
            while (true) {
                jumps = bufferedReader.readShort();
                if (jumps == 0){
                    break;
                }
                lastTick = jumps;
                tick += jumps;
                short layer = -1;
                boolean lol = false;
                while (true) {
                    jumps = bufferedReader.readShort();
                    if (jumps == 0)
                        break;
                    layer += jumps;
                    byte inst = bufferedReader.readByte();
                    byte key = bufferedReader.readByte();
                    ajLR.FgWb.add(new NBSFile.Note(
                            tick / (768F / 2F),
                            (key - 33),
                            inst
                    ));
                    maxTick = tick / (768F / 2F);
                    System.out.println("tick: "+ tick / (768F / 2F) +" key: "+(key - 33)+" inst:"+inst);
                }
            }
            bufferedReader.close();
            ajLR.maxtick = maxTick;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return ajLR;
    }
}
