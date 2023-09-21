tag @s add am_the_attacker
advancement revoke @s only aidp:deal_damage
execute as @s[nbt={SelectedItem:{tag:{custom_item:1}}}] run execute as @e[distance=..5,nbt={HurtTime:10s},tag=!am_the_attacker] run function aidp:swords/sword1 
tag @s remove am_the_attacker