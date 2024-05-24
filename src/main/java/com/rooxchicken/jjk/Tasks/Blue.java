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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.rooxchicken.jjk.JJKPlugin;

import net.minecraft.server.network.TextFilter.e;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.level.block.SoundEffectType;

public class Blue extends Task
{
    private Player player;
    private Location start;
    private int t = 0;
    private int ticks = 0;

    private double size = 0;
    private int count = 20;

    private double[] cacheX;
    private double[] cacheZ;

    private Color[] colors;

    public Blue(Plugin _plugin, Player _player)
    {
        super(_plugin);
        player = _player;
        start = player.getLocation().clone().add(0, 1, 0);
        tickThreshold = 1;

        cacheX = new double[count];
        cacheZ = new double[count];

        for(int i = 0; i < count; i++)
        {
            cacheX[i] = -1;
            cacheZ[i] = -1;
        }

        colors = new Color[] {Color.BLUE, Color.AQUA, Color.BLUE};

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 2);
    }

    @Override
    public void run()
    {
        JJKPlugin.tasks.add(new BlueEffect(null, start, player));
        for(Object e : JJKPlugin.getNearbyEntities(start, (int)Math.ceil(size)*4))
        {
            if(e instanceof Entity && player != e)
            {
                Entity entity = (Entity)e;
                //double distance = 3-JJKPlugin.ClampD(start.distance(entity.getLocation()), 0, 3);
                entity.setVelocity(start.clone().subtract(entity.getLocation()).toVector().multiply(size));
            }
        }

        double offset = 0.02;
        for(int i = 0; i < count; i++)
        {
            double sphereOffset = Math.sin(Math.toRadians((i * (90.0/count) ) * 2 - 90.0));
            double sphereOffsetXZ = Math.sin(Math.toRadians((i * (90.0/count) ) * 2));
            //double yDirOffset = Math.sin(Math.toRadians((i*(180.0/count))) * (90.0/count));
            for(int k = 0; k < count; k++)
            {
                Location particlePos = start.clone();
                double xOffset = 0;
                double zOffset = 0;

                if(cacheX[k] == -1)
                {
                    double rad = Math.toRadians(k*(360.0/count));
                    xOffset = Math.sin(rad);
                    zOffset = Math.cos(rad);
                    
                    cacheX[k] = xOffset;
                    cacheZ[k] = zOffset;
                }
                else
                {
                    xOffset = cacheX[k];
                    zOffset = cacheZ[k];
                }

                player.getWorld().spawnParticle(Particle.REDSTONE, particlePos.add(xOffset * sphereOffsetXZ * size, sphereOffset * size, zOffset * sphereOffsetXZ * size), 1, offset, offset, offset, new Particle.DustOptions(colors[(int)(Math.random()*3)], 1f));
                if(particlePos.getBlock().getType().isSolid())
                    cancel = true;
            }
        }

        if(player.isSneaking() && t == 0)
        {
            if(!JJKPlugin.useCursedEnergy(player, 3))
            {
                player.getWorld().playSound(start, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
                t = 1;
            }

            size += Math.pow(0.6, ticks) * 0.2;
            ticks++;

            double rad = Math.toRadians(start.getYaw()+90);
            start = player.getLocation().clone().add(Math.cos(rad), 1, Math.sin(rad));
            return;
        }
        else if(t == 0)
        {
            player.getWorld().playSound(start, Sound.ENTITY_BAT_TAKEOFF, 1, 1);

        }


        start.add(start.getDirection());

        t++;

        if(t > 20*size*2)
            cancel = true;
    }

    public Player getPlayer() { return player; }
    
}
