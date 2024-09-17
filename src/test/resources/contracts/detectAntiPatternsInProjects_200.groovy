package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Fetch all configurations of user."
    request {
        method POST()
        url("/v2/detect/analyze")
        headers {
            header("Authorization": "Bearer token")
            contentType applicationJson()
        }
        body(
                "configurationId" : "1",
                "selectedAntipatterns" : [3],
                "selectedProjects" : [1]
        )
    }
    response {
        body(
            [
                    [
                            "project" : [
                                    "description" : "",
                                    "id" : 1,
                                    "name" : "Aplikace pro Centrum blizkovychodnich studii (FF) Medici"
                            ],
                            "queryResultItems" : [
                                        ["antiPattern" : ["catalogueFileName" : "Business_As_Usual.md",
                                                         "description" : "Absence of a retrospective after individual iterations or after the completion project.",
                                                         "id" : 3,
                                                         "name" : "BusinessAsUsual",
                                                         "printName" : "Business As Usual",
                                                         "thresholds" : ["divisionOfIterationsWithRetrospective" : ["description" : "Minimum percentage of the total number of iterations with a retrospective (0,100)",
                                                                                                                    "errorMessage" : "Percentage must be float number between 0 and 100",
                                                                                                                    "isErrorMessageShown" : false,
                                                                                                                    "name" : "divisionOfIterationsWithRetrospective",
                                                                                                                    "printName" : "Division of iterations with retrospective",
                                                                                                                    "type" : "Percentage"
                                                         ],
                                                                         "searchSubstringsWithRetrospective" : ["description" : "Substring that will be search in wikipages and activities",
                                                                                                                "errorMessage" : "Maximum number of substrings is ten and must not starts and ends with characters || ",
                                                                                                                "isErrorMessageShown" : false,
                                                                                                                "name" : "searchSubstringsWithRetrospective",
                                                                                                                "printName" : "Search substrings with retrospective",
                                                                                                                "type" : "String"
                                                                         ]
                                                         ]
                                        ],
                                        "isDetected" : false,
                                        "resultDetails" : [
                                                [
                                                        "resultDetailName" : "Min retrospective limit",
                                                        "resultDetailValue" : "3"
                                                ],
                                                [
                                                        "resultDetailName" : "Found retrospectives",
                                                        "resultDetailValue" : "4"
                                                ],
                                                [
                                                        "resultDetailName" : "Total number of iterations",
                                                        "resultDetailValue" : "5"
                                                ],
                                                [
                                                        "resultDetailName" : "Conclusion",
                                                        "resultDetailValue" : "There is right number of retrospectives"
                                                ]
                                        ]
                                        ]
                            ]
                    ]
            ]
        )
        status 200
    }
}

