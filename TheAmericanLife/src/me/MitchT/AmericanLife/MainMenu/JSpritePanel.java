package me.MitchT.AmericanLife.MainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class JSpritePanel extends JPanel implements ActionListener
{
    private int currentFrame = 0;
    private ImageIcon[] frames;
    private Timer timer;
    private JLabel spriteLabel;
    
    public JSpritePanel(BufferedImage spriteSheet, int frameDelay, int spriteWidth, int spriteHeight, int frameCount, int desiredWidth, int desiredHeight)
    {
        this.frames = new ImageIcon[frameCount];
        
        this.timer = new Timer(frameDelay, this);
        timer.setRepeats(true);
        timer.start();
        
        for(int i = 0; i < frameCount; i++)
        {
            BufferedImage subImage = spriteSheet.getSubimage(0+(i*spriteWidth), 0, spriteWidth, spriteHeight);
            this.frames[i] = new ImageIcon(subImage.getScaledInstance(desiredWidth, desiredHeight, BufferedImage.SCALE_REPLICATE));
        }
        
        spriteLabel = new JLabel(this.frames[currentFrame]);
        this.add(spriteLabel);
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        currentFrame++;
        if(currentFrame+1 > this.frames.length)
            currentFrame = 0;
        spriteLabel.setIcon(this.frames[currentFrame]);
    }
}
