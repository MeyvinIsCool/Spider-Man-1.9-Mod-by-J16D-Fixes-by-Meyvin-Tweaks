// by J16D and Meyvin Tweaks
// Adapted by Meyvin Tweaks
// Spider-Man Mod for GTA SA c.2018 - 2022
// Hit Counter v2 - Events CLEO+
// Still In Development Stage !
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

CONST_INT player 0
CONST_INT reset_time 4000   //MS

SCRIPT_START
{
NOP
SCRIPT_NAME sp_hit
LVAR_INT player_actor toggleSpiderMod isInMainMenu toggleHUD hud_mode is_in_interior
LVAR_INT iTempVar iTempVar2 counter is_hud_enabled is_opening_door
LVAR_FLOAT x y z 
LVAR_INT eventArgVar iChar iHitCounter iLastCount pEntity iEntityType j
LVAR_INT flag_player_hit_counter

GET_PLAYER_CHAR 0 player_actor
is_opening_door = FALSE
is_hud_enabled = TRUE
CLEO_CALL get_screen_aspect_ratio 0 iTempVar    // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
CLEO_CALL storeCurrentAspectRatio 0 iTempVar
GOSUB REQUEST_Animations
iHitCounter = 0
iLastCount = 0
timera = 0
flag_player_hit_counter = 1
SET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter
SET_CLEO_SHARED_VAR varHitCount iHitCounter

main_loop:
    IF IS_PLAYER_PLAYING player
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE

            IF isInMainMenu = 0     //1:true 0: false
                
                IF toggleHUD = 1  // 0:OFF || 1:ON
                    GOSUB readVars
                    GOSUB hudCheck
                    GOSUB openDoorCheck
                    GOSUB activeInteriorCheck
                    IF IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                    OR IS_ON_CUTSCENE 
                    //OR IS_HUD_VISIBLE 
                    OR is_hud_enabled = FALSE
                    OR is_opening_door = TRUE
                        USE_TEXT_COMMANDS FALSE // don't show textures
                        SET_SCRIPT_EVENT_CHAR_DAMAGE OFF task_assign eventArgVar  // Turn OFF The Counting System
                    ELSE
                        SET_SCRIPT_EVENT_CHAR_DAMAGE ON task_assign eventArgVar   // Turn ON The Counting System                                
                    ENDIF
                    iLastCount = iHitCounter        // The Best Way I Could Find To Make This Script Work

                    WHILE toggleHUD = 1
                        GOSUB readVars
                        GOSUB openDoorCheck
                        //GOSUB hudCheck
                        //GOSUB activeInteriorCheck
                        IF IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                        OR IS_ON_CUTSCENE 
                        //OR IS_HUD_VISIBLE 
                        OR is_hud_enabled = TRUE
                        //OR is_opening_door = FALSE  
                            GOSUB drawHitCounter    // Hit Counting Starts
                        ENDIF      
 
                        GET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter
                        GET_CLEO_SHARED_VAR varHitCount iHitCounter     // Checks For Increase Value From Other Scripts
                        IF NOT iHitCounter = iLastCount
                            timera = 0                                  // Resets The Hit Counting Timer
                            iLastCount = iHitCounter                 
                        ENDIF

                        IF NOT is_in_interior = 0   // Disables IF Player Is In Interior
                            USE_TEXT_COMMANDS FALSE
                            WHILE NOT is_in_interior = 0     //1:true 0: false
                                GOSUB activeInteriorCheck
                            WAIT 1500
                            ENDWHILE   
                        ENDIF    
                         
                        IF isInMainMenu = 1         // Disables IF Is In Menu
                            USE_TEXT_COMMANDS FALSE
                            WHILE isInMainMenu = 1     //1:true 0: false
                                GOTO end_hud_script
                            WAIT 0
                            ENDWHILE       
                        ENDIF
                    ENDWHILE           
                ENDIF
                    
            ELSE
                end_hud_script:                     // End Hit Counting Script
                USE_TEXT_COMMANDS FALSE
                //DISPLAY_HUD TRUE
                USE_TEXT_COMMANDS FALSE               
                WAIT 25
                REMOVE_TEXTURE_DICTIONARY
                WAIT 0
                TERMINATE_THIS_CUSTOM_SCRIPT
            ENDIF
        ENDIF         
        WAIT 1
    ENDIF
GOTO main_loop

task_assign:
IF GET_CHAR_DAMAGE_LAST_FRAME eventArgVar (pEntity j j z)
    IF pEntity > 0x0
    AND NOT pEntity = player_actor
        GET_ENTITY_TYPE pEntity (iEntityType)
        IF iEntityType = ENTITY_TYPE_PED
            GET_PED_REF pEntity (iChar)
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.0 (x y z)
            ADD_BLOOD x y z 0.0 0.0 0.0 150 iChar
            IF player_actor = iChar                             //  Fix The Pedestrians Hits Count 
                iHitCounter ++             
                SET_CLEO_SHARED_VAR varHitCount iHitCounter
            ENDIF
        ENDIF
    ENDIF
ENDIF
RETURN_SCRIPT_EVENT

REQUEST_Animations:
    IF NOT HAS_ANIMATION_LOADED "spider"
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_Animations

drawHitCounter:   
    IF timera > reset_time
        iHitCounter = 0
        SET_CLEO_SHARED_VAR varHitCount iHitCounter
    ENDIF
    IF flag_player_hit_counter = 1
        IF iHitCounter > 0
            //CLEO_CALL GUI_display_text 0 40.0 140.0 1 2    //PARAMS: posX posY id_text id_format_text
            CLEO_CALL GUI_display_number 0 39.35 49.45 2 1 iHitCounter  //PARAMS: posX posY id_text id_format iNumber
        ELSE
            CLEO_CALL GUI_display_text 0 39.35 49.85 3 1    //PARAMS: posX posY id_text id_format_text
        ENDIF
    ENDIF 
    WAIT 0
RETURN

readVars:
    GET_CLEO_SHARED_VAR varHUD (toggleHUD)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
RETURN

activeInteriorCheck:
    GET_AREA_VISIBLE (is_in_interior)
RETURN

hudCheck:
    READ_MEMORY 0xBA6769 4 FALSE (hud_mode)
    IF hud_mode = FALSE
        is_hud_enabled = FALSE
    ELSE
        is_hud_enabled = TRUE
    ENDIF
RETURN

openDoorCheck:
    READ_MEMORY 0x96A7CC 4 FALSE (iTempVar2)
    IF iTempVar2 = 1
    OR iTempVar2 = 2
        is_opening_door = TRUE
    ELSE
        is_opening_door = FALSE
    ENDIF
RETURN

}
SCRIPT_END

