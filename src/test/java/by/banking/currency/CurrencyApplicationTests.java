package by.banking.currency;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CurrencyApplicationTests {

    @Autowired
    private OkHttpClient client;

    @Test
    void checkNbrbApiIsAvailable() throws IOException {
        Request request = new Request.Builder()
                .url("https://www.nbrb.by/api/exrates/currencies")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        assertEquals(response.code(), 200);
    }

}
