package me.MitchT.AmericanLife;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainMenu extends JFrame implements ActionListener
{
    
    JButton[] menuButtons = new JButton[3];
    
    public MainMenu()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        this.setContentPane(mainPanel);
        
        this.setPreferredSize(new Dimension(800, 600));
        this.setResizable(false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));
        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        
        JButton startButton = new JButton("Start Game");
        JButton optionsButton = new JButton("Options");
        JButton quitButton = new JButton("Quit");
        
        menuButtons[0] = startButton;
        menuButtons[1] = optionsButton;
        menuButtons[2] = quitButton;
        
        for(JButton button : menuButtons)
        {
            button.addActionListener(this);
            buttonPanel.add(button);
        }
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        
        if(arg0.getSource().equals(menuButtons[0]))
        { // Play
            new Game(this).start();
        }
        else if(arg0.getSource().equals(menuButtons[1])) // Options
        {
            
        }
        else if(arg0.getSource().equals(menuButtons[2])) // Quit
        {
            System.exit(0);
        }
        
    }
}
