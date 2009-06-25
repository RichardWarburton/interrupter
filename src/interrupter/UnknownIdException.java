package interrupter;

public class UnknownIdException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1176446412586682824L;

	public UnknownIdException(int id) {
		super("Unknown id "+id+" for interruptor session");
	}

}
