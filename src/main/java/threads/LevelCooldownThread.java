package threads;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class LevelCooldownThread extends Thread {
    private static HashMap<String, Integer> coolingDown;
    private static ArrayList<String> removalCache;

    public LevelCooldownThread() {
        coolingDown = new HashMap<>();
        removalCache = new ArrayList<>();
    }

    @Override
    public void run() {
        // I swear to god I'm not using Java for anything else other than this project.

        // noinspection InfiniteLoopStatement
        while (true) {
            try {
                for (String userId : coolingDown.keySet()) {
                    int cooldown = coolingDown.get(userId);
                    cooldown -= 1;
                    if (cooldown <= 0) {
                        removalCache.add(userId);
                        continue;
                    }
                    coolingDown.replace(userId, cooldown);
                }

                removalCache.forEach(coolingDown.keySet()::remove);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public static HashMap<String, Integer> getCoolingDown() { return coolingDown; }

}
