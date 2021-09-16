package com.lemon.maiko.client;

import com.lemon.maiko.api.foaas.res.Operation;

import java.util.List;

public interface FoaasClient {

    List<Operation> getOperations();
}
