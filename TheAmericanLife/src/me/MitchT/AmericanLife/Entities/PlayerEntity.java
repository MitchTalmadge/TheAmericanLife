package me.MitchT.AmericanLife.Entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import me.MitchT.AmericanLife.Game;
import me.MitchT.AmericanLife.GameLoopListener;
import me.MitchT.AmericanLife.Components.Component;

public class PlayerEntity extends Entity implements GameLoopListener
{
    private int animationFrame = 0;
    private final int animationSpeed = 10;
    private int animationCounter = 0;
    private boolean lookDir = true;
    
    private Point position;
    private BufferedImage spriteSheet;
    private Point spriteDimensions;
    private Point desiredDimensions;
    
    public PlayerEntity(Point position, int renderLayer, BufferedImage spriteSheet, Point spriteDimensions, Point desiredDimensions)
    {
        super(renderLayer, new Component[] {});
        
        Game.instance.getGameLoop().registerGameLoopListener(this);
        
        this.position = position;
        this.spriteSheet = spriteSheet;
        this.spriteDimensions = spriteDimensions;
        this.desiredDimensions = desiredDimensions;
    }
    
    public Point getPosition()
    {
        return this.position;
    }
    
    public void setPosition(Point position)
    {
        this.position = position;
    }
    
    public void setAnimationFrame(int frame)
    {
        this.animationFrame = frame;
    }
    
    public int getAnimationFrame()
    {
        return this.animationFrame;
    }
    
    public Image getAnimationFrameImage()
    {
        BufferedImage frameImage = this.spriteSheet.getSubimage(getAnimationFrame() * spriteDimensions.x, 0, spriteDimensions.x, spriteDimensions.y);
        return frameImage.getScaledInstance(desiredDimensions.x, desiredDimensions.y, BufferedImage.SCALE_FAST);
    }
    
    public AffineTransform getTransforms()
    {
        if(!lookDir)
        {
            AffineTransform aTransform = new AffineTransform();
            aTransform.concatenate(AffineTransform.getScaleInstance(-1, 1));
            aTransform.concatenate(AffineTransform.getTranslateInstance(-desiredDimensions.x - (position.x*2), 0));
            return aTransform;
        }
        return null;
    }
    
    public boolean getLookDir()
    {
        return this.lookDir;
    }
    
    public Point getDesiredDimensions()
    {
    	return desiredDimensions;
    }
    
    @Override
    public void update()
    {
        boolean[] keysDown = Game.instance.getKeysDown();
        
        if(animationCounter == animationSpeed && (keysDown[0] || keysDown[1]))
        {
            animationCounter = 0;
            if(getAnimationFrame() == 2)
                setAnimationFrame(3);
            else
                setAnimationFrame(2);
            if(keysDown[0])
            {
                this.lookDir = false;
            }
            if(keysDown[1])
            {
                this.lookDir = true;
            }
        }
        else if(!keysDown[0] && !keysDown[1])
        {
            setAnimationFrame(0);
            animationCounter = animationSpeed;
        }
        else if(animationCounter < animationSpeed)
            animationCounter++;
    }
    
    @Override
    public void render()
    {
    }
}
