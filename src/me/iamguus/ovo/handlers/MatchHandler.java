package me.iamguus.ovo.handlers;

import me.iamguus.ovo.classes.MatchInvite;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Guus on 2-1-2016.
 */
public class MatchHandler {

    private static MatchHandler instance;

    private Set<MatchInvite> matchInvites;

    public Set<MatchInvite> getMatchInvites() { return this.matchInvites; }

    public MatchInvite getMatchInvite(UUID inviter, UUID invited) {
        for (MatchInvite invites : matchInvites) {
            if (invites.getInviter().equals(inviter) && invites.getInvited().equals(invited)) {
                return invites;
            }
        }
        return null;
    }

    public MatchInvite getMatchInvite(UUID invited) {
        for (MatchInvite invites : matchInvites) {
            if (invites.getInvited().equals(invited)) {
                return invites;
            }
        }

        return null;
    }

    public static MatchHandler get() {
        if (instance == null) {
            instance = new MatchHandler();
        }

        return instance;
    }
}
