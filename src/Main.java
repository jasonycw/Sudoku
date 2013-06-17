

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class Main {

	
	/**
	 * @param args
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {

		Class testClass = Tests.class;
		int total = 0;
		int passed = 0;
		for(Method m:testClass.getDeclaredMethods())
		{
			if(!m.getName().contains("test")) continue;
			boolean isSuccess;
			try {
				isSuccess =(Boolean) m.invoke(new Tests());
				
			} catch (Exception e) {
				
				e.printStackTrace();
				isSuccess = false;
			}
			if(isSuccess)
				System.out.println(m.getName()+" succeeded");
			else
				System.out.println(m.getName()+" failed");
			total ++;
			if(isSuccess)
				passed++;
		}
		
		System.out.println("Passed "+passed+"/"+total+" tests");
	}

}
