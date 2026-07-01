--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"
require "ISUI/AdminPanel/ISAdminPowerUI"

EtherDebugMenu = ISPanel:derive("EtherDebugMenu");
EtherDebugMenu.instance = nil;
EtherDebugMenu.forceEnable = false;
EtherDebugMenu.shiftDown = 0;
EtherDebugMenu.tab = "MAIN"

function EtherDebugMenu:setupButtons()
-- MAIN
self:addButtonInfo("General", EtherDebugMenu.onClickGeneral, "MAIN");
self:addButtonInfo("BrushTool", EtherDebugMenu.onClickBrushTool, "MAIN");
self:addButtonInfo("Climate", EtherDebugMenu.onClickClimate, "MAIN");
self:addButtonInfo("CraftRecipies", EtherDebugMenu.onClickCraftRecipes, "MAIN");
self:addButtonInfo("Player", EtherDebugMenu.onClickPlayer, "MAIN");
self:addButtonInfo("Items", EtherDebugMenu.onClickItems, "MAIN");
self:addButtonInfo("Fluids", EtherDebugMenu.onClickFluids, "MAIN");
self:addButtonInfo("Entities", EtherDebugMenu.onClickEntities, "MAIN");
self:addButtonInfo("Scripts", EtherDebugMenu.onClickScripts, "MAIN");
self:addButtonInfo("XUI", EtherDebugMenu.onClickXUI, "MAIN");
self:addButtonInfo("RecipeMonitor", EtherDebugMenu.onClickRecipeMonitor, "MAIN");
self:addButtonInfo("Sandbox", EtherDebugMenu.onClickSandbox, "MAIN");
self:addButtonInfo("Close", nil, "MAIN", 10);

-- DEV
self:addButtonInfo("Audio", EtherDebugMenu.onClickAudio, "DEV");
self:addButtonInfo("IsoRegions", EtherDebugMenu.onClickIsoRegions, "DEV");
self:addButtonInfo("Population", EtherDebugMenu.onClickPopulation, "DEV");
self:addButtonInfo("Stash", EtherDebugMenu.onClickStash, "DEV");
self:addButtonInfo("AnimMonitor", EtherDebugMenu.onClickAnimMonitor, "DEV");
self:addButtonInfo("Radio", EtherDebugMenu.onClickRadio, "DEV");
self:addButtonInfo("AnimViewer", EtherDebugMenu.onClickAnimViewer, "DEV");
self:addButtonInfo("Attachment", EtherDebugMenu.onClickAttachment, "DEV");
self:addButtonInfo("ChunkDebug", EtherDebugMenu.onClickChunkDebug, "DEV");
self:addButtonInfo("GlobalObject", EtherDebugMenu.onClickGlobalObject, "DEV");
self:addButtonInfo("MapEdit", EtherDebugMenu.onClickMapEdit, "DEV");
self:addButtonInfo("VehicleEdit", EtherDebugMenu.onClickVehicleEdit, "DEV");
self:addButtonInfo("WorldFlares", EtherDebugMenu.onClickWorldFlares, "DEV");
self:addButtonInfo("GlobalModData", EtherDebugMenu.onClickGlobalModData, "DEV");
self:addButtonInfo("NewUI", EtherDebugMenu.onClickNewUI, "DEV");
self:addButtonInfo("UnitTests", EtherDebugMenu.onClickUnitTests, "DEV");
self:addButtonInfo("CharacterDebug", EtherDebugMenu.onClickCharacterDebug, "DEV");
self:addButtonInfo("ForgetRecipes", EtherDebugMenu.onClickForgetRecipes, "DEV");
self:addButtonInfo("Close", nil, "DEV", 10);

end

--MAIN
function EtherDebugMenu.onClickGeneral() ISGeneralDebug.OnOpenPanel() end
function EtherDebugMenu.onClickBrushTool() BrushToolManager.openPanel(getPlayer()) end
function EtherDebugMenu.onClickClimate() ClimateControlDebug.OnOpenPanel() end
function EtherDebugMenu.onClickCraftRecipes() ISCraftRecipeDbgWindow.OnOpenPanel() end
function EtherDebugMenu.onClickPlayer() ISPlayerStatsUI.OnOpenPanel() end
function EtherDebugMenu.onClickItems() ISItemsListViewer.OnOpenPanel() end
function EtherDebugMenu.onClickFluids()  ISFluidDebugWindow.OnOpenPanel() end
function EtherDebugMenu.onClickEntities() ISEntitiesDebugWindow.OnOpenPanel() end
function EtherDebugMenu.onClickScripts() ISScriptsDebugWindow.OnOpenPanel() end
function EtherDebugMenu.onClickXUI() XuiDebugWindow.OnOpenPanel() end
function EtherDebugMenu.onClickRecipeMonitor() ISRecipeMonitor.OnOpenPanel() end
function EtherDebugMenu.onClickSandbox() ISDebugMenu:onClickSandboxSettings() end


