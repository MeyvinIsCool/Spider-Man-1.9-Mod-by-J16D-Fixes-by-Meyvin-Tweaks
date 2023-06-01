// by Meyvin Tweaks
// Web zip to buildings (IN EXPERIMENTAL STAGE !!! , and may cause bugs and crashes due to the OUTDATED CODE)
// Spider-Man Mod for GTA SA c.2018 - 2023
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_ml
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod
LVAR_INT iTempVar randomVal is_near_pole
LVAR_FLOAT xAngle zAngle x[4] y[4] z[4] v1 v2 fDistance currentTime fCharSpeed
LVAR_INT baseObject iWebActor iWebActorR obj sfx sfxB

CONST_INT STOP 0 

GET_PLAYER_CHAR 0 player_actor
GOSUB REQUEST_Animations
GOSUB REQUEST_webAnimations
GOSUB loadTextures
USE_TEXT_COMMANDS TRUE

GET_CLEO_SHARED_VAR varBuidlingZipFlag iTempVar
iTempVar = 1
SET_CLEO_SHARED_VAR varBuidlingZipFlag iTempVar

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1 // TRUE

            IF CLEO_CALL isActorInWater 0 player_actor
                WHILE  CLEO_CALL isActorInWater 0 player_actor
                    WAIT 0
                ENDWHILE
            ENDIF

            GET_CLEO_SHARED_VAR varBuidlingZipFlag iTempVar

            IF GOSUB is_not_char_playing_car_missions_anims
            AND iTempVar = 1

                //Compatible reservoirs 
                IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor -2022.26 13.982 61.60 25.0
                AND NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor -2192.0 389.789 64.624 25.0
                AND NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor -1873.89 900.735 65.2756 25.0
                AND NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor -1812.37 1039.22 82.0859 25.0
                AND NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor -1589.35 951.715 34.5971 25.0

                    // Buildings
                    IF GOSUB get_building_side
                        GOSUB draw_building_indicator
                        //----------------------------------- Zip to Point
                        // L2 + R2 
                        IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                        AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                        AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                        AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~
                            IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~          
                                IF GOSUB is_not_player_playing_swing_anims

                                    IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -3.0) (1 0 0 0 0)   //AIR
                                        //----------------------------------- Zip if $player on Air
                                        GOSUB REQUEST_Animations
                                        GOSUB REQUEST_webAnimations
                                        GOSUB in_air_zip_to_buidling
                                        IF is_near_pole = TRUE
                                            IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                                IF GOSUB does_skill_Point_Launch_enabled
                                                    GOSUB point_launch_air_building                                                           
                                                ELSE
                                                    GET_CHAR_HEADING player_actor (zAngle)
                                                    GET_COORD_FROM_ANGLED_DISTANCE x[0] y[0] zAngle 0.2 (x[0] y[0])
                                                    GOSUB stay_on_building_from_air                                                     
                                                ENDIF
                                            ELSE
                                                GET_CHAR_HEADING player_actor (zAngle)
                                                GET_COORD_FROM_ANGLED_DISTANCE x[0] y[0] zAngle 0.2 (x[0] y[0])
                                                GOSUB stay_on_building_from_air                                                   
                                            ENDIF                                                                                          
                                            is_near_pole = FALSE
                                            
                                        ENDIF

                                    ELSE
                                        //----------------------------------- Zip if $player on Ground
                                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
                                        IF z[0] >= z[1]   // fix for stuck player
                                            //----------------------------------- Zip if $player on Ground
                                            GOSUB REQUEST_Animations
                                            GOSUB REQUEST_webAnimations
                                            GOSUB on_ground_zip_to_point
                                            IF is_near_pole = TRUE
                                                IF IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
                                                    IF GOSUB does_skill_Point_Launch_enabled
                                                        GOSUB point_launch_ground_building
                                                    ELSE
                                                        GET_CHAR_HEADING player_actor (zAngle)
                                                        GET_COORD_FROM_ANGLED_DISTANCE x[0] y[0] zAngle 0.25 (x[0] y[0])
                                                        GOSUB stay_on_building_from_ground
                                                    ENDIF
                                                ELSE
                                                    GET_CHAR_HEADING player_actor (zAngle)
                                                    GET_COORD_FROM_ANGLED_DISTANCE x[0] y[0] zAngle 0.25 (x[0] y[0])
                                                    GOSUB stay_on_building_from_ground
                                                ENDIF
                                                is_near_pole = FALSE

                                            ENDIF
                                        ENDIF


                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF

                    ENDIF
                ENDIF
                
            ENDIF
        ELSE
            // Release files
            REMOVE_ANIMATION "mweb"
            REMOVE_ANIMATION "spider"
            REMOVE_AUDIO_STREAM sfx
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            REMOVE_TEXTURE_DICTIONARY
            WAIT 50
            TERMINATE_THIS_CUSTOM_SCRIPT

        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop 

//-+-----------buildings----------------------
get_building_side:
    CLEO_CALL getXYZAimCoords 0 player_actor 50.0 0.0 (x[0] y[0] z[0]) (x[2] y[2] z[2]) //(fDistance)
    z[0] += 0.65

    CLEO_CALL getXYZAimCoords 0 player_actor 50.0 1.0 (x[1] y[1] z[1]) (x[3] y[3] z[3]) //(fDistance)
    //PRINT_FORMATTED_NOW "Col1 %.2f %.2f %.2f ~n~norm1 %.2f %.2f %.2f ~n~Col2 %.2f %.2f %.2f ~n~norm2 %.2f %.2f %.2f" 1000 x[0] y[0] z[0] x[2] y[2] z[2] x[1] y[1] z[1] x[3] y[3] z[3]

    IF NOT x[2] = x[3]
    OR NOT y[2] = y[3]
    OR NOT z[2] = z[3]
        RETURN_TRUE
    ELSE
        RETURN_FALSE
   ENDIF
