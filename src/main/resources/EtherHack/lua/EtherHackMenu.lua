require "ISUI/ISPanel"

local etherModules = {
    "EtherHack/lua/EtherDebugClient.lua",
    "EtherHack/lua/components/override/EtherEditInventoryItem.lua",
    "EtherHack/lua/components/override/EtherEditWorldObjects.lua",
    "EtherHack/lua/components/override/EtherDebugMenu.lua",
    "EtherHack/lua/components/ui/UIButtonsPanel.lua",
    "EtherHack/lua/components/ui/UICheckbox.lua",
    "EtherHack/lua/components/ui/UIButton.lua",
    "EtherHack/lua/components/ui/UISlider.lua",
    "EtherHack/lua/components/ui/UIMechanics.lua",
    "EtherHack/lua/components/ui/UIModalAddXP.lua",
    "EtherHack/lua/components/ui/UIMovableMiniMap.lua",
    "EtherHack/lua/components/ui/UIModalAddTrait.lua",
    "EtherHack/lua/components/ui/UIHealth.lua",
    "EtherHack/lua/components/ui/UIItemTables.lua",
    "EtherHack/lua/components/ui/UIMap.lua",
    "EtherHack/lua/components/ui/UISkillTable.lua",
    "EtherHack/lua/components/ui/UITraitsTable.lua",
    "EtherHack/lua/components/panels/EtherInfoPanel.lua",
    "EtherHack/lua/components/panels/EtherCharacterPanel.lua",
    "EtherHack/lua/components/panels/EtherItemCreator.lua",
    "EtherHack/lua/components/panels/EtherPlayerEditor.lua",
    "EtherHack/lua/components/panels/EtherVisualsPanel.lua",
    "EtherHack/lua/components/panels/EtherMapPanel.lua",
    "EtherHack/lua/components/panels/EtherExploitPanel.lua",
    "EtherHack/lua/components/panels/EtherSettingsPanel.lua"
}

for _, module in ipairs(etherModules) do
    requireExtra(module);
end

EtherMain                   = ISPanel:derive("EtherMain");
EtherMain.instance          = nil;
EtherMain.menuKeyID         = 210;
EtherMain.defaultWidth      = 720;
EtherMain.defaultHeight     = 560;
EtherMain.minWidth          = 620;
EtherMain.minHeight         = 430;
EtherMain.sidebarWidth      = 58;
EtherMain.resizeHandleSize  = 24;
EtherMain.panelPadding      = 18;
EtherMain.rowHeight         = 28;
EtherMain.buttonHeight      = 24;
EtherMain.currentTabID      = 1;
EtherMain.accentColor       = {r = getAccentUIColor():getR(), g = getAccentUIColor():getG(), b = getAccentUIColor():getB(), a = 1.0};

local function etherClamp(value, minValue, maxValue)
    if value < minValue then return minValue end
    if value > maxValue then return maxValue end
    return value
end

local function etherSetBounds(element, x, y, width, height)
    if element == nil then return end

    element.x = x;
    element.y = y;
    element.width = width;
    element.height = height;

    if element.setX then element:setX(x) end
    if element.setY then element:setY(y) end
    if element.setWidth then element:setWidth(width) end
    if element.setHeight then element:setHeight(height) end
end

local function etherGetSavedWindowSize()
    local width = EtherMain.defaultWidth;
    local height = EtherMain.defaultHeight;

    if getEtherUIWidth ~= nil then
        local ok, savedWidth = pcall(getEtherUIWidth);
        if ok and type(savedWidth) == "number" then
            width = savedWidth;
        end
    end

    if getEtherUIHeight ~= nil then
        local ok, savedHeight = pcall(getEtherUIHeight);
        if ok and type(savedHeight) == "number" then
            height = savedHeight;
        end
    end

    local screenWidth = getCore():getScreenWidth();
    local screenHeight = getCore():getScreenHeight();
    width = etherClamp(width, EtherMain.minWidth, math.max(EtherMain.minWidth, screenWidth - 40));
    height = etherClamp(height, EtherMain.minHeight, math.max(EtherMain.minHeight, screenHeight - 40));

    return width, height;
end

function EtherMain:close()
    EtherMain.instance:setVisible(false);
    EtherMain.instance:removeFromUIManager();
    EtherMain.instance = nil;
end

function EtherMain:isInResizeHandle(x, y)
    return x >= self.width - EtherMain.resizeHandleSize and y >= self.height - EtherMain.resizeHandleSize;
end

function EtherMain:saveWindowSize()
    if saveEtherUISize ~= nil then
        pcall(saveEtherUISize, math.floor(self.width), math.floor(self.height));
    end
end

function EtherMain:setWindowSize(width, height, refreshPanel)
    local maxWidth = math.max(EtherMain.minWidth, getCore():getScreenWidth() - self.x - 8);
    local maxHeight = math.max(EtherMain.minHeight, getCore():getScreenHeight() - self.y - 8);
    width = etherClamp(math.floor(width), EtherMain.minWidth, maxWidth);
    height = etherClamp(math.floor(height), EtherMain.minHeight, maxHeight);

    etherSetBounds(self, self.x, self.y, width, height);
    self:layoutChildren(refreshPanel);
end

function EtherMain:layoutChildren(refreshPanel)
    if self.buttonsPanel == nil then return end

    etherSetBounds(self.buttonsPanel, 0, 0, EtherMain.sidebarWidth, self.height);

    if refreshPanel then
        self.buttonsPanel:openPanel(self.buttonsPanel.currentTabID or EtherMain.currentTabID);
    elseif self.buttonsPanel.currentPanel ~= nil then
        etherSetBounds(
            self.buttonsPanel.currentPanel,
            EtherMain.sidebarWidth,
            0,
            self.width - EtherMain.sidebarWidth,
            self.height - EtherMain.resizeHandleSize
        );
    end
