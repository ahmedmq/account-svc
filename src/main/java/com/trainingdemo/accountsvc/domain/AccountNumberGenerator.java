package com.trainingdemo.accountsvc.domain;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class AccountNumberGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        Random random = new Random();
        int randomNum = random.nextInt(9999999);
        return String.format("ACC%07d",randomNum);
    }
}
