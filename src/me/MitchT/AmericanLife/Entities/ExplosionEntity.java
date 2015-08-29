package me.MitchT.AmericanLife.Entities;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import me.MitchT.AmericanLife.Game;
import me.MitchT.AmericanLife.GameLoopListener;
import me.MitchT.AmericanLife.Components.Component;

public class ExplosionEntity extends Entity implements GameLoopListener
{
    
    private Point minimum;
    private Point maximum;
    private BufferedImage spriteSheet;
    private int frameWidth;
    private int frameHeight;
    private int frameCount;
    private int frameDelay;
    private int explosionFreq;
    
    private int delayCounter = 0;
    private Point currentPos;

    public ExplosionEntity(int renderLayer, Point minimum, Point maximum, BufferedImage spriteSheet, int frameWidth, int frameHeight, int frameCount, int frameDelay, int explosionFreq)
    {
        super(renderLayer, new Component[] {});
        
        this.minimum = minimum;
        this.maximum = maximum;
        this.spriteSheet = spriteSheet;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameCount = frameCount;
        this.frameDelay = frameDelay;
        this.explosionFreq = explosionFreq;
        
        Game.instance.getGameLoop().registerGameLoopListener(this);
    }
    
    public Point getCurrentPosition()
    {
        return this.currentPos;
    }
    
    public boolean shouldRender()
    {
        return delayCounter > explosionFreq;
    }
    
    public Image getCurrentFrameImage()
    {
        return this.spriteSheet.getSubimage(((delayCounter - explosionFreq)/frameDelay)*frameWidth, 0, frameWidth, frameHeight);
    }

    @Override
    public void update()
    {
        delayCounter++;
        
        if(delayCounter == explosionFreq)
        {
            Random random = new Random();
            this.currentPos = new Point((maximum.x == minimum.x) ? minimum.x : random.nextInt(maximum.x - minimum.x) + minimum.x, (minimum.y == maximum.y) ? minimum.y : random.nextInt(maximum.y - minimum.y) + minimum.y);
        }
        else if(delayCounter > explosionFreq + ((frameCount-1)*frameDelay))
        {
            delayCounter = 0;
        }
    }

    @Override
    public void render()
    {
    }
}
