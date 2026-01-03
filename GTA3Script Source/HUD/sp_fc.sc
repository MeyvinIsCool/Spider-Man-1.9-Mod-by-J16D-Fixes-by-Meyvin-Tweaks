// by Meyvin Tweaks
// Spider-Man Mod for GTA SA c.2018 - 2026
// Focus Bar - CLEO+
// ALPHA Stage (need to polish more features)
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

SCRIPT_START
{
SCRIPT_NAME sp_fc
LVAR_INT player_actor toggleSpiderMod isInMainMenu toggleHUD hud_mode is_in_interior
LVAR_INT iTempVar iTempVar2 iFocusUI is_hud_enabled is_opening_door
LVAR_INT flag_player_hit_counter iFocusHit iFocus iLastCount pl_health pl_max_health
LVAR_FLOAT sx sy fFocus fLastFocus coordX[3] sizeX[3] sizeY[3] coordArrow
LVAR_FLOAT fCurrentMaxHealth

GET_PLAYER_CHAR 0 player_actor

//Default Values 
iFocusHit = 0
iFocus = 0
iTempVar2 = 0
fLastFocus = 0.0
iFocusUI = 0

SET_CLEO_SHARED_VAR varFocusCount iFocusHit             //Focus Counter
SET_CLEO_SHARED_VAR varUseFocus iFocus       //Hits Counter

CLEO_CALL get_screen_aspect_ratio 0 iTempVar    // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
CLEO_CALL storeCurrentAspectRatio 0 iTempVar
GOSUB REQUEST_Animations
GOSUB loadHudTextures

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
    IF IS_PLAYER_PLAYING player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE

            IF isInMainMenu = 0     //1:true 0: false

                IF toggleHUD = 1  // 0:OFF || 1:ON
                    GOSUB readVars
                    GOSUB hudCheck
                    GOSUB openDoorCheck
                    GOSUB activeInteriorCheck                       

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
                            GOSUB drawFocusBar    // Focus Bar 
                        ENDIF   
                        iLastCount = iFocusHit        // The Best Way I Could Find To Make This Script Work   
                        fLastFocus = fFocus

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

                focusbar_UI:
                GET_CLEO_SHARED_VAR varUseFocus iFocus
                IF iFocus = 15
                    GOSUB readVars
                    IF isInMainMenu = 0
                        STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 10
                        iTempVar2 = 1
                    ENDIF
                ELSE
                    IF iFocus = 30
                        GOSUB readVars
                        IF isInMainMenu = 0
                            STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 10
                            iTempVar2 = 2
                        ENDIF
                    ELSE
                        IF iFocus = 45
                            GOSUB readVars
                            IF isInMainMenu = 0
                                STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 10
                                iTempVar2 = 3
                            ENDIF  
                        ENDIF
                    ENDIF
                ENDIF                
                    
            ELSE
                end_hud_script:                 
                USE_TEXT_COMMANDS FALSE
                //DISPLAY_HUD TRUE
                USE_TEXT_COMMANDS FALSE               
                WAIT 25
                //REMOVE_TEXTURE_DICTIONARY
                //WAIT 0
                //TERMINATE_THIS_CUSTOM_SCRIPT
                GOTO start_check
            ENDIF
        ENDIF         
    ENDIF
    WAIT 0
GOTO main_loop

REQUEST_Animations:
    IF NOT HAS_ANIMATION_LOADED "spider"
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_Animations

drawFocusBar:   
    GET_CLEO_SHARED_VAR varHitCountFlag flag_player_hit_counter
    GET_CLEO_SHARED_VAR varFocusCount iFocusHit     // Checks For Increase Value From Other Scripts
    GET_CLEO_SHARED_VAR varUseFocus iFocus  

    IF iFocusHit > iLastCount
        iFocus ++
        SET_CLEO_SHARED_VAR varUseFocus iFocus
        iLastCount = iFocusHit
    ENDIF

    IF iFocus > 45
        iFocus = 45
        SET_CLEO_SHARED_VAR varUseFocus iFocus 
        IF iFocusHit > 45
            iFocusHit = 45
            SET_CLEO_SHARED_VAR varFocusCount iFocusHit
        ENDIF
    ELSE
        IF iFocus < 0                                //Safety Measures
            iFocus = 0
            SET_CLEO_SHARED_VAR varUseFocus iFocus 
            IF iFocusHit < 0
                iFocusHit = 0
                SET_CLEO_SHARED_VAR varFocusCount iFocusHit
            ENDIF        
        ENDIF    
    ENDIF

    //GET_FIXED_XY_ASPECT_RATIO (400.0 100.0) (sx sy)    //(300.0 93.33)
    sx = 288.00 
    sy = 142.55        
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    IF flag_player_hit_counter = 1
        DRAW_SPRITE idFBar (143.6 67.5) (sx sy) (255 255 255 255)
    ENDIF    
    fFocus =# iFocus

    IF flag_player_hit_counter = 1 
        IF fFocus >= 1.0
        AND fFocus <= 14.0
            CLEO_CALL barFunc_1 0 fFocus coordX[0] (sizeX[0] sizeY[0] coordArrow)   
            IF fLastFocus < fFocus
                DRAW_RECT (coordX[0] 57.95) (sizeX[0] sizeY[0]) (48 200 255 255)
            ENDIF  
            DRAW_RECT (coordX[0] 57.95) (sizeX[0] sizeY[0]) (255 255 255 120) 
            sx = 300.00 
            sy = 120.55   
            USE_TEXT_COMMANDS FALSE   
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idFArrow (coordArrow 41.8) (sx sy) (255 255 255 255)
        ELSE
            IF fFocus = 0.0
                sx = 300.00 
                sy = 120.55   
                USE_TEXT_COMMANDS FALSE    
                SET_SPRITES_DRAW_BEFORE_FADE TRUE            
                DRAW_SPRITE idFArrow (140.0 41.8) (sx sy) (255 255 255 255)
            ENDIF              
        ENDIF

        IF fFocus >= 15.0
            IF fFocus = 15.0
                CLEO_CALL barFunc_1 0 fFocus coordX[0] (sizeX[0] sizeY[0] coordArrow)
                sx = 300.00 
                sy = 120.55 
                USE_TEXT_COMMANDS FALSE    
                SET_SPRITES_DRAW_BEFORE_FADE TRUE        
                //DRAW_SPRITE idFArrow (174.52 41.8) (sx sy) (255 255 255 255)  
            ENDIF
            DRAW_RECT (coordX[0] 57.95) (sizeX[0] sizeY[0]) (11 247 196 255) //focus charged - bar 1
        ENDIF    
        
        IF fFocus >= 16.0
        AND fFocus <= 29.0
            CLEO_CALL barFunc_2 0 fFocus coordX[1] (sizeX[1] sizeY[1] coordArrow)   
            IF fLastFocus < fFocus
                DRAW_RECT (coordX[1] 57.95) (sizeX[1] sizeY[1]) (48 200 255 255)
            ENDIF          
            DRAW_RECT (coordX[1] 57.95) (sizeX[1] sizeY[1]) (255 255 255 120)  //bar     
            sx = 300.00 
            sy = 120.55   
            USE_TEXT_COMMANDS FALSE    
            SET_SPRITES_DRAW_BEFORE_FADE TRUE           
            DRAW_SPRITE idFArrow (coordArrow 41.8) (sx sy) (255 255 255 255)         
        ENDIF
        IF fFocus >= 30.0
            IF fFocus = 30.0
                CLEO_CALL barFunc_2 0 fFocus coordX[1] (sizeX[1] sizeY[1] coordArrow) 
                sx = 300.00 
                sy = 120.55 
                USE_TEXT_COMMANDS FALSE    
                SET_SPRITES_DRAW_BEFORE_FADE TRUE         
                //DRAW_SPRITE idFArrow (210.9 41.8) (sx sy) (255 255 255 255)
            ENDIF 
            DRAW_RECT (coordX[1] 57.95) (sizeX[1] sizeY[1]) (11 247 196 255) //focus charged - bar 2
        ENDIF

        IF fFocus >= 31.0
        AND fFocus <= 44.0
            CLEO_CALL barFunc_3 0 fFocus coordX[2] (sizeX[2] sizeY[2] coordArrow)   
            IF fLastFocus < fFocus
                DRAW_RECT (coordX[2] 57.95) (sizeX[2] sizeY[2]) (48 200 255 255)
            ENDIF          
            DRAW_RECT (coordX[2] 57.95) (sizeX[2] sizeY[2]) (255 255 255 120)  //bar  
            sx = 300.00 
            sy = 120.55   
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE TRUE
            DRAW_SPRITE idFArrow (coordArrow 41.8) (sx sy) (255 255 255 255)                   
        ENDIF
        IF fFocus >= 45.0
            IF fFocus = 45.0
                CLEO_CALL barFunc_3 0 fFocus coordX[2] (sizeX[2] sizeY[2] coordArrow) 
                sx = 300.00 
                sy = 120.55    
                USE_TEXT_COMMANDS FALSE
                SET_SPRITES_DRAW_BEFORE_FADE TRUE          
                //DRAW_SPRITE idFArrow (248.0 41.8) (sx sy) (255 255 255 255)            
            ENDIF 
            DRAW_RECT (coordX[2] 57.95) (sizeX[2] sizeY[2]) (11 247 196 255) //focus charged - bar 3  
        ENDIF     
    ENDIF              

//-+------ Focus Bar Full UI - STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" {id}
    IF iFocus >= 0
    AND iFocus <= 14
        iFocusUI = 0
    ENDIF
    IF iFocus >= 15
        IF NOT iFocusUI >= 1
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 10
            iFocusUI = 1
        ENDIF
    ENDIF
    IF iFocus >= 30
        IF NOT iFocusUI >= 2
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 10
            iFocusUI = 2
        ENDIF       
    ENDIF 
    IF iFocus >= 45
        IF NOT iFocusUI >= 3
            STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 10
            iFocusUI = 3
        ENDIF           
    ENDIF    
    IF iFocus >= 0
        GET_CLEO_SHARED_VAR varUseFocus iFocus
        IF iFocus <= 14
            iFocusUI = 0
        ELSE
            IF iFocus >= 15
            AND iFocus <= 29
                iFocusUI = 1
            ELSE
                IF iFocus >= 30
                AND iFocus <= 44
                    iFocusUI = 2
                ENDIF
            ENDIF
        ENDIF    
    ENDIF
//-+----------------------------------------------------------
    // Focus Bar Use
    IF iFocus > 0
        GOSUB getCurrentHealth
        IF IS_BUTTON_JUST_PRESSED PAD1 DPADLEFT
            IF NOT pl_health = pl_max_health
                STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_lf.cs"     // Life Regeneration
            ENDIF
        ENDIF
    ENDIF

    WAIT 0
RETURN

getCurrentHealth:
    GET_CHAR_MAX_HEALTH player_actor (fCurrentMaxHealth)
    pl_max_health =# fCurrentMaxHealth
    GET_CHAR_HEALTH player_actor (pl_health)
RETURN

loadHudTextures:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY sphud
        //Focus Bar
        LOAD_SPRITE idFBar "sp_fbar"     
        LOAD_SPRITE idFArrow "f_arr"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
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
    READ_MEMORY 0x96A7CC 4 FALSE (iTempVar)
    IF iTempVar = 1
    OR iTempVar = 2
        is_opening_door = TRUE
    ELSE
        is_opening_door = FALSE
    ENDIF
RETURN

}
SCRIPT_END

