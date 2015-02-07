package me.MitchT.AmericanLife.Audio;

import java.util.concurrent.locks.LockSupport;


public class AudioManager
{
    
    private AudioThread thread;
    private String[] playlist;
    
    public void setPlaylist(String[] playlist)
    {
        this.playlist = playlist;
    }
    
    public void startPlaylist()
    {
        stop();
        
        if(playlist != null)
        {
            thread = new AudioThread(playlist);
            thread.start();
        }
    }
    
    public void play(String audioPath)
    {
        stop();
        
        thread = new AudioThread(new String[]{audioPath});
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