RETURN

draw_building_indicator:
    GET_CLEO_SHARED_VAR varHUD (iTempVar)
    IF iTempVar = 1     // 0:OFF || 1:ON
        CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (v1 v2) (x[3] y[3])
        GET_FIXED_XY_ASPECT_RATIO 30.0 30.0 (x[3] y[3])
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE TRUE
        DRAW_SPRITE objCrosshair (v1 v2) (x[3] y[3]) (255 255 255 200)
        IF GOSUB is_spider_hud_enabled
            GOSUB draw_tip_key_command
        ENDIF
    ENDIF
RETURN

draw_tip_key_command:
    //GET_FIXED_XY_ASPECT_RATIO 120.0 60.0 (x[3] y[3])
    x[3] = 90.00
    y[3] = 56.00
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idTip1 (50.0 400.0) (x[3] y[3]) (255 255 255 200)
    IF IS_PC_USING_JOYPAD
        iTempVar = 703  //~k~~PED_CYCLE_WEAPON_LEFT~
        CLEO_CALL GUI_DrawHelperText 0 (45.0 400.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
        iTempVar = 704  //~k~~PED_CYCLE_WEAPON_RIGHT~
        CLEO_CALL GUI_DrawHelperText 0 (80.0 397.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ELSE
        iTempVar = 705  //~h~Q
        CLEO_CALL GUI_DrawHelperText 0 (45.0 400.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
        iTempVar = 706  //~h~E
        CLEO_CALL GUI_DrawHelperText 0 (80.0 397.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ENDIF
RETURN

is_spider_hud_enabled:
    GET_CLEO_SHARED_VAR varHUD (iTempVar)
    IF iTempVar = 1     // 0:OFF || 1:ON            
        GET_CLEO_SHARED_VAR varHudRadar (iTempVar)  //display indicator only if radar is enabled
        IF iTempVar = 1
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

in_air_zip_to_buidling:
    GOSUB destroyTwoWebs
    GOSUB createTwoWebs
    //GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[2] y[2] z[2])
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("airToLampA" "spider") 13.0 (0 1 1 1) -2
    WAIT 1
    SET_CHAR_ANIM_SPEED player_actor "airToLampA" 2.0
    WHILE IS_CHAR_PLAYING_ANIM player_actor ("airToLampA")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("airToLampA") (currentTime)
        IF currentTime >= 0.98  //0.236   // frame
            SET_CHAR_ANIM_PLAYING_FLAG player_actor ("airToLampA") 0
            BREAK
        ENDIF
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.001 (x[1] y[1] z[1])
        SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
        CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
        GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
        SET_CHAR_ROTATION player_actor xAngle 0.0 zAngle
        IF DOES_OBJECT_EXIST baseObject
            SET_OBJECT_ROTATION baseObject xAngle 0.0 zAngle
        ENDIF
        //IF NOT IS_CHAR_REALLY_IN_AIR player_actor
        IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -2.0) (1 0 0 0 0)   //AIR
        ELSE
            //CLEAR_CHAR_TASKS player_actor
            //CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            WAIT 0
            is_near_pole = FALSE
            GOSUB destroyTwoWebs
            RETURN
        ENDIF
        WAIT 0
    ENDWHILE
    IF DOES_OBJECT_EXIST obj    
        SET_OBJECT_COLLISION obj FALSE  //fix camera&attach bug
    ENDIF

    GENERATE_RANDOM_INT_IN_RANGE 0 3 (randomVal)
    GOSUB playWebSound
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
    GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance)
    x[2] = 1.0 //1.5
    y[2] = fDistance - x[2]

    randomVal = 0
    GOSUB playSFXSound

    IF y[2] > 35.0
        randomVal = 0   // anim for long distance
    ELSE
        randomVal = 1   // anim for short distance
    ENDIF

    timera = 0
    //WHILE fDistance >= x[2]     //_distance              
    IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 1.5

        WHILE NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 1.5
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
            GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance)
            fDistance -= x[2]
            CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
            GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
            SET_CHAR_ROTATION player_actor xAngle 0.0 zAngle 

            CLEO_CALL linearInterpolation 0 (y[2] x[2] fDistance) (0.0 1.0) (currentTime)
            IF currentTime > 1.0
                currentTime = 1.0
            ENDIF
            SWITCH randomVal
                CASE 0  // anim for long distance
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("airToLampB" "spider") 34.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("airToLampB") currentTime
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("airToLampB") 0                                                        
                    BREAK
                CASE 1  // anim for short distance
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("airToLampB_B" "spider") 44.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME player_actor ("airToLampB_B") currentTime
                    SET_CHAR_ANIM_PLAYING_FLAG player_actor ("airToLampB_B") 0
                    BREAK
            ENDSWITCH
            IF DOES_CHAR_EXIST iWebActor
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("LA_airToLampA" "mweb") 44.0 (0 1 1 1) -2
                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("LA_airToLampA") currentTime
                SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("LA_airToLampA") 0
            ENDIF
            IF DOES_CHAR_EXIST iWebActorR
                TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("SH_airToLampA" "mweb") 44.0 (0 1 1 1) -2
                SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("SH_airToLampA") currentTime
                SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("SH_airToLampA") 0  
            ENDIF
            IF DOES_OBJECT_EXIST baseObject
                IF currentTime > 0.279
                //AND 0.302 > currentTime
                AND IS_OBJECT_ATTACHED baseObject
                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                ENDIF
            ENDIF
            GET_CHAR_SPEED player_actor (fCharSpeed)
            fCharSpeed *= 1.020
            CLAMP_FLOAT fCharSpeed 35.0 60.0 (fCharSpeed)
            CLEO_CALL setCharVelocityTo 0 player_actor (x[0] y[0] z[0]) fCharSpeed
            //PRINT_FORMATTED_NOW "vel:%.1f" 1 fCharSpeed //DEBUG

            IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 1 0 1 0)
            //CLEO_CALL isClearBetweenCoords 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 1 0 1 0)
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                WAIT 0
                is_near_pole = FALSE
                GOSUB destroyTwoWebs
                RETURN
            ENDIF
            IF timera > 750
                GOSUB stay_on_building_from_air
                GOSUB destroyWeb
                /*
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor                
                WAIT 0 
                TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("groundToLampC" "spider") 13.0 (0 1 1 1) -2
                WAIT 1
                SET_CHAR_ANIM_SPEED player_actor "groundToLampC" 1.65  
                */
                RETURN
            ENDIF 
        ENDWHILE
    ENDIF
    is_near_pole = TRUE
    GOSUB destroyTwoWebs
