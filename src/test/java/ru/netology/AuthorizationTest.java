package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;

public class AuthorizationTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfullyLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("h2.heading_theme_alfa-on-white").shouldBe(Condition.visible, Duration.ofMillis(5000)).shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldDisplayErrorIfUserNotRegistered() {
        var notRegisteredUser = getUser("active");

        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible, Duration.ofMillis(5000)).shouldHave(Condition.text("Ошибка " + "Ошибка! Неверно указан логин или пароль"));
    }


    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldDisplayErrorIfUserBlocked() {
        var blockedUser = getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible, Duration.ofMillis(5000)).shouldHave(Condition.text("Ошибка! " + "Пользователь заблокирован"));
    }


    @Test
    @DisplayName("Should get error message if wrong login")
    void shouldDisplayErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").should(Condition.visible, Duration.ofMillis(5000)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }


    @Test
    @DisplayName("Should get error message if wrong password")
    void shouldDisplayErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        String wrongPassword = getRandomPassword();

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible, Duration.ofMillis(5000)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}
