package me.MitchT.AmericanLife.MainMenu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class JButtonPanel extends JPanel implements MouseListener
{
    
    MenuButton[] menuButtons;
    private BufferedImage menuIcons;
    private BufferedImage handIcon;
    private ArrayList<ButtonPanelListener> buttonPanelListeners = new ArrayList<>();
    
    public JButtonPanel(BufferedImage menuIcons, int numberOfButtons)
    {
        this.menuIcons = menuIcons;
        this.menuButtons = new MenuButton[numberOfButtons];
     
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        
        for(int i = 0; i < numberOfButtons; i++)
        {
            MenuButton button = new MenuButton(menuIcons, 375, 75, 0, (150 * i));
            if(i < numberOfButtons - 1)
                button.setBorder(new EmptyBorder(0, 0, 25, 0));
            this.add(button);
            button.addMouseListener(this);
            menuButtons[i] = button;
        }
    }
    
    public void registerButtonPanelListener(ButtonPanelListener listener)
    {
        unregisterButtonPanelListener(listener);
        buttonPanelListeners.add(listener);
    }
    
    public void unregisterButtonPanelListener(ButtonPanelListener listener)
    {
        if(buttonPanelListeners.contains(listener))
            buttonPanelListeners.remove(listener);
    }
    
    @Override
    public void mouseClicked(MouseEvent paramMouseEvent)
    {
    }

    @Override
    public void mouseEntered(MouseEvent paramMouseEvent)
    {
        MenuButton button = (MenuButton) paramMouseEvent.getSource();
        button.setHovering(true);
    }

    @Override
    public void mouseExited(MouseEvent paramMouseEvent)
    {
        MenuButton button = (MenuButton) paramMouseEvent.getSource();
        button.setHovering(false);
    }

    @Override
    public void mousePressed(MouseEvent paramMouseEvent)
    {
    }

    @Override
    public void mouseReleased(MouseEvent paramMouseEvent)
    {
        int buttonId = 0;
        for(int i = 0; i < menuButtons.length; i++)
        {
            if(menuButtons[i].equals(paramMouseEvent.getSource()))
                buttonId = i;
        }
        for(ButtonPanelListener listener : buttonPanelListeners)
        {
            listener.buttonClicked((MenuButton) paramMouseEvent.getSource(), buttonId);
        }
    }
    
}
