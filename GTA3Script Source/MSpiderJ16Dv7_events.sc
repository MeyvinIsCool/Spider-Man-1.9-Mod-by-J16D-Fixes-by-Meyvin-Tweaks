// by J16D
// start of Events included:
//   -Car chase (sp_cc.cs)
//   -Backpack  (m_bp.cs)
//   -Thug Hideouts (m_th.cs)
//   -Street Crimes (m_w.cs)
//   -Car chase (m_dd.cs)
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
CONST_INT time_update 30000//ms   
CONST_INT time_delay 60000  //ms    
CONST_INT time_delay_after_mission 20000 //ms
CONST_INT time_mission_randomize 2500 //ms 

SCRIPT_START
{
SCRIPT_NAME m_ev

LVAR_INT player_actor toggleSpiderMod iRandomMission
LVAR_INT idZone[2] i iEventBlip iSfx[1]
LVAR_FLOAT x[2] y[2] z[2] randm_x randm_y randm_z v1 v2 v3 v4 fDistance fVolume
LVAR_INT iRandomVal iTempVar iDistance flag_player_on_mission isInMainMenu
LVAR_INT crimealert

GET_PLAYER_CHAR 0 player_actor
flag_player_on_mission = 0
SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON

start:
WHILE TRUE
    GOSUB readVars
    IF toggleSpiderMod = 1  //TRUE
        BREAK
    ENDIF
    WAIT 0
ENDWHILE

IF DOES_FILE_EXIST "CLEO\SpiderJ16D\config.ini"
    READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "config" "LANG" (iTempVar)   // 0:SPA ||1:ENG
ELSE
    iTempVar = 1 //(0:spanish|1:English)
ENDIF
CLEO_CALL storeLanguage 0 iTempVar      

//submissions
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\m_bp.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\m_bp.cs"       // backpack
    ENDIF
    GOSUB readVars
    IF NOT flag_player_on_mission = 3   //3:thugs hidouts
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\m_th.cs"
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\m_th.cs"       // Thug Hideouts
        ENDIF
    ENDIF
    IF NOT flag_player_on_mission = 4   //4:street crimes
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\m_w.cs"
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\m_w.cs"       // Street Crimes
        ENDIF       
    ENDIF   

timera = 0
iRandomMission = 0

GOSUB generate_random_coords
GOSUB generate_random_coords_2

main_loop:
    IF IS_PLAYER_PLAYING 0
        IF NOT IS_ON_MISSION
            GOSUB readVars
            IF toggleSpiderMod = 1  //TRUE
                IF isInMainMenu = 0     //1:true 0: false

                    IF flag_player_on_mission = 0               
                        IF timerb >= time_mission_randomize               

                            GENERATE_RANDOM_INT_IN_RANGE 1 3 iRandomMission  

                            IF iRandomMission = 1
                            AND flag_player_on_mission = 0
                                GOSUB car_chase_event
                            ENDIF

                            IF iRandomMission = 2
                            AND flag_player_on_mission = 0
                                GOSUB drug_deal_event         
                            ENDIF                

                        ENDIF                                           
                    ELSE
                        timerb = 0
                    ENDIF

                    //PRINT_FORMATTED_NOW "Mission %i TimerB %i" 1 iRandomMission timerb    //debug

                ENDIF
            ELSE
                GET_AUDIO_STREAM_STATE iSfx[0] (iTempVar)
                IF iTempVar = 1     //playing
                    SET_AUDIO_STREAM_STATE iSfx[0] 0    //Stop
                ENDIF
                REMOVE_AUDIO_STREAM iSfx[0]
                IF DOES_BLIP_EXIST iEventBlip
                    REMOVE_BLIP iEventBlip
                ENDIF
                WAIT 50
                GOTO start
            ENDIF
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

//---+--------------- CAR CHASE EVENT
car_chase_event:
    IF timera > time_update
        GOSUB update_zone_events
        //PRINT_FORMATTED_NOW "updated: %i" 1000 idZone[1]    //debug
        WAIT 900
    ELSE
        GOSUB readVars
        IF flag_player_on_mission = 0   //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2    
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
            CLEO_CALL get_info_zone_id 0 x[0] y[0] z[0] (idZone[0])
            IF idZone[1] = idZone[0]
                CLEO_CALL save_coords_car_chase_event 0 x[1] y[1] z[1]
                timera = 0
                ADD_SPRITE_BLIP_FOR_COORD x[1] y[1] z[1] RADAR_SPRITE_ENEMYATTACK (iEventBlip)
                GOSUB play_sfx_start_event_alert_cc
                crimealert = 0
                SET_CLEO_SHARED_VAR varCrimeAlert crimealert
                WAIT 0                
                crimealert = 1
                SET_CLEO_SHARED_VAR varCrimeAlert crimealert
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
                    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 9 0 801 808    //{id} {mission_id} {text1_id} {text2_id}
                ENDIF                

                WHILE idZone[1] = idZone[0]
                    timerb = 0
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
                    CLEO_CALL get_info_zone_id 0 x[0] y[0] z[0] (idZone[0])
                    //PRINT_FORMATTED_NOW "zone: %i = %i" 1 idZone[0] idZone[1]  //debug

                    IF NOT DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_3d.cs"
                        IF IS_POINT_ON_SCREEN x[1] y[1] z[1] 300.0
                            GOSUB draw_on_screen_distance_coords
                        ENDIF
                    ENDIF

                    GOSUB readVars
                    IF flag_player_on_mission = 0   //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[1] y[1] z[1] 150.0
                            IF DOES_BLIP_EXIST iEventBlip
                                REMOVE_BLIP iEventBlip
                            ENDIF
                            //start mission
                            flag_player_on_mission = 2  //2:car chase
                            SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON
                            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_cc.cs"
                                STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_cc.cs" x[1] y[1] z[1] // car_chase_mission
                            ENDIF                          
                            WAIT 1000
                            WHILE flag_player_on_mission > 0
                                GOSUB readVars
                                //PRINT_FORMATTED_NOW "mission: %i" 1 flag_player_on_mission
                                WAIT 0
                            ENDWHILE

                            timera = 0
                            WHILE time_delay_after_mission > timera
                                WAIT 0
                            ENDWHILE
                            GOTO reset_location
                        ENDIF
                    ENDIF
                    IF isInMainMenu = 1     //1:true 0: false
                    OR toggleSpiderMod = 0  //FALSE
                        BREAK
                    ENDIF
                    IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[1] y[1] z[1] 300.0
                        BREAK
                    ENDIF
                    IF timera > time_delay
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE

                reset_location:
                GET_AUDIO_STREAM_STATE iSfx[0] (iTempVar)
                IF iTempVar = 1     //playing
                    SET_AUDIO_STREAM_STATE iSfx[0] 0    //Stop
                ENDIF
                REMOVE_AUDIO_STREAM iSfx[0]
                IF DOES_BLIP_EXIST iEventBlip
                    REMOVE_BLIP iEventBlip
                ENDIF
                GOSUB update_zone_events

            //ELSE
                //PRINT_FORMATTED_NOW "zone: %i not equal %i" 1 idZone[0] idZone[1]  //debug
            ENDIF
        ELSE
            timera = 0
        ENDIF
    ENDIF
RETURN 

//---+--------------- DRUG DEAL EVENT
drug_deal_event:
    IF timera > time_update
        GOSUB update_zone_events_2
        //PRINT_FORMATTED_NOW "updated: %i" 1000 idZone[1]    //debug
        WAIT 900
    ELSE
        GOSUB readVars
        IF flag_player_on_mission = 0   //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2    
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
            CLEO_CALL get_info_zone_id 0 x[0] y[0] z[0] (idZone[0])            
            IF idZone[1] = idZone[0]
                CLEO_CALL save_coords_drug_deal_event 0 x[0] y[0] z[0]
                timera = 0
                ADD_SPRITE_BLIP_FOR_COORD x[1] y[1] z[1] RADAR_SPRITE_ENEMYATTACK (iEventBlip)
                GOSUB play_sfx_start_event_alert_dd
                crimealert = 0
                SET_CLEO_SHARED_VAR varCrimeAlert crimealert
                WAIT 0                
                crimealert = 1
                SET_CLEO_SHARED_VAR varCrimeAlert crimealert
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
                    STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 9 0 804 808    //{id} {mission_id} {text1_id} {text2_id}
                ENDIF                
            
                WHILE idZone[1] = idZone[0]
                    timerb = 0
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
                    CLEO_CALL get_info_zone_id 0 x[0] y[0] z[0] (idZone[0])
                    //PRINT_FORMATTED_NOW "zone: %i = %i" 1 idZone[0] idZone[1]  //debug

                    IF NOT DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_3d.cs"
                        IF IS_POINT_ON_SCREEN x[1] y[1] z[1] 300.0
                            GOSUB draw_on_screen_distance_coords
                        ENDIF
                    ENDIF

                    GOSUB readVars
                    IF flag_player_on_mission = 0   //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[1] y[1] z[1] 50.0   //10.0
                            IF DOES_BLIP_EXIST iEventBlip
                                REMOVE_BLIP iEventBlip
                            ENDIF
                            //start mission
                            flag_player_on_mission = 5  //5:drug deal
                            SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON
                            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\m_dd.cs"
                                STREAM_CUSTOM_SCRIPT "SpiderJ16D\m_dd.cs" x[1] y[1] z[1] // drug_deal_mission
                            ENDIF                          
                            WAIT 1000
                            WHILE flag_player_on_mission > 0
                                GOSUB readVars
                                //PRINT_FORMATTED_NOW "mission: %i" 1 flag_player_on_mission
                                WAIT 0
                            ENDWHILE

                            timera = 0
                            WHILE time_delay_after_mission > timera
                                WAIT 0
                            ENDWHILE
                            GOTO reset_location_2
                        ENDIF
                    ENDIF
                    IF isInMainMenu = 1     //1:true 0: false
                    OR toggleSpiderMod = 0  //FALSE
                        BREAK
                    ENDIF
                    IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[1] y[1] z[1] 300.0
                        BREAK
                    ENDIF
                    IF timera > time_delay
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE

                reset_location_2:
                GET_AUDIO_STREAM_STATE iSfx[0] (iTempVar)
                IF iTempVar = 1     //playing
                    SET_AUDIO_STREAM_STATE iSfx[0] 0    //Stop
                ENDIF
                REMOVE_AUDIO_STREAM iSfx[0]
                IF DOES_BLIP_EXIST iEventBlip
                    REMOVE_BLIP iEventBlip
                ENDIF
                GOSUB update_zone_events_2

            ELSE
                //PRINT_FORMATTED_NOW "zone: %i not equal %i" 1 idZone[0] idZone[1]  //debug
            ENDIF
        ELSE
            timera = 0
        ENDIF
    ENDIF
RETURN

draw_on_screen_distance_coords:
    CONVERT_3D_TO_SCREEN_2D x[1] y[1] z[1] TRUE TRUE (v1 v2) (v3 v3)
    GET_DISTANCE_BETWEEN_COORDS_3D x[0] y[0] z[0] x[1] y[1] z[1] (fDistance)
    iDistance =# fDistance
    GOSUB GUI_TextFormat_Text
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iDistance    //~1~ m
RETURN

GUI_TextFormat_Text:
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_SCALE 0.14 0.65
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

update_zone_events:
    GOSUB generate_random_coords
    CLEO_CALL get_info_zone_id 0 x[1] y[1] z[1] (idZone[1])
    timera = 0
RETURN

generate_random_coords:
    //SF01, 3, -3000.0, -742.306, -500.0, -1270.53, 1530.24, 500.0, 2, UNUSED
    GENERATE_RANDOM_FLOAT_IN_RANGE -3000.0 -1270.53 (randm_x)
    GENERATE_RANDOM_FLOAT_IN_RANGE -742.306 1530.24 (randm_y)
    GENERATE_RANDOM_FLOAT_IN_RANGE -500.000 500.00 (randm_z)
    GET_CLOSEST_CAR_NODE randm_x randm_y randm_z (x[1] y[1] z[1])
    //GET_CLOSEST_CAR_NODE_WITH_HEADING randm_x randm_y randm_z (x[1] y[1] z[1]) (v4)
    //GET_NTH_CLOSEST_CAR_NODE (randm_x randm_y randm_z) 2 (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_DISTANCE_BETWEEN_COORDS_3D x[0] y[0] z[0] x[1] y[1] z[1] (fDistance)
    IF 200.0 > fDistance
    OR fDistance > 300.0
        WAIT 0
        GOTO generate_random_coords
    ENDIF
RETURN

update_zone_events_2:
    GOSUB generate_random_coords_2
    CLEO_CALL get_info_zone_id 0 x[1] y[1] z[1] (idZone[1])
    timera = 0
RETURN

generate_random_coords_2:
    //SF01, 3, -3000.0, -742.306, -500.0, -1270.53, 1530.24, 500.0, 2, UNUSED
    GENERATE_RANDOM_FLOAT_IN_RANGE -2808.0754 -1874.1842 (randm_x)
    GENERATE_RANDOM_FLOAT_IN_RANGE -335.2986 1113.9125 (randm_y)
    GENERATE_RANDOM_FLOAT_IN_RANGE -500.000 500.00 (randm_z)
    GET_CLOSEST_CAR_NODE randm_x randm_y randm_z (x[1] y[1] z[1])    
    //CLEO_CALL get_map_coords 0 (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_DISTANCE_BETWEEN_COORDS_3D x[0] y[0] z[0] x[1] y[1] z[1] (fDistance)

    IF 200.0 > fDistance
    OR fDistance > 300.0
        WAIT 0
        GOTO generate_random_coords_2
    ENDIF
RETURN

play_sfx_start_event_alert_cc:
    REMOVE_AUDIO_STREAM iSfx[0]
    GENERATE_RANDOM_INT_IN_RANGE 0 3 (iRandomVal)   // 0-2
    CLEO_CALL getLanguage 0 (iTempVar)
    IF iTempVar = 1    //(0:spanish|1:English)
        SWITCH iRandomVal
            CASE 0
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\cc_pa_eng1.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\cc_pa_eng1.mp3" (iSfx[0])
                    SET_AUDIO_STREAM_STATE iSfx[0] 1
                ENDIF
                BREAK
            CASE 1
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\cc_pa_eng2.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\cc_pa_eng2.mp3" (iSfx[0])
                    SET_AUDIO_STREAM_STATE iSfx[0] 1
                ENDIF
                BREAK
            CASE 2
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\cc_pa_eng3.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\cc_pa_eng3.mp3" (iSfx[0])
                    SET_AUDIO_STREAM_STATE iSfx[0] 1
                ENDIF
                BREAK
        ENDSWITCH
    ELSE    //SPANISH
        SWITCH iRandomVal   
            CASE 0
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\cc_pa_esp1.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\cc_pa_esp1.mp3" (iSfx[0])
                    SET_AUDIO_STREAM_STATE iSfx[0] 1
                ENDIF
                BREAK
            CASE 1
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\cc_pa_esp2.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\cc_pa_esp2.mp3" (iSfx[0])
                    SET_AUDIO_STREAM_STATE iSfx[0] 1
                ENDIF
                BREAK
            CASE 2
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\cc_pa_esp3.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\cc_pa_esp3.mp3" (iSfx[0])
                    SET_AUDIO_STREAM_STATE iSfx[0] 1
                ENDIF
                BREAK
        ENDSWITCH
    ENDIF
RETURN

play_sfx_start_event_alert_dd:
    REMOVE_AUDIO_STREAM iSfx[0]
    //GENERATE_RANDOM_INT_IN_RANGE 0 3 (iRandomVal)   // 0-2
    iRandomVal = 0
    CLEO_CALL getLanguage 0 (iTempVar)
    IF iTempVar = 1    //(0:spanish|1:English)
        SWITCH iRandomVal
            CASE 0
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\dd_pa_eng1.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dd_pa_eng1.mp3" (iSfx[0])
                    SET_AUDIO_STREAM_STATE iSfx[0] 1
                ENDIF
                BREAK
        ENDSWITCH
    ELSE    //SPANISH
        SWITCH iRandomVal   
            CASE 0
                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\dd_pa_eng1.mp3"
                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\dd_pa_eng1.mp3" (iSfx[0])
                    SET_AUDIO_STREAM_STATE iSfx[0] 1              
                ENDIF
                BREAK
        ENDSWITCH
    ENDIF
RETURN
//-------------------
}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL get_info_zone_id 0 x y z (idZone)
get_info_zone_id:
    LVAR_FLOAT x y z //in
    LVAR_INT idZone
    LVAR_TEXT_LABEL zone
    GET_NAME_OF_INFO_ZONE x y z (zone)
    //GET_NAME_OF_ZONE x[0] y[0] z[0] (zone) 
    IF IS_STRING_EQUAL "SFDWT1" $zone 6 FALSE ""
    OR IS_STRING_EQUAL "SFDWT2" $zone 6 FALSE ""
    OR IS_STRING_EQUAL "SFDWT3" $zone 6 FALSE ""
    OR IS_STRING_EQUAL "SFDWT5" $zone 6 FALSE ""
    OR IS_STRING_EQUAL "FINA" $zone 4 FALSE ""
    OR IS_STRING_EQUAL "ESPN2" $zone 5 FALSE ""
    OR IS_STRING_EQUAL "ESPN3" $zone 5 FALSE ""
        idZone = 1
    ELSE
        IF IS_STRING_EQUAL "SFDWT4" $zone 6 FALSE ""
        OR IS_STRING_EQUAL "ESPE1" $zone 5 FALSE ""
        OR IS_STRING_EQUAL "ESPE2" $zone 5 FALSE ""
        OR IS_STRING_EQUAL "ESPE3" $zone 5 FALSE ""
            idZone = 2
        ELSE
            IF IS_STRING_EQUAL "ESPN1" $zone 5 FALSE ""
            OR IS_STRING_EQUAL "JUNIHO" $zone 6 FALSE ""
            OR IS_STRING_EQUAL "JUNIHI" $zone 6 FALSE ""
            OR IS_STRING_EQUAL "CALT" $zone 4 FALSE ""
            OR IS_STRING_EQUAL "CHINA" $zone 5 FALSE ""
                idZone = 3
            ELSE
                IF IS_STRING_EQUAL "BATTP" $zone 5 FALSE ""
                OR IS_STRING_EQUAL "PARA" $zone 4 FALSE ""
                OR IS_STRING_EQUAL "BAYV" $zone 4 FALSE ""
                OR IS_STRING_EQUAL "CIVI" $zone 4 FALSE ""
                    idZone = 4
                ELSE
                    IF IS_STRING_EQUAL "SFDWT6" $zone 6 FALSE ""
                    OR IS_STRING_EQUAL "WESTP1" $zone 6 FALSE ""
                    OR IS_STRING_EQUAL "WESTP2" $zone 6 FALSE ""
                    OR IS_STRING_EQUAL "WESTP3" $zone 6 FALSE ""
                    OR IS_STRING_EQUAL "THEA1" $zone 5 FALSE ""
                    OR IS_STRING_EQUAL "THEA2" $zone 5 FALSE ""
                    OR IS_STRING_EQUAL "THEA3" $zone 5 FALSE ""
                        idZone = 5
                    ELSE
                        IF IS_STRING_EQUAL "OCEAF1" $zone 6 FALSE ""
                        OR IS_STRING_EQUAL "OCEAF2" $zone 6 FALSE ""
                        OR IS_STRING_EQUAL "OCEAF3" $zone 6 FALSE ""
                        OR IS_STRING_EQUAL "CITYS" $zone 5 FALSE ""
                            idZone = 6
                        ELSE
                            IF IS_STRING_EQUAL "DOH1" $zone 4 FALSE ""
                            OR IS_STRING_EQUAL "DOH2" $zone 4 FALSE ""
                            OR IS_STRING_EQUAL "GARC" $zone 4 FALSE ""
                            OR IS_STRING_EQUAL "HASH" $zone 4 FALSE ""
                            OR IS_STRING_EQUAL "CRANB" $zone 5 FALSE ""
                                idZone = 7
                            ELSE
                                IF IS_STRING_EQUAL "SFAIR1" $zone 6 FALSE ""
                                OR IS_STRING_EQUAL "SFAIR3" $zone 6 FALSE ""
                                OR IS_STRING_EQUAL "EASB1" $zone 5 FALSE ""
                                OR IS_STRING_EQUAL "EASB2" $zone 5 FALSE ""
                                    idZone = 8
                                ELSE
                                    idZone = 0
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0 idZone
}
{
//CLEO_CALL save_coords_car_chase_event 0 x y z
save_coords_car_chase_event:
    LVAR_FLOAT x y z //in
    LVAR_INT iVar
    IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
        GET_LABEL_POINTER bytes32 (iVar)
        STRING_FORMAT (iVar) "%.2f %.2f %.2f" x y z
        WRITE_STRING_TO_INI_FILE $iVar "cleo\SpiderJ16D\config.ini" "other" "ccstart"
    ELSE
        PRINT_FORMATTED_NOW "ERROR coords file not found" 1500
        WAIT 1500
        CLEO_RETURN 0
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL save_coords_car_chase_event 0 x y z
save_coords_drug_deal_event:
    LVAR_FLOAT x y z //in
    LVAR_INT iVar
    IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
        GET_LABEL_POINTER bytes32 (iVar)
        STRING_FORMAT (iVar) "%.2f %.2f %.2f" x y z
        WRITE_STRING_TO_INI_FILE $iVar "cleo\SpiderJ16D\config.ini" "other" "ddstart"
    ELSE
        PRINT_FORMATTED_NOW "ERROR coords file not found" 1500
        WAIT 1500
        CLEO_RETURN 0
    ENDIF
CLEO_RETURN 0
}

