package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Creates new user in the application"
    request {
        method POST()
        url("/v2/user/register")
        headers {
            contentType applicationJson()
        }
        body(
                "name": "new_user",
                "password": "foo",
                "email": "foo@foo.foo"
        )
    }
    response {
        body(
                "message" : "User successfully created"
        )
        status 201
    }
}