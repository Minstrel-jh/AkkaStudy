package dispatcher;

import java.io.Serializable;

public class StartCommand implements Serializable {

    private int actorCount = 0;

    public StartCommand() {}

    public StartCommand(int actorCount) {
        this.actorCount = actorCount;
    }

    public int getActorCount() {
        return actorCount;
    }

    public void setActorCount(int actorCount) {
        this.actorCount = actorCount;
    }
}
