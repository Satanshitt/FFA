package cf.nytrux.ffa.object;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BetterUserManager {

    @Getter
    private static BetterUserManager instance = null;

    @Getter
    private final Map<UUID, GameUser> users;

    public BetterUserManager() {
        instance = this;

        this.users = new HashMap<>();
    }

    public final void loadUser(final UUID uuid) {
        this.users.put(uuid, new GameUser(uuid));
    }

    public final void unloadUser(final UUID uuid) {
        this.users.get(uuid).unload();
        this.users.remove(uuid);
    }

    private void unloadAllUsers() {
        for(Map.Entry<UUID, GameUser> user : this.users.entrySet())
            this.unloadUser(user.getKey());

    }

    public final void unload() {
        instance = null;

        this.unloadAllUsers();
    }
}
