package me.MitchT.AmericanLife.Components;


/**
 * This component takes advantage of other registered Components of an Entity to
 * allow for human input controlling those Components.
 * 
 * @author Mitch
 *
 */
public class HumanInputComponent extends Component
{
    private boolean[] movementDir = new boolean[4];
    
    public HumanInputComponent()
    {
        ;
    }
    
    @Override
    public void initialize()
    {
        ;
    }
}
