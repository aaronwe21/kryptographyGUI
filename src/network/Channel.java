package network;

import com.google.common.eventbus.EventBus;

public class Channel extends EventBus {

    private String name;

    public Channel(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
