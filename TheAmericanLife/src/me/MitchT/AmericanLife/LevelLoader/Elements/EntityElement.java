package me.MitchT.AmericanLife.LevelLoader.Elements;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import me.MitchT.AmericanLife.Entities.ExplosionEntity;
import me.MitchT.AmericanLife.Entities.PositionedEntity;
import me.MitchT.AmericanLife.Entities.PosterEntity;
import me.MitchT.AmericanLife.Entities.RepeatingEntity;
import me.MitchT.AmericanLife.Entities.StaticEntity;
import me.MitchT.AmericanLife.LevelLoader.InvalidLevelException;
import me.MitchT.AmericanLife.LevelLoader.XMLHelper;

import org.w3c.dom.Element;

public class EntityElement extends LevelElement
{
    
    @Override
    public void processElements(ArrayList<Element> elements) throws InvalidLevelException
    {
        System.out.println("Processing EntityElement");
        for(Element element : elements)
        {
            if(element.getAttribute("type").equals("Static"))
            {
                if(XMLHelper.doesElementContainChildren(element, "position", "imagePath", "u", "v", "w", "h", "renderLayer"))
                {
                    Point position = XMLHelper.getChildAsPoint(element, "position");
                    String imagePath = "/assets/images/" + XMLHelper.getChildAsString(element, "imagePath");
                    int u = XMLHelper.getChildAsInteger(element, "u");
                    int v = XMLHelper.getChildAsInteger(element, "v");
                    int w = XMLHelper.getChildAsInteger(element, "w");
                    int h = XMLHelper.getChildAsInteger(element, "h");
                    int renderLayer = XMLHelper.getChildAsInteger(element, "renderLayer");
                    BufferedImage image = null;
                    try
                    {
                        image = ImageIO.read(getClass().getResource(imagePath));
                        image = image.getSubimage(u, v, w, h);
                        game.addEntity(new StaticEntity(position, image, renderLayer));
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else if(element.getAttribute("type").equals("Repeating"))
            {
                if(XMLHelper.doesElementContainChildren(element, "position", "imagePath", "u", "v", "w", "h", "renderLayer"))
                {
                    Point position = XMLHelper.getChildAsPoint(element, "position");
                    String imagePath = "/assets/images/" + XMLHelper.getChildAsString(element, "imagePath");
                    int u = XMLHelper.getChildAsInteger(element, "u");
                    int v = XMLHelper.getChildAsInteger(element, "v");
                    int w = XMLHelper.getChildAsInteger(element, "w");
                    int h = XMLHelper.getChildAsInteger(element, "h");
                    int renderLayer = XMLHelper.getChildAsInteger(element, "renderLayer");
                    BufferedImage image = null;
                    try
                    {
                        image = ImageIO.read(getClass().getResource(imagePath));
                        image = image.getSubimage(u, v, w, h);
                        
                        int repeatCount = -1;
                        if(XMLHelper.doesElementContainChildren(element, "repeatCount"))
                        {
                            repeatCount = XMLHelper.getChildAsInteger(element, "repeatCount");
                        }
                        
                        game.addEntity(new RepeatingEntity(position, image, renderLayer, repeatCount));
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else if(element.getAttribute("type").equals("Positioned"))
            {
                if(XMLHelper.doesElementContainChildren(element, "position", "imagePath", "u", "v", "w", "h", "renderLayer"))
                {
                    Point position = XMLHelper.getChildAsPoint(element, "position");
                    String imagePath = "/assets/images/" + XMLHelper.getChildAsString(element, "imagePath");
                    int u = XMLHelper.getChildAsInteger(element, "u");
                    int v = XMLHelper.getChildAsInteger(element, "v");
                    int w = XMLHelper.getChildAsInteger(element, "w");
                    int h = XMLHelper.getChildAsInteger(element, "h");
                    int renderLayer = XMLHelper.getChildAsInteger(element, "renderLayer");
                    BufferedImage image = null;
                    try
                    {
                        image = ImageIO.read(getClass().getResource(imagePath));
                        image = image.getSubimage(u, v, w, h);
                        game.addEntity(new PositionedEntity(position, image, renderLayer));
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else if(element.getAttribute("type").equals("Poster"))
            {
                if(XMLHelper.doesElementContainChildren(element, "position", "imagePathSmall", "imagePathFull", "renderLayer"))
                {
                    Point position = XMLHelper.getChildAsPoint(element, "position");
                    String imagePathSmall = "/assets/images/textures/posters/" + XMLHelper.getChildAsString(element, "imagePathSmall");
                    String imagePathFull = "/assets/images/textures/posters/" + XMLHelper.getChildAsString(element, "imagePathFull");
                    int renderLayer = XMLHelper.getChildAsInteger(element, "renderLayer");
                    BufferedImage imageSmall = null;
                    BufferedImage imageFull = null;
                    try
                    {
                        imageSmall = ImageIO.read(getClass().getResource(imagePathSmall));
                        imageFull = ImageIO.read(getClass().getResource(imagePathFull));
                        
                        game.addEntity(new PosterEntity(position, imageSmall, imageFull, renderLayer));
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else if(element.getAttribute("type").equals("Explosion"))
            {
                if(XMLHelper.doesElementContainChildren(element, "minimum", "maximum", "imagePath", "renderLayer", "frameWidth", "frameHeight", "frameCount", "frameDelay", "explosionFreq"))
                {
                    Point minimum = XMLHelper.getChildAsPoint(element, "minimum");
                    Point maximum = XMLHelper.getChildAsPoint(element, "maximum");
                    String imagePath = "/assets/images/spritesheets/explosions/" + XMLHelper.getChildAsString(element, "imagePath");
                    int renderLayer = XMLHelper.getChildAsInteger(element, "renderLayer");
                    int frameWidth = XMLHelper.getChildAsInteger(element, "frameWidth");
                    int frameHeight = XMLHelper.getChildAsInteger(element, "frameHeight");
                    int frameCount = XMLHelper.getChildAsInteger(element, "frameCount");
                    int frameDelay = XMLHelper.getChildAsInteger(element, "frameDelay");
                    int explosionFreq = XMLHelper.getChildAsInteger(element, "explosionFreq");
                    BufferedImage image = null;
                    try
                    {
                        image = ImageIO.read(getClass().getResource(imagePath));
                        
                        game.addEntity(new ExplosionEntity(renderLayer, minimum, maximum, image, frameWidth, frameHeight, frameCount, frameDelay, explosionFreq));
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
}