//CALL SCM HELPERS

//-+-----Focus Bar Function-+-------
{
//CLEO_CALL barFunc 0 /*size*/1000.0 /*pos*/posX (/*size*/sizeX sizeY)
barFunc_1:
    LVAR_FLOAT sizeBar   // In
    LVAR_FLOAT var[2]
    LVAR_FLOAT xSize ySize arrCoord
    var[1] = sizeBar
    var[1] /= 44.5 //fresX
    //var[1] *= 300.0
    //CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (var[1] 12.0) (xSize ySize)  //var[1] = 100
    var[1] *= 100.0
    xSize = var[1]
    ySize = 3.15
    var[0] = xSize
    var[0] /= 2.0
    var[0] += 77.4 //77.4+(100/2)= 127.4
    arrCoord = var[0]
    arrCoord *= 2.0
    arrCoord -= 14.5 
CLEO_RETURN 0 var[0] xSize ySize arrCoord
}
{
//CLEO_CALL barFunc 0 /*size*/1000.0 /*pos*/posX (/*size*/sizeX sizeY)
barFunc_2:
    LVAR_FLOAT sizeBar   // In
    LVAR_FLOAT var[2]
    LVAR_FLOAT xSize ySize arrCoord
    var[1] = sizeBar
    var[1] /= 44.5 //fresX
    //var[1] *= 300.0
    //CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (var[1] 12.0) (xSize ySize)  //var[1] = 100
    var[1] *= 100.0
    xSize = var[1]
    ySize = 3.15
    var[0] = xSize
    var[0] /= 2.0
    var[0] += 97.35 //97.35+(100/2)= 147.35
    arrCoord = var[0]
    arrCoord *= 2.0
    arrCoord -= 51.0       
    xSize -= 33.7
CLEO_RETURN 0 var[0] xSize ySize arrCoord
}
{
//CLEO_CALL barFunc 0 /*size*/1000.0 /*pos*/posX (/*size*/sizeX sizeY)
barFunc_3:
    LVAR_FLOAT sizeBar   // In
    LVAR_FLOAT var[2]
    LVAR_FLOAT xSize ySize arrCoord
    var[1] = sizeBar
    var[1] /= 44.5 //fresX
    //var[1] *= 300.0
    //CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (var[1] 12.0) (xSize ySize)  //var[1] = 100
    var[1] *= 100.0
    xSize = var[1]
    ySize = 3.15
    var[0] = xSize
    var[0] /= 2.0
    var[0] += 117.5 //117.5+(100/2)= 167.5
    arrCoord = var[0]
    arrCoord *= 2.0
    arrCoord -= 88.05     
    xSize -= 67.6
CLEO_RETURN 0 var[0] xSize ySize arrCoord
}
//-+--------------------------------

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
CONST_INT varThrowFix           28    //sp_thob          ||1= Activated     || 0= Deactivated

CONST_INT varLevelChar          30    //sp_lvl    || Level
CONST_INT varStatusLevelChar    31    //If value >0 automatically will add that number to Experience Points (Max Reward +2500)

CONST_INT varIdWebWeapon        32    //sp_mm     || 1-8 weap
CONST_INT varWeapAmmo           33    //sp_wep    ||store current weap ammo
CONST_INT varIdPowers           34    //MSpiderJ16Dv7 - sp_po     ||Id powers 1 - 12
CONST_INT varPowersProgress     35    //sp_po     || current power progress
CONST_INT varHitCount           36    //sp_hit    || hitcounting
CONST_INT varHitCountFlag       37    //sp_hit    || hitcounting and focus bar
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

//Additional Skills
CONST_INT varSkill1a            59    //sp_dw    ||1= Activated     || 0= Deactivated

CONST_INT varFocusCount         70    //sp_hit    || focus bar
CONST_INT varUseFocus           71    //sp_hit    || focus bar


//TEXTURES
CONST_INT idFBar 1
CONST_INT idFArrow 2