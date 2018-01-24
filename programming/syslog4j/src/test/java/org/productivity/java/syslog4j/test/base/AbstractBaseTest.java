package org.productivity.java.syslog4j.test.base;

import junit.framework.TestCase;

public abstract class AbstractBaseTest extends TestCase {
	protected void sleep(int ms) {
		try {
			Thread.sleep(ms);
			
		} catch (InterruptedException ie) {
			//
		}
	}
}
