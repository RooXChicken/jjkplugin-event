package com.rooxchicken.jjk.Tasks;

import java.util.List;
import java.util.Collection;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;
import com.rooxchicken.jjk.CursedTechniques.Shrine;

import net.minecraft.server.network.TextFilter.e;

public class InfinityBarrier extends Task
{
    public int tickThreshold = 1;

    private Player player;

    private int i = 0;

    public InfinityBarrier(Plugin _plugin, Player _player)
    {
        super(_plugin);

        player = _player;
        player.getPersistentDataContainer().set(JJKPlugin.maxCursedEnergyKey, PersistentDataType.INTEGER, 2000);
    }

    @Override
    public void run()
    {
        if(!JJKPlugin.useCursedEnergy(player, 1))
        {
            cancel = true;
            return;
        }
        
        Object[] entities = JJKPlugin.getNearbyEntities(player.getLocation(), 2);
            
        for(Object _e : entities)
        {
            Entity e = (Entity)_e;
            if(e != player)
            {
                double distance = JJKPlugin.ClampD(player.getLocation().distance(e.getLocation()), 0, 3)*4;
                e.setVelocity(e.getVelocity().multiply(Math.pow(2.0, -(12-distance))));

                player.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 1, 0, 0, 0, new Particle.DustOptions(Color.WHITE, (float)(3-distance/4)));
            }
        }

        i++;
    }

    public boolean checkDisabled(Player disable)
    {
        if(i == 0)
            return false;
        
        if(player.getName().equals(disable.getName()))
        {
            cancel = true;
            return true;
        }

        return false;
    }

    public boolean cancelDamage(Player e, double damage)
    {
        if(player.getName().equals(e.getName()))
        {
            JJKPlugin.useCursedEnergy(player, (int)Math.ceil(damage*2));
            return true;
        }

        return false;
    }
}
