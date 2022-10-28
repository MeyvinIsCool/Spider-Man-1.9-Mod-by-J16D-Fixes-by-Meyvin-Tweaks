// by Meyvin Tweaks
// in Colaboration with GTA Loyalty
// SAMS: Remastered | Side Mission #2 - No More Supplies
// Spider-Man Mod for GTA SA c.2018 - 2022
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

MISSION_START
	SCRIPT_NAME sams_s2
	GOSUB mission_start_init
	IF HAS_DEATHARREST_BEEN_EXECUTED
		GOSUB mission_failed
	ENDIF
	GOSUB mission_cleanup
MISSION_END

{
//Constans
CONST_INT player 0

// Variables for mission
LVAR_INT player_actor
LVAR_INT flag_player_on_mission flag_player_hit_counter toggleMusic 
LVAR_INT iTempVar iTempVar1 iTempVar2 iTempVar3 counter 
LVAR_INT sfx1 sfx2 sfx3 music_sfx1 music_sfx2 iEventBlip iEventBlip2 
LVAR_INT audio_line_is_active iEventTask kills_counter
LVAR_FLOAT x[6] y[6] z[6] fDistance[6] 
LVAR_FLOAT objX[2] objY[2] objZ[2] fObjDistance[2] 
LVAR_INT r g b a
LVAR_FLOAT fProgressTower v1 v2 v3 v4 sx sy fVolume
LVAR_INT iWeather iCheckpoint
LVAR_INT flag_enemy1_killed flag_enemy2_killed flag_enemy3_killed flag_enemy4_killed flag_enemy5_killed flag_enemy6_killed flag_enemy7_killed flag_enemy8_killed
LVAR_INT iEnemy[8] iObj[5] iEnemyBlip[8]
LVAR_INT iCrates[4]

mission_start_init:
REGISTER_MISSION_GIVEN
flag_player_on_mission = 1
SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON
flag_player_hit_counter = 0
SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter       // 0:OFF || 1:ON
GET_PLAYER_CHAR 0 player_actor

REQUEST_MODEL DNB1 
REQUEST_MODEL DNB2 
REQUEST_MODEL DNB3
REQUEST_MODEL MICRO_UZI 
REQUEST_MODEL COLT45 
REQUEST_MODEL 1581
REQUEST_MODEL 1224

REQUEST_ANIMATION "INT_HOUSE"
REQUEST_ANIMATION "spider"
LOAD_TEXTURE_DICTIONARY spsams
LOAD_SPRITE idTowerB "surv_tow"
LOAD_SPRITE idTip2 "htip2"
LOAD_SPRITE idWay "mk1"
LOAD_ALL_MODELS_NOW

// ***************************************Mission Start*************************************
CAMERA_RESET_NEW_SCRIPTABLES
CAMERA_RESET_NEW_SCRIPTABLES
CAMERA_PERSIST_TRACK TRUE
CAMERA_PERSIST_POS TRUE
CAMERA_SET_VECTOR_MOVE (-1918.9305 1040.1719 68.0078) (-1918.9305 1040.1719 68.0078) 1500 0
CAMERA_SET_VECTOR_TRACK (-1918.9305 1040.1719 68.0078) (-1918.9305 1040.1719 68.0078) 1500 0
GOSUB sub_lock_player_controls
WAIT 0
SET_CHAR_COORDINATES_NO_OFFSET player_actor -1903.954 1042.778 68.0078
SET_CHAR_HEADING player_actor 308.0
TASK_TOGGLE_DUCK player_actor TRUE
WAIT 1
GOSUB sub_Fade_in_500ms
GET_CLEO_SHARED_VAR varMusic (toggleMusic)
IF toggleMusic = 1 // TRUE
	IF DOES_FILE_EXIST "CLEO\SpiderJ16D\music\SD_8.mp3"
		LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\music\SD_8.mp3" (music_sfx1)
		SET_MUSIC_DOES_FADE TRUE
		SET_AUDIO_STREAM_LOOPED music_sfx1 1
		SET_AUDIO_STREAM_STATE music_sfx1 1	// -1|0:stop || 1:play || 2:pause || 3:resume
		CLEO_CALL increase_volume 0 music_sfx1 0.75	//max_volume 0.0-1.0
	ENDIF
ENDIF
WAIT 500
CAMERA_SET_VECTOR_MOVE (-1918.9305 1040.1719 68.0078) (-1895.1395 1064.4817 134.0078) 13000 0
CAMERA_SET_VECTOR_TRACK (-1918.9305 1040.1719 68.0078) (-1895.1395 1064.4817 134.0078) 13000 0

IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 4 1 21 22 //{id} {mission_id} {text1_id} {text2_id}
ENDIF
GET_CURRENT_WEATHER iWeather

//dialogue
REMOVE_AUDIO_STREAM music_sfx2
IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya1.mp3" (sfx2) 
    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	GET_AUDIO_SFX_VOLUME (fVolume)
	fVolume = 0.9
	SET_AUDIO_STREAM_VOLUME sfx2 fVolume
ENDIF
PRINT_NOW PAYA1 3500 1
WAIT 3500
REMOVE_AUDIO_STREAM music_sfx2
IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya2.mp3" (sfx2) 
    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	GET_AUDIO_SFX_VOLUME (fVolume)
	fVolume = 0.9
	SET_AUDIO_STREAM_VOLUME sfx2 fVolume
ENDIF
PRINT_NOW PAYA2 3500 1

timera = 0	//delay
WHILE 2000 > timera
	WAIT 0
ENDWHILE
audio_line_is_active = 1
SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active        // 0:OFF || 1:ON

timera = 0	//delay
WHILE 8000 > timera
	WAIT 0
ENDWHILE

CAMERA_PERSIST_TRACK FALSE
CAMERA_PERSIST_POS FALSE
RESTORE_CAMERA_JUMPCUT
CAMERA_RESET_NEW_SCRIPTABLES
CAMERA_RESET_NEW_SCRIPTABLES
GOSUB sub_unlock_player_controls
WAIT 100
flag_player_hit_counter = 1
SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter       // 0:OFF || 1:ON
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 4 1 21 22 //{id} {mission_id} {text1_id} {text2_id}
ENDIF
WAIT 50
CREATE_CHECKPOINT 3 (-1831.844 1045.885 113.2112) (0.0 0.0 0.0) (3.0) iCheckpoint
kills_counter = 0

mission_part1:  
	IF LOCATE_CHAR_ANY_MEANS_3D player_actor -1831.844 1045.885 113.2112 3.0 3.0 3.0 FALSE
		DO_FADE 300 FADE_OUT
		DELETE_CHECKPOINT iCheckpoint
        flag_player_hit_counter = 0
        SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter       // 0:OFF || 1:ON        
		WAIT 1500
		DISPLAY_ZONE_NAMES FALSE
		CLEAR_CHAR_TASKS player_actor
		SET_EXTRA_COLOURS 1 0
		SET_AREA_VISIBLE 3
		SET_CHAR_AREA_VISIBLE player_actor 3
		SWITCH_ENTRY_EXIT PAPER 1
		LOAD_SCENE (352.1167, 215.0228, 1008.383)
		SET_CHAR_COORDINATES_NO_OFFSET player_actor 352.1167 215.0228 1008.383
        WAIT 50
		SET_CHAR_HEADING player_actor 180.0        

		WAIT 100 
		RESTORE_CAMERA_JUMPCUT
        SWITCH_WIDESCREEN FALSE
		WAIT 1200 	
		DO_FADE 300 FADE_IN
        CLEAR_CHAR_TASKS player_actor
		DISPLAY_ZONE_NAMES TRUE
		SWITCH_ENTRY_EXIT PAPER 0
		SHOW_BLIPS_ON_ALL_LEVELS TRUE
		GOSUB create_enemys_phase1
        GOSUB sub_lock_player_controls

        flag_enemy1_killed = FALSE
        flag_enemy2_killed = FALSE
        flag_enemy3_killed = FALSE
        flag_enemy4_killed = FALSE
        flag_enemy5_killed = FALSE        
		
        IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya3.mp3" (sfx2) 
            SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	        GET_AUDIO_SFX_VOLUME (fVolume)
	        fVolume = 0.9
	        SET_AUDIO_STREAM_VOLUME sfx2 fVolume
        ENDIF        
		PRINT_NOW PAYA3 4300 1
		WAIT 4500
        IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya4.mp3" (sfx2) 
            SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	        GET_AUDIO_SFX_VOLUME (fVolume)
	        fVolume = 0.9
	        SET_AUDIO_STREAM_VOLUME sfx2 fVolume
        ENDIF                
		PRINT_NOW PAYA4 2900 1
		WAIT 3200

		CREATE_OBJECT 2165 (344.4429 162.5566 1013.188) iObj[0]
		SET_OBJECT_HEADING iObj[0] 90.0
		CREATE_OBJECT 6400 (368.8392 162.2411 1024.289) iObj[1]
		CREATE_OBJECT 2181 (347.133 166.0196 1013.188) iObj[2]
		SET_OBJECT_HEADING iObj[2] 0.0
		CREATE_OBJECT 2008 (349.6674 162.5125 1013.188) iObj[3]
		SET_OBJECT_HEADING iObj[3] 270.0

		CREATE_OBJECT 1224 (346.0262 163.659 1024.789) iCrates[0]
		SET_OBJECT_HEADING iCrates[0] 59.0       
		CREATE_OBJECT 1224 (346.3127 162.1201 1024.796) iCrates[1]
		SET_OBJECT_HEADING iCrates[1] 99.0    
		CREATE_OBJECT 1224 (346.204 160.3664 1024.789) iCrates[2]
		SET_OBJECT_HEADING iCrates[2] 120.0                     
		CREATE_OBJECT 1224 (348.5568 160.837 1024.789) iCrates[3]
		SET_OBJECT_HEADING iCrates[3] 239.0   

		PRINT_NOW JDSM30 3500 1      
        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 4 1 21 23 //{id} {mission_id} {text1_id} {text2_id}
        ENDIF           
        flag_player_hit_counter = 1
        SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter       // 0:OFF || 1:ON          
        GOSUB sub_unlock_player_controls
        ADD_SPRITE_BLIP_FOR_COORD (346.9816 161.8278 1014.188) RADAR_SPRITE_WAYPOINT (iEventBlip)
        iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
        GOSUB state_play_sfx 

	ENDIF

	IF IS_CHAR_DEAD iEnemy[0]
        flag_enemy1_killed = TRUE
		REMOVE_BLIP iEnemyBlip[0]
    ENDIF        
	IF IS_CHAR_DEAD iEnemy[1]
        flag_enemy2_killed = TRUE
		REMOVE_BLIP iEnemyBlip[1]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[2]
        flag_enemy3_killed = TRUE
		REMOVE_BLIP iEnemyBlip[2]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[3]
        flag_enemy4_killed = TRUE
		REMOVE_BLIP iEnemyBlip[3]
    ENDIF                             
	IF IS_CHAR_DEAD iEnemy[4]
        flag_enemy5_killed = TRUE
		REMOVE_BLIP iEnemyBlip[4]
    ENDIF  

    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
    IF NOT IS_CHAR_DEAD iEnemy[0]
        IF IS_CHAR_SHOOTING iEnemy[0]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp5.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP2 2500 1
                WAIT 2500    
                GOSUB discovered      
                RETURN
            ENDIF                         
        ENDIF               
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[1]  
        IF IS_CHAR_SHOOTING iEnemy[1]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP1 2500 1
                WAIT 2500    
                GOSUB discovered  
                RETURN    
            ENDIF                         
        ENDIF               
    ENDIF    

    IF NOT IS_CHAR_DEAD iEnemy[2]  
        IF IS_CHAR_SHOOTING iEnemy[2]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP2 2500 1
                WAIT 2500    
                GOSUB discovered
                RETURN      
            ENDIF                         
        ENDIF               
    ENDIF    

    IF NOT IS_CHAR_DEAD iEnemy[3]  
        IF IS_CHAR_SHOOTING iEnemy[3]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp5.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP2 2500 1
                WAIT 2500    
                GOSUB discovered   
                RETURN   
            ENDIF                         
        ENDIF               
    ENDIF    

    IF NOT IS_CHAR_DEAD iEnemy[4]  
        IF IS_CHAR_SHOOTING iEnemy[4]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP1 2500 1
                WAIT 2500    
                GOSUB discovered  
                RETURN    
            ENDIF                         
        ENDIF               
    ENDIF

//-+----------------------------3D Blips Enemy--------------------------------------------------------

    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
    IF NOT IS_CHAR_DEAD iEnemy[0] 
     
        GET_CHAR_COORDINATES iEnemy[0] (x[1] y[1] z[1])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance[0])

        IF fDistance[0] > 10.0
        AND 300.0 > fDistance[0]
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fDistance[0]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[1] 
     
        GET_CHAR_COORDINATES iEnemy[1] (x[2] y[2] z[2])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[2] y[2] z[2]) (fDistance[1])

        IF fDistance[1] > 10.0
        AND 300.0 > fDistance[1]
            CONVERT_3D_TO_SCREEN_2D (x[2] y[2] z[2]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[2] 
     
        GET_CHAR_COORDINATES iEnemy[2] (x[3] y[3] z[3])
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[3] y[3] z[3]) (fDistance[2])

        IF fDistance[2] > 10.0
        AND 300.0 > fDistance[2]
            CONVERT_3D_TO_SCREEN_2D (x[3] y[3] z[3]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[2]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[3] 
     
        GET_CHAR_COORDINATES iEnemy[3] (x[4] y[4] z[4])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[4] y[4] z[4]) (fDistance[3])

        IF fDistance[3] > 10.0
        AND 300.0 > (fDistance[3])
            CONVERT_3D_TO_SCREEN_2D (x[4] y[4] z[4]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[3]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[4] 
     
        GET_CHAR_COORDINATES iEnemy[4] (x[5] y[5] z[5])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[5] y[5] z[5]) (fDistance[4])

        IF fDistance[4] > 10.0
        AND 300.0 > fDistance[4]
            CONVERT_3D_TO_SCREEN_2D (x[5] y[5] z[5]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[4]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF    

    IF DOES_BLIP_EXIST (iEventBlip)
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (347.559 165.272 1014.187) (fObjDistance[1])

        IF fObjDistance[1] > 10.0
        AND 300.0 > fObjDistance[1]
            CONVERT_3D_TO_SCREEN_2D (347.559 165.272 1014.787) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            DRAW_SPRITE idWay (v1 v2) (v3 v3) (255 255 255 235)
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fObjDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF 
    ENDIF 
//-+-------------------------------------------------------------------------------------

	IF LOCATE_CHAR_ANY_MEANS_3D player_actor 346.9816 161.8278 1014.188 3.0 3.0 3.0 FALSE
        REMOVE_BLIP (iEventBlip)
		WAIT 500 
        IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya5.mp3" (sfx2) 
            SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	        GET_AUDIO_SFX_VOLUME (fVolume)
	        fVolume = 0.9
	        SET_AUDIO_STREAM_VOLUME sfx2 fVolume
        ENDIF                
		PRINT_NOW PAYA5 3400 1
		WAIT 3600
        IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya6.mp3" (sfx2) 
            SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	        GET_AUDIO_SFX_VOLUME (fVolume)
	        fVolume = 0.9
	        SET_AUDIO_STREAM_VOLUME sfx2 fVolume
        ENDIF                
		PRINT_NOW PAYA6 2300 1
		WAIT 2600
		CREATE_PICKUP 1581 3 (319.6563 181.9412 1014.188) iObj[4]
		ADD_BLIP_FOR_PICKUP iObj[4] iEventBlip2		
		GOTO init_part2_A_1		
	ENDIF

    IF IS_CHAR_DEAD player_actor
        GOSUB mission_failed
        WAIT 2000
        GOSUB mission_cleanup
        RETURN
    ENDIF  

	WAIT 0 
GOTO mission_part1  

init_part2_A_1:
IF DOES_BLIP_EXIST iEventBlip
	REMOVE_BLIP iEventBlip
ENDIF

mission_part2_A_1:
	IF HAS_PICKUP_BEEN_COLLECTED iObj[4] 
		IF DOES_BLIP_EXIST iEventBlip2
			REMOVE_BLIP iEventBlip2
		ENDIF	
        IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya7.mp3" (sfx2) 
            SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	        GET_AUDIO_SFX_VOLUME (fVolume)
	        fVolume = 0.9
	        SET_AUDIO_STREAM_VOLUME sfx2 fVolume
        ENDIF                
		PRINT_NOW PAYA7 3000 1
		WAIT 3200
        GOTO init_part2_A_2
    ENDIF  
//-+----------------------------3D Blips--------------------------------------------------------

    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
    IF NOT IS_CHAR_DEAD iEnemy[0] 
     
        GET_CHAR_COORDINATES iEnemy[0] (x[1] y[1] z[1])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance[0])

        IF fDistance[0] > 10.0
        AND 300.0 > fDistance[0]
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fDistance[0]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

	IF IS_CHAR_DEAD iEnemy[0]
        flag_enemy1_killed = TRUE
		REMOVE_BLIP iEnemyBlip[0]
    ENDIF        
	IF IS_CHAR_DEAD iEnemy[1]
        flag_enemy2_killed = TRUE
		REMOVE_BLIP iEnemyBlip[1]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[2]
        flag_enemy3_killed = TRUE
		REMOVE_BLIP iEnemyBlip[2]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[3]
        flag_enemy4_killed = TRUE
		REMOVE_BLIP iEnemyBlip[3]
    ENDIF                             
	IF IS_CHAR_DEAD iEnemy[4]
        flag_enemy5_killed = TRUE
		REMOVE_BLIP iEnemyBlip[4]
    ENDIF 

    IF NOT IS_CHAR_DEAD iEnemy[1] 
     
        GET_CHAR_COORDINATES iEnemy[1] (x[2] y[2] z[2])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[2] y[2] z[2]) (fDistance[1])

        IF fDistance[1] > 10.0
        AND 300.0 > fDistance[1]
            CONVERT_3D_TO_SCREEN_2D (x[2] y[2] z[2]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[2] 
     
        GET_CHAR_COORDINATES iEnemy[2] (x[3] y[3] z[3])
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[3] y[3] z[3]) (fDistance[2])

        IF fDistance[2] > 10.0
        AND 300.0 > fDistance[2]
            CONVERT_3D_TO_SCREEN_2D (x[3] y[3] z[3]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[2]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[3] 
     
        GET_CHAR_COORDINATES iEnemy[3] (x[4] y[4] z[4])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[4] y[4] z[4]) (fDistance[3])

        IF fDistance[3] > 10.0
        AND 300.0 > (fDistance[3])
            CONVERT_3D_TO_SCREEN_2D (x[4] y[4] z[4]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[3]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[4] 
     
        GET_CHAR_COORDINATES iEnemy[4] (x[5] y[5] z[5])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[5] y[5] z[5]) (fDistance[4])

        IF fDistance[4] > 10.0
        AND 300.0 > fDistance[4]
            CONVERT_3D_TO_SCREEN_2D (x[5] y[5] z[5]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[4]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF    

    IF DOES_PICKUP_EXIST iObj[4]
        GET_PICKUP_COORDINATES iObj[4] objX[0] objY[0] objZ[0]
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (objX[0] objY[0] objZ[0]) (fObjDistance[0])

        IF fObjDistance[0] > 10.0
        AND 300.0 > fObjDistance[0]
            CONVERT_3D_TO_SCREEN_2D (objX[0] objY[0] objZ[0]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 1 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fObjDistance[0]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF 
    ENDIF 

    IF DOES_BLIP_EXIST (iEventBlip)
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (347.559 165.272 1014.187) (fObjDistance[1])

        IF fObjDistance[1] > 10.0
        AND 300.0 > fObjDistance[1]
            CONVERT_3D_TO_SCREEN_2D (347.559 165.272 1014.787) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            DRAW_SPRITE idWay (v1 v2) (v3 v3) (255 255 255 235)
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fObjDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF 
    ENDIF             
//-+-------------------------------------------------------------------------
    IF NOT IS_CHAR_DEAD iEnemy[0]
        IF IS_CHAR_SHOOTING iEnemy[0]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp5.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP1 2500 1
                WAIT 2500    
                GOSUB discovered      
                RETURN
            ENDIF                         
        ENDIF               
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[1]  
        IF IS_CHAR_SHOOTING iEnemy[1]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP1 2500 1
                WAIT 2500    
                GOSUB discovered  
                RETURN    
            ENDIF                         
        ENDIF               
    ENDIF    

    IF NOT IS_CHAR_DEAD iEnemy[2]  
        IF IS_CHAR_SHOOTING iEnemy[2]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP1 2500 1
                WAIT 2500    
                GOSUB discovered
                RETURN      
            ENDIF                         
        ENDIF               
    ENDIF    

    IF NOT IS_CHAR_DEAD iEnemy[3]  
        IF IS_CHAR_SHOOTING iEnemy[3]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp5.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP1 2500 1
                WAIT 2500    
                GOSUB discovered   
                RETURN   
            ENDIF                         
        ENDIF               
    ENDIF    

    IF NOT IS_CHAR_DEAD iEnemy[4]  
        IF IS_CHAR_SHOOTING iEnemy[4]      
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	            GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                PRINT_NOW MSP1 2500 1
                WAIT 2500    
                GOSUB discovered  
                RETURN    
            ENDIF                         
        ENDIF               
    ENDIF  

	IF IS_CHAR_DEAD iEnemy[0]
        flag_enemy1_killed = TRUE
		REMOVE_BLIP iEnemyBlip[0]
    ENDIF        
	IF IS_CHAR_DEAD iEnemy[1]
        flag_enemy2_killed = TRUE
		REMOVE_BLIP iEnemyBlip[1]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[2]
        flag_enemy3_killed = TRUE
		REMOVE_BLIP iEnemyBlip[2]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[3]
        flag_enemy4_killed = TRUE
		REMOVE_BLIP iEnemyBlip[3]
    ENDIF                             
	IF IS_CHAR_DEAD iEnemy[4]
        flag_enemy5_killed = TRUE
		REMOVE_BLIP iEnemyBlip[4]
    ENDIF 

    IF IS_CHAR_DEAD player_actor
        GOSUB mission_failed
        WAIT 2000
        GOSUB mission_cleanup
        RETURN  
	ENDIF
	WAIT 0
GOTO mission_part2_A_1

init_part2_A_2:
ADD_SPRITE_BLIP_FOR_COORD (347.559 165.272 1014.187) RADAR_SPRITE_WAYPOINT (iEventBlip)
iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
GOSUB state_play_sfx    

mission_part2_A_2:
	IF LOCATE_STOPPED_CHAR_ON_FOOT_3D player_actor (347.559 165.272 1014.187) (1.0 1.0 1.0) TRUE
		flag_player_hit_counter = 0
		SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter       // 0:OFF || 1:ON			
		REMOVE_BLIP iEventBlip
		GOSUB sub_lock_player_controls
		fProgressTower = 0.0
		WHILE TRUE
			IF IS_BUTTON_PRESSED PAD1 CROSS		// ~k~~PED_SPRINT~
				TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "WASH_UP" "INT_HOUSE" 4.0 (1 0 0 0) 6000
				WAIT 0
				iTempVar2 = 0	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
				GOSUB state_play_sfx_bar_progress
				WHILE IS_BUTTON_PRESSED PAD1 CROSS	// ~k~~PED_SPRINT~
					fProgressTower +=@ 7.5
					IF fProgressTower >= 1000.00
						fProgressTower = 1000.0
						SET_AUDIO_STREAM_STATE sfx1 0	// -1|0:stop || 1:play || 2:pause || 3:resume
						iTempVar2 = 1	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
						GOSUB state_play_sfx_bar_progress
						WAIT 0
						GOSUB sub_unlock_player_controls
						flag_player_hit_counter = 1
						SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter       // 0:OFF || 1:ON							
						GOTO init_part2_B
					ENDIF
					GOSUB draw_tower_interface
					CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 218.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 603 2 0.0 0.0	//SINCRONIZANDO CON TORRE...
					GOSUB draw_key_press_tower
					WAIT 0
				ENDWHILE
				SET_AUDIO_STREAM_STATE sfx1 0	// -1|0:stop || 1:play || 2:pause || 3:resume
			ENDIF
			fProgressTower -=@ 15.0
			IF 0.0 > fProgressTower
				fProgressTower = 0.0
			ENDIF
			GOSUB draw_tower_interface
			CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 218.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 602 2 0.0 0.0	//SE DETECT� SE�AL CORRUPTA
			GOSUB draw_key_press_tower
			WAIT 0
		ENDWHILE
	ENDIF

//-+----------------------------3D Blips--------------------------------------------------------

    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
    IF NOT IS_CHAR_DEAD iEnemy[0] 
     
        GET_CHAR_COORDINATES iEnemy[0] (x[1] y[1] z[1])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance[0])

        IF fDistance[0] > 10.0
        AND 300.0 > fDistance[0]
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fDistance[0]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

	IF IS_CHAR_DEAD iEnemy[0]
        flag_enemy1_killed = TRUE
		REMOVE_BLIP iEnemyBlip[0]
    ENDIF        
	IF IS_CHAR_DEAD iEnemy[1]
        flag_enemy2_killed = TRUE
		REMOVE_BLIP iEnemyBlip[1]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[2]
        flag_enemy3_killed = TRUE
		REMOVE_BLIP iEnemyBlip[2]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[3]
        flag_enemy4_killed = TRUE
		REMOVE_BLIP iEnemyBlip[3]
    ENDIF                             
	IF IS_CHAR_DEAD iEnemy[4]
        flag_enemy5_killed = TRUE
		REMOVE_BLIP iEnemyBlip[4]
    ENDIF 

    IF NOT IS_CHAR_DEAD iEnemy[1] 
     
        GET_CHAR_COORDINATES iEnemy[1] (x[2] y[2] z[2])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[2] y[2] z[2]) (fDistance[1])

        IF fDistance[1] > 10.0
        AND 300.0 > fDistance[1]
            CONVERT_3D_TO_SCREEN_2D (x[2] y[2] z[2]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[2] 
     
        GET_CHAR_COORDINATES iEnemy[2] (x[3] y[3] z[3])
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[3] y[3] z[3]) (fDistance[2])

        IF fDistance[2] > 10.0
        AND 300.0 > fDistance[2]
            CONVERT_3D_TO_SCREEN_2D (x[3] y[3] z[3]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[2]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[3] 
     
        GET_CHAR_COORDINATES iEnemy[3] (x[4] y[4] z[4])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[4] y[4] z[4]) (fDistance[3])

        IF fDistance[3] > 10.0
        AND 300.0 > (fDistance[3])
            CONVERT_3D_TO_SCREEN_2D (x[4] y[4] z[4]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[3]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[4] 
     
        GET_CHAR_COORDINATES iEnemy[4] (x[5] y[5] z[5])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[5] y[5] z[5]) (fDistance[4])

        IF fDistance[4] > 10.0
        AND 300.0 > fDistance[4]
            CONVERT_3D_TO_SCREEN_2D (x[5] y[5] z[5]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[4]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF    

    IF DOES_PICKUP_EXIST iObj[4]
        GET_PICKUP_COORDINATES iObj[4] objX[0] objY[0] objZ[0]
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (objX[0] objY[0] objZ[0]) (fObjDistance[0])

        IF fObjDistance[0] > 10.0
        AND 300.0 > fObjDistance[0]
            CONVERT_3D_TO_SCREEN_2D (objX[0] objY[0] objZ[0]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 1 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fObjDistance[0]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF 
    ENDIF 

    IF DOES_BLIP_EXIST (iEventBlip)
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (347.559 165.272 1014.187) (fObjDistance[1])

        IF fObjDistance[1] > 10.0
        AND 300.0 > fObjDistance[1]
            CONVERT_3D_TO_SCREEN_2D (347.559 165.272 1014.787) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            DRAW_SPRITE idWay (v1 v2) (v3 v3) (255 255 255 235)
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fObjDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF 
    ENDIF             
//-+-------------------------------------------------------------------------
    
	WAIT 0
GOTO mission_part2_A_2

init_part2_B:
IF DOES_BLIP_EXIST iEventBlip
	REMOVE_BLIP iEventBlip
ENDIF
IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya8.mp3" (sfx2) 
    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	GET_AUDIO_SFX_VOLUME (fVolume)
	fVolume = 0.9
	SET_AUDIO_STREAM_VOLUME sfx2 fVolume
ENDIF        
PRINT_NOW PAYA8 3000 1
WAIT 1500
IF DOES_OBJECT_EXIST iObj[1]
    DELETE_OBJECT iObj[1]
ENDIF
IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
	STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 4 1 21 24 //{id} {mission_id} {text1_id} {text2_id}
ENDIF  
ADD_SPRITE_BLIP_FOR_COORD (351.3243 162.0824 1025.789) RADAR_SPRITE_WAYPOINT (iEventBlip)     
iTempVar1 = 1	// 0:combat end sfx || 1:blip sfx
GOSUB state_play_sfx 

mission_part2_B:
    IF LOCATE_CHAR_ANY_MEANS_3D player_actor 351.3243 162.0824 1025.789 3.0 3.0 3.0 FALSE 

        IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya9.mp3" (sfx2) 
            SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	        GET_AUDIO_SFX_VOLUME (fVolume)
    	    fVolume = 0.9
	        SET_AUDIO_STREAM_VOLUME sfx2 fVolume      
            PRINT_NOW PAYA9 3900 1
            WAIT 4000   
            GOTO mission_part2_C
        ENDIF  

    ELSE
        IF NOT IS_CHAR_DEAD iEnemy[0]
            IF IS_CHAR_SHOOTING iEnemy[0]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp5.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
	                fVolume = 0.9
	                SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered      
                    RETURN
                ENDIF                         
            ENDIF               
        ENDIF

        IF NOT IS_CHAR_DEAD iEnemy[1]  
            IF IS_CHAR_SHOOTING iEnemy[1]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
	                fVolume = 0.9
	                SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered  
                    RETURN    
                ENDIF                         
            ENDIF               
        ENDIF    

        IF NOT IS_CHAR_DEAD iEnemy[2]  
            IF IS_CHAR_SHOOTING iEnemy[2]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
	                fVolume = 0.9
    	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered
                    RETURN      
                ENDIF                         
            ENDIF               
        ENDIF    

        IF NOT IS_CHAR_DEAD iEnemy[3]  
            IF IS_CHAR_SHOOTING iEnemy[3]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp5.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
	                fVolume = 0.9
    	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered   
                    RETURN   
                ENDIF                         
            ENDIF               
        ENDIF    

        IF NOT IS_CHAR_DEAD iEnemy[4]  
            IF IS_CHAR_SHOOTING iEnemy[4]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
    	            fVolume = 0.9
	                SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered  
                    RETURN    
                ENDIF                         
            ENDIF    
        ENDIF             
    ENDIF     

	IF IS_CHAR_DEAD iEnemy[0]
        flag_enemy1_killed = TRUE
		REMOVE_BLIP iEnemyBlip[0]
    ENDIF        
	IF IS_CHAR_DEAD iEnemy[1]
        flag_enemy2_killed = TRUE
		REMOVE_BLIP iEnemyBlip[1]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[2]
        flag_enemy3_killed = TRUE
		REMOVE_BLIP iEnemyBlip[2]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[3]
        flag_enemy4_killed = TRUE
		REMOVE_BLIP iEnemyBlip[3]
    ENDIF                             
	IF IS_CHAR_DEAD iEnemy[4]
        flag_enemy5_killed = TRUE
		REMOVE_BLIP iEnemyBlip[4]
    ENDIF 

    IF IS_CHAR_DEAD player_actor
        GOSUB mission_failed
        WAIT 2000
        GOSUB mission_cleanup
        RETURN
    ENDIF       
//-+----------------------------3D Blips--------------------------------------------------------
    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
    IF NOT IS_CHAR_DEAD iEnemy[0] 
     
        GET_CHAR_COORDINATES iEnemy[0] (x[1] y[1] z[1])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance[0])

        IF fDistance[0] > 10.0
        AND 300.0 > fDistance[0]
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fDistance[0]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[1] 
     
        GET_CHAR_COORDINATES iEnemy[1] (x[2] y[2] z[2])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[2] y[2] z[2]) (fDistance[1])

        IF fDistance[1] > 10.0
        AND 300.0 > fDistance[1]
            CONVERT_3D_TO_SCREEN_2D (x[2] y[2] z[2]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[2] 
     
        GET_CHAR_COORDINATES iEnemy[2] (x[3] y[3] z[3])
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[3] y[3] z[3]) (fDistance[2])

        IF fDistance[2] > 10.0
        AND 300.0 > fDistance[2]
            CONVERT_3D_TO_SCREEN_2D (x[3] y[3] z[3]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[2]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[3] 
     
        GET_CHAR_COORDINATES iEnemy[3] (x[4] y[4] z[4])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[4] y[4] z[4]) (fDistance[3])

        IF fDistance[3] > 10.0
        AND 300.0 > (fDistance[3])
            CONVERT_3D_TO_SCREEN_2D (x[4] y[4] z[4]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[3]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF

    IF NOT IS_CHAR_DEAD iEnemy[4] 
     
        GET_CHAR_COORDINATES iEnemy[4] (x[5] y[5] z[5])  
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[5] y[5] z[5]) (fDistance[4])

        IF fDistance[4] > 10.0
        AND 300.0 > fDistance[4]
            CONVERT_3D_TO_SCREEN_2D (x[5] y[5] z[5]) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 0 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0            
            iTempVar =# fDistance[4]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF   
    ENDIF    

    IF DOES_BLIP_EXIST (iEventBlip)
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (351.3243 162.0824 1025.789) (fObjDistance[1])

        IF fObjDistance[1] > 10.0
        AND 300.0 > fObjDistance[1]
            CONVERT_3D_TO_SCREEN_2D (351.3243 162.0824 1025.789) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            DRAW_SPRITE idWay (v1 v2) (v3 v3) (255 255 255 235)
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fObjDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF 
    ENDIF  
