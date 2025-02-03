package com.avaj.model.artifacts;

public class Helm extends AArtifact {

    public Helm(int lvl) {

        this.type = ArtifactType.HELM;

        namesAdr = new String[][] {
                {"Shampoo", "src/main/resources/img/artifacts/helm/shampoo.png"},
                {"Birds Nest", "src/main/resources/img/artifacts/helm/nest.png"},
                {"Clown's Wig", "src/main/resources/img/artifacts/helm/wig.png"},
                {"Santa Hat", "src/main/resources/img/artifacts/helm/santa-hat.png"},
                {"Headache Protection", "src/main/resources/img/artifacts/helm/condom.png"},
                {"Umbrella", "src/main/resources/img/artifacts/helm/umbrella.png"},
                {"Mortarboard", "src/main/resources/img/artifacts/helm/mortarboard.png"},
                {"Papal Mitre", "src/main/resources/img/artifacts/helm/mitre.png"},
                {"Anti-Aircraft Missile", "src/main/resources/img/artifacts/helm/missile.png"},
                {"NATO Membership", "src/main/resources/img/artifacts/helm/nato.png"},
        };

        this.name = namesAdr[lvl][0];
        this.power = lvl + 1;

        iconAddr = namesAdr[lvl][1];
    }
}
