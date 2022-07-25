// by J16D
// Spider-Man Mod for GTA SA c.2022
// ScrewBall Challenge Start
// Re-made Script by MeyvinIsCool - GTA3Script
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo
 
//Size
CONST_INT BYTE                 1
CONST_INT WORD                 2
CONST_INT DWORD                4
CONST_INT SIZE_RWMEMORY        8
CONST_INT SIZE_VECTOR          12
CONST_INT SIZE_QUAT            16
CONST_INT SIZE_COLPOINT_DATA   44
CONST_INT SIZE_MATRIX          64 
//TexturesID
CONST_INT tPBBackScoreB     73
CONST_INT tPBSBack1         74
CONST_INT tPBSBack2         75
CONST_INT tPBSBack3         76
CONST_INT tPBSBack1Active   77
CONST_INT tPBSBack2Active   78
CONST_INT tPBSBack3Active   79
CONST_INT iconSuccess       80     
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

//-+-----------------------------------------------------------------------------------------

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_scrb
LVAR_INT player_actor onmission iMissionVal
LVAR_FLOAT x y z xSize ySize
LVAR_INT toggleSpiderMod isInMainMenu
LVAR_INT flag_player_on_mission 
LVAR_INT blipIcon 
LVAR_INT ultimateScore 
LVAR_INT spectacularScore 
LVAR_INT amazingScore 
LVAR_INT reward1 
LVAR_INT reward2 
LVAR_INT reward3  
LVAR_INT iTotalScore 

GET_PLAYER_CHAR 0 player_actor

WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0

flag_player_on_mission = 0
WRITE_MEMORY 0xA476AC 4 flag_player_on_mission FALSE 	// $ONMISSION = 0

x = -2204.3181 
y = -126.6313 
z = 61.81

ADD_SPRITE_BLIP_FOR_COORD x y z RADAR_SPRITE_DATE_DISCO blipIcon

ultimateScore = 13500   // Minimum score 13500  Ultimate level
spectacularScore = 11500   // Minimum score 11500  Spectacular level
amazingScore = 9500    // Minimum score 9500   Amazing Level

LOAD_TEXTURE_DICTIONARY scrb
LOAD_SPRITE tPBBackScoreB   "btimD"
LOAD_SPRITE tPBSBack1       "rb1"
LOAD_SPRITE tPBSBack2       "rb2"
LOAD_SPRITE tPBSBack3       "rb3"
LOAD_SPRITE tPBSBack1Active "rb11"
LOAD_SPRITE tPBSBack2Active "rb22"
LOAD_SPRITE tPBSBack3Active "rb33"
LOAD_SPRITE iconSuccess     "Success"
USE_TEXT_COMMANDS TRUE
USE_TEXT_COMMANDS FALSE

WHILE TRUE
    IF IS_PLAYER_PLAYING 0     //$PLAYER_CHAR
        GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
        GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
        IF toggleSpiderMod = 1  //TRUE
            GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
            IF isInMainMenu = 0     //1:true 0: false
                READ_MEMORY 0xA476AC 4 FALSE onmission
                IF onmission = 0
                    // First Challenge
                    IF LOCATE_CHAR_ANY_MEANS_3D player_actor x y z 5.0 5.0 5.0 FALSE
                        IF flag_player_on_mission = 0
                            READ_INT_FROM_INI_FILE "CLEO\SpiderJ16D\config.ini" "score" "sc0" iTotalScore
                            CLEO_CALL getRewardsInfo 0 0 reward1 reward2 reward3
                            timera = 0
                            WHILE LOCATE_CHAR_ANY_MEANS_3D player_actor x y z 5.0 5.0 5.0 FALSE  
                                WAIT 0      
                                IF timera > 400
                                    GOSUB draw_mission_start
                                    GOSUB draw_key_press
                                    IF IS_BUTTON_PRESSED PAD1 TRIANGLE  // ~k~~VEHICLE_ENTER_EXIT~  

                                        WHILE  IS_BUTTON_PRESSED PAD1 TRIANGLE  // ~k~~VEHICLE_ENTER_EXIT~  
                                            WAIT 0
                                        ENDWHILE
                                        WAIT 0
                                        //GENERATE_RANDOM_INT_IN_RANGE 1 3 iMissionVal      // Removed A Mission
                                        GENERATE_RANDOM_INT_IN_RANGE 1 2 iMissionVal        // Only Mission 1 Going To Start !
                                        //PRINT_FORMATTED_NOW "iMissionVal %i" 1000 iMissionVal // DEBUG TO CHECK MISSION VARIATION
                                        USE_TEXT_COMMANDS TRUE
                                        USE_TEXT_COMMANDS FALSE
                                        IF iMissionVal = 1
                                            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\Screwball Missions\screwball_challenge_a.cm"
                                                WAIT 100
                                                flag_player_on_mission = 1
                                                WRITE_MEMORY 0xA476AC 4 flag_player_on_mission FALSE 	// $ONMISSION = 1
                                                WAIT 0
                                                LOAD_AND_LAUNCH_CUSTOM_MISSION "SpiderJ16D\Screwball Missions\screwball_challenge_a"
                                                //PRINT_FORMATTED_NOW "Mission 1!" 1500 //DEBUG
                                            ELSE
                                                PRINT_FORMATTED_NOW "~r~Error! Mission 1 Not Found! Re-Install Spider-Man Mod!" 1500
                                                iMissionVal = 0 
                                            ENDIF
                                            WAIT 1000
                                        ENDIF
                                    ENDIF
                                    // Second Challange
                                ENDIF                        
                            ENDWHILE
                        ELSE
                            PRINT_FORMATTED_NOW "Finish your current mission first!" 2000
                            WAIT 2000
                        ENDIF                        
                    ENDIF
                    WAIT 0
                ENDIF           
            ENDIF
        ELSE
            IF DOES_BLIP_EXIST blipIcon
                REMOVE_BLIP blipIcon
            ENDIF
            USE_TEXT_COMMANDS FALSE
            WAIT 0
            TERMINATE_THIS_CUSTOM_SCRIPT            
        ENDIF                           
    ENDIF
    WAIT 0 
