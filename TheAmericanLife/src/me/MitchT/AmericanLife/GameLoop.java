package me.MitchT.AmericanLife;

import java.util.ArrayList;

public class GameLoop extends Thread
{
    private ArrayList<GameLoopListener> listenerList = new ArrayList<GameLoopListener>();
    
    private boolean gameRunning = false;
    
    @Override
    public void run()
    {
        double nextTime = (double) System.nanoTime() / 1000000000.0;
        double lastTime = (double) System.nanoTime() / 1000000000.0;
        double maxTimeDiff = 0.5;
        int skippedFrames = 1;
        int maxSkippedFrames = 5;
        double delta = ((double)1/60);
        
        int fps = 0;
        double lastFpsTime = 0;
        
        gameRunning = true;
        
        while(gameRunning)
        {
            double currTime = (double) System.nanoTime() / 1000000000.0;
            lastFpsTime += currTime - lastTime;
            lastTime = currTime;
            if((currTime - nextTime) > maxTimeDiff)
                nextTime = currTime;
            if(currTime >= nextTime)
            {
                fps++;
                
                if(lastFpsTime >= 1)
                {
                    //System.out.println("FPS: "+fps);
                    fps = 0;
                    lastFpsTime = 0;
                }
                nextTime += delta;
                update();
                if((currTime < nextTime) || (skippedFrames > maxSkippedFrames))
                {
                    render();
                    skippedFrames = 1;
                }
                else
                {
                    skippedFrames++;
                }
            }
            else
            {
                int sleepTime = (int) (1000.0 * (nextTime - currTime));
                if(sleepTime > 0)
                {
                    try
                    {
                        Thread.sleep(sleepTime);
                    }
                    catch(InterruptedException expected)
                    {
                        ;
                    }
                }
            }
        }
    }
    
    private void update()
    {
        for(GameLoopListener listener : listenerList)
            listener.update();
    }
    
    private void render()
    {
        for(GameLoopListener listener : listenerList)
            listener.render();
    }
    
    public void registerGameLoopListener(GameLoopListener listener)
    {
        if(!this.listenerList.contains(listener))
            this.listenerList.add(listener);
    }
    
}
