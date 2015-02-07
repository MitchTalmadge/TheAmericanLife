package me.MitchT.AmericanLife.Components;

import me.MitchT.AmericanLife.Entities.Entity;

public abstract class Component
{
    private Entity parentEntity;
    public Class<? extends Component>[] requiredComponents;    
    
    public abstract void initialize();

    public Class<? extends Component>[] getRequiredComponents()
    {
        return this.requiredComponents;
    }
    
    public Entity getParentEntity()
    {
        return parentEntity;
    }
    
    public void setParentEntity(Entity parentEntity)
    {
        this.parentEntity = parentEntity;
    }
}
