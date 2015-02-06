package me.MitchT.AmericanLife.MainMenu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import me.MitchT.AmericanLife.Game;

public class MainMenu extends JFrame implements ButtonPanelListener
{
    
    public MainMenu()
    {
        
        BufferedImage menuIcons = null;
        BufferedImage logoIcon = null;
        BufferedImage backgroundIcon = null;
        try
        {
            menuIcons = ImageIO.read(getClass().getResource("/assets/images/MenuIcons.png"));
            logoIcon = ImageIO.read(getClass().getResource("/assets/images/LogoIcon.png"));
            backgroundIcon = ImageIO.read(getClass().getResource("/assets/images/BackgroundIcon.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
        
        JPanelWithBackground mainPanel = new JPanelWithBackground(backgroundIcon);
        mainPanel.setLayout(new BorderLayout());
        
        this.setContentPane(mainPanel);
        
        this.setPreferredSize(new Dimension(800, 540));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //---- LOGO ----//
        
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        getContentPane().add(logoPanel, BorderLayout.NORTH);
        
        JLabel logoLabel = new JLabel(new ImageIcon(logoIcon));
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        
        //---- BUTTONS ----//
        JButtonPanel buttonPanel = new JButtonPanel(menuIcons, 3);
        buttonPanel.registerButtonPanelListener(this);
        buttonPanel.setBorder(new EmptyBorder(100, 0, 0, 25));
        this.getContentPane().add(buttonPanel, BorderLayout.EAST);
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(false);
    }

    @Override
    public void buttonClicked(MenuButton button, int buttonId)
    {
        switch(buttonId)
        {
            case 0:
                new Game(this).start();
                this.setContentPane(new JPanel());
                this.repaint();
                break;
            case 1:
                System.out.println("Options");
                break;
            case 2:
                System.exit(0);
                break;
            default:
                break;
        }
    }
}