--DEV
function EtherDebugMenu.onClickAudio() ISAudioDebugPanel.OnOpenPanel() end
function EtherDebugMenu.onClickIsoRegions() IsoRegionsWindow.OnOpenPanel() end
function EtherDebugMenu.onClickPopulation() ZombiePopulationWindow.OnOpenPanel() end
function EtherDebugMenu.onClickStash() StashDebug.OnOpenPanel() end
function EtherDebugMenu.onClickAnimMonitor() ISAnimDebugMonitor.OnOpenPanel() end
function EtherDebugMenu.onClickRadio() ZomboidRadioDebug.OnOpenPanel() end
function EtherDebugMenu.onClickAnimViewer() showAnimationViewer() end
function EtherDebugMenu.onClickAttachment() showAttachmentEditor() end
function EtherDebugMenu.onClickChunkDebug() showChunkDebugger() end
function EtherDebugMenu.onClickGlobalObject() showGlobalObjectDebugger() end
function EtherDebugMenu.onClickMapEdit() showWorldMapEditor(nil) end
function EtherDebugMenu.onClickVehicleEdit() showVehicleEditor(nil) end
function EtherDebugMenu.onClickWorldFlares() WorldFlaresDebug.OnOpenPanel() end
function EtherDebugMenu.onClickGlobalModData() GlobalModDataDebug.OnOpenPanel() end
function EtherDebugMenu.onClickNewUI() doNewUIDebug() end
function EtherDebugMenu.onClickUnitTests() UnitTestsDebug:OnOpenPanel() end
function EtherDebugMenu.onClickCharacterDebug() ISCharacterDebugUI.OnOpenPanel() end
function EtherDebugMenu.onClickForgetRecipes() ISDebugMenu:onForgetRecipes() end

function EtherDebugMenu:addButtonInfo(_title, _func, _tab, _marginTop)
    self.buttons = self.buttons or {};

    table.insert(self.buttons, { title = _title, func = _func, tab = _tab, marginTop = (_marginTop or 0) })
end

function EtherDebugMenu.OnOpenPanel()
        if EtherDebugMenu.instance==nil then
            EtherDebugMenu.instance = EtherDebugMenu:new (100, 100, 200, 20, getPlayer());
            EtherDebugMenu.instance:initialise();
            EtherDebugMenu.instance:instantiate();
        end

        EtherDebugMenu.instance:addToUIManager();
        EtherDebugMenu.instance:setVisible(true);

        return EtherDebugMenu.instance;
end

function EtherDebugMenu:initialise()
    ISPanel.initialise(self);
end

function EtherDebugMenu:createChildren()
    ISPanel.createChildren(self);

    self.buttons = {};
    self:setupButtons();

    local maxWidth = 0
    for k,v in ipairs(self.buttons)  do
        local width = getTextManager():MeasureStringX(UIFont.Small, v.title) + 10
        maxWidth = math.max(maxWidth, width)
    end
    self:setWidth(math.max(self.width, 10 + maxWidth + 10))
    self:ignoreWidthChange()

    local x,y = 10,10;
    local w,h = self.width-20,20;
    local margin = 5;

    local y, obj = ISDebugUtils.addLabel(self,"Header",x+(w/2),y,"DEBUG MENU",UIFont.Medium);
    obj.center = true;

    y = y+5;

    self.mainButton = ISButton:new(x,y+margin,w/2-3,h,"Main", self,  EtherDebugMenu.onClick_Main);
    self.mainButton:initialise();
    self:addChild(self.mainButton);

    self.devButton = ISButton:new(x + w/2+6,y+margin,w/2-6,h,"Dev", self,  EtherDebugMenu.onClick_Dev);
    self.devButton:initialise();
    self:addChild(self.devButton);

    y = y + h + 5
    self.mainTab = { _y=y, _buttons = {} }
    self.devTab = { _y=y, _buttons = {} }
    self.cheatsTab = { _y=y, _buttons = {} }

    for k,v in ipairs(self.buttons) do
        if v.tab == "MAIN" then
            if v.marginTop and v.marginTop > 0 then self.mainTab._y = self.mainTab._y + v.marginTop end
            self.mainTab._y, obj = ISDebugUtils.addButton(self,v,x,self.mainTab._y+margin,w,h,v.title,EtherDebugMenu.onClick);
            table.insert(self.mainTab._buttons, obj)
        end
        if v.tab == "DEV" then
            if v.marginTop and v.marginTop > 0 then self.devTab._y = self.devTab._y + v.marginTop end
            self.devTab._y, obj = ISDebugUtils.addButton(self,v,x,self.devTab._y+margin,w,h,v.title,EtherDebugMenu.onClick);
            table.insert(self.devTab._buttons, obj)
        end    
    end

if EtherDebugMenu.tab == "MAIN" then self:onClick_Main() end
if EtherDebugMenu.tab == "DEV" then self:onClick_Dev() end
end

function EtherDebugMenu:onClick_Main() EtherDebugMenu.tab = "MAIN" self:visibility() self:setHeight(self.mainTab._y+10); end
function EtherDebugMenu:onClick_Dev() EtherDebugMenu.tab = "DEV" self:visibility() self:setHeight(self.devTab._y+10); end

function EtherDebugMenu:visibility()
    for _, b in ipairs(self.mainTab._buttons) do b:setVisible(EtherDebugMenu.tab == "MAIN") end
    for _, b in ipairs(self.devTab._buttons) do b:setVisible(EtherDebugMenu.tab == "DEV") end 
end

function EtherDebugMenu:onClick(_button)
    if _button.customData.func then
        _button.customData.func();
    else
        self:close();
    end
end

function EtherDebugMenu:close()
    self:setVisible(false);
    self:removeFromUIManager();
    EtherDebugMenu.instance = nil
end

function EtherDebugMenu:new(x, y, width, height)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    --EtherDebugMenu.instance = o
    EtherDebugMenu.RegisterClass(self);
    return o;
end

EtherDebugMenu.classes = {}

function EtherDebugMenu.RegisterClass(_class)
    table.insert(EtherDebugMenu.classes, _class);
end

function EtherDebugMenu.OnPlayerDeath(playerObj)
    for _,class in ipairs(EtherDebugMenu.classes) do
        if class.instance then
            class.instance:setVisible(false);
            class.instance:removeFromUIManager();
            class.instance = nil;
        end
    end
end

Events.OnPlayerDeath.Add(EtherDebugMenu.OnPlayerDeath)
