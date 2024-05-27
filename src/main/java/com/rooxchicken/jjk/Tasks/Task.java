package com.rooxchicken.jjk.Tasks;

import org.bukkit.plugin.Plugin;

public abstract class Task
{
    private Plugin plugin;
    public int id;

    private int tick = 0;
    public int tickThreshold = 1;
    public boolean cancel = false;

    public Task(Plugin _plugin) { plugin = _plugin; }

    public void tick()
    {
        tick++;
        if(tick < tickThreshold-1)
            return;

        run();
        tick = 0;
    }

    public void run() {}

    public void onCancel() {}
}
