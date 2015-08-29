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
    private boolean hasStarted = false;
    private int frameDelay;
    
    public JSpritePanel(BufferedImage spriteSheet, int startDelay, int frameDelay, int spriteWidth, int spriteHeight, int frameCount, int desiredWidth, int desiredHeight)
    {
        this.frames = new ImageIcon[frameCount];
        this.frameDelay = frameDelay;
        
        for(int i = 0; i < frameCount; i++)
        {
            BufferedImage subImage = spriteSheet.getSubimage((i * spriteWidth), 0, spriteWidth, spriteHeight);
            this.frames[i] = new ImageIcon(subImage.getScaledInstance(desiredWidth, desiredHeight, BufferedImage.SCALE_REPLICATE));
        }
        
        spriteLabel = new JLabel(this.frames[currentFrame]);
        this.add(spriteLabel);
        
        this.timer = new Timer(startDelay, this);
        timer.setRepeats(false);
        timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        if(hasStarted)
        {
        currentFrame++;
        if(currentFrame+1 > this.frames.length)
            currentFrame = 0;
        spriteLabel.setIcon(this.frames[currentFrame]);
        }
        else 
        {
            hasStarted = true;
            timer = new Timer(frameDelay, this);
            timer.setRepeats(true);
            timer.start();
        }
    }
}
