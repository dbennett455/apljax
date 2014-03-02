package apljax;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.TreeMap;

import org.junit.Test;

import com.github.apljax.Scanner;
import com.github.apljax.resource.ResourceClass;
import com.github.apljax.resource.ResourceField;
import com.github.apljax.resource.ResourceMethod;
import com.github.apljax.resource.ResourceParameter;
import com.github.apljax.resource.Resources;

public class DumpResources {

    //private final Logger log=LoggerFactory.getLogger(TestScanner.class);

	@Test
	public void test() {

		Scanner  scanner=new Scanner();
		Resources resources=scanner.scan();
		TreeMap<String,ResourceClass> resourceClasses=resources.getResources();
		for (String className : resourceClasses.keySet()) {
			ResourceClass rc=resourceClasses.get(className);
			// output class info
			System.out.println(className);
			System.out.println("\tpath:"+rc.getResourcePath());
			System.out.println("\tconsumes:"+Arrays.toString(rc.getConsumes()));
			System.out.println("\tproduces:"+Arrays.toString(rc.getProduces()));
			// iterate through fields
			TreeMap<String,ResourceField> resourceFields=rc.getResourceFields();
			for (String fieldName : resourceFields.keySet()) {
				ResourceField rf=resourceFields.get(fieldName);
				// output field info
				System.out.println("\t\tfield:"+rf.getFieldInfo().getName());
				System.out.println("\t\t\tname:" + rf.getName());
				System.out.println("\t\t\tdata type:"+rf.getJavaType());
				System.out.println("\t\t\treq type:"+rf.getRequestType());
				System.out.println("\t\t\tdefault value:"+rf.getDefaultValue());
			}
			// iterate through methods
			TreeMap<String,ResourceMethod> resourceMethods=rc.getResourceMethods();
			for (String methodName : resourceMethods.keySet()) {
				ResourceMethod rm=resourceMethods.get(methodName);
				// output method info
				System.out.println("\t\tmethod:"+rm.getMethodInfo().getName());
				System.out.println("\t\t\tpath:" + rm.getResourcePath());
				System.out.println("\t\t\treq type:"+rm.getRequestMethod());
				System.out.println("\t\t\tconsumes:"+Arrays.toString(rm.getConsumes()));
				System.out.println("\t\t\tproduces:"+Arrays.toString(rm.getProduces()));
				// iterate through parameters
				TreeMap<Integer,ResourceParameter> resourceParameters=rm.getResourceParameters();
				for (Integer paramIndex : resourceParameters.keySet()) {
					ResourceParameter rp=resourceParameters.get(paramIndex);
					// output parameter info
					System.out.println("\t\t\tparameter:"+rp.getName());
					System.out.println("\t\t\t\tindex:"+paramIndex);
					System.out.println("\t\t\t\tdata type:"+rp.getMethodParameter().getType());
					System.out.println("\t\t\t\treq type:"+rp.getType());
					System.out.println("\t\t\t\tdefault value:"+rp.getDefaultValue());
				}
			}
		}
		assertNotNull(resources);
	}

}
