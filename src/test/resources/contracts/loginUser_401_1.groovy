package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Login of unregistered user."
    request {
        method POST()
        url("/v2/user/login")
        headers {
            contentType applicationJson()
        }
        body(
                "name": "non_existing",
                "password": "foo"
        )
    }
    response {
        body(
                "message" : "User login failed"
        )
        status 401
    }
}