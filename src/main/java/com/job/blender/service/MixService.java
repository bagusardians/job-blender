package com.job.blender.service;

import java.util.List;

public interface MixService {

    void mix(List<Transaction> depositTransactions, String houseAddress);

}
