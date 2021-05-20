package airport;

import java.util.Random;

/**
 * General utility functions.
 *
 * @author Xavier Santos
 */
public class utils {

    /**
     * Generates a random integer value between a minimum and maximum value.
     *
     * @param min minimum value
     * @param max maximum value
     * @return random int number
     */
    public static int randInt(int min, int max) {
        Random rn = new Random();
        int range = max - min + 1;
        return rn.nextInt(range) + min;
    }
}
