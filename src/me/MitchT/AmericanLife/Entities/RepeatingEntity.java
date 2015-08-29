package me.MitchT.AmericanLife.Entities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import me.MitchT.AmericanLife.Components.Component;

public class RepeatingEntity extends Entity
{
    private Point position;
    private BufferedImage image;
    private int repeatCount;
    
    public RepeatingEntity(Point position, BufferedImage image, int renderLayer, int repeatCount)
    {
        super(renderLayer, new Component[] {});
        this.position = position;
        this.image = image;
        this.repeatCount = repeatCount;
    }
    
    public BufferedImage getImage()
    {
        return this.image;
    }
    
    public int getWidth()
    {
        return this.getImage().getWidth();
    }
    
    public int getHeight()
    {
        return this.getImage().getHeight();
    }
    
    public int getX()
    {
        return this.position.x;
    }
    
    public int getY()
    {
        return this.position.y;
    }
    
    public int getRepeatCount()
    {
        return repeatCount;
    }
}
