package me.MitchT.AmericanLife.LevelLoader;

public class InvalidLevelException extends Exception
{
    
    public InvalidLevelException()
    {
        super();
    }
    
    public InvalidLevelException(String details)
    {
        super(details);
    }
    
}
