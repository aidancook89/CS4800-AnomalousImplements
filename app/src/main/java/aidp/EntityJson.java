package aidp;

import java.util.ArrayList;

/**
 * Class EntityJson
 */
public class EntityJson {
    public String type;
    public String name;
    public String color;
    public ArrayList<String> modifiers;
    public ArrayList<String> potion_effects;
    public ArrayList<String> generic_effects;

    /**
     * EntityJson
     * @param type - Entity Type
     * @param name - Entity Name
     * @param color - Name color
     * @param potion_effects - ArrayList of potion effects to be applied to entity
     * @param modifiers - ArrayList of entity modifiers to be applied
     * @param generic_effects - ArrayList of generic entity effects to be applied
     */
    public EntityJson (
        String type, 
        String name, 
        String color,
        ArrayList<String> potion_effects,
        ArrayList<String> modifiers,
        ArrayList<String> generic_effects) {
            this.type = type;
            this.name = name;
            this.color = color;
            this.potion_effects = potion_effects;
            this.modifiers = modifiers;
            this.generic_effects = generic_effects;
    }
}
