package com.job.blender.service;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class AddressServiceImplTest extends TestCase {

    AddressService underTest = new AddressServiceImpl();

    public void testCreateDepositAddress_ThenRetrieveTheUserAddresses_BothMatched() {
        List<String> userAddresses = Arrays.asList("address1", "address2");
        String depositAddress = underTest.createDepositAddress(userAddresses);
        List<String> userAddressesRetrieved = underTest.getUserAddresses(depositAddress);
        assertEquals(userAddresses, userAddressesRetrieved);
    }

    public void testGetUserAddresses_NoMatchedAddress_ReturnEmptyList() {
        List<String> userAddressesRetrieved = underTest.getUserAddresses("some_address");
        assertEquals(0, userAddressesRetrieved.size());
    }

    public void testIsDepositAddressExist_ReturnTrue() {
        List<String> userAddresses = Arrays.asList("address1", "address2");
        String depositAddress = underTest.createDepositAddress(userAddresses);
        assertTrue(underTest.isDepositAddressExist(depositAddress));
    }
}