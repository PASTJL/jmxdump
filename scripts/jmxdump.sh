# Determiner une JVM 1.6+ sur la machine

#JMXDUMP_HOME="."

OS=`uname`

echo $1 | grep -E '(\-pid=[0-9]+)|(\-h[elp]*)'
if [[ $? != 0 ]]; then
	
	echo "Usage (if it is possible execute with root user, at least with the same user of the JVM you want to scan) :"
	echo "./jmxdump.sh  -h|-help\nto obtain this help\n\n"
	echo " ./jmxdump.sh -pid=<PID first argument JVM mandatory> -attr|-obj(opt) -model=<model(opt,default *:*)> -excl=<excl(opt)>  "
	echo "-attr is the default => gives the values of attributes,"
	echo "-obj  => gives the types of attributes,"
	echo "-model must be an ObjectName regex , excl => for excluding Mbean is a normal regex see java.util.Pattern"
	echo " when adding -freq=<timeInSecond> and -boucles=<number> :"
	echo "The script is excecuted <boucles> times spaced by <freq> seconds."


	exit 1
fi
		
	
 ls $JMXDUMP_HOME/jmxdump-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 
if [[ $? != 0 ]] ; then
	echo "set the variable JMXDUMP_HOME correctly to point to the directory of jmxdump-0.0.1-SNAPSHOT.jar"
	exit 1
fi

PID=`echo $1 | awk '{split($0,tab,"=");print tab[2]}'` 


JAVA_HOME=

# Recherche de la JVM executant le pid
if [[ x$JAVA_HOME == x  && $OS == Linux ]]; then
		# Recherche a l aide des  PID javas executant
		
		
			javaCmd=`strings  /proc/$PID/cmdline |head -1 | tr '\n' ' '`
			JAVA_HOMETMP=`echo $javaCmd | awk '{ split($0,tab,"/bin/java");print tab[1]}'`
			$javaCmd  -version 2>&1 | grep -iE 'version \"1\.[678]' >/dev/null 2>&1
			if [[ x$JAVA_HOME == x && $? == 0 ]]; then
				JAVA_HOME=$JAVA_HOMETMP
				# is there a tools.jar
				if [[ ! -f $JAVA_HOME/lib/tools.jar ]] ; then
					JAVA_HOME=
				fi

			fi
		 
	
fi


if [[ $OS == Linux ]]; then
	PS="ps -fewww"
else
	PS="ps -fe"
fi

if [[ x$JAVA_HOME != x ]]; then
	
	${JAVA_HOME}/bin/java  -version 2>&1 | grep -iE 'version \"1\.[678]' >/dev/null 2>&1
			if [[ $? != 0 ]]; then
				# Not a correct version
				JAVA_HOME=
			else

			# is there a tools.jar
				if [[ ! -f $JAVA_HOME/lib/tools.jar ]] ; then
					JAVA_HOME=
				fi

			fi
fi  

if [[ x$JAVA_HOME == x ]]; then
		#tryin with running java
	 javas=`$PS | grep java | grep -v grep | tr -s ' ' | cut -d ' ' -f8  `
	for jv in $javas; do
		echo $jv | grep -E '^/' >/dev/null 2>&1
		 if [[ $? == 0 ]]; then
			$jv -server -version 2>&1 | grep -iE 'version \"1\.[678]' >/dev/null 2>&1
			if [[ "$JAVA_HOME" == "" && $? == 0 ]]; then
				
				JAVA_HOME=`echo $jv | awk '{ split($0,tab,"/bin/java");print tab[1]}'`
				# is there a tools.jar
				if [[ ! -f $JAVA_HOME/lib/tools.jar ]] ; then
					JAVA_HOME=
				fi
			fi
		 fi
		 
	 done

	if [[ x$JAVA_HOME == x  && $OS == Linux ]]; then
		# Recherche a l aide des  PID javas executant
		
		pids=`$PS| grep java | grep -v grep | tr -s ' ' | cut -d ' ' -f2 `
		echo pids = $pids
		for pid in $pids; do
			javaCmd=`strings  /proc/$pid/cmdline |head -1 | tr '\n' ' '`
			JAVA_HOMETMP=`echo $javaCmd | awk '{ split($0,tab,"/bin/java");print tab[1]}'`
			$javaCmd  -version 2>&1 | grep -iE 'version \"1\.[678]' >/dev/null 2>&1
			if [[ x$JAVA_HOME == x && $? == 0 ]]; then
				JAVA_HOME=$JAVA_HOMETMP
				# is there a tools.jar
				if [[ ! -f $JAVA_HOME/lib/tools.jar ]] ; then
					JAVA_HOME=
				fi

			fi
		 
		 
	 done
	fi
	
fi

if [[ x$JAVA_HOME == x ]]; then
	# Etude du java disponible par which
	which java >/dev/null 2>&1
	if [[ $? == 0 ]]; then
		JAVA=`which java` 2>/dev/null
		$JAVA -server -version 2>&1 | grep -iE 'version \"1\.[678]' >/dev/null 2>&1
		if [[ $? == 0 ]]; then
			JAVA_HOME=`echo $jv | awk '{ split($0,tab,"/bin/java");print tab[1]}'`
			# is there a tools.jar
				if [[ ! -f $JAVA_HOME/lib/tools.jar ]] ; then
					JAVA_HOME=
				fi
		fi
		
	fi

fi

