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

    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);
    self:addCheckBox(getTranslate(""), function(isChecked) toggle(isChecked); end, is(), false);

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
