package org.uengine.smcp.twister.engine.priv.core.definition;

/**
 * An alarm event is declared on a Pick activity to define a deadline on
 */

public interface AlarmEvent {

    public static final int DURATION_EXPR = 1;
    public static final int DEADLINE_EXPR = 2;

	/**
	 * 
	 * @uml.property name="timeExpression"
	 */
	public String getTimeExpression();

    public int getType();

    public void setTimeExpression(String timeExpression, int type);
}