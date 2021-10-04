package me.hsgamer.bettergui.villagedefensecustomshop.requirement;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.java.JavaPlugin;
import plugily.projects.villagedefense.ConfigPreferences;
import plugily.projects.villagedefense.Main;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.ArenaRegistry;
import plugily.projects.villagedefense.handlers.language.Messages;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WolfLimitRequirement extends BaseRequirement<Boolean> {
    private final Main plugin;

    public WolfLimitRequirement(String name) {
        super(name);
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    @Override
    public Boolean getParsedValue(UUID uuid) {
        return Boolean.parseBoolean(String.valueOf(value));
    }

    @Override
    public boolean check(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return false;
        }
        Arena arena = ArenaRegistry.getArena(player);
        if (arena == null) {
            return false;
        }

        int spawnedAmount = 0;
        List<Wolf> wolves = arena.getWolves();

        if (plugin.getConfigPreferences().getOption(ConfigPreferences.Option.CAN_BUY_GOLEMSWOLVES_IF_THEY_DIED)) {
            wolves = wolves.stream().filter(Wolf::isDead).collect(Collectors.toList());
        }

        String spawnedName = plugin.getChatManager().colorMessage(Messages.SPAWNED_WOLF_NAME).replace("%player%", player.getName());

        for (Wolf wolf : wolves) {
            if (spawnedName.equals(wolf.getCustomName())) {
                spawnedAmount++;
            }
        }

        int spawnLimit = plugin.getConfig().getInt("Wolves-Spawn-Limit", 20);
        return getParsedValue(uuid).equals(spawnedAmount < spawnLimit);
    }

    @Override
    public void take(UUID uuid) {
        // EMPTY
    }
}
