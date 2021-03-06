package me.MitchT.AmericanLife;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import me.MitchT.AmericanLife.Audio.AudioManager;
import me.MitchT.AmericanLife.Entities.Entity;
import me.MitchT.AmericanLife.Entities.ExplosionEntity;
import me.MitchT.AmericanLife.Entities.PlayerEntity;
import me.MitchT.AmericanLife.Entities.PositionedEntity;
import me.MitchT.AmericanLife.Entities.PosterEntity;
import me.MitchT.AmericanLife.Entities.RepeatingEntity;
import me.MitchT.AmericanLife.Entities.StaticEntity;
import me.MitchT.AmericanLife.LevelLoader.LevelManager;
import me.MitchT.AmericanLife.LevelLoader.LevelsEnum;

public class Game extends Canvas implements GameLoopListener, KeyListener
{
    public static Game instance;
    
    private boolean[] keysDown = new boolean[4];
    private final int scrollSpeed = 1;
    private int speedCounter = 0;
    private int cameraX = 0;
    
    private boolean playerEntering = true;
    private boolean playerExiting = false;
    private final int transitionSpeed = 1;
    private final int transitionInc = 10;
    private int transitionCounter = 0;
    
    private boolean gameOver = false;
    private final int gameOverScrollSpeed = 1;
    private int gameOverCounter = 0;
    private BufferedImage creditsImage;
    
    private final int borderHeight = 100;
    private final int fadeWidth = 200;
    private final int fadeDarkWidth = 50;
    
    private Font titleFont;
    private Font topDialogueFont;
    private Font bottomDialogueFont;
    private Font hintFont;
    private final String posterString = "Push DOWN to view the full poster!";
    
    private int titleSolidCounter = 0;
    private int titleFadeCounter = 0;
    private final int titleSolidTime = 100;
    private final int titleFadeTime = 100;
    
    private TreeMap<Integer, ArrayList<Entity>> entitiesMap = new TreeMap<>();
    private Set<Integer> entitiesKeySet = entitiesMap.keySet();
    private HashMap<Integer, String[]> dialogueMap = new HashMap<>();
    
    private LevelManager levelManager;
    private AudioManager audioManager;
    
    private GameLoop gameLoop;
    
    private int stageWidth = getWidth();
    private PlayerEntity player;
    
    private BufferedImage drawBuffer;
    private Graphics2D drawGraphics;
    
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
        
        drawBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        drawGraphics = (Graphics2D) drawBuffer.getGraphics();
        
