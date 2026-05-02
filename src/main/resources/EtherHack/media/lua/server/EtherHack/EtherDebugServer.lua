local MODULE = "EtherDebug"

local EtherDebugServer = {}

local function sendNotice(player, ok, action, message)
    if player then
        sendServerCommand(player, MODULE, "notice", {
            ok = ok == true,
            action = tostring(action or ""),
            message = tostring(message or "")
        })
    end
end

local function capabilityName(capability)
    if capability and capability.name then
        return capability:name()
    end
    return tostring(capability)
end

local function hasCapability(player, capability)
    if not player or not capability then return false end
    local role = player:getRole()
    return role ~= nil and role:hasCapability(capability)
end

local function deny(player, action, capability)
    local message = "Denied " .. tostring(action) .. ": missing " .. capabilityName(capability)
    print("[EtherDebug] " .. message .. " for " .. tostring(player and player:getUsername()))
    sendNotice(player, false, action, message)
end

local function clampNumber(value, minValue, maxValue)
    value = tonumber(value)
    if not value then return nil end
    if value < minValue then return minValue end
    if value > maxValue then return maxValue end
    return value
end

function EtherDebugServer.giveItem(player, args)
    local capability = Capability.AddItem
    if not hasCapability(player, capability) then return deny(player, "giveItem", capability) end

    args = args or {}
    local itemType = tostring(args.item or "")
    local count = clampNumber(args.count or 1, 1, 100)
    if itemType == "" or not count then
        return sendNotice(player, false, "giveItem", "Invalid item request.")
    end

    if not getScriptManager():FindItem(itemType) then
        return sendNotice(player, false, "giveItem", "Unknown item: " .. itemType)
    end

    for _ = 1, count do
        local item = player:getInventory():AddItem(itemType)
        if item then
            sendAddItemToContainer(player:getInventory(), item)
        end
    end

    sendNotice(player, true, "giveItem", "Added " .. tostring(count) .. " x " .. itemType)
end

local toggleCommands = {
    god = {
        capability = Capability.ToggleGodModHimself,
        apply = function(player, enabled) player:setGodMod(enabled) end
    },
    invisible = {
        capability = Capability.ToggleInvisibleHimself,
        apply = function(player, enabled) player:setInvisible(enabled) end
    },
    noclip = {
        capability = Capability.ToggleNoclipHimself,
        apply = function(player, enabled) player:setNoClip(enabled) end
    },
    unlimitedCarry = {
        capability = Capability.ToggleUnlimitedCarry,
        apply = function(player, enabled) player:setUnlimitedCarry(enabled) end
    },
    unlimitedEndurance = {
        capability = Capability.ToggleUnlimitedEndurance,
        apply = function(player, enabled) player:setUnlimitedEndurance(enabled) end
    },
    unlimitedAmmo = {
        capability = Capability.ToggleUnlimitedAmmo,
        apply = function(player, enabled) player:setUnlimitedAmmo(enabled) end
    }
}

function EtherDebugServer.toggleSelf(player, args)
    args = args or {}
    local name = tostring(args.name or "")
    local command = toggleCommands[name]
    if not command then
        return sendNotice(player, false, "toggleSelf", "Unknown toggle: " .. name)
    end

    if not hasCapability(player, command.capability) then
        return deny(player, name, command.capability)
    end

    local enabled = args.enabled == true
    command.apply(player, enabled)
    sendServerCommand(player, MODULE, "applyLocalToggle", { name = name, enabled = enabled })
end

function EtherDebugServer.teleportSelf(player, args)
    local capability = Capability.TeleportToCoordinates
    if not hasCapability(player, capability) then return deny(player, "teleportSelf", capability) end

    args = args or {}
    local x = tonumber(args.x)
    local y = tonumber(args.y)
    local z = tonumber(args.z) or 0
    if not x or not y then
        return sendNotice(player, false, "teleportSelf", "Invalid teleport coordinates.")
    end

    local ok = false
    if GameServer and GameServer.sendTeleport then
        ok = pcall(function() GameServer.sendTeleport(player, x, y, z) end)
    end
    if not ok and player.teleportTo then
        ok = pcall(function() player:teleportTo(x, y, z) end)
    end

    sendServerCommand(player, MODULE, "applyTeleport", { x = x, y = y, z = z })
end

function EtherDebugServer.status(player, args)
    local role = player and player:getRole()
    local ok = role ~= nil
    sendServerCommand(player, MODULE, "status", {
        ok = ok,
        message = ok and "EtherDebug server channel is available." or "No role is assigned to this player."
    })
end

local function onClientCommand(module, command, player, args)
    if module ~= MODULE then return end

    local handler = EtherDebugServer[command]
    if handler then
        handler(player, args or {})
    else
        sendNotice(player, false, command, "Unknown command: " .. tostring(command))
    end
end

Events.OnClientCommand.Add(onClientCommand)
