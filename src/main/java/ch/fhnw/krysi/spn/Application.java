package main.java.ch.fhnw.krysi.spn;

import java.math.BigInteger;

import java.util.Arrays;
import java.util.HashMap;

public class Application {

    public static void main(String[] args) {

        String key = "00111010100101001101011000111111";
        String chiffre = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";
        String[] keyParts = new String[4];

        int r = 4;
        int n = 4;
        int m = 4;
        int s = 32;

        HashMap<Integer, Integer> sBox = new HashMap<>();
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

        HashMap<Integer, Integer> bitPermute = new HashMap<>();
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

        // String of 0's and 1's to byteArray
        byte[] chiffreBin = new BigInteger(chiffre, 2).toByteArray();

        // BitString from Hex
        String b = new BigInteger(String.valueOf(bitPermute.get(0x0A))).toString(2);
        System.out.println(b);

        keyParts[0] = key.substring(0,16);
        keyParts[1] = key.substring(4,20);
        keyParts[2] = key.substring(8,24);
        keyParts[3] = key.substring(12,32);

        System.out.println(Arrays.toString(keyParts));



    }

}
