package by.banking.currency.ui.controller;

import by.banking.currency.service.NbrbService;
import by.banking.currency.ui.model.request.CurrRequestModel;
import by.banking.currency.ui.model.response.CurrResponseModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    NbrbService service;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity addCurrencyRate(@Valid @RequestBody CurrRequestModel requestModel) {
        service.extractCurrencyRateFromNbrbApi(requestModel);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{codeValue}/{dateValue}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CurrResponseModel getCurrencyRate(@PathVariable String codeValue, @PathVariable String dateValue) {
        return service.findCurrencyRate(codeValue, dateValue);
    }
}
