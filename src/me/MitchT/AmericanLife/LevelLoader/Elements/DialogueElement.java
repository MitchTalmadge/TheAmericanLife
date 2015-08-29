package me.MitchT.AmericanLife.LevelLoader.Elements;

import java.util.ArrayList;

import me.MitchT.AmericanLife.LevelLoader.InvalidLevelException;
import me.MitchT.AmericanLife.LevelLoader.XMLHelper;

import org.w3c.dom.Element;

public class DialogueElement extends LevelElement
{
    
    @Override
    public void processElements(ArrayList<Element> elements) throws InvalidLevelException
    {
        System.out.println("Processing DialogueElement");
        for(Element element : elements)
        {
            if(XMLHelper.doesElementContainChildren(element, "topText", "bottomText", "stageX"))
            {
                String[] topText = XMLHelper.getChildrenAsStrings(element, "topText");
                String[] texts = new String[topText.length+1];

                System.arraycopy(topText, 0, texts, 0, topText.length);
                
                String bottomText = XMLHelper.getChildAsString(element, "bottomText");
                texts[texts.length-1] = bottomText;
                
                int stageX = XMLHelper.getChildAsInteger(element, "stageX");
                
                game.addDialogue(stageX, texts);
            }
        }
    }
    
}