RETURN

point_launch_air_building:
    GOSUB destroyTwoWebs
    GET_CHAR_SPEED player_actor (fCharSpeed)
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    SET_CHAR_COLLISION player_actor FALSE
    CLEO_CALL setSmokeFX 0 player_actor (0.0 0.0 -0.5) 25.0

    CLEAR_CHAR_TASKS player_actor 
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "airToLampD" "spider" 36.0 (0 1 1 0) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "airToLampD" 1.30  //1.35

    WHILE IS_CHAR_PLAYING_ANIM player_actor ("airToLampD")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("airToLampD") (currentTime)
        IF currentTime >= 0.171  //frame 6/35
            SET_CHAR_COLLISION player_actor TRUE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    SET_CHAR_COLLISION player_actor TRUE
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 1.0 (x[0] y[0] z[0])
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    CLEO_CALL setCharViewPointToCamera 0 player_actor
    y[2] = 0.60 //0.5     //2.0
    z[2] = 0.40 //0.40    //1.5
    CLAMP_FLOAT fCharSpeed 40.0 55.0 (fCharSpeed)   //55.0
    CLEO_CALL setCharVelocity 0 player_actor (0.0 y[2] z[2]) fCharSpeed

    randomVal = 1
    GOSUB playSFXSound
    //CLEO_CALL setSmokeFX 0 player_actor (0.0 0.0 -0.5) 25.0
    WAIT 25
    WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
        WAIT 0
    ENDWHILE
RETURN

stay_on_building_from_air:
    GOSUB destroyTwoWebs
    SET_CHAR_COLLISION player_actor FALSE
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "groundToLampC" "spider" 54.0 (0 1 1 1) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "groundToLampC" 1.65
    WHILE IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("groundToLampC") (currentTime)
        IF currentTime >= 0.943   // frame 50/53
            SET_CHAR_ANIM_PLAYING_FLAG player_actor ("groundToLampC") STOP
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE

    WHILE IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")
        IF IS_BUTTON_PRESSED PAD1 SQUARE            // ~k~~PED_JUMPING~
        AND NOT IS_BUTTON_PRESSED PAD1 CROSS        // ~k~~PED_SPRINT~
        AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE       // ~k~~PED_FIREWEAPON~
        AND NOT IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2    // ~k~~PED_CYCLE_WEAPON_LEFT~/

            //----------------------------------- Jump from Building
            SET_CHAR_COLLISION player_actor TRUE
            GOSUB REQUEST_Animations
            y[2] = 1.5
            z[2] = 2.0
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_launch_A" "spider") 6.0 (0 1 1 0) -1
            WAIT 0
            CLEO_CALL setCharVelocity 0 player_actor (0.0 y[2] z[2]) 10.5

            WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_A")
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_launch_A") (currentTime)
                IF currentTime >= 0.800  //frame 4/5
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            
            WAIT 0
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_glide_A" "spider") 23.0 (0 1 1 1) -2
            WAIT 50

        ENDIF

        IF IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
        OR IS_BUTTON_PRESSED PAD1 LEFTSTICKY  //~k~~GO_FORWARD~ / ~k~~GO_BACK~
            y[2] = 0.0
            z[2] = 0.5
            CLEO_CALL setCharVelocity 0 player_actor (0.0 y[2] z[2]) 1.5
            WAIT 5
            SET_CHAR_COLLISION player_actor TRUE
            TASK_TOGGLE_DUCK player_actor TRUE        
        ENDIF
        WAIT 0
    ENDWHILE
    //CLEAR_CHAR_TASKS player_actor
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
        WAIT 0
    ENDWHILE
RETURN

