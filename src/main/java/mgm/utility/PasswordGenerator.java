package mgm.utility;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static Element currentSet = Element.UPPERCASE;

    public String generatePassword(int length, boolean useUppercase, boolean useLowercase, boolean useDigits, boolean useSpecialCharacters) {
        if (length <= 0 || (!useUppercase && !useLowercase && !useDigits && !useSpecialCharacters)) {
            throw new IllegalArgumentException("Invalid password generation criteria.");
        }

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(getNextCharacter());
        }

        return password.toString();
    }

    public String getNextCharacter(){
        String value = getCharacter();

        switch (currentSet){
            case UPPERCASE:
                currentSet = Element.LOWERCASE;
                break;

            case LOWERCASE:
                currentSet = Element.DIGITS;
                break;

            case DIGITS:
                currentSet = Element.SPECIAL_CHARACTERS;
                break;

            case SPECIAL_CHARACTERS:
                currentSet = Element.UPPERCASE;
                break;
        }

        return value;
    }

    public String getCharacter(){
        int index = RANDOM.nextInt(currentSet.value.length());
        return currentSet.value.charAt(index) + "";
    }
}
