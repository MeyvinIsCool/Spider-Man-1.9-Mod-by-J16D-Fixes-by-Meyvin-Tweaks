// by Meyvin Tweaks
// Dynamic Health System
// Spider-Man Mod for GTA SA c.2018 - 2025
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

SCRIPT_START
{
WAIT 0
SCRIPT_NAME sp_hl
LVAR_INT player_actor toggleSpiderMod isInMainMenu toggleHUD hud_mode is_in_interior 
LVAR_INT iTempVar iTempVar3 is_opening_door is_hud_enabled
LVAR_INT flag_player_on_mission flag_player_hit_counter
LVAR_FLOAT posX posY sx sy
LVAR_FLOAT iTempVar2 pl_max_health hb_display_percent hb_smooth_damage hb_smooth_heal

GET_PLAYER_CHAR 0 player_actor
GOSUB loadHudTextures
is_opening_door = FALSE
is_hud_enabled = TRUE
CLEO_CALL get_screen_aspect_ratio 0 iTempVar    // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
USE_TEXT_COMMANDS FALSE

posX = 82.30 
posY = 32.45

// Smoothing state for health bar
hb_display_percent = 100.0
// Separate smoothing: damage (when health drops) and heal (when health increases)
hb_smooth_damage = 0.12 // faster when losing health
hb_smooth_heal = 0.06   // slower when healing

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
                        GOSUB hudCheck
                        //GOSUB openDoorCheck
                        //GOSUB activeInteriorCheck

                        IF IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                        OR IS_ON_CUTSCENE 
                        //OR IS_HUD_VISIBLE 
                        OR is_hud_enabled = TRUE
                        //OR is_opening_door = FALSE
                            GOSUB is_health_bar_enabled
                            IF iTempVar3 = 1    //1:true 0: false
                                GOSUB drawHealth    // Draw Health
                            ENDIF
                        ELSE
                            USE_TEXT_COMMANDS FALSE
                        ENDIF      
                
                        /*IF is_in_interior = 1   // Disables IF Player Is In Interior
                            IF flag_player_on_mission = 0   // 0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
                                USE_TEXT_COMMANDS FALSE
                                WHILE NOT is_in_interior = 0     //1:true 0: false
                                    GOSUB activeInteriorCheck
                                WAIT 1500
                                ENDWHILE   
                            ENDIF
                        ENDIF  */    

                        IF isInMainMenu = 1         // Disables IF Is In Menu
                            USE_TEXT_COMMANDS FALSE
                            WHILE isInMainMenu = 1     //1:true 0: false
                                GOTO end_hud_script
                                WAIT 0
                            ENDWHILE       
                        ENDIF  
                        WAIT 0     
                    ENDWHILE
         
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

is_health_bar_enabled:
    GET_CLEO_SHARED_VAR varHudHealth (iTempVar3)
RETURN

drawHealth:
    GET_CHAR_HEALTH player_actor (iTempVar)
    GET_CHAR_MAX_HEALTH player_actor (pl_max_health)

    // Convert health to float and normalize to percentage (0-100)
    iTempVar2 =# iTempVar    // Convert int to float 
    iTempVar2 /= pl_max_health
    iTempVar2 *= 100.0

    // Smooth the displayed health percent toward the target using lerp
    // Use different speeds for damage vs heal
    iTempVar2 =# iTempVar    // target percent
    iTempVar2 /= pl_max_health
    iTempVar2 *= 100.0
    
    LVAR_FLOAT diff factor
    diff = iTempVar2
    diff -= hb_display_percent
    IF diff < 0.0
        factor = hb_smooth_damage
    ELSE
        factor = hb_smooth_heal
    ENDIF
    diff *= factor
    hb_display_percent += diff

    // Optionally snap when very close to avoid tiny differences
    // (recompute difference into iTempVar2 variable)
    iTempVar2 =# iTempVar    
    iTempVar2 /= pl_max_health
    iTempVar2 *= 100.0
    iTempVar2 -= hb_display_percent
    IF iTempVar2 < 0.5
        IF iTempVar2 > -0.5
            // close enough, snap
            iTempVar2 =# iTempVar    
            iTempVar2 /= pl_max_health
            iTempVar2 *= 100.0
            hb_display_percent = iTempVar2
        ENDIF
    ENDIF

    GET_FIXED_XY_ASPECT_RATIO (400.0 100.0) (sx sy)    //(300.0 93.33)
    sx = 310.00 
    sy = 130.35
    // Draw Texture On Screen
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    IF flag_player_hit_counter = 1
        IF iTempVar <= 20
            DRAW_SPRITE idHB (105.15 41.05) (sx sy) (155 30 30 255)
            // Draw Health Bar On (pass smoothed percent)
            sx = 160.45
            sy = 9.0
            //CLEO_CALL drawBar 0 xCoord yCoord fValue xMaxSize yMaxSize (r g b a)
            CLEO_CALL drawBar 0 posX posY hb_display_percent sx sy (255 255 255 160)            
        ELSE
            DRAW_SPRITE idHB (105.15 41.05) (sx sy) (255 255 255 255) 
            // Draw Health Bar On (pass smoothed percent)
            sx = 160.45
            sy = 9.0
            //CLEO_CALL drawBar 0 xCoord yCoord fValue xMaxSize yMaxSize (r g b a)
            CLEO_CALL drawBar 0 posX posY hb_display_percent sx sy (255 255 255 160)            
        ENDIF   
        GOSUB drawHealthNumber
        GOSUB drawRedBackgroundDeath  
    ENDIF  
RETURN

drawHealthNumber:
    GET_CHAR_HEALTH player_actor (iTempVar)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_SCALE 0.24 1.17
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 215 225 235 255
    SET_TEXT_EDGE 1 (94 120 137 100)
    USE_TEXT_COMMANDS FALSE
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DISPLAY_TEXT_WITH_NUMBER 23.5 25.0 NUMBER iTempVar
RETURN

drawRedBackgroundDeath:
    IF iTempVar <= 20
        CLEO_CALL getCurrentResolution 0 (sx sy) 
        GET_FIXED_XY_ASPECT_RATIO (sx sy) (sx sy)    //(300.0 93.33)
        USE_TEXT_COMMANDS FALSE
        SET_SPRITES_DRAW_BEFORE_FADE TRUE
        DRAW_SPRITE idHealthLow (320.0 224.0) (sx sy) (155 30 30 255)
    ENDIF
RETURN

//-+-------------------- Texture Load ----------------------
loadHudTextures:
    //TEXTURES
    CONST_INT idHealthLow 10
    CONST_INT idStars 11
    //Health bar
    CONST_INT idHB 15
    CONST_INT idHBa 16
    CONST_INT idHBArrow 17
    //Armour Bar
    CONST_INT idWA 35
    CONST_INT idWA_a 36
    CONST_INT idWA_b 37
    CONST_INT idWA_c 38
    CONST_INT idAmmoW1 39
    CONST_INT idAmmoW2 40
    CONST_INT idAmmoW3 41
    CONST_INT idAmmoW4 42
    CONST_INT idAmmoW5 43
    CONST_INT idAmmoW6 44
    CONST_INT idAmmoW7 45
    CONST_INT idAmmoW8 46
    CONST_INT idAmmoW9 47
    CONST_INT idAmmoW10 48
    //Armour Weap
    CONST_INT idWeap1 49
    CONST_INT idWeap2 50
    CONST_INT idWeap3 51
    CONST_INT idWeap4 52
    CONST_INT idWeap5 53
    CONST_INT idWeap6 54
    CONST_INT idWeap7 55
    CONST_INT idWeap8 56
    //Armour Power
    CONST_INT idSPPowerBP 57
    CONST_INT idSPPowerBPJ 58
    CONST_INT idSPPowerDS 59
    CONST_INT idSPPowerEP 60
    CONST_INT idSPPowerHD 61
    CONST_INT idSPPowerIA 62
    CONST_INT idSPPowerLG 63
    CONST_INT idSPPowerNS 64
    CONST_INT idSPPowerRO 65
    CONST_INT idSPPowerSB 66
    CONST_INT idSPPowerSF 67
    CONST_INT idSPPowerWB 68
    CONST_INT idSPPowerQS 69
    CONST_INT idSPPowerEQ 70
    CONST_INT idSPPowerQD 71
    CONST_INT idSPPowerKR 72
    CONST_INT idSPPowerNULL 74
    //Armour Power Circle
    CONST_INT idPowerBar1 75
    CONST_INT idPowerBar2 76
    CONST_INT idPowerBar3 77
    CONST_INT idPowerBar4 78
    CONST_INT idPowerBar5 79
    CONST_INT idPowerBar6 80
    CONST_INT idPowerBar7 81
    CONST_INT idPowerBar8 82

    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY sphud
        //wanted star & background red
        LOAD_SPRITE idStars "st1"
        LOAD_SPRITE idHealthLow "splhealth"
        //Health bar
        LOAD_SPRITE idHBa "h_bar1"
        LOAD_SPRITE idHB "h_bar"
        LOAD_SPRITE idHBArrow "h_arr"
        //Armour bar
        LOAD_SPRITE idWA "a_bar"
        LOAD_SPRITE idWA_a "a_bara"
        LOAD_SPRITE idWA_b "a_barb"
        LOAD_SPRITE idWA_c "a_barc"
        LOAD_SPRITE idAmmoW1 "a1"
        LOAD_SPRITE idAmmoW2 "a2"
        LOAD_SPRITE idAmmoW3 "a3"
        LOAD_SPRITE idAmmoW4 "a4"
        LOAD_SPRITE idAmmoW5 "a5"
        LOAD_SPRITE idAmmoW6 "a6"
        LOAD_SPRITE idAmmoW7 "a7"
        LOAD_SPRITE idAmmoW8 "a8"
        LOAD_SPRITE idAmmoW9 "a9"
        LOAD_SPRITE idAmmoW10 "a10"
        //Armour Weap
        LOAD_SPRITE idWeap1 "wa1"
        LOAD_SPRITE idWeap2 "wa2"
        LOAD_SPRITE idWeap3 "wa3"
        LOAD_SPRITE idWeap4 "wa4"
        LOAD_SPRITE idWeap5 "wa5"
        LOAD_SPRITE idWeap6 "wa6"
        LOAD_SPRITE idWeap7 "wa7"
        LOAD_SPRITE idWeap8 "wa8"
        //Armour Power
        LOAD_SPRITE idSPPowerWB "p_wb"
        LOAD_SPRITE idSPPowerHD "p_hd"
        LOAD_SPRITE idSPPowerBP "p_bp"
        LOAD_SPRITE idSPPowerSB "p_sb"
        LOAD_SPRITE idSPPowerNS "p_ns"
        LOAD_SPRITE idSPPowerEP "p_ep"
        LOAD_SPRITE idSPPowerRO "p_ro"
        LOAD_SPRITE idSPPowerBPJ "p_bpj"
        LOAD_SPRITE idSPPowerLG "p_lg"
        LOAD_SPRITE idSPPowerIA "p_ia"
        LOAD_SPRITE idSPPowerDS "p_ds"
        LOAD_SPRITE idSPPowerSF "p_sf"
        LOAD_SPRITE idSPPowerQS "p_qs"
        LOAD_SPRITE idSPPowerEQ "p_eq"
        LOAD_SPRITE idSPPowerQD "p_qd"
        LOAD_SPRITE idSPPowerKR "p_kr"
        LOAD_SPRITE idSPPowerNULL "p_null"
        //Armour Power Circle
        LOAD_SPRITE idPowerBar1 "c_b_1"
        LOAD_SPRITE idPowerBar2 "c_b_2"
        LOAD_SPRITE idPowerBar3 "c_b_3"
        LOAD_SPRITE idPowerBar4 "c_b_4"
        LOAD_SPRITE idPowerBar5 "c_b_5"
        LOAD_SPRITE idPowerBar6 "c_b_6"
        LOAD_SPRITE idPowerBar7 "c_b_7"
        LOAD_SPRITE idPowerBar8 "c_b_8"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN
//-+--------------------------------------------------------

readVars:
    GET_CLEO_SHARED_VAR varHUD (toggleHUD)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
    GET_CLEO_SHARED_VAR varHitCountFlag (flag_player_hit_counter)
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
    READ_MEMORY 0x96A7CC 4 FALSE (iTempVar3)
    IF iTempVar3 = 1
    OR iTempVar3 = 2
        is_opening_door = TRUE
    ELSE
        is_opening_door = FALSE
    ENDIF
RETURN

}
SCRIPT_END

