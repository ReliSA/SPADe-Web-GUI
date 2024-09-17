if not exists (select * from sysobjects where name='app_metadata' and xtype='U')

    create table app_metadata (
        id int identity(1, 1),
        appDataKey nvarchar(255),
        appDataValue nvarchar(max)
    )

    go

insert into app_metadata (appDataKey, appDataValue)
    values
        ('basics', N'[
          {
            "version": "1.0.0",
            "authors": [
              {
                "name": "Ondřej Váně",
                "email": "vaneo@students.zcu.cz"
              }
            ],
            "description": "This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application."
          },
          {
            "version": "1.1.0",
            "authors": [
              {
                "name": "Ondřej Váně",
                "email": "vaneo@students.zcu.cz"
              }
            ],
            "description": "This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application."
          },
          {
            "version": "1.2.0",
            "authors": [
              {
                "name": "Petr Štěpánek",
                "email": "petrs1@students.zcu.cz"
              }
            ],
            "description": "This application is used to detect presence of anti-patterns in project management tools data. Ten selected anti-patterns are implemented in this application."
          },
          {
            "version": "2.0.0",
            "authors": [
              {
                "name": "Petr Štěpánek",
                "email": "petrs1@students.zcu.cz"
              },
              {
                "name": "Petr Urban",
                "email": "urbanp@students.zcu.cz"
              },
              {
                "name": "Jiří Trefil",
                "email": "trefil@students.zcu.cz"
              },
              {
                "name": "Václav Hrabík",
                "email": "hrabikv@students.zcu.cz"
              }
            ],
            "description": "Experience the next evolution of our application with version 2.0.0, featuring a revamped infrastructure, three distinct apps, MS SQL Server integration, a comprehensive user provider system, and contract testing for enhanced reliability."
          }
        ]')