{
//CLEO_CALL storeLanguage 0 var
storeLanguage:
    LVAR_INT inVal 
    LVAR_INT pActiveItem
    GET_LABEL_POINTER language_memory_bytes4 pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getLanguage 0 (var)
getLanguage:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER language_memory_bytes4 (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}

language_memory_bytes4:
DUMP
00 00 00 00
ENDDUMP

bytes32:
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 
ENDDUMP

coords_buffer_12bytes:
DUMP
//backpacks
00000000 00000000 00000000  // 12bytes
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
CONST_INT varDrugDealProgress   18    //for stadistics ||MSpiderJ16Dv7

CONST_INT varAlternativeSwing   20    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varSwingBuilding      21    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFixGround          22    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varMouseControl       23    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varAimSetup           24    // 0:Manual Aim || 1:Auto Aim //sp_dw
CONST_INT varPlayerCanDrive     25    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFriendlyN          26    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varThrowVehDoors      27    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated

CONST_INT varLevelChar          30    //sp_lvl    || Level
CONST_INT varStatusLevelChar    31    //If value >0 automatically will add that number to Experience Points (Max Reward +2500)

CONST_INT varIdWebWeapon        32    //sp_mm     || 1-8 weap
CONST_INT varWeapAmmo           33    //sp_wep    ||store current weap ammo
CONST_INT varIdPowers           34    //MSpiderJ16Dv7 - sp_po     ||Id powers 1 - 12
CONST_INT varPowersProgress     35    //sp_po     || current power progress
CONST_INT varHitCount           36    //sp_hit    || hitcounting
CONST_INT varHitCountFlag       37    //sp_hit    || hitcounting  
CONST_INT varReservoirInactive  38    //sp_res    || disable reservoirs 
CONST_INT varCrimeAlert         39

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
