require "ISUI/ISComboBox"
require "ISUI/ISPanel"

--*********************************************************
--* Глобальные установки UI
--*********************************************************
UIItemTables = ISPanel:derive("UIItemTables");

local fontHeightSmall = getTextManager():getFontHeight(UIFont.Small)
local CATEGORY_ANY = "__ETHER_ANY__"
local CATEGORY_NONE = "__ETHER_NONE__"
local CATEGORY_ANY_TEXT = "<Any>"
local CATEGORY_NONE_TEXT = "<No category set>"

function UIItemTables.getItemDisplayCategory(scriptItem)
    if scriptItem == nil then return nil end

    local category = scriptItem:getDisplayCategory()
    if category == nil or category == "" then
        return nil
    end

    return category
end

function UIItemTables.getDisplayCategoryText(category)
    if category == nil or category == "" then
        return CATEGORY_NONE_TEXT
    end

    local textKey = "IGUI_ItemCat_" .. category
    local translated = getText(textKey)
    if translated == nil or translated == textKey then
        return category
    end

    return translated
end

--*********************************************************
--* Обработка render
--*********************************************************
function UIItemTables:render()
    ISPanel.render(self);
    
    local y = self.datas.y + self.datas.height + 5
    self:drawText(getText("IGUI_DbViewer_TotalResult") .. self.totalResult, 0, y, 1,1,1,1,UIFont.Small)
end

