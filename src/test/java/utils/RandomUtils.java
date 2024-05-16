package utils;

import com.github.javafaker.Faker;

import java.util.Locale;

public class RandomUtils {

    Faker faker = new Faker(new Locale("ru"));

    public String getTitle() {
        return faker.harryPotter().spell();
        //return faker.lorem().characters(5,10);
    }

    public String getName() {
        return faker.name().firstName();
    }

    public String getEmail() {
        return faker.internet().emailAddress();
    }

    public String getPhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }
}
