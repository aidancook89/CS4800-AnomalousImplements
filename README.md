# Anomalous Implements 
## AI Enhanced Minecraft Datapack Generator

Generates a randomized Minecraft datapack containing custom Swords and Entities using the OpenAI API. 

How to use:
1. Download project and move to root directory
2. Run "./gradlew build"
3. Run "./gradlew run -PapiKey={YOUR OPEN AI API KEY}"
4. Locate the newly generated datapack folder in the current user's Downloads folder
5. Copy or Move this folder into "C:\Users\{CURRENT_USER}\AppData\Roaming\.minecraft\saves\{DESIRED_WORLD}\datapacks"
6. Open the world in Minecraft
7. Type "/" to open the command console in Minecraft, then type "reload" and press enter
8. The console should respond with "Reloading" followed by "Data Pack Loaded" if load is successful
