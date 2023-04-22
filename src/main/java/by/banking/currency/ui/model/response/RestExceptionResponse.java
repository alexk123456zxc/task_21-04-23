package by.banking.currency.ui.model.response;

import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
public class RestExceptionResponse {
    public RestExceptionResponse(String msg) {
        this.message = msg;
        this.timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    }

    String message;
    String timeStamp;
}
