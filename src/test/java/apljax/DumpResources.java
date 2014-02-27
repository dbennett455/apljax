package apljax;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.apljax.Resources;
import com.github.apljax.Scanner;
import com.github.apljax.os.ResourceClass;
import com.github.apljax.os.ResourceMethod;
import com.github.apljax.os.ResourceParameter;

public class DumpResources {

    private final Logger log=LoggerFactory.getLogger(TestScanner.class);

	@Test
	public void test() {
		log.info("test info message");

		Scanner  scanner=new Scanner();
		Resources resources=scanner.scan();
		TreeMap<String,ResourceClass> resourceClasses=resources.getResources();
		for (String className : resourceClasses.keySet()) {
			ResourceClass rc=resourceClasses.get(className);
			// output class info
			System.out.print(className);
			System.out.println(" - path:"+rc.getPath());
			// iterate through methods
			TreeMap<String,ResourceMethod> resourceMethods= rc.getResourceMethods();
			for (String methodName : resourceMethods.keySet()) {
				ResourceMethod rm=resourceMethods.get(methodName);
				// output method info
				System.out.print("\t"+rm.getMethodInfo().getName());
				System.out.print(" - path: " + rm.getPath());
				System.out.println(" - reqtype: "+rm.getRequestMethod());
				// iterate through parameters
				TreeMap<Integer,ResourceParameter> resourceParameters=rm.getResourceParameters();
				for (Integer paramIndex : resourceParameters.keySet()) {
					ResourceParameter rp=resourceParameters.get(paramIndex);
					// output parameter info
					System.out.print("\t\t"+rp.getName());
					System.out.print(" - index:"+paramIndex);
					System.out.println(" - param type:"+rp.getType());
				}
			}
		}
		assertNotNull(resources);
	}

}
