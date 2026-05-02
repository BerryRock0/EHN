EtherDebugClient = EtherDebugClient or {}

local MODULE = "EtherDebug"

local function getLocalPlayer()
    return getPlayer()
end

local function notify(text, isBad)
    local player = getLocalPlayer()
    if player and HaloTextHelper then
        if isBad and HaloTextHelper.addBadText then
            HaloTextHelper.addBadText(player, text)
        elseif HaloTextHelper.addGoodText then
            HaloTextHelper.addGoodText(player, text)
        end
    end
    print("[EtherDebug] " .. tostring(text))
end

function EtherDebugClient.send(command, args)
    local player = getLocalPlayer()
    if not player then return false end
    sendClientCommand(player, MODULE, command, args or {})
    return true
end

local function applyLocalToggle(name, enabled)
    local player = getLocalPlayer()
    enabled = enabled == true

    if name == "god" then
        if toggleGodMode then toggleGodMode(enabled) end
        if player then player:setGodMod(enabled) end
    elseif name == "invisible" then
        if toggleInvisible then toggleInvisible(enabled) end
        if player then player:setInvisible(enabled) end
    elseif name == "noclip" then
        if toggleNoclip then toggleNoclip(enabled) end
        if player then player:setNoClip(enabled) end
    elseif name == "unlimitedCarry" then
        if toggleEnableUnlimitedCarry then toggleEnableUnlimitedCarry(enabled) end
        if player and player.setUnlimitedCarry then player:setUnlimitedCarry(enabled) end
    elseif name == "unlimitedEndurance" then
        if toggleUnlimitedEndurance then toggleUnlimitedEndurance(enabled) end
        if player and player.setUnlimitedEndurance then player:setUnlimitedEndurance(enabled) end
    elseif name == "unlimitedAmmo" then
        if toggleUnlimitedAmmo then toggleUnlimitedAmmo(enabled) end
        if player and player.setUnlimitedAmmo then player:setUnlimitedAmmo(enabled) end
    end
end

function EtherDebugClient.toggleSelf(name, enabled)
    if isClient() then
        return EtherDebugClient.send("toggleSelf", { name = name, enabled = enabled == true })
    end

    applyLocalToggle(name, enabled)
    return true
end

function EtherDebugClient.giveItem(itemType, count)
    if not itemType or itemType == "" then return false end

    if isClient() then
        return EtherDebugClient.send("giveItem", { item = itemType, count = count or 1 })
    end

    if giveItem then
        giveItem(itemType, count or 1)
        return true
    end
    return false
end

function EtherDebugClient.teleportSelf(x, y, z)
    x = tonumber(x)
    y = tonumber(y)
    z = tonumber(z) or 0
    if not x or not y then return false end

    if isClient() then
        return EtherDebugClient.send("teleportSelf", { x = x, y = y, z = z })
    end

    if safePlayerTeleport then
        safePlayerTeleport(math.floor(x), math.floor(y))
    else
        local player = getLocalPlayer()
        if player then player:teleportTo(x, y, z) end
    end
    return true
end

function EtherDebugClient.requestStatus()
    if isClient() then
        return EtherDebugClient.send("status", {})
    end
    notify("EtherDebug is running locally.", false)
    return true
end

function EtherDebugClient.applyServerCommand(command, args)
    args = args or {}

    if command == "notice" then
        notify(tostring(args.message or "EtherDebug command processed."), args.ok == false)
    elseif command == "applyLocalToggle" then
        applyLocalToggle(tostring(args.name or ""), args.enabled == true)
    elseif command == "applyTeleport" then
        local player = getLocalPlayer()
        local x = tonumber(args.x)
        local y = tonumber(args.y)
        local z = tonumber(args.z) or 0
        if player and x and y then
            player:teleportTo(x, y, z)
        end
    elseif command == "status" then
        notify(tostring(args.message or "EtherDebug server channel is available."), args.ok == false)
    end
end

local function onServerCommand(module, command, args)
    if module ~= MODULE then return end
    EtherDebugClient.applyServerCommand(command, args)
end

Events.OnServerCommand.Add(onServerCommand)
