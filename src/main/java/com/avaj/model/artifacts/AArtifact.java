package com.avaj.model.artifacts;

import lombok.Getter;

@Getter
public abstract class AArtifact {

    public enum ArtifactType {
        WEAPON,
        ARMOR,
        HELM
    }


    protected String[][] namesAdr;

    protected ArtifactType type;
    protected String name;
    protected int power;

    protected String iconAddr;

}
