require "ISUI/ISPanel"

UIButtonsPanel = ISPanel:derive("UIButtonsPanel");

function UIButtonsPanel:prerender()
    ISPanel.prerender(self);

    for id = 1, #self.buttons do
        local button = self.buttons[id]
        local buttonY = self.topPadding + (id - 1) * self.buttonStep;

        if button.id == self.currentTabID then
            self:drawRect(0, buttonY - 8, 5, self.buttonStep - 4, 1.0, EtherMain.accentColor.r, EtherMain.accentColor.g, EtherMain.accentColor.b);
            button:setTextureRGBA(EtherMain.accentColor.r, EtherMain.accentColor.g, EtherMain.accentColor.b, 1.0)
        else
            button:setTextureRGBA(1.0, 1.0, 1.0, 0.86)
        end
    end
end

function UIButtonsPanel:openPanel(id)
    if #self.buttons <= 0 then return end

    id = id or 1;
    local button = self.buttons[id] or self.buttons[1];
    local panelById = button.panelTag;
    self.currentTabID = button.id;

    if self.currentPanel ~= nil then
        self.currentPanel:setVisible(false);
        self.parent:removeChild(self.currentPanel)
    end

    local panelHeight = self.parent.height - EtherMain.resizeHandleSize;
    local panel = panelById:new(self.width, 0, self.parent.width - self.width, panelHeight);
    panel:initialise();
    panel:instantiate();
    panel:setVisible(true);
    self.parent:addChild(panel);

    self.currentPanel = panel;
    EtherMain.currentTabID = self.currentTabID;
end

function UIButtonsPanel:onButtonClick(button)
    self:openPanel(button.id);
end

function UIButtonsPanel:layoutButtons()
    for index, button in ipairs(self.buttons) do
        local x = math.floor((self.width - self.buttonSize.width) / 2);
        local y = self.topPadding + (index - 1) * self.buttonStep;

        button.x = x;
        button.y = y;
        if button.setX then button:setX(x) end
        if button.setY then button:setY(y) end
    end
end

function UIButtonsPanel:addButton(iconPath, panelTag)
    local id = #self.buttons + 1;
    local posX = math.floor((self.width - self.buttonSize.width) / 2);
    local posY = self.topPadding + (id - 1) * self.buttonStep;

    local button = ISButton:new(posX, posY, self.buttonSize.width, self.buttonSize.height, "", self, self.onButtonClick);
    button.anchorRight = false;
    button.anchorLeft = true;
    button:initialise();
    button.borderColor.a = 0.0;
    button.backgroundColor.a = 0;
    button.backgroundColorMouseOver.a = 0.08;
    button.id = id;
    button.panelTag = panelTag;
    button:setImage(getExtraTexture(iconPath));
    self:addChild(button);
    button:setVisible(true);

    table.insert(self.buttons, button);
end

function UIButtonsPanel:new(posX, posY, width, height, parent, accentColor)
    local menuTableData = ISPanel:new(posX, posY, width, height);
    setmetatable(menuTableData, self);
    menuTableData.background = true;
    menuTableData.backgroundColor = {r=0.09, g=0.09, b=0.09, a=1.0};
    menuTableData.borderColor = {r=0, g=0, b=0, a=0};
    menuTableData.moveWithMouse = false;
    self.__index = self;

    menuTableData.parent = parent;
    menuTableData.buttons = {};
    menuTableData.buttonSize = {width = 34, height = 34};
    menuTableData.buttonStep = 54;
    menuTableData.topPadding = 14;
    menuTableData.currentTabID = 1;
    menuTableData.currentPanel = nil;

    return menuTableData;
end
