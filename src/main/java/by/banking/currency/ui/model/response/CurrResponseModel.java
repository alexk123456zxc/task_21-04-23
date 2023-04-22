package by.banking.currency.ui.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrResponseModel {
    String currencyDate;
    String currencyCode;
    String rate;
    String quantity;
    String dynamics;
}
