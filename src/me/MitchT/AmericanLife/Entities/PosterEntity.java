package me.MitchT.AmericanLife.Entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import me.MitchT.AmericanLife.Components.Component;

public class PosterEntity extends Entity
{
    
    private Point position;
    private BufferedImage posterImageSmall;
    private BufferedImage posterImageFull;
    
    public PosterEntity(Point position, BufferedImage posterImageSmall, BufferedImage posterImageFull, int renderLayer)
    {
        super(renderLayer, new Component[] {});
        this.position = position;
        this.posterImageSmall = posterImageSmall;
        
        Image posterImageFullScaled = posterImageFull.getScaledInstance((int)((400f/posterImageFull.getHeight())*posterImageFull.getWidth()), 400, BufferedImage.SCALE_SMOOTH);
                
        this.posterImageFull = new BufferedImage(posterImageFullScaled.getWidth(null), posterImageFullScaled.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gfx = (Graphics2D) this.posterImageFull.getGraphics();
        gfx.drawImage(posterImageFullScaled, 0, 0, null);
        gfx.dispose();
    }
    
    public BufferedImage getPosterImageSmall()
    {
        return this.posterImageSmall;
    }
    
    public BufferedImage getPosterImageFull()
    {
        return this.posterImageFull;
    }
    
    public int getX()
    {
        return this.position.x;
    }
    
    public int getY()
    {
        return this.position.y;
    }
    
}
