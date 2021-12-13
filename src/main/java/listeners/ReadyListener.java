package listeners;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import threads.LevelCooldownThread;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent e) {
        LevelCooldownThread levelCooldownThread = new LevelCooldownThread();
        levelCooldownThread.start();
    }
}
