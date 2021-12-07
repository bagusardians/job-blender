package com.job.blender.service;

import java.util.*;
import java.util.stream.Collectors;

public class AddressServiceImpl implements AddressService {
    Map<String, List<String>> db = new HashMap<>();

    @Override
    public String createDepositAddress(List<String> userAddresses) {
        String newAddress = UUID.randomUUID().toString();
        db.put(newAddress, userAddresses.stream().distinct().collect(Collectors.toList()));
        return newAddress;
    }

    @Override
    public List<String> getUserAddresses(String depositAddress) {
        if (db.containsKey(depositAddress)) {
            return db.get(depositAddress);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isDepositAddressExist(String depositAddress) {
        return db.containsKey(depositAddress);
    }
}
