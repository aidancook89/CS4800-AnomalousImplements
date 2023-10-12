package aidp;

import java.io.IOException;
import java.nio.file.Files;

public class LootTableBuilder {

    private String table;

    public LootTableBuilder(Sword s1) {
        table = createTable() + createPools(s1) + createConditions();
        try {
            Files.writeString(App.f_loot_table, table);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public String createTable() {
            String output = "{\n\t\"type\":\"minecraft:entity\",\n";
            return output;
        }

        public String createPools(Sword s1) {
            return "\t\"pools\": \t\t{\n\t\t\t\"rolls\" : 1,\n\t\t\t\"entries\" : [\n\t\t\t\t{"
            + "\n\t\t\t\t\t\"type\" : \"item\", \n\t\t\t\t\t\"name\" : \"" + s1.getType() + "\",\n\t\t\t\t\t\"functions\" : ["
            + "\n\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\"function\" : \"set_nbt\",\n\t\t\t\t\t\t\t\"tag\" : \"" + SwordBuilder.buildTag(s1)
            + "\"\n\t\t\t\t\t\t}\n\t\t\t\t\t]\n\t\t\t\t\n\t\t\t}\n\t\t\t],";
        }

        public String createConditions() {
            return "\n\t\t\t\"conditions\" : [\n\t\t\t\t{\n\t\t\t\t\t\"condition\" : "
            + "\"minecraft:random_chance\",\n\t\t\t\t\t\"chance\" : 1\n\t\t\t\t}\n\t\t\t]\n\t\t}\n\t]\n}";
        }

}
