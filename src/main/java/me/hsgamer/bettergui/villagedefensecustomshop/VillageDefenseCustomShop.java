package me.hsgamer.bettergui.villagedefensecustomshop;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.lib.core.common.Validate;
import me.hsgamer.bettergui.lib.core.config.Config;
import me.hsgamer.bettergui.villagedefensecustomshop.action.GiveOrbAction;
import me.hsgamer.bettergui.villagedefensecustomshop.requirement.OrbRequirement;
import plugily.projects.villagedefense.arena.ArenaRegistry;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class VillageDefenseCustomShop extends BetterGUIAddon {

    @Override
    public void onEnable() {
        RequirementBuilder.INSTANCE.register(OrbRequirement::new, "orb");
        ActionBuilder.INSTANCE.register(GiveOrbAction::new, "give-orb");
    }

    @Override
    public void onPostEnable() {
        Config config = getConfig();
        Map<Integer, String> defaultMap = convert(config.getNormalizedValues("default", false));
        ArenaRegistry.getArenas().forEach(arena -> {
            Map<Integer, String> map = defaultMap;
            if (config.contains(arena.getId())) {
                map = convert(config.getNormalizedValues(arena.getId(), false));
            }
            if (!map.isEmpty()) {
                MenuOpener opener = new MenuOpener(arena);
                opener.getMap().putAll(map);
                arena.getShopManager().setOpenMenuConsumer(opener);
            }
        });
    }

    private Map<Integer, String> convert(Map<String, Object> map) {
        Map<Integer, String> converted = new HashMap<>();
        map.forEach((k, v) -> Validate.getNumber(k).map(BigDecimal::intValue).ifPresent(wave -> converted.put(wave, String.valueOf(v))));
        return converted;
    }
}
