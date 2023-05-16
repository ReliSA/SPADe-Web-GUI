package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Fetch all configurations of user."
    request {
        method POST()
        url("/v2/configuration/configuration_name")
        headers {
            header("Authorization": "Bearer token")
            contentType applicationJson()
        }
        body(
                "name": "config_user"
        )
    }
    response {
        body(
                "message": "ok",
                "configuration_ids": ["1"],
                "configuration_names": ["default config"]
        )
        status 200
    }
}