//-+----------------------------------------------------------------------------------------------    

    WAIT 0
GOTO mission_part2_B

mission_part2_C:
    IF LOCATE_CHAR_ANY_MEANS_3D player_actor 353.147 161.991 1025.789 1.2 1.2 1.2 FALSE 
        REMOVE_BLIP (iEventBlip)  
        SET_SPRITES_DRAW_BEFORE_FADE TRUE       
        sx = 90.00
        sy = 56.00             
        DRAW_SPRITE idTip2 (50.0 420.0) (sx sy) (255 255 255 200)      
        CLEO_CALL GUI_DrawHelperText 0 (55.0 421.0) (29 2) (0.0 0.0)   // gxtId(i)|Format(i)|LeftPadding(f)|TopPadding(f)  
            
        IF IS_KEY_JUST_PRESSED VK_KEY_F
            ADD_EXPLOSION (346.204 160.3664 1024.789) 1
            WAIT 100
            GOSUB remove_chars
            DO_FADE 800 FADE_OUT
            WAIT 800       
            SWITCH_WIDESCREEN TRUE
            SET_FIXED_CAMERA_POSITION (362.2183 163.5193 1025.789 0.0 0.0 0.0)
            POINT_CAMERA_AT_POINT (376.5595 161.7801 1025.989) (2)  
	        flag_player_hit_counter = 0
            SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter       // 0:OFF || 1:ON	         
            CREATE_CHAR PEDTYPE_MISSION1 DNB3 378.1134 160.0734 1025.789 iEnemy[0]

            WAIT 1
            GIVE_WEAPON_TO_CHAR iEnemy[0] WEAPONTYPE_MICRO_UZI 99999
            SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[0] FALSE
            SET_CHAR_COORDINATES_NO_OFFSET player_actor (356.7773 162.1285 1025.796)
            SET_CHAR_HEADING player_actor 270.0
            WAIT 800 
            DO_FADE 800 FADE_IN

            OPEN_SEQUENCE_TASK iEventTask
            PERFORM_SEQUENCE_TASK iEnemy[0] iEventTask
            TASK_GO_STRAIGHT_TO_COORD iEnemy[0] (378.2163 162.4003 1025.789) 6 6000
            TASK_GO_STRAIGHT_TO_COORD iEnemy[0] (365.417 162.1681 1025.789) 6 6000
            CLOSE_SEQUENCE_TASK iEventTask
            GOSUB create_enemys_phase2
            WAIT 3000
            SWITCH_WIDESCREEN FALSE

    		flag_player_hit_counter = 1
    	    SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter       // 0:OFF || 1:ON	          
            RESTORE_CAMERA_JUMPCUT
            SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[0] FALSE
	        ADD_BLIP_FOR_CHAR iEnemy[0] iEnemyBlip[0]                     
            flag_enemy1_killed = FALSE
            flag_enemy2_killed = FALSE
            flag_enemy3_killed = FALSE
            flag_enemy4_killed = FALSE
            flag_enemy5_killed = FALSE        
            flag_enemy6_killed = FALSE     
            flag_enemy7_killed = FALSE
            flag_enemy8_killed = FALSE       
            TASK_KILL_CHAR_ON_FOOT iEnemy[0] player_actor
            TASK_KILL_CHAR_ON_FOOT iEnemy[1] player_actor
            TASK_KILL_CHAR_ON_FOOT iEnemy[2] player_actor
            TASK_KILL_CHAR_ON_FOOT iEnemy[3] player_actor
            TASK_KILL_CHAR_ON_FOOT iEnemy[4] player_actor
            TASK_KILL_CHAR_ON_FOOT iEnemy[5] player_actor
            TASK_KILL_CHAR_ON_FOOT iEnemy[6] player_actor
            TASK_KILL_CHAR_ON_FOOT iEnemy[7] player_actor
            SET_RELATIONSHIP (4) (24) (0) 

            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
    	        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 4 1 21 25 //{id} {mission_id} {text1_id} {text2_id}
            ENDIF  

            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya11.mp3" (sfx2) 
                SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
        	    GET_AUDIO_SFX_VOLUME (fVolume)
	            fVolume = 0.9
                SET_AUDIO_STREAM_VOLUME sfx2 fVolume
                PRINT_NOW PAYA11 2600 1
                WAIT 2800  
                GOTO mission_part2_D           
            ENDIF       
        ENDIF              
    ELSE
        IF NOT IS_CHAR_DEAD iEnemy[0]
            IF IS_CHAR_SHOOTING iEnemy[0]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp5.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
	                fVolume = 0.9
	                SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered      
                    RETURN
                ENDIF                         
            ENDIF               
        ENDIF

        IF NOT IS_CHAR_DEAD iEnemy[1]  
            IF IS_CHAR_SHOOTING iEnemy[1]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
	                fVolume = 0.9
	                SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered  
                    RETURN    
                ENDIF                         
            ENDIF               
        ENDIF    

        IF NOT IS_CHAR_DEAD iEnemy[2]  
            IF IS_CHAR_SHOOTING iEnemy[2]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
	                fVolume = 0.9
    	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered
                    RETURN      
                ENDIF                         
            ENDIF               
        ENDIF    

        IF NOT IS_CHAR_DEAD iEnemy[3]  
            IF IS_CHAR_SHOOTING iEnemy[3]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp5.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
	                fVolume = 0.9
    	            SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered   
                    RETURN   
                ENDIF                         
            ENDIF               
        ENDIF    

        IF NOT IS_CHAR_DEAD iEnemy[4]  
            IF IS_CHAR_SHOOTING iEnemy[4]      
                IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\msp2.mp3" (sfx2) 
                    SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
	                GET_AUDIO_SFX_VOLUME (fVolume)
    	            fVolume = 0.9
	                SET_AUDIO_STREAM_VOLUME sfx2 fVolume               
                    PRINT_NOW MSP1 2500 1
                    WAIT 2500    
                    GOSUB discovered  
                    RETURN    
                ENDIF                         
            ENDIF    
        ENDIF             
    ENDIF      

	IF IS_CHAR_DEAD iEnemy[0]
        flag_enemy1_killed = TRUE
		REMOVE_BLIP iEnemyBlip[0]
    ENDIF        
	IF IS_CHAR_DEAD iEnemy[1]
        flag_enemy2_killed = TRUE
		REMOVE_BLIP iEnemyBlip[1]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[2]
        flag_enemy3_killed = TRUE
		REMOVE_BLIP iEnemyBlip[2]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[3]
        flag_enemy4_killed = TRUE
		REMOVE_BLIP iEnemyBlip[3]
    ENDIF                             
	IF IS_CHAR_DEAD iEnemy[4]
        flag_enemy5_killed = TRUE
		REMOVE_BLIP iEnemyBlip[4]
    ENDIF 

    IF IS_CHAR_DEAD player_actor
        GOSUB mission_failed
        WAIT 2000
        GOSUB mission_cleanup
        RETURN
    ENDIF  

    WAIT 0
