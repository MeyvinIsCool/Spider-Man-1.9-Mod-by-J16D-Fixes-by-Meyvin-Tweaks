// by Meyvin Tweaks
// Throw Objects v2
// Spider-Man Mod for GTA SA c.2018 - 2022
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_thob
WAIT 0 
WAIT 0
WAIT 0
WAIT 0

// Coordinates Format:
//     | x[0] y[0] z[0] --> Object | | x[1] y[1] z[1] --> Spider-Man |

LVAR_INT player_actor toggleSpiderMod 
LVAR_INT iTempVar is_near_car obj iVeh iHitCounter
LVAR_FLOAT zAngle v1 v2 fDistance currentTime fCharSpeed
LVAR_FLOAT x[4] y[4] z[4] 
LVAR_INT randomVal sfx iPed 

GET_PLAYER_CHAR 0 player_actor
GOSUB loadTextures
USE_TEXT_COMMANDS TRUE

start_check:
GOSUB readVars
IF toggleSpiderMod = 0
    WHILE toggleSpiderMod = 0
        WAIT 0
        GOSUB readVars 
        IF toggleSpiderMod = 1
            BREAK
        ENDIF
    ENDWHILE
ENDIF

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

            IF IS_CHAR_REALLY_IN_AIR player_actor
                //in air
            ELSE
                GET_CLEO_SHARED_VAR varThrowVehDoors (iTempVar)     ////MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
                IF iTempVar = 1
                    GET_CLEO_SHARED_VAR varOnmission (iTempVar) // flag_player_on_mission ||0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
                    //IF NOT iTempVar = 2   //car chase     //Fix crash

                        GET_CLEO_SHARED_VAR varThrowFix (is_near_car)
                        IF CLEO_CALL get_object_near_char 0 player_actor 20.0 (obj)
  
                            IF DOES_OBJECT_EXIST obj    //secure check
 
                                IF CLEO_CALL get_object_offset_indicator 0 obj (x[0] y[0] z[0])   

                                    //DRAW_CORONA (x[0] y[0] z[0]) (1.0) (CORONATYPE_SHINYSTAR, FLARETYPE_NONE) (255 0 0)
                                    IF DOES_OBJECT_EXIST obj
                                    AND NOT is_near_car = 1
                                    AND GOSUB is_not_player_playing_car_anim
                                    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("yank_object")
                                        GOSUB draw_indicator_object
                                        // L1 
                                        IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                        AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                        AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                        AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~

                                            IF NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2    // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  

                                                GOSUB process_push_object
                                                WAIT 1000
                                                WHILE IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2        // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                                    WAIT 0
                                                ENDWHILE
                                            ENDIF  
                                        ENDIF
                                    //ELSE
                                        //PRINT_FORMATTED_NOW "~r~Can't Throw Objects At The Moment" 1
                                    ENDIF
                                ENDIF
                            ENDIF    
                        ENDIF
                    //ENDIF                         
                ENDIF

                // Throw Vehicle Doors (COMPLETED) - Scripted By MeyvinIsCool
                //IF CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -3.0) (1 0 0 0 0)
                IF IS_CHAR_REALLY_IN_AIR player_actor
                    //in air
                ELSE
                    GET_CLEO_SHARED_VAR varThrowVehDoors (iTempVar)     ////MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
                    IF iTempVar = 1
                        GET_CLEO_SHARED_VAR varOnmission (iTempVar) // flag_player_on_mission ||0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
                        IF NOT iTempVar = 2   //car chase     //Fix crash
                            //on ground                          
                            GET_CLEO_SHARED_VAR varThrowFix (is_near_car)       
                            IF CLEO_CALL getClosestVehicle 0 (iVeh)                             
                                IF DOES_VEHICLE_EXIST iVeh
                                AND IS_CAR_ON_SCREEN iVeh
                                //AND GOSUB is_not_player_playing_car_anim
                                AND NOT IS_CHAR_PLAYING_ANIM player_actor ("yank_object")
                                    GOSUB draw_indicator_vehicles                                                                                                                                                            
                                    // L1 
                                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                    AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                    AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                    AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~

                                        IF NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2    // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                        AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                        AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                        AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  

                                            GOSUB process_push_doors
                                            WHILE IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2        // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                                WAIT 0
                                            ENDWHILE
                                        ENDIF                                                                                                                                                     
                                    ENDIF    
                                //ELSE
                                    //PRINT_FORMATTED_NOW "~r~Can't Throw Objects At The Moment" 1                                                                          
                                ENDIF                                                                                                 
                            ENDIF                              
                        ENDIF

                    ENDIF
                ENDIF

            ENDIF
        ELSE
            // Release files
            WAIT 1000
            GOSUB readVars 
            IF toggleSpiderMod = 0                    
                REMOVE_ANIMATION "mweb"
                REMOVE_ANIMATION "spider"
                REMOVE_AUDIO_STREAM sfx
                USE_TEXT_COMMANDS FALSE
                //WAIT 0
                //REMOVE_TEXTURE_DICTIONARY
                WAIT 50
                //TERMINATE_THIS_CUSTOM_SCRIPT
                GOTO start_check
            ENDIF
        ENDIF        
    ENDIF 
    WAIT 0  