        try
        {
            creditsImage = ImageIO.read(getClass().getResource("/images/Credits.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        this.titleFont = getFont().deriveFont(30f);
        this.topDialogueFont = getFont().deriveFont(15f);
        this.bottomDialogueFont = getFont().deriveFont(30f);
        this.hintFont = getFont().deriveFont(15f);
        
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
        
        this.entitiesMap.clear();
        this.entitiesKeySet = entitiesMap.keySet();
        this.dialogueMap.clear();
        
        this.titleSolidCounter = 0;
        this.titleFadeCounter = 0;
        
        this.cameraX = -getWidth();
        this.speedCounter = 0;
        this.keysDown[0] = false;
        this.keysDown[1] = false;
        this.keysDown[2] = false;
        this.keysDown[3] = false;
        
        this.playerEntering = true;
        this.playerExiting = false;
        this.transitionCounter = 0;
        
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
        gameLoop.start();
    }
    
    @Override
    public void update()
    {
        if(playerEntering)
        {
            clearKeysDown();
            if(transitionCounter >= transitionSpeed)
            {
                transitionCounter = 0;
                cameraX += transitionInc;
                
                if(cameraX >= 0)
                {
                    playerEntering = false;
                    cameraX = 0;
                }
            }
            else
            {
                transitionCounter++;
            }
            return;
        }
        else if(gameOver)
        {
            if(gameOverCounter <= creditsImage.getHeight() + getHeight())
                gameOverCounter += gameOverScrollSpeed;
            else
            {
              //---- Clean Up ----//
                this.audioManager.stop();
                
                if(this.gameLoop != null)
                    gameLoop.dispose();
                gameLoop = new GameLoop();
                gameLoop.registerGameLoopListener(this);
                
                this.entitiesMap.clear();
                this.entitiesKeySet = entitiesMap.keySet();
                this.dialogueMap.clear();
                
                this.titleSolidCounter = 0;
                this.titleFadeCounter = 0;
                
                this.cameraX = -getWidth();
                this.speedCounter = 0;
                this.keysDown[0] = false;
                this.keysDown[1] = false;
                this.keysDown[2] = false;
                this.keysDown[3] = false;
                
                this.playerEntering = true;
                this.playerExiting = false;
                this.transitionCounter = 0;
                
                this.stageWidth = getWidth();
                this.player = null;
                System.exit(0);
            }
            return;
        }
        else if(playerExiting)
        {
            clearKeysDown();
            if(transitionCounter >= transitionSpeed)
            {
                transitionCounter = 0;
                cameraX += transitionInc;
                
                if(cameraX >= stageWidth + getWidth())
                {
                    int nextLevelId = this.levelManager.getCurrentLevelId() + 1;
                    if(LevelsEnum.getXmlPathByLevelID(nextLevelId) != null)
                        loadLevel(nextLevelId);
                    else
                        gameOver = true;
                    return;
                }
            }
            else
            {
                transitionCounter++;
            }
            return;
        }
        else if(player.getPosition().x > getWidth() - fadeDarkWidth && cameraX >= stageWidth) //End of Level
        {
            playerExiting = true;
        }
        
        if(titleSolidCounter < titleSolidTime)
            titleSolidCounter++;
        else if(titleFadeCounter < titleFadeTime)
            titleFadeCounter++;
        
        if(cameraX < 0)
            cameraX = 0;
        if(cameraX > stageWidth)
            cameraX = stageWidth;
        
        if((!keysDown[0] && !keysDown[1]) || (keysDown[0] && keysDown[1]))
            speedCounter = scrollSpeed;
        else if(speedCounter == scrollSpeed)
        {
            speedCounter = 0;
            if(keysDown[0]) //Left
            {
                if((cameraX >= stageWidth && player.getPosition().x > (getWidth() / 2 - (float) player.getDesiredDimensions().x / 2)) || (cameraX <= 0 && player.getPosition().x > 0))
                {
                    player.setPosition(new Point(player.getPosition().x - player.getWalkSpeed(), player.getPosition().y));
                }
                else if(cameraX <= 0 && player.getPosition().x > 0)
                {
                    player.setPosition(new Point(player.getPosition().x - player.getWalkSpeed(), player.getPosition().y));
                }
                else if(cameraX > 0)
                    cameraX -= player.getWalkSpeed();
            }
            if(keysDown[1]) //Right
            {
                if((cameraX <= 0 && player.getPosition().x < (getWidth() / 2 - (float) player.getDesiredDimensions().x / 2)) || (cameraX >= stageWidth && player.getPosition().x < getWidth()))
                {
                    player.setPosition(new Point(player.getPosition().x + player.getWalkSpeed(), player.getPosition().y));
                }
                else if(cameraX < stageWidth)
                    cameraX += player.getWalkSpeed();
            }
        }
        else if(speedCounter < scrollSpeed)
            speedCounter++;
    }
    
    @Override
    public void update(Graphics g)
    {
        paint(g);
    }
    
    @Override
    public void paint(Graphics g)
    {
        if(gameOver)
        {
            drawGraphics.setColor(Color.BLACK);
            drawGraphics.fillRect(0, 0, getWidth(), getHeight());
            
            drawGraphics.drawImage(creditsImage, getWidth()/2 - creditsImage.getWidth()/2, getHeight() - gameOverCounter, null);
            g.drawImage(drawBuffer, 0, 0, null);
            return;
        }
        PosterEntity posterToDraw = null;
        for(Integer i : entitiesKeySet)
        {
            ArrayList<Entity> entityArray = entitiesMap.get(i);
            for(Entity entity : entityArray)
            {
                if(entity instanceof PlayerEntity)
                {
                    PlayerEntity playerEntity = (PlayerEntity) entity;
                    
                    AffineTransform oldTransform = drawGraphics.getTransform();
                    if(playerEntity.getTransforms() != null)
                        drawGraphics.transform(playerEntity.getTransforms());
                    
                    drawGraphics.drawImage(playerEntity.getAnimationFrameImage(), playerEntity.getPosition().x, playerEntity.getPosition().y, null);
                    
                    drawGraphics.setTransform(oldTransform);
                }
                else if(entity instanceof StaticEntity)
                {
                    StaticEntity staticEntity = (StaticEntity) entity;
                    drawGraphics.drawImage(staticEntity.getImage(), staticEntity.getX(), staticEntity.getY() + borderHeight, null);
                }
                else if(entity instanceof RepeatingEntity)
                {
                    RepeatingEntity repeatingEntity = (RepeatingEntity) entity;
                    
                    int numRepeats = repeatingEntity.getRepeatCount();
                    numRepeats -= Math.abs(Math.floor(((double) cameraX / repeatingEntity.getWidth())));
                    
                    int repeatCount = (int) Math.ceil((double) this.getWidth() / repeatingEntity.getWidth());
                    if(cameraX % repeatingEntity.getWidth() > 0)
                        repeatCount++;
                    
                    if(repeatCount > numRepeats && numRepeats >= 0)
                        repeatCount = numRepeats;
                    
                    for(int j = 0; j < repeatCount; j++)
                        drawGraphics.drawImage(repeatingEntity.getImage(), repeatingEntity.getX() + (j * repeatingEntity.getWidth()) - (cameraX % repeatingEntity.getWidth()), repeatingEntity.getY() + borderHeight, null);
                }
                else if(entity instanceof PositionedEntity)
                {
                    PositionedEntity positionedEntity = (PositionedEntity) entity;
                    if(cameraX + getWidth() > positionedEntity.getX() && cameraX < positionedEntity.getX() + positionedEntity.getWidth())
                    {
                        drawGraphics.drawImage(positionedEntity.getImage(), positionedEntity.getX() - cameraX, positionedEntity.getY() + borderHeight, null);
                    }
                }
                else if(entity instanceof PosterEntity)
                {
                    PosterEntity posterEntity = (PosterEntity) entity;
                    if(cameraX + getWidth() > posterEntity.getX() && cameraX < posterEntity.getX() + posterEntity.getPosterImageSmall().getWidth())
                    {
                        drawGraphics.drawImage(posterEntity.getPosterImageSmall(), posterEntity.getX() - cameraX, posterEntity.getY() + borderHeight, null);
                        if(player.getPosition().getX() + player.getAnimationFrameImage().getWidth(null) >= posterEntity.getX() - cameraX && player.getPosition().getX() <= posterEntity.getX() - cameraX + posterEntity.getPosterImageSmall().getWidth())
                        {
                            posterToDraw = posterEntity;
                        }
                    }
                }
                else if(entity instanceof ExplosionEntity)
                {
                    ExplosionEntity explosionEntity = (ExplosionEntity) entity;
                    if(explosionEntity.shouldRender())
                    {
                        if(cameraX + getWidth() > explosionEntity.getCurrentPosition().getX() && cameraX < explosionEntity.getCurrentPosition().getX() + explosionEntity.getCurrentFrameImage().getWidth(null))
                            drawGraphics.drawImage(explosionEntity.getCurrentFrameImage(), (int) explosionEntity.getCurrentPosition().getX() - cameraX, (int) explosionEntity.getCurrentPosition().getY() + borderHeight, null);
                    }
                }
            }
        }
        
        //---- Draw Edge Black Fade ----//
        drawGraphics.setColor(Color.BLACK);
        if(cameraX < fadeWidth) //Left Edge
        {
            int alpha = (int) (((float) (fadeWidth - cameraX) / ((float) fadeWidth - fadeDarkWidth)) * 255);
            GradientPaint gradientPaint = new GradientPaint((fadeDarkWidth - cameraX >= 0) ? (fadeDarkWidth - cameraX) : 0, 0, new Color(0, 0, 0, (fadeDarkWidth - cameraX >= 0) ? 255 : alpha), fadeWidth - cameraX, 0, new Color(0, 0, 0, 0));
            drawGraphics.setPaint(gradientPaint);
            drawGraphics.fillRect(0, 0, fadeWidth - cameraX, this.getHeight());
        }
        else if(cameraX > this.stageWidth - fadeWidth) //Right Edge
        {
            int alpha = (int) ((float) (fadeWidth - (stageWidth - cameraX)) / ((float) fadeWidth - fadeDarkWidth) * 255);
            GradientPaint gradientPaint = new GradientPaint((stageWidth - cameraX <= fadeDarkWidth) ? (getWidth() - (fadeDarkWidth - (stageWidth - cameraX))) : getWidth(), 0, new Color(0, 0, 0, (stageWidth - cameraX <= fadeDarkWidth) ? 255 : alpha), (getWidth() - fadeWidth) + (stageWidth - cameraX), 0, new Color(0, 0, 0, 0));
            drawGraphics.setPaint(gradientPaint);
            drawGraphics.fillRect(getWidth() - (fadeWidth - (stageWidth - cameraX)), 0, (fadeWidth - (stageWidth - cameraX)), this.getHeight());
        }
        
        //---- Draw Top/Bottom Borders ----//
        drawGraphics.setColor(Color.BLACK);
        drawGraphics.fillRect(0, 0, this.getWidth(), borderHeight);
        drawGraphics.fillRect(0, this.getHeight() - borderHeight, this.getWidth(), borderHeight);
        
        //---- Draw Title Text ----//
        if(titleSolidCounter < titleSolidTime)
        {
            drawGraphics.setColor(new Color(255, 255, 255, 255));
            drawGraphics.setFont(titleFont);
            int strWidth = drawGraphics.getFontMetrics(titleFont).stringWidth(levelManager.getCurrentLevelName());
            drawGraphics.drawString(levelManager.getCurrentLevelName(), this.getWidth() / 2 - strWidth / 2, borderHeight - 10);
        }
        else if(titleFadeCounter < titleFadeTime)
        {
            drawGraphics.setColor(new Color(255, 255, 255, (int) ((1f - (float) titleFadeCounter / titleFadeTime) * 255f)));
            drawGraphics.setFont(titleFont);
            int strWidth = drawGraphics.getFontMetrics(titleFont).stringWidth(levelManager.getCurrentLevelName());
            drawGraphics.drawString(levelManager.getCurrentLevelName(), this.getWidth() / 2 - strWidth / 2, borderHeight - 10);
        }
        
        String[] dialogueTexts = null;
        int maxDialogueX = 0;
        
        //---- Draw Dialogue ----//
        for(Entry<Integer, String[]> entry : dialogueMap.entrySet())
        {
            int stageX = entry.getKey();
            String[] text = entry.getValue();
            
            String bottomText = text[text.length - 1];
            
            int edgeOfDialogue = stageX + drawGraphics.getFontMetrics(bottomDialogueFont).stringWidth(bottomText) + 6;
            
            if(edgeOfDialogue >= cameraX && stageX <= cameraX + getWidth())
            {
                drawGraphics.setColor(Color.WHITE);
                drawGraphics.setFont(bottomDialogueFont);
                drawGraphics.fillRect(stageX - cameraX, this.getHeight() - borderHeight, 4, 30);
                drawGraphics.drawString(bottomText, stageX - cameraX + 6, this.getHeight() - borderHeight + 30);
                
            }
            
            if(player.getPosition().x >= stageX - cameraX && stageX >= maxDialogueX)
            {
                maxDialogueX = stageX;
                dialogueTexts = text;
            }
        }
        
        if(titleFadeCounter >= titleFadeTime && dialogueTexts != null && !gameOver)
        {
            drawGraphics.setFont(topDialogueFont);
            drawGraphics.setColor(Color.WHITE);
            for(int i = 0; i < dialogueTexts.length - 1; i++)
            {
                drawGraphics.drawString(dialogueTexts[i], getWidth() / 2 - drawGraphics.getFontMetrics(topDialogueFont).stringWidth(dialogueTexts[i]) / 2, 15 + 15 * i);
            }
        }
        
        //---- Draw Poster Full ----//
        if(posterToDraw != null && !keysDown[2])
        {
            drawGraphics.setColor(Color.WHITE);
            drawGraphics.setFont(hintFont);
            drawGraphics.drawString(posterString, getWidth() / 2 - (drawGraphics.getFontMetrics(hintFont).stringWidth(posterString) / 2), getHeight() - 20);
        }
        else if(posterToDraw != null)
        {
            drawGraphics.drawImage(posterToDraw.getPosterImageFull(), getWidth() / 2 - posterToDraw.getPosterImageFull().getWidth() / 2, getHeight() / 2 - posterToDraw.getPosterImageFull().getHeight() / 2, null);
        }
        
        g.drawImage(drawBuffer, 0, 0, null);
    }
    
    @Override
    public void render()
    {
        repaint();
    }
    
    public void addEntity(Entity entity)
    {
        if(entity instanceof PlayerEntity)
        {
            this.player = (PlayerEntity) entity;
        }
        int renderLayer = entity.getRenderLayer();
        if(entitiesMap.containsKey(renderLayer))
        {
            for(Entity entityInMap : entitiesMap.get(renderLayer))
            {
                if(entityInMap.equals(entity))
                    return;
            }
        }
        else
        {
            entitiesMap.put(renderLayer, new ArrayList<Entity>());
        }
        entitiesMap.get(renderLayer).add(entity);
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
        else if(event.getKeyCode() == KeyEvent.VK_DOWN)
        {
            keysDown[2] = true;
        }
        else if(event.getKeyCode() == KeyEvent.VK_SHIFT)
        {
            keysDown[3] = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            keysDown[0] = false;
        }
        else if(event.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            keysDown[1] = false;
        }
        else if(event.getKeyCode() == KeyEvent.VK_DOWN)
        {
            keysDown[2] = false;
        }
        else if(event.getKeyCode() == KeyEvent.VK_SHIFT)
        {
            keysDown[3] = false;
        }
        else if(event.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            System.exit(0);
        }
        else if(event.getKeyCode() == KeyEvent.VK_X)
        {
            loadLevel(levelManager.getCurrentLevelId() + 1);
        }
        else if(event.getKeyCode() == KeyEvent.VK_Z)
        {
            loadLevel(levelManager.getCurrentLevelId() - 1);
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
    
    private void clearKeysDown()
    {
        for(int i = 0; i < keysDown.length; i++)
        {
            keysDown[i] = false;
        }
    }
    
    public void setStageWidth(int stageWidth)
    {
        this.stageWidth = stageWidth;
    }
    
    public void addDialogue(int stageX, String[] texts)
    {
        this.dialogueMap.put(stageX, texts);
    }
}