GOTO mission_part2_C

mission_part2_D:
    IF flag_enemy1_killed = TRUE
    AND flag_enemy2_killed = TRUE
    AND flag_enemy3_killed = TRUE
    AND flag_enemy4_killed = TRUE
    AND flag_enemy5_killed = TRUE
    AND flag_enemy6_killed = TRUE
    AND flag_enemy7_killed = TRUE
    AND flag_enemy8_killed = TRUE
		iTempVar1 = 0	// 0:combat end sfx || 1:blip sfx
		GOSUB state_play_sfx    
        IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya12.mp3" (sfx2) 
            SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
            GET_AUDIO_SFX_VOLUME (fVolume)
            fVolume = 0.9
	        SET_AUDIO_STREAM_VOLUME sfx2 fVolume
            PRINT_NOW PAYA12 3700 1  
            GOSUB enemys_killed 
            WAIT 4000     
            ADD_BLIP_FOR_COORD 352.1167 215.0228 1008.383 (iEventBlip)
            GOTO mission_part3                
        ENDIF        
    ENDIF

	IF IS_CHAR_DEAD iEnemy[0]
        flag_enemy1_killed = TRUE
		REMOVE_BLIP iEnemyBlip[0]
    ENDIF        
	IF IS_CHAR_DEAD iEnemy[1]
        flag_enemy2_killed = TRUE
		REMOVE_BLIP iEnemyBlip[1]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[2]
        flag_enemy3_killed = TRUE
		REMOVE_BLIP iEnemyBlip[2]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[3]
        flag_enemy4_killed = TRUE
		REMOVE_BLIP iEnemyBlip[3]
    ENDIF                             
	IF IS_CHAR_DEAD iEnemy[4]
        flag_enemy5_killed = TRUE
		REMOVE_BLIP iEnemyBlip[4]
    ENDIF 
	IF IS_CHAR_DEAD iEnemy[5]
        flag_enemy6_killed = TRUE
		REMOVE_BLIP iEnemyBlip[5]
    ENDIF  
	IF IS_CHAR_DEAD iEnemy[6]
        flag_enemy7_killed = TRUE
		REMOVE_BLIP iEnemyBlip[6]
    ENDIF                   
	IF IS_CHAR_DEAD iEnemy[7]
        flag_enemy8_killed = TRUE
		REMOVE_BLIP iEnemyBlip[7]
    ENDIF  

    IF IS_CHAR_DEAD player_actor
        GOSUB mission_failed
        WAIT 0
        GOSUB mission_cleanup
        RETURN
    ENDIF      

    WAIT 0