GOTO main_loop

draw_tip_key_command:
    //GET_FIXED_XY_ASPECT_RATIO 120.0 60.0 (x[3] y[3])
    x[3] = 90.00
    y[3] = 56.00
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idTip3 (50.0 400.0) (x[3] y[3]) (255 255 255 200)
    IF IS_PC_USING_JOYPAD
        iTempVar = 703  //~k~~PED_CYCLE_WEAPON_LEFT~
        CLEO_CALL GUI_DrawHelperText 0 (45.0 400.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
    ELSE
        iTempVar = 705  //~h~Q
        CLEO_CALL GUI_DrawHelperText 0 (45.0 400.0) (iTempVar 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)
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

//-+--------------------objects--------------------------
draw_indicator_object:
    IF GOSUB is_spider_hud_enabled

        IF CLEO_CALL get_object_offset_indicator 0 obj (x[0] y[0] z[0])
            GOSUB draw_tip_key_command          //Adds Indication 
        ENDIF
            
        IF NOT IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active   
            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (0.0 0.0 0.0) (x[0] y[0] z[0])
            CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (v1 v2) (x[3] y[3])
            GET_FIXED_XY_ASPECT_RATIO 30.0 30.0 (x[3] y[3])
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idLR (v1 v2) (x[3] y[3]) (255 255 255 200)      
        ENDIF
    ENDIF
RETURN

process_push_object: 
    IF DOES_OBJECT_EXIST obj
    AND GOSUB draw_indicator_object

        SET_OBJECT_DYNAMIC obj TRUE
        SET_OBJECT_MASS obj 800.0
        SET_OBJECT_TURN_MASS obj 800.0
        SET_OBJECT_COLLISION obj FALSE

        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (0.0 0.0 0.0) (x[0] y[0] z[0])
        GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
        SET_CHAR_HEADING player_actor zAngle

        GOSUB REQUEST_Animations
        WAIT 0
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_WITH_FLAGS player_actor ("yank_object" "spider") 63.0 (0 1 1 0) -1 0 1
        GOSUB playWebSound
        WAIT 0
        fDistance = 0.0
        //SET_CHAR_ANIM_SPEED player_actor "yank_object" 1.25

        IF IS_CHAR_PLAYING_ANIM player_actor ("yank_object")
            WHILE IS_CHAR_PLAYING_ANIM player_actor ("yank_object")

                GET_CHAR_ANIM_CURRENT_TIME player_actor ("yank_object") (currentTime)
                IF currentTime >= 0.129 // frame 8/62   //0.061  //frame 4
                        
                    CLEO_CALL setCharViewPointToCamera 0 player_actor
                    SET_CHAR_ANIM_SPEED player_actor "yank_object" 1.1
                    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
                    GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])                    
                    CLEO_CALL linearInterpolation 0 (0.129 0.774 currentTime) (720.0 0.0) (zAngle)    //(360*2+90=810)
                    COS zAngle (x[1])
                    SIN zAngle (y[1])
                    x[1] *= 3.0
                    y[1] *= 3.0
                    x[0] += x[1]
                    y[0] += y[1]
                    z[0] += fDistance
                    fDistance +=@ 0.056
                    SET_OBJECT_COORDINATES obj x[0] y[0] z[0] 

                    CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (x[3] y[3]) (x[2] y[2])
                    CLEO_CALL getActorBonePos 0 player_actor 25 (x[2] y[2] z[2])    //Right hand
                    CONVERT_3D_TO_SCREEN_2D (x[2] y[2] z[2]) TRUE TRUE (v1 v2) (x[2] y[2])
                    CLEO_CALL drawline 0 v1 v2 x[3] y[3] 0.5 (255 255 255 255)
                ENDIF
                IF currentTime >= 0.774     //frame 48/62   //0.788     //frame 52
                    BREAK
                ENDIF
                WAIT 0             
            ENDWHILE    
            SET_OBJECT_DYNAMIC obj TRUE     //To Avoid Object Stuck in Air Bug
            SET_OBJECT_COLLISION obj TRUE    
            IF CLEO_CALL get_target_char_from_char 0 player_actor 35.0 (iPed)   //recicled-var
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iPed (0.0 0.0 0.8) (x[1] y[1] z[1])
                GET_CLEO_SHARED_VAR varHitCount iHitCounter
                iHitCounter += 1 
                SET_CLEO_SHARED_VAR varHitCount iHitCounter                
            ELSE
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 9.0 1.5) (x[1] y[1] z[1])
            ENDIF
            CLEO_CALL setObjVelocityTo 0 obj (x[1] y[1] z[1]) 60.0        
        ENDIF    
        //GET_OBJECT_MODEL obj (iTempVar)
        //PRINT_FORMATTED_NOW "ID:%i" 1000 iTempVar
        WAIT 50
     
        IF DOES_OBJECT_EXIST obj
            MARK_OBJECT_AS_NO_LONGER_NEEDED obj
        ENDIF
    ENDIF
