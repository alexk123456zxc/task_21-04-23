package by.banking.currency.repository;

import by.banking.currency.dbentity.CurrEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrEntity, Long> {
    Optional<CurrEntity> findByCurAbbreviationAndDate(String curAbbreviation, String Date);

    @Query(value = "select id, cur_abbreviation, cur_id, cur_name, cur_official_rate, cur_scale, PARSEDATETIME(FORMATDATETIME(date, 'yyyy-MM-dd'), 'yyyy-MM-dd') date from appstorage.currency_rates\n" +
            "where PARSEDATETIME(FORMATDATETIME(date, 'yyyy-MM-dd'), 'yyyy-MM-dd') < PARSEDATETIME(FORMATDATETIME(:dateValue, 'yyyy-MM-dd'), 'yyyy-MM-dd')\n" +
            "and cur_abbreviation = :currency limit 1", nativeQuery = true)
    Optional<CurrEntity> findNearestPreviousRate(String dateValue, String currency);
}
