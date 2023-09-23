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

    private String playerParticle = "";
    private String entityParticle = "";

    private ArrayList<String> enchantmentsList = new ArrayList<String>();
    private ArrayList<String> attributeModifiersList = new ArrayList<String>();
    private ArrayList<String> playerPotionEffectList = new ArrayList<String>();
    private ArrayList<String> entityPotionEffectList = new ArrayList<String>();

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
            getEntityPotionEffectString(),
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
    // HELD POTION EFFECTS
    //////////////////////////////// 
	public void addPlayerPotionEffect(String effect, int amount, boolean hideParticles) {
        String potionCommand = String.format(
            "execute as @a[nbt={SelectedItem:{tag:{CustomID:%d}}}] run effect give @s minecraft:%s 1 %d %b",
            customID, effect, amount, hideParticles);
        playerPotionEffectList.add(potionCommand);
	}

    public String getPlayerPotionEffectString() {
        String output = String.format("############ %s:%s:%d ############\n", 
            type, nameText, customID);
        for (int i = 0; i < playerPotionEffectList.size(); i++) {
            output += playerPotionEffectList.get(i) + "\n";
        }
        return output;
    }
    


    ////////////////////////////////
    // ATTACK POTION EFFECTS
    //////////////////////////////// 
	public void addEntityPotionEffect(String effect, int amount, boolean hideParticles) {
        String command = String.format("effect give @s %s 1 %d %b", effect, amount, hideParticles);
        entityPotionEffectList.add(command);
	}

    public String getEntityPotionEffectString() {
        String output = String.format("");
        for (int i = 0; i < entityPotionEffectList.size(); i++) {
            output += entityPotionEffectList.get(i) + "\n";
        }
        return output;
    }



	////////////////////////////////
    // PARTICLES
    //////////////////////////////// 
    public void setPlayerParticle(String particle) {
        playerParticle = particle; 
    }

    public String getPlayerParticleString() {
        return String.format("particle %s ~ ~1 ~ 0 0 0 0.3 20 force", playerParticle);
    }

    public void setEntityParticle(String particle) {
        entityParticle = particle; 
    }

    public String getEntityParticleString() {
        return String.format("execute as @s run particle %s ~ ~1 ~ 0 0 0 0.3 20 force", entityParticle);
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