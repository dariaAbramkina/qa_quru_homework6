import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class ParametrizedTests {
    @BeforeEach
    void precondition() {
        Configuration.browserSize = "1920x1080";
        open("https://www.aviasales.ru/");
    }

    @AfterEach
    void closeBrowser() {
        closeWebDriver();
    }

    @ValueSource(strings = {"Моск", "Санкт"})
    @ParameterizedTest(name = "Check search for departure city for value {0}")
    void checkSearchInDepartureCityField(String searchInput) {
        $("[data-test-id='origin-autocomplete-field']").clear();
        $("[data-test-id='origin-autocomplete-field']").setValue(searchInput);
        $("[class='autocomplete__suggestion-info']").shouldHave(Condition.text(searchInput));
    }

    @CsvSource(value = {
            "Моск| Москва, Россия",
            "Санкт| Санкт-Петербург, Россия"
    }, delimiter = '|')
    @ParameterizedTest(name = "Check search result for departure city for value {0} is equal to {1}")
    void checkSearchResultForDepartureCityFieldFirst(String searchInput, String searchOutput) {
        $("[data-test-id='origin-autocomplete-field']").clear();
        $("[data-test-id='origin-autocomplete-field']").setValue(searchInput);
        $("[class='autocomplete__suggestion-info']").shouldHave(Condition.exactText(searchOutput));
    }

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("Новосиб", "Новосибирск, Россия"),
                Arguments.of("Омс", "Омск, Россия")
        );
    }

    @MethodSource(value = "arguments")
    @ParameterizedTest(name = "Check search result for departure city for value {0} is equal to {1}")
    void checkSearchResultForDepartureCityFieldSecond(String searchInput, String searchOutput) {
        $("[data-test-id='origin-autocomplete-field']").clear();
        $("[data-test-id='origin-autocomplete-field']").setValue(searchInput);
        $("[class='autocomplete__suggestion-info']").shouldHave(Condition.exactText(searchOutput));
    }
}
