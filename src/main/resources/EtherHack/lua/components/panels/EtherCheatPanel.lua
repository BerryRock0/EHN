require "ISUI/ISPanel"

--*********************************************************
--* Глобальные установки UI
--*********************************************************
EtherCheatPanel = ISPanel:derive("EtherCheatPanel"); -- Наследование от ISPanel

--*********************************************************
--* Добавление чекбоксов
--*********************************************************
function EtherCheatPanel:addCheckBox(title, method, isSelected, isOnlyInGame)
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
function EtherCheatPanel:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del * 40));
	return true;
end

--*********************************************************
--* Обновление панели
--*********************************************************
function EtherCheatPanel:updatePanel()
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
function EtherCheatPanel:createChildren()
    ISPanel.createChildren(self);

    self:setScrollChildren(true)
    self:setScrollHeight(0)
    self:addScrollBars();

    self:addCheckBox(getTranslate("UI_CheatPanel_GodMode"), function(isChecked) toggleGodMod(isChecked); end, isGodMod(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_Invisible"), function(isChecked) toggleInvisible(isChecked); end, isInvisible(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_UnlimitedEndurance"), function(isChecked) toggleEndurance(isChecked); end, isEndurance(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_UnlimitedAmmo"), function(isChecked) toggleAmmo(isChecked); end, isAmmo(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_KnowAllRecipes"), function(isChecked) toggleKnowAllRecipes(isChecked); end, isKnowAllRecipes(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_UnlimitedCarry"), function(isChecked) toggleCarry(isChecked); end, isCarry(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_Build"), function(isChecked) toggleBuild(isChecked); end, isBuild(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_Farming"), function(isChecked) toggleFarming(isChecked); end, isFarming(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_Fishing"), function(isChecked) toggleFishing(isChecked); end, isFishing(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_Health"), function(isChecked) toggleHealth(isChecked); end, isHealth(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_Mechanics"), function(isChecked) toggleMechanics(isChecked); end, isMechanics(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_FastMove"), function(isChecked) toggleFastMove(isChecked); end, isFastMove(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_Movables"), function(isChecked) toggleMovables(isChecked); end, isMovables(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_TimedActionInstant"), function(isChecked) toggleTimedActionInstant(isChecked); end, isTimedActionInstant(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_BrushTool"), function(isChecked) toggleBrushTool(isChecked); end, isBrushTool(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_NoClip"), function(isChecked) toggleNoClip(isChecked); end, isNoClip(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_CanSeeEveryone"), function(isChecked) toggleSee(isChecked); end, isSee(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_CanHearEveryone"), function(isChecked) toggleHear(isChecked); end, isHear(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_ZombiesDontAttack"), function(isChecked) toggleZombiesDontAttack(isChecked); end, isZombiesDontAttack(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_LootZed"), function(isChecked) toggleLootZed(isChecked); end, isLootZed(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_LootLog"), function(isChecked) toggleLootLog(isChecked); end, isLootLog(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_DebugMenuContext"), function(isChecked) toggleDebugMenuContext(isChecked); end, isDebugMenuContext(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_Animal"), function(isChecked) toggleAnimal(isChecked); end, isAnimal(), false);
    self:addCheckBox(getTranslate("UI_CheatPanel_AnimalExtraValues"), function(isChecked) toggleAnimalExtraValues(isChecked); end, isAnimalExtraValues(), false);

    self:updatePanel();
end

--*********************************************************
--* Обработка prerender
--*********************************************************
function EtherCheatPanel:prerender()
    self:setStencilRect(0,10,self:getWidth(),self:getHeight() - 20);
    ISPanel.prerender(self);
end

--*********************************************************
--* Обработка render
--*********************************************************
function EtherCheatPanel:render()
    ISPanel.render(self);
    self:clearStencilRect();
end

--*********************************************************
--* Создание нового экземпляра меню
--*********************************************************
function EtherCheatPanel:new(posX, posY, width, height)
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
