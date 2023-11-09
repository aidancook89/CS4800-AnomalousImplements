package aidp;

import java.util.ArrayList;


public class EntityJson {
    public String type;
    public String name;
    public String color;
    public ArrayList<String> modifiers;
    public ArrayList<String> potion_effects;
    public ArrayList<String> generic_effects;

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
