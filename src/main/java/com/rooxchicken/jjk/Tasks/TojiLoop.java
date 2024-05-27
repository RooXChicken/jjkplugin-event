package com.rooxchicken.jjk.Tasks;

import java.util.List;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;

import net.minecraft.server.network.TextFilter.e;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.level.block.SoundEffectType;

public class TojiLoop extends Task
{
    public TojiLoop(Plugin _plugin, Location _moveTo, Player _player)
    {
        super(_plugin);
        tickThreshold = 20;
    }

    @Override
    public void run()
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            PersistentDataContainer data = player.getPersistentDataContainer();
            if(!data.has(JJKPlugin.cursedTechniqueKey, PersistentDataType.INTEGER))
            {
                int ct = data.get(JJKPlugin.cursedTechniqueKey, PersistentDataType.INTEGER);
                if(ct == 3)
                {
                    JJKPlugin.hasToji = true;
                    PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 2);
                    player.addPotionEffect(strength);
                    PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 20, 2);
                    player.addPotionEffect(speed);
                }
            }
        }
    }
    
}
