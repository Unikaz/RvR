package RvR;

import java.util.EventListener;

public interface RvREvents extends EventListener {
    void onTick();
    void onEnd();
}
