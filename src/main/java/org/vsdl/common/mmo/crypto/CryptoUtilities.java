package org.vsdl.common.mmo.crypto;

import java.math.BigInteger;
import java.util.Random;

public class CryptoUtilities {

    private static final int DIGIT_0 = '0';
    private static final int DIGIT_9 = '9';
    private static final int ALPHA_A = 'A';
    private static final int ALPHA_Z = 'Z';
    private static final int ALPHA_a = 'a';
    private static final int ALPHA_z = 'z';

    public static boolean isAlphaNumeric(char c) {
        return ((c >= DIGIT_0 && c <= DIGIT_9) || (c >= ALPHA_A && c <= ALPHA_Z) || (c >= ALPHA_a && c <= ALPHA_z));
    }

    public static void checkAlphaNumeric(String s) {
        for (Character c : s.toCharArray()) {
            if (!isAlphaNumeric(c)) {
                throw new IllegalArgumentException(
                        "Input contained non-alphanumeric characters."
                );
            }
        }
    }

    public static BigInteger fromAlphaNumeric(String alphaNumericString) {
        checkAlphaNumeric(alphaNumericString);
        if (alphaNumericString.charAt(0) == '0') {
            throw new IllegalArgumentException("Input string must not contain leading 0s.");
        }
        return new BigInteger(alphaNumericString.toLowerCase(), Character.MAX_RADIX);
    }

    public static String toAlphaNumeric(BigInteger bigInteger) {
        return bigInteger.toString(Character.MAX_RADIX);
    }

    public static String randomAlphaNumericString(int length) {
        StringBuilder sb = new StringBuilder();
        Random rng = new Random();
        for (int i = 0; i < length;) {
            char c = (char) rng.nextInt(Byte.MAX_VALUE);
            //prevent non alpha symbols and leading zeros
            if (isAlphaNumeric(c) && !(i == 0 && c == '0')) {
                sb.append(c);
                ++i;
            }
        }
        return sb.toString().toLowerCase();
    }

    public static BigInteger randomKey(int length) {
        return new BigInteger(randomAlphaNumericString(length), Character.MAX_RADIX);
    }

    public static String hash(String saltedPassword) {
        BigInteger i = fromAlphaNumeric(saltedPassword);
        return toAlphaNumeric(
                new BigInteger(
                        Integer.toString(
                                i.hashCode(),
                                Character.MAX_RADIX
                        ),
                        Character.MAX_RADIX
                )
        );
    }

    public static String salt(String password, String salt) {
        return salt + password;
    }
}