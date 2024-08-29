package br.com.selectgearmotors.transaction.util;

import com.github.javafaker.Faker;

public class CnpjGenerator {
    private static final Faker faker = new Faker();

    public static String generateCnpj() {
        return faker.regexify("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }
}
