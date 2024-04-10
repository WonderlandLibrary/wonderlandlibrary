package info.sigmaclient.sigma.utils.nbs;

import java.io.File;

public class NoteFile {
    public NBSReader.NBSFile nbsFile;
    NBSReader nbsReader;
    public NoteFile(File LOL){
        nbsReader = new NBSReader();
        nbsFile = nbsReader.readFine(LOL);
    }
}
