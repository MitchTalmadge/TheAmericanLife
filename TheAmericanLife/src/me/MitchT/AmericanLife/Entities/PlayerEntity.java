package me.MitchT.AmericanLife.Entities;

import java.awt.Point;
import java.awt.image.BufferedImage;

import me.MitchT.AmericanLife.Components.Component;
import me.MitchT.AmericanLife.Components.HumanInputComponent;

public class PlayerEntity extends Entity
{
    public PlayerEntity(Point position, BufferedImage spriteSheet)
    {
        super(1, new Component[] {new HumanInputComponent()});
    }
}