--*********************************************************
--* Создание дочерних элементов
--*********************************************************
function UIItemTables:createChildren()
    ISPanel.createChildren(self);

    self.datas = ISScrollingListBox:new(0, 25, self.width, self.height - 150);
    self.datas:initialise();
    self.datas:instantiate();
    self.datas.itemheight = fontHeightSmall + 4 * 2
    self.datas.selected = 0;
    self.datas.joypadParent = self;
    self.datas.font = UIFont.NewSmall;
    self.datas.doDrawItem = self.drawDatas;
    self.datas.drawBorder = true;
    self.datas:addColumn(getTranslate("UI_ItemCreator_Title_ItemName"), 0);
    self.datas:addColumn(getTranslate("UI_ItemCreator_Title_ItemCategory"), 250)
    self:addChild(self.datas);

    local filterGap = 10
    local filterY = self.height - 20
    local filterLabelY = self.height - 40
    local categoryWidth = math.max(155, math.floor(self.width * 0.28))
    local textFilterWidth = math.floor((self.width - categoryWidth - filterGap * 2) / 2)
    local idFilterX = categoryWidth + filterGap + textFilterWidth + filterGap

    self.filterByCategoryTitle = ISLabel:new(0, filterLabelY, 20, getTranslate("UI_ItemCreator_Title_ItemCategory"), 1, 1, 1, 1, UIFont.Medium, true)
    self.filterByCategoryTitle:initialise()
    self.filterByCategoryTitle:instantiate()
    self:addChild(self.filterByCategoryTitle)

    self.filterByCategory = ISComboBox:new(0, filterY, categoryWidth, 20)
    self.filterByCategory.font = UIFont.Small
    self.filterByCategory:initialise()
    self.filterByCategory:instantiate()
    self.filterByCategory.target = self.filterByCategory
    self.filterByCategory.itemsListFilter = self.filterDisplayCategory
    self.filterByCategory.onChange = UIItemTables.onFilterChange
    self:addChild(self.filterByCategory)
    table.insert(self.filterWidgets, self.filterByCategory)

    self.filterByNameTitle = ISLabel:new(categoryWidth + filterGap, filterLabelY, 20, getTranslate("UI_ItemCreator_Title_FilterByName"), 1, 1, 1, 1, UIFont.Medium, true)
    self.filterByNameTitle:initialise()
    self.filterByNameTitle:instantiate()
    self:addChild(self.filterByNameTitle)

    self.filterByName = ISTextEntryBox:new("", categoryWidth + filterGap, filterY, textFilterWidth, 20);
    self.filterByName.font = UIFont.Small;
    self.filterByName:initialise();
    self.filterByName:instantiate();
    self.filterByName.target = self.filterByName;
    self.filterByName.itemsListFilter = self.filterName;
    self.filterByName.onTextChange = UIItemTables.onFilterChange;
    self.filterByName.onTextChangeFunction = UIItemTables.onFilterChange;
    self.filterByName:setClearButton(true)
    self:addChild(self.filterByName);
    table.insert(self.filterWidgets, self.filterByName);

    self.filterByIdTitle = ISLabel:new(idFilterX, filterLabelY, 20, getTranslate("UI_ItemCreator_Title_FilterById"), 1, 1, 1, 1, UIFont.Medium, true)
    self.filterByIdTitle:initialise()
    self.filterByIdTitle:instantiate()
    self:addChild(self.filterByIdTitle)

    self.filterById = ISTextEntryBox:new("", idFilterX, filterY, self.width - idFilterX, 20);
    self.filterById.font = UIFont.Small;
    self.filterById:initialise();
    self.filterById:instantiate();
    self.filterById:setClearButton(true)
    self.filterById.target = self.filterById;
    self.filterById.itemsListFilter = self.filterType;
    self.filterById.onTextChange = UIItemTables.onFilterChange;
    self.filterById.onTextChangeFunction = UIItemTables.onFilterChange;
    self:addChild(self.filterById);
    table.insert(self.filterWidgets, self.filterById);

    self.addItemX1 = UIButton:new(0, self.height - 80, 100, 24, getTranslate("UI_ItemCreator_Button_AddItemX1"), 
    function() 
        local item = self:getSelectedScriptItem();
        if item then EtherDebugClient.giveItem(item:getFullName(), 1) end
    end)
    self.addItemX1:initialise();
    self.addItemX1:instantiate();
    self.addItemX1:setAnchorLeft(true);
    self.addItemX1:setAnchorRight(false);
    self.addItemX1:setAnchorTop(false);
    self.addItemX1:setAnchorBottom(true);
    self.addItemX1.isOnlyInGame = true;
    self.addItemX1.isRequireSelected = true;
    self:addChild(self.addItemX1);
    table.insert(self.buttonList, self.addItemX1);

    self.addItemX2 = UIButton:new(self.addItemX1:getX() + self.addItemX1.width + 10, self.height - 80, 100, 24, getTranslate("UI_ItemCreator_Button_AddItemX2"), 
    function() 
        local item = self:getSelectedScriptItem();
        if item then EtherDebugClient.giveItem(item:getFullName(), 2) end
    end)
    self.addItemX2:initialise();
    self.addItemX2:instantiate();
    self.addItemX2:setAnchorLeft(true);
    self.addItemX2:setAnchorRight(false);
    self.addItemX2:setAnchorTop(false);
    self.addItemX2:setAnchorBottom(true);
    self.addItemX2.isOnlyInGame = true;
    self.addItemX2.isRequireSelected = true;
    self:addChild(self.addItemX2);
    table.insert(self.buttonList, self.addItemX2);

    self.addItemX5 = UIButton:new(self.addItemX2:getX() + self.addItemX2.width + 10, self.height - 80, 100, 24, getTranslate("UI_ItemCreator_Button_AddItemX5"), 
    function() 
        local item = self:getSelectedScriptItem();
        if item then EtherDebugClient.giveItem(item:getFullName(), 5) end
    end)
    self.addItemX5:initialise();
    self.addItemX5:instantiate();
    self.addItemX5:setAnchorLeft(true);
    self.addItemX5:setAnchorRight(false);
    self.addItemX5:setAnchorTop(false);
    self.addItemX5:setAnchorBottom(true);
    self.addItemX5.isOnlyInGame = true;
    self.addItemX5.isRequireSelected = true;
    self:addChild(self.addItemX5);
    table.insert(self.buttonList, self.addItemX5);

    self.addItemX10 = UIButton:new(self.addItemX5:getX() + self.addItemX5.width + 10, self.height - 80, 100, 24, getTranslate("UI_ItemCreator_Button_AddItemX10"), 
    function() 
        local item = self:getSelectedScriptItem();
        if item then EtherDebugClient.giveItem(item:getFullName(), 10) end
    end)
    self.addItemX10:initialise();
    self.addItemX10:instantiate();
    self.addItemX10:setAnchorLeft(true);
    self.addItemX10:setAnchorRight(false);
    self.addItemX10:setAnchorTop(false);
    self.addItemX10:setAnchorBottom(true);
    self.addItemX10.isOnlyInGame = true;
    self.addItemX10.isRequireSelected = true;
    self:addChild(self.addItemX10);
    table.insert(self.buttonList, self.addItemX10);

    self:updatePanel();
