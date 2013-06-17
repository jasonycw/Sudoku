
/**
 * the input is invalid (length < 9 or there is duplicate in the input)
 *
 */
public class InvalidAlphabetException extends Exception {

	public InvalidAlphabetException() {
		super();
		// 
	}

	public InvalidAlphabetException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// 
	}

	public InvalidAlphabetException(String arg0) {
		super(arg0);
		// 
	}

	public InvalidAlphabetException(Throwable arg0) {
		super(arg0);
		// 
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 154545L;

}
