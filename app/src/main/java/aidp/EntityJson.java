package aidp;

import java.util.ArrayList;


public class EntityJson {
    public String type;
    public String name;
    public String color;
    public ArrayList<String> potionList;
    public ArrayList<String> modifiers;
    public ArrayList<String> generic;

    public EntityJson (
        String type, 
        String name, 
        String color,
        ArrayList<String> potionList,
        ArrayList<String> modifiers,
        ArrayList<String> generic) {
            this.type = type;
            this.name = name;
            this.color = color;
            this.potionList = potionList;
            this.modifiers = modifiers;
            this.generic = generic;
    }
}
