package me.MitchT.AmericanLife.Components;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This Enum determines intialization order of Components.
 * <hr>
 * <ul>
 * <li>Lower # = Higher Priority = Sooner Initialization; For example:<br>
 * If ComponentA is priority #1 and ComponentB is priority #2, ComponentA will
 * be initialized <b>first</b>.</li><br>
 * <li>Multiple Components can have the same priority.</li><br>
 * <li><b>clazz = null</b> represents all unspecified components.</li>
 * <hr>
 * 
 * @author Mitch Talmadge (mitcht@aptitekk.com)
 *
 */
public enum ComponentPriorityEnum
{
    //Lower #s = Higher Priority = Sooner Initialization; For example:
    //If ComponentA is priority #1 and ComponentB is priority #2, ComponentA will be initialized FIRST.
    //Multiple Components can have the same priority.
    //clazz = null represents all unspecified components.
    
    UNSPECIFIED(10, null);
    
    private int priority;
    private Class<? extends Component> clazz;
    
    ComponentPriorityEnum(int priority, Class<? extends Component> clazz)
    {
        this.priority = priority;
        this.clazz = clazz;
    }
    
    public static ComponentPriorityEnum getEnumByClass(
            Class<? extends Component> clazzToMatch)
    {
        for(ComponentPriorityEnum enom : values())
        {
            if(enom.clazz != null && enom.clazz.equals(clazzToMatch))
                return enom;
        }
        return UNSPECIFIED;
    }
    
    /**
     * Sorts the Component List provided by priority and returns it as an argument.
     * @param unsortedList The list of Components to be sorted.
     * @return the sorted list of Components
     */
    public static ArrayList<Component> sortComponentList(ArrayList<Component> unsortedList)
    {
        ArrayList<Component> sortedList = new ArrayList<>();
        
        while(!unsortedList.isEmpty())
        {
            int lowestID = -1;
            int lowestIndex = -1;
            int currIndex = -1;
            for(Component component : unsortedList)
            {
                currIndex++;
                Class<? extends Component> compClazz = component.getClass();
                ComponentPriorityEnum enom = getEnumByClass(compClazz);
                if(lowestID == -1 || enom.priority < lowestID)
                {
                    lowestID = enom.priority;
                    lowestIndex = currIndex;
                }
            }
            if(lowestID > 0)
            {
                sortedList.add(unsortedList.get(lowestIndex));
                unsortedList.remove(lowestIndex);
            }
        }
        return sortedList;
    }
}
