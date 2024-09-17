package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "User already exists"
    request {
        method POST()
        url("/v2/user/register")
        headers {
            contentType applicationJson()
        }
        body(
                "name": "existing",
                "password": "foo",
                "email": "foo@foo.foo"
        )
    }
    response {
        body(
                "message" : "User exists"
        )
        status 400
    }
}