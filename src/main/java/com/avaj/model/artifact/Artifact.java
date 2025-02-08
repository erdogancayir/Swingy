package com.avaj.model.artifact;

import lombok.Getter;

public abstract class Artifact {
    protected String name;
    protected int boost;

    public Artifact(String name, int boost) {
        this.name = name;
        this.boost = boost;
    }

    @Override
    public String toString() {
        return name + " (+" + boost + ")";
    }

    public int getBoost() {
        return boost;
    }
}