RETURN

//-+--------------------vehicles--------------------------
draw_indicator_vehicles:        
    IF DOES_VEHICLE_EXIST iVeh             
        CLEO_CALL get_side_of_char_on_vehicle 0 player_actor iVeh (randomVal) //1:left|2:right                  
        IF GOSUB is_spider_hud_enabled
            IF CLEO_CALL get_side_of_char_on_vehicle 0 player_actor iVeh (randomVal) //1:left|2:right
                GOSUB draw_tip_key_command          //Adds Indication 
            ENDIF
        ENDIF         
        SWITCH randomVal
            CASE 1  //Left
                iTempVar = 10   //door_lf_dummy
                BREAK
            CASE 2
                iTempVar = 8    //door_rf_dummy
                BREAK
        ENDSWITCH
        IF NOT randomVal = 0
            CLEO_CALL get_vehicle_dummy_offset 0 iVeh iTempVar (x[1] y[1] z[1])
            GET_OFFSET_FROM_CAR_IN_WORLD_COORDS iVeh (x[1] y[1] z[1]) (x[0] y[0] z[0])

            CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (v1 v2) (x[1] y[1])
            GET_FIXED_XY_ASPECT_RATIO 30.0 30.0 (x[1] y[1])
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idLR (v1 v2) (x[1] y[1]) (255 255 255 200)       
            is_near_car = 1
            SET_CLEO_SHARED_VAR varThrowFix (is_near_car)       
        ENDIF                 
        //PRINT_FORMATTED_NOW "side:%i" 1 randomVal  //debug
        //PRINT_FORMATTED_NOW "x:%.1f y:%1f z:%1f" 1 x[1] y[1] z[1]  
    ENDIF    
RETURN

