package me.MitchT.AmericanLife;

import java.awt.Graphics;


import javax.swing.JFrame;

public class Game extends Thread
{
    private JFrame gameFrame;
    
    private boolean gameRunning = false;
    private final int TARGET_FPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    private long lastLoopTime;
    private long lastFpsTime;
    private int fps;
    
    public Game(JFrame gameFrame)
    {
        super("GameThread");
        this.gameFrame = gameFrame;
    }
    
    @Override
    public void run()
    {
        Main.getAudioManager().stop();
        lastLoopTime = System.nanoTime();
        
        gameRunning = true;
        
        while(gameRunning)
        {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double lastTimePerFrame = updateLength / ((double) OPTIMAL_TIME);
            
            lastFpsTime += updateLength;
            fps++;
            
            if(lastFpsTime >= 1000000000)
            {
                System.out.println("(FPS: " + fps + ")");
                lastFpsTime = 0;
                fps = 0;
            }
            
            update(lastTimePerFrame);
            render(lastTimePerFrame, gameFrame.getGraphics());
            
            try
            {
                Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
            }
            catch(InterruptedException expected)
            {
                ;
            }
        }
    }
    
    private void update(double lastTimePerFrame)
    {
        
    }
    
    private void render(double lastTimePerFrame, Graphics g)
    {
        
    }
}
