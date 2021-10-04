package me.hsgamer.bettergui.villagedefensecustomshop.requirement;

import me.hsgamer.bettergui.api.requirement.TakableRequirement;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.core.expression.ExpressionUtils;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plugily.projects.villagedefense.Main;
import plugily.projects.villagedefense.api.StatsStorage;
import plugily.projects.villagedefense.arena.ArenaRegistry;
import plugily.projects.villagedefense.arena.options.ArenaOption;
import plugily.projects.villagedefense.user.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class OrbRequirement extends TakableRequirement<Integer> {
    private final Main plugin;
    private final Map<UUID, Integer> checked = new HashMap<>();

    public OrbRequirement(String name) {
        super(name);
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    @Override
    public Integer getParsedValue(UUID uuid) {
        String parsed = VariableManager.setVariables(String.valueOf(value).trim(), uuid);
        return Optional.ofNullable(ExpressionUtils.getResult(parsed)).map(BigDecimal::intValue).orElseGet(() -> {
            MessageUtils.sendMessage(uuid, MessageConfig.INVALID_NUMBER.getValue().replace("{input}", parsed));
            return 0;
        });
    }

    @Override
    protected boolean getDefaultTake() {
        return true;
    }

    @Override
    protected Object getDefaultValue() {
        return "0";
    }

    @Override
    protected void takeChecked(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        User user = plugin.getUserManager().getUser(player);
        int orbs = checked.remove(player.getUniqueId());
        int currentOrbs = user.getStat(StatsStorage.StatisticType.ORBS);
        user.setStat(StatsStorage.StatisticType.ORBS, currentOrbs - orbs);
        Optional.ofNullable(ArenaRegistry.getArena(player)).ifPresent(arena -> arena.addOptionValue(ArenaOption.TOTAL_ORBS_SPENT, orbs));
    }

    @Override
    public boolean check(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return false;
        }
        User user = plugin.getUserManager().getUser(player);
        int orbs = getParsedValue(uuid);
        if (orbs > 0 && user.getStat(StatsStorage.StatisticType.ORBS) < orbs) {
            return false;
        }
        checked.put(player.getUniqueId(), orbs);
        return true;
    }
}
