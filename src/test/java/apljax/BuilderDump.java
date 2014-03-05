package apljax;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.TreeMap;

import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;





import com.github.apljax.os.Bowl;
import com.github.apljax.process.DefaultBuilder;
import com.github.apljax.util.JavaDataToJavascriptData;
import com.propertytrak.rest.params.IntegerParam;
import com.propertytrak.rest.params.LongParam;

public class BuilderDump {

	static {
		// custom Java classes to Javascript number
		JavaDataToJavascriptData.addNumberType(IntegerParam.class.getName());
		JavaDataToJavascriptData.addNumberType(LongParam.class.getName());
	}

    //private final Logger log=LoggerFactory.getLogger(BuilderDump.class);
	@Test
	public void test() {
		DefaultBuilder builder=new DefaultBuilder();
		Bowl bowl=builder.pour();
		assertNotNull(bowl);
		this.dumpTree(bowl, 0);
	}

	@SuppressWarnings("rawtypes")
	private void dumpTree(TreeMap treeMap, int down) {
		for (Object n : treeMap.keySet()) {
			Object v=treeMap.get(n);

			if (down > 0)
				System.out.print(new String(new char[down]).replace("\0", "\t"));
			System.out.print(n.toString()+":");

			if (TreeMap.class.isAssignableFrom(v.getClass())) {
				System.out.println();
				dumpTree((TreeMap)v, down+1);
			} else if (v instanceof String[]) {
				System.out.println(Arrays.toString((String[])v));
			} else {
				System.out.println(v.toString());
			}
		}
	}

}
