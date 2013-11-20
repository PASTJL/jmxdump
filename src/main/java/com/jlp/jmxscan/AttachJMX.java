/*Copyright 2013 Jean-Louis PASTUREL 
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.jlp.jmxscan;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.tools.attach.VirtualMachine;

//import ibm.tools.attach.*;

public class AttachJMX {

	static public String usage() {
		return "Usage (if it is possible execute with root user, at least with the same user of the JVM you want to scan) :\n"
				+

				" java -DPROVIDER=\"HotSpot|IBM\" -classpath <JDK_HOME>/lib/tools.jar:./jmxscan.jar com.jlp.jmxscan.AttachJMX   -h|-help  to obtain this help\n"
				+ "java -DPROVIDER=\"HotSpot|IBM\" -classpath <JDK_HOME>/lib/tools.jar:./jmxscan.jar com.jlp.jmxscan.AttachJMX -pid=<PID JVM mandatory> -attr|-obj(opt) -model=<model(opt,default *:*)> -excl=<excl(opt)>\n"
				+ "-attr is the default => gives the values of attributes,\n"
				+ "-obj  => gives the types of attributes,\n"
				+ "-model must be an ObjectName regex , excl => for excluding Mbean is a normal regex see java.util.Pattern\n\n"
				+ "\n\n when adding -freq=<timeInSecond> and -boucles=<number> :\n"
				+ "The script is excecuted <boucles> times spaced by <freq> seconds.";
	}

	public static void main(String args[]) throws Exception {

		if (args.length == 1
				&& (args[0].equals("-h") || args[0].equals("-help"))) {
			System.out.println(usage());
			System.exit(1);
		}

		List<String> lstArgs = Arrays.asList(args);
		Map<String, String> mapArgs = new HashMap<String, String>();
		for (String str : lstArgs) {
			if (str.contains("=")
					&& (str.contains("-pid") || str.contains("-model")
							|| str.contains("-excl")
							|| str.contains("-boucles") || str
								.contains("-frequency"))) {
				mapArgs.put(str.split("=")[0],
						str.substring(str.indexOf("=") + 1));
			}
		}

		String pid = "0";
		if (mapArgs.containsKey("-pid")) {
			pid = mapArgs.get("-pid");
		} else {
			System.out.println(usage());
			System.exit(1);
			;
		}
		String model = "*:*";
		if (mapArgs.containsKey("-model")) {
			model = mapArgs.get("-model");
		}
		String excl = "";
		if (mapArgs.containsKey("-excl")) {
			excl = mapArgs.get("-excl");
		}
		String boucles = "1";
		if (mapArgs.containsKey("-boucles")) {
			boucles = mapArgs.get("-boucles");
		}
		String frequency = "1";
		if (mapArgs.containsKey("-frequency")) {
			frequency = mapArgs.get("-frequency");
		}
		String provider = System.getProperty("PROVIDER", "HotSpot");
		VirtualMachine vm = null;
		String connectorAddr = null;
		if (provider.equalsIgnoreCase("HotSpot")) {
			vm = VirtualMachine.attach(pid);
			connectorAddr = vm.getAgentProperties().getProperty(
					"com.sun.management.jmxremote.localConnectorAddress");
			if (connectorAddr == null) {
				String agent = vm.getSystemProperties()
						.getProperty("java.home")
						+ File.separator
						+ "lib"
						+ File.separator + "management-agent.jar";
				vm.loadAgent(agent);
				connectorAddr = vm.getAgentProperties().getProperty(
						"com.sun.management.jmxremote.localConnectorAddress");
			}
		} else if (provider.equalsIgnoreCase("IBM")) {
			System.out.println("Trying with IBM with Java Reflexion");

			Method method = Class.forName("ibm.tools.attach.J9VirtualMachine")
					.getSuperclass().getDeclaredMethod("attach", String.class);
			method.setAccessible(true);
			vm = (VirtualMachine) method.invoke(null, pid);

			// vm =
			// Class.forName("ibm.tools.attach.J9VirtualMachine").attach(pid);

			String agentIBM = "instrument,"
					+ vm.getSystemProperties().getProperty("java.home")
					+ File.separator + "lib" + File.separator
					+ "management-agent.jar=";
			vm.loadAgentLibrary(agentIBM);

			connectorAddr = vm.getSystemProperties().getProperty(
					"com.sun.management.jmxremote.localConnectorAddress");

			System.out.println(" IBM connectorAddr =" + connectorAddr);
		} else {
			System.out.println("Unknown JVM provider");
			System.exit(1);
		}

		JMXServiceURL serviceURL = new JMXServiceURL(connectorAddr);
		JMXConnector connector = JMXConnectorFactory.connect(serviceURL);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd:HH:mm:ss.SSS",
				Locale.ENGLISH);

		// Gestion des boucles
		int bcl = Integer.parseInt(boucles);
		long waitTime = Long.parseLong(frequency) * 1000;
		for (int bc = 0; bc < bcl; bc++) {

			JMXScan jmxscan = new JMXScan(model, excl, connector);

			System.out.println("Date = " + sdf.format(new Date()));
			System.out
					.println("########################################################");
			System.out.println("PID JVM  =" + pid);
			System.out
					.println("########################################################");
			if (lstArgs.contains("-obj")) {
				jmxscan.printObjectNames();
			} else {
				jmxscan.printAttributes();
			}
			System.out
					.println("########################################################");
			System.out.println("End JMXDump = " + sdf.format(new Date()));
			System.out.println();
			if (bcl > 1) {
				Thread.sleep(waitTime);
			}
		}
		vm.detach();

	}
}
