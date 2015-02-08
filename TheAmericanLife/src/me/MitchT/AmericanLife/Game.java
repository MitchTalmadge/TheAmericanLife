package me.MitchT.AmericanLife;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import me.MitchT.AmericanLife.Audio.AudioManager;
import me.MitchT.AmericanLife.Entities.Entity;
import me.MitchT.AmericanLife.Entities.PlayerEntity;
import me.MitchT.AmericanLife.Entities.RepeatingEntity;
import me.MitchT.AmericanLife.Entities.StaticEntity;
import me.MitchT.AmericanLife.LevelLoader.LevelManager;

public class Game extends Canvas implements GameLoopListener, KeyListener
{
    public static Game instance;
    
    private boolean[] keysDown = new boolean[2];
    private final int scrollSpeed = 1;
    private final int scrollInc = 2;
    private int speedCounter = 0;
    private int cameraX = 0;
    private int cameraY = 0;
    
    private int borderHeight = 100;
    private Font titleFont;
    private int titleSolidCounter = 0;
    private int titleFadeCounter = 0;
    private final int titleSolidTime = 100;
    private final int titleFadeTime = 100;
    
    private TreeMap<Integer, ArrayList<Entity>> entities = new TreeMap<Integer, ArrayList<Entity>>();
    
    private LevelManager levelManager;
    private AudioManager audioManager;
    
    private GameLoop gameLoop;
    
    public Game(JFrame gameFrame)
    {
        instance = this;
        
        //---- GUI ----//
        gameFrame.setContentPane(new JPanel());
        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(this, BorderLayout.CENTER);
        gameFrame.addKeyListener(this);
        this.addKeyListener(this);
        gameFrame.setBackground(Color.BLACK);
        this.setBackground(Color.BLACK);
        gameFrame.setVisible(true);
        this.setVisible(true);
        
        this.titleFont = getFont().deriveFont(30f);
        
        //---- Managers ----//
        this.levelManager = new LevelManager();
        this.audioManager = Main.getAudioManager();
        
        //---- Game Loop ----//
        this.gameLoop = new GameLoop();
        gameLoop.registerGameLoopListener(this);
        
        init();
    }
    
    private void init()
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
        titleSolidCounter = 0;
        titleFadeCounter = 0;
        audioManager.startPlaylist();
        gameLoop.start();
    }
    
    @Override
    public void update()
    {
        if(titleSolidCounter < titleSolidTime)
            titleSolidCounter++;
        else if(titleFadeCounter < titleFadeTime)
            titleFadeCounter++;
        
        if(speedCounter == scrollSpeed && (keysDown[0] || keysDown[1]))
        {
            speedCounter = 0;
            if(keysDown[0])
                if(cameraX > 0)
                    cameraX -= scrollInc;
            if(keysDown[1])
                if(cameraX < 1000)
                    cameraX += scrollInc;
            System.out.println(cameraX);
        }
        else if(!keysDown[0] && !keysDown[1])
            speedCounter = scrollSpeed;
        else if(speedCounter < scrollSpeed)
            speedCounter++;
    }
    
    @Override
    public void render()
    {
        BufferStrategy strategy = this.getBufferStrategy();
        if(strategy == null)
        {
            this.createBufferStrategy(2);
            strategy = this.getBufferStrategy();
        }
        
        Graphics2D gfx = (Graphics2D) strategy.getDrawGraphics();
        
        Set<Integer> keySet = entities.keySet();
        for(Integer i : keySet)
        {
            ArrayList<Entity> entityArray = entities.get(i);
            for(Entity entity : entityArray)
            {
                if(entity instanceof PlayerEntity)
                {
                    PlayerEntity playerEntity = (PlayerEntity) entity;

                    AffineTransform oldTransform = gfx.getTransform();
                    if(playerEntity.getTransforms() != null)
                        gfx.transform(playerEntity.getTransforms());
                    
                    gfx.drawImage(playerEntity.getAnimationFrameImage(), playerEntity.getPosition().x, playerEntity.getPosition().y, null);
                    
                    gfx.setTransform(oldTransform);
                }
                else if(entity instanceof StaticEntity)
                {
                    StaticEntity staticEntity = (StaticEntity) entity;
                    gfx.drawImage(staticEntity.getImage(), staticEntity.getX(), staticEntity.getY() + borderHeight, null);
                }
                else if(entity instanceof RepeatingEntity)
                {
                    RepeatingEntity repeatingEntity = (RepeatingEntity) entity;
                    int repeatCount = (int) Math.ceil((double) this.getWidth() / repeatingEntity.getWidth());
                    if(cameraX % repeatingEntity.getWidth() > 0)
                        repeatCount++;
                    for(int j = 0; j < repeatCount; j++)
                        gfx.drawImage(repeatingEntity.getImage(), repeatingEntity.getX() + (j * repeatingEntity.getWidth()) - (cameraX % repeatingEntity.getWidth()), repeatingEntity.getY() + borderHeight, null);
                }
            }
        }
        
        //---- Draw Top/Bottom Borders ----//
        gfx.setColor(Color.BLACK);
        gfx.fillRect(0, 0, this.getWidth(), borderHeight);
        gfx.fillRect(0, this.getHeight() - borderHeight, this.getWidth(), borderHeight);
        
        //---- Draw Title Text ----//
        if(titleSolidCounter < titleSolidTime)
        {
            gfx.setColor(new Color(255, 255, 255, 255));
            gfx.setFont(titleFont);
            int strWidth = gfx.getFontMetrics(titleFont).stringWidth(levelManager.getCurrentLevelName());
            gfx.drawString(levelManager.getCurrentLevelName(), this.getWidth() / 2 - strWidth / 2, borderHeight - 10);
        }
        else if(titleFadeCounter < titleFadeTime)
        {
            gfx.setColor(new Color(255, 255, 255, (int) ((1f - (float) titleFadeCounter / titleFadeTime) * 255f)));
            gfx.setFont(titleFont);
            int strWidth = gfx.getFontMetrics(titleFont).stringWidth(levelManager.getCurrentLevelName());
            gfx.drawString(levelManager.getCurrentLevelName(), this.getWidth() / 2 - strWidth / 2, borderHeight - 10);
        }
        
        gfx.dispose();
        strategy.show();
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
        System.out.println("Added entity of type " + entity.getClass().getSimpleName() + " to RenderLayer " + renderLayer);
    }
    
    @Override
    public void keyPressed(KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.VK_A || event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            keysDown[0] = true;
        }
        else if(event.getKeyCode() == KeyEvent.VK_D || event.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            keysDown[1] = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.VK_A || event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            keysDown[0] = false;
        }
        else if(event.getKeyCode() == KeyEvent.VK_D || event.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            keysDown[1] = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent event)
    {
    }
    
    public GameLoop getGameLoop()
    {
        return this.gameLoop;
    }
    
    public boolean[] getKeysDown()
    {
        return keysDown;
    }
}