GOTO mission_part2_D

mission_part3:
	IF IS_CHAR_DEAD iEnemy[0]
        flag_enemy1_killed = TRUE
		REMOVE_BLIP iEnemyBlip[0]
    ENDIF        
	IF IS_CHAR_DEAD iEnemy[1]
        flag_enemy2_killed = TRUE
		REMOVE_BLIP iEnemyBlip[1]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[2]
        flag_enemy3_killed = TRUE
		REMOVE_BLIP iEnemyBlip[2]
    ENDIF     
	IF IS_CHAR_DEAD iEnemy[3]
        flag_enemy4_killed = TRUE
		REMOVE_BLIP iEnemyBlip[3]
    ENDIF                             
	IF IS_CHAR_DEAD iEnemy[4]
        flag_enemy5_killed = TRUE
		REMOVE_BLIP iEnemyBlip[4]
    ENDIF 
	IF IS_CHAR_DEAD iEnemy[5]
        flag_enemy6_killed = TRUE
		REMOVE_BLIP iEnemyBlip[5]
    ENDIF  
	IF IS_CHAR_DEAD iEnemy[6]
        flag_enemy7_killed = TRUE
		REMOVE_BLIP iEnemyBlip[6]
    ENDIF                   
	IF IS_CHAR_DEAD iEnemy[7]
        flag_enemy8_killed = TRUE
		REMOVE_BLIP iEnemyBlip[7]
    ENDIF  

