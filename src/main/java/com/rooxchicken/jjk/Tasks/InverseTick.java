package com.rooxchicken.jjk.Tasks;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

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
import com.rooxchicken.jjk.CursedTechniques.Inverse;
import com.rooxchicken.jjk.CursedTechniques.Shrine;

import net.minecraft.server.network.TextFilter.e;

public class InverseTick extends Task
{
    public int tickThreshold = 1;

    public InverseTick(Plugin _plugin)
    {
        super(_plugin);
    }

    @Override
    public void run()
    {
        ArrayList<Player> toRemove = new ArrayList<Player>();
        for(Player player : Inverse.inversed)
        {
            if(!JJKPlugin.useCursedEnergy(player, 1))
            {
                toRemove.add(player);
            }
        }

        for(Player player : toRemove)
        {
            Inverse.inversed.remove(player);
        }
    }
}
