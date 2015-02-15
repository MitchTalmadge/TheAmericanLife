package me.MitchT.AmericanLife.LevelLoader.Elements;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import me.MitchT.AmericanLife.Entities.PositionedEntity;
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
        }
    }
    
}
