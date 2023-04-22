package by.banking.currency;

import by.banking.currency.dbentity.CurrEntity;
import by.banking.currency.repository.CurrencyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/create-data-for-test.sql")
@Sql(scripts = "/cleanup-data-after-test.sql", executionPhase = AFTER_TEST_METHOD)
public class UserRepositoryH2Test {
    @Autowired
    private CurrencyRepository repo;

    @Test
    void findCurrencyRateByDate() {
        Optional<CurrEntity> record = repo.findByCurAbbreviationAndDate("USD", "2099-01-01").stream().findFirst();
        Assertions.assertNotNull(record.orElse(null));
    }

}