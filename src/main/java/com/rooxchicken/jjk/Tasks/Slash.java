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

import net.minecraft.server.network.TextFilter.e;

public class Slash extends Task
{
    public int tickThreshold = 1;

    private Player player;
    private Location startPos;
    private boolean randomDirection = false;
    private boolean doesDamage = false;
    private int damage;

    private double xOffset = 0;
    private double zOffset = 0;
    private double variety = Math.random()*2-1;

    private int i = 0;

    public Slash(Plugin _plugin, Player _player, Location _startPos, boolean _randomDirection, boolean _doesDamage, int _damage)
    {
        super(_plugin);

        player = _player;
        startPos = _startPos.clone();
        randomDirection = _randomDirection;
        doesDamage = _doesDamage;
        damage = _damage;

        double yaw = Math.toRadians(startPos.getYaw());
        xOffset = Math.cos(yaw)/3;
        zOffset = Math.sin(yaw)/3;

        player.getWorld().playSound(startPos, Sound.ENTITY_BLAZE_SHOOT, 0.4f, 1);
        player.getPersistentDataContainer().set(JJKPlugin.maxCursedEnergyKey, PersistentDataType.INTEGER, 2000);

        if(randomDirection)
            startPos.setDirection(new Vector(Math.random()*2-1, Math.random()*2-1, Math.random()*2-1));
    }

    @Override
    public void run()
    {
        exec();
        exec();

        if(i > 400)
            cancel = true;
    }

    private void exec()
    {
        Location slashLoc = startPos.clone();
        for(int k = 0; k < 3; k++)
        {
            Location slash = slashLoc.clone().add(slashLoc.getDirection().multiply(i+0.1)).add(new Vector(xOffset*(1-k), 2 + (1-k)*variety, zOffset*(1-k)));
            player.getWorld().spawnParticle(Particle.REDSTONE, slash, 1, 0, 0, 0, new Particle.DustOptions(Color.RED, 0.6f));
            player.getWorld().spawnParticle(Particle.REDSTONE, slash, 1, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 1));

            if(slash.getBlock().getType().isSolid() || slash.getBlock().getType() == Material.COBWEB)
            {
                if(doesDamage && slash.getBlock().getType() != Material.BEDROCK)
                    slash.getBlock().setType(Material.AIR);
                cancel = true;
            }

            if(k == 1)
            {

            Object[] entities = JJKPlugin.getNearbyEntities(slash, 2);

            for(Object _e : entities)
            {
                Entity e = (Entity)_e;
                if (e instanceof LivingEntity)
                {
                    if(e != player)
                    {
                        if(doesDamage)
                            ((LivingEntity)e).damage(damage);
                        cancel = true;
                    }
                }
            }
        }
            
        }

        i++;
    }
    
}
