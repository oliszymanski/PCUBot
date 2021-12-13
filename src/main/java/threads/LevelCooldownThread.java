package threads;

import java.util.HashMap;

public class LevelCooldownThread extends Thread {
    private static HashMap<String, Integer> coolingDown;

    public LevelCooldownThread() {
        coolingDown = new HashMap<>();
    }

    @Override
    public void run() {
        // I swear to god I'm not using Java for anything else other than this project.
        while (true) {
            try {
                for (String userId : coolingDown.keySet()) {
                    int cooldown = coolingDown.get(userId);
                    cooldown -= 1;
                    if (cooldown <= 0) {
                        coolingDown.remove(userId);
                        continue;
                    }
                    coolingDown.replace(userId, cooldown);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static HashMap<String, Integer> getCoolingDown() { return coolingDown; }

}
