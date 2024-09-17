package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Login of registered user with wrong password."
    request {
        method POST()
        url("/v2/user/login")
        headers {
            contentType applicationJson()
        }
        body(
                "name": "foo",
                "password": "wrong"
        )
    }
    response {
        body(
                "message" : "User login failed"
        )
        status 401
    }
}