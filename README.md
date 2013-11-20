<HTML>
<HEAD>
	<META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=utf-8">
	<TITLE></TITLE>
	<META NAME="GENERATOR" CONTENT="LibreOffice 3.6  (Linux)">
	<META NAME="CREATED" CONTENT="0;0">
	<META NAME="CHANGEDBY" CONTENT="JLP ">
	<META NAME="CHANGED" CONTENT="20131120;18442700">
	<META NAME="CHANGEDBY" CONTENT="JLP ">
	<META NAME="CHANGEDBY" CONTENT="JLP ">
	<META NAME="CHANGEDBY" CONTENT="JLP ">
	<META NAME="CHANGEDBY" CONTENT="JLP ">
</HEAD>
<BODY LANG="fr-FR" DIR="LTR">
<OL>
	<LI><H1>Description 
	</H1>
</OL>
<P STYLE="margin-bottom: 0cm"><SPAN LANG="fr-FR"><B>jmxdump</B></SPAN>
<SPAN LANG="fr-FR">is a tool that scans the exposed Mbeans on a local
JVM using the Attach API. Only tested in a Linux OS, but the script
can</SPAN> <SPAN LANG="fr-FR">be adpated for Windows, OSX and other
*nix.</SPAN></P>
<P STYLE="margin-bottom: 0cm"><SPAN LANG="fr-FR">The attach API is
contained in the jar</SPAN> <SPAN LANG="fr-FR"><B>tools.jar</B></SPAN><SPAN LANG="fr-FR">,
</SPAN><FONT COLOR="#ff420e"><SPAN LANG="fr-FR"><B>so a JDK is
mandatory on the server</B></SPAN></FONT><SPAN LANG="fr-FR"><B>.</B></SPAN></P>
<P STYLE="margin-bottom: 0cm"><FONT COLOR="#ff420e"><B>jmxdump
doesn't run with a JRE.</B></FONT></P>
<P STYLE="margin-bottom: 0cm"><SPAN LANG="fr-FR">The file </SPAN><SPAN LANG="fr-FR"><B>jmxdump.sh
</B></SPAN><SPAN LANG="fr-FR"><SPAN STYLE="font-weight: normal">must
be excutable and must be launched with the same unix user of the JVM,
or with root user ( a «&nbsp;su&nbsp;» command is done in the
script).</SPAN></SPAN></P>
<P STYLE="margin-bottom: 0cm"><BR>
</P>
<P STYLE="margin-bottom: 0cm">The covered JDKs are JDK 1.6+
«&nbsp;HotSpot&nbsp;» ( Oracle / Open JDK) and IBM JDK J9 1.6+.</P>
<P STYLE="margin-bottom: 0cm"><BR>
</P>
<P STYLE="margin-bottom: 0cm; font-weight: normal">The usage of this
script is described below&nbsp;:</P>
<P STYLE="margin-bottom: 0cm; background: #ccffff; font-weight: normal">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>echo &quot;Usage (if it is
possible execute with root user, at least with the same user of the
JVM you want to scan) :&quot; </FONT></FONT>
</P>
<P STYLE="margin-bottom: 0cm; background: #ccffff; font-weight: normal">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>echo &quot;./jmxdump.sh
-h|-help\nto obtain this help\n\n&quot; </FONT></FONT>
</P>
<P STYLE="margin-bottom: 0cm; background: #ccffff; font-weight: normal">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>echo &quot; ./jmxdump.sh
-pid=&lt;PID first argument JVM mandatory&gt; -attr|-obj(opt)
-model=&lt;model(opt,default *:*)&gt; -excl=&lt;excl(opt)&gt; &quot; </FONT></FONT>
</P>
<P STYLE="margin-bottom: 0cm; background: #ccffff; font-weight: normal">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>echo &quot;-attr is the
default =&gt; gives the values of attributes,&quot; </FONT></FONT>
</P>
<P STYLE="margin-bottom: 0cm; background: #ccffff; font-weight: normal">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>echo &quot;-obj =&gt;
gives the types of attributes,&quot; </FONT></FONT>
</P>
<P STYLE="margin-bottom: 0cm; background: #ccffff; font-weight: normal">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>echo &quot;-model must be
an ObjectName regex , excl =&gt; for excluding Mbean is a normal
regex see java.util.Pattern&quot; </FONT></FONT>
</P>
<P STYLE="margin-bottom: 0cm; background: #ccffff; font-weight: normal">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>echo &quot; when adding
-freq=&lt;timeInSecond&gt; and -boucles=&lt;number&gt; :&quot; </FONT></FONT>
</P>
<P STYLE="margin-bottom: 0cm; background: #ccffff; font-weight: normal">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>echo &quot;The script is
excecuted &lt;boucles&gt; times spaced by &lt;freq&gt; seconds.&quot;
</FONT></FONT>
</P>
<P STYLE="margin-left: 1.27cm; margin-top: 0.18cm; margin-bottom: 0cm">
<BR>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm">The output is the
stdout, it can be redirected to a file.</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm">Simple output with
2 boucles is given below&nbsp;:</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2>./jmxdump.sh -pid=19632
-model=&quot;jboss.as:deployment=jdbc_pool_basic.war,subsystem=web,servlet=PutData&quot;
-boucles=2 -frequency=2 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm">and the output&nbsp;:</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">Date
= 2013/11/20:18:23:00.140 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">########################################################
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">PID
JVM =19632 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">########################################################
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">-----------------------------------------------------------
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">objectName=jboss.as:deployment=jdbc_pool_basic.war,subsystem=web,servlet=PutData
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">-----------------------------------------------------------
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">minTime
= 1 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">loadTime
= 1 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">processingTime
= 505 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">maxTime
= 202 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">requestCount
= 2 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">-----------------------------------------------------------
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">########################################################
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">End
JMXDump = 2013/11/20:18:23:00.181 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">Date
= 2013/11/20:18:23:02.187 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">########################################################
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">PID
JVM =19632 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">########################################################
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">-----------------------------------------------------------
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">objectName=jboss.as:deployment=jdbc_pool_basic.war,subsystem=web,servlet=PutData
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">-----------------------------------------------------------
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">minTime
= 1 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">loadTime
= 1 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">processingTime
= 505 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">maxTime
= 202 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">requestCount
= 2 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">-----------------------------------------------------------
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">########################################################
</FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm; background: #ccffff">
<FONT FACE="Courier 10 Pitch"><FONT SIZE=2 STYLE="font-size: 9pt">End
JMXDump = 2013/11/20:18:23:02.209 </FONT></FONT>
</P>
<P STYLE="margin-top: 0.18cm; margin-bottom: 0cm"><BR>
</P>
<P STYLE="margin-left: 1.27cm; margin-top: 0.18cm; margin-bottom: 0cm">
<BR>
</P>
<P STYLE="margin-left: 1.27cm; margin-top: 0.18cm; margin-bottom: 0cm">
<BR>
</P>
<P STYLE="margin-bottom: 0cm"><BR>
</P>
<OL START=2>
	<LI><H1>Build of jmxdump</H1>
</OL>
<P><SPAN LANG="fr-FR">The build is realized with Maven plugin to
Eclipse (m2m) downloaded from <A HREF="http://download.eclipse.org/technology/m2e/releases">this
update site</A>.</SPAN></P>
<OL START=3>
	<LI><H1><SPAN LANG="fr-FR">License</SPAN></H1>
</OL>
<P><SPAN LANG="fr-FR">The license is jmxdump is Apache 2.0 =&gt; 
<A HREF="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</A></SPAN></P>
<P><BR><BR>
</P>
<P><BR><BR>
</P>
<P STYLE="margin-bottom: 0cm">&nbsp;</P>
<P STYLE="margin-bottom: 0cm">&nbsp;</P>
<P><BR><BR>
</P>
</BODY>
</HTML>