ENDWHILE

draw_mission_start:
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 50.5 122.5 0.0 15.0 255 255 255 0 1.0 0 0 0 0 255 255 253 230 83 1 0.0   // EMP Challenge
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 57.5 125.0 65.0 20.0 255 255 255 0 0.75 0 0 1 0 255 255 255 250 -1 -1 0.0 //SIDES_LINES
    
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 115.0 135.0 50.0 15.0 255 255 255 0 1.0 0 0 0 0 255 255 253 230 95 2 0.0  // BEST SCORE
    CLEO_CALL GUI_DrawBox_WithNumber 0 115.0 149.0 50.0 15.0 255 255 255 0 121 4 0.0 iTotalScore  //~1~

    CLEO_CALL GUI_DrawBoxOutline_WithText 0 79.0 160.0 90.0 20.0 255 255 255 0 0.75 0 0 1 0 255 255 255 250 -1 -1 0.0 //SIDES_LINES division

    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 250.0 250.0 xSize ySize    
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE tPBBackScoreB 79.0 190.0 xSize ySize 255 255 255 235

    //PRINT_FORMATTED_NOW "RW:%i RW:%i RW:%i" time 1 reward1 reward2 reward3  //DEBUG

    IF  reward1 = 1
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 50.0 90.0 xSize ySize   
        DRAW_SPRITE tPBSBack2Active 39.0 220.0 xSize ySize 255 255 255 235
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 15.0 15.0 xSize ySize   
        DRAW_SPRITE iconSuccess 39.0 220.0 xSize ySize 255 255 255 235      

    ELSE
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 50.0 90.0 xSize ySize   
        DRAW_SPRITE tPBSBack2 39.0 220.0 xSize ySize 255 255 255 235    
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 39.0 200.0 30.0 15.0 255 255 255 0 1.0 0 0 0 0 255 255 253 230 92 13 0.0  // AMAZING~n~LEVEL
    CLEO_CALL GUI_DrawBox_WithNumber 0 39.0 185.0 30.0 15.0 255 255 255 0 121 7 0.0 amazingScore    //~1~

    IF  reward2 = 1
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 50.0 90.0 xSize ySize   
        DRAW_SPRITE tPBSBack1Active 79.0 220.0 xSize ySize 255 255 255 235
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 15.0 15.0 xSize ySize   
        DRAW_SPRITE iconSuccess 79.0 220.0 xSize ySize 255 255 255 235       
    ELSE
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 50.0 90.0 xSize ySize   
        DRAW_SPRITE tPBSBack1 79.0 220.0 xSize ySize 255 255 255 235    
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 79.0 200.0 30.0 15.0 255 255 255 0 1.0 0 0 0 0 255 255 253 230 93 13 0.0  // SPECTACULAR~n~LEVEL
    CLEO_CALL GUI_DrawBox_WithNumber 0 79.0 185.0 30.0 15.0 255 255 255 0 121 7 0.0 spectacularScore    //~1~

    IF  reward3 = 1
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 50.0 90.0 xSize ySize   
        DRAW_SPRITE tPBSBack3Active 119.0 220.0 xSize ySize 255 255 255 235
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 15.0 15.0 xSize ySize   
        DRAW_SPRITE iconSuccess 119.0 220.0 xSize ySize 255 255 255 235       
    ELSE
        CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 50.0 90.0 xSize ySize   
        DRAW_SPRITE tPBSBack3 119.0 220.0 xSize ySize 255 255 255 235    
    ENDIF
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 119.0 200.0 30.0 15.0 255 255 255 0 1.0 0 0 0 0 255 255 253 230 94 13 0.0  // ULTIMATE~n~LEVEL
    CLEO_CALL GUI_DrawBox_WithNumber 0 119.0 185.0 30.0 15.0 255 255 255 0 121 7 0.0 ultimateScore    //~1~

    USE_TEXT_COMMANDS FALSE
