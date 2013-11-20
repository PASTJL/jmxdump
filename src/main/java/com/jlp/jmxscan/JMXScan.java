
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import javax.management.remote.JMXConnector;

public  class JMXScan {
	
	private String model;
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getExcl() {
		return excl;
	}

	public void setExcl(String excl) {
		this.excl = excl;
	}

	public JMXConnector getJmxc() {
		return jmxc;
	}

	public void setJmxc(JMXConnector jmxc) {
		this.jmxc = jmxc;
	}

	
	private List<ObjectName> objectNames=new ArrayList<ObjectName>();
	private String excl;
	private JMXConnector jmxc;
	
	MBeanServerConnection con ;
	public JMXScan( String model,  String excl,JMXConnector jmxc){
		this.model=model;
		this.excl=excl;
		this.jmxc=jmxc;
		
	

		 try {
			con = jmxc.getMBeanServerConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    if (excl.length() == 0) {
      if (model.length() == 0) {
    	  try {
    		  Set<ObjectName> tmp=con.queryNames(new ObjectName("*:*"), null);
    		  for(ObjectName item: tmp){
  				objectNames.add(item);
  			}
			//objectNames= (List<ObjectName>) Arrays.asList((ObjectName[]) con.queryNames(new ObjectName("*:*"), null).toArray());
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      } else {
    	  try {
    		  Set<ObjectName> tmp=con.queryNames(new ObjectName(model), null);
    		  for(ObjectName item: tmp){
  				objectNames.add(item);
  			}
		//	objectNames= (List<ObjectName>) Arrays.asList((ObjectName[]) con.queryNames(new ObjectName(model), null).toArray());
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    } else {
      if (model.length() == 0) {
    	 List<ObjectName> tmp=new ArrayList<ObjectName>();
		try {
			  Set<ObjectName> tmpSet=con.queryNames(new ObjectName("*:*"), null);
    		  for(ObjectName item: tmpSet){
  				tmp.add(item);
  			}
		//	tmp = ((List<ObjectName>) Arrays.asList((ObjectName[])con.queryNames(new ObjectName("*:*"), null).toArray()));
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 objectNames=new ArrayList<ObjectName>();
    		Pattern pat=Pattern.compile(excl);
    	 for(ObjectName name : tmp)
    			{
    		 	Matcher match=pat.matcher(name.toString());
    			if(!match.find()){
    				objectNames.add(name);
    			}
    			
    			}
    			  
    			 
      } else {
    	  List<ObjectName> tmp=new ArrayList<ObjectName>();
		try {
			Set<ObjectName> tmpSet=con.queryNames(new ObjectName(model), null);
			for(ObjectName item: tmpSet){
				tmp.add(item);
			}
		//	tmp = ((List<ObjectName>) Arrays.asList((ObjectName[])(con.queryNames(new ObjectName(model), null).toArray())));
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  objectNames=new ArrayList<ObjectName>();
  		Pattern pat=Pattern.compile(excl);
  	 for(ObjectName name : tmp)
  			{
  		 	Matcher match=pat.matcher(name.toString());
  			if(!match.find()){
  				objectNames.add(name);
  			}
  			
  			}
      }
    }
  
	}

 public void printObjectNames() {
	 for(ObjectName objName : objectNames){
     System.out. println("-----------------------------------------------------------");
      System.out. println("objectName=" + objName.toString());
      System.out. println("-----------------------------------------------------------");

      try {
        MBeanAttributeInfo[] attributesInfo = con.getMBeanInfo(objName).getAttributes();
        int ln=attributesInfo.length;
        String[] attributesNames = new String[ln];
        for(int i=0;i<ln;i++){
        	 attributesNames[i]=attributesInfo[i].getName();
        }
        HashMap<String,String> attributesNamesType = new HashMap<String,String>();
        for(int i=0;i<ln;i++){
        	attributesNamesType.put(attributesInfo[i].getName(), attributesInfo[i].getType());
       }
       
          AttributeList attributeList = new AttributeList();
         
        for (String attrN :  attributesNames ) {
          try {
        	  AttributeList attrList=con.getAttributes(objName, new String[]{attrN});
        	  
            
            if (null != attrList && attrList.size() > 0) {
              // println("ajout de : "+attrTmp.toString)
              attributeList .addAll(attrList);
            }
          } catch (Throwable th){
            
          }

        }
        //      for (attr <- attributesNamesType) {
        //
        //        val toAffich: String = myTypeToString(attr, attributesNamesType.get(attr.getName()).get)
        //        println(attr._1 + " => " + attr._2)
        //      }
        //      println("###########################################################")
        //      println

        for (Attribute attr : attributeList.asList()) {

          String toAffich = myTypeToString(attr, attributesNamesType.get(attr.getName()));
          // println(attr._1 + " => " + attr._2)
         System.out. println(toAffich);
        }
      } catch ( Throwable th) {
       
      }
      System.out. println("-----------------------------------------------------------");
     System.out. println();

    }
  }

  private String myTypeToString( javax.management.Attribute attr,  String typeArr)
    {
      if (typeArr .equals ("javax.management.openmbean.CompositeData"))
      
      {
                   if (null != attr.getValue()) {
             return " " + attr.getName() + " => javax.management.openmbean.CompositeData => {" + compositeDataTypeToString((javax.management.openmbean.CompositeDataSupport)attr.getValue()) + "}";
            } else {
            	 return " " + attr.getName() + " => javax.management.openmbean.CompositeData => {}";
            }
          }
      else if (typeArr.equals("javax.management.openmbean.CompositeDataSupport"))
          {
            if (null != attr.getValue()) {
             return  " " + attr.getName() + " => javax.management.openmbean.CompositeDataSupport => {" + compositeDataTypeToString((javax.management.openmbean.CompositeDataSupport) attr.getValue()) + "}";
            } else {
            return  " " + attr.getName() + " => " + "javax.management.openmbean.CompositeDataSupport => {}";
            }
          }
      else if (typeArr.equals("[Ljavax.management.openmbean.CompositeDataSupport;" ))
          {
    	  List<javax.management.openmbean.CompositeDataSupport> lst  = Arrays.asList((javax.management.openmbean.CompositeDataSupport[]) attr.getValue());
           String ret = "";
            for(CompositeDataSupport item : lst) {
            
                ret += " " + attr.getName() + " => " + compositeDataTypeToString(item);
            }
           return ret;
          }
      else if (typeArr.equals("[Ljavax.management.openmbean.CompositeData;")) {
          List<javax.management.openmbean.CompositeData> lst = Arrays.asList((javax.management.openmbean.CompositeData[]) attr.getValue());
          String ret = "";
          for (CompositeData item : lst) {
           
              ret += " " + attr.getName ()+ " => " + compositeDataTypeToString(item);
          }
          return ret;
        }
      else if (typeArr.equals("javax.management.openmbean.TabularData"))
          {
            if (null != attr.getValue()) {
              return " " + attr.getName() + " => javax.management.openmbean.TabularData => {" + tabularDataTypeToString((javax.management.openmbean.TabularDataSupport) attr.getValue()) + "}";
            } else {
             return  " " + attr.getName() + " => " + "javax.management.openmbean.TabularData => {}";
            }

          }
      else if (typeArr.equals("javax.management.openmbean.TabularDataSupport"))
          {
            if (null != attr.getValue()) {
             return  " " + attr.getName() + " => javax.management.openmbean.TabularDataSupport => {" + tabularDataTypeToString((javax.management.openmbean.TabularDataSupport) attr.getValue()) + "}";
            } else {
             return  " " + attr.getName() + " => " + "javax.management.openmbean.TabularDataSupport => {}";
            }

          }
      else if (typeArr.equals("[Ljavax.management.openmbean.TabularDataSupport;"))
          {
            List<javax.management.openmbean.TabularDataSupport> lst= Arrays.asList((javax.management.openmbean.TabularDataSupport[]) attr.getValue());
            String ret = "";
            for(TabularDataSupport item : lst) {
            
                ret += " " + attr.getName() + " => " + tabularDataTypeToString(item);
            }
           return  ret;
          }
      else if (typeArr.equals( "[Ljavax.management.openmbean.TabularData;" )) {
          List<javax.management.openmbean.TabularData> lst = Arrays.asList((javax.management.openmbean.TabularData[]) attr.getValue());
          String ret = "";
           for(TabularData item : lst ) {
           
              ret += " " + attr.getName() + " => " + tabularDataTypeToString(item);
          }
         return ret;
        }

      else return" " + attr.getName() + " => " + typeArr;
      }
    
  private String compositeDataTypeToString(CompositeData cd)
    {
     CompositeType cdTyp = cd.getCompositeType();
      Set<String> keys = cdTyp.keySet();
      String ret = "";
      if (keys.size() > 0) {
        for (String key : keys) {
          Object value = cd.get(key);

          OpenType<?> typ = cdTyp.getType(key);
          String clazz = typ.getClassName();

          if (null != value) {

            // Gestion des Types
            if(clazz.equals( "javax.management.openmbean.TabularDataSupport"))
                ret += " " + key + " => " + "javax.management.openmbean.TabularDataSupport => {" +
                  tabularDataTypeToString((javax.management.openmbean.TabularDataSupport)value) + "}";
            else if (clazz.equals( "javax.management.openmbean.TabularData" ))
                ret += " " + key + " => " + "javax.management.openmbean.TabularData => {" +
                  tabularDataTypeToString((javax.management.openmbean.TabularDataSupport) value) + "}";
            else if (clazz.equals("javax.management.openmbean.CompositeData" ))
                ret += " " + key + " => " + "javax.management.openmbean.CompositeData => {" +
                  compositeDataTypeToString((javax.management.openmbean.CompositeData) value) + "}";
            else if (clazz.equals("javax.management.openmbean.CompositeDataSupport" ))
                ret += " " + key + " => " + "javax.management.openmbean.CompositeDataSupport => {" +
                  compositeDataTypeToString((javax.management.openmbean.CompositeData) value) + "}";
            else
                {
                  if (value instanceof java.util.Map) {
                    ret += " " + key + " => " + "java.util.Map";
                  } else if (value instanceof Object[]) {
                    ret += " " + key + " => " + "Array";
                  } else

                    ret += " " + key + " => " + clazz;
                }
            

          } 
        }
        
        }else {
            ret += " => javax.management.openmbean.CompositeData => {}";
          }
       
      return ret;
      }
    
    
 private String tabularDataTypeToString( TabularData td)
    {

      TabularType tdTyp = td.getTabularType();
      CompositeType rowTyp = tdTyp.getRowType();

      String ret = "";
      Set<Map.Entry<Object,Object>> entries = ((TabularDataSupport) td).entrySet();
      if (entries.size() > 0) {
        for (Map.Entry<Object,Object> entry : entries) {
          Object key = entry.getKey();
          Object value = entry.getValue();

          if (null != value) {
            if (value instanceof CompositeData || value instanceof CompositeDataSupport) {
              ret += " " + key + " => javax.management.openmbean.CompositeData => {" + compositeDataTypeToString((CompositeDataSupport)value) + "}";
            } else if (value instanceof TabularDataSupport || value instanceof TabularData) {
              ret += " " + key + " => javax.management.openmbean.TabularData => {" + tabularDataTypeToString((TabularDataSupport)value) + "}";
            } else if (value instanceof java.util.Map) {
              ret += " " + key + " => java.util.Map ";
            } else if (value instanceof Object[]) {
              ret += " => Array";
            } else {

              ret += " " + key + " => " + value.getClass().getName();
            }
          } else {
            ret += " " + key + " => javax.management.openmbean.TabularData => {}";
          }
        }
      } else {
        ret += " => javax.management.openmbean.TabularData => {}";
      }

     return ret;

    }

 public void  printAttributes() {
	  for(ObjectName objName :objectNames )
    { 
     System.out. println("-----------------------------------------------------------");
     System.out. println("objectName=" + objName.toString());
     System.out. println("-----------------------------------------------------------");

      try {
       MBeanAttributeInfo[] attributesInfo = con.getMBeanInfo(objName).getAttributes();
       int ln=attributesInfo.length;
       String[] attributesNames = new String[ln];
       for(int i=0;i<ln;i++){
       	 attributesNames[i]=attributesInfo[i].getName();
       }
       HashMap<String,String> attributesNamesType = new HashMap<String,String>();
       for(int i=0;i<ln;i++){
       	attributesNamesType.put(attributesInfo[i].getName(), attributesInfo[i].getType());
      }
        
        
       
       AttributeList attributeList = new AttributeList();
       
       for (String attrN :  attributesNames ) {
         try {
       	  AttributeList attrList=con.getAttributes(objName, new String[]{attrN});
       	  
           
           if (null != attrList && attrList.size() > 0) {
             // println("ajout de : "+attrTmp.toString)
             attributeList .addAll(attrList);
           }
         } catch (Throwable th){
           
         }

        }

        for (Attribute attr : attributeList.asList()) {

          if (null == attr.getValue()) {
           System.out. println(attr.getName() + " = ");
          } else {
            // traitement des cas particulier
            // println("attr.getName()=" + attr.getName())
            //println("typeArr=" + attributesNamesType.get(attr.getName()).get)

            String toAffich  =myToString(attr, attributesNamesType.get(attr.getName()));
            System.out.println(attr.getName() + " = " + toAffich);
          }

        }

      } catch (Throwable th){
      
      }
      System.out.println("-----------------------------------------------------------");
      System.out.println();
    }
  }

private String  myToString(javax.management.Attribute attr,  String typeArr)
    {
      if (typeArr.trim().equals("int")) return ((Integer) attr.getValue()).toString();
      else if (typeArr.trim().equals( "long"))return ((Long)attr.getValue()).toString();
      else if (typeArr.trim().equals("double"))return ((Double)attr.getValue()).toString();
      else if (typeArr.trim().equals( "float")) return ((Float) attr.getValue()).toString();
      else if (typeArr.trim().equals( "boolean")) return ((Boolean) attr.getValue()).toString();
      else if (typeArr.trim().equals(  "short")) return ((Short) attr.getValue()).toString();
      else if (typeArr.trim().equals( "character"))return ((Character) attr.getValue()).toString();

      else if (typeArr.trim().equals( "[Ljava.lang.Long;" ) ||typeArr.trim().equals( "[Ljava.lang.Short;" ) || typeArr.trim().equals("[Ljava.lang.Integer;") ||
    		  typeArr.trim().equals( "[Ljava.lang.Double;") || typeArr.trim().equals("[Ljava.lang.Float;")) {

          String ret = "";
          try {

            List<Object> lst = Arrays.asList((Object[]) attr.getValue());

            for ( Object item: lst) {
            
                ret += item + " | ";
            }
          } catch (Throwable th){
           ret = attr.getValue().toString();
          }
          return ret;
        }
      else if (typeArr.trim().equals("[Ljava.lang.String;")) {

    	  String ret = "";
          try {

           List<String> lst = Arrays.asList((String[]) attr.getValue());

           for ( String item: lst) {
               
               ret += item + " | ";
           }
          } catch (Throwable th){
              ret = attr.getValue().toString();
             }
             return ret;
        }
      else if (typeArr.trim().equals("[Ljavax.management.ObjectName;" )){

         String ret = "";
          try {

            List<ObjectName> lst= Arrays.asList((ObjectName[]) attr.getValue());

            for ( ObjectName item: lst) {
                
                ret += item + " | ";
            }
           } catch (Throwable th){
               ret = attr.getValue().toString();
              }
              return ret;
        }

      else if (typeArr.trim().equals("javax.management.openmbean.CompositeData"))
          {
           return  compositeDataToString(attr.getName(), (javax.management.openmbean.CompositeData) attr.getValue());
          }
      else if (typeArr.trim().equals("javax.management.openmbean.CompositeDataSupport" ))
          {
            return compositeDataToString(attr.getName(), (javax.management.openmbean.CompositeDataSupport) attr.getValue());
          }
      else if (typeArr.trim().equals("[Ljavax.management.openmbean.CompositeDataSupport;" ))
          {
            List<javax.management.openmbean.CompositeDataSupport>  lst= Arrays.asList((javax.management.openmbean.CompositeDataSupport[]) attr.getValue());
            String ret = "";
            for ( CompositeDataSupport item : lst) {
              
                ret += compositeDataToString(attr.getName(), item);
            }
            return ret;
          }
      else if (typeArr.trim().equals("[Ljavax.management.openmbean.CompositeData;" ))
          {
            List<javax.management.openmbean.CompositeData> lst = Arrays.asList( ( javax.management.openmbean.CompositeData[]) attr.getValue());
            String ret = "";
            for (CompositeData item : lst) {
                        ret += compositeDataToString(attr.getName(), item);
            }
            return ret;
          }

      else if (typeArr.trim().equals("[Ljavax.management.openmbean.TabularData;" ))
          {
            List<javax.management.openmbean.TabularData> lst =  Arrays.asList( (javax.management.openmbean.TabularData[]) attr.getValue());
            String ret = "";
            for ( TabularData item : lst) {
                ret += tabularDataToString(attr.getName(), item);
            }
            return ret;
          }
      else if (typeArr.trim().equals("javax.management.openmbean.TabularData"))
          {
            return tabularDataToString(attr.getName(), (javax.management.openmbean.TabularData) attr.getValue());

          }
      else if (typeArr.trim().equals("[Ljavax.management.openmbean.TabularDataSupport;"))
          {
            List<javax.management.openmbean.TabularDataSupport> lst = Arrays.asList((javax.management.openmbean.TabularDataSupport[]) attr.getValue());
            String ret = "";
            for (TabularDataSupport item : lst) {
           
                ret += tabularDataToString(attr.getName(), item);
            }
            return ret;
          }
      else if (typeArr.trim().equals("javax.management.openmbean.TabularDataSupport" ))
          {
          return   tabularDataToString(attr.getName(), (javax.management.openmbean.TabularDataSupport) attr.getValue());

          }
      else if (typeArr.trim().equals("java.util.Properties" )){
          Set<Map.Entry<Object,Object>> en = ((java.util.Properties) attr.getValue()).entrySet(); 
        		 ;
          String ret = "";
          for (Map.Entry<Object,Object> entry : en) {

            ret += " " + attr.getName() + "." + entry.getKey().toString() + " = " + entry.getValue().toString();
          }
         return   ret;
        }

      else{
          return attr.getValue().toString();
      }
     
      }

   

 private String tabularDataToString(String name , javax.management.openmbean.TabularData td)
    {

      TabularType tdTyp = td.getTabularType();
      CompositeType rowTyp = tdTyp.getRowType();

    String  ret = "";
      Set<Entry<Object,Object>> entries = ((TabularDataSupport) td).entrySet();
      for (Entry entry : entries) {
        Object key = entry.getKey();
        Object value = entry.getValue();

        if (null != value) {
          if (value instanceof CompositeData || value instanceof CompositeDataSupport ) {
            ret += compositeDataToString(name, (CompositeData) value);
          } else if (value instanceof TabularDataSupport || value instanceof TabularData ) {
            ret += tabularDataToString(name, (TabularDataSupport) value);
          } else if (value instanceof java.util.Map) {
            ret += name + "." + mapToString((java.util.Map) value);
          } else if (value instanceof Object[]) {
            ret += name + "." + arrayToString((Object[])value);
          } else {

            ret += "|" + name + "." + key + "=" + value.toString();
          }
        } else {

        }
      }

    return ret;

    }
 
  private String  arrayToString( Object[] tab) {
    String ret = "";
    for (Object elem : tab) {
      ret += "|" + elem.toString();
    }
    return ret;
  }

 private String mapToString( java.util.Map<String, Object> map)
    {
     String ret = "";

     
      Set<Map.Entry<String,Object>> entries =   map.entrySet();
        for (Entry entry : entries) {

          ret += "|" + entry.getKey() + "=" + entry.getValue() + "\n";
        }
    

      return ret;
    }
 
 private String compositeDataToString( String name,  CompositeData cd)
    {

    CompositeType cdTyp = cd.getCompositeType();
      Set<String> keys = cdTyp.keySet();
      String  ret = "";
      for (String key : keys) {
        Object value = cd.get(key);

        OpenType<?> typ = cdTyp.getType(key);
        String clazz = typ.getClassName();

        if (null != value) {
          // Gestion des Types
          if ( clazz.equals("javax.management.openmbean.TabularDataSupport"))
              ret += tabularDataToString(name, (javax.management.openmbean.TabularDataSupport) value);
          else if (clazz.equals("javax.management.openmbean.TabularData" ))
              ret += tabularDataToString(name, (javax.management.openmbean.TabularDataSupport) value);
              else if (clazz.equals("javax.management.openmbean.CompositeData" ))
              ret += compositeDataToString(name, (javax.management.openmbean.CompositeData ) value);
              else if (clazz.equals("javax.management.openmbean.CompositeDataSupport" ))
              ret += compositeDataToString(name,(javax.management.openmbean.CompositeData) value);
              else
              {
                if (value instanceof java.util.Map<?,?>) {
                  ret += name + "." + mapToString((java.util.Map<String,Object>) value);
                } else if (value instanceof Object[]) {
                  ret += name + "." + arrayToString( (Object[]) value);
                } else

                  ret += "|" + name + "." + key + "=" + value.toString();
              }
          }

        
      }

      return ret;
    }
 
 public static void launch(String[]  args){
	 
	
		 
	 
	 
 }
}