on_ground_zip_to_point:
    GOSUB destroyTwoWebs
    GOSUB createTwoWebs
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
    SET_CHAR_HEADING player_actor zAngle
    
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("groundToLampA" "spider") 11.0 (0 1 1 1) -1
    WAIT 0
    TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("LA_groundToLampB" "mweb") 31.0 (0 1 1 1) -2
    TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("SH_groundToLampB" "mweb") 31.0 (0 1 1 1) -2

    IF IS_CHAR_PLAYING_ANIM player_actor ("groundToLampA")

        WHILE IS_CHAR_PLAYING_ANIM player_actor ("groundToLampA")
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("groundToLampA") (currentTime)
            CLEO_CALL linearInterpolation 0 (0.0 1.0 currentTime) (0.0 0.367) (fCharSpeed)
            IF DOES_CHAR_EXIST iWebActor
                IF IS_CHAR_PLAYING_ANIM iWebActor ("LA_groundToLampB")
                    //TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("LA_groundToLampB" "mweb") 31.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME iWebActor ("LA_groundToLampB") fCharSpeed
                    SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("LA_groundToLampB") FALSE
                ENDIF
            ENDIF
            IF DOES_CHAR_EXIST iWebActorR
                IF IS_CHAR_PLAYING_ANIM iWebActorR ("SH_groundToLampB")
                    //TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("SH_groundToLampB" "mweb") 31.0 (0 1 1 1) -2
                    SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("SH_groundToLampB") fCharSpeed
                    SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("SH_groundToLampB") FALSE
                ENDIF
            ENDIF
            IF currentTime >= 0.900   // frame 9/10
                SET_CHAR_ANIM_PLAYING_FLAG player_actor ("groundToLampA") FALSE
                IF DOES_CHAR_EXIST iWebActor
                    SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("LA_groundToLampB") TRUE
                ENDIF
                IF DOES_CHAR_EXIST iWebActorR
                    SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("SH_groundToLampB") TRUE
                ENDIF
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
    IF DOES_OBJECT_EXIST obj
        SET_OBJECT_COLLISION obj FALSE
    ENDIF

    randomVal = 3
    GOSUB playWebSound
    IF DOES_OBJECT_EXIST baseObject
        IF DOES_CHAR_EXIST iWebActor
        AND DOES_CHAR_EXIST iWebActorR
            IF IS_CHAR_PLAYING_ANIM iWebActor ("LA_groundToLampB")
            AND IS_CHAR_PLAYING_ANIM iWebActorR ("SH_groundToLampB")
                ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
            ENDIF
        ENDIF
    ENDIF
    GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance) //_distance
    IF 35.0 > fDistance
        fDistance = 35.0
    ENDIF
    fCharSpeed = fDistance * 0.85

    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("groundToLampB" "spider") 21.0 (0 1 1 1) -1
    WAIT 0
    WHILE IS_CHAR_PLAYING_ANIM player_actor ("groundToLampB")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("groundToLampB") (currentTime)
        IF currentTime >= 0.20   // frame 4/20
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE                                                
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
    z[1] += 1.0
    SET_CHAR_COORDINATES_SIMPLE player_actor x[1] y[1] z[1]
    
    timera = 0
    //WHILE fDistance >= 1.5     //_distance
    IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 1.5

        WHILE NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 1.5
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
            GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance) //_distance
            CLEO_CALL getXangleBetweenPoints 0 (x[1] y[1] z[1]) (x[0] y[0] z[0]) (xAngle)
            GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
            SET_CHAR_ROTATION player_actor xAngle 0.0 zAngle
            IF DOES_OBJECT_EXIST baseObject
                SET_OBJECT_ROTATION baseObject xAngle 0.0 zAngle
            ENDIF
            IF IS_CHAR_PLAYING_ANIM player_actor ("groundToLampB")
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("groundToLampB") (currentTime)
                IF currentTime > 0.500      //frame 10/20
                AND 0.600 > currentTime     //frame 12/20
                    IF DOES_OBJECT_EXIST baseObject
                        IF IS_OBJECT_ATTACHED baseObject
                            DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
            fCharSpeed *= 1.020    
            CLAMP_FLOAT fCharSpeed 35.0 60.0 (fCharSpeed)     
            CLEO_CALL setCharVelocityTo 0 player_actor (x[0] y[0] z[0]) fCharSpeed
            //PRINT_FORMATTED_NOW "vel:%.1f" 1 fCharSpeed //DEBUG
            
            IF NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 1 0 1 0)
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                WAIT 0
                is_near_pole = FALSE
                GOSUB destroyTwoWebs
                IF DOES_OBJECT_EXIST obj
                    SET_OBJECT_COLLISION obj TRUE
                ENDIF
                RETURN
            ENDIF
            IF timera > 4000
                CLEAR_CHAR_TASKS player_actor
                CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
                WAIT 0
                is_near_pole = FALSE
                GOSUB destroyTwoWebs
                IF DOES_OBJECT_EXIST obj
                    SET_OBJECT_COLLISION obj TRUE
                ENDIF
                RETURN
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
    is_near_pole = TRUE
    GOSUB destroyTwoWebs
RETURN

point_launch_ground_building:
    GOSUB destroyTwoWebs
    GET_CHAR_SPEED player_actor (fCharSpeed)

    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    SET_CHAR_COLLISION player_actor FALSE
    CLEO_CALL setSmokeFX 0 player_actor (0.0 0.0 -0.5) 25.0

    CLEAR_CHAR_TASKS player_actor 
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "groundToLampD" "spider" 47.0 (0 1 1 0) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "groundToLampD" 1.80   //1.85
    WHILE IS_CHAR_PLAYING_ANIM player_actor ("groundToLampD")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("groundToLampD") (currentTime)
        IF currentTime >= 0.217     // frame 10/46
            SET_CHAR_COLLISION player_actor TRUE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE
    SET_CHAR_COLLISION player_actor TRUE
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 1.0 (x[0] y[0] z[0])
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    CLEO_CALL setCharViewPointToCamera 0 player_actor
    y[2] = 0.75 //0.65  //2.0
    z[2] = 0.45 //0.45  //1.8
    fCharSpeed *= 1.25
    CLAMP_FLOAT fCharSpeed 40.0 50.0 (fCharSpeed)
    CLEO_CALL setCharVelocity 0 player_actor (0.0 y[2] z[2]) fCharSpeed

    randomVal = 1
    GOSUB playSFXSound
    WAIT 0
    WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
        WAIT 0
    ENDWHILE
RETURN

