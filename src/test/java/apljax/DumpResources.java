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

public class DumpResources {

    private final Logger log=LoggerFactory.getLogger(TestScanner.class);

	@Test
	public void test() {

		Scanner  scanner=new Scanner();
		Resources resources=scanner.scan();
		TreeMap<String,ResourceClass> resourceClasses=resources.getResources();
		for (String className : resourceClasses.keySet()) {
			ResourceClass rc=resourceClasses.get(className);
			// output class info
			System.out.print(className);
			System.out.print(",path:"+rc.getPath());
			System.out.println("");
			// iterate through fields
			TreeMap<String,ResourceField> resourceFields=rc.getResourceFields();
			for (String fieldName : resourceFields.keySet()) {
				ResourceField rf=resourceFields.get(fieldName);
				// output field info
				System.out.print("\tfield:"+rf.getFieldInfo().getName());
				System.out.print(",name:" + rf.getName());
				System.out.print(",data type:"+rf.getJavaType());
				System.out.print(",req type:"+rf.getRequestType());
				System.out.print(",default value:"+rf.getDefaultValue());
				System.out.println("");
			}
			// iterate through methods
			TreeMap<String,ResourceMethod> resourceMethods=rc.getResourceMethods();
			for (String methodName : resourceMethods.keySet()) {
				ResourceMethod rm=resourceMethods.get(methodName);
				// output method info
				System.out.print("\tmethod:"+rm.getMethodInfo().getName());
				System.out.print(",path:" + rm.getPath());
				System.out.print(",req type:"+rm.getRequestMethod());
				System.out.println("");
				// iterate through parameters
				TreeMap<Integer,ResourceParameter> resourceParameters=rm.getResourceParameters();
				for (Integer paramIndex : resourceParameters.keySet()) {
					ResourceParameter rp=resourceParameters.get(paramIndex);
					// output parameter info
					System.out.print("\t\t"+rp.getName());
					System.out.print(",index:"+paramIndex);
					System.out.print(",data type:"+rp.getMethodParameter().getType());
					System.out.print(",req type:"+rp.getType());
					System.out.print(",default value:"+rp.getDefaultValue());
					System.out.println("");
				}
			}
		}
		assertNotNull(resources);
	}

}
