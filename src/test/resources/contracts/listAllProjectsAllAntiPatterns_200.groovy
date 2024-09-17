package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Fetch all configurations of user."
    request {
        method GET()
        url("/v2/detect/list")
        headers {
            header("Authorization": "Bearer token")
            contentType applicationJson()
        }
    }
    response {
        body(
                "antiPatterns": [
                    ["catalogueFileName" : "Business_As_Usual.md",
                     "description" : "Absence of a retrospective after individual iterations or after the completion project.",
                     "id" : 3,
                     "name" : "BusinessAsUsual",
                     "printName" : "Business As Usual",
                     "thresholds" :     ["divisionOfIterationsWithRetrospective" :
                                                    [    "description" : "Minimum percentage of the total number of iterations with a retrospective (0,100)",
                                                         "errorMessage" : "Percentage must be float number between 0 and 100",
                                                         "isErrorMessageShown" : false,
                                                         "name" : "divisionOfIterationsWithRetrospective",
                                                         "printName" : "Division of iterations with retrospective",
                                                         "type" : "Percentage"
                                                    ],
                                         "searchSubstringsWithRetrospective" :
                                                    [    "description" : "Substring that will be search in wikipages and activities",
                                                         "errorMessage" : "Maximum number of substrings is ten and must not starts and ends with characters || ",
                                                         "isErrorMessageShown" : false,
                                                         "name" : "searchSubstringsWithRetrospective",
                                                         "printName" : "Search substrings with retrospective",
                                                         "type" : "String"
                                                    ]
                                        ]
                    ]
                ],
                "projects" : [
                        [
                                "description" : "generic description in czech",
                                "id" : 6,
                                "name" : "Aplikace nad otevrenymi daty (KIV) BHVS"
                        ]
                ]

        )
        status 200
    }
}