stay_on_building_from_ground:
    GOSUB destroyTwoWebs
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    SET_CHAR_COLLISION player_actor FALSE

    CLEAR_CHAR_TASKS player_actor 
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "groundToLampC" "spider" 54.0 (0 1 1 1) -2
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "groundToLampC" 1.65
    WHILE IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")
        GET_CHAR_ANIM_CURRENT_TIME player_actor ("groundToLampC") (currentTime)
        IF currentTime >= 0.943   // frame 50/53
            SET_CHAR_ANIM_PLAYING_FLAG player_actor ("groundToLampC") STOP
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE

    WHILE IS_CHAR_PLAYING_ANIM player_actor ("groundToLampC")

        IF IS_BUTTON_PRESSED PAD1 SQUARE            // ~k~~PED_JUMPING~
        AND NOT IS_BUTTON_PRESSED PAD1 CROSS        // ~k~~PED_SPRINT~
        AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE       // ~k~~PED_FIREWEAPON~
        AND NOT IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2    // ~k~~PED_CYCLE_WEAPON_LEFT~/   

            //----------------------------------- Jump from Building
            SET_CHAR_COLLISION player_actor TRUE
            GOSUB REQUEST_Animations
            y[2] = 1.5
            z[2] = 2.0
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_launch_A" "spider") 6.0 (0 1 1 0) -1
            WAIT 0
            CLEO_CALL setCharVelocity 0 player_actor (0.0 y[2] z[2]) 8.0

            WHILE IS_CHAR_PLAYING_ANIM player_actor ("jump_launch_A")
                GET_CHAR_ANIM_CURRENT_TIME player_actor ("jump_launch_A") (currentTime)
                IF currentTime >= 0.800  //frame 4/5
                    BREAK
                ENDIF
                WAIT 0
            ENDWHILE
            WAIT 0
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("jump_glide_A" "spider") 23.0 (0 1 1 0) -2
            WAIT 50
        ENDIF

        IF IS_BUTTON_PRESSED PAD1 LEFTSTICKX   // ~k~~GO_LEFT~ / ~k~~GO_RIGHT~
        OR IS_BUTTON_PRESSED PAD1 LEFTSTICKY  //~k~~GO_FORWARD~ / ~k~~GO_BACK~
            SET_CHAR_COLLISION player_actor TRUE
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            WAIT 0
            TASK_TOGGLE_DUCK player_actor TRUE
            WAIT 500
        ENDIF

        WAIT 0
    ENDWHILE
    //CLEAR_CHAR_TASKS player_actor
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    WHILE IS_BUTTON_PRESSED PAD1 SQUARE  // ~k~~PED_JUMPING~
        WAIT 0
    ENDWHILE
RETURN

//-+----------------createWebs--------------------------
createTwoWebs:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_CHAR_EXIST iWebActorR
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject) 
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (3.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (-3.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1
        UNLOAD_SPECIAL_CHARACTER 9
        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle
    ENDIF
RETURN

destroyTwoWebs:
    IF DOES_CHAR_EXIST iWebActor
        DELETE_CHAR iWebActor
    ENDIF
    IF DOES_CHAR_EXIST iWebActorR
        DELETE_CHAR iWebActorR
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        DELETE_OBJECT baseObject
    ENDIF
RETURN

destroyWeb:
    IF DOES_CHAR_EXIST iWebActor
        DELETE_CHAR iWebActor
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        DELETE_OBJECT baseObject
    ENDIF
RETURN

playWebSound:
    REMOVE_AUDIO_STREAM sfx
    SWITCH randomVal
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN

playSFXSound:
    REMOVE_AUDIO_STREAM sfxB
    SWITCH randomVal
        CASE 0      // sfx pull in air
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web5_f.mp3" (sfxB)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfxB player_actor
                SET_AUDIO_STREAM_STATE sfxB 1 
                SET_AUDIO_STREAM_VOLUME sfxB 0.5
            ENDIF
        BREAK
        CASE 1      // sfx jump from pole
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\jump2.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\jump2.mp3" (sfxB)
                SET_AUDIO_STREAM_STATE sfxB 1 
                SET_AUDIO_STREAM_VOLUME sfxB 0.5
            ENDIF        
        BREAK
    ENDSWITCH
RETURN
//-+-----------------------GET--------------------------
readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
RETURN

REQUEST_Animations:
    IF NOT HAS_ANIMATION_LOADED "spider"
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_Animations

REQUEST_webAnimations:
    IF NOT HAS_ANIMATION_LOADED "mweb"
        REQUEST_ANIMATION "mweb"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_webAnimations

loadTextures:
    LOAD_TEXTURE_DICTIONARY spaim
    //Textures
    CONST_INT idTip1 58
    CONST_INT idLR 59
    CONST_INT tCrosshair 60
    CONST_INT objCrosshair 61
    CONST_INT idTip3 63
    LOAD_SPRITE idLR "clr"
    LOAD_SPRITE objCrosshair "ilock"
    LOAD_SPRITE idTip1 "htip1"
    LOAD_SPRITE idTip3 "htip3"
    //LOAD_SPRITE tCrosshair "crosshair"
RETURN

is_not_player_playing_swing_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_A")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_B")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_C")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_D")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_E")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_L_F")
    
        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_A")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_B")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_C")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_D")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_E")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("swing_R_F")

            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

does_skill_Point_Launch_enabled:
    GET_CLEO_SHARED_VAR varSkill3a (iTempVar)   // 0:OFF || 1:ON
    IF iTempVar = 1
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_not_char_playing_car_missions_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_idle_Z")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_00")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_01")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_02")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_00")
        IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_01")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_02")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_00")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_01")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_00")
        AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_01")
            IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_front")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_fall")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_center")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_left")
            AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_hit_right")
                RETURN_TRUE
                RETURN
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN
//-+----------------------------------------------------------

}
SCRIPT_END

