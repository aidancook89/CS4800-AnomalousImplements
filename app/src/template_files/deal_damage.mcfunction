say ran deal_damage.mcfunction

tag @s add am_the_attacker
advancement revoke @s only aidp:deal_damage
execute as @e[distance=..10,nbt={HurtTime:10s},tag=!am_the_attacker] at @s run say test
tag @s remove am_the_attacker