//-+----------------------------3D Blips Enemy--------------------------------------------------------
    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
    IF DOES_BLIP_EXIST (iEventBlip)
        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (352.1167 215.0228 1008.383) (fObjDistance[1])

        IF fObjDistance[1] > 10.0
        AND 300.0 > fObjDistance[1]
            CONVERT_3D_TO_SCREEN_2D (352.1167 215.0228 1008.383) TRUE TRUE (v1 v2) (v3 v3)
            GET_FIXED_XY_ASPECT_RATIO 12.0 12.0 (v3 v3)
            USE_TEXT_COMMANDS FALSE
            GET_HUD_COLOUR 11 r g b a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 6.5 8.0 0 0 0 a
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_RECT v1 v2 4.5 6.0 r g b a
            v1 -= 6.0
            v2 += 6.0                
            iTempVar =# fObjDistance[1]
            GOSUB GUI_TextFormat_Text
            USE_TEXT_COMMANDS FALSE
            DISPLAY_TEXT_WITH_NUMBER v1 v2 J16D440 iTempVar    //~1~ m
        ENDIF 
    ENDIF  
//-+-------------------------------------------------------------------------

    IF LOCATE_CHAR_ANY_MEANS_3D player_actor 352.1167 215.0228 1008.383 2.0 2.0 2.0 TRUE 
        REMOVE_BLIP (iEventBlip)
        REMOVE_SPHERE (iEventBlip2)
        GOTO init_part4
    ENDIF     

    IF IS_CHAR_DEAD player_actor
        GOSUB mission_failed
        WAIT 2000
        GOSUB mission_cleanup
        RETURN
    ENDIF      

    WAIT 0
GOTO mission_part3

init_part4:   
    DO_FADE 600 FADE_OUT
    WAIT 600
    SET_CHAR_HEADING player_actor 270.0    
    RESTORE_CAMERA_JUMPCUT  
    SET_AREA_VISIBLE 0            
    FORCE_WEATHER_NOW iWeather
    RELEASE_WEATHER
    SET_CHAR_AREA_VISIBLE player_actor FALSE                     
    WAIT 1000     
    WAIT 50
	SET_CHAR_COORDINATES_NO_OFFSET player_actor -1831.844 1045.885 113.2112            
    DO_FADE 600 FADE_IN   
    WAIT 600

    IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\sams\paya13.mp3" (sfx2) 
        SET_AUDIO_STREAM_STATE sfx2 1	// -1|0:stop || 1:play || 2:pause || 3:resume
        GET_AUDIO_SFX_VOLUME (fVolume)
	    fVolume = 0.9
	    SET_AUDIO_STREAM_VOLUME sfx2 fVolume
    ENDIF            
    GOSUB enemys_killed
    PRINT_NOW PAYA13 3900 1
    WAIT 3900
    GOSUB mission_passed

    IF IS_CHAR_DEAD player_actor
        GOSUB mission_failed
        WAIT 2000
        GOSUB mission_cleanup
        RETURN
    ENDIF  

RETURN

//-+-------------------------------------------------------------------------

//-+--- MISSION STUFF
readVars:
	GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
RETURN

enemys_killed:
	IF flag_enemy1_killed = TRUE
	AND flag_enemy2_killed = TRUE
	AND flag_enemy3_killed = TRUE   
	AND flag_enemy4_killed = TRUE
	AND flag_enemy5_killed = TRUE
	AND flag_enemy6_killed = TRUE   
	AND flag_enemy7_killed = TRUE
		kills_counter += 7
		RETURN_TRUE
		RETURN    
    ENDIF      
    WAIT 0
GOTO enemys_killed

mission_failed:
	GET_AUDIO_STREAM_STATE music_sfx1 (iTempVar3)
	IF iTempVar3 = 1	//playing
		CLEO_CALL decrease_volume 0 music_sfx1 0.75	 //max_volume 0.0-1.0
	ENDIF
	GET_AUDIO_STREAM_STATE music_sfx2 (iTempVar3)
	IF iTempVar3 = 1	//playing
		CLEO_CALL decrease_volume 0 music_sfx2 0.75	 //max_volume 0.0-1.0
	ENDIF
	IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
		STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 1	//{id}
        WAIT 2000
    ENDIF
RETURN

mission_passed:
	kills_counter *= 15
	iTempVar1 = 140
	counter = 0
	counter = iTempVar1 + kills_counter
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prtb.cs"
        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prtb.cs" 5 counter iTempVar1 kills_counter    //{id} {total xp} {mission xp} {combat xp}
		GET_AUDIO_STREAM_STATE music_sfx1 (iTempVar3)
		IF iTempVar3 = 1	//playing
			CLEO_CALL decrease_volume 0 music_sfx1 0.75	 //max_volume 0.0-1.0
		ENDIF
        WAIT 2000
    ENDIF
    SET_CLEO_SHARED_VAR varStatusLevelChar counter   //set value of +250
RETURN

