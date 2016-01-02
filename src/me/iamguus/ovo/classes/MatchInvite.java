package me.iamguus.ovo.classes;

import java.util.UUID;

/**
 * Created by Guus on 2-1-2016.
 */
public class MatchInvite {

    private UUID inviter;
    private UUID invited;
    private Arena arena;

    public MatchInvite(UUID inviter, UUID invited, Arena arena) {
        this.inviter = inviter;
        this.invited = invited;
        this.arena = arena;
    }

    public UUID getInviter() {
        return inviter;
    }

    public UUID getInvited() {
        return invited;
    }

    public Arena getArena() {
        return arena;
    }
}