process_push_doors:
    IF DOES_VEHICLE_EXIST iVeh
    AND CLEO_CALL get_side_of_char_on_vehicle 0 player_actor iVeh (randomVal)      // This Will Fix The Crash As It Only Going To Active Only If This Gets True

        IF CLEO_CALL get_dummy_vehicle 0 iVeh iTempVar (obj)
            IF iTempVar = 8 //door_rf_dummy
                POP_CAR_DOOR iVeh 3 FALSE   //3-Front right door (passenger)
            ELSE
                POP_CAR_DOOR iVeh 2 FALSE   //2-Front left door (driver)
            ENDIF
            SET_OBJECT_DYNAMIC obj TRUE
            SET_OBJECT_MASS obj 800.0
            SET_OBJECT_TURN_MASS obj 800.0
            SET_OBJECT_COLLISION obj FALSE

            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (0.0 0.0 0.0) (x[0] y[0] z[0])
            GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (zAngle)
            SET_CHAR_HEADING player_actor zAngle

            GOSUB REQUEST_Animations
            CLEAR_CHAR_TASKS player_actor
            CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
            TASK_PLAY_ANIM_WITH_FLAGS player_actor ("yank_object" "spider") 63.0 (0 1 1 0) -1 0 1
            GOSUB playWebSound
            WAIT 0
            //SET_CHAR_ANIM_SPEED player_actor "yank_object" 1.25
            fDistance = 0.0

            IF IS_CHAR_PLAYING_ANIM player_actor ("yank_object")
                WHILE IS_CHAR_PLAYING_ANIM player_actor ("yank_object")

                    GET_CHAR_ANIM_CURRENT_TIME player_actor ("yank_object") (currentTime)
                    IF currentTime >= 0.129 // frame 8/62   //0.061  //frame 4
                        SET_CHAR_ANIM_SPEED player_actor "yank_object" 1.1
                        
                        CLEO_CALL setCharViewPointToCamera 0 player_actor
                        GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
                        GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])
                        CLEO_CALL linearInterpolation 0 (0.129 0.774 currentTime) (720.0 0.0) (zAngle)    //(360*2+90=810)
                        COS zAngle (x[1])
                        SIN zAngle (y[1])
                        x[1] *= 3.0
                        y[1] *= 3.0
                        x[0] += x[1]
                        y[0] += y[1]
                        z[0] += fDistance
                        //CLEO_CALL setObjectPosSimple 0 obj x[0] y[0] z[0]
                        SET_OBJECT_COORDINATES obj x[0] y[0] z[0]
                        fDistance +=@ 0.056

                        CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (x[3] y[3]) (x[2] y[2])
                        CLEO_CALL getActorBonePos 0 player_actor 25 (x[2] y[2] z[2])    //Right hand
                        CONVERT_3D_TO_SCREEN_2D (x[2] y[2] z[2]) TRUE TRUE (v1 v2) (x[2] y[2])
                        CLEO_CALL drawline 0 v1 v2 x[3] y[3] 0.5 (255 255 255 255)
                    ENDIF
                    IF currentTime >= 0.774     //frame 48/62   //0.788     //frame 52
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE
                SET_OBJECT_COLLISION obj TRUE
                IF CLEO_CALL get_target_char_from_char 0 player_actor 35.0 (iPed)   //recicled-var
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iPed (0.0 0.0 0.8) (x[1] y[1] z[1])
                    GET_CLEO_SHARED_VAR varHitCount iHitCounter
                    iHitCounter += 1 
                    SET_CLEO_SHARED_VAR varHitCount iHitCounter
                ELSE
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 9.0 1.5) (x[1] y[1] z[1])
                ENDIF
                CLEO_CALL setObjVelocityTo 0 obj (x[1] y[1] z[1]) 60.0            
            ENDIF
            //GET_OBJECT_MODEL obj (iTempVar)
            //PRINT_FORMATTED_NOW "ID:%i" 1000 iTempVar
            WAIT 50
            IF DOES_OBJECT_EXIST obj
                MARK_OBJECT_AS_NO_LONGER_NEEDED obj
            ENDIF

        ENDIF

    ENDIF
RETURN
//-+----------------------------------------------------------

//-+-----------------------GET--------------------------
readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
RETURN

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

REQUEST_Animations:
    IF NOT HAS_ANIMATION_LOADED "spider"
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_Animations

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

is_not_player_playing_car_anim:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("c_idle_Z")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_00")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_01")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_02")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_00")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_01")
    AND NOT IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_02")
        RETURN_TRUE
        RETURN
    ENDIF
    RETURN_FALSE
RETURN

