// by J16D
// Fixes by Meyvin Tweaks
// Life Regeneration / Finisher (Not yet implemented) (WIP)
// Spider-Man Mod for GTA SA c.2018 - 2026
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------

//data
CONST_INT inital_delay 50 //ms
CONST_FLOAT speed 0.05
CONST_INT player 0

CONST_INT iFocusPoint_1 1
CONST_INT iFocusPoint_2 2
CONST_INT iFocusPoint_3 3
CONST_INT iFocusPoint_4 4
CONST_INT iFocusPoint_5 5
CONST_INT iFocusPoint_6 6
CONST_INT iFocusPoint_7 7
CONST_INT iFocusPoint_8 8
CONST_INT iFocusPoint_9 9
CONST_INT iFocusPoint_10 10
CONST_INT iFocusPoint_11 11
CONST_INT iFocusPoint_12 12
CONST_INT iFocusPoint_13 13
CONST_INT iFocusPoint_14 14
CONST_INT iFocusPoint_15 15

SCRIPT_START
{
SCRIPT_NAME sp_lf
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod
LVAR_INT iFocusCounter pl_health pl_max_health
LVAR_INT iHealPoint
LVAR_FLOAT fCurrentMaxHealth
 
GET_PLAYER_CHAR 0 player_actor

main_loop:
    IF IS_PLAYER_PLAYING player
        IF NOT HAS_CHAR_BEEN_ARRESTED player_actor
            IF NOT IS_CHAR_DEAD player_actor
                GET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                GOSUB getCurrentHealth
                IF pl_health < pl_max_health
                    GOSUB focus_heal

                    timera = 0
                    WHILE inital_delay >= timera
                        GOSUB getHealthMath
                        WAIT 0
                    ENDWHILE

                    IF IS_BUTTON_PRESSED PAD1 DPADDOWN
                        WHILE IS_BUTTON_PRESSED PAD1 DPADDOWN
                            WAIT 0
                        ENDWHILE
                    ENDIF  
                    IF timera >= inital_delay
                        USE_TEXT_COMMANDS FALSE
                        WAIT 50
                        TERMINATE_THIS_CUSTOM_SCRIPT    
                    ENDIF                        
                ENDIF
            ENDIF        
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
RETURN

getCurrentHealth:
    GET_CHAR_MAX_HEALTH player_actor (fCurrentMaxHealth)
    pl_max_health =# fCurrentMaxHealth
    GET_CHAR_HEALTH player_actor (pl_health)
RETURN

getHealthMath:
    GET_CHAR_HEALTH player_actor (pl_health)
    pl_health += iHealPoint
    SET_CHAR_HEALTH player_actor (pl_health)
RETURN

focus_heal:
    IF iFocusCounter >= 15
        iFocusCounter -= 15
        iHealPoint = iFocusPoint_15
        SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
    ELSE
        IF iFocusCounter = 14
            iFocusCounter -= 14
            iHealPoint = iFocusPoint_14
            SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
        ELSE
            IF iFocusCounter = 13
                iFocusCounter -= 13
                iHealPoint = iFocusPoint_13
                SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
            ELSE 
                IF iFocusCounter = 12
                    iFocusCounter -= 12
                    iHealPoint = iFocusPoint_12
                    SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                ELSE 
                    IF iFocusCounter = 11
                        iFocusCounter -= 11
                        iHealPoint = iFocusPoint_11
                        SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                    ELSE 
                        IF iFocusCounter = 10
                            iFocusCounter -= 10
                            iHealPoint = iFocusPoint_10
                            SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                        ELSE  
                            IF iFocusCounter = 9
                                iFocusCounter -= 9
                                iHealPoint = iFocusPoint_9
                                SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                            ELSE    
                                IF iFocusCounter = 8
                                    iFocusCounter -= 8
                                    iHealPoint = iFocusPoint_8
                                    SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                                ELSE    
                                    IF iFocusCounter = 7
                                        iFocusCounter -= 7
                                        iHealPoint = iFocusPoint_7
                                        SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                                    ELSE    
                                        IF iFocusCounter = 6
                                            iFocusCounter -= 6
                                            iHealPoint = iFocusPoint_6
                                            SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                                        ELSE       
                                            IF iFocusCounter = 5
                                                iFocusCounter -= 5
                                                iHealPoint = iFocusPoint_5
                                                SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                                            ELSE    
                                                IF iFocusCounter = 4
                                                    iFocusCounter -= 4
                                                    iHealPoint = iFocusPoint_4
                                                    SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                                                ELSE    
                                                    IF iFocusCounter = 3
                                                        iFocusCounter -= 3
                                                        iHealPoint = iFocusPoint_3
                                                        SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                                                    ELSE   
                                                        IF iFocusCounter = 2
                                                            iFocusCounter -= 2
                                                            iHealPoint = iFocusPoint_2
                                                            SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                                                        ELSE    
                                                            IF iFocusCounter = 1
                                                                iFocusCounter -= 1
                                                                iHealPoint = iFocusPoint_1
                                                                SET_CLEO_SHARED_VAR varUseFocus iFocusCounter
                                                            ENDIF  
                                                        ENDIF
                                                    ENDIF
                                                ENDIF
                                            ENDIF
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDIF
    ENDIF                                            
RETURN

}
SCRIPT_END


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

CONST_INT varFocusCount         70    //sp_hit    || focus bar
CONST_INT varUseFocus           71    //sp_hit    || focus bar