//-+---CALL SCM HELPERS
{
//CLEO_CALL linearInterpolation 0 (x0 x1 x) (y0 y1) (y)
linearInterpolation:
LVAR_FLOAT x0   //Min 
LVAR_FLOAT x1   //Max
LVAR_FLOAT x    //Mid
LVAR_FLOAT y0   //Min
LVAR_FLOAT y1   //Max
LVAR_FLOAT result[2]
result[0] = (x1 - x0)
IF result[0] = 0.0
    result[1] = (y0 + y1)
    result[1] /= 2.0
ELSE
    y1 = (y1 - y0)
    x1 = (x1 - x0)
    x = (x - x0)
    result[0] = (y1 / x1)
    result[1] = (result[0] * x)
    result[1] = (result[1] + y0)
ENDIF
CLEO_RETURN 0 result[1]     //y0 + (x - x0) * (y1 - y0)/(x1 - x0) 
}
{
//CLEO_CALL isActorInWater 0 player_actor
isActorInWater:
    LVAR_INT scplayer   //in
    LVAR_FLOAT x y z height
    IF IS_PLAYER_PLAYING scplayer
        GET_CHAR_COORDINATES scplayer (x y z)
        GET_WATER_HEIGHT_AT_COORDS x y TRUE (height)
        IF height > z
            RETURN_TRUE
        ELSE
            RETURN_FALSE
        ENDIF
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isClearInSight:
    LVAR_INT tempPlayer
    LVAR_FLOAT x y z
    LVAR_INT isSolid isCar isActor isObject isParticle
    LVAR_FLOAT xA yA zA xB yB zB 
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer x y z (xA yA zA)
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 0.0 (xB yB zB)
    IF IS_LINE_OF_SIGHT_CLEAR xB yB zB xA yA zA (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL setCharVelocityTo 0 iPlayer (x y z) Amp
setCharVelocityTo:
    LVAR_INT scplayer    //in
    LVAR_FLOAT xIn yIn zIn iAmplitude   //in
    LVAR_FLOAT x[2] y[2] z[2] fDistance
    x[1] = xIn
    y[1] = yIn
    z[1] = zIn
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0]
    GET_DISTANCE_BETWEEN_COORDS_3D (0.0 0.0 0.0) (x[1] y[1] z[1]) fDistance
    x[1] = (x[1] / fDistance)
    y[1] = (y[1] / fDistance)
    z[1] = (z[1] / fDistance)
    x[1] *= iAmplitude
    y[1] *= iAmplitude
    z[1] *= iAmplitude
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
    WAIT 0
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
CLEO_RETURN 0
}
{
//CLEO_CALL getXangleBetweenPoints 0 /*from*/ 0.0 0.0 0.0 /*and*/ 1.0 0.0 0.0 (/*xAngle*/fSyncAngle)
getXangleBetweenPoints:
    LVAR_FLOAT xA yA zA xB yB zB    //in
    LVAR_FLOAT pointY pointZ
    LVAR_FLOAT xAngle
    GET_DISTANCE_BETWEEN_COORDS_2D xA yA xB yB (pointY)
    pointZ = (zA - zB)
    GET_HEADING_FROM_VECTOR_2D pointY pointZ (xAngle)
    //CLEO_CALL ATAN 0 /*2D_Coord*/ pointY pointZ /*angle*/ (xAngle)
    xAngle -= 270.0
    xAngle *= -1.0
CLEO_RETURN 0 xAngle
}
{
//CLEO_CALL setSmokeFX 0 /*char*/player_actor /*offset*/(0.0 0.0 0.0) /*speed*/ 25.0
setSmokeFX:
    LVAR_INT char
    LVAR_FLOAT xOffset yOffset zOffset
    LVAR_FLOAT speed
    LVAR_FLOAT x[2] y[2] z[2]
    LVAR_FLOAT angle
    IF DOES_CHAR_EXIST char
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS char (xOffset yOffset zOffset) (x[0] y[0] z[0])
        speed *= 0.05
        speed += 1.5
        angle = 0.0
        WHILE 360.0 > angle
            COS angle (x[1]) 
            SIN angle (y[1])
            x[1] *= speed
            y[1] *= speed
            ADD_SMOKE_PARTICLE (x[0] y[0] z[0]) (x[1] y[1] 0.15) (0.8313 0.8313 0.8313) 1.0 (0.15) (0.125)
            angle += 15.0
        ENDWHILE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL setCharVelocity 0 player_actor /*offset*/ 0.0 1.0 1.0 /*amplitude*/ 5.0
setCharVelocity:
    LVAR_INT scplayer //in
    LVAR_FLOAT xVel yVel zVel amplitude //in
    LVAR_FLOAT x[2] y[2] z[2]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer xVel yVel zVel (x[1] y[1] z[1])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0]
    x[1] *= amplitude
    y[1] *= amplitude
    z[1] *= amplitude
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
CLEO_RETURN 0
}
{
//CLEO_CALL setCharViewPointToCamera 0 player_actor
setCharViewPointToCamera:
    LVAR_INT scplayer   //in
    LVAR_FLOAT xPoint yPoint zPoint xPos yPos zPos newAngle
    GET_ACTIVE_CAMERA_POINT_AT xPoint yPoint zPoint
    GET_ACTIVE_CAMERA_COORDINATES xPos yPos zPos
    xPoint = xPoint - xPos
    yPoint = yPoint - yPos
    GET_HEADING_FROM_VECTOR_2D xPoint yPoint (newAngle)
    SET_CHAR_HEADING scplayer newAngle
CLEO_RETURN 0
}
{
//CLEO_CALL get_object_offset_indicator 0 obj (x y z)
get_object_offset_indicator:
    LVAR_INT obj    //in
    LVAR_INT idModel
    LVAR_FLOAT x[3] y[3] z[3]
    IF DOES_OBJECT_EXIST obj
    AND NOT HAS_OBJECT_BEEN_DAMAGED obj 
        //Small
        IF DOES_OBJECT_HAVE_THIS_MODEL obj 1568     //chinalamp_sf
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1223     //lampost_coast ----|||
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1232     //Streetlamp1   ----|||
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1231     //Streetlamp2   (double)
        //OR DOES_OBJECT_HAVE_THIS_MODEL obj 1375     //tramstop_SF
            GET_OBJECT_MODEL obj (idModel)
            GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
            x[1] = (x[1] + 0.5)    //0.45
            z[2] = (z[2] - 0.25)   //0.6
            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.0 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2]
            RETURN_TRUE
        ELSE
            //Tall (one)
            IF DOES_OBJECT_HAVE_THIS_MODEL obj 1297     //lamppost1     ----|||
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1298     //lamppost1_d   ----|||
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1226     //lamppost3     ----|||
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 3853     //Gay_lamppost  (same as above)
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1350     //CJ_TRAFFIC_LIGHT4
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 3855     //GAY_TRAFFIC_LIGHT (same as above)
                GET_OBJECT_MODEL obj (idModel)
                GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
                x[1] = (x[1] + 0.50)   //0.45
                z[2] = (z[2] - 0.25)   //0.6
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.30 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2]
                RETURN_TRUE
            ELSE
                //Tall (one)
                IF DOES_OBJECT_HAVE_THIS_MODEL obj 3460     //vegaslampost
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 1294     //mlamppost 
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 1295     //doublestreetlght1
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 1296     //doublestreetlght1_d   
                    GET_OBJECT_MODEL obj (idModel)
                    GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
                    x[1] = (x[1] + 0.50)   //0.45
                    z[2] = (z[2] - 0.25)   //0.6
                    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.30 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2]
                    RETURN_TRUE
                ELSE
                    //Tall (two)
                    IF DOES_OBJECT_HAVE_THIS_MODEL obj 1290     //lamppost2
                    OR DOES_OBJECT_HAVE_THIS_MODEL obj 3463     //vegaslampost2
                        GET_OBJECT_MODEL obj (idModel)
                        GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
                        x[1] = (x[1] + 0.50)   //0.45
                        z[2] = (z[2] - 0.20)   //0.6
                        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.30 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2]
                        RETURN_TRUE
                    ELSE
                        //Traffic_Light
                        IF DOES_OBJECT_HAVE_THIS_MODEL obj 1315     //TRAFFICLIGHT1   
                        //OR DOES_OBJECT_HAVE_THIS_MODEL obj 1283     //MTraffic1   
                        //OR DOES_OBJECT_HAVE_THIS_MODEL obj 3516     //vgsstriptlights1 
                        //OR DOES_OBJECT_HAVE_THIS_MODEL obj 1284     //MTraffic2
                            GET_OBJECT_MODEL obj (idModel)
                            GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
                            x[1] = (x[1] - 3.00)   //0.45
                            z[2] = (z[2] - 0.20)   //0.6
                            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.30 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2]
                            RETURN_TRUE
                        ELSE
                            //lamps construction
                            IF DOES_OBJECT_HAVE_THIS_MODEL obj 8875     //vgsEcnstrct14     ----|||
                            OR DOES_OBJECT_HAVE_THIS_MODEL obj 8877     //vgsEcnstrct10   ----|||
                            OR DOES_OBJECT_HAVE_THIS_MODEL obj 8879     //vgsEcnstrct08     ----|||
                            OR DOES_OBJECT_HAVE_THIS_MODEL obj 3398     //cxrf_floodlite_     ----|||
                                GET_OBJECT_MODEL obj (idModel)
                                GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
                                z[2] = (z[2] + 0.20)
                                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (0.0 0.0 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2]
                                RETURN_TRUE
                            ELSE
                                RETURN_FALSE
                            ENDIF

                            /*IF DOES_OBJECT_HAVE_THIS_MODEL obj 1974     //kb_golfball
                                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (0.0 0.0.0 0.25) (x[0] y[0] z[0])
                                RETURN_TRUE
                            ELSE
                                RETURN_FALSE    
                            ENDIF*/
                            /*
                            //Pole
                            IF DOES_OBJECT_HAVE_THIS_MODEL obj 1308     //telgrphpole02
                            OR DOES_OBJECT_HAVE_THIS_MODEL obj 3854     //GAY_telgrphpole
                            OR DOES_OBJECT_HAVE_THIS_MODEL obj 3459     //vgntelepole1
                            OR DOES_OBJECT_HAVE_THIS_MODEL obj 3875     //SFtelepole
                            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1307     //telgrphpoleall
                                RETURN_TRUE
                                RETURN
                            ELSE
                                RETURN_FALSE
                                RETURN
                            ENDIF        
                            */            
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0 x[0] y[0] z[0]
}

