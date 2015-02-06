package me.MitchT.AmericanLife.MainMenu;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class JPanelWithBackground extends JPanel
{
    
    private BufferedImage backgroundImage;
    
    public JPanelWithBackground(BufferedImage backgroundImage)
    {
        this.backgroundImage = backgroundImage;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(backgroundImage, 0, 0, this);
    }
    
}
