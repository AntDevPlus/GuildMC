package fr.antdevplus.objects.relation;

public enum RelationEvent {
    CASUS_BELLI(-10),
    KILL(-50),
    HEAL(10),
    TRADE(25),
    HELP(50);

    private final int value;
    RelationEvent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
