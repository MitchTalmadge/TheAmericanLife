package me.MitchT.AmericanLife.Entities;

import java.util.ArrayList;
import java.util.UUID;

import me.MitchT.AmericanLife.Components.Component;
import me.MitchT.AmericanLife.Components.ComponentPriorityEnum;

public abstract class Entity
{
    private ArrayList<Component> componentList;
    private UUID entityUUID;
    private int renderLayer;
    
    public Entity(int renderLayer, Component[] componentsToRegister)
    {
        this.renderLayer = renderLayer;
        this.componentList = new ArrayList<Component>();
        this.entityUUID = UUID.randomUUID();
        
        for(Component component : componentsToRegister)
            this.registerComponent(component);
        
        this.componentList = ComponentPriorityEnum.sortComponentList(componentList);
        for(Component component : componentList)
        {
            component.initialize();
        }
    }
    
    public UUID getUUID()
    {
        return this.entityUUID;
    }
    
    public int getRenderLayer()
    {
        return this.renderLayer;
    }
    
    public ArrayList<Component> getComponentList()
    {
        return this.componentList;
    }
    
    private void registerComponent(Component componentToReg)
    {
        Class<? extends Component> componentClass = componentToReg.getClass();
        for(Component component : componentList)
        {
            if(component.getClass() == componentClass)
            {
                return;
            }
        }
        
        componentList.add(componentToReg);
        componentToReg.setParentEntity(this);
        return;
    }
    
    public boolean hasComponentType(Class<? extends Component> componentClass)
    {
        for(Component component : componentList)
        {
            if(component.getClass() == componentClass)
            {
                return true;
            }
        }
        return false;
    }
    
    public <T extends Component> T getComponentByType(Class<T> componentClass)
    {
        for(Component component : componentList)
        {
            if(component.getClass() == componentClass)
            {
                return componentClass.cast(component);
            }
        }
        return null;
    }
    
}
