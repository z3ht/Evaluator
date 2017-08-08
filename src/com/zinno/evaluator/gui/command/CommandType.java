package com.zinno.evaluator.gui.command;

public enum CommandType {
	UUID,
	REPORT,
	ALERT,
	BAN,
	CHAT,
	CLEARCHAT,
	EVALUATE,
	FREEZE,
	INVSEE,
	KICK,
	MUTE,
	NOTE,
	RESTRICTCHAT,
	SMUTE,
	SPY,
	STRIP,
	TEMPBAN,
	TRACK,
	UNBAN,
	WARN,
	XRAY;
	
	public boolean requiresReason() {
		switch(this) {
		case EVALUATE:
		case FREEZE:
		case INVSEE:
		case SPY:
		case TRACK:
		case STRIP:
		case XRAY:
		case UUID:
			return false;
		default:
			return true;
		}
	}
	
	public boolean requiresPriority() {
		switch(this) {
		case RESTRICTCHAT:
		case ALERT:
			return true;
		default:
			return false;
		}
	}
	
	public boolean requiresName() {
		switch(this) {
		case ALERT:
		case CHAT:
		case CLEARCHAT:
		case RESTRICTCHAT:
		case UUID:
			return false;
		default:
			return true;
		}
	}
	
	public String toConfig() {
		switch(this) {
		case UUID:
			return "UUID";
		case CLEARCHAT:
			return "ClearChat";
		case INVSEE:
			return "InvSee";
		case RESTRICTCHAT:
			return "RestrictChat";
		case SMUTE:
			return "SMute";
		case TEMPBAN:
			return "TempBan";
		default:
			String firstLetter = this.toString().substring(0, 1).toUpperCase();
			String theRest = this.toString().substring(1, this.toString().length()).toLowerCase();
			return firstLetter + theRest;
		}
	}
}
