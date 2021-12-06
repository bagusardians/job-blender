package com.job.blender.service;

import java.math.BigDecimal;

public interface FeeService {

    BigDecimal collectFee(BigDecimal originalAmount);
}