end

function EtherMain:drawResizeHandle()
    local size = EtherMain.resizeHandleSize;
    local x = self.width - size;
    local y = self.height - size;
    local r = EtherMain.accentColor.r;
    local g = EtherMain.accentColor.g;
    local b = EtherMain.accentColor.b;

    self:drawRect(x, y, size, size, 0.34, 0, 0, 0);
    self:drawRectBorder(x, y, size, size, 0.85, r, g, b);
    self:drawRect(self.width - 18, self.height - 6, 12, 2, 0.95, r, g, b);
    self:drawRect(self.width - 14, self.height - 11, 8, 2, 0.85, r, g, b);
    self:drawRect(self.width - 10, self.height - 16, 4, 2, 0.75, r, g, b);
end

function EtherMain:onMouseDown(x, y)
    if self:isInResizeHandle(x, y) then
        self.isResizing = true;
        self.resizeStartMouseX = getMouseX();
        self.resizeStartMouseY = getMouseY();
        self.resizeStartWidth = self.width;
        self.resizeStartHeight = self.height;
        self.moveWithMouse = false;
        return true;
    end

    if ISPanel.onMouseDown ~= nil then
        return ISPanel.onMouseDown(self, x, y);
    end
end

function EtherMain:onMouseMove(dx, dy)
    if self.isResizing then
        local width = self.resizeStartWidth + getMouseX() - self.resizeStartMouseX;
        local height = self.resizeStartHeight + getMouseY() - self.resizeStartMouseY;
        self:setWindowSize(width, height, false);
        return true;
    end

    if ISPanel.onMouseMove ~= nil then
        return ISPanel.onMouseMove(self, dx, dy);
    end
end

function EtherMain:onMouseMoveOutside(dx, dy)
    if self.isResizing then
        return self:onMouseMove(dx, dy);
    end
end

function EtherMain:stopResize()
    if not self.isResizing then return end

    self.isResizing = false;
    self.moveWithMouse = true;
    self:setWindowSize(self.width, self.height, true);
    self:saveWindowSize();
end

function EtherMain:onMouseUp(x, y)
    if self.isResizing then
        self:stopResize();
        return true;
    end

    if ISPanel.onMouseUp ~= nil then
        return ISPanel.onMouseUp(self, x, y);
    end
end

function EtherMain:onMouseUpOutside(x, y)
    if self.isResizing then
        self:stopResize();
        return true;
    end
end

function EtherMain:createChildren()
    ISPanel.createChildren(self);

    self.buttonsPanel = UIButtonsPanel:new(0, 0, EtherMain.sidebarWidth, self.height, self, EtherMain.accentColor);
    self.buttonsPanel:initialise();
    self.buttonsPanel:instantiate();
    self.buttonsPanel:setVisible(true);
    self:addChild(self.buttonsPanel);

    self.buttonsPanel:addButton("EtherHack/media/ui/info.png", EtherInfoPanel);
    self.buttonsPanel:addButton("EtherHack/media/ui/character.png", EtherCharacterPanel);
    self.buttonsPanel:addButton("EtherHack/media/ui/itemCreator.png", EtherItemCreator);
    self.buttonsPanel:addButton("EtherHack/media/ui/playerEditor.png", EtherPlayerEditor);
    self.buttonsPanel:addButton("EtherHack/media/ui/visuals.png", EtherVisualsPanel);
    self.buttonsPanel:addButton("EtherHack/media/ui/teleport.png", EtherMapPanel);
    self.buttonsPanel:addButton("EtherHack/media/ui/exploit.png", EtherExploitPanel);
    self.buttonsPanel:addButton("EtherHack/media/ui/settings.png", EtherSettingsPanel);

    self.buttonsPanel:openPanel(EtherMain.currentTabID);
end

function EtherMain:prerender()
    ISPanel.prerender(self);
    self:drawResizeHandle();
end

function EtherMain:render()
    ISPanel.render(self);
    self:drawResizeHandle();
    self:drawRectBorder(0, 0, self.width, self.height, 0.45, EtherMain.accentColor.r, EtherMain.accentColor.g, EtherMain.accentColor.b);
end

function EtherMain.OnOpenPanel(key)
    if key == EtherMain.menuKeyID then
        if EtherMain.instance ~= nil then
            EtherMain.instance:setVisible(false);
            EtherMain.instance:removeFromUIManager();
            EtherMain.instance = nil;
            return
        end

        EtherMain.instance = EtherMain:new();
        EtherMain.instance:initialise();
        EtherMain.instance:instantiate();
        EtherMain.instance:addToUIManager();
        EtherMain.instance:setVisible(true);
        EtherMain.instance:setAlwaysOnTop(false);
    end
end

function EtherMain:new()
    local width, height = etherGetSavedWindowSize();
    local positionX = getCore():getScreenWidth() / 2 - width / 2;
    local positionY = getCore():getScreenHeight() / 2 - height / 2;

    local menuTableData = ISPanel:new(positionX, positionY, width, height);
    setmetatable(menuTableData, self);
    menuTableData.background = true;
    menuTableData.backgroundColor = {r=0.05, g=0.05, b=0.05, a=1};
    menuTableData.borderColor = {r=0, g=0, b=0, a=0};
    menuTableData.moveWithMouse = true;
    menuTableData.isResizing = false;
    self.__index = self;

    return menuTableData;
end

Events.OnKeyPressed.Add(EtherMain.OnOpenPanel);
