package com.abstractwolf.cursedeffects.manager;

import com.abstractwolf.cursedeffects.manager.data.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private Map<UUID, UserData> userCache;

    public UserManager() {
        userCache = new HashMap<>();
    }

    public void addToCache(UUID uuid) {
        if (userCache.containsKey(uuid)) {
            return;
        }
        userCache.put(uuid, new UserData(uuid));
    }

    public void removeFromCache(UUID uuid) {
        if (!userCache.containsKey(uuid)) {
            return;
        }
        userCache.remove(uuid);
    }

    public UserData getUserData(UUID uuid) {
        return userCache.get(uuid);
    }
}