//-+----------------------------------------------------------

}
SCRIPT_END
//----CALL-SCM-HELPER
{
//CLEO_CALL get_object_near_char 0 scplayer 60.0 (iNewObj)
get_object_near_char:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMaxDistance //in
    LVAR_INT p i obj iNewObj
    LVAR_FLOAT x[2] y[2] z[2] fDistance v1 v2
    IF DOES_CHAR_EXIST scplayer
        i = 0
        WHILE GET_ANY_OBJECT_NO_SAVE_RECURSIVE i (i obj)
            IF DOES_OBJECT_EXIST obj
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (0.0 0.0 0.0) (x[1] y[1] z[1])
                GET_ACTIVE_CAMERA_COORDINATES (x[0] y[0] z[0])
                GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance) 
                IF fMaxDistance >= fDistance
                    IF IS_OBJECT_ON_SCREEN obj
                    AND IS_LINE_OF_SIGHT_CLEAR (x[0] y[0] z[0]) (x[1] y[1] z[1]) (1 0 0 0 0)   //(isSolid isCar isActor isObject isParticle)

                        CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (x[0] y[0])
                        GET_DISTANCE_BETWEEN_COORDS_2D (339.0 179.0) (v1 v2) (fDistance)

                        IF 30.0 > fDistance
                            iNewObj = obj
                            BREAK
                        ENDIF

                    ENDIF
                ENDIF
            ENDIF
        ENDWHILE
        IF DOES_OBJECT_EXIST iNewObj
            RETURN_TRUE
        ELSE
            RETURN_FALSE
        ENDIF
    ENDIF
CLEO_RETURN 0 iNewObj
}
{
//CLEO_CALL get_object_offset_indicator 0 obj 
get_object_offset_indicator:
    LVAR_INT obj    //in
    LVAR_INT idModel
    LVAR_FLOAT x[3] y[3] z[3]
    IF DOES_OBJECT_EXIST obj
    AND NOT HAS_OBJECT_BEEN_DAMAGED obj  
        //Small    
        IF DOES_OBJECT_HAVE_THIS_MODEL obj 1220     
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1221
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1230
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1438     
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1440     
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1264  
        OR DOES_OBJECT_HAVE_THIS_MODEL obj 1265   
            GET_OBJECT_MODEL obj (idModel)
            GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
            x[1] = (x[1] + 0.5)    //0.45
            z[2] = (z[2] - 0.25)   //0.6            
            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.0 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2]
            RETURN_TRUE
        ELSE     
            IF DOES_OBJECT_HAVE_THIS_MODEL obj 1227
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1285
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1286
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1287
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1288
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1289
            OR DOES_OBJECT_HAVE_THIS_MODEL obj 1271
                GET_OBJECT_MODEL obj (idModel)
                GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
                x[1] = (x[1] + 0.5)    //0.45
                z[2] = (z[2] - 0.25)   //0.6            
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.0 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2] 
                RETURN_TRUE
            ELSE                             
                IF DOES_OBJECT_HAVE_THIS_MODEL obj 1258
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 3798
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 3799
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 1300
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 1344     //Project Urbanize
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 1345     //Project Urbanize
                OR DOES_OBJECT_HAVE_THIS_MODEL obj 1372     //Project Urbanize
                    GET_OBJECT_MODEL obj (idModel)
                    GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
                    x[1] = (x[1] + 0.5)    //0.45
                    z[2] = (z[2] - 0.25)   //0.6            
                    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.0 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2] 
                    RETURN_TRUE  
                ELSE       
                    IF DOES_OBJECT_HAVE_THIS_MODEL obj 1439     //Project Urbanize
                    OR DOES_OBJECT_HAVE_THIS_MODEL obj 3035     //Project Urbanize
                    OR DOES_OBJECT_HAVE_THIS_MODEL obj 1331     //Project Urbanize
                    OR DOES_OBJECT_HAVE_THIS_MODEL obj 1359     //Project Urbanize
                    OR DOES_OBJECT_HAVE_THIS_MODEL obj 1334     //Project Urbanize
                    OR DOES_OBJECT_HAVE_THIS_MODEL obj 1211     //Fire Hydrant
                        GET_OBJECT_MODEL obj (idModel)
                        GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
                        x[1] = (x[1] + 0.5)    //0.45
                        z[2] = (z[2] - 0.25)   //0.6            
                        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS obj (x[1] 0.0 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2] 
                        RETURN_TRUE  
                    ELSE                       
                        RETURN_FALSE
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0 x[0] y[0] z[0]
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
//CLEO_CALL drawline 0 x y x1 y1 fThickness r g b a
drawline:
    LVAR_FLOAT x y x1 y1 fThickness //in
    LVAR_INT r g b a    //in
    LVAR_FLOAT fDistance zAngle
    GET_DISTANCE_BETWEEN_COORDS_2D x y x1 y1 (fDistance)
    x1 -= x
    y1 -= y
    GET_HEADING_FROM_VECTOR_2D x1 y1 (zAngle)
    zAngle += 90.0
    x1 /= 2.0
    y1 /= 2.0
    x += x1
    y += y1
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE_WITH_ROTATION 666 x y fDistance fThickness zAngle r g b a
CLEO_RETURN 0
}
{
//CLEO_CALL setObjVelocityTo 0 iObject (x y z) Amp
setObjVelocityTo:
    LVAR_INT iObj   //in
    LVAR_FLOAT xIn yIn zIn  //in
    LVAR_FLOAT iAmplitude   //in
    LVAR_FLOAT x[2] y[2] z[2] fDistance
    IF DOES_OBJECT_EXIST iObj
        x[1] = xIn
        y[1] = yIn
        z[1] = zIn
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
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
        SET_OBJECT_VELOCITY iObj x[1] y[1] z[1]
        WAIT 0
        SET_OBJECT_VELOCITY iObj x[1] y[1] z[1]
    ENDIF
CLEO_RETURN 0
}
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
//CLEO_CALL getActorBonePos 0 /*actor*/actor /*bone*/0 /*store_to*/var1 var2 var3 
getActorBonePos:
    LVAR_INT scplayer iBone  //in
    LVAR_FLOAT fx fy fz
    LVAR_INT var5 //var6
    GET_PED_POINTER scplayer (scplayer)
    GET_VAR_POINTER (fx) (var5)
    CALL_METHOD 0x5E4280 /*struct*/scplayer /*params*/3 /*pop*/0 /*bUnk*/1 /*nBone*/iBone /*pPoint*/ var5
    /// 0x5E4280 - RwV3d *__thiscall CPed__getBonePosition(RwV3d *vPosition int iBoneID, bool bIncludeAnim)
    /// https://wiki.multitheftauto.com/wiki/GetPedBonePosition
