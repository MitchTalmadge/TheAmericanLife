package me.MitchT.AmericanLife.LevelLoader.Elements;

import java.util.ArrayList;

import me.MitchT.AmericanLife.Game;
import me.MitchT.AmericanLife.LevelLoader.InvalidLevelException;

import org.w3c.dom.Element;

public abstract class LevelElement
{
    protected Game game = Game.instance;
    
    public abstract void processElements(ArrayList<Element> elements) throws InvalidLevelException;
    
}
