package me.MitchT.AmericanLife.LevelLoader.Elements;

import java.util.ArrayList;

import me.MitchT.AmericanLife.LevelLoader.InvalidLevelException;
import me.MitchT.AmericanLife.LevelLoader.XMLHelper;

import org.w3c.dom.Element;

public class YearElement extends LevelElement
{
    
    @Override
    public void processElements(ArrayList<Element> elements) throws InvalidLevelException
    {
        System.out.println("Processing YearElement");
        for(Element element : elements)
        {
            if(XMLHelper.doesElementContainChildren(element, "text", "stageX"))
            {
                String text = XMLHelper.getChildAsString(element, "text");
                int stageX = XMLHelper.getChildAsInteger(element, "stageX");
                
                game.addYear(stageX, text);
            }
        }
    }
    
}