CLEO_RETURN 0 fx fy fz
}
{
//CLEO_CALL get_target_char_from_char 0 scplayer fMaxDistance (char)
get_target_char_from_char:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMaxDistance  //in
    LVAR_INT p i char iNewPed 
    LVAR_FLOAT xScreenSize x[2] y[2] z[2] fDistance v1 v2
    LVAR_INT pedType
    IF DOES_CHAR_EXIST scplayer
        xScreenSize = 50.0
        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i char)
            IF DOES_CHAR_EXIST char
            AND NOT IS_CHAR_DEAD char
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR scplayer char
                IF NOT IS_CHAR_IN_ANY_CAR char
                AND NOT IS_CHAR_ON_ANY_BIKE char
                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE char
                    //GET_PED_TYPE char (pedType)
                    //IF NOT pedType = PEDTYPE_COP
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS char (0.0 0.0 0.0) (x[1] y[1] z[1])
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer (0.0 0.0 0.0) (x[0] y[0] z[0])
                        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance) 
                        IF fMaxDistance > fDistance
                            IF IS_CHAR_ON_SCREEN char
                            AND IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0)   //(isSolid isCar isActor isObject isParticle)
                                CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (x[0] y[0])
                                GET_DISTANCE_BETWEEN_COORDS_2D (339.0 179.0) (v1 v2) (fDistance)
                                IF xScreenSize >= fDistance
                                    xScreenSize = fDistance
                                    iNewPed = char
                                ENDIF
                            ENDIF
                        ENDIF
                    //ENDIF
                ENDIF
            ENDIF
        ENDWHILE
        IF DOES_CHAR_EXIST iNewPed
            RETURN_TRUE
        ELSE
            RETURN_FALSE
        ENDIF
    ENDIF
