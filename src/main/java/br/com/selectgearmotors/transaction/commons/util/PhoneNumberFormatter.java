package br.com.selectgearmotors.transaction.commons.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumberFormatter {

    public static String formatPhoneNumber(String phoneNumber) {
        // Remove todos os caracteres não numéricos
        String cleanNumber = phoneNumber.replaceAll("[^\\d]", "");
        // Adiciona o prefixo +55
        return "+55" + cleanNumber;
    }

    /*public static void main(String[] args) {
        String phoneNumber = "(34) 97452-6758";
        String formattedNumber = formatPhoneNumber(phoneNumber);
        System.out.println("Número formatado: " + formattedNumber);
    }*/
}
