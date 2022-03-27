package main.java.ch.fhnw.krysi.spn;

import java.util.*;

public class Decryptor {

    String       key      = "00111010100101001101011000111111"; // Key from PDF
    String       chiffre  = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000"; // Chiffre from PDF
    List<String> keyParts = new ArrayList<>(); // Holder for the KEys

    int    r = 4;
    int    n = 4;
    int    m = 4;
    int    s = 32;
    String ym1;

    HashMap<Integer, Integer> sBox; // SBox (from PDF)
    HashMap<Integer, Integer> bitPermute; // Bit Permutation (from PDF)

    public Decryptor() {

        // Create a HashMap for SBox (given in the PDF)
        this.sBox = new HashMap<>();
        sBox.put(0x00, 0x0E);
        sBox.put(0x01, 0x04);
        sBox.put(0x02, 0x0D);
        sBox.put(0x03, 0x01);
        sBox.put(0x04, 0x02);
        sBox.put(0x05, 0x0F);
        sBox.put(0x06, 0x0B);
        sBox.put(0x07, 0x08);
        sBox.put(0x08, 0x03);
        sBox.put(0x09, 0x0A);
        sBox.put(0x0A, 0x06);
        sBox.put(0x0B, 0x0C);
        sBox.put(0x0C, 0x05);
        sBox.put(0x0D, 0x09);
        sBox.put(0x0E, 0x00);
        sBox.put(0x0F, 0x07);

        // Create a HashMap for the Bit Permutation (given in the PDF)
        this.bitPermute = new HashMap<>();
        bitPermute.put(0x00, 0x00);
        bitPermute.put(0x01, 0x04);
        bitPermute.put(0x02, 0x08);
        bitPermute.put(0x03, 0x0C);
        bitPermute.put(0x04, 0x01);
        bitPermute.put(0x05, 0x05);
        bitPermute.put(0x06, 0x09);
        bitPermute.put(0x07, 0x0D);
        bitPermute.put(0x08, 0x02);
        bitPermute.put(0x09, 0x06);
        bitPermute.put(0x0A, 0x0A);
        bitPermute.put(0x0B, 0x0E);
        bitPermute.put(0x0C, 0x03);
        bitPermute.put(0x0D, 0x07);
        bitPermute.put(0x0E, 0x0B);
        bitPermute.put(0x0F, 0x0F);

        System.out.println("+-------------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                   Starting DECRYPTION with                                                  |");
        System.out.println("|                                                                                                             |");
        System.out.println("|                                       r=" + r + " n=" + n + " m=" + m + " s=" + s + "                                                      |");
        System.out.println("| S-Box=" + sBox + "          |");
        System.out.println("| Bitpermutation=" + bitPermute + " |");
        System.out.println("| Key=" + key + "                                                                        |");
        System.out.println("+-------------------------------------------------------------------------------------------------------------+");


        // Create Key Parts
        doCalcKeys(key);

        // Get y-1 from chiffre Text
        this.ym1 = chiffre.substring(0, m * n);

        // remove y-1 from chiffre Text
        String chiffre_wo_ym1 = chiffre.substring((n * m));

        // Array for all Y's and the Decrypted Results. Length based on n * m
        int length = chiffre_wo_ym1.length() / (m * n);

        String[] y_array           = new String[length];
        String[] decrypted_results = new String[length];


        // Cut the Chiffre and put it in the y's array
        for (int i = 0; i < chiffre_wo_ym1.length(); i += (m * n)) {
            y_array[i / 16] = chiffre_wo_ym1.substring(i, i + (m * n));
        }

        // Run the SPN and increment y-1 + 1
        for (int i = 0; i < decrypted_results.length; i++) {
            decrypted_results[i] = runSPN(this.ym1);

            StringBuilder nym1 = new StringBuilder(Integer.toBinaryString(Integer.valueOf(this.ym1, 2) + 1));
            while (nym1.length() != this.ym1.length()) {
                nym1.insert(0, '0');
            }
            this.ym1 = nym1.toString();
        }


        // Collect all results and apply an xor with the element from the y's
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < decrypted_results.length; i++) {
            result.append(xor(decrypted_results[i], y_array[i]));

        }

        // Print the result to the Console
        System.out.println("| Decrypted Text: " + toText(result.toString()) + "                                                                                |");
        System.out.println("+-------------------------------------------------------------------------------------------------------------+");
    }

    // Calculate the Keys and stuff them into the keyParts List
    private void doCalcKeys(String key) {
        for (int e = 0; e <= key.length() - m * n; e += n) {
            keyParts.add(key.substring(e, (m * n) + e));
        }
    }

    // Run the SPN Step
    private String runSPN(String toSPN) {

        String afterSPN;

        // First do the WhiteStep
        afterSPN = xor(toSPN, keyParts.get(0));

        // do the normal steps
        for (int i = 1; i < keyParts.size() - 1; i++) {
            afterSPN = doSBoxStep(afterSPN);
            afterSPN = doBitpermutation(afterSPN);
            afterSPN = xor(afterSPN, keyParts.get(i));
        }

        // last step is minimized step
        afterSPN = doSBoxStep(afterSPN);
        afterSPN = xor(afterSPN, keyParts.get(keyParts.size() - 1));

        return afterSPN;
    }


    // Do a Bit Permutation based on bitPermute Table
    private String doBitpermutation(String toPermute) {
        StringBuilder result = new StringBuilder("0000000000000000");
        int           k      = 0;
        while (k < 16) {
            result.setCharAt(this.bitPermute.get(k), toPermute.charAt(k));
            k++;
        }
        return result.toString();
    }


    // XOR two Strings
    private String xor(String chiffre, String key) {
        StringBuilder result = new StringBuilder();
        int           i      = 0;
        while (i < chiffre.length()) {
            if (chiffre.charAt(i) != key.charAt(i)) {
                result.append('1');
            } else {
                result.append('0');
            }
            i++;
        }
        return result.toString();
    }


    // Run the SBox
    private String doSBoxStep(String toSBox) {
        StringBuilder result = new StringBuilder();
        int           i      = 0;

        while (i < 16) {

            // Get the position within the SBox
            int pos = Integer.parseInt(toSBox.substring(i, i + 4), 2);

            // Create a StringBuilder to get the SBox Value
            StringBuilder padded = new StringBuilder(Integer.toBinaryString(sBox.get(pos)));

            // add 0's to have a 4 bit string
            while (padded.length() != 4) {
                padded.insert(0, '0');
            }
            result.append(padded);
            i += 4;
        }
        return result.toString();
    }

    // Transform the decrypted BitString to Text
    private String toText(String decrypted) {

        if (decrypted.endsWith("0")) {
            decrypted = decrypted.substring(0, decrypted.length() - 16);
        }

        StringBuilder result = new StringBuilder();

        // Create Chars in pieces of 8 Bits, that's the ASCII length
        for (int i = 0; i < decrypted.length(); i += 8) {
            String piece     = decrypted.substring(i, i + 8);
            int    pieceChar = Integer.parseInt(piece, 2);
            result.append((char) pieceChar);
        }

        return result.toString();
    }
}
