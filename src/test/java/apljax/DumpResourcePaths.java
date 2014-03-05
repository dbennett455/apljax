package apljax;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;

import com.github.apljax.process.Scanner;
import com.github.apljax.resource.JavaClass;
import com.github.apljax.resource.JavaMethod;
import com.github.apljax.resource.JavaResources;

public class DumpResourcePaths {

    //private final Logger log=LoggerFactory.getLogger(TestScanner.class);

	@Test
	public void test() {

		Scanner  scanner=new Scanner();
		JavaResources resources=scanner.scan();
		resources.setRootUrl("http://localhost/app");
		TreeMap<String,JavaClass> resourceClasses=resources.getResources();
		for (String className : resourceClasses.keySet()) {
			JavaClass rc=resourceClasses.get(className);
			// output class info
			System.out.print("class:"+className);
			System.out.print("\tresource:"+rc.getResourceId());
			System.out.print("\tpath:"+rc.getFullPath());
			System.out.println("");
			// iterate through methods
			TreeMap<String,JavaMethod> resourceMethods=rc.getResourceMethods();
			for (String methodName : resourceMethods.keySet()) {
				JavaMethod rm=resourceMethods.get(methodName);
				// output method info
				System.out.print("\tmethod:"+rm.getMethodInfo().getName());
				System.out.print("\t\tresource:"+rm.getResourceId());
				System.out.print("\t\tpath:"+rm.getFullPath());
				System.out.println("");
			}
		}
		assertNotNull(resources);
	}
}
