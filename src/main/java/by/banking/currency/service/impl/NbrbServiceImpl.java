package by.banking.currency.service.impl;

import by.banking.currency.dbentity.CurrEntity;
import by.banking.currency.exception.NbrbApiException;
import by.banking.currency.exception.RecordNotFoundException;
import by.banking.currency.repository.CurrencyRepository;
import by.banking.currency.service.NbrbService;
import by.banking.currency.ui.model.request.CurrRequestModel;
import by.banking.currency.ui.model.response.CurrResponseModel;
import by.banking.currency.ui.model.response.Dinamics;
import by.banking.currency.util.DateStringHelper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import okhttp3.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class NbrbServiceImpl implements NbrbService {
    @Autowired
    private OkHttpClient client;
    @Autowired
    private CurrencyRepository repo;
    @Autowired
    private ObjectMapper mapper;

    @Override
    @Transactional
    public void extractCurrencyRateFromNbrbApi(CurrRequestModel requestModel) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.nbrb.by/api/exrates/rates").newBuilder();
        String requestDate = DateStringHelper.removeBeginingZerosFromDateString(requestModel.getCurrencyDate());
        urlBuilder.addQueryParameter("ondate", requestDate);
        urlBuilder.addQueryParameter("periodicity", "0");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
                    throws NbrbApiException {
                try {
                    List<CurrEntity> entities = mapper.readValue(response.body().string(), new TypeReference<>() {
                    });
                    for (CurrEntity entityToSave : entities) {
                        entityToSave.setDate(DateStringHelper.removeTimeFromDateString(entityToSave.getDate()));
                        if (repo.findByCurAbbreviationAndDate(entityToSave.getCurAbbreviation(), entityToSave.getDate()).isEmpty()) {
                            repo.save(entityToSave);
                        }
                    }
                } catch (JsonParseException ex) {
                    throw new NbrbApiException("Проверьте правильность запрашиваемых данных");
                } catch (IOException ex) {
                    throw new NbrbApiException("Данные с api нацбанка не могут быть получены");
                }
            }

            public void onFailure(Call call, IOException e) {
                throw new RuntimeException();
            }
        });
    }

    @Override
    public CurrResponseModel findCurrencyRate(String codeValue, String dateValue) {
        Optional<CurrEntity> currEntity = repo.findByCurAbbreviationAndDate(codeValue, dateValue);
        if (currEntity.isPresent()) {

            Converter<CurrEntity, CurrResponseModel> genericConverter = context -> {
                CurrEntity s = context.getSource();
                CurrResponseModel d = context.getDestination();
                d.setCurrencyDate(s.getDate());
                d.setCurrencyCode(s.getCurAbbreviation());
                d.setRate(s.getCurOfficialRate());
                d.setQuantity(s.getCurScale());
                return d;
            };

            CurrResponseModel responseModel = new CurrResponseModel();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.createTypeMap(CurrEntity.class, CurrResponseModel.class).setConverter(genericConverter);
            modelMapper.map(currEntity.get(), responseModel);

            Optional<CurrEntity> currPrev = repo.findNearestPreviousRate(dateValue, codeValue);
            if (currPrev.isPresent()){
                responseModel.setDynamics(DateStringHelper.findRateDinamics(currPrev.get().getCurOfficialRate(), responseModel.getRate()));
            }else{
                responseModel.setDynamics(Dinamics.EQUAL.name());
            }

            return responseModel;
        } else {
            throw new RecordNotFoundException("Данной записи в базе данных не обнаружено");
        }
    }
}
