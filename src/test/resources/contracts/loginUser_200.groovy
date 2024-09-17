package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Login of registered user."
    request {
        method POST()
        url("/v2/user/login")
        headers {
            contentType applicationJson()
        }
        body(
                "name": "existing",
                "password": "foo"
        )
    }
    response {
        body(
                "message": "User logged in successfully",
                "jwtToken": "token"
        )
        status 200
    }
}