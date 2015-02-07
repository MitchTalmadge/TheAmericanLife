package me.MitchT.AmericanLife.LevelLoader.Elements;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import me.MitchT.AmericanLife.Entities.PlayerEntity;
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
            if(element.getAttribute("type").equals("Player"))
            {
                if(XMLHelper.doesElementContainChildren(element, "position", "spriteSheet"))
                {
                    Point playerPosition = XMLHelper.getChildAsPoint(element, "position");
                    
                    PlayerEntity playerEntity = new PlayerEntity(playerPosition, null);
                }
            }
            else if(element.getAttribute("type").equals("Static"))
            {
                if(XMLHelper.doesElementContainChildren(element, "position", "imagePath", "u", "v", "w", "h"))
                {
                    Point position = XMLHelper.getChildAsPoint(element, "position");
                    String imagePath = "/assets/images/"+XMLHelper.getChildAsString(element, "imagePath");
                    int u = XMLHelper.getChildAsInteger(element, "u");
                    int v = XMLHelper.getChildAsInteger(element, "v");
                    int w = XMLHelper.getChildAsInteger(element, "w");
                    int h = XMLHelper.getChildAsInteger(element, "h");
                    BufferedImage image = null;
                    try
                    {
                        image = ImageIO.read(getClass().getResource(imagePath));
                        image = image.getSubimage(u, v, w, h);
                        game.addEntity(new StaticEntity(position, image));
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
