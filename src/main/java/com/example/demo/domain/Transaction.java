package com.example.demo.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    //@Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private UUID accountId;
    private String currency;
    private BigDecimal amount;
    private String description;
    private Type type;
}
