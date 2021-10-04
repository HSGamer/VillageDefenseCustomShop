package me.hsgamer.bettergui.villagedefensecustomshop.action;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.lib.core.common.Validate;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plugily.projects.villagedefense.Main;
import plugily.projects.villagedefense.api.StatsStorage;
import plugily.projects.villagedefense.user.User;

import java.math.BigDecimal;
import java.util.UUID;

public class GiveOrbAction extends BaseAction {
    private final Main plugin;

    public GiveOrbAction(String string) {
        super(string);
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    @Override
    public void addToTaskChain(UUID uuid, TaskChain<?> taskChain) {
        Validate.getNumber(getReplacedString(uuid))
                .map(BigDecimal::intValue)
                .ifPresent(orb -> {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        return;
                    }
                    User user = plugin.getUserManager().getUser(player);
                    taskChain.sync(() -> user.addStat(StatsStorage.StatisticType.ORBS, orb));
                });
    }
}
