package contracts.accountservice

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    request {
        url("/api/transactions/9999")
        method("GET")
    }

    response {
        status 200
        body(
                transactionId: "9999",
                accountId: "ACC1234567",
                transactionType: "DEPOSIT",
                amount: 99,
                balance: 100,
                description: "Token Amount",
                createAt: "2022-01-01T01:01:01"
        )
        headers {
            contentType(applicationJson())
        }
    }

}
