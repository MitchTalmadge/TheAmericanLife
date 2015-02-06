package me.MitchT.AmericanLife;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import me.MitchT.AmericanLife.MainMenu.MainMenu;

public class Main
{
    
    public static void main(String[] args)
    {
        new MainMenu().setVisible(true);
        playSound();
    }
    
    private static void playSound() 
    {
        try
        {
            Player player = new Player(Main.class.getResourceAsStream("/assets/music/TheEntertainer.mp3"));
            player.play();
        }
        catch(JavaLayerException e)
        {
            e.printStackTrace();
        }
    }
}
