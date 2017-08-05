package com.zinno.evaluator.util.gui;

public enum CommandType {
	ALERT,
	BAN,
	EVALUATE,
	FREEZE,
	INVSEE,
	KICK,
	MUTE,
	NOTE,
	SMUTE,
	SPY,
	STRIP,
	TEMPBAN,
	TRACK,
	UNBAN,
	WARN,
	XRAY;

	public boolean requiresName() {
		switch(this) {
		case ALERT:
			return false;
		default:
			return true;
		}
	}
	
	public boolean requiresReason() {
		switch(this) {
		case EVALUATE:
		case FREEZE:
		case INVSEE:
		case SPY:
		case TRACK:
		case STRIP:
		case XRAY:
			return false;
		default:
			return true;
		}
	}
}