mission_cleanup:
	flag_player_on_mission = 0
	SET_CLEO_SHARED_VAR varOnmission flag_player_on_mission        // 0:OFF || 1:ON

	IF DOES_OBJECT_EXIST iObj[0]
		DELETE_OBJECT iObj[0]
	ENDIF
	IF DOES_OBJECT_EXIST iObj[1]
		DELETE_OBJECT iObj[1]
	ENDIF
	IF DOES_OBJECT_EXIST iObj[2]
		DELETE_OBJECT iObj[2]
	ENDIF
	IF DOES_OBJECT_EXIST iObj[3]
		DELETE_OBJECT iObj[3]
	ENDIF
	IF DOES_OBJECT_EXIST iObj[4]
		DELETE_OBJECT iObj[4]
	ENDIF
    	
	IF DOES_OBJECT_EXIST iCrates[0]
		DELETE_OBJECT iCrates[0]
	ENDIF
	IF DOES_OBJECT_EXIST iCrates[1]
		DELETE_OBJECT iCrates[1]
	ENDIF
	IF DOES_OBJECT_EXIST iCrates[2]
		DELETE_OBJECT iCrates[2]
	ENDIF
	IF DOES_OBJECT_EXIST iCrates[3]
		DELETE_OBJECT iCrates[3]
	ENDIF                    
	REMOVE_AUDIO_STREAM music_sfx1
	REMOVE_AUDIO_STREAM music_sfx2

	IF DOES_CHAR_EXIST iEnemy[0]
		DELETE_CHAR iEnemy[0]
	ENDIF
	IF DOES_CHAR_EXIST iEnemy[1]
		DELETE_CHAR iEnemy[1]
	ENDIF
	IF DOES_CHAR_EXIST iEnemy[2]
		DELETE_CHAR iEnemy[2]
	ENDIF
	IF DOES_CHAR_EXIST iEnemy[3]
		DELETE_CHAR iEnemy[3]
	ENDIF
	IF DOES_CHAR_EXIST iEnemy[4]
		DELETE_CHAR iEnemy[4]
	ENDIF
	IF DOES_CHAR_EXIST iEnemy[5]
		DELETE_CHAR iEnemy[5]
	ENDIF
	IF DOES_CHAR_EXIST iEnemy[6]
		DELETE_CHAR iEnemy[6]
	ENDIF
	IF DOES_CHAR_EXIST iEnemy[7]
		DELETE_CHAR iEnemy[7]
	ENDIF               

    IF DOES_BLIP_EXIST iEventBlip
	    REMOVE_BLIP iEventBlip
    ENDIF

	IF DOES_BLIP_EXIST iEnemyBlip[0]
		REMOVE_BLIP iEnemyBlip[0]
	ENDIF
	IF DOES_BLIP_EXIST iEnemyBlip[1]
		REMOVE_BLIP iEnemyBlip[1]
	ENDIF
	IF DOES_BLIP_EXIST iEnemyBlip[2]
		REMOVE_BLIP iEnemyBlip[2]
	ENDIF        
	IF DOES_BLIP_EXIST iEnemyBlip[3]
		REMOVE_BLIP iEnemyBlip[3]
	ENDIF
	IF DOES_BLIP_EXIST iEnemyBlip[4]
		REMOVE_BLIP iEnemyBlip[4]
	ENDIF
	IF DOES_BLIP_EXIST iEnemyBlip[5]
		REMOVE_BLIP iEnemyBlip[5]
	ENDIF
	IF DOES_BLIP_EXIST iEnemyBlip[6]
		REMOVE_BLIP iEnemyBlip[6]
	ENDIF
	IF DOES_BLIP_EXIST iEnemyBlip[7]
		REMOVE_BLIP iEnemyBlip[7]
	ENDIF                    


	REMOVE_ANIMATION "INT_HOUSE"
	REMOVE_ANIMATION "spider"
	REMOVE_ANIMATION "mweb"
	MARK_MODEL_AS_NO_LONGER_NEEDED 1581
    MARK_MODEL_AS_NO_LONGER_NEEDED 1224
    UNLOAD_SPECIAL_CHARACTER 9
	MARK_MODEL_AS_NO_LONGER_NEEDED DNB1 
	MARK_MODEL_AS_NO_LONGER_NEEDED DNB2 
	MARK_MODEL_AS_NO_LONGER_NEEDED DNB3 
	MARK_MODEL_AS_NO_LONGER_NEEDED MICRO_UZI
	MARK_MODEL_AS_NO_LONGER_NEEDED COLT45 
	USE_TEXT_COMMANDS FALSE
	WAIT 0
	REMOVE_TEXTURE_DICTIONARY
	
	MISSION_HAS_FINISHED	//cleanup
RETURN

discovered:
    DO_FADE 600 FADE_OUT
    WAIT 600
    SET_CHAR_HEADING player_actor 270.0    
    RESTORE_CAMERA_JUMPCUT  
    SET_AREA_VISIBLE 0            
    FORCE_WEATHER_NOW iWeather
    RELEASE_WEATHER
    SET_CHAR_AREA_VISIBLE player_actor FALSE                     
    WAIT 1000     
    WAIT 50
	SET_CHAR_COORDINATES_NO_OFFSET player_actor -1831.844 1045.885 113.2112            
    DO_FADE 600 FADE_IN   
    WAIT 600
    PRINT_NOW PAYAFA 3500 1
    GOSUB mission_failed
RETURN

//-+--- GOSUB HELPERS
draw_tower_interface:
    sx = 220.0
    sy = 180.0
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE FALSE
    DRAW_SPRITE idTowerB (460.0 200.0) (sx sy) (255 255 255 230)
	CLEO_CALL GUI_DrawBoxOutline_WithText 0 (410.0 154.0) (164.5 20.0) (0 0 0 0) (1.0) (0 0 0 0) (255 255 253 230) 26 19 0.0 0.0	//TORRE DE VIGILANCIA
	CLEO_CALL barFunc 0 fProgressTower 390.0 205.0
RETURN

draw_key_press_tower:
    IF IS_PC_USING_JOYPAD
        iTempVar1 = 506     //
    ELSE
        iTempVar1 = 505    //~b~~h~~k~~PED_SPRINT~~w~ Manten presionado.
    ENDIF
	sx = 164.5
    sy = 20.0
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 (463.5 238.5) (sx sy) (19 18 13 50) (1.0) (0 0 0 0) (255 255 253 230) iTempVar1 9 0.0 0.0
    USE_TEXT_COMMANDS FALSE
RETURN

sub_lock_player_controls:
	RESTORE_CAMERA_JUMPCUT 
	CLEAR_CHAR_TASKS player_actor
	SET_PLAYER_CONTROL player FALSE
	SET_EVERYONE_IGNORE_PLAYER player TRUE 
	SWITCH_WIDESCREEN TRUE
RETURN

sub_unlock_player_controls:
	RESTORE_CAMERA_JUMPCUT 
	SET_CAMERA_BEHIND_PLAYER
    CLEAR_CHAR_TASKS player_actor
	SET_PLAYER_CONTROL player TRUE
	SET_EVERYONE_IGNORE_PLAYER player FALSE 
	SWITCH_WIDESCREEN FALSE
RETURN

sub_Fade_out_500ms:
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_OUT
    WHILE GET_FADING_STATUS
        WAIT 0 
    ENDWHILE
RETURN

sub_Fade_in_500ms:
    SET_FADING_COLOUR 0 0 0 
    DO_FADE 500 FADE_IN
    WHILE GET_FADING_STATUS
        WAIT 0 
    ENDWHILE
RETURN 

is_spider_hud_enabled:
    GET_CLEO_SHARED_VAR varHUD (iTempVar3)
    IF iTempVar3 = 1     // 0:OFF || 1:ON            
        GET_CLEO_SHARED_VAR varHudRadar (iTempVar3)  //display indicator only if radar is enabled
        IF iTempVar = 1
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

state_play_sfx:	// 0:combat end sfx || 1:blip sfx
    SWITCH iTempVar1
        CASE 0
			REMOVE_AUDIO_STREAM sfx1
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\combat_end.mp3" (sfx1) 
                SET_AUDIO_STREAM_STATE sfx1 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				GET_AUDIO_SFX_VOLUME (fVolume)
				fVolume = 0.9
				SET_AUDIO_STREAM_VOLUME sfx1 fVolume
            ENDIF
        BREAK
        CASE 1
			REMOVE_AUDIO_STREAM sfx3
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\blip.mp3" (sfx3) 
                SET_AUDIO_STREAM_STATE sfx3 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx3 fVolume
            ENDIF
        BREAK
    ENDSWITCH
RETURN


state_play_sfx_bar_progress:
	REMOVE_AUDIO_STREAM sfx1
    SWITCH iTempVar2	// 0:sfx_bar||1:sfx_succesful||2:sfx_tower
        CASE 0
			IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\tsp1.mp3" (sfx1) 
				SET_AUDIO_STREAM_LOOPED sfx1 1
				SET_AUDIO_STREAM_STATE sfx1 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				//SET_AUDIO_STREAM_VOLUME sfx1 0.80
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx1 fVolume
			ENDIF
        BREAK
        CASE 1
			IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\tsp2.mp3" (sfx1) 
				SET_AUDIO_STREAM_STATE sfx1 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				//SET_AUDIO_STREAM_VOLUME sfx1 0.80
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx1 fVolume
			ENDIF
        BREAK
		CASE 2
			IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\tsp3.mp3" (sfx1) 
				SET_AUDIO_STREAM_STATE sfx1 1	// -1|0:stop || 1:play || 2:pause || 3:resume
				//SET_AUDIO_STREAM_VOLUME sfx1 0.80
				GET_AUDIO_SFX_VOLUME (fVolume)
				SET_AUDIO_STREAM_VOLUME sfx1 fVolume
			ENDIF
			BREAK
    ENDSWITCH
RETURN

