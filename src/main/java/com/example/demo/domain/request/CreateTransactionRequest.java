package com.example.demo.domain.request;

import com.example.demo.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequest {

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
             message = "must be UUID format")
    private String accountId;

    @NotNull
    private String currency;

    @Positive
    private BigDecimal amount;

    private String description;

    @NotNull
    private Type type;
}