//CALL SCM HELPERS
{
//CLEO_CALL getHudRadar 0 (var)
getHudRadar:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_radar_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL getCurrentAspectRatio 0 (var)
getCurrentAspectRatio:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER Screen_AspectRatio (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL setRadarPostion 0 40.0 104.0 94.0 76.0   //Left|Top|Width|Height  ||Default
setRadarPostion:
    LVAR_FLOAT rL rT rW rH    //in
    LVAR_INT pItem iRadarLeft iRadarTop iRadarWidth iRadarHeight iAlpha
    GET_LABEL_POINTER HudRadarAndComponentsCoords (pItem)
    iRadarLeft = pItem
    WRITE_MEMORY iRadarLeft 4 rL FALSE
    iRadarTop = pItem + 0x04
    WRITE_MEMORY iRadarTop 4 rT FALSE
    iRadarWidth = pItem + 0x08
    WRITE_MEMORY iRadarWidth 4 rW FALSE
    iRadarHeight = pItem + 0x0C
    WRITE_MEMORY iRadarHeight 4 rH FALSE
    // Map and sprites position relative to the radar rectangle.
    WRITE_MEMORY 0x005834C2 4 iRadarWidth TRUE
    WRITE_MEMORY 0x005834D4 4 iRadarLeft TRUE
    WRITE_MEMORY 0x005834F6 4 iRadarHeight TRUE
    WRITE_MEMORY 0x00583500 4 iRadarTop TRUE
CLEO_RETURN 0
}
{
//CLEO_CALL get_screen_aspect_ratio 0 var
get_screen_aspect_ratio:
    LVAR_FLOAT val[3] fResX fResY fAspectRatio
    LVAR_INT id
    CLEO_CALL getCurrentResolution 0 (fResX fResY)
    fAspectRatio = fResX
    fAspectRatio /= fResY
    val[0] = 16.0
    val[1] = 9.0
    val[2] = val[0]
    val[2] /= val[1]    //16:9
    IF fAspectRatio = val[2]    //16:9
        id = 1  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
        CLEO_RETURN 0 id
    ELSE
        val[0] = 4.0
        val[1] = 3.0
        val[2] = val[0]
        val[2] /= val[1]    //4:3
        IF fAspectRatio = val[2]    //4:3
            id = 2  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
            CLEO_RETURN 0 id
        ELSE
            val[0] = 16.0
            val[1] = 10.0
            val[2] = val[0]
            val[2] /= val[1]    //16:10
            IF fAspectRatio = val[2]    //16:10
                id = 3  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
                CLEO_RETURN 0 id
            ELSE
                val[0] = 5.0
                val[1] = 4.0
                val[2] = val[0]
                val[2] /= val[1]    //5:4
                IF fAspectRatio = val[2]    //5:4
                    id = 4  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
                    CLEO_RETURN 0 id
                ENDIF
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0 0
}
{
//CLEO_CALL getCurrentResolution 0 (fX fY)
getCurrentResolution:
    LVAR_INT iresX iresY
    LVAR_FLOAT fresX fresY
    GET_CURRENT_RESOLUTION (iresX iresY)
    fresX =# iresX
    fresY =# iresY
CLEO_RETURN 0 (fresX fresY)
}
{
//CLEO_CALL storeCurrentAspectRatio 0 var
storeCurrentAspectRatio:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER Screen_AspectRatio pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_display_text 0 posX posY id_text id_format_text
GUI_display_text:
    LVAR_FLOAT x y  //in
    LVAR_INT textId formatId   //in
    LVAR_TEXT_LABEL gxt
    STRING_FORMAT gxt "CMB_%i" textId
    CLEO_CALL GUI_text_format 0 formatId
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT (x y) $gxt
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_display_number 0 posX posY id_text id_format iNumber
GUI_display_number:
    LVAR_FLOAT x y  //in
    LVAR_INT textId formatId iNumber //in
    LVAR_TEXT_LABEL gxt
    STRING_FORMAT gxt "CMB_%i" textId
    CLEO_CALL GUI_text_format 0 formatId
    USE_TEXT_COMMANDS FALSE
    DISPLAY_TEXT_WITH_NUMBER (x y) $gxt iNumber
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_text_format 0 id_format 
GUI_text_format:
    LVAR_INT formatId   //in
    LVAR_FLOAT c d
    SWITCH formatId
        CASE 1
            GOSUB GUI_TextFormat_number
            BREAK
        CASE 2
            GOSUB GUI_TextFormat_text
            BREAK
        DEFAULT
            GOSUB GUI_TextFormat_text
            BREAK            
    ENDSWITCH
CLEO_RETURN 0

GUI_TextFormat_number:
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (c d)
    SET_TEXT_SCALE c d
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_CENTRE TRUE
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_text:
    SET_TEXT_COLOUR 240 240 240 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (c d)
    SET_TEXT_SCALE c d
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_CENTRE TRUE
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
}

HudRadarAndComponentsCoords:
DUMP
    00002042    // (40.0f) fRadarLeft   
    0000D042    // (104.0f) fRadarTop   
    0000BC42    // (94.0f) fRadarWidth   
    00009842    // (76.0f) fRadarHeight   
    // Unscaled, R* bug.   
    //00008040    // (4.0f) fRadarDiscHorzMargin   
    //00008040    // (4.0f) fRadarDiscVertMargin
ENDDUMP

Screen_AspectRatio:
DUMP
00000000    //id:1 - 16:9  | id:2 - 4:3
ENDDUMP

GUI_Memory_hud_radar_item:
DUMP
00 00 00 00
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

CONST_INT varOnmission          11    //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
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