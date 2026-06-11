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

	local addRoleBtn = ISButton:new(250, 10, 60, 18, getTranslate("UI_CheatPanel_AddRole"), self, self.onAddRoleButton)
    editTimeBtn:initialise()
    editTimeBtn:instantiate()
    self:addChild(addRoleBtn)

	local removeRoleBtn = ISButton:new(250, 30, 60, 18, getTranslate("UI_CheatPanel_RemoveRole"), self, self.onRemoveRoleButton)
    editTimeBtn:initialise()
    editTimeBtn:instantiate()
    self:addChild(removeRoleBtn)
	
    self:updatePanel();
end

function EtherPlayerEditor:onAddRoleButton()
	local modal = ISTextBox:new(0, 0, 280, 180, getTranslate("UI_CheatPanel_AddRole"),
		tostring(getRole()),
        self,
		function(target, button)
			if button.internal == "OK" then
				local value = tostring(button.parent.entry:getText())
				if value then
					addRole(value)
					self:updateLabels()
				end
			end
		end)
	modal:initialise()
    modal:addToUIManager()
end

function EtherPlayerEditor:onRemoveRoleButton()
	local modal = ISTextBox:new(0, 0, 280, 180, getTranslate("UI_CheatPanel_RemoveRole"),
		tostring(getRole()),
        self,
		function(target, button)
			if button.internal == "OK" then
				local value = tostring(button.parent.entry:getText())
				if value then
					removeRole(value)
					self:updateLabels()
				end
			end
		end)
	modal:initialise()
    modal:addToUIManager()
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
