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

public class StageElement extends LevelElement
{
    
    @Override
    public void processElements(ArrayList<Element> elements) throws InvalidLevelException
    {
        System.out.println("Processing StageElement");
        Element playerElement = elements.get(0);
        if(XMLHelper.doesElementContainChildren(playerElement, "width"))
        {
            System.out.println("Loading Stage Properties...");
            
            int stageWidth = XMLHelper.getChildAsInteger(playerElement, "width");
            game.setStageWidth(stageWidth);
        }
    }
    
}
