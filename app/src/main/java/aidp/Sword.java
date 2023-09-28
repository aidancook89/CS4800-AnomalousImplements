package aidp;

import java.util.ArrayList;

public class Sword {
    private String tag;
    private String type;

    private int customID = 0;

    private int rarity = 0;
    private String[][] rarityLookup = new String[][] {
        {"COMMON", "white"},
        {"UNCOMMON", "green"},
        {"RARE", "blue"},
        {"EPIC", "purple"},
        {"LEGENDARY", "gold"}
    };


    private String nameText = "default";
    private String nameColor = "white"; 
    private String loreText = "default";

    private ArrayList<String> enchantmentsList = new ArrayList<String>();
    private ArrayList<String> attributeModifiersList = new ArrayList<String>();
    private ArrayList<String> playerPotionEffectList = new ArrayList<String>();
    private ArrayList<String> playerParticleList = new ArrayList<String>();
    private ArrayList<String> entityPotionEffectList = new ArrayList<String>();
    private ArrayList<String> entityParticleList = new ArrayList<String>();

    public Sword(String type, int customID, int rarity) {
		this.type = type;
		this.customID = customID;
        this.rarity = rarity;
    }

	public String getGiveCommand() {
		buildTag();
		return "give @a " + type + tag + " 1"; 
	} 

	public void buildTag() {
        tag = "{display:{";
        tag += getNameString() + ",";
        tag += getLoreString() + "}";
        tag += getEnchantmentsString();
        tag += getAttributesString();
        tag += String.format(",CustomID:%d", customID);
        tag += "}";
	}
    
        

    ////////////////////////////////
    // NAME AND LORE
    //////////////////////////////// 
    public void setName(String name, String color) {
		this.nameText = name;
		nameColor = color;
	}

    public String getNameString() {
        return String.format("Name:'{\"text\":\"%s\",\"color\":\"%s\",\"bold\":\"true\",\"italic\":\"false\"}'", nameText, nameColor);
    }

    public void setLore(String lore) {
		this.loreText = lore;
	}

    public String getLoreString() {
        String output = "Lore:[";
        output += "'{\"text\":\"\"}',";
        output += String.format("'{\"text\":\"%s\",\"color\":\"%s\",\"italic\":\"true\",\"underlined\":\"true\"}',", rarityLookup[rarity][0], rarityLookup[rarity][1]); 
        output += "'{\"text\":\"\"}',";
        output += String.format("'{\"text\":\"%s\",\"color\":\"%s\"}'", loreText, "gray");
        output += "]";
        return output;
    }



    ////////////////////////////////
    // ATTACK
    //////////////////////////////// 
    public String getDealDamageString() {
        return String.format("execute as @s[nbt={SelectedItem:{tag:{CustomID:%d}}}] run execute as @e[distance=..5,nbt={HurtTime:10s},tag=!am_the_attacker] run function aidp:swords/%s", customID, getAttackFunctionName());
    }

    public String getAttackFunctionName() {
        return String.format("sword%s", customID);
    }

    public String getAttackFunctionString() {
        return String.format("%s\n%s\n%s\n",
            getEntityPotionString(),
            getEntityParticleString(),
            getPlayerParticleString());
    }


    ////////////////////////////////
    // ENCHANTMENTS
    //////////////////////////////// 
    public void addEnchantment(String enchantment, int level) {
		enchantmentsList.add(String.format(
			"{id:\"minecraft:%s\",lvl:%ds}", 
			enchantment, level));
	}

    public String getEnchantmentsString() {
        String output = "";
        if (enchantmentsList.size() > 0) {
            output = String.format(",Enchantments:%s", listToString(enchantmentsList));
        }
        return output;
    }



    ////////////////////////////////
    // ATTRIBUTES
    //////////////////////////////// 
    public void addAttributeModifier(String modifier, int amount) {}
 
    public String getAttributesString() {
        String output = "";
        if (attributeModifiersList.size() > 0) {
            output = String.format(",AttributeModifiers:%s", listToString(attributeModifiersList));
        }
        return output;
    }

	

    ////////////////////////////////
    // POTION EFFECTS
    //////////////////////////////// 
	public void addPlayerPotion(String effect, int length, int amount, boolean hideParticles) {
        String potionCommand = String.format(
            "execute as @a[nbt={SelectedItem:{tag:{CustomID:%d}}}] run effect give @s minecraft:%s %d %d %b",
            customID, effect, length, amount, hideParticles);
        playerPotionEffectList.add(potionCommand);
	}

    public String getPlayerPotionString() {
        String output = String.format("############ %s:%s:%d ############\n", 
            type, nameText, customID);
        for (String item : playerPotionEffectList) {
            output += item + "\n";
        }
        return output;
    }
    
    public void addEntityPotion(String effect, int length, int amount, boolean hideParticles) {
        String command = String.format("effect give @s %s %d %d %b", effect, length, amount, hideParticles);
        entityPotionEffectList.add(command);
	}

    public String getEntityPotionString() {
        String output = "";
        for (String item : entityPotionEffectList) {
            output += item + "\n";
        }
        return output;
    }



	////////////////////////////////
    // PARTICLES
    //////////////////////////////// 
    public void addPlayerParticle(String particle) {
        String command = String.format("particle %s ~ ~1 ~ 0 0 0 0.3 20 force", particle);
        playerParticleList.add(command);
    }

    public String getPlayerParticleString() {
        String output = "";
        for (String item : playerParticleList) {
            output += item + "\n";
        }
        return output;
    }

    public void addEntityParticle(String particle) {
        String command = String.format("execute as @s run particle %s ~ ~1 ~ 0 0 0 0.3 20 force", particle);
        playerParticleList.add(command);
    }

    public String getEntityParticleString() {
        String output = "";
        for (String item : entityParticleList) {
            output += item + "\n";
        }
        return output;
    }



	////////////////////////////////
    // HELPER FUNCTIONS
    //////////////////////////////// 
	public static String listToString(ArrayList<String> arrayList) {
		String output = "[";
		for (String element : arrayList) {
			output += element + ",";	
		}
		if (output.length() > 1) output = output.substring(0, output.length()-1) + "]";
		else output += "]";
		return output;
	}
}