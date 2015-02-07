package me.MitchT.AmericanLife;

import me.MitchT.AmericanLife.Audio.AudioManager;
import me.MitchT.AmericanLife.MainMenu.MainMenu;

public class Main
{
    private static AudioManager audioManager;
    
    public static void main(String[] args)
    {
        audioManager = new AudioManager();
        new MainMenu().setVisible(true);
    }
    
    public static AudioManager getAudioManager()
    {
        return audioManager;
    }
    
}
