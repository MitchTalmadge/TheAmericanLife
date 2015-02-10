package me.MitchT.AmericanLife;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import me.MitchT.AmericanLife.Audio.AudioManager;
import me.MitchT.AmericanLife.Entities.Entity;
import me.MitchT.AmericanLife.Entities.PlayerEntity;
import me.MitchT.AmericanLife.Entities.PositionedEntity;
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
    
    private boolean playerEntered = false;
    private final int enterSpeed = 1;
    private final int enterInc = 2;
    private int enterCounter = 0;
    
    private final int borderHeight = 100;
    private final int fadeWidth = 200;
    private final int fadeDarkWidth = 50;
    private Font titleFont;
    private int titleSolidCounter = 0;
    private int titleFadeCounter = 0;
    private final int titleSolidTime = 100;
    private final int titleFadeTime = 100;
    
    private TreeMap<Integer, ArrayList<Entity>> entities = new TreeMap<Integer, ArrayList<Entity>>();
    
    private LevelManager levelManager;
    private AudioManager audioManager;
    
    private GameLoop gameLoop;

    private int stageWidth = getWidth();
    private PlayerEntity player;
    
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
        
        init();
    }
    
    private void init()
    {
        loadLevel(1);
    }
    
    private void loadLevel(int levelId)
    {
        //---- Clean Up ----//
        this.audioManager.stop();
        
        if(this.gameLoop != null)
            gameLoop.dispose();
        gameLoop = new GameLoop();
        gameLoop.registerGameLoopListener(this);
        
        this.entities.clear();
        this.titleSolidCounter = 0;
        this.titleFadeCounter = 0;
        
        this.cameraX = 0;
        this.speedCounter = 0;
        this.keysDown[0] = false;
        this.keysDown[1] = false;
        
        this.playerEntered = false;
        this.enterCounter = 0;
        
        this.stageWidth = getWidth();
        this.player = null;
        
        try
        {
            Thread.sleep(500);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        
        //---- Initialize ----//
        this.levelManager.loadLevel(levelId);
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
            if(keysDown[0]) //Left
            {
            	if((cameraX == stageWidth && player.getPosition().x > (getWidth()/2 - (float)player.getDesiredDimensions().x/2)) || (cameraX == 0 && player.getPosition().x > 0))
            	{
            		player.setPosition(new Point(player.getPosition().x - scrollInc, player.getPosition().y));
            	}
            	else if(cameraX == 0 && player.getPosition().x > 0)
            	{
            		player.setPosition(new Point(player.getPosition().x - scrollInc, player.getPosition().y));
            	}
            	else if(cameraX > 0)
                    cameraX -= scrollInc;
            }
            if(keysDown[1]) //Right
            {
            	if((cameraX == 0 && player.getPosition().x < (getWidth()/2 - (float)player.getDesiredDimensions().x/2)) || (cameraX == stageWidth && player.getPosition().x < getWidth()))
            	{
            		player.setPosition(new Point(player.getPosition().x + scrollInc, player.getPosition().y));
            	}
            	else if(cameraX < stageWidth)
                    cameraX += scrollInc;
            }
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
                else if(entity instanceof PositionedEntity)
                {
                	PositionedEntity positionedEntity = (PositionedEntity) entity;
                	if(cameraX + getWidth() > positionedEntity.getX() && cameraX < positionedEntity.getX()+positionedEntity.getWidth())
                	{
                		gfx.drawImage(positionedEntity.getImage(), positionedEntity.getX() - cameraX, positionedEntity.getY(), null);
                	}
                }
            }
        }
        
        //---- Draw Edge Black Fade ----//
        gfx.setColor(Color.BLACK);
        if(cameraX < fadeWidth) //Left Edge
        {
            int alpha = (int)(((float)(fadeWidth-cameraX)/((float)fadeWidth - fadeDarkWidth))*255);
            GradientPaint gradientPaint = new GradientPaint((fadeDarkWidth-cameraX >= 0) ? (fadeDarkWidth-cameraX) : 0, 0, new Color(0, 0, 0, (fadeDarkWidth-cameraX >= 0) ? 255 : alpha), fadeWidth-cameraX, 0, new Color(0,0,0,0));
            gfx.setPaint(gradientPaint);
            gfx.fillRect(0, 0, fadeWidth - cameraX, this.getHeight());
        }
        else if(cameraX > this.stageWidth - fadeWidth) //Right Edge
        {
            int alpha = (int)((float)(fadeWidth - (stageWidth - cameraX))/((float)fadeWidth - fadeDarkWidth)*255);
            GradientPaint gradientPaint = new GradientPaint((stageWidth - cameraX <= fadeDarkWidth) ? (getWidth() - (fadeDarkWidth - (stageWidth - cameraX))) : getWidth(), 0, new Color(0, 0, 0, (stageWidth - cameraX <= fadeDarkWidth) ? 255 : alpha), (getWidth() - fadeWidth) + (stageWidth - cameraX), 0, new Color(0,0,0,0));
            gfx.setPaint(gradientPaint);
            gfx.fillRect(getWidth() - (fadeWidth - (stageWidth - cameraX)), 0, (fadeWidth - (stageWidth - cameraX)), this.getHeight());
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
    	if(entity instanceof PlayerEntity)
    	{
    		this.player = (PlayerEntity) entity;
    	}
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
        else if(event.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
        	System.exit(0);
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

    public void setStageWidth(int stageWidth)
    {
        this.stageWidth = stageWidth;
    }
}