end

--*********************************************************
--* Обновление панели
--*********************************************************
function UIItemTables:updatePanel()
    local player = getPlayer()
    local hasPlayer = player ~= nil and not player:isDead()
    local hasSelectedItem = self:getSelectedScriptItem() ~= nil

    for i=1, #self.buttonList do
        local item = self.buttonList[i];
        local enabled = true
        if item.isOnlyInGame then
            enabled = enabled and hasPlayer
        end
        if item.isRequireSelected then
            enabled = enabled and hasSelectedItem
        end
        item:setEnable(enabled);
    end
end

function UIItemTables:getSelectedScriptItem()
    if self.datas == nil or self.datas.items == nil then return nil end

    local selected = self.datas.items[self.datas.selected]
    if selected == nil then
        return nil
    end

    return selected.item
end


--*********************************************************
--* Инициализация списков
--*********************************************************
function UIItemTables:initList(module)
    self.totalResult = 0;
    local displayCategoryNames = {}
    local displayCategoryMap = {}
    for _, v in ipairs(module) do
        self.datas:addItem(v:getDisplayName(), v);

        local displayCategory = UIItemTables.getItemDisplayCategory(v)
        if displayCategory ~= nil and not displayCategoryMap[displayCategory] then
            displayCategoryMap[displayCategory] = true
            table.insert(displayCategoryNames, displayCategory)
        end
        self.totalResult = self.totalResult + 1;
    end
    table.sort(self.datas.items, function(a,b) return not string.sort(a.item:getDisplayName(), b.item:getDisplayName()); end);
    self.datas.fullList = self.datas.items

    table.sort(displayCategoryNames, function(a,b)
        return not string.sort(UIItemTables.getDisplayCategoryText(a), UIItemTables.getDisplayCategoryText(b))
    end)

    self.filterByCategory:clear()
    self.filterByCategory.selected = 0
    self.filterByCategory:addOptionWithData(CATEGORY_ANY_TEXT, CATEGORY_ANY)
    self.filterByCategory:addOptionWithData(CATEGORY_NONE_TEXT, CATEGORY_NONE)
    for _, displayCategoryName in ipairs(displayCategoryNames) do
        self.filterByCategory:addOptionWithData(UIItemTables.getDisplayCategoryText(displayCategoryName), displayCategoryName)
    end
end

--*********************************************************
--* Обновление таблицы
--*********************************************************
function UIItemTables:update()
    self.datas.doDrawItem = self.drawDatas;
    self:updatePanel();
end

--*********************************************************
--* Filter by display category
--*********************************************************
function UIItemTables:filterDisplayCategory(widget, scriptItem)
    local selectedCategory = widget:getOptionData(widget.selected)
    if selectedCategory == nil or selectedCategory == CATEGORY_ANY then
        return true
    end

    local itemCategory = UIItemTables.getItemDisplayCategory(scriptItem)
    if selectedCategory == CATEGORY_NONE then
        return itemCategory == nil
    end

    return itemCategory == selectedCategory
end

--*********************************************************
--* Фильтр по названию
--*********************************************************
function UIItemTables:filterName(widget, scriptItem)
    local txtToCheck = string.lower(scriptItem:getDisplayName() or "")
    local filterTxt = string.lower(widget:getInternalText())
    return checkStringPattern(filterTxt) and string.match(txtToCheck, filterTxt)
end

