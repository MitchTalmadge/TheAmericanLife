package me.MitchT.AmericanLife.LevelLoader.Elements;

import java.util.ArrayList;

import me.MitchT.AmericanLife.Main;
import me.MitchT.AmericanLife.LevelLoader.InvalidLevelException;
import me.MitchT.AmericanLife.LevelLoader.XMLHelper;

import org.w3c.dom.Element;

public class PlayListElement extends LevelElement
{
    
    private String[] musicPlaylist;

    @Override
    public void processElements(ArrayList<Element> elements) throws InvalidLevelException
    {
        System.out.println("Processing PlayListElement");
        Element playListElement = elements.get(0);
        if(XMLHelper.doesElementContainChildren(playListElement, "song"))
        {
            System.out.println("Loading Level Playlist...");
            int amountOfElements = playListElement.getElementsByTagName("song").getLength();
            musicPlaylist = new String[amountOfElements];
            for(int i = 0; i < amountOfElements; i++)
            {
                musicPlaylist[i] = "/assets/music/"+playListElement.getElementsByTagName("song").item(i).getFirstChild().getTextContent();
                System.out.println("Added song to Playlist: " + musicPlaylist[i]);
            }
            Main.getAudioManager().setPlaylist(musicPlaylist);
            Main.getAudioManager().startPlaylist();
        }
    }
    
}
