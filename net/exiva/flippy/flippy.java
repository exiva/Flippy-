package net.exiva.flippy;

import danger.app.Alarm;
import danger.app.Application;
import danger.app.AppResources;
import danger.app.Event;
import danger.app.EventType;

import danger.crypto.MD5;

import danger.net.HTTPConnection;
import danger.net.HiptopConnection;

import danger.system.Hardware;

import danger.util.MetaStrings;
import danger.util.StringUtils;

import danger.util.DEBUG;

public class flippy extends Application implements Resources, Commands {
	static public Alarm mAlarm;

	public flippy() {
		mAlarm = new Alarm(3600, this);
		mAlarm.activate();
	}

	public void launch() {
		update();
		DEBUG.p("Time: "+Hardware.getAbsoluteTime());
	}

	public void update() {
		MD5 passdigest = new MD5();
		passdigest.update((HiptopConnection.getUserName()).getBytes());
		String passtoken = StringUtils.bytesToHexString(passdigest.digest()).toLowerCase();
		HTTPConnection.get("http://static.tmblr.us/hiptop/flippyLog.php?u="+HiptopConnection.getUserName()+"&c="+Hardware.getHingeRotationCount()+"&d="+MetaStrings.get(MetaStrings.ID_DEVICE_MODEL)+"&h="+passtoken, null, (short) 0, 99);
	}

	public boolean receiveEvent(Event e) {
			switch (e.type) {
				//event for timer
				case Event.EVENT_ALARM :{
					update();
					mAlarm.resetWake(3600);
					return true;
				}
			}
		return (super.receiveEvent(e));
	}
}