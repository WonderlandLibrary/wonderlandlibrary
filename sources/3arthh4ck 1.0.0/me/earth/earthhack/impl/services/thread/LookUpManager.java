package me.earth.earthhack.impl.services.thread;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.earthhack.impl.util.thread.LookUpUtil;
import me.earth.earthhack.impl.util.thread.ThreadUtil;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LookUpManager implements Globals, Runnable
{
    private static final LookUpManager INSTANCE = new LookUpManager();

    private final ScheduledExecutorService service = ThreadUtil.newSingleThreadDaemonExecutor();
    private final Queue<LookUp> toLookUp = new ConcurrentLinkedQueue<>();
    private final StopWatch last_lookup  = new StopWatch();

    private LookUpManager() { /* This is a singleton */ }

    public static LookUpManager getInstance()
    {
        return INSTANCE;
    }

    public void init()
    {
        service.scheduleAtFixedRate(this, 0, 250, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run()
    {
        try
        {
            if (last_lookup.passed(5000))
            {
                LookUp lookUp = toLookUp.poll();
                if (lookUp != null)
                {
                    doBigLookUp(lookUp);
                    last_lookup.reset();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void doLookUp(LookUp lookUp)
    {
        switch (lookUp.type)
        {
            case NAME:
                String name = LookUpUtil.getNameSimple(lookUp.uuid);
                if (name != null)
                {
                    lookUp.name = name;
                    lookUp.onSuccess();
                    break;
                }
                toLookUp.add(lookUp);
                break;
            case UUID:
                UUID uuid = LookUpUtil.getUUIDSimple(lookUp.name);
                if (uuid != null)
                {
                    lookUp.uuid = uuid;
                    lookUp.onSuccess();
                    break;
                }
                toLookUp.add(lookUp);
                break;
            case HISTORY:
                UUID id = LookUpUtil.getUUIDSimple(lookUp.name);
                if (id != null)
                {
                    lookUp.uuid = id;
                }
                toLookUp.add(lookUp);
                break;
        }
    }

    private void doBigLookUp(LookUp lookUp)
    {
        switch (lookUp.type)
        {
            case NAME:
                String name = LookUpUtil.getName(lookUp.uuid);
                if (name != null)
                {
                    lookUp.name = name;
                    lookUp.onSuccess();
                }
                else
                {
                    lookUp.onFailure();
                }
                break;
            case UUID:
                UUID uuid = LookUpUtil.getUUID(lookUp.name);
                if (uuid != null)
                {
                    lookUp.uuid = uuid;
                    lookUp.onSuccess();
                }
                else
                {
                    lookUp.onFailure();
                }
                break;
            case HISTORY:
                UUID id = lookUp.uuid;
                if (id == null)
                {
                    id = LookUpUtil.getUUID(lookUp.name);
                }

                if (id != null) //double lookup idk
                {
                    lookUp.names = LookUpUtil.getNameHistory(id);
                    lookUp.onSuccess();
                }
                else
                {
                    lookUp.onFailure();
                }
                break;
        }
    }

}
