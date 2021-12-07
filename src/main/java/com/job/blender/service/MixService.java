package com.job.blender.service;

import java.util.List;

public interface MixService {

    boolean mix(List<Transaction> depositTransactions, String houseAddress);

}