create_enemys_phase1:
	CREATE_CHAR PEDTYPE_MISSION1 DNB1 (386.5331 173.7619 1008.383) iEnemy[0]
	SET_CHAR_HEADING iEnemy[0] 90.0
	GIVE_WEAPON_TO_CHAR iEnemy[0] WEAPONTYPE_M4 99999 
	FLUSH_PATROL_ROUTE 
	EXTEND_PATROL_ROUTE 362.3208 173.6776 1008.383 "ROADCROSS" "PED" 
	EXTEND_PATROL_ROUTE 386.5331 173.7619 1008.383 "NONE" "NONE" 
	TASK_FOLLOW_PATROL_ROUTE iEnemy[0] 4 2 

	CREATE_CHAR PEDTYPE_MISSION1 DNB2 (363.7758 170.7715 1014.188) iEnemy[1]
	SET_CHAR_HEADING iEnemy[1] 90.0
	GIVE_WEAPON_TO_CHAR iEnemy[1] WEAPONTYPE_MICRO_UZI 99999 
	FLUSH_PATROL_ROUTE 
	EXTEND_PATROL_ROUTE 338.6635 170.9095 1014.188 "ROADCROSS" "PED" 
	EXTEND_PATROL_ROUTE 363.7758 170.7715 1014.188 "NONE" "NONE" 
	TASK_FOLLOW_PATROL_ROUTE iEnemy[1] 4 2 	

	CREATE_CHAR PEDTYPE_MISSION1 DNB3 (337.524 155.1443 1014.18) iEnemy[2]
	GIVE_WEAPON_TO_CHAR iEnemy[2] WEAPONTYPE_AK47 99999 
	FLUSH_PATROL_ROUTE 
	EXTEND_PATROL_ROUTE 337.2095 169.5428 1014.188 "ROADCROSS" "PED" 
	EXTEND_PATROL_ROUTE 337.524 155.1443 1014.18 "NONE" "NONE" 
	TASK_FOLLOW_PATROL_ROUTE iEnemy[2] 4 2 		

	CREATE_CHAR PEDTYPE_MISSION1 DNB1 (368.7614 162.2794 1019.984) iEnemy[3]
	SET_CHAR_HEADING iEnemy[3] 90.0
	GIVE_WEAPON_TO_CHAR iEnemy[3] WEAPONTYPE_MICRO_UZI 99999 
	
	CREATE_CHAR PEDTYPE_MISSION1 DNB3 (370.5349 162.6643 1025.789) iEnemy[4]
	SET_CHAR_HEADING iEnemy[4] 270.0
	GIVE_WEAPON_TO_CHAR iEnemy[4] WEAPONTYPE_AK47 99999 
	FLUSH_PATROL_ROUTE 
	EXTEND_PATROL_ROUTE 377.9289 163.0574 1025.789 "ROADCROSS" "PED" 
	EXTEND_PATROL_ROUTE 370.5349 162.6643 1025.789 "NONE" "NONE" 
	TASK_FOLLOW_PATROL_ROUTE iEnemy[4] 4 2 			

	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[0] FALSE
	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[1] FALSE
	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[2] FALSE
	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[3] FALSE
	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[4] FALSE

	ADD_BLIP_FOR_CHAR iEnemy[0] iEnemyBlip[0]
	ADD_BLIP_FOR_CHAR iEnemy[1] iEnemyBlip[1]
	ADD_BLIP_FOR_CHAR iEnemy[2] iEnemyBlip[2]
	ADD_BLIP_FOR_CHAR iEnemy[3] iEnemyBlip[3]
	ADD_BLIP_FOR_CHAR iEnemy[4] iEnemyBlip[4]
RETURN

create_enemys_phase2:
	CREATE_CHAR PEDTYPE_MISSION1 DNB1 (378.3238 153.0898 1023.789) iEnemy[1]
	GIVE_WEAPON_TO_CHAR iEnemy[1] WEAPONTYPE_PISTOL 99999 
    SET_CHAR_STAY_IN_SAME_PLACE iEnemy[1] TRUE

	CREATE_CHAR PEDTYPE_MISSION1 DNB2 (371.4269 162.3932 1019.984) iEnemy[2]
	SET_CHAR_HEADING iEnemy[2] 180.0
	GIVE_WEAPON_TO_CHAR iEnemy[2] WEAPONTYPE_MICRO_UZI 99999 
    SET_CHAR_STAY_IN_SAME_PLACE iEnemy[2] TRUE

	CREATE_CHAR PEDTYPE_MISSION1 DNB3 (379.2331 153.6626 1017.984) iEnemy[3]
	GIVE_WEAPON_TO_CHAR iEnemy[3] WEAPONTYPE_PISTOL 99999 
    SET_CHAR_STAY_IN_SAME_PLACE iEnemy[3] TRUE

	CREATE_CHAR PEDTYPE_MISSION1 DNB1 (371.6941 153.3649 1015.984) iEnemy[4]
	SET_CHAR_HEADING iEnemy[4] 270.0
	GIVE_WEAPON_TO_CHAR iEnemy[4] WEAPONTYPE_MICRO_UZI 99999 
    SET_CHAR_STAY_IN_SAME_PLACE iEnemy[4] TRUE
	
	CREATE_CHAR PEDTYPE_MISSION1 DNB2 (371.8807 162.8354 1014.188) iEnemy[5]
	SET_CHAR_HEADING iEnemy[5] 180.0
	GIVE_WEAPON_TO_CHAR iEnemy[5] WEAPONTYPE_MICRO_UZI 99999 
    SET_CHAR_STAY_IN_SAME_PLACE iEnemy[5] TRUE		

	CREATE_CHAR PEDTYPE_MISSION1 DNB3 (371.9805 153.3409 1010.188) iEnemy[6]
	SET_CHAR_HEADING iEnemy[6] 270.0
	GIVE_WEAPON_TO_CHAR iEnemy[6] WEAPONTYPE_PISTOL 99999 
    SET_CHAR_STAY_IN_SAME_PLACE iEnemy[6] TRUE

	CREATE_CHAR PEDTYPE_MISSION1 DNB3 (367.3557 175.9177 1008.383) iEnemy[7]
	SET_CHAR_HEADING iEnemy[7] 208.0
	GIVE_WEAPON_TO_CHAR iEnemy[7] WEAPONTYPE_PISTOL 99999 
    SET_CHAR_STAY_IN_SAME_PLACE iEnemy[7] TRUE        	

	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[1] FALSE
	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[2] FALSE
	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[3] FALSE
	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[4] FALSE
	SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[5] FALSE
    SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[6] FALSE
    SET_CHAR_DROPS_WEAPONS_WHEN_DEAD iEnemy[7] FALSE

	ADD_BLIP_FOR_CHAR iEnemy[1] iEnemyBlip[1]
	ADD_BLIP_FOR_CHAR iEnemy[2] iEnemyBlip[2]
	ADD_BLIP_FOR_CHAR iEnemy[3] iEnemyBlip[3]
	ADD_BLIP_FOR_CHAR iEnemy[4] iEnemyBlip[4]
	ADD_BLIP_FOR_CHAR iEnemy[5] iEnemyBlip[5]
    ADD_BLIP_FOR_CHAR iEnemy[6] iEnemyBlip[6]
    ADD_BLIP_FOR_CHAR iEnemy[7] iEnemyBlip[7]
RETURN

remove_chars:
    IF DOES_BLIP_EXIST iEnemyBlip[0]
        REMOVE_BLIP iEnemyBlip[0]
    ENDIF
    IF DOES_BLIP_EXIST iEnemyBlip[1]
        REMOVE_BLIP iEnemyBlip[1]
    ENDIF
    IF DOES_BLIP_EXIST iEnemyBlip[2]
        REMOVE_BLIP iEnemyBlip[2]
    ENDIF
    IF DOES_BLIP_EXIST iEnemyBlip[3]
        REMOVE_BLIP iEnemyBlip[3]
    ENDIF
    IF DOES_BLIP_EXIST iEnemyBlip[4]
        REMOVE_BLIP iEnemyBlip[4]
    ENDIF   
    IF DOES_BLIP_EXIST iEnemyBlip[5]
        REMOVE_BLIP iEnemyBlip[5]
    ENDIF    
    IF DOES_BLIP_EXIST iEnemyBlip[6]
        REMOVE_BLIP iEnemyBlip[6]
    ENDIF    
    IF DOES_BLIP_EXIST iEnemyBlip[7]
        REMOVE_BLIP iEnemyBlip[7]
    ENDIF                      
    IF DOES_CHAR_EXIST iEnemy[0]
        DELETE_CHAR iEnemy[0]
    ENDIF
    IF DOES_CHAR_EXIST iEnemy[1]
        DELETE_CHAR iEnemy[1]
    ENDIF
    IF DOES_CHAR_EXIST iEnemy[2]
        DELETE_CHAR iEnemy[2]
    ENDIF
    IF DOES_CHAR_EXIST iEnemy[3]
        DELETE_CHAR iEnemy[3]
    ENDIF
    IF DOES_CHAR_EXIST iEnemy[4]
        DELETE_CHAR iEnemy[4]
    ENDIF      
    IF DOES_CHAR_EXIST iEnemy[5]
        DELETE_CHAR iEnemy[5]
    ENDIF
    IF DOES_CHAR_EXIST iEnemy[6]
        DELETE_CHAR iEnemy[6]
    ENDIF 
    IF DOES_CHAR_EXIST iEnemy[7]
        DELETE_CHAR iEnemy[7]
    ENDIF                
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
}

