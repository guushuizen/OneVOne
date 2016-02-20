package me.iamguus.ovo.classes;

import java.util.UUID;

/**
 * Created by Guus on 2-1-2016.
 */
public class MatchInvite {

    private UUID inviter;
    private UUID invited;

    public MatchInvite(UUID inviter, UUID invited) {
        this.inviter = inviter;
        this.invited = invited;
    }

    public UUID getInviter() {
        return inviter;
    }

    public UUID getInvited() {
        return invited;
    }

}
