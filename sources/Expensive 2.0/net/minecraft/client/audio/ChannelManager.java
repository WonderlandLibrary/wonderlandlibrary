package net.minecraft.client.audio;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class ChannelManager
{
    private final Set<Entry> channels = Sets.newIdentityHashSet();
    private final SoundSystem sndSystem;
    private final Executor soundExecutor;

    public ChannelManager(SoundSystem sndSystem, Executor executor)
    {
        this.sndSystem = sndSystem;
        this.soundExecutor = executor;
    }

    public CompletableFuture<Entry> requestSoundEntry(SoundSystem.Mode systemMode)
    {
        CompletableFuture<Entry> completablefuture = new CompletableFuture<>();
        this.soundExecutor.execute(() ->
        {
            SoundSource soundsource = this.sndSystem.getSource(systemMode);

            if (soundsource != null)
            {
                Entry channelmanager$entry = new Entry(soundsource);
                this.channels.add(channelmanager$entry);
                completablefuture.complete(channelmanager$entry);
            }
            else {
                completablefuture.complete((Entry)null);
            }
        });
        return completablefuture;
    }

    public void runForAllSoundSources(Consumer<Stream<SoundSource>> sourceStreamConsumer)
    {
        this.soundExecutor.execute(() ->
        {
            sourceStreamConsumer.accept(this.channels.stream().map((managerEntry) -> {
                return managerEntry.source;
            }).filter(Objects::nonNull));
        });
    }

    public void tick()
    {
        this.soundExecutor.execute(() ->
        {
            Iterator<Entry> iterator = this.channels.iterator();

            while (iterator.hasNext())
            {
                Entry channelmanager$entry = iterator.next();
                channelmanager$entry.source.tick();

                if (channelmanager$entry.source.isStopped())
                {
                    channelmanager$entry.release();
                    iterator.remove();
                }
            }
        });
    }

    public void releaseAll()
    {
        this.channels.forEach(Entry::release);
        this.channels.clear();
    }

    public class Entry
    {
        @Nullable
        private SoundSource source;
        private boolean released;

        public boolean isReleased()
        {
            return this.released;
        }

        public Entry(SoundSource sound)
        {
            this.source = sound;
        }

        public void runOnSoundExecutor(Consumer<SoundSource> soundConsumer)
        {
            ChannelManager.this.soundExecutor.execute(() ->
            {
                if (this.source != null)
                {
                    soundConsumer.accept(this.source);
                }
            });
        }

        public void release()
        {
            this.released = true;
            ChannelManager.this.sndSystem.release(this.source);
            this.source = null;
        }
    }
}