//-+---CALL SCM HELPERS
{
//CLEO_CALL drawBar 0 xCoord yCoord fValue xMaxSize yMaxSize (r g b a)
drawBar:
    LVAR_FLOAT xCoord yCoord fValue xMaxSize yMaxSize //in
    LVAR_INT r g b a // in
    //fValue is expected to be 0-100 (health percentage)
    LVAR_FLOAT currentWidth bgCenterX healthCenterX centerY rectW rectH
    LVAR_FLOAT sx sy

    // Calculate current bar width based on percentage
    currentWidth = fValue
    currentWidth /= 100.0
    currentWidth *= xMaxSize

    // Compute centers from the same left-edge anchor (xCoord)
    // Background center: left-edge + half of max width
    bgCenterX = xCoord
    bgCenterX += xMaxSize
    bgCenterX /= 2.0

    // Health bar center: left-edge + half of current width
    healthCenterX = xCoord
    healthCenterX += currentWidth
    healthCenterX /= 2.0

    centerY = yCoord
    rectW = currentWidth
    rectH = yMaxSize

    // Draw background bar (full width) centered at bgCenterX
    DRAW_RECT bgCenterX centerY xMaxSize yMaxSize (80 80 80 160)
    // Draw health bar - left-aligned with background
    DRAW_RECT healthCenterX centerY rectW rectH (r g b a)

    // Draw health arrow indicator above health bar
    GET_FIXED_XY_ASPECT_RATIO (400.0 100.0) (sx sy)    //(300.0 93.33)
    sx = 310.00 
    sy = 130.35
    healthCenterX *= 2.0
    healthCenterX += 23.70 // adjust to align arrow properly  
    DRAW_SPRITE idHBArrow (healthCenterX 52.5) (sx sy) (255 255 255 255) // hl_arr texture
    USE_TEXT_COMMANDS FALSE
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

CONST_INT varFocusCount         70    //sp_hit    || focus bar
CONST_INT varUseFocus           71    //sp_hit    || focus bar