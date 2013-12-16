package com.simon.huber.hackenbush.exceptions;

/**
 * Wird geworfen, wenn auf zwei Werte die Einfachheitsregel nicht angewendet
 * werden kann.
 * 
 * @author Simon Huber
 * 
 */
public class EinfachheitsRegelException extends Exception {

    private static final long serialVersionUID = 1L;

    public EinfachheitsRegelException(String msg) {
	super(msg);
    }

}
