package com.avaj.validationTest;

import com.avaj.model.hero.Hero;
import com.avaj.model.hero.Warrior;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidationTest {

    private Validator validator;
    private Warrior hero;

    @BeforeAll
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        hero = new Warrior("TestWarrior");  // Geçerli bir Warrior nesnesi oluşturduk
    }

    @Test
    @Order(1)
    void testHeroLevel() {
        assertTrue(hero.getLevel() > 0, "Hero level should be greater than 0.");
    }

    @Test
    @Order(2)
    void testValidHero() {
        Set<ConstraintViolation<Hero>> violations = validator.validate(hero);
        assertTrue(violations.isEmpty(), "Valid hero should not have any validation errors.");
    }

    @Test
    @Order(3)
    void testInvalidHeroName() {
        Hero invalidHero = new Warrior(""); // Boş isim ile oluşturulan hero
        Set<ConstraintViolation<Hero>> violations = validator.validate(invalidHero);
        assertFalse(violations.isEmpty(), "Hero with an empty name should fail validation.");
    }

    @Test
    @Order(4)
    void testNegativeLevel() {
        Hero invalidHero = new Warrior("NegativeHero");
        invalidHero.SetLevel(-5); // Geçersiz seviye
        Set<ConstraintViolation<Hero>> violations = validator.validate(invalidHero);
        assertFalse(violations.isEmpty(), "Hero with negative level should fail validation.");
    }

    @Test
    @Order(5)
    void testZeroHitPoints() {
        Hero invalidHero = new Warrior("LowHealth");
        invalidHero.SetHitPoints(0); // Geçersiz sağlık
        Set<ConstraintViolation<Hero>> violations = validator.validate(invalidHero);
        assertFalse(violations.isEmpty(), "Hero with zero hit points should fail validation.");
    }
}
