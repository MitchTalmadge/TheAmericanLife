package me.MitchT.AmericanLife.Audio;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class AudioThread extends Thread
{
    
    private final String[] audioPaths;
    private Player player;
    private AtomicBoolean paused = new AtomicBoolean(false);

    public AudioThread(String[] audioPaths)
    {
        super("AudioThread");
        this.setDaemon(true);
        this.audioPaths = audioPaths;
    }
    
    @Override
    public void run()
    {
        try
        {
            Thread.sleep(100);
            for(String audioPath : audioPaths)
            {
                player = new Player(getClass().getResourceAsStream(audioPath));
                while(player.play(1))
                    if(paused.get())
                        LockSupport.park();
            }
        }
        catch(JavaLayerException e)
        {
            e.printStackTrace();
        }
        catch(InterruptedException e)
        {
        }
    }
    
    public void setPaused(boolean paused)
    {
        this.paused.set(paused);
    }
    
    
}
