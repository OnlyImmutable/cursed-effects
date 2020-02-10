package com.abstractwolf.cursedeffects.utils.command;

import com.abstractwolf.cursedeffects.commands.CursedEffectCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager
{

    private List<AbstractCommand> commands;

    public CommandManager()
    {

        commands = new ArrayList<>();

    }

    public void addCommand(AbstractCommand command)
    {

        this.commands.add(command);

    }

    public void clearCommands()
    {

        this.commands.clear();

    }

    public void registerCommands()
    {

        addCommand(new CursedEffectCommand());

    }

    public List<AbstractCommand> getCommands()
    {

        return this.commands;

    }

}