--*********************************************************
--* Фильтр по ID
--*********************************************************
function UIItemTables:filterType(widget, scriptItem)
    local nameToCheck = string.lower(scriptItem:getName() or "")
    local fullNameToCheck = string.lower(scriptItem:getFullName() or "")
    local filterTxt = string.lower(widget:getInternalText())

    if not checkStringPattern(filterTxt) then
        return false
    end

    return string.match(nameToCheck, filterTxt) or string.match(fullNameToCheck, filterTxt)
end

--*********************************************************
--* Применение фильтра при написании текста
--*********************************************************
function UIItemTables.onFilterChange(widget)
    local datas = widget.parent.datas;
    widget.parent.totalResult = 0;
    datas:clear();
    if datas.setScrollHeight then datas:setScrollHeight(0) end
    for i,v in ipairs(datas.fullList) do -- check every items
        local add = true;
        for j,widget in ipairs(widget.parent.filterWidgets) do -- check every filters
            if not widget.itemsListFilter(self, widget, v.item) then
                add = false
                break
            end
        end
        if add then
            datas:addItem(v.item:getDisplayName(), v.item);
            widget.parent.totalResult = widget.parent.totalResult + 1;
        end
    end
    widget.parent:updatePanel();
end

--*********************************************************
--* Отрисовка данных
--*********************************************************
function UIItemTables:drawDatas(y, item, alt)
    if y + self:getYScroll() + self.itemheight < 0 or y + self:getYScroll() >= self.height then
        return y + self.itemheight
    end
    
    local a = 0.9;

    if self.selected == item.index then
        self:drawRect(0, y, self:getWidth(), self.itemheight, 0.3, EtherMain.accentColor.r, EtherMain.accentColor.g, EtherMain.accentColor.b);
    end

    if alt then
        self:drawRect(0, y, self:getWidth(), self.itemheight, 0.3, 0.3, 0.3, 0.3);
    end

    self:drawRectBorder(0, y, self:getWidth(), self.itemheight, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

    local iconX = 4
    local iconSize = fontHeightSmall;

    local clipX = self.columns[1].size
    local clipX2 = self.columns[2].size
    local clipY = math.max(0, y + self:getYScroll())
    local clipY2 = math.min(self.height, y + self:getYScroll() + self.itemheight)
    
    self:setStencilRect(clipX, clipY, clipX2 - clipX, clipY2 - clipY)
    self:drawText(item.item:getDisplayName(), 25, y + 4, 1, 1, 1, a, self.font);
    self:clearStencilRect()

    local displayCategory = UIItemTables.getItemDisplayCategory(item.item)
    self:drawText(UIItemTables.getDisplayCategoryText(displayCategory), self.columns[2].size + 10, y + 4, 1, 1, 1, a, self.font);
    
    self:repaintStencilRect(0, clipY, self.width - 20, clipY2 - clipY)

    local icon = item.item:getIcon()
    if item.item:getIconsForTexture() and not item.item:getIconsForTexture():isEmpty() then
        icon = item.item:getIconsForTexture():get(0)
    end
    if icon then
        local texture = getTexture("Item_" .. icon)
        if texture then
            self:drawTextureScaledAspect2(texture, self.columns[1].size + iconX, y + (self.itemheight - iconSize) / 2, iconSize, iconSize,  1, 1, 1, 1);
        end
    end
    
    return y + self.itemheight;
end

--*********************************************************
--* Создание нового экземпляра меню
--*********************************************************
function UIItemTables:new (x, y, width, height)
    local menuTableData = ISPanel:new(x, y, width, height);
    setmetatable(menuTableData, self);
    menuTableData.listHeaderColor = {r=0.4, g=0.4, b=0.4, a=0.0};
    menuTableData.borderColor = {r=0.4, g=0.4, b=0.4, a=0};
    menuTableData.backgroundColor = {r=0, g=0, b=0, a=0};
    menuTableData.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.0};
    menuTableData.totalResult = 0;
    menuTableData.filterWidgets = {};
    menuTableData.buttonList = {};
    UIItemTables.instance = menuTableData;
    return menuTableData;
end