{
//CLEO_CALL getXYZAimCoords 0 scplayer fRange fZPoint (x y z) (x2 y2 z2) fVar
getXYZAimCoords:
    LVAR_INT scplayer   //in
    LVAR_FLOAT range fZPoint  //in
    LVAR_FLOAT fromX fromY fromZ
    LVAR_FLOAT camX camY camZ pointX pointY pointZ
    LVAR_INT var1 var2 i j k
    LVAR_FLOAT resultX resultY resultZ x y z
    IF DOES_CHAR_EXIST scplayer
        GET_CHAR_COORDINATES scplayer (fromX fromY fromZ)
        GET_VAR_POINTER (camX) (var1)
        GET_VAR_POINTER (pointX) (var2)
        CALL_METHOD 0x514970 /*struct*/0xB6F028 /*params*/6 /*pop*/0 /*pPoint*/var2 /*pCam*/var1 /*fZ*/fromZ /*fY*/fromY /*fX*/fromX /*fRange*/ range
        pointZ += fZPoint
        GET_PLAYER_CHAR 0 scplayer
        GET_PED_POINTER scplayer (i)
        GET_LABEL_POINTER Buffer44 j
        IF GET_COLLISION_BETWEEN_POINTS (camX camY camZ) (pointX pointY pointZ) TRUE FALSE FALSE FALSE FALSE FALSE TRUE TRUE i j (resultX resultY resultZ i)
            GET_ENTITY_TYPE i (k)
            IF k = ENTITY_TYPE_BUILDING
                GET_COLPOINT_NORMAL_VECTOR j x y z
                x *= 10.0
                y *= 10.0
                z *= 10.0
                CLEO_RETURN 0 resultX resultY resultZ x y z
            ENDIF
            /*
            GET_ENTITY_TYPE i (k)
            IF k = ENTITY_TYPE_BUILDING
                GET_ENTITY_COORDINATES i (pointX pointY pointZ)
                resultZ -= pointZ
                pointZ += resultZ
                pointZ += 0.75
                CLEO_RETURN 0 resultX resultY pointZ x y z
            ELSE
                CLEO_RETURN 0 resultX resultY resultZ x y z
            ENDIF
            */
        ELSE
            resultX = pointX
            resultY = pointY
            resultZ = pointZ
        ENDIF
    ENDIF
CLEO_RETURN 0 resultX resultY resultZ 0.0 0.0 0.0
}


