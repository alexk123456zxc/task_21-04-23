package by.banking.currency.ui.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrRequestModel {
    @NotNull
    @Size(min = 8)
    String currencyDate;
}
