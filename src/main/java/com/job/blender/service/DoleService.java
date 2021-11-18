package com.job.blender.service;

import java.math.BigDecimal;
import java.util.List;

public interface DoleService {

    void dole(String houseAddress, BigDecimal amount, List<String> destinationAddresses);
}
