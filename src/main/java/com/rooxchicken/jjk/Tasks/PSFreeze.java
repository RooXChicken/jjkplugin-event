package com.rooxchicken.jjk.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.CursedTechniques.ProjectionSorcery;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PSFreeze extends Task
{
    public PSFreeze(Plugin _plugin)
    {
        super(_plugin);
        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            if(player.hasPotionEffect(PotionEffectType.SPEED))
            {
                if(player.getPotionEffect(PotionEffectType.SPEED).getAmplifier() == 5)
                {
                    player.getWorld().spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation()).multiply(0.5), 40, 0.1, 0.4, 0.1, new Particle.DustOptions(Color.WHITE, 1));
                }
            }
        }
        for(Entity entity : ProjectionSorcery.playerFrozenMap.keySet())
        {
            PersistentDataContainer data = entity.getPersistentDataContainer();
            int freezeTime = data.get(ProjectionSorcery.freezeTimeKey, PersistentDataType.INTEGER);
            data.set(ProjectionSorcery.freezeTimeKey, PersistentDataType.INTEGER, --freezeTime);

            if(freezeTime < 0)
            {
                data.remove(ProjectionSorcery.freezeTimeKey);
                ProjectionSorcery.playerFrozenMap.remove(entity);
                entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                return;
            }

            entity.teleport(ProjectionSorcery.playerFrozenMap.get(entity));
            entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 40, 0.5, 0.4, 0.5, new Particle.DustOptions(Color.WHITE, 1));
            //player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 0, 0, 0, new Particle.DustOptions(Color.WHITE, (float)(3-distance/4)));
        }
    }
}