{
//CLEO_CALL GUI_DrawHelperText 0 /*pos*/(320.0 240.0) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0
GUI_DrawHelperText:
LVAR_FLOAT posX posY    // In
LVAR_INT textId formatId    //in
LVAR_FLOAT paddingLeft paddingTop   //in
LVAR_FLOAT h
LVAR_TEXT_LABEL gxt
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "SP%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    ELSE
        SET_TEXT_CENTRE FALSE
    ENDIF
    posX += paddingLeft
    CLEO_CALL GUI_SetTextFormatByID 0 formatId (h)
    posY -= h
    posY += paddingTop
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT posX posY $gxt
ENDIF
CLEO_RETURN 0
}
// --- Format IDs
{
GUI_SetTextFormatByID:
LVAR_INT formatId //In
SWITCH formatId
    CASE 1
        GOSUB GUI_TextFormat_Small
        CLEO_RETURN 0 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_Medium  
        CLEO_RETURN 0 4.0
        BREAK
ENDSWITCH

GUI_TextFormat_Small:   //White
    SET_TEXT_COLOUR 255 255 255 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_SCALE 0.14 0.65 //0.12 0.58 //0.14 0.65
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Medium:  //White
    SET_TEXT_COLOUR 255 255 255 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_SCALE 0.18 0.87 //0.14 0.65
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
}

Buffer44:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 //12
00 00 00 00 00 00 00 00 00 00 00 00 //24
00 00 00 00 00 00 00 00 00 00 00 00 //36
00 00 00 00 00 00 00 00             //44
ENDDUMP


//-+---CONSTANTS--------------------
//GLOBAL_CLEO_SHARED_VARS
//100 slots - range 0 to 99
CONST_INT varStatusSpiderMod    0     //1= Mod activated || 0= Mod Deactivated
CONST_INT varHUD                1     //1= Activated     || 0= Deactivated
CONST_INT varMusic              2     //1= Music On	    || 0= Music Off

CONST_INT varHudRadar           3     //sp_hud - MSpiderJ16Dv7
CONST_INT varHudHealth          4     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudAmmo            5     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudMoney           6     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudTime            7     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudBreath          8     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudArmour          9     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudWantedS         10    //sp_hud    ||1= Activated     || 0= Deactivated

CONST_INT varOnmission          11    //0:Off ||1:on mission || 2:car chase || 3:thug hidouts || 4:street crimes || 5:boss2
CONST_INT varCrimesProgress     12    //for stadistics ||MSpiderJ16Dv7
CONST_INT varPcampProgress      13    //for stadistics ||MSpiderJ16Dv7
CONST_INT varCarChaseProgress   14    //for stadistics ||MSpiderJ16Dv7
CONST_INT varScrewBallProgress  15    //for stadistics ||MSpiderJ16Dv7
CONST_INT varBackpacksProgress  16    //for stadistics ||MSpiderJ16Dv7
CONST_INT varLandmarksProgress  17    //for stadistics ||MSpiderJ16Dv7

CONST_INT varAlternativeSwing   20    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varSwingBuilding      21    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFixGround          22    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varMouseControl       23    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varAimSetup           24    // 0:Manual Aim || 1:Auto Aim //sp_dw
CONST_INT varPlayerCanDrive     25    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFriendlyN          26    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varThrowVehDoors      27    //MSpiderJ16Dv7    ||1= Activated     || 0= 
CONST_INT varThrowFix           28    //sp_thob          ||1= Activated     || 0= Deactivated
CONST_INT varBuidlingZipFlag    29    //sp_mlb           ||1= Activated     || 0= Deactivated

CONST_INT varLevelChar          30    //sp_lvl    || Level
CONST_INT varStatusLevelChar    31    //If value >0 automatically will add that number to Experience Points (Max Reward +2500)

CONST_INT varIdWebWeapon        32    //sp_mm     || 1-8 weap
CONST_INT varWeapAmmo           33    //sp_wep    ||store current weap ammo
CONST_INT varIdPowers           34    //MSpiderJ16Dv7 - sp_po     ||Id powers 1 - 12
CONST_INT varPowersProgress     35    //sp_po     || current power progress
CONST_INT varHitCount           36    //sp_hit    || hitcounting
CONST_INT varHitCountFlag       37    //sp_hit    || hitcounting  
CONST_INT varReservoirInactive  38    //sp_res    || disable reservoirs 

CONST_INT varInMenu             40    //1= On Menu       || 0= Menu Closed
CONST_INT varMapLegendLandMark  43    //Show: 1= enable   || 0= disable
CONST_INT varMapLegendBackPack  44    //Show: 1= enable   || 0= disable

CONST_INT varSkill1             50    //sp_dw    ||1= Activated     || 0= Deactivated
CONST_INT varSkill2             51    //sp_ev    ||1= Activated     || 0= Deactivated
CONST_INT varSkill2a            52    //sp_ev    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3             53    //sp_me    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3a            54    //sp_ml    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3b            55    //sp_me    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c            56    //sp_main  ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c1           57    //sp_mb    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c2           58    //sp_mb    ||1= Activated     || 0= Deactivated                        