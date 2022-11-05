// by Mr.Genos Cyborg
// Spider-Man Mod for GTA SA c.2022
// Pizza Time Mission With Fixes - Meyvin Tweaks
// Remade Mission by Meyvin Tweaks - GTA3Script
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//Size
CONST_INT BYTE                 1
CONST_INT WORD                 2
CONST_INT DWORD                4
CONST_INT SIZE_RWMEMORY        8
CONST_INT SIZE_VECTOR          12
CONST_INT SIZE_QUAT            16
CONST_INT SIZE_COLPOINT_DATA   44
CONST_INT SIZE_MATRIX          64
//Textures
CONST_INT tCrosshair        60
CONST_INT objCrosshair      61
CONST_INT tPhotoBomb        62
CONST_INT tPBback           63
CONST_INT tPBScrewballNot   64
CONST_INT tPBbar0           65
CONST_INT tPBbar1           66
CONST_INT tPBScrewBallToon  67
CONST_INT tPBbCounter       68
CONST_INT tPBbCounterBlue   69
CONST_INT tPBBackTimer      70
CONST_INT tPBBackScore      71
CONST_INT tPBBackInfo       72
CONST_INT tPBBackScoreB     73
CONST_INT tPBSBack1         74
CONST_INT tPBSBack2         75
CONST_INT tPBSBack3         76
CONST_INT tPBSBack1Active   77
CONST_INT tPBSBack2Active   78
CONST_INT tPBSBack3Active   79
CONST_INT iconSuccess       80

CONST_INT player 0
CONST_INT delay_restart_mission 60000   //20 sec

