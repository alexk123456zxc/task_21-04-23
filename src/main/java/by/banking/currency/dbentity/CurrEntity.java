package by.banking.currency.dbentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Table(name = "currency_rates", schema = "appstorage")
@Data
@SequenceGenerator(name = "ID",  sequenceName = "curr_sq",
        allocationSize = 1, schema = "appstorage")
public class CurrEntity implements Serializable {
    private static long serialVersionUID = -1506158750269370240L;
    @Id
    @GeneratedValue(generator = "ID", strategy = GenerationType.SEQUENCE)
    @Column
    private long id;
    @JsonProperty("Cur_ID")
    @Column(name="cur_id")
    private String curId;
    @JsonProperty("Date")
    @Column(name="date")
    private String date;
    @JsonProperty("Cur_Abbreviation")
    @Column(name = "cur_abbreviation")
    private String curAbbreviation;
    @JsonProperty("Cur_Scale")
    @Column(name="cur_scale")
    private String curScale;
    @JsonProperty("Cur_Name")
    @Column(name="cur_name")
    String curName;
    @JsonProperty("Cur_OfficialRate")
    @Column(name="cur_official_rate")
    String curOfficialRate;

}

