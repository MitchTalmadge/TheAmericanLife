package me.MitchT.AmericanLife;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;

import me.MitchT.AmericanLife.Audio.AudioManager;
import me.MitchT.AmericanLife.Entities.Entity;
import me.MitchT.AmericanLife.Entities.StaticEntity;
import me.MitchT.AmericanLife.LevelLoader.LevelManager;

public class Game extends Thread
{
    public static Game instance;

    private JFrame gameFrame;
    
    private boolean gameRunning = false;
    private final int TARGET_FPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    private long lastLoopTime;
    private long lastFpsTime;
    private int fps;
    
    private int cameraX = 0;
    private TreeMap<Integer, ArrayList<Entity>> entities = new TreeMap<Integer, ArrayList<Entity>>();

    private LevelManager levelManager;
    private AudioManager audioManager;
    
    public Game(JFrame gameFrame)
    {
        super("GameThread");
        instance = this;
        this.gameFrame = gameFrame;
        this.levelManager = new LevelManager();
        this.audioManager = Main.getAudioManager();
    }
    
    @Override
    public void run()
    {
        Main.getAudioManager().stop();
        try
        {
            Thread.sleep(500);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        levelManager.loadLevel(1);
        audioManager.startPlaylist();
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
        Set<Integer> keySet = entities.keySet();
        for(Integer i : keySet)
        {
            ArrayList<Entity> entityArray = entities.get(i);
            for(Entity entity : entityArray)
            {
                if(entity instanceof StaticEntity)
                {
                    StaticEntity staticEntity = (StaticEntity) entity;
                    g.drawImage(staticEntity.getImage(), staticEntity.getX(), staticEntity.getY(), null);
                }
            }
        }
    }
    
    public void addEntity(Entity entity)
    {
        int renderLayer = entity.getRenderLayer();
        if(entities.containsKey(renderLayer))
        {
            for(Entity entityInMap : entities.get(renderLayer))
            {
                if(entityInMap.equals(entity))
                    return;
            }
        }
        else 
        {
            entities.put(renderLayer, new ArrayList<Entity>());
        }
        entities.get(renderLayer).add(entity);
        System.out.println("Added entity of type "+entity.getClass().getSimpleName()+" to RenderLayer "+renderLayer);
    }
}