SCRIPT_START
{
SCRIPT_NAME sp_piz
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor
LVAR_FLOAT sizeX sizeY
LVAR_INT toggleSpiderMod isInMainMenu
LVAR_INT iRandomVal2 iTotalTime cTimerb_A iMinutes iSeconds iTime iExtraTimeScore
LVAR_INT flag_player_on_mission flag_player_hit_counter
LVAR_INT iEventBlip iEventBlip2
LVAR_INT deliver iObj
LVAR_INT cust1 cust2 cust3 cust4 cust5 cust6
LVAR_INT i is_in_interior
LVAR_INT sfx

GET_PLAYER_CHAR 0 player_actor

start:
//Cash Waypoint - Main Mission
ADD_SPRITE_BLIP_FOR_COORD (-1721.506 1354.953 7.186) RADAR_SPRITE_FIRE (iEventBlip) 

WHILE TRUE
    IF IS_PLAYER_PLAYING 0
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE
            IF isInMainMenu = 0     //1:true 0: false
                IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D player_actor -1721.506 1354.953 7.186 1.25 1.25 1.25 TRUE
                    IF flag_player_on_mission = 0
                        REMOVE_BLIP iEventBlip
                        BREAK
                    ELSE    
                        PRINT_FORMATTED_NOW "Finish your current mission first!" 2000
                        WAIT 2000
                    ENDIF
                ENDIF
            ENDIF         
        ELSE
            IF DOES_BLIP_EXIST iEventBlip
                REMOVE_BLIP iEventBlip
            ENDIF
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT            
        ENDIF
    ENDIF
    WAIT 0
ENDWHILE

//start mission
flag_player_on_mission = 1  //3:criminal
SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON
flag_player_hit_counter = 0
SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter        // 0:OFF || 1:ON
WAIT 1
GOSUB loadGeneralFiles

GOSUB sub_lock_player_controls
GOSUB sub_FadeOut_700ms
//CLEO_CALL SetCharPosSimple 0 player_actor (-1713.323975 1359.68843 17.25)
SET_CHAR_COORDINATES_NO_OFFSET player_actor -1702.237061 1368.487305 17.25
SET_CHAR_HEADING player_actor 136.0
GOSUB create_pizzaStack
//TASK_GO_STRAIGHT_TO_COORD player_actor (-1712.651611 1360.4712290 17.25) 4 -2	//walk
TASK_GO_STRAIGHT_TO_COORD player_actor (-1714.191 1358.952 17.25) 4 -2	//walk
CAMERA_RESET_NEW_SCRIPTABLES
CAMERA_RESET_NEW_SCRIPTABLES
CAMERA_PERSIST_TRACK TRUE
CAMERA_PERSIST_POS TRUE
CAMERA_SET_VECTOR_MOVE (-1709.414 1367.774 17.25) (-1711.415 1359.423 17.25) 13000 0
CAMERA_SET_VECTOR_TRACK (-1715.190 1360.011 17.25) (-1715.615 1359.423 17.25) 13000 0
CAMERA_SET_SHAKE_SIMULATION_SIMPLE 1 8500.0 2.0
GOSUB sub_FadeIn_700ms
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 8 0 815 816 //{id} {mission_id} {text1_id} {text2_id}
ENDIF
WAIT 13200
GOSUB sub_unlock_player_controls
RESTORE_CAMERA
CAMERA_PERSIST_TRACK FALSE
CAMERA_PERSIST_POS FALSE
SET_CAMERA_BEHIND_PLAYER   
flag_player_hit_counter = 1
SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter        // 0:OFF || 1:ON  
deliver = 0
iTime =  165000  // 165 sec    
timerb = 0   
GOSUB create_customer1 
            
main_loop:
    IF IS_PLAYER_PLAYING 0
        GOSUB readVars
        IF toggleSpiderMod = 1  //TRUE

            IF deliver = 0
                GOSUB draw_timer
                GOSUB deliver_customer1
            ENDIF
            IF deliver = 1
                GOSUB draw_timer
                GOSUB deliver_customer2
            ENDIF
            IF deliver = 2
                GOSUB draw_timer
                GOSUB deliver_customer3
            ENDIF            
            IF deliver = 3
                GOSUB draw_timer
                GOSUB deliver_customer4
            ENDIF   
            IF deliver = 4
                GOSUB draw_timer
                GOSUB deliver_customer5
            ENDIF    
            IF deliver = 5
                GOSUB draw_timer
                GOSUB deliver_customer6
            ENDIF                               
            IF deliver >= 6
                GOTO mission_passed
            ENDIF                           
            IF timerb > iTime
                GOTO mission_failed
            ENDIF            

            IF HAS_CHAR_BEEN_ARRESTED player_actor
            OR IS_CHAR_DEAD player_actor
                GOSUB mission_failed
                WAIT 2000
            ENDIF

            IF isInMainMenu = 1     //1:true 0: false
                WHILE isInMainMenu = 1     //1:true 0: false
                    GOSUB readVars
                    WAIT 0
                ENDWHILE
                WHILE GET_FADING_STATUS
                    WAIT 0
                ENDWHILE
                WAIT 1000
                USE_TEXT_COMMANDS FALSE
                WAIT 0
                GOSUB mission_failed
                WAIT 1000                              
            ENDIF
            IF isInMainMenu = 1     //1:true 0: false
                WHILE isInMainMenu = 1     //1:true 0: false
                    GOSUB readVars
                    WAIT 0
                ENDWHILE
                WHILE GET_FADING_STATUS
                    WAIT 0
                ENDWHILE
                WAIT 1000
            ENDIF

        ELSE
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            GOSUB mission_failed
            WAIT 2000
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

//------------------------------------------------

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
RETURN

activeInteriorCheck:
    GET_AREA_VISIBLE (is_in_interior)
RETURN

sub_FadeOut_700ms:
    CLEAR_PRINTS 
    CLEAR_HELP
    USE_TEXT_COMMANDS FALSE
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 700 FADE_OUT
    WHILE GET_FADING_STATUS
        WAIT 0
    ENDWHILE   
RETURN

sub_FadeIn_700ms:
    CLEAR_PRINTS 
    CLEAR_HELP
    USE_TEXT_COMMANDS FALSE
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 700 FADE_IN
    WHILE GET_FADING_STATUS
        WAIT 0
    ENDWHILE
RETURN

sub_lock_player_controls:
    SWITCH_WIDESCREEN TRUE
    RESTORE_CAMERA_JUMPCUT
	CLEAR_CHAR_TASKS player_actor
	SET_PLAYER_CONTROL 0 FALSE
    SET_PLAYER_JUMP_BUTTON 0 FALSE
	SET_EVERYONE_IGNORE_PLAYER 0 TRUE 
RETURN

sub_unlock_player_controls:
    SWITCH_WIDESCREEN FALSE   
	RESTORE_CAMERA_JUMPCUT 
	SET_CAMERA_BEHIND_PLAYER
	CLEAR_CHAR_TASKS player_actor
	SET_PLAYER_CONTROL 0 TRUE
    SET_PLAYER_JUMP_BUTTON 0 TRUE
	SET_EVERYONE_IGNORE_PLAYER 0 FALSE
RETURN

create_pizzaStack:
    i = 0
    REQUEST_MODEL 2814
    LOAD_ALL_MODELS_NOW
    DELETE_RENDER_OBJECT i
    CREATE_RENDER_OBJECT_TO_CHAR_BONE player_actor 2814 35 (-0.1 0.0 -0.0) (0.0 0.0 0.0) i
    SET_RENDER_OBJECT_ROTATION i (90.0 0.0 -0.0)
RETURN

create_customer1:
    ADD_SPRITE_BLIP_FOR_COORD (-1929.76 1187.325 45.445) RADAR_SPRITE_WAYPOINT (iEventBlip)
    ADD_SPHERE (-1929.76 1187.325 45.445) 1.0 (iEventBlip2) 
    WHILE NOT LOCATE_CHAR_ANY_MEANS_3D player_actor -1929.76 1187.325 45.445 125.5 125.5 125.5 FALSE
        GOSUB draw_timer
        WAIT 0
    ENDWHILE
    REQUEST_MODEL SBFYRI
    WHILE NOT HAS_MODEL_LOADED SBFYRI
        GOSUB draw_timer
        WAIT 0
    ENDWHILE
    CREATE_CHAR 5 SBFYRI -1929.76 1189.325 45.445 (cust1)
	SET_CHAR_HEADING cust1 183.0 
	//ADD_BLIP_FOR_COORD -1929.76 1187.325 45.445 (iEventBlip)  
	TASK_PLAY_ANIM_NON_INTERRUPTABLE cust1 "WOMAN_IDLESTANCE" "PED" 4.0 1 0 0 0 -1 
RETURN 

deliver_customer1:
    IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D player_actor -1929.76 1187.325 45.445 1.2 1.2 1.4 TRUE
        REMOVE_BLIP iEventBlip
		REMOVE_SPHERE iEventBlip2
        GOSUB attach_pizza_1
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\Talk 1.mp3" sfx
        SET_AUDIO_STREAM_STATE sfx 1
        PRINT_NOW TEXT1 3000 1 
        WAIT 3000
        deliver += 1
        MARK_MODEL_AS_NO_LONGER_NEEDED SBFYRI
        PRINT_FORMATTED_NOW "Pizza Number %i Delivered" 2222 deliver
        WAIT 300
        GOSUB create_customer2
    ENDIF
RETURN

attach_pizza_1:
    CREATE_OBJECT 2814 0.0 0.0 0.0 iObj
    SET_OBJECT_COLLISION iObj 0 
    SET_OBJECT_PROOFS iObj 1 1 1 1 1 
    TASK_PICK_UP_OBJECT cust1 iObj 0.0 0.0 0.0 5 16 NULL NULL -1
RETURN

create_customer2:
    ADD_SPRITE_BLIP_FOR_COORD (-1605.571 788.315 6.82) RADAR_SPRITE_WAYPOINT (iEventBlip)
    ADD_SPHERE (-1605.571 788.315 6.82) 1.0 (iEventBlip2)  
    WHILE NOT LOCATE_CHAR_ANY_MEANS_3D player_actor -1605.571 788.315 6.82 125.5 125.5 125.5 FALSE
        GOSUB draw_timer
        WAIT 0
    ENDWHILE
    REQUEST_MODEL OFYST
    WHILE NOT HAS_MODEL_LOADED OFYST
        GOSUB draw_timer
        WAIT 0
    ENDWHILE
    CREATE_CHAR 5 OFYST -1605.929 786.825 7.63 (cust2)
	SET_CHAR_HEADING cust2 348.0 
	//ADD_BLIP_FOR_COORD -1605.571 788.315 6.82 (iEventBlip)
	TASK_PLAY_ANIM_NON_INTERRUPTABLE cust2 "WOMAN_IDLESTANCE" "PED" 4.0 1 0 0 0 -1 
RETURN 

deliver_customer2:
    IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D player_actor -1605.571 788.315 6.82 1.2 1.2 1.4 TRUE
        REMOVE_BLIP iEventBlip
		REMOVE_SPHERE iEventBlip2    
        GOSUB attach_pizza_2
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\Talk 1.mp3" sfx
        SET_AUDIO_STREAM_STATE sfx 1
        PRINT_NOW TEXT2 3000 1
        WAIT 3000
        deliver += 1
        MARK_MODEL_AS_NO_LONGER_NEEDED OFYST
        PRINT_FORMATTED_NOW "Pizza Number %i Delivered" 2222 deliver
        GOSUB create_customer3
    ENDIF
RETURN

attach_pizza_2:
    CREATE_OBJECT 2814 0.0 0.0 0.0 iObj
    SET_OBJECT_COLLISION iObj 0 
    SET_OBJECT_PROOFS iObj 1 1 1 1 1 
    TASK_PICK_UP_OBJECT cust2 iObj 0.0 0.0 0.0 5 16 NULL NULL -1
RETURN

create_customer3:
    ADD_SPRITE_BLIP_FOR_COORD (-1875.622 1125.53 45.445) RADAR_SPRITE_WAYPOINT (iEventBlip)
    ADD_SPHERE (-1875.622 1125.53 45.445) 1.0 (iEventBlip2) 
    WHILE NOT LOCATE_CHAR_ANY_MEANS_3D player_actor -1875.622 1125.53 45.445 125.5 125.5 125.5 FALSE
        GOSUB draw_timer
        WAIT 0
    ENDWHILE    
    REQUEST_MODEL HECK1
    WHILE NOT HAS_MODEL_LOADED HECK1
        GOSUB draw_timer
        WAIT 0
    ENDWHILE
    CREATE_CHAR 5 HECK1 -1874.622 1125.53 47.145 (cust3)
	SET_CHAR_HEADING cust3 90.0 
	//ADD_BLIP_FOR_COORD -1875.622 1125.53 45.445 (iEventBlip)
	TASK_PLAY_ANIM_NON_INTERRUPTABLE cust3 "IDLE_GANG1" "PED" 4.0 1 0 0 0 -1 
RETURN 

deliver_customer3:
    IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D player_actor -1875.622 1125.53 45.445 1.2 1.2 1.4 TRUE
        REMOVE_BLIP iEventBlip
        REMOVE_SPHERE iEventBlip2
        GOSUB attach_pizza_3
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\Talk 3.mp3" sfx
        SET_AUDIO_STREAM_STATE sfx 1
        PRINT_NOW TEXT3 3000 1
        WAIT 3000
        deliver += 1
        MARK_MODEL_AS_NO_LONGER_NEEDED HECK1
        PRINT_FORMATTED_NOW "Pizza Number %i Delivered" 2222 deliver
        GOSUB create_customer4
    ENDIF
RETURN

attach_pizza_3:
    CREATE_OBJECT 2814 0.0 0.0 0.0 iObj 
    SET_OBJECT_COLLISION iObj 0 
    SET_OBJECT_PROOFS iObj 1 1 1 1 1 
    TASK_PICK_UP_OBJECT cust3 iObj 0.0 0.0 0.0 5 16 NULL NULL -1
RETURN

create_customer4:
    ADD_SPRITE_BLIP_FOR_COORD (-2014.835 785.569 45.445) RADAR_SPRITE_WAYPOINT (iEventBlip)
    ADD_SPHERE (-2014.835 785.569 45.445) 1.0 (iEventBlip2)    
    WHILE NOT LOCATE_CHAR_ANY_MEANS_3D player_actor -2014.835 785.569 45.445 125.5 125.5 125.5 FALSE
        GOSUB draw_timer
        WAIT 0
    ENDWHILE      
    REQUEST_MODEL HMYRI
    WHILE NOT HAS_MODEL_LOADED HMYRI
        GOSUB draw_timer
        WAIT 0
    ENDWHILE
    CREATE_CHAR 5 HMYRI -2016.397 785.54 45.445 (cust4)
	SET_CHAR_HEADING cust4 270.0 
	//ADD_BLIP_FOR_COORD -2014.835 785.569 45.445 (iEventBlip)
	TASK_PLAY_ANIM_NON_INTERRUPTABLE cust4 "IDLE_GANG1" "PED" 4.0 1 0 0 0 -1 
RETURN 

deliver_customer4:
    IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D player_actor -2014.835 785.569 45.445 1.2 1.2 1.4 TRUE
        REMOVE_BLIP iEventBlip
        REMOVE_SPHERE iEventBlip2
        GOSUB attach_pizza_4
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\Talk 4.mp3" sfx
        SET_AUDIO_STREAM_STATE sfx 1
        PRINT_NOW TEXT4 3000 1
        WAIT 3000
        deliver += 1
        MARK_MODEL_AS_NO_LONGER_NEEDED HMYRI
        PRINT_FORMATTED_NOW "Pizza Number %i Delivered" 2222 deliver
        GOSUB create_customer5
    ENDIF
RETURN
    
attach_pizza_4:
    CREATE_OBJECT 2814 0.0 0.0 0.0 iObj 
    SET_OBJECT_COLLISION iObj 0 
    SET_OBJECT_PROOFS iObj 1 1 1 1 1 
    TASK_PICK_UP_OBJECT cust4 iObj 0.0 0.0 0.0 5 16 NULL NULL -1
RETURN

create_customer5:
    ADD_SPRITE_BLIP_FOR_COORD (-2111.440674 327.662842 35.164063) RADAR_SPRITE_WAYPOINT (iEventBlip)
    ADD_SPHERE (-2111.440674 327.662842 35.164063) 1.0 (iEventBlip2)
    WHILE NOT LOCATE_CHAR_ANY_MEANS_3D player_actor (-2111.440674 327.662842 35.164063 125.5 125.5 125.5 FALSE
        GOSUB draw_timer
        WAIT 0
    ENDWHILE       
    REQUEST_MODEL BMYAP
    WHILE NOT HAS_MODEL_LOADED BMYAP
        GOSUB draw_timer
        WAIT 0
    ENDWHILE
    CREATE_CHAR 5 BMYAP -2111.448730 329.202972 36.164063 (cust5)
	SET_CHAR_HEADING cust5 180.0 
	//ADD_BLIP_FOR_COORD -2111.440674 327.662842 35.164063 (iEventBlip)      
	TASK_PLAY_ANIM_NON_INTERRUPTABLE cust5 "IDLE_GANG1" "PED" 4.0 1 0 0 0 -1 
RETURN 

deliver_customer5:
    IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D player_actor -2111.440674 327.662842 35.164063 1.2 1.2 1.4 TRUE
        REMOVE_BLIP iEventBlip
        REMOVE_SPHERE iEventBlip2        
        GOSUB attach_pizza_5
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\Talk 3.mp3" sfx
        SET_AUDIO_STREAM_STATE sfx 1
        PRINT_NOW TEXT5 3000 1
        WAIT 3000
        deliver += 1
        MARK_MODEL_AS_NO_LONGER_NEEDED BMYAP
        PRINT_FORMATTED_NOW "Pizza Number %i Delivered" 2222 deliver
        GOSUB create_customer6
    ENDIF
RETURN

attach_pizza_5:
    CREATE_OBJECT 2814 0.0 0.0 0.0 iObj 
    SET_OBJECT_COLLISION iObj 0 
    SET_OBJECT_PROOFS iObj 1 1 1 1 1 
    TASK_PICK_UP_OBJECT cust5 iObj 0.0 0.0 0.0 5 16 NULL NULL -1
RETURN

create_customer6:
    ADD_SPRITE_BLIP_FOR_COORD (-1705.179 785.739 24.890) RADAR_SPRITE_WAYPOINT (iEventBlip)
    ADD_SPHERE (-1705.179 785.739 24.890) 1.0 (iEventBlip2)
    WHILE NOT LOCATE_CHAR_ANY_MEANS_3D player_actor (-1705.179 785.739 24.890) 125.5 125.5 125.5 FALSE
        GOSUB draw_timer
        WAIT 0
    ENDWHILE       
    REQUEST_MODEL BFYST
    WHILE NOT HAS_MODEL_LOADED BFYST
        GOSUB draw_timer
        WAIT 0
    ENDWHILE
    CREATE_CHAR 5 BFYST -1704.413 785.818 25.400 (cust6)
	SET_CHAR_HEADING cust6 90.0 
	//-1705.179 785.739 24.890 (iEventBlip)      
	TASK_PLAY_ANIM_NON_INTERRUPTABLE cust6 "IDLE_GANG1" "PED" 4.0 1 0 0 0 -1 
RETURN 

deliver_customer6:
    IF LOCATE_STOPPED_CHAR_ANY_MEANS_3D player_actor -1705.179 785.739 24.890 1.2 1.2 1.4 TRUE
        REMOVE_BLIP iEventBlip
        REMOVE_SPHERE iEventBlip2        
        GOSUB attach_pizza_6
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\Talk 5.mp3" sfx
        SET_AUDIO_STREAM_STATE sfx 1
        PRINT_NOW TEXT6 3000 1
        WAIT 3000
        deliver += 1
        MARK_MODEL_AS_NO_LONGER_NEEDED BFYST
        PRINT_FORMATTED_NOW "Pizza Number %i Delivered" 2222 deliver
    ENDIF
RETURN

attach_pizza_6:
    DELETE_RENDER_OBJECT i
    CREATE_OBJECT 2814 0.0 0.0 0.0 iObj 
    SET_OBJECT_COLLISION iObj 0 
    SET_OBJECT_PROOFS iObj 1 1 1 1 1 
    TASK_PICK_UP_OBJECT cust6 iObj 0.0 0.0 0.0 5 16 NULL NULL -1
RETURN

mission_passed:
    flag_player_on_mission = 0
	WRITE_MEMORY 0xA476AC 4 (flag_player_on_mission) FALSE 	// $ONMISSION = 0
    SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission 
    //PRINT_BIG MSCP 3000 2
    LVAR_INT iExperienceReward

    iExperienceReward = 0

    iTotalTime = iTime
    iTotalTime -= timerb

    IF iTotalTime > 40000  // 40 sec
        iExtraTimeScore = 150
    ELSE
        IF iTotalTime > 30000  // 30 sec
            iExtraTimeScore = 100
        ELSE
            IF iTotalTime > 20000  // 20 sec
                iExtraTimeScore = 50
            ELSE
                iExtraTimeScore = 0
            ENDIF        
        ENDIF
    ENDIF 

    IF deliver = 6
        iExperienceReward += 150
    ENDIF

    iRandomVal2 = 0
    iRandomVal2 = iExperienceReward + iExtraTimeScore   

    SET_CLEO_SHARED_VAR varStatusLevelChar iRandomVal2

    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 7 iRandomVal2 iExperienceReward iExtraTimeScore //{id} {total xp} {delivery xp} {extra time xp}
        WAIT 2000
    ENDIF    

    WAIT 7000

    IF DOES_CHAR_EXIST cust1    
        REMOVE_CHAR_ELEGANTLY cust1
    ENDIF
    IF DOES_CHAR_EXIST cust2    
        REMOVE_CHAR_ELEGANTLY cust2
    ENDIF
    IF DOES_CHAR_EXIST cust3    
        REMOVE_CHAR_ELEGANTLY cust3
    ENDIF
    IF DOES_CHAR_EXIST cust4    
        REMOVE_CHAR_ELEGANTLY cust4
    ENDIF
    IF DOES_CHAR_EXIST cust5    
        REMOVE_CHAR_ELEGANTLY cust5
    ENDIF
    IF DOES_CHAR_EXIST cust6 
        REMOVE_CHAR_ELEGANTLY cust6
    ENDIF       
    IF DOES_BLIP_EXIST iEventBlip             
        REMOVE_BLIP iEventBlip 
    ENDIF

    REMOVE_SPHERE iEventBlip2
 
    WAIT delay_restart_mission
GOTO start

mission_failed:
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 3  //{id}
        WAIT 2000
    ENDIF
    IF DOES_CHAR_EXIST cust1    
        REMOVE_CHAR_ELEGANTLY cust1
    ENDIF
    IF DOES_CHAR_EXIST cust2    
        REMOVE_CHAR_ELEGANTLY cust2
    ENDIF
    IF DOES_CHAR_EXIST cust3    
        REMOVE_CHAR_ELEGANTLY cust3
    ENDIF
    IF DOES_CHAR_EXIST cust4    
        REMOVE_CHAR_ELEGANTLY cust4
    ENDIF
    IF DOES_CHAR_EXIST cust5    
        REMOVE_CHAR_ELEGANTLY cust5
    ENDIF
    IF DOES_CHAR_EXIST cust6 
        REMOVE_CHAR_ELEGANTLY cust6
    ENDIF 
    IF DOES_BLIP_EXIST iEventBlip             
        REMOVE_BLIP iEventBlip 
    ENDIF

    REMOVE_SPHERE iEventBlip2    
    WAIT delay_restart_mission   
GOTO start

loadGeneralFiles:
	REQUEST_ANIMATION "spider"
	REQUEST_ANIMATION "mweb"
	LOAD_ALL_MODELS_NOW
	LOAD_TEXTURE_DICTIONARY scrb
	LOAD_SPRITE objCrosshair    "ilock"
	LOAD_SPRITE tPhotoBomb      "cam"
	LOAD_SPRITE tPBback         "scr01"
	LOAD_SPRITE tPBScrewballNot "scr00"
	LOAD_SPRITE tPBbar0         "scr02"
	LOAD_SPRITE tPBbar1         "scr03"
	LOAD_SPRITE tPBScrewBallToon "scr04"
	LOAD_SPRITE tPBbCounter     "scr05"
	LOAD_SPRITE tPBbCounterBlue "scr06"
    LOAD_SPRITE tPBBackTimer    "btimB"
    LOAD_SPRITE tPBBackScore    "btim"
    LOAD_SPRITE tPBBackInfo     "btimC"
    LOAD_SPRITE tPBBackScoreB   "btimD"

    LOAD_SPRITE tPBSBack1       "rb1"
    LOAD_SPRITE tPBSBack2       "rb2"
    LOAD_SPRITE tPBSBack3       "rb3"
    LOAD_SPRITE tPBSBack1Active "rb11"
    LOAD_SPRITE tPBSBack2Active "rb22"
    LOAD_SPRITE tPBSBack3Active "rb33"
    LOAD_SPRITE iconSuccess     "Success"
RETURN

draw_timer:
    iMinutes = 0
    iSeconds = iTime   //25
    cTimerb_A = timerb          //28
    iSeconds /= 1000
    cTimerb_A /= 1000
    iSeconds -= cTimerb_A
    WHILE iSeconds > 60 
        IF iSeconds >= 60
            iMinutes += 1
            iSeconds += -60
        ENDIF
    ENDWHILE
    iSeconds = iSeconds
    IF 10 > iSeconds
        GOSUB GUI_TextFormat_timer_Colour
        DISPLAY_TEXT_WITH_2_NUMBERS (58.5 188.0) J16D86 iMinutes iSeconds  //~1~:0~1~    
    ELSE
        GOSUB GUI_TextFormat_timer_Colour
        DISPLAY_TEXT_WITH_2_NUMBERS (59.5 188.0) J16D87 iMinutes iSeconds  //~1~:~1~        
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (59.55 180.0) (40.0 15.0) (255 255 255 0) (1.0) (0 0 0 0) (255 255 253 230) 813 9 0.0     // Pizza Time 
    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (95.0 75.0) (sizeX sizeY)
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackScore (60.0 190.0) (sizeX sizeY) (255 255 255 235)    
    USE_TEXT_COMMANDS FALSE
RETURN

}
SCRIPT_END

//-+----------------------------------draw codes-------------------------------------
GUI_TextFormat_BigItemText_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.48 2.32
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 254 251 255
    SET_TEXT_EDGE 1 (65 0 82 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_BigItem_Colour_win:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.54 2.60
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 246 255 255 255
    SET_TEXT_EDGE 1 (66 43 173 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_Counter_race_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.48 2.32
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_timer_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.3 1.45
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_TitleSmall_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_TitleMedium_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9183
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_TitleBig_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.25 1.2084
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_ScoreLeft_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.692
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
    SET_TEXT_CENTRE 1
    SET_TEXT_DRAW_BEFORE_FADE 1
RETURN

GUI_TextFormat_TitleScore_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_TitleScoreMedium_Colour:
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.16 0.7734
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN
{
//CLEO_CALL getCurrentResolution 0 (fX fY)
getCurrentResolution:
    LVAR_INT iresX iresY
    LVAR_FLOAT fresX fresY
    READ_MEMORY 0x00C17044 DWORD FALSE (iresX) // Get current resolution X
    READ_MEMORY 0x00C17048 DWORD FALSE (iresY) // Y
    fresX =# iresX
    fresY =# iresY
CLEO_RETURN 0 (fresX fresY)
}
{
//CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 (640.0 480.0) (sizX sizY)
GetXYSizeInScreen4x3ScaleBy640x480:
    LVAR_FLOAT x y // In
    LVAR_FLOAT fresX fresY
    CLEO_CALL getCurrentResolution 0 (fresX fresY)
    fresY *= 1.33333333
    fresX /= fresY
    x /= fresX
    y /= 1.07142857
CLEO_RETURN 0 (x y)
}

//-+------------------------------IMGUI-EXTRA-------------------------------------
{
GUI_DrawBoxOutline_WithText:
/*
//CLEO_CALL GUI_DrawBoxOutline_WithText 0 PosXY (320.0 240.0) SizeXY (200.0 200.0) RGBA (0 0 0 180) OutlineSize (1.4) OutlineSides (1 1 0 1) OutlineRGBA (200 200 200 200) TextID -1 FormatID 1 Padding 3.0
v0 = posx
v1 = posy
v2 = sizex
v3 = sizey
v4 = r
v5 = g
v6 = b
v7 = a
v8 = outline size
v9 = outline side top    
v10 = outline side right
v11 = outline side bottom
v12 = outline side left
v13 = outline r    
v14 = outline g
v15 = outline b
v16 = outline a
v17 = textid
v18 = formatid
v19 = padding
*/
LVAR_FLOAT v0 v1 v2 v3
LVAR_INT v4 v5 v6 v7
LVAR_FLOAT v8
LVAR_INT v9 v10 v11 v12 v13 v14 v15 v16 v17 v18
LVAR_FLOAT v19
LVAR_FLOAT v20 v21 v22 v23 v25
LVAR_INT v29
LVAR_TEXT_LABEL vTextA
// Create Box
IF v7 > 0 //box
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v0 v1)/*size*/(v2 v3)/*rgba*/(v4 v5 v6 v7)
ENDIF
// Create Outlines
IF v12 = TRUE  //outline side left
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v22 /= 2.0
    v20 -= v22
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v8 v3)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v9 = TRUE  //outline side top
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v23 /= 2.0
    v21 -= v23
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v2 v8)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v10 = TRUE  //outline side right
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v22 /= 2.0
    v20 += v22
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v8 v3)/*rgba*/(v13 v14 v15 v16)
ENDIF
IF v11 = TRUE  //outline side bottom
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    v23 /= 2.0
    v21 += v23
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v20 v21)/*size*/(v2 v8)/*rgba*/(v13 v14 v15 v16)
ENDIF
// Create Text
IF v17 > 0  //Text
    STRING_FORMAT (vTextA)"J16D%i" v17
    //Do Padding 
    GOSUB GUI_DrawBoxOutline_VarsBoxToOutline
    IF v19 > 0.0    // Padding Left/Right
        v22 /= 2.0
        v20 -= v22
    ELSE
        IF 0.0 > v19
            //to left
            v22 /= 2.0
            v20 += v22
        ELSE
            SET_TEXT_CENTRE 1
        ENDIF
    ENDIF
    v20 += v19
    CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
    v21 -= v25
    DISPLAY_TEXT (v20 v21) $vTextA