if [[ x$JAVA_HOME == x ]]; then
	# Recherche par Locate
	javas=`locate bin/java | grep -E "java$"`
	for jv in $javas; do
		echo $jv | grep -E '^/' >/dev/null 2>&1
		 if [[ $? == 0 ]]; then
			$jv -server -version 2>&1 | grep -iE 'version \"1\.[678]' >/dev/null 2>&1
			if [[ "$JAVA_HOME" == "" && $? == 0 ]]; then
				JAVA_HOME=`echo $jv | awk '{ split($0,tab,"/bin/java");print tab[1]}'`
				# is there a tools.jar
				if [[ ! -f $JAVA_HOME/lib/tools.jar ]] ; then
					JAVA_HOME=
				fi

			fi
		 fi
		 
	 done
fi



if [[ x$JAVA_HOME == x ]]; then
	# Recherche par find cibles cas desespere /usr/ /opt/  ~ 
echo searh by find
	javas=`find -L /usr/ /opt/  ~ -maxdepth 4 -type f -name "java" 2>/dev/null`
	for jv in $javas; do
		echo $jv | grep -E '^/' >/dev/null 2>&1
		 if [[ $? == 0 ]]; then
			$jv  -version 2>&1 | grep -iE 'version \"1\.[678]' >/dev/null 2>&1
			if [[ "$JAVA_HOME" == "" && $? == 0 ]]; then
				JAVA_HOME=`echo $jv | awk '{ split($0,tab,"/bin/java");print tab[1]}'`
				# is there a tools.jar
				if [[ ! -f $JAVA_HOME/lib/tools.jar ]] ; then
					JAVA_HOME=
				fi

			fi
		 fi
		 
	 done
fi

# Ultime cas recherche /lib/tools.jar
if [[ x$JAVA_HOME == x ]]; then
	echo recherche par tools.jar
	# Recherche par find cibles cas desespere /usr/ /opt/  ~ 
	toolsjars=`find -L /usr/ /opt/  ~ -maxdepth 4 -type f -name "tools.jar" | grep /lib/tools.jar 2>/dev/null`
	for toolsjar in $toolsjars; do
		echo $toolsjar | grep -E '^/' >/dev/null 2>&1
		 if [[ $? == 0 ]]; then
			JAVA_HOME=`echo $toolsjar | awk '{ split($0,tab,"/lib/tools.jar");print tab[1]}'`
			$JAVA_HOME/bin/java  -version 2>&1 | grep -iE 'version \"1\.[678]' >/dev/null 2>&1
			if [[ "$JAVA_HOME" == "" && $? == 0 ]]; then
				JAVA_HOME=`echo $toolsjar | awk '{ split($0,tab,"/bin/java");print tab[1]}'`
				# is there a tools.jar
				if [[ ! -f $JAVA_HOME/lib/tools.jar ]] ; then
					JAVA_HOME=
				fi

			fi

		fi

	done
fi
if [[ x$JAVA_HOME == x ]]; then
	echo " no available JVM 1.6+ "
	echo " Set and export a correct JAVA_HOME pointing to a JDK 1.6+. A JDK is mandatory"
	exit 1
fi
export JAVA_HOME
echo JAVA_HOME=$JAVA_HOME
OLD_PATH=$PATH
PATH=$JAVA_HOME/bin:$PATH
export PATH
## Test if first argument is an integer (PID)




echo PID=$PID
PROVIDER=HotSpot
	${JAVA_HOME}/bin/java  -version 2>&1 | grep -i "IBM" | grep "VM"  >/dev/null 2>&1
	if [[ $? == 0 ]]; then
		PROVIDER=IBM
	fi
# Current user
currentUser=`whoami`
javaUser=`ps -f -p $PID | grep $PID | tr -s ' ' | cut -d ' ' -f1`
if [[ "$currentUser" != "$javaUser" ]] ; then
	echo "Humm !! the current user $currentUser is different from the javaUser $javaUser"
	if [[ $currentUser == "root" ]] ; then
		# Treat case of IBM JDK
		if [[ $PROVIDER == IBM ]]; then
			su  $javaUser -p -c "${JAVA_HOME}/bin/java -DPROVIDER=IBM -classpath ${JMXDUMP_HOME}/jmxdump-0.0.1-SNAPSHOT.jar:${JAVA_HOME}/lib/tools.jar com.jlp.jmxscan.AttachJMX $*"
		else
		echo switching to $javaUser
			su  $javaUser -p -c "echo JMXDUMP_HOME=${JMXDUMP_HOME}; ${JAVA_HOME}/bin/java -DPROVIDER=HotSpot -classpath ${JMXDUMP_HOME}/jmxdump-0.0.1-SNAPSHOT.jar:${JAVA_HOME}/lib/tools.jar com.jlp.jmxscan.AttachJMX $*"
		fi
	else
		echo "No chance to attach jmxscan with user=$currentUser"
		echo "launch this script with the JVM user or at least with root user"
		PATH=$OLD_PATH
		export PATH
		exit 1
	fi
else
	echo "All is right!  the current user $currentUser is equal to the javaUser $javaUser"



	
	# Treat case of IBM JDK
	if [[ $PROVIDER == IBM ]]; then
		${JAVA_HOME}/bin/java -DPROVIDER=IBM -classpath ${JMXDUMP_HOME}/jmxdump-0.0.1-SNAPSHOT.jar:${JAVA_HOME}/lib/tools.jar com.jlp.jmxscan.AttachJMX $*
	else
		${JAVA_HOME}/bin/java -DPROVIDER=HotSpot -classpath ${JMXDUMP_HOME}/jmxdump-0.0.1-SNAPSHOT.jar:${JAVA_HOME}/lib/tools.jar com.jlp.jmxscan.AttachJMX $*
	fi
fi
PATH=$OLD_PATH
export PATH	
