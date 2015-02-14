package me.MitchT.AmericanLife;

import java.util.ArrayList;

public class GameLoopAlt extends Thread
{
    private ArrayList<GameLoopListener> listenerList = new ArrayList<GameLoopListener>();
    
    private boolean gameRunning = false;
    private final boolean displayStats = false;
    private final int TARGET_TPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / TARGET_TPS;
    
    @Override
    public void run()
    {
        gameRunning = true;
        
        long lastLoopTime = System.nanoTime();
        long lastFpsTime = System.nanoTime();
        int fps = 0;
        
        while(gameRunning)
        {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double updateDelta = updateLength / ((double) OPTIMAL_TIME);
            
            lastFpsTime += updateLength;
            fps++;
            
            if(lastFpsTime >= 1000000000)
            {
                if(displayStats)
                    System.out.println("(FPS: " + fps + ")");
                lastFpsTime = 0;
                fps = 0;
            }
            
            update(updateDelta);
            
            render();
            
            try
            {
                long sleepTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                if(sleepTime > 0)
                    Thread.sleep(sleepTime);
            }
            catch(InterruptedException expected)
            {
                ;
            }
        }
        
        this.listenerList.clear();
        this.listenerList = null;
    }
    
    private void update(double updateDelta)
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
    
    public void dispose()
    {
        this.gameRunning = false;
    }
    
}