ENDIF
CLEO_RETURN 0

GUI_DrawBoxOutline_VarsBoxToOutline:
    v20 = v0
    v21 = v1
    v22 = v2
    v23 = v3
RETURN
}
{
GUI_DrawBox_WithNumber:
/*
//CLEO_CALL GUI_DrawBox_WithNumber 0 PosXY (320.0 240.0) SizeXY (200.0 200.0) RGBA (0 0 0 180) TextID -1 FormatID 1 Padding 3.0 number 5
v0 = posx
v1 = posy
v2 = sizex
v3 = sizey
v4 = r
v5 = g
v6 = b
v7 = a
v17 = textid
v18 = formatid
v19 = padding
v8 = number
*/
LVAR_FLOAT v0 v1 v2 v3
LVAR_INT v4 v5 v6 v7
LVAR_INT v17 v18
LVAR_FLOAT v19
LVAR_INT v8
LVAR_FLOAT v20 v21 v22 v25
LVAR_INT v29
LVAR_TEXT_LABEL vTextA
// Create Box
IF v7 > 0 //box
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_RECT /*pos*/(v0 v1)/*size*/(v2 v3)/*rgba*/(v4 v5 v6 v7)
ENDIF
// Create Text
IF v17 > 0  //Text
    STRING_FORMAT (vTextA)"J16D%i" v17
    //Do Padding 
    GOSUB GUI_DrawBox_VarsBoxNumberToOutline
    IF v19 > 0.0    // Padding Left/Right
        v22 /= 2.0
        v20 -= v22
    ELSE
        IF 0.0 > v19
            //to left
            v22 /= 2.0
            v20 += v22
        ELSE
            SET_TEXT_CENTRE 1
        ENDIF
    ENDIF
    v20 += v19
    CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
    v21 -= v25
    DISPLAY_TEXT_WITH_NUMBER (v20 v21) $vTextA v8
ENDIF
CLEO_RETURN 0

GUI_DrawBox_VarsBoxNumberToOutline:
    v20 = v0
    v21 = v1
    v22 = v2
RETURN
}
{
GUI_SetTextFormatByID:
//CLEO_CALL GUI_SetTextFormatByID 0 /*ID*/ v18 /*PaddingBottom*/ v25
LVAR_INT iID
LVAR_FLOAT fPadding
LVAR_INT ptAlpha
LVAR_FLOAT vtAlpha
SWITCH iID
    CASE 1
        GOSUB GUI_TextFormat_ItemMenu
        fPadding = 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_ItemMenu_Active
        fPadding = 4.5
        BREAK
    CASE 3
        GOSUB GUI_TextFormat_SmallMenu
        fPadding = 5.0
        BREAK
    CASE 4
        GOSUB GUI_TextFormat_SmallMenu_Active
        fPadding = 5.0
        BREAK
    CASE 5
        GOSUB GUI_TextFormat_MediumMenu
        fPadding = 4.5
        BREAK
    CASE 6
        GOSUB GUI_TextFormat_MediumMenu_Numbers
        fPadding = 5.0
        BREAK
    CASE 7
        GOSUB GUI_TextFormat_BigMenu_Numbers
        fPadding = 6.0
        BREAK
    CASE 8
        GOSUB GUI_TextFormat_BigMenu_Numbers_Color
        fPadding = 6.0
        BREAK
    CASE 9
        GOSUB GUI_TextFormat_MediumTitle
        fPadding = 5.0
        BREAK
    CASE 10
        GOSUB GUI_TextFormat_SmallItemMenu_Colour
        fPadding = 5.0
        BREAK
    CASE 11
        GOSUB GUI_TextFormat_SmallItemMenu
        fPadding = 5.0
        BREAK
    CASE 12
        GOSUB GUI_TextFormat_MediumItemTitle
        fPadding = 4.5
        BREAK
    CASE 13
        GOSUB GUI_TextFormat_TextReward1_Colour
        fPadding = 4.5
        BREAK
    CASE 14
        GOSUB GUI_TextFormat_TextReward2_Colour
        fPadding = 4.5
        BREAK
    CASE 15
        GOSUB GUI_TextFormat_TextReward3_Colour
        fPadding = 4.5
        BREAK
ENDSWITCH
CLEO_RETURN 0 fPadding

GUI_TextFormat_ItemMenu:            //1   Title Finish Awards   (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.25 1.2084
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_ItemMenu_Active:     //2 Title Blue Medium (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.16 0.7734
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallMenu:           //3 small letters (for score) 
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallMenu_Active:    //4  format for numbers (for score) white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.692
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 237 254 255 255
    SET_TEXT_EDGE 1 (53 86 144 255)
RETURN

GUI_TextFormat_MediumMenu:          //5  format XP level text
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.21 1.015
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 15 236 198 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_MediumMenu_Numbers:  //6  format XP level Numbers
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9184
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 211 226 224 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_BigMenu_Numbers:     //7     numbers white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.6912
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 252 252 255 255
    SET_TEXT_EDGE 1 (255 255 255 0)
RETURN

GUI_TextFormat_BigMenu_Numbers_Color:   //8     numbers blue
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.35 1.6912
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 35 237 193 255
    SET_TEXT_EDGE 1 (255 255 255 0)
RETURN

GUI_TextFormat_MediumTitle:         //9     Text medium format white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9183
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN    

GUI_TextFormat_SmallItemMenu_Colour:     //10 Title Blue Small (for score)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_SmallItemMenu:       //11    Text small format white
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.14 0.67666
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_MediumItemTitle:     //12     Text Medium / Names
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.19 0.9183
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_EDGE 1 (0 0 0 100)
RETURN

GUI_TextFormat_TextReward1_Colour:     //13    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 59 36 23 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_TextReward2_Colour:     //14    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 12 60 78 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN

GUI_TextFormat_TextReward3_Colour:     //15    format rewards square indicator
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.145 0.70
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 72 63 17 255
    SET_TEXT_EDGE 1 (0 0 0 0)
RETURN
}

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
CONST_INT varThrowVehDoors      27    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated

CONST_INT varLevelChar          30    //sp_lvl    || Level
CONST_INT varStatusLevelChar    31    //If value >0 automatically will add that number to Experience Points (Max Reward +2500)

CONST_INT varIdWebWeapon        32    //sp_mm     || 1-8 weap
CONST_INT varWeapAmmo           33    //sp_wep    ||store current weap ammo
CONST_INT varIdPowers           34    //MSpiderJ16Dv7 - sp_po     ||Id powers 1 - 12
CONST_INT varPowersProgress     35    //sp_po     || current power progress
CONST_INT varHitCount           36    //sp_hit    || hitcounting
CONST_INT varHitCountFlag       37    //sp_hit    || hitcounting

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