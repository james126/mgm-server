package mgm.utility;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?/";

    private static final SecureRandom RANDOM = new SecureRandom();

    public String generatePassword(int length, boolean useUppercase, boolean useLowercase,
            boolean useDigits, boolean useSpecialCharacters) {
        if (length <= 0 || (!useUppercase && !useLowercase && !useDigits && !useSpecialCharacters)) {
            throw new IllegalArgumentException("Invalid password generation criteria.");
        }

        StringBuilder characterPool = new StringBuilder();
        if (useUppercase) {
            characterPool.append(UPPERCASE);
        }
        if (useLowercase) {
            characterPool.append(LOWERCASE);
        }
        if (useDigits) {
            characterPool.append(DIGITS);
        }
        if (useSpecialCharacters) {
            characterPool.append(SPECIAL_CHARACTERS);
        }

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }

        return password.toString();
    }
}