RETURN

draw_key_press:
    CONST_INT JOYPAD  0
    CONST_INT MOUSE   1     
    LVAR_INT idGXT inputType      
        
    CLEO_CALL getInputType 0 (inputType)      ///0=joypad; 1=mouse
    IF  inputType = JOYPAD
        idGXT = 151     // k~~VEHICLE_ENTER_EXIT~
    ELSE
        IF inputType = MOUSE
        
            idGXT = 161 // k~~VEHICLE_ENTER_EXIT~
        ENDIF
    ENDIF
    CLEO_CALL GetXYSizeInScreen4x3ScaleBy640x480 0 167.0 20.0 xSize ySize
    SET_SPRITES_DRAW_BEFORE_FADE TRUE    
    CLEO_CALL GUI_DrawBoxOutline_WithText 0 78.0 290.0 xSize ySize 19 18 13 100 1.0 0 0 0 0 255 255 253 230 idGXT 9 0.0
    USE_TEXT_COMMANDS FALSE      
RETURN

}
SCRIPT_END
{
//CLEO_CALL getRewardsInfo 0 /*mission*/0 /*rewards*/ rew1 rew2 rew3
getRewardsInfo:
    LVAR_INT counter    //In
    LVAR_TEXT_LABEL _lName _lstringVar
    LVAR_INT iCounterValues 
    LVAR_INT rew1 rew2 rew3
    STRING_FORMAT (_lName)"rew%i" counter
    IF DOES_FILE_EXIST "cleo\SpiderJ16D\config.ini"
        READ_STRING_FROM_INI_FILE "cleo\SpiderJ16D\config.ini" "rewards" $_lName (_lstringVar)
        IF SCAN_STRING $_lstringVar "%i %i %i" iCounterValues (rew1 rew2 rew3)
            //PRINT_FORMATTED_NOW "rew: %i" 1000 iCounterValues //DEBUG
        ELSE
            rew1 = 0
            rew2 = 0
            rew3 = 0
        ENDIF
    ELSE
        PRINT_FORMATTED_NOW "ERROR: Can't Read Rewards" 1500
        WAIT 1500
        CLEO_RETURN 0 rew1 rew2 rew3
    ENDIF
CLEO_RETURN 0 rew1 rew2 rew3
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
{
//CLEO_CALL GetXYSizeInScreenScaleByUserResolution 0 (1920.0 1080.0) (sizX sizY)
GetXYSizeInScreenScaleByUserResolution:
    LVAR_FLOAT x y // In
    LVAR_FLOAT fresX fresY
    CLEO_CALL getCurrentResolution 0 (fresX fresY)
    fresX /= 640.0
    x /= fresX
    fresY /= 448.0
    y /= fresY
CLEO_RETURN 0 (x y)
}
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
//CLEO_CALL getInputType 0 (input)  ///0=joypad; 1=mouse
getInputType:
    LVAR_INT val
    LVAR_INT hInput
    READ_MEMORY 0xB6EC2E BYTE FALSE (hInput)
CLEO_RETURN 0 hInput
}

//-+--- Shine GUI
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

// --- Functions
{
    LVAR_INT i
    LVAR_FLOAT g
    GUI_GetPulseAlpha:
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha i
    READ_MEMORY i 4 FALSE (g)
    i =# g
    CLEO_RETURN 0 i
}

{
    LVAR_INT item //In
    LVAR_INT i
    GUI_SetAtiveGXT:
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    WRITE_MEMORY i 4 item FALSE
    CLEO_RETURN 0
}

{
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha

    CONST_FLOAT ItemPulseSpeed 2.0

    GUI_Pulse_Update:

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
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    GUI_Pulse_Reset:
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    WRITE_MEMORY pAlpha 4 255.0 FALSE
    WRITE_MEMORY pStep 1 1 FALSE
    CLEO_RETURN 0
}

{
    LVAR_INT item // Item
    LVAR_INT i
    StoreActiveItem:
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    WRITE_MEMORY i 4 item FALSE
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