CLEO_RETURN 0 iNewPed
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
//CLEO_CALL getClosestVehicle 0 (veh)
getClosestVehicle:
    LVAR_INT p i veh player_actor iNewVeh veh_class
    LVAR_FLOAT fRadius x[2] y[2] z[2] fDistance
    GET_PLAYER_CHAR 0 player_actor
    fRadius = 20.0
    i = 0
    WHILE GET_ANY_CAR_NO_SAVE_RECURSIVE i (i veh)
        IF DOES_VEHICLE_EXIST veh
        AND IS_CAR_ON_SCREEN veh
            GET_VEHICLE_CLASS veh (veh_class)
            IF 7 > veh_class
            AND veh_class >= 0
                IF LOCATE_CHAR_ANY_MEANS_CAR_3D player_actor veh 30.0 30.0 30.0 FALSE
                    GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (0.0 0.0 0.0) (x[1] y[1] z[1])
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
                    GET_DISTANCE_BETWEEN_COORDS_2D (x[0] y[0]) (x[1] y[1]) (fDistance)
                    IF fRadius > fDistance
                        fRadius = fDistance
                        iNewVeh = veh
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDWHILE
    IF DOES_VEHICLE_EXIST iNewVeh
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0 iNewVeh
}
{
//CLEO_CALL get_vehicle_dummy_offset 0 veh id (x y z)
get_vehicle_dummy_offset:
    LVAR_INT hVehicle id //in
    LVAR_INT pVehicle pRpClump pRwFrame 
    LVAR_FLOAT x y z 
    IF DOES_VEHICLE_EXIST hVehicle
        GET_VEHICLE_POINTER hVehicle (pVehicle)
        pVehicle += 0x18
        READ_MEMORY pVehicle 4 0 (pRpClump) 
        //RwFrame *__cdecl CClumpModelInfo__GetFrameFromName(RpClump *clump, char *name)
        IF id = 8   //door_rf_dummy
            CALL_FUNCTION_RETURN 0x004C5400 2 2 "door_rf_dummy" pRpClump (pRwFrame)
        ELSE
            //door_lf_dummy=10
            CALL_FUNCTION_RETURN 0x004C5400 2 2 "door_lf_dummy" pRpClump (pRwFrame)
        ENDIF
        IF NOT pRwFrame = 0 //has this dummy
            pRwFrame += 0x40
            READ_MEMORY pRwFrame 4 0 (x)
            pRwFrame += 4
            READ_MEMORY pRwFrame 4 0 (y)
            pRwFrame += 4  
            READ_MEMORY pRwFrame 4 0 (z)
            //y -= 0.5    //fix on center of doors dummy's
        ENDIF
    ENDIF
CLEO_RETURN 0 x y z
}
{
//CLEO_CALL get_side_of_char_on_vehicle 0 scplayer veh (side_veh) //1:left|2:right
get_side_of_char_on_vehicle:
    CONST_INT left_side 1
    CONST_INT right_side 2
    LVAR_INT scplayer veh //in
    LVAR_INT idModel side_veh
    LVAR_FLOAT x[4] y[4] z[4]
    LVAR_FLOAT xCoord[2]
    IF DOES_VEHICLE_EXIST veh
        GET_CAR_MODEL veh (idModel)
        GET_MODEL_DIMENSIONS idModel (x[0] y[0] z[0]) (x[1] y[1] z[1])
        x[0] = x[1] * -0.63 //left
        x[1] *= 0.63        //right
        xCoord[0] = x[0]    //left
        xCoord[0] *= 20.0
        xCoord[1] = x[1]    //Right
        xCoord[1] *= 20.0
        y[0] = y[1] * 1.1   //front
        y[1] *= 1.0       
        y[1] -= 6.0         //back
        //z[0]
        z[1] *= 1.0    //up
        z[1] += 10.0
        GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (x[1] y[0] z[0]) (x[2] y[2] z[2])       //right-front
        GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (xCoord[1] y[1] z[1]) (x[3] y[3] z[3])  //right-back-up
        //DRAW_CORONA x[2] y[2] z[2] 0.25 CORONATYPE_SHINYSTAR FLARETYPE_NONE 255 0 0
        //DRAW_CORONA x[3] y[3] z[3] 0.25 CORONATYPE_SHINYSTAR FLARETYPE_NONE 0 255 0
        IF IS_CHAR_IN_AREA_3D scplayer x[2] y[2] z[2] x[3] y[3] z[3] FALSE
            side_veh = right_side
        ELSE
            GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (x[0] y[0] z[0]) (x[2] y[2] z[2])       //left-front
            GET_OFFSET_FROM_CAR_IN_WORLD_COORDS veh (xCoord[0] y[1] z[1]) (x[3] y[3] z[3])  //left-back-up
            IF IS_CHAR_IN_AREA_3D scplayer x[2] y[2] z[2] x[3] y[3] z[3] FALSE
                side_veh = left_side
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0 side_veh  //1:left|2:right
}
{
//CLEO_CALL get_dummy_vehicle 0 iVeh idDummy (iObj)
get_dummy_vehicle:
    LVAR_INT hVehicle id   //in
    LVAR_INT pVehicle hObject pObject 
    LVAR_FLOAT x y z
    LVAR_FLOAT vel_x vel_y vel_z
    LVAR_INT pPos pVel
    IF DOES_VEHICLE_EXIST hVehicle
        FIX_CAR_DOOR hVehicle 3     //3-Front right door (passenger)
        FIX_CAR_DOOR hVehicle 2     //2-Front left door (driver)
        GET_VEHICLE_POINTER hVehicle (pVehicle)
        //CObject *__thiscall CAutomobile::SpawnFlyingComponent(CAutomobile *car, int nodeId, int mode)
        CALL_METHOD_RETURN 0x6A8580 pVehicle 2 0 (0 id)(pObject)
        GET_OBJECT_REF pObject (hObject)
    ENDIF
    IF DOES_OBJECT_EXIST hObject
        RETURN_TRUE
    ELSE
        hObject = -1
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0 hObject
/*
nodeId:
Door_rf_dummy, 8
Door_lf_dummy, 10
mode:
0 - big sphere (internal id 375)
1 - hood flying (internal id 379)
2 - nothing (internal id 374)
3 - jumps (internal id 377)
4 - jumps (internal id 378)
5 - nothing (internal id 376)
*/
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

CONST_INT varBuildingZip        18    //sp_mlb           ||1= Activated     || 0= Deactivated
CONST_INT varBuildingZipFlag    19    //sp_mlb           ||1= Activated     || 0= Deactivated

CONST_INT varAlternativeSwing   20    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varSwingBuilding      21    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFixGround          22    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varMouseControl       23    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varAimSetup           24    // 0:Manual Aim || 1:Auto Aim //sp_dw
CONST_INT varPlayerCanDrive     25    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFriendlyN          26    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varThrowVehDoors      27    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varThrowFix           28    //sp_thob          ||1= Activated     || 0= Deactivated

CONST_INT varLevelChar          30    //sp_lvl    || Level
CONST_INT varStatusLevelChar    31    //If value >0 automatically will add that number to Experience Points (Max Reward +2500)

CONST_INT varIdWebWeapon        32    //sp_mm     || 1-8 weap
CONST_INT varWeapAmmo           33    //sp_wep    ||store current weap ammo
CONST_INT varIdPowers           34    //MSpiderJ16Dv7 - sp_po     ||Id powers 1 - 12
CONST_INT varPowersProgress     35    //sp_po     || current power progress
CONST_INT varHitCount           36    //sp_hit    || hitcounting
CONST_INT varHitCountFlag       37    //sp_hit    || hitcounting  
CONST_INT varReservoirInactive  38    //sp_res    || disable reservoirs 
CONST_INT varIronArmsCondition  39    //sp_po    ||1= Activated     || 0= Deactivated

CONST_INT varInMenu             40    //1= On Menu       || 0= Menu Closed
CONST_INT varMapLegendLandMark  43    //Show: 1= enable   || 0= disable
CONST_INT varMapLegendBackPack  44    //Show: 1= enable   || 0= disable

CONST_INT varDrugDealProgress   45    //for stadistics ||MSpiderJ16Dv7
CONST_INT varAssaultProgress    46    //for stadistics ||MSpiderJ16Dv7 
CONST_INT varMuggingProgress    47    //for stadistics ||MSpiderJ16Dv7 (Due to messy global shared vars , I have to add it here)

CONST_INT varAudioActive     	49    // 0:OFF || 1:ON  ||global var to check -spech- audio playing

CONST_INT varSkill1             50    //sp_dw    ||1= Activated     || 0= Deactivated
CONST_INT varSkill2             51    //sp_ev    ||1= Activated     || 0= Deactivated
CONST_INT varSkill2a            52    //sp_ev    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3             53    //sp_me    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3a            54    //sp_ml    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3b            55    //sp_me    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c            56    //sp_main  ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c1           57    //sp_mb    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c2           58    //sp_mb    ||1= Activated     || 0= Deactivated

//Additional Skills
CONST_INT varSkill1a            59    //sp_dw    ||1= Activated     || 0= Deactivated

CONST_INT varFocusCount         70    //sp_hit    || focus bar
CONST_INT varUseFocus           71    //sp_hit    || focus bar