package by.banking.currency.service;

import by.banking.currency.ui.model.request.CurrRequestModel;
import by.banking.currency.ui.model.response.CurrResponseModel;

public interface NbrbService {
    void extractCurrencyRateFromNbrbApi(CurrRequestModel requestModel);

    CurrResponseModel findCurrencyRate(String codeValue, String DateValue);
}
