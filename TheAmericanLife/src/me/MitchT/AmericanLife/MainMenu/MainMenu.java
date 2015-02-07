package me.MitchT.AmericanLife.MainMenu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import me.MitchT.AmericanLife.Game;
import me.MitchT.AmericanLife.Main;

public class MainMenu extends JFrame implements ButtonPanelListener
{
    
    public MainMenu()
    {
        Main.getAudioManager().play("/assets/music/TheEntertainer.mp3");
        
        BufferedImage menuIcons = null;
        BufferedImage logoIcon = null;
        BufferedImage backgroundIcon = null;
        BufferedImage creditsIcon = null;
        BufferedImage businessMan = null;
        BufferedImage pictureFrame = null;
        try
        {
            menuIcons = ImageIO.read(getClass().getResource("/assets/images/MenuIcons.png"));
            logoIcon = ImageIO.read(getClass().getResource("/assets/images/LogoIcon.png"));
            backgroundIcon = ImageIO.read(getClass().getResource("/assets/images/BackgroundIcon.png"));
            creditsIcon = ImageIO.read(getClass().getResource("/assets/images/CreditsIcon.png"));
            businessMan = ImageIO.read(getClass().getResource("/assets/images/spritesheets/BusinessMan.png"));
            pictureFrame = ImageIO.read(getClass().getResource("/assets/images/PictureFrame.png"));
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
        this.add(logoPanel, BorderLayout.NORTH);
        
        JLabel logoLabel = new JLabel(new ImageIcon(logoIcon));
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        
        //---- CREDITS ----//
        
        JPanel creditsPanel = new JPanel();
        creditsPanel.setOpaque(false);
        creditsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel creditsLabel = new JLabel(new ImageIcon(creditsIcon));
        creditsPanel.add(creditsLabel);
        this.add(creditsPanel, BorderLayout.SOUTH);
        
        //---- BUTTONS ----//
        JButtonPanel buttonPanel = new JButtonPanel(menuIcons, 3);
        buttonPanel.registerButtonPanelListener(this);
        buttonPanel.setBorder(new EmptyBorder(100, 0, 0, 25));
        this.add(buttonPanel, BorderLayout.EAST);
        
        //---- SPRITE ----//
        JPanel westPanel = new JPanel();
        westPanel.setOpaque(false);
        westPanel.setLayout(new BorderLayout());
        westPanel.add(Box.createVerticalStrut(40), BorderLayout.NORTH);
        westPanel.add(Box.createHorizontalStrut(40), BorderLayout.WEST);
        this.add(westPanel, BorderLayout.WEST);
        
        JPanelWithBackground pictureFramePanel = new JPanelWithBackground(pictureFrame);
        pictureFramePanel.setOpaque(false);
        pictureFramePanel.setPreferredSize(new Dimension(256,327));
        westPanel.add(pictureFramePanel, BorderLayout.CENTER);
        
        JSpritePanel spritePanel = new JSpritePanel(businessMan, 750, 11, 19, 2, 110, 190);
        spritePanel.setOpaque(false);
        spritePanel.setBorder(new EmptyBorder(95,0,0,20));
        pictureFramePanel.add(spritePanel);
        
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
