package me.MitchT.AmericanLife.Audio;

import java.util.concurrent.locks.LockSupport;


public class AudioManager
{
    
    private AudioThread thread;
    
    public void play(String audioPath)
    {
        stop();
        
        thread = new AudioThread(audioPath);
        thread.start();
    }
    
    public void setPaused(boolean paused)
    {
        if(thread != null)
        {
            thread.setPaused(paused);
            if(!paused)
                LockSupport.unpark(thread);
        }
    }
    
    public void stop()
    {
        if(thread != null)
        {
            setPaused(true);
            thread.interrupt();
            thread = null;
        }
    }
    
}
