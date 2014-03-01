package apljax;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.apljax.Scanner;
import com.github.apljax.resource.ResourceClass;
import com.github.apljax.resource.ResourceField;
import com.github.apljax.resource.ResourceMethod;
import com.github.apljax.resource.ResourceParameter;
import com.github.apljax.resource.Resources;

public class DumpResourcePaths {

    private final Logger log=LoggerFactory.getLogger(TestScanner.class);

	@Test
	public void test() {

		Scanner  scanner=new Scanner();
		Resources resources=scanner.scan();
		resources.setRootUrl("http://localhost/app");
		TreeMap<String,ResourceClass> resourceClasses=resources.getResources();
		for (String className : resourceClasses.keySet()) {
			ResourceClass rc=resourceClasses.get(className);
			// output class info
			System.out.print("class:"+className);
			System.out.print("\tresource:"+rc.getResourceId());
			System.out.print("\tpath:"+rc.getFullPath());
			System.out.println("");
			// iterate through methods
			TreeMap<String,ResourceMethod> resourceMethods=rc.getResourceMethods();
			for (String methodName : resourceMethods.keySet()) {
				ResourceMethod rm=resourceMethods.get(methodName);
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
