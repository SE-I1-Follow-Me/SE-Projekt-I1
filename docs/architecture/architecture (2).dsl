workspace {

    model {
    
        user = person "User"

        
        softwareSystem = softwareSystem "Roboter Software System" "Kotlin App (Springboot), DB" {
        
            server = container "Database" "MSSQL (on GNU Linux Ubuntu)" {
            
            }
            
            app = container "Android Application" "Kotlin (based on Springboot)" {
            
                lang = component "Language Selection" "Selection of German/ English"{
                    user -> this "configures"
                }
                comm = component "DB Communication" "Sends requests to the Database"{
                    this -> server "access"
                }
                
               auth = component "Authentification" "via generated tokens"{
                    lang -> auth "shows"
                    auth -> comm "uses"
                }
                
                status = component "Robot Status Overview" "Map View, crucial information (battery level,position , direction, local time)"{
                    auth -> this "shows"
                    this -> comm "uses"
                }
                
                con = component "Main Controller" "control button cockpit"{
                    auth -> this "shows"
                    this -> comm "uses"
                }
                
                follow = component "Follow Me Function" "Controll algorythm that let the roboter follow its owner"{
                    con -> this "contains"
                }
                
                lspace = component "Local Memory" "uses free smartphone space"{
                    lang -> this "uses"

                }
                
            }
                
        }
        sim = softwareSystem "Simulation System" {
        
                server -> sim "Sends: Commands, Position, Direction"

        }
    }

    views {
        systemContext softwareSystem "SystemContext" {
            include *
            autoLayout
        }
        
        container softwareSystem {
            include *
            autolayout 
        }
        
        systemContext sim {
            include *
            autolayout 
        }
        
        component app {
            include *
            autolayout 
        }

        styles {
            element "Software System" {
                background #5BA7DC
                color #ffffff
            }
            element "Person" {
                shape person
                background #1168bd
                color #ffffff
            }
        }
    }
}