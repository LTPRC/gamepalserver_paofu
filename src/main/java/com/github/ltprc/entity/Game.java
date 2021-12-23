package com.github.ltprc.entity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public abstract class Game {

    /**
     * Status
     * -1 error
     * 0  ready
     * 1  running
     * 2  paused
     * 3  finished
     */
    private static final int STATUS_ERROR = -1;
    private static final int STATUS_READY = 0;
    private static final int STATUS_RUNNING = 1;
    private static final int STATUS_PAUSED = 2;
    private static final int STATUS_FINISHED = 3;

    private String name;
    private Subject subject;
    private int status = STATUS_READY;
    @NonNull
    private Set<String> playerNameSet = new LinkedHashSet<>();
    private Set<String> notReadyplayerNameSet = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<String> getPlayerNameSet() {
        return playerNameSet;
    }

    public void setPlayerNameSet(Set<String> playerNameSet) {
        this.playerNameSet = playerNameSet;
    }

    public boolean addPlayer(Player player) {
        if (playerNameSet.size() >= subject.getMaxPlayerNum()
                || playerNameSet.contains(player.getName()) || status != STATUS_READY) {
            return false;
        }
        playerNameSet.add(player.getName());
        notReadyplayerNameSet.add(player.getName());
        return true;
    }

    public boolean removePlayer(String playerName) {
        if (!playerNameSet.contains(playerName) || status != STATUS_READY) {
            return false;
        }
        playerNameSet.remove(playerName);
        notReadyplayerNameSet.remove(playerName);
        return true;
    }

    public boolean readyPlayer(String playerName) {
        if (!playerNameSet.contains(playerName)) {
            return false;
        }
        notReadyplayerNameSet.remove(playerName);
        return true;
    }

    public boolean unreadyPlayer(Player player) {
        if (!playerNameSet.contains(player.getName())) {
            return false;
        }
        notReadyplayerNameSet.add(player.getName());
        return true;
    }

    public boolean startGame() {
        if (playerNameSet.size() < subject.getMinPlayerNum() || !notReadyplayerNameSet.isEmpty()) {
            return false;
        }
        return true;
    }
}
