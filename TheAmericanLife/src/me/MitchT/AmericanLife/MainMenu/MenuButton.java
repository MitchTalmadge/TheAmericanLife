package me.MitchT.AmericanLife.MainMenu;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MenuButton extends JLabel
{
    private ImageIcon enabledIcon;
    private ImageIcon disabledIcon;
    private boolean hovering = false;
    
    public MenuButton(BufferedImage menuIcons, int w, int h, int x, int y)
    {
        this.enabledIcon = new ImageIcon(menuIcons.getSubimage(x, y, w, h));
        this.disabledIcon = new ImageIcon(menuIcons.getSubimage(x, y + h, w, h));
        this.setIcon(this.disabledIcon);
        
        this.setPreferredSize(new Dimension(w, h));
    }
    
    public void setHovering(boolean hovering)
    {
        this.setIcon((hovering) ? enabledIcon : disabledIcon);
        this.hovering = hovering;
    }
    
    public boolean isHovering()
    {
        return this.hovering;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(other == null)
            return false;
        if(!(other instanceof MenuButton))
            return false;
        
        MenuButton otherButton = (MenuButton) other;
        
        if(otherButton.isHovering() != this.isHovering())
            return false;
        
        return super.equals(other);
    }
    
}
