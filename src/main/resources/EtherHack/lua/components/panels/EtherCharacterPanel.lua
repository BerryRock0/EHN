require "ISUI/ISPanel"

--*********************************************************
--* Глобальные установки UI
--*********************************************************
EtherCharacterPanel = ISPanel:derive("EtherCharacterPanel"); -- Наследование от ISPanel

--*********************************************************
--* Добавление чекбоксов
--*********************************************************
function EtherCharacterPanel:addCheckBox(title, method, isSelected, isOnlyInGame)
    local checkBoxAmount = #self.checkBoxList;
    local checkboxX = EtherMain.panelPadding;
    local checkboxY = EtherMain.panelPadding + checkBoxAmount * EtherMain.rowHeight;

    local checkbox = UICheckbox:new(checkboxX, checkboxY, title, isSelected, method);
    checkbox:initialise();
    checkbox:instantiate();
    checkbox:setAnchorLeft(true);
    checkbox:setAnchorRight(false);
    checkbox:setAnchorTop(false);
    checkbox:setAnchorBottom(true);
    checkbox.isOnlyInGame = isOnlyInGame;
    self:addChild(checkbox);

    self:setScrollHeight(self:getScrollHeight() + EtherMain.rowHeight);

    table.insert(self.checkBoxList, checkbox);
end

--*********************************************************
--* Обработка событий колесика мыши
--*********************************************************
function EtherCharacterPanel:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del * 40));
	return true;
end

--*********************************************************
--* Обновление панели
--*********************************************************
function EtherCharacterPanel:updatePanel()
    for i=1, #self.checkBoxList do
        local item = self.checkBoxList[i];
        if item.isOnlyInGame and self.localPlayer == nil then
        item:setEnable(false);
        end
    end
end

--*********************************************************
--* Создание дочерних элементов
--*********************************************************
function EtherCharacterPanel:createChildren()
    ISPanel.createChildren(self);

    self:setScrollChildren(true)
    self:setScrollHeight(0)
    self:addScrollBars();

    self:addCheckBox(getTranslate("UI_CharacterPanel_MultiHitZombies"), function(isChecked)
        toggleMultiHitZombies(isChecked);
    end, isMultiHitZombies(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_DisableSpentRoundChamber"), function(isChecked)
        toggleNoSpentRoundChamber(isChecked)
    end, isNoSpentRoundChamber(), false);
	
    self:addCheckBox(getTranslate("UI_CharacterPanel_DisableJam"), function(isChecked)
        toggleNoJam(isChecked)
    end, isNoJam(), false);
	
    self:addCheckBox(getTranslate("UI_CharacterPanel_DisableRecoil"), function(isChecked)
        toggleNoRecoil(isChecked)
    end, isNoRecoil(), false);

	self:addCheckBox(getTranslate("UI_CharacterPanel_DisableReload"), function(isChecked)
        toggleNoReload(isChecked)
    end, isNoReload(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_AlwaysRack"), function(isChecked)
        toggleAlwaysRack(isChecked);
    end, isAlwaysRack(), false);

	self:addCheckBox(getTranslate("UI_CharacterPanel_AlwaysRoundChamber"), function(isChecked)
        toggleAlwaysRoundChamber(isChecked);
    end, isAlwaysRoundChamber(), false);
	
    self:addCheckBox(getTranslate("UI_CharacterPanel_AlwaysAiming"), function(isChecked)
        toggleAlwaysAiming(isChecked);
    end, isAlwaysAiming(), false);
	
	self:addCheckBox(getTranslate("UI_CharacterPanel_AlwaysCritical"), function(isChecked)
        toggleAlwaysCritical(isChecked);
    end, isAlwaysCritical(), false);
	
	self:addCheckBox(getTranslate("UI_CharacterPanel_AlwaysKnockdown"), function(isChecked)
        toggleAlwaysKnockdown(isChecked);
    end, isAlwaysKnockdown(), false);
	
	self:addCheckBox(getTranslate("UI_CharacterPanel_InstantKill"), function(isChecked)
        toggleExtraDamage(isChecked);
        if(not isChecked) then
            resetWeaponsStats()
        end
    end, isExtraDamage(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_UnlimitedAmmo"), function(isChecked)
        EtherDebugClient.toggleSelf("unlimitedAmmo", isChecked);
    end, isUnlimitedAmmo(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_ZombieDontAttack"), function(isChecked)
        toggleZombieDontAttack(isChecked);
    end, isZombieDontAttack(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_NightVision"), function(isChecked)
        toggleNightVision(isChecked);
    end, isEnableNightVision(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_UnlimitedCarry"), function(isChecked)
        EtherDebugClient.toggleSelf("unlimitedCarry", isChecked);
    end, isEnableUnlimitedCarry(), false);
	
    self:addCheckBox(getTranslate("UI_CharacterPanel_BuildCheat"), function(isChecked)
        ISBuildMenu.cheat = isChecked;
    end, ISBuildMenu.cheat, false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_FarmingCheat"), function(isChecked)
        ISFarmingMenu.cheat = isChecked;
    end, ISFarmingMenu.cheat, false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_TimedActionCheat"), function(isChecked)
        toggleTimedActionCheat(isChecked);
    end, isTimedActionCheat(), false);	

    self:addCheckBox(getTranslate("UI_CharacterPanel_UnlimitedCondition"), function(isChecked)
        toggleUnlimitedCondition(isChecked);
    end, isUnlimitedCondition(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_AutoRepairsItems"), function(isChecked)
        toggleAutoRepairItems(isChecked);
    end, isAutoRepairItems(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_DisableRecoil"), function(isChecked)
        toggleNoRecoil(isChecked)
    end, isNoRecoil(), false);

	self:addCheckBox(getTranslate("UI_CharacterPanel_GodMode"), function(isChecked)
        EtherDebugClient.toggleSelf("god", isChecked);
    end, isEnableGodMode(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_NoClip"), function(isChecked)
        EtherDebugClient.toggleSelf("noclip", isChecked);
    end, isEnableNoclip(), false);

    self:addCheckBox(getTranslate("UI_CharacterPanel_Invisible"), function(isChecked)
        EtherDebugClient.toggleSelf("invisible", isChecked);
    end, isEnableInvisible(), false);

    self:updatePanel();
end

--*********************************************************
--* Обработка prerender
--*********************************************************
function EtherCharacterPanel:prerender()
    self:setStencilRect(0,10,self:getWidth(),self:getHeight() - 20);
    ISPanel.prerender(self);
end

--*********************************************************
--* Обработка render
--*********************************************************
function EtherCharacterPanel:render()
    ISPanel.render(self);
    self:clearStencilRect();
end

--*********************************************************
--* Создание нового экземпляра меню
--*********************************************************
function EtherCharacterPanel:new(posX, posY, width, height)
    local menuTableData = {};

    menuTableData = ISPanel:new(posX, posY, width, height);
    setmetatable(menuTableData, self);
    menuTableData.background = true;
	menuTableData.backgroundColor = {r=0.0, g=0.0, b=0.0, a=0.0};
	menuTableData.borderColor = {r=0.0, g=0.0, b=0.0, a=0.0};
    menuTableData.moveWithMouse = true;
    menuTableData.localPlayer = getPlayer();
    menuTableData.checkBoxList = {};
    self.__index = self;

    self.checkBoxList = {}; -- Список всех чекбоксов

    return menuTableData;
end
