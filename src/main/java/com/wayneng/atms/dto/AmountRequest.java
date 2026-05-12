package com.wayneng.atms.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AmountRequest {

    private String sessionId;
    private BigDecimal amount;
}