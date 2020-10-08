package network;

import com.google.common.eventbus.Subscribe;

import java.io.File;
import java.util.List;

public abstract class Participant {
    protected int id;
    protected String name;

    public Participant (int id, String name)
    {
        this.id = id;
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