//--+--- CALLSCM HELPERS
{
//CLEO_CALL barFunc 0 fSize posx posy	//max size 1000.0
barFunc:
    LVAR_FLOAT sizeBar posx posy  // In
    LVAR_FLOAT copyPosX xSize
    xSize = sizeBar
    xSize /= 1000.0 	//fresX
    xSize *= 146.0
    copyPosX = xSize
    copyPosX /= 2.0
    copyPosX += posx 	//270+(120/2)= 330
	USE_TEXT_COMMANDS FALSE
    DRAW_RECT (copyPosX posy) (xSize 2.5) (11 255 187 210)
CLEO_RETURN 0
}
{
//CLEO_CALL setCharVelocity 0 player_actor /*offset*/ 0.0 1.0 1.0 /*amplitude*/ 5.0
setCharVelocity:
    LVAR_INT scplayer   //in
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
    WAIT 0
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
CLEO_RETURN 0
}
{
//CLEO_CALL sync_music_to_game_sfx 0 sfx fMaxVolume
sync_music_to_game_sfx:
    LVAR_INT sfx //IN
    LVAR_FLOAT fMaxVolume //IN
    LVAR_FLOAT fGameVolume
	GET_AUDIO_SFX_VOLUME (fGameVolume)
    fMaxVolume *= fGameVolume
    SET_AUDIO_STREAM_VOLUME sfx fMaxVolume
CLEO_RETURN 0
}
{
//CLEO_CALL increase_volume 0 sfx fMaxVolume
increase_volume:
    LVAR_INT sfx    //IN
    LVAR_FLOAT fMaxVolume   //IN
    LVAR_FLOAT fVolume fGameVolume
    fVolume = 0.0
	GET_AUDIO_SFX_VOLUME (fGameVolume)
    fMaxVolume *= fGameVolume
    SET_AUDIO_STREAM_VOLUME sfx fVolume
    WHILE fVolume < fMaxVolume
        fVolume +=@ 0.05
        IF fVolume > 0.5
            fVolume = fMaxVolume
        ENDIF
        SET_AUDIO_STREAM_VOLUME sfx fVolume
        WAIT 0
    ENDWHILE
    SET_AUDIO_STREAM_VOLUME sfx fVolume
CLEO_RETURN 0
}
{
//CLEO_CALL decrease_volume 0 sfx fMaxVolume
decrease_volume:
    LVAR_INT sfx    //IN
    LVAR_FLOAT fMaxVolume   //IN
    LVAR_FLOAT fVolume fGameVolume
	GET_AUDIO_SFX_VOLUME (fGameVolume)
    fMaxVolume *= fGameVolume
    fVolume = fMaxVolume
    SET_AUDIO_STREAM_VOLUME sfx fVolume
    WHILE fVolume > 0.1 
        fVolume -=@ 0.05
        IF 0.0 > fVolume
            fVolume = 0.0
        ENDIF
        SET_AUDIO_STREAM_VOLUME sfx fVolume
        WAIT 0
    ENDWHILE
    SET_AUDIO_STREAM_VOLUME sfx 0.0
CLEO_RETURN 0
}
//-+--- Shine GUI
{
//CLEO_CALL GUI_DrawBoxOutline_WithText 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*ouline*/(1.4 1 1 1 1 200 200 200 200) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0
GUI_DrawBoxOutline_WithText:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_FLOAT outlineSize 
LVAR_INT outlineTop outlineRight outlineBottom outlineLeft outlineR outlineG outlineB outlineA textId formatId
LVAR_FLOAT paddingLeft paddingTop

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Outlines
IF outlineLeft = TRUE //outline side left
    f = sizeX / 2.0  
    h = posX - f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT h posY outlineSize sizeY (outlineR outlineG outlineB outlineA)
ENDIF    
IF outlineTop = TRUE //outline side top
    f = sizeY / 2.0  
    h = posY - f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX h sizeX outlineSize (outlineR outlineG outlineB outlineA)
ENDIF  
IF outlineRight = TRUE //outline side right
    f = sizeX / 2.0    
    h = posX + f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT h posY outlineSize sizeY (outlineR outlineG outlineB outlineA)
ENDIF    
IF outlineBottom = TRUE //outline side bottom
    f = sizeY / 2.0  
    h = posY + f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX h sizeX outlineSize (outlineR outlineG outlineB outlineA)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "JDSM%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //Menu Item
            formatId = 8 //Menu Item ACTIVE
        ENDIF  
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT posX posY $gxt
ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_DrawBox_WithNumber 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0 /*number*/ 5
GUI_DrawBox_WithNumber:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_INT textId formatId
LVAR_FLOAT paddingLeft paddingTop
LVAR_INT iNumber

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "JDSM%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left  
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //
            formatId = 8 // ACTIVE
        ENDIF
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF  
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT_WITH_NUMBER (posX posY) $gxt iNumber
ENDIF
CLEO_RETURN 0
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
    STRING_FORMAT gxt "JDSM%i" textId
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
LVAR_INT i
LVAR_FLOAT g
LVAR_FLOAT xSize ySize
SWITCH formatId
    CASE 1
        GOSUB GUI_TextFormat_Title3_LeftMenu
        CLEO_RETURN 0 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_Title4_Suits  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 3
        GOSUB GUI_TextFormat_SmallMenu  
        CLEO_RETURN 0 5.0
        BREAK
    CASE 4
        GOSUB GUI_TextFormat_SmallMenu_Active  
        CLEO_RETURN 0 5.0
        BREAK
    CASE 5
        GOSUB GUI_TextFormat_Title2_Menu  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 6
        GOSUB GUI_TextFormat_Subtitle_Medium_Names  
        CLEO_RETURN 0 3.5
        BREAK
    CASE 7
        GOSUB GUI_TextFormat_Title1_Menu  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 8
        GOSUB GUI_TextFormat_Title1_Menu_Active  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 9
        GOSUB GUI_TextFormat_Text1_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 10
        GOSUB GUI_TextFormat_Number1_Big_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 11
        GOSUB GUI_TextFormat_Text2_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 12
        GOSUB GUI_TextFormat_Text3_Medium  
        CLEO_RETURN 0 3.0
        BREAK
    CASE 13
        GOSUB GUI_TextFormat_Text4_Big  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 14
        GOSUB GUI_TextFormat_Number2_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 15
        GOSUB GUI_TextFormat_Number3_Small  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 16
        GOSUB GUI_TextFormat_Title5_Map  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 17
        GOSUB GUI_TextFormat_Text5_List_Map  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 18
        GOSUB GUI_TextFormat_Interface_Main_Title  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 19
        GOSUB GUI_TextFormat_Interface_Normal_Text  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 20
        GOSUB GUI_TextFormat_Interface_A_Text  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 21
        GOSUB GUI_TextFormat_Interface_Number_Big  
        CLEO_RETURN 0 0.0
        BREAK
ENDSWITCH

GUI_TextFormat_Title3_LeftMenu:    //1   Title 3 - Suit/SuitMods  (BLUE-SHINY)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1 
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN    

GUI_TextFormat_Title4_Suits:     //2     Title 4 - Suits Matrix  (BLUE-SHINY)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.28 1.05 (xSize ySize)    //0.21 0.98
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
    /*
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT 2
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 0 0 0 100
    SET_TEXT_SCALE 0.3 1.0
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
    */
RETURN

GUI_TextFormat_SmallMenu: //3
    SET_TEXT_COLOUR 240 240 240 255  
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.27 1.07 (xSize ySize)    //0.2 1.0
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_SmallMenu_Active: //4    
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.27 1.07 (xSize ySize)    //0.2 1.0
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN

GUI_TextFormat_Title2_Menu:  //5   Title 2  (GRAY)
    SET_TEXT_FONT FONT_SUBTITLES
    GET_FIXED_XY_ASPECT_RATIO 0.29 1.09 (xSize ySize)    //0.22 1.02
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 200 200 200 255
    SET_TEXT_EDGE 1 (0 0 0 200)
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN  

GUI_TextFormat_Subtitle_Medium_Names:  //6  Text Medium / Names  (LIGHT-BLUE)
    SET_TEXT_FONT FONT_SUBTITLES
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 50 180 255 200
    SET_TEXT_EDGE 1 (0 0 0 200)
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN  

GUI_TextFormat_Title1_Menu:   //7  Title 1    (WHITE-GRAY)
    SET_TEXT_COLOUR 240 240 240 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Title1_Menu_Active:  //8  Title 1 Active   (LIGHT-BLUE)
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN

GUI_TextFormat_Text1_Small_Colour:  //9   (BLUE)
    SET_TEXT_COLOUR 0 95 160 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Number1_Big_Colour:  //10  BIG Numbers for Level (BLUE-SHINY)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.74 2.73 (xSize ySize)    //0.55 2.55
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text2_Small_Colour:  //11   LEVEL letters small (BLUE-SHINY)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.19 0.7 (xSize ySize)    //0.14 0.65
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text3_Medium:  //12     Text Descriptions  (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text4_Big:   //13     Text Names   (LIGHT-BLUE)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.38 1.45 (xSize ySize)    //0.28 1.35
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN   

GUI_TextFormat_Number2_Small_Colour:  //14   Level Numbers  (LIGHT-BLUE)
    SET_TEXT_COLOUR 6 253 244 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Number3_Small:  //15   Level XP Numbers  (WHITE)
    SET_TEXT_COLOUR 255 255 255 220  
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Title5_Map:  //16  Title Map   (DARK-BLUE)
    SET_TEXT_COLOUR 43 57 58 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (43 57 58 100)
    GET_FIXED_XY_ASPECT_RATIO 0.26 0.99 (xSize ySize)    //0.19 0.92
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text5_List_Map:  //17  Text List Map (MAGENTA)
    SET_TEXT_COLOUR 17 242 198 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.26 0.99 (xSize ySize)    //0.19 0.92
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Main_Title:  //18  Text BIG (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.35 1.35 (xSize ySize)    //0.26 1.26
    GET_FIXED_XY_ASPECT_RATIO 0.29 1.14 (xSize ySize)    //0.22 1.06
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Normal_Text:  //19  Text (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    GET_FIXED_XY_ASPECT_RATIO 0.26 1.04 (xSize ySize)    //0.20 0.97
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_A_Text:  //20  Text (BLUE)
    SET_TEXT_COLOUR 63 214 241 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    GET_FIXED_XY_ASPECT_RATIO 0.26 1.04 (xSize ySize)    //0.20 0.97
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Number_Big:  //21  Text (BLUE)
    SET_TEXT_COLOUR 63 214 241 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.4 1.55 (xSize ySize)    //0.30 1.45
    GET_FIXED_XY_ASPECT_RATIO 0.35 1.35 (xSize ySize)    //0.26 1.26
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
}
// --- Functions
{
GUI_GetPulseAlpha:
    LVAR_INT i
    LVAR_FLOAT g
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha i
    READ_MEMORY i 4 FALSE (g)
    i =# g
CLEO_RETURN 0 i
}
{
GUI_SetAtiveGXT:
    LVAR_INT item //In
    LVAR_INT i
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    WRITE_MEMORY i 4 item FALSE
CLEO_RETURN 0
}
{
GUI_Pulse_Update:
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    CONST_FLOAT ItemPulseSpeed 2.0
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    READ_MEMORY pAlpha 4 FALSE (fAlpha)
    READ_MEMORY pStep 1 FALSE (iStep)
    IF iStep = 1
        fAlpha -=@ ItemPulseSpeed  
        IF fAlpha < 180.0
            fAlpha = 180.0
            iStep = 2
        ENDIF
    ELSE //Up
        fAlpha +=@ ItemPulseSpeed  
        IF fAlpha > 255.0
            fAlpha = 255.0
            iStep = 1
        ENDIF
    ENDIF
    WRITE_MEMORY pAlpha 4 fAlpha FALSE
    WRITE_MEMORY pStep 1 iStep FALSE
CLEO_RETURN 0
}
{
GUI_Pulse_Reset:
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    WRITE_MEMORY pAlpha 4 255.0 FALSE
    WRITE_MEMORY pStep 1 1 FALSE
CLEO_RETURN 0
}
// Thread Memory
GUI_Memory_ActiveItem:
DUMP
00 00 00 00
ENDDUMP
  
GUI_Memory_ItemMenuActive_PulseAlpha:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_ItemMenuActive_PulseAlpha_Step:
DUMP
00
ENDDUMP
//-+-----------------------------------------------------------------


Buffer:	//8x2*4= 64
DUMP
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 
00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 
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

CONST_INT varAudioActive     	45    // 0:OFF || 1:ON  ||global var to check -spech- audio playing

//-+---
CONST_INT idTip2 19
CONST_INT idWay 25
CONST_INT idTowerB 55

