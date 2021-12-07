package com.job.blender.service;

import java.util.List;

public interface AddressService {

    String createDepositAddress(List<String> userAddresses);

    List<String> getUserAddresses(String depositAddress);

    boolean isDepositAddressExist(String depositAddress);
}
