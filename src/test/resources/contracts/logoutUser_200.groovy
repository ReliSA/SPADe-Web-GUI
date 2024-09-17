package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Logout of logged in user."
    request {
        method POST()
        url("/v2/user/logout")
        headers {
            header("Authorization": "Bearer token")
            contentType applicationJson()
        }
        body(
                "name": "existing"
        )
    }
    response {
        body(
                "message": "User logged out"
        )
        status 200
    }
}