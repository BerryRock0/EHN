require "ISUI/ISPanel"

--*********************************************************
--* Глобальные установки UI
--*********************************************************
EtherItemCreator = ISPanel:derive("EtherItemCreator"); -- Наследование от ISPanel

--*********************************************************
--* Обработка prerender
--*********************************************************
function EtherItemCreator:prerender()
    self:setStencilRect(0,10,self:getWidth(),self:getHeight() - 20);
    ISPanel.prerender(self);
end

--*********************************************************
--* Обработка render
--*********************************************************
function EtherItemCreator:render()
    ISPanel.render(self);
    self:clearStencilRect();

    if self.localPlayer == nil then 
        self:drawTextCentre(self.workInGameText, self.width / 2, self.height / 2, 1.0, 1.0, 1.0, 1.0, UIFont.Large)
    end;
end

--*********************************************************
--* Создание дочерних элементов
--*********************************************************
function EtherItemCreator:createChildren()
    ISPanel.createChildren(self);

    if self.localPlayer == nil then return end;

    self.panel = ISTabPanel:new(15, 10, self.width - 15 * 2, self.height - 30);
    self.panel:initialise();
    self.panel.borderColor = { r = 0, g = 0, b = 0, a = 0};
    self.panel.target = self;
    self.panel.equalTabWidth = false
    self:addChild(self.panel);

    self:initList();
end

--*********************************************************
--* Инициализация таблиц с предметами
--*********************************************************
function EtherItemCreator:initList()
    self.items = getAllItems();
    self.displayCategories = {};

    local displayCategoryNames = {}
    local allItems = {}
    for i=0,self.items:size()-1 do
        local item = self.items:get(i);
        if not item:getObsolete() and not item:isHidden() then
            table.insert(allItems, item)

            local displayCategory = UIItemTables.getItemDisplayCategory(item)
            if displayCategory ~= nil then
                if not self.displayCategories[displayCategory] then
                    self.displayCategories[displayCategory] = {}
                    table.insert(displayCategoryNames, displayCategory)
                end
                table.insert(self.displayCategories[displayCategory], item)
            end
        end
    end

    table.sort(displayCategoryNames, function(a,b)
        return not string.sort(UIItemTables.getDisplayCategoryText(a), UIItemTables.getDisplayCategoryText(b))
    end)

    local listBox = UIItemTables:new(0, 0, self.panel.width, self.panel.height - self.panel.tabHeight);
    listBox:initialise();
    self.panel:addView("All", listBox);
    listBox:initList(allItems);

    for _,displayCategoryName in ipairs(displayCategoryNames) do
        local categoryItems = self.displayCategories[displayCategoryName]
        if categoryItems and #categoryItems > 0 then
            local categoryTable = UIItemTables:new(0, 0, self.panel.width, self.panel.height - self.panel.tabHeight);
            categoryTable:initialise();
            self.panel:addView(UIItemTables.getDisplayCategoryText(displayCategoryName), categoryTable);
            categoryTable:initList(categoryItems);
        end
    end

    self.panel:activateView("All");
end

--*********************************************************
--* Создание нового экземпляра меню
--*********************************************************
function EtherItemCreator:new(posX, posY, width, height)
    local menuTableData = {};

    menuTableData = ISPanel:new(posX, posY, width, height);
    setmetatable(menuTableData, self);
    menuTableData.background = true;
	menuTableData.backgroundColor = {r=0.0, g=0.0, b=0.0, a=0.0};
	menuTableData.borderColor = {r=0.0, g=0.0, b=0.0, a=0.0};
    menuTableData.moveWithMouse = true;
    menuTableData.workInGameText = getTranslate("UI_ItemCreator_PanelWorkOnlyInGame");
    menuTableData.localPlayer = getPlayer();
    self.__index = self;

    return menuTableData;
end
