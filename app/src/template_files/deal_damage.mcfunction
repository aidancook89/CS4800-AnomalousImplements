tag @s add am_the_attacker
advancement revoke @s only aidp:deal_damage
execute if predicate aidp:holding_item/custom_sword as @e[distance=..10,nbt={HurtTime:10s},tag=!am_the_attacker] at @s run function aidp:swords/sword1
tag @s remove am_the_attacker