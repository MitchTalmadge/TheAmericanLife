package me.MitchT.AmericanLife.LevelLoader.Elements;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import me.MitchT.AmericanLife.Entities.PlayerEntity;
import me.MitchT.AmericanLife.LevelLoader.InvalidLevelException;
import me.MitchT.AmericanLife.LevelLoader.XMLHelper;

import org.w3c.dom.Element;

public class PlayerElement extends LevelElement
{
    
    @Override
    public void processElements(ArrayList<Element> elements) throws InvalidLevelException
    {
        System.out.println("Processing PlayerElement");
        Element playerElement = elements.get(0);
        if(XMLHelper.doesElementContainChildren(playerElement, "position", "renderLayer", "spriteSheet", "spriteDimensions", "desiredDimensions", "walkSpeed"))
        {
            System.out.println("Loading Player...");
            
            Point position = XMLHelper.getChildAsPoint(playerElement, "position");
            int renderLayer = XMLHelper.getChildAsInteger(playerElement, "renderLayer");
            int walkSpeed = XMLHelper.getChildAsInteger(playerElement, "walkSpeed");
            
            BufferedImage spriteSheet;
            try
            {
                spriteSheet = ImageIO.read(getClass().getResource("/images/spritesheets/" +XMLHelper.getChildAsString(playerElement, "spriteSheet")));
                Point spriteDimensions = XMLHelper.getChildAsPoint(playerElement, "spriteDimensions");
                Point desiredDimensions = XMLHelper.getChildAsPoint(playerElement, "desiredDimensions");
                game.addEntity(new PlayerEntity(position, renderLayer, spriteSheet, spriteDimensions, desiredDimensions, walkSpeed));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
