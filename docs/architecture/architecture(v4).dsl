workspace {

    model {
    
        user = person "User"

        
        softwareSystem = softwareSystem "Roboter Software System" "Kotlin App (Springboot) & MySQL DB" {
        
            server = container "Database" "MySQL 8.0.~ (on GNU Linux Ubuntu)" "Database" "Database"{
                
            }
            
            spring = container "Spring Boot" "Spring Boot Framework with Spring Security" "Framework" "Framework"{
                spring -> server "sends transactions"
            }
            
            app = container "Android Application" "Kotlin (based on Springboot)" "App" "App"{
            
                lang = component "Language Selection" "Selection of German/ English"{
                    user -> this "configures"
                }
                comm = component "Database Connector" "Central module for sending and receiving data transactions from the db; it is also a placeholder for possible security and database addons (Maps)"{
                    this -> spring "access"
                }
                
               auth = component "Login Controller" "via account access"{
                    lang -> auth "shows"
                    auth -> comm "uses"
                }
                
                status = component "Robot Status Overview" "Map View, crucial information (battery level,position , direction, local time, [...])"{
                    auth -> this "shows"
                    this -> comm "uses"
                }
                
                con = component "Main Controller" "remote control button cockpit (overview)"{
                    auth -> this "shows"
                    this -> comm "uses"
                }
                
                follow = component "Follow Me Function" "Controll algorythm that let the roboter follows his owner"{
                    con -> this "contains"
                }
                
                lspace = component "Local Memory" "uses local (mobil device) space" "Database" "Local Database"{
                    lang -> this "uses"

                }
                
            }
                
        }
        sim = softwareSystem "Simulation System" "light testing and demonstration windows application" {
        
                sim -> spring "Sends: Commands, Position, Direction"

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
            element "App" {
                shape MobileDeviceLandscape
                background #C70039
                color #ffffff
            }
            element "Software System" {
                background #5BA7DC
                color #ffffff
            }
            element "Person" {
                shape person
                background #1168bd
                color #ffffff
            }
            element "Framework" {
                shape hexagon
                background #900C30
                color #ffffff
            }
            element "Database" {
                shape cylinder
                background #900C30
                color #ffffff
            }
            element "Local Database" {
                shape cylinder
                background #999999
                color #ffffff
            }
        }
    }
}