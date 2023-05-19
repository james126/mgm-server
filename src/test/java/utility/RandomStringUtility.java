package utility;

import java.util.Random;

public class RandomStringUtility {

    public static String generateRandomString(int length){
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        while (sb.length() < 10){
            int result = rand.nextInt(26) + 97;
            if (sb.length() == 0){
                sb.append(Character.toUpperCase((char) result));
            } else {
                sb.append((char) result);
            }
        }

        sb.replace(0, 1, String.valueOf(Character.toUpperCase(sb.charAt(0))));
        return sb.toString();
    }
}
