package aidp;

import java.util.ArrayList;

public class Sword {
    public String tag;
    public String type;

    public String dealDamageString = "";
    public int customID = 0;

    public String nameText = "default";
    public String nameColor = "white"; 
    public boolean nameBold = false;
    public boolean nameItalic = false;
    public boolean nameUnderlined = false;
    public boolean nameStrikethrough = false;
    public boolean nameObfuscated = false;

    public String loreText = "default";
    public String loreColor = "white";
    public boolean loreBold = false;
    public boolean loreItalic = false;
    public boolean loreUnderlined = false;
    public boolean loreStrikethrough = false;
    public boolean loreObfuscated = false;

    public String playerParticle = "";
    public String entityParticle = "";

    public ArrayList<String> enchantmentsList = new ArrayList<String>();
    public ArrayList<String> attributeModifiersList = new ArrayList<String>();
    public ArrayList<String> playerPotionEffectList = new ArrayList<String>();
    public ArrayList<String> entityPotionEffectList = new ArrayList<String>();

    public Sword(String type, int customID) {
		this.type = type;
		this.customID = customID;
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
    // NAME
    //////////////////////////////// 
    public void setName(String name, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
		this.nameText = name;
		nameColor = color;
		nameBold = bold;
		nameItalic = italic;
		nameUnderlined = underlined;
		nameStrikethrough = strikethrough;
		nameObfuscated = obfuscated;
	}

    public String getNameString() {
        String output = String.format("Name:'{\"text\":\"%s\",\"color\":\"%s\"", nameText, nameColor);
        if (nameBold) output += String.format(",\"bold\":%b", nameBold);
        if (nameItalic) output += String.format(",\"italic\":%b", nameItalic);
        if (nameUnderlined) output += String.format(",\"underline\":%b", nameUnderlined);
        if (nameStrikethrough) output += String.format(",\"strikethrough\":%b", nameStrikethrough);
        if (nameObfuscated) output += String.format(",\"obfuscated\":%b", nameObfuscated);
        output += "}'";
        return output;
    }

	

    ////////////////////////////////
    // LORE
    //////////////////////////////// 
    public void setLore(String lore, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
		this.loreText = lore;
		loreColor = color;
		loreBold = bold;
		loreItalic = italic;
		loreUnderlined = underlined;
		loreStrikethrough = strikethrough;
		loreObfuscated = obfuscated;
	}

    public String getLoreString() {
        String output = String.format("Lore:['{\"text\":\"%s\",\"color\":\"%s\"", loreText, loreColor);
        if (loreBold) output += String.format(",\"bold\":%b", loreBold);
        if (loreItalic) output += String.format(",\"italic\":%b", loreItalic);
        if (loreUnderlined) output += String.format(",\"underline\":%b", loreUnderlined);
        if (loreStrikethrough) output += String.format(",\"strikethrough\":%b", loreStrikethrough);
        if (loreObfuscated) output += String.format(",\"obfuscated\":%b", loreObfuscated);
        output += "}']";
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