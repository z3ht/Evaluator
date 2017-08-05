package com.zinno.evaluator.commands.hubcommand;

import java.util.List;

import com.zinno.evaluator.commands.SubCommand;

public interface HubCommand {
	
	public void addSubCommand(List<String> alias, SubCommand subCommand);

	public String getConfigName();
